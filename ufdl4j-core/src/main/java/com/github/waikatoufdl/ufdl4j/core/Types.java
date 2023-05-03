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

  /**
   * Wraps the signature in a PK signature.
   *
   * @param signature	the signature to wrap
   * @return		the signature
   */
  public static String pk(String signature) {
    return "PK<" + signature + ">";
  }

  /**
   * Wraps the signature in a Name signature.
   *
   * @param signature	the signature to wrap
   * @return		the signature
   */
  public static String name(String signature) {
    return "Name<" + signature + ">";
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
    return "Domain<'" + description + "'>";
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
    return "Framework<'" + name + "', '" + version + "'>";
  }

  /**
   * Returns a dataset signature.
   *
   * @param signature	the signature to wrap
   * @return		the signature
   */
  public static String dataset(String signature) {
    return "Dataset<" + signature + ">";
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
    return "DockerImage<" + domain(domDesc) + ", " + framework(fwName, fwVersion) + ">";
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
    return "DockerImage<" + domain(domDesc) + ", " + framework(fwName, fwVersion) + (version == null ? "" : ", '" + version + "'>");
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
    return "PretrainedModel<" + domain(domDesc) + ", " + framework(fwName, fwVersion) + ">";
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
    return "Model<" + domain(domDesc) + ", " + framework(fwName, fwVersion) + ">";
  }

  /**
   * Returns a JobOutput signature.
   *
   * @param signature	the signature to wrap
   * @return		the signature
   */
  public static String jobOutput(String signature) {
    return "JobOutput<" + signature + ">";
  }

  /**
   * Returns the signature for a Train contract.
   *
   * @param domain	the domain
   * @param framework 	the framework
   * @return		the signature
   */
  public static String train(Domains.Domain domain, Frameworks.Framework framework) {
    return contract("Train", domain.getDescription(), framework.getName(), framework.getVersion());
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
    return contract("Train", domDesc, fwName, fwVersion);
  }

  /**
   * Returns the signature for a Predict contract.
   *
   * @param domain	the domain
   * @param framework 	the framework
   * @return		the signature
   */
  public static String predict(Domains.Domain domain, Frameworks.Framework framework) {
    return contract("Predict", domain.getDescription(), framework.getName(), framework.getVersion());
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
    return contract("Predict", domDesc, fwName, fwVersion);
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
    return contract + "<" + domain(domDesc) + ", " + framework(fwName, fwVersion) + ">";
  }
}
