/*
 * AbstractJsonObjectWrapper.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdljavaclient.core;

import com.google.gson.JsonObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Ancestor for objects that wrap around JSON objects and provide access methods
 * to the data.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractJsonObjectWrapper
  extends AbstractLoggingObject {

  private static final long serialVersionUID = 8986254238262104142L;

  /** the date/time format to use. */
  public final static String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSVV";

  /** for parsing date/time strings. */
  protected transient DateTimeFormatter m_DateTimeFormatter;

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
   * Returns the date formatter/parser.
   *
   * @return		the instance
   */
  protected synchronized DateTimeFormatter getDateTimeFormatter() {
    if (m_DateTimeFormatter == null)
      m_DateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
    return m_DateTimeFormatter;
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
   * Returns the specified date/time.
   *
   * @param key		the key to retrieve as date/time
   * @param defValue	the default value
   * @return		the value associated with the key or, if not found or not a date or failed to parse, the default value
   * @see		#getDateTimeFormatter()
   */
  protected LocalDateTime getDateTime(String key, LocalDateTime defValue) {
    try {
      if (m_Data.has(key) && m_Data.get(key).isJsonPrimitive() && m_Data.get(key).getAsJsonPrimitive().isString())
	return LocalDateTime.parse(m_Data.get(key).getAsString(), getDateTimeFormatter());
    }
    catch (Exception e) {
      e.printStackTrace();
      // ignored
    }
    return defValue;
  }

  /**
   * Returns the underlying data.
   *
   * @return		the data
   */
  public JsonObject getData() {
    return m_Data;
  }
}
