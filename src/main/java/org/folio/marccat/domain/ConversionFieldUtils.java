package org.folio.marccat.domain;

import org.folio.marccat.config.Global;
import org.folio.marccat.resources.domain.FixedField;
import org.folio.marccat.shared.GeneralInformation;

import static java.util.Optional.ofNullable;

public class ConversionFieldUtils {


  /**
   * Inject leader values for drop-down list selected.
   *
   * @param fixedField the fixedField to populate.
   */
  public static void setLeaderValuesInFixedField(final FixedField fixedField) {
    final String leaderValue = fixedField.getDisplayValue();
    if (leaderValue == null)
      return;

    fixedField.setItemRecordStatusCode(String.valueOf(leaderValue.charAt(5)));
    fixedField.setItemRecordTypeCode(String.valueOf(leaderValue.charAt(6)));
    fixedField.setItemBibliographicLevelCode(String.valueOf(leaderValue.charAt(7)));
    fixedField.setItemControlTypeCode(String.valueOf(leaderValue.charAt(8)));
    fixedField.setCharacterCodingSchemeCode(String.valueOf(leaderValue.charAt(9)));
    fixedField.setEncodingLevel(String.valueOf(leaderValue.charAt(17)));
    fixedField.setDescriptiveCataloguingCode(String.valueOf(leaderValue.charAt(18)));
    fixedField.setLinkedRecordCode(String.valueOf(leaderValue.charAt(19)));
  }

