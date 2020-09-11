/*
 * CudaVersions.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.core.MediaTypeHelper;
import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapperWithPK;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonObjectWithShortDescription;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;
import com.github.waikatoufdl.ufdl4j.filter.AbstractExpression;
import com.github.waikatoufdl.ufdl4j.filter.Filter;
import com.github.waikatoufdl.ufdl4j.filter.GenericFilter;
import com.github.waikatoufdl.ufdl4j.filter.field.ExactString;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates hardware generation operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class HardwareGenerations
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  /**
   * Container class for hardware generation information.
   */
  public static class HardwareGeneration
    extends AbstractJsonObjectWrapperWithPK
    implements JsonObjectWithShortDescription {

    private static final long serialVersionUID = 3523630902439390574L;

    /**
     * Initializes the hardware generation.
     *
     * @param data	the data to use
     */
    public HardwareGeneration(JsonObject data) {
      super(data);
    }

    /**
     * Returns the hardware generation primary key.
     *
     * @return		the primary key
     */
    public int getPK() {
      return getInt("pk");
    }

    /**
     * Returns the version.
     *
     * @return		the version
     */
    public String getGeneration() {
      return getString("generation");
    }

    /**
     * Returns the minimum compute capability.
     *
     * @return		the capability
     */
    public Double getMinComputeCapability() {
      return getDouble("min_compute_capability");
    }

    /**
     * Returns the maximum compute capability.
     *
     * @return		the capability
     */
    public Double getMaxComputeCapability() {
      return getDouble("max_compute_capability");
    }

    /**
     * Returns the short description.
     *
     * @return		the short description
     */
    @Override
    public String getShortDescription() {
      return getGeneration();
    }

    /**
     * Returns a short description of the state.
     *
     * @return		the state
     */
    @Override
    public String toString() {
      return "pk=" + getPK() + ", generation=" + getGeneration() + ", min=" + getMinComputeCapability() + ", max=" + getMaxComputeCapability();
    }
  }

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "Hardware Generations";
  }

  /**
   * The URL path to use.
   *
   * @return		the path
   */
  @Override
  public String getPath() {
    return "/v1/hardware/";
  }

  /**
   * For listing the hardware generations.
   *
   * @return		the list of hardware generations
   * @throws Exception	if request fails
   */
  public List<HardwareGeneration> list() throws Exception {
    return list(null);
  }

  /**
   * For listing the hardware generations.
   *
   * @param filter 	the filter to apply, can be null
   * @return		the list of hardware generations
   * @throws Exception	if request fails
   */
  public List<HardwareGeneration> list(Filter filter) throws Exception {
    List<HardwareGeneration>		result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing hardware generations" + (filter == null ? "" : ", filter: " + filter.toJsonObject()));

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
          result.add(new HardwareGeneration(array.get(i).getAsJsonObject()));
      }
    }
    else {
      throw new FailedRequestException("Failed to list hardware generations!" + (filter == null ? "" : "\nFilter: " + filter.toJsonObject()), response);
    }

    return result;
  }

  /**
   * For loading a specific hardware generation by primary key.
   *
   * @param pk 		the primary key of the hardware generation to load
   * @return		the hardware generation
   * @throws Exception	if request fails
   */
  public HardwareGeneration load(int pk) throws Exception {
    HardwareGeneration result;
    JsonResponse 	response;
    JsonElement		element;
    Request 		request;

    getLogger().info("loading hardware generation with id: " + pk);

    result   = null;
    request  = newGet(getPath() + pk);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new HardwareGeneration(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to load hardware generation: " + pk, response);
    }

    return result;
  }

  /**
   * For loading a specific hardware generation by name.
   *
   * @param generation 	the name
   * @return		the hardware generation object, null if failed to locate
   * @throws Exception	if request fails
   */
  public HardwareGeneration load(String generation) throws Exception {
    HardwareGeneration 	result;
    Filter		filter;

    getLogger().info("loading hardware generation: " + generation);

    result = null;

    filter = new GenericFilter(
      new AbstractExpression[]{
        new ExactString("generation", generation),
      }
    );
    for (HardwareGeneration gen : list(filter)) {
      result = gen;
      break;
    }

    if (result == null)
      getLogger().warning("failed to load hardware generation: " + generation);

    return result;
  }

  /**
   * Creates a hardware generation object.
   *
   * @param generation 	the name
   * @param min		the minimum compute capability (incl)
   * @param max 	the maximum compute capability (excl)
   * @return		the CudaVersion object, null if failed to create
   * @throws Exception	if request fails or hardware generation already exists
   */
  public HardwareGeneration create(String generation, Double min, Double max) throws Exception {
    HardwareGeneration result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("creating hardware generation: " + generation);

    data = new JsonObject();
    data.addProperty("generation", generation);
    data.addProperty("min_compute_capability", min);
    data.addProperty("max_compute_capability", max);
    request = newPost(getPath() + "create")
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok())
      result = new HardwareGeneration(response.jsonObject());
    else
      throw new FailedRequestException("Failed to create hardware generation: " + generation, response);

    return result;
  }

  /**
   * Updates the hardware generation.
   *
   * @param obj 	the hardware generation to update
   * @param generation 	the new hardware generation
   * @param min		the new minimum compute capability (incl)
   * @param max 	the new maximum compute capability (excl)
   * @return		the hardware generation object
   * @throws Exception	if request fails
   */
  public HardwareGeneration update(HardwareGeneration obj, String generation, Double min, Double max) throws Exception {
    return update(obj.getPK(), generation, min, max);
  }

  /**
   * Updates the hardware generation.
   *
   * @param pk 		the PK of the hardware generation to update
   * @param generation 	the new hardware generation
   * @param min		the new minimum compute capability (incl)
   * @param max 	the new maximum compute capability (excl)
   * @return		the hardware generation object
   * @throws Exception	if request fails
   */
  public HardwareGeneration update(int pk, String generation, Double min, Double max) throws Exception {
    HardwareGeneration result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("updating hardware generation: " + pk);

    data = new JsonObject();
    data.addProperty("generation", generation);
    data.addProperty("min_compute_capability", min);
    data.addProperty("max_compute_capability", max);
    request = newPut(getPath() + pk)
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok())
      result = new HardwareGeneration(response.jsonObject());
    else
      throw new FailedRequestException("Failed to update hardware generation: " + pk, response);

    return result;
  }

  /**
   * (Partially) Updates the hardware generation identified by the object, using only the non-null arguments.
   *
   * @param obj	the user to update
   * @param generation 	the new hardware generation, ignored if null
   * @param min		the new minimum compute capability (incl), ignored if null
   * @param max 	the new maximum compute capability (excl), ignored if null
   * @return		the user object, null if failed to create
   * @throws Exception	if request fails
   */
  public HardwareGeneration partialUpdate(HardwareGeneration obj, String generation, Double min, Double max) throws Exception {
    return partialUpdate(obj.getPK(), generation, min, max);
  }

  /**
   * (Partially) Updates the hardware generation identified by the PK, using only the non-null arguments.
   *
   * @param pk 		the PK of the user to update
   * @param generation 	the new hardware generation name, ignored if null
   * @param min	the new full version, ignored if null
   * @param max 	the new minimum driver version, ignored if null
   * @return		the user object, null if failed to create
   * @throws Exception	if request fails
   */
  public HardwareGeneration partialUpdate(int pk, String generation, Double min, Double max) throws Exception {
    HardwareGeneration result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("partially updating hardware generation: " + pk);

    data = new JsonObject();
    if (generation != null)
      data.addProperty("generation", generation);
    if (min != null)
      data.addProperty("min_compute_capability", min);
    if (max != null)
      data.addProperty("max_compute_capability", max);
    request = newPatch(getPath() + pk)
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok())
      result = new HardwareGeneration(response.jsonObject());
    else
      throw new FailedRequestException("Failed to partially update hardware generation: " + pk, response);

    return result;
  }

  /**
   * For deleting a specific hardware generation.
   *
   * @param hardwareGeneration the hardware generation to delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid hardware generation PK
   */
  public boolean delete(HardwareGeneration hardwareGeneration) throws Exception {
    return delete(hardwareGeneration.getPK());
  }

  /**
   * For deleting a specific hardware generation.
   *
   * @param pk 		the ID of the hardware generation
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid hardware generation PK
   */
  public boolean delete(int pk) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("deleting hardware generation with PK: " + pk);

    request  = newDelete(getPath() + pk);
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete hardware generation: " + pk, response);
  }
}
