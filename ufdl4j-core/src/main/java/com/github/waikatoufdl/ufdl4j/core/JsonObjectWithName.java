/*
 * JsonObjectWithName.java
 * Copyright (C) 2023 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.core;

/**
 * Interface for JSON objects with a name.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface JsonObjectWithName {

  /**
   * Returns the name.
   *
   * @return		the name
   */
  public abstract String getName();
}
