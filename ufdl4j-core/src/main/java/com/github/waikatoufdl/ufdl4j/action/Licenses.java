/*
 * Licenses.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapper;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Encapsulates user operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Licenses
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  /**
   * The available permissions.
   */
  public enum Permission {
    DISTRIBUTION,
    PATENT_USE,
    PRIVATE_USE,
    MODIFICATION,
    COMMERCIAL_USE;

    public static Permission parse(String s) {
      return valueOf(s.toUpperCase().replace(" ", "_"));
    }
  }

  /**
   * The available conditions.
   */
  public enum Condition {
    LICENSE_AND_COPYRIGHT_NOTICE,
    SAME_LICENSE,
    NETWORK_USE_IS_DISTRIBUTION,
    STATE_CHANGES,
    DISCLOSE_SOURCE;

    public static Condition parse(String s) {
      return valueOf(s.toUpperCase().replace(" ", "_"));
    }
  }

  /**
   * The available limitations.
   */
  public enum Limitation {
    LIABILITY,
    WARRANTY;

    public static Limitation parse(String s) {
      return valueOf(s.toUpperCase().replace(" ", "_"));
    }
  }

  /**
   * Container class for license information.
   */
  public static class License
    extends AbstractJsonObjectWrapper {

    private static final long serialVersionUID = 3523630902439390574L;

    /**
     * Initializes the user.
     *
     * @param data	the data to use
     */
    public License(JsonObject data) {
      super(data);
    }

    /**
     * Returns the user primary key.
     *
     * @return		the primary key
     */
    public int getPK() {
      return getInt("pk");
    }

    /**
     * Returns the user name.
     *
     * @return		the user
     */
    public String getName() {
      return getString("name", "");
    }

    /**
     * Returns the first name.
     *
     * @return		the first name
     */
    public String getURL() {
      return getString("url", "");
    }

    /**
     * Returns the permissions.
     *
     * @return		the permissions
     */
    public Set<Permission> getPermissions() {
      Set<Permission>	result;
      List 		list;

      result = new HashSet<>();

      if (m_Data.has("permissions")) {
	list = getList("permissions");
	for (Object item : list) {
	  try {
	    result.add(Permission.parse("" + item));
	  }
	  catch (Exception e) {
	    getLogger().warning("Unknown permission: " + item);
	  }
	}
      }

      return result;
    }

    /**
     * Returns the conditions.
     *
     * @return		the conditions
     */
    public Set<Condition> getConditions() {
      Set<Condition>	result;
      List 		list;

      result = new HashSet<>();

      if (m_Data.has("conditions")) {
	list = getList("conditions");
	for (Object item : list) {
	  try {
	    result.add(Condition.parse("" + item));
	  }
	  catch (Exception e) {
	    getLogger().warning("Unknown condition: " + item);
	  }
	}
      }

      return result;
    }

    /**
     * Returns the permissions.
     *
     * @return		the permissions
     */
    public Set<Limitation> getLimitations() {
      Set<Limitation>	result;
      List 		list;

      result = new HashSet<>();

      if (m_Data.has("limitations")) {
	list = getList("limitations");
	for (Object item : list) {
	  try {
	    result.add(Limitation.parse("" + item));
	  }
	  catch (Exception e) {
	    getLogger().warning("Unknown limitation: " + item);
	  }
	}
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
      return "pk=" + getPK() + ", user=" + getName() + ", permissions=" + getPermissions() + ", conditions=" + getConditions() + ", limitations=" + getLimitations();
    }
  }

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "Licenses";
  }

  /**
   * The URL path to use.
   *
   * @return		the path
   */
  @Override
  public String getPath() {
    return "/v1/core/licences/";
  }

  /**
   * For listing the licenses.
   *
   * @return		the list of licenses
   * @throws Exception	if request fails
   */
  public List<License> list() throws Exception {
    List<License>	result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing licenses");

    result   = new ArrayList<>();
    request  = newGet(getPath());
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonArray()) {
        array = element.getAsJsonArray();
        for (i = 0; i < array.size(); i++)
          result.add(new License(array.get(i).getAsJsonObject()));
      }
    }
    else {
      throw new FailedRequestException("Failed to list licenses!", response);
    }

    return result;
  }

  /**
   * For loading a specific license by primary key.
   *
   * @param pk 		the primary key of the license to load
   * @return		the license
   * @throws Exception	if request fails
   */
  public License load(int pk) throws Exception {
    License		result;
    JsonResponse 	response;
    JsonElement		element;
    Request 		request;

    getLogger().info("loading license with id: " + pk);

    result   = null;
    request  = newGet(getPath() + pk);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new License(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to load license: " + pk, response);
    }

    return result;
  }

  /**
   * For loading a specific license by name.
   *
   * @param name 	the license name
   * @return		the license object, null if failed to create
   * @throws Exception	if request fails
   */
  public License load(String name) throws Exception {
    License	result;

    getLogger().info("loading license with name: " + name);

    result = null;

    for (License license : list()) {
      if (license.getName().equals(name)) {
        result = license;
        break;
      }
    }

    return result;
  }
}
