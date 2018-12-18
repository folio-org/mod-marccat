package org.folio.marccat.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

/**
 * Heading Type Schema
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"value", "label"})
public class HeadingType {

  @JsonProperty("value")
  private Integer code;

  @JsonProperty("label")
  private String description;

  /**
   * @return The code
   */
  @JsonProperty("value")
  public Integer getCode() {
    return code;
  }

  /**
   * @param code The code
   */
  @JsonProperty("value")
  public void setCode(Integer code) {
    this.code = code;
  }

  public HeadingType withCode(Integer code) {
    this.code = code;
    return this;
  }

  /**
   * @return The description
   */
  @JsonProperty("label")
  public String getDescription() {
    return description;
  }

  /**
   * @param description The description
   */
  @JsonProperty("label")
  public void setDescription(String description) {
    this.description = description;
  }

  public HeadingType withDescription(String description) {
    this.description = description;
    return this;
  }
}
