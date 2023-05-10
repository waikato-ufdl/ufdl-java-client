/*
 * ManagingObjectDetectionDatasets.java
 * Copyright (C) 2020-2023 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.Domains;
import com.github.waikatoufdl.ufdl4j.action.Licenses.License;
import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets;
import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets.Annotation;
import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets.Annotations;
import com.github.waikatoufdl.ufdl4j.action.Projects.Project;
import com.github.waikatoufdl.ufdl4j.filter.DomainFilter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
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

    // get action
    ObjectDetectionDatasets action = client.action(ObjectDetectionDatasets.class);

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
      newName, "object detection dataset", project, gpl3, true, "");
    System.out.println(newDataset);

    // add files to dataset
    List<String> files = new ArrayList<>();
    if (args.length > 3) {
      for (int i = 3; i < args.length; i++)
        files.add(args[i]);
    }
    else {
      files.add("src/main/resources/com/github/waikatoufdl/ufdl4j/PKLot-2012-10-27_08_25_41.jpg");
      files.add("src/main/resources/com/github/waikatoufdl/ufdl4j/PKLot-2012-12-07_16_47_25.jpg");
      files.add("src/main/resources/com/github/waikatoufdl/ufdl4j/PKLot-2013-04-11_16_50_12.jpg");
    }
    Random rnd = new Random(1);
    File file;
    for (String f: files) {
      file = new File(f);
      System.out.println("--> adding file");
      action.addFile(newDataset, file, file.getName());
      // add file type
      System.out.println("--> adding file type");
      BufferedImage img = ImageIO.read(file);
      int width = img.getWidth();
      int height = img.getHeight();
      action.setFileType(newDataset, file.getName(), null, width, height, null);
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
      // metadata
      System.out.println("--> adding metadata");
      action.setMetadata(newDataset, file.getName(), "Full file path: " + file.getAbsolutePath());
      System.out.println(action.getMetadata(newDataset, file.getName()));
    }
    // remove some annotations
    file = new File(files.get(0));
    System.out.println("--> removing annotations for '" + file.getName() + "'");
    System.out.println(action.deleteAnnotations(newDataset, file.getName()));

    // get all annotations
    System.out.println("--> get all annotations");
    Map<String,Annotations> all = action.getAnnotations(newDataset);
    System.out.println(all);

    // get annotations for second image
    if (files.size() > 1) {
      System.out.println("--> get annotations for image");
      List<Annotation> anns = action.getAnnotations(newDataset, new File(files.get(1)).getName());
      System.out.println(anns);
    }

    // download dataset in MSCOCO format
    System.out.println("--> downloading dataset");
    File output = new File(System.getProperty("java.io.tmpdir") + "/" + newName + ".zip");
    if (action.download(newDataset, new String[]{"to-coco-od", "-o", "dataset.json"}, output))
      System.out.println("--> downloaded dataset to " + output);

    // get file from dataset
    file = new File(files.get(0));
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

    // list datasets
    Domains.Domain domain = client.domains().load("od");
    DomainFilter domainFilter = new DomainFilter(domain);
    System.out.println("--> listing datasets for domain " + domain);
    for (Dataset dataset: client.datasets().list(domainFilter))
      System.out.println(dataset);

    // delete file from dataset
    file = new File(files.get(0));
    System.out.println("--> deleting file");
    if (action.deleteFile(newDataset, file.getName()))
      System.out.println("--> deleted file: " + file.getName());

    client.close();
  }
}
