package org.folio.marccat.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Heading Decorator
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"keyNumber", "displayValue", "countNameTitle", "countAuthorities", "countDocuments", "crossReferences"})
public class HeadingDecorator {

  @JsonProperty("keyNumber")
  private Integer keyNumber;

  @JsonProperty("displayValue")
  private String displayValue;

  @JsonProperty("countTitleNameDocuments")
  private Integer countTitleNameDocuments;

  @JsonProperty("countAuthorities")
  private Integer countAuthorities;

  @JsonProperty("countDocuments")
  private Integer countDocuments;

  @JsonProperty("accessPointlanguage")
  private String accessPointlanguage;

  @JsonProperty("crossReferences")
  @Valid
  private List<Ref> crossReferences = new ArrayList<Ref>();


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

  public HeadingDecorator withKeyNumber(Integer keyNumber) {
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
   * @param stringText The displayValue
   */
  @JsonProperty("displayValue")
  public void setDisplayValue(String stringText) {
    this.displayValue = stringText;
  }

  public HeadingDecorator withDisplayValue(String displayValue) {
    this.displayValue = displayValue;
    return this;
  }

  /**
   * @return The countTitleNameDocuments
   */
  @JsonProperty("countTitleNameDocuments")
  public Integer getCountTitleNameDocuments() {
    return countTitleNameDocuments;
  }

  /**
   * @param countTitleNameDocuments The countTitleNameDocuments
   */
  @JsonProperty("countTitleNameDocuments")
  public void setCountTitleNameDocuments(Integer countTitleNameDocuments) {
    this.countTitleNameDocuments = countTitleNameDocuments;
  }

  public HeadingDecorator withCountTitleNameDocuments(Integer countTitleNameDocuments) {
    this.countTitleNameDocuments = countTitleNameDocuments;
    return this;
  }

  /**
   * @return The countAuthorities
   */
  @JsonProperty("countAuthorities")
  public Integer getCountAuthorities() {
    return countAuthorities;
  }

  /**
   * @param countAuthorities The countAuthorities
   */
  @JsonProperty("countAuthorities")
  public void setCountAuthorities(Integer countAuthorities) {
    this.countAuthorities = countAuthorities;
  }

  public HeadingDecorator withCountAuthorities(Integer countAuthorities) {
    this.countAuthorities = countAuthorities;
    return this;
  }

  /**
   * @return The countDocuments
   */
  @JsonProperty("countDocuments")
  public Integer getCountDocuments() {
    return countDocuments;
  }

  /**
   * @param countDocuments The countDocuments
   */
  @JsonProperty("countDocuments")
  public void setCountDocuments(Integer countDocuments) {
    this.countDocuments = countDocuments;
  }

  public HeadingDecorator withCountDocuments(Integer countDocuments) {
    this.countDocuments = countDocuments;
    return this;
  }

  /**
   * @return The accessPointlanguage
   */
  @JsonProperty("accessPointlanguage")
  public String getAccessPointlanguage() {
    return accessPointlanguage;
  }

  /**
   * @param accessPointlanguage The accessPointlanguage
   */
  @JsonProperty("accessPointlanguage")
  public void setAccessPointlanguage(String accessPointlanguage) {
    this.accessPointlanguage = accessPointlanguage;
  }

  public HeadingDecorator withAccessPointlanguage(String accessPointlanguage) {
    this.accessPointlanguage = accessPointlanguage;
    return this;
  }

  /**
   * @return The cross references
   */
  @JsonProperty("crossReferences")
  public List<Ref> getCrossReferences() {
    return crossReferences;
  }

  /**
   * @param crossReferences The cross references
   */
  @JsonProperty("crossReferences")
  public void setCrossReferences(List<Ref> crossReferences) {
    this.crossReferences = crossReferences;
  }

}
