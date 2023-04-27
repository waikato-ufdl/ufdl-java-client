/*
 * JsonObjectWithDomain.java
 * Copyright (C) 2023 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.core;

/**
 * Interface for JSON objects with a domain.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface JsonObjectWithDomain {

  /**
   * Returns the domain.
   *
   * @return		the domain
   */
  public abstract String getDomain();
}
