package org.folio.marccat.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import javax.validation.constraints.Size;

/**
 * Heading
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"tag", "ind1", "ind2", "categoryCode", "keyNumber", "displayValue"})
public class Heading {

  @JsonProperty("tag")
  private String tag;

  @JsonProperty("ind1")
  @Size(min = 1, max = 1)
  private String ind1;

  @JsonProperty("ind2")
  @Size(min = 1, max = 1)
  private String ind2;

  @JsonProperty("categoryCode")
  private Integer categoryCode;

  @JsonProperty("keyNumber")
  private Integer keyNumber;

  @JsonProperty("displayValue")
  private String displayValue;


  /**
   * @return The keyNumber
   */
  @JsonProperty("keyNumber")
  public Integer getKeyNumber() {
    return keyNumber;
  }

  /**
   * @param keyNumber The keyNumber
   */
  @JsonProperty("keyNumber")
  public void setKeyNumber(Integer keyNumber) {
    this.keyNumber = keyNumber;
  }

  public Heading withKeyNumber(Integer keyNumber) {
    this.keyNumber = keyNumber;
    return this;
  }

  /**
   * @return The displayValue
   */
  @JsonProperty("displayValue")
  public String getDisplayValue() {
    return displayValue;
  }

  /**
   * @param displayValue The displayValue
   */
  @JsonProperty("displayValue")
  public void setDisplayValue(String displayValue) {
    this.displayValue = displayValue;
  }

  public Heading withDisplayValue(String displayValue) {
    this.displayValue = displayValue;
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

  public Heading withTag(String tag) {
    this.tag = tag;
    return this;
  }

  /**
   * @return The ind1
   */
  @JsonProperty("ind1")
  public String getInd1() {
    return ind1;
  }

  /**
   * @param ind1 The ind1
   */
  @JsonProperty("ind1")
  public void setInd1(String ind1) {
    this.ind1 = ind1;
  }

  public Heading withInd1(String ind1) {
    this.ind1 = ind1;
    return this;
  }

  /**
   * @return The ind2
   */
  @JsonProperty("ind2")
  public String getInd2() {
    return ind2;
  }

  /**
   * @param ind2 The ind2
   */
  @JsonProperty("ind2")
  public void setInd2(String ind2) {
    this.ind2 = ind2;
  }

  public Heading withInd2(String ind2) {
    this.ind2 = ind2;
    return this;
  }

  /**
   * @return The categoryCode
   */
  @JsonProperty("categoryCode")
  public Integer getCategoryCode() {
    return categoryCode;
  }

  /**
   * @param category The categoryCode
   */
  @JsonProperty("categoryCode")
  public void setCategoryCode(Integer categoryCode) {
    this.categoryCode = categoryCode;
  }

  public Heading withCategoryCode(Integer categoryCode) {
    this.categoryCode = categoryCode;
    return this;
  }


}
