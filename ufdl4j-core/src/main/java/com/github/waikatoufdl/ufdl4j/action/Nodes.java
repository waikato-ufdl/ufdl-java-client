/*
 * Nodes.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapper;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;
import com.github.waikatoufdl.ufdl4j.filter.AbstractExpression;
import com.github.waikatoufdl.ufdl4j.filter.Filter;
import com.github.waikatoufdl.ufdl4j.filter.GenericFilter;
import com.github.waikatoufdl.ufdl4j.filter.field.ExactString;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.entity.ContentType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates node operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Nodes
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  /**
   * Container class for node information.
   */
  public static class Node
    extends AbstractJsonObjectWrapper {

    private static final long serialVersionUID = 3523630902439390574L;

    /**
     * Initializes the node.
     *
     * @param data	the data to use
     */
    public Node(JsonObject data) {
      super(data);
    }

    /**
     * Returns the node primary key.
     *
     * @return		the primary key
     */
    public int getPK() {
      return getInt("pk");
    }

    /**
     * Returns the IP.
     *
     * @return		the IP
     */
    public String getIP() {
      return getString("ip");
    }

    /**
     * Returns the driver version.
     *
     * @return		the version
     */
    public String getDriverVersion() {
      return getString("driver_version");
    }

    /**
     * Returns the hardware generation.
     *
     * @return		the generation
     */
    public int getHardwareGeneration() {
      return getInt("hardware_generation");
    }

    /**
     * Returns the GPU memory.
     *
     * @return		the memory
     */
    public int getGPUMemory() {
      return getInt("gpu_mem");
    }

    /**
     * Returns the CPU memory.
     *
     * @return		the memory
     */
    public int getCPUMemory() {
      return getInt("cpu_mem");
    }

    /**
     * Returns the last seen time.
     *
     * @return		the date, can be null
     */
    public LocalDateTime getLastSeen() {
      return getDateTime("last_seen", null);
    }

    /**
     * Returns a short description of the state.
     *
     * @return		the state
     */
    @Override
    public String toString() {
      return "pk=" + getPK() + ", ip=" + getIP() + ", driver=" + getDriverVersion() + ", generation=" + getHardwareGeneration() + ", gpu=" + getGPUMemory() + ", cpu=" + getCPUMemory();
    }
  }

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "Nodes";
  }

  /**
   * The URL path to use.
   *
   * @return		the path
   */
  @Override
  public String getPath() {
    return "/v1/core/nodes/";
  }

  /**
   * For listing the nodes.
   *
   * @return		the list of nodes
   * @throws Exception	if request fails
   */
  public List<Node> list() throws Exception {
    return list(null);
  }

  /**
   * For listing the nodes.
   *
   * @param filter 	the filter to apply, can be null
   * @return		the list of nodes
   * @throws Exception	if request fails
   */
  public List<Node> list(Filter filter) throws Exception {
    List<Node>		result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing nodes" + (filter == null ? "" : ", filter: " + filter.toJsonObject()));

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
          result.add(new Node(array.get(i).getAsJsonObject()));
      }
    }
    else {
      throw new FailedRequestException("Failed to list nodes!" + (filter == null ? "" : "\nFilter: " + filter.toJsonObject()), response);
    }

    return result;
  }

  /**
   * For loading a specific node by primary key.
   *
   * @param pk 		the primary key of the node to load
   * @return		the node
   * @throws Exception	if request fails
   */
  public Node load(int pk) throws Exception {
    Node		result;
    JsonResponse 	response;
    JsonElement		element;
    Request 		request;

    getLogger().info("loading node with id: " + pk);

    result   = null;
    request  = newGet(getPath() + pk);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new Node(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to load node: " + pk, response);
    }

    return result;
  }

  /**
   * For loading a specific node by IP.
   *
   * @param ip 		the IP
   * @return		the node object, null if failed to locate
   * @throws Exception	if request fails
   */
  public Node load(String ip) throws Exception {
    Node	result;
    Filter	filter;

    getLogger().info("loading node: " + ip);

    result = null;

    filter = new GenericFilter(
      new AbstractExpression[]{
        new ExactString("ip", ip),
      }
    );
    for (Node node : list(filter)) {
      result = node;
      break;
    }

    if (result == null)
      getLogger().warning("failed to load node: " + ip);

    return result;
  }

  /**
   * Creates a node object.
   *
   * @param ip		the IP address
   * @param generation 	the hardware generation
   * @param gpu 	the GPU memory
   * @param cpu 	the CPU memory
   * @param driver 	the driver version
   * @return		the Node object, null if failed to create
   * @throws Exception	if request fails or node already exists
   */
  public Node create(String ip, String driver, String generation, int gpu, int cpu) throws Exception {
    Node		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("creating node: " + driver);

    data = new JsonObject();
    data.addProperty("ip", ip);
    data.addProperty("driver_version", driver);
    data.addProperty("hardware_generation", generation);
    data.addProperty("gpu_mem", gpu);
    data.addProperty("cpu_mem", cpu);
    request = newPost(getPath())
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new Node(response.jsonObject());
    else
      throw new FailedRequestException("Failed to create node: " + driver, response);

    return result;
  }

  /**
   * Updates the node.
   *
   * @param obj 	the node to update
   * @param ip		the IP address
   * @param generation 	the hardware generation
   * @param gpu 	the GPU memory
   * @param cpu 	the CPU memory
   * @param driver 	the driver version
   * @return		the node object
   * @throws Exception	if request fails
   */
  public Node update(Node obj, String ip, String driver, String generation, int gpu, int cpu) throws Exception {
    return update(obj.getPK(), ip, driver, generation, gpu, cpu);
  }

  /**
   * Updates the node.
   *
   * @param pk 		the PK of the node to update
   * @param ip		the IP address
   * @param generation 	the hardware generation
   * @param gpu 	the GPU memory
   * @param cpu 	the CPU memory
   * @param driver 	the driver version
   * @return		the node object
   * @throws Exception	if request fails
   */
  public Node update(int pk, String ip, String driver, String generation, int gpu, int cpu) throws Exception {
    Node		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("updating node: " + pk);

    data = new JsonObject();
    data.addProperty("ip", ip);
    data.addProperty("driver_version", driver);
    data.addProperty("hardware_generation", generation);
    data.addProperty("gpu_mem", gpu);
    data.addProperty("cpu_mem", cpu);
    request = newPut(getPath() + pk)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new Node(response.jsonObject());
    else
      throw new FailedRequestException("Failed to update node: " + pk, response);

    return result;
  }

  /**
   * (Partially) Updates the node identified by the object, using only the non-null arguments.
   *
   * @param obj	the user to update
   * @param ip		the IP address, ignored if null
   * @param generation 	the hardware generation, ignored if null
   * @param gpu 	the GPU memory, ignored if null
   * @param cpu 	the CPU memory, ignored if null
   * @param driver 	the driver version, ignored if null
   * @return		the user object, null if failed to create
   * @throws Exception	if request fails
   */
  public Node partialUpdate(Node obj, String ip, String driver, String generation, Integer gpu, Integer cpu) throws Exception {
    return partialUpdate(obj.getPK(), ip, driver, generation, gpu, cpu);
  }

  /**
   * (Partially) Updates the node identified by the PK, using only the non-null arguments.
   *
   * @param pk 		the PK of the user to update
   * @param ip		the IP address, ignored if null
   * @param generation 	the hardware generation, ignored if null
   * @param gpu 	the GPU memory, ignored if null
   * @param cpu 	the CPU memory, ignored if null
   * @param driver 	the driver version, ignored if null
   * @return		the user object, null if failed to create
   * @throws Exception	if request fails
   */
  public Node partialUpdate(int pk, String ip, String driver, String generation, Integer gpu, Integer cpu) throws Exception {
    Node		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("partially updating node: " + pk);

    data = new JsonObject();
    if (ip != null)
      data.addProperty("ip", ip);
    if (driver != null)
      data.addProperty("driver_version", driver);
    if (generation != null)
      data.addProperty("hardware_generation", generation);
    if (gpu != null)
      data.addProperty("gpu_mem", gpu);
    if (cpu != null)
      data.addProperty("cpu_mem", cpu);
    request = newPatch(getPath() + pk)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new Node(response.jsonObject());
    else
      throw new FailedRequestException("Failed to partially update node: " + pk, response);

    return result;
  }

  /**
   * For deleting a specific node.
   *
   * @param node the node to delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid node PK
   */
  public boolean delete(Node node) throws Exception {
    return delete(node.getPK());
  }

  /**
   * For deleting a specific node.
   *
   * @param pk 		the ID of the node
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid node PK
   */
  public boolean delete(int pk) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("deleting node with PK: " + pk);

    request  = newDelete(getPath() + pk + "/");
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete node: " + pk, response);
  }
}
