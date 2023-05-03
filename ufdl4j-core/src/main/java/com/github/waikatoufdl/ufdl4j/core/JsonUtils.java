/*
 * JsonUtils.java
 * Copyright (C) 2020-2023 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  public static List asList(JsonObject object, String key, List defValue) {
    List 	list;

    if (!object.has(key))
      return defValue;
    if (object.get(key).isJsonNull())
      return defValue;

    if (object.has(key) && object.get(key).isJsonArray()) {
      list = asList(object.getAsJsonArray(key));
      return list;
    }
    else {
      return defValue;
    }
  }

  /**
   * Returns the specified array as list. In case of primitives (boolean/string/number),
   * they get returned as such, otherwise as JsonElement.
   *
   * @param array 	the array to process
   * @return		the value associated with the key or, if not found or not a string, the default value
   */
  public static List asList(JsonArray array) {
    List		list;
    int			i;
    JsonPrimitive prim;

    list  = new ArrayList();
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

  /**
   * Turns a Java array into a JSON array.
   *
   * @param obj		the Java array to convert
   * @return		the JSON array
   */
  public static JsonArray arrayToJson(Object obj) {
    JsonArray	result;
    Object	o;
    int		i;

    result = new JsonArray();

    for (i = 0; i < Array.getLength(obj); i++) {
      o = Array.get(obj, i);
      if (o instanceof Boolean)
        result.add((Boolean) o);
      else if (o instanceof Number)
        result.add((Number) o);
      else
        result.add("" + o);
    }

    return result;
  }

  /**
   * Turns a map into a json object.
   *
   * @param map		the map to convert
   * @return		the generated json object
   */
  public static JsonObject mapToJson(Map map) {
    JsonObject  result;
    Object	obj;
    String	key;

    result = new JsonObject();
    for (Object keyObj: map.keySet()) {
      key = "" + keyObj;
      obj = map.get(keyObj);
      if (obj instanceof Number)
	result.addProperty(key, (Number) obj);
      else if (obj instanceof Boolean)
	result.addProperty(key, (Boolean) obj);
      else if (obj instanceof String)
	result.addProperty(key, (String) obj);
      else if (obj instanceof Map)
	result.add(key, mapToJson((Map) obj));
      else if (obj.getClass().isArray()) {
	result.add(key, arrayToJson(obj));
      }
    }

    return result;
  }

  /**
   * Turns a Json object into a map.
   *
   * @param obj		the json object to convert
   * @return		the generated map
   */
  public static Map jsonToMap(JsonObject obj) {
    Map			result;
    JsonElement		elem;
    Object		val;

    result = new HashMap();
    for (String key: obj.keySet()) {
      elem = obj.get(key);
      val  = toObject(elem);
      if (val != null)
	result.put(key, val);
    }

    return result;
  }

  /**
   * Tries to convert the Json object into a Java one.
   *
   * @param obj		the Json object to convert
   * @return		the Java object or null if failed to convert
   */
  public static Object toObject(JsonElement obj) {
    Object		result;
    JsonPrimitive 	prim;

    result = null;

    if (obj.isJsonPrimitive()) {
      prim = obj.getAsJsonPrimitive();
      if (prim.isBoolean())
	result = prim.getAsBoolean();
      else if (prim.isNumber())
	result = prim.getAsNumber();
      else if (prim.isString())
	result = prim.getAsString();
    }
    else if (obj.isJsonObject()) {
      result = jsonToMap(obj.getAsJsonObject());
    }
    else if (obj.isJsonArray()) {
      result = JsonUtils.asList(obj.getAsJsonArray());
    }

    return result;
  }
}
