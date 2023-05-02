/*
 * JobTemplates.java
 * Copyright (C) 2020-2023 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.core.MediaTypeHelper;
import com.github.fracpete.requests4j.request.Request;
import com.github.fracpete.requests4j.response.JsonResponse;
import com.github.waikatoufdl.ufdl4j.action.Jobs.Job;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapper;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapperWithPK;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonObjectWithDomain;
import com.github.waikatoufdl.ufdl4j.core.JsonObjectWithIntVersion;
import com.github.waikatoufdl.ufdl4j.core.JsonObjectWithShortDescription;
import com.github.waikatoufdl.ufdl4j.core.JsonUtils;
import com.github.waikatoufdl.ufdl4j.core.SoftDeleteObject;
import com.github.waikatoufdl.ufdl4j.filter.AbstractExpression;
import com.github.waikatoufdl.ufdl4j.filter.Filter;
import com.github.waikatoufdl.ufdl4j.filter.GenericFilter;
import com.github.waikatoufdl.ufdl4j.filter.field.ExactNumber;
import com.github.waikatoufdl.ufdl4j.filter.field.ExactString;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.tika.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.waikatoufdl.ufdl4j.core.JsonUtils.mapToJson;

/**
 * Encapsulates job template operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class JobTemplates
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  /**
   * Container class for parameter definitions.
   */
  public static class Parameter
    extends AbstractJsonObjectWrapper
    implements JsonObjectWithShortDescription {

    private static final long serialVersionUID = -5737527598417159651L;

    /** the name of the parameter. */
    protected String m_Name;

    /**
     * Initializes the wrapper.
     *
     * @param data the underlying JSON to use
     */
    public Parameter(String name, JsonObject data) {
      super(data);
      m_Name = name;
    }

    /**
     * Returns the name of the parameter.
     *
     * @return		the name
     */
    public String getName() {
      return m_Name;
    }

    /**
     * Returns the types of this parameter.
     *
     * @return		the list of types
     */
    public List<String> getTypes() {
      List<String>	result;
      List		list;
      JsonElement	elem;

      result = new ArrayList<>();
      if (hasValue("types")) {
	elem = m_Data.get("types");
	if (elem.isJsonArray()) {
	  list = JsonUtils.asList(elem.getAsJsonArray());
	  for (Object item: list)
	    result.add("" + item);
	}
	else if (elem.isJsonObject()){
	  result.addAll(m_Data.get("types").getAsJsonObject().keySet());
	}
      }

      return result;
    }

    /**
     * Returns the help for the parameter.
     *
     * @return		the help
     */
    public String getHelp() {
      return getString("help", "");
    }

    /**
     * Returns the default type, if any.
     *
     * @return		the default type, null if not available
     * @see		#getDefault()
     */
    public String getDefaultType() {
      return getString("default_type", null);
    }

    /**
     * Checks whether a default value is available.
     *
     * @return		true if available
     */
    public boolean hasDefault() {
      return (getDefault() != null);
    }

    /**
     * Returns the default value, if any.
     *
     * @return		the default (type depends on type), null if not available
     * @see		#getDefaultType()
     */
    public Object getDefault() {
      Object		result;
      JsonElement	element;
      JsonObject	def;

      result = null;

      if (hasValue("default")) {
	element = m_Data.get("default");
	if (element.isJsonObject()) {
	  def = element.getAsJsonObject();
	  if (def.has("value"))
	    result = JsonUtils.toObject(def.get("value"));
	}
      }

      return result;
    }

    /**
     * Returns whether this parameter is a constant.
     *
     * @return		true if a constant
     */
    public boolean isConstant() {
      return getBoolean("const", false);
    }

    /**
     * Returns the short description.
     *
     * @return		the short description
     */
    @Override
    public String getShortDescription() {
      return getName() + "/" + getTypes();
    }

    /**
     * Returns a short description of the state.
     *
     * @return the description
     */
    @Override
    public String toString() {
      return "name=" + getName() + ", types=" + getTypes() + ", default=" + getDefault() + ", default_type=" + getDefaultType() + ", const=" + isConstant();
    }
  }

  /**
   * Container class for job template information.
   */
  public static class JobTemplate
    extends AbstractJsonObjectWrapperWithPK
    implements SoftDeleteObject, JsonObjectWithShortDescription, JsonObjectWithIntVersion, JsonObjectWithDomain {

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
    @Override
    public int getVersion() {
      return getInt("version");
    }

    /**
     * Returns the description.
     *
     * @return		the description, null if not available
     */
    public String getDescription() {
      return getString("description", null);
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
     * Returns the domain.
     *
     * @return		the domain
     */
    @Override
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
     * Returns the parameters of this template.
     *
     * @return		the parameters
     */
    public List<Parameter> getParameters() {
      List<Parameter>	result;
      JsonObject	params;

      result = new ArrayList<>();
      params = m_Data.getAsJsonObject("parameters");
      for (String name: params.keySet())
	result.add(new Parameter(name, params.getAsJsonObject(name)));

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
      return "pk=" + getPK() + ", name=" + getName() + ", version=" + getVersion() + ", params=" + getParameters();
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
    return "/v1/job-templates/";
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
    request  = newPost(getPath() + "list");
    if (filter != null)
      request.body(filter.toJsonObject().toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
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
   * Returns all the templates that match the contract name.
   *
   * @param contract	the contract name, eg Train or Predict
   * @return		the matching templates
   * @throws Exception	if request fails
   */
  public List<JobTemplate> getAllMatchingTemplates(String contract) throws Exception {
    return getAllMatchingTemplates(contract, null);
  }

  /**
   * Returns all the templates that match the contract name.
   *
   * @param contract	the contract name, eg Train or Predict
   * @param types 	the types, ignored if null
   * @return		the matching templates
   * @throws Exception	if request fails
   */
  public List<JobTemplate> getAllMatchingTemplates(String contract, Map<String,String> types) throws Exception {
    List<JobTemplate>	result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    if (types == null)
      types = new HashMap<>();

    getLogger().info("Getting all matching templates for contract=" + contract + " and types=" + types);

    result   = null;
    request  = newGet(getPath() + "get-all-matching-templates/" + contract, types);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonArray()) {
	array  = element.getAsJsonArray();
	result = new ArrayList<>();
	for (i = 0; i < array.size(); i++)
	  result.add(new JobTemplate(array.get(i).getAsJsonObject()));
      }
    }
    else {
      throw new FailedRequestException("Failed to get all matching templates for contract=" + contract + " and types=" + types, response);
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
   * Retrieves all the parameters for the template.
   *
   * @param jobTemplate the job template to get the parameters for
   * @return		the parameters
   * @throws Exception	if request fails, eg invalid job template PK
   */
  public List<Parameter> getAllParameters(JobTemplate jobTemplate) throws Exception {
    return getAllParameters(jobTemplate.getPK());
  }

  /**
   * Retrieves all the parameters for the template.
   *
   * @param pk 		the ID of the job template
   * @return		the parameters
   * @throws Exception	if request fails, eg invalid job template PK
   */
  public List<Parameter> getAllParameters(int pk) throws Exception {
    List<Parameter>	result;
    JsonResponse 	response;
    Request 		request;
    JsonObject 		obj;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("get all parameters for job template with PK: " + pk);

    request  = newGet(getPath() + pk + "/get-all-parameters");
    response = execute(request);
    if (response.ok()) {
      obj    = response.jsonObject();
      result = new ArrayList<>();
      for (String key: obj.keySet())
	result.add(new Parameter(key, obj.getAsJsonObject(key)));
      return result;
    }
    else {
      throw new FailedRequestException("Failed to get all parameters for job template: " + pk, response);
    }
  }

  /**
   * Retrieves the types for the template.
   *
   * @param jobTemplate the job template to get the parameters for
   * @return		the types
   * @throws Exception	if request fails, eg invalid job template PK
   */
  public Map<String,String> getTypes(JobTemplate jobTemplate) throws Exception {
    return getTypes(jobTemplate.getPK());
  }

  /**
   * Retrieves the types for the template.
   *
   * @param pk 		the ID of the job template
   * @return		the types
   * @throws Exception	if request fails, eg invalid job template PK
   */
  public Map<String,String> getTypes(int pk) throws Exception {
    Map<String,String>	result;
    JsonResponse 	response;
    Request 		request;
    JsonObject 		obj;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("get types for job template with PK: " + pk);

    request  = newGet(getPath() + pk + "/get-types");
    response = execute(request);
    if (response.ok()) {
      obj    = response.jsonObject();
      result = new HashMap<>();
      for (String key: obj.keySet())
	result.put(key, obj.getAsJsonPrimitive(key).getAsString());
      return result;
    }
    else {
      throw new FailedRequestException("Failed to get types for job template: " + pk, response);
    }
  }

  /**
   * Retrieves the outputs for the template.
   *
   * @param jobTemplate the job template to get the parameters for
   * @return		the outputs
   * @throws Exception	if request fails, eg invalid job template PK
   */
  public Map<String,String> getOutputs(JobTemplate jobTemplate) throws Exception {
    return getOutputs(jobTemplate.getPK());
  }

  /**
   * Retrieves the outputs for the template.
   *
   * @param pk 		the ID of the job template
   * @return		the outputs
   * @throws Exception	if request fails, eg invalid job template PK
   */
  public Map<String,String> getOutputs(int pk) throws Exception {
    Map<String,String>	result;
    JsonResponse 	response;
    Request 		request;
    JsonObject 		obj;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("get outputs for job template with PK: " + pk);

    request  = newGet(getPath() + pk + "/get-outputs");
    response = execute(request);
    if (response.ok()) {
      obj    = response.jsonObject();
      result = new HashMap<>();
      for (String key: obj.keySet())
	result.put(key, obj.getAsJsonPrimitive(key).getAsString());
      return result;
    }
    else {
      throw new FailedRequestException("Failed to get outputs for job template: " + pk, response);
    }
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

    request  = newDelete(getPath() + pk + (hard ? "/hard" : ""));
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
   * Creates a job from the given template, docker image and values.
   *
   * @param jobTemplate	the job template
   * @param inputValues	the input values (name -> value)
   * @param paramValues	the parameter values (name -> value)
   * @param description the description, ignored if null or empty
   * @return		the Job object, null if failed to create
   * @throws Exception	if request fails
   */
  public Job newJob(JobTemplate jobTemplate, Map<String,Map<String,String>> inputValues, Map<String,Map<String,String>> paramValues, String description) throws Exception {
    return newJob(jobTemplate.getPK(), inputValues, paramValues, description);
  }

  /**
   * Creates a job from the given template, docker image and values.
   *
   * @param pk		the PK of the job template
   * @param inputValues	the input values (name -> value)
   * @param paramValues	the parameter values (name -> value)
   * @param description the description, ignored if null or empty
   * @return		the Job object, null if failed to create
   * @throws Exception	if request fails
   */
  public Job newJob(int pk, Map<String,Map<String,String>> inputValues, Map<String,Map<String,String>> paramValues, String description) throws Exception {
    Job			result;
    JsonObject		data;
    JsonObject		sub;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("creating job from template: " + pk);

    data = new JsonObject();
    if ((description != null) && !description.isEmpty())
      data.addProperty("description", description);

    // input values
    sub = new JsonObject();
    for (String key: inputValues.keySet())
      sub.add(key, mapToJson(inputValues.get(key)));
    data.add("input_values", sub);

    // parameter values
    if (paramValues.size() > 0) {
      sub = new JsonObject();
      for (String key: paramValues.keySet())
	sub.add(key, mapToJson(paramValues.get(key)));
      data.add("parameter_values", sub);
    }

    request = newPost(getPath() + pk + "/create-job")
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok())
      result = new Job(response.jsonObject());
    else
      throw new FailedRequestException("Failed to create job from template: " + pk, response);

    return result;
  }

  /**
   * For exporting a job template as json.
   *
   * @param template  	the template to export
   * @return		the JSON representation of the template
   * @throws Exception	if request fails
   */
  public JsonObject exportTemplate(JobTemplate template) throws Exception {
    return exportTemplate(template.getPK());
  }

  /**
   * For exporting a job template as json.
   *
   * @param pk  	the primary key
   * @return		the JSON representation of the template
   * @throws Exception	if request fails
   */
  public JsonObject exportTemplate(int pk) throws Exception {
    Request 		request;
    JsonResponse	response;

    getLogger().info("exporting job template: " + pk);

    request  = newGet(getPath() + pk + "/export");
    response = execute(request);
    if (response.ok())
      return response.jsonObject();
    else
      throw new FailedRequestException("Failed to export job template: " + pk, response);
  }

  /**
   * For importing a job template from a json file.
   *
   * @param template	the template file to import
   * @return		the job template
   * @throws Exception	if request fails
   */
  public JobTemplate importTemplate(File template) throws Exception {
    JsonObject		json;
    FileReader		freader;
    BufferedReader	breader;

    if (!template.exists())
      throw new IOException("File does not exist: " + template);
    if (template.isDirectory())
      throw new IOException("Template points to a directory: " + template);

    freader = null;
    breader = null;
    try {
      freader = new FileReader(template);
      breader = new BufferedReader(freader);
      json = (JsonObject) JsonParser.parseReader(breader);
      return importTemplate(json);
    }
    catch (Exception e) {
      throw new IOException("Failed to load job template: " + template, e);
    }
    finally {
      IOUtils.closeQuietly(breader);
      IOUtils.closeQuietly(freader);
    }
  }

  /**
   * For importing a job template from json.
   *
   * @param template	the template to import
   * @return		the job template
   * @throws Exception	if request fails
   */
  public JobTemplate importTemplate(JsonObject template) throws Exception {
    Request 		request;
    JsonResponse	response;

    getLogger().info("importing job template");

    request = newPost(getPath() + "import")
      .body(template.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok())
      return new JobTemplate(response.jsonObject());
    else
      throw new FailedRequestException("Failed to import job template: " + template, response);
  }
}
