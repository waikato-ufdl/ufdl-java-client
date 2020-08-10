/*
 * ManagingDomains.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Domains.Domain;

/**
 * Example code for managing domains.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ManagingDomains {

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

    // list domains
    System.out.println("--> listing domains");
    for (Domain domain: client.domains().list())
      System.out.println(domain);

    // load domain
    int pk = 1;
    System.out.println("--> loading domain " + pk);
    Domain domain = client.domains().load(pk);
    System.out.println(domain);

    client.close();
  }
}
