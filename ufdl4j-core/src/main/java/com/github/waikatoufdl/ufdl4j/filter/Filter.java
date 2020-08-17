/*
 * Filter.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.filter;

import com.github.waikatoufdl.ufdl4j.core.JsonObjectSupplier;
import com.github.waikatoufdl.ufdl4j.core.LoggingObject;
import com.google.gson.JsonObject;

import java.io.Serializable;

/**
 * Interface for filter specification.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface Filter
  extends Serializable, LoggingObject, JsonObjectSupplier {

  /**
   * Returns a short string description.
   *
   * @return		the description
   */
  @Override
  public String toString();

  /**
   * Returns the JSON structure.
   *
   * @return		the json
   */
  @Override
  public JsonObject toJsonObject();

  /**
   * Returns the stored data as pretty printed string.
   *
   * @return		the pretty json
   */
  @Override
  public String toPrettyPrint();
}
