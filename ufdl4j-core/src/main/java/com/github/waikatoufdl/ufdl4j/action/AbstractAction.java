/*
 * AbstractAction.java
 * Copyright (C) 2019-2023 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.fracpete.requests4j.response.FileResponse;
import com.github.fracpete.requests4j.response.JsonResponse;
import com.github.fracpete.requests4j.response.StreamResponse;
import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.context.Connection;
import com.github.waikatoufdl.ufdl4j.core.AbstractLoggingObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Map;

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

  /** the client. */
  protected Client m_Client;

  /**
   * Initializes the action.
   */
  protected AbstractAction() {
    super();
    m_Connection = null;
    m_Client     = null;
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
   * Sets the client this action is used by.
   *
   *  @param value	the client
   */
  public void setClient(Client value) {
    m_Client = value;
  }

  /**
   * Returns the client this is action is used by.
   *
   * @return		the client
   */
  public Client getClient() {
    return m_Client;
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
   * Creates a new GET request.
   *
   * @param path	the path to use (server gets automatically added)
   * @param params	the query parameters to add
   * @return		the GET request
   * @throws MalformedURLException	if invalid URL
   */
  protected Request newGet(String path, Map<String,String> params) throws MalformedURLException {
    String	url;

    url = m_Connection.server().build(path, params);
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
   * @return		the response
   * @throws Exception	if an error occurred
   */
  protected JsonResponse execute(Request request) throws Exception {
    JsonResponse 	result;

    preExecute(request);
    result = request.execute(new JsonResponse());

    // expired access token?
    if (result.statusCode() == 401) {
      m_Connection.authentication().refresh();
      preExecute(request);
      result = request.execute(new JsonResponse());
    }

    // expired refresh token?
    if (result.statusCode() == 401) {
      m_Connection.authentication().obtain();
      preExecute(request);
      result = request.execute(new JsonResponse());
    }

    return result;
  }

  /**
   * Executes the request, downloading a file. Automatically fills in authentication.
   *
   * @param request	the request to execute
   * @param output 	the file to write to
   * @return		the response
   * @throws Exception	if an error occurred
   */
  protected FileResponse download(Request request, File output) throws Exception {
    return download(request, output, true);
  }

  /**
   * Executes the request, downloading a file. Automatically fills in authentication.
   *
   * @param request	the request to execute
   * @param output 	the file to write to
   * @param keepEmpty 	whether to keep empty files or remove them automatically
   * @return		the response
   * @throws Exception	if an error occurred
   */
  protected FileResponse download(Request request, File output, boolean keepEmpty) throws Exception {
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

    if (!keepEmpty && output.exists() && (output.length() == 0)) {
      if (!output.delete())
	throw new IOException("Failed to remove empty file: " + output);
    }

    return result;
  }

  /**
   * Executes the request, downloads the file as stream. Automatically fills in authentication.
   *
   * @param request	the request to execute
   * @param stream 	the output stream to use (caller needs to close it)
   * @return		the response
   * @throws Exception	if an error occurred
   */
  protected StreamResponse stream(Request request, OutputStream stream) throws Exception {
    StreamResponse 	result;

    preExecute(request);

    result = request.execute(new StreamResponse(stream));

    // expired access token?
    if (result.statusCode() == 401) {
      m_Connection.authentication().refresh();
      result = request.execute(new StreamResponse(stream));
    }

    // expired refresh token?
    if (result.statusCode() == 401) {
      m_Connection.authentication().obtain();
      result = request.execute(new StreamResponse(stream));
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
