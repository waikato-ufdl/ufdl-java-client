/*
 * ExactNumber.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.filter.field;

import com.google.gson.JsonObject;

/**
 * Checks for the exact number in numeric fields.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ExactNumber
  extends AbstractExactMatchExpression<Number> {

  private static final long serialVersionUID = 5917789873168755176L;

  /**
   * Initializes the expression with the field.
   *
   * @param field  	the field to apply to
   * @param value 	the value to look for
   */
  public ExactNumber(String field, Number value) {
    this(field, value, false);
  }

  /**
   * Initializes the expression with the field.
   *
   * @param field  	the field to apply to
   * @param value 	the value to look for
   * @param invert 	whether to invert
   */
  public ExactNumber(String field, Number value, boolean invert) {
    super(field, value, invert);
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

    return result;
  }
}
