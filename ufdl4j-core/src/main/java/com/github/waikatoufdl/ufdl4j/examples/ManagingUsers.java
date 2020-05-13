/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
    System.out.println("deleting user '" + blahuser + "'? " + client.users().delete(blahuser.getPK()));

    // load user via primary key
    System.out.println("--> loading user with PK=1");
    System.out.println(client.users().load(1));

    client.close();
  }
}
