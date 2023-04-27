/*
 * ManagingDatasets.java
 * Copyright (C) 2020-2023 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.Licenses.License;
import com.github.waikatoufdl.ufdl4j.action.Projects.Project;
import com.github.waikatoufdl.ufdl4j.action.Teams;

import java.util.List;

/**
 * Example code for managing datasets.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ManagingDatasets {

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

    // list datasets
    System.out.println("--> listing datasets");
    for (Dataset dataset: client.datasets().list())
      System.out.println(dataset);

    // grab first available project ID
    Project project = null;
    List<Project> projects = client.projects().list();
    if (projects.size() > 0)
      project = projects.get(0);
    // create dummy project if none present
    if (project == null) {
      // get team
      Teams.Team team = null;
      for (Teams.Team t : client.teams().list()) {
        team = t;
        break;
      }
      // create dummy team
      if (team == null)
        team = client.teams().create("blahteam");
      // create project
      project = client.projects().create("blahproject", team.getPK());
    }

    // load license
    License gpl3 = client.licenses().load("GPL3");

    // create dataset
    System.out.println("--> creating dataset");
    Dataset newDataset = client.datasets().create(
      "dummy-" + System.currentTimeMillis(), "dataset", project.getPK(), gpl3, true, "");
    System.out.println(newDataset);

    // update dataset
    System.out.println("--> partial update dataset");
    newDataset = client.datasets().partialUpdate(newDataset, null, "new desription", null, null, null, null);
    System.out.println(newDataset);

    client.close();
  }
}
