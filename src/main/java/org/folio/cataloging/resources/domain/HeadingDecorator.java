package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

/**
 * Heading Decorator
 * <p>
 *
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "headingNumber", "stringText", "database", "countNameTitle", "countCrossReferences", "countAuthorities", "countDocuments", "verificationlevel", "indexingLanguage", "accessPointlanguage"})
public class HeadingDecorator {

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

    public HeadingDecorator withHeadingNumber(Integer headingNumber) {
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

    public HeadingDecorator withStringText(String stringText) {
        this.stringText = stringText;
        return this;
    }

    /**
     *
     * @return
     * The database
     */
    @JsonProperty("database")
    public String getDatabase() {
        return database;
    }

    /**
     *
     * @param database
     * The database
     */
    @JsonProperty("database")
    public void setDatabase(String database) {
        this.database = database;
    }

    public HeadingDecorator withDatabase(String database) {
        this.database = database;
        return this;
    }

    /**
     *
     * @return
     * The countTitleNameDocuments
     */
    @JsonProperty("countTitleNameDocuments")
    public Integer getCountTitleNameDocuments() {
        return countTitleNameDocuments;
    }

    /**
     *
     * @param countTitleNameDocuments
     * The countTitleNameDocuments
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
     *
     * @return
     * The countCrossReferences
     */
    @JsonProperty("countCrossReferences")
    public Integer getCountCrossReferences() {
        return countCrossReferences;
    }

    /**
     *
     * @param countCrossReferences
     * The countCrossReferences
     */
    @JsonProperty("countCrossReferences")
    public void setCountCrossReferences(Integer countCrossReferences) {
        this.countCrossReferences = countCrossReferences;
    }

    public HeadingDecorator withCountCrossReferences(Integer countCrossReferences) {
        this.countCrossReferences = countCrossReferences;
        return this;
    }

   /**
     *
     * @return
     * The countAuthorities
     */
    @JsonProperty("countAuthorities")
    public Integer getCountAuthorities() {
        return countAuthorities;
    }

    /**
     *
     * @param countAuthorities
     * The countAuthorities
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
     *
     * @return
     * The countDocuments
     */
    @JsonProperty("countDocuments")
    public Integer getCountDocuments() {
        return countDocuments;
    }

    /**
     *
     * @param countDocuments
     * The countDocuments
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
     *
     * @return
     * The verificationlevel
     */
    @JsonProperty("verificationlevel")
    public String getVerificationlevel() {
        return verificationlevel;
    }

    /**
     *
     * @param verificationlevel
     * The verificationlevel
     */
    @JsonProperty("verificationlevel")
    public void setVerificationlevel(String verificationlevel) {
        this.verificationlevel = verificationlevel;
    }

    public HeadingDecorator withVerificationlevel(String verificationlevel) {
        this.verificationlevel = verificationlevel;
        return this;
    }

	 /**
     *
     * @return
     * The indexingLanguage
     */
    @JsonProperty("indexingLanguage")
    public String getIndexingLanguage() {
        return indexingLanguage;
    }

    /**
     *
     * @param indexingLanguage
     * The indexingLanguage
     */
    @JsonProperty("indexingLanguage")
    public void setIndexingLanguage(String indexingLanguage) {
        this.indexingLanguage = indexingLanguage;
    }

    public HeadingDecorator withIndexingLanguage(String indexingLanguage) {
        this.indexingLanguage = indexingLanguage;
        return this;
    }

    /**
     *
     * @return
     * The accessPointlanguage
     */
    @JsonProperty("accessPointlanguage")
    public String getAccessPointlanguage() {
        return accessPointlanguage;
    }

    /**
     *
     * @param accessPointlanguage
     * The accessPointlanguage
     */
    @JsonProperty("accessPointlanguage")
    public void setAccessPointlanguage(String accessPointlanguage) {
        this.accessPointlanguage = accessPointlanguage;
    }

    public HeadingDecorator withAccessPointlanguage(String accessPointlanguage) {
        this.accessPointlanguage = accessPointlanguage;
        return this;
    }




}
