/*
 * Authentication.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.auth;

import com.github.waikatoufdl.ufdl4j.context.Server;
import com.github.waikatoufdl.ufdl4j.core.AbstractLoggingObject;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;
import com.google.gson.JsonObject;
import org.apache.http.entity.ContentType;

import java.util.logging.Level;

/**
 * Handles authentication.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Authentication
  extends AbstractLoggingObject {

  private static final long serialVersionUID = -30480018943298421L;

  public final static String URL_OBTAIN = "/v1/auth/obtain/";

  public final static String URL_REFRESH = "/v1/auth/refresh/";

  public static final String KEY_USERNAME = "username";

  public static final String KEY_PASSWORD = "password";

  public static final String KEY_ACCESS = "access";

  public static final String KEY_REFRESH = "refresh";

  /** the context to use. */
  protected Server m_Server;

  /** the user. */
  protected String m_User;

  /** the password. */
  protected String m_Password;

  /** the tokens. */
  protected Tokens m_Tokens;

  /** for handling the storage of the tokens. */
  protected TokenStorageHandler m_Storage;

  /**
   * Initializes the authentication with empty user/password.
   */
  public Authentication() {
    this("", "");
  }

  /**
   * Initializes the authentication.
   *
   * @param user	the user to use
   * @param password	the password to use
   */
  public Authentication(String user, String password) {
    this(user, password, new LocalStorage());
  }

  /**
   * Initializes the authentication.
   *
   * @param user	the user to use
   * @param password	the password to use
   */
  public Authentication(String user, String password, TokenStorageHandler storage) {
    m_User     = user;
    m_Password = password;
    m_Server   = null;
    m_Tokens   = null;
    m_Storage  = storage;
  }

  /**
   * Returns the user.
   *
   * @return		the user
   */
  public String getUser() {
    return m_User;
  }

  /**
   * Returns the password.
   *
   * @return		the password
   */
  public String getPassword() {
    return m_Password;
  }

  /**
   * Returns the tokens, loads or obtains them if necessary.
   *
   * @return		the tokens
   */
  public Tokens getTokens() {
    if (m_Tokens == null)
      m_Tokens = m_Storage.load(this);

    // obtain
    if (!m_Tokens.isValid()) {
      try {
	obtain();
      }
      catch (Exception e) {
        getLogger().log(Level.SEVERE, "Failed to obtain tokens for '" + getUser() + "'!", e);
      }
    }

    return m_Tokens;
  }

  /**
   * Sets the context to use.
   *
   * @param value	the context
   */
  public void setServer(Server value) {
    m_Server = value;
  }

  /**
   * Returns the context in use.
   *
   * @return		the context, null if none set
   */
  public Server getServer() {
    return m_Server;
  }

  /**
   * Returns the storage handler.
   *
   * @return		the handler
   */
  public TokenStorageHandler storage() {
    return m_Storage;
  }

  /**
   * Obtains the tokens, using the supplied user/password.
   *
   * @return		null if successful, otherwise error message
   */
  public String obtain() throws Exception {
    JsonResponse	response;
    JsonObject 		body;
    JsonObject 		tokens;

    getLogger().info("Obtaining tokens");
    body = new JsonObject();
    body.addProperty(KEY_USERNAME, m_User);
    body.addProperty(KEY_PASSWORD, m_Password);
    response = m_Server.getSession()
      .post(m_Server.build(URL_OBTAIN))
      .body(body.toString(), ContentType.APPLICATION_JSON)
      .execute(new JsonResponse());
    if (response.ok()) {
      tokens   = response.jsonObject();
      m_Tokens = new Tokens(tokens.get(KEY_REFRESH).getAsString(), tokens.get(KEY_ACCESS).getAsString());
      m_Storage.store(this, m_Tokens);
      return null;
    }
    else {
      m_Tokens = new Tokens();
      getLogger().severe("Failed to obtain tokens: " + response.toString());
      return response.toString();
    }
  }

  /**
   * Updates the access token, using the refresh token.
   * Automatically obtains refresh/access tokens if none set.
   *
   * @return		null if successful, otherwise error message
   */
  public String refresh() throws Exception {
    JsonResponse	response;
    JsonObject 		body;
    JsonObject		tokens;

    if (m_Tokens.getRefreshToken() == null)
      return obtain();

    getLogger().info("Refreshing tokens");
    body = new JsonObject();
    body.addProperty(KEY_REFRESH, m_Tokens.getRefreshToken());
    response = m_Server.getSession()
      .post(m_Server.build(URL_REFRESH))
      .body(body.toString(), ContentType.APPLICATION_JSON)
      .execute(new JsonResponse());
    if (response.ok()) {
      tokens   = response.jsonObject();
      m_Tokens = new Tokens(m_Tokens.getRefreshToken(), tokens.get(KEY_ACCESS).getAsString());
      return null;
    }
    else {
      getLogger().severe("Failed to refresh tokens: " + response.toString());
      return response.toString();
    }
  }

  /**
   * Returns a short description of the state.
   *
   * @return		the description
   */
  public String toString() {
    return "user=" + getUser() + ", password=" + getPassword().replaceAll(".", "*") + ", tokens=" + m_Tokens;
  }
}
