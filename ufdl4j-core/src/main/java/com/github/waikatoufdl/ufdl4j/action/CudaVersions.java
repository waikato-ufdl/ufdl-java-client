/*
 * CudaVersions.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapper;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;
import com.github.waikatoufdl.ufdl4j.filter.Filter;
import com.github.waikatoufdl.ufdl4j.filter.VersionFilter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.entity.ContentType;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates cuda version operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class CudaVersions
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  /**
   * Container class for cuda version information.
   */
  public static class CudaVersion
    extends AbstractJsonObjectWrapper {

    private static final long serialVersionUID = 3523630902439390574L;

    /**
     * Initializes the cuda version.
     *
     * @param data	the data to use
     */
    public CudaVersion(JsonObject data) {
      super(data);
    }

    /**
     * Returns the cuda version primary key.
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
    public String getVersion() {
      return getString("version");
    }

    /**
     * Returns the full version.
     *
     * @return		the version
     */
    public String getFullVersion() {
      return getString("full_version");
    }

    /**
     * Returns the minimum driver version.
     *
     * @return		the version
     */
    public String getMinDriverVersion() {
      return getString("min_driver_version");
    }

    /**
     * Returns a short description of the state.
     *
     * @return		the state
     */
    @Override
    public String toString() {
      return "pk=" + getPK() + ", version=" + getVersion() + ", driver=" + getMinDriverVersion();
    }
  }

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "CudaVersions";
  }

  /**
   * The URL path to use.
   *
   * @return		the path
   */
  @Override
  public String getPath() {
    return "/v1/core/cuda/";
  }

  /**
   * For listing the cuda versions.
   *
   * @return		the list of cuda versions
   * @throws Exception	if request fails
   */
  public List<CudaVersion> list() throws Exception {
    return list(null);
  }

  /**
   * For listing the cuda versions.
   *
   * @param filter 	the filter to apply, can be null
   * @return		the list of cuda versions
   * @throws Exception	if request fails
   */
  public List<CudaVersion> list(Filter filter) throws Exception {
    List<CudaVersion>	result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing cuda versions" + (filter == null ? "" : ", filter: " + filter.toJsonObject()));

    result   = new ArrayList<>();
    request  = newGet(getPath());
    if (filter != null)
      request.body(filter.toJsonObject().toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonArray()) {
        array = element.getAsJsonArray();
        for (i = 0; i < array.size(); i++)
          result.add(new CudaVersion(array.get(i).getAsJsonObject()));
      }
    }
    else {
      throw new FailedRequestException("Failed to list cuda versions!" + (filter == null ? "" : "\nFilter: " + filter.toJsonObject()), response);
    }

    return result;
  }

  /**
   * For loading a specific cuda version by primary key.
   *
   * @param pk 		the primary key of the cuda version to load
   * @return		the cuda version
   * @throws Exception	if request fails
   */
  public CudaVersion load(int pk) throws Exception {
    CudaVersion		result;
    JsonResponse 	response;
    JsonElement		element;
    Request 		request;

    getLogger().info("loading cuda version with id: " + pk);

    result   = null;
    request  = newGet(getPath() + pk);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new CudaVersion(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to load cuda version: " + pk, response);
    }

    return result;
  }

  /**
   * For loading a specific cuda version by version.
   *
   * @param version 	the version
   * @return		the cuda version object, null if failed to locate
   * @throws Exception	if request fails
   */
  public CudaVersion load(String version) throws Exception {
    CudaVersion	result;

    getLogger().info("loading cuda version: " + version);

    result = null;

    for (CudaVersion cuda : list(new VersionFilter(version))) {
      result = cuda;
      break;
    }

    if (result == null)
      getLogger().warning("failed to load cuda version: " + version);

    return result;
  }

  /**
   * Creates a cuda version object.
   *
   * @param version 	the version
   * @param fullVersion	the full version
   * @param minDriverVersion 	the minimum driver version
   * @return		the CudaVersion object, null if failed to create
   * @throws Exception	if request fails or cuda version already exists
   */
  public CudaVersion create(String version, String fullVersion, String minDriverVersion) throws Exception {
    CudaVersion		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("creating cuda version: " + version);

    data = new JsonObject();
    data.addProperty("version", version);
    data.addProperty("full_version", fullVersion);
    data.addProperty("min_driver_version", minDriverVersion);
    request = newPost(getPath())
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new CudaVersion(response.jsonObject());
    else
      throw new FailedRequestException("Failed to create cuda version: " + version, response);

    return result;
  }

  /**
   * Updates the cuda version.
   *
   * @param obj 	the cuda version to update
   * @param version 	the new cuda version
   * @param fullVersion	the new full version
   * @param minDriverVersion 	the new minimum driver version
   * @return		the cuda version object
   * @throws Exception	if request fails
   */
  public CudaVersion update(CudaVersion obj, String version, String fullVersion, String minDriverVersion) throws Exception {
    return update(obj.getPK(), version, fullVersion, minDriverVersion);
  }

  /**
   * Updates the cuda version.
   *
   * @param pk 		the PK of the cuda version to update
   * @param version 	the new cuda version name
   * @param fullVersion	the new full version
   * @param minDriverVersion 	the new minimum driver version
   * @return		the cuda version object
   * @throws Exception	if request fails
   */
  public CudaVersion update(int pk, String version, String fullVersion, String minDriverVersion) throws Exception {
    CudaVersion		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("updating cuda version: " + pk);

    data = new JsonObject();
    data.addProperty("version", version);
    data.addProperty("full_version", fullVersion);
    data.addProperty("min_driver_version", minDriverVersion);
    request = newPut(getPath() + pk)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new CudaVersion(response.jsonObject());
    else
      throw new FailedRequestException("Failed to update cuda version: " + pk, response);

    return result;
  }

  /**
   * (Partially) Updates the cuda version identified by the object, using only the non-null arguments.
   *
   * @param obj	the user to update
   * @param version 	the new cuda version name, ignored if null
   * @param fullVersion	the new full version, ignored if null
   * @param minDriverVersion 	the new minimum driver version, ignored if null
   * @return		the user object, null if failed to create
   * @throws Exception	if request fails
   */
  public CudaVersion partialUpdate(CudaVersion obj, String version, String fullVersion, String minDriverVersion) throws Exception {
    return partialUpdate(obj.getPK(), version, fullVersion, minDriverVersion);
  }

  /**
   * (Partially) Updates the cuda version identified by the PK, using only the non-null arguments.
   *
   * @param pk 		the PK of the user to update
   * @param version 	the new cuda version name, ignored if null
   * @param fullVersion	the new full version, ignored if null
   * @param minDriverVersion 	the new minimum driver version, ignored if null
   * @return		the cuda version object, null if failed to create
   * @throws Exception	if request fails
   */
  public CudaVersion partialUpdate(int pk, String version, String fullVersion, String minDriverVersion) throws Exception {
    CudaVersion		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("partially updating cuda version: " + pk);

    data = new JsonObject();
    if (version != null)
      data.addProperty("version", version);
    if (fullVersion != null)
      data.addProperty("full_version", fullVersion);
    if (minDriverVersion != null)
      data.addProperty("min_driver_version", minDriverVersion);
    request = newPatch(getPath() + pk)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new CudaVersion(response.jsonObject());
    else
      throw new FailedRequestException("Failed to partially update cuda version: " + pk, response);

    return result;
  }

  /**
   * For deleting a specific cuda version.
   *
   * @param cudaVersion the cuda version to delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid cuda version PK
   */
  public boolean delete(CudaVersion cudaVersion) throws Exception {
    return delete(cudaVersion.getPK());
  }

  /**
   * For deleting a specific cuda version.
   *
   * @param pk 		the ID of the cuda version
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid cuda version PK
   */
  public boolean delete(int pk) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("deleting cuda version with PK: " + pk);

    request  = newDelete(getPath() + pk + "/");
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete cuda version: " + pk, response);
  }
}
