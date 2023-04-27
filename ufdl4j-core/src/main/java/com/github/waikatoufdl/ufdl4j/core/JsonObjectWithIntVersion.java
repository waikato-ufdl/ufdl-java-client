/*
 * JsonObjectWithIntVersion.java
 * Copyright (C) 2023 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.core;

/**
 * Interface for JSON objects with an integer version.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface JsonObjectWithIntVersion {

  /**
   * Returns the version.
   *
   * @return		the version
   */
  public abstract int getVersion();
}
