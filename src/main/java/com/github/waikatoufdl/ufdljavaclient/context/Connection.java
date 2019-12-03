/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Connection.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdljavaclient.context;

import com.github.fracpete.requests4j.Session;
import com.github.waikatoufdl.ufdljavaclient.auth.Authentication;
import com.github.waikatoufdl.ufdljavaclient.core.AbstractLoggingObject;

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
    m_Session        = new Session();
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
    m_Authentication = new Authentication(user, password);
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
