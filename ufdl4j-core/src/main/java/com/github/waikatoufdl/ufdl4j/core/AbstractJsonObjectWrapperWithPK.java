/*
 * AbstractJsonObjectWrapperWithPK.java
 * Copyright (C) 2020-2023 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.core;

import com.google.gson.JsonObject;

/**
 * Ancestor for JSON objects that have a PK.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractJsonObjectWrapperWithPK
  extends AbstractJsonObjectWrapper
  implements JsonObjectWithPK {

  private static final long serialVersionUID = 1657626047226634290L;

  /**
   * Initializes the wrapper.
   *
   * @param data 	the underlying JSON to use
   */
  protected AbstractJsonObjectWrapperWithPK(JsonObject data) {
    super(data);
  }
}
