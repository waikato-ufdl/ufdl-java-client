/*
 * LoggingObject.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.core;

import java.util.logging.Logger;

/**
 * Interface for classes that support logging.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface LoggingObject {

  /**
   * Returns the logger for this class.
   *
   * @return		the logger
   */
  public Logger getLogger();
}
