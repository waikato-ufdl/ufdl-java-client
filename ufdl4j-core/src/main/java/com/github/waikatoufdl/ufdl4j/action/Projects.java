/*
 * Projects.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapper;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.entity.ContentType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates project operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Projects
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  public static final String PATH = "/v1/core/projects/";

  /**
   * Container class for project information.
   */
  public static class Project
    extends AbstractJsonObjectWrapper {

    private static final long serialVersionUID = 3523630902439390574L;

    /**
     * Initializes the project.
     *
     * @param data	the data to use
     */
    public Project(JsonObject data) {
      super(data);
    }

    /**
     * Returns the project primary key.
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
     * Returns the team ID.
     *
     * @return		the ID
     */
    public int getTeamID() {
      return getInt("team_id", -1);
    }

    /**
     * Returns the project name.
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
      return "pk=" + getPK() + ", name=" + getName();
    }
  }

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "Projects";
  }

  /**
   * For listing the projects.
   *
   * @return		the list of projects
   * @throws Exception	if request fails
   */
  public List<Project> list() throws Exception {
    List<Project>		result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing projects");

    result   = new ArrayList<>();
    request  = newGet(PATH);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonArray()) {
        array = element.getAsJsonArray();
        for (i = 0; i < array.size(); i++)
          result.add(new Project(array.get(i).getAsJsonObject()));
      }
    }
    else {
      throw new FailedRequestException("Failed to list projects!", response);
    }

    return result;
  }

  /**
   * For loading a specific project by primary key.
   *
   * @param pk 		the primary key of the project to load
   * @return		the project
   * @throws Exception	if request fails
   */
  public Project load(int pk) throws Exception {
    Project		result;
    JsonResponse 	response;
    JsonElement		element;
    Request 		request;

    getLogger().info("loading project with id: " + pk);

    result   = null;
    request  = newGet(PATH + pk);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new Project(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to load project: " + pk, response);
    }

    return result;
  }

  /**
   * For loading a specific project by name.
   *
   * @param name 	the project name
   * @return		the project object, null if failed to create
   * @throws Exception	if request fails
   */
  public Project load(String name) throws Exception {
    Project	result;

    getLogger().info("loading project with name: " + name);

    result = null;

    for (Project project : list()) {
      if (project.getName().equals(name)) {
        result = project;
        break;
      }
    }

    return result;
  }

  /**
   * Creates the project.
   *
   * @param project 	the project name
   * @param team	the team PK
   * @return		the project object, null if failed to create
   * @throws Exception	if request fails or project already exists
   */
  public Project create(String project, int team) throws Exception {
    Project		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("creating project: " + project);

    data = new JsonObject();
    data.addProperty("name", project);
    data.addProperty("team", team);
    request = newPost(PATH)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new Project(response.jsonObject());
    else
      throw new FailedRequestException("Failed to create project: " + project, response);

    return result;
  }

  /**
   * For deleting a specific project.
   *
   * @param project 	the project to delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid user PK
   */
  public boolean delete(Project project) throws Exception {
    return delete(project.getPK());
  }

  /**
   * For deleting a specific project.
   *
   * @param pk 		the ID of the project
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid user PK
   */
  public boolean delete(int pk) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("deleting project with PK: " + pk);

    request  = newDelete(PATH + pk + "/");
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete project: " + pk, response);
  }
}
