/*
 * ManagingNodes.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Nodes.Node;

/**
 * Example code for managing cuda versions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ManagingNodes {

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

    // listing nodes
    System.out.println("--> listing nodes");
    Node localhost = null;
    for (Node node : client.nodes().list()) {
      System.out.println(node);
      if (node.getIP().equals("127.0.0.1"))
        localhost = node;
    }

    // create 'localhost' if necessary
    if (localhost == null) {
      System.out.println("--> creating node");
      localhost = client.nodes().create("127.0.0.1", "1.0", "Pascal", 10, 10);
      System.out.println(localhost);
    }

    // load localhost
    System.out.println("--> loading node");
    localhost = client.nodes().load("127.0.0.1");

    // updating blah
    System.out.println("--> updating node");
    localhost = client.nodes().update(localhost, "127.0.0.1", "1.0", "Volta", 10, 10);
    System.out.println(localhost);

    // partially updating blahnode
    System.out.println("--> partially updating node");
    localhost = client.nodes().partialUpdate(localhost, null, null, "Pascal", 11, 11);
    System.out.println(localhost);

    // delete blah
    System.out.println("deleting node '" + localhost + "'? " + client.nodes().delete(localhost));

    client.close();
  }
}
