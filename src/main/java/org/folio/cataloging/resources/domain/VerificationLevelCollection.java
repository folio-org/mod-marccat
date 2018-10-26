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
@JsonPropertyOrder({"verificationLevels"})
public class VerificationLevelCollection {

  /**
   * (Required)
   */
  @JsonProperty("verificationLevels")
  @Valid
  @NotNull
  private List <VerificationLevel> verificationLevels = new ArrayList <VerificationLevel>();

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object>();

  /**
   * (Required)
   *
   * @return The verificationLevels
   */
  @JsonProperty("verificationLevels")
  public List <VerificationLevel> getVerificationLevels() {
    return verificationLevels;
  }

  /**
   * (Required)
   *
   * @param verificationLevels The verificationLevels
   */
  @JsonProperty("verificationLevels")
  public void setVerificationLevels(List <VerificationLevel> verificationLevels) {
    this.verificationLevels = verificationLevels;
  }

  public VerificationLevelCollection withVerificationLevels(List <VerificationLevel> verificationLevels) {
    this.verificationLevels = verificationLevels;
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

  public VerificationLevelCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }
}
