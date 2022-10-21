/*
 * Client.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j;

import com.github.waikatoufdl.ufdl4j.action.AbstractAction;
import com.github.waikatoufdl.ufdl4j.action.CudaVersions;
import com.github.waikatoufdl.ufdl4j.action.Datasets;
import com.github.waikatoufdl.ufdl4j.action.DockerImages;
import com.github.waikatoufdl.ufdl4j.action.Domains;
import com.github.waikatoufdl.ufdl4j.action.Frameworks;
import com.github.waikatoufdl.ufdl4j.action.Generic;
import com.github.waikatoufdl.ufdl4j.action.HardwareGenerations;
import com.github.waikatoufdl.ufdl4j.action.JobTemplates;
import com.github.waikatoufdl.ufdl4j.action.JobTypes;
import com.github.waikatoufdl.ufdl4j.action.Jobs;
import com.github.waikatoufdl.ufdl4j.action.Licenses;
import com.github.waikatoufdl.ufdl4j.action.Log;
import com.github.waikatoufdl.ufdl4j.action.Nodes;
import com.github.waikatoufdl.ufdl4j.action.PretrainedModels;
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

  /** the host. */
  protected String m_Host;

  /** the user. */
  protected String m_User;

  /** the password. */
  protected String m_Password;

  /** the token storage. */
  protected TokenStorageHandler m_TokenStorage;

  /** the connect timeout. */
  protected int m_ConnectTimeout;

  /** the read timeout. */
  protected int m_ReadTimeout;

  /** the write timeout. */
  protected int m_WriteTimeout;

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

  /** for managing the licenses. */
  protected Licenses m_Licenses;

  /** for managing the log. */
  protected Log m_Log;

  /** the domains. */
  protected Domains m_Domains;

  /** the cuda versions. */
  protected CudaVersions m_Cuda;

  /** the frameworks. */
  protected Frameworks m_Frameworks;

  /** the hardware generations. */
  protected HardwareGenerations m_Hardware;

  /** the docker images. */
  protected DockerImages m_Docker;

  /** the pretrained models. */
  protected PretrainedModels m_PretrainedModels;

  /** the nodes. */
  protected Nodes m_Nodes;

  /** the jobtypes. */
  protected JobTypes m_JobTypes;

  /** the job templates. */
  protected JobTemplates m_JobTemplates;

  /** the jobs. */
  protected Jobs m_Jobs;

  /** for generic calls. */
  protected Generic m_Generic;

  /** action singletons. */
  protected Map<Class, AbstractAction> m_Actions;

  /**
   * Initializes the client.
   */
  public Client() {
    m_Connection = new Connection();
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
    this(host, user, password, storage, -1, -1, -1);
  }

  /**
   * Initializes the client.
   *
   * @param host 		the host to use
   * @param user 		the user
   * @param password 		the password
   * @param storage 		the handler for the tokens
   * @param connectTimeout  	the timeout in seconds for connecting
   * @param readTimeout 	the timeout in seconds for reading
   * @param writeTimeout 	the timeout in seconds for writing
   */
  public Client(String host, String user, String password, TokenStorageHandler storage, int connectTimeout, int readTimeout, int writeTimeout) {
    m_Host           = host;
    m_User           = user;
    m_Password       = password;
    m_TokenStorage   = storage;
    m_ConnectTimeout = connectTimeout;
    m_ReadTimeout    = readTimeout;
    m_WriteTimeout   = writeTimeout;
  }

  /**
   * Returns the underlying connection.
   *
   * @return		the connection
   */
  public synchronized Connection connection() {
    if (m_Connection == null) {
      m_Connection = new Connection(m_ConnectTimeout, m_ReadTimeout, m_WriteTimeout);
      m_Connection.server(m_Host);
      m_Connection.authentication(m_User, m_Password, m_TokenStorage);
    }
    return m_Connection;
  }

  /**
   * Initializes the actions if necessary.
   */
  protected synchronized void initActions() {
    if (m_Actions != null)
      return;

    m_Actions = new HashMap<>();
    try {
      m_Users            = action(Users.class);
      m_Datasets         = action(Datasets.class);
      m_Projects         = action(Projects.class);
      m_Teams            = action(Teams.class);
      m_Licenses         = action(Licenses.class);
      m_Log              = action(Log.class);
      m_Domains          = action(Domains.class);
      m_Cuda             = action(CudaVersions.class);
      m_Frameworks       = action(Frameworks.class);
      m_Hardware         = action(HardwareGenerations.class);
      m_Docker           = action(DockerImages.class);
      m_PretrainedModels = action(PretrainedModels.class);
      m_Nodes            = action(Nodes.class);
      m_JobTypes         = action(JobTypes.class);
      m_JobTemplates     = action(JobTemplates.class);
      m_Jobs             = action(Jobs.class);
      m_Generic          = action(Generic.class);
    }
    catch (Exception e) {
      getLogger().log(Level.SEVERE, "Failed to initialize actions!", e);
    }
  }

  /**
   * Returns the users action.
   *
   * @return		the users action
   */
  public Users users() {
    initActions();
    return m_Users;
  }

  /**
   * Returns the datasets action.
   *
   * @return		the datasets action
   */
  public Datasets datasets() {
    initActions();
    return m_Datasets;
  }

  /**
   * Returns the projects action.
   *
   * @return		the projects action
   */
  public Projects projects() {
    initActions();
    return m_Projects;
  }

  /**
   * Returns the teams action.
   *
   * @return		the teams action
   */
  public Teams teams() {
    initActions();
    return m_Teams;
  }

  /**
   * Returns the licenses action.
   *
   * @return		the licenses action
   */
  public Licenses licenses() {
    initActions();
    return m_Licenses;
  }

  /**
   * Returns the log action.
   *
   * @return		the log action
   */
  public Log log() {
    initActions();
    return m_Log;
  }

  /**
   * Returns the domains action.
   *
   * @return		the domains action
   */
  public Domains domains() {
    initActions();
    return m_Domains;
  }

  /**
   * Returns the cuda versions action.
   *
   * @return		the cuda versions action
   */
  public CudaVersions cuda() {
    initActions();
    return m_Cuda;
  }

  /**
   * Returns the frameworks action.
   *
   * @return		the frameworks action
   */
  public Frameworks frameworks() {
    initActions();
    return m_Frameworks;
  }

  /**
   * Returns the hardware generations action.
   *
   * @return		the hardware generations action
   */
  public HardwareGenerations hardware() {
    initActions();
    return m_Hardware;
  }

  /**
   * Returns the docker images action.
   *
   * @return		the docker images action
   */
  public DockerImages docker() {
    initActions();
    return m_Docker;
  }

  /**
   * Returns the pretrained models action.
   *
   * @return		the pretrained models action
   */
  public PretrainedModels pretrainedModels() {
    initActions();
    return m_PretrainedModels;
  }

  /**
   * Returns the nodes action.
   *
   * @return		the nodes action
   */
  public Nodes nodes() {
    initActions();
    return m_Nodes;
  }

  /**
   * Returns the job types action.
   *
   * @return		the job types action
   */
  public JobTypes jobTypes() {
    initActions();
    return m_JobTypes;
  }

  /**
   * Returns the job templates action.
   *
   * @return		the job templates action
   */
  public JobTemplates jobTemplates() {
    initActions();
    return m_JobTemplates;
  }

  /**
   * Returns the jobs action.
   *
   * @return		the jobs action
   */
  public Jobs jobs() {
    initActions();
    return m_Jobs;
  }

  /**
   * Returns the generic (API calls) action.
   *
   * @return		the generic action
   */
  public Generic generic() {
    initActions();
    return m_Generic;
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

    result = action.getDeclaredConstructor().newInstance();
    result.setConnection(connection());

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

    initActions();

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
