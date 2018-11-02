package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

/**
 * Error schema definition.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"code", "descrption"})
public class Error {

  @JsonProperty("code")
  private String code;

  @JsonProperty("description")
  private String description;

  /**
   * error code
   * <p>
   *
   * @return The error code
   */
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  /**
   * error code
   * <p>
   *
   * @param code The error code
   */
  @JsonProperty("code")
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * error description
   * <p>
   *
   * @return The error description
   */
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  /**
   * error description
   * <p>
   *
   * @param description The error description
   */
  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

}
