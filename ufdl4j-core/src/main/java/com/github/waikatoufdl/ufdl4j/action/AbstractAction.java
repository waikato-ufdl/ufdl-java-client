/*
 * AbstractAction.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.fracpete.requests4j.response.FileResponse;
import com.github.waikatoufdl.ufdl4j.context.Connection;
import com.github.waikatoufdl.ufdl4j.core.AbstractLoggingObject;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;

import java.io.File;
import java.net.MalformedURLException;

/**
 * Ancestor for actions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractAction
  extends AbstractLoggingObject {

  private static final long serialVersionUID = -5445418236260163963L;

  public static final String HEADER_AUTHORIZATION = "Authorization";

  public static final String PREFIX_BEARER = "Bearer";

  /** the connection. */
  protected Connection m_Connection;

  /**
   * Initializes the action.
   */
  protected AbstractAction() {
    super();
    m_Connection = null;
  }

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  public abstract String getName();

  /**
   * The URL path to use.
   *
   * @return		the path
   */
  public abstract String getPath();

  /**
   * Sets the connection to use.
   *
   * @param value	the connection
   */
  public void setConnection(Connection value) {
    m_Connection = value;
  }

  /**
   * Returns the connection in use.
   *
   * @return		the connection, null if none set
   */
  public Connection getConnection() {
    return m_Connection;
  }

  /**
   * Creates a new GET request.
   *
   * @param path	the path to use (server gets automatically added)
   * @return		the GET request
   * @throws MalformedURLException	if invalid URL
   */
  protected Request newGet(String path) throws MalformedURLException {
    String	url;

    url = m_Connection.server().build(path);
    return m_Connection.session().get(url).allowRedirects(true);
  }

  /**
   * Creates a new POST request.
   *
   * @param path	the path to use (server gets automatically added)
   * @return		the POST request
   * @throws MalformedURLException	if invalid URL
   */
  protected Request newPost(String path) throws MalformedURLException {
    String	url;

    url = m_Connection.server().build(path);
    return m_Connection.session().post(url).allowRedirects(true);
  }

  /**
   * Creates a new PUT request.
   *
   * @param path	the path to use (server gets automatically added)
   * @return		the PUT request
   * @throws MalformedURLException	if invalid URL
   */
  protected Request newPut(String path) throws MalformedURLException {
    String	url;

    url = m_Connection.server().build(path);
    return m_Connection.session().put(url).allowRedirects(true);
  }

  /**
   * Creates a new PATCH request.
   *
   * @param path	the path to use (server gets automatically added)
   * @return		the PATCH request
   * @throws MalformedURLException	if invalid URL
   */
  protected Request newPatch(String path) throws MalformedURLException {
    String	url;

    url = m_Connection.server().build(path);
    return m_Connection.session().patch(url).allowRedirects(true);
  }

  /**
   * Creates a new HEAD request.
   *
   * @param path	the path to use (server gets automatically added)
   * @return		the HEAD request
   * @throws MalformedURLException	if invalid URL
   */
  protected Request newHead(String path) throws MalformedURLException {
    String	url;

    url = m_Connection.server().build(path);
    return m_Connection.session().head(url).allowRedirects(true);
  }

  /**
   * Creates a new OPTIONS request.
   *
   * @param path	the path to use (server gets automatically added)
   * @return		the OPTIONS request
   * @throws MalformedURLException	if invalid URL
   */
  protected Request newOptions(String path) throws MalformedURLException {
    String	url;

    url = m_Connection.server().build(path);
    return m_Connection.session().options(url).allowRedirects(true);
  }

  /**
   * Creates a new DELETE request.
   *
   * @param path	the path to use (server gets automatically added)
   * @return		the DELETE request
   * @throws MalformedURLException	if invalid URL
   */
  protected Request newDelete(String path) throws MalformedURLException {
    String	url;

    url = m_Connection.server().build(path);
    return m_Connection.session().delete(url).allowRedirects(true);
  }

  /**
   * Performs checks before executing the request and sets the authentication data.
   *
   * @param request	the request to update
   * @throws Exception	if checks fail
   */
  protected void preExecute(Request request) throws Exception {
    if (m_Connection == null)
      throw new IllegalStateException("No connection set!");
    if (!m_Connection.authentication().getTokens().isValid())
      throw new IllegalStateException("No valid authentication available!");
    request.header(HEADER_AUTHORIZATION, PREFIX_BEARER + " " + m_Connection.authentication().getTokens().getAccessToken());
  }

  /**
   * Executes the request. Automatically fills in authentication.
   *
   * @param request	the request to execute
   * @return		null if successful, otherwise error message
   */
  protected JsonResponse execute(Request request) throws Exception {
    JsonResponse 	result;

    preExecute(request);

    result = request.execute(new JsonResponse());

    // expired access token?
    if (result.statusCode() == 401) {
      m_Connection.authentication().refresh();
      result = request.execute(new JsonResponse());
    }

    // expired refresh token?
    if (result.statusCode() == 401) {
      m_Connection.authentication().obtain();
      result = request.execute(new JsonResponse());
    }

    return result;
  }

  /**
   * Executes the request, downloading a file. Automatically fills in authentication.
   *
   * @param request	the request to execute
   * @return		null if successful, otherwise error message
   */
  protected FileResponse download(Request request, File output) throws Exception {
    FileResponse 	result;

    preExecute(request);

    result = request.execute(new FileResponse(output));

    // expired access token?
    if (result.statusCode() == 401) {
      m_Connection.authentication().refresh();
      result = request.execute(new FileResponse(output));
    }

    // expired refresh token?
    if (result.statusCode() == 401) {
      m_Connection.authentication().obtain();
      result = request.execute(new FileResponse(output));
    }

    return result;
  }

  /**
   * Returns a short description of the state.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return getName();
  }
}
