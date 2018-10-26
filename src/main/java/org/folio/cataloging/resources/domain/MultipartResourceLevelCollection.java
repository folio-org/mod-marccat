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
@JsonPropertyOrder({"multipartResourceLevels"})
public class MultipartResourceLevelCollection {

  /**
   * (Required)
   */
  @JsonProperty("multipartResourceLevels")
  @Valid
  @NotNull
  private List <MultipartResourceLevel> multipartResourceLevels = new ArrayList <MultipartResourceLevel>();

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object>();

  /**
   * (Required)
   *
   * @return The multipartResourceLevels
   */
  @JsonProperty("multipartResourceLevels")
  public List <MultipartResourceLevel> getMultipartResourceLevels() {
    return multipartResourceLevels;
  }

  /**
   * (Required)
   *
   * @param multipartResourceLevels The multipartResourceLevels
   */
  @JsonProperty("multipartResourceLevels")
  public void setMultipartResourceLevels(List <MultipartResourceLevel> multipartResourceLevels) {
    this.multipartResourceLevels = multipartResourceLevels;
  }

  public MultipartResourceLevelCollection withMultipartResourceLevels(List <MultipartResourceLevel> multipartResourceLevels) {
    this.multipartResourceLevels = multipartResourceLevels;
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

  public MultipartResourceLevelCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }
}
