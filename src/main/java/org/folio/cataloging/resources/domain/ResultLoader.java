package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "filename", "ids", "added", "rejected", "errorCount" })
public class ResultLoader {

  @JsonProperty("filename")
  private String filename;

  @JsonProperty("ids")
  private List<Integer> ids;

  @JsonProperty("added")
  private int added;

  @JsonProperty("rejected")
  private int rejected;

  @JsonProperty("errorCount")
  private int errorCount;

  /**
   *
   * @return
   *     The filename
   */
  @JsonProperty("filename")
  public String getFilename() {
    return filename;
  }

  /**
   *
   * @param filename
   *    the filename
   */
  @JsonProperty("filename")
  public void setFilename(String filename) {
    this.filename = filename;
  }

  /**
   *
   * @return
   *     The an list
   */
  @JsonProperty("ids")
  public List<Integer> getIds() {
    return ids;
  }

  /**
   *
   * @param ids
   *    the an list
   */
  @JsonProperty("ids")
  public void setIds(List<Integer> ids) {
    this.ids = ids;
  }

  /**
   *
   * @return
   *     The number of added record
   */
  @JsonProperty("added")
  public int getAdded() {
    return added;
  }

  /**
   *
   * @param added
   *    The number of added record
   */
  @JsonProperty("added")
  public void setAdded(int added) {
    this.added = added;
  }

  /**
   *
   * @return
   *     The number of rejected record
   */
  @JsonProperty("rejected")
  public int getRejected() {
    return rejected;
  }

  /**
   *
   * @param rejected
   *    The number of rejected record
   */
  @JsonProperty("rejected")
  public void setRejected(int rejected) {
    this.rejected = rejected;
  }

  /**
   *
   * @return
   *     The number of error record
   */
  @JsonProperty("errorCount")
  public int getErrorCount() {
    return errorCount;
  }

  /**
   *
   * @param errorCount
   *    The number of error record
   */
  @JsonProperty("errorCount")
  public void setErrorCount(int errorCount) {
    this.errorCount = errorCount;
  }
}
