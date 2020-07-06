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
 * CustomDisplayEnum.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.core;

/**
 * Interface for enums that have a custom display string.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface CustomDisplayEnum {

  /**
   * Returns the display string.
   *
   * @return		the display string
   */
  public String toDisplay();

  /**
   * Returns the raw enum string.
   *
   * @return		the raw enum string
   */
  public String toRaw();

  /**
   * Returns the display string.
   *
   * @return		the display string
   */
  public String toString();
}
