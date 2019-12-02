/*
 * AbstractLoggingObject.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdljavaclient.core;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Ancestor for classes with logging support.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractLoggingObject
  implements Serializable {

  private static final long serialVersionUID = 2158229474578643464L;

  /** the logger to use. */
  protected Logger m_Logger;

  /**
   * Returns the logger for this class.
   *
   * @return		the logger
   */
  public synchronized Logger getLogger() {
    if (m_Logger == null)
      m_Logger = Logging.getLogger(getClass());
    return m_Logger;
  }

  /**
   * Returns a short description of the state.
   *
   * @return		the description
   */
  public abstract String toString();
}
