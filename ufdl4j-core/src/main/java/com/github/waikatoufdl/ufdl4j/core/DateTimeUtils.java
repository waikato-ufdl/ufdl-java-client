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
