package org.folio.marccat.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

/**
 * Logical View Schema
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"value", "label"})
public class View {

  @JsonProperty("value")
  private String code;

  @JsonProperty("label")
  private String longDescription;

  /**
   * @return The code
   */
  @JsonProperty("value")
  public String getCode() {
    return code;
  }

  /**
   * @param code The code
   */
  @JsonProperty("value")
  public void setCode(String code) {
    this.code = code;
  }

  public View withCode(String code) {
    this.code = code;
    return this;
  }

  /**
   * @return The longDescription
   */
  @JsonProperty("label")
  public String getLongDescription() {
    return longDescription;
  }

  /**
   * @param longDescription The longDescription
   */
  @JsonProperty("label")
  public void setLongDescription(String longDescription) {
    this.longDescription = longDescription;
  }

  public View withLongDescription(String longDescription) {
    this.longDescription = longDescription;
    return this;
  }
}
