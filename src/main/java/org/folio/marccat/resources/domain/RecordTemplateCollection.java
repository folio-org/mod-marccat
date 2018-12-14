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
@JsonPropertyOrder({"recordTemplates"})
public class RecordTemplateCollection {

  /**
   * (Required)
   */
  @JsonProperty("recordTemplates")
  @Valid
  @NotNull
  private List<RecordTemplate> recordTemplates = new ArrayList<RecordTemplate>();

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  /**
   * (Required)
   *
   * @return The recordTemplates
   */
  @JsonProperty("recordTemplates")
  public List<RecordTemplate> getRecordTemplates() {
    return recordTemplates;
  }

  /**
   * (Required)
   *
   * @param recordTemplates The recordTemplates
   */
  @JsonProperty("recordTemplates")
  public void setRecordTemplates(List<RecordTemplate> recordTemplates) {
    this.recordTemplates = recordTemplates;
  }

  public RecordTemplateCollection withRecordTemplates(List<RecordTemplate> recordTemplates) {
    this.recordTemplates = recordTemplates;
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

  public RecordTemplateCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }
}
