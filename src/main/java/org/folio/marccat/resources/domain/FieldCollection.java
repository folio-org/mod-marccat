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
@JsonPropertyOrder({"fields"})
public class FieldCollection {

  /**
   * (Required)
   */
  @JsonProperty("fields")
  @Valid
  @NotNull
  private List<Field> fields = new ArrayList<>();

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  /**
   * (Required)
   *
   * @return The fields
   */
  @JsonProperty("fields")
  public List<Field> getFields() {
    return fields;
  }

  /**
   * (Required)
   *
   * @param fields The fields
   */
  @JsonProperty("fields")
  public void setFields(List<Field> fields) {
    this.fields = fields;
  }

  public FieldCollection withFields(List<Field> fields) {
    this.fields = fields;
    return this;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

  public FieldCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }
}
