/*
 * Users.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdljavaclient.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdljavaclient.core.AbstractJsonObjectWrapper;
import com.github.waikatoufdl.ufdljavaclient.core.JsonResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
     * Returns the user ID.
     *
     * @return		the ID
     */
    public int getID() {
      return getInt("id", -1);
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
      return "id=" + getID() + ", user=" + getUserName();
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
    return result;
  }
}
