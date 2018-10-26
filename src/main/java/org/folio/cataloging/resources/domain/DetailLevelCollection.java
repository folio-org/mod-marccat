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
@JsonPropertyOrder({"detailLevels"})
public class DetailLevelCollection {

  /**
   * (Required)
   */
  @JsonProperty("detailLevels")
  @Valid
  @NotNull
  private List <DetailLevel> detailLevels = new ArrayList <DetailLevel>();

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object>();

  /**
   * (Required)
   *
   * @return The detailLevels
   */
  @JsonProperty("detailLevels")
  public List <DetailLevel> getDetailLevels() {
    return detailLevels;
  }

  /**
   * (Required)
   *
   * @param detailLevels The detailLevels
   */
  @JsonProperty("detailLevels")
  public void setDetailLevels(List <DetailLevel> detailLevels) {
    this.detailLevels = detailLevels;
  }

  public DetailLevelCollection withDetailLevels(List <DetailLevel> detailLevels) {
    this.detailLevels = detailLevels;
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

  public DetailLevelCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }
}
