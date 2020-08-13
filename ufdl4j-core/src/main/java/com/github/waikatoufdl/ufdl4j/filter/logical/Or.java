/*
 * Or.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.filter.logical;

import com.github.waikatoufdl.ufdl4j.filter.AbstractExpression;

/**
 * Logical OR expression.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Or
  extends AbstractLogicalExpression {

  private static final long serialVersionUID = -834731626494736273L;

  /**
   * Initializes the logical expression with the sub-expressions.
   *
   * @param subExpressions	the sub expressions to use
   * @throws IllegalArgumentException	if invalid expressions
   */
  public Or(AbstractExpression[] subExpressions) throws IllegalArgumentException {
    this(subExpressions, false);
  }

  /**
   * Initializes the logical expression with the sub-expressions.
   *
   * @param subExpressions	the sub expressions to use
   * @param invert		true if inverted
   * @throws IllegalArgumentException	if invalid expressions
   */
  public Or(AbstractExpression[] subExpressions, boolean invert) throws IllegalArgumentException {
    super(subExpressions, invert);
  }

  /**
   * Returns the type of the expression.
   *
   * @return		the type
   */
  @Override
  public String getType() {
    return "or";
  }
}
