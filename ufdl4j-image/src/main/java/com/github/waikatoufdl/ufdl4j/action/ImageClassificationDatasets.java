/*
 * ImageClassificationDatasets.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.core.MediaTypeHelper;
import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;
import com.github.waikatoufdl.ufdl4j.core.JsonUtils;
import com.google.gson.JsonObject;

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

    private static final long serialVersionUID = 2031525681885013068L;

    /**
     * Initializes the dataset.
     *
     * @param data the data to use
     */
    public ImageClassificationDataset(JsonObject data) {
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

    getLogger().info("loading categories for: " + pk);

    result   = null;
    request  = newGet(getPath() + pk + "/categories");
    response = execute(request);
    if (response.ok()) {
      obj = response.json().getAsJsonObject();
      result = new HashMap<>();
      for (String key: obj.keySet()) {
        categories = new ArrayList<>();
        result.put(key, categories);
        list = JsonUtils.asList(obj, key, new ArrayList());
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
   * For loading the categories of an image from a specific dataset.
   *
   * @param dataset	the dataset to get the categories for
   * @param name 	the name of the image to get the categories for
   * @return		the categories
   * @throws Exception	if request fails
   */
  public List<String> getCategories(Dataset dataset, String name) throws Exception {
    return getCategories(dataset.getPK(), name);
  }

  /**
   * For loading the categories of an image from a specific dataset by primary key.
   *
   * @param pk 		the primary key of the dataset to get the categories for
   * @param name 	the name of the image to get the categories for
   * @return		the categories
   * @throws Exception	if request fails
   */
  public List<String> getCategories(int pk, String name) throws Exception {
    List<String>		result;
    Map<String,List<String>>	categories;

    result     = new ArrayList<>();
    categories = getCategories(pk);
    if (categories.containsKey(name))
      result = categories.get(name);

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

    getLogger().info("adding categories to: " + pk);

    result   = false;
    data     = new JsonObject();
    data.addProperty("method", "add");
    data.add("images", JsonUtils.toArray(images));
    data.add("categories", JsonUtils.toArray(categories));
    request  = newPatch(getPath() + pk + "/categories")
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
    response = execute(request);
    if (response.ok()) {
      result = true;
    }
    else {
      throw new FailedRequestException("Failed to add categories to: " + pk, response);
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
    JsonObject		data;
    JsonResponse	response;
    Request 		request;

    getLogger().info("removing categories from: " + pk);

    result   = false;
    data     = new JsonObject();
    data.addProperty("method", "remove");
    data.add("images", JsonUtils.toArray(images));
    data.add("categories", JsonUtils.toArray(categories));
    request  = newPatch(getPath() + pk + "/categories")
      .body(data.toString(), MediaTypeHelper.APPLICATION_JSON_UTF8);
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
