/*
 * JsonResponse.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.core;

import com.github.fracpete.requests4j.response.BasicResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;

/**
 * Extends {@link BasicResponse} to automatically parse the received text as JSON.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class JsonResponse
  extends BasicResponse {

  private static final long serialVersionUID = 1602511858541037362L;

  /**
   * Returns the body as text (UTF-8).
   *
   * @return		the text
   */
  public JsonElement json() throws UnsupportedEncodingException {
    return JsonParser.parseString(text());
  }

  /**
   * Returns the body as text (UTF-8).
   *
   * @return		the text
   */
  public JsonObject jsonObject() throws UnsupportedEncodingException {
    return JsonParser.parseString(text()).getAsJsonObject();
  }

  /**
   * Returns the body as text (UTF-8).
   *
   * @return		the text
   */
  public JsonArray jsonArray() throws UnsupportedEncodingException {
    return JsonParser.parseString(text()).getAsJsonArray();
  }
}
