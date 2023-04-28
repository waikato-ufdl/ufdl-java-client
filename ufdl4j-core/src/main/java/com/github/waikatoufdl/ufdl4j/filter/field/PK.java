/*
 * PK.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package com.github.waikatoufdl.ufdl4j.filter.field;

import com.github.waikatoufdl.ufdl4j.core.JsonObjectWithPK;

/**
 * Looks for objects with specified PK.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class PK
  extends ExactNumber {

  private static final long serialVersionUID = 2346576319451446917L;

  /**
   * Initializes the expression with the field.
   *
   * @param field  	the field to apply to
   * @param obj 	the object to look for
   */
  public PK(String field, JsonObjectWithPK obj) {
    super(field, obj.getPK());
  }

  /**
   * Initializes the expression with the field.
   *
   * @param field  	the field to apply to
   * @param pk 		the PK to look for
   */
  public PK(String field, int pk) {
    super(field, pk);
  }
}
