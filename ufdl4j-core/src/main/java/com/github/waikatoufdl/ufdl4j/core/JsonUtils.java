/*
 * JsonUtils.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for JSON related tasks.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class JsonUtils {

  /**
   * Generates a pretty-printed string from the JSON element.
   *
   * @param e		the element to turn into astring
   * @return		the generated string
   */
  public static String prettyPrint(JsonElement e) {
    Gson 	gson;

    gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(e);
  }

  /**
   * Returns the specified array as list. In case of primitives (boolean/string/number),
   * they get returned as such, otherwise as JsonElement.
   *
   * @param object 	the object to process
   * @param key		the key to retrieve as string
   * @param defValue	the default value
   * @return		the value associated with the key or, if not found or not a string, the default value
   */
  public static List getList(JsonObject object, String key, List defValue) {
    List		list;
    int			i;
    JsonArray array;
    JsonPrimitive prim;

    if (object.has(key) && object.get(key).isJsonArray()) {
      list  = new ArrayList();
      array = object.getAsJsonArray(key);
      for (i = 0; i < array.size(); i++) {
        if (array.get(i).isJsonPrimitive()) {
          prim = array.get(i).getAsJsonPrimitive();
          if (prim.isString())
	    list.add(prim.getAsString());
          else if (prim.isBoolean())
	    list.add(prim.getAsBoolean());
          else if (prim.isNumber())
            list.add(prim.getAsNumber());
	}
	else {
          list.add(array.get(i));
	}
      }
      return list;
    }
    else {
      return defValue;
    }
  }

  /**
   * Turns the list with primitives or strings into an array.
   *
   * @param values	the list to convert
   * @return		the array
   */
  public static JsonArray toArray(List values) {
    JsonArray	result;

    result = new JsonArray();
    for (Object value: values) {
      if (value instanceof String)
        result.add((String) value);
      else if (value instanceof Number)
        result.add((Number) value);
      else if (value instanceof Boolean)
        result.add((Boolean) value);
      else if (value instanceof Character)
        result.add((Character) value);
      else
        throw new IllegalStateException("Unhandled list element type: " + value.getClass().getName());
    }

    return result;
  }
}
