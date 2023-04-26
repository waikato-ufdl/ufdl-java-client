/*
 * DescriptionFilter.java
 * Copyright (C) 2023 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.filter;

import com.github.waikatoufdl.ufdl4j.core.AbstractLoggingObject;
import com.github.waikatoufdl.ufdl4j.core.JsonUtils;
import com.github.waikatoufdl.ufdl4j.filter.field.ExactString;
import com.google.gson.JsonObject;

/**
 * Simple exact description filter.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class DescriptionFilter
  extends AbstractLoggingObject
  implements Filter {

  private static final long serialVersionUID = -7995513145236262468L;

  /** the name. */
  protected String m_Description;

  /**
   * Initializes the filter.
   *
   * @param description	the description to filter
   */
  public DescriptionFilter(String description) {
    super();
    if (description == null)
      throw new IllegalArgumentException("Description cannot be null!");
    m_Description = description;
  }

  /**
   * Returns the name.
   *
   * @return		the name
   */
  public String getDescription() {
    return m_Description;
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
	new ExactString("description", m_Description),
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
    return "description=" + getDescription();
  }
}
