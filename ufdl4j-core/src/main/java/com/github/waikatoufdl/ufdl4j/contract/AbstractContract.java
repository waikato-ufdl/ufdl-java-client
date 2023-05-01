/*
 * AbstractContract.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package com.github.waikatoufdl.ufdl4j.contract;

import com.github.waikatoufdl.ufdl4j.action.Domains;
import com.github.waikatoufdl.ufdl4j.action.Frameworks;
import com.github.waikatoufdl.ufdl4j.core.AbstractLoggingObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Ancestor for contracts.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractContract
  extends AbstractLoggingObject {

  private static final long serialVersionUID = -3851086128287497482L;

  /** the available contracts. */
  protected static List<AbstractContract> m_Contracts;

  /**
   * Returns the name of the contract.
   *
   * @return		the name
   */
  public abstract String getName();

  /**
   * Returns the input types for the contract.
   *
   * @param domain	the domain
   * @param framework	the framework
   * @return		the input types
   */
  public abstract Map<String,String> inputs(Domains.Domain domain, Frameworks.Framework framework);

  /**
   * Returns the output types for the contract.
   *
   * @param domain	the domain
   * @param framework	the framework
   * @return		the output types
   */
  public abstract Map<String,String> outputs(Domains.Domain domain, Frameworks.Framework framework);

  /**
   * Just returns the name of the contract.
   *
   * @return		the name
   */
  @Override
  public String toString() {
    return getName();
  }

  /**
   * Returns the available contract classes (manual list).
   *
   * @return		the classes
   */
  public static List<Class<? extends AbstractContract>> getContractTypes() {
    return Arrays.asList(
      Train.class,
      Predict.class
    );
  }

  /**
   * Returns the list of available contract instances.
   *
   * @return		the instances
   */
  public static synchronized List<AbstractContract> getContracts() {
    if (m_Contracts == null) {
      m_Contracts = new ArrayList<>();
      for (Class<? extends AbstractContract> cls: getContractTypes()) {
        try {
	  m_Contracts.add(cls.getDeclaredConstructor().newInstance());
	}
        catch (Exception e) {
          System.err.println("Failed to instantiate contract: " + cls.getName());
          e.printStackTrace();
	}
      }
    }

    return m_Contracts;
  }

  /**
   * Returns the names of the available contracts.
   *
   * @return		the list
   */
  public static List<String> getContractNames() {
    List<String>	result;

    result = new ArrayList<>();
    for (AbstractContract contract: getContracts())
      result.add(contract.getName());

    return result;
  }

  /**
   * Returns the contract associated with the name.
   *
   * @param name	the name to get the contract for
   * @return		the contrat, null if not found
   */
  public static AbstractContract forName(String name) {
    AbstractContract	result;

    result = null;

    for (AbstractContract contract: getContracts()) {
      if (contract.getName().equals(name)) {
        result = contract;
        break;
      }
    }

    return result;
  }
}
