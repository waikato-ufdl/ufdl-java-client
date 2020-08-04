/*
 * LocalStorage.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.auth;

import com.github.waikatoufdl.ufdl4j.core.AbstractLoggingObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.tika.io.IOUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.logging.Level;

/**
 * For managing storage of tokens in the local file system.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class LocalStorage
  extends AbstractLoggingObject
  implements TokenStorageHandler {

  private static final long serialVersionUID = 8396124600663114923L;

  public static final String TOKENS_FILE = "tokens.json";

  /** the config directory. */
  protected String m_ConfigDir;

  /**
   * Returns the configuration directory.
   *
   * @return		the directory
   */
  public synchronized String getConfigDir() {
    String	dir;

    if (m_ConfigDir == null) {
      dir = System.getenv("APPDATA");
      if (dir == null)
	dir = System.getenv("XDG_CONFIG_HOME");
      if (dir == null)
	dir = System.getProperty("user.home");
      dir += File.separator + ".config" + File.separator + "ufdl";
      m_ConfigDir = dir;
    }

    return m_ConfigDir;
  }

  /**
   * Returns the path to the tokens.json file.
   *
   * @return		the full path
   * @see		#TOKENS_FILE
   * @see		#getConfigDir()
   */
  public String getTokenFile() {
    return getConfigDir() + File.separator + TOKENS_FILE;
  }

  /**
   * Loads the tokens from disk.
   *
   * @return		the JSON object
   */
  protected JsonObject load() {
    JsonObject		result;
    FileReader		freader;
    BufferedReader	breader;
    File		config;

    config = new File(getTokenFile());
    if (config.exists()) {
      getLogger().info("Loading tokens from: " + config);
      freader = null;
      breader = null;
      try {
        freader = new FileReader(config);
        breader = new BufferedReader(freader);
	result = JsonParser.parseReader(breader).getAsJsonObject();
      }
      catch (Exception e) {
	result = new JsonObject();
        getLogger().log(Level.SEVERE, "Failed to load tokens from disk: " + config, e);
      }
      finally {
	IOUtils.closeQuietly(breader);
	IOUtils.closeQuietly(freader);
      }
    }
    else {
      getLogger().info("Token file not present: " + config);
      result = new JsonObject();
    }

    return result;
  }

  /**
   * Stores the tokens.
   *
   * @param data	the authentication data to store
   * @return		null if successful, otherwise error message
   */
  protected String store(JsonObject data) {
    String		result;
    FileWriter		fwriter;
    BufferedWriter	bwriter;
    File		config;
    File		dir;

    result  = null;
    config  = new File(getTokenFile());
    dir     = config.getParentFile();
    fwriter = null;
    bwriter = null;
    getLogger().info("Storing tokens on disk: " + config);
    try {
      // create parent dir?
      if (!dir.exists()) {
	if (!dir.mkdirs()) {
	  result = "Failed to create directory for storing tokens: " + dir;
	  getLogger().severe(result);
	  return result;
	}
      }
      fwriter = new FileWriter(config);
      bwriter = new BufferedWriter(fwriter);
      bwriter.write(data.toString());
      bwriter.flush();
      fwriter.flush();
    }
    catch (Exception e) {
      result = "Failed to store tokens on disk: " + config;
      getLogger().log(Level.SEVERE, result, e);
    }
    finally {
      IOUtils.closeQuietly(bwriter);
      IOUtils.closeQuietly(fwriter);
    }

    return result;
  }

  /**
   * Retrieves the tokens for the user.
   *
   * @param context	the context
   * @return		the tokens
   */
  public Tokens load(Authentication context) {
    Tokens	result;
    JsonObject	data;
    JsonObject  server;
    JsonArray   auth;

    result = new Tokens();
    data   = load();
    if (data.has(context.getServer().getURL())) {
      server = data.getAsJsonObject(context.getServer().getURL());
      if (server.has(context.getUser())) {
        try {
          auth = server.getAsJsonArray(context.getUser());
          if (auth.size() != 2)
            getLogger().warning("Expected two values (access/refresh) in token array, but got: " + auth.size());
          else
            result = new Tokens(auth.get(1).getAsString(), auth.get(0).getAsString());
        }
        catch (Exception e) {
          getLogger().log(Level.SEVERE, "Failed to read tokens from: " + data);
        }
      }
      else {
        getLogger().info("No data stored for user '" + context.getUser() + ".");
      }
    }
    else {
      getLogger().info("No data stored for server/user '" + context.getServer().getURL() + "/" + context.getUser() + ".");
    }

    getLogger().fine("refresh token: " + result.getRefreshToken());
    getLogger().fine("access token: " + result.getAccessToken());

    return result;
  }

  /**
   * Stores the tokens on disk.
   *
   * @param context	the context
   * @param tokens	the tokens to store
   * @return		null if successfully stored, otherwise error message
   */
  public String store(Authentication context, Tokens tokens) {
    String	result;
    JsonObject	data;
    JsonArray	auth;
    JsonObject  server;

    if (!tokens.isValid()) {
      result = "Tokens are not valid, cannot store!";
      getLogger().warning(result);
      return result;
    }

    data = load();
    if (data.has(context.getServer().getURL()))
      server = data.getAsJsonObject(context.getServer().getURL());
    else
      server = new JsonObject();
    auth = new JsonArray();
    auth.add(tokens.getAccessToken());
    auth.add(tokens.getRefreshToken());
    server.add(context.getUser(), auth);
    data.add(context.getServer().getURL(), server);

    getLogger().fine("refresh token: " + tokens.getRefreshToken());
    getLogger().fine("access token: " + tokens.getAccessToken());

    return store(data);
  }

  /**
   * Returns a short description of the state.
   *
   * @return		the state
   */
  @Override
  public String toString() {
    return "tokenfile=" + getTokenFile();
  }
}
