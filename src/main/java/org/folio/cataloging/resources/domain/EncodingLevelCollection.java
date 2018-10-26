package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"encodingLevels"})
public class EncodingLevelCollection {

  /**
   * (Required)
   */
  @JsonProperty("encodingLevels")
  @Valid
  @NotNull
  private List <EncodingLevel> encodingLevels = new ArrayList <EncodingLevel>();

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object>();

  /**
   * (Required)
   *
   * @return The encodingLevels
   */
  @JsonProperty("encodingLevels")
  public List <EncodingLevel> getEncodingLevels() {
    return encodingLevels;
  }

  /**
   * (Required)
   *
   * @param encodingLevels The encodingLevels
   */
  @JsonProperty("encodingLevels")
  public void setEncodingLevels(List <EncodingLevel> encodingLevels) {
    this.encodingLevels = encodingLevels;
  }

  public EncodingLevelCollection withEncodingLevels(List <EncodingLevel> encodingLevels) {
    this.encodingLevels = encodingLevels;
    return this;
  }

  @JsonAnyGetter
  public Map <String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

  public EncodingLevelCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }
}
