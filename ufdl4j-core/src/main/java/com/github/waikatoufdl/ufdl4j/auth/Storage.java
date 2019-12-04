/*
 * Storage.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.auth;

import com.github.waikatoufdl.ufdl4j.core.AbstractLoggingObject;
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
 * For managing storage of tokens.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Storage
  extends AbstractLoggingObject {

  private static final long serialVersionUID = 8396124600663114923L;

  public static final String TOKENS_FILE = "tokens.json";

  public static final String KEY_REFRESH = "refresh";

  public static final String KEY_ACCESS = "access";

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
    JsonObject	auth;

    result = new Tokens();
    data   = load();
    if (data.has(context.getUser())) {
      auth   = data.getAsJsonObject(context.getUser());
      if (!auth.has(KEY_REFRESH))
        getLogger().warning("No value for '" + KEY_REFRESH + "' stored for user '" + context.getUser() + "!");
      else if (!auth.has(KEY_ACCESS))
        getLogger().warning("No value for '" + KEY_ACCESS + "' stored for user '" + context.getUser() + "!");
      else
	result = new Tokens(auth.get(KEY_REFRESH).getAsString(), auth.get(KEY_ACCESS).getAsString());
    }
    else {
      getLogger().info("No data stored user '" + context.getUser() + ".");
    }

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
    JsonObject	auth;

    if (!tokens.isValid()) {
      result = "Tokens are not valid, cannot store!";
      getLogger().warning(result);
      return result;
    }

    data = load();
    auth = new JsonObject();
    auth.addProperty(KEY_REFRESH, tokens.getRefreshToken());
    auth.addProperty(KEY_ACCESS, tokens.getAccessToken());
    data.add(context.getUser(), auth);

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
