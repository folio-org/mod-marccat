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
@JsonPropertyOrder({"languageTypes"})
public class LanguageTypeCollection {

  /**
   * (Required)
   */
  @JsonProperty("languageTypes")
  @Valid
  @NotNull
  private List <LanguageType> languageTypes = new ArrayList <LanguageType> ( );

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object> ( );

  /**
   * (Required)
   *
   * @return The languageTypes
   */
  @JsonProperty("languageTypes")
  public List <LanguageType> getLanguageTypes() {
    return languageTypes;
  }

  /**
   * (Required)
   *
   * @param languageTypes The languageTypes
   */
  @JsonProperty("languageTypes")
  public void setLanguageTypes(List <LanguageType> languageTypes) {
    this.languageTypes = languageTypes;
  }

  public LanguageTypeCollection withLanguageTypes(List <LanguageType> languageTypes) {
    this.languageTypes = languageTypes;
    return this;
  }

  @JsonAnyGetter
  public Map <String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put (name, value);
  }

  public LanguageTypeCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put (name, value);
    return this;
  }
}
