/*
 * ManagingManagingJobs.java
 * Copyright (C) 2020-2023 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.DockerImages.DockerImage;
import com.github.waikatoufdl.ufdl4j.action.Domains;
import com.github.waikatoufdl.ufdl4j.action.Frameworks.Framework;
import com.github.waikatoufdl.ufdl4j.action.JobTemplates;
import com.github.waikatoufdl.ufdl4j.action.JobTemplates.JobTemplate;
import com.github.waikatoufdl.ufdl4j.action.Jobs.Job;
import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets;
import com.github.waikatoufdl.ufdl4j.action.PretrainedModels;
import com.github.waikatoufdl.ufdl4j.filter.GenericFilter;
import com.github.waikatoufdl.ufdl4j.filter.NameAndVersionFilter;
import com.github.waikatoufdl.ufdl4j.filter.field.ExactBoolean;
import com.github.waikatoufdl.ufdl4j.filter.field.PK;

import java.util.HashMap;
import java.util.Map;

import static com.github.waikatoufdl.ufdl4j.core.TypeValuePair.typeValuePair;
import static com.github.waikatoufdl.ufdl4j.core.Types.dataset;
import static com.github.waikatoufdl.ufdl4j.core.Types.dockerImage;
import static com.github.waikatoufdl.ufdl4j.core.Types.domain;
import static com.github.waikatoufdl.ufdl4j.core.Types.pk;
import static com.github.waikatoufdl.ufdl4j.core.Types.pretrainedModel;

/**
 * Example code for managing jobs.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ManagingJobs {

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

    Domains.Domain domain = client.domains().load("od");

    // find datasets
    Dataset tfdataset = null;
    String tfdatasetName = "tmpdata";
    System.out.println("--> find dataset");
    for (Dataset dataset: client.datasets(ObjectDetectionDatasets.class).list()) {
      if (dataset.getName().equals(tfdatasetName)) {
        tfdataset = dataset;
	System.out.println("Found: " + dataset);
	break;
      }
    }

    // create team?
    if (client.teams().load("blahteam") == null) {
      System.out.println("--> creating team");
      System.out.println(client.teams().create("blahteam"));
    }

    // create project?
    if (client.projects().list().size() == 0) {
      System.out.println("--> creating project");
      System.out.println(client.projects().create("blahproject", client.teams().list().get(0).getPK()));
    }

    // create dataset
    if (tfdataset == null) {
      System.out.println("--> creating dataset");
      tfdataset = client.datasets(ObjectDetectionDatasets.class).create(
	tfdatasetName,
	"dummy description",
	client.projects().list().get(0).getPK(),
	client.licenses().list().get(0),
	true,
	"");
      System.out.println(tfdataset);
    }

    // find framework
    String tfframeworkName = "tensorflow";
    String tfframeworkVersion = "1.14";
    System.out.println("--> looking for framework: " + tfframeworkName + "/" + tfframeworkVersion);
    Framework tfframework = null;
    for (Framework framework : client.frameworks().list(new NameAndVersionFilter(tfframeworkName, tfframeworkVersion))) {
      tfframework = framework;
      System.out.println("Found: " + tfframework);
      break;
    }
    if (tfframework == null) {
      System.err.println("No " + tfframeworkName + "/" + tfframeworkVersion+ " framework defined?");
      client.close();
      System.exit(1);
    }

    // find docker image
    DockerImage tfimg = null;
    System.out.println("--> looking for docker image");
    GenericFilter filter = new GenericFilter();
    filter.addExpression(new PK("domain", domain));
    filter.addExpression(new PK("framework", tfframework));
    filter.addExpression(new ExactBoolean("cpu", false));
    for (DockerImage image : client.docker().list(filter)) {
      tfimg = image;
      System.out.println("Found: " + tfimg);
      break;
    }
    if (tfimg == null) {
      System.err.println("No docker image defined that fits: " + filter.toString());
      client.close();
      System.exit(1);
    }

    // find resnet101 pretrained model
    System.out.println("--> looking for template for domain/framework: " + domain + "/" + tfframework);
    filter = new GenericFilter();
    filter.addExpression(new PK("domain", domain));
    filter.addExpression(new PK("framework", tfframework));
    PretrainedModels.PretrainedModel tfpretrained = null;
    for (PretrainedModels.PretrainedModel pretrained : client.pretrainedModels().list(filter)) {
      System.out.println(pretrained);
      if (pretrained.getName().contains("resnet101")) {
        tfpretrained = pretrained;
        break;
      }
    }
    if (tfpretrained == null) {
      System.err.println("No pretrained model defined that fits: " + filter.toString());
      client.close();
      System.exit(1);
    }

    // find resnet101 template that can Train for dataset/framework
    System.out.println("--> looking for template for framework: " + tfframework);
    JobTemplate tftemplate = null;
    Map<String,String> params = new HashMap<>();
    params.put("dataset", pk(dataset(domain(domain))));
    String frameworkType = dockerImage(domain, tfframework);
    boolean found = false;
    for (JobTemplate template: client.jobTemplates().getAllMatchingTemplates("Train", params)) {
      if (template.getName().contains("resnet101")) {
        for (JobTemplates.Parameter p : template.getParameters()) {
          for (String type : p.getTypes()) {
            if (type.equals(frameworkType)) {
              found = true;
              tftemplate = template;
              System.out.println("Found: " + tftemplate);
              break;
            }
          }
          if (found)
            break;
        }
      }
    }
    if (tftemplate == null) {
      System.err.println("No job template found!");
      client.close();
      return;
    }

    // create job
    System.out.println("--> creating job");
    Map<String,Map<String,String>> inputs = new HashMap<>();
    inputs.put("dataset", typeValuePair(pk(dataset(domain(domain))), "" + tfdataset.getPK()));
    Map<String,Map<String,String>> parameters = new HashMap<>();
    parameters.put("docker_image", typeValuePair(pk(dockerImage(domain, tfframework)), "" + tfimg.getPK()));
    parameters.put("pretrained_model", typeValuePair(pk(pretrainedModel(domain, tfframework)), "" + tfpretrained.getPK()));
    Job tfjob = client.jobTemplates().newJob(tftemplate, inputs, parameters, "example job using tftest");
    System.out.println(tfjob);

    // listing jobs
    System.out.println("--> listing jobs");
    for (Job job : client.jobs().list())
      System.out.println(job);

    // delete job
    // TODO hard delete does not work
    System.out.println("--> deleting job");
    System.out.println(client.jobs().delete(tfjob, false));

    // delete template
    System.out.println("--> deleting job template");
    System.out.println(client.jobTemplates().delete(tftemplate, false));

    // delete dataset
    System.out.println("--> deleting dataset");
    System.out.println(client.datasets(ObjectDetectionDatasets.class).delete(tfdataset, false));

    // delete project?
    if (client.projects().load("blahproject") != null)
      client.projects().delete(client.projects().load("blahproject"), false);

    // delete team?
    if (client.teams().load("blahteam") != null)
       client.teams().delete(client.teams().load("blahteam"), false);

    client.close();
  }
}
