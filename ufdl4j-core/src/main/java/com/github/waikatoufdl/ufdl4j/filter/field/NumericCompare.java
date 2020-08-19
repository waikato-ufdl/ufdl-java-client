/*
 * NumericCompare.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.filter.field;

import com.google.gson.JsonObject;

/**
 * Compares two numbers.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class NumericCompare
  extends AbstractCompareExpression<Number> {

  private static final long serialVersionUID = 5917789873168755176L;

  /**
   * Initializes the expression with the field.
   *
   * @param field  	the field to apply to
   * @param value 	the value to look for
   * @param comparison  how to compare the values
   */
  public NumericCompare(String field, Number value, Comparison comparison) {
    this(field, value, comparison, false);
  }

  /**
   * Initializes the expression with the field.
   *
   * @param field  	the field to apply to
   * @param value 	the value to look for
   * @param comparison  how to compare the values
   * @param invert 	whether to invert
   */
  public NumericCompare(String field, Number value, Comparison comparison, boolean invert) {
    super(field, value, comparison, invert);
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
