/*
 * ManagingProjects.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Projects.Project;
import com.github.waikatoufdl.ufdl4j.action.Teams.Team;

import java.util.List;

/**
 * Example code for managing projects.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ManagingProjects {

  /**
   * Expects three arguments:
   * 1. backend URL, eg http://127.0.0.1:8000
   * 2. backend user, eg admin
   * 3. user password, eg admin
   * Otherwise the above default values are used
   *
   * @param args	the parameters to supply
   * @throws Exception	if queries fail for some reason
   */
  public static void main(String[] args) throws Exception {
    System.out.println("--> connecting to backend");
    Client client;
    if (args.length < 3)
      client = new Client("http://127.0.0.1:8000", "admin", "admin");
    else
      client = new Client(args[0], args[1], args[1]);

    // list projects
    Project blahproject = null;
    System.out.println("--> listing projects");
    for (Project project: client.projects().list()) {
      System.out.println(project);
      if (project.getName().equals("blahproject"))
        blahproject = project;
    }

    // create 'blahproject' if necessary
    if (blahproject == null) {
      int team_id = -1;
      List<Team> teams = client.teams().list();
      if (teams.size() > 0)
        team_id = teams.get(0).getPK();
      System.out.println("--> creating project");
      blahproject = client.projects().create("blahproject", team_id);
      System.out.println(blahproject);
    }

    // updating 'blahproject'
    System.out.println("--> partially updating project");
    blahproject = client.projects().partialUpdate(blahproject, "blahproject2", null);
    System.out.println(blahproject);

    // updating 'blahproject2'
    System.out.println("--> partially updating project");
    blahproject = client.projects().partialUpdate(blahproject, "blahproject", null);
    System.out.println(blahproject);

    // (soft) delete 'blahproject'
    System.out.println("deleting project '" + blahproject + "'? " + client.projects().delete(blahproject, false));

    // reinstating 'blahproject'
    System.out.println("reinstating project '" + blahproject + "'? " + client.projects().reinstate(blahproject));

    client.close();
  }

}
