/*
 * Client.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j;

import com.github.waikatoufdl.ufdl4j.action.AbstractAction;
import com.github.waikatoufdl.ufdl4j.action.Datasets;
import com.github.waikatoufdl.ufdl4j.action.Projects;
import com.github.waikatoufdl.ufdl4j.action.Teams;
import com.github.waikatoufdl.ufdl4j.action.Users;
import com.github.waikatoufdl.ufdl4j.auth.LocalStorage;
import com.github.waikatoufdl.ufdl4j.auth.TokenStorageHandler;
import com.github.waikatoufdl.ufdl4j.context.Connection;
import com.github.waikatoufdl.ufdl4j.core.AbstractLoggingObject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * The client for communicating with the UFDL backend.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Client
  extends AbstractLoggingObject {

  private static final long serialVersionUID = 4630756838422216386L;

  /** the connection to use. */
  protected Connection m_Connection;

  /** for managing the users. */
  protected Users m_Users;

  /** for managing the datasets. */
  protected Datasets m_Datasets;

  /** for managing the projects. */
  protected Projects m_Projects;

  /** for managing the teams. */
  protected Teams m_Teams;

  /** action singletons. */
  protected Map<Class, AbstractAction> m_Actions;

  /**
   * Initializes the client.
   */
  public Client() {
    m_Connection = new Connection();
    m_Actions    = new HashMap<>();
    try {
      m_Users    = action(Users.class);
      m_Datasets = action(Datasets.class);
      m_Projects = action(Projects.class);
      m_Teams    = action(Teams.class);
    }
    catch (Exception e) {
      getLogger().log(Level.SEVERE, "Failed to setup client!", e);
    }
  }

  /**
   * Initializes the client.
   *
   * @param host 	the host to use
   * @param user 	the user
   * @param password 	the password
   */
  public Client(String host, String user, String password) {
    this(host, user, password, new LocalStorage());
  }

  /**
   * Initializes the client.
   *
   * @param host 	the host to use
   * @param user 	the user
   * @param password 	the password
   * @param storage 	the handler for the tokens
   */
  public Client(String host, String user, String password, TokenStorageHandler storage) {
    this();
    connection().server(host);
    connection().authentication(user, password, storage);
  }

  /**
   * Returns the underlying connection.
   *
   * @return		the connection
   */
  public Connection connection() {
    return m_Connection;
  }

  /**
   * Returns the users action.
   *
   * @return		the users action
   */
  public Users users() {
    return m_Users;
  }

  /**
   * Returns the datasets action.
   *
   * @return		the datasets action
   */
  public Datasets datasets() {
    return m_Datasets;
  }

  /**
   * Returns the projects action.
   *
   * @return		the projects action
   */
  public Projects projects() {
    return m_Projects;
  }

  /**
   * Returns the teams action.
   *
   * @return		the teams action
   */
  public Teams teams() {
    return m_Teams;
  }

  /**
   * Always creates a new instance of an action, sets the connection and returns the instance.
   *
   * @param action	the action to instantiate
   * @param <T>		the type of action
   * @return		the action instance
   * @throws Exception	if instantiation fails
   * @see		#connection()
   */
  public <T extends AbstractAction> T newAction(Class<T> action) throws Exception {
    T 	result;

    result = action.newInstance();
    result.setConnection(m_Connection);

    return result;
  }

  /**
   * Creates an instance of an action if not yet present, sets the connection
   * and returns the instance.
   *
   * @param action	the type action to instantiate/return
   * @param <T>		the type of action
   * @return		the action instance
   * @throws Exception	if instantiation fails
   * @see		#connection()
   */
  public <T extends AbstractAction> T action(Class<T> action) throws Exception {
    T 	result;

    if (m_Actions.containsKey(action))
      return (T) m_Actions.get(action);

    result = newAction(action);
    m_Actions.put(action, result);

    return result;
  }

  /**
   * Closes the client.
   */
  public void close() {
    m_Connection.close();
  }

  /**
   * Returns a short description of the state.
   *
   * @return		the description
   */
  public String toString() {
    return m_Connection.toString();
  }
}
