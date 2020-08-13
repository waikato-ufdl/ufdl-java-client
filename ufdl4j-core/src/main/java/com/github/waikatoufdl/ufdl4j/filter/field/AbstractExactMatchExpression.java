/*
 * AbstractExactMatchExpression.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.filter.field;

/**
 * Ancestor for exact match expressions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractExactMatchExpression<T>
  extends AbstractFieldExpression {

  private static final long serialVersionUID = 800911045812096379L;

  /** the value to look for. */
  protected T m_Value;

  /**
   * Initializes the expression with the field.
   *
   * @param field	the field to apply to
   * @param invert	whether to invert
   */
  protected AbstractExactMatchExpression(String field, T value, boolean invert) {
    super(field, invert);
    m_Value = value;
  }

  /**
   * Hook method for checking the value before accepting it.
   *
   * @param value 	the value to check
   * @return		the value
   * @throws IllegalArgumentException	if invalid value
   */
  protected T checkValue(T value) throws IllegalArgumentException {
    if (value == null)
      throw new IllegalArgumentException("Value cannot be null!");
    return value;
  }

  /**
   * Returns the value to look for.
   *
   * @return		the value
   */
  public T getValue() {
    return m_Value;
  }

  /**
   * Returns the type of the expression.
   *
   * @return		the type
   */
  @Override
  public String getType() {
    return "exact";
  }

  /**
   * Returns a short description of the expression.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return super.toString() + ", value=" + getValue();
  }
}
