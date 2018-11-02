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
@JsonPropertyOrder({"modifiedRecordTypes"})
public class ModifiedRecordTypeCollection {

  /**
   * (Required)
   */
  @JsonProperty("modifiedRecordTypes")
  @Valid
  @NotNull
  private List <ModifiedRecordType> modifiedRecordTypes = new ArrayList <ModifiedRecordType>();

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object>();

  /**
   * (Required)
   *
   * @return The modifiedRecordTypes
   */
  @JsonProperty("modifiedRecordTypes")
  public List <ModifiedRecordType> getModifiedRecordTypes() {
    return modifiedRecordTypes;
  }

  /**
   * (Required)
   *
   * @param modifiedRecordTypes The modifiedRecordTypes
   */
  @JsonProperty("modifiedRecordTypes")
  public void setModifiedRecordTypes(List <ModifiedRecordType> modifiedRecordTypes) {
    this.modifiedRecordTypes = modifiedRecordTypes;
  }

  public ModifiedRecordTypeCollection withModifiedRecordTypes(List <ModifiedRecordType> modifiedRecordTypes) {
    this.modifiedRecordTypes = modifiedRecordTypes;
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

  public ModifiedRecordTypeCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }
}
