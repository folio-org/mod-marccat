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
@JsonPropertyOrder({"countDocuments"})
public class CountDocumentCollection {

  /**
   * (Required)
   */
  @JsonProperty("countDocuments")
  @Valid
  @NotNull
  private List <CountDocument> countDocuments = new ArrayList <CountDocument> ( );

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object> ( );


  /**
   * (Required)
   *
   * @return The headings
   */
  @JsonProperty("countDocuments")
  public List <CountDocument> getCountDocuments() {
    return countDocuments;
  }

  /**
   * (Required)
   *
   * @param indexes The indexes
   */
  @JsonProperty("countDocuments")
  public void setCountDocuments(List <CountDocument> countDocuments) {
    this.countDocuments = countDocuments;
  }

  public CountDocumentCollection withCountDocuments(List <CountDocument> countDocuments) {
    this.countDocuments = countDocuments;
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

  public CountDocumentCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put (name, value);
    return this;
  }

}
