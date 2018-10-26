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
@JsonPropertyOrder({"recordDisplayFormats"})
public class RecordDisplayFormatCollection {

  /**
   * (Required)
   */
  @JsonProperty("recordDisplayFormats")
  @Valid
  @NotNull
  private List <RecordDisplayFormat> recordDisplayFormats = new ArrayList <RecordDisplayFormat>();

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object>();

  /**
   * (Required)
   *
   * @return The recordDisplayFormats
   */
  @JsonProperty("recordDisplayFormats")
  public List <RecordDisplayFormat> getRecordDisplayFormats() {
    return recordDisplayFormats;
  }

  /**
   * (Required)
   *
   * @param recordDisplayFormats The recordDisplayFormats
   */
  @JsonProperty("recordDisplayFormats")
  public void setRecordDisplayFormats(List <RecordDisplayFormat> recordDisplayFormats) {
    this.recordDisplayFormats = recordDisplayFormats;
  }

  public RecordDisplayFormatCollection withRecordDisplayFormats(List <RecordDisplayFormat> recordDisplayFormats) {
    this.recordDisplayFormats = recordDisplayFormats;
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

  public RecordDisplayFormatCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }
}
