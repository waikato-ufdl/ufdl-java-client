/*
 * ManagingManagingJobs.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.DockerImages.DockerImage;
import com.github.waikatoufdl.ufdl4j.action.Frameworks.Framework;
import com.github.waikatoufdl.ufdl4j.action.JobTemplates.JobTemplate;
import com.github.waikatoufdl.ufdl4j.action.Jobs.Job;

import java.util.HashMap;
import java.util.Map;

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

    // find datasets
    Dataset tfdataset = null;
    String tfdatasetName = "tmpdata";
    System.out.println("--> find dataset");
    for (Dataset dataset: client.datasets().list()) {
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
      tfdataset = client.datasets().create(
	tfdatasetName,
	"dummy description",
	client.projects().list().get(0).getPK(),
	client.licenses().list().get(0).getPK(),
	true,
	"");
      System.out.println(tfdataset);
    }

    // find framework
    String tfframeworkName = "tensorflow";
    String tfframeworkVersion = "1.14";
    System.out.println("--> looking for framework: " + tfframeworkName + "/" + tfframeworkVersion);
    Framework tfframework = null;
    for (Framework framework : client.frameworks().list()) {
      if (framework.getName().equals(tfframeworkName) && framework.getVersion().equals(tfframeworkVersion)) {
	tfframework = framework;
	System.out.println("Found: " + tfframework);
	break;
      }
    }
    if (tfframework == null) {
      System.err.println("No " + tfframeworkName + "/" + tfframeworkVersion+ " framework defined?");
      client.close();
      System.exit(1);
    }

    // find docker image
    DockerImage tfimg = null;
    String tfimageName = "tf_imageclass_cpu";
    String tfimageVersion = "1.14";
    System.out.println("--> looking for docker image: " + tfimageName + "/" + tfimageVersion);
    for (DockerImage image : client.docker().list()) {
      if (image.getName().equals(tfimageName) && image.getVersion().equals(tfimageVersion)) {
	tfimg = image;
	System.out.println("Found: " + tfimg);
	break;
      }
    }
    if (tfimg == null) {
      System.err.println("No " + tfimageName + "/" + tfimageVersion + " docker image defined?");
      client.close();
      System.exit(1);
    }

    // find template
    System.out.println("--> looking for template for framework: " + tfframework);
    JobTemplate tftemplate = null;
    for (JobTemplate template : client.jobTemplates().list()) {
      if (template.getFramework() == tfframework.getPK()) {
	tftemplate = template;
	System.out.println("Found: " + tftemplate);
	break;
      }
    }

    // create 'tftest' template if necessary
    if (tftemplate == null) {
      System.out.println("--> creating job template");
      tftemplate = client.jobTemplates().create("tftest", 1, "public", tfframework.getPK(), "ic", "train", "my.funky.Executor", "", "echo ${num}");
      client.jobTemplates().addInput(tftemplate, "dataset", "dataset", "");
      client.jobTemplates().addParameter(tftemplate, "num", "int", "10");
      System.out.println(tftemplate);
    }

    // create job
    System.out.println("--> creating job");
    Map<String,String> inputs = new HashMap<>();
    inputs.put("dataset", "" + tfdataset.getPK());
    Map<String,String> params = new HashMap<>();
    params.put("num", "11");
    Job tfjob = client.jobTemplates().newJob(tftemplate, tfimg.getPK(), inputs, params);
    System.out.println(tfjob);

    // listing jobs
    System.out.println("--> listing jobs");
    for (Job job : client.jobs().list())
      System.out.println(job);

    // delete job
    System.out.println("--> deleting job");
    System.out.println(client.jobs().delete(tfjob, true));

    // delete template
    System.out.println("--> deleting job template");
    System.out.println(client.jobTemplates().delete(tftemplate, false));

    // delete dataset
    System.out.println("--> deleting dataset");
    System.out.println(client.datasets().delete(tfdataset, true));

    // delete project?
    if (client.projects().load("blahproject") != null)
      client.projects().delete(client.projects().load("blahproject"), true);

    // delete team?
    if (client.teams().load("blahteam") != null)
       client.teams().delete(client.teams().load("blahteam"), false);

    client.close();
  }
}
