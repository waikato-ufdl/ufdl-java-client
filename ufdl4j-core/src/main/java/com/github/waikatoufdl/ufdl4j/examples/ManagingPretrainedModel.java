/*
 * ManagingManagingPretrainedModelss.java
 * Copyright (C) 2020-2023 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Domains;
import com.github.waikatoufdl.ufdl4j.action.PretrainedModels.PretrainedModel;
import com.github.waikatoufdl.ufdl4j.filter.DomainFilter;

import java.io.File;

/**
 * Example code for managing pretrained models.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ManagingPretrainedModel {

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

    // listing templates
    System.out.println("--> listing pretrained models");
    PretrainedModel blahpre = null;
    for (PretrainedModel template : client.pretrainedModels().list()) {
      System.out.println(template);
      if (template.getName().equals("blah"))
        blahpre = template;
    }

    // listing templates
    Domains.Domain d = client.domains().load("od");
    DomainFilter dfilter = new DomainFilter(d);
    System.out.println("--> listing pretrained models for domain " + d);
    for (PretrainedModel pretrained : client.pretrainedModels().list(dfilter)) {
      System.out.println(pretrained);
    }

    // create 'blah' if necessary
    if (blahpre == null) {
      System.out.println("--> creating pretrained model");
      blahpre = client.pretrainedModels().create(
        "blah",
        client.frameworks().list().get(0).getPK(),
        "ic",
        client.licenses().list().get(0).getName(),
        "http://example.com/where",
        "blah");
      System.out.println(blahpre);
    }

    // updating blah
    System.out.println("--> updating pretrained model");
    blahpre = client.pretrainedModels().update(
      blahpre,
      "blah",
      client.frameworks().list().get(0).getPK(),
      "od",
      client.licenses().list().get(0).getName(),
      "http://example.com/where/else",
      "blah",
      "");
    System.out.println(blahpre);

    // partially updating blahtemplate
    System.out.println("--> partially updating pretrained model");
    blahpre = client.pretrainedModels().partialUpdate(
      blahpre,
      null,
      null,
      "ic",
      null,
      "http://example.com/where",
      null,
      null);

    System.out.println(blahpre);

    // soft delete 'blah'
    System.out.println("soft deleting pretrained model '" + blahpre + "'? " + client.pretrainedModels().delete(blahpre, false));

    // reinstate 'blah'
    System.out.println("reinstating pretrained model '" + blahpre + "'? " + client.pretrainedModels().reinstate(blahpre));

    // hard delete 'blah'
    System.out.println("hard deleting pretrained model '" + blahpre + "'? " + client.pretrainedModels().delete(blahpre, true));

    // download model
    String mobilenetv3 = "mobilenet_v3_small-8427ecf0";
    File modelFile = new File(System.getProperty("java.io.tmpdir") + "/" + mobilenetv3 + ".pth");
    client.pretrainedModels().download(mobilenetv3, modelFile);
    if (modelFile.exists()) {
      System.out.println(modelFile + ": " + modelFile.length() + " bytes");
      System.out.println("Deleting: " + modelFile);
      modelFile.delete();
    }

    client.close();
  }
}
