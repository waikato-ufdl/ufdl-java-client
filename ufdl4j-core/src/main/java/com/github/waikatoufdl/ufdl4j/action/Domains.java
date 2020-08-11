/*
 * Domain.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapper;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates domain operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Domains
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  /**
   * Container class for log entry information.
   */
  public static class Domain
    extends AbstractJsonObjectWrapper {

    private static final long serialVersionUID = 3523630902439390574L;

    /**
     * Initializes the user.
     *
     * @param data	the data to use
     */
    public Domain(JsonObject data) {
      super(data);
    }

    /**
     * Returns the user primary key.
     *
     * @return		the primary key
     */
    public int getPK() {
      return getInt("pk");
    }

    /**
     * Returns the message.
     *
     * @return		the message
     */
    public String getName() {
      return getString("name", "");
    }

    /**
     * Returns a short description of the state.
     *
     * @return		the state
     */
    @Override
    public String toString() {
      return "pk=" + getPK() + ", name=" + getName();
    }
  }

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "Domains";
  }

  /**
   * The URL path to use.
   *
   * @return		the path
   */
  @Override
  public String getPath() {
    return "/v1/core/domains/";
  }

  /**
   * For listing the domains.
   *
   * @return		the list of users
   * @throws Exception	if request fails
   */
  public List<Domain> list() throws Exception {
    List<Domain>	result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing domains");

    result   = new ArrayList<>();
    request  = newGet(getPath());
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonArray()) {
        array = element.getAsJsonArray();
        for (i = 0; i < array.size(); i++)
          result.add(new Domain(array.get(i).getAsJsonObject()));
      }
    }
    else {
      throw new FailedRequestException("Failed to list domains!", response);
    }

    return result;
  }

  /**
   * For loading a specific domain by primary key.
   *
   * @param pk 		the primary key of the domain to load
   * @return		the entry
   * @throws Exception	if request fails
   */
  public Domain load(int pk) throws Exception {
    Domain result;
    JsonResponse 	response;
    JsonElement		element;
    Request 		request;

    getLogger().info("loading domain with id: " + pk);

    result   = null;
    request  = newGet(getPath() + pk);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new Domain(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to load domain: " + pk, response);
    }

    return result;
  }
}