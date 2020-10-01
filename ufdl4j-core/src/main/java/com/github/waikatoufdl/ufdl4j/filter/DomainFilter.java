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

  /** how to order the results. */
  protected OrderBy[] m_Order;

  /**
   * Initializes the filter.
   *
   * @param domain	the name to filter
   */
  public DomainFilter(int domain) {
    this(domain, null);
  }

  /**
   * Initializes the filter.
   *
   * @param domain	the name to filter
   * @param order 	how to order the results, can be null
   */
  public DomainFilter(int domain, OrderBy[] order) {
    super();
    if (order == null)
      order = new OrderBy[0];
    m_Domain = domain;
    m_Order  = order;
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
   * Returns how to order the results.
   *
   * @return		the ordering
   */
  public OrderBy[] getOrder() {
    return m_Order;
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
      },
      m_Order
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
