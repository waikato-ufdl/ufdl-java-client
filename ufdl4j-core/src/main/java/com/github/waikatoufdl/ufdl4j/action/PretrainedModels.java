/*
 * PretrainedModels.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapper;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;
import com.github.waikatoufdl.ufdl4j.core.SoftDeleteObject;
import com.github.waikatoufdl.ufdl4j.filter.Filter;
import com.github.waikatoufdl.ufdl4j.filter.NameFilter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.entity.ContentType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates pretrained model operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class PretrainedModels
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  /**
   * Container class for pretrained model information.
   */
  public static class PretrainedModel
    extends AbstractJsonObjectWrapper
    implements SoftDeleteObject {

    private static final long serialVersionUID = 3523630902439390574L;

    /**
     * Initializes the pretrained model.
     *
     * @param data	the data to use
     */
    public PretrainedModel(JsonObject data) {
      super(data);
    }

    /**
     * Returns the pretrained model primary key.
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
     * Returns the framework PK.
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
     * Returns the license PK.
     *
     * @return		the license
     */
    public int getLicense() {
      return getInt("licence");
    }

    /**
     * Returns the download URL.
     *
     * @return		the URL
     */
    public String getURL() {
      return getString("url");
    }

    /**
     * Returns the description.
     *
     * @return		the description
     */
    public String getDescription() {
      return getString("description");
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
      return "pk=" + getPK() + ", name=" + getName() + ", framework=" + getFramework() + ", domain=" + getDomain() + ", url=" + getURL();
    }
  }

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "Pretrained Models";
  }

  /**
   * The URL path to use.
   *
   * @return		the path
   */
  @Override
  public String getPath() {
    return "/v1/core/pretrained-models/";
  }

  /**
   * For listing the pretrained models.
   *
   * @return		the list of pretrained models
   * @throws Exception	if request fails
   */
  public List<PretrainedModel> list() throws Exception {
    return list(null);
  }

  /**
   * For listing the pretrained models.
   *
   * @param filter 	the filter to apply, can be null
   * @return		the list of pretrained models
   * @throws Exception	if request fails
   */
  public List<PretrainedModel> list(Filter filter) throws Exception {
    List<PretrainedModel>	result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing pretrained models" + (filter == null ? "" : ", filter: " + filter.toJsonObject()));

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
          result.add(new PretrainedModel(array.get(i).getAsJsonObject()));
      }
    }
    else {
      throw new FailedRequestException("Failed to list pretrained models!" + (filter == null ? "" : "\nFilter: " + filter.toJsonObject()), response);
    }

    return result;
  }

  /**
   * For loading a specific pretrained model by primary key.
   *
   * @param pk 		the primary key of the pretrained model to load
   * @return		the pretrained model
   * @throws Exception	if request fails
   */
  public PretrainedModel load(int pk) throws Exception {
    PretrainedModel		result;
    JsonResponse 	response;
    JsonElement		element;
    Request 		request;

    getLogger().info("loading pretrained model with id: " + pk);

    result   = null;
    request  = newGet(getPath() + pk);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new PretrainedModel(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to load pretrained model: " + pk, response);
    }

    return result;
  }

  /**
   * For loading a specific pretrained model by name.
   *
   * @param name 	the name
   * @return		the pretrained model object, null if failed to locate
   * @throws Exception	if request fails
   */
  public PretrainedModel load(String name) throws Exception {
    PretrainedModel	result;

    getLogger().info("loading pretrained model: " + name);

    result = null;

    for (PretrainedModel template : list(new NameFilter(name))) {
      result = template;
      break;
    }

    if (result == null)
      getLogger().warning("failed to load pretrained model: " + name);

    return result;
  }

  /**
   * Creates a pretrained model object.
   *
   * @param name 	the name
   * @param framework   the framework PK
   * @param domain 	the domain
   * @param license	the license PK
   * @param url		the download URL
   * @param description	the model description
   * @return		the PretrainedModel object, null if failed to create
   * @throws Exception	if request fails or pretrained model already exists
   */
  public PretrainedModel create(String name, int framework, String domain, int license, String url, String description) throws Exception {
    PretrainedModel		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("creating pretrained model: " + framework + "/" + domain + "/" + url);

    data = new JsonObject();
    data.addProperty("name", name);
    data.addProperty("framework", framework);
    data.addProperty("domain", domain);
    data.addProperty("licence", license);
    data.addProperty("url", url);
    data.addProperty("description", description);
    request = newPost(getPath())
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new PretrainedModel(response.jsonObject());
    else
      throw new FailedRequestException("Failed to create pretrained model: " + framework + "/" + domain + "/" + url, response);

    return result;
  }

  /**
   * Updates the pretrained model.
   *
   * @param obj 	the pretrained model to update
   * @param name 	the new name
   * @param framework   the new framework PK
   * @param domain 	the new domain
   * @param license	the new license PK
   * @param url		the new download URL
   * @param description	the new model description
   * @return		the pretrained model object
   * @throws Exception	if request fails
   */
  public PretrainedModel update(PretrainedModel obj, String name, int framework, String domain, int license, String url, String description) throws Exception {
    return update(obj.getPK(), name, framework, domain, license, url, description);
  }

  /**
   * Updates the pretrained model.
   *
   * @param pk 		the PK of the pretrained model to update
   * @param name 	the new name
   * @param framework   the new framework PK
   * @param domain 	the new domain
   * @param license	the new license PK
   * @param url		the new download URL
   * @param description	the new model description
   * @return		the pretrained model object
   * @throws Exception	if request fails
   */
  public PretrainedModel update(int pk, String name, int framework, String domain, int license, String url, String description) throws Exception {
    PretrainedModel		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("updating pretrained model: " + pk);

    data = new JsonObject();
    data.addProperty("name", name);
    data.addProperty("framework", framework);
    data.addProperty("domain", domain);
    data.addProperty("licence", license);
    data.addProperty("url", url);
    data.addProperty("description", description);
    request = newPut(getPath() + pk)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new PretrainedModel(response.jsonObject());
    else
      throw new FailedRequestException("Failed to update pretrained model: " + pk, response);

    return result;
  }

  /**
   * (Partially) Updates the pretrained model identified by the object, using only the non-null arguments.
   *
   * @param obj	the user to update
   * @param name 	the new name, ignored if null
   * @param framework   the new framework PK, ignored if null
   * @param domain 	the new domain, ignored if null
   * @param license	the new license PK, ignored if null
   * @param url		the new download URL, ignored if null
   * @param description	the new model description, ignored if null
   * @return		the user object, null if failed to create
   * @throws Exception	if request fails
   */
  public PretrainedModel partialUpdate(PretrainedModel obj, String name, Integer framework, String domain, Integer license, String url, String description) throws Exception {
    return partialUpdate(obj.getPK(), name, framework, domain, license, url, description);
  }

  /**
   * (Partially) Updates the pretrained model identified by the PK, using only the non-null arguments.
   *
   * @param pk 		the PK of the user to update
   * @param name 	the new name, ignored if null
   * @param framework   the new framework PK, ignored if null
   * @param domain 	the new domain, ignored if null
   * @param license	the new license PK, ignored if null
   * @param url		the new download URL, ignored if null
   * @param description	the new model description, ignored if null
   * @return		the pretrained model object, null if failed to create
   * @throws Exception	if request fails
   */
  public PretrainedModel partialUpdate(int pk, String name, Integer framework, String domain, Integer license, String url, String description) throws Exception {
    PretrainedModel		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("partially updating pretrained model: " + pk);

    data = new JsonObject();
    if (name != null)
      data.addProperty("name", name);
    if (framework != null)
      data.addProperty("framework", framework);
    if (domain != null)
      data.addProperty("domain", domain);
    if (license != null)
      data.addProperty("licence", license);
    if (url != null)
      data.addProperty("url", url);
    if (description != null)
      data.addProperty("description", description);
    request = newPatch(getPath() + pk)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new PretrainedModel(response.jsonObject());
    else
      throw new FailedRequestException("Failed to partially update pretrained model: " + pk, response);

    return result;
  }

  /**
   * For deleting a specific pretrained model.
   *
   * @param jobTemplate the pretrained model to delete
   * @param hard 	whether hard or soft delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid pretrained model PK
   */
  public boolean delete(PretrainedModel jobTemplate, boolean hard) throws Exception {
    return delete(jobTemplate.getPK(), hard);
  }

  /**
   * For deleting a specific pretrained model.
   *
   * @param pk 		the ID of the pretrained model
   * @param hard 	whether hard or soft delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid pretrained model PK
   */
  public boolean delete(int pk, boolean hard) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("deleting pretrained model with PK (hard=" + hard + "): " + pk);

    request  = newDelete(getPath() + pk + (hard ? "/hard" : "/"));
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete pretrained model (hard=" + hard + "): " + pk, response);
  }

  /**
   * For reinstating a specific pretrained model.
   *
   * @param jobTemplate 	the pretrained model to reinstate
   * @return		true if successfully reinstated
   * @throws Exception	if request fails, eg invalid pretrained model PK
   */
  public boolean reinstate(PretrainedModel jobTemplate) throws Exception {
    return reinstate(jobTemplate.getPK());
  }

  /**
   * For reinstating a specific pretrained model.
   *
   * @param pk 		the ID of the pretrained model
   * @return		true if successfully reinstated
   * @throws Exception	if request fails, eg invalid pretrained model PK
   */
  public boolean reinstate(int pk) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("Reinstating pretrained model with PK: " + pk);

    request  = newDelete(getPath() + pk + "/reinstate");
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to reinstate pretrained model: " + pk, response);
  }
}