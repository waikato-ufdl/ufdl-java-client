/*
 * DockerImages.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapper;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;
import com.github.waikatoufdl.ufdl4j.core.JsonUtils;
import com.github.waikatoufdl.ufdl4j.core.Utils;
import com.github.waikatoufdl.ufdl4j.filter.Filter;
import com.github.waikatoufdl.ufdl4j.filter.NameAndVersionFilter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.entity.ContentType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Encapsulates docker image operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class DockerImages
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  /**
   * Container class for docker image information.
   */
  public static class DockerImage
    extends AbstractJsonObjectWrapper {

    private static final long serialVersionUID = 3523630902439390574L;

    /**
     * Initializes the docker image.
     *
     * @param data	the data to use
     */
    public DockerImage(JsonObject data) {
      super(data);
    }

    /**
     * Returns the docker image primary key.
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
    public String getVersion() {
      return getString("version");
    }

    /**
     * Returns the url.
     *
     * @return		the url
     */
    public String getUrl() {
      return getString("url");
    }

    /**
     * Returns the registry URL.
     *
     * @return		the URL
     */
    public String getRegistryUrl() {
      return getString("registry_url");
    }

    /**
     * Returns the registry user name.
     *
     * @return		the user name
     */
    public String getRegistryUserName() {
      return getString("registry_username");
    }

    /**
     * Returns the registry password.
     *
     * @return		the password
     */
    public String getRegistryPassword() {
      return getString("registry_password");
    }

    /**
     * Returns the CUDA version PK.
     *
     * @return		the version
     */
    public int getCudaVersion() {
      return getInt("cuda_version");
    }

    /**
     * Returns the framework.
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
     * Returns the task.
     *
     * @return		the task
     */
    public String[] getTasks() {
      String[]	result;
      Object[]	tasks;
      int	i;

      tasks = JsonUtils.asList(getData(), "tasks", new ArrayList()).toArray();
      result = new String[tasks.length];
      for (i = 0; i < tasks.length; i++)
        result[i] = "" + tasks[i];

      return result;
    }

    /**
     * Returns the minimum hardware generation PK.
     *
     * @return		the generation
     */
    public int getMinHardwareGeneration() {
      return getInt("min_hardware_generation");
    }

    /**
     * Returns whether the image runs on a CPU as well.
     *
     * @return		whether runs on a CPU as well
     */
    public boolean getCPU() {
      return getBoolean("cpu");
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
     * Returns a short description of the state.
     *
     * @return		the state
     */
    @Override
    public String toString() {
      return "pk=" + getPK() + ", name=" + getName() + ", version=" + getVersion() + ", domain=" + getDomain() + ", tasks=" + Utils.arrayToString(getTasks());
    }
  }

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "Docker images";
  }

  /**
   * The URL path to use.
   *
   * @return		the path
   */
  @Override
  public String getPath() {
    return "/v1/core/docker/";
  }

  /**
   * For listing the docker images.
   *
   * @return		the list of docker images
   * @throws Exception	if request fails
   */
  public List<DockerImage> list() throws Exception {
    return list(null);
  }

  /**
   * For listing the docker images.
   *
   * @param filter 	the filter to apply, can be null
   * @return		the list of docker images
   * @throws Exception	if request fails
   */
  public List<DockerImage> list(Filter filter) throws Exception {
    List<DockerImage>		result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing docker images" + (filter == null ? "" : ", filter: " + filter.toJsonObject()));

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
          result.add(new DockerImage(array.get(i).getAsJsonObject()));
      }
    }
    else {
      throw new FailedRequestException("Failed to list docker images!" + (filter == null ? "" : "\nFilter: " + filter.toJsonObject()), response);
    }

    return result;
  }

  /**
   * For loading a specific docker image by primary key.
   *
   * @param pk 		the primary key of the dockerImage to load
   * @return		the dockerImage
   * @throws Exception	if request fails
   */
  public DockerImage load(int pk) throws Exception {
    DockerImage		result;
    JsonResponse 	response;
    JsonElement		element;
    Request 		request;

    getLogger().info("loading docker image with id: " + pk);

    result   = null;
    request  = newGet(getPath() + pk);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
        result = new DockerImage(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to load docker image: " + pk, response);
    }

    return result;
  }

  /**
   * For loading a specific docker image by name.
   *
   * @param name 	the name
   * @param version 	the version
   * @return		the docker image object, null if failed to locate
   * @throws Exception	if request fails
   */
  public DockerImage load(String name, String version) throws Exception {
    DockerImage	result;

    getLogger().info("loading docker image: " + name + "/" + version);

    result = null;

    for (DockerImage image : list(new NameAndVersionFilter(name, version))) {
      result = image;
      break;
    }

    if (result == null)
      getLogger().warning("failed to load docker image: " + name + "/" + version);

    return result;
  }

  /**
   * Creates a docker image object.
   *
   * @param name 	the name
   * @param version	the version
   * @param url 	the image url
   * @param registryUrl 	the url of the registry
   * @param registryUser 	the user to use for connecting to the registry
   * @param registryPassword 	the password to use for the registry
   * @param cudaVersion the minimum cuda version PK
   * @param framework 	the framework PK
   * @param domain 	the domain this image applies to
   * @param tasks 	the tasks this image is for
   * @param minHardware	the minimum hardware generation PK
   * @param cpu 	whether image runs on CPU as well
   * @param license 	the license PK
   * @return		the DockerImage object, null if failed to create
   * @throws Exception	if request fails or docker image already exists
   */
  public DockerImage create(String name, String version, String url,
                            String registryUrl, String registryUser, String registryPassword,
                            int cudaVersion, int framework, String domain,
                            String[] tasks, int minHardware, boolean cpu, int license) throws Exception {
    DockerImage		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("creating docker image: " + name);

    data = new JsonObject();
    data.addProperty("name", name);
    data.addProperty("version", version);
    data.addProperty("url", url);
    data.addProperty("registry_url", registryUrl);
    data.addProperty("registry_user", registryUser);
    data.addProperty("registry_password", registryPassword);
    data.addProperty("cuda_version", cudaVersion);
    data.addProperty("framework", framework);
    data.addProperty("domain", domain);
    data.add("tasks", JsonUtils.toArray(Arrays.asList(tasks)));
    data.addProperty("min_hardware_generation", minHardware);
    data.addProperty("cpu", cpu);
    data.addProperty("licence", license);
    request = newPost(getPath())
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new DockerImage(response.jsonObject());
    else
      throw new FailedRequestException("Failed to create docker image: " + name, response);

    return result;
  }

  /**
   * Updates the docker image.
   *
   * @param obj 	the docker image to update
   * @param name 	the new name
   * @param version	the new version
   * @param url 	the new image url
   * @param registryUrl 	the new url of the registry
   * @param registryUser 	the new user to use for connecting to the registry
   * @param registryPassword 	the new password to use for the registry
   * @param cudaVersion the new minimum cuda version PK
   * @param framework 	the new framework PK
   * @param domain 	the new domain this image applies to
   * @param tasks 	the new tasks this image is for
   * @param minHardware	the new minimum hardware generation PK
   * @param cpu 	the new whether image runs on CPU as well
   * @param license 	the new license PK
   * @return		the dockerImage object
   * @throws Exception	if request fails
   */
  public DockerImage update(DockerImage obj, String name, String version, String url,
                            String registryUrl, String registryUser, String registryPassword,
                            int cudaVersion, int framework, String domain,
                            String[] tasks, int minHardware, boolean cpu, int license) throws Exception {
    return update(obj.getPK(), name, version, url, registryUrl, registryUser, registryPassword,
      cudaVersion, framework, domain, tasks, minHardware, cpu, license);
  }

  /**
   * Updates the dockerImage.
   *
   * @param pk 		the PK of the dockerImage to update
   * @param name 	the new name
   * @param version	the new version
   * @param url 	the new image url
   * @param registryUrl 	the new url of the registry
   * @param registryUser 	the new user to use for connecting to the registry
   * @param registryPassword 	the new password to use for the registry
   * @param cudaVersion the new minimum cuda version PK
   * @param framework 	the new framework PK
   * @param domain 	the new domain this image applies to
   * @param tasks 	the new task this image is for
   * @param minHardware	the new minimum hardware generation PK
   * @param cpu 	the new whether image runs on CPU as well
   * @param license 	the new license PK
   * @return		the dockerImage object
   * @throws Exception	if request fails
   */
  public DockerImage update(int pk, String name, String version, String url,
                            String registryUrl, String registryUser, String registryPassword,
                            int cudaVersion, int framework, String domain,
                            String[] tasks, int minHardware, boolean cpu, int license) throws Exception {
    DockerImage		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("updating docker image: " + pk);

    data = new JsonObject();
    data.addProperty("name", name);
    data.addProperty("version", version);
    data.addProperty("url", url);
    data.addProperty("registry_url", registryUrl);
    data.addProperty("registry_user", registryUser);
    data.addProperty("registry_password", registryPassword);
    data.addProperty("cuda_version", cudaVersion);
    data.addProperty("framework", framework);
    data.addProperty("domain", domain);
    data.add("tasks", JsonUtils.toArray(Arrays.asList(tasks)));
    data.addProperty("min_hardware_generation", minHardware);
    data.addProperty("cpu", cpu);
    data.addProperty("licence", license);
    request = newPut(getPath() + pk)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new DockerImage(response.jsonObject());
    else
      throw new FailedRequestException("Failed to update docker image: " + pk, response);

    return result;
  }

  /**
   * (Partially) Updates the docker image identified by the object, using only the non-null arguments.
   *
   * @param obj	the user to update
   * @param name 	the new name, ignored if null
   * @param version	the new version, ignored if null
   * @param url 	the new image url, ignored if null
   * @param registryUrl 	the new url of the registry, ignored if null
   * @param registryUser 	the new user to use for connecting to the registry, ignored if null
   * @param registryPassword 	the new password to use for the registry, ignored if null
   * @param cudaVersion the new minimum cuda version PK, ignored if null
   * @param framework 	the new framework PK, ignored if null
   * @param domain 	the new domain this image applies to, ignored if null
   * @param tasks 	the new task this image is for, ignored if null
   * @param minHardware	the new minimum hardware generation PK, ignored if null
   * @param cpu 	the new whether image runs on CPU as well, ignored if null
   * @param license 	the new license PK, ignored if null
   * @return		the user object, null if failed to create
   * @throws Exception	if request fails
   */
  public DockerImage partialUpdate(DockerImage obj, String name, String version, String url,
                                   String registryUrl, String registryUser, String registryPassword,
                                   Integer cudaVersion, Integer framework, String domain,
                                   String[] tasks, Integer minHardware, Boolean cpu, Integer license) throws Exception {
    return partialUpdate(obj.getPK(), name, version, url, registryUrl, registryUser, registryPassword,
      cudaVersion, framework, domain, tasks, minHardware, cpu, license);
  }

  /**
   * (Partially) Updates the docker image identified by the PK, using only the non-null arguments.
   *
   * @param pk 		the PK of the user to update
   * @param name 	the new name, ignored if null
   * @param version	the new version, ignored if null
   * @param url 	the new image url, ignored if null
   * @param registryUrl 	the new url of the registry, ignored if null
   * @param registryUser 	the new user to use for connecting to the registry, ignored if null
   * @param registryPassword 	the new password to use for the registry, ignored if null
   * @param cudaVersion the new minimum cuda version, ignored if null
   * @param framework 	the new framework PK, ignored if null
   * @param domain 	the new domain this image applies to, ignored if null
   * @param tasks 	the new tasks this image is for, ignored if null
   * @param minHardware	the new minimum hardware generation, ignored if null
   * @param cpu 	the new whether image runs on CPU as well, ignored if null
   * @param license 	the new license PK, ignored if null
   * @return		the user object, null if failed to create
   * @throws Exception	if request fails
   */
  public DockerImage partialUpdate(int pk, String name, String version, String url,
                                   String registryUrl, String registryUser, String registryPassword,
                                   Integer cudaVersion, Integer framework, String domain,
                                   String[] tasks, Integer minHardware, Boolean cpu, Integer license) throws Exception {
    DockerImage		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("partially updating docker image: " + pk);

    data = new JsonObject();
    if (name != null)
      data.addProperty("name", name);
    if (version != null)
      data.addProperty("version", version);
    if (url != null)
      data.addProperty("url", url);
    if (registryUrl != null)
      data.addProperty("registry_url", registryUrl);
    if (registryUser != null)
      data.addProperty("registry_user", registryUser);
    if (registryPassword != null)
      data.addProperty("registry_password", registryPassword);
    if (cudaVersion != null)
      data.addProperty("cuda_version", cudaVersion);
    if (framework != null)
      data.addProperty("framework", framework);
    if (domain != null)
      data.addProperty("domain", domain);
    if (tasks != null)
      data.add("tasks", JsonUtils.toArray(Arrays.asList(tasks)));
    if (minHardware != null)
      data.addProperty("min_hardware_generation", minHardware);
    if (cpu != null)
      data.addProperty("cpu", cpu);
    if (license != null)
      data.addProperty("licence", license);
    request = newPatch(getPath() + pk)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new DockerImage(response.jsonObject());
    else
      throw new FailedRequestException("Failed to partially update docker image: " + pk, response);

    return result;
  }

  /**
   * For deleting a specific docker image.
   *
   * @param dockerImage the docker image to delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid dockerImage PK
   */
  public boolean delete(DockerImage dockerImage) throws Exception {
    return delete(dockerImage.getPK());
  }

  /**
   * For deleting a specific docker image.
   *
   * @param pk 		the ID of the docker image
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid dockerImage PK
   */
  public boolean delete(int pk) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("deleting docker image with PK: " + pk);

    request  = newDelete(getPath() + pk + "/");
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete docker image: " + pk, response);
  }
}
