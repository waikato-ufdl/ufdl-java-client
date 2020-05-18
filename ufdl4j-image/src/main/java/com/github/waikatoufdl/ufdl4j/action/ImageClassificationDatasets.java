/*
 * ImageClassificationDatasets.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

/**
 * Encapsulates dataset operations for image classification.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ImageClassificationDatasets
  extends Datasets {

  private static final long serialVersionUID = -6031589074773486947L;

  /**
   * The URL path to use.
   *
   * @return		the path
   */
  @Override
  public String getPath() {
    return "/v1/classify/datasets/";
  }
}
