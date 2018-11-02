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
@JsonPropertyOrder({"authoritySources"})
public class AuthoritySourceCollection {

  /**
   * (Required)
   */
  @JsonProperty("authoritySources")
  @Valid
  @NotNull
  private List <AuthoritySource> authoritySources = new ArrayList <AuthoritySource>();

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object>();

  /**
   * (Required)
   *
   * @return The authoritySources
   */
  @JsonProperty("authoritySources")
  public List <AuthoritySource> getAuthoritySources() {
    return authoritySources;
  }

  /**
   * (Required)
   *
   * @param authoritySources The authoritySources
   */
  @JsonProperty("authoritySources")
  public void setAuthoritySources(List <AuthoritySource> authoritySources) {
    this.authoritySources = authoritySources;
  }

  public AuthoritySourceCollection withAuthoritySources(List <AuthoritySource> authoritySources) {
    this.authoritySources = authoritySources;
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

  public AuthoritySourceCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }
}
