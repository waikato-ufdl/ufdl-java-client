/*
 * SpeechDatasets.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.action;

import com.github.fracpete.requests4j.request.Request;
import com.github.waikatoufdl.ufdl4j.core.FailedRequestException;
import com.github.waikatoufdl.ufdl4j.core.JsonResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.entity.ContentType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates dataset operations for speech.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class SpeechDatasets
  extends Datasets {

  private static final long serialVersionUID = -6031589074773486947L;

  /**
   * Container class for speech dataset information.
   */
  public static class SpeechDataset
    extends Dataset {

    private static final long serialVersionUID = -2813933771483250370L;

    /**
     * Initializes the dataset.
     *
     * @param data the data to use
     */
    public SpeechDataset(JsonObject data) {
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

    /**
     * Returns the transcripts associated with each sound file.
     *
     * @return 		the transcripts
     */
    public Map<String,String> transcripts() {
      Map<String,String>	result;
      JsonObject 		transcriptions;
      JsonObject		transObj;

      result = new HashMap<>();
      if (m_Data.has("transcriptions")) {
	transcriptions = m_Data.getAsJsonObject("transcriptions");
	for (String name : transcriptions.keySet()) {
	  transObj = transcriptions.get(name).getAsJsonObject();
	  if (transObj.has("transcription"))
	    result.put(name, transObj.get("transcription").getAsString());
	  else
	    result.put(name, "");
	}
      }

      return result;
    }

    /**
     * Returns the transcript associated with the specified sound file.
     *
     * @param name 	the name of the file to get the transcript for
     * @return 		the transcript, empty string if it doesn't exist
     */
    public String transcript(String name) {
      String			result;
      Map<String,String>	all;

      result = "";
      all    = transcripts();
      if (all.containsKey(name))
        result = all.get(name);

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
    return "/v1/speech/datasets/";
  }

  /**
   * For loading the transcripts for a specific dataset.
   *
   * @param dataset	the dataset to get the categories for
   * @return		the transcripts (file -> transcript)
   * @throws Exception	if request fails
   */
  public Map<String,String> getTranscripts(Dataset dataset) throws Exception {
    return getTranscripts(dataset.getPK());
  }

  /**
   * For loading the transcripts for a specific dataset by primary key.
   *
   * @param pk 		the primary key of the dataset to get the categories for
   * @return		the transcripts (file -> transcript)
   * @throws Exception	if request fails
   */
  public Map<String,String> getTranscripts(int pk) throws Exception {
    Map<String,String>	result;
    Request 		request;
    JsonResponse 	response;
    JsonObject		obj;
    JsonObject		fileObj;

    getLogger().info("loading transcripts for: " + pk);

    result   = null;
    request  = newGet(getPath() + pk + "/transcriptions");
    response = execute(request);
    if (response.ok()) {
      obj = response.json().getAsJsonObject();
      result = new HashMap<>();
      for (String name : obj.keySet()) {
        fileObj = obj.get(name).getAsJsonObject();
        if (fileObj.has("transcription"))
	  result.put(name, fileObj.get("transcription").getAsString());
        else
          result.put(name, "");
      }
    }
    else {
      throw new FailedRequestException("Failed to get transcripts for: " + pk, response);
    }

    return result;
  }

  /**
   * For loading the transcript of a sound file from a specific dataset.
   *
   * @param dataset	the dataset to get the transcript for
   * @param name 	the name of the file to get the transcript for
   * @return		the transcript or empty string if not present
   * @throws Exception	if request fails
   */
  public String getTranscript(Dataset dataset, String name) throws Exception {
    return getTranscript(dataset.getPK(), name);
  }

  /**
   * For loading the transcript of a sound file from a specific dataset by primary key.
   *
   * @param pk 		the primary key of the dataset to get the transcript for
   * @param name 	the name of the file to get the transcript for
   * @return		the transcript or empty string if not present
   * @throws Exception	if request fails
   */
  public String getTranscript(int pk, String name) throws Exception {
    String		result;
    Map<String,String> 	transcripts;

    result      = "";
    transcripts = getTranscripts(pk);
    if (transcripts.containsKey(name))
      result = transcripts.get(name);

    return result;
  }

  /**
   * Sets the transcript for a file.
   *
   * @param dataset	the dataset to set the transcript for
   * @param name 	the file to update
   * @param transcript 	the transcript itself
   * @return		true if successful
   * @throws Exception	if request fails
   */
  public boolean setTranscript(Dataset dataset, String name, String transcript) throws Exception {
    return setTranscript(dataset.getPK(), name, transcript);
  }

  /**
   * Sets the transcript for a file by primary key.
   *
   * @param pk 		the primary key of the dataset to set the transcript for
   * @param name 	the file to update
   * @param transcript 	the transcript itself
   * @return		true if successful
   * @throws Exception	if request fails
   */
  public boolean setTranscript(int pk, String name, String transcript) throws Exception {
    boolean		result;
    JsonObject		data;
    JsonResponse	response;
    Request 		request;

    getLogger().info("setting transcript for '" + name + "' in: " + pk);

    result   = false;
    data     = new JsonObject();
    data.addProperty("transcription", transcript);
    request  = newPost(getPath() + pk + "/transcriptions/" + name)
      .body(data.toString(), ContentType.APPLICATION_JSON);
    response = execute(request);
    if (response.ok()) {
      result = true;
    }
    else {
      throw new FailedRequestException("Failed to set transcript for '" + name + "' in: " + pk, response);
    }

    return result;
  }
}
