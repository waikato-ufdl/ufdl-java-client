/*
 * ManagingHardwareGenerations.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.HardwareGenerations.HardwareGeneration;

/**
 * Example code for managing hardware generations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ManagingHardwareGenerations {

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
    System.out.println("--> listing hardware generations");
    HardwareGeneration genFermat = null;
    for (HardwareGeneration gen : client.hardware().list()) {
      System.out.println(gen);
      if (gen.getGeneration().equals("Fermat"))
        genFermat = gen;
    }

    // create 'Fermat' if necessary
    if (genFermat == null) {
      System.out.println("--> creating hardware generation");
      genFermat = client.hardware().create("Fermat", 1.0, 7.0);
      System.out.println(genFermat);
    }

    // load Fermat
    System.out.println("--> loading hardware generation");
    genFermat = client.hardware().load("Fermat");

    // updating Fermat
    System.out.println("--> updating hardware generation");
    genFermat = client.hardware().update(genFermat, "Fermat", 5.0, 11.0);
    System.out.println(genFermat);

    // partially updating blahteam
    System.out.println("--> partially updating hardware generation");
    genFermat = client.hardware().partialUpdate(genFermat, null, null, 9.0);
    System.out.println(genFermat);

    // delete '99'
    System.out.println("deleting hardware generation '" + genFermat + "'? " + client.hardware().delete(genFermat));

    client.close();
  }
}
