/*
 * DomainFilter.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.filter;

import com.github.waikatoufdl.ufdl4j.core.AbstractLoggingObject;
import com.github.waikatoufdl.ufdl4j.core.JsonUtils;
import com.github.waikatoufdl.ufdl4j.filter.field.ExactNumber;
import com.google.gson.JsonObject;

/**
 * Simple exact domain filter.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class DomainFilter
  extends AbstractLoggingObject
  implements Filter {

  private static final long serialVersionUID = -7995513145236262468L;

  /** the domain. */
  protected int m_Domain;

  /**
   * Initializes the filter.
   *
   * @param domain	the name to filter
   */
  public DomainFilter(int domain) {
    super();
    m_Domain = domain;
  }

  /**
   * Returns the domain.
   *
   * @return		the domain
   */
  public int getDomain() {
    return m_Domain;
  }

  /**
   * Returns the JSON structure.
   *
   * @return		the json
   */
  @Override
  public JsonObject toJsonObject() {
    return new GenericFilter(
      new AbstractExpression[]{
	new ExactNumber("domain", m_Domain),
      }
    ).toJsonObject();
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

  /**
   * Returns a short description of the state.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return "domain=" + getDomain();
  }
}
