/*
 * MemoryOnlyStorage.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.auth;

/**
 * Stores the tokens only in memory.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class MemoryOnlyStorage
  implements TokenStorageHandler {

  /** the tokens. */
  protected Tokens m_Tokens;

  /**
   * Retrieves the tokens for the user.
   *
   * @param context	the context
   * @return		the tokens
   */
  @Override
  public synchronized Tokens load(Authentication context) {
    if (m_Tokens == null)
      m_Tokens = new Tokens();
    return m_Tokens;
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
    m_Tokens = tokens;
    return null;
  }
}
