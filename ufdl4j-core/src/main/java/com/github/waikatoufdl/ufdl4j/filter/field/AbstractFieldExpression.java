/*
 * AbstractFieldExpression.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.filter.field;

import com.github.waikatoufdl.ufdl4j.filter.AbstractExpression;
import com.google.gson.JsonObject;

/**
 * Ancestor for field expressions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractFieldExpression
  extends AbstractExpression {

  private static final long serialVersionUID = -586470007521788589L;

  /** the field the expression is for. */
  protected String m_Field;

  /**
   * Initializes the expression with the field.
   *
   * @param field	the field to apply to
   * @param invert	whether to invert
   */
  protected AbstractFieldExpression(String field, boolean invert) {
    super(invert);
    m_Field = field;
  }

  /**
   * Returns the field this expression is for.
   *
   * @return		the field
   */
  public String getField() {
    return m_Field;
  }

  /**
   * Returns a short description of the expression.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return getType() + ", field=" + getField() + ", invert=" + isInverted();
  }

  /**
   * Returns the JSON structure.
   *
   * @return		the json
   */
  @Override
  public JsonObject toJsonObject() {
    JsonObject result;

    result = new JsonObject();
    result.addProperty("type", getType());
    result.addProperty("field", getField());
    if (isInverted())
      result.addProperty("invert", true);

    return result;
  }
}
