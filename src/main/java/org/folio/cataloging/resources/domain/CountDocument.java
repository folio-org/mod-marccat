package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.annotation.Generated;
/**
 * Count Document
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"countDocuments", "query"})
public class CountDocument {

  @JsonProperty("countDocuments")
  private Integer countDocuments;

  @JsonProperty("query")
  private String query;

  /**
   * @return The countDocuments
   */
  @JsonProperty("countDocuments")
  public Integer getCountDocuments() {
    return countDocuments;
  }

  /**
   * @param countDocuments The countDocuments
   */
  @JsonProperty("countDocuments")
  public void setCountDocuments(Integer countDocuments) {
    this.countDocuments = countDocuments;
  }

  public CountDocument withCountDocuments(Integer countDocuments) {
    this.countDocuments = countDocuments;
    return this;
  }

  /**
   * @return The query
   */
  @JsonProperty("query")
  public String getQuery() {
    return query;
  }

  /**
   * @param query The query
   */
  @JsonProperty("query")
  public void setQuery(String query) {
    this.query = query;
  }

  public CountDocument withQuery(String query) {
    this.query = query;
    return this;
  }

}
