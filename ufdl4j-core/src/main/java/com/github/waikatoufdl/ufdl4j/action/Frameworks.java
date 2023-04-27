/*
 * Frameworks.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.core.MediaTypeHelper;
import com.github.fracpete.requests4j.request.Request;
import com.github.fracpete.requests4j.response.JsonResponse;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapperWithPK;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonObjectWithName;
import com.github.waikatoufdl.ufdl4j.core.JsonObjectWithShortDescription;
import com.github.waikatoufdl.ufdl4j.core.JsonObjectWithVersion;
import com.github.waikatoufdl.ufdl4j.filter.Filter;
import com.github.waikatoufdl.ufdl4j.filter.NameAndVersionFilter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates framework operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Frameworks
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  /**
   * Container class for framework information.
   */
  public static class Framework
    extends AbstractJsonObjectWrapperWithPK
    implements JsonObjectWithShortDescription, JsonObjectWithName, JsonObjectWithVersion {

    private static final long serialVersionUID = 3523630902439390574L;

    /**
     * Initializes the framework.
     *
     * @param data	the data to use
     */
    public Framework(JsonObject data) {
      super(data);
    }

    /**
     * Returns the framework primary key.
     *
     * @return		the primary key
     */
    @Override
    public int getPK() {
      return getInt("pk");
    }

    /**
     * Returns the name.
     *
     * @return		the name
     */
    @Override
    public String getName() {
      return getString("name");
    }

    /**
     * Returns the version.
     *
     * @return		the version
     */
    @Override
    public String getVersion() {
      return getString("version");
    }

    /**
     * Returns the short description.
     *
     * @return		the short description
     */
    @Override
    public String getShortDescription() {
      return getName() + "/" + getVersion();
    }

    /**
     * Returns a short description of the state.
     *
     * @return		the state
     */
    @Override
    public String toString() {
      return "pk=" + getPK() + ", name=" + getName() + ", version=" + getVersion();
    }
  }

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "Frameworks";
  }

  /**
   * The URL path to use.
   *
   * @return		the path
   */
  @Override
  public String getPath() {
    return "/v1/frameworks/";
  }

  /**
   * For listing the frameworks.
   *
   * @return		the list of frameworks
   * @throws Exception	if request fails
   */
  public List<Framework> list() throws Exception {
    return list(null);
  }

  /**
   * For listing the frameworks.
   *
   * @param filter 	the filter to apply, can be null
   * @return		the list of frameworks
   * @throws Exception	if request fails
   */
  public List<Framework> list(Filter filter) throws Exception {
    List<Framework>		result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing frameworks" + (filter == null ? "" : ", filter: " + filter.toJsonObject()));

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
          result.add(new Framework(array.get(i).getAsJsonObject()));
      }
    }
    else {
      throw new FailedRequestException("Failed to list frameworks!" + (filter == null ? "" : "\nFilter: " + filter.toJsonObject()), response);
    }

    return result;
  }

  /**
   * For loading a specific framework by primary key.
   *
   * @param pk 		the primary key of the framework to load
   * @return		the framework
   * @throws Exception	if request fails
   */
  public Framework load(int pk) throws Exception {
    Framework		result;
    JsonResponse 	response;
    JsonElement		element;
    Request 		request;

    getLogger().info("loading framework with id: " + pk);

    result   = null;
    request  = newGet(getPath() + pk);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
        result = new Framework(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to load framework: " + pk, response);
    }

    return result;
  }

  /**
   * For loading a specific framework by name.
   *
   * @param name 	the name
   * @return		the (first matching) framework object, null if failed to locate
   * @throws Exception	if request fails
   */
  public Framework load(String name, String version) throws Exception {
    Framework	result;

    getLogger().info("loading framework: " + name + "/" + version);

    result = null;

    for (Framework framework : list(new NameAndVersionFilter(name, version))) {
      result = framework;
      break;
    }

    if (result == null)
      getLogger().warning("failed to load framework: " + name + "/" + version);

    return result;
  }

  /**
   * Creates a framework object.
   *
   * @param name	the name
   * @param version 	the version
   * @return		the Framework object, null if failed to create
   * @throws Exception	if request fails or framework already exists
   */
  public Framework create(String name, String version) throws Exception {
    Framework		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("creating framework: " + version);

    data = new JsonObject();
    data.addProperty("name", name);
    data.addProperty("version", version);
    request = newPost(getPath() + "create")
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok())
      result = new Framework(response.jsonObject());
    else
      throw new FailedRequestException("Failed to create framework: " + version, response);

    return result;
  }

  /**
   * Updates the framework.
   *
   * @param obj 	the framework to update
   * @param name	the new name
   * @param version 	the new framework
   * @return		the framework object
   * @throws Exception	if request fails
   */
  public Framework update(Framework obj, String name, String version) throws Exception {
    return update(obj.getPK(), name, version);
  }

  /**
   * Updates the framework.
   *
   * @param pk 		the PK of the framework to update
   * @param name	the new name
   * @param version 	the new framework name
   * @return		the framework object
   * @throws Exception	if request fails
   */
  public Framework update(int pk, String name, String version) throws Exception {
    Framework		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("updating framework: " + pk);

    data = new JsonObject();
    data.addProperty("name", name);
    data.addProperty("version", version);
    request = newPut(getPath() + pk)
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok())
      result = new Framework(response.jsonObject());
    else
      throw new FailedRequestException("Failed to update framework: " + pk, response);

    return result;
  }

  /**
   * (Partially) Updates the framework identified by the object, using only the non-null arguments.
   *
   * @param obj	the user to update
   * @param name 	the new framework name, ignored if null
   * @param version	the new name, ignored if null
   * @return		the user object, null if failed to create
   * @throws Exception	if request fails
   */
  public Framework partialUpdate(Framework obj, String name, String version) throws Exception {
    return partialUpdate(obj.getPK(), name, version);
  }

  /**
   * (Partially) Updates the framework identified by the PK, using only the non-null arguments.
   *
   * @param pk 		the PK of the user to update
   * @param name 	the new framework name, ignored if null
   * @param version	the new full version, ignored if null
   * @return		the user object, null if failed to create
   * @throws Exception	if request fails
   */
  public Framework partialUpdate(int pk, String name, String version) throws Exception {
    Framework		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("partially updating framework: " + pk);

    data = new JsonObject();
    if (name != null)
      data.addProperty("name", name);
    if (version != null)
      data.addProperty("version", version);
    request = newPatch(getPath() + pk)
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok())
      result = new Framework(response.jsonObject());
    else
      throw new FailedRequestException("Failed to partially update framework: " + pk, response);

    return result;
  }

  /**
   * For deleting a specific framework.
   *
   * @param framework the framework to delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid framework PK
   */
  public boolean delete(Framework framework) throws Exception {
    return delete(framework.getPK());
  }

  /**
   * For deleting a specific framework.
   *
   * @param pk 		the ID of the framework
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid framework PK
   */
  public boolean delete(int pk) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("deleting framework with PK: " + pk);

    request  = newDelete(getPath() + pk);
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete framework: " + pk, response);
  }
}