  /**
   * Inject material or other material values for drop-down list selected.
   *
   * @param fixedField the fixedField to populate.
   */
  public static void setMaterialValuesInFixedField(final FixedField fixedField, final String formOfMaterial) {

    final GeneralInformation gi = new GeneralInformation();
    gi.setFormOfMaterial(formOfMaterial);
    gi.setMaterialDescription008Indicator(fixedField.getCode().equalsIgnoreCase(Global.MATERIAL_TAG_CODE) ? "1" : "0");
    final String displayValue = fixedField.getDisplayValue();

    int startPosition = 1;
    if ("1".equals(gi.getMaterialDescription008Indicator())) {
      startPosition = 18;
      fixedField.setDateEnteredOnFile(displayValue.substring(0, 6));
      fixedField.setDateTypeCode(String.valueOf(displayValue.charAt(6)));
      fixedField.setDateFirstPublication(displayValue.substring(7, 11));
      fixedField.setDateLastPublication(displayValue.substring(11, 15));
      fixedField.setPlaceOfPublication(displayValue.substring(15, 18));
      fixedField.setLanguageCode(displayValue.substring(35, 38));
      fixedField.setRecordModifiedCode(String.valueOf(displayValue.charAt(38)));
      fixedField.setRecordCataloguingSourceCode(String.valueOf(displayValue.charAt(39)));
    } else { //006
      fixedField.setMaterialTypeCode(String.valueOf(displayValue.charAt(0)));
    }

    if (gi.isBook()) {
      fixedField.setBookIllustrationCode1(String.valueOf(displayValue.charAt(startPosition)));
      fixedField.setBookIllustrationCode2(String.valueOf(displayValue.charAt(startPosition + 1)));
      fixedField.setBookIllustrationCode2(String.valueOf(displayValue.charAt(startPosition + 2)));
      fixedField.setBookIllustrationCode2(String.valueOf(displayValue.charAt(startPosition + 3)));
      fixedField.setTargetAudienceCode(String.valueOf(displayValue.charAt(startPosition + 4)));
      fixedField.setFormOfItemCode(String.valueOf(displayValue.charAt(startPosition + 5)));
      fixedField.setNatureOfContent1(String.valueOf(displayValue.charAt(startPosition + 6)));
      fixedField.setNatureOfContent2(String.valueOf(displayValue.charAt(startPosition + 7)));
      fixedField.setNatureOfContent3(String.valueOf(displayValue.charAt(startPosition + 8)));
      fixedField.setNatureOfContent4(String.valueOf(displayValue.charAt(startPosition + 9)));
      fixedField.setGovernmentPublicationCode(String.valueOf(displayValue.charAt(startPosition + 10)));
      fixedField.setConferencePublicationCode(String.valueOf(displayValue.charAt(startPosition + 11)));
      fixedField.setBookFestschrift(String.valueOf(displayValue.charAt(startPosition + 12)));
      fixedField.setBookIndexAvailabilityCode(String.valueOf(displayValue.charAt(startPosition + 13)));
      fixedField.setBookLiteraryFormTypeCode(String.valueOf(displayValue.charAt(startPosition + 15)));
      fixedField.setBookBiographyCode(String.valueOf(displayValue.charAt(startPosition + 16)));
      fixedField.setMaterialType(FixedField.MaterialType.BOOK);
    } else if (gi.isSerial()) {
      fixedField.setSerialFrequencyCode(String.valueOf(displayValue.charAt(startPosition)));
      fixedField.setSerialRegularityCode(String.valueOf(displayValue.charAt(startPosition + 1)));
      fixedField.setSerialTypeCode(String.valueOf(displayValue.charAt(startPosition + 2)));
      fixedField.setSerialFormOriginalItemCode(String.valueOf(displayValue.charAt(startPosition + 4)));
      fixedField.setFormOfItemCode(String.valueOf(displayValue.charAt(startPosition + 5)));
      fixedField.setNatureOfContent1(String.valueOf(displayValue.charAt(startPosition + 6)));
      fixedField.setNatureOfContent2(String.valueOf(displayValue.charAt(startPosition + 7)));
      fixedField.setNatureOfContent3(String.valueOf(displayValue.charAt(startPosition + 8)));
      fixedField.setNatureOfContent4(String.valueOf(displayValue.charAt(startPosition + 9)));
      fixedField.setGovernmentPublicationCode(String.valueOf(displayValue.charAt(startPosition + 10)));
      fixedField.setConferencePublicationCode(String.valueOf(displayValue.charAt(startPosition + 11)));
      fixedField.setSerialOriginalAlphabetOfTitleCode(String.valueOf(displayValue.charAt(startPosition + 15)));
      fixedField.setSerialEntryConventionCode(String.valueOf(displayValue.charAt(startPosition + 16)));
      fixedField.setMaterialType(FixedField.MaterialType.CONTINUING_RESOURCE);
    } else if (gi.isComputerFile()) {
      fixedField.setTargetAudienceCode(String.valueOf(displayValue.charAt(startPosition + 4)));
      fixedField.setFormOfItemCode(String.valueOf(displayValue.charAt(startPosition + 5)));
      fixedField.setComputerFileTypeCode(String.valueOf(displayValue.charAt(startPosition + 8)));
      fixedField.setGovernmentPublicationCode(String.valueOf(displayValue.charAt(startPosition + 10)));
      fixedField.setMaterialType(FixedField.MaterialType.COMPUTER_FILE);
    } else if (gi.isMap()) {
      fixedField.setCartographicReliefCode1(String.valueOf(displayValue.charAt(startPosition)));
      fixedField.setCartographicReliefCode2(String.valueOf(displayValue.charAt(startPosition + 1)));
      fixedField.setCartographicReliefCode3(String.valueOf(displayValue.charAt(startPosition + 2)));
      fixedField.setCartographicReliefCode4(String.valueOf(displayValue.charAt(startPosition + 3)));
      fixedField.setCartographicProjectionCode(displayValue.substring(startPosition + 4, startPosition + 6));
      fixedField.setCartographicMaterial(String.valueOf(displayValue.charAt(startPosition + 7)));
      fixedField.setGovernmentPublicationCode(String.valueOf(displayValue.charAt(startPosition + 10)));
      fixedField.setFormOfItemCode(String.valueOf(displayValue.charAt(startPosition + 11)));
      fixedField.setCartographicIndexAvailabilityCode(String.valueOf(displayValue.charAt(startPosition + 13)));
      fixedField.setCartographicFormatCode1(String.valueOf(displayValue.charAt(startPosition + 15)));
      fixedField.setCartographicFormatCode2(String.valueOf(displayValue.charAt(startPosition + 16)));
      fixedField.setMaterialType(FixedField.MaterialType.MAP);
    } else if (gi.isMixedMaterial()) {
      fixedField.setFormOfItemCode(String.valueOf(displayValue.charAt(startPosition + 5)));
      fixedField.setMaterialType(FixedField.MaterialType.MIXED_MATERIAL);
    } else if (gi.isMusic()) {
      fixedField.setMusicFormOfCompositionCode(displayValue.substring(startPosition, startPosition + 2));
      fixedField.setMusicFormatCode(String.valueOf(displayValue.charAt(startPosition + 2)));
      fixedField.setMusicPartsCode(String.valueOf(displayValue.charAt(startPosition + 3)));
      fixedField.setTargetAudienceCode(String.valueOf(displayValue.charAt(startPosition + 4)));
      fixedField.setFormOfItemCode(String.valueOf(displayValue.charAt(startPosition + 5)));
      fixedField.setMusicTextualMaterialCode1(String.valueOf(displayValue.charAt(startPosition + 6)));
      fixedField.setMusicTextualMaterialCode2(String.valueOf(displayValue.charAt(startPosition + 7)));
      fixedField.setMusicTextualMaterialCode3(String.valueOf(displayValue.charAt(startPosition + 8)));
      fixedField.setMusicTextualMaterialCode4(String.valueOf(displayValue.charAt(startPosition + 9)));
      fixedField.setMusicTextualMaterialCode5(String.valueOf(displayValue.charAt(startPosition + 10)));
      fixedField.setMusicTextualMaterialCode6(String.valueOf(displayValue.charAt(startPosition + 11)));
      fixedField.setMusicLiteraryTextCode1(String.valueOf(displayValue.charAt(startPosition + 12)));
      fixedField.setMusicLiteraryTextCode2(String.valueOf(displayValue.charAt(startPosition + 13)));
      if(displayValue.length() > startPosition + 15) {
        fixedField.setMusicTranspositionArrangementCode(String.valueOf(displayValue.charAt(startPosition + 15)));
      }
      fixedField.setMaterialType(FixedField.MaterialType.MUSIC);
    } else if (gi.isVisualMaterial()) {
      fixedField.setVisualRunningTime(displayValue.substring(startPosition, startPosition + 3));
      fixedField.setTargetAudienceCode(String.valueOf(displayValue.charAt(startPosition + 4)));
      fixedField.setGovernmentPublicationCode(String.valueOf(displayValue.charAt(startPosition + 10)));
      fixedField.setFormOfItemCode(String.valueOf(displayValue.charAt(startPosition + 11)));
      fixedField.setVisualMaterialTypeCode(String.valueOf(displayValue.charAt(startPosition + 15)));
      fixedField.setVisualTechniqueCode(String.valueOf(displayValue.charAt(startPosition + 16)));
      fixedField.setMaterialType(FixedField.MaterialType.VISUAL_MATERIAL);
    }
  }

