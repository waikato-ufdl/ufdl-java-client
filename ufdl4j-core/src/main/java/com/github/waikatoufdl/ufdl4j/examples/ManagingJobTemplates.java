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

    // listing teams
    System.out.println("--> listing job templates");
    JobTemplate blahjobtype = null;
    for (JobTemplate templates : client.jobTemplates().list()) {
      System.out.println(templates);
      if (templates.getName().equals("blah"))
        blahjobtype = templates;
    }

    // create 'blah' if necessary
    if (blahjobtype == null) {
      System.out.println("--> creating job template");
      blahjobtype = client.jobTemplates().create("blah", 1, "public", 1, "ic", "train", "my.funky.Executor", "", "echo 1");
      System.out.println(blahjobtype);
    }

    // load blah
    System.out.println("--> loading job template");
    blahjobtype = client.jobTemplates().load("blah");

    // updating blah
    System.out.println("--> updating job template");
    blahjobtype = client.jobTemplates().update(blahjobtype, "blah2", 1, "public", 1, "ic", "train", "my.funky.Executor", "", "echo 1");
    System.out.println(blahjobtype);

    // partially updating blahteam
    System.out.println("--> partially updating job template");
    blahjobtype = client.jobTemplates().partialUpdate(blahjobtype, "blah", null, "project", null, null, null, null, null, null);
    System.out.println(blahjobtype);

    // soft delete 'blah'
    System.out.println("soft deleting job template '" + blahjobtype + "'? " + client.jobTemplates().delete(blahjobtype, false));

    // reinstate 'blah'
    System.out.println("reinstating job template '" + blahjobtype + "'? " + client.jobTemplates().reinstate(blahjobtype));

    // hard delete 'blah'
    System.out.println("hard deleting job template '" + blahjobtype + "'? " + client.jobTemplates().delete(blahjobtype, true));

    client.close();
  }
}
