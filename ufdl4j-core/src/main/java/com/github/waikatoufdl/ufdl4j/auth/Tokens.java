/*
 * Tokens.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.auth;

import com.github.waikatoufdl.ufdl4j.core.AbstractLoggingObject;

/**
 * Simple container for the tokens.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Tokens
  extends AbstractLoggingObject {

  private static final long serialVersionUID = -2685498010440079540L;

  /** the refresh token. */
  protected String m_RefreshToken;

  /** the access token. */
  protected String m_AccessToken;

  /**
   * Initializes the container with empty tokens.
   */
  public Tokens() {
    this("", "");
  }

  /**
   * Initializes the container.
   *
   * @param refresh	the refresh token
   * @param access	the access token
   */
  public Tokens(String refresh, String access) {
    m_RefreshToken = refresh;
    m_AccessToken  = access;
  }

  /**
   * Returns the refresh token.
   *
   * @return		the token
   */
  public String getRefreshToken() {
    return m_RefreshToken;
  }

  /**
   * Returns the access token.
   *
   * @return		the token
   */
  public String getAccessToken() {
    return m_AccessToken;
  }

  /**
   * Checks whether the tokens are set (ie not null and not empty).
   *
   * @return		true if valid
   */
  public boolean isValid() {
    return (m_RefreshToken != null) && !m_RefreshToken.isEmpty()
      && (m_AccessToken != null) && !m_AccessToken.isEmpty();
  }

  /**
   * Invalidates the tokens.
   */
  public void invalidate() {
    m_RefreshToken = "";
    m_AccessToken  = "";
  }

  /**
   * Short description of the state.
   *
   * @return		the state
   */
  @Override
  public String toString() {
    return "refresh=" + getRefreshToken() + ", access=" + getAccessToken();
  }
}
