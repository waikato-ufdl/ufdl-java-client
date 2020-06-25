/*
 * ManagingSpeechDatasets.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.Licenses.License;
import com.github.waikatoufdl.ufdl4j.action.Projects.Project;
import com.github.waikatoufdl.ufdl4j.action.SpeechDatasets;
import com.github.waikatoufdl.ufdl4j.action.SpeechDatasets.SpeechDataset;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

/**
 * Example code for managing speech datasets.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ManagingSpeechDatasets {

  /**
   * Expects three arguments:
   * 1. backend URL, eg http://127.0.0.1:8000
   * 2. backend user, eg admin
   * 3. user password, eg admin
   * Otherwise the above default values are used.
   * Additional arguments are interpreted as pairs of sound and transcript files to upload.
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
    SpeechDatasets action = client.action(SpeechDatasets.class);

    // load license
    License gpl3 = client.licenses().load("GPL3");

    // create dataset
    System.out.println("--> creating dataset");
    String newName = "dummy-" + System.currentTimeMillis();
    Dataset newDataset = action.create(
      newName, "speech dataset", project, gpl3, true, "");
    System.out.println(newDataset);

    // add files to dataset
    if (args.length > 3) {
      for (int i = 3; i < args.length; i += 2) {
        File soundfile = new File(args[i]);
        File transfile = new File(args[i+1]);
        List<String> lines = Files.readAllLines(transfile.toPath());
        StringBuilder transcript = new StringBuilder();
        for (String line: lines)
          transcript.append(line).append("\n");
        System.out.println("--> adding file");
        action.addFile(newDataset, soundfile, soundfile.getName());
        // set transcript
        action.setTranscript(newDataset, soundfile.getName(), transcript.toString().trim());
        // metadata
        System.out.println("--> adding metadata");
        action.setMetadata(newDataset, soundfile.getName(), "Sound file: " + soundfile.getAbsolutePath() + "\nTranscript file: " + transfile.getAbsolutePath());
        System.out.println(action.getMetadata(newDataset, soundfile.getName()));
      }
    }

    // get transcripts
    System.out.println("--> downloading transcripts");
    Map<String,String> transcripts = action.getTranscripts(newDataset);
    System.out.println(transcripts);

    // re-load dataset
    System.out.println("--> re-loading dataset");
    newDataset = action.load(newDataset.getPK());
    SpeechDataset spDataset = newDataset.as(SpeechDataset.class);
    System.out.println("--> dataset transcripts");
    System.out.println(spDataset);
    System.out.println(spDataset.transcripts());

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
