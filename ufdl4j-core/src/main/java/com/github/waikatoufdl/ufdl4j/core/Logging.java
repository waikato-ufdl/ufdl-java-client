/*
 * Logging.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.core;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class for logging.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Logging {

  /** the cache for the loglevels. */
  protected static Map<Class,Level> m_LogLevelCache = new HashMap<>();

  /** the default handler. */
  protected static Handler m_DefaultHandler;

  /**
   * Returns the default handler for loggers.
   *
   * @return		the handler
   */
  protected static synchronized Handler getDefaultHandler() {
    if (m_DefaultHandler == null)
      m_DefaultHandler = new ConsoleHandler();
    return m_DefaultHandler;
  }

  /**
   * Instantiates a logger for the given class.
   *
   * @param cls		the class the logger is for
   * @return		the instance
   */
  public static Logger getLogger(Class cls) {
    Logger	result;

    result = Logger.getLogger(cls.getName());
    result.setUseParentHandlers(false);
    result.addHandler(getDefaultHandler());

    return result;
  }
}
