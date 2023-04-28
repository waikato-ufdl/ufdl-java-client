/*
 * ManagingDockerImages.java
 * Copyright (C) 2020-2023 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.DockerImages.DockerImage;
import com.github.waikatoufdl.ufdl4j.action.Frameworks;
import com.github.waikatoufdl.ufdl4j.action.HardwareGenerations.HardwareGeneration;
import com.github.waikatoufdl.ufdl4j.filter.DomainFilter;

/**
 * Example code for managing docker images.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ManagingDockerImages {

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

    // listing images
    System.out.println("--> listing docker images");
    DockerImage blahimg = null;
    for (DockerImage image : client.docker().list(new DomainFilter(client.domains().load("ic")))) {
      System.out.println(image);
      if (image.getName().equals("blah"))
        blahimg = image;
    }

    // create 'blah' if necessary
    HardwareGeneration hwPascal = client.hardware().load("Pascal");
    Frameworks.Framework fw = client.frameworks().load("tensorflow", "1.14");
    String domain = client.domains().load("ic").getName();
    if (blahimg == null) {
      System.out.println("--> creating docker image");
      blahimg = client.docker().create("blah", "1.0", "some:443/where/image", "some:443", "user", "pw", "10.0", fw.getPK(), domain, new String[]{"Train"}, hwPascal.getGeneration(), false, client.licenses().list().get(0).getName());
      System.out.println(blahimg);
    }

    // load blah
    System.out.println("--> loading docker image");
    blahimg = client.docker().load("blah", "1.0");
    String newVersion = "2.0";
    if (blahimg == null) {
      blahimg = client.docker().load("blah", "2.0");
      newVersion = "1.0";
    }

    // updating blah
    System.out.println("--> updating docker image");
    blahimg = client.docker().update(blahimg, "blah", newVersion, "some:443/where/image", "some:443", "user", "pw", "10.0", fw.getPK(), domain, new String[]{"Train"}, hwPascal.getGeneration(), false, client.licenses().list().get(0).getName());
    System.out.println(blahimg);

    // partially updating blahimg
    if (newVersion.equals("1.0"))
      newVersion = "2.0";
    else
      newVersion = "1.0";
    System.out.println("--> partially updating docker image");
    blahimg = client.docker().partialUpdate(blahimg, null, newVersion, null, null, null, null, null, null, null, null, null, null, null);
    System.out.println(blahimg);

    // delete 'blah'
    System.out.println("deleting docker image '" + blahimg + "'? " + client.docker().delete(blahimg));

    client.close();
  }
}
