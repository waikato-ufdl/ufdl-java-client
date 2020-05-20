/*
 * SoftDeleteObject.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.core;

import java.time.LocalDateTime;

/**
 * Soft delete objects don't get removed. Instead, their deletion times is not null.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface SoftDeleteObject {

  /**
   * Returns the deletion date/time.
   *
   * @return		the date/time, can be null
   */
  public LocalDateTime getDeletionTime();
}
