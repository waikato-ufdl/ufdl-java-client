/*
 * ObjectDetectionDatasets.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapper;
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
 * Encapsulates dataset operations for object detection datasets.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ObjectDetectionDatasets
  extends Datasets {

  private static final long serialVersionUID = -6031589074773486947L;

  /**
   * Wraps a polygon.
   */
  public static class Polygon
    extends AbstractJsonObjectWrapper {

    /**
     * Initializes the wrapper.
     *
     * @param data	the underlying json
     */
    public Polygon(JsonObject data) {
      super(data);
    }

    /**
     * Returns the coordinates, list of x/y pairs.
     *
     * @return		the coordinates
     */
    public List<int[]> getCoordinates() {
      // TODO
      return new ArrayList<>();
    }

    /**
     * Returns a short description of the state.
     *
     * @return		the description
     */
    @Override
    public String toString() {
      // TODO
      return null;
    }
  }

  /**
   * Wraps a single annotation.
   */
  public static class Annotation
    extends AbstractJsonObjectWrapper {

    private static final long serialVersionUID = 6655060814716736432L;

    /**
     * Initializes the wrapper.
     *
     * @param data	the underlying json
     */
    public Annotation(JsonObject data) {
      super(data);
    }

    /**
     * Initializes the wrapper.
     *
     * @param x		the X
     * @param y		the Y
     * @param width 	the width
     * @param height 	the height
     * @param label 	the label
     */
    public Annotation(int x, int y, int width, int height, String label) {
      this(x, y, width, height, label, null);
    }

    /**
     * Initializes the wrapper.
     *
     * @param x		the X
     * @param y		the Y
     * @param width 	the width
     * @param height 	the height
     * @param label 	the label
     * @param polygon 	the polygon, can be null
     */
    public Annotation(int x, int y, int width, int height, String label, Polygon polygon) {
      super(new JsonObject());
      m_Data.addProperty("x", x);
      m_Data.addProperty("y", y);
      m_Data.addProperty("width", width);
      m_Data.addProperty("height", height);
      m_Data.addProperty("label", label);
      if (polygon != null)
        m_Data.add("polygon", polygon.getData());
    }

    /**
     * Returns the X.
     *
     * @return		the X
     */
    public int getX() {
      return getInt("x");
    }

    /**
     * Returns the Y.
     *
     * @return		the X
     */
    public int getY() {
      return getInt("y");
    }

    /**
     * Returns the width.
     *
     * @return		the width
     */
    public int getWidth() {
      return getInt("width");
    }

    /**
     * Returns the height.
     *
     * @return		the height
     */
    public int getHeight() {
      return getInt("height");
    }

    /**
     * Returns the label.
     *
     * @return		the label
     */
    public String getLabel() {
      return getString("label", "");
    }

    /**
     * Returns the prefix.
     *
     * @return		the prefix
     */
    public String getPrefix() {
      return getString("prefix", "Object");
    }

    /**
     * Returns whether a polygon is present.
     *
     * @return		true if present
     */
    public boolean hasPolygon() {
      return m_Data.has("polygon");
    }

    /**
     * Returns the polygon.
     *
     * @return		the polygon or null if none present
     */
    public Polygon getPolygon() {
      if (!hasPolygon())
        return null;
      else
        return new Polygon(m_Data.getAsJsonObject("polygon"));
    }

    /**
     * Returns a short description of the state.
     *
     * @return		the description
     */
    @Override
    public String toString() {
      return "x=" + getX() + ", y=" + getY() + ", width=" + getWidth() + ", height=" + getHeight() + ", label=" + getLabel() + ", poly=" + hasPolygon();
    }
  }

  /**
   * Container class for object detection dataset information.
   */
  public static class ObjectDetectionDataset
    extends Dataset {

    /**
     * Initializes the dataset.
     *
     * @param data the data to use
     */
    public ObjectDetectionDataset(JsonObject data) {
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
    return "/v1/objdet/datasets/";
  }

  /**
   * For loading the annotations for a specific dataset.
   *
   * @param dataset	the dataset to get the annotations for
   * @return		the annotations (image -> annotations)
   * @throws Exception	if request fails
   */
  public Map<String,List<Annotation>> getAnnotations(Dataset dataset) throws Exception {
    return getAnnotations(dataset.getPK());
  }

  /**
   * For loading the annotations for a specific dataset by primary key.
   *
   * @param pk 		the primary key of the dataset to get the annotations for
   * @return		the annotations (image -> annotations)
   * @throws Exception	if request fails
   */
  public Map<String,List<Annotation>> getAnnotations(int pk) throws Exception {
    Map<String,List<Annotation>>	result;
    Request 				request;
    JsonResponse 			response;
    JsonObject 				obj;
    JsonObject				img;
    JsonArray 				anns;
    List				list;

    getLogger().info("loading annotations for: " + pk);

    result   = null;
    request  = newGet(getPath() + pk + "/annotations");
    response = execute(request);
    if (response.ok()) {
      result = new HashMap<>();
      obj = response.json().getAsJsonObject();
      for (String key : obj.keySet()) {
	result.put(key, new ArrayList<>());
	img  = obj.getAsJsonObject(key);
	if (img.has("annotations")) {
	  anns = img.getAsJsonArray("annotations");
	  list = JsonUtils.asList(anns);
	  for (Object item : list) {
	    if (item instanceof JsonObject)
	      result.get(key).add(new Annotation((JsonObject) item));
	  }
	}
      }
    }
    else {
      throw new FailedRequestException("Failed to get annotations for: " + pk, response);
    }

    return result;
  }

  /**
   * For loading the annotations for an image from a dataset.
   *
   * @param dataset	the dataset to get the annotations for
   * @param name 	the name of the image to get the annotations for
   * @return		the annotations
   * @throws Exception	if request fails
   */
  public List<Annotation> getAnnotations(Dataset dataset, String name) throws Exception {
    return getAnnotations(dataset.getPK(), name);
  }

  /**
   * For loading the annotations for an image from a dataset by primary key.
   *
   * @param pk 		the primary key of the dataset to get the annotations for
   * @param name 	the name of the image to get the annotations for
   * @return		the annotations
   * @throws Exception	if request fails
   */
  public List<Annotation> getAnnotations(int pk, String name) throws Exception {
    List<Annotation>	result;
    Request 		request;
    JsonResponse 	response;
    List		list;

    getLogger().info("loading annotations for '" + name + "' from: " + pk);

    result   = null;
    request  = newGet(getPath() + pk + "/annotations/" + name);
    response = execute(request);
    if (response.ok()) {
      result = new ArrayList<>();
      list = JsonUtils.asList(response.json().getAsJsonArray());
      for (Object item: list) {
        if (item instanceof JsonObject)
	result.add(new Annotation((JsonObject) item));
      }
    }
    else {
      throw new FailedRequestException("Failed to get annotations of '" + name + "' from: " + pk, response);
    }

    return result;
  }

  /**
   * For settings the annotations for an image of a dataset.
   *
   * @param dataset	the dataset to get the annotations for
   * @param name 	the name of the image to get the annotations for
   * @param annotations the annotations
   * @return 		true if successful
   * @throws Exception	if request fails
   */
  public boolean setAnnotations(Dataset dataset, String name, List<Annotation> annotations) throws Exception {
    return setAnnotations(dataset.getPK(), name, annotations);
  }

  /**
   * For settings the annotations for an image of a dataset by primary key.
   *
   * @param pk 		the primary key of the dataset to get the annotations for
   * @param name 	the name of the image to get the annotations for
   * @param annotations the annotations
   * @return 		true if successful
   * @throws Exception	if request fails
   */
  public boolean setAnnotations(int pk, String name, List<Annotation> annotations) throws Exception {
    boolean		result;
    Request 		request;
    JsonObject		data;
    JsonArray		array;
    JsonResponse 	response;
    List		list;

    getLogger().info("setting annotations for '" + name + "' from: " + pk);

    result   = false;
    data     = new JsonObject();
    array    = new JsonArray();
    data.add("annotations", array);
    for (Annotation ann: annotations)
      array.add(ann.getData());
    request  = newPost(getPath() + pk + "/annotations/" + name)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = true;
    else
      throw new FailedRequestException("Failed to set annotations of '" + name + "' from: " + pk, response);

    return result;
  }

  /**
   * For deleting the annotations of an image of a dataset.
   *
   * @param dataset	the dataset to get the annotations for
   * @param name 	the name of the image to get the annotations for
   * @return 		true if successful
   * @throws Exception	if request fails
   */
  public boolean deleteAnnotations(Dataset dataset, String name) throws Exception {
    return deleteAnnotations(dataset.getPK(), name);
  }

  /**
   * For deleting the annotations of an image of a dataset by primary key.
   *
   * @param pk 		the primary key of the dataset to get the annotations for
   * @param name 	the name of the image to get the annotations for
   * @return 		true if successful
   * @throws Exception	if request fails
   */
  public boolean deleteAnnotations(int pk, String name) throws Exception {
    boolean		result;
    Request 		request;
    JsonResponse 	response;

    getLogger().info("deleting annotations of '" + name + "' from: " + pk);

    result   = false;
    request  = newDelete(getPath() + pk + "/annotations/" + name);
    response = execute(request);
    if (response.ok())
      result = true;
    else
      throw new FailedRequestException("Failed to delete annotations of '" + name + "' from: " + pk, response);

    return result;
  }
}