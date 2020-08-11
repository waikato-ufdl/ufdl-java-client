/*
 * ManagingCudaVersions.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.CudaVersions.CudaVersion;

/**
 * Example code for managing cuda versions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ManagingCudaVersions {

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

    // listing cuda versions
    System.out.println("--> listing cuda versions");
    CudaVersion cuda99 = null;
    for (CudaVersion cuda : client.cuda().list()) {
      System.out.println(cuda);
      if (cuda.getVersion().equals("99"))
        cuda99 = cuda;
    }

    // create '99' if necessary
    if (cuda99 == null) {
      System.out.println("--> creating cuda version");
      cuda99 = client.cuda().create("99.0", "99.1.123", "657.111.222");
      System.out.println(cuda99);
    }

    // load 99
    System.out.println("--> loading cuda version");
    cuda99 = client.cuda().load("99.0");

    // updating 99
    System.out.println("--> updating cuda version");
    cuda99 = client.cuda().update(cuda99, "99.0", "99.1.123", "657.111.333");
    System.out.println(cuda99);

    // partially updating blahcuda
    System.out.println("--> partially updating cuda version");
    cuda99 = client.cuda().partialUpdate(cuda99, null, null, "657.111.222");
    System.out.println(cuda99);

    // delete '99'
    System.out.println("deleting cuda version '" + cuda99 + "'? " + client.cuda().delete(cuda99));

    client.close();
  }
}
