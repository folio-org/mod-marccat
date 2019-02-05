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
@JsonPropertyOrder({"headingNumber", "stringText", "countNameTitle", "countAuthorities", "countDocuments", "crossReferences"})
public class HeadingDecorator {

  @JsonProperty("headingNumber")
  private Integer headingNumber;

  @JsonProperty("stringText")
  private String stringText;

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
   * @return The headingNumber
   */
  @JsonProperty("headingNumber")
  public Integer getHeadingNumber() {
    return headingNumber;
  }

  /**
   * @param headingNumber The headingNumber
   */
  @JsonProperty("headingNumber")
  public void setHeadingNumber(Integer headingNumber) {
    this.headingNumber = headingNumber;
  }

  public HeadingDecorator withHeadingNumber(Integer headingNumber) {
    this.headingNumber = headingNumber;
    return this;
  }

  /**
   * @return The stringText
   */
  @JsonProperty("stringText")
  public String getStringText() {
    return stringText;
  }

  /**
   * @param stringText The stringText
   */
  @JsonProperty("stringText")
  public void setStringText(String stringText) {
    this.stringText = stringText;
  }

  public HeadingDecorator withStringText(String stringText) {
    this.stringText = stringText;
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
