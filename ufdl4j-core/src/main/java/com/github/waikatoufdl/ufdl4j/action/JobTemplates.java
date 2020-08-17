/*
 * JobTemplates.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdl4j.action.Jobs.Job;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapper;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;
import com.github.waikatoufdl.ufdl4j.core.SoftDeleteObject;
import com.github.waikatoufdl.ufdl4j.filter.AbstractExpression;
import com.github.waikatoufdl.ufdl4j.filter.Filter;
import com.github.waikatoufdl.ufdl4j.filter.GenericFilter;
import com.github.waikatoufdl.ufdl4j.filter.field.ExactNumber;
import com.github.waikatoufdl.ufdl4j.filter.field.ExactString;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.entity.ContentType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates job template operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class JobTemplates
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  /**
   * Container class for job template information.
   */
  public static class JobTemplate
    extends AbstractJsonObjectWrapper
    implements SoftDeleteObject {

    private static final long serialVersionUID = 3523630902439390574L;

    /**
     * Initializes the job template.
     *
     * @param data	the data to use
     */
    public JobTemplate(JsonObject data) {
      super(data);
    }

    /**
     * Returns the job template primary key.
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
     * Returns the version.
     *
     * @return		the version
     */
    public int getVersion() {
      return getInt("version");
    }

    /**
     * Returns the scope.
     *
     * @return		the scope
     */
    public String getScope() {
      return getString("scope");
    }

    /**
     * Returns the framework.
     *
     * @return		the framework
     */
    public int getFramework() {
      return getInt("framework");
    }

    /**
     * Returns the domain.
     *
     * @return		the domain
     */
    public String getDomain() {
      return getString("domain");
    }

    /**
     * Returns the type.
     *
     * @return		the type
     */
    public String getType() {
      return getString("type");
    }

    /**
     * Returns the inputs as a list of maps (name/type/options).
     *
     * @return		the inputs
     */
    public List<Map<String,String>> getInputs() {
      List<Map<String,String>>  result;
      JsonArray			inputs;
      JsonObject		inputObj;
      int			i;
      Map<String,String>	inputMap;

      result = new ArrayList<>();
      inputs = getData().getAsJsonArray("inputs");
      for (i = 0; i < inputs.size(); i++) {
	inputObj = inputs.get(i).getAsJsonObject();
	inputMap = new HashMap<>();
	inputMap.put("name", inputObj.get("name").getAsString());
	inputMap.put("type", inputObj.get("type").getAsString());
	inputMap.put("options", inputObj.has("options") ? inputObj.get("options").getAsString() : "");
	result.add(inputMap);
      }

      return result;
    }

    /**
     * Returns the parameters as a list of maps (name/type/default).
     *
     * @return		the parameters
     */
    public List<Map<String,String>> getParameters() {
      List<Map<String,String>>  result;
      JsonArray 		parameters;
      JsonObject 		paramObj;
      int			i;
      Map<String,String> 	paramMap;

      result = new ArrayList<>();
      parameters = getData().getAsJsonArray("parameters");
      for (i = 0; i < parameters.size(); i++) {
	paramObj = parameters.get(i).getAsJsonObject();
	paramMap = new HashMap<>();
	paramMap.put("name", paramObj.get("name").getAsString());
	paramMap.put("type", paramObj.get("type").getAsString());
	paramMap.put("default", paramObj.has("default") ? paramObj.get("default").getAsString() : "");
	result.add(paramMap);
      }

      return result;
    }

    /**
     * Returns the executor class.
     *
     * @return		the class
     */
    public String getExecutorClass() {
      return getString("executor_class");
    }

    /**
     * Returns the required packages (pip).
     *
     * @return		the packages
     */
    public String getRequiredPackages() {
      return getString("required_packages");
    }

    /**
     * Returns the license.
     *
     * @return		the license
     */
    public int getLicense() {
      return getInt("licence", -1);
    }

    /**
     * Returns the template body.
     *
     * @return		the body
     */
    public String getBody() {
      return getString("body");
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
     * Returns a short description of the state.
     *
     * @return		the state
     */
    @Override
    public String toString() {
      return "pk=" + getPK() + ", name=" + getName() + ", version=" + getVersion() + ", inputs=" + getInputs() + ", params=" + getParameters();
    }
  }

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "JobTemplates";
  }

  /**
   * The URL path to use.
   *
   * @return		the path
   */
  @Override
  public String getPath() {
    return "/v1/core/job-templates/";
  }

  /**
   * For listing the job templates.
   *
   * @return		the list of job templates
   * @throws Exception	if request fails
   */
  public List<JobTemplate> list() throws Exception {
    return list(null);
  }

  /**
   * For listing the job templates.
   *
   * @param filter 	the filter to apply, can be null
   * @return		the list of job templates
   * @throws Exception	if request fails
   */
  public List<JobTemplate> list(Filter filter) throws Exception {
    List<JobTemplate>	result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing job templates" + (filter == null ? "" : ", filter: " + filter.toJsonObject()));

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
          result.add(new JobTemplate(array.get(i).getAsJsonObject()));
      }
    }
    else {
      throw new FailedRequestException("Failed to list job templates!" + (filter == null ? "" : "\nFilter: " + filter.toJsonObject()), response);
    }

    return result;
  }

  /**
   * For loading a specific job template by primary key.
   *
   * @param pk 		the primary key of the job template to load
   * @return		the job template
   * @throws Exception	if request fails
   */
  public JobTemplate load(int pk) throws Exception {
    JobTemplate		result;
    JsonResponse 	response;
    JsonElement		element;
    Request 		request;

    getLogger().info("loading job template with id: " + pk);

    result   = null;
    request  = newGet(getPath() + pk);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new JobTemplate(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to load job template: " + pk, response);
    }

    return result;
  }

  /**
   * For loading a specific job template by name.
   *
   * @param name 	the name
   * @return		the job template object, null if failed to locate
   * @throws Exception	if request fails
   */
  public JobTemplate load(String name, int version) throws Exception {
    JobTemplate	result;
    Filter	filter;

    getLogger().info("loading job template: " + name + "/" + version);

    result = null;

    filter = new GenericFilter(
      new AbstractExpression[]{
        new ExactString("name", name),
        new ExactNumber("version", version),
      }
    );
    for (JobTemplate template : list(filter)) {
      result = template;
      break;
    }

    if (result == null)
      getLogger().warning("failed to load job template: " + name + "/" + version);

    return result;
  }

  /**
   * Creates a job template object.
   *
   * @param name 	the name
   * @param version	the version
   * @param scope 	the scope
   * @param framework   the framework PK
   * @param domain 	the domain
   * @param type	the type
   * @param executor 	the executor class
   * @param packages 	the required packages (pip)
   * @param body 	the actual template
   * @param license 	the license PK
   * @return		the JobTemplate object, null if failed to create
   * @throws Exception	if request fails or job template already exists
   */
  public JobTemplate create(String name, int version, String scope, int framework, String domain, String type, String executor, String packages, String body, int license) throws Exception {
    JobTemplate		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("creating job template: " + name);

    data = new JsonObject();
    data.addProperty("name", name);
    data.addProperty("version", version);
    data.addProperty("scope", scope);
    data.addProperty("framework", framework);
    data.addProperty("domain", domain);
    data.addProperty("type", type);
    data.addProperty("executor_class", executor);
    data.addProperty("required_packages", packages);
    data.addProperty("licence", license);
    data.addProperty("body", body);
    request = newPost(getPath())
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new JobTemplate(response.jsonObject());
    else
      throw new FailedRequestException("Failed to create job template: " + name, response);

    return result;
  }

  /**
   * Updates the job template.
   *
   * @param obj 	the job template to update
   * @param name 	the new name
   * @param version	the new version
   * @param scope 	the new scope
   * @param framework   the new framework PK
   * @param domain 	the new domain
   * @param type	the new type
   * @param executor 	the new executor class
   * @param packages 	the new required packages (pip)
   * @param body 	the new actual template
   * @param license 	the new license PK
   * @return		the job template object
   * @throws Exception	if request fails
   */
  public JobTemplate update(JobTemplate obj, String name, int version, String scope, int framework, String domain, String type, String executor, String packages, String body, int license) throws Exception {
    return update(obj.getPK(), name, version, scope, framework, domain, type, executor, packages, body, license);
  }

  /**
   * Updates the job template.
   *
   * @param pk 		the PK of the job template to update
   * @param name 	the new name
   * @param version	the new version
   * @param scope 	the new scope
   * @param framework   the new framework PK
   * @param domain 	the new domain
   * @param type	the new type
   * @param executor 	the new executor class
   * @param packages 	the new required packages (pip)
   * @param body 	the new actual template
   * @param license 	the new license PK
   * @return		the job template object
   * @throws Exception	if request fails
   */
  public JobTemplate update(int pk, String name, int version, String scope, int framework, String domain, String type, String executor, String packages, String body, int license) throws Exception {
    JobTemplate		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("updating job template: " + pk);

    data = new JsonObject();
    data.addProperty("name", name);
    data.addProperty("version", version);
    data.addProperty("scope", scope);
    data.addProperty("framework", framework);
    data.addProperty("domain", domain);
    data.addProperty("type", type);
    data.addProperty("executor_class", executor);
    data.addProperty("required_packages", packages);
    data.addProperty("licence", license);
    data.addProperty("body", body);
    request = newPut(getPath() + pk)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new JobTemplate(response.jsonObject());
    else
      throw new FailedRequestException("Failed to update job template: " + pk, response);

    return result;
  }

  /**
   * (Partially) Updates the job template identified by the object, using only the non-null arguments.
   *
   * @param obj	the user to update
   * @param name 	the new name, ignored if null
   * @param version	the new version, ignored if null
   * @param scope 	the new scope, ignored if null
   * @param framework   the new framework PK, ignored if null
   * @param domain 	the new domain, ignored if null
   * @param type	the new type, ignored if null
   * @param executor 	the new executor class, ignored if null
   * @param packages 	the new required packages (pip), ignored if null
   * @param body 	the new actual template, ignored if null
   * @param license 	the new license PK, ignored if null
   * @return		the user object, null if failed to create
   * @throws Exception	if request fails
   */
  public JobTemplate partialUpdate(JobTemplate obj, String name, Integer version, String scope, Integer framework, String domain, String type, String executor, String packages, String body, Integer license) throws Exception {
    return partialUpdate(obj.getPK(), name, version, scope, framework, domain, type, executor, packages, body, license);
  }

  /**
   * (Partially) Updates the job template identified by the PK, using only the non-null arguments.
   *
   * @param pk 		the PK of the user to update
   * @param name 	the new name, ignored if null
   * @param version	the new version, ignored if null
   * @param scope 	the new scope, ignored if null
   * @param framework   the new framework PK, ignored if null
   * @param domain 	the new domain, ignored if null
   * @param type	the new type, ignored if null
   * @param executor 	the new executor class, ignored if null
   * @param packages 	the new required packages (pip), ignored if null
   * @param body 	the new actual template, ignored if null
   * @param license 	the new license PK, ignored if null
   * @return		the job template object, null if failed to create
   * @throws Exception	if request fails
   */
  public JobTemplate partialUpdate(int pk, String name, Integer version, String scope, Integer framework, String domain, String type, String executor, String packages, String body, Integer license) throws Exception {
    JobTemplate		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("partially updating job template: " + pk);

    data = new JsonObject();
    if (name != null)
      data.addProperty("name", name);
    if (version != null)
      data.addProperty("version", version);
    if (scope != null)
      data.addProperty("scope", scope);
    if (framework != null)
      data.addProperty("framework", framework);
    if (domain != null)
      data.addProperty("domain", domain);
    if (type != null)
      data.addProperty("type", type);
    if (executor != null)
      data.addProperty("executor_class", executor);
    if (packages != null)
      data.addProperty("required_packages", packages);
    if (license != null)
      data.addProperty("licence", license);
    if (body != null)
      data.addProperty("body", body);
    request = newPatch(getPath() + pk)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new JobTemplate(response.jsonObject());
    else
      throw new FailedRequestException("Failed to partially update job template: " + pk, response);

    return result;
  }

  /**
   * For deleting a specific job template.
   *
   * @param jobTemplate the job template to delete
   * @param hard 	whether hard or soft delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid job template PK
   */
  public boolean delete(JobTemplate jobTemplate, boolean hard) throws Exception {
    return delete(jobTemplate.getPK(), hard);
  }

  /**
   * For deleting a specific job template.
   *
   * @param pk 		the ID of the job template
   * @param hard 	whether hard or soft delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid job template PK
   */
  public boolean delete(int pk, boolean hard) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("deleting job template with PK (hard=" + hard + "): " + pk);

    request  = newDelete(getPath() + pk + (hard ? "/hard" : "/"));
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete job template (hard=" + hard + "): " + pk, response);
  }

  /**
   * For reinstating a specific job template.
   *
   * @param jobTemplate 	the job template to reinstate
   * @return		true if successfully reinstated
   * @throws Exception	if request fails, eg invalid job template PK
   */
  public boolean reinstate(JobTemplate jobTemplate) throws Exception {
    return reinstate(jobTemplate.getPK());
  }

  /**
   * For reinstating a specific job template.
   *
   * @param pk 		the ID of the job template
   * @return		true if successfully reinstated
   * @throws Exception	if request fails, eg invalid job template PK
   */
  public boolean reinstate(int pk) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("Reinstating job template with PK: " + pk);

    request  = newDelete(getPath() + pk + "/reinstate");
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to reinstate job template: " + pk, response);
  }

  /**
   * For adding an input to a job template.
   *
   * @param jobTemplate the job template to update
   * @param name	the name of the input
   * @param type 	the type of the input
   * @param options 	the (optional) options
   * @return		true if successfully added
   * @throws Exception	if request fails, eg invalid job template PK
   */
  public boolean addInput(JobTemplate jobTemplate, String name, String type, String options) throws Exception {
    return addInput(jobTemplate.getPK(), name, type, options);
  }

  /**
   * For adding an input to a job template.
   *
   * @param pk 		the job template to update
   * @param name	the name of the input
   * @param type 	the type of the input
   * @param options 	the (optional) options
   * @return		true if successfully added
   * @throws Exception	if request fails, eg invalid job template PK
   */
  public boolean addInput(int pk, String name, String type, String options) throws Exception {
    JsonResponse 	response;
    JsonObject		data;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("Adding input '" + name + "' to job template with PK: " + pk);

    data = new JsonObject();
    data.addProperty("type", type);
    data.addProperty("options", options);
    request  = newPost(getPath() + pk + "/inputs/" + name)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to add input '" + name + "' to job template: " + pk, response);
  }

  /**
   * For removing an input from a job template.
   *
   * @param jobTemplate the job template to update
   * @param name	the name of the input
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid job template PK
   */
  public boolean deleteInput(JobTemplate jobTemplate, String name) throws Exception {
    return deleteInput(jobTemplate.getPK(), name);
  }

  /**
   * For removing an input from a job template.
   *
   * @param pk 		the job template to update
   * @param name	the name of the input
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid job template PK
   */
  public boolean deleteInput(int pk, String name) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("Removing input '" + name + "' from job template with PK: " + pk);

    request  = newDelete(getPath() + pk + "/inputs/" + name);
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to remove input '" + name + "' from job template: " + pk, response);
  }

  /**
   * For adding a parameter to a job template.
   *
   * @param jobTemplate the job template to update
   * @param name	the name of the parameter
   * @param type 	the type of the parameter
   * @param defaultValue 	the default value
   * @return		true if successfully added
   * @throws Exception	if request fails, eg invalid job template PK
   */
  public boolean addParameter(JobTemplate jobTemplate, String name, String type, String defaultValue) throws Exception {
    return addParameter(jobTemplate.getPK(), name, type, defaultValue);
  }

  /**
   * For adding a parameter to a job template.
   *
   * @param pk 		the job template to update
   * @param name	the name of the parameter
   * @param type 	the type of the parameter
   * @param defaultValue 	the default value
   * @return		true if successfully added
   * @throws Exception	if request fails, eg invalid job template PK
   */
  public boolean addParameter(int pk, String name, String type, String defaultValue) throws Exception {
    JsonResponse 	response;
    JsonObject		data;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("Adding parameter '" + name + "' to job template with PK: " + pk);

    data = new JsonObject();
    data.addProperty("type", type);
    data.addProperty("default", defaultValue);
    request  = newPost(getPath() + pk + "/parameters/" + name)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to add parameter '" + name + "' to job template: " + pk, response);
  }

  /**
   * For removing a parameter from a job template.
   *
   * @param jobTemplate the job template to update
   * @param name	the name of the parameter
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid job template PK
   */
  public boolean deleteParameter(JobTemplate jobTemplate, String name) throws Exception {
    return deleteParameter(jobTemplate.getPK(), name);
  }

  /**
   * For removing a parameter from a job template.
   *
   * @param pk 		the job template to update
   * @param name	the name of the parameter
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid job template PK
   */
  public boolean deleteParameter(int pk, String name) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("Removing parameter '" + name + "' from job template with PK: " + pk);

    request  = newDelete(getPath() + pk + "/parameters/" + name);
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to remove parameter '" + name + "' from job template: " + pk, response);
  }

  /**
   * Creates a job from the given template, docker image and values.
   *
   * @param jobTemplate	the job template
   * @param dockerImage	the image to use
   * @param inputValues	the input values (name -> value)
   * @param paramValues	the parameter values (name -> value)
   * @return		the Job object, null if failed to create
   * @throws Exception	if request fails
   */
  public Job newJob(JobTemplate jobTemplate, int dockerImage, Map<String,String> inputValues, Map<String,String> paramValues) throws Exception {
    return newJob(jobTemplate.getPK(), dockerImage, inputValues, paramValues);
  }

  /**
   * Creates a job from the given template, docker image and values.
   *
   * @param pk		the PK of the job template
   * @param dockerImage	the image to use
   * @param inputValues	the input values (name -> value)
   * @param paramValues	the parameter values (name -> value)
   * @return		the Job object, null if failed to create
   * @throws Exception	if request fails
   */
  public Job newJob(int pk, int dockerImage, Map<String,String> inputValues, Map<String,String> paramValues) throws Exception {
    Job			result;
    JsonObject		data;
    JsonObject		sub;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("creating job from template: " + pk);

    data = new JsonObject();
    data.addProperty("docker_image", dockerImage);

    // input values
    sub = new JsonObject();
    for (String key: inputValues.keySet())
      sub.addProperty(key, inputValues.get(key));
    data.add("input_values", sub);

    // parameter values
    if (paramValues.size() > 0) {
      sub = new JsonObject();
      for (String key: paramValues.keySet())
	sub.addProperty(key, paramValues.get(key));
      data.add("parameter_values", sub);
    }

    request = newPost(getPath() + pk + "/create-job")
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new Job(response.jsonObject());
    else
      throw new FailedRequestException("Failed to create job from template: " + pk, response);

    return result;
  }
}
