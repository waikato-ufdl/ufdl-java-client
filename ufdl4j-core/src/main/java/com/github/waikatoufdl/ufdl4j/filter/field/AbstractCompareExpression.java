/*
 * AbstractExactMatchExpression.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.filter.field;

import com.google.gson.JsonObject;

/**
 * Ancestor for comparison expressions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractCompareExpression<T>
  extends AbstractFieldExpression {

  private static final long serialVersionUID = 800911045812096379L;

  /**
   * The comparison to perform.
   */
  public enum Comparison {
    LESS_THAN("<"),
    LESS_OR_EQUAL_THAN("<="),
    GREATER_OR_EQUAL_THAN(">="),
    GREATER_THAN(">");

    /** the operator string. */
    private String m_Operator;

    /**
     * Initializes the enum value.
     *
     * @param operator 	the operator string to use
     */
    private Comparison(String operator) {
      m_Operator = operator;
    }

    /**
     * Returns the operator string.
     *
     * @return		the operator
     */
    public String getOperator() {
      return m_Operator;
    }
  }

  /** the value to compare against. */
  protected T m_Value;

  /** the comparison to use. */
  protected Comparison m_Comparison;

  /**
   * Initializes the expression with the field.
   *
   * @param field	the field to apply to
   * @param value 	the value to compare against
   * @param comparison 	the comparison to perform
   * @param invert	whether to invert
   */
  protected AbstractCompareExpression(String field, T value, Comparison comparison, boolean invert) {
    super(field, invert);
    m_Value      = checkValue(value);
    m_Comparison = comparison;
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
   * Returns the comparison to perform.
   *
   * @return		the comparison
   */
  public Comparison getComparison() {
    return m_Comparison;
  }

  /**
   * Returns the type of the expression.
   *
   * @return		the type
   */
  @Override
  public String getType() {
    return "compare";
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
    result.addProperty("operator", getComparison().getOperator());

    return result;
  }

  /**
   * Returns a short description of the expression.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return super.toString() + ", value=" + getValue() + ", comparison=" + getComparison();
  }
}
