/*
 * AbstractLogicalExpression.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.filter.logical;

import com.github.waikatoufdl.ufdl4j.filter.AbstractExpression;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Ancestor for logical expressions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractLogicalExpression
  extends AbstractExpression {

  private static final long serialVersionUID = -7903990449000632054L;

  /** the sub expressions. */
  protected AbstractExpression[] m_SubExpressions;

  /**
   * Initializes the logical expression with no sub-expressions.
   *
   * @throws IllegalArgumentException	if invalid expressions
   */
  protected AbstractLogicalExpression() throws IllegalArgumentException {
    this(false);
  }

  /**
   * Initializes the logical expression with no sub-expressions.
   *
   * @param invert		true if inverted
   * @throws IllegalArgumentException	if invalid expressions
   */
  protected AbstractLogicalExpression(boolean invert) throws IllegalArgumentException {
    super(invert);
    m_SubExpressions = new AbstractExpression[0];
  }

  /**
   * Initializes the logical expression with the sub-expressions.
   *
   * @param subExpressions	the sub expressions to use
   * @param invert		true if inverted
   * @throws IllegalArgumentException	if invalid expressions
   */
  protected AbstractLogicalExpression(AbstractExpression[] subExpressions, boolean invert) throws IllegalArgumentException {
    super(invert);
    m_SubExpressions = checkSubExpressions(subExpressions);
  }

  /**
   * Hook method for checking the sub-expressions before accepting them.
   *
   * @param subExpressions	the expressions to check
   * @return			the expressions
   * @throws IllegalArgumentException	if invalid expressions
   */
  public AbstractExpression[] checkSubExpressions(AbstractExpression[] subExpressions) throws IllegalArgumentException {
    if (subExpressions == null)
      throw new IllegalArgumentException("Sub-expressions cannot be null!");
    if (subExpressions.length < 2)
      throw new IllegalArgumentException("At least two sub-expressions have to be supplied");
    return subExpressions;
  }

  /**
   * Returns the sub-expressions.
   *
   * @return		the expressions
   */
  public AbstractExpression[] getSubExpressions() {
    return m_SubExpressions;
  }

  /**
   * Appends the subexpression at the end.
   *
   * @param subExpression	the expression to add
   * @return			itself
   */
  public AbstractLogicalExpression addSubExpression(AbstractExpression subExpression) {
    AbstractExpression[] 	subExpressions;
    int				i;

    subExpressions = new AbstractExpression[m_SubExpressions.length + 1];
    for (i = 0; i < m_SubExpressions.length; i++)
      subExpressions[i] = m_SubExpressions[i];
    subExpressions[subExpressions.length - 1] = subExpression;
    m_SubExpressions = subExpressions;
    return this;
  }

  /**
   * Returns a short string description.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return "#subexpressions=" + getSubExpressions().length;
  }

  /**
   * Returns the JSON structure.
   *
   * @return		the json
   */
  @Override
  public JsonObject toJsonObject() {
    JsonObject 		result;
    int			i;
    JsonArray 		expressions;

    result = new JsonObject();
    result.addProperty("type", getType());

    expressions = new JsonArray();
    result.add("sub_expressions", expressions);
    for (i = 0; i < getSubExpressions().length; i++)
      expressions.add(getSubExpressions()[i].toJsonObject());

    return result;
  }
}
