package org.folio.marccat.shared;

import org.folio.marccat.config.constants.Global;

/**
 * Class related to:
 * 007 - Physical Description Fixed Field
 * Contains only methods of logic to manage tag 007.
 *
 * @author nbianchini
 */

public class PhysicalInformation {

  private int headerType;
  private String categoryOfMaterial;
  private String specificMaterialDesignationCode;

  private String colourCode;
  private String physicalMediumCode;
  private String typeOfReproductionCode;
  private String polarityCode;
  private String dimensionsCode;
  private String generationCode;
  private String baseOfFilmCode;
  private String includesSoundCode;
  private String mediumForSoundCode;
  private String secondarySupportMaterialCode;
  private String configurationCode;
  private String obsolete1;
  private String obsolete2;

  /* specific for Electronic Resource */
  private String imageBitDepth;
  private String fileFormatsCode;
  private String qualityAssuranceTargetCode;
  private String antecedentSourceCode;
  private String levelOfCompressionCode;
  private String reformattingQualityCode;

  /* specific for Map */
  private String productionDetailsCode;

  /* specific for Microform */
  private String reductionRatioRangeCode;
  private String reductionRatioCode;
  private String emulsionOnFilmCode;

  /* specific for Motion Picture*/
  private String presentationFormatCode;
  private String productionElementsCode;
  private String refinedCategoriesOfColourCode;
  private String kindOfColourStockCode;
  private String deteriorationStageCode;
  private String completenessCode;
  private String inspectionDate;

  /* specific for Non Projected Graphic*/
  private String primarySupportMaterialCode;

  /* specific for Projected Graphic */
  private String baseOfEmulsionCode;
  private String soundOnMediumOrSeparateCode;

  /* specific for Remote Sensing Image */
  private String altitudeOfSensorCode;
  private String attitudeOfSensorCode;
  private String cloudCoverCode;
  private String platformConstructionTypeCode;
  private String platformUseCode;
  private String sensorTypeCode;
  private String dataTypeCode;

  /* specific for Sound Recording*/
  private String speedCode;
  private String grooveWidthCode;
  private String tapeWidthCode;
  private String tapeConfigurationCode;
  private String discTypeCode;
  private String sndMaterialTypeCode;
  private String cuttingTypeCode;
  private String specialPlaybackCharacteristicsCode;
  private String storageTechniqueCode;

  /* specific for Tactile Material*/
  private String classOfBrailleWritingCodes;
  private String levelOfContractionCode;
  private String brailleMusicFormatCodes;
  private String specificPhysicalCharacteristicsCode;

  /* specific for Video Recording */
  private String formatCode;

  /**
   * Checks if is a map type.
   *
   * @return true if is a map type false otherwise.
   */
  public boolean isMap(final String categoryOfMaterial) {
    return categoryOfMaterial.equals(Global.MAP_CODE);
  }

  /**
   * Checks if is an electronic resource type.
   *
   * @return true if is a electronic resource type false otherwise.
   */
  public boolean isElectronicResource(final String categoryOfMaterial) {
    return categoryOfMaterial.equals(Global.ELECTRONIC_RESOURCE);
  }

  /**
   * Checks if is a GLOBE type.
   *
   * @return true if is a GLOBE type false otherwise.
   */
  public boolean isGlobe(final String categoryOfMaterial) {
    return categoryOfMaterial.equals(Global.GLOBE);
  }

  /**
   * Checks if is a tactile material type.
   *
   * @return true if is a tactile material type false otherwise.
   */
  public boolean isTactileMaterial(final String categoryOfMaterial) {
    return categoryOfMaterial.equals(Global.TACTILE_MATERIAL);
  }

  /**
   * Checks if is a projected graphic type.
   *
   * @return true if is a projected graphic type false otherwise.
   */
  public boolean isProjectedGraphic(final String categoryOfMaterial) {
    return categoryOfMaterial.equals(Global.PROJECTED_GRAPHIC);
  }

  /**
   * Checks if is a MICROFORM type.
   *
   * @return true if is a MICROFORM type false otherwise.
   */
  public boolean isMicroform(final String categoryOfMaterial) {
    return categoryOfMaterial.equals(Global.MICROFORM);
  }

