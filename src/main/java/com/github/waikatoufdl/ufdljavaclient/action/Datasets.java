/*
 * Datasets.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdljavaclient.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdljavaclient.core.AbstractJsonObjectWrapper;
import com.github.waikatoufdl.ufdljavaclient.core.FailedRequestException;
import com.github.waikatoufdl.ufdljavaclient.core.JsonResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
     * Returns the dataset ID.
     *
     * @return		the ID
     */
    public int getID() {
      return getInt("id", -1);
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
     * Returns a short description of the state.
     *
     * @return		the state
     */
    @Override
    public String toString() {
      return "id=" + getID() + ", name=" + getName() + ", licence=" + getLicence();
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
   * For listing the users.
   *
   * @return		the list of users
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
}
