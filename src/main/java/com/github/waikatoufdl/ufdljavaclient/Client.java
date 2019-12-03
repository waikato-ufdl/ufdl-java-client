/*
 * Client.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdljavaclient;

import com.github.waikatoufdl.ufdljavaclient.action.AbstractAction;
import com.github.waikatoufdl.ufdljavaclient.action.Datasets;
import com.github.waikatoufdl.ufdljavaclient.action.Memberships;
import com.github.waikatoufdl.ufdljavaclient.action.Organisations;
import com.github.waikatoufdl.ufdljavaclient.action.Projects;
import com.github.waikatoufdl.ufdljavaclient.action.Users;
import com.github.waikatoufdl.ufdljavaclient.context.Connection;
import com.github.waikatoufdl.ufdljavaclient.core.AbstractLoggingObject;

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

  /** for managing the organizations. */
  protected Organisations m_Organisations;

  /** for managhing the memberships. */
  protected Memberships m_Memberships;

  /**
   * Initializes the client.
   */
  public Client() {
    m_Connection = new Connection();
    try {
      m_Users         = newAction(Users.class);
      m_Datasets      = newAction(Datasets.class);
      m_Projects      = newAction(Projects.class);
      m_Organisations = newAction(Organisations.class);
      m_Memberships   = newAction(Memberships.class);
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
    this();
    connection().server(host);
    connection().authentication(user, password);
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
   * Returns the organisations action.
   *
   * @return		the organisations action
   */
  public Organisations organisations() {
    return m_Organisations;
  }

  /**
   * Returns the memberships action.
   *
   * @return		the memberships action
   */
  public Memberships memberships() {
    return m_Memberships;
  }

  /**
   * Creates a new instance of an action, sets the connection and returns the instance.
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
