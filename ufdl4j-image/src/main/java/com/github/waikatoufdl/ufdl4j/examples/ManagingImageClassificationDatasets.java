/*
 * ManagingImageClassificationDatasets.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.ImageClassificationDatasets;
import com.github.waikatoufdl.ufdl4j.action.Projects.Project;

import java.io.File;
import java.util.List;

/**
 * Example code for managing image classification datasets.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ManagingImageClassificationDatasets {

  /**
   * Expects three arguments:
   * 1. backend URL, eg http://127.0.0.1:8000
   * 2. backend user, eg admin
   * 3. user password, eg admin
   * Otherwise the above default values are used.
   * Additional arguments are interpreted as files to upload.
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
    int project = -1;
    List<Project> projects = client.projects().list();
    if (projects.size() > 0)
      project = projects.get(0).getPK();

    // get action
    ImageClassificationDatasets action = client.action(ImageClassificationDatasets.class);

    // create dataset
    System.out.println("--> creating dataset");
    Dataset newDataset = action.create(
      "dummy-" + System.currentTimeMillis(), 1, project, "GPLv3", true, "");
    System.out.println(newDataset);

    // add files to dataset
    if (args.length > 3) {
      for (int i = 3; i < args.length; i++) {
        File file = new File(args[i]);
        action.addFile(newDataset.getPK(), file, file.getName());
      }
    }

    client.close();
  }
}
