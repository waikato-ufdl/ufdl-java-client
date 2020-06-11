/*
 * ManagingLogEntries.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Log.LogEntry;
import com.github.waikatoufdl.ufdl4j.action.LogLevel;

/**
 * Example code for managing log entries.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ManagingLogEntries {

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
    System.out.println("--> listing log entries (max 10)");
    int count = 0;
    for (LogEntry entry: client.log().list()) {
      System.out.println(entry);
      count++;
      if (count == 10)
        break;
    }

    // create log entry
    System.out.println("--> creating log entry");
    LogEntry dummyentry = client.log().create(LogLevel.INFO, "my log entry");
    System.out.println(dummyentry);

    // loading log entry
    System.out.println("--> loading log entry with pk=" + dummyentry.getPK());
    dummyentry = client.log().load(dummyentry.getPK());
    System.out.println(dummyentry);

    // delete dummy entry
    System.out.println("deleting log entry '" + dummyentry + "'? " + client.log().delete(dummyentry));

    client.close();
  }
}
