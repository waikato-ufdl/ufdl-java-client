/*
 * Connection.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.context;

import com.github.fracpete.requests4j.Session;
import com.github.waikatoufdl.ufdl4j.auth.Authentication;
import com.github.waikatoufdl.ufdl4j.auth.LocalStorage;
import com.github.waikatoufdl.ufdl4j.auth.TokenStorageHandler;
import com.github.waikatoufdl.ufdl4j.core.AbstractLoggingObject;

/**
 * Contains the connection context.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Connection
  extends AbstractLoggingObject {

  private static final long serialVersionUID = -41024684662002189L;

  /** the session in use. */
  protected Session m_Session;

  /** the server context. */
  protected Server m_Server;

  /** the authentication. */
  protected Authentication m_Authentication;

  /**
   * Initializes the connection.
   */
  public Connection() {
    this(-1, -1, -1);
  }

  /**
   * Initializes the connection.
   *
   * @param connectTimeout  	the timeout in seconds for connecting
   * @param readTimeout 	the timeout in seconds for reading
   * @param writeTimeout 	the timeout in seconds for writing
   */
  public Connection(int connectTimeout, int readTimeout, int writeTimeout) {
    m_Session = new Session()
      .connectTimeout(connectTimeout)
      .readTimeout(readTimeout)
      .writeTimeout(writeTimeout);
    m_Server         = new Server();
    m_Authentication = new Authentication();
    server(Server.DEFAULT_HOST);
    authentication("", "");
  }

  /**
   * Initializes the server context.
   *
   * @param url		the UFDL backend to connect to
   * @return		the client itself
   */
  public Connection server(String url) {
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
  public Connection authentication(String user, String password) {
    return authentication(user, password, new LocalStorage());
  }

  /**
   * Stores the authentication information.
   *
   * @param user	the user
   * @param password	the password
   * @param storage 	the handler for storing the tokens
   * @return		the client itself
   */
  public Connection authentication(String user, String password, TokenStorageHandler storage) {
    m_Authentication = new Authentication(user, password, storage);
    m_Authentication.setServer(m_Server);
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
   * Closes the client.
   */
  public void close() {
    m_Session.close();
  }

  /**
   * Returns a short description of the state.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return m_Server + ", " + m_Authentication;
  }
}
