/*
 * JsonObjectWithPK.java
 * Copyright (C) 2020-2023 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.core;

/**
 * Interface for JSON objects with a primary key.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface JsonObjectWithPK {

  /**
   * Returns the primary key.
   *
   * @return		the primary key
   */
  public abstract int getPK();
}
