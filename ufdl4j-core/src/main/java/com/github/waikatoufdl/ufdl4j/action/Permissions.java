/*
 * Permissions.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

/**
 * General permissions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public enum Permissions {
  READ("R"),
  WRITE("W"),
  ADMIN("A");

  /** the string to use as display. */
  private String m_Display;

  /** the commandline string. */
  private String m_Raw;

  /**
   * Initializes the enum.
   *
   * @param display 	the display string to use
   */
  private Permissions(String display) {
    m_Display = display;
    m_Raw     = super.toString();
  }

  /**
   * Returns the display string.
   *
   * @return		the display string
   */
  public String toDisplay() {
    return m_Display;
  }

  /**
   * Returns the raw enum string.
   *
   * @return		the raw enum string
   */
  public String toRaw() {
    return m_Raw;
  }

  /**
   * Returns the display string.
   *
   * @return		the display string
   */
  public String toString() {
    return m_Display;
  }

  /**
   * Returns an enum generated from the string.
   *
   * @param str		the string to convert to an enum
   * @return		the generated enum or null in case of error
   */
  public static Permissions parse(String str) {
    Permissions	result;

    result = null;

    // default parsing
    try {
      result = valueOf(str);
    }
    catch (Exception e) {
      // ignored
    }

    // try display
    if (result == null) {
      for (Permissions p : values()) {
	if (p.toDisplay().equals(str)) {
	  result = p;
	  break;
	}
      }
    }

    return result;
  }
}
