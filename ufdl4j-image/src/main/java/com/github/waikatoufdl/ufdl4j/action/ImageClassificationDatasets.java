/*
 * ImageClassificationDatasets.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;
import com.github.waikatoufdl.ufdl4j.core.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.entity.ContentType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  /**
   * For loading the categories for a specific dataset.
   *
   * @param dataset	the dataset to get the categories for
   * @return		the categories (image -> categories)
   * @throws Exception	if request fails
   */
  public Map<String,List<String>> getCategories(Dataset dataset) throws Exception {
    return getCategories(dataset.getPK());
  }

  /**
   * For loading the categories for a specific dataset by primary key.
   *
   * @param pk 		the primary key of the dataset to get the categories for
   * @return		the categories (image -> categories)
   * @throws Exception	if request fails
   */
  public Map<String,List<String>> getCategories(int pk) throws Exception {
    Map<String,List<String>>	result;
    Request 			request;
    JsonResponse 		response;
    JsonObject			obj;
    List			list;
    List<String>		categories;

    getLogger().info("loading categories for id: " + pk);

    result   = null;
    request  = newGet(getPath() + pk + "/categories");
    response = execute(request);
    if (response.ok()) {
      obj = response.json().getAsJsonObject();
      result = new HashMap<>();
      for (String key: obj.keySet()) {
        categories = new ArrayList<>();
        result.put(key, categories);
        list = JsonUtils.getList(obj, key, new ArrayList());
        for (Object item: list)
          categories.add("" + item);
      }
    }
    else {
      throw new FailedRequestException("Failed to get categories for: " + pk, response);
    }

    return result;
  }

  /**
   * For adding categories to a specific dataset.
   *
   * @param dataset	the dataset to add the categories to
   * @param images 	the images to update
   * @param categories 	the categories to add to each image
   * @return		true if successful
   * @throws Exception	if request fails
   */
  public boolean addCategories(Dataset dataset, List<String> images, List<String> categories) throws Exception {
    return addCategories(dataset.getPK(), images, categories);
  }

  /**
   * For adding categories to a specific dataset by primary key.
   *
   * @param pk 		the primary key of the dataset to add the categories to
   * @param images 	the images to update
   * @param categories 	the categories to add to each image
   * @return		true if successful
   * @throws Exception	if request fails
   */
  public boolean addCategories(int pk, List<String> images, List<String> categories) throws Exception {
    boolean		result;
    JsonObject		data;
    JsonResponse	response;
    Request 		request;

    getLogger().info("adding categories to id: " + pk);

    result   = false;
    data     = new JsonObject();
    data.add("images", JsonUtils.toArray(images));
    data.add("categories", JsonUtils.toArray(categories));
    request  = newPost(getPath() + pk + "/categories")
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok()) {
      result = true;
    }
    else {
      throw new FailedRequestException("Failed to add categories for: " + pk, response);
    }

    return result;
  }

  /**
   * For removing categories from a specific dataset by primary key.
   *
   * @param dataset	the dataset to remove the categories from
   * @param images 	the images to update
   * @param categories 	the categories to add to each image
   * @return		true if successful
   * @throws Exception	if request fails
   */
  public boolean removeCategories(Dataset dataset, List<String> images, List<String> categories) throws Exception {
    return removeCategories(dataset.getPK(), images, categories);
  }

  /**
   * For removing categories from a specific dataset by primary key.
   *
   * @param pk 		the primary key of the dataset to remove the categories from
   * @param images 	the images to update
   * @param categories 	the categories to add to each image
   * @return		true if successful
   * @throws Exception	if request fails
   */
  public boolean removeCategories(int pk, List<String> images, List<String> categories) throws Exception {
    boolean		result;
    JsonResponse	response;
    Request 		request;

    getLogger().info("removing categories from id: " + pk);

    // TODO UFDL API needs changing, using PATCH with JSON body

    result   = false;
    request  = newDelete(getPath() + pk + "/categories")
      .parameter("images", images)
      .parameter("categories", categories);
    response = execute(request);
    if (response.ok()) {
      result = true;
    }
    else {
      throw new FailedRequestException("Failed to remove categories from: " + pk, response);
    }

    return result;
  }
}
