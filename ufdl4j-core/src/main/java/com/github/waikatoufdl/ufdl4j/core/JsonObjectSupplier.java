/*
 * JsonObjectSupplier.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.core;

import com.google.gson.JsonObject;

/**
 * Interface for objects that return a JsonObject.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface JsonObjectSupplier {

  /**
   * Returns the JSON structure.
   *
   * @return		the json
   */
  public JsonObject toJsonObject();

  /**
   * Returns the stored data as pretty printed string.
   *
   * @return		the pretty json
   */
  public String toPrettyPrint();
}
