/*
 * Client.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdljavaclient;

import com.github.fracpete.requests4j.Session;
import com.github.waikatoufdl.ufdljavaclient.action.AbstractAction;
import com.github.waikatoufdl.ufdljavaclient.auth.Authentication;
import com.github.waikatoufdl.ufdljavaclient.context.Server;
import com.github.waikatoufdl.ufdljavaclient.core.AbstractLoggingObject;

/**
 * The client for communicating with the UFDL backend.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Client
  extends AbstractLoggingObject {

  private static final long serialVersionUID = 4630756838422216386L;

  /** the session in use. */
  protected Session m_Session;

  /** the server context. */
  protected Server m_Server;

  /** the authentication. */
  protected Authentication m_Authentication;

  /**
   * Initializes the client.
   */
  public Client() {
    m_Session = new Session();
    m_Server  = new Server("http://localhost:8000");
    m_Server.setSession(m_Session);
    m_Authentication = new Authentication("", "");
    m_Authentication.setServer(m_Server);
  }

  /**
   * Initializes the server context.
   *
   * @param url		the UFDL backend to connect to
   * @return		the client itself
   */
  public Client server(String url) {
    m_Server = new Server(url);
    m_Server.setSession(m_Session);
    m_Authentication.setServer(m_Server);
    return this;
  }

  /**
   * Returns the server context.
   *
   * @return		the context
   */
  public Server server() {
    return m_Server;
  }

  /**
   * Stores the authentication information.
   *
   * @param user	the user
   * @param password	the password
   * @return		the client itself
   */
  public Client authentication(String user, String password) {
    m_Authentication = new Authentication(user, password);
    m_Authentication.setServer(server());
    return this;
  }

  /**
   * Returns the authentication.
   *
   * @return		the authentication
   */
  public Authentication authentication() {
    return m_Authentication;
  }

  /**
   * Returns the session object.
   *
   * @return		the session
   */
  public Session session() {
    return m_Session;
  }

  /**
   * Creates a new instance of an action, sets the client (itself) and returns the instance.
   *
   * @param action	the action to instantiate
   * @param <T>		the type of action
   * @return		the action instance
   * @throws Exception	if instantiation fails
   */
  public  <T extends AbstractAction> T newAction(Class<T> action) throws Exception {
    T 	result;

    result = action.newInstance();
    result.setClient(this);

    return result;
  }

  /**
   * Closes the client.
   */
  public void close() {
    m_Server.close();
  }

  /**
   * Returns a short description of the state.
   *
   * @return		the description
   */
  public String toString() {
    return "context: " + server() + "\n"
      + "authentication: " + authentication();
  }
}
