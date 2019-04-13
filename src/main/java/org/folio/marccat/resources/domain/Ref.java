package org.folio.marccat.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

/**
 * REF
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"refType", "displayValue"})
public class Ref {

  @JsonProperty("refType")
  private Integer refType;

  @JsonProperty("displayValue")
  private String displayValue;

  /**
   * @return The ref type
   */
  @JsonProperty("refType")
  public Integer getRefType() {
    return refType;
  }

  /**
   * @param refType The ref type
   */
  @JsonProperty("refType")
  public void setRefType(Integer refType) {
    this.refType = refType;
  }

  public Ref withRefType(Integer refType) {
    this.refType = refType;
    return this;
  }

  /**
   * @return The displayValue
   */
  @JsonProperty("displayValue")
  public String getDisplayValue() {
    return displayValue;
  }

  /**
   * @param displayValue The displayValue
   */
  @JsonProperty("displayValue")
  public void setDisplayValue(String displayValue) {
    this.displayValue = displayValue;
  }

  public Ref withStringText(String stringText) {
    this.displayValue = stringText;
    return this;
  }

}
