/*
 * IsNull.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.filter.field;

/**
 * Checks for null values in fields.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class IsNull
  extends AbstractFieldExpression {

  private static final long serialVersionUID = 5917789873168755176L;

  /**
   * Initializes the expression with the field.
   *
   * @param field		the field to apply to
   */
  public IsNull(String field) {
    this(field, false);
  }

  /**
   * Initializes the expression with the field.
   *
   * @param field		the field to apply to
   * @param invert 		whether to invert
   */
  public IsNull(String field, boolean invert) {
    super(field, invert);
  }

  /**
   * Returns the type of the expression.
   *
   * @return		the type
   */
  @Override
  public String getType() {
    return "isnull";
  }
}
