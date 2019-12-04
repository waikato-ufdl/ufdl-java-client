/*
 * Server.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.context;

import com.github.fracpete.requests4j.Session;
import com.github.waikatoufdl.ufdl4j.core.AbstractLoggingObject;

/**
 * The server context.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Server
  extends AbstractLoggingObject {

  private static final long serialVersionUID = -3984451065997561103L;

  public static final String DEFAULT_HOST = "http://localhost:8000";

  /** the host to connect to. */
  protected String m_URL;

  /** the session. */
  protected Session m_Session;

  /**
   * Initializes the server.
   */
  public Server() {
    this(DEFAULT_HOST);
  }

  /**
   * Initializes the server.
   *
   * @param value	the URL of the server
   */
  public Server(String value) {
    if (value.endsWith("/"))
      value = value.substring(0, value.length() - 1);
    m_URL = value;
  }

  /**
   * Returns the URL.
   *
   * @return		the URL
   */
  public String getURL() {
    return m_URL;
  }

  /**
   * Combines the URL with the path.
   *
   * @param path	the path to use
   * @return		the full URL
   */
  public String build(String path) {
    if (!path.startsWith("/"))
      path = "/" + path;
    return getURL() + path;
  }

  /**
   * Sets the session.
   *
   * @param value	the session
   */
  public void setSession(Session value) {
    m_Session = value;
  }

  /**
   * Returns the session.
   *
   * @return		the session, null if none set
   */
  public Session getSession() {
    return m_Session;
  }

  /**
   * Returns a short description of the state.
   *
   * @return		the description
   */
  public String toString() {
    return "url=" + getURL() + ", session=" + getSession();
  }
}
