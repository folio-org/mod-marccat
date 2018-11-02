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
@JsonPropertyOrder({"skipInFiling"})
public class SkipInFilingCollection {

  /**
   * (Required)
   */
  @JsonProperty("skipInFiling")
  @Valid
  @NotNull
  private List <SkipInFiling> skipInFiling = new ArrayList <SkipInFiling>();

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object>();

  /**
   * (Required)
   *
   * @return The skipInFiling
   */
  @JsonProperty("skipInFiling")
  public List <SkipInFiling> getSkipInFiling() {
    return skipInFiling;
  }

  /**
   * (Required)
   *
   * @param skipInFiling The skipInFiling
   */
  @JsonProperty("skipInFiling")
  public void setSkipInFiling(List <SkipInFiling> skipInFiling) {
    this.skipInFiling = skipInFiling;
  }

  public SkipInFilingCollection withSkipInFiling(List <SkipInFiling> skipInFiling) {
    this.skipInFiling = skipInFiling;
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

  public SkipInFilingCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }
}
