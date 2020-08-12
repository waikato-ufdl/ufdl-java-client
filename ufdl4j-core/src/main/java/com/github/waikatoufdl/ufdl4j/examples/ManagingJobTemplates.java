/*
 * ManagingManagingJobTemplatess.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.JobTemplates.JobTemplate;

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
    JobTemplate blahtemplate = null;
    for (JobTemplate template : client.jobTemplates().list()) {
      System.out.println(template);
      if (template.getName().equals("blah"))
        blahtemplate = template;
    }

    // create 'blah' if necessary
    if (blahtemplate == null) {
      System.out.println("--> creating job template");
      blahtemplate = client.jobTemplates().create("blah", 1, "public", 1, "ic", "train", "my.funky.Executor", "", "echo 1");
      System.out.println(blahtemplate);
    }

    // load blah
    System.out.println("--> loading job template");
    blahtemplate = client.jobTemplates().load("blah");

    // updating blah
    System.out.println("--> updating job template");
    blahtemplate = client.jobTemplates().update(blahtemplate, "blah2", 1, "public", 1, "ic", "train", "my.funky.Executor", "", "echo 1");
    System.out.println(blahtemplate);

    // partially updating blahtemplate
    System.out.println("--> partially updating job template");
    blahtemplate = client.jobTemplates().partialUpdate(blahtemplate, "blah", null, "project", null, null, null, null, null, null);
    System.out.println(blahtemplate);

    // soft delete 'blah'
    System.out.println("soft deleting job template '" + blahtemplate + "'? " + client.jobTemplates().delete(blahtemplate, false));

    // reinstate 'blah'
    System.out.println("reinstating job template '" + blahtemplate + "'? " + client.jobTemplates().reinstate(blahtemplate));

    // hard delete 'blah'
    System.out.println("hard deleting job template '" + blahtemplate + "'? " + client.jobTemplates().delete(blahtemplate, true));

    client.close();
  }
}
