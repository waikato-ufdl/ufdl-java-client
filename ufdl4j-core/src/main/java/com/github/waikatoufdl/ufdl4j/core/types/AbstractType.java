/*
 * AbstractType.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package com.github.waikatoufdl.ufdl4j.core.types;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.core.AbstractLoggingObject;

/**
 * Ancestor for types.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractType<T>
  extends AbstractLoggingObject {

  private static final long serialVersionUID = -6314406945474902979L;

  /**
   * Removes single quotes from the string.
   *
   * @param s		the string to unquote
   * @return		the processed string
   */
  protected String unquote(String s) {
    if (s.startsWith("'") && s.endsWith("'"))
      return s.substring(1, s.length() - 1);
    else
      return s;
  }

  /**
   * Splits the string on comma.
   *
   * @param s		the string to split
   * @return		the parts of the string
   */
  protected String[] split(String s) {
    String[]	result;
    int		i;

    result = s.split(",");
    for (i = 0; i < result.length; i++)
      result[i] = result[i].trim();

    return result;
  }

  /**
   * Returns the type name.
   *
   * @return		the type name
   */
  public abstract String getTypeName();

  /**
   * Parses the type definition.
   *
   * @param client 	the client to use
   * @param definition	the type definition
   * @return		the parsed type
   * @throws Exception	if parsing fails
   */
  public abstract T parse(Client client, String definition) throws Exception;

  /**
   * Just returns the type name.
   *
   * @return		the type name
   */
  @Override
  public String toString() {
    return getTypeName();
  }
}
