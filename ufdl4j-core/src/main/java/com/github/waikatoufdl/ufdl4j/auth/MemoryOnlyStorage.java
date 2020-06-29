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
