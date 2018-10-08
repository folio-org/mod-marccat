package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

/**
 * Heading
 * <p>
 *
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "tag", "indicator1", "indicator2","category","headingNumber", "stringText"})
public class Heading {

  @JsonProperty("tag")
  private String tag;

  @JsonProperty("indicator1")
  private String indicator1;

  @JsonProperty("indicator2")
  private String indicator2;

  @JsonProperty("category")
  private Integer category;

  @JsonProperty("headingNumber")
  private Integer headingNumber;

  @JsonProperty("stringText")
  private String stringText;




  /**
   *
   * @return
   * The headingNumber
   */
  @JsonProperty("headingNumber")
  public Integer getHeadingNumber() {
    return headingNumber;
  }

  /**
   *
   * @param headingNumber
   * The headingNumber
   */
  @JsonProperty("headingNumber")
  public void setHeadingNumber(Integer headingNumber) {
    this.headingNumber = headingNumber;
  }

  public Heading withHeadingNumber(Integer headingNumber) {
    this.headingNumber = headingNumber;
    return this;
  }

  /**
   *
   * @return
   * The stringText
   */
  @JsonProperty("stringText")
  public String getStringText() {
    return stringText;
  }

  /**
   *
   * @param stringText
   * The stringText
   */
  @JsonProperty("stringText")
  public void setStringText(String stringText) {
    this.stringText = stringText;
  }

  public Heading withStringText(String stringText) {
    this.stringText = stringText;
    return this;
  }




  /**
   *
   * @return
   * The tag
   */
  @JsonProperty("tag")
  public String getTag() {
    return tag;
  }

  /**
   *
   * @param tag
   * The tag
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
   *
   * @return
   * The ind1
   */
  @JsonProperty("indicator1")
  public String getIndicator1() {
    return indicator1;
  }

  /**
   *
   * @param ind1
   * The ind1
   */
  @JsonProperty("indicator1")
  public void setIndicator1(String indicator1) {
    this.indicator1 = indicator1;
  }

  public Heading withIndicator1(String indicator1) {
    this.indicator1 = indicator1;
    return this;
  }

  /**
   *
   * @return
   * The ind2
   */
  @JsonProperty("indicator2")
  public String getIndicator2() {
    return indicator2;
  }

  /**
   *
   * @param ind2
   * The ind1
   */
  @JsonProperty("indicator2")
  public void setInd2(String indicator2) {
    this.indicator2 = indicator2;
  }

  public Heading withIndicator2(String indicator2) {
    this.indicator2 = indicator2;
    return this;
  }

  /**
   *
   * @return
   * The category
   */
  @JsonProperty("category")
  public Integer getCategory() {
    return category;
  }

  /**
   *
   * @param category
   * The category
   */
  @JsonProperty("category")
  public void setCategory(Integer category) {
    this.category = category;
  }

  public Heading withCategory(Integer category) {
    this.category = category;
    return this;
  }


}
