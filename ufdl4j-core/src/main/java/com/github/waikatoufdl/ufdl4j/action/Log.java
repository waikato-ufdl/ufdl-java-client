/*
 * Log.java
 * Copyright (C) 2020-2023 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.core.MediaTypeHelper;
import com.github.fracpete.requests4j.request.Request;
import com.github.fracpete.requests4j.response.JsonResponse;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapperWithPK;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.filter.Filter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates log operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Log
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  /**
   * Container class for log entry information.
   */
  public static class LogEntry
    extends AbstractJsonObjectWrapperWithPK {

    private static final long serialVersionUID = 3523630902439390574L;

    /**
     * Initializes the log entry.
     *
     * @param data	the data to use
     */
    public LogEntry(JsonObject data) {
      super(data);
    }

    /**
     * Returns the log entry primary key.
     *
     * @return		the primary key
     */
    @Override
    public int getPK() {
      return getInt("pk");
    }

    /**
     * Returns the creation time.
     *
     * @return		the date, can be null
     */
    public LocalDateTime getCreationTime() {
      return getDateTime("creation_time", null);
    }

    /**
     * Returns the level (as integer).
     *
     * @return		the level
     */
    public int getLevelAsInt() {
      return getInt("level");
    }

    /**
     * Returns the level.
     *
     * @return		the level
     */
    public LogLevel getLevel() {
      return LogLevel.parse(getLevelAsInt());
    }

    /**
     * Returns the message.
     *
     * @return		the message
     */
    public String getMessage() {
      return getString("message", "");
    }

    /**
     * Returns whether the entry is internal.
     *
     * @return		true if internal
     */
    public boolean isInternal() {
      return getBoolean("is_internal", false);
    }

    /**
     * Returns a short description of the state.
     *
     * @return		the state
     */
    @Override
    public String toString() {
      return "pk=" + getPK() + ", ts=" + getCreationTime() + ", level=" + getLevel() + ", message=" + getMessage();
    }
  }

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "Log";
  }

  /**
   * The URL path to use.
   *
   * @return		the path
   */
  @Override
  public String getPath() {
    return "/v1/log/";
  }

  /**
   * For listing the log entries.
   *
   * @return		the list of log entries
   * @throws Exception	if request fails
   */
  public List<LogEntry> list() throws Exception {
    return list(null);
  }

  /**
   * For listing the log entries.
   *
   * @param filter 	the filter to apply, can be null
   * @return		the list of log entries
   * @throws Exception	if request fails
   */
  public List<LogEntry> list(Filter filter) throws Exception {
    List<LogEntry>	result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing log entries" + (filter == null ? "" : ", filter: " + filter.toJsonObject()));

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
          result.add(new LogEntry(array.get(i).getAsJsonObject()));
      }
    }
    else {
      throw new FailedRequestException("Failed to list log entries!" + (filter == null ? "" : "\nFilter: " + filter.toJsonObject()), response);
    }

    return result;
  }

  /**
   * For loading a specific log entry by primary key.
   *
   * @param pk 		the primary key of the log entry to load
   * @return		the entry
   * @throws Exception	if request fails
   */
  public LogEntry load(int pk) throws Exception {
    LogEntry 		result;
    JsonResponse 	response;
    JsonElement		element;
    Request 		request;

    getLogger().info("loading log entry with id: " + pk);

    result   = null;
    request  = newGet(getPath() + pk);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new LogEntry(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to load log entry: " + pk, response);
    }

    return result;
  }

  /**
   * Creates the log entry a "now" timestamp.
   *
   * @param level 	the level
   * @param message 	the message
   * @return		the log entry object, null if failed to create
   * @throws Exception	if request fails or log entry already exists
   */
  public LogEntry create(LogLevel level, String message) throws Exception {
    return create(LocalDateTime.now(ZoneId.systemDefault()), level.getLevel(), message);
  }

  /**
   * Creates the log entry with a "now" timestamp.
   *
   * @param level 	the level
   * @param message 	the message
   * @return		the log entry object, null if failed to create
   * @throws Exception	if request fails or log entry already exists
   */
  public LogEntry create(int level, String message) throws Exception {
    return create(LocalDateTime.now(ZoneId.systemDefault()), level, message);
  }

  /**
   * Creates the log entry.
   *
   * @param ts 		the timestamp
   * @param level 	the level
   * @param message 	the message
   * @return		the log entry object, null if failed to create
   * @throws Exception	if request fails or log entry already exists
   */
  public LogEntry create(LocalDateTime ts, LogLevel level, String message) throws Exception {
    return create(ts, level.getLevel(), message);
  }

  /**
   * Creates the log entry.
   *
   * @param ts 		the timestamp
   * @param level 	the level
   * @param message 	the message
   * @return		the log entry object, null if failed to create
   * @throws Exception	if request fails or log entry already exists
   */
  public LogEntry create(LocalDateTime ts, int level, String message) throws Exception {
    LogEntry result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("creating log entry: " + level + "/" + message);

    data = new JsonObject();
    data.addProperty("creation_time", ts.toString());
    data.addProperty("level", level);
    data.addProperty("message", message);
    request = newPost(getPath() + "create")
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok())
      result = new LogEntry(response.jsonObject());
    else
      throw new FailedRequestException("Failed to create log entry: " + level + "/" + message, response);

    return result;
  }

  /**
   * For deleting a specific log entry.
   *
   * @param entry 	the entry to delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid log entry PK
   */
  public boolean delete(LogEntry entry) throws Exception {
    return delete(entry.getPK());
  }

  /**
   * For deleting a specific log entry.
   *
   * @param pk 		the ID of the log entry
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid log entry PK
   */
  public boolean delete(int pk) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("deleting log entry with PK: " + pk);

    request  = newDelete(getPath() + pk);
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete log entry: " + pk, response);
  }
}
