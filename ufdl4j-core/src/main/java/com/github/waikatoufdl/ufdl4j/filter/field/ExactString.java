/*
 * ExactString.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.filter.field;

import com.google.gson.JsonObject;

/**
 * Checks for the exact string in string fields.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ExactString
  extends AbstractExactMatchExpression<String> {

  private static final long serialVersionUID = 5917789873168755176L;

  /** whether the comparison is case-insensitive. */
  protected boolean m_CaseInsensitive;

  /**
   * Initializes the expression with the field (case-sensitive).
   *
   * @param field  	the field to apply to
   * @param value 	the value to look for
   */
  public ExactString(String field, String value) {
    this(field, value, false);
  }

  /**
   * Initializes the expression with the field.
   *
   * @param field  	the field to apply to
   * @param value 	the value to look for
   * @param caseInsensitive 	whether to comparison is case-insensitive
   */
  public ExactString(String field, String value, boolean caseInsensitive) {
    this(field, value, caseInsensitive, false);
  }

  /**
   * Initializes the expression with the field.
   *
   * @param field  	the field to apply to
   * @param value 	the value to look for
   * @param caseInsensitive 	whether to comparison is case-insensitive
   * @param invert 	whether to invert
   */
  public ExactString(String field, String value, boolean caseInsensitive, boolean invert) {
    super(field, value, invert);
    m_CaseInsensitive = caseInsensitive;
  }

  /**
   * Hook method for checking the value before accepting it.
   *
   * @param value 	the value to check
   * @return		the value
   * @throws IllegalArgumentException	if invalid value
   */
  protected String checkValue(String value) throws IllegalArgumentException {
    String  result;

    result = super.checkValue(value);
    if (value.isEmpty())
      throw new IllegalArgumentException("Value cannot be empty!");

    return result;
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
   * Returns a short description of the expression.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return super.toString() + ", caseinsensitive=" + isCaseInsensitive();
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
    result.addProperty("value", getValue());
    result.addProperty("case_insensitive", isCaseInsensitive());

    return result;
  }
}
