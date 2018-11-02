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
@JsonPropertyOrder({"headings"})
public class HeadingCollection {

  /**
   * (Required)
   */
  @JsonProperty("headings")
  @Valid
  @NotNull
  private List <Heading> headings = new ArrayList <Heading>();

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object>();


  /**
   * (Required)
   *
   * @return The headings
   */
  @JsonProperty("headings")
  public List <Heading> getHeadings() {
    return headings;
  }

  /**
   * (Required)
   *
   * @param indexes The indexes
   */
  @JsonProperty("headings")
  public void setHeadings(List <Heading> headings) {
    this.headings = headings;
  }

  public HeadingCollection withHeadings(List <Heading> headings) {
    this.headings = headings;
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

  public HeadingCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }

}
