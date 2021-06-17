/*
 * ImageSegmentationDatasets.java
 * Copyright (C) 2021 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.attachment.FileAttachment;
import com.github.fracpete.requests4j.core.MediaTypeHelper;
import com.github.fracpete.requests4j.request.Request;
import com.github.fracpete.requests4j.response.Response;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;
import com.github.waikatoufdl.ufdl4j.core.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates dataset operations for image segmentation.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ImageSegmentationDatasets
  extends Datasets {

  private static final long serialVersionUID = -6031589074773486947L;

  /**
   * Container class for image classification dataset information.
   */
  public static class ImageSegmentationDataset
    extends Dataset {

    private static final long serialVersionUID = 2031525681885013068L;

    /**
     * Initializes the dataset.
     *
     * @param data the data to use
     */
    public ImageSegmentationDataset(JsonObject data) {
      super(data);
    }
  }

  /**
   * The URL path to use.
   *
   * @return		the path
   */
  @Override
  public String getPath() {
    return "/v1/segments/datasets/";
  }

  /**
   * For loading the layers/labels for a specific dataset.
   *
   * @param dataset	the dataset to get the categories for
   * @return		the labels
   * @throws Exception	if request fails
   */
  public List<String> getLabels(Dataset dataset) throws Exception {
    return getLabels(dataset.getPK());
  }

  /**
   * For loading the layers/labels for a specific dataset by primary key.
   *
   * @param pk 		the primary key of the dataset to get the categories for
   * @return		the labels
   * @throws Exception	if request fails
   */
  public List<String> getLabels(int pk) throws Exception {
    List<String>	result;
    Request 		request;
    JsonResponse 	response;
    JsonArray 		array;
    int			i;

    getLogger().info("loading labels for: " + pk);

    result   = new ArrayList<>();
    request  = newGet(getPath() + pk + "/labels");
    response = execute(request);
    if (response.ok()) {
      array = response.json().getAsJsonArray();
      for (i = 0; i < array.size(); i++)
        result.add(array.get(i).getAsString());
    }
    else {
      throw new FailedRequestException("Failed to get labels for: " + pk, response);
    }

    return result;
  }

  /**
   * For setting the labels to a specific dataset.
   *
   * @param dataset	the dataset to add the labels for
   * @param labels 	the labels to set
   * @return		true if successful
   * @throws Exception	if request fails
   */
  public boolean setLabels(Dataset dataset, List<String> labels) throws Exception {
    return setLabels(dataset.getPK(), labels);
  }

  /**
   * For settings the labels to a specific dataset by primary key.
   *
   * @param pk 		the primary key of the dataset to set the labels for
   * @param labels 	the labels to add to each image
   * @return		true if successful
   * @throws Exception	if request fails
   */
  public boolean setLabels(int pk, List<String> labels) throws Exception {
    boolean		result;
    JsonObject		data;
    JsonResponse	response;
    Request 		request;

    getLogger().info("setting labels for: " + pk);

    result   = false;
    data     = new JsonObject();
    data.add("labels", JsonUtils.toArray(labels));
    request  = newPost(getPath() + pk + "/labels")
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok()) {
      result = true;
    }
    else {
      throw new FailedRequestException("Failed to set labels for: " + pk, response);
    }

    return result;
  }

  /**
   * Adds the file as layer to the dataset (uploads it to the server).
   *
   * @param dataset	the dataset
   * @param name	the name to use in the dataset
   * @param label 	the label of the layer
   * @param file	the file to upload
   * @return		true if successfully added/uploaded
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean setLayer(Dataset dataset, String name, String label, File file) throws Exception {
    return setLayer(dataset.getPK(), name, label, file);
  }

  /**
   * Adds the file as layer to the dataset (uploads it to the server).
   *
   * @param pk		the dataset ID
   * @param name	the name to use in the dataset
   * @param label 	the label of the layer
   * @param file	the file to upload
   * @return		true if successfully added/uploaded
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean setLayer(int pk, String name, String label, File file) throws Exception {
    Request 	request;
    Response response;

    getLogger().info("adding file '" + name + "' as label '" + label + "' to: " + pk);

    request = newPost(getPath() + pk + "/layers/" + name + "/" + label)
      .attachment(new FileAttachment(file));
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to add file '" + name + "' as label '" + label + "' to dataset " + pk + ": " + file, response);
  }

  /**
   * Retrieves the layer for the specified file from the dataset (downloads it from the server).
   *
   * @param dataset	the dataset
   * @param output	the output file
   * @param name	the name used in the dataset
   * @param label 	the layer to retrieve
   * @return		true if layer was available and successfully downloaded
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean getLayer(Dataset dataset, String name, String label, File output) throws Exception {
    return getLayer(dataset.getPK(), name, label, output);
  }

  /**
   * Retrieves the layer for the specified file from the dataset (downloads it from the server).
   *
   * @param pk		the dataset ID
   * @param output	the output file
   * @param name	the name used in the dataset
   * @param label 	the layer to retrieve
   * @return		true if layer was available and successfully downloaded
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean getLayer(int pk, String name, String label, File output) throws Exception {
    Request 	request;
    Response 	response;

    getLogger().info("getting layer '" + label + "' of file '" + name + "' from: " + pk);

    request = newGet(getPath() + pk + "/layers/" + name + "/" + label);
    response = download(request, output, false);
    if (response.ok())
      return output.exists();
    else
      throw new FailedRequestException("Failed to get label '" + label + "' of file from dataset " + pk + ": " + name, response);
  }
}
