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
public class HeadingDecoratorCollection {

  /**
   * (Required)
   */
  @JsonProperty("headings")
  @Valid
  @NotNull
  private List <HeadingDecorator> headings = new ArrayList <HeadingDecorator>();

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object>();


  /**
   * (Required)
   *
   * @return The headings
   */
  @JsonProperty("headings")
  public List <HeadingDecorator> getHeadings() {
    return headings;
  }

  /**
   * (Required)
   *
   * @param indexes The indexes
   */
  @JsonProperty("headings")
  public void setHeadings(List <HeadingDecorator> headings) {
    this.headings = headings;
  }

  public HeadingDecoratorCollection withHeadings(List <HeadingDecorator> headings) {
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

  public HeadingDecoratorCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }

}
