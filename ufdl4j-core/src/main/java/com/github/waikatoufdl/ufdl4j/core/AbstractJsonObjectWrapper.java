/*
 * AbstractJsonObjectWrapper.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.core;

import com.google.gson.JsonObject;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;

/**
 * Ancestor for objects that wrap around JSON objects and provide access methods
 * to the data.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractJsonObjectWrapper
  extends AbstractLoggingObject {

  private static final long serialVersionUID = 8986254238262104142L;

  /** the underlying JSON object. */
  protected JsonObject m_Data;

  /**
   * Initializes the wrapper.
   *
   * @param data 	the underlying JSON to use
   */
  protected AbstractJsonObjectWrapper(JsonObject data) {
    if (data == null)
      throw new IllegalArgumentException("JSON Object cannot be null!");
    m_Data = data;
  }

  /**
   * Returns the specified integer, throws an {@link IllegalStateException} if not present or not a number.
   *
   * @param key		the key to retrieve as integer
   * @return		the value associated with the key
   */
  protected int getInt(String key) {
    if (!m_Data.has(key))
      throw new IllegalStateException("Key does not exist: " + key);
    if (m_Data.get(key).isJsonPrimitive() && m_Data.get(key).getAsJsonPrimitive().isNumber())
      return m_Data.get(key).getAsInt();
    else
      throw new IllegalStateException("Value for key '" + key + "' is not a number!");
  }

  /**
   * Returns the specified integer.
   *
   * @param key		the key to retrieve as integer
   * @param defValue	the default value
   * @return		the value associated with the key or, if not found or not a number, the default value
   */
  protected int getInt(String key, int defValue) {
    if (m_Data.has(key) && m_Data.get(key).isJsonPrimitive() && m_Data.get(key).getAsJsonPrimitive().isNumber())
      return m_Data.get(key).getAsInt();
    else
      return defValue;
  }

  /**
   * Returns the specified long, throws an {@link IllegalStateException} if not present or not a number.
   *
   * @param key		the key to retrieve as long
   * @return		the value associated with the key
   */
  protected long getLong(String key) {
    if (!m_Data.has(key))
      throw new IllegalStateException("Key does not exist: " + key);
    if (m_Data.get(key).isJsonPrimitive() && m_Data.get(key).getAsJsonPrimitive().isNumber())
      return m_Data.get(key).getAsLong();
    else
      throw new IllegalStateException("Value for key '" + key + "' is not a number!");
  }

  /**
   * Returns the specified long.
   *
   * @param key		the key to retrieve as long
   * @param defValue	the default value
   * @return		the value associated with the key or, if not found or not a number, the default value
   */
  protected long getLong(String key, long defValue) {
    if (m_Data.has(key) && m_Data.get(key).isJsonPrimitive() && m_Data.get(key).getAsJsonPrimitive().isNumber())
      return m_Data.get(key).getAsLong();
    else
      return defValue;
  }

  /**
   * Returns the specified float, throws an {@link IllegalStateException} if not present or not a number.
   *
   * @param key		the key to retrieve as float
   * @return		the value associated with the key
   */
  protected float getFloat(String key) {
    if (!m_Data.has(key))
      throw new IllegalStateException("Key does not exist: " + key);
    if (m_Data.get(key).isJsonPrimitive() && m_Data.get(key).getAsJsonPrimitive().isNumber())
      return m_Data.get(key).getAsFloat();
    else
      throw new IllegalStateException("Value for key '" + key + "' is not a number!");
  }

  /**
   * Returns the specified float.
   *
   * @param key		the key to retrieve as float
   * @param defValue	the default value
   * @return		the value associated with the key or, if not found or not a number, the default value
   */
  protected float getFloat(String key, float defValue) {
    if (m_Data.has(key) && m_Data.get(key).isJsonPrimitive() && m_Data.get(key).getAsJsonPrimitive().isNumber())
      return m_Data.get(key).getAsFloat();
    else
      return defValue;
  }

  /**
   * Returns the specified double, throws an {@link IllegalStateException} if not present or not a number.
   *
   * @param key		the key to retrieve as double
   * @return		the value associated with the key
   */
  protected double getDouble(String key) {
    if (!m_Data.has(key))
      throw new IllegalStateException("Key does not exist: " + key);
    if (m_Data.get(key).isJsonPrimitive() && m_Data.get(key).getAsJsonPrimitive().isNumber())
      return m_Data.get(key).getAsDouble();
    else
      throw new IllegalStateException("Value for key '" + key + "' is not a number!");
  }

  /**
   * Returns the specified double.
   *
   * @param key		the key to retrieve as double
   * @param defValue	the default value
   * @return		the value associated with the key or, if not found or not a number, the default value
   */
  protected double getDouble(String key, double defValue) {
    if (m_Data.has(key) && m_Data.get(key).isJsonPrimitive() && m_Data.get(key).getAsJsonPrimitive().isNumber())
      return m_Data.get(key).getAsDouble();
    else
      return defValue;
  }

  /**
   * Returns the specified boolean, throws an {@link IllegalStateException} if not present or not a boolean.
   *
   * @param key		the key to retrieve as boolean
   * @return		the value associated with the key
   */
  protected boolean getBoolean(String key) {
    if (!m_Data.has(key))
      throw new IllegalStateException("Key does not exist: " + key);
    if (m_Data.get(key).isJsonPrimitive() && m_Data.get(key).getAsJsonPrimitive().isBoolean())
      return m_Data.get(key).getAsBoolean();
    else
      throw new IllegalStateException("Value for key '" + key + "' is not a boolean!");
  }

  /**
   * Returns the specified boolean.
   *
   * @param key		the key to retrieve as boolean
   * @param defValue	the default value
   * @return		the value associated with the key or, if not found or not a boolean, the default value
   */
  protected boolean getBoolean(String key, boolean defValue) {
    if (m_Data.has(key) && m_Data.get(key).isJsonPrimitive() && m_Data.get(key).getAsJsonPrimitive().isBoolean())
      return m_Data.get(key).getAsBoolean();
    else
      return defValue;
  }

  /**
   * Returns the specified long, throws an {@link IllegalStateException} if not present or not a string.
   *
   * @param key		the key to retrieve as string
   * @return		the value associated with the key
   */
  protected String getString(String key) {
    if (!m_Data.has(key))
      throw new IllegalStateException("Key does not exist: " + key);
    if (m_Data.get(key).isJsonPrimitive() && m_Data.get(key).getAsJsonPrimitive().isString())
      return m_Data.get(key).getAsString();
    else
      throw new IllegalStateException("Value for key '" + key + "' is not a string!");
  }

  /**
   * Returns the specified string.
   *
   * @param key		the key to retrieve as string
   * @param defValue	the default value
   * @return		the value associated with the key or, if not found or not a string, the default value
   */
  protected String getString(String key, String defValue) {
    if (m_Data.has(key) && m_Data.get(key).isJsonPrimitive() && m_Data.get(key).getAsJsonPrimitive().isString())
      return m_Data.get(key).getAsString();
    else
      return defValue;
  }

  /**
   * Returns the specified long, throws an {@link IllegalStateException} if not present or not a number.
   *
   * @param key		the key to retrieve as long
   * @return		the value associated with the key
   */
  protected LocalDateTime getDateTime(String key) {
    if (!m_Data.has(key))
      throw new IllegalStateException("Key does not exist: " + key);
    if (m_Data.get(key).isJsonPrimitive() && m_Data.get(key).getAsJsonPrimitive().isString()) {
      try {
	return LocalDateTime.parse(m_Data.get(key).getAsString(), DateTimeUtils.getDateTimeFormatter());
      }
      catch (Exception e) {
        throw new IllegalStateException("Failed to parse key '" + key + "' as date/time!", e);
      }
    }
    else {
      throw new IllegalStateException("Value for key '" + key + "' is not a string!");
    }
  }

  /**
   * Returns the specified date/time.
   *
   * @param key		the key to retrieve as date/time
   * @param defValue	the default value
   * @return		the value associated with the key or, if not found or not a date or failed to parse, the default value
   * @see		DateTimeUtils#getDateTimeFormatter()
   */
  protected LocalDateTime getDateTime(String key, LocalDateTime defValue) {
    try {
      if (m_Data.has(key) && m_Data.get(key).isJsonPrimitive() && m_Data.get(key).getAsJsonPrimitive().isString())
	return LocalDateTime.parse(m_Data.get(key).getAsString(), DateTimeUtils.getDateTimeFormatter());
    }
    catch (Exception e) {
      getLogger().log(Level.SEVERE, "Failed to parse key '" + key + "' as date/time!", e);
    }
    return defValue;
  }

  /**
   * Returns the specified array as list. In case of primitives (boolean/string/number),
   * they get returned as such, otherwise as JsonElement.
   * If key not present, throws an {@link IllegalStateException}.
   *
   * @param key		the key to retrieve as string
   * @return		the value associated with the key
   */
  protected List getList(String key) {
    if (!m_Data.has(key))
      throw new IllegalStateException("Key does not exist: " + key);
    return getList(key, null);
  }

  /**
   * Returns the specified array as list. In case of primitives (boolean/string/number),
   * they get returned as such, otherwise as JsonElement.
   *
   * @param key		the key to retrieve as string
   * @param defValue	the default value
   * @return		the value associated with the key or, if not found or not a string, the default value
   */
  protected List getList(String key, List defValue) {
    return JsonUtils.asList(m_Data, key, defValue);
  }

  /**
   * Returns the underlying data.
   *
   * @return		the data
   */
  public JsonObject getData() {
    return m_Data;
  }

  /**
   * Returns the stored data as pretty printed string.
   *
   * @return		the pretty json
   */
  public String toPrettyPrint() {
    return JsonUtils.prettyPrint(m_Data);
  }

  /**
   * Creates a new instance of the specified wrapper class, reusing the
   * internal JSON object.
   *
   * @param wrapper	the wrapper class to instantiate
   * @param <T>		the type of wrapper
   * @return		the wrapper instance
   * @throws Exception	if instantiation fails
   */
  public <T extends AbstractJsonObjectWrapper> T as(Class<T> wrapper) throws Exception {
    T 			result;
    Constructor<T>	constr;

    constr = wrapper.getConstructor(JsonObject.class);
    result = constr.newInstance(m_Data);

    return result;
  }
}
