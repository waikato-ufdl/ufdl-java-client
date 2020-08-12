/*
 * CustomDisplayEnum.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.core;

/**
 * Interface for enums that have a custom display string.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface CustomDisplayEnum {

  /**
   * Returns the display string.
   *
   * @return		the display string
   */
  public String toDisplay();

  /**
   * Returns the raw enum string.
   *
   * @return		the raw enum string
   */
  public String toRaw();

  /**
   * Returns the display string.
   *
   * @return		the display string
   */
  public String toString();
}
