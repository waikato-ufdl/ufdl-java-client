/*
 * JobTypes.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.core.MediaTypeHelper;
import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapperWithPK;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonObjectWithShortDescription;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;
import com.github.waikatoufdl.ufdl4j.filter.Filter;
import com.github.waikatoufdl.ufdl4j.filter.NameFilter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates job type operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class JobTypes
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  /**
   * Container class for job type information.
   */
  public static class JobType
    extends AbstractJsonObjectWrapperWithPK
    implements JsonObjectWithShortDescription {

    private static final long serialVersionUID = 3523630902439390574L;

    /**
     * Initializes the job type.
     *
     * @param data	the data to use
     */
    public JobType(JsonObject data) {
      super(data);
    }

    /**
     * Returns the job type primary key.
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
      return getString("name");
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
    return "JobTypes";
  }

  /**
   * The URL path to use.
   *
   * @return		the path
   */
  @Override
  public String getPath() {
    return "/v1/core/job-types/";
  }

  /**
   * For listing the job types.
   *
   * @return		the list of job types
   * @throws Exception	if request fails
   */
  public List<JobType> list() throws Exception {
    return list(null);
  }

  /**
   * For listing the job types.
   *
   * @param filter 	the filter to apply, can be null
   * @return		the list of job types
   * @throws Exception	if request fails
   */
  public List<JobType> list(Filter filter) throws Exception {
    List<JobType>	result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing job types" + (filter == null ? "" : ", filter: " + filter.toJsonObject()));

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
          result.add(new JobType(array.get(i).getAsJsonObject()));
      }
    }
    else {
      throw new FailedRequestException("Failed to list job types!" + (filter == null ? "" : "\nFilter: " + filter.toJsonObject()), response);
    }

    return result;
  }

  /**
   * For loading a specific job type by primary key.
   *
   * @param pk 		the primary key of the job type to load
   * @return		the job type
   * @throws Exception	if request fails
   */
  public JobType load(int pk) throws Exception {
    JobType		result;
    JsonResponse 	response;
    JsonElement		element;
    Request 		request;

    getLogger().info("loading job type with id: " + pk);

    result   = null;
    request  = newGet(getPath() + pk);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new JobType(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to load job type: " + pk, response);
    }

    return result;
  }

  /**
   * For loading a specific job type by name.
   *
   * @param name 	the name
   * @return		the job type object, null if failed to locate
   * @throws Exception	if request fails
   */
  public JobType load(String name) throws Exception {
    JobType	result;

    getLogger().info("loading job type: " + name);

    result = null;

    for (JobType type : list(new NameFilter(name))) {
      result = type;
      break;
    }

    if (result == null)
      getLogger().warning("failed to load job type: " + name);

    return result;
  }

  /**
   * Creates a job type object.
   *
   * @param name 	the name
   * @return		the JobType object, null if failed to create
   * @throws Exception	if request fails or job type already exists
   */
  public JobType create(String name) throws Exception {
    JobType		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("creating job type: " + name);

    data = new JsonObject();
    data.addProperty("name", name);
    request = newPost(getPath() + "create")
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok())
      result = new JobType(response.jsonObject());
    else
      throw new FailedRequestException("Failed to create job type: " + name, response);

    return result;
  }

  /**
   * Updates the job type.
   *
   * @param obj 	the job type to update
   * @param name 	the new name
   * @return		the job type object
   * @throws Exception	if request fails
   */
  public JobType update(JobType obj, String name) throws Exception {
    return update(obj.getPK(), name);
  }

  /**
   * Updates the job type.
   *
   * @param pk 		the PK of the job type to update
   * @param name 	the new name
   * @return		the job type object
   * @throws Exception	if request fails
   */
  public JobType update(int pk, String name) throws Exception {
    JobType		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("updating job type: " + pk);

    data = new JsonObject();
    data.addProperty("name", name);
    request = newPut(getPath() + pk)
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok())
      result = new JobType(response.jsonObject());
    else
      throw new FailedRequestException("Failed to update job type: " + pk, response);

    return result;
  }

  /**
   * (Partially) Updates the job type identified by the object, using only the non-null arguments.
   *
   * @param obj		the type to update
   * @param name 	the new name, ignored if null
   * @return		the user object, null if failed to create
   * @throws Exception	if request fails
   */
  public JobType partialUpdate(JobType obj, String name) throws Exception {
    return partialUpdate(obj.getPK(), name);
  }

  /**
   * (Partially) Updates the job type identified by the PK, using only the non-null arguments.
   *
   * @param pk 		the PK of the user to update
   * @param name 	the new name, ignored if null
   * @return		the user object, null if failed to create
   * @throws Exception	if request fails
   */
  public JobType partialUpdate(int pk, String name) throws Exception {
    JobType		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("partially updating job type: " + pk);

    data = new JsonObject();
    if (name != null)
      data.addProperty("name", name);
    request = newPatch(getPath() + pk)
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok())
      result = new JobType(response.jsonObject());
    else
      throw new FailedRequestException("Failed to partially update job type: " + pk, response);

    return result;
  }

  /**
   * For deleting a specific job type.
   *
   * @param jobType 	the job type to delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid job type PK
   */
  public boolean delete(JobType jobType) throws Exception {
    return delete(jobType.getPK());
  }

  /**
   * For deleting a specific job type.
   *
   * @param pk 		the ID of the job type
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid job type PK
   */
  public boolean delete(int pk) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("deleting job type with PK: " + pk);

    request  = newDelete(getPath() + pk + "/");
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete job type: " + pk, response);
  }
}
