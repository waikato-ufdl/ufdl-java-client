/*
 * Datasets.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.attachment.FileAttachment;
import com.github.fracpete.requests4j.request.Request;
import com.github.fracpete.requests4j.response.BasicResponse;
import com.github.fracpete.requests4j.response.FileResponse;
import com.github.fracpete.requests4j.response.Response;
import com.github.fracpete.requests4j.response.StreamResponse;
import com.github.waikatoufdl.ufdl4j.action.Licenses.License;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapperWithPK;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonObjectWithShortDescription;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;
import com.github.waikatoufdl.ufdl4j.core.SoftDeleteObject;
import com.github.waikatoufdl.ufdl4j.filter.Filter;
import com.github.waikatoufdl.ufdl4j.filter.NameFilter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.entity.ContentType;

import java.io.File;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates dataset operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Datasets
  extends AbstractAction {

  private static final long serialVersionUID = 7013386269355130329L;

  /**
   * Container class for dataset information.
   */
  public static class Dataset
    extends AbstractJsonObjectWrapperWithPK
    implements SoftDeleteObject, JsonObjectWithShortDescription {

    private static final long serialVersionUID = 3523630902439390574L;

    /**
     * Initializes the dataset.
     *
     * @param data	the data to use
     */
    public Dataset(JsonObject data) {
      super(data);
    }

    /**
     * Returns the dataset primary key.
     *
     * @return		the primary key
     */
    public int getPK() {
      return getInt("pk");
    }

    /**
     * Returns the creator ID.
     *
     * @return		the ID
     */
    public int getCreator() {
      return getInt("creator", -1);
    }

    /**
     * Returns the project ID.
     *
     * @return		the ID
     */
    public int getProject() {
      return getInt("project", -1);
    }

    /**
     * Returns the dataset name.
     *
     * @return		the name
     */
    public String getName() {
      return getString("name", "");
    }

    /**
     * Returns the dataset description.
     *
     * @return		the description
     */
    public String getDescription() {
      return getString("description", "");
    }

    /**
     * Returns the version.
     *
     * @return		the version
     */
    public int getVersion() {
      return getInt("version", -1);
    }

    /**
     * Returns the license.
     *
     * @return		the license
     */
    public int getLicense() {
      return getInt("licence", -1);
    }

    /**
     * Returns the tags.
     *
     * @return		the tags
     */
    public String getTags() {
      return getString("tags", "");
    }

    /**
     * Returns the creation date/time.
     *
     * @return		the date/time, can be null
     */
    public LocalDateTime getCreationTime() {
      return getDateTime("creation_time", null);
    }

    /**
     * Returns the deletion date/time.
     *
     * @return		the date/time, can be null
     */
    public LocalDateTime getDeletionTime() {
      return getDateTime("deletion_time", null);
    }

    /**
     * Returns whether the dataset public.
     *
     * @return		true if public
     */
    public boolean isPublic() {
      return getBoolean("is_public", false);
    }

    /**
     * Returns the short description.
     *
     * @return		the short description
     */
    @Override
    public String getShortDescription() {
      return getName() + "/" + getVersion();
    }

    /**
     * Returns a short description of the state.
     *
     * @return		the state
     */
    @Override
    public String toString() {
      return "pk=" + getPK() + ", name=" + getName() + ", project=" + getProject() + ", license=" + getLicense() + ", desc=" + getDescription();
    }
  }

  /**
   * Returns the name of the action.
   *
   * @return		the name
   */
  @Override
  public String getName() {
    return "Datasets";
  }

  /**
   * The URL path to use.
   *
   * @return		the path
   */
  @Override
  public String getPath() {
    return "/v1/core/datasets/";
  }

  /**
   * For listing the datasets.
   *
   * @return		the list of datasets
   * @throws Exception	if request fails
   */
  public List<Dataset> list() throws Exception {
    return list(null);
  }

  /**
   * For listing the datasets.
   *
   * @param filter 	the filter to apply, can be null
   * @return		the list of datasets
   * @throws Exception	if request fails
   */
  public List<Dataset> list(Filter filter) throws Exception {
    List<Dataset>		result;
    JsonResponse 	response;
    JsonElement		element;
    JsonArray		array;
    Request 		request;
    int			i;

    getLogger().info("listing datasets" + (filter == null ? "" : ", filter: " + filter.toJsonObject()));

    result   = new ArrayList<>();
    request  = newPost(getPath() + "list");
    if (filter != null)
      request.body(filter.toJsonObject().toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonArray()) {
        array = element.getAsJsonArray();
        for (i = 0; i < array.size(); i++)
          result.add(new Dataset(array.get(i).getAsJsonObject()));
      }
    }
    else {
      throw new FailedRequestException("Failed to list datasets!" + (filter == null ? "" : "\nFilter: " + filter.toJsonObject()), response);
    }

    return result;
  }

  /**
   * For loading a specific dataset by primary key.
   *
   * @param pk 		the primary key of the dataset to load
   * @return		the dataset
   * @throws Exception	if request fails
   */
  public Dataset load(int pk) throws Exception {
    Dataset		result;
    JsonResponse 	response;
    JsonElement		element;
    Request 		request;

    getLogger().info("loading dataset with id: " + pk);

    result   = null;
    request  = newGet(getPath() + pk);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject())
	result = new Dataset(element.getAsJsonObject());
    }
    else {
      throw new FailedRequestException("Failed to load dataset: " + pk, response);
    }

    return result;
  }

  /**
   * For loading a specific dataset by name.
   *
   * @param name 	the dataset name
   * @return		the dataset object, null if failed to create
   * @throws Exception	if request fails
   */
  public Dataset load(String name) throws Exception {
    Dataset	result;

    getLogger().info("loading dataset with name: " + name);

    result = null;

    for (Dataset dataset : list(new NameFilter(name))) {
      result = dataset;
      break;
    }

    if (result == null)
      getLogger().warning("failed to load dataset: " + name);

    return result;
  }

  /**
   * Creates the dataset.
   *
   * @param dataset 	the dataset name
   * @param description the description of the dataset
   * @param project 	the project PK this dataset belongs to
   * @param license 	the license for the dataset
   * @param isPublic 	whether the dataset is public
   * @param tags 	tags for the dataset
   * @return		the dataset object, null if failed to create
   * @throws Exception	if request fails or dataset already exists
   */
  public Dataset create(String dataset, String description, int project, License license, boolean isPublic, String tags) throws Exception {
    return create(dataset, description, project, license.getPK(), isPublic, tags);
  }

  /**
   * Creates the dataset.
   *
   * @param dataset 	the dataset name
   * @param description the description of the dataset
   * @param project 	the project PK this dataset belongs to
   * @param license 	the license PK for the dataset
   * @param isPublic 	whether the dataset is public
   * @param tags 	tags for the dataset
   * @return		the dataset object, null if failed to create
   * @throws Exception	if request fails or dataset already exists
   */
  public Dataset create(String dataset, String description, int project, int license, boolean isPublic, String tags) throws Exception {
    Dataset		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("creating dataset: " + dataset);

    data = new JsonObject();
    data.addProperty("name", dataset);
    data.addProperty("description", description);
    data.addProperty("project", project);
    data.addProperty("licence", license);
    data.addProperty("is_public", isPublic);
    data.addProperty("tags", tags);
    request = newPost(getPath() + "create")
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new Dataset(response.jsonObject());
    else
      throw new FailedRequestException("Failed to create dataset: " + dataset, response);

    return result;
  }

  /**
   * Updates the dataset.
   *
   * @param datasetObj	the dataset to update
   * @param dataset 	the dataset name
   * @param description the description of the dataset
   * @param project 	the project PK this dataset belongs to
   * @param license 	the license PK for the dataset
   * @param isPublic 	whether the dataset is public
   * @param tags 	tags for the dataset
   * @return		the dataset object, null if failed to create
   * @throws Exception	if request fails or dataset already exists
   */
  public Dataset update(Dataset datasetObj, String dataset, String description, int project, int license, boolean isPublic, String tags) throws Exception {
    return update(datasetObj.getPK(), dataset, description, project, license, isPublic, tags);
  }

  /**
   * Updates the dataset.
   *
   * @param pk		the PK of the dataset to update
   * @param dataset 	the dataset name
   * @param description the description of the dataset
   * @param project 	the project PK this dataset belongs to
   * @param license 	the license PK for the dataset
   * @param isPublic 	whether the dataset is public
   * @param tags 	tags for the dataset
   * @return		the dataset object, null if failed to create
   * @throws Exception	if request fails or dataset already exists
   */
  public Dataset update(int pk, String dataset, String description, int project, int license, boolean isPublic, String tags) throws Exception {
    Dataset		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("updating dataset: " + pk);

    data = new JsonObject();
    data.addProperty("name", dataset);
    data.addProperty("description", description);
    data.addProperty("project", project);
    data.addProperty("licence", license);
    data.addProperty("is_public", isPublic);
    data.addProperty("tags", tags);
    request = newPut(getPath() + pk)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new Dataset(response.jsonObject());
    else
      throw new FailedRequestException("Failed to update dataset: " + pk, response);

    return result;
  }

  /**
   * (Partially) Updates the dataset with the non-null values.
   *
   * @param datasetObj	the dataset to update
   * @param dataset 	the dataset name
   * @param description the description of the dataset
   * @param project 	the project PK this dataset belongs to
   * @param license 	the license PK for the dataset
   * @param isPublic 	whether the dataset is public
   * @param tags 	tags for the dataset
   * @return		the dataset object, null if failed to create
   * @throws Exception	if request fails or dataset already exists
   */
  public Dataset partialUpdate(Dataset datasetObj, String dataset, String description, Integer project, Integer license, Boolean isPublic, String tags) throws Exception {
    return partialUpdate(datasetObj.getPK(), dataset, description, project, license, isPublic, tags);
  }

  /**
   * (Partially) Updates the dataset with the non-null values.
   *
   * @param pk		the PK of the dataset to update
   * @param dataset 	the dataset name
   * @param description the description of the dataset
   * @param project 	the project PK this dataset belongs to
   * @param license 	the license PK for the dataset
   * @param isPublic 	whether the dataset is public
   * @param tags 	tags for the dataset
   * @return		the dataset object, null if failed to create
   * @throws Exception	if request fails or dataset already exists
   */
  public Dataset partialUpdate(int pk, String dataset, String description, Integer project, Integer license, Boolean isPublic, String tags) throws Exception {
    Dataset		result;
    JsonObject		data;
    JsonResponse 	response;
    Request 		request;

    getLogger().info("partially updating dataset: " + pk);

    data = new JsonObject();
    if (dataset != null)
      data.addProperty("name", dataset);
    if (description != null)
      data.addProperty("description", description);
    if (project != null)
      data.addProperty("project", project);
    if (license != null)
      data.addProperty("licence", license);
    if (isPublic != null)
      data.addProperty("is_public", isPublic);
    if (tags != null)
      data.addProperty("tags", tags);
    request = newPatch(getPath() + pk)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      result = new Dataset(response.jsonObject());
    else
      throw new FailedRequestException("Failed to partially update dataset: " + pk, response);

    return result;
  }

  /**
   * Adds the file to the dataset (uploads it to the server).
   *
   * @param dataset	the dataset
   * @param file	the file to upload
   * @param name	the name to use in the dataset
   * @return		true if successfully added/uploaded
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean addFile(Dataset dataset, File file, String name) throws Exception {
    return addFile(dataset.getPK(), file, name);
  }

  /**
   * Adds the file to the dataset (uploads it to the server).
   *
   * @param pk		the dataset ID
   * @param file	the file to upload
   * @param name	the name to use in the dataset
   * @return		true if successfully added/uploaded
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean addFile(int pk, File file, String name) throws Exception {
    Request 	request;
    Response 	response;

    request = newPost(getPath() + pk + "/files/" + name)
      .attachment(new FileAttachment(file));
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to add file to dataset " + pk + ": " + file, response);
  }

  /**
   * Retrieves the specified file from the dataset (downloads it from the server).
   *
   * @param dataset	the dataset
   * @param output	the output file
   * @param name	the name used in the dataset
   * @return		true if successfully downloaded
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean getFile(Dataset dataset, String name, File output) throws Exception {
    return getFile(dataset.getPK(), name, output);
  }

  /**
   * Retrieves the specified file from the dataset (downloads it from the server).
   *
   * @param pk		the dataset ID
   * @param output	the output file
   * @param name	the name used in the dataset
   * @return		true if successfully downloaded
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean getFile(int pk, String name, File output) throws Exception {
    Request 	request;
    Response 	response;

    request = newGet(getPath() + pk + "/files/" + name);
    response = download(request, output);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to get file from dataset " + pk + ": " + name, response);
  }

  /**
   * Retrieves the specified file from the dataset (downloads it from the server).
   *
   * @param dataset	the dataset
   * @param name	the name used in the dataset
   * @return		the file content as byte array
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public byte[] getFile(Dataset dataset, String name) throws Exception {
    return getFile(dataset.getPK(), name);
  }

  /**
   * Retrieves the specified file from the dataset (downloads it from the server).
   *
   * @param pk		the dataset ID
   * @param name	the name used in the dataset
   * @return		the file content as byte array
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public byte[] getFile(int pk, String name) throws Exception {
    Request 		request;
    BasicResponse 	response;

    request = newGet(getPath() + pk + "/files/" + name);
    response = execute(request);
    if (response.ok())
      return response.body();
    else
      throw new FailedRequestException("Failed to get file from dataset " + pk + ": " + name, response);
  }

  /**
   * Retrieves the specified file from the dataset and outputs it using the output stream.
   *
   * @param dataset	the dataset
   * @param name	the name used in the dataset
   * @param stream 	the output stream to write to
   * @return		true if successfully streamed
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean getFile(Dataset dataset, String name, OutputStream stream) throws Exception {
    return getFile(dataset.getPK(), name, stream);
  }

  /**
   * Retrieves the specified file from the dataset and outputs it using the output stream.
   *
   * @param pk		the dataset ID
   * @param name	the name used in the dataset
   * @param stream 	the output stream to write to
   * @return		true if successfully streamed
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean getFile(int pk, String name, OutputStream stream) throws Exception {
    Request 		request;
    StreamResponse response;

    request = newGet(getPath() + pk + "/files/" + name);
    response = stream(request, stream);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to get file from dataset " + pk + ": " + name, response);
  }

  /**
   * Deletes the specified file from the dataset (downloads it from the server).
   *
   * @param dataset	the dataset
   * @param name	the name used in the dataset
   * @return		true if successfully downloaded
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean deleteFile(Dataset dataset, String name) throws Exception {
    return deleteFile(dataset.getPK(), name);
  }

  /**
   * Deletes the specified file from the dataset (downloads it from the server).
   *
   * @param pk		the dataset ID
   * @param name	the name used in the dataset
   * @return		true if successfully downloaded
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean deleteFile(int pk, String name) throws Exception {
    Request 	request;
    Response 	response;

    request = newDelete(getPath() + pk + "/files/" + name);
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete file from dataset " + pk + ": " + name, response);
  }

  /**
   * Sets the meta-data of the specified file in the dataset.
   *
   * @param dataset	the dataset
   * @param name	the name of the file in the dataset
   * @param metadata 	the meta-data to upload, use empty string to remove data
   * @return		true if successfully added/uploaded
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean setMetadata(Dataset dataset, String name, String metadata) throws Exception {
    return setMetadata(dataset.getPK(), name, metadata);
  }

  /**
   * Sets the meta-data of the specified file in the dataset.
   *
   * @param pk		the dataset ID
   * @param name	the name of the file in the dataset
   * @param metadata 	the meta-data to upload, use empty string to remove data
   * @return		true if successfully added/uploaded
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean setMetadata(int pk, String name, String metadata) throws Exception {
    Request 		request;
    JsonObject		data;
    JsonResponse 	response;

    getLogger().info("setting metadata for file '" + name + "' in dataset id: " + pk);

    data = new JsonObject();
    data.addProperty("metadata", metadata);
    request = newPost(getPath() + pk + "/metadata/" + name)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to set metadata for file '" + name + "' in dataset " + pk + "!", response);
  }

  /**
   * Retrieves the metadata for the specified file from the dataset (downloads it from the server).
   *
   * @param dataset	the dataset
   * @param name	the name used in the dataset
   * @return		the metadata content as string
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public String getMetadata(Dataset dataset, String name) throws Exception {
    return getMetadata(dataset.getPK(), name);
  }

  /**
   * Retrieves the metadata for the specified file from the dataset (downloads it from the server).
   *
   * @param pk		the dataset ID
   * @param name	the name used in the dataset
   * @return		the metadata content as string
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public String getMetadata(int pk, String name) throws Exception {
    Request 		request;
    JsonResponse 	response;
    JsonElement		element;
    JsonObject		obj;

    request  = newGet(getPath() + pk + "/metadata/" + name);
    response = execute(request);
    if (response.ok()) {
      element = response.json();
      if (element.isJsonObject()) {
        obj = element.getAsJsonObject();
        if (obj.has("metadata"))
	  return obj.get("metadata").getAsString();
	else
	  throw new FailedRequestException("No metadata returned for file '" + name + "' from dataset " + pk + "!", response);
      }
      else {
	throw new FailedRequestException("Expected JSON response for file '" + name + "' from dataset " + pk + "!", response);
      }
    }
    else {
      throw new FailedRequestException("Failed to get metadata of file '" + name + "' from dataset " + pk + "!", response);
    }
  }

  /**
   * For downloading a specific dataset.
   *
   * @param dataset	the dataset to download
   * @param output	the file to save the downloaded dataset to (zip or tar.gz)
   * @return		true if successful
   * @throws Exception	if request fails
   */
  public boolean download(Dataset dataset, File output) throws Exception {
    return download(dataset.getPK(), new String[0], output);
  }

  /**
   * For downloading a specific dataset.
   *
   * @param pk 		the primary key of the dataset to download
   * @param output	the file to save the downloaded dataset to (zip or tar.gz)
   * @return		true if successful
   * @throws Exception	if request fails
   */
  public boolean download(int pk, File output) throws Exception {
    return download(pk, new String[0], output);
  }

  /**
   * For downloading a specific dataset.
   *
   * @param dataset	the dataset to download
   * @param params 	the parameters for <a href="https://github.com/waikato-ufdl/wai-annotations">wai.annotations</a>
   *                    to generate the output (eg ["vgg", "-o", "ann.json"] or ["tfrecords", "-o", "train.tfrecords"])
   * @param output	the file to save the downloaded dataset to (zip or tar.gz)
   * @return		true if successful
   * @throws Exception	if request fails
   */
  public boolean download(Dataset dataset, String[] params, File output) throws Exception {
    return download(dataset.getPK(), params, output);
  }

  /**
   * For downloading a specific dataset.
   *
   * @param pk 		the primary key of the dataset to download
   * @param params 	the parameters for <a href="https://github.com/waikato-ufdl/wai-annotations">wai.annotations</a>
   *                    to generate the output (eg ["vgg", "-o", "ann.json"] or ["tfrecords", "-o", "train.tfrecords"])
   * @param output	the file to save the downloaded dataset to (zip or tar.gz)
   * @return		true if successful
   * @throws Exception	if request fails
   */
  public boolean download(int pk, String[] params, File output) throws Exception {
    FileResponse 	response;
    Request 		request;
    String 		filetype;
    JsonObject		data;
    JsonObject		parameters;
    JsonArray		parameterArray;

    getLogger().info("downloading dataset with id: " + pk);

    if (output.getName().endsWith(".zip"))
      filetype = "zip";
    else if (output.getName().endsWith(".tar.gz"))
      filetype = "tar.gz";
    else
      throw new IllegalArgumentException("Only zip or tar.gz available for download: " + output);

    parameterArray = new JsonArray();
    for (String param: params)
      parameterArray.add(param);
    data = new JsonObject();
    data.addProperty("filetype", filetype);
    parameters = new JsonObject();
    data.add("params", parameters);
    parameters.add("annotations_args", parameterArray);
    request = newPost(getPath() + pk + "/download")
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = download(request, output);
    if (!response.ok())
      throw new FailedRequestException("Failed to download dataset: " + pk, response);

    return true;
  }

  /**
   * For copying a dataset.
   *
   * @param dataset	the dataset to copy
   * @param newName	the new name
   * @return		true if successful
   * @throws Exception	if request fails
   */
  public boolean copy(Dataset dataset, String newName) throws Exception {
    return copy(dataset.getPK(), newName);
  }

  /**
   * For copying a dataset.
   *
   * @param pk 		the primary key of the dataset to copy
   * @param newName	the new name
   * @return		true if successful
   * @throws Exception	if request fails
   */
  public boolean copy(int pk, String newName) throws Exception {
    JsonResponse 	response;
    Request 		request;
    JsonObject		data;

    getLogger().info("copying dataset with id: " + pk);

    data = new JsonObject();
    data.addProperty("new_name", newName);
    request = newPost(getPath() + pk + "/copy")
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (!response.ok())
      throw new FailedRequestException("Failed to copy dataset: " + pk, response);

    return true;
  }

  /**
   * For merging two datasets.
   *
   * @param dataset 	the dataset to merge into
   * @param source 	the dataset to merge data from
   * @param delete 	whether to delete the source dataset after the merge
   * @param hard 	whether to performa a hard delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean merge(Dataset dataset, Dataset source, boolean delete, boolean hard) throws Exception {
    return merge(dataset.getPK(), source.getPK(), delete, hard);
  }

  /**
   * For merging two datasets.
   *
   * @param pk 		the dataset PK to merge into
   * @param source_pk 	the dataset PK to merge data from
   * @param delete 	whether to delete the source dataset after the merge
   * @param hard 	whether to performa a hard delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean merge(int pk, int source_pk, boolean delete, boolean hard) throws Exception {
    JsonResponse 	response;
    JsonObject		data;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);
    if (source_pk == -1)
      throw new IllegalArgumentException("Invalid source PK: " + source_pk);

    getLogger().info("merging dataset PK " + pk + " with source PK " + source_pk + " (delete=" + delete + ")");

    data = new JsonObject();
    data.addProperty("delete", delete);
    if (delete)
      data.addProperty("hard", hard);
    request  = newPost(getPath() + pk + "/merge/" + source_pk)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to merge dataset PK " + pk + " with source PK " + source_pk + " (delete=" + delete + ")", response);
  }

  /**
   * For deleting a specific dataset.
   *
   * @param dataset 	the dataset to delete
   * @param hard 	whether hard or soft delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean delete(Dataset dataset, boolean hard) throws Exception {
    return delete(dataset.getPK(), hard);
  }

  /**
   * For deleting a specific dataset.
   *
   * @param pk 		the ID of the dataset
   * @param hard 	whether hard or soft delete
   * @return		true if successfully deleted
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean delete(int pk, boolean hard) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("deleting dataset with PK (hard=" + hard + "): " + pk);

    request  = newDelete(getPath() + pk + (hard ? "/hard" : "/"));
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to delete dataset (hard=" + hard + "): " + pk, response);
  }

  /**
   * For reinstating a specific dataset.
   *
   * @param dataset 	the dataset to reinstate
   * @return		true if successfully reinstated
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean reinstate(Dataset dataset) throws Exception {
    return reinstate(dataset.getPK());
  }

  /**
   * For reinstating a specific dataset.
   *
   * @param pk 		the ID of the dataset
   * @return		true if successfully reinstated
   * @throws Exception	if request fails, eg invalid dataset PK
   */
  public boolean reinstate(int pk) throws Exception {
    JsonResponse 	response;
    Request 		request;

    if (pk == -1)
      throw new IllegalArgumentException("Invalid PK: " + pk);

    getLogger().info("Reinstating dataset with PK: " + pk);

    request  = newDelete(getPath() + pk + "/reinstate");
    response = execute(request);
    if (response.ok())
      return true;
    else
      throw new FailedRequestException("Failed to reinstate dataset: " + pk, response);
  }
}
