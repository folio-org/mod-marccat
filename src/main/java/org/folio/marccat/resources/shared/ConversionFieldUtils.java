package org.folio.marccat.resources.shared;

import org.folio.marccat.config.constants.Global;
import org.folio.marccat.resources.domain.FixedField;
import org.folio.marccat.shared.GeneralInformation;

import static java.util.Optional.ofNullable;

/**
 * ConversionFieldUtils the utility class for Field
 *
 * @author nbianchini
 * @author carment
 * @since 1.0
 */
public class ConversionFieldUtils {

  private ConversionFieldUtils() {
    throw new IllegalStateException("Utility class");
  }

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
      fixedField.setBookIllustrationCode3(String.valueOf(displayValue.charAt(startPosition + 2)));
      fixedField.setBookIllustrationCode4(String.valueOf(displayValue.charAt(startPosition + 3)));
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
      fixedField.setNatureOfEntireWork(String.valueOf(displayValue.charAt(startPosition + 6)));
      fixedField.setNatureOfContent1(String.valueOf(displayValue.charAt(startPosition + 7)));
      fixedField.setNatureOfContent2(String.valueOf(displayValue.charAt(startPosition + 8)));
      fixedField.setNatureOfContent3(String.valueOf(displayValue.charAt(startPosition + 9)));
      fixedField.setGovernmentPublicationCode(String.valueOf(displayValue.charAt(startPosition + 10)));
      fixedField.setConferencePublicationCode(String.valueOf(displayValue.charAt(startPosition + 11)));
      fixedField.setSerialOriginalAlphabetOfTitleCode(String.valueOf(displayValue.charAt(startPosition + 15)));
      fixedField.setSerialSuccessiveLatestCode(String.valueOf(displayValue.charAt(startPosition + 16)));
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
      if (displayValue.length() > startPosition + 15) {
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

  /**
   * Inject physical information values for drop-down list selected.
   *
   * @param fixedField the fixedField to populate.
   */
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
      fixedField.setColorCode(String.valueOf(valueField.charAt(3)));
      fixedField.setDimensionCode(String.valueOf(valueField.charAt(4)));
      fixedField.setIncludesSoundCode(String.valueOf(valueField.charAt(5)));
      fixedField.setImageBitDepth(valueField.substring(6, 9));
      fixedField.setFileFormatsCode(String.valueOf(valueField.charAt(9)));
      fixedField.setQualityAssuranceTargetCode(String.valueOf(valueField.charAt(10)));
      fixedField.setAntecedentSourceCode(String.valueOf(valueField.charAt(11)));
      fixedField.setLevelOfCompressionCode(String.valueOf(valueField.charAt(12)));
      fixedField.setReformattingQualityCode(String.valueOf(valueField.charAt(13)));
      fixedField.setPhysicalType(FixedField.PhysicalType.ELECTRONICAL_RESOURCE);
    } else if (categoryOfMaterial.equals(Global.GLOBE)) {
      fixedField.setColorCode(String.valueOf(valueField.charAt(3)));
      fixedField.setPhysicalMediumCode(String.valueOf(valueField.charAt(4)));
      fixedField.setTypeOfReproductionCode(String.valueOf(valueField.charAt(5)));
      fixedField.setPhysicalType(FixedField.PhysicalType.GLOBE);
    } else if (categoryOfMaterial.equals(Global.MAP_CODE)) {
      fixedField.setColorCode(String.valueOf(valueField.charAt(3)));
      fixedField.setPhysicalMediumCode(String.valueOf(valueField.charAt(4)));
      fixedField.setTypeOfReproductionCode(String.valueOf(valueField.charAt(5)));
      fixedField.setProductionDetailsCode(String.valueOf(valueField.charAt(6)));
      fixedField.setPolarityCode(String.valueOf(valueField.charAt(7)));
      fixedField.setPhysicalType(FixedField.PhysicalType.MAP);
    } else if (categoryOfMaterial.equals(Global.TACTILE_MATERIAL)) {
      fixedField.setClassOfBrailleWritingCode1(String.valueOf(valueField.charAt(3)));
      fixedField.setClassOfBrailleWritingCode2(String.valueOf(valueField.charAt(4)));
      fixedField.setLevelOfContractionCode(String.valueOf(valueField.charAt(5)));
      fixedField.setBrailleMusicFormatCode1(String.valueOf(valueField.charAt(6)));
      fixedField.setBrailleMusicFormatCode2(String.valueOf(valueField.charAt(7)));
      fixedField.setBrailleMusicFormatCode3(String.valueOf(valueField.charAt(8)));
      fixedField.setSpecificPhysicalCharacteristicsCode(String.valueOf(valueField.charAt(9)));
      fixedField.setPhysicalType(FixedField.PhysicalType.TACTILE_MATERIAL);
    } else if (categoryOfMaterial.equals(Global.PROJECTED_GRAPHIC)) {
      fixedField.setColorCode(String.valueOf(valueField.charAt(3)));
      fixedField.setBaseOfEmulsionCode(String.valueOf(valueField.charAt(4)));
      fixedField.setSoundOnMediumOrSeparateCode(String.valueOf(valueField.charAt(5)));
      fixedField.setMediumForSoundCode(String.valueOf(valueField.charAt(6)));
      fixedField.setDimensionCode(String.valueOf(valueField.charAt(7)));
      fixedField.setSecondarySupportMaterialCode(String.valueOf(valueField.charAt(8)));
      fixedField.setPhysicalType(FixedField.PhysicalType.PROJECTED_GRAPHIC);
    } else if (categoryOfMaterial.equals(Global.MICROFORM)) {
      fixedField.setPolarityCode(String.valueOf(valueField.charAt(3)));
      fixedField.setDimensionCode(String.valueOf(valueField.charAt(4)));
      fixedField.setReductionRatioRangeCode(String.valueOf(valueField.charAt(5)));
      fixedField.setReductionRatioCode(valueField.substring(6, 9));
      fixedField.setColorCode(String.valueOf(valueField.charAt(9)));
      fixedField.setEmulsionOnFilmCode(String.valueOf(valueField.charAt(10)));
      fixedField.setGenerationCode(String.valueOf(valueField.charAt(11)));
      fixedField.setBaseOfFilmCode(String.valueOf(valueField.charAt(12)));
      fixedField.setPhysicalType(FixedField.PhysicalType.MICROFORM);
    } else if (categoryOfMaterial.equals(Global.NON_PROJECTED_GRAPHIC)) {
      fixedField.setColorCode(String.valueOf(valueField.charAt(3)));
      fixedField.setPrimarySupportMaterialCode(String.valueOf(valueField.charAt(4)));
      fixedField.setSecondarySupportMaterialCode(String.valueOf(valueField.charAt(5)));
      fixedField.setPhysicalType(FixedField.PhysicalType.NON_PROJECTED_GRAPHIC);
    } else if (categoryOfMaterial.equals(Global.MOTION_PICTURE)) {
      fixedField.setColorCode(String.valueOf(valueField.charAt(3)));
      fixedField.setPresentationFormatCode(String.valueOf(valueField.charAt(4)));
      fixedField.setSoundOnMediumOrSeparateCode(String.valueOf(valueField.charAt(5)));
      fixedField.setMediumForSoundCode(String.valueOf(valueField.charAt(6)));
      fixedField.setDimensionCode(String.valueOf(valueField.charAt(7)));
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
      fixedField.setRemoteSensingDataTypeCode(valueField.substring(9));
      fixedField.setPhysicalType(FixedField.PhysicalType.REMOTE_SENSING_IMAGE);
    } else if (categoryOfMaterial.equals(Global.SOUND_RECORDING)) {
      fixedField.setSpeedCode(String.valueOf(valueField.charAt(3)));
      fixedField.setConfigurationCode(String.valueOf(valueField.charAt(4)));
      fixedField.setGrooveWidthCode(String.valueOf(valueField.charAt(5)));
      fixedField.setDimensionCode(String.valueOf(valueField.charAt(6)));
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
      fixedField.setColorCode(String.valueOf(valueField.charAt(3)));
      fixedField.setVideoRecordingFormatCode(String.valueOf(valueField.charAt(4)));
      fixedField.setMediumForSoundCode(String.valueOf(valueField.charAt(5)));
      fixedField.setSoundOnMediumOrSeparateCode(String.valueOf(valueField.charAt(6)));
      fixedField.setDimensionCode(String.valueOf(valueField.charAt(7)));
      fixedField.setConfigurationCode(String.valueOf(valueField.charAt(8)));
      fixedField.setPhysicalType(FixedField.PhysicalType.VIDEO_RECORDING);
    }
  }

  /**
   * Return a display value for the material description.
   *
   * @param fixedField     the fixedField to populate.
   * @param formOfMaterial the form of material.
   */
  /**
   * Return a display value for the material description.
   *
   * @param fixedField     the fixedField to populate.
   * @param formOfMaterial the form of material.
   */
  public static FixedField getDisplayValueOfMaterial(final FixedField fixedField, final String formOfMaterial) {
    final GeneralInformation gi = new GeneralInformation();
    gi.setFormOfMaterial(formOfMaterial);
    gi.setMaterialDescription008Indicator(fixedField.getCode().equalsIgnoreCase(Global.MATERIAL_TAG_CODE) ? "1" : "0");
    StringBuilder sb = new StringBuilder();
    if ("1".equals(gi.getMaterialDescription008Indicator())) {
      sb.append(fixedField.getDateEnteredOnFile());
      sb.append(fixedField.getDateTypeCode());
      sb.append(fixedField.getDateFirstPublication());
      sb.append(fixedField.getDateLastPublication());
      sb.append(fixedField.getPlaceOfPublication());

    } else { //006
      sb.append(fixedField.getMaterialTypeCode());
    }
    if (gi.isBook()) {
      sb.append(fixedField.getBookIllustrationCode1());
      sb.append(fixedField.getBookIllustrationCode2());
      sb.append(fixedField.getBookIllustrationCode3());
      sb.append(fixedField.getBookIllustrationCode4());
      sb.append(fixedField.getTargetAudienceCode());
      sb.append(fixedField.getFormOfItemCode());
      sb.append(fixedField.getNatureOfContent1());
      sb.append(fixedField.getNatureOfContent2());
      sb.append(fixedField.getNatureOfContent3());
      sb.append(fixedField.getNatureOfContent4());
      sb.append(fixedField.getGovernmentPublicationCode());
      sb.append(fixedField.getConferencePublicationCode());
      sb.append(fixedField.getBookFestschrift());
      sb.append(fixedField.getBookIndexAvailabilityCode());
      sb.append(" ");
      sb.append(fixedField.getBookLiteraryFormTypeCode());
      sb.append(fixedField.getBookBiographyCode());
    } else if (gi.isSerial()) {
      sb.append(fixedField.getSerialFrequencyCode());
      sb.append(fixedField.getSerialRegularityCode());
      sb.append(" ");
      sb.append(fixedField.getSerialTypeCode());
      sb.append(fixedField.getSerialFormOriginalItemCode());
      sb.append(fixedField.getFormOfItemCode());
      sb.append(fixedField.getNatureOfEntireWork());
      sb.append(fixedField.getNatureOfContent1());
      sb.append(fixedField.getNatureOfContent2());
      sb.append(fixedField.getNatureOfContent3());
      sb.append(fixedField.getGovernmentPublicationCode());
      sb.append(fixedField.getConferencePublicationCode());
      sb.append("   ");
      sb.append(fixedField.getSerialOriginalAlphabetOfTitleCode());
      sb.append(fixedField.getSerialSuccessiveLatestCode());
    } else if (gi.isComputerFile()) {
      sb.append("    ");
      sb.append(fixedField.getComputerTargetAudienceCode());
      sb.append(fixedField.getComputerFileFormCode());
      sb.append("  ");
      sb.append(fixedField.getComputerFileTypeCode());
      sb.append(" ");
      sb.append(fixedField.getGovernmentPublicationCode());
      sb.append("      ");
    } else if (gi.isMap()) {
      sb.append(fixedField.getCartographicReliefCode1());
      sb.append(fixedField.getCartographicReliefCode2());
      sb.append(fixedField.getCartographicReliefCode3());
      sb.append(fixedField.getCartographicReliefCode4());
      sb.append(fixedField.getCartographicProjectionCode());
      sb.append(" ");
      sb.append(fixedField.getCartographicMaterial());
      sb.append("  ");
      sb.append(fixedField.getGovernmentPublicationCode());
      sb.append(fixedField.getFormOfItemCode());
      sb.append(" ");
      sb.append(fixedField.getCartographicIndexAvailabilityCode());
      sb.append(" ");
      sb.append(fixedField.getCartographicFormatCode1());
      sb.append(fixedField.getCartographicFormatCode2());
    } else if (gi.isMixedMaterial()) {
      sb.append("     ");
      sb.append(fixedField.getFormOfItemCode());
      sb.append("           ");
    } else if (gi.isMusic()) {
      sb.append(fixedField.getMusicFormOfCompositionCode());
      sb.append(fixedField.getMusicFormatCode());
      sb.append(fixedField.getMusicPartsCode());
      sb.append(fixedField.getTargetAudienceCode());
      sb.append(fixedField.getFormOfItemCode());
      sb.append(fixedField.getMusicTextualMaterialCode1());
      sb.append(fixedField.getMusicTextualMaterialCode2());
      sb.append(fixedField.getMusicTextualMaterialCode3());
      sb.append(fixedField.getMusicTextualMaterialCode4());
      sb.append(fixedField.getMusicTextualMaterialCode5());
      sb.append(fixedField.getMusicTextualMaterialCode6());
      sb.append(fixedField.getMusicLiteraryTextCode1());
      sb.append(fixedField.getMusicLiteraryTextCode2());
      sb.append(" ");
      sb.append(fixedField.getMusicTranspositionArrangementCode());
      sb.append(" ");
    } else if (gi.isVisualMaterial()) {
      sb.append(fixedField.getVisualRunningTime());
      sb.append(" ");
      sb.append(fixedField.getTargetAudienceCode());
      sb.append("     ");
      sb.append(fixedField.getGovernmentPublicationCode());
      sb.append(fixedField.getFormOfItemCode());
      sb.append("   ");
      sb.append(fixedField.getVisualMaterialTypeCode());
      sb.append(fixedField.getVisualTechniqueCode());
    }
    if ("1".equals(gi.getMaterialDescription008Indicator())) {
      sb.append(fixedField.getLanguageCode());
      sb.append(fixedField.getRecordModifiedCode());
      sb.append(fixedField.getRecordCataloguingSourceCode());
    }

    fixedField.setDisplayValue(sb.toString());
    return fixedField;
  }


  /**
   * Return a display value for the physical information.
   *
   * @param fixedField the fixedField to populate.
   */
  public static FixedField getDisplayValueOfPhysicalInformation(final FixedField fixedField) {

    final String categoryOfMaterial = ofNullable(fixedField.getCategoryOfMaterial())
      .map(category -> fixedField.getCategoryOfMaterial())
      .orElseGet(() -> {
        return ofNullable(Global.PHYSICAL_TYPES_MAP.get(fixedField.getHeaderTypeCode())).orElse(Global.UNSPECIFIED);
      });
    StringBuilder sb = new StringBuilder();
    sb.append(categoryOfMaterial);
    sb.append(fixedField.getSpecificMaterialDesignationCode());
    if (categoryOfMaterial.equals(Global.ELECTRONIC_RESOURCE)) {
      sb.append(" ");
      sb.append(fixedField.getColorCode());
      sb.append(fixedField.getDimensionCode());
      sb.append(fixedField.getIncludesSoundCode());
      sb.append(fixedField.getImageBitDepth());
      sb.append(fixedField.getFileFormatsCode());
      sb.append(fixedField.getQualityAssuranceTargetCode());
      sb.append(fixedField.getAntecedentSourceCode());
      sb.append(fixedField.getLevelOfCompressionCode());
      sb.append(fixedField.getReformattingQualityCode());
    } else if (categoryOfMaterial.equals(Global.GLOBE)) {
      sb.append(" ");
      sb.append(fixedField.getColorCode());
      sb.append(fixedField.getPhysicalMediumCode());
      sb.append(fixedField.getTypeOfReproductionCode());
    } else if (categoryOfMaterial.equals(Global.MAP_CODE)) {
      sb.append(" ");
      sb.append(fixedField.getColorCode());
      sb.append(fixedField.getPhysicalMediumCode());
      sb.append(fixedField.getTypeOfReproductionCode());
      sb.append(fixedField.getProductionDetailsCode());
      sb.append(fixedField.getPolarityCode());
    } else if (categoryOfMaterial.equals(Global.TACTILE_MATERIAL)) {
      sb.append(" ");
      sb.append(fixedField.getClassOfBrailleWritingCode1());
      sb.append(fixedField.getClassOfBrailleWritingCode2());
      sb.append(fixedField.getLevelOfContractionCode());
      sb.append(fixedField.getBrailleMusicFormatCode1());
      sb.append(fixedField.getBrailleMusicFormatCode2());
      sb.append(fixedField.getBrailleMusicFormatCode3());
      sb.append(fixedField.getSpecificPhysicalCharacteristicsCode());
    } else if (categoryOfMaterial.equals(Global.PROJECTED_GRAPHIC)) {
      sb.append(" ");
      sb.append(fixedField.getColorCode());
      sb.append(fixedField.getBaseOfEmulsionCode());
      sb.append(fixedField.getSoundOnMediumOrSeparateCode());
      sb.append(fixedField.getMediumForSoundCode());
      sb.append(fixedField.getDimensionCode());
      sb.append(fixedField.getSecondarySupportMaterialCode());
    } else if (categoryOfMaterial.equals(Global.MICROFORM)) {
      sb.append(" ");
      sb.append(fixedField.getPolarityCode());
      sb.append(fixedField.getDimensionCode());
      sb.append(fixedField.getReductionRatioRangeCode());
      sb.append(fixedField.getReductionRatioCode());
      sb.append(fixedField.getColorCode());
      sb.append(fixedField.getEmulsionOnFilmCode());
      sb.append(fixedField.getGenerationCode());
      sb.append(fixedField.getBaseOfFilmCode());
    } else if (categoryOfMaterial.equals(Global.NON_PROJECTED_GRAPHIC)) {
      sb.append(" ");
      sb.append(fixedField.getColorCode());
      sb.append(fixedField.getPrimarySupportMaterialCode());
      sb.append(fixedField.getSecondarySupportMaterialCode());
    } else if (categoryOfMaterial.equals(Global.MOTION_PICTURE)) {
      sb.append(" ");
      sb.append(fixedField.getColorCode());
      sb.append(fixedField.getPresentationFormatCode());
      sb.append(fixedField.getSoundOnMediumOrSeparateCode());
      sb.append(fixedField.getMediumForSoundCode());
      sb.append(fixedField.getDimensionCode());
      sb.append(fixedField.getConfigurationCode());
      sb.append(fixedField.getProductionElementsCode());
      sb.append(fixedField.getPolarityCode());
      sb.append(fixedField.getGenerationCode());
      sb.append(fixedField.getBaseOfFilmCode());
      sb.append(fixedField.getRefinedCategoriesOfColourCode());
      sb.append(fixedField.getKindOfColourStockCode());
      sb.append(fixedField.getDeteriorationStageCode());
      sb.append(fixedField.getCompletenessCode());
      sb.append(fixedField.getInspectionDate());
    } else if (categoryOfMaterial.equals(Global.REMOTE_SENSING_IMAGE)) {
      sb.append(" ");
      sb.append(fixedField.getAltitudeOfSensorCode());
      sb.append(fixedField.getAttitudeOfSensorCode());
      sb.append(fixedField.getCloudCoverCode());
      sb.append(fixedField.getPlatformConstructionTypeCode());
      sb.append(fixedField.getPlatformUseCode());
      sb.append(fixedField.getSensorTypeCode());
      sb.append(fixedField.getRemoteSensingDataTypeCode());
    } else if (categoryOfMaterial.equals(Global.SOUND_RECORDING)) {
      sb.append(" ");
      sb.append(fixedField.getSpeedCode());
      sb.append(fixedField.getConfigurationCode());
      sb.append(fixedField.getGrooveWidthCode());
      sb.append(fixedField.getDimensionCode());
      sb.append(fixedField.getTapeWidthCode());
      sb.append(fixedField.getTapeConfigurationCode());
      sb.append(fixedField.getDiscTypeCode());
      sb.append(fixedField.getSndMaterialTypeCode());
      sb.append(fixedField.getCuttingTypeCode());
      sb.append(fixedField.getSpecialPlaybackCharacteristicsCode());
      sb.append(fixedField.getStorageTechniqueCode());
    } else if (categoryOfMaterial.equals(Global.VIDEO_RECORDING)) {
      sb.append(" ");
      sb.append(fixedField.getColorCode());
      sb.append(fixedField.getVideoRecordingFormatCode());
      sb.append(fixedField.getMediumForSoundCode());
      sb.append(fixedField.getSoundOnMediumOrSeparateCode());
      sb.append(fixedField.getDimensionCode());
      sb.append(fixedField.getConfigurationCode());
    }
    fixedField.setDisplayValue(sb.toString());
    return fixedField;
  }





}
