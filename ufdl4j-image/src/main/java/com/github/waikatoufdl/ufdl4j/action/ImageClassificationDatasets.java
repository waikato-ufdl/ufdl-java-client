/*
 * ImageClassificationDatasets.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates dataset operations for image classification.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ImageClassificationDatasets
  extends Datasets {

  private static final long serialVersionUID = -6031589074773486947L;

  /**
   * Container class for image classification dataset information.
   */
  public static class ImageClassificationDataset
    extends Dataset {

    /**
     * Initializes the dataset.
     *
     * @param data the data to use
     */
    public ImageClassificationDataset(JsonObject data) {
      super(data);
    }

    /**
     * Returns the files of this dataset.
     *
     * @return		the files
     */
    public List<String> files() {
      List<String> result;
      JsonArray array;
      int i;

      result = new ArrayList<>();
      if (getData().has("files")) {
	array = getData().getAsJsonArray("files");
	for (i = 0; i < array.size(); i++)
	  result.add(array.get(i).getAsString());
      }
      return result;
    }
  }

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
