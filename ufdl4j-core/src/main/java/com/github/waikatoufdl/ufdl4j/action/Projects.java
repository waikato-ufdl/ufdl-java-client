/*
 * Projects.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.core.MediaTypeHelper;
import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapperWithPK;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonObjectWithShortDescription;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;
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
 * Encapsulates project operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Projects
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  /**
   * Container class for project information.
   */
  public static class Project
    extends AbstractJsonObjectWrapperWithPK
    implements SoftDeleteObject, JsonObjectWithShortDescription {

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
    public int getCreator() {
      return getInt("creator", -1);
    }

    /**
     * Returns the team ID.
     *
     * @return		the ID
     */
    public int getTeam() {
      return getInt("team", -1);
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
   * The URL path to use.
   *
   * @return		the path
   */
  @Override
  public String getPath() {
    return "/v1/core/projects/";
  }

  /**
   * For listing the projects.
   *
   * @return		the list of projects
   * @throws Exception	if request fails
   */
  public List<Project> list() throws Exception {
    return list(null);
  }

  /**
   * For listing the projects.
   *
   * @param filter 	the filter to apply, can be null
   * @return		the list of projects
   * @throws Exception	if request fails
   */
  public List<Project> list(Filter filter) throws Exception {
    List<Project>	result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing projects" + (filter == null ? "" : ", filter: " + filter.toJsonObject()));

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
          result.add(new Project(array.get(i).getAsJsonObject()));
      }
    }
    else {
      throw new FailedRequestException("Failed to list projects!" + (filter == null ? "" : "\nFilter: " + filter.toJsonObject()), response);
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
    request  = newGet(getPath() + pk);
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

    for (Project project : list(new NameFilter(name))) {
      result = project;
      break;
    }

    if (result == null)
      getLogger().warning("failed to load project: " + name);

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
    request = newPost(getPath() + "create")
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok())
      result = new Project(response.jsonObject());
    else
      throw new FailedRequestException("Failed to create project: " + project, response);

    return result;
  }

  /**
   * Updates the project.
   *
   * @param projectObj	the project to update
   * @param project 	the project name
   * @param team	the team PK
   * @return		the project object, null if failed to create
   * @throws Exception	if request fails or project already exists
   */
  public Project update(Project projectObj, String project, int team) throws Exception {
    return update(projectObj.getPK(), project, team);
  }

  /**
   * Updates the project.
   *
   * @param pk 		the PK of the project to update
   * @param project 	the project name
   * @param team	the team PK
   * @return		the project object, null if failed to create
   * @throws Exception	if request fails or project already exists
   */
  public Project update(int pk, String project, int team) throws Exception {
    Project		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("updating project: " + pk);

    data = new JsonObject();
    data.addProperty("name", project);
    data.addProperty("team", team);
    request = newPut(getPath() + pk)
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok())
      result = new Project(response.jsonObject());
    else
      throw new FailedRequestException("Failed to update project: " + pk, response);

    return result;
  }

  /**
   * (Partially) Updates the project using the null values.
   *
   * @param projectObj	the project to update
   * @param project 	the project name
   * @param team	the team PK
   * @return		the project object, null if failed to create
   * @throws Exception	if request fails or project already exists
   */
  public Project partialUpdate(Project projectObj, String project, Integer team) throws Exception {
    return partialUpdate(projectObj.getPK(), project, team);
  }

  /**
   * (Partially) Updates the project using the null values.
   *
   * @param pk 		the PK of the project to update
   * @param project 	the project name
   * @param team	the team PK
   * @return		the project object, null if failed to create
   * @throws Exception	if request fails or project already exists
   */
  public Project partialUpdate(int pk, String project, Integer team) throws Exception {
    Project		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("partially updating project: " + pk);

    data = new JsonObject();
    if (project != null)
      data.addProperty("name", project);
    if (team != null)
      data.addProperty("team", team);
    request = newPatch(getPath() + pk)
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok())
      result = new Project(response.jsonObject());
    else
      throw new FailedRequestException("Failed to partially update project: " + pk, response);

    return result;
  }

  /**
   * For deleting a specific project.
   *
   * @param project 	the project to delete
   * @param hard 	whether hard or soft delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid user PK
   */
  public boolean delete(Project project, boolean hard) throws Exception {
    return delete(project.getPK(), hard);
  }

  /**
   * For deleting a specific project.
   *
   * @param pk 		the ID of the project
   * @param hard 	whether hard or soft delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid user PK
   */
  public boolean delete(int pk, boolean hard) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("deleting project with PK (hard=" + hard + "): " + pk);

    request  = newDelete(getPath() + pk + (hard ? "/hard" : "/"));
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete project (hard=" + hard + "): " + pk, response);
  }

  /**
   * For reinstating a specific project.
   *
   * @param project 	the project to reinstate
   * @return		true if successfully reinstated
   * @throws Exception	if request fails, eg invalid project PK
   */
  public boolean reinstate(Project project) throws Exception {
    return reinstate(project.getPK());
  }

  /**
   * For reinstating a specific project.
   *
   * @param pk 		the ID of the project
   * @return		true if successfully reinstated
   * @throws Exception	if request fails, eg invalid project PK
   */
  public boolean reinstate(int pk) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("Reinstating project with PK: " + pk);

    request  = newDelete(getPath() + pk + "/reinstate");
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to reinstate project: " + pk, response);
  }
}
