/*
 * Teams.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdl4j.action.Users.User;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapper;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;
import com.github.waikatoufdl.ufdl4j.core.SoftDeleteObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.entity.ContentType;

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
    extends AbstractJsonObjectWrapper
    implements SoftDeleteObject {

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
    public int getPK() {
      return getInt("pk");
    }

    /**
     * Returns the creator ID.
     *
     * @return		the ID
     */
    public int getCreatorID() {
      return getInt("creator", -1);
    }

    /**
     * Returns the team name.
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
     * Returns the members of the team.
     *
     * @return		the members
     */
    public List<String> members() {
      List<String>	result;
      List		list;

      result = new ArrayList<>();
      list = getList("members", null);
      if (list != null) {
        for (Object o: list)
          result.add(o.toString());
      }

      return result;
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
    return "/v1/core/teams/";
  }

  /**
   * For listing the teams.
   *
   * @return		the list of teams
   * @throws Exception	if request fails
   */
  public List<Team> list() throws Exception {
    List<Team>		result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing teams");

    result   = new ArrayList<>();
    request  = newGet(getPath());
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
      throw new FailedRequestException("Failed to list teams!", response);
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
   * @return		the team object, null if failed to create
   * @throws Exception	if request fails
   */
  public Team load(String name) throws Exception {
    Team	result;

    getLogger().info("loading team with name: " + name);

    result = null;

    for (Team team : list()) {
      if (team.getName().equals(name)) {
        result = team;
        break;
      }
    }

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
    request = newPost(getPath())
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new Team(response.jsonObject());
    else
      throw new FailedRequestException("Failed to create team: " + team, response);

    return result;
  }

  /**
   * For deleting a specific team.
   *
   * @param team 	the team to delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid team PK
   */
  public boolean delete(Team team) throws Exception {
    return delete(team.getPK());
  }

  /**
   * For deleting a specific team.
   *
   * @param pk 		the ID of the team
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid team PK
   */
  public boolean delete(int pk) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("deleting team with PK: " + pk);

    request  = newDelete(getPath() + pk + "/");
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete team: " + pk, response);
  }

  /**
   * Adds the membership of a user to a team.
   *
   * @param team 	the team to add the user to
   * @param user 	the user to add
   * @param permissions the permissions to use
   */
  public boolean addMembership(Team team, User user, Permissions permissions) throws Exception {
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("adding membership of user '" + user.getUserName() + "' to team '" + getName());

    data     = new JsonObject();
    data.addProperty("method", "add");
    data.addProperty("username", user.getUserName());
    data.addProperty("permissions", permissions.toString());
    request  = newPatch(getPath() + team.getPK() + "/memberships")
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to add user '" + user.getUserName() + "' to team '" + getName() + "'!", response);
  }

  /**
   * Updates the membership of user in a team.
   *
   * @param team 	the team to add the user to
   * @param user 	the user to add
   * @param permissions the permissions to use
   */
  public boolean updateMembership(Team team, User user, Permissions permissions) throws Exception {
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("updating membership of user '" + user.getUserName() + "' in team '" + getName());

    data     = new JsonObject();
    data.addProperty("method", "update");
    data.addProperty("username", user.getUserName());
    data.addProperty("permissions", permissions.toString());
    request  = newPatch(getPath() + team.getPK() + "/memberships")
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to update membership of user '" + user.getUserName() + "' in team '" + getName() + "'!", response);
  }

  /**
   * Removes the membership of user from a team.
   *
   * @param team 	the team to add the user to
   * @param user 	the user to add
   */
  public boolean removeMembership(Team team, User user) throws Exception {
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("removing membership of user '" + user.getUserName() + "' from team '" + getName());

    data     = new JsonObject();
    data.addProperty("method", "remove");
    data.addProperty("username", user.getUserName());
    request  = newPatch(getPath() + team.getPK() + "/memberships")
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to remove membership of user '" + user.getUserName() + "' from team '" + getName() + "'!", response);
  }
}
