/*
 * Licenses.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapper;
import com.github.waikatoufdl.ufdl4j.core.CustomDisplayEnum;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;
import com.github.waikatoufdl.ufdl4j.core.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.entity.ContentType;

import java.util.ArrayList;
import java.util.Arrays;
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
  public enum Permission
    implements CustomDisplayEnum {

    DISTRIBUTION("Distribution"),
    PATENT_USE("Patent use"),
    PRIVATE_USE("Private use"),
    MODIFICATION("Modification"),
    COMMERCIAL_USE("Commercial use");

    /** the string to use for display. */
    private String m_Display;

    /** the commandline string. */
    private String m_Raw;

    /**
     * Initializes the enum with the name string.
     *
     * @param display	the string to use
     */
    private Permission(String display) {
      m_Display = display;
    }

    /**
     * Returns the display string.
     *
     * @return		the display string
     */
    @Override
    public String toDisplay() {
      return m_Display;
    }

    /**
     * Returns the raw enum string.
     *
     * @return		the raw enum string
     */
    @Override
    public String toRaw() {
      return m_Raw;
    }

    /**
     * Returns the display string.
     *
     * @return		the display string
     */
    @Override
    public String toString() {
      return m_Display;
    }

    /**
     * Parses the string.
     *
     * @param s		the string to parse
     * @return		the permission
     */
    public static Permission parse(String s) {
      for (Permission value : Permission.values()) {
        if (value.toDisplay().equals(s))
          return value;
      }

      // try raw
      for (Permission p : values()) {
	if (p.toRaw().equals(s))
	  return p;
      }

      return valueOf(s.toUpperCase().replace(" ", "_"));
    }

    /**
     * Turns the enum array into a string array of associated names.
     *
     * @param values	the values to convert
     * @return		the names
     */
    public static String[] toNames(Permission[] values) {
      List<String>	result;

      result = new ArrayList<>();
      for (Permission value: values)
        result.add(value.toDisplay());

      return result.toArray(new String[0]);
    }
  }

  /**
   * The available conditions.
   */
  public enum Condition
    implements CustomDisplayEnum {

    LICENSE_AND_COPYRIGHT_NOTICE("License and copyright notice"),
    SAME_LICENSE("Same license"),
    SAME_LICENSE_LIBRARY("Same license (library)"),
    SAME_LICENSE_FILE("Same license (file)"),
    NETWORK_USE_IS_DISTRIBUTION("Network use is distribution"),
    STATE_CHANGES("State changes"),
    DISCLOSE_SOURCE("Disclose source");

    /** the string to use for display. */
    private String m_Display;

    /** the commandline string. */
    private String m_Raw;

    /**
     * Initializes the enum with the name string.
     *
     * @param display	the string to use
     */
    private Condition(String display) {
      m_Display = display;
    }

    /**
     * Returns the display string.
     *
     * @return		the display string
     */
    @Override
    public String toDisplay() {
      return m_Display;
    }

    /**
     * Returns the raw enum string.
     *
     * @return		the raw enum string
     */
    @Override
    public String toRaw() {
      return m_Raw;
    }

    /**
     * Returns the display string.
     *
     * @return		the display string
     */
    @Override
    public String toString() {
      return m_Display;
    }

    /**
     * Parses the string.
     *
     * @param s		the string to parse
     * @return		the condition
     */
    public static Condition parse(String s) {
      for (Condition value : Condition.values()) {
        if (value.toDisplay().equals(s))
          return value;
      }

      // try raw
      for (Condition c : values()) {
	if (c.toRaw().equals(s))
	  return c;
      }

      return valueOf(s.toUpperCase().replace(" ", "_"));
    }

    /**
     * Turns the enum array into a string array of associated names.
     *
     * @param values	the values to convert
     * @return		the names
     */
    public static String[] toNames(Condition[] values) {
      List<String>	result;

      result = new ArrayList<>();
      for (Condition value: values)
        result.add(value.toDisplay());

      return result.toArray(new String[0]);
    }
  }

  /**
   * The available limitations.
   */
  public enum Limitation
    implements CustomDisplayEnum {

    LIABILITY("Liability"),
    WARRANTY("Warranty"),
    TRADEMARK_USE("Trademark use"),
    PATENT_USE("Patent use");

    /** the string to use for display. */
    private String m_Display;

    /** the commandline string. */
    private String m_Raw;

    /**
     * Initializes the enum with the name string.
     *
     * @param display	the string to use
     */
    private Limitation(String display) {
      m_Display = display;
    }

    /**
     * Returns the display string.
     *
     * @return		the display string
     */
    @Override
    public String toDisplay() {
      return m_Display;
    }

    /**
     * Returns the raw enum string.
     *
     * @return		the raw enum string
     */
    @Override
    public String toRaw() {
      return m_Raw;
    }

    /**
     * Returns the display string.
     *
     * @return		the display string
     */
    @Override
    public String toString() {
      return m_Display;
    }

    /**
     * Parses the string.
     *
     * @param s		the string to parse
     * @return		the limitation
     */
    public static Limitation parse(String s) {
      for (Limitation value : Limitation.values()) {
        if (value.toDisplay().equals(s))
          return value;
      }

      // try raw
      for (Limitation l : values()) {
	if (l.toRaw().equals(s))
	  return l;
      }

      return valueOf(s.toUpperCase().replace(" ", "_"));
    }

    /**
     * Turns the enum array into a string array of associated names.
     *
     * @param values	the values to convert
     * @return		the names
     */
    public static String[] toNames(Limitation[] values) {
      List<String>	result;

      result = new ArrayList<>();
      for (Limitation value: values)
        result.add(value.toDisplay());

      return result.toArray(new String[0]);
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
   * For creating a license.
   *
   * @param name	the name of the license
   * @param url		the URL to the full text of the license
   * @return		true if successfully created
   * @throws Exception	if request fails
   */
  public License create(String name, String url) throws Exception {
    License		result;
    JsonObject		data;
    JsonResponse 	response;
    JsonElement		element;
    Request 		request;

    getLogger().info("creating license: " + name);

    result   = null;
    data     = new JsonObject();
    data.addProperty("name", name);
    data.addProperty("url", url);
    request  = newPost(getPath())
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new License(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to create license: " + name, response);
    }

    return result;
  }

  /**
   * For updating a license.
   *
   * @param license 	the license to update
   * @param name	the new name of the license
   * @param url		the new URL to the full text of the license
   * @return		true if successfully created
   * @throws Exception	if request fails
   */
  public License update(License license, String name, String url) throws Exception {
    return update(license.getPK(), name, url);
  }

  /**
   * For updating a license.
   *
   * @param pk 		the license ID
   * @param name	the new name of the license
   * @param url		the new URL to the full text of the license
   * @return		true if successfully created
   * @throws Exception	if request fails
   */
  public License update(int pk, String name, String url) throws Exception {
    License		result;
    JsonObject		data;
    JsonResponse 	response;
    JsonElement		element;
    Request 		request;

    getLogger().info("updating license: " + pk);

    result   = null;
    data     = new JsonObject();
    data.addProperty("name", name);
    data.addProperty("url", url);
    request  = newPut(getPath() + pk)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new License(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to update license: " + name, response);
    }

    return result;
  }

  /**
   * For (partially) updating a license, using only the non-null values.
   *
   * @param license 	the license to update
   * @param name	the new name of the license
   * @param url		the new URL to the full text of the license
   * @return		true if successfully created
   * @throws Exception	if request fails
   */
  public License partialUpdate(License license, String name, String url) throws Exception {
    return partialUpdate(license.getPK(), name, url);
  }

  /**
   * For (partially) updating a license, using only the non-null values.
   *
   * @param pk 		the license ID
   * @param name	the new name of the license
   * @param url		the new URL to the full text of the license
   * @return		true if successfully created
   * @throws Exception	if request fails
   */
  public License partialUpdate(int pk, String name, String url) throws Exception {
    License		result;
    JsonObject		data;
    JsonResponse 	response;
    JsonElement		element;
    Request 		request;

    getLogger().info("(partially) updating license: " + pk);

    result   = null;
    data     = new JsonObject();
    if (name != null)
      data.addProperty("name", name);
    if (url != null)
      data.addProperty("url", url);
    request  = newPatch(getPath() + pk)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new License(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to (partially) update license: " + name, response);
    }

    return result;
  }

  /**
   * Generic method for modifying the sub-descriptors.
   *
   * @param pk		the PK of the license to modify
   * @param method	the method (add/remove)
   * @param type	the type (permission/condition/limitation)
   * @param names	the names of the sub-descriptors
   * @return		true if successful
   * @throws Exception	if modification fails or invalid PK
   */
  protected boolean modifySubDescriptors(int pk, String method, String type, String[] names) throws Exception {
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    if (!(method.equals("add") || method.equals("remove")))
      throw new IllegalArgumentException("Unknown subdescriptors method: " + method);
    if (!(type.equals("permissions") || type.equals("conditions") || type.equals("limitations")))
      throw new IllegalArgumentException("Unknown subdescriptors type: " + type);

    data     = new JsonObject();
    data.addProperty("method", method);
    data.addProperty("type", type);
    data.add("names", JsonUtils.toArray(Arrays.asList(names)));
    request  = newPatch(getPath() + pk + "/subdescriptors")
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to " + method + " " + type + " sub-descriptors for license " + pk, response);
  }

  /**
   * Adds the the sub-descriptors to the license.
   *
   * @param license	the license to add to
   * @param permissions the permissions to add, ignored if null
   * @param conditions 	the conditions to add, ignored if null
   * @param limitations the limitations to add, ignored if null
   * @return		true if successful
   * @throws Exception	if modification fails or invalid PK
   */
  public boolean addSubDescriptors(License license, Permission[] permissions, Condition[] conditions, Limitation[] limitations) throws Exception {
    return addSubDescriptors(license.getPK(), permissions, conditions, limitations);
  }

  /**
   * Adds the the sub-descriptors to the license.
   *
   * @param pk 		the license pk to add to
   * @param permissions the permissions to add, ignored if null
   * @param conditions 	the conditions to add, ignored if null
   * @param limitations the limitations to add, ignored if null
   * @return		true if successful
   * @throws Exception	if modification fails or invalid PK
   */
  public boolean addSubDescriptors(int pk, Permission[] permissions, Condition[] conditions, Limitation[] limitations) throws Exception {
    getLogger().info("adding sub-descriptors to license " + pk);

    if ((permissions != null) && (permissions.length > 0))
      modifySubDescriptors(pk, "add", "permissions", Permission.toNames(permissions));
    if ((conditions != null) && (conditions.length > 0))
      modifySubDescriptors(pk, "add", "conditions", Condition.toNames(conditions));
    if ((limitations != null) && (limitations.length > 0))
      modifySubDescriptors(pk, "add", "limitations", Limitation.toNames(limitations));

    return true;
  }

  /**
   * Removes the the sub-descriptors to the license.
   *
   * @param license	the license to add to
   * @param permissions the permissions to add, ignored if null
   * @param conditions 	the conditions to add, ignored if null
   * @param limitations the limitations to add, ignored if null
   * @return		true if successful
   * @throws Exception	if modification fails or invalid PK
   */
  public boolean removeSubDescriptors(License license, Permission[] permissions, Condition[] conditions, Limitation[] limitations) throws Exception {
    return removeSubDescriptors(license.getPK(), permissions, conditions, limitations);
  }

  /**
   * Removes the the sub-descriptors to the license.
   *
   * @param pk 		the license pk to add to
   * @param permissions the permissions to add, ignored if null
   * @param conditions 	the conditions to add, ignored if null
   * @param limitations the limitations to add, ignored if null
   * @return		true if successful
   * @throws Exception	if modification fails or invalid PK
   */
  public boolean removeSubDescriptors(int pk, Permission[] permissions, Condition[] conditions, Limitation[] limitations) throws Exception {
    getLogger().info("removing sub-descriptors from license " + pk);

    if ((permissions != null) && (permissions.length > 0))
      modifySubDescriptors(pk, "remove", "permissions", Permission.toNames(permissions));
    if ((conditions != null) && (conditions.length > 0))
      modifySubDescriptors(pk, "remove", "conditions", Condition.toNames(conditions));
    if ((limitations != null) && (limitations.length > 0))
      modifySubDescriptors(pk, "remove", "limitations", Limitation.toNames(limitations));

    return true;
  }

  /**
   * For deleting a specific license.
   *
   * @param license 	the license to delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid license PK
   */
  public boolean delete(License license) throws Exception {
    return delete(license.getPK());
  }

  /**
   * For deleting a specific license.
   *
   * @param pk 		the ID of the license
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid license PK
   */
  public boolean delete(int pk) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("deleting license with PK: " + pk);

    request  = newDelete(getPath() + pk + "/");
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete license: " + pk, response);
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
