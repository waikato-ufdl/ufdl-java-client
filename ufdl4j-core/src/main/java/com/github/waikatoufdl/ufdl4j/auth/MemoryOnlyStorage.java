/*
 * MemoryOnlyStorage.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.auth;

import com.github.waikatoufdl.ufdl4j.core.AbstractLoggingObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores the tokens only in memory.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class MemoryOnlyStorage
  extends AbstractLoggingObject
  implements TokenStorageHandler {

  private static final long serialVersionUID = -7096896606724147641L;

  /** the tokens. */
  protected Map<String,Tokens> m_Tokens;

  /**
   * Retrieves the tokens for the user.
   *
   * @param context	the context
   * @return		the tokens
   */
  @Override
  public synchronized Tokens load(Authentication context) {
    String	url;

    url = context.getServer().getURL();

    if (m_Tokens == null)
      m_Tokens = new HashMap<>();

    if (!m_Tokens.containsKey(url)) {
      getLogger().info("Creating empty tokens for: " + url);
      m_Tokens.put(url, new Tokens());
    }

    return m_Tokens.get(url);
  }

  /**
   * Stores the tokens on disk.
   *
   * @param context	the context
   * @param tokens	the tokens to store
   * @return		null if successfully stored, otherwise error message
   */
  @Override
  public synchronized String store(Authentication context, Tokens tokens) {
    String	url;

    url = context.getServer().getURL();

    if (m_Tokens == null)
      m_Tokens = new HashMap<>();

    getLogger().info("Storing tokens for: " + url);
    m_Tokens.put(url, tokens);

    return null;
  }

  /**
   * Returns a short description of the state.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return m_Tokens.toString();
  }
}
