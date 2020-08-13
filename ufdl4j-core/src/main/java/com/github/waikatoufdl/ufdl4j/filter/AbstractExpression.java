/*
 * AbstractExpression.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.filter;

import com.github.waikatoufdl.ufdl4j.core.AbstractLoggingObject;
import com.github.waikatoufdl.ufdl4j.core.JsonObjectSupplier;
import com.github.waikatoufdl.ufdl4j.core.JsonUtils;

/**
 * Ancestor for expressions, logical or field ones.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractExpression
  extends AbstractLoggingObject
  implements JsonObjectSupplier {

  private static final long serialVersionUID = 5169044763591262822L;

  /** whether to invert. */
  protected boolean m_Invert;

  /**
   * Initializes the expression.
   *
   * @param invert	true if inverted
   */
  protected AbstractExpression(boolean invert) {
    m_Invert = invert;
  }

  /**
   * Whether the expression is inverted.
   *
   * @return		true if inverted.
   */
  public boolean isInverted() {
    return m_Invert;
  }

  /**
   * Returns the type of the expression.
   *
   * @return		the type
   */
  public abstract String getType();

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
