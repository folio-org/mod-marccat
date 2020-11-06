package org.folio.marccat.resources.domain;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * fixedField
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "keyNumber", "categoryCode", "description", "headerTypeCode", "code", "displayValue", "materialType", "physicalType", "dateEnteredOnFile",
    "dateTypeCode", "dateFirstPublication", "dateLastPublication", "placeOfPublication", "bookIllustrationCode1", "bookIllustrationCode2",
    "bookIllustrationCode3", "bookIllustrationCode4", "targetAudienceCode", "formOfItemCode", "natureOfEntireWork", "natureOfContent1", "natureOfContent2",
    "natureOfContent3", "natureOfContent4", "governmentPublicationCode", "conferencePublicationCode", "bookFestschrift", "bookIndexAvailabilityCode",
    "bookLiteraryFormTypeCode", "bookBiographyCode", "cartographicReliefCode1", "cartographicReliefCode2", "cartographicReliefCode3", "cartographicReliefCode4",
    "cartographicProjectionCode", "cartographicMaterial", "cartographicIndexAvailabilityCode", "cartographicFormatCode1", "cartographicFormatCode2",
    "musicFormOfCompositionCode", "musicFormatCode", "musicPartsCode", "musicTextualMaterialCode1", "musicTextualMaterialCode2", "musicTextualMaterialCode3",
    "musicTextualMaterialCode4", "musicTextualMaterialCode5", "musicTextualMaterialCode6", "musicLiteraryTextCode1", "musicLiteraryTextCode2",
    "musicTranspositionArrangementCode", "computerFileTypeCode", "computerTargetAudienceCode", "computerFileFormCode", "visualRunningTime",
    "visualMaterialTypeCode", "visualTechniqueCode", "serialFrequencyCode", "serialRegularityCode", "serialTypeCode", "serialFormOriginalItemCode",
    "serialOriginalAlphabetOfTitleCode", "serialSuccessiveLatestCode", "languageCode", "recordModifiedCode", "recordCataloguingSourceCode",
    "itemRecordStatusCode", "itemRecordTypeCode", "itemBibliographicLevelCode", "itemControlTypeCode", "characterCodingSchemeCode", "encodingLevel",
    "descriptiveCataloguingCode", "linkedRecordCode", "materialTypeCode", "categoryOfMaterial", "specificMaterialDesignationCode", "colorCode",
    "physicalMediumCode", "typeOfReproductionCode", "polarityCode", "dimensionCode", "generationCode", "baseOfFilmCode", "includesSoundCode",
    "mediumForSoundCode", "secondarySupportMaterialCode", "configurationCode", "obsolete1", "obsolete2", "imageBitDepth", "fileFormatsCode",
    "qualityAssuranceTargetCode", "antecedentSourceCode", "levelOfCompressionCode", "reformattingQualityCode", "productionDetailsCode",
    "reductionRatioRangeCode", "reductionRatioCode", "emulsionOnFilmCode", "presentationFormatCode", "productionElementsCode", "refinedCategoriesOfColorCode",
    "kindOfColorStockCode", "deteriorationStageCode", "completenessCode", "inspectionDate", "primarySupportMaterialCode", "baseOfEmulsionCode",
    "videoRecordingFormatCode", "remoteSensingDataTypeCode", "soundOnMediumOrSeparateCode", "altitudeOfSensorCode", "attitudeOfSensorCode", "cloudCoverCode",
    "platformConstructionTypeCode", "platformUseCode", "sensorTypeCode", "remoteDataTypeCode", "speedCode", "grooveWidthCode", "tapeWidthCode",
    "tapeConfigurationCode", "discTypeCode", "sndMaterialTypeCode", "cuttingTypeCode", "specialPlaybackCharacteristicsCode", "storageTechniqueCode",
    "classOfBrailleWritingCode1", "classOfBrailleWritingCode2", "levelOfContractionCode", "brailleMusicFormatCode1", "brailleMusicFormatCode2",
    "brailleMusicFormatCode3", "specificPhysicalCharacteristicsCode", "formatCode", "subjectDescriptor", "romanizationScheme", "bilingualUsage", "recordType",
    "cataloguingRules", "subjectSystem", "seriesType", "seriesNumbering", "mainAddedEntryIndicator", "subjectEntryIndicator", "seriesEntryIndicator",
    "subDivisionType", "governmentAgency", "referenceStatus", "recordRevision", "nonUniqueName", "headingStatus", "recordModification", "cataloguingSourceCode",
    "sequenceNumber", "attributes" })
public class FixedField {

  @JsonProperty("attributes")
  private Map<String, Object> attributes = new HashMap<>();

  @JsonProperty("keyNumber")
  private Integer keyNumber;

  @JsonProperty("categoryCode")
  private Integer categoryCode;

  @JsonProperty("description")
  private String description;

  @JsonProperty("headerTypeCode")
  private Integer headerTypeCode;

  @JsonProperty("code")
  private String code;

  @JsonProperty("displayValue")
  private String displayValue;

  @JsonProperty("materialType")
  private MaterialType materialType;

  @JsonProperty("physicalType")
  private PhysicalType physicalType;

  @JsonProperty("dateEnteredOnFile")
  private String dateEnteredOnFile;

  @JsonProperty("dateTypeCode")
  private String dateTypeCode;

  @JsonProperty("dateFirstPublication")
  private String dateFirstPublication;

  @JsonProperty("dateLastPublication")
  private String dateLastPublication;

  @JsonProperty("placeOfPublication")
  private String placeOfPublication;

  @JsonProperty("bookIllustrationCode1")
  private String bookIllustrationCode1;

  @JsonProperty("bookIllustrationCode2")
  private String bookIllustrationCode2;

  @JsonProperty("bookIllustrationCode3")
  private String bookIllustrationCode3;

  @JsonProperty("bookIllustrationCode4")
  private String bookIllustrationCode4;

  @JsonProperty("targetAudienceCode")
  private String targetAudienceCode;

  @JsonProperty("formOfItemCode")
  private String formOfItemCode;

  @JsonProperty("natureOfEntireWork")
  private String natureOfEntireWork;

  @JsonProperty("natureOfContent1")
  private String natureOfContent1;

  @JsonProperty("natureOfContent2")
  private String natureOfContent2;

  @JsonProperty("natureOfContent3")
  private String natureOfContent3;

  @JsonProperty("natureOfContent4")
  private String natureOfContent4;

  @JsonProperty("governmentPublicationCode")
  private String governmentPublicationCode;

  @JsonProperty("conferencePublicationCode")
  private String conferencePublicationCode;

  @JsonProperty("bookFestschrift")
  private String bookFestschrift;

  @JsonProperty("bookIndexAvailabilityCode")
  private String bookIndexAvailabilityCode;

  @JsonProperty("bookLiteraryFormTypeCode")
  private String bookLiteraryFormTypeCode;

  @JsonProperty("bookBiographyCode")
  private String bookBiographyCode;

  @JsonProperty("cartographicReliefCode1")
  private String cartographicReliefCode1;

  @JsonProperty("cartographicReliefCode2")
  private String cartographicReliefCode2;

  @JsonProperty("cartographicReliefCode3")
  private String cartographicReliefCode3;

  @JsonProperty("cartographicReliefCode4")
  private String cartographicReliefCode4;

  @JsonProperty("cartographicProjectionCode")
  private String cartographicProjectionCode;

  @JsonProperty("cartographicMaterial")
  private String cartographicMaterial;

  @JsonProperty("cartographicIndexAvailabilityCode")
  private String cartographicIndexAvailabilityCode;

  @JsonProperty("cartographicFormatCode1")
  private String cartographicFormatCode1;

  @JsonProperty("cartographicFormatCode2")
  private String cartographicFormatCode2;

  @JsonProperty("musicFormOfCompositionCode")
  private String musicFormOfCompositionCode;

  @JsonProperty("musicFormatCode")
  private String musicFormatCode;

  @JsonProperty("musicPartsCode")
  private String musicPartsCode;

  @JsonProperty("musicTextualMaterialCode1")
  private String musicTextualMaterialCode1;

  @JsonProperty("musicTextualMaterialCode2")
  private String musicTextualMaterialCode2;

  @JsonProperty("musicTextualMaterialCode3")
  private String musicTextualMaterialCode3;

  @JsonProperty("musicTextualMaterialCode4")
  private String musicTextualMaterialCode4;

  @JsonProperty("musicTextualMaterialCode5")
  private String musicTextualMaterialCode5;

  @JsonProperty("musicTextualMaterialCode6")
  private String musicTextualMaterialCode6;

  @JsonProperty("musicLiteraryTextCode1")
  private String musicLiteraryTextCode1;

  @JsonProperty("musicLiteraryTextCode2")
  private String musicLiteraryTextCode2;

  @JsonProperty("musicTranspositionArrangementCode")
  private String musicTranspositionArrangementCode;

  @JsonProperty("computerTargetAudienceCode")
  private String computerTargetAudienceCode;

  @JsonProperty("computerFileFormCode")
  private String computerFileFormCode;

  @JsonProperty("computerFileTypeCode")
  private String computerFileTypeCode;

  @JsonProperty("visualRunningTime")
  private String visualRunningTime;

  @JsonProperty("visualMaterialTypeCode")
  private String visualMaterialTypeCode;

  @JsonProperty("visualTechniqueCode")
  private String visualTechniqueCode;

  @JsonProperty("serialFrequencyCode")
  private String serialFrequencyCode;

  @JsonProperty("serialRegularityCode")
  private String serialRegularityCode;

  @JsonProperty("serialTypeCode")
  private String serialTypeCode;

  @JsonProperty("serialFormOriginalItemCode")
  private String serialFormOriginalItemCode;

  @JsonProperty("serialOriginalAlphabetOfTitleCode")
  private String serialOriginalAlphabetOfTitleCode;

  @JsonProperty("serialSuccessiveLatestCode")
  private String serialSuccessiveLatestCode;

  @JsonProperty("languageCode")
  private String languageCode;

  @JsonProperty("recordModifiedCode")
  private String recordModifiedCode;

  @JsonProperty("recordCataloguingSourceCode")
  private String recordCataloguingSourceCode;

  @JsonProperty("itemRecordStatusCode")
  private String itemRecordStatusCode;

  @JsonProperty("itemRecordTypeCode")
  private String itemRecordTypeCode;

  @JsonProperty("itemBibliographicLevelCode")
  private String itemBibliographicLevelCode;

  @JsonProperty("itemControlTypeCode")
  private String itemControlTypeCode;

  @JsonProperty("characterCodingSchemeCode")
  private String characterCodingSchemeCode;

  @JsonProperty("encodingLevel")
  private String encodingLevel;

  @JsonProperty("descriptiveCataloguingCode")
  private String descriptiveCataloguingCode;

  @JsonProperty("linkedRecordCode")
  private String linkedRecordCode;

  @JsonProperty("materialTypeCode")
  private String materialTypeCode;

  @JsonProperty("categoryOfMaterial")
  private String categoryOfMaterial;

  @JsonProperty("specificMaterialDesignationCode")
  private String specificMaterialDesignationCode;

  @JsonProperty("colorCode")
  private String colorCode;

  @JsonProperty("physicalMediumCode")
  private String physicalMediumCode;

  @JsonProperty("typeOfReproductionCode")
  private String typeOfReproductionCode;

  @JsonProperty("polarityCode")
  private String polarityCode;

  @JsonProperty("dimensionCode")
  private String dimensionCode;

  @JsonProperty("generationCode")
  private String generationCode;

  @JsonProperty("baseOfFilmCode")
  private String baseOfFilmCode;

  @JsonProperty("includesSoundCode")
  private String includesSoundCode;

  @JsonProperty("mediumForSoundCode")
  private String mediumForSoundCode;

  @JsonProperty("secondarySupportMaterialCode")
  private String secondarySupportMaterialCode;

  @JsonProperty("configurationCode")
  private String configurationCode;

  @JsonProperty("obsolete1")
  private String obsolete1;

  @JsonProperty("obsolete2")
  private String obsolete2;

  @JsonProperty("imageBitDepth")
  private String imageBitDepth;

  @JsonProperty("fileFormatsCode")
  private String fileFormatsCode;

  @JsonProperty("qualityAssuranceTargetCode")
  private String qualityAssuranceTargetCode;

  @JsonProperty("antecedentSourceCode")
  private String antecedentSourceCode;

  @JsonProperty("levelOfCompressionCode")
  private String levelOfCompressionCode;

  @JsonProperty("reformattingQualityCode")
  private String reformattingQualityCode;

  @JsonProperty("productionDetailsCode")
  private String productionDetailsCode;

  @JsonProperty("reductionRatioRangeCode")
  private String reductionRatioRangeCode;

  @JsonProperty("reductionRatioCode")
  private String reductionRatioCode;

  @JsonProperty("emulsionOnFilmCode")
  private String emulsionOnFilmCode;

  @JsonProperty("presentationFormatCode")
  private String presentationFormatCode;

  @JsonProperty("productionElementsCode")
  private String productionElementsCode;

  @JsonProperty("refinedCategoriesOfColorCode")
  private String refinedCategoriesOfColorCode;

  @JsonProperty("kindOfColorStockCode")
  private String kindOfColorStockCode;

  @JsonProperty("deteriorationStageCode")
  private String deteriorationStageCode;

  @JsonProperty("completenessCode")
  private String completenessCode;

  @JsonProperty("inspectionDate")
  private String inspectionDate;

  @JsonProperty("primarySupportMaterialCode")
  private String primarySupportMaterialCode;

  @JsonProperty("baseOfEmulsionCode")
  private String baseOfEmulsionCode;

  @JsonProperty("videoRecordingFormatCode")
  private String videoRecordingFormatCode;

  @JsonProperty("remoteSensingDataTypeCode")
  private String remoteSensingDataTypeCode;

  @JsonProperty("soundOnMediumOrSeparateCode")
  private String soundOnMediumOrSeparateCode;

  @JsonProperty("altitudeOfSensorCode")
  private String altitudeOfSensorCode;

  @JsonProperty("attitudeOfSensorCode")
  private String attitudeOfSensorCode;

  @JsonProperty("cloudCoverCode")
  private String cloudCoverCode;

  @JsonProperty("platformConstructionTypeCode")
  private String platformConstructionTypeCode;

  @JsonProperty("platformUseCode")
  private String platformUseCode;

  @JsonProperty("sensorTypeCode")
  private String sensorTypeCode;

  @JsonProperty("remoteDataTypeCode")
  private String remoteDataTypeCode;

  @JsonProperty("speedCode")
  private String speedCode;

  @JsonProperty("grooveWidthCode")
  private String grooveWidthCode;

  @JsonProperty("tapeWidthCode")
  private String tapeWidthCode;

  @JsonProperty("tapeConfigurationCode")
  private String tapeConfigurationCode;

  @JsonProperty("discTypeCode")
  private String discTypeCode;

  @JsonProperty("sndMaterialTypeCode")
  private String sndMaterialTypeCode;

  @JsonProperty("cuttingTypeCode")
  private String cuttingTypeCode;

  @JsonProperty("specialPlaybackCharacteristicsCode")
  private String specialPlaybackCharacteristicsCode;

  @JsonProperty("storageTechniqueCode")
  private String storageTechniqueCode;

  @JsonProperty("classOfBrailleWritingCode1")
  private String classOfBrailleWritingCode1;

  @JsonProperty("classOfBrailleWritingCode2")
  private String classOfBrailleWritingCode2;

  @JsonProperty("levelOfContractionCode")
  private String levelOfContractionCode;

  @JsonProperty("brailleMusicFormatCode1")
  private String brailleMusicFormatCode1;

  @JsonProperty("brailleMusicFormatCode2")
  private String brailleMusicFormatCode2;

  @JsonProperty("brailleMusicFormatCode3")
  private String brailleMusicFormatCode3;

