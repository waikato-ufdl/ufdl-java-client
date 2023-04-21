/*
 * DockerImages.java
 * Copyright (C) 2020-2023 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.core.MediaTypeHelper;
import com.github.fracpete.requests4j.request.Request;
import com.github.fracpete.requests4j.response.JsonResponse;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapperWithPK;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonObjectWithShortDescription;
import com.github.waikatoufdl.ufdl4j.core.JsonUtils;
import com.github.waikatoufdl.ufdl4j.core.Utils;
import com.github.waikatoufdl.ufdl4j.filter.Filter;
import com.github.waikatoufdl.ufdl4j.filter.NameAndVersionFilter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
    extends AbstractJsonObjectWrapperWithPK
    implements JsonObjectWithShortDescription {

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
     * Returns the CUDA version.
     *
     * @return		the version
     */
    public CudaVersions.CudaVersion getCudaVersion() {
      if (m_Data.has("cuda_version") && !m_Data.get("cuda_version").isJsonNull())
        return new CudaVersions.CudaVersion(m_Data.getAsJsonObject("cuda_version"));
      else
        return null;
    }

    /**
     * Returns the framework.
     *
     * @return		the framework
     */
    public Frameworks.Framework getFramework() {
      return new Frameworks.Framework(m_Data.getAsJsonObject("framework"));
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
     * Returns the tasks.
     *
     * @return		the tasks
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
     * Returns the minimum hardware generation.
     *
     * @return		the generation, can be null if it runs on cpu
     */
    public HardwareGenerations.HardwareGeneration getMinHardwareGeneration() {
      if (m_Data.has("min_hardware_generation") && !m_Data.get("min_hardware_generation").isJsonNull())
        return new HardwareGenerations.HardwareGeneration(m_Data.getAsJsonObject("min_hardware_generation"));
      else
        return null;
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
    public String getLicense() {
      if (m_Data.has("licence"))
        return m_Data.get("licence").getAsString();
      else
        return null;
    }

    /**
     * Returns the short description.
     *
     * @return		the short description
     */
    @Override
    public String getShortDescription() {
      return getName() + "/" + getVersion();
    }

    /**
     * Returns a short description of the state.
     *
     * @return		the state
     */
    @Override
    public String toString() {
      return "pk=" + getPK() + ", name=" + getName() + ", version=" + getVersion() + ", domain=" + getDomain()
        + ", cuda=" + getCudaVersion() + ", license=" + getLicense() + ", minHardware=" + getMinHardwareGeneration()
        + ", cpu=" + getCPU()
        + ", tasks=[" + Utils.arrayToString(getTasks()) + "], framework=[" + getFramework() + "]";
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
    return "/v1/docker/";
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
    request  = newPost(getPath() + "list");
    if (filter != null)
      request.body(filter.toJsonObject().toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
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
    JsonResponse response;
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
   * @param cudaVersion the minimum cuda version, eg 10.0
   * @param framework 	the framework PK
   * @param domain 	the domain this image applies to
   * @param tasks 	the tasks this image is for, eg Train, Predict, Export
   * @param minHardware	the minimum hardware generation, eg Fermi
   * @param cpu 	whether image runs on CPU as well
   * @param license 	the license name, eg GPL3
   * @return		the DockerImage object, null if failed to create
   * @throws Exception	if request fails or docker image already exists
   */
  public DockerImage create(String name, String version, String url,
                            String registryUrl, String registryUser, String registryPassword,
                            String cudaVersion, int framework, String domain,
                            String[] tasks, String minHardware, boolean cpu,
                            String license) throws Exception {
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
    data.addProperty("registry_username", registryUser);
    data.addProperty("registry_password", registryPassword);
    data.addProperty("cuda_version", cudaVersion);
    data.addProperty("framework", framework);
    data.addProperty("domain", domain);
    data.add("tasks", JsonUtils.toArray(Arrays.asList(tasks)));
    data.addProperty("min_hardware_generation", minHardware);
    data.addProperty("cpu", cpu);
    data.addProperty("licence", license);
    request = newPost(getPath() + "create")
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
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
   * @param cudaVersion the new minimum cuda version, eg 10.0
   * @param framework 	the new framework PK
   * @param domain 	the new domain this image applies to
   * @param tasks 	the new task this image is for, eg: Train, Predict, Export
   * @param minHardware	the new minimum hardware generation, eg Fermi
   * @param cpu 	the new whether image runs on CPU as well
   * @param license 	the new license name, eg GPL3
   * @return		the dockerImage object
   * @throws Exception	if request fails
   */
  public DockerImage update(DockerImage obj, String name, String version, String url,
                            String registryUrl, String registryUser, String registryPassword,
                            String cudaVersion, int framework, String domain,
                            String[] tasks, String minHardware, boolean cpu,
                            String license) throws Exception {
    return update(obj.getPK(), name, version, url,
      registryUrl, registryUser, registryPassword,
      cudaVersion, framework, domain,
      tasks, minHardware, cpu,
      license);
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
   * @param cudaVersion the new minimum cuda version, eg 10.0
   * @param framework 	the new framework PK
   * @param domain 	the new domain this image applies to
   * @param tasks 	the new task this image is for, eg: Train, Predict, Export
   * @param minHardware	the new minimum hardware generation, eg Fermi
   * @param cpu 	the new whether image runs on CPU as well
   * @param license 	the new license name, eg GPL3
   * @return		the dockerImage object
   * @throws Exception	if request fails
   */
  public DockerImage update(int pk, String name, String version, String url,
                            String registryUrl, String registryUser, String registryPassword,
                            String cudaVersion, int framework, String domain,
                            String[] tasks, String minHardware, boolean cpu,
                            String license) throws Exception {
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
    data.addProperty("registry_username", registryUser);
    data.addProperty("registry_password", registryPassword);
    data.addProperty("cuda_version", cudaVersion);
    data.addProperty("framework", framework);
    data.addProperty("domain", domain);
    data.add("tasks", JsonUtils.toArray(Arrays.asList(tasks)));
    data.addProperty("min_hardware_generation", minHardware);
    data.addProperty("cpu", cpu);
    data.addProperty("licence", license);
    request = newPut(getPath() + pk)
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
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
   * @param cudaVersion the new minimum cuda version, eg 10.0, ignored if null
   * @param framework 	the new framework PK, ignored if null
   * @param domain 	the new domain this image applies to, ignored if null
   * @param tasks 	the new tasks this image is for, eg Train, Predict Export, ignored if null
   * @param minHardware	the new minimum hardware generation, eg Fermi, ignored if null
   * @param cpu 	the new whether image runs on CPU as well, ignored if null
   * @param license 	the new license, eg GPL3, ignored if null
   * @return		the docker image object, null if failed to create
   * @throws Exception	if request fails
   */
  public DockerImage partialUpdate(DockerImage obj, String name, String version, String url,
                                   String registryUrl, String registryUser, String registryPassword,
                                   String cudaVersion, Integer framework, String domain,
                                   String[] tasks, String minHardware, Boolean cpu,
                                   String license) throws Exception {
    return partialUpdate(obj.getPK(), name, version, url,
      registryUrl, registryUser, registryPassword,
      cudaVersion, framework, domain,
      tasks, minHardware, cpu,
      license);
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
   * @param cudaVersion the new minimum cuda version, eg 10.0, ignored if null
   * @param framework 	the new framework PK, ignored if null
   * @param domain 	the new domain this image applies to, ignored if null
   * @param tasks 	the new tasks this image is for, eg Train, Predict Export, ignored if null
   * @param minHardware	the new minimum hardware generation, eg Fermi, ignored if null
   * @param cpu 	the new whether image runs on CPU as well, ignored if null
   * @param license 	the new license, eg GPL3, ignored if null
   * @return		the docker image object, null if failed to create
   * @throws Exception	if request fails
   */
  public DockerImage partialUpdate(int pk, String name, String version, String url,
                                   String registryUrl, String registryUser, String registryPassword,
                                   String cudaVersion, Integer framework, String domain,
                                   String[] tasks, String minHardware, Boolean cpu,
                                   String license) throws Exception {
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
      data.addProperty("registry_username", registryUser);
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
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
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

    request  = newDelete(getPath() + pk);
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete docker image: " + pk, response);
  }
}