  /**
   * Checks if is a non projected graphic type.
   *
   * @return true if is a non projected graphic type false otherwise.
   */
  public boolean isNonProjectedGraphic(final String categoryOfMaterial) {
    return categoryOfMaterial.equals(Global.NON_PROJECTED_GRAPHIC);
  }

  /**
   * Checks if is a motion picture type.
   *
   * @return true if is a motion picture type false otherwise.
   */
  public boolean isMotionPicture(final String categoryOfMaterial) {
    return categoryOfMaterial.equals(Global.MOTION_PICTURE);
  }

  /**
   * Checks if is a kit type.
   *
   * @return true if is a kit type false otherwise.
   */
  public boolean isKit(final String categoryOfMaterial) {
    return categoryOfMaterial.equals(Global.KIT_CODE);
  }

  /**
   * Checks if is a notated music type.
   *
   * @return true if is a notated music type false otherwise.
   */
  public boolean isNotatedMusic(final String categoryOfMaterial) {
    return categoryOfMaterial.equals(Global.NOTATED_MUSIC);
  }

  /**
   * Checks if is a remote sensing image type.
   *
   * @return true if is a remote sensing image type false otherwise.
   */
  public boolean isRemoteSensingImage(final String categoryOfMaterial) {
    return categoryOfMaterial.equals(Global.REMOTE_SENSING_IMAGE);
  }

  /**
   * Checks if is a sound recording type.
   *
   * @return true if is a sound recording type false otherwise.
   */
  public boolean isSoundRecording(final String categoryOfMaterial) {
    return categoryOfMaterial.equals(Global.SOUND_RECORDING);
  }

  /**
   * Checks if is a text type.
   *
   * @return true if is a text type false otherwise.
   */
  public boolean isText(final String categoryOfMaterial) {
    return categoryOfMaterial.equals(Global.TEXT_CODE);
  }

  /**
   * Checks if is a video recording type.
   *
   * @return true if is a video recording type false otherwise.
   */
  public boolean isVideoRecording(final String categoryOfMaterial) {
    return categoryOfMaterial.equals(Global.VIDEO_RECORDING);
  }

  /**
   * Checks if is a UNSPECIFIED type.
   *
   * @return true if is a UNSPECIFIED type false otherwise.
   */
  public boolean isUnspecified(final String categoryOfMaterial) {
    return categoryOfMaterial.equals(Global.UNSPECIFIED);
  }

  /**
   * The default string value text associated to category of material type.
   *
   * @param categoryOfMaterial the category of material.
   * @return the value string text.
   */
  public String getValueString(final String categoryOfMaterial) {
    if (isMap(categoryOfMaterial))
      return Global.MAP_CODE + "d" + " " + "aunun";
    else if (isElectronicResource(categoryOfMaterial))
      return Global.ELECTRONIC_RESOURCE + "u" + " " + "uuu---uuuuu";
    else if (isGlobe(categoryOfMaterial))
      return Global.GLOBE + "u" + " " + "auu";
    else if (isTactileMaterial(categoryOfMaterial))
      return Global.TACTILE_MATERIAL + "u" + " " + "uuuuuuu";
    else if (isProjectedGraphic(categoryOfMaterial))
      return Global.PROJECTED_GRAPHIC + "u" + " " + "uuuuuu";
    else if (isMicroform(categoryOfMaterial))
      return Global.MICROFORM + "u" + " " + "uuu---uuuu";
    else if (isNonProjectedGraphic(categoryOfMaterial))
      return Global.NON_PROJECTED_GRAPHIC + "u" + " " + "uuu";
    else if (isMotionPicture(categoryOfMaterial))
      return Global.MOTION_PICTURE + "u" + " " + "uuuuuu|uuuuu|u||||||";
    else if (isRemoteSensingImage(categoryOfMaterial))
      return Global.REMOTE_SENSING_IMAGE + "u" + " " + "uuuuuuuu";
    else if (isSoundRecording(categoryOfMaterial))
      return Global.SOUND_RECORDING + "u" + " " + "uuuuuuuuuuu";
    else if (isKit(categoryOfMaterial))
      return Global.KIT_CODE + "u";
    else if (isNotatedMusic(categoryOfMaterial))
      return Global.NOTATED_MUSIC + "u";
    else if (isText(categoryOfMaterial))
      return Global.TEXT_CODE + "u";
    else if (isVideoRecording(categoryOfMaterial))
      return Global.VIDEO_RECORDING + "u" + " " + "uuuuuu";
    else if (isUnspecified(categoryOfMaterial))
      return Global.UNSPECIFIED + "u";

    return null;
  }


