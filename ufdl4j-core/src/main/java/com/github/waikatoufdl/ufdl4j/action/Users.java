/*
 * Users.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapperWithPK;
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
 * Encapsulates user operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Users
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  /**
   * Container class for user information.
   */
  public static class User
    extends AbstractJsonObjectWrapperWithPK {

    private static final long serialVersionUID = 3523630902439390574L;

    /**
     * Initializes the user.
     *
     * @param data	the data to use
     */
    public User(JsonObject data) {
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
    public String getUserName() {
      return getString("username", "");
    }

    /**
     * Returns the first name.
     *
     * @return		the first name
     */
    public String getFirstName() {
      return getString("first_name", "");
    }

    /**
     * Returns the last name.
     *
     * @return		the last name
     */
    public String getLastName() {
      return getString("last_name", "");
    }

    /**
     * Returns the email.
     *
     * @return		the email
     */
    public String getEmail() {
      return getString("email", "");
    }

    /**
     * Returns the joining date.
     *
     * @return		the date, can be null
     */
    public LocalDateTime getJoined() {
      return getDateTime("date_joined", null);
    }

    /**
     * Returns the last login date.
     *
     * @return		the date, can be null
     */
    public LocalDateTime getLastLogin() {
      return getDateTime("last_login", null);
    }

    /**
     * Returns whether the user is active.
     *
     * @return		true if active
     */
    public boolean isActive() {
      return getBoolean("is_active", false);
    }

    /**
     * Returns whether the user is staff.
     *
     * @return		true if staff
     */
    public boolean isStaff() {
      return getBoolean("is_staff", false);
    }

    /**
     * Returns whether the user is a superuser.
     *
     * @return		true if superuser
     */
    public boolean isSuperuser() {
      return getBoolean("is_superuser", false);
    }

    /**
     * Returns a short description of the state.
     *
     * @return		the state
     */
    @Override
    public String toString() {
      return "pk=" + getPK() + ", user=" + getUserName() + ", email=" + getEmail() + ", staff=" + isStaff() + ", superuser=" + isSuperuser() + ", active=" + isActive();
    }
  }

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "Users";
  }

  /**
   * The URL path to use.
   *
   * @return		the path
   */
  @Override
  public String getPath() {
    return "/v1/core/users/";
  }

  /**
   * For listing the users.
   *
   * @return		the list of users
   * @throws Exception	if request fails
   */
  public List<User> list() throws Exception {
    return list(null);
  }

  /**
   * For listing the users.
   *
   * @param filter 	the filter to apply, can be null
   * @return		the list of users
   * @throws Exception	if request fails
   */
  public List<User> list(Filter filter) throws Exception {
    List<User>		result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing users" + (filter == null ? "" : ", filter: " + filter.toJsonObject()));

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
          result.add(new User(array.get(i).getAsJsonObject()));
      }
    }
    else {
      throw new FailedRequestException("Failed to list users!" + (filter == null ? "" : "\nFilter: " + filter.toJsonObject()), response);
    }

    return result;
  }

  /**
   * For loading a specific user by primary key.
   *
   * @param pk 		the primary key of the user to load
   * @return		the user
   * @throws Exception	if request fails
   */
  public User load(int pk) throws Exception {
    User		result;
    JsonResponse 	response;
    JsonElement		element;
    Request 		request;

    getLogger().info("loading user with id: " + pk);

    result   = null;
    request  = newGet(getPath() + pk);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new User(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to load user: " + pk, response);
    }

    return result;
  }

  /**
   * For loading a specific user by name.
   *
   * @param name 	the user name
   * @return		the user object, null if failed to create
   * @throws Exception	if request fails
   */
  public User load(String name) throws Exception {
    User	result;
    Filter	filter;

    getLogger().info("loading user with name: " + name);

    result = null;

    filter = new GenericFilter(
      new AbstractExpression[]{
        new ExactString("username", name),
      }
    );
    for (User user: list(filter)) {
      result = user;
      break;
    }

    if (result == null)
      getLogger().warning("failed to load user: " + name);

    return result;
  }

  /**
   * Creates the user.
   *
   * @param user 	the user name
   * @param password 	the user's password
   * @param firstName 	the user's first name
   * @param lastName 	the user's last name
   * @param email 	the user's email address
   * @return		the user object, null if failed to create
   * @throws Exception	if request fails or user already exists
   */
  public User create(String user, String password, String firstName, String lastName, String email) throws Exception {
    User		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("creating user: " + user + "/" + password.replaceAll(".", "*"));

    data = new JsonObject();
    data.addProperty("username", user);
    data.addProperty("password", password);
    data.addProperty("first_name", firstName);
    data.addProperty("last_name", lastName);
    data.addProperty("email", email);
    request = newPost(getPath())
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new User(response.jsonObject());
    else
      throw new FailedRequestException("Failed to create user: " + user + "/" + password.replaceAll(".", "*"), response);

    return result;
  }

  /**
   * Updates the user identified by the PK.
   *
   * @param userObj 	the user to update
   * @param user 	the user name
   * @param password 	the user's password
   * @param firstName 	the user's first name
   * @param lastName 	the user's last name
   * @param email 	the user's email address
   * @return		the user object, null if failed to create
   * @throws Exception	if request fails
   */
  public User update(User userObj, String user, String password, String firstName, String lastName, String email) throws Exception {
    return update(userObj.getPK(), user, password, firstName, lastName, email);
  }

  /**
   * Updates the user identified by the PK.
   *
   * @param pk 		the PK of the user to update
   * @param user 	the user name
   * @param password 	the user's password
   * @param firstName 	the user's first name
   * @param lastName 	the user's last name
   * @param email 	the user's email address
   * @return		the user object, null if failed to create
   * @throws Exception	if request fails
   */
  public User update(int pk, String user, String password, String firstName, String lastName, String email) throws Exception {
    User		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("updating user: " + pk);

    data = new JsonObject();
    data.addProperty("username", user);
    data.addProperty("password", password);
    data.addProperty("first_name", firstName);
    data.addProperty("last_name", lastName);
    data.addProperty("email", email);
    request = newPut(getPath() + pk)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new User(response.jsonObject());
    else
      throw new FailedRequestException("Failed to update user: " + pk, response);

    return result;
  }

  /**
   * (Partially) Updates the user identified by the PK, using only the non-null arguments.
   *
   * @param userObj	the user to update
   * @param user 	the user name
   * @param password 	the user's password
   * @param firstName 	the user's first name
   * @param lastName 	the user's last name
   * @param email 	the user's email address
   * @param isActive 	the user's active state
   * @return		the user object, null if failed to create
   * @throws Exception	if request fails
   */
  public User partialUpdate(User userObj, String user, String password, String firstName, String lastName, String email, Boolean isActive) throws Exception {
    return partialUpdate(userObj.getPK(), user, password, firstName, lastName, email, isActive);
  }

  /**
   * (Partially) Updates the user identified by the PK, using only the non-null arguments.
   *
   * @param pk 		the PK of the user to update
   * @param user 	the user name
   * @param password 	the user's password
   * @param firstName 	the user's first name
   * @param lastName 	the user's last name
   * @param email 	the user's email address
   * @param isActive 	the user's active state
   * @return		the user object, null if failed to create
   * @throws Exception	if request fails
   */
  public User partialUpdate(int pk, String user, String password, String firstName, String lastName, String email, Boolean isActive) throws Exception {
    User		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("partially updating user: " + pk);

    data = new JsonObject();
    if (user != null)
      data.addProperty("username", user);
    if (password != null)
      data.addProperty("password", password);
    if (firstName != null)
      data.addProperty("first_name", firstName);
    if (lastName != null)
      data.addProperty("last_name", lastName);
    if (email != null)
      data.addProperty("email", email);
    if (isActive != null)
      data.addProperty("is_active", isActive);
    request = newPatch(getPath() + pk)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new User(response.jsonObject());
    else
      throw new FailedRequestException("Failed to partially update user: " + pk, response);

    return result;
  }

  /**
   * For deleting a specific user.
   *
   * @param user 	the user to delete
   * @param hard 	whether to remove it or just flagged it as inactive
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid user PK
   */
  public boolean delete(User user, boolean hard) throws Exception {
    return delete(user.getPK(), hard);
  }

  /**
   * For deleting a specific user.
   *
   * @param pk 		the ID of the user
   * @param hard 	whether to remove it or just flagged it as inactive
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid user PK
   */
  public boolean delete(int pk, boolean hard) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("deleting user with PK (hard=" + hard + "): " + pk);

    // TODO hard delete
    request  = newDelete(getPath() + pk + "/");
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete user (hard=" + hard + "): " + pk, response);
  }

  /**
   * For reinstating a specific user.
   *
   * @param user 	the user to reinstate
   * @return		true if successfully reinstated
   * @throws Exception	if request fails, eg invalid user PK
   */
  public boolean reinstate(User user) throws Exception {
    return reinstate(user.getPK());
  }

  /**
   * For reinstating a specific user.
   *
   * @param pk 		the ID of the user
   * @return		true if successfully reinstated
   * @throws Exception	if request fails, eg invalid user PK
   */
  public boolean reinstate(int pk) throws Exception {
    getLogger().info("reinstating user with PK: " + pk);
    return (partialUpdate(pk, null, null, null, null, null, true) != null);
  }
}
