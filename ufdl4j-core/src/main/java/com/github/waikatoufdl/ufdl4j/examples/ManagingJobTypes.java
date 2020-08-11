/*
 * ManagingJobTypes.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.JobTypes.JobType;

/**
 * Example code for managing job types.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ManagingJobTypes {

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

    // listing types
    System.out.println("--> listing job types");
    JobType blahjobtype = null;
    for (JobType types : client.jobTypes().list()) {
      System.out.println(types);
      if (types.getName().equals("blah"))
        blahjobtype = types;
    }

    // create 'blah' if necessary
    if (blahjobtype == null) {
      System.out.println("--> creating job type");
      blahjobtype = client.jobTypes().create("blah");
      System.out.println(blahjobtype);
    }

    // load blah
    System.out.println("--> loading job type");
    blahjobtype = client.jobTypes().load("blah");

    // updating blah
    System.out.println("--> updating job type");
    blahjobtype = client.jobTypes().update(blahjobtype, "blah2");
    System.out.println(blahjobtype);

    // partially updating blahtype
    System.out.println("--> partially updating job type");
    blahjobtype = client.jobTypes().partialUpdate(blahjobtype, "blah");
    System.out.println(blahjobtype);

    // delete 'blah'
    System.out.println("deleting job type '" + blahjobtype + "'? " + client.jobTypes().delete(blahjobtype));

    client.close();
  }
}
