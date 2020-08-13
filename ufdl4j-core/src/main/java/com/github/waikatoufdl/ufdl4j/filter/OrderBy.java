/*
 * OrderBy.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.filter;

import com.github.waikatoufdl.ufdl4j.core.JsonObjectSupplier;
import com.github.waikatoufdl.ufdl4j.core.JsonUtils;
import com.google.gson.JsonObject;

/**
 * TODO: What class does.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class OrderBy
  implements JsonObjectSupplier {

  /** the field in question. */
  protected String m_Field;

  /** whether to sort ascending or descending. */
  protected boolean m_Ascending;

  /** whether to list nulls first. */
  protected Boolean m_NullsFirst;

  /**
   * Initializes the order with ascending and undefined nulls-first behavior.
   *
   * @param field 	the field to apply the order to
   */
  public OrderBy(String field) {
    this(field, true);
  }

  /**
   * Initializes the order with specified ascending and undefined nulls-first behavior.
   *
   * @param field 	the field to apply the order to
   * @param ascending 	whether to sort ascending or descending
   */
  public OrderBy(String field, boolean ascending) {
    this(field, ascending, null);
  }

  /**
   * Initializes the order with specified ascending and nulls-first behavior.
   *
   * @param field 	the field to apply the order to
   * @param ascending 	whether to sort ascending or descending
   * @param nullsFirst 	if null, uses DB-behavior for null sorting, otherwise specified
   */
  public OrderBy(String field, boolean ascending, Boolean nullsFirst) {
    m_Field      = field;
    m_Ascending  = ascending;
    m_NullsFirst = nullsFirst;
  }

  /**
   * Returns the field this sorting is for.
   *
   * @return		the field name
   */
  public String getField() {
    return m_Field;
  }

  /**
   * Returns whether the sorting is ascending or descending.
   *
   * @return		true if ascending
   */
  public boolean isAscending() {
    return m_Ascending;
  }

  /**
   * Returns the nulls-first behavior.
   *
   * @return		if null, then DB-behavior is used
   */
  public Boolean getNullsFirst() {
    return m_NullsFirst;
  }

  /**
   * Returns a short string description.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return "field=" + getField() + ", asc=" + isAscending() + ", nullsFirst=" + getNullsFirst();
  }

  /**
   * Returns the JSON structure.
   *
   * @return		the json
   */
  @Override
  public JsonObject toJsonObject() {
    JsonObject	result;

    result = new JsonObject();
    result.addProperty("field", getField());
    if (!isAscending())
      result.addProperty("ascending", false);
    if (getNullsFirst() != null)
      result.addProperty("nulls_first", getNullsFirst());

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
