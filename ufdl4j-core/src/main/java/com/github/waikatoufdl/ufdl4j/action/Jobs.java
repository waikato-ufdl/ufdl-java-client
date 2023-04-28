/*
 * Jobs.java
 * Copyright (C) 2020-2023 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.attachment.ByteArrayAttachment;
import com.github.fracpete.requests4j.attachment.FileAttachment;
import com.github.fracpete.requests4j.core.MediaTypeHelper;
import com.github.fracpete.requests4j.request.Request;
import com.github.fracpete.requests4j.response.BasicResponse;
import com.github.fracpete.requests4j.response.JsonResponse;
import com.github.fracpete.requests4j.response.Response;
import com.github.fracpete.requests4j.response.StreamResponse;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapper;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapperWithPK;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.SoftDeleteObject;
import com.github.waikatoufdl.ufdl4j.filter.Filter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.waikatoufdl.ufdl4j.core.JsonUtils.jsonToMap;

/**
 * Encapsulates job operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Jobs
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  /**
   * Container class for a single job output.
   */
  public static class JobOutput
    extends AbstractJsonObjectWrapper {

    private static final long serialVersionUID = 1081824028559386319L;

    /**
     * Initializes the job output.
     *
     * @param data	the data to use
     */
    public JobOutput(JsonObject data) {
      super(data);
    }

    /**
     * Returns the name of the output.
     *
     * @return		the name
     */
    public String getName() {
      return getString("name");
    }

    /**
     * Returns the type of the output.
     *
     * @return		the type
     */
    public String getType() {
      return getString("type");
    }

    /**
     * Returns a short description of the state.
     *
     * @return		the description
     */
    @Override
    public String toString() {
      return "name=" + getName() + ", type=" + getType();
    }
  }

  /**
   * Container class for job information.
   */
  public static class Job
    extends AbstractJsonObjectWrapperWithPK
    implements SoftDeleteObject {

    private static final long serialVersionUID = 3523630902439390574L;

    /**
     * Initializes the job.
     *
     * @param data	the data to use
     */
    public Job(JsonObject data) {
      super(data);
    }

    /**
     * Returns the job primary key.
     *
     * @return		the primary key
     */
    public int getPK() {
      return getInt("pk");
    }

    /**
     * Returns the job template PK.
     *
     * @return		the template
     */
    public int getJobTemplate() {
      return getData().get("template").getAsJsonObject().get("pk").getAsInt();
    }

    /**
     * Returns the node executing the job.
     *
     * @return		the node ID, -1 if none set
     */
    public int getNode() {
      return getInt("node", -1);
    }

    /**
     * Returns the error encountered.
     *
     * @return		the error, null if none encountered
     */
    public String getError() {
      return getString("error", null);
    }

    /**
     * Returns the input values (key -> value).
     *
     * @return		the values
     */
    public Map<String,Map> getInputValues() {
      Map<String,Map>	result;
      JsonObject			values;

      result = new HashMap<>();
      values = getData().getAsJsonObject("input_values");
      for (String key: values.keySet())
        result.put(key, jsonToMap(values.get(key).getAsJsonObject()));

      return result;
    }

    /**
     * Returns the parameter values (key -> value).
     *
     * @return		the values
     */
    public Map<String,Map> getParameterValues() {
      Map<String,Map>	result;
      JsonObject			values;

      result = new HashMap<>();
      if (getData().has("parameter_values")) {
	values = getData().getAsJsonObject("parameter_values");
	for (String key : values.keySet())
	  result.put(key, jsonToMap(values.get(key).getAsJsonObject()));
      }

      return result;
    }

    /**
     * Returns the outputs, if any.
     *
     * @return		the outputs
     */
    public List<JobOutput> getOutputs() {
      List<JobOutput>	result;
      JsonArray		outputs;
      int		i;

      result = new ArrayList<>();
      if (getData().has("outputs")) {
	outputs = getData().getAsJsonArray("outputs");
	for (i = 0; i < outputs.size(); i++)
	  result.add(new JobOutput(outputs.get(i).getAsJsonObject()));
      }

      return result;
    }

    /**
     * Returns the creator ID.
     *
     * @return		the ID
     */
    public int getCreator() {
      return getInt("creator", -1);
    }

    /**
     * Returns the creation date/time.
     *
     * @return		the date/time, can be null
     */
    public LocalDateTime getCreationTime() {
      return getDateTime("creation_time", null);
    }

    /**
     * Returns the deletion date/time.
     *
     * @return		the date/time, can be null
     */
    public LocalDateTime getDeletionTime() {
      return getDateTime("deletion_time", null);
    }

    /**
     * Returns the start date/time.
     *
     * @return		the date/time, can be null
     */
    public LocalDateTime getStartTime() {
      return getDateTime("start_time", null);
    }

    /**
     * Returns the end date/time.
     *
     * @return		the date/time, can be null
     */
    public LocalDateTime getEndTime() {
      return getDateTime("end_time", null);
    }

    /**
     * Returns the description.
     *
     * @return		the description, can be null
     */
    public String getDescription() {
      return getString("description", null);
    }

    /**
     * Returns a short description of the state.
     *
     * @return		the state
     */
    @Override
    public String toString() {
      return "pk=" + getPK() + ", template=" + getJobTemplate() + ", input=" + getInputValues() + ", params=" + getParameterValues();
    }
  }

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "Jobs";
  }

  /**
   * The URL path to use.
   *
   * @return		the path
   */
  @Override
  public String getPath() {
    return "/v1/jobs/";
  }

  /**
   * For listing the jobs.
   *
   * @return		the list of jobs
   * @throws Exception	if request fails
   */
  public List<Job> list() throws Exception {
    return list(null);
  }

  /**
   * For listing the jobs.
   *
   * @param filter 	the filter to apply, can be null
   * @return		the list of jobs
   * @throws Exception	if request fails
   */
  public List<Job> list(Filter filter) throws Exception {
    List<Job>		result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing jobs" + (filter == null ? "" : ", filter: " + filter.toJsonObject()));

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
          result.add(new Job(array.get(i).getAsJsonObject()));
      }
    }
    else {
      throw new FailedRequestException("Failed to list jobs!" + (filter == null ? "" : "\nFilter: " + filter.toJsonObject()), response);
    }

    return result;
  }

  /**
   * For loading a specific job by primary key.
   *
   * @param pk 		the primary key of the job to load
   * @return		the job
   * @throws Exception	if request fails
   */
  public Job load(int pk) throws Exception {
    Job		result;
    JsonResponse 	response;
    JsonElement		element;
    Request 		request;

    getLogger().info("loading job with id: " + pk);

    result   = null;
    request  = newGet(getPath() + pk);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new Job(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to load job: " + pk, response);
    }

    return result;
  }

  /**
   * Adds the file as an output to the job.
   *
   * @param job 	the job
   * @param file	the file to upload
   * @param name	the name of the output
   * @param type 	the type of the output
   * @return		true if successfully added/uploaded
   * @throws Exception	if request fails, eg invalid job PK
   */
  public boolean addOutput(Job job, String name, String type, File file) throws Exception {
    return addOutput(job.getPK(), name, type, file);
  }

  /**
   * Adds the file as an output to the job.
   *
   * @param pk		the job ID
   * @param file	the file to upload
   * @param name	the name of the output
   * @param type 	the type of the output
   * @return		true if successfully added/uploaded
   * @throws Exception	if request fails, eg invalid job PK
   */
  public boolean addOutput(int pk, String name, String type, File file) throws Exception {
    Request 	request;
    Response response;

    request = newPost(getPath() + pk + "/outputs/" + name + "/" + type)
      .attachment(new FileAttachment(file));
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to add file to job " + pk + " as output '" + name + "/" + type + "': " + file, response);
  }

  /**
   * Adds the byte array as an output to the job.
   *
   * @param job 	the job
   * @param name	the name of the output
   * @param type 	the type of the output
   * @param data	the data to upload
   * @return		true if successfully added/uploaded
   * @throws Exception	if request fails, eg invalid job PK
   */
  public boolean addOutput(Job job, String name, String type, byte[] data) throws Exception {
    return addOutput(job.getPK(), name, type, data);
  }

  /**
   * Adds the byte array as an output to the job.
   *
   * @param pk		the job ID
   * @param name	the name of the output
   * @param type 	the type of the output
   * @param data	the data to upload
   * @return		true if successfully added/uploaded
   * @throws Exception	if request fails, eg invalid job PK
   */
  public boolean addOutput(int pk, String name, String type, byte[] data) throws Exception {
    Request 	request;
    Response response;

    request = newPost(getPath() + pk + "/outputs/" + name + "/" + type)
      .attachment(new ByteArrayAttachment(data));
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to add byte array to job " + pk + " as output '" + name + "/" + type + "'", response);
  }

  /**
   * Downloads the specified output from the job.
   *
   * @param job 	the job to update
   * @param name	the name of the output
   * @param type 	the type of the output
   * @return		the raw data of the output
   * @throws Exception	if request fails, eg invalid job PK
   */
  public byte[] getOutput(Job job, String name, String type) throws Exception {
    return getOutput(job.getPK(), name, type);
  }

  /**
   * Downloads the specified output from the job.
   *
   * @param pk		the job ID
   * @param name	the name of the output
   * @param type 	the type of the output
   * @return		the raw data of the output
   * @throws Exception	if request fails, eg invalid job PK
   */
  public byte[] getOutput(int pk, String name, String type) throws Exception {
    Request 		request;
    BasicResponse 	response;

    request  = newGet(getPath() + pk + "/outputs/" + name + "/" + type);
    response = execute(request);
    if (response.ok())
      return response.body();
    else
      throw new FailedRequestException("Failed to retrieve output '" + name + "/" + type + "' from job: " + pk, response);
  }

  /**
   * Downloads the output of the job to the specified file.
   *
   * @param job 	the job
   * @param name	the name of the output
   * @param type 	the type of the output
   * @param output	the file to save the download to
   * @return		true if successfully added/uploaded
   * @throws Exception	if request fails, eg invalid job PK
   */
  public boolean getOutput(Job job, String name, String type, File output) throws Exception {
    return getOutput(job.getPK(), name, type, output);
  }

  /**
   * Downloads the output of the job to the specified file.
   *
   * @param pk		the job ID
   * @param name	the name of the output
   * @param type 	the type of the output
   * @param output	the file to save the download to
   * @return		true if successfully added/uploaded
   * @throws Exception	if request fails, eg invalid job PK
   */
  public boolean getOutput(int pk, String name, String type, File output) throws Exception {
    Request 	request;
    Response 	response;

    request = newGet(getPath() + pk + "/outputs/" + name + "/" + type);
    response = download(request, output);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to download output '" + name + "/" + type + "' from job " + pk + " as: " + output, response);
  }

  /**
   * Downloads the output of the job to the stream.
   *
   * @param job 	the job
   * @param name	the name of the output
   * @param type 	the type of the output
   * @param stream	the stream to save the output to
   * @return		true if successfully added/uploaded
   * @throws Exception	if request fails, eg invalid job PK
   */
  public boolean getOutput(Job job, String name, String type, OutputStream stream) throws Exception {
    return getOutput(job.getPK(), name, type, stream);
  }

  /**
   * Downloads the output of the job to the stream.
   *
   * @param pk		the job ID
   * @param name	the name of the output
   * @param type 	the type of the output
   * @param stream	the stream to save the output to
   * @return		true if successfully added/uploaded
   * @throws Exception	if request fails, eg invalid job PK
   */
  public boolean getOutput(int pk, String name, String type, OutputStream stream) throws Exception {
    Request 	request;
    StreamResponse response;

    request  = newGet(getPath() + pk + "/outputs/" + name + "/" + type);
    response = stream(request, stream);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to download output '" + name + "/" + type + "' from job " + pk + " as stream!", response);
  }

  /**
   * Removes the specified output from the job.
   *
   * @param job 	the job to update
   * @param name	the name of the output
   * @param type 	the type of the output
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid job PK
   */
  public boolean deleteOutput(Job job, String name, String type) throws Exception {
    return deleteOutput(job.getPK(), name, type);
  }

  /**
   * Removes the specified output from the job.
   *
   * @param pk		the job ID
   * @param name	the name of the output
   * @param type 	the type of the output
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid job PK
   */
  public boolean deleteOutput(int pk, String name, String type) throws Exception {
    Request 	request;
    Response response;

    request  = newDelete(getPath() + pk + "/outputs/" + name + "/" + type);
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to remove output '" + name + "/" + type + "' from job: " + pk, response);
  }

  /**
   * For deleting a specific job.
   *
   * @param job 	the job to delete
   * @param hard 	whether hard or soft delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid job PK
   */
  public boolean delete(Job job, boolean hard) throws Exception {
    return delete(job.getPK(), hard);
  }

  /**
   * For deleting a specific job.
   *
   * @param pk 		the ID of the job
   * @param hard 	whether hard or soft delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid job PK
   */
  public boolean delete(int pk, boolean hard) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("deleting job with PK (hard=" + hard + "): " + pk);

    request  = newDelete(getPath() + pk + (hard ? "/hard" : ""));
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete job (hard=" + hard + "): " + pk, response);
  }

  /**
   * For reinstating a specific job.
   *
   * @param job 	the job to reinstate
   * @return		true if successfully reinstated
   * @throws Exception	if request fails, eg invalid job PK
   */
  public boolean reinstate(Job job) throws Exception {
    return reinstate(job.getPK());
  }

  /**
   * For reinstating a specific job.
   *
   * @param pk 		the ID of the job
   * @return		true if successfully reinstated
   * @throws Exception	if request fails, eg invalid job PK
   */
  public boolean reinstate(int pk) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("Reinstating job with PK: " + pk);

    request  = newDelete(getPath() + pk + "/reinstate");
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to reinstate job: " + pk, response);
  }

  /**
   * For resetting a specific job.
   *
   * @param job 	the job to reset
   * @return		the Job
   * @throws Exception	if request fails, eg invalid job PK
   */
  public Job reset(Job job) throws Exception {
    return reset(job.getPK());
  }

  /**
   * For resetting a specific job.
   *
   * @param pk 		the ID of the job to reset
   * @return		the Job
   * @throws Exception	if request fails, eg invalid job PK
   */
  public Job reset(int pk) throws Exception {
    Job			result;
    JsonResponse 	response;
    Request 		request;
    JsonElement		element;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("Resetting job with PK: " + pk);

    result   = null;
    request  = newDelete(getPath() + pk + "/reset");
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new Job(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to reset job: " + pk, response);
    }

    return result;
  }

  /**
   * For aborting a specific job.
   *
   * @param job 	the job to abort
   * @return		the Job
   * @throws Exception	if request fails, eg invalid job PK
   */
  public Job abort(Job job) throws Exception {
    return abort(job.getPK());
  }

  /**
   * For aborting a specific job.
   *
   * @param pk 		the ID of the job to abort
   * @return		the Job
   * @throws Exception	if request fails, eg invalid job PK
   */
  public Job abort(int pk) throws Exception {
    Job			result;
    JsonResponse 	response;
    Request 		request;
    JsonElement		element;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("Aborting job with PK: " + pk);

    result   = null;
    request  = newDelete(getPath() + pk + "/abort");
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new Job(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to abor job: " + pk, response);
    }

    return result;
  }

  /**
   * For releasing a specific job.
   *
   * @param job 	the job to release
   * @return		the Job
   * @throws Exception	if request fails, eg invalid job PK
   */
  public Job release(Job job) throws Exception {
    return release(job.getPK());
  }

  /**
   * For releasing a specific job.
   *
   * @param pk 		the ID of the job to release
   * @return		the Job
   * @throws Exception	if request fails, eg invalid job PK
   */
  public Job release(int pk) throws Exception {
    Job			result;
    JsonResponse 	response;
    Request 		request;
    JsonElement		element;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("Releasing job with PK: " + pk);

    result   = null;
    request  = newGet(getPath() + pk + "/release");
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new Job(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to reset job: " + pk, response);
    }

    return result;
  }
}
