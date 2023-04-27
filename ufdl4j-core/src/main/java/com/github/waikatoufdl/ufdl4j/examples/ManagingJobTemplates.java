/*
 * ManagingManagingJobTemplatess.java
 * Copyright (C) 2020-2023 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.JobTemplates.JobTemplate;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Example code for managing job templates.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ManagingJobTemplates {

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

    // listing templates
    System.out.println("--> listing job templates");
    JobTemplate testTemplate = null;
    for (JobTemplate template : client.jobTemplates().list()) {
      System.out.println(template);
      if (template.getName().equals("yolo_v5_objectdetection-train-test"))
        testTemplate = template;
    }

    // import 'yolo_v5_objectdetection-train-test' if necessary
    InputStream is = ClassLoader.getSystemResourceAsStream("com/github/waikatoufdl/ufdl4j/examples/0012_yolo_v5_objectdetection_train.json");
    JsonObject template = (JsonObject) JsonParser.parseReader(new InputStreamReader(is));
    if (testTemplate == null) {
      System.out.println("--> importing job template");
      testTemplate = client.jobTemplates().importTemplate(template);
      System.out.println(testTemplate);
    }

    // load blah
    System.out.println("--> loading job template");
    testTemplate = client.jobTemplates().load("yolo_v5_objectdetection-train-test", 1);
    System.out.println(testTemplate);

    // soft delete 'blah'
    System.out.println("soft deleting job template '" + testTemplate + "'? " + client.jobTemplates().delete(testTemplate, false));

    // reinstate 'blah'
    System.out.println("reinstating job template '" + testTemplate + "'? " + client.jobTemplates().reinstate(testTemplate));

    // hard delete 'blah'
    // TODO hard delete doesn't work at the moment
    //System.out.println("hard deleting job template '" + testTemplate + "'? " + client.jobTemplates().delete(testTemplate, true));

    client.close();
  }
}
