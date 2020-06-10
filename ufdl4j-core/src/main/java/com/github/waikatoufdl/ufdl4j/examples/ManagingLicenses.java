/*
 * ManagingLicenses.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Licenses.License;

/**
 * Example code for managing licenses.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ManagingLicenses {

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

    // list licenses (and check whether 'glp3' already exists)
    System.out.println("--> listing licenses");
    for (License license : client.licenses().list())
      System.out.println(license);

    // load license via primary key
    System.out.println("--> loading license with PK=1");
    System.out.println(client.licenses().load(1));

    // load license via name
    System.out.println("--> loading license with name=GPL3");
    System.out.println(client.licenses().load("GPL3"));

    client.close();
  }
}
