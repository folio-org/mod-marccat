package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

/**
 * Heading
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"tag", "ind1", "ind2", "category", "headingNumber", "stringText", "verificationlevel", "indexingLanguage", "accessPointlanguage"})
public class Heading {

  @JsonProperty("tag")
  private String tag;

  @JsonProperty("ind1")
  private String ind1;

  @JsonProperty("ind2")
  private String ind2;

  @JsonProperty("category")
  private Integer category;

  @JsonProperty("headingNumber")
  private Integer headingNumber;

  @JsonProperty("stringText")
  private String stringText;

  @JsonProperty("database")
  private String database;

  @JsonProperty("countTitleNameDocuments")
  private Integer countTitleNameDocuments;

  @JsonProperty("countCrossReferences")
  private Integer countCrossReferences;

  @JsonProperty("countAuthorities")
  private Integer countAuthorities;

  @JsonProperty("countDocuments")
  private Integer countDocuments;

  @JsonProperty("verificationlevel")
  private String verificationlevel;

  @JsonProperty("indexingLanguage")
  private String indexingLanguage;

  @JsonProperty("accessPointlanguage")
  private String accessPointlanguage;


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

  public Heading withHeadingNumber(Integer headingNumber) {
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

  public Heading withStringText(String stringText) {
    this.stringText = stringText;
    return this;
  }

  /**
   * @return The database
   */
  @JsonProperty("database")
  public String getDatabase() {
    return database;
  }

  /**
   * @param database The database
   */
  @JsonProperty("database")
  public void setDatabase(String database) {
    this.database = database;
  }

  public Heading withDatabase(String database) {
    this.database = database;
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

  public Heading withCountTitleNameDocuments(Integer countTitleNameDocuments) {
    this.countTitleNameDocuments = countTitleNameDocuments;
    return this;
  }

  /**
   * @return The countCrossReferences
   */
  @JsonProperty("countCrossReferences")
  public Integer getCountCrossReferences() {
    return countCrossReferences;
  }

  /**
   * @param countCrossReferences The countCrossReferences
   */
  @JsonProperty("countCrossReferences")
  public void setCountCrossReferences(Integer countCrossReferences) {
    this.countCrossReferences = countCrossReferences;
  }

  public Heading withCountCrossReferences(Integer countCrossReferences) {
    this.countCrossReferences = countCrossReferences;
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

  public Heading withCountAuthorities(Integer countAuthorities) {
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

  public Heading withCountDocuments(Integer countDocuments) {
    this.countDocuments = countDocuments;
    return this;
  }

  /**
   * @return The verificationlevel
   */
  @JsonProperty("verificationlevel")
  public String getVerificationlevel() {
    return verificationlevel;
  }

  /**
   * @param verificationlevel The verificationlevel
   */
  @JsonProperty("verificationlevel")
  public void setVerificationlevel(String verificationlevel) {
    this.verificationlevel = verificationlevel;
  }

  public Heading withVerificationlevel(String verificationlevel) {
    this.verificationlevel = verificationlevel;
    return this;
  }

  /**
   * @return The indexingLanguage
   */
  @JsonProperty("indexingLanguage")
  public String getIndexingLanguage() {
    return indexingLanguage;
  }

  /**
   * @param indexingLanguage The indexingLanguage
   */
  @JsonProperty("indexingLanguage")
  public void setIndexingLanguage(String indexingLanguage) {
    this.indexingLanguage = indexingLanguage;
  }

  public Heading withIndexingLanguage(String indexingLanguage) {
    this.indexingLanguage = indexingLanguage;
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

  public Heading withAccessPointlanguage(String accessPointlanguage) {
    this.accessPointlanguage = accessPointlanguage;
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
   * @param ind2 The ind1
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

  public Heading withCategory(Integer category) {
    this.category = category;
    return this;
  }


}
