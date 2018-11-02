package org.folio.marccat.resources.domain;

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
@JsonPropertyOrder({"bibliographicLevels"})
public class BibliographicLevelCollection {

  /**
   * (Required)
   */
  @JsonProperty("bibliographicLevels")
  @Valid
  @NotNull
  private List <BibliographicLevel> bibliographicLevels = new ArrayList <BibliographicLevel>();

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object>();

  /**
   * (Required)
   *
   * @return The bibliographicLevels
   */
  @JsonProperty("bibliographicLevels")
  public List <BibliographicLevel> getBibliographicLevels() {
    return bibliographicLevels;
  }

  /**
   * (Required)
   *
   * @param bibliographicLevels The bibliographicLevels
   */
  @JsonProperty("bibliographicLevels")
  public void setBibliographicLevels(List <BibliographicLevel> bibliographicLevels) {
    this.bibliographicLevels = bibliographicLevels;
  }

  public BibliographicLevelCollection withBibliographicLevels(List <BibliographicLevel> bibliographicLevels) {
    this.bibliographicLevels = bibliographicLevels;
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

  public BibliographicLevelCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }
}
