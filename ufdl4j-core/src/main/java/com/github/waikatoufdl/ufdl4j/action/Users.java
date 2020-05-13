/*
 * Users.java
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
 * Encapsulates user operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Users
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  public static final String PATH = "/v1/core/users/";

  /**
   * Container class for user information.
   */
  public static class User
    extends AbstractJsonObjectWrapper {

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
      return "pk=" + getPK() + ", user=" + getUserName();
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
   * For listing the users.
   *
   * @return		the list of users
   * @throws Exception	if request fails
   */
  public List<User> list() throws Exception {
    List<User>		result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing users");

    result   = new ArrayList<>();
    request  = newGet(PATH);
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
      throw new FailedRequestException("Failed to list users!", response);
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
    request  = newGet(PATH + pk);
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

    getLogger().info("loading user with name: " + name);

    result = null;

    for (User user: list()) {
      if (user.getUserName().equals(name)) {
        result = user;
        break;
      }
    }

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
    request = newPost(PATH)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new User(response.jsonObject());
    else
      throw new FailedRequestException("Failed to create user: " + user + "/" + password.replaceAll(".", "*"), response);

    return result;
  }

  /**
   * For deleting a specific user.
   *
   * @param user 	the user to delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid user PK
   */
  public boolean delete(User user) throws Exception {
    return delete(user.getPK());
  }

  /**
   * For deleting a specific user.
   *
   * @param pk 		the ID of the user
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid user PK
   */
  public boolean delete(int pk) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("deleting user with PK: " + pk);

    request  = newDelete(PATH + pk + "/");
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete user: " + pk, response);
  }
}
