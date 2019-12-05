/*
 * FailedRequestException.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.core;

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
    super(response.statusCode() + "/" + response.statusMessage() + ": " + msg);
  }
}
