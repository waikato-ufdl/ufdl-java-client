/*
 * Memberships.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdljavaclient.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdljavaclient.core.AbstractJsonObjectWrapper;
import com.github.waikatoufdl.ufdljavaclient.core.JsonResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates membership operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Memberships
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  public static final String PATH = "/v1/core/memberships/";

  /**
   * Container class for membership information.
   */
  public static class Membership
    extends AbstractJsonObjectWrapper {

    private static final long serialVersionUID = 3523630902439390574L;

    /**
     * Initializes the membership.
     *
     * @param data	the data to use
     */
    public Membership(JsonObject data) {
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
     * Returns the organisation ID.
     *
     * @return		the ID
     */
    public int getOrganisationID() {
      return getInt("organisation_id", -1);
    }

    /**
     * Returns the user ID.
     *
     * @return		the ID
     */
    public int getUserID() {
      return getInt("user_id", -1);
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
     * Returns the permissions.
     *
     * @return		the permissions
     */
    public String getPermissions() {
      return getString("permissions", "");
    }

    /**
     * Returns a short description of the state.
     *
     * @return		the state
     */
    @Override
    public String toString() {
      return "id=" + getID() + ", organisationID=" + getOrganisationID() + ", userID=" + getUserID() + ", permissions=" + getPermissions();
    }
  }

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "Memberships";
  }

  /**
   * For listing the users.
   *
   * @return		the list of users
   * @throws Exception	if request fails
   */
  public List<Membership> list() throws Exception {
    List<Membership>		result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing memberships");

    result   = new ArrayList<>();
    request  = newGet(PATH);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonArray()) {
        array = element.getAsJsonArray();
        for (i = 0; i < array.size(); i++)
          result.add(new Membership(array.get(i).getAsJsonObject()));
      }
    }
    return result;
  }
}