  public static void setPhysicalInformationValuesInFixedField(final FixedField fixedField) {

    final String categoryOfMaterial = ofNullable(fixedField.getCategoryOfMaterial())
      .map(category -> fixedField.getCategoryOfMaterial())
      .orElseGet(() -> {
        return ofNullable(Global.PHYSICAL_TYPES_MAP.get(fixedField.getHeaderTypeCode())).orElse(Global.UNSPECIFIED);
      });

    final String valueField = fixedField.getDisplayValue();

    if (valueField == null || categoryOfMaterial == null)
      return;

    fixedField.setSpecificMaterialDesignationCode(String.valueOf(valueField.charAt(1)));
    if (categoryOfMaterial.equals(Global.ELECTRONIC_RESOURCE)) {
      fixedField.setColourCode(String.valueOf(valueField.charAt(3)));
      fixedField.setDimensionsCode(String.valueOf(valueField.charAt(4)));
      fixedField.setIncludesSoundCode(String.valueOf(valueField.charAt(5)));
      fixedField.setImageBitDepth(valueField.substring(6, 9));
      fixedField.setFileFormatsCode(String.valueOf(valueField.charAt(9)));
      fixedField.setQualityAssuranceTargetCode(String.valueOf(valueField.charAt(10)));
      fixedField.setAntecedentSourceCode(String.valueOf(valueField.charAt(11)));
      fixedField.setLevelOfCompressionCode(String.valueOf(valueField.charAt(12)));
      fixedField.setReformattingQualityCode(String.valueOf(valueField.charAt(13)));
      fixedField.setPhysicalType(FixedField.PhysicalType.ELECTRONICAL_RESOURCE);
    } else if (categoryOfMaterial.equals(Global.GLOBE)) {
      fixedField.setColourCode(String.valueOf(valueField.charAt(3)));
      fixedField.setPhysicalMediumCode(String.valueOf(valueField.charAt(4)));
      fixedField.setTypeOfReproductionCode(String.valueOf(valueField.charAt(5)));
      fixedField.setPhysicalType(FixedField.PhysicalType.GLOBE);
    } else if (categoryOfMaterial.equals(Global.MAP_CODE)) {
      fixedField.setColourCode(String.valueOf(valueField.charAt(3)));
      fixedField.setPhysicalMediumCode(String.valueOf(valueField.charAt(4)));
      fixedField.setTypeOfReproductionCode(String.valueOf(valueField.charAt(5)));
      fixedField.setProductionDetailsCode(String.valueOf(valueField.charAt(6)));
      fixedField.setPolarityCode(String.valueOf(valueField.charAt(7)));
      fixedField.setPhysicalType(FixedField.PhysicalType.MAP);
    } else if (categoryOfMaterial.equals(Global.TACTILE_MATERIAL)) {
      fixedField.setClassOfBrailleWritingCodes(valueField.substring(3, 5));
      fixedField.setLevelOfContractionCode(String.valueOf(valueField.charAt(5)));
      fixedField.setBrailleMusicFormatCodes(valueField.substring(6, 9));
      fixedField.setSpecificPhysicalCharacteristicsCode(String.valueOf(valueField.charAt(9)));
      fixedField.setPhysicalType(FixedField.PhysicalType.TACTILE_MATERIAL);
    } else if (categoryOfMaterial.equals(Global.PROJECTED_GRAPHIC)) {
      fixedField.setColourCode(String.valueOf(valueField.charAt(3)));
      fixedField.setBaseOfEmulsionCode(String.valueOf(valueField.charAt(4)));
      fixedField.setSoundOnMediumOrSeparateCode(String.valueOf(valueField.charAt(5)));
      fixedField.setMediumForSoundCode(String.valueOf(valueField.charAt(6)));
      fixedField.setDimensionsCode(String.valueOf(valueField.charAt(7)));
      fixedField.setSecondarySupportMaterialCode(String.valueOf(valueField.charAt(8)));
      fixedField.setPhysicalType(FixedField.PhysicalType.PROJECTED_GRAPHIC);
    } else if (categoryOfMaterial.equals(Global.MICROFORM)) {
      fixedField.setPolarityCode(String.valueOf(valueField.charAt(3)));
      fixedField.setDimensionsCode(String.valueOf(valueField.charAt(4)));
      fixedField.setReductionRatioRangeCode(String.valueOf(valueField.charAt(5)));
      fixedField.setReductionRatioCode(valueField.substring(6, 9));
      fixedField.setColourCode(String.valueOf(valueField.charAt(9)));
      fixedField.setEmulsionOnFilmCode(String.valueOf(valueField.charAt(10)));
      fixedField.setGenerationCode(String.valueOf(valueField.charAt(11)));
      fixedField.setBaseOfFilmCode(String.valueOf(valueField.charAt(12)));
      fixedField.setPhysicalType(FixedField.PhysicalType.MICROFORM);
    } else if (categoryOfMaterial.equals(Global.NON_PROJECTED_GRAPHIC)) {
      fixedField.setColourCode(String.valueOf(valueField.charAt(3)));
      fixedField.setPrimarySupportMaterialCode(String.valueOf(valueField.charAt(4)));
      fixedField.setSecondarySupportMaterialCode(String.valueOf(valueField.charAt(5)));
      fixedField.setPhysicalType(FixedField.PhysicalType.NON_PROJECTED_GRAPHIC);
    } else if (categoryOfMaterial.equals(Global.MOTION_PICTURE)) {
      fixedField.setColourCode(String.valueOf(valueField.charAt(3)));
      fixedField.setPresentationFormatCode(String.valueOf(valueField.charAt(4)));
      fixedField.setSoundOnMediumOrSeparateCode(String.valueOf(valueField.charAt(5)));
      fixedField.setMediumForSoundCode(String.valueOf(valueField.charAt(6)));
      fixedField.setDimensionsCode(String.valueOf(valueField.charAt(7)));
      fixedField.setConfigurationCode(String.valueOf(valueField.charAt(8)));
      fixedField.setProductionElementsCode(String.valueOf(valueField.charAt(9)));
      fixedField.setPolarityCode(String.valueOf(valueField.charAt(10)));
      fixedField.setGenerationCode(String.valueOf(valueField.charAt(11)));
      fixedField.setBaseOfFilmCode(String.valueOf(valueField.charAt(12)));
      fixedField.setRefinedCategoriesOfColourCode(String.valueOf(valueField.charAt(13)));
      fixedField.setKindOfColourStockCode(String.valueOf(valueField.charAt(14)));
      fixedField.setDeteriorationStageCode(String.valueOf(valueField.charAt(15)));
      fixedField.setCompletenessCode(String.valueOf(valueField.charAt(16)));
      fixedField.setInspectionDate(valueField.substring(17));
      fixedField.setPhysicalType(FixedField.PhysicalType.MOTION_PICTURE);
    } else if (categoryOfMaterial.equals(Global.KIT_CODE)) {
      fixedField.setPhysicalType(FixedField.PhysicalType.KIT);
    } else if (categoryOfMaterial.equals(Global.NOTATED_MUSIC)) {
      fixedField.setPhysicalType(FixedField.PhysicalType.NOTATED_MUSIC);
    } else if (categoryOfMaterial.equals(Global.REMOTE_SENSING_IMAGE)) {
      fixedField.setAltitudeOfSensorCode(String.valueOf(valueField.charAt(3)));
      fixedField.setAttitudeOfSensorCode(String.valueOf(valueField.charAt(4)));
      fixedField.setCloudCoverCode(String.valueOf(valueField.charAt(5)));
      fixedField.setPlatformConstructionTypeCode(String.valueOf(valueField.charAt(6)));
      fixedField.setPlatformUseCode(String.valueOf(valueField.charAt(7)));
      fixedField.setSensorTypeCode(String.valueOf(valueField.charAt(8)));
      fixedField.setRemoteDataTypeCode(valueField.substring(9));
      fixedField.setPhysicalType(FixedField.PhysicalType.REMOTE_SENSING_IMAGE);
    } else if (categoryOfMaterial.equals(Global.SOUND_RECORDING)) {
      fixedField.setSpeedCode(String.valueOf(valueField.charAt(3)));
      fixedField.setConfigurationCode(String.valueOf(valueField.charAt(4)));
      fixedField.setGrooveWidthCode(String.valueOf(valueField.charAt(5)));
      fixedField.setDimensionsCode(String.valueOf(valueField.charAt(6)));
      fixedField.setTapeWidthCode(String.valueOf(valueField.charAt(7)));
      fixedField.setTapeConfigurationCode(String.valueOf(valueField.charAt(8)));
      fixedField.setDiscTypeCode(String.valueOf(valueField.charAt(9)));
      fixedField.setSndMaterialTypeCode(String.valueOf(valueField.charAt(10)));
      fixedField.setCuttingTypeCode(String.valueOf(valueField.charAt(11)));
      fixedField.setSpecialPlaybackCharacteristicsCode(String.valueOf(valueField.charAt(12)));
      fixedField.setStorageTechniqueCode(String.valueOf(valueField.charAt(13)));
      fixedField.setPhysicalType(FixedField.PhysicalType.SOUND_RECORDING);
    } else if (categoryOfMaterial.equals(Global.TEXT_CODE)) {
      fixedField.setPhysicalType(FixedField.PhysicalType.TEXT);
    } else if (categoryOfMaterial.equals(Global.UNSPECIFIED)) {
      fixedField.setPhysicalType(FixedField.PhysicalType.UNSPECIFIED);
    } else if (categoryOfMaterial.equals(Global.VIDEO_RECORDING)) {
      fixedField.setColourCode(String.valueOf(valueField.charAt(3)));
      fixedField.setFormatCode(String.valueOf(valueField.charAt(4)));
      fixedField.setIncludesSoundCode(String.valueOf(valueField.charAt(5)));
      fixedField.setMediumForSoundCode(String.valueOf(valueField.charAt(6)));
      fixedField.setDimensionsCode(String.valueOf(valueField.charAt(7)));
      fixedField.setConfigurationCode(String.valueOf(valueField.charAt(8)));
      fixedField.setPhysicalType(FixedField.PhysicalType.VIDEO_RECORDING);
    }
  }
}