  /* GETTERS AND SETTERS */
  public int getHeaderType() {
    return headerType;
  }

  public void setHeaderType(int headerType) {
    this.headerType = headerType;
  }

  public String getCategoryOfMaterial() {
    return categoryOfMaterial;
  }

  public void setCategoryOfMaterial(String categoryOfMaterial) {
    this.categoryOfMaterial = categoryOfMaterial;
  }

  public String getSpecificMaterialDesignationCode() {
    return specificMaterialDesignationCode;
  }

  public void setSpecificMaterialDesignationCode(String specificMaterialDesignationCode) {
    this.specificMaterialDesignationCode = specificMaterialDesignationCode;
  }

  public String getColourCode() {
    return colourCode;
  }

  public void setColourCode(String colourCode) {
    this.colourCode = colourCode;
  }

  public String getPhysicalMediumCode() {
    return physicalMediumCode;
  }

  public void setPhysicalMediumCode(String physicalMediumCode) {
    this.physicalMediumCode = physicalMediumCode;
  }

  public String getTypeOfReproductionCode() {
    return typeOfReproductionCode;
  }

  public void setTypeOfReproductionCode(String typeOfReproductionCode) {
    this.typeOfReproductionCode = typeOfReproductionCode;
  }

  public String getPolarityCode() {
    return polarityCode;
  }

  public void setPolarityCode(String polarityCode) {
    this.polarityCode = polarityCode;
  }

  public String getDimensionsCode() {
    return dimensionsCode;
  }

  public void setDimensionsCode(String dimensionsCode) {
    this.dimensionsCode = dimensionsCode;
  }

  public String getGenerationCode() {
    return generationCode;
  }

  public void setGenerationCode(String generationCode) {
    this.generationCode = generationCode;
  }

  public String getBaseOfFilmCode() {
    return baseOfFilmCode;
  }

  public void setBaseOfFilmCode(String baseOfFilmCode) {
    this.baseOfFilmCode = baseOfFilmCode;
  }

  public String getIncludesSoundCode() {
    return includesSoundCode;
  }

  public void setIncludesSoundCode(String includesSoundCode) {
    this.includesSoundCode = includesSoundCode;
  }

  public String getMediumForSoundCode() {
    return mediumForSoundCode;
  }

  public void setMediumForSoundCode(String mediumForSoundCode) {
    this.mediumForSoundCode = mediumForSoundCode;
  }

  public String getSecondarySupportMaterialCode() {
    return secondarySupportMaterialCode;
  }

  public void setSecondarySupportMaterialCode(String secondarySupportMaterialCode) {
    this.secondarySupportMaterialCode = secondarySupportMaterialCode;
  }

  public String getConfigurationCode() {
    return configurationCode;
  }

  public void setConfigurationCode(String configurationCode) {
    this.configurationCode = configurationCode;
  }

  public String getObsolete1() {
    return obsolete1;
  }

  public void setObsolete1(String obsolete1) {
    this.obsolete1 = obsolete1;
  }

  public String getObsolete2() {
    return obsolete2;
  }

  public void setObsolete2(String obsolete2) {
    this.obsolete2 = obsolete2;
  }

  public String getImageBitDepth() {
    return imageBitDepth;
  }

  public void setImageBitDepth(String imageBitDepth) {
    this.imageBitDepth = imageBitDepth;
  }

  public String getFileFormatsCode() {
    return fileFormatsCode;
  }

  public void setFileFormatsCode(String fileFormatsCode) {
    this.fileFormatsCode = fileFormatsCode;
  }

  public String getQualityAssuranceTargetCode() {
    return qualityAssuranceTargetCode;
  }

  public void setQualityAssuranceTargetCode(String qualityAssuranceTargetCode) {
    this.qualityAssuranceTargetCode = qualityAssuranceTargetCode;
  }

  public String getAntecedentSourceCode() {
    return antecedentSourceCode;
  }

