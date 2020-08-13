/*
 * ManagingFrameworks.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Frameworks.Framework;

/**
 * Example code for managing frameworks.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ManagingFrameworks {

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

    // listing frameworks
    System.out.println("--> listing frameworks");
    Framework blahframework = null;
    for (Framework framework : client.frameworks().list()) {
      System.out.println(framework);
      if (framework.getName().equals("blah"))
        blahframework = framework;
    }

    // create 'blah' if necessary
    if (blahframework == null) {
      System.out.println("--> creating framework");
      blahframework = client.frameworks().create("blah", "1.0");
      System.out.println(blahframework);
    }

    // load blah
    System.out.println("--> loading framework");
    blahframework = client.frameworks().load("blah", "1.0");

    // updating blah
    System.out.println("--> updating framework");
    blahframework = client.frameworks().update(blahframework, "blah", "2.0");
    System.out.println(blahframework);

    // partially updating blahframework
    System.out.println("--> partially updating framework");
    blahframework = client.frameworks().partialUpdate(blahframework, null, "1.0");
    System.out.println(blahframework);

    // delete blah
    System.out.println("deleting framework '" + blahframework + "'? " + client.frameworks().delete(blahframework));

    client.close();
  }
}
