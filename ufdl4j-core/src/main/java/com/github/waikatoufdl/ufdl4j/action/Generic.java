/*
 * Generic.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.fracpete.requests4j.response.JsonResponse;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import okhttp3.MediaType;

/**
 * For generic API calls.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Generic
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "Generic";
  }

  /**
   * Not used.
   *
   * @return		always empty
   */
  @Override
  public String getPath() {
    return "NOT USED";
  }

  /**
   * Call to retrieve text.
   *
   * @param request 	the request to execute
   * @param body 	the body, ignored if null
   * @param mediaType 	the media type, ignored if body is null
   * @return		the raw text
   * @throws Exception	if request fails
   */
  protected String plain(Request request, String body, MediaType mediaType) throws Exception {
    JsonResponse response;

    if (body != null)
      request.body(body, mediaType);
    response = execute(request);
    if (response.ok())
      return response.text();
    else
      throw new FailedRequestException("Failed to perform " + request.method() + " request: " + request.url(), response);
  }

  /**
   * Call to retrieve text.
   *
   * @param request 	the request to execute
   * @param body 	the body, ignored if null
   * @param mediaType 	the media type, ignored if body is null
   * @return		the raw text
   * @throws Exception	if request fails
   */
  protected String plain(Request request, byte[] body, MediaType mediaType) throws Exception {
    JsonResponse	response;

    if (body != null)
      request.body(body, mediaType);
    response = execute(request);
    if (response.ok())
      return response.text();
    else
      throw new FailedRequestException("Failed to perform " + request.method() + " request: " + request.url(), response);
  }

  /**
   * Call to retrieve binary data.
   *
   * @param request 	the request to execute
   * @param body 	the body, ignored if null
   * @param mediaType 	the media type, ignored if body is null
   * @return		the binary data
   * @throws Exception	if request fails
   */
  protected byte[] binary(Request request, String body, MediaType mediaType) throws Exception {
    JsonResponse	response;

    if (body != null)
      request.body(body, mediaType);
    response = execute(request);
    if (response.ok())
      return response.body();
    else
      throw new FailedRequestException("Failed to perform " + request.method() + " request: " + request.url(), response);
  }

  /**
   * Call to retrieve binary data.
   *
   * @param request 	the request to execute
   * @param body 	the body, ignored if null
   * @param mediaType 	the media type, ignored if body is null
   * @return		the binary data
   * @throws Exception	if request fails
   */
  protected byte[] binary(Request request, byte[] body, MediaType mediaType) throws Exception {
    JsonResponse	response;

    if (body != null)
      request.body(body, mediaType);
    response = execute(request);
    if (response.ok())
      return response.body();
    else
      throw new FailedRequestException("Failed to perform " + request.method() + " request: " + request.url(), response);
  }

  /**
   * Simple GET call to retrieve text.
   *
   * @param path	the path to use (eg /v1/...)
   * @return		the raw text
   * @throws Exception	if request fails
   */
  public String get(String path) throws Exception {
    return plain(newGet(path), (String) null, null);
  }

  /**
   * Simple GET call to retrieve binary data.
   *
   * @param path	the path to use (eg /v1/...)
   * @return		the binary data
   * @throws Exception	if request fails
   */
  public byte[] getBinary(String path) throws Exception {
    return binary(newGet(path), (String) null, null);
  }

  /**
   * Simple POST call to retrieve text.
   *
   * @param path	the path to use (eg /v1/...)
   * @param body	the body to post, ignored if null
   * @param mediaType 	the media type of the body
   * @return		the raw text
   * @throws Exception	if request fails
   */
  public String post(String path, String body, MediaType mediaType) throws Exception {
    return plain(newPost(path), body, mediaType);
  }

  /**
   * Simple POST call to retrieve text.
   *
   * @param path	the path to use (eg /v1/...)
   * @param body	the body to post, ignored if null
   * @param mediaType 	the media type of the body
   * @return		the raw text
   * @throws Exception	if request fails
   */
  public String post(String path, byte[] body, MediaType mediaType) throws Exception {
    return plain(newPost(path), body, mediaType);
  }

  /**
   * Simple POST call to retrieve binary data.
   *
   * @param path	the path to use (eg /v1/...)
   * @param body	the body to post
   * @param mediaType 	the media type of the body
   * @return		the binary data
   * @throws Exception	if request fails
   */
  public byte[] postBinary(String path, String body, MediaType mediaType) throws Exception {
    return binary(newPost(path), body, mediaType);
  }

  /**
   * Simple POST call to retrieve binary data.
   *
   * @param path	the path to use (eg /v1/...)
   * @param body	the body to post
   * @param mediaType 	the media type of the body
   * @return		the binary data
   * @throws Exception	if request fails
   */
  public byte[] postBinary(String path, byte[] body, MediaType mediaType) throws Exception {
    return binary(newPost(path), body, mediaType);
  }

  /**
   * Simple PUT call to retrieve text.
   *
   * @param path	the path to use (eg /v1/...)
   * @param body	the body to post, ignored if null
   * @param mediaType 	the media type of the body
   * @return		the raw text
   * @throws Exception	if request fails
   */
  public String put(String path, String body, MediaType mediaType) throws Exception {
    return plain(newPut(path), body, mediaType);
  }

  /**
   * Simple PUT call to retrieve text.
   *
   * @param path	the path to use (eg /v1/...)
   * @param body	the body to post, ignored if null
   * @param mediaType 	the media type of the body
   * @return		the raw text
   * @throws Exception	if request fails
   */
  public String put(String path, byte[] body, MediaType mediaType) throws Exception {
    return plain(newPut(path), body, mediaType);
  }

  /**
   * Simple PUT call to retrieve binary data.
   *
   * @param path	the path to use (eg /v1/...)
   * @param body	the body to post
   * @param mediaType 	the media type of the body
   * @return		the binary data
   * @throws Exception	if request fails
   */
  public byte[] putBinary(String path, String body, MediaType mediaType) throws Exception {
    return binary(newPut(path), body, mediaType);
  }

  /**
   * Simple PUT call to retrieve binary data.
   *
   * @param path	the path to use (eg /v1/...)
   * @param body	the body to post
   * @param mediaType 	the media type of the body
   * @return		the binary data
   * @throws Exception	if request fails
   */
  public byte[] putBinary(String path, byte[] body, MediaType mediaType) throws Exception {
    return binary(newPut(path), body, mediaType);
  }

  /**
   * Simple PATCH call to retrieve text.
   *
   * @param path	the path to use (eg /v1/...)
   * @param body	the body to post, ignored if null
   * @param mediaType 	the media type of the body
   * @return		the raw text
   * @throws Exception	if request fails
   */
  public String patch(String path, String body, MediaType mediaType) throws Exception {
    return plain(newPatch(path), body, mediaType);
  }

  /**
   * Simple PATCH call to retrieve text.
   *
   * @param path	the path to use (eg /v1/...)
   * @param body	the body to post, ignored if null
   * @param mediaType 	the media type of the body
   * @return		the raw text
   * @throws Exception	if request fails
   */
  public String patch(String path, byte[] body, MediaType mediaType) throws Exception {
    return plain(newPatch(path), body, mediaType);
  }

  /**
   * Simple PATCH call to retrieve binary data.
   *
   * @param path	the path to use (eg /v1/...)
   * @param body	the body to post
   * @param mediaType 	the media type of the body
   * @return		the binary data
   * @throws Exception	if request fails
   */
  public byte[] patchBinary(String path, String body, MediaType mediaType) throws Exception {
    return binary(newPatch(path), body, mediaType);
  }

  /**
   * Simple PATCH call to retrieve binary data.
   *
   * @param path	the path to use (eg /v1/...)
   * @param body	the body to post
   * @param mediaType 	the media type of the body
   * @return		the binary data
   * @throws Exception	if request fails
   */
  public byte[] patchBinary(String path, byte[] body, MediaType mediaType) throws Exception {
    return binary(newPatch(path), body, mediaType);
  }

  /**
   * Simple DELETE call.
   *
   * @param path	the path to use (eg /v1/...)
   * @return		the raw text
   * @throws Exception	if request fails
   */
  public String delete(String path) throws Exception {
    return plain(newDelete(path), (String) null, null);
  }

  /**
   * Simple HEAD call.
   *
   * @param path	the path to use (eg /v1/...)
   * @return		the raw text
   * @throws Exception	if request fails
   */
  public String head(String path) throws Exception {
    return plain(newHead(path), (String) null, null);
  }

  /**
   * Simple OPTIONS call.
   *
   * @param path	the path to use (eg /v1/...)
   * @return		the raw text
   * @throws Exception	if request fails
   */
  public String options(String path) throws Exception {
    return plain(newOptions(path), (String) null, null);
  }
}
