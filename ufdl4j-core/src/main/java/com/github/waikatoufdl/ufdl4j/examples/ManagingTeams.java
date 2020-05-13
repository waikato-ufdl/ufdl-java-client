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
 * ManagingTeams.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Teams.Team;

/**
 * Example code for managing projects.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ManagingTeams {

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
    System.out.println("--> listing teams");
    for (Team team : client.teams().list())
      System.out.println(team);

    // TODO

    client.close();
  }
}
