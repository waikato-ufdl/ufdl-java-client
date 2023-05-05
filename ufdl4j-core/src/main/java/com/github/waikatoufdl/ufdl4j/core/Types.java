/*
 * Types.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package com.github.waikatoufdl.ufdl4j.core;

import com.github.waikatoufdl.ufdl4j.action.Domains;
import com.github.waikatoufdl.ufdl4j.action.Frameworks;

/**
 * Helper class for constructing type signatures.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class Types {

  public static final String OPEN = "<";

  public static final String CLOSE = ">";

  public static final String PK = "PK" + OPEN;

  public static final String NAME = "Name" + OPEN;

  public static final String DOMAIN = "Domain" + OPEN;

  public static final String FRAMEWORK = "Framework" + OPEN;

  public static final String DATASET = "Dataset" + OPEN;

  public static final String DOCKER_IMAGE = "DockerImage" + OPEN;

  public static final String PRETRAINED_MODEL = "PretrainedModel" + OPEN;

  public static final String MODEL = "Model" + OPEN;

  public static final String JOB_OUTPUT = "JobOutput" + OPEN;

  public static final String ARRAY = "Array" + OPEN;

  public static final String CONTRACT_TRAIN = "Train";

  public static final String CONTRACT_PREDICT = "Predict";

  public static final String PRIMITIVE_BOOL = "bool";

  public static final String PRIMITIVE_INT = "int";

  public static final String PRIMITIVE_FLOAT = "float";

  public static final String PRIMITIVE_STR = "str";

  public static final String ARRAY_STR = ARRAY + PRIMITIVE_STR + CLOSE;

  /**
   * Wraps the signature in a PK signature.
   *
   * @param signature	the signature to wrap
   * @return		the signature
   */
  public static String pk(String signature) {
    return PK + signature + CLOSE;
  }

  /**
   * Wraps the signature in a Name signature.
   *
   * @param signature	the signature to wrap
   * @return		the signature
   */
  public static String name(String signature) {
    return NAME + signature + CLOSE;
  }

  /**
   * Returns a domain signature.
   *
   * @param domain	the domain to use
   * @return		the signature
   */
  public static String domain(Domains.Domain domain) {
    return domain(domain.getDescription());
  }

  /**
   * Returns a domain signature.
   *
   * @param description	the domain description to use
   * @return		the signature
   */
  public static String domain(String description) {
    return DOMAIN + "'" + description + "'" + CLOSE;
  }

  /**
   * Returns a framework signature.
   *
   * @param framework	the framework
   * @return		the signature
   */
  public static String framework(Frameworks.Framework framework) {
    return framework(framework.getName(), framework.getVersion());
  }

  /**
   * Returns a framework signature.
   *
   * @param name	the name of the framework
   * @param version	the version of the framework
   * @return		the signature
   */
  public static String framework(String name, String version) {
    return FRAMEWORK + "'" + name + "', '" + version + "'" + CLOSE;
  }

  /**
   * Returns a dataset signature.
   *
   * @param signature	the signature to wrap
   * @return		the signature
   */
  public static String dataset(String signature) {
    return DATASET + signature + CLOSE;
  }

  /**
   * Returns a DockerImage signature.
   *
   * @param domain	the domain
   * @param framework 	the framework
   * @return		the signature
   */
  public static String dockerImage(Domains.Domain domain, Frameworks.Framework framework) {
    return dockerImage(domain.getDescription(), framework.getName(), framework.getVersion());
  }

  /**
   * Returns a DockerImage signature.
   *
   * @param domDesc	the domain description
   * @param fwName 	the name of the framework
   * @param fwVersion 	the version of the framework
   * @return		the signature
   */
  public static String dockerImage(String domDesc, String fwName, String fwVersion) {
    return DOCKER_IMAGE + domain(domDesc) + ", " + framework(fwName, fwVersion) + CLOSE;
  }

  /**
   * Returns a DockerImage signature.
   *
   * @param domain	the domain
   * @param framework 	the framework
   * @param version	the version of the docker image, generates only a fragment if null (up to the version)
   * @return		the signature
   */
  public static String dockerImage(Domains.Domain domain, Frameworks.Framework framework, String version) {
    return dockerImage(domain.getDescription(), framework.getName(), framework.getVersion(), version);
  }

  /**
   * Returns a DockerImage signature.
   *
   * @param domDesc	the domain description
   * @param fwName 	the name of the framework
   * @param fwVersion 	the version of the framework
   * @param version	the version of the docker image, generates only a fragment if null (up to the version)
   * @return		the signature
   */
  public static String dockerImage(String domDesc, String fwName, String fwVersion, String version) {
    return DOCKER_IMAGE + domain(domDesc) + ", " + framework(fwName, fwVersion) + (version == null ? "" : ", '" + version + "'" + CLOSE);
  }

  /**
   * Returns a PretrainedModel signature.
   *
   * @param domain	the domain
   * @param framework 	the framework
   * @return		the signature
   */
  public static String pretrainedModel(Domains.Domain domain, Frameworks.Framework framework) {
    return pretrainedModel(domain.getDescription(), framework.getName(), framework.getVersion());
  }

  /**
   * Returns a PretrainedModel signature.
   *
   * @param domDesc	the domain description
   * @param fwName 	the name of the framework
   * @param fwVersion 	the version of the framework
   * @return		the signature
   */
  public static String pretrainedModel(String domDesc, String fwName, String fwVersion) {
    return PRETRAINED_MODEL + domain(domDesc) + ", " + framework(fwName, fwVersion) + CLOSE;
  }

  /**
   * Returns a Model signature.
   *
   * @param domain	the domain
   * @param framework 	the framework
   * @return		the signature
   */
  public static String model(Domains.Domain domain, Frameworks.Framework framework) {
    return model(domain.getDescription(), framework.getName(), framework.getVersion());
  }

  /**
   * Returns a Model signature.
   *
   * @param domDesc	the domain description
   * @param fwName 	the name of the framework
   * @param fwVersion 	the version of the framework
   * @return		the signature
   */
  public static String model(String domDesc, String fwName, String fwVersion) {
    return MODEL + domain(domDesc) + ", " + framework(fwName, fwVersion) + CLOSE;
  }

  /**
   * Returns a JobOutput signature.
   *
   * @param signature	the signature to wrap
   * @return		the signature
   */
  public static String jobOutput(String signature) {
    return JOB_OUTPUT + signature + CLOSE;
  }

  /**
   * Returns the signature for a Train contract.
   *
   * @param domain	the domain
   * @param framework 	the framework
   * @return		the signature
   */
  public static String train(Domains.Domain domain, Frameworks.Framework framework) {
    return contract(CONTRACT_TRAIN, domain.getDescription(), framework.getName(), framework.getVersion());
  }

  /**
   * Returns the signature for a Train contract.
   *
   * @param domDesc	the domain description
   * @param fwName 	the name of the framework
   * @param fwVersion 	the version of the framework
   * @return		the signature
   */
  public static String train(String domDesc, String fwName, String fwVersion) {
    return contract(CONTRACT_TRAIN, domDesc, fwName, fwVersion);
  }

  /**
   * Returns the signature for a Predict contract.
   *
   * @param domain	the domain
   * @param framework 	the framework
   * @return		the signature
   */
  public static String predict(Domains.Domain domain, Frameworks.Framework framework) {
    return contract(CONTRACT_PREDICT, domain.getDescription(), framework.getName(), framework.getVersion());
  }

  /**
   * Returns the signature for a Predict contract.
   *
   * @param domDesc	the domain description
   * @param fwName 	the name of the framework
   * @param fwVersion 	the version of the framework
   * @return		the signature
   */
  public static String predict(String domDesc, String fwName, String fwVersion) {
    return contract(CONTRACT_PREDICT, domDesc, fwName, fwVersion);
  }

  /**
   * Returns the signature for a contract.
   *
   * @param contract 	the contract, e.g., Train or Predict
   * @param domain	the domain
   * @param framework 	the framework
   * @return		the signature
   */
  public static String contract(String contract, Domains.Domain domain, Frameworks.Framework framework) {
    return contract(contract, domain.getDescription(), framework.getName(), framework.getVersion());
  }

  /**
   * Returns the signature for a contract.
   *
   * @param contract 	the contract, e.g., Train or Predict
   * @param domDesc	the domain description
   * @param fwName 	the name of the framework
   * @param fwVersion 	the version of the framework
   * @return		the signature
   */
  public static String contract(String contract, String domDesc, String fwName, String fwVersion) {
    return contract + OPEN + domain(domDesc) + ", " + framework(fwName, fwVersion) + CLOSE;
  }
}
