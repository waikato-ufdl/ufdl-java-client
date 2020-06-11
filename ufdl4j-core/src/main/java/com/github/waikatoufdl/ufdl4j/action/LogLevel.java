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
 * LogLevel.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

/**
 * The standard logging levels.
 */
public enum LogLevel {
  DEBUG(10),
  INFO(20),
  WARNING(30),
  ERROR(40),
  FATAL(50),
  OTHER(-1);

  /** the integer value of the level. */
  private int m_Level;

  /**
   * Initializes the level.
   *
   * @param level	the integer value
   */
  private LogLevel(int level) {
    m_Level = level;
  }

  /**
   * Returns the level as integer.
   *
   * @return		the level
   */
  public int getLevel() {
    return m_Level;
  }

  /**
   * Evaluates either name or integer value to a Level.
   *
   * @param s		the string to parse
   * @return		the level
   */
  public static LogLevel parse(String s) {
    int	level;

    try {
      level = Integer.parseInt(s);
      return parse(level);
    }
    catch (Exception e) {
      return valueOf(s);
    }
  }

  /**
   * Evaluates the integer value to a Level.
   *
   * @param level	the integer to interpret
   * @return		the level
   */
  public static LogLevel parse(int level) {
    switch (level) {
      case 10:
	return DEBUG;
      case 20:
	return INFO;
      case 30:
	return WARNING;
      case 40:
	return ERROR;
      case 50:
	return FATAL;
      default:
	return OTHER;
    }
  }
}