  public void setAntecedentSourceCode(String antecedentSourceCode) {
    this.antecedentSourceCode = antecedentSourceCode;
  }

  public String getLevelOfCompressionCode() {
    return levelOfCompressionCode;
  }

  public void setLevelOfCompressionCode(String levelOfCompressionCode) {
    this.levelOfCompressionCode = levelOfCompressionCode;
  }

  public String getReformattingQualityCode() {
    return reformattingQualityCode;
  }

  public void setReformattingQualityCode(String reformattingQualityCode) {
    this.reformattingQualityCode = reformattingQualityCode;
  }

  public String getProductionDetailsCode() {
    return productionDetailsCode;
  }

  public void setProductionDetailsCode(String productionDetailsCode) {
    this.productionDetailsCode = productionDetailsCode;
  }

  public String getReductionRatioRangeCode() {
    return reductionRatioRangeCode;
  }

  public void setReductionRatioRangeCode(String reductionRatioRangeCode) {
    this.reductionRatioRangeCode = reductionRatioRangeCode;
  }

  public String getReductionRatioCode() {
    return reductionRatioCode;
  }

  public void setReductionRatioCode(String reductionRatioCode) {
    this.reductionRatioCode = reductionRatioCode;
  }

  public String getEmulsionOnFilmCode() {
    return emulsionOnFilmCode;
  }

  public void setEmulsionOnFilmCode(String emulsionOnFilmCode) {
    this.emulsionOnFilmCode = emulsionOnFilmCode;
  }

  public String getPresentationFormatCode() {
    return presentationFormatCode;
  }

  public void setPresentationFormatCode(String presentationFormatCode) {
    this.presentationFormatCode = presentationFormatCode;
  }

  public String getProductionElementsCode() {
    return productionElementsCode;
  }

  public void setProductionElementsCode(String productionElementsCode) {
    this.productionElementsCode = productionElementsCode;
  }

  public String getRefinedCategoriesOfColourCode() {
    return refinedCategoriesOfColourCode;
  }

  public void setRefinedCategoriesOfColourCode(String refinedCategoriesOfColourCode) {
    this.refinedCategoriesOfColourCode = refinedCategoriesOfColourCode;
  }

  public String getKindOfColourStockCode() {
    return kindOfColourStockCode;
  }

  public void setKindOfColourStockCode(String kindOfColourStockCode) {
    this.kindOfColourStockCode = kindOfColourStockCode;
  }

  public String getDeteriorationStageCode() {
    return deteriorationStageCode;
  }

  public void setDeteriorationStageCode(String deteriorationStageCode) {
    this.deteriorationStageCode = deteriorationStageCode;
  }

  public String getCompletenessCode() {
    return completenessCode;
  }

  public void setCompletenessCode(String completenessCode) {
    this.completenessCode = completenessCode;
  }

  public String getInspectionDate() {
    return inspectionDate;
  }

  public void setInspectionDate(String inspectionDate) {
    this.inspectionDate = inspectionDate;
  }

  public String getPrimarySupportMaterialCode() {
    return primarySupportMaterialCode;
  }

  public void setPrimarySupportMaterialCode(String primarySupportMaterialCode) {
    this.primarySupportMaterialCode = primarySupportMaterialCode;
  }

  public String getBaseOfEmulsionCode() {
    return baseOfEmulsionCode;
  }

  public void setBaseOfEmulsionCode(String baseOfEmulsionCode) {
    this.baseOfEmulsionCode = baseOfEmulsionCode;
  }

  public String getSoundOnMediumOrSeparateCode() {
    return soundOnMediumOrSeparateCode;
  }

  public void setSoundOnMediumOrSeparateCode(String soundOnMediumOrSeparateCode) {
    this.soundOnMediumOrSeparateCode = soundOnMediumOrSeparateCode;
  }

  public String getAltitudeOfSensorCode() {
    return altitudeOfSensorCode;
  }

  public void setAltitudeOfSensorCode(String altitudeOfSensorCode) {
    this.altitudeOfSensorCode = altitudeOfSensorCode;
  }

  public String getAttitudeOfSensorCode() {
    return attitudeOfSensorCode;
  }

  public void setAttitudeOfSensorCode(String attitudeOfSensorCode) {
    this.attitudeOfSensorCode = attitudeOfSensorCode;
  }

