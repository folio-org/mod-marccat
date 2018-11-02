package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"subfieldsTag"})
public class SubfieldsTagCollection {

  @JsonProperty("subfieldsTag")
  @Valid
  private List <SubfieldsTag> subfieldsTag = new ArrayList <SubfieldsTag>();

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object>();

  /**
   * @return The subfieldsTag
   */
  @JsonProperty("subfieldsTag")
  public List <SubfieldsTag> getSubfieldsTag() {
    return subfieldsTag;
  }

  /**
   * @param subfieldsTag The subfieldsTag
   */
  @JsonProperty("subfieldsTag")
  public void setSubfieldsTag(List <SubfieldsTag> subfieldsTag) {
    this.subfieldsTag = subfieldsTag;
  }

  public SubfieldsTagCollection withSubfieldsTag(List <SubfieldsTag> subfieldsTag) {
    this.subfieldsTag = subfieldsTag;
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

  public SubfieldsTagCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }
}
