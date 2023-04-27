/*
 * ManagingImageSegmentationDatasets.java
 * Copyright (C) 2021-2023 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.Domains;
import com.github.waikatoufdl.ufdl4j.action.ImageSegmentationDatasets;
import com.github.waikatoufdl.ufdl4j.action.Licenses.License;
import com.github.waikatoufdl.ufdl4j.action.Projects.Project;
import com.github.waikatoufdl.ufdl4j.filter.DomainFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Example code for managing image segmentation datasets.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ManagingImageSegmentationDatasets {

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

    // get action
    ImageSegmentationDatasets action = client.action(ImageSegmentationDatasets.class);

    // list datasets
    System.out.println("--> listing datasets");
    for (Dataset dataset: action.list())
      System.out.println(dataset);

    // grab first available project ID
    int project = -1;
    List<Project> projects = client.projects().list();
    if (projects.size() > 0)
      project = projects.get(0).getPK();

    // load license
    License gpl3 = client.licenses().load("GPL3");

    // create dataset
    System.out.println("--> creating dataset");
    String newName = "dummy-" + System.currentTimeMillis();
    Dataset newDataset = action.create(
      newName, "image segmentation dataset", project, gpl3, true, "");
    System.out.println(newDataset);

    // add files to dataset
    if (args.length > 3) {
      // determine layers and group files with their layers
      Set<String> layersSet = new HashSet<>();
      Map<String,List<String>> files = new HashMap<>();
      for (int i = 3; i < args.length; i++) {
	if (!new File(args[i]).exists())
	  continue;
	if (args[i].endsWith(".jpg")) {
	  files.put(args[i], new ArrayList<>());
	}
	else if (args[i].endsWith(".png")) {
	  String jpg = args[i].replaceAll("-[a-zA-Z0-9]+.png", ".jpg");
	  files.get(jpg).add(args[i]);
	  String layer = args[i].replaceAll(".*-([a-zA-Z0-9]+).png", "$1");
	  layersSet.add(layer);
	}
      }
      List<String> delete = new ArrayList<>();
      for (String key: files.keySet()) {
	if (files.get(key).size() == 0)
	  delete.add(key);
      }
      for (String key: delete)
	files.remove(key);
      List<String> layers = new ArrayList<>(layersSet);
      Collections.sort(layers);

      // set layers/labels
      action.setLabels(newDataset, layers);
      for (String key: files.keySet()) {
	File file = new File(key);
	System.out.println("--> adding file");
	action.addFile(newDataset, file, file.getName());
	// add layers
	for (String l: files.get(key)) {
	  String layer = l.replaceAll(".*-([a-zA-Z0-9]+).png", "$1");
	  action.setLayer(newDataset, file.getName(), layer, new File(l));
	}
	// metadata
	System.out.println("--> adding metadata");
	action.setMetadata(newDataset, file.getName(), "Full file path: " + file.getAbsolutePath());
	System.out.println(action.getMetadata(newDataset, file.getName()));
      }
    }

    // get labels
    System.out.println("--> retrieving labels");
    List<String> labels = action.getLabels(newDataset);
    System.out.println(labels);

    // get labels for individual files
    System.out.println("--> retrieving labels per file");
    newDataset = action.load(newDataset.getPK());
    for (String f: newDataset.getFiles()) {
      System.out.println("file: " + f);
      for (String l: labels) {
        File output = new File(System.getProperty("java.io.tmpdir") + "/" + f.replace(".jpg", "") + "-" + l + ".png");
        boolean ok = action.getLayer(newDataset, f, l, output);
        System.out.println("layer '" + l + "' downloaded: " + ok);
      }
    }

    // download dataset
    System.out.println("--> downloading dataset");
    File output = new File(System.getProperty("java.io.tmpdir") + "/" + newName + ".zip");
    if (action.download(newDataset, new String[]{"to-blue-channel-is", "-o", "."}, output))
      System.out.println("--> downloaded dataset to " + output);

    // list datasets
    Domains.Domain domain = client.domains().load("is");
    DomainFilter domainFilter = new DomainFilter(domain.getPK());
    System.out.println("--> listing datasets for domain " + domain);
    for (Dataset dataset: client.datasets().list(domainFilter))
      System.out.println(dataset);

    client.close();
  }
}
