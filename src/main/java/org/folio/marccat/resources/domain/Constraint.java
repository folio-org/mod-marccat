package org.folio.marccat.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"value", "label"})
public class Constraint {

  @JsonProperty("value")
  private String code;
  @JsonProperty("label")
  private String label;

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

  public Constraint withCode(String code) {
    this.code = code;
    return this;
  }

  /**
   * @return The label
   */
  @JsonProperty("label")
  public String getLabel() {
    return label;
  }

  /**
   * @param label The label
   */
  @JsonProperty("label")
  public void setLabel(String label) {
    this.label = label;
  }

  public Constraint withLabel(String label) {
    this.label = label;
    return this;
  }

}
