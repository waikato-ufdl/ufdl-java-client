/*
 * DateTimeUtils.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.core;

import java.time.format.DateTimeFormatter;

/**
 * Helper class for date/time related operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class DateTimeUtils {

  /** the date/time format to use. */
  public final static String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSVV";

  /** for parsing date/time strings. */
  protected static DateTimeFormatter m_DateTimeFormatter;

  /**
   * Returns the date formatter/parser.
   *
   * @return		the instance
   */
  public static synchronized DateTimeFormatter getDateTimeFormatter() {
    if (m_DateTimeFormatter == null)
      m_DateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
    return m_DateTimeFormatter;
  }
}
