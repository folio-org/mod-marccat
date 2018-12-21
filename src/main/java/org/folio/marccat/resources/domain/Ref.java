package org.folio.marccat.resources.domain;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;

/**
 * REF
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"refType", "stringText"})
public class Ref {

  @JsonProperty("refType")
  private Integer refType;

  @JsonProperty("stringText")
  private String stringText;

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
   * @return The stringText
   */
  @JsonProperty("stringText")
  public String getStringText() {
    return stringText;
  }

  /**
   * @param stringText The stringText
   */
  @JsonProperty("stringText")
  public void setStringText(String stringText) {
    this.stringText = stringText;
  }

  public Ref withStringText(String stringText) {
    this.stringText = stringText;
    return this;
  }

}
