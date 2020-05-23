/*
 * TokenStorageHandler.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.auth;

/**
 * Interface for Token storage handlers.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface TokenStorageHandler {

  /**
   * Retrieves the tokens for the user.
   *
   * @param context	the context
   * @return		the tokens
   */
  public Tokens load(Authentication context);

  /**
   * Stores the tokens on disk.
   *
   * @param context	the context
   * @param tokens	the tokens to store
   * @return		null if successfully stored, otherwise error message
   */
  public String store(Authentication context, Tokens tokens);
}
