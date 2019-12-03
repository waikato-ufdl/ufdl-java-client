/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * FailedRequestException.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdljavaclient.core;

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
