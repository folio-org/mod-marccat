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
@JsonPropertyOrder({"diacritics"})
public class DiacriticCollection {

  /**
   * (Required)
   */
  @JsonProperty("diacritics")
  @Valid
  @NotNull
  private List <Diacritic> diacritics = new ArrayList <Diacritic>();

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object>();

  /**
   * (Required)
   *
   * @return The diacritics
   */
  @JsonProperty("diacritics")
  public List <Diacritic> getDiacritics() {
    return diacritics;
  }

  /**
   * (Required)
   *
   * @param diacritics The diacritics
   */
  @JsonProperty("diacritics")
  public void setDiacritics(List <Diacritic> diacritics) {
    this.diacritics = diacritics;
  }

  public DiacriticCollection withDiacritics(List <Diacritic> diacritics) {
    this.diacritics = diacritics;
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

  public DiacriticCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }
}
