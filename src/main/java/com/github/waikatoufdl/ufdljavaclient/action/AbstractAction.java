/*
 * AbstractAction.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdljavaclient.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdljavaclient.Client;
import com.github.waikatoufdl.ufdljavaclient.core.AbstractLoggingObject;
import com.github.waikatoufdl.ufdljavaclient.core.JsonResponse;

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

  /** the client. */
  protected Client m_Client;

  /**
   * Initializes the action.
   */
  protected AbstractAction() {
    super();
    m_Client = null;
  }

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  public abstract String getName();

  /**
   * Sets the client to use.
   *
   * @param value	the client
   */
  public void setClient(Client value) {
    m_Client = value;
  }

  /**
   * Returns the client in use.
   *
   * @return		the client, null if none set
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

    url = m_Client.server().build(path);
    return m_Client.session().get(url);
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

    url = m_Client.server().build(path);
    return m_Client.session().post(url);
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

    url = m_Client.server().build(path);
    return m_Client.session().put(url);
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

    url = m_Client.server().build(path);
    return m_Client.session().patch(url);
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

    url = m_Client.server().build(path);
    return m_Client.session().head(url);
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

    url = m_Client.server().build(path);
    return m_Client.session().options(url);
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

    url = m_Client.server().build(path);
    return m_Client.session().delete(url);
  }

  /**
   * Executes the request. Automatically fills in authentication.
   *
   * @param request	the request to execute
   * @return		null if successful, otherwise error message
   */
  protected JsonResponse execute(Request request) throws Exception {
    JsonResponse 	result;

    if (m_Client == null)
      throw new IllegalStateException("No Client set!");
    if (!m_Client.authentication().getTokens().isValid())
      throw new IllegalStateException("No valid authentication available!");
    request.header(HEADER_AUTHORIZATION, PREFIX_BEARER + " " + m_Client.authentication().getTokens().getAccessToken());
    result = request.execute(new JsonResponse());

    // expired access token?
    if (result.statusCode() == 401) {
      m_Client.authentication().refresh();
      result = request.execute(new JsonResponse());
    }

    // expired refresh token?
    if (result.statusCode() == 401) {
      m_Client.authentication().obtain();
      result = request.execute(new JsonResponse());
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
