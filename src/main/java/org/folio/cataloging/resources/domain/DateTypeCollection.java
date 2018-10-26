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
@JsonPropertyOrder({"dateTypes"})
public class DateTypeCollection {

  /**
   * (Required)
   */
  @JsonProperty("dateTypes")
  @Valid
  @NotNull
  private List <DateType> dateTypes = new ArrayList <DateType>();

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object>();

  /**
   * (Required)
   *
   * @return The dateTypes
   */
  @JsonProperty("dateTypes")
  public List <DateType> getDateTypes() {
    return dateTypes;
  }

  /**
   * (Required)
   *
   * @param dateTypes The dateTypes
   */
  @JsonProperty("dateTypes")
  public void setDateTypes(List <DateType> dateTypes) {
    this.dateTypes = dateTypes;
  }

  public DateTypeCollection withDateTypes(List <DateType> dateTypes) {
    this.dateTypes = dateTypes;
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

  public DateTypeCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }
}
