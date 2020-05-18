/*
 * ManagingUsers.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Users.User;

/**
 * Example code for managing users.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ManagingUsers {

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

    // list users (and check whether 'blahuser' already exists)
    System.out.println("--> listing users");
    User blahuser = null;
    for (User user: client.users().list()) {
      System.out.println(user);
      if (user.getUserName().equals("blahuser"))
	blahuser = user;
    }

    // create 'blahuser' if necessary
    if (blahuser == null) {
      System.out.println("--> creating user");
      blahuser = client.users().create("blahuser", "blahpw", "blahfirst", "blahlast", "blah@example.org");
      System.out.println(blahuser);
    }

    // delete 'blahuser'
    System.out.println("deleting user '" + blahuser + "'? " + client.users().delete(blahuser));

    // load user via primary key
    System.out.println("--> loading user with PK=1");
    System.out.println(client.users().load(1));

    // load user via name
    System.out.println("--> loading user with name=admin");
    System.out.println(client.users().load("admin"));

    client.close();
  }
}
