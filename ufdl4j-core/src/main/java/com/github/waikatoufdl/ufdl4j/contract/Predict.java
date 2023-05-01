/*
 * Predict.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package com.github.waikatoufdl.ufdl4j.contract;

import com.github.waikatoufdl.ufdl4j.action.Domains;
import com.github.waikatoufdl.ufdl4j.action.Frameworks;
import com.github.waikatoufdl.ufdl4j.core.Types;

import java.util.HashMap;
import java.util.Map;

import static com.github.waikatoufdl.ufdl4j.core.Types.dataset;
import static com.github.waikatoufdl.ufdl4j.core.Types.jobOutput;
import static com.github.waikatoufdl.ufdl4j.core.Types.model;
import static com.github.waikatoufdl.ufdl4j.core.Types.pk;

/**
 * Predict contract.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class Predict
  extends AbstractContract {

  private static final long serialVersionUID = 1627306977944521316L;

  /**
   * Returns the name of the contract.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "Predict";
  }

  /**
   * Returns the input types for the contract.
   *
   * @param domain	the domain
   * @param framework	the framework
   * @return		the input types
   */
  public Map<String,String> inputs(Domains.Domain domain, Frameworks.Framework framework) {
    Map<String,String>	result;

    result = new HashMap<>();
    result.put("model", jobOutput(model(domain, framework)));
    result.put("dataset", pk(dataset(Types.domain(domain))));

    return result;
  }

  /**
   * Returns the output types for the contract.
   *
   * @param domain	the domain
   * @param framework	the framework
   * @return		the output types
   */
  public Map<String,String> outputs(Domains.Domain domain, Frameworks.Framework framework) {
    Map<String,String>	result;

    result = new HashMap<>();
    result.put("predictions", pk(dataset(Types.domain(domain))));

    return result;
  }
}
