/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * NameAndVersionFilter.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.filter;

import com.github.waikatoufdl.ufdl4j.core.AbstractLoggingObject;
import com.github.waikatoufdl.ufdl4j.core.JsonUtils;
import com.github.waikatoufdl.ufdl4j.filter.field.ExactString;
import com.github.waikatoufdl.ufdl4j.filter.logical.And;
import com.google.gson.JsonObject;

/**
 * Simple exact name/version filter.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class NameAndVersionFilter
  extends AbstractLoggingObject
  implements Filter {

  private static final long serialVersionUID = -7995513145236262468L;

  /** the name. */
  protected String m_Name;

  /** the version. */
  protected String m_Version;

  /**
   * Initializes the filter.
   *
   * @param name	the name to filter
   */
  public NameAndVersionFilter(String name, String version) {
    super();
    if (name == null)
      throw new IllegalArgumentException("Name cannot be null!");
    if (version == null)
      throw new IllegalArgumentException("Version cannot be null!");
    m_Name = name;
    m_Version = version;
  }

  /**
   * Returns the name.
   *
   * @return		the name
   */
  public String getName() {
    return m_Name;
  }

  /**
   * Returns the version.
   *
   * @return		the version
   */
  public String getVersion() {
    return m_Version;
  }

  /**
   * Returns the JSON structure.
   *
   * @return		the json
   */
  @Override
  public JsonObject toJsonObject() {
    return new GenericFilter(
      new AbstractExpression[]{
        new And(
          new AbstractExpression[]{
            new ExactString("name", m_Name),
            new ExactString("version", m_Version),
          }
        )
      }
    ).toJsonObject();
  }

  /**
   * Returns the stored data as pretty printed string.
   *
   * @return		the pretty json
   */
  @Override
  public String toPrettyPrint() {
    return JsonUtils.prettyPrint(toJsonObject());
  }

  /**
   * Returns a short description of the state.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return "name=" + getName();
  }
}
