/*
 * FrameworkType.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package com.github.waikatoufdl.ufdl4j.core.types;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Frameworks;

/**
 * For parsing framework types.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class FrameworkType
  extends AbstractType<Frameworks.Framework> {

  private static final long serialVersionUID = 6890224440246884304L;

  public static final String TYPE = "FrameworkType";

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
   * @param client     the client to use
   * @param definition the type definition
   * @return the parsed type
   * @throws Exception if parsing fails
   */
  @Override
  public Frameworks.Framework parse(Client client, String definition) throws Exception {
    String[] parts;
    int i;

    definition = definition.substring(definition.indexOf("<") + 1, definition.lastIndexOf(">"));
    parts = split(definition);
    for (i = 0; i < parts.length; i++)
      parts[i] = unquote(parts[i]);

    return client.frameworks().load(parts[0], parts[1]);
  }
}