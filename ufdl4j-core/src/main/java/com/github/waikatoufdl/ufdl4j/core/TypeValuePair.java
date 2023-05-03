/*
 * TypeValueMap.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package com.github.waikatoufdl.ufdl4j.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Returns a map of a type-value pair.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class TypeValuePair {

  /**
   * Generates a map from a type-value pair.
   *
   * @param type	the type
   * @param value	the value
   * @return		the generated map
   */
  public static Map<String,Object> typeValuePair(String type, Object value) {
    Map<String,Object>  result;

    result = new HashMap<>();
    result.put("type", type);
    result.put("value", value);

    return result;
  }
}
