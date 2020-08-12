/*
 * ManagingDockerImages.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.DockerImages.DockerImage;

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
    for (DockerImage image : client.docker().list()) {
      System.out.println(image);
      if (image.getName().equals("blah"))
        blahimg = image;
    }

    // create 'blah' if necessary
    if (blahimg == null) {
      System.out.println("--> creating docker image");
      blahimg = client.docker().create("blah", "1.0", "some:443/where/image", "some:443", "user", "pw", "10.0", 1, "ic", new String[]{"train"}, "Pascal", false);
      System.out.println(blahimg);
    }

    // load blah
    System.out.println("--> loading docker image");
    blahimg = client.docker().load("blah");

    // updating blah
    System.out.println("--> updating docker image");
    blahimg = client.docker().update(blahimg, "blah", "2.0", "some:443/where/image", "some:443", "user", "pw", "10.0", 1, "ic", new String[]{"train"}, "Pascal", false);
    System.out.println(blahimg);

    // partially updating blahimg
    System.out.println("--> partially updating docker image");
    blahimg = client.docker().partialUpdate(blahimg, "1.0", null, null, null, null, null, null, null, null, null, null, null);
    System.out.println(blahimg);

    // delete 'blah'
    System.out.println("deleting docker image '" + blahimg + "'? " + client.docker().delete(blahimg));

    client.close();
  }
}
