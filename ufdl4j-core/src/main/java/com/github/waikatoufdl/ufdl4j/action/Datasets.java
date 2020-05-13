/*
 * Datasets.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapper;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates dataset operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Datasets
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  public static final String PATH = "/v1/core/datasets/";

  /**
   * Container class for dataset information.
   */
  public static class Dataset
    extends AbstractJsonObjectWrapper {

    private static final long serialVersionUID = 3523630902439390574L;

    /**
     * Initializes the dataset.
     *
     * @param data	the data to use
     */
    public Dataset(JsonObject data) {
      super(data);
    }

    /**
     * Returns the dataset primary key.
     *
     * @return		the primary key
     */
    public int getPK() {
      return getInt("pk");
    }

    /**
     * Returns the creator ID.
     *
     * @return		the ID
     */
    public int getCreatorID() {
      return getInt("creator_id", -1);
    }

    /**
     * Returns the project ID.
     *
     * @return		the ID
     */
    public int getProjectID() {
      return getInt("project_id", -1);
    }

    /**
     * Returns the dataset name.
     *
     * @return		the name
     */
    public String getName() {
      return getString("name", "");
    }

    /**
     * Returns the version.
     *
     * @return		the version
     */
    public int getVersion() {
      return getInt("version", -1);
    }

    /**
     * Returns the licence.
     *
     * @return		the licence
     */
    public String getLicence() {
      return getString("licence", "");
    }

    /**
     * Returns the tags.
     *
     * @return		the tags
     */
    public String getTags() {
      return getString("tags", "");
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
     * Returns whether the dataset public.
     *
     * @return		true if public
     */
    public boolean isPublic() {
      return getBoolean("is_public", false);
    }

    /**
     * Wraps the internal JSON object in the specified Dataset class.
     *
     * @param cls	the dataset class to use
     * @param <T>	the type of dataset
     * @return		the new wrapper
     * @throws Exception	if wrapping fails
     */
    public <T extends Dataset> T as(Class<T> cls) throws Exception {
      Constructor<T>	constr;

      constr = cls.getConstructor(JsonObject.class);
      return constr.newInstance(m_Data);
    }

    /**
     * Returns a short description of the state.
     *
     * @return		the state
     */
    @Override
    public String toString() {
      return "pk=" + getPK() + ", name=" + getName() + ", licence=" + getLicence();
    }
  }

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "Datasets";
  }

  /**
   * For listing the datasets.
   *
   * @return		the list of datasets
   * @throws Exception	if request fails
   */
  public List<Dataset> list() throws Exception {
    List<Dataset>		result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing datasets");

    result   = new ArrayList<>();
    request  = newGet(PATH);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonArray()) {
        array = element.getAsJsonArray();
        for (i = 0; i < array.size(); i++)
          result.add(new Dataset(array.get(i).getAsJsonObject()));
      }
    }
    else {
      throw new FailedRequestException("Failed to list datasets!", response);
    }

    return result;
  }

  /**
   * For loading a specific dataset by primary key.
   *
   * @param pk 		the primary key of the dataset to load
   * @return		the dataset
   * @throws Exception	if request fails
   */
  public Dataset load(int pk) throws Exception {
    Dataset		result;
    JsonResponse 	response;
    JsonElement		element;
    Request 		request;

    getLogger().info("loading dataset with id: " + pk);

    result   = null;
    request  = newGet(PATH + pk);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new Dataset(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to load dataset: " + pk, response);
    }

    return result;
  }

  /**
   * For loading a specific dataset by name.
   *
   * @param name 	the dataset name
   * @return		the dataset object, null if failed to create
   * @throws Exception	if request fails
   */
  public Dataset load(String name) throws Exception {
    Dataset	result;

    getLogger().info("loading dataset with name: " + name);

    result = null;

    for (Dataset dataset : list()) {
      if (dataset.getName().equals(name)) {
        result = dataset;
        break;
      }
    }

    return result;
  }

  /**
   * For deleting a specific dataset.
   *
   * @param dataset 	the dataset to delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean delete(Dataset dataset) throws Exception {
    return delete(dataset.getPK());
  }

  /**
   * For deleting a specific dataset.
   *
   * @param pk 		the ID of the dataset
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid user PK
   */
  public boolean delete(int pk) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("deleting dataset with PK: " + pk);

    request  = newDelete(PATH + pk + "/");
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete dataset: " + pk, response);
  }
}