  public String getCloudCoverCode() {
    return cloudCoverCode;
  }

  public void setCloudCoverCode(String cloudCoverCode) {
    this.cloudCoverCode = cloudCoverCode;
  }

  public String getPlatformConstructionTypeCode() {
    return platformConstructionTypeCode;
  }

  public void setPlatformConstructionTypeCode(String platformConstructionTypeCode) {
    this.platformConstructionTypeCode = platformConstructionTypeCode;
  }

  public String getPlatformUseCode() {
    return platformUseCode;
  }

  public void setPlatformUseCode(String platformUseCode) {
    this.platformUseCode = platformUseCode;
  }

  public String getSensorTypeCode() {
    return sensorTypeCode;
  }

  public void setSensorTypeCode(String sensorTypeCode) {
    this.sensorTypeCode = sensorTypeCode;
  }

  public String getDataTypeCode() {
    return dataTypeCode;
  }

  public void setDataTypeCode(String dataTypeCode) {
    this.dataTypeCode = dataTypeCode;
  }

  public String getSpeedCode() {
    return speedCode;
  }

  public void setSpeedCode(String speedCode) {
    this.speedCode = speedCode;
  }

  public String getGrooveWidthCode() {
    return grooveWidthCode;
  }

  public void setGrooveWidthCode(String grooveWidthCode) {
    this.grooveWidthCode = grooveWidthCode;
  }

  public String getTapeWidthCode() {
    return tapeWidthCode;
  }

  public void setTapeWidthCode(String tapeWidthCode) {
    this.tapeWidthCode = tapeWidthCode;
  }

  public String getTapeConfigurationCode() {
    return tapeConfigurationCode;
  }

  public void setTapeConfigurationCode(String tapeConfigurationCode) {
    this.tapeConfigurationCode = tapeConfigurationCode;
  }

  public String getDiscTypeCode() {
    return discTypeCode;
  }

  public void setDiscTypeCode(String discTypeCode) {
    this.discTypeCode = discTypeCode;
  }

  public String getSndMaterialTypeCode() {
    return sndMaterialTypeCode;
  }

  public void setSndMaterialTypeCode(String sndMaterialTypeCode) {
    this.sndMaterialTypeCode = sndMaterialTypeCode;
  }

  public String getCuttingTypeCode() {
    return cuttingTypeCode;
  }

  public void setCuttingTypeCode(String cuttingTypeCode) {
    this.cuttingTypeCode = cuttingTypeCode;
  }

  public String getSpecialPlaybackCharacteristicsCode() {
    return specialPlaybackCharacteristicsCode;
  }

  public void setSpecialPlaybackCharacteristicsCode(String specialPlaybackCharacteristicsCode) {
    this.specialPlaybackCharacteristicsCode = specialPlaybackCharacteristicsCode;
  }

  public String getStorageTechniqueCode() {
    return storageTechniqueCode;
  }

  public void setStorageTechniqueCode(String storageTechniqueCode) {
    this.storageTechniqueCode = storageTechniqueCode;
  }

  public String getClassOfBrailleWritingCodes() {
    return classOfBrailleWritingCodes;
  }

  public void setClassOfBrailleWritingCodes(String classOfBrailleWritingCodes) {
    this.classOfBrailleWritingCodes = classOfBrailleWritingCodes;
  }

  public String getLevelOfContractionCode() {
    return levelOfContractionCode;
  }

  public void setLevelOfContractionCode(String levelOfContractionCode) {
    this.levelOfContractionCode = levelOfContractionCode;
  }

  public String getBrailleMusicFormatCodes() {
    return brailleMusicFormatCodes;
  }

  public void setBrailleMusicFormatCodes(String brailleMusicFormatCodes) {
    this.brailleMusicFormatCodes = brailleMusicFormatCodes;
  }

  public String getSpecificPhysicalCharacteristicsCode() {
    return specificPhysicalCharacteristicsCode;
  }

  public void setSpecificPhysicalCharacteristicsCode(String specificPhysicalCharacteristicsCode) {
    this.specificPhysicalCharacteristicsCode = specificPhysicalCharacteristicsCode;
  }

  public String getFormatCode() {
    return formatCode;
  }

  public void setFormatCode(String formatCode) {
    this.formatCode = formatCode;
  }


}