  @JsonProperty("specificPhysicalCharacteristicsCode")
  private String specificPhysicalCharacteristicsCode;

  @JsonProperty("formatCode")
  private String formatCode;

  private String subjectDescriptor;
  private String romanizationScheme;
  private String bilingualUsage;
  private String recordType;
  private String cataloguingRules;
  private String subjectSystem;
  private String seriesType;
  private String seriesNumbering;
  private String mainAddedEntryIndicator;
  private String subjectEntryIndicator;
  private String seriesEntryIndicator;
  private String subDivisionType;
  private String governmentAgency;
  private String referenceStatus;
  private String recordRevision;
  private String nonUniqueName;
  private String headingStatus;
  private String recordModification;
  private String cataloguingSourceCode;

  @JsonProperty("sequenceNumber")
  private int sequenceNumber;

  /**
   * @return The headerTypeCode
   */
  @JsonProperty("headerTypeCode")
  public Integer getHeaderTypeCode() {
    return headerTypeCode;
  }

  /**
   * @param headerTypeCode The headerTypeCode
   */
  @JsonProperty("headerTypeCode")
  public void setHeaderTypeCode(Integer headerTypeCode) {
    this.headerTypeCode = headerTypeCode;
  }

  public FixedField withHeaderTypeCode(Integer headerTypeCode) {
    this.headerTypeCode = headerTypeCode;
    return this;
  }

  /**
   * @return The code
   */
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  /**
   * @param code The code
   */
  @JsonProperty("code")
  public void setCode(String code) {
    this.code = code;
  }

