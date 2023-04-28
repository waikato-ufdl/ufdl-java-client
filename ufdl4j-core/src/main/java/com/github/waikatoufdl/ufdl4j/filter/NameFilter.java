/*
 * NameFilter.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.filter;

import com.github.waikatoufdl.ufdl4j.core.AbstractLoggingObject;
import com.github.waikatoufdl.ufdl4j.core.JsonObjectWithName;
import com.github.waikatoufdl.ufdl4j.core.JsonUtils;
import com.github.waikatoufdl.ufdl4j.filter.field.ExactString;
import com.google.gson.JsonObject;

/**
 * Simple exact name filter.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class NameFilter
  extends AbstractLoggingObject
  implements Filter {

  private static final long serialVersionUID = -7995513145236262468L;

  /** the name. */
  protected String m_Name;

  /**
   * Initializes the filter.
   *
   * @param obj		the object to obtain the name from for filtering
   */
  public NameFilter(JsonObjectWithName obj) {
    this(obj.getName());
  }

  /**
   * Initializes the filter.
   *
   * @param name	the name to filter
   */
  public NameFilter(String name) {
    super();
    if (name == null)
      throw new IllegalArgumentException("Name cannot be null!");
    m_Name = name;
  }

  /**
   * Returns the name.
   *
   * @return		the name
   */
  public String getName() {
    return m_Name;
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
	new ExactString("name", m_Name),
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
    return "name=" + getName();
  }
}
