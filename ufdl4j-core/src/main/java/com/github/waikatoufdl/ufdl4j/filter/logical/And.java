/*
 * And.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.filter.logical;

import com.github.waikatoufdl.ufdl4j.filter.AbstractExpression;

/**
 * Logical AND expression.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class And
  extends AbstractLogicalExpression {

  private static final long serialVersionUID = -834731626494736273L;

  /**
   * Initializes the logical expression with the sub-expressions.
   *
   * @param subExpressions	the sub expressions to use
   * @throws IllegalArgumentException	if invalid expressions
   */
  public And(AbstractExpression[] subExpressions) throws IllegalArgumentException {
    this(subExpressions, false);
  }

  /**
   * Initializes the logical expression with the sub-expressions.
   *
   * @param subExpressions	the sub expressions to use
   * @param invert		true if inverted
   * @throws IllegalArgumentException	if invalid expressions
   */
  public And(AbstractExpression[] subExpressions, boolean invert) throws IllegalArgumentException {
    super(subExpressions, invert);
  }

  /**
   * Hook method for checking the sub-expressions before accepting them.
   *
   * @param subExpressions	the expressions to check
   * @return			the expressions
   * @throws IllegalArgumentException	if invalid expressions
   */
  public AbstractExpression[] checkSubExpressions(AbstractExpression[] subExpressions) throws IllegalArgumentException {
    super.checkSubExpressions(subExpressions);
    for (AbstractExpression sub: subExpressions) {
      if (sub instanceof Or)
        throw new IllegalArgumentException("Sub-expressions cannot contain other " + Or.class.getName() + " expressions!");
    }
    return subExpressions;
  }

  /**
   * Returns the type of the expression.
   *
   * @return		the type
   */
  @Override
  public String getType() {
    return "and";
  }
}