  public FixedField withCode(String code) {
    this.code = code;
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

  public FixedField withDisplayValue(String displayValue) {
    this.displayValue = displayValue;
    return this;
  }

  /**
   * @return The materialType
   */
  @JsonProperty("materialType")
  public MaterialType getMaterialType() {
    return materialType;
  }

  /**
   * @param materialType The materialType
   */
  @JsonProperty("materialType")
  public void setMaterialType(MaterialType materialType) {
    this.materialType = materialType;
    this.attributes.put("materialType", materialType);
  }

  public FixedField withMaterialType(MaterialType materialType) {
    this.materialType = materialType;
    return this;
  }

  /**
   * @return The physicalType
   */
  @JsonProperty("physicalType")
  public PhysicalType getPhysicalType() {
    return physicalType;
  }

  /**
   * @param physicalType The physicalType
   */
  @JsonProperty("physicalType")
  public void setPhysicalType(PhysicalType physicalType) {
    this.physicalType = physicalType;
  }

  public FixedField withPhysicalType(PhysicalType physicalType) {
    this.physicalType = physicalType;
    this.attributes.put("physicalType", physicalType);
    return this;
  }

  /**
   * @return The dateEnteredOnFile
   */
  @JsonProperty("dateEnteredOnFile")
  public String getDateEnteredOnFile() {
    return dateEnteredOnFile;
  }

  /**
   * @param dateEnteredOnFile The dateEnteredOnFile
   */
  @JsonProperty("dateEnteredOnFile")
  public void setDateEnteredOnFile(String dateEnteredOnFile) {
    this.attributes.put("dateEnteredOnFile", dateEnteredOnFile);
    this.dateEnteredOnFile = dateEnteredOnFile;
  }

  public FixedField withDateEnteredOnFile(String dateEnteredOnFile) {
    this.attributes.put("dateEnteredOnFile", dateEnteredOnFile);
    this.dateEnteredOnFile = dateEnteredOnFile;
    return this;
  }

  /**
   * @return The dateTypeCode
   */
  @JsonProperty("dateTypeCode")
  public String getDateTypeCode() {
    return dateTypeCode;
  }

  /**
   * @param dateTypeCode The dateTypeCode
   */
  @JsonProperty("dateTypeCode")
  public void setDateTypeCode(String dateTypeCode) {
    this.attributes.put("dateTypeCode", dateTypeCode);
    this.dateTypeCode = dateTypeCode;
  }

  public FixedField withDateTypeCode(String dateTypeCode) {
    this.attributes.put("dateTypeCode", dateTypeCode);
    this.dateTypeCode = dateTypeCode;
    return this;
  }

  /**
   * @return The dateFirstPublication
   */
  @JsonProperty("dateFirstPublication")
  public String getDateFirstPublication() {
    return dateFirstPublication;
  }

  /**
   * @param dateFirstPublication The dateFirstPublication
   */
  @JsonProperty("dateFirstPublication")
  public void setDateFirstPublication(String dateFirstPublication) {
    this.attributes.put("dateFirstPublication", dateFirstPublication);
    this.dateFirstPublication = dateFirstPublication;
  }

  public FixedField withDateFirstPublication(String dateFirstPublication) {
    this.attributes.put("dateFirstPublication", dateFirstPublication);
    this.dateFirstPublication = dateFirstPublication;
    return this;
  }

  /**
   * @return The dateLastPublication
   */
  @JsonProperty("dateLastPublication")
  public String getDateLastPublication() {
    return dateLastPublication;
  }

  /**
   * @param dateLastPublication The dateLastPublication
   */
  @JsonProperty("dateLastPublication")
  public void setDateLastPublication(String dateLastPublication) {
    this.attributes.put("dateLastPublication", dateLastPublication);
    this.dateLastPublication = dateLastPublication;
  }

  public FixedField withDateLastPublication(String dateLastPublication) {
    this.attributes.put("dateLastPublication", dateLastPublication);
    this.dateLastPublication = dateLastPublication;
    return this;
  }

  /**
   * @return The placeOfPublication
   */
  @JsonProperty("placeOfPublication")
  public String getPlaceOfPublication() {
    return placeOfPublication;
  }

  /**
   * @param placeOfPublication The placeOfPublication
   */
  @JsonProperty("placeOfPublication")
  public void setPlaceOfPublication(String placeOfPublication) {
    this.attributes.put("placeOfPublication", placeOfPublication);
    this.placeOfPublication = placeOfPublication;
  }

  public FixedField withPlaceOfPublication(String placeOfPublication) {
    this.attributes.put("placeOfPublication", placeOfPublication);
    this.placeOfPublication = placeOfPublication;
    return this;
  }

  /**
   * @return The bookIllustrationCode1
   */
  @JsonProperty("bookIllustrationCode1")
  public String getBookIllustrationCode1() {
    return bookIllustrationCode1;
  }

  /**
   * @param bookIllustrationCode1 The bookIllustrationCode1
   */
  @JsonProperty("bookIllustrationCode1")
  public void setBookIllustrationCode1(String bookIllustrationCode1) {
    this.attributes.put("bookIllustrationCode1", bookIllustrationCode1);
    this.bookIllustrationCode1 = bookIllustrationCode1;
  }

  public FixedField withBookIllustrationCode1(String bookIllustrationCode1) {
    this.attributes.put("bookIllustrationCode1", bookIllustrationCode1);
    this.bookIllustrationCode1 = bookIllustrationCode1;
    return this;
  }

  /**
   * @return The bookIllustrationCode2
   */
  @JsonProperty("bookIllustrationCode2")
  public String getBookIllustrationCode2() {
    return bookIllustrationCode2;
  }

  /**
   * @param bookIllustrationCode2 The bookIllustrationCode2
   */
  @JsonProperty("bookIllustrationCode2")
  public void setBookIllustrationCode2(String bookIllustrationCode2) {
    this.attributes.put("bookIllustrationCode2", bookIllustrationCode2);
    this.bookIllustrationCode2 = bookIllustrationCode2;
  }

  public FixedField withBookIllustrationCode2(String bookIllustrationCode2) {
    this.attributes.put("bookIllustrationCode2", bookIllustrationCode2);
    this.bookIllustrationCode2 = bookIllustrationCode2;
    return this;
  }

  /**
   * @return The bookIllustrationCode3
   */
  @JsonProperty("bookIllustrationCode3")
  public String getBookIllustrationCode3() {
    return bookIllustrationCode3;
  }

  /**
   * @param bookIllustrationCode3 The bookIllustrationCode3
   */
  @JsonProperty("bookIllustrationCode3")
  public void setBookIllustrationCode3(String bookIllustrationCode3) {
    this.attributes.put("bookIllustrationCode3", bookIllustrationCode3);
    this.bookIllustrationCode3 = bookIllustrationCode3;
  }

  public FixedField withBookIllustrationCode3(String bookIllustrationCode3) {
    this.attributes.put("bookIllustrationCode3", bookIllustrationCode3);
    this.bookIllustrationCode3 = bookIllustrationCode3;
    return this;
  }

  /**
   * @return The bookIllustrationCode4
   */
  @JsonProperty("bookIllustrationCode4")
  public String getBookIllustrationCode4() {
    return bookIllustrationCode4;
  }

  /**
   * @param bookIllustrationCode4 The bookIllustrationCode4
   */
  @JsonProperty("bookIllustrationCode4")
  public void setBookIllustrationCode4(String bookIllustrationCode4) {
    this.attributes.put("bookIllustrationCode4", bookIllustrationCode4);
    this.bookIllustrationCode4 = bookIllustrationCode4;
  }

  public FixedField withBookIllustrationCode4(String bookIllustrationCode4) {
    this.attributes.put("bookIllustrationCode4", bookIllustrationCode4);
    this.bookIllustrationCode4 = bookIllustrationCode4;
    return this;
  }

  /**
   * @return The targetAudienceCode
   */
  @JsonProperty("targetAudienceCode")
  public String getTargetAudienceCode() {
    return targetAudienceCode;
  }

  /**
   * @param targetAudienceCode The targetAudienceCode
   */
  @JsonProperty("targetAudienceCode")
  public void setTargetAudienceCode(String targetAudienceCode) {
    this.attributes.put("targetAudienceCode", targetAudienceCode);
    this.targetAudienceCode = targetAudienceCode;
  }

  public FixedField withTargetAudienceCode(String targetAudienceCode) {
    this.attributes.put("targetAudienceCode", targetAudienceCode);
    this.targetAudienceCode = targetAudienceCode;
    return this;
  }

  /**
   * @return The formOfItemCode
   */
  @JsonProperty("formOfItemCode")
  public String getFormOfItemCode() {
    return formOfItemCode;
  }

  /**
   * @param formOfItemCode The formOfItemCode
   */
  @JsonProperty("formOfItemCode")
  public void setFormOfItemCode(String formOfItemCode) {
    this.attributes.put("formOfItemCode", formOfItemCode);
    this.formOfItemCode = formOfItemCode;
  }

  public FixedField withFormOfItemCode(String formOfItemCode) {
    this.attributes.put("formOfItemCode", formOfItemCode);
    this.formOfItemCode = formOfItemCode;
    return this;
  }

  /**
   * @return The natureOfEntireWork
   */
  @JsonProperty("natureOfEntireWork")
  public String getNatureOfEntireWork() {
    return natureOfEntireWork;
  }

  /**
   * @param natureOfEntireWork The natureOfEntireWork
   */
  @JsonProperty("natureOfEntireWork")
  public void setNatureOfEntireWork(String natureOfEntireWork) {
    this.attributes.put("natureOfEntireWork", natureOfEntireWork);
    this.natureOfEntireWork = natureOfEntireWork;
  }

  public FixedField withNatureOfEntireWork(String natureOfEntireWork) {
    this.attributes.put("natureOfEntireWork", natureOfEntireWork);
    this.natureOfEntireWork = natureOfEntireWork;
    return this;
  }

  /**
   * @return The natureOfContent1
   */
  @JsonProperty("natureOfContent1")
  public String getNatureOfContent1() {
    return natureOfContent1;
  }

  /**
   * @param natureOfContent1 The natureOfContent1
   */
  @JsonProperty("natureOfContent1")
  public void setNatureOfContent1(String natureOfContent1) {
    this.attributes.put("natureOfContent1", natureOfContent1);
    this.natureOfContent1 = natureOfContent1;
  }

  public FixedField withNatureOfContent1(String natureOfContent1) {
    this.attributes.put("natureOfContent1", natureOfContent1);
    this.natureOfContent1 = natureOfContent1;
    return this;
  }

  /**
   * @return The natureOfContent2
   */
  @JsonProperty("natureOfContent2")
  public String getNatureOfContent2() {
    return natureOfContent2;
  }

  /**
   * @param natureOfContent2 The natureOfContent2
   */
  @JsonProperty("natureOfContent2")
  public void setNatureOfContent2(String natureOfContent2) {
    this.attributes.put("natureOfContent2", natureOfContent2);
    this.natureOfContent2 = natureOfContent2;
  }

  public FixedField withNatureOfContent2(String natureOfContent2) {
    this.attributes.put("natureOfContent2", natureOfContent2);
    this.natureOfContent2 = natureOfContent2;
    return this;
  }

  /**
   * @return The natureOfContent3
   */
  @JsonProperty("natureOfContent3")
  public String getNatureOfContent3() {
    return natureOfContent3;
  }

  /**
   * @param natureOfContent3 The natureOfContent3
   */
  @JsonProperty("natureOfContent3")
  public void setNatureOfContent3(String natureOfContent3) {
    this.attributes.put("natureOfContent3", natureOfContent3);
    this.natureOfContent3 = natureOfContent3;
  }

  public FixedField withNatureOfContent3(String natureOfContent3) {
    this.attributes.put("natureOfContent3", natureOfContent3);
    this.natureOfContent3 = natureOfContent3;
    return this;
  }

  /**
   * @return The natureOfContent4
   */
  @JsonProperty("natureOfContent4")
  public String getNatureOfContent4() {
    return natureOfContent4;
  }

  /**
   * @param natureOfContent4 The natureOfContent4
   */
  @JsonProperty("natureOfContent4")
  public void setNatureOfContent4(String natureOfContent4) {
    this.attributes.put("natureOfContent4", natureOfContent4);
    this.natureOfContent4 = natureOfContent4;
  }

  public FixedField withNatureOfContent4(String natureOfContent4) {
    this.attributes.put("natureOfContent4", natureOfContent4);
    this.natureOfContent4 = natureOfContent4;
    return this;
  }

  /**
   * @return The governmentPublicationCode
   */
  @JsonProperty("governmentPublicationCode")
  public String getGovernmentPublicationCode() {
    return governmentPublicationCode;
  }

  /**
   * @param governmentPublicationCode The governmentPublicationCode
   */
  @JsonProperty("governmentPublicationCode")
  public void setGovernmentPublicationCode(String governmentPublicationCode) {
    this.attributes.put("governmentPublicationCode", governmentPublicationCode);
    this.governmentPublicationCode = governmentPublicationCode;
  }

  public FixedField withGovernmentPublicationCode(String governmentPublicationCode) {
    this.attributes.put("governmentPublicationCode", governmentPublicationCode);
    this.governmentPublicationCode = governmentPublicationCode;
    return this;
  }

  /**
   * @return The conferencePublicationCode
   */
  @JsonProperty("conferencePublicationCode")
  public String getConferencePublicationCode() {
    return conferencePublicationCode;
  }

  /**
   * @param conferencePublicationCode The conferencePublicationCode
   */
  @JsonProperty("conferencePublicationCode")
  public void setConferencePublicationCode(String conferencePublicationCode) {
    this.attributes.put("conferencePublicationCode", conferencePublicationCode);
    this.conferencePublicationCode = conferencePublicationCode;
  }

  public FixedField withConferencePublicationCode(String conferencePublicationCode) {
    this.attributes.put("conferencePublicationCode", conferencePublicationCode);
    this.conferencePublicationCode = conferencePublicationCode;
    return this;
  }

  /**
   * @return The bookFestschrift
   */
  @JsonProperty("bookFestschrift")
  public String getBookFestschrift() {
    return bookFestschrift;
  }

  /**
   * @param bookFestschrift The bookFestschrift
   */
  @JsonProperty("bookFestschrift")
  public void setBookFestschrift(String bookFestschrift) {
    this.attributes.put("bookFestschrift", bookFestschrift);
    this.bookFestschrift = bookFestschrift;
  }

  public FixedField withBookFestschrift(String bookFestschrift) {
    this.attributes.put("bookFestschrift", bookFestschrift);
    this.bookFestschrift = bookFestschrift;
    return this;
  }

  /**
   * @return The bookIndexAvailabilityCode
   */
  @JsonProperty("bookIndexAvailabilityCode")
  public String getBookIndexAvailabilityCode() {
    return bookIndexAvailabilityCode;
  }

  /**
   * @param bookIndexAvailabilityCode The bookIndexAvailabilityCode
   */
  @JsonProperty("bookIndexAvailabilityCode")
  public void setBookIndexAvailabilityCode(String bookIndexAvailabilityCode) {
    this.attributes.put("bookIndexAvailabilityCode", bookIndexAvailabilityCode);
    this.bookIndexAvailabilityCode = bookIndexAvailabilityCode;
  }

  public FixedField withBookIndexAvailabilityCode(String bookIndexAvailabilityCode) {
    this.attributes.put("bookIndexAvailabilityCode", bookIndexAvailabilityCode);
    this.bookIndexAvailabilityCode = bookIndexAvailabilityCode;
    return this;
  }

  /**
   * @return The bookLiteraryFormTypeCode
   */
  @JsonProperty("bookLiteraryFormTypeCode")
  public String getBookLiteraryFormTypeCode() {
    return bookLiteraryFormTypeCode;
  }

  /**
   * @param bookLiteraryFormTypeCode The bookLiteraryFormTypeCode
   */
  @JsonProperty("bookLiteraryFormTypeCode")
  public void setBookLiteraryFormTypeCode(String bookLiteraryFormTypeCode) {
    this.attributes.put("bookLiteraryFormTypeCode", bookLiteraryFormTypeCode);
    this.bookLiteraryFormTypeCode = bookLiteraryFormTypeCode;
  }

  public FixedField withBookLiteraryFormTypeCode(String bookLiteraryFormTypeCode) {
    this.attributes.put("bookLiteraryFormTypeCode", bookLiteraryFormTypeCode);
    this.bookLiteraryFormTypeCode = bookLiteraryFormTypeCode;
    return this;
  }

  /**
   * @return The bookBiographyCode
   */
  @JsonProperty("bookBiographyCode")
  public String getBookBiographyCode() {
    return bookBiographyCode;
  }

  /**
   * @param bookBiographyCode The bookBiographyCode
   */
  @JsonProperty("bookBiographyCode")
  public void setBookBiographyCode(String bookBiographyCode) {
    this.attributes.put("bookBiographyCode", bookBiographyCode);
    this.bookBiographyCode = bookBiographyCode;
  }

  public FixedField withBookBiographyCode(String bookBiographyCode) {
    this.attributes.put("bookBiographyCode", bookBiographyCode);
    this.bookBiographyCode = bookBiographyCode;
    return this;
  }

  /**
   * @return The cartographicReliefCode1
   */
  @JsonProperty("cartographicReliefCode1")
  public String getCartographicReliefCode1() {
    return cartographicReliefCode1;
  }

  /**
   * @param cartographicReliefCode1 The cartographicReliefCode1
   */
  @JsonProperty("cartographicReliefCode1")
  public void setCartographicReliefCode1(String cartographicReliefCode1) {
    this.attributes.put("cartographicReliefCode1", cartographicReliefCode1);
    this.cartographicReliefCode1 = cartographicReliefCode1;
  }

  public FixedField withCartographicReliefCode1(String cartographicReliefCode1) {
    this.attributes.put("cartographicReliefCode1", cartographicReliefCode1);
    this.cartographicReliefCode1 = cartographicReliefCode1;
    return this;
  }

  /**
   * @return The cartographicReliefCode2
   */
  @JsonProperty("cartographicReliefCode2")
  public String getCartographicReliefCode2() {
    return cartographicReliefCode2;
  }

  /**
   * @param cartographicReliefCode2 The cartographicReliefCode2
   */
  @JsonProperty("cartographicReliefCode2")
  public void setCartographicReliefCode2(String cartographicReliefCode2) {
    this.attributes.put("cartographicReliefCode2", cartographicReliefCode2);
    this.cartographicReliefCode2 = cartographicReliefCode2;
  }

  public FixedField withCartographicReliefCode2(String cartographicReliefCode2) {
    this.attributes.put("cartographicReliefCode2", cartographicReliefCode2);
    this.cartographicReliefCode2 = cartographicReliefCode2;
    return this;
  }

  /**
   * @return The cartographicReliefCode3
   */
  @JsonProperty("cartographicReliefCode3")
  public String getCartographicReliefCode3() {
    return cartographicReliefCode3;
  }

  /**
   * @param cartographicReliefCode3 The cartographicReliefCode3
   */
  @JsonProperty("cartographicReliefCode3")
  public void setCartographicReliefCode3(String cartographicReliefCode3) {
    this.attributes.put("cartographicReliefCode3", cartographicReliefCode3);
    this.cartographicReliefCode3 = cartographicReliefCode3;
  }

  public FixedField withCartographicReliefCode3(String cartographicReliefCode3) {
    this.attributes.put("cartographicReliefCode3", cartographicReliefCode3);
    this.cartographicReliefCode3 = cartographicReliefCode3;
    return this;
  }

  /**
   * @return The cartographicReliefCode4
   */
  @JsonProperty("cartographicReliefCode4")
  public String getCartographicReliefCode4() {
    return cartographicReliefCode4;
  }

  /**
   * @param cartographicReliefCode4 The cartographicReliefCode4
   */
  @JsonProperty("cartographicReliefCode4")
  public void setCartographicReliefCode4(String cartographicReliefCode4) {
    this.attributes.put("cartographicReliefCode4", cartographicReliefCode4);
    this.cartographicReliefCode4 = cartographicReliefCode4;
  }

  public FixedField withCartographicReliefCode4(String cartographicReliefCode4) {
    this.attributes.put("cartographicReliefCode4", cartographicReliefCode4);
    this.cartographicReliefCode4 = cartographicReliefCode4;
    return this;
  }

  /**
   * @return The cartographicProjectionCode
   */
  @JsonProperty("cartographicProjectionCode")
  public String getCartographicProjectionCode() {
    return cartographicProjectionCode;
  }

  /**
   * @param cartographicProjectionCode The cartographicProjectionCode
   */
  @JsonProperty("cartographicProjectionCode")
  public void setCartographicProjectionCode(String cartographicProjectionCode) {
    this.attributes.put("cartographicProjectionCode", cartographicProjectionCode);
    this.cartographicProjectionCode = cartographicProjectionCode;
  }

  public FixedField withCartographicProjectionCode(String cartographicProjectionCode) {
    this.attributes.put("cartographicProjectionCode", cartographicProjectionCode);
    this.cartographicProjectionCode = cartographicProjectionCode;
    return this;
  }

  /**
   * @return The cartographicMaterial
   */
  @JsonProperty("cartographicMaterial")
  public String getCartographicMaterial() {
    return cartographicMaterial;
  }

  /**
   * @param cartographicMaterial The cartographicMaterial
   */
  @JsonProperty("cartographicMaterial")
  public void setCartographicMaterial(String cartographicMaterial) {
    this.attributes.put("cartographicMaterial", cartographicMaterial);
    this.cartographicMaterial = cartographicMaterial;
  }

  public FixedField withCartographicMaterial(String cartographicMaterial) {
    this.attributes.put("cartographicMaterial", cartographicMaterial);
    this.cartographicMaterial = cartographicMaterial;
    return this;
  }

  /**
   * @return The cartographicIndexAvailabilityCode
   */
  @JsonProperty("cartographicIndexAvailabilityCode")
  public String getCartographicIndexAvailabilityCode() {
    return cartographicIndexAvailabilityCode;
  }

  /**
   * @param cartographicIndexAvailabilityCode The
   *        cartographicIndexAvailabilityCode
   */
  @JsonProperty("cartographicIndexAvailabilityCode")
  public void setCartographicIndexAvailabilityCode(String cartographicIndexAvailabilityCode) {
    this.attributes.put("cartographicIndexAvailabilityCode", cartographicIndexAvailabilityCode);
    this.cartographicIndexAvailabilityCode = cartographicIndexAvailabilityCode;
  }

  public FixedField withCartographicIndexAvailabilityCode(String cartographicIndexAvailabilityCode) {
    this.attributes.put("cartographicIndexAvailabilityCode", cartographicIndexAvailabilityCode);
    this.cartographicIndexAvailabilityCode = cartographicIndexAvailabilityCode;
    return this;
  }

  /**
   * @return The cartographicFormatCode1
   */
  @JsonProperty("cartographicFormatCode1")
  public String getCartographicFormatCode1() {
    return cartographicFormatCode1;
  }

  /**
   * @param cartographicFormatCode1 The cartographicFormatCode1
   */
  @JsonProperty("cartographicFormatCode1")
  public void setCartographicFormatCode1(String cartographicFormatCode1) {
    this.attributes.put("cartographicFormatCode1", cartographicFormatCode1);
    this.cartographicFormatCode1 = cartographicFormatCode1;
  }

  public FixedField withCartographicFormatCode1(String cartographicFormatCode1) {
    this.attributes.put("cartographicFormatCode1", cartographicFormatCode1);
    this.cartographicFormatCode1 = cartographicFormatCode1;
    return this;
  }

  /**
   * @return The cartographicFormatCode2
   */
  @JsonProperty("cartographicFormatCode2")
  public String getCartographicFormatCode2() {
    return cartographicFormatCode2;
  }

  /**
   * @param cartographicFormatCode2 The cartographicFormatCode2
   */
  @JsonProperty("cartographicFormatCode2")
  public void setCartographicFormatCode2(String cartographicFormatCode2) {
    this.attributes.put("cartographicFormatCode2", cartographicFormatCode2);
    this.cartographicFormatCode2 = cartographicFormatCode2;
  }

  public FixedField withCartographicFormatCode2(String cartographicFormatCode2) {
    this.attributes.put("cartographicFormatCode2", cartographicFormatCode2);
    this.cartographicFormatCode2 = cartographicFormatCode2;
    return this;
  }

  /**
   * @return The musicFormOfCompositionCode
   */
  @JsonProperty("musicFormOfCompositionCode")
  public String getMusicFormOfCompositionCode() {
    return musicFormOfCompositionCode;
  }

  /**
   * @param musicFormOfCompositionCode The musicFormOfCompositionCode
   */
  @JsonProperty("musicFormOfCompositionCode")
  public void setMusicFormOfCompositionCode(String musicFormOfCompositionCode) {
    this.attributes.put("musicFormOfCompositionCode", musicFormOfCompositionCode);
    this.musicFormOfCompositionCode = musicFormOfCompositionCode;
  }

  public FixedField withMusicFormOfCompositionCode(String musicFormOfCompositionCode) {
    this.attributes.put("musicFormOfCompositionCode", musicFormOfCompositionCode);
    this.musicFormOfCompositionCode = musicFormOfCompositionCode;
    return this;
  }

  /**
   * @return The musicFormatCode
   */
  @JsonProperty("musicFormatCode")
  public String getMusicFormatCode() {
    return musicFormatCode;
  }

  /**
   * @param musicFormatCode The musicFormatCode
   */
  @JsonProperty("musicFormatCode")
  public void setMusicFormatCode(String musicFormatCode) {
    this.attributes.put("musicFormatCode", musicFormatCode);
    this.musicFormatCode = musicFormatCode;
  }

  public FixedField withMusicFormatCode(String musicFormatCode) {
    this.attributes.put("musicFormatCode", musicFormatCode);
    this.musicFormatCode = musicFormatCode;
    return this;
  }

  /**
   * @return The musicPartsCode
   */
  @JsonProperty("musicPartsCode")
  public String getMusicPartsCode() {
    return musicPartsCode;
  }

  /**
   * @param musicPartsCode The musicPartsCode
   */
  @JsonProperty("musicPartsCode")
  public void setMusicPartsCode(String musicPartsCode) {
    this.attributes.put("musicPartsCode", musicPartsCode);
    this.musicPartsCode = musicPartsCode;
  }

  public FixedField withMusicPartsCode(String musicPartsCode) {
    this.attributes.put("musicPartsCode", musicPartsCode);
    this.musicPartsCode = musicPartsCode;
    return this;
  }

  /**
   * @return The musicTextualMaterialCode1
   */
  @JsonProperty("musicTextualMaterialCode1")
  public String getMusicTextualMaterialCode1() {
    return musicTextualMaterialCode1;
  }

  /**
   * @param musicTextualMaterialCode1 The musicTextualMaterialCode1
   */
  @JsonProperty("musicTextualMaterialCode1")
  public void setMusicTextualMaterialCode1(String musicTextualMaterialCode1) {
    this.attributes.put("musicTextualMaterialCode1", musicTextualMaterialCode1);
    this.musicTextualMaterialCode1 = musicTextualMaterialCode1;
  }

  public FixedField withMusicTextualMaterialCode1(String musicTextualMaterialCode1) {
    this.attributes.put("musicTextualMaterialCode1", musicTextualMaterialCode1);
    this.musicTextualMaterialCode1 = musicTextualMaterialCode1;
    return this;
  }

  /**
   * @return The musicTextualMaterialCode2
   */
  @JsonProperty("musicTextualMaterialCode2")
  public String getMusicTextualMaterialCode2() {
    return musicTextualMaterialCode2;
  }

  /**
   * @param musicTextualMaterialCode2 The musicTextualMaterialCode2
   */
  @JsonProperty("musicTextualMaterialCode2")
  public void setMusicTextualMaterialCode2(String musicTextualMaterialCode2) {
    this.attributes.put("musicTextualMaterialCode2", musicTextualMaterialCode2);
    this.musicTextualMaterialCode2 = musicTextualMaterialCode2;
  }

  public FixedField withMusicTextualMaterialCode2(String musicTextualMaterialCode2) {
    this.attributes.put("musicTextualMaterialCode2", musicTextualMaterialCode2);
    this.musicTextualMaterialCode2 = musicTextualMaterialCode2;
    return this;
  }

  /**
   * @return The musicTextualMaterialCode3
   */
  @JsonProperty("musicTextualMaterialCode3")
  public String getMusicTextualMaterialCode3() {
    return musicTextualMaterialCode3;
  }

  /**
   * @param musicTextualMaterialCode3 The musicTextualMaterialCode3
   */
  @JsonProperty("musicTextualMaterialCode3")
  public void setMusicTextualMaterialCode3(String musicTextualMaterialCode3) {
    this.attributes.put("musicTextualMaterialCode3", musicTextualMaterialCode3);
    this.musicTextualMaterialCode3 = musicTextualMaterialCode3;
  }

  public FixedField withMusicTextualMaterialCode3(String musicTextualMaterialCode3) {
    this.attributes.put("musicTextualMaterialCode3", musicTextualMaterialCode3);
    this.musicTextualMaterialCode3 = musicTextualMaterialCode3;
    return this;
  }

  /**
   * @return The musicTextualMaterialCode4
   */
  @JsonProperty("musicTextualMaterialCode4")
  public String getMusicTextualMaterialCode4() {
    return musicTextualMaterialCode4;
  }

  /**
   * @param musicTextualMaterialCode4 The musicTextualMaterialCode4
   */
  @JsonProperty("musicTextualMaterialCode4")
  public void setMusicTextualMaterialCode4(String musicTextualMaterialCode4) {
    this.attributes.put("musicTextualMaterialCode4", musicTextualMaterialCode4);
    this.musicTextualMaterialCode4 = musicTextualMaterialCode4;
  }

  public FixedField withMusicTextualMaterialCode4(String musicTextualMaterialCode4) {
    this.attributes.put("musicTextualMaterialCode4", musicTextualMaterialCode4);
    this.musicTextualMaterialCode4 = musicTextualMaterialCode4;
    return this;
  }

  /**
   * @return The musicTextualMaterialCode5
   */
  @JsonProperty("musicTextualMaterialCode5")
  public String getMusicTextualMaterialCode5() {
    return musicTextualMaterialCode5;
  }

  /**
   * @param musicTextualMaterialCode5 The musicTextualMaterialCode5
   */
  @JsonProperty("musicTextualMaterialCode5")
  public void setMusicTextualMaterialCode5(String musicTextualMaterialCode5) {
    this.attributes.put("musicTextualMaterialCode5", musicTextualMaterialCode5);
    this.musicTextualMaterialCode5 = musicTextualMaterialCode5;
  }

  public FixedField withMusicTextualMaterialCode5(String musicTextualMaterialCode5) {
    this.attributes.put("musicTextualMaterialCode5", musicTextualMaterialCode5);
    this.musicTextualMaterialCode5 = musicTextualMaterialCode5;
    return this;
  }

  /**
   * @return The musicTextualMaterialCode6
   */
  @JsonProperty("musicTextualMaterialCode6")
  public String getMusicTextualMaterialCode6() {
    return musicTextualMaterialCode6;
  }

  /**
   * @param musicTextualMaterialCode6 The musicTextualMaterialCode6
   */
  @JsonProperty("musicTextualMaterialCode6")
  public void setMusicTextualMaterialCode6(String musicTextualMaterialCode6) {
    this.attributes.put("musicTextualMaterialCode6", musicTextualMaterialCode6);
    this.musicTextualMaterialCode6 = musicTextualMaterialCode6;
  }

  public FixedField withMusicTextualMaterialCode6(String musicTextualMaterialCode6) {
    this.attributes.put("musicTextualMaterialCode6", musicTextualMaterialCode6);
    this.musicTextualMaterialCode6 = musicTextualMaterialCode6;
    return this;
  }

  /**
   * @return The musicLiteraryTextCode1
   */
  @JsonProperty("musicLiteraryTextCode1")
  public String getMusicLiteraryTextCode1() {
    return musicLiteraryTextCode1;
  }

  /**
   * @param musicLiteraryTextCode1 The musicLiteraryTextCode1
   */
  @JsonProperty("musicLiteraryTextCode1")
  public void setMusicLiteraryTextCode1(String musicLiteraryTextCode1) {
    this.attributes.put("musicLiteraryTextCode1", musicLiteraryTextCode1);
    this.musicLiteraryTextCode1 = musicLiteraryTextCode1;
  }

  public FixedField withMusicLiteraryTextCode1(String musicLiteraryTextCode1) {
    this.attributes.put("musicLiteraryTextCode1", musicLiteraryTextCode1);
    this.musicLiteraryTextCode1 = musicLiteraryTextCode1;
    return this;
  }

  /**
   * @return The musicLiteraryTextCode2
   */
  @JsonProperty("musicLiteraryTextCode2")
  public String getMusicLiteraryTextCode2() {
    return musicLiteraryTextCode2;
  }

  /**
   * @param musicLiteraryTextCode2 The musicLiteraryTextCode2
   */
  @JsonProperty("musicLiteraryTextCode2")
  public void setMusicLiteraryTextCode2(String musicLiteraryTextCode2) {
    this.attributes.put("musicLiteraryTextCode2", musicLiteraryTextCode2);
    this.musicLiteraryTextCode2 = musicLiteraryTextCode2;
  }

  public FixedField withMusicLiteraryTextCode2(String musicLiteraryTextCode2) {
    this.attributes.put("musicLiteraryTextCode2", musicLiteraryTextCode2);
    this.musicLiteraryTextCode2 = musicLiteraryTextCode2;
    return this;
  }

  /**
   * @return The musicTranspositionArrangementCode
   */
  @JsonProperty("musicTranspositionArrangementCode")
  public String getMusicTranspositionArrangementCode() {
    return musicTranspositionArrangementCode;
  }

  /**
   * @param musicTranspositionArrangementCode The
   *        musicTranspositionArrangementCode
   */
  @JsonProperty("musicTranspositionArrangementCode")
  public void setMusicTranspositionArrangementCode(String musicTranspositionArrangementCode) {
    this.attributes.put("musicTranspositionArrangementCode", musicTranspositionArrangementCode);
    this.musicTranspositionArrangementCode = musicTranspositionArrangementCode;
  }

  public FixedField withMusicTranspositionArrangementCode(String musicTranspositionArrangementCode) {
    this.attributes.put("musicTranspositionArrangementCode", musicTranspositionArrangementCode);
    this.musicTranspositionArrangementCode = musicTranspositionArrangementCode;
    return this;
  }

  /**
   * @return The computerFileTypeCode
   */
  @JsonProperty("computerFileTypeCode")
  public String getComputerFileTypeCode() {
    return computerFileTypeCode;
  }

  /**
   * @param computerFileTypeCode The computerFileTypeCode
   */
  @JsonProperty("computerFileTypeCode")
  public void setComputerFileTypeCode(String computerFileTypeCode) {
    this.attributes.put("computerFileTypeCode", computerFileTypeCode);
    this.computerFileTypeCode = computerFileTypeCode;
  }

  public FixedField withComputerFileTypeCode(String computerFileTypeCode) {
    this.attributes.put("computerFileTypeCode", computerFileTypeCode);
    this.computerFileTypeCode = computerFileTypeCode;
    return this;
  }

  /**
   * @return The computerTargetAudienceCode
   */
  @JsonProperty("computerTargetAudienceCode")
  public String getComputerTargetAudienceCode() {
    return computerTargetAudienceCode;
  }

  /**
   * @param computerTargetAudienceCode The computerTargetAudienceCode
   */
  @JsonProperty("computerTargetAudienceCode")
  public void setComputerTargetAudienceCode(String computerTargetAudienceCode) {
    this.attributes.put("computerTargetAudienceCode", computerTargetAudienceCode);
    this.computerTargetAudienceCode = computerTargetAudienceCode;
  }

  public FixedField withComputerTargetAudienceCode(String computerTargetAudienceCode) {
    this.attributes.put("computerTargetAudienceCode", computerTargetAudienceCode);
    this.computerTargetAudienceCode = computerTargetAudienceCode;
    return this;
  }

  /**
   * @return The computerFileFormCode
   */
  @JsonProperty("computerFileFormCode")
  public String getComputerFileFormCode() {
    return computerFileFormCode;
  }

  /**
   * @param computerFileFormCode The computerFileFormCode
   */
  @JsonProperty("computerFileFormCode")
  public void setComputerFileFormCode(String computerFileFormCode) {
    this.attributes.put("computerFileFormCode", computerFileFormCode);
    this.computerFileFormCode = computerFileFormCode;
  }

  public FixedField withComputerFileFormCode(String computerFileFormCode) {
    this.attributes.put("computerFileFormCode", computerFileFormCode);
    this.computerFileFormCode = computerFileFormCode;
    return this;
  }

  /**
   * @return The visualRunningTime
   */
  @JsonProperty("visualRunningTime")
  public String getVisualRunningTime() {
    return visualRunningTime;
  }

  /**
   * @param visualRunningTime The visualRunningTime
   */
  @JsonProperty("visualRunningTime")
  public void setVisualRunningTime(String visualRunningTime) {
    this.attributes.put("visualRunningTime", visualRunningTime);
    this.visualRunningTime = visualRunningTime;
  }

  public FixedField withVisualRunningTime(String visualRunningTime) {
    this.attributes.put("visualRunningTime", visualRunningTime);
    this.visualRunningTime = visualRunningTime;
    return this;
  }

  /**
   * @return The visualMaterialTypeCode
   */
  @JsonProperty("visualMaterialTypeCode")
  public String getVisualMaterialTypeCode() {
    return visualMaterialTypeCode;
  }

  /**
   * @param visualMaterialTypeCode The visualMaterialTypeCode
   */
  @JsonProperty("visualMaterialTypeCode")
  public void setVisualMaterialTypeCode(String visualMaterialTypeCode) {
    this.attributes.put("visualMaterialTypeCode", visualMaterialTypeCode);
    this.visualMaterialTypeCode = visualMaterialTypeCode;
  }

  public FixedField withVisualMaterialTypeCode(String visualMaterialTypeCode) {
    this.attributes.put("visualMaterialTypeCode", visualMaterialTypeCode);
    this.visualMaterialTypeCode = visualMaterialTypeCode;
    return this;
  }

  /**
   * @return The visualTechniqueCode
   */
  @JsonProperty("visualTechniqueCode")
  public String getVisualTechniqueCode() {
    return visualTechniqueCode;
  }

  /**
   * @param visualTechniqueCode The visualTechniqueCode
   */
  @JsonProperty("visualTechniqueCode")
  public void setVisualTechniqueCode(String visualTechniqueCode) {
    this.attributes.put("visualTechniqueCode", visualTechniqueCode);
    this.visualTechniqueCode = visualTechniqueCode;
  }

  public FixedField withVisualTechniqueCode(String visualTechniqueCode) {
    this.attributes.put("visualTechniqueCode", visualTechniqueCode);
    this.visualTechniqueCode = visualTechniqueCode;
    return this;
  }

  /**
   * @return The serialFrequencyCode
   */
  @JsonProperty("serialFrequencyCode")
  public String getSerialFrequencyCode() {
    return serialFrequencyCode;
  }

  /**
   * @param serialFrequencyCode The serialFrequencyCode
   */
  @JsonProperty("serialFrequencyCode")
  public void setSerialFrequencyCode(String serialFrequencyCode) {
    this.attributes.put("serialFrequencyCode", serialFrequencyCode);
    this.serialFrequencyCode = serialFrequencyCode;
  }

  public FixedField withSerialFrequencyCode(String serialFrequencyCode) {
    this.attributes.put("serialFrequencyCode", serialFrequencyCode);
    this.serialFrequencyCode = serialFrequencyCode;
    return this;
  }

  /**
   * @return The serialRegularityCode
   */
  @JsonProperty("serialRegularityCode")
  public String getSerialRegularityCode() {
    return serialRegularityCode;
  }

  /**
   * @param serialRegularityCode The serialRegularityCode
   */
  @JsonProperty("serialRegularityCode")
  public void setSerialRegularityCode(String serialRegularityCode) {
    this.attributes.put("serialRegularityCode", serialRegularityCode);
    this.serialRegularityCode = serialRegularityCode;
  }

  public FixedField withSerialRegularityCode(String serialRegularityCode) {
    this.attributes.put("serialRegularityCode", serialRegularityCode);
    this.serialRegularityCode = serialRegularityCode;
    return this;
  }

  /**
   * @return The serialTypeCode
   */
  @JsonProperty("serialTypeCode")
  public String getSerialTypeCode() {
    return serialTypeCode;
  }

  /**
   * @param serialTypeCode The serialTypeCode
   */
  @JsonProperty("serialTypeCode")
  public void setSerialTypeCode(String serialTypeCode) {
    this.attributes.put("serialTypeCode", serialTypeCode);
    this.serialTypeCode = serialTypeCode;
  }

  public FixedField withSerialTypeCode(String serialTypeCode) {
    this.attributes.put("serialTypeCode", serialTypeCode);
    this.serialTypeCode = serialTypeCode;
    return this;
  }

  /**
   * @return The serialFormOriginalItemCode
   */
  @JsonProperty("serialFormOriginalItemCode")
  public String getSerialFormOriginalItemCode() {
    return serialFormOriginalItemCode;
  }

  /**
   * @param serialFormOriginalItemCode The serialFormOriginalItemCode
   */
  @JsonProperty("serialFormOriginalItemCode")
  public void setSerialFormOriginalItemCode(String serialFormOriginalItemCode) {
    this.attributes.put("serialFormOriginalItemCode", serialFormOriginalItemCode);
    this.serialFormOriginalItemCode = serialFormOriginalItemCode;
  }

  public FixedField withSerialFormOriginalItemCode(String serialFormOriginalItemCode) {
    this.attributes.put("serialFormOriginalItemCode", serialFormOriginalItemCode);
    this.serialFormOriginalItemCode = serialFormOriginalItemCode;
    return this;
  }

  /**
   * @return The serialOriginalAlphabetOfTitleCode
   */
  @JsonProperty("serialOriginalAlphabetOfTitleCode")
  public String getSerialOriginalAlphabetOfTitleCode() {
    return serialOriginalAlphabetOfTitleCode;
  }

  /**
   * @param serialOriginalAlphabetOfTitleCode The
   *        serialOriginalAlphabetOfTitleCode
   */
  @JsonProperty("serialOriginalAlphabetOfTitleCode")
  public void setSerialOriginalAlphabetOfTitleCode(String serialOriginalAlphabetOfTitleCode) {
    this.attributes.put("serialOriginalAlphabetOfTitleCode", serialOriginalAlphabetOfTitleCode);
    this.serialOriginalAlphabetOfTitleCode = serialOriginalAlphabetOfTitleCode;
  }

  public FixedField withSerialOriginalAlphabetOfTitleCode(String serialOriginalAlphabetOfTitleCode) {
    this.attributes.put("serialOriginalAlphabetOfTitleCode", serialOriginalAlphabetOfTitleCode);
    this.serialOriginalAlphabetOfTitleCode = serialOriginalAlphabetOfTitleCode;
    return this;
  }

  /**
   * @return The serialSuccessiveLatestCode
   */
  @JsonProperty("serialSuccessiveLatestCode")
  public String getSerialSuccessiveLatestCode() {
    return serialSuccessiveLatestCode;
  }

  /**
   * @param serialSuccessiveLatestCode The serialSuccessiveLatestCode
   */
  @JsonProperty("serialSuccessiveLatestCode")
  public void setSerialSuccessiveLatestCode(String serialSuccessiveLatestCode) {
    this.attributes.put("serialSuccessiveLatestCode", serialSuccessiveLatestCode);
    this.serialSuccessiveLatestCode = serialSuccessiveLatestCode;
  }

  public FixedField withSerialSuccessiveLatestCode(String serialSuccessiveLatestCode) {
    this.attributes.put("serialSuccessiveLatestCode", serialSuccessiveLatestCode);
    this.serialSuccessiveLatestCode = serialSuccessiveLatestCode;
    return this;
  }

  /**
   * @return The languageCode
   */
  @JsonProperty("languageCode")
  public String getLanguageCode() {
    return languageCode;
  }

  /**
   * @param languageCode The languageCode
   */
  @JsonProperty("languageCode")
  public void setLanguageCode(String languageCode) {
    this.attributes.put("languageCode", languageCode);
    this.languageCode = languageCode;
  }

  public FixedField withLanguageCode(String languageCode) {
    this.attributes.put("languageCode", languageCode);
    this.languageCode = languageCode;
    return this;
  }

  /**
   * @return The recordModifiedCode
   */
  @JsonProperty("recordModifiedCode")
  public String getRecordModifiedCode() {
    return recordModifiedCode;
  }

  /**
   * @param recordModifiedCode The recordModifiedCode
   */
  @JsonProperty("recordModifiedCode")
  public void setRecordModifiedCode(String recordModifiedCode) {
    this.attributes.put("recordModifiedCode", recordModifiedCode);
    this.recordModifiedCode = recordModifiedCode;
  }

  public FixedField withRecordModifiedCode(String recordModifiedCode) {
    this.attributes.put("recordModifiedCode", recordModifiedCode);
    this.recordModifiedCode = recordModifiedCode;
    return this;
  }

  /**
   * @return The recordCataloguingSourceCode
   */
  @JsonProperty("recordCataloguingSourceCode")
  public String getRecordCataloguingSourceCode() {
    return recordCataloguingSourceCode;
  }

  /**
   * @param recordCataloguingSourceCode The recordCataloguingSourceCode
   */
  @JsonProperty("recordCataloguingSourceCode")
  public void setRecordCataloguingSourceCode(String recordCataloguingSourceCode) {
    this.attributes.put("recordCataloguingSourceCode", recordCataloguingSourceCode);
    this.recordCataloguingSourceCode = recordCataloguingSourceCode;
  }

  public FixedField withRecordCataloguingSourceCode(String recordCataloguingSourceCode) {
    this.attributes.put("recordCataloguingSourceCode", recordCataloguingSourceCode);
    this.recordCataloguingSourceCode = recordCataloguingSourceCode;
    return this;
  }

  /**
   * @return The itemRecordStatusCode
   */
  @JsonProperty("itemRecordStatusCode")
  public String getItemRecordStatusCode() {
    return itemRecordStatusCode;
  }

  /**
   * @param itemRecordStatusCode The itemRecordStatusCode
   */
  @JsonProperty("itemRecordStatusCode")
  public void setItemRecordStatusCode(String itemRecordStatusCode) {
    this.attributes.put("itemRecordStatusCode", itemRecordStatusCode);
    this.itemRecordStatusCode = itemRecordStatusCode;
  }

  public FixedField withItemRecordStatusCode(String itemRecordStatusCode) {
    this.attributes.put("itemRecordStatusCode", itemRecordStatusCode);
    this.itemRecordStatusCode = itemRecordStatusCode;
    return this;
  }

  /**
   * @return The itemRecordTypeCode
   */
  @JsonProperty("itemRecordTypeCode")
  public String getItemRecordTypeCode() {
    return itemRecordTypeCode;
  }

  /**
   * @param itemRecordTypeCode The itemRecordTypeCode
   */
  @JsonProperty("itemRecordTypeCode")
  public void setItemRecordTypeCode(String itemRecordTypeCode) {
    this.attributes.put("itemRecordTypeCode", itemRecordTypeCode);
    this.itemRecordTypeCode = itemRecordTypeCode;
  }

  public FixedField withItemRecordTypeCode(String itemRecordTypeCode) {
    this.attributes.put("itemRecordTypeCode", itemRecordTypeCode);
    this.itemRecordTypeCode = itemRecordTypeCode;
    return this;
  }

  /**
   * @return The itemBibliographicLevelCode
   */
  @JsonProperty("itemBibliographicLevelCode")
  public String getItemBibliographicLevelCode() {
    return itemBibliographicLevelCode;
  }

  /**
   * @param itemBibliographicLevelCode The itemBibliographicLevelCode
   */
  @JsonProperty("itemBibliographicLevelCode")
  public void setItemBibliographicLevelCode(String itemBibliographicLevelCode) {
    this.attributes.put("itemBibliographicLevelCode", itemBibliographicLevelCode);
    this.itemBibliographicLevelCode = itemBibliographicLevelCode;
  }

  public FixedField withItemBibliographicLevelCode(String itemBibliographicLevelCode) {
    this.attributes.put("itemBibliographicLevelCode", itemBibliographicLevelCode);
    this.itemBibliographicLevelCode = itemBibliographicLevelCode;
    return this;
  }

  /**
   * @return The itemControlTypeCode
   */
  @JsonProperty("itemControlTypeCode")
  public String getItemControlTypeCode() {
    return itemControlTypeCode;
  }

  /**
   * @param itemControlTypeCode The itemControlTypeCode
   */
  @JsonProperty("itemControlTypeCode")
  public void setItemControlTypeCode(String itemControlTypeCode) {
    this.attributes.put("itemControlTypeCode", itemControlTypeCode);
    this.itemControlTypeCode = itemControlTypeCode;
  }

  public FixedField withItemControlTypeCode(String itemControlTypeCode) {
    this.attributes.put("itemControlTypeCode", itemControlTypeCode);
    this.itemControlTypeCode = itemControlTypeCode;
    return this;
  }

  /**
   * @return The characterCodingSchemeCode
   */
  @JsonProperty("characterCodingSchemeCode")
  public String getCharacterCodingSchemeCode() {
    return characterCodingSchemeCode;
  }

  /**
   * @param characterCodingSchemeCode The characterCodingSchemeCode
   */
  @JsonProperty("characterCodingSchemeCode")
  public void setCharacterCodingSchemeCode(String characterCodingSchemeCode) {
    this.attributes.put("characterCodingSchemeCode", characterCodingSchemeCode);
    this.characterCodingSchemeCode = characterCodingSchemeCode;
  }

  public FixedField withCharacterCodingSchemeCode(String characterCodingSchemeCode) {
    this.attributes.put("characterCodingSchemeCode", characterCodingSchemeCode);
    this.characterCodingSchemeCode = characterCodingSchemeCode;
    return this;
  }

  /**
   * @return The encodingLevel
   */
  @JsonProperty("encodingLevel")
  public String getEncodingLevel() {
    return encodingLevel;
  }

  /**
   * @param encodingLevel The encodingLevel
   */
  @JsonProperty("encodingLevel")
  public void setEncodingLevel(String encodingLevel) {
    this.attributes.put("encodingLevel", encodingLevel);
    this.encodingLevel = encodingLevel;
  }

  public FixedField withEncodingLevel(String encodingLevel) {
    this.attributes.put("encodingLevel", encodingLevel);
    this.encodingLevel = encodingLevel;
    return this;
  }

  /**
   * @return The descriptiveCataloguingCode
   */
  @JsonProperty("descriptiveCataloguingCode")
  public String getDescriptiveCataloguingCode() {
    return descriptiveCataloguingCode;
  }

  /**
   * @param descriptiveCataloguingCode The descriptiveCataloguingCode
   */
  @JsonProperty("descriptiveCataloguingCode")
  public void setDescriptiveCataloguingCode(String descriptiveCataloguingCode) {
    this.attributes.put("descriptiveCataloguingCode", descriptiveCataloguingCode);
    this.descriptiveCataloguingCode = descriptiveCataloguingCode;
  }

  public FixedField withDescriptiveCataloguingCode(String descriptiveCataloguingCode) {
    this.attributes.put("descriptiveCataloguingCode", descriptiveCataloguingCode);
    this.descriptiveCataloguingCode = descriptiveCataloguingCode;
    return this;
  }

  /**
   * @return The linkedRecordCode
   */
  @JsonProperty("linkedRecordCode")
  public String getLinkedRecordCode() {
    return linkedRecordCode;
  }

  /**
   * @param linkedRecordCode The linkedRecordCode
   */
  @JsonProperty("linkedRecordCode")
  public void setLinkedRecordCode(String linkedRecordCode) {
    this.attributes.put("linkedRecordCode", linkedRecordCode);
    this.linkedRecordCode = linkedRecordCode;
  }

  public FixedField withLinkedRecordCode(String linkedRecordCode) {
    this.attributes.put("linkedRecordCode", linkedRecordCode);
    this.linkedRecordCode = linkedRecordCode;
    return this;
  }

  /**
   * @return The materialTypeCode
   */
  @JsonProperty("materialTypeCode")
  public String getMaterialTypeCode() {
    return materialTypeCode;
  }

  /**
   * @param materialTypeCode The materialTypeCode
   */
  @JsonProperty("materialTypeCode")
  public void setMaterialTypeCode(String materialTypeCode) {
    this.attributes.put("materialTypeCode", materialTypeCode);
    this.materialTypeCode = materialTypeCode;
  }

  public FixedField withMaterialTypeCode(String materialTypeCode) {
    this.attributes.put("materialTypeCode", materialTypeCode);
    this.materialTypeCode = materialTypeCode;
    return this;
  }

  /**
   * @return The categoryOfMaterial
   */
  @JsonProperty("categoryOfMaterial")
  public String getCategoryOfMaterial() {
    return categoryOfMaterial;
  }

  /**
   * @param categoryOfMaterial The categoryOfMaterial
   */
  @JsonProperty("categoryOfMaterial")
  public void setCategoryOfMaterial(String categoryOfMaterial) {
    this.attributes.put("categoryOfMaterial", categoryOfMaterial);
    this.categoryOfMaterial = categoryOfMaterial;
  }

  public FixedField withCategoryOfMaterial(String categoryOfMaterial) {
    this.attributes.put("categoryOfMaterial", categoryOfMaterial);
    this.categoryOfMaterial = categoryOfMaterial;
    return this;
  }

  /**
   * @return The specificMaterialDesignationCode
   */
  @JsonProperty("specificMaterialDesignationCode")
  public String getSpecificMaterialDesignationCode() {
    return specificMaterialDesignationCode;
  }

  /**
   * @param specificMaterialDesignationCode The specificMaterialDesignationCode
   */
  @JsonProperty("specificMaterialDesignationCode")
  public void setSpecificMaterialDesignationCode(String specificMaterialDesignationCode) {
    this.attributes.put("specificMaterialDesignationCode", specificMaterialDesignationCode);
    this.specificMaterialDesignationCode = specificMaterialDesignationCode;
  }

  public FixedField withSpecificMaterialDesignationCode(String specificMaterialDesignationCode) {
    this.attributes.put("specificMaterialDesignationCode", specificMaterialDesignationCode);
    this.specificMaterialDesignationCode = specificMaterialDesignationCode;
    return this;
  }

  /**
   * @return The colorCode
   */
  @JsonProperty("colorCode")
  public String getColorCode() {
    return colorCode;
  }

  /**
   * @param colorCode The colorCode
   */
  @JsonProperty("colorCode")
  public void setColorCode(String colorCode) {
    this.attributes.put("colorCode", colorCode);
    this.colorCode = colorCode;
  }

  public FixedField withColorCode(String colorCode) {
    this.attributes.put("colorCode", colorCode);
    this.colorCode = colorCode;
    return this;
  }

  /**
   * @return The physicalMediumCode
   */
  @JsonProperty("physicalMediumCode")
  public String getPhysicalMediumCode() {
    return physicalMediumCode;
  }

  /**
   * @param physicalMediumCode The physicalMediumCode
   */
  @JsonProperty("physicalMediumCode")
  public void setPhysicalMediumCode(String physicalMediumCode) {
    this.attributes.put("physicalMediumCode", physicalMediumCode);
    this.physicalMediumCode = physicalMediumCode;
  }

  public FixedField withPhysicalMediumCode(String physicalMediumCode) {
    this.attributes.put("physicalMediumCode", physicalMediumCode);
    this.physicalMediumCode = physicalMediumCode;
    return this;
  }

  /**
   * @return The typeOfReproductionCode
   */
  @JsonProperty("typeOfReproductionCode")
  public String getTypeOfReproductionCode() {
    return typeOfReproductionCode;
  }

  /**
   * @param typeOfReproductionCode The typeOfReproductionCode
   */
  @JsonProperty("typeOfReproductionCode")
  public void setTypeOfReproductionCode(String typeOfReproductionCode) {
    this.attributes.put("typeOfReproductionCode", typeOfReproductionCode);
    this.typeOfReproductionCode = typeOfReproductionCode;
  }

  public FixedField withTypeOfReproductionCode(String typeOfReproductionCode) {
    this.attributes.put("typeOfReproductionCode", typeOfReproductionCode);
    this.typeOfReproductionCode = typeOfReproductionCode;
    return this;
  }

  /**
   * @return The polarityCode
   */
  @JsonProperty("polarityCode")
  public String getPolarityCode() {
    return polarityCode;
  }

  /**
   * @param polarityCode The polarityCode
   */
  @JsonProperty("polarityCode")
  public void setPolarityCode(String polarityCode) {
    this.attributes.put("polarityCode", polarityCode);
    this.polarityCode = polarityCode;
  }

  public FixedField withPolarityCode(String polarityCode) {
    this.attributes.put("polarityCode", polarityCode);
    this.polarityCode = polarityCode;
    return this;
  }

  /**
   * @return The dimensionCode
   */
  @JsonProperty("dimensionCode")
  public String getDimensionCode() {
    return dimensionCode;
  }

  /**
   * @param dimensionCode The dimensionsCode
   */
  @JsonProperty("dimensionCode")
  public void setDimensionCode(String dimensionCode) {
    this.attributes.put("dimensionCode", dimensionCode);
    this.dimensionCode = dimensionCode;
  }

  public FixedField withDimensionCode(String dimensionCode) {
    this.attributes.put("dimensionCode", dimensionCode);
    this.dimensionCode = dimensionCode;
    return this;
  }

  /**
   * @return The generationCode
   */
  @JsonProperty("generationCode")
  public String getGenerationCode() {
    return generationCode;
  }

  /**
   * @param generationCode The generationCode
   */
  @JsonProperty("generationCode")
  public void setGenerationCode(String generationCode) {
    this.attributes.put("generationCode", generationCode);
    this.generationCode = generationCode;
  }

  public FixedField withGenerationCode(String generationCode) {
    this.attributes.put("generationCode", generationCode);
    this.generationCode = generationCode;
    return this;
  }

  /**
   * @return The baseOfFilmCode
   */
  @JsonProperty("baseOfFilmCode")
  public String getBaseOfFilmCode() {
    return baseOfFilmCode;
  }

  /**
   * @param baseOfFilmCode The baseOfFilmCode
   */
  @JsonProperty("baseOfFilmCode")
  public void setBaseOfFilmCode(String baseOfFilmCode) {
    this.attributes.put("baseOfFilmCode", baseOfFilmCode);
    this.baseOfFilmCode = baseOfFilmCode;
  }

  public FixedField withBaseOfFilmCode(String baseOfFilmCode) {
    this.attributes.put("baseOfFilmCode", baseOfFilmCode);
    this.baseOfFilmCode = baseOfFilmCode;
    return this;
  }

  /**
   * @return The includesSoundCode
   */
  @JsonProperty("includesSoundCode")
  public String getIncludesSoundCode() {
    return includesSoundCode;
  }

  /**
   * @param includesSoundCode The includesSoundCode
   */
  @JsonProperty("includesSoundCode")
  public void setIncludesSoundCode(String includesSoundCode) {
    this.attributes.put("includesSoundCode", includesSoundCode);
    this.includesSoundCode = includesSoundCode;
  }

  public FixedField withIncludesSoundCode(String includesSoundCode) {
    this.attributes.put("includesSoundCode", includesSoundCode);
    this.includesSoundCode = includesSoundCode;
    return this;
  }

  /**
   * @return The mediumForSoundCode
   */
  @JsonProperty("mediumForSoundCode")
  public String getMediumForSoundCode() {
    return mediumForSoundCode;
  }

  /**
   * @param mediumForSoundCode The mediumForSoundCode
   */
  @JsonProperty("mediumForSoundCode")
  public void setMediumForSoundCode(String mediumForSoundCode) {
    this.attributes.put("mediumForSoundCode", mediumForSoundCode);
    this.mediumForSoundCode = mediumForSoundCode;
  }

  public FixedField withMediumForSoundCode(String mediumForSoundCode) {
    this.attributes.put("mediumForSoundCode", mediumForSoundCode);
    this.mediumForSoundCode = mediumForSoundCode;
    return this;
  }

  /**
   * @return The secondarySupportMaterialCode
   */
  @JsonProperty("secondarySupportMaterialCode")
  public String getSecondarySupportMaterialCode() {
    return secondarySupportMaterialCode;
  }

  /**
   * @param secondarySupportMaterialCode The secondarySupportMaterialCode
   */
  @JsonProperty("secondarySupportMaterialCode")
  public void setSecondarySupportMaterialCode(String secondarySupportMaterialCode) {
    this.attributes.put("secondarySupportMaterialCode", secondarySupportMaterialCode);
    this.secondarySupportMaterialCode = secondarySupportMaterialCode;
  }

  public FixedField withSecondarySupportMaterialCode(String secondarySupportMaterialCode) {
    this.attributes.put("secondarySupportMaterialCode", secondarySupportMaterialCode);
    this.secondarySupportMaterialCode = secondarySupportMaterialCode;
    return this;
  }

  /**
   * @return The configurationCode
   */
  @JsonProperty("configurationCode")
  public String getConfigurationCode() {
    return configurationCode;
  }

  /**
   * @param configurationCode The configurationCode
   */
  @JsonProperty("configurationCode")
  public void setConfigurationCode(String configurationCode) {
    this.attributes.put("configurationCode", configurationCode);
    this.configurationCode = configurationCode;
  }

  public FixedField withConfigurationCode(String configurationCode) {
    this.attributes.put("configurationCode", configurationCode);
    this.configurationCode = configurationCode;
    return this;
  }

  /**
   * @return The obsolete1
   */
  @JsonProperty("obsolete1")
  public String getObsolete1() {
    return obsolete1;
  }

  /**
   * @param obsolete1 The obsolete1
   */
  @JsonProperty("obsolete1")
  public void setObsolete1(String obsolete1) {
    this.attributes.put("obsolete1", obsolete1);
    this.obsolete1 = obsolete1;
  }

  public FixedField withObsolete1(String obsolete1) {
    this.attributes.put("obsolete1", obsolete1);
    this.obsolete1 = obsolete1;
    return this;
  }

  /**
   * @return The obsolete2
   */
  @JsonProperty("obsolete2")
  public String getObsolete2() {
    return obsolete2;
  }

  /**
   * @param obsolete2 The obsolete2
   */
  @JsonProperty("obsolete2")
  public void setObsolete2(String obsolete2) {
    this.attributes.put("obsolete2", obsolete2);
    this.obsolete2 = obsolete2;
  }

  public FixedField withObsolete2(String obsolete2) {
    this.attributes.put("obsolete2", obsolete2);
    this.obsolete2 = obsolete2;
    return this;
  }

  /**
   * @return The imageBitDepth
   */
  @JsonProperty("imageBitDepth")
  public String getImageBitDepth() {
    return imageBitDepth;
  }

  /**
   * @param imageBitDepth The imageBitDepth
   */
  @JsonProperty("imageBitDepth")
  public void setImageBitDepth(String imageBitDepth) {
    this.attributes.put("imageBitDepth", imageBitDepth);
    this.imageBitDepth = imageBitDepth;
  }

  public FixedField withImageBitDepth(String imageBitDepth) {
    this.attributes.put("imageBitDepth", imageBitDepth);
    this.imageBitDepth = imageBitDepth;
    return this;
  }

  /**
   * @return The fileFormatsCode
   */
  @JsonProperty("fileFormatsCode")
  public String getFileFormatsCode() {
    return fileFormatsCode;
  }

  /**
   * @param fileFormatsCode The fileFormatsCode
   */
  @JsonProperty("fileFormatsCode")
  public void setFileFormatsCode(String fileFormatsCode) {
    this.attributes.put("fileFormatsCode", fileFormatsCode);
    this.fileFormatsCode = fileFormatsCode;
  }

  public FixedField withFileFormatsCode(String fileFormatsCode) {
    this.attributes.put("fileFormatsCode", fileFormatsCode);
    this.fileFormatsCode = fileFormatsCode;
    return this;
  }

  /**
   * @return The qualityAssuranceTargetCode
   */
  @JsonProperty("qualityAssuranceTargetCode")
  public String getQualityAssuranceTargetCode() {
    return qualityAssuranceTargetCode;
  }

  /**
   * @param qualityAssuranceTargetCode The qualityAssuranceTargetCode
   */
  @JsonProperty("qualityAssuranceTargetCode")
  public void setQualityAssuranceTargetCode(String qualityAssuranceTargetCode) {
    this.attributes.put("qualityAssuranceTargetCode", qualityAssuranceTargetCode);
    this.qualityAssuranceTargetCode = qualityAssuranceTargetCode;
  }

  public FixedField withQualityAssuranceTargetCode(String qualityAssuranceTargetCode) {
    this.attributes.put("qualityAssuranceTargetCode", qualityAssuranceTargetCode);
    this.qualityAssuranceTargetCode = qualityAssuranceTargetCode;
    return this;
  }

  /**
   * @return The antecedentSourceCode
   */
  @JsonProperty("antecedentSourceCode")
  public String getAntecedentSourceCode() {
    return antecedentSourceCode;
  }

  /**
   * @param antecedentSourceCode The antecedentSourceCode
   */
  @JsonProperty("antecedentSourceCode")
  public void setAntecedentSourceCode(String antecedentSourceCode) {
    this.attributes.put("antecedentSourceCode", antecedentSourceCode);
    this.antecedentSourceCode = antecedentSourceCode;
  }

  public FixedField withAntecedentSourceCode(String antecedentSourceCode) {
    this.attributes.put("antecedentSourceCode", antecedentSourceCode);
    this.antecedentSourceCode = antecedentSourceCode;
    return this;
  }

  /**
   * @return The levelOfCompressionCode
   */
  @JsonProperty("levelOfCompressionCode")
  public String getLevelOfCompressionCode() {
    return levelOfCompressionCode;
  }

  /**
   * @param levelOfCompressionCode The levelOfCompressionCode
   */
  @JsonProperty("levelOfCompressionCode")
  public void setLevelOfCompressionCode(String levelOfCompressionCode) {
    this.attributes.put("levelOfCompressionCode", levelOfCompressionCode);
    this.levelOfCompressionCode = levelOfCompressionCode;
  }

  public FixedField withLevelOfCompressionCode(String levelOfCompressionCode) {
    this.attributes.put("levelOfCompressionCode", levelOfCompressionCode);
    this.levelOfCompressionCode = levelOfCompressionCode;
    return this;
  }

  /**
   * @return The reformattingQualityCode
   */
  @JsonProperty("reformattingQualityCode")
  public String getReformattingQualityCode() {
    return reformattingQualityCode;
  }

  /**
   * @param reformattingQualityCode The reformattingQualityCode
   */
  @JsonProperty("reformattingQualityCode")
  public void setReformattingQualityCode(String reformattingQualityCode) {
    this.attributes.put("reformattingQualityCode", reformattingQualityCode);
    this.reformattingQualityCode = reformattingQualityCode;
  }

  public FixedField withReformattingQualityCode(String reformattingQualityCode) {
    this.attributes.put("reformattingQualityCode", reformattingQualityCode);
    this.reformattingQualityCode = reformattingQualityCode;
    return this;
  }

  /**
   * @return The productionDetailsCode
   */
  @JsonProperty("productionDetailsCode")
  public String getProductionDetailsCode() {
    return productionDetailsCode;
  }

  /**
   * @param productionDetailsCode The productionDetailsCode
   */
  @JsonProperty("productionDetailsCode")
  public void setProductionDetailsCode(String productionDetailsCode) {
    this.attributes.put("productionDetailsCode", productionDetailsCode);
    this.productionDetailsCode = productionDetailsCode;
  }

  public FixedField withProductionDetailsCode(String productionDetailsCode) {
    this.attributes.put("productionDetailsCode", productionDetailsCode);
    this.productionDetailsCode = productionDetailsCode;
    return this;
  }

  /**
   * @return The reductionRatioRangeCode
   */
  @JsonProperty("reductionRatioRangeCode")
  public String getReductionRatioRangeCode() {
    return reductionRatioRangeCode;
  }

  /**
   * @param reductionRatioRangeCode The reductionRatioRangeCode
   */
  @JsonProperty("reductionRatioRangeCode")
  public void setReductionRatioRangeCode(String reductionRatioRangeCode) {
    this.attributes.put("reductionRatioRangeCode", reductionRatioRangeCode);
    this.reductionRatioRangeCode = reductionRatioRangeCode;
  }

  public FixedField withReductionRatioRangeCode(String reductionRatioRangeCode) {
    this.attributes.put("reductionRatioRangeCode", reductionRatioRangeCode);
    this.reductionRatioRangeCode = reductionRatioRangeCode;
    return this;
  }

  /**
   * @return The reductionRatioCode
   */
  @JsonProperty("reductionRatioCode")
  public String getReductionRatioCode() {
    return reductionRatioCode;
  }

  /**
   * @param reductionRatioCode The reductionRatioCode
   */
  @JsonProperty("reductionRatioCode")
  public void setReductionRatioCode(String reductionRatioCode) {
    this.attributes.put("reductionRatioCode", reductionRatioCode);
    this.reductionRatioCode = reductionRatioCode;
  }

  public FixedField withReductionRatioCode(String reductionRatioCode) {
    this.attributes.put("reductionRatioCode", reductionRatioCode);
    this.reductionRatioCode = reductionRatioCode;
    return this;
  }

  /**
   * @return The emulsionOnFilmCode
   */
  @JsonProperty("emulsionOnFilmCode")
  public String getEmulsionOnFilmCode() {
    return emulsionOnFilmCode;
  }

  /**
   * @param emulsionOnFilmCode The emulsionOnFilmCode
   */
  @JsonProperty("emulsionOnFilmCode")
  public void setEmulsionOnFilmCode(String emulsionOnFilmCode) {
    this.attributes.put("emulsionOnFilmCode", emulsionOnFilmCode);
    this.emulsionOnFilmCode = emulsionOnFilmCode;
  }

  public FixedField withEmulsionOnFilmCode(String emulsionOnFilmCode) {
    this.attributes.put("emulsionOnFilmCode", emulsionOnFilmCode);
    this.emulsionOnFilmCode = emulsionOnFilmCode;
    return this;
  }

  /**
   * @return The presentationFormatCode
   */
  @JsonProperty("presentationFormatCode")
  public String getPresentationFormatCode() {
    return presentationFormatCode;
  }

  /**
   * @param presentationFormatCode The presentationFormatCode
   */
  @JsonProperty("presentationFormatCode")
  public void setPresentationFormatCode(String presentationFormatCode) {
    this.attributes.put("presentationFormatCode", presentationFormatCode);
    this.presentationFormatCode = presentationFormatCode;
  }

  public FixedField withPresentationFormatCode(String presentationFormatCode) {
    this.attributes.put("presentationFormatCode", presentationFormatCode);
    this.presentationFormatCode = presentationFormatCode;
    return this;
  }

  /**
   * @return The productionElementsCode
   */
  @JsonProperty("productionElementsCode")
  public String getProductionElementsCode() {
    return productionElementsCode;
  }

  /**
   * @param productionElementsCode The productionElementsCode
   */
  @JsonProperty("productionElementsCode")
  public void setProductionElementsCode(String productionElementsCode) {
    this.attributes.put("productionElementsCode", productionElementsCode);
    this.productionElementsCode = productionElementsCode;
  }

  public FixedField withProductionElementsCode(String productionElementsCode) {
    this.attributes.put("productionElementsCode", productionElementsCode);
    this.productionElementsCode = productionElementsCode;
    return this;
  }

  /**
   * @return The refinedCategoriesOfColorCode
   */
  @JsonProperty("refinedCategoriesOfColorCode")
  public String getRefinedCategoriesOfColourCode() {
    return refinedCategoriesOfColorCode;
  }

  /**
   * @param refinedCategoriesOfColorCode The refinedCategoriesOfColorCode
   */
  @JsonProperty("refinedCategoriesOfColorCode")
  public void setRefinedCategoriesOfColourCode(String refinedCategoriesOfColorCode) {
    this.attributes.put("refinedCategoriesOfColorCode", refinedCategoriesOfColorCode);
    this.refinedCategoriesOfColorCode = refinedCategoriesOfColorCode;
  }

  public FixedField withRefinedCategoriesOfColourCode(String refinedCategoriesOfColorCode) {
    this.attributes.put("refinedCategoriesOfColorCode", refinedCategoriesOfColorCode);
    this.refinedCategoriesOfColorCode = refinedCategoriesOfColorCode;
    return this;
  }

  /**
   * @return The kindOfColorStockCode
   */
  @JsonProperty("kindOfColorStockCode")
  public String getKindOfColourStockCode() {
    return kindOfColorStockCode;
  }

  /**
   * @param kindOfColorStockCode The kindOfColorStockCode
   */
  @JsonProperty("kindOfColorStockCode")
  public void setKindOfColourStockCode(String kindOfColorStockCode) {
    this.attributes.put("kindOfColorStockCode", kindOfColorStockCode);
    this.kindOfColorStockCode = kindOfColorStockCode;
  }

  public FixedField withKindOfColorStockCode(String kindOfColorStockCode) {
    this.attributes.put("kindOfColorStockCode", kindOfColorStockCode);
    this.kindOfColorStockCode = kindOfColorStockCode;
    return this;
  }

  /**
   * @return The deteriorationStageCode
   */
  @JsonProperty("deteriorationStageCode")
  public String getDeteriorationStageCode() {
    return deteriorationStageCode;
  }

  /**
   * @param deteriorationStageCode The deteriorationStageCode
   */
  @JsonProperty("deteriorationStageCode")
  public void setDeteriorationStageCode(String deteriorationStageCode) {
    this.attributes.put("deteriorationStageCode", deteriorationStageCode);
    this.deteriorationStageCode = deteriorationStageCode;
  }

  public FixedField withDeteriorationStageCode(String deteriorationStageCode) {
    this.attributes.put("deteriorationStageCode", deteriorationStageCode);
    this.deteriorationStageCode = deteriorationStageCode;
    return this;
  }

  /**
   * @return The completenessCode
   */
  @JsonProperty("completenessCode")
  public String getCompletenessCode() {
    return completenessCode;
  }

  /**
   * @param completenessCode The completenessCode
   */
  @JsonProperty("completenessCode")
  public void setCompletenessCode(String completenessCode) {
    this.attributes.put("completenessCode", completenessCode);
    this.completenessCode = completenessCode;
  }

  public FixedField withCompletenessCode(String completenessCode) {
    this.attributes.put("completenessCode", completenessCode);
    this.completenessCode = completenessCode;
    return this;
  }

  /**
   * @return The inspectionDate
   */
  @JsonProperty("inspectionDate")
  public String getInspectionDate() {
    return inspectionDate;
  }

  /**
   * @param inspectionDate The inspectionDate
   */
  @JsonProperty("inspectionDate")
  public void setInspectionDate(String inspectionDate) {
    this.attributes.put("inspectionDate", inspectionDate);
    this.inspectionDate = inspectionDate;
  }

  public FixedField withInspectionDate(String inspectionDate) {
    this.attributes.put("inspectionDate", inspectionDate);
    this.inspectionDate = inspectionDate;
    return this;
  }

  /**
   * @return The primarySupportMaterialCode
   */
  @JsonProperty("primarySupportMaterialCode")
  public String getPrimarySupportMaterialCode() {
    return primarySupportMaterialCode;
  }

  /**
   * @param primarySupportMaterialCode The primarySupportMaterialCode
   */
  @JsonProperty("primarySupportMaterialCode")
  public void setPrimarySupportMaterialCode(String primarySupportMaterialCode) {
    this.attributes.put("primarySupportMaterialCode", primarySupportMaterialCode);
    this.primarySupportMaterialCode = primarySupportMaterialCode;
  }

  public FixedField withPrimarySupportMaterialCode(String primarySupportMaterialCode) {
    this.attributes.put("primarySupportMaterialCode", primarySupportMaterialCode);
    this.primarySupportMaterialCode = primarySupportMaterialCode;
    return this;
  }

  /**
   * @return The baseOfEmulsionCode
   */
  @JsonProperty("baseOfEmulsionCode")
  public String getBaseOfEmulsionCode() {
    return baseOfEmulsionCode;
  }

  /**
   * @param baseOfEmulsionCode The baseOfEmulsionCode
   */
  @JsonProperty("baseOfEmulsionCode")
  public void setBaseOfEmulsionCode(String baseOfEmulsionCode) {
    this.attributes.put("baseOfEmulsionCode", baseOfEmulsionCode);
    this.baseOfEmulsionCode = baseOfEmulsionCode;
  }

  public FixedField withBaseOfEmulsionCode(String baseOfEmulsionCode) {
    this.attributes.put("baseOfEmulsionCode", baseOfEmulsionCode);
    this.baseOfEmulsionCode = baseOfEmulsionCode;
    return this;
  }

  /**
   * @return The videoRecordingFormatCode
   */
  @JsonProperty("videoRecordingFormatCode")
  public String getVideoRecordingFormatCode() {
    return videoRecordingFormatCode;
  }

  /**
   * @param videoRecordingFormatCode The videoRecordingFormatCode
   */
  @JsonProperty("videoRecordingFormatCode")
  public void setVideoRecordingFormatCode(String videoRecordingFormatCode) {
    this.attributes.put("videoRecordingFormatCode", videoRecordingFormatCode);
    this.videoRecordingFormatCode = videoRecordingFormatCode;
  }

  public FixedField withVideoRecordingFormatCode(String videoRecordingFormatCode) {
    this.attributes.put("videoRecordingFormatCode", videoRecordingFormatCode);
    this.videoRecordingFormatCode = videoRecordingFormatCode;
    return this;
  }

  /**
   * @return The remoteSensingDataTypeCode
   */
  @JsonProperty("remoteSensingDataTypeCode")
  public String getRemoteSensingDataTypeCode() {
    return remoteSensingDataTypeCode;
  }

  /**
   * @param remoteSensingDataTypeCode The remoteSensingDataTypeCode
   */
  @JsonProperty("remoteSensingDataTypeCode")
  public void setRemoteSensingDataTypeCode(String remoteSensingDataTypeCode) {
    this.attributes.put("remoteSensingDataTypeCode", remoteSensingDataTypeCode);
    this.remoteSensingDataTypeCode = remoteSensingDataTypeCode;
  }

  public FixedField withRemoteSensingDataTypeCode(String remoteSensingDataTypeCode) {
    this.attributes.put("remoteSensingDataTypeCode", remoteSensingDataTypeCode);
    this.remoteSensingDataTypeCode = remoteSensingDataTypeCode;
    return this;
  }

  /**
   * @return The soundOnMediumOrSeparateCode
   */
  @JsonProperty("soundOnMediumOrSeparateCode")
  public String getSoundOnMediumOrSeparateCode() {
    return soundOnMediumOrSeparateCode;
  }

  /**
   * @param soundOnMediumOrSeparateCode The soundOnMediumOrSeparateCode
   */
  @JsonProperty("soundOnMediumOrSeparateCode")
  public void setSoundOnMediumOrSeparateCode(String soundOnMediumOrSeparateCode) {
    this.attributes.put("soundOnMediumOrSeparateCode", soundOnMediumOrSeparateCode);
    this.soundOnMediumOrSeparateCode = soundOnMediumOrSeparateCode;
  }

  public FixedField withSoundOnMediumOrSeparateCode(String soundOnMediumOrSeparateCode) {
    this.attributes.put("soundOnMediumOrSeparateCode", soundOnMediumOrSeparateCode);
    this.soundOnMediumOrSeparateCode = soundOnMediumOrSeparateCode;
    return this;
  }

  /**
   * @return The altitudeOfSensorCode
   */
  @JsonProperty("altitudeOfSensorCode")
  public String getAltitudeOfSensorCode() {
    return altitudeOfSensorCode;
  }

  /**
   * @param altitudeOfSensorCode The altitudeOfSensorCode
   */
  @JsonProperty("altitudeOfSensorCode")
  public void setAltitudeOfSensorCode(String altitudeOfSensorCode) {
    this.attributes.put("altitudeOfSensorCode", altitudeOfSensorCode);
    this.altitudeOfSensorCode = altitudeOfSensorCode;
  }

  public FixedField withAltitudeOfSensorCode(String altitudeOfSensorCode) {
    this.attributes.put("altitudeOfSensorCode", altitudeOfSensorCode);
    this.altitudeOfSensorCode = altitudeOfSensorCode;
    return this;
  }

  /**
   * @return The attitudeOfSensorCode
   */
  @JsonProperty("attitudeOfSensorCode")
  public String getAttitudeOfSensorCode() {
    return attitudeOfSensorCode;
  }

  /**
   * @param attitudeOfSensorCode The attitudeOfSensorCode
   */
  @JsonProperty("attitudeOfSensorCode")
  public void setAttitudeOfSensorCode(String attitudeOfSensorCode) {
    this.attributes.put("attitudeOfSensorCode", attitudeOfSensorCode);
    this.attitudeOfSensorCode = attitudeOfSensorCode;
  }

  public FixedField withAttitudeOfSensorCode(String attitudeOfSensorCode) {
    this.attributes.put("attitudeOfSensorCode", attitudeOfSensorCode);
    this.attitudeOfSensorCode = attitudeOfSensorCode;
    return this;
  }

  /**
   * @return The cloudCoverCode
   */
  @JsonProperty("cloudCoverCode")
  public String getCloudCoverCode() {
    return cloudCoverCode;
  }

  /**
   * @param cloudCoverCode The cloudCoverCode
   */
  @JsonProperty("cloudCoverCode")
  public void setCloudCoverCode(String cloudCoverCode) {
    this.attributes.put("cloudCoverCode", cloudCoverCode);
    this.cloudCoverCode = cloudCoverCode;
  }

  public FixedField withCloudCoverCode(String cloudCoverCode) {
    this.attributes.put("cloudCoverCode", cloudCoverCode);
    this.cloudCoverCode = cloudCoverCode;
    return this;
  }

  /**
   * @return The platformConstructionTypeCode
   */
  @JsonProperty("platformConstructionTypeCode")
  public String getPlatformConstructionTypeCode() {
    return platformConstructionTypeCode;
  }

  /**
   * @param platformConstructionTypeCode The platformConstructionTypeCode
   */
  @JsonProperty("platformConstructionTypeCode")
  public void setPlatformConstructionTypeCode(String platformConstructionTypeCode) {
    this.attributes.put("platformConstructionTypeCode", platformConstructionTypeCode);
    this.platformConstructionTypeCode = platformConstructionTypeCode;
  }

  public FixedField withPlatformConstructionTypeCode(String platformConstructionTypeCode) {
    this.attributes.put("platformConstructionTypeCode", platformConstructionTypeCode);
    this.platformConstructionTypeCode = platformConstructionTypeCode;
    return this;
  }

  /**
   * @return The platformUseCode
   */
  @JsonProperty("platformUseCode")
  public String getPlatformUseCode() {
    return platformUseCode;
  }

  /**
   * @param platformUseCode The platformUseCode
   */
  @JsonProperty("platformUseCode")
  public void setPlatformUseCode(String platformUseCode) {
    this.attributes.put("platformUseCode", platformUseCode);
    this.platformUseCode = platformUseCode;
  }

  public FixedField withPlatformUseCode(String platformUseCode) {
    this.attributes.put("platformUseCode", platformUseCode);
    this.platformUseCode = platformUseCode;
    return this;
  }

  /**
   * @return The sensorTypeCode
   */
  @JsonProperty("sensorTypeCode")
  public String getSensorTypeCode() {
    return sensorTypeCode;
  }

  /**
   * @param sensorTypeCode The sensorTypeCode
   */
  @JsonProperty("sensorTypeCode")
  public void setSensorTypeCode(String sensorTypeCode) {
    this.attributes.put("sensorTypeCode", sensorTypeCode);
    this.sensorTypeCode = sensorTypeCode;
  }

  public FixedField withSensorTypeCode(String sensorTypeCode) {
    this.attributes.put("sensorTypeCode", sensorTypeCode);
    this.sensorTypeCode = sensorTypeCode;
    return this;
  }

  /**
   * @return The remoteDataTypeCode
   */
  @JsonProperty("remoteDataTypeCode")
  public String getRemoteDataTypeCode() {
    return remoteDataTypeCode;
  }

  /**
   * @param remoteDataTypeCode The remoteDataTypeCode
   */
  @JsonProperty("remoteDataTypeCode")
  public void setRemoteDataTypeCode(String remoteDataTypeCode) {
    this.attributes.put("remoteDataTypeCode", remoteDataTypeCode);
    this.remoteDataTypeCode = remoteDataTypeCode;
  }

  /**
   * @return The speedCode
   */
  @JsonProperty("speedCode")
  public String getSpeedCode() {
    return speedCode;
  }

  /**
   * @param speedCode The speedCode
   */
  @JsonProperty("speedCode")
  public void setSpeedCode(String speedCode) {
    this.attributes.put("speedCode", speedCode);
    this.speedCode = speedCode;
  }

  public FixedField withSpeedCode(String speedCode) {
    this.attributes.put("speedCode", speedCode);
    this.speedCode = speedCode;
    return this;
  }

  /**
   * @return The grooveWidthCode
   */
  @JsonProperty("grooveWidthCode")
  public String getGrooveWidthCode() {
    return grooveWidthCode;
  }

  /**
   * @param grooveWidthCode The grooveWidthCode
   */
  @JsonProperty("grooveWidthCode")
  public void setGrooveWidthCode(String grooveWidthCode) {
    this.attributes.put("grooveWidthCode", grooveWidthCode);
    this.grooveWidthCode = grooveWidthCode;
  }

  public FixedField withGrooveWidthCode(String grooveWidthCode) {
    this.attributes.put("grooveWidthCode", grooveWidthCode);
    this.grooveWidthCode = grooveWidthCode;
    return this;
  }

  /**
   * @return The tapeWidthCode
   */
  @JsonProperty("tapeWidthCode")
  public String getTapeWidthCode() {
    return tapeWidthCode;
  }

  /**
   * @param tapeWidthCode The tapeWidthCode
   */
  @JsonProperty("tapeWidthCode")
  public void setTapeWidthCode(String tapeWidthCode) {
    this.attributes.put("tapeWidthCode", tapeWidthCode);
    this.tapeWidthCode = tapeWidthCode;
  }

  public FixedField withTapeWidthCode(String tapeWidthCode) {
    this.attributes.put("tapeWidthCode", tapeWidthCode);
    this.tapeWidthCode = tapeWidthCode;
    return this;
  }

  /**
   * @return The tapeConfigurationCode
   */
  @JsonProperty("tapeConfigurationCode")
  public String getTapeConfigurationCode() {
    return tapeConfigurationCode;
  }

  /**
   * @param tapeConfigurationCode The tapeConfigurationCode
   */
  @JsonProperty("tapeConfigurationCode")
  public void setTapeConfigurationCode(String tapeConfigurationCode) {
    this.attributes.put("tapeConfigurationCode", tapeConfigurationCode);
    this.tapeConfigurationCode = tapeConfigurationCode;
  }

  public FixedField withTapeConfigurationCode(String tapeConfigurationCode) {
    this.attributes.put("tapeConfigurationCode", tapeConfigurationCode);
    this.tapeConfigurationCode = tapeConfigurationCode;
    return this;
  }

  /**
   * @return The discTypeCode
   */
  @JsonProperty("discTypeCode")
  public String getDiscTypeCode() {
    return discTypeCode;
  }

  /**
   * @param discTypeCode The discTypeCode
   */
  @JsonProperty("discTypeCode")
  public void setDiscTypeCode(String discTypeCode) {
    this.attributes.put("discTypeCode", discTypeCode);
    this.discTypeCode = discTypeCode;
  }

  public FixedField withDiscTypeCode(String discTypeCode) {
    this.attributes.put("discTypeCode", discTypeCode);
    this.discTypeCode = discTypeCode;
    return this;
  }

  /**
   * @return The sndMaterialTypeCode
   */
  @JsonProperty("sndMaterialTypeCode")
  public String getSndMaterialTypeCode() {
    return sndMaterialTypeCode;
  }

  /**
   * @param sndMaterialTypeCode The sndMaterialTypeCode
   */
  @JsonProperty("sndMaterialTypeCode")
  public void setSndMaterialTypeCode(String sndMaterialTypeCode) {
    this.attributes.put("sndMaterialTypeCode", sndMaterialTypeCode);
    this.sndMaterialTypeCode = sndMaterialTypeCode;
  }

  public FixedField withSndMaterialTypeCode(String sndMaterialTypeCode) {
    this.attributes.put("sndMaterialTypeCode", sndMaterialTypeCode);
    this.sndMaterialTypeCode = sndMaterialTypeCode;
    return this;
  }

  /**
   * @return The cuttingTypeCode
   */
  @JsonProperty("cuttingTypeCode")
  public String getCuttingTypeCode() {
    return cuttingTypeCode;
  }

  /**
   * @param cuttingTypeCode The cuttingTypeCode
   */
  @JsonProperty("cuttingTypeCode")
  public void setCuttingTypeCode(String cuttingTypeCode) {
    this.attributes.put("cuttingTypeCode", cuttingTypeCode);
    this.cuttingTypeCode = cuttingTypeCode;
  }

  public FixedField withCuttingTypeCode(String cuttingTypeCode) {
    this.attributes.put("cuttingTypeCode", cuttingTypeCode);
    this.cuttingTypeCode = cuttingTypeCode;
    return this;
  }

  /**
   * @return The specialPlaybackCharacteristicsCode
   */
  @JsonProperty("specialPlaybackCharacteristicsCode")
  public String getSpecialPlaybackCharacteristicsCode() {
    return specialPlaybackCharacteristicsCode;
  }

  /**
   * @param specialPlaybackCharacteristicsCode The
   *        specialPlaybackCharacteristicsCode
   */
  @JsonProperty("specialPlaybackCharacteristicsCode")
  public void setSpecialPlaybackCharacteristicsCode(String specialPlaybackCharacteristicsCode) {
    this.attributes.put("specialPlaybackCharacteristicsCode", specialPlaybackCharacteristicsCode);
    this.specialPlaybackCharacteristicsCode = specialPlaybackCharacteristicsCode;
  }

  public FixedField withSpecialPlaybackCharacteristicsCode(String specialPlaybackCharacteristicsCode) {
    this.attributes.put("specialPlaybackCharacteristicsCode", specialPlaybackCharacteristicsCode);
    this.specialPlaybackCharacteristicsCode = specialPlaybackCharacteristicsCode;
    return this;
  }

  /**
   * @return The storageTechniqueCode
   */
  @JsonProperty("storageTechniqueCode")
  public String getStorageTechniqueCode() {
    return storageTechniqueCode;
  }

  /**
   * @param storageTechniqueCode The storageTechniqueCode
   */
  @JsonProperty("storageTechniqueCode")
  public void setStorageTechniqueCode(String storageTechniqueCode) {
    this.attributes.put("storageTechniqueCode", storageTechniqueCode);
    this.storageTechniqueCode = storageTechniqueCode;
  }

  public FixedField withStorageTechniqueCode(String storageTechniqueCode) {
    this.attributes.put("storageTechniqueCode", storageTechniqueCode);
    this.storageTechniqueCode = storageTechniqueCode;
    return this;
  }

  /**
   * @return The classOfBrailleWritingCode1
   */
  @JsonProperty("classOfBrailleWritingCode1")
  public String getClassOfBrailleWritingCode1() {
    return classOfBrailleWritingCode1;
  }

  /**
   * @param classOfBrailleWritingCode1 The classOfBrailleWritingCode1
   */
  @JsonProperty("classOfBrailleWritingCode1")
  public void setClassOfBrailleWritingCode1(String classOfBrailleWritingCode1) {
    this.attributes.put("classOfBrailleWritingCode1", classOfBrailleWritingCode1);
    this.classOfBrailleWritingCode1 = classOfBrailleWritingCode1;
  }

  public FixedField withClassOfBrailleWritingCode1(String classOfBrailleWritingCode1) {
    this.attributes.put("classOfBrailleWritingCode1", classOfBrailleWritingCode1);
    this.classOfBrailleWritingCode1 = classOfBrailleWritingCode1;
    return this;
  }

  /**
   * @return The classOfBrailleWritingCode2
   */
  @JsonProperty("classOfBrailleWritingCode2")
  public String getClassOfBrailleWritingCode2() {
    return classOfBrailleWritingCode2;
  }

  /**
   * @param classOfBrailleWritingCode2 The classOfBrailleWritingCode2
   */
  @JsonProperty("classOfBrailleWritingCode2")
  public void setClassOfBrailleWritingCode2(String classOfBrailleWritingCode2) {
    this.attributes.put("classOfBrailleWritingCode2", classOfBrailleWritingCode2);
    this.classOfBrailleWritingCode2 = classOfBrailleWritingCode2;
  }

  public FixedField withClassOfBrailleWritingCode2(String classOfBrailleWritingCode2) {
    this.attributes.put("classOfBrailleWritingCode2", classOfBrailleWritingCode2);
    this.classOfBrailleWritingCode2 = classOfBrailleWritingCode2;
    return this;
  }

  /**
   * @return The levelOfContractionCode
   */
  @JsonProperty("levelOfContractionCode")
  public String getLevelOfContractionCode() {
    return levelOfContractionCode;
  }

  /**
   * @param levelOfContractionCode The levelOfContractionCode
   */
  @JsonProperty("levelOfContractionCode")
  public void setLevelOfContractionCode(String levelOfContractionCode) {
    this.attributes.put("levelOfContractionCode", levelOfContractionCode);
    this.levelOfContractionCode = levelOfContractionCode;
  }

  public FixedField withLevelOfContractionCode(String levelOfContractionCode) {
    this.attributes.put("levelOfContractionCode", levelOfContractionCode);
    this.levelOfContractionCode = levelOfContractionCode;
    return this;
  }

  /**
   * @return The brailleMusicFormatCode1
   */
  @JsonProperty("brailleMusicFormatCode1")
  public String getBrailleMusicFormatCode1() {
    return brailleMusicFormatCode1;
  }

  /**
   * @param brailleMusicFormatCode1 The brailleMusicFormatCode1
   */
  @JsonProperty("brailleMusicFormatCode1")
  public void setBrailleMusicFormatCode1(String brailleMusicFormatCode1) {
    this.attributes.put("brailleMusicFormatCode1", brailleMusicFormatCode1);
    this.brailleMusicFormatCode1 = brailleMusicFormatCode1;
  }

  public FixedField withBrailleMusicFormatCode1(String brailleMusicFormatCode1) {
    this.attributes.put("brailleMusicFormatCode1", brailleMusicFormatCode1);
    this.brailleMusicFormatCode1 = brailleMusicFormatCode1;
    return this;
  }

  /**
   * @return The brailleMusicFormatCode2
   */
  @JsonProperty("brailleMusicFormatCode2")
  public String getBrailleMusicFormatCode2() {
    return brailleMusicFormatCode2;
  }

  /**
   * @param brailleMusicFormatCode2 The brailleMusicFormatCode2
   */
  @JsonProperty("brailleMusicFormatCode2")
  public void setBrailleMusicFormatCode2(String brailleMusicFormatCode2) {
    this.attributes.put("brailleMusicFormatCode2", brailleMusicFormatCode2);
    this.brailleMusicFormatCode2 = brailleMusicFormatCode2;
  }

  public FixedField withBrailleMusicFormatCode2(String brailleMusicFormatCode2) {
    this.attributes.put("brailleMusicFormatCode2", brailleMusicFormatCode2);
    this.brailleMusicFormatCode2 = brailleMusicFormatCode2;
    return this;
  }

  /**
   * @return The brailleMusicFormatCode3
   */
  @JsonProperty("brailleMusicFormatCode3")
  public String getBrailleMusicFormatCode3() {
    return brailleMusicFormatCode3;
  }

  /**
   * @param brailleMusicFormatCode3 The brailleMusicFormatCode3
   */
  @JsonProperty("brailleMusicFormatCode3")
  public void setBrailleMusicFormatCode3(String brailleMusicFormatCode3) {
    this.attributes.put("brailleMusicFormatCode3", brailleMusicFormatCode3);
    this.brailleMusicFormatCode3 = brailleMusicFormatCode3;
  }

  public FixedField withBrailleMusicFormatCode3(String brailleMusicFormatCode3) {
    this.attributes.put("brailleMusicFormatCode3", brailleMusicFormatCode3);
    this.brailleMusicFormatCode3 = brailleMusicFormatCode3;
    return this;
  }

  /**
   * @return The specificPhysicalCharacteristicsCode
   */
  @JsonProperty("specificPhysicalCharacteristicsCode")
  public String getSpecificPhysicalCharacteristicsCode() {
    return specificPhysicalCharacteristicsCode;
  }

  /**
   * @param specificPhysicalCharacteristicsCode The
   *        specificPhysicalCharacteristicsCode
   */
  @JsonProperty("specificPhysicalCharacteristicsCode")
  public void setSpecificPhysicalCharacteristicsCode(String specificPhysicalCharacteristicsCode) {
    this.attributes.put("specificPhysicalCharacteristicsCode", specificPhysicalCharacteristicsCode);
    this.specificPhysicalCharacteristicsCode = specificPhysicalCharacteristicsCode;
  }

  public FixedField withSpecificPhysicalCharacteristicsCode(String specificPhysicalCharacteristicsCode) {
    this.attributes.put("specificPhysicalCharacteristicsCode", specificPhysicalCharacteristicsCode);
    this.specificPhysicalCharacteristicsCode = specificPhysicalCharacteristicsCode;
    return this;
  }

  /**
   * @return The formatCode
   */
  @JsonProperty("formatCode")
  public String getFormatCode() {
    return formatCode;
  }

  /**
   * @param formatCode The formatCode
   */
  @JsonProperty("formatCode")
  public void setFormatCode(String formatCode) {
    this.attributes.put("formatCode", formatCode);
    this.formatCode = formatCode;
  }

  public FixedField withFormatCode(String formatCode) {
    this.attributes.put("formatCode", formatCode);
    this.formatCode = formatCode;
    return this;
  }

  /**
   * @return The subjectDescriptor
   */
  public String getSubjectDescriptor() {
    return subjectDescriptor;
  }

  /**
   * @param subjectDescriptor The subjectDescriptor
   */
  public void setSubjectDescriptor(String subjectDescriptor) {
    this.attributes.put("subjectDescriptor", subjectDescriptor);
    this.subjectDescriptor = subjectDescriptor;
  }

  /**
   * @return The romanizationScheme
   */
  public String getRomanizationScheme() {
    return romanizationScheme;
  }

  /**
   * @param romanizationScheme The romanizationScheme
   */
  public void setRomanizationScheme(String romanizationScheme) {
    this.attributes.put("romanizationScheme", romanizationScheme);
    this.romanizationScheme = romanizationScheme;
  }

  /**
   * @return The bilingualUsage
   */
  public String getBilingualUsage() {
    return bilingualUsage;
  }

  /**
   * @param bilingualUsage The bilingualUsage
   */
  public void setBilingualUsage(String bilingualUsage) {
    this.attributes.put("bilingualUsage", bilingualUsage);
    this.bilingualUsage = bilingualUsage;
  }

  /**
   * @return The recordType
   */
  public String getRecordType() {
    return recordType;
  }

  /**
   * @param recordType The recordType
   */
  public void setRecordType(String recordType) {
    this.attributes.put("recordType", recordType);
    this.recordType = recordType;
  }

  /**
   * @return The cataloguingRules
   */
  public String getCataloguingRules() {
    return cataloguingRules;
  }

  /**
   * @param cataloguingRules The cataloguingRules
   */
  public void setCataloguingRules(String cataloguingRules) {
    this.attributes.put("cataloguingRules", cataloguingRules);
    this.cataloguingRules = cataloguingRules;
  }

  /**
   * @return The subjectSystem
   */
  public String getSubjectSystem() {
    return subjectSystem;
  }

  /**
   * @param subjectSystem The subjectSystem
   */
  public void setSubjectSystem(String subjectSystem) {
    this.attributes.put("subjectSystem", subjectSystem);
    this.subjectSystem = subjectSystem;
  }

  /**
   * @return The seriesType
   */
  public String getSeriesType() {
    return seriesType;
  }

  /**
   * @param seriesType The seriesType
   */
  public void setSeriesType(String seriesType) {
    this.attributes.put("seriesType", seriesType);
    this.seriesType = seriesType;
  }

  /**
   * @return The seriesNumbering
   */
  public String getSeriesNumbering() {
    return seriesNumbering;
  }

  /**
   * @param seriesNumbering The seriesNumbering
   */
  public void setSeriesNumbering(String seriesNumbering) {
    this.attributes.put("seriesNumbering", seriesNumbering);
    this.seriesNumbering = seriesNumbering;
  }

  /**
   * @return The mainAddedEntryIndicator
   */
  public String getMainAddedEntryIndicator() {
    return mainAddedEntryIndicator;
  }

  /**
   * @param mainAddedEntryIndicator The mainAddedEntryIndicator
   */
  public void setMainAddedEntryIndicator(String mainAddedEntryIndicator) {
    this.attributes.put("mainAddedEntryIndicator", mainAddedEntryIndicator);
    this.mainAddedEntryIndicator = mainAddedEntryIndicator;
  }

  /**
   * @return The subjectEntryIndicator
   */
  public String getSubjectEntryIndicator() {
    return subjectEntryIndicator;
  }

  /**
   * @param subjectEntryIndicator The subjectEntryIndicator
   */
  public void setSubjectEntryIndicator(String subjectEntryIndicator) {
    this.attributes.put("subjectEntryIndicator", subjectEntryIndicator);
    this.subjectEntryIndicator = subjectEntryIndicator;
  }

  /**
   * @return The seriesEntryIndicator
   */
  public String getSeriesEntryIndicator() {
    return seriesEntryIndicator;
  }

  /**
   * @param seriesEntryIndicator The seriesEntryIndicator
   */
  public void setSeriesEntryIndicator(String seriesEntryIndicator) {
    this.attributes.put("seriesEntryIndicator", seriesEntryIndicator);
    this.seriesEntryIndicator = seriesEntryIndicator;
  }

  /**
   * @return The subDivisionType
   */
  public String getSubDivisionType() {
    return subDivisionType;
  }

  /**
   * @param subDivisionType The subDivisionType
   */
  public void setSubDivisionType(String subDivisionType) {
    this.attributes.put("subDivisionType", subDivisionType);
    this.subDivisionType = subDivisionType;
  }

  /**
   * @return The governmentAgency
   */
  public String getGovernmentAgency() {
    return governmentAgency;
  }

  /**
   * @param governmentAgency The governmentAgency
   */
  public void setGovernmentAgency(String governmentAgency) {
    this.attributes.put("governmentAgency", governmentAgency);
    this.governmentAgency = governmentAgency;
  }

  /**
   * @return The referenceStatus
   */
  public String getReferenceStatus() {
    return referenceStatus;
  }

  /**
   * @param referenceStatus The referenceStatus
   */
  public void setReferenceStatus(String referenceStatus) {
    this.attributes.put("referenceStatus", referenceStatus);
    this.referenceStatus = referenceStatus;
  }

  /**
   * @return The recordRevision
   */
  public String getRecordRevision() {
    return recordRevision;
  }

  /**
   * @param recordRevision The recordRevision
   */
  public void setRecordRevision(String recordRevision) {
    this.attributes.put("recordRevision", recordRevision);
    this.recordRevision = recordRevision;
  }

  /**
   * @return The nonUniqueName
   */
  public String getNonUniqueName() {
    return nonUniqueName;
  }

  /**
   * @param nonUniqueName The nonUniqueName
   */
  public void setNonUniqueName(String nonUniqueName) {
    this.attributes.put("nonUniqueName", nonUniqueName);
    this.nonUniqueName = nonUniqueName;
  }

  /**
   * @return The headingStatus
   */
  public String getHeadingStatus() {
    return headingStatus;
  }

  /**
   * @param headingStatus The headingStatus
   */
  public void setHeadingStatus(String headingStatus) {
    this.attributes.put("headingStatus", headingStatus);
    this.headingStatus = headingStatus;
  }

  /**
   * @return The recordModification
   */
  public String getRecordModification() {
    return recordModification;
  }

  /**
   * @param recordModification The recordModification
   */
  public void setRecordModification(String recordModification) {
    this.attributes.put("recordModification", recordModification);
    this.recordModification = recordModification;
  }

  /**
   * @return The cataloguingSourceCode
   */
  public String getCataloguingSourceCode() {
    return cataloguingSourceCode;
  }

  /**
   * @param cataloguingSourceCode The cataloguingSourceCode
   */
  public void setCataloguingSourceCoder(String cataloguingSourceCode) {
    this.attributes.put("cataloguingSourceCode", cataloguingSourceCode);
    this.cataloguingSourceCode = cataloguingSourceCode;
  }

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

  /**
   * @return The categoryCode
   */
  @JsonProperty("categoryCode")
  public Integer getCategoryCode() {
    return categoryCode;
  }

  /**
   * @param categoryCode The categoryCode
   */
  @JsonProperty("categoryCode")
  public void setCategoryCode(Integer categoryCode) {
    this.categoryCode = categoryCode;
  }

  /**
   * @return The description
   */
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  /**
   * @param description The description
   */
  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return The sequenceNumber
   */
  @JsonProperty("sequenceNumber")
  public int getSequenceNumber() {
    return sequenceNumber;
  }

  /**
   * @param sequenceNumber The sequenceNumber
   */
  @JsonProperty("sequenceNumber")
  public void setSequenceNumber(int sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  /**
   * @return The attributes
   */
  @JsonProperty("attributes")
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Generated("org.jsonschema2pojo")
  public static enum MaterialType {

    MUSIC("music"),
    MAP("map"),
    COMPUTER_FILE("computerFile"),
    BOOK("book"),
    MIXED_MATERIAL("mixedMaterial"),
    CONTINUING_RESOURCE("continuingResource"),
    VISUAL_MATERIAL("visualMaterial"),
    UNDEFINED("undefined");

    private static final Map<String, MaterialType> CONSTANTS = new HashMap<String, MaterialType>();

    static {
      for (MaterialType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    private MaterialType(String value) {
      this.value = value;
    }

    @JsonCreator
    public static MaterialType fromValue(String value) {
      MaterialType constant = CONSTANTS.get(value);
      if (constant == null) {
        throw new IllegalArgumentException(value);
      } else {
        return constant;
      }
    }

    @JsonValue
    @Override
    public String toString() {
      return this.value;
    }
  }

  @Generated("org.jsonschema2pojo")
  public static enum PhysicalType {

    MAP("map"),
    ELECTRONICAL_RESOURCE("electronicalResource"),
    GLOBE("globe"), TACTILE_MATERIAL("tactileMaterial"),
    PROJECTED_GRAPHIC("projectedGraphic"),
    MICROFORM("microform"),
    NON_PROJECTED_GRAPHIC("nonProjectedGraphic"),
    MOTION_PICTURE("motionPicture"), KIT("kit"),
    NOTATED_MUSIC("notatedMusic"),
    REMOTE_SENSING_IMAGE("remoteSensingImage"),
    SOUND_RECORDING("soundRecording"),
    TEXT("text"),
    VIDEO_RECORDING("videoRecording"),
    UNSPECIFIED("unspecified");

    private static final Map<String, PhysicalType> CONSTANTS = new HashMap<String, PhysicalType>();

    static {
      for (PhysicalType c : values()) {
        CONSTANTS.put(c.value, c);
      }
    }

    private final String value;

    private PhysicalType(String value) {
      this.value = value;
    }

    @JsonCreator
    public static PhysicalType fromValue(String value) {
      PhysicalType constant = CONSTANTS.get(value);
      if (constant == null) {
        throw new IllegalArgumentException(value);
      } else {
        return constant;
      }
    }

    @JsonValue
    @Override
    public String toString() {
      return this.value;
    }
  }

}
