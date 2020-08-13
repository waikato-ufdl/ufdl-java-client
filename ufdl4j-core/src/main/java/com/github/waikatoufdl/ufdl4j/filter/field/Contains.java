/*
 * Contains.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.filter.field;

import com.google.gson.JsonObject;

/**
 * Checks for sub-strings in string fields.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Contains
  extends AbstractFieldExpression {

  private static final long serialVersionUID = 5917789873168755176L;

  /** the substring to look for. */
  protected String m_SubString;

  /** whether the search is case-insensitive. */
  protected boolean m_CaseInsensitive;

  /**
   * Initializes the expression with the field.
   *
   * @param field		the field to apply to
   * @param subString 		the string to search for
   * @param caseInsensitive 	whether the search is case-insensitive
   */
  public Contains(String field, String subString, boolean caseInsensitive) {
    this(field, subString, caseInsensitive, false);
  }

  /**
   * Initializes the expression with the field.
   *
   * @param field		the field to apply to
   * @param subString 		the string to search for
   * @param caseInsensitive 	whether the search is case-insensitive
   * @param invert 		whether to invert
   */
  public Contains(String field, String subString, boolean caseInsensitive, boolean invert) {
    super(field, invert);
    if (subString == null)
      throw new IllegalArgumentException("Substring cannot be null!");
    if (subString.isEmpty())
      throw new IllegalArgumentException("Substring cannot be empty!");
    m_SubString       = subString;
    m_CaseInsensitive = caseInsensitive;
  }

  /**
   * Returns the sub string to look for.
   *
   * @return		the string
   */
  public String getSubString() {
    return m_SubString;
  }

  /**
   * Returns whether the search is case-insensitive.
   *
   * @return		true if case-insensitive
   */
  public boolean isCaseInsensitive() {
    return m_CaseInsensitive;
  }

  /**
   * Returns the type of the expression.
   *
   * @return		the type
   */
  @Override
  public String getType() {
    return "contains";
  }

  /**
   * Returns a short description of the expression.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return getType();
  }

  /**
   * Returns the JSON structure.
   *
   * @return		the json
   */
  @Override
  public JsonObject toJsonObject() {
    JsonObject 	result;

    result = super.toJsonObject();
    result.addProperty("sub_string", getSubString());
    result.addProperty("case_insensitive", isCaseInsensitive());

    return result;
  }
}
