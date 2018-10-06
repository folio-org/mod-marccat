package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Subfields tag Schema
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"category", "tag", "defaultSubfield", "subfields", "repeatable"})
public class SubfieldsTag {

  @JsonProperty("category")
  private Integer category;

  @JsonProperty("tag")
  private String tag;

  @JsonProperty("defaultSubfield")
  private String defaultSubfield;

  @JsonProperty("subfields")
  @Valid
  private List <String> subfields = new ArrayList <String> ( );

  @JsonProperty("repeatable")
  @Valid
  private List <String> repeatable = new ArrayList <String> ( );

  /**
   * @return The category
   */
  @JsonProperty("category")
  public Integer getCategory() {
    return category;
  }

  /**
   * @param category The category
   */
  @JsonProperty("category")
  public void setCategory(Integer category) {
    this.category = category;
  }

  public SubfieldsTag withCategory(Integer category) {
    this.category = category;
    return this;
  }

  /**
   * @return The tag
   */
  @JsonProperty("tag")
  public String getTag() {
    return tag;
  }

  /**
   * @param tag The tag
   */
  @JsonProperty("tag")
  public void setTag(String tag) {
    this.tag = tag;
  }

  public SubfieldsTag withTag(String tag) {
    this.tag = tag;
    return this;
  }

  /**
   * @return The defaultSubfield
   */
  @JsonProperty("defaultSubfield")
  public String getDefaultSubfield() {
    return defaultSubfield;
  }

  /**
   * @param defaultSubfield The defaultSubfield
   */
  @JsonProperty("defaultSubfield")
  public void setDefaultSubfield(String defaultSubfield) {
    this.defaultSubfield = defaultSubfield;
  }

  public SubfieldsTag withDefaultSubfield(String defaultSubfield) {
    this.defaultSubfield = defaultSubfield;
    return this;
  }

  /**
   * @return The subfields
   */
  @JsonProperty("subfields")
  public List <String> getSubfields() {
    return subfields;
  }

  /**
   * @param subfields The subfields
   */
  @JsonProperty("subfields")
  public void setSubfields(List <String> subfields) {
    this.subfields = subfields;
  }

  public SubfieldsTag withSubfields(List <String> subfields) {
    this.subfields = subfields;
    return this;
  }

  /**
   * @return The repeatable
   */
  @JsonProperty("repeatable")
  public List <String> getRepeatable() {
    return repeatable;
  }

  /**
   * @param repeatable The repeatable
   */
  @JsonProperty("repeatable")
  public void setRepeatable(List <String> repeatable) {
    this.repeatable = repeatable;
  }

  public SubfieldsTag withRepeatable(List <String> repeatable) {
    this.repeatable = repeatable;
    return this;
  }
}
