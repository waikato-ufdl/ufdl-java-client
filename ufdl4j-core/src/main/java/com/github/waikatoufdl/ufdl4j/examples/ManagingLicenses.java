/*
 * ManagingLicenses.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Licenses.Condition;
import com.github.waikatoufdl.ufdl4j.action.Licenses.Domain;
import com.github.waikatoufdl.ufdl4j.action.Licenses.License;
import com.github.waikatoufdl.ufdl4j.action.Licenses.Limitation;
import com.github.waikatoufdl.ufdl4j.action.Licenses.Permission;

import static com.github.waikatoufdl.ufdl4j.action.Licenses.Condition.SAME_LICENSE;
import static com.github.waikatoufdl.ufdl4j.action.Licenses.Domain.DATA;
import static com.github.waikatoufdl.ufdl4j.action.Licenses.Limitation.LIABILITY;
import static com.github.waikatoufdl.ufdl4j.action.Licenses.Limitation.WARRANTY;
import static com.github.waikatoufdl.ufdl4j.action.Licenses.Permission.COMMERCIAL_USE;
import static com.github.waikatoufdl.ufdl4j.action.Licenses.Permission.DISTRIBUTION;

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
    License dummy = null;
    for (License license : client.licenses().list()) {
      System.out.println(license);
      if (license.getName().equals("DUMMY"))
        dummy = license;
    }

    // load license via primary key
    System.out.println("--> loading license with PK=1");
    System.out.println(client.licenses().load(1));

    // load license via name
    System.out.println("--> loading license with name=GPL3");
    System.out.println(client.licenses().load("GPL3"));

    // create license
    if (dummy == null) {
      System.out.println("--> creating license with name=DUMMY");
      dummy = client.licenses().create("DUMMY", "http://nowhere.com");
      System.out.println(dummy);

      // add sub-descriptors
      System.out.println("--> adding sub-descriptions");
      client.licenses().addSubDescriptors(
        dummy,
	new Domain[]{DATA},
	new Permission[]{COMMERCIAL_USE, DISTRIBUTION},
	new Condition[]{SAME_LICENSE},
	new Limitation[]{LIABILITY, WARRANTY});
      dummy = client.licenses().load(dummy.getPK());
      System.out.println(dummy);

      // remove sub-descriptors
      System.out.println("--> removing sub-descriptions");
      client.licenses().removeSubDescriptors(
        dummy,
	null,
	new Permission[]{DISTRIBUTION},
	null,
	null);
      dummy = client.licenses().load(dummy.getPK());
      System.out.println(dummy);
    }

    // (partially) update license
    System.out.println("--> updating license name (1)");
    dummy = client.licenses().partialUpdate(dummy, "DUMMY2", null);
    System.out.println(dummy);
    System.out.println("--> updating license name (2)");
    dummy = client.licenses().partialUpdate(dummy, "DUMMY", null);
    System.out.println(dummy);

    // update license
    System.out.println("--> updating license (1)");
    dummy = client.licenses().update(dummy, "DUMMY2", "http://somewhereelse.com");
    System.out.println(dummy);
    System.out.println("--> updating license (2)");
    dummy = client.licenses().update(dummy, "DUMMY", "http://nowhere.com");
    System.out.println(dummy);

    // delete license
    System.out.println("--> deleting license: " + dummy);
    System.out.println(client.licenses().delete(dummy));

    client.close();
  }
}
