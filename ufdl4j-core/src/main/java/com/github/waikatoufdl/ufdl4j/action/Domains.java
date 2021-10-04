/*
 * Domain.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.core.MediaTypeHelper;
import com.github.fracpete.requests4j.request.Request;
import com.github.fracpete.requests4j.response.JsonResponse;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapperWithPK;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonObjectWithShortDescription;
import com.github.waikatoufdl.ufdl4j.filter.Filter;
import com.github.waikatoufdl.ufdl4j.filter.NameFilter;
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
    extends AbstractJsonObjectWrapperWithPK
    implements JsonObjectWithShortDescription {

    private static final long serialVersionUID = 3523630902439390574L;

    /**
     * Initializes the domain.
     *
     * @param data	the data to use
     */
    public Domain(JsonObject data) {
      super(data);
    }

    /**
     * Returns the primary key.
     *
     * @return		the primary key
     */
    public int getPK() {
      return getInt("pk");
    }

    /**
     * Returns the name.
     *
     * @return		the name
     */
    public String getName() {
      return getString("name", "");
    }

    /**
     * Returns the description.
     *
     * @return		the description
     */
    public String getDescription() {
      return getString("description", "");
    }

    /**
     * Returns the short description.
     *
     * @return		the short description
     */
    @Override
    public String getShortDescription() {
      return getName();
    }

    /**
     * Returns a short description of the state.
     *
     * @return		the state
     */
    @Override
    public String toString() {
      return "pk=" + getPK() + ", name=" + getName() + ", description=" + getDescription();
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
    return "/v1/domains/";
  }

  /**
   * For listing the domains.
   *
   * @return		the list of domains
   * @throws Exception	if request fails
   */
  public List<Domain> list() throws Exception {
    return list(null);
  }

  /**
   * For listing the domains.
   *
   * @param filter 	the filter to apply, can be null
   * @return		the list of domains
   * @throws Exception	if request fails
   */
  public List<Domain> list(Filter filter) throws Exception {
    List<Domain>	result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing domains" + (filter == null ? "" : ", filter: " + filter.toJsonObject()));

    result   = new ArrayList<>();
    request  = newPost(getPath() + "list");
    if (filter != null)
      request.body(filter.toJsonObject().toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
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
      throw new FailedRequestException("Failed to list domains!" + (filter == null ? "" : "\nFilter: " + filter.toJsonObject()), response);
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

  /**
   * For loading a specific license by name.
   *
   * @param name 	the license name
   * @return		the license object, null if failed to create
   * @throws Exception	if request fails
   */
  public Domain load(String name) throws Exception {
    Domain	result;

    getLogger().info("loading domain with name: " + name);

    result = null;

    for (Domain domain : list(new NameFilter(name))) {
      result = domain;
      break;
    }

    if (result == null)
      getLogger().warning("failed to load domain: " + name);

    return result;
  }
}
