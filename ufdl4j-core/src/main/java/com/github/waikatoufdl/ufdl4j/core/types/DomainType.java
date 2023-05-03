/*
 * DomainType.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package com.github.waikatoufdl.ufdl4j.core.types;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Domains;

/**
 * For parsing domain types.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class DomainType
  extends AbstractType<Domains.Domain>{

  private static final long serialVersionUID = 6890224440246884304L;

  public static final String TYPE = "DomainType";

  /**
   * Returns the type name.
   *
   * @return the type name
   */
  @Override
  public String getTypeName() {
    return TYPE;
  }

  /**
   * Parses the type definition.
   *
   * @param client 	the client to use
   * @param definition 	the type definition
   * @return 		the parsed type
   * @throws Exception	if parsing fails
   */
  @Override
  public Domains.Domain parse(Client client, String definition) throws Exception {
    definition = definition.substring(definition.indexOf("<") + 1, definition.lastIndexOf(">"));
    definition = unquote(definition);
    return client.domains().load(definition);
  }
}
