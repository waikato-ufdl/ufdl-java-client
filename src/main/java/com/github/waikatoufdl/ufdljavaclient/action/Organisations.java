/*
 * Organisations.java
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
 * Encapsulates organisation operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Organisations
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  public static final String PATH = "/v1/core/organisations/";

  /**
   * Container class for organisation information.
   */
  public static class Organisation
    extends AbstractJsonObjectWrapper {

    private static final long serialVersionUID = 3523630902439390574L;

    /**
     * Initializes the membership.
     *
     * @param data	the data to use
     */
    public Organisation(JsonObject data) {
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
     * Returns the organisation name.
     *
     * @return		the name
     */
    public String getName() {
      return getString("name", "");
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
      return "id=" + getID() + ", name=" + getName();
    }
  }

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "Organisations";
  }

  /**
   * For listing the users.
   *
   * @return		the list of users
   * @throws Exception	if request fails
   */
  public List<Organisation> list() throws Exception {
    List<Organisation>		result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing organisations");

    result   = new ArrayList<>();
    request  = newGet(PATH);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonArray()) {
        array = element.getAsJsonArray();
        for (i = 0; i < array.size(); i++)
          result.add(new Organisation(array.get(i).getAsJsonObject()));
      }
    }
    else {
      throw new FailedRequestException("Failed to list organisations!", response);
    }

    return result;
  }
}
