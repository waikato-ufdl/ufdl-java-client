/*
 * ManagingObjectDetectionDatasets.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.Licenses.License;
import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets;
import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets.Annotation;
import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets.Annotations;
import com.github.waikatoufdl.ufdl4j.action.Projects.Project;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Example code for managing object detection datasets.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ManagingObjectDetectionDatasets {

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
    ObjectDetectionDatasets action = client.action(ObjectDetectionDatasets.class);

    // load license
    License gpl3 = client.licenses().load("GPL3");

    // create dataset
    System.out.println("--> creating dataset");
    String newName = "dummy-" + System.currentTimeMillis();
    Dataset newDataset = action.create(
      newName, "object detection dataset", project, gpl3, true, "");
    System.out.println(newDataset);

    // add files to dataset
    if (args.length > 3) {
      Random rnd = new Random(1);
      for (int i = 3; i < args.length; i++) {
        File file = new File(args[i]);
        System.out.println("--> adding file");
        action.addFile(newDataset, file, file.getName());
        // add annotations
	Annotations annotations = new Annotations();
	int num = rnd.nextInt(10) + 1;
	for (int n = 0; n < num; n++) {
	  annotations.add(new Annotation(
	    rnd.nextInt(100),
	    rnd.nextInt(100),
	    rnd.nextInt(100) + 1,
	    rnd.nextInt(100) + 1,
	    "label" + rnd.nextInt(5)));
	}
	action.setAnnotations(newDataset, file.getName(), annotations);
      }
      // remove annotations
      System.out.println("--> removing annotations");
      System.out.println(action.deleteAnnotations(newDataset, new File(args[3]).getName()));
    }

    // get all annotations
    System.out.println("--> get all annotations");
    Map<String,Annotations> all = action.getAnnotations(newDataset);
    System.out.println(all);

    // get annotations for second image
    if (args.length > 4) {
      System.out.println("--> get annotations for image");
      List<Annotation> anns = action.getAnnotations(newDataset, new File(args[4]).getName());
      System.out.println(anns);
    }

    // download dataset
    System.out.println("--> downloading dataset");
    File output = new File(System.getProperty("java.io.tmpdir") + "/" + newName + ".zip");
    if (action.download(newDataset, output))
      System.out.println("--> downloaded dataset to " + output);

    // get file from dataset
    if (args.length > 3) {
      File file = new File(args[3]);
      output = new File(System.getProperty("java.io.tmpdir") + "/" + newName + "-" + file.getName());
      System.out.println("--> downloading file");
      if (action.getFile(newDataset, file.getName(), output))
	System.out.println("--> downloaded file: " + output);
      System.out.println("--> streaming file");
      FileOutputStream stream = new FileOutputStream(output);
      if (action.getFile(newDataset, file.getName(), stream))
	System.out.println("--> streamed file: " + output);
      stream.flush();
      stream.close();
    }

    // delete file from dataset
    if (args.length > 3) {
      File file = new File(args[3]);
      System.out.println("--> deleting file");
      if (action.deleteFile(newDataset, file.getName()))
	System.out.println("--> deleted file: " + file.getName());
    }

    client.close();
  }
}
