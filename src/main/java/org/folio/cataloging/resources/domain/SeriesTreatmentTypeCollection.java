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
@JsonPropertyOrder({"seriesTreatmentTypes"})
public class SeriesTreatmentTypeCollection {

  /**
   * (Required)
   */
  @JsonProperty("seriesTreatmentTypes")
  @Valid
  @NotNull
  private List <SeriesTreatmentType> seriesTreatmentTypes = new ArrayList <SeriesTreatmentType> ( );

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object> ( );

  /**
   * (Required)
   *
   * @return The seriesTreatmentTypes
   */
  @JsonProperty("seriesTreatmentTypes")
  public List <SeriesTreatmentType> getSeriesTreatmentTypes() {
    return seriesTreatmentTypes;
  }

  /**
   * (Required)
   *
   * @param seriesTreatmentTypes The seriesTreatmentTypes
   */
  @JsonProperty("seriesTreatmentTypes")
  public void setSeriesTreatmentTypes(List <SeriesTreatmentType> seriesTreatmentTypes) {
    this.seriesTreatmentTypes = seriesTreatmentTypes;
  }

  public SeriesTreatmentTypeCollection withSeriesTreatmentTypes(List <SeriesTreatmentType> seriesTreatmentTypes) {
    this.seriesTreatmentTypes = seriesTreatmentTypes;
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

  public SeriesTreatmentTypeCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put (name, value);
    return this;
  }
}
