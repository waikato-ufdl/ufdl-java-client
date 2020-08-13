/*
 * Filter.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.filter;

import com.github.waikatoufdl.ufdl4j.core.AbstractLoggingObject;
import com.github.waikatoufdl.ufdl4j.core.JsonObjectSupplier;
import com.github.waikatoufdl.ufdl4j.core.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Represents a filter specification.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Filter
  extends AbstractLoggingObject
  implements JsonObjectSupplier {

  private static final long serialVersionUID = 3644686418413637038L;

  /** the expressions. */
  protected AbstractExpression[] m_Expressions;

  /** the ordering. */
  protected OrderBy[] m_Order;

  /** whether to include inactive elements. */
  protected boolean m_IncludeInactive;

  /**
   * Initializes the filter with no expressions, no sorting, excludes inactive elements.
   */
  public Filter() {
    this(new AbstractExpression[0]);
  }

  /**
   * Initializes the filter with the expressions, no sorting, excludes inactive elements.
   *
   * @param expressions		the expressions
   */
  public Filter(AbstractExpression[] expressions) {
    this(expressions, new OrderBy[0]);
  }

  /**
   * Initializes the filter with the expressions and ordering, excludes inactive elements.
   *
   * @param expressions		the expressions
   * @param order 		the ordering to use
   */
  public Filter(AbstractExpression[] expressions, OrderBy[] order) {
    this(expressions, order, false);
  }


  /**
   * Initializes the filter with the expressions and ordering.
   *
   * @param expressions		the expressions
   * @param order 		the ordering to use, ignored if null
   * @param includeInactive 	whether to include inactive elements
   */
  public Filter(AbstractExpression[] expressions, OrderBy[] order, boolean includeInactive) {
    m_Expressions     = expressions;
    m_Order           = (order == null) ? (new OrderBy[0]) : order;
    m_IncludeInactive = includeInactive;
  }

  /**
   * Returns the underlying expressions.
   *
   * @return		the expressions
   */
  public AbstractExpression[] getExpressions() {
    return m_Expressions;
  }

  /**
   * Returns the ordering.
   *
   * @return		the ordering
   */
  public OrderBy[] getOrder() {
    return m_Order;
  }

  /**
   * Returns whether inactive elements are included.
   *
   * @return		true if included
   */
  public boolean isIncludeInactive() {
    return m_IncludeInactive;
  }

  /**
   * Appends the expression at the end.
   *
   * @param expression		the expression to add
   * @return			itself
   */
  public Filter addExpression(AbstractExpression expression) {
    AbstractExpression[] 	expressions;
    int				i;

    expressions = new AbstractExpression[m_Expressions.length + 1];
    for (i = 0; i < m_Expressions.length; i++)
      expressions[i] = m_Expressions[i];
    expressions[expressions.length - 1] = expression;
    m_Expressions = expressions;
    return this;
  }

  /**
   * Appends the orderby at the end.
   *
   * @param orderby		the orderby to add
   * @return			itself
   */
  public Filter addOrderBy(OrderBy orderby) {
    OrderBy[] 		orders;
    int			i;

    orders = new OrderBy[m_Order.length + 1];
    for (i = 0; i < m_Order.length; i++)
      orders[i] = m_Order[i];
    orders[orders.length - 1] = orderby;
    m_Order = orders;
    return this;
  }

  /**
   * Returns a short string description.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return "#expressions=" + getExpressions().length + ", #order=" + getOrder().length + ", inactive=" + isIncludeInactive();
  }

  /**
   * Returns the JSON structure.
   *
   * @return		the json
   */
  @Override
  public JsonObject toJsonObject() {
    JsonObject	result;
    int		i;
    JsonArray	expressions;
    JsonArray 	order;

    result = new JsonObject();

    expressions = new JsonArray();
    result.add("expressions", expressions);
    for (i = 0; i < getExpressions().length; i++)
      expressions.add(getExpressions()[i].toJsonObject());

    if (getOrder().length > 0) {
      order = new JsonArray();
      result.add("order_by", order);
      for (i = 0; i < getOrder().length; i++)
        order.add(getOrder()[i].toJsonObject());
    }

    result.addProperty("include_inactive", isIncludeInactive());

    return result;
  }

  /**
   * Returns the stored data as pretty printed string.
   *
   * @return		the pretty json
   */
  @Override
  public String toPrettyPrint() {
    return JsonUtils.prettyPrint(toJsonObject());
  }
}
