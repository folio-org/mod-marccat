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
@JsonPropertyOrder({"materialTypes"})
public class MaterialTypeCollection {

  /**
   * (Required)
   */
  @JsonProperty("materialTypes")
  @Valid
  @NotNull
  private List <MaterialType> materialTypes = new ArrayList <MaterialType>();

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object>();

  /**
   * (Required)
   *
   * @return The materialTypes
   */
  @JsonProperty("materialTypes")
  public List <MaterialType> getMaterialTypes() {
    return materialTypes;
  }

  /**
   * (Required)
   *
   * @param materialTypes The materialTypes
   */
  @JsonProperty("materialTypes")
  public void setMaterialTypes(List <MaterialType> materialTypes) {
    this.materialTypes = materialTypes;
  }

  public MaterialTypeCollection withMaterialTypes(List <MaterialType> materialTypes) {
    this.materialTypes = materialTypes;
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

  public MaterialTypeCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }
}
