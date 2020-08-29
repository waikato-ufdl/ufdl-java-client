/*
 * FailedRequestException.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.core;

import com.github.fracpete.requests4j.response.BasicResponse;
import com.github.fracpete.requests4j.response.Response;

/**
 * Convenience exception for failed requests.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class FailedRequestException
  extends Exception {

  private static final long serialVersionUID = 1844792766565512809L;

  /**
   * Initializes the exception with the message and the response.
   *
   * @param msg		the message to use
   * @param response	the response of the failed request
   */
  public FailedRequestException(String msg, Response response) {
    super(toString(msg, response));
  }

  /**
   * Turns the response into a string.
   *
   * @param response 	the response to turn into a string.
   * @return		the generated string
   */
  protected static String toString(String msg, Response response) {
    StringBuilder	result;
    String		body;

    result = new StringBuilder(msg);
    result.append("\n").append(response.statusCode()).append(": ").append(response.statusMessage());
    if (response instanceof BasicResponse) {
      try {
        body = ((BasicResponse) response).text();
        if (!body.isEmpty())
          result.append("\n").append(body);
      }
      catch (Exception e) {
        // ignored
      }
    }

    return result.toString();
  }
}
