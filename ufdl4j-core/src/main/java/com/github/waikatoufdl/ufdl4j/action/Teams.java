/*
 * Teams.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.core.MediaTypeHelper;
import com.github.fracpete.requests4j.request.Request;
import com.github.fracpete.requests4j.response.JsonResponse;
import com.github.waikatoufdl.ufdl4j.action.Users.User;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapperWithPK;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonObjectWithName;
import com.github.waikatoufdl.ufdl4j.core.JsonObjectWithShortDescription;
import com.github.waikatoufdl.ufdl4j.core.SoftDeleteObject;
import com.github.waikatoufdl.ufdl4j.filter.Filter;
import com.github.waikatoufdl.ufdl4j.filter.NameFilter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates team operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Teams
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  /**
   * Container class for team information.
   */
  public static class Team
    extends AbstractJsonObjectWrapperWithPK
    implements SoftDeleteObject, JsonObjectWithShortDescription, JsonObjectWithName {

    private static final long serialVersionUID = 3523630902439390574L;

    /**
     * Initializes the team.
     *
     * @param data	the data to use
     */
    public Team(JsonObject data) {
      super(data);
    }

    /**
     * Returns the team primary key.
     *
     * @return		the primary key
     */
    @Override
    public int getPK() {
      return getInt("pk");
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
     * Returns the team name.
     *
     * @return		the name
     */
    @Override
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
     * Returns the members of the team (names).
     *
     * @return		the members
     */
    public List<String> members() {
      List<String>	result;
      List		list;
      JsonObject	jobj;

      result = new ArrayList<>();
      list = getList("members", null);
      if (list != null) {
        for (Object o: list) {
          jobj = (JsonObject) o;
          result.add(jobj.get("username").getAsString());
        }
      }

      return result;
    }

    /**
     * Returns the members of the team (PKs).
     *
     * @return		the members
     */
    public List<Integer> membersPK() {
      List<Integer>	result;
      List		list;
      JsonObject	jobj;

      result = new ArrayList<>();
      list = getList("members", null);
      if (list != null) {
        for (Object o: list) {
          jobj = (JsonObject) o;
          result.add(jobj.get("pk").getAsInt());
        }
      }

      return result;
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
      return "pk=" + getPK() + ", name=" + getName() + ", members=" + members();
    }
  }

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "Teams";
  }

  /**
   * The URL path to use.
   *
   * @return		the path
   */
  @Override
  public String getPath() {
    return "/v1/teams/";
  }

  /**
   * For listing the teams.
   *
   * @return		the list of teams
   * @throws Exception	if request fails
   */
  public List<Team> list() throws Exception {
    return list(null);
  }

  /**
   * For listing the teams.
   *
   * @param filter 	the filter to apply, can be null
   * @return		the list of teams
   * @throws Exception	if request fails
   */
  public List<Team> list(Filter filter) throws Exception {
    List<Team>		result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing teams" + (filter == null ? "" : ", filter: " + filter.toJsonObject()));

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
          result.add(new Team(array.get(i).getAsJsonObject()));
      }
    }
    else {
      throw new FailedRequestException("Failed to list teams!" + (filter == null ? "" : "\nFilter: " + filter.toJsonObject()), response);
    }

    return result;
  }

  /**
   * For loading a specific team by primary key.
   *
   * @param pk 		the primary key of the team to load
   * @return		the team
   * @throws Exception	if request fails
   */
  public Team load(int pk) throws Exception {
    Team		result;
    JsonResponse 	response;
    JsonElement		element;
    Request 		request;

    getLogger().info("loading team with id: " + pk);

    result   = null;
    request  = newGet(getPath() + pk);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new Team(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to load team: " + pk, response);
    }

    return result;
  }

  /**
   * For loading a specific team by name.
   *
   * @param name 	the team name
   * @return		the team object, null if failed to locate
   * @throws Exception	if request fails
   */
  public Team load(String name) throws Exception {
    Team	result;

    getLogger().info("loading team with name: " + name);

    result = null;

    for (Team team : list(new NameFilter(name))) {
      result = team;
      break;
    }

    if (result == null)
      getLogger().warning("failed to load team: " + name);

    return result;
  }

  /**
   * Creates the team.
   *
   * @param team 	the team name
   * @return		the team object, null if failed to create
   * @throws Exception	if request fails or team already exists
   */
  public Team create(String team) throws Exception {
    Team		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("creating team: " + team);

    data = new JsonObject();
    data.addProperty("name", team);
    request = newPost(getPath() + "create")
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok())
      result = new Team(response.jsonObject());
    else
      throw new FailedRequestException("Failed to create team: " + team, response);

    return result;
  }

  /**
   * Updates the team.
   *
   * @param teamObj 	the team to update
   * @param team 	the new team name
   * @return		the team object
   * @throws Exception	if request fails
   */
  public Team update(Team teamObj, String team) throws Exception {
    return update(teamObj.getPK(), team);
  }

  /**
   * Updates the team.
   *
   * @param pk 		the PK of the team to update
   * @param team 	the new team name
   * @return		the team object
   * @throws Exception	if request fails
   */
  public Team update(int pk, String team) throws Exception {
    Team		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("updating team: " + pk);

    data = new JsonObject();
    data.addProperty("name", team);
    request = newPut(getPath() + pk)
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok())
      result = new Team(response.jsonObject());
    else
      throw new FailedRequestException("Failed to update team: " + pk, response);

    return result;
  }

  /**
   * For deleting a specific team.
   *
   * @param team 	the team to delete
   * @param hard 	whether hard or soft delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid team PK
   */
  public boolean delete(Team team, boolean hard) throws Exception {
    return delete(team.getPK(), hard);
  }

  /**
   * For deleting a specific team.
   *
   * @param pk 		the ID of the team
   * @param hard 	whether hard or soft delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid team PK
   */
  public boolean delete(int pk, boolean hard) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("deleting team with PK (hard=" + hard + "): " + pk);

    request  = newDelete(getPath() + pk + (hard ? "/hard" : "/"));
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete team (hard=" + hard + "): " + pk, response);
  }

  /**
   * For reinstating a specific team.
   *
   * @param team 	the team to reinstate
   * @return		true if successfully reinstated
   * @throws Exception	if request fails, eg invalid team PK
   */
  public boolean reinstate(Team team) throws Exception {
    return reinstate(team.getPK());
  }

  /**
   * For reinstating a specific team.
   *
   * @param pk 		the ID of the team
   * @return		true if successfully reinstated
   * @throws Exception	if request fails, eg invalid team PK
   */
  public boolean reinstate(int pk) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("Reinstating team with PK: " + pk);

    request  = newDelete(getPath() + pk + "/reinstate");
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to reinstate team: " + pk, response);
  }

  /**
   * Adds the membership of a user to a team.
   *
   * @param team 	the team to add the user to
   * @param user 	the user to add
   * @param permissions the permissions to use
   * @throws Exception	if request fails, eg invalid team PK
   */
  public boolean addMembership(Team team, User user, Permissions permissions) throws Exception {
    return addMembership(team.getPK(), user, permissions);
  }

  /**
   * Adds the membership of a user to a team.
   *
   * @param pk 		the team PK to add the user to
   * @param user 	the user to add
   * @param permissions the permissions to use
   * @throws Exception	if request fails, eg invalid team PK
   */
  public boolean addMembership(int pk, User user, Permissions permissions) throws Exception {
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("adding membership of user '" + user.getUserName() + "' to team '" + pk);

    data     = new JsonObject();
    data.addProperty("method", "add");
    data.addProperty("username", user.getUserName());
    data.addProperty("permissions", permissions.toString());
    request  = newPatch(getPath() + pk + "/memberships")
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to add user '" + user.getUserName() + "' to team '" + pk + "'!", response);
  }

  /**
   * Updates the membership of user in a team.
   *
   * @param team 	the team to add the user to
   * @param user 	the user to add
   * @param permissions the permissions to use
   * @throws Exception	if request fails, eg invalid team PK
   */
  public boolean updateMembership(Team team, User user, Permissions permissions) throws Exception {
    return updateMembership(team.getPK(), user, permissions);
  }

  /**
   * Updates the membership of user in a team.
   *
   * @param pk 		the team PK to add the user to
   * @param user 	the user to add
   * @param permissions the permissions to use
   * @throws Exception	if request fails, eg invalid team PK
   */
  public boolean updateMembership(int pk, User user, Permissions permissions) throws Exception {
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("updating membership of user '" + user.getUserName() + "' in team '" + pk);

    data     = new JsonObject();
    data.addProperty("method", "update");
    data.addProperty("username", user.getUserName());
    data.addProperty("permissions", permissions.toString());
    request  = newPatch(getPath() + pk + "/memberships")
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to update membership of user '" + user.getUserName() + "' in team '" + pk + "'!", response);
  }

  /**
   * Removes the membership of user from a team.
   *
   * @param team 	the team to add the user to
   * @param user 	the user to add
   * @throws Exception	if request fails, eg invalid team PK
   */
  public boolean removeMembership(Team team, User user) throws Exception {
    return removeMembership(team.getPK(), user);
  }

  /**
   * Removes the membership of user from a team.
   *
   * @param pk 		the team pk to remove the user from
   * @param user 	the user to add
   * @throws Exception	if request fails, eg invalid team PK
   */
  public boolean removeMembership(int pk, User user) throws Exception {
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("removing membership of user '" + user.getUserName() + "' from team '" + pk);

    data     = new JsonObject();
    data.addProperty("method", "remove");
    data.addProperty("username", user.getUserName());
    request  = newPatch(getPath() + pk + "/memberships")
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to remove membership of user '" + user.getUserName() + "' from team '" + pk + "'!", response);
  }
}
