package org.folio.marccat.resources.shared;

import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.MessageCatalog;
import org.folio.marccat.enumaration.CodeListsType;
import org.folio.marccat.integration.StorageService;
import org.folio.marccat.resources.domain.*;
import org.folio.marccat.shared.CatalogingInformation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.folio.marccat.config.log.Global.*;
import static org.folio.marccat.config.log.MessageCatalog._00016_FIELD_PARAMETER_INVALID;
import static org.folio.marccat.config.log.MessageCatalog._00019_HEADER_TYPE_ID_WRONG;
import static org.folio.marccat.enumaration.CodeListsType.*;
import static org.folio.marccat.resources.shared.MappingUtils.toPairItem;

public class FixedFieldUtils {

  protected static Log logger = new Log(FixedFieldUtils.class);
  private static final String MATERIAL_TYPE = "materialTypeCode";

  /**
   * Check if is a fixed field or not.
   *
   * @param field the tag entity.
   * @return true if is fixedfield, false otherwise.
   */
  public static boolean isFixedField(final Field field) {
    return FIXED_FIELDS.contains(field.getCode());
  }

  /**
   * Check if is a fixedField or not.
   *
   * @param code the tag number code.
   * @return true if is fixedfield, false otherwise.
   */
  public static boolean isFixedField(final String code) {
    return FIXED_FIELDS.contains(code);
  }

  /**
   * Sets values Video Recording type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setPhysicalInfoVRcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, VR_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(COLOR_CODE, storageService.getCodesList(lang, VR_COLOR).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("videoRecordingFormatCodes", storageService.getCodesList(lang, VR_FORMAT).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(SOUND_ON_MEDIUM_OR_SEPARATE_CODE, storageService.getCodesList(lang, SOUND_MEDIUM_OR_SEP).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(MEDIUM_FOR_SOUND_CODE, storageService.getCodesList(lang, MEDIUM_FOR_SOUND).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(DIMENSION_CODE, storageService.getCodesList(lang, VR_DIMENSIONS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(CONFIGURATION_CODE, storageService.getCodesList(lang, VR_CONF_PLAYBACK).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Unspecified type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setPhysicalInfoUNScodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, UNS_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Text type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setPhysicalInfoTXTcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, TXT_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Sound Recording type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setPhysicalInfoSNDcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, SND_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("speedCode", storageService.getCodesList(lang, SND_SPEED).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("configurationCode", storageService.getCodesList(lang, SND_CONF_PLAYBACK).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("grooveWidthCode", storageService.getCodesList(lang, SND_GROOVE_WIDTH).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(DIMENSION_CODE, storageService.getCodesList(lang, SND_DIMENSIONS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("tapeWidthCode", storageService.getCodesList(lang, SND_TAPE_WIDTH).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("tapeConfigurationCode", storageService.getCodesList(lang, SND_TAPE_CONF).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("discTypeCode", storageService.getCodesList(lang, SND_DISC_TYPE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("sndMaterialTypeCode", storageService.getCodesList(lang, SND_MATERIAL_TYPE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("cuttingTypeCode", storageService.getCodesList(lang, SND_CUTTING).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("specialPlaybackCharacteristicsCode", storageService.getCodesList(lang, SND_SPEC_PLAYBACK).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("storageTechniqueCode", storageService.getCodesList(lang, SND_STORAGE_TECNIQUE).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Remote Sensing Image type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setPhysicalInfoRSIcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, RSI_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("altitudeOfSensorCode", storageService.getCodesList(lang, RSI_ALTITUDE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("attitudeOfSensorCode", storageService.getCodesList(lang, RSI_ATTITUDE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("cloudCoverCode", storageService.getCodesList(lang, RSI_CLOUD_COVER).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("platformConstructionTypeCode", storageService.getCodesList(lang, RSI_PLAT_CONSTRUCTION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("platformUseCode", storageService.getCodesList(lang, RSI_PLAT_USE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("sensorTypeCode", storageService.getCodesList(lang, RSI_SENSOR_TYPE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("remoteSensingDataTypeCodes", storageService.getCodesList(lang, RSI_DATA_TYPE).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Notated Music type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setPhysicalInfoNMcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, NMU_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Kit type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setPhysicalInfoKITcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, KIT_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Motion Picture type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setPhysicalInfoMPcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {

    fixedFieldCodesGroup.addResults(new FixedFieldElement(SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, MP_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(COLOR_CODE, storageService.getCodesList(lang, MP_COLOR).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("presentationFormatCode", storageService.getCodesList(lang, MP_PRESENT_FORMAT).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("soundOnMediumOrSeparateCode", storageService.getCodesList(lang, SOUND_MEDIUM_OR_SEP).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("mediumForSoundCode", storageService.getCodesList(lang, MEDIUM_FOR_SOUND).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(DIMENSION_CODE, storageService.getCodesList(lang, MP_DIMENSIONS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("configurationCode", storageService.getCodesList(lang, MP_CONF_PLAYBACK).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("productionElementsCode", storageService.getCodesList(lang, MP_PROD_ELEM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(POLARITY_CODE, storageService.getCodesList(lang, MP_POLARITY).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("generationCode", storageService.getCodesList(lang, MP_GENERATION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("baseOfFilmCode", storageService.getCodesList(lang, MP_BASE_FILM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("refinedCategoriesOfColorCode", storageService.getCodesList(lang, MP_REFINE_CAT_COLOR).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("kindOfColorStockCode", storageService.getCodesList(lang, MP_KIND_COLORS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("deteriorationStageCode", storageService.getCodesList(lang, MP_DETERIORATION_STAGE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("completenessCode", storageService.getCodesList(lang, MP_COMPLETENESS).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Non Project Graphic type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setPhysicalInfoNPGcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, NPG_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(COLOR_CODE, storageService.getCodesList(lang, NPG_COLOR).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("primarySupportMaterialCode", storageService.getCodesList(lang, NPG_PRIMARY_SUPPORT).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("secondarySupportMaterialCode", storageService.getCodesList(lang, NPG_SECONDARY_SUPPORT).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Microform type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setPhysicalInfoMICcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, MIC_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(POLARITY_CODE, storageService.getCodesList(lang, MIC_POLARITY).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(DIMENSION_CODE, storageService.getCodesList(lang, MIC_DIMENSIONS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("reductionRatioRangeCode", storageService.getCodesList(lang, MIC_REDUCT_RATIO_RANGE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(COLOR_CODE, storageService.getCodesList(lang, MIC_COLOR).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("emulsionOnFilmCode", storageService.getCodesList(lang, MIC_EMUL_FILM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("generationCode", storageService.getCodesList(lang, MIC_GENERATION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("baseOfFilmCode", storageService.getCodesList(lang, MIC_BASE_FILM).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Project Graphic type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setPhysicalInfoPGcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, PG_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(COLOR_CODE, storageService.getCodesList(lang, PG_COLOR).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("baseOfEmulsionCode", storageService.getCodesList(lang, PG_EMUL_BASE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("soundOnMediumOrSeparateCode", storageService.getCodesList(lang, SOUND_MEDIUM_OR_SEP).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("mediumForSoundCodes", storageService.getCodesList(lang, MEDIUM_FOR_SOUND).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("dimensionCodes", storageService.getCodesList(lang, PG_DIMENSIONS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("secondarySupportMaterialCode", storageService.getCodesList(lang, PG_SECONDARY_SUPPORT).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Map type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setPhysicalInfoMAPcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, MAP_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(COLOR_CODE, storageService.getCodesList(lang, MAP_COLOR).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("physicalMediumCode", storageService.getCodesList(lang, MAP_PHYSICAL_MEDIUM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("typeOfReproductionCode", storageService.getCodesList(lang, MAP_TYPE_OF_REPRODUCTION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("productionDetailsCode", storageService.getCodesList(lang, MAP_PRODUCTION_DETAILS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(POLARITY_CODE, storageService.getCodesList(lang, MAP_POLARITY).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Globe type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setPhysicalInfoGLBcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, GLB_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("colorCode", storageService.getCodesList(lang, GLB_COLOR).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("physicalMediumCode", storageService.getCodesList(lang, GLB_PHYSICAL_MEDIUM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("typeOfReproductionCode", storageService.getCodesList(lang, GLB_TYPE_OF_REPRODUCTION).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Tactile Material type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setPhysicalInfoTCTcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement("specificMaterialDesignationCode", storageService.getCodesList(lang, TCT_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("classOfBrailleWritingCodes", storageService.getCodesList(lang, TCT_CLASS_BRAILLE_WRITING).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("levelOfContractionCode", storageService.getCodesList(lang, TCT_CONTRACTION_LVL).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("brailleMusicFormatCodes", storageService.getCodesList(lang, TCT_BRAILLE_MUSIC_FORMAT).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("specificPhysicalCharacteristicsCode", storageService.getCodesList(lang, TCT_SPECIAL_PHYSICAL_CHAR).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Computer File type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setPhysicalInfoCFcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement("specificMaterialDesignationCode", storageService.getCodesList(lang, CF_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("colorCode", storageService.getCodesList(lang, CF_COLOR).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("dimensionCodes", storageService.getCodesList(lang, CF_DIMENSIONS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("includesSoundCodes", storageService.getCodesList(lang, SOUND_MEDIUM_OR_SEP).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("fileFormatsCode", storageService.getCodesList(lang, CF_FILE_FORMAT).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("qualityAssuranceTargetCode", storageService.getCodesList(lang, CF_QUALITY_ASS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("antecedentSourceCode", storageService.getCodesList(lang, CF_ANTECEDENT_SRC).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("levelOfCompressionCode", storageService.getCodesList(lang, CF_COMPRESSION_LVL).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("reformattingQualityCode", storageService.getCodesList(lang, CF_REFORMATTING_QUALITY).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Inject codes groups for 008 and 006 tags.
   *
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   * @param storageService       the storage service.
   * @param lang                 the lang associated with the current request.
   * @param headerTypeCode       the header type code selected.
   * @param tag                  the tag code.
   */
  public static void injectMaterialCodes(final FixedFieldCodesGroup fixedFieldCodesGroup,
                                         final StorageService storageService,
                                         final String lang,
                                         final int headerTypeCode,
                                         final String tag) {
    Map<String, Object> mapRecordTypeMaterial = storageService.getMaterialTypeInfosByHeaderCode(headerTypeCode, tag);
    if (mapRecordTypeMaterial != null) {
      if (tag.equals(MATERIAL_TAG_CODE)) {
        fixedFieldCodesGroup.addResults(new FixedFieldElement("dateTypes", storageService.getCodesList(lang, DATE_TYPE).stream().map(toPairItem).collect(toList())));
        fixedFieldCodesGroup.addResults(new FixedFieldElement("country", storageService.getCodesList(lang, MARC_COUNTRY).stream().map(toPairItem).collect(toList())));
      }
      String material = (String) mapRecordTypeMaterial.get(FORM_OF_MATERIAL_LABEL);
      switch (material) {
        case BOOK_TYPE:
          setBookMaterialCodes(lang, storageService, fixedFieldCodesGroup);
          if (tag.equals(OTHER_MATERIAL_TAG_CODE)) {
            fixedFieldCodesGroup.addResults(new FixedFieldElement(MATERIAL_TYPE,
              storageService.getCodesList(lang, BOOK_MATERIAL_CODE).stream().map(toPairItem).collect(toList())));
          }
          break;
        case MUSIC_TYPE:
          setMusicMaterialCodes(lang, storageService, fixedFieldCodesGroup);
          if (tag.equals(OTHER_MATERIAL_TAG_CODE)) {
            fixedFieldCodesGroup.addResults(new FixedFieldElement(MATERIAL_TYPE,
              storageService.getCodesList(lang, MUSIC_MATERIAL_CODE).stream().map(toPairItem).collect(toList())));
          }

          break;
        case SERIAL_TYPE:
          setSerialMaterialCodes(lang, storageService, fixedFieldCodesGroup);
          if (tag.equals(OTHER_MATERIAL_TAG_CODE)) {
            Pair p = new Pair();
            p.setCode("s");
            p.setDescription("Serial");
            List<Pair> list = Collections.singletonList(p);
            fixedFieldCodesGroup.addResults(new FixedFieldElement(MATERIAL_TYPE, list));
          }
          break;
        case MIXED_TYPE:
          fixedFieldCodesGroup.addResults(new FixedFieldElement(FORM_OF_ITEM_CODE, storageService.getCodesList(lang, FORM_OF_ITEM).stream().map(toPairItem).collect(toList())));
          if (tag.equals(OTHER_MATERIAL_TAG_CODE)) {
            Pair p = new Pair();
            p.setCode("p");
            p.setDescription("Mixed material");
            List<Pair> list = Collections.singletonList(p);
            fixedFieldCodesGroup.addResults(new FixedFieldElement(MATERIAL_TYPE, list));
          }
          break;
        case MAP_TYPE:
          setMapMaterialCodes(lang, storageService, fixedFieldCodesGroup);
          if (tag.equals(OTHER_MATERIAL_TAG_CODE)) {
            fixedFieldCodesGroup.addResults(new FixedFieldElement(MATERIAL_TYPE,
              storageService.getCodesList(lang, MAP_TYPE_MATERIAL).stream().map(toPairItem).collect(toList())));
          }
          break;
        case VISUAL_TYPE:
          setVisualMaterialCodes(lang, storageService, fixedFieldCodesGroup);
          if (tag.equals(OTHER_MATERIAL_TAG_CODE)) {
            fixedFieldCodesGroup.addResults(new FixedFieldElement(MATERIAL_TYPE,
              storageService.getCodesList(lang, VM_MATERIAL_CODE).stream().map(toPairItem).collect(toList())));
          }
          break;
        case COMPUTER_TYPE:
          setComputerMaterialCodes(lang, storageService, fixedFieldCodesGroup);
          if (tag.equals(OTHER_MATERIAL_TAG_CODE)) {
            Pair p = new Pair();
            p.setCode("m");
            p.setDescription("Computer file");
            List<Pair> list = Collections.singletonList(p);
            fixedFieldCodesGroup.addResults(new FixedFieldElement(MATERIAL_TYPE, list));
          }

          break;
        default:

      }

      if (tag.equals(MATERIAL_TAG_CODE)) {
        fixedFieldCodesGroup.addResults(new FixedFieldElement("language", storageService.getCodesList(lang, LANGUAGE).stream().map(toPairItem).collect(toList())));
        fixedFieldCodesGroup.addResults(new FixedFieldElement("modifiedRecordTypes", storageService.getCodesList(lang, MODIFIED_RECORD_TYPE).stream().map(toPairItem).collect(toList())));
        fixedFieldCodesGroup.addResults(new FixedFieldElement("catalogSources", storageService.getCodesList(lang, CATALOGUING_SOURCE).stream().map(toPairItem).collect(toList())));
      }
    } else {
      logger.error(MessageCatalog._00019_HEADER_TYPE_ID_WRONG, tag);
    }

  }


  /**
   * Sets values Computer File type for 008/006 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setComputerMaterialCodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement("computerTargetAudienceCodes", storageService.getCodesList(lang, COMPUTER_TARGET_AUDIENCE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("computerFormOfItemCodes", storageService.getCodesList(lang, COMPUTER_FORM_OF_ITEM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("computerTypeMaterialCodes", storageService.getCodesList(lang, COMPUTER_TYPE_MATERIAL).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(GOVERNMENT_PUBLICATION_CODE, storageService.getCodesList(lang, GOV_PUBLICATION).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Visual type for 008/006 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setVisualMaterialCodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement("visualTargetAudienceCodes", storageService.getCodesList(lang, VSL_TARGET_AUDIENCE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(GOVERNMENT_PUBLICATION_CODE, storageService.getCodesList(lang, GOV_PUBLICATION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(FORM_OF_ITEM_CODE, storageService.getCodesList(lang, FORM_OF_ITEM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("visualTypeMaterialCodes", storageService.getCodesList(lang, VSL_TYPE_MATERIAL).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("visualTechnique", storageService.getCodesList(lang, VSL_TECHNIQUE).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Map type for 008/006 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setMapMaterialCodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement("mapReliefCodes", storageService.getCodesList(lang, MAP_RELIEF).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("mapProjectionCodes", storageService.getCodesList(lang, MAP_PROJECTION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("mapTypeCartographicMaterialCodes", storageService.getCodesList(lang, MAP_TYPE_MATERIAL).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(GOVERNMENT_PUBLICATION_CODE, storageService.getCodesList(lang, GOV_PUBLICATION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(FORM_OF_ITEM_CODE, storageService.getCodesList(lang, FORM_OF_ITEM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("mapIndexCodes", storageService.getCodesList(lang, MAP_INDEX).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("mapSpecialFormatCharacteristicCodes", storageService.getCodesList(lang, MAP_SPECIAL_FORMAT_CHARACTERISTIC).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Music type for 008/006 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setMusicMaterialCodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {

    fixedFieldCodesGroup.addResults(new FixedFieldElement("musicFormats", storageService.getCodesList(lang, MSC_FORMAT).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("musicFormOfCompositions", storageService.getCodesList(lang, MSC_PARTS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("musicParts", storageService.getCodesList(lang, MSC_PARTS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("targetAudienceCode", storageService.getCodesList(lang, TARGET_AUDIENCE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(FORM_OF_ITEM_CODE, storageService.getCodesList(lang, FORM_OF_ITEM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("musicTextualMaterialCodes", storageService.getCodesList(lang, MSC_TEXTUAL_MAT_CODE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("musicLiteraryTextCodes", storageService.getCodesList(lang, MSC_LITERARY_TEXT).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("musicTranspositionArrangementCode", storageService.getCodesList(lang, MSC_TRANSPOSITION_CODE).stream().map(toPairItem).collect(toList())));

  }

  /**
   * Sets values Continuing resource type for 008/006 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setSerialMaterialCodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {

    final List<Pair> natureOfContents = storageService.getCodesList(lang, NATURE_OF_CONTENT).stream().map(toPairItem).collect(toList());

    fixedFieldCodesGroup.addResults(new FixedFieldElement("serialFrequencyCode", storageService.getCodesList(lang, SRL_FREQUENCY).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("serialRegularityCode", storageService.getCodesList(lang, SRL_REGULARITY).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("serialTypeOfContinuingResourceCodes", storageService.getCodesList(lang, SRL_REGULARITY).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("serialFormOriginalCodes", storageService.getCodesList(lang, SRL_FORM_ORGNL_ITEM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(FORM_OF_ITEM_CODE, storageService.getCodesList(lang, FORM_OF_ITEM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("natureOfContent1", natureOfContents));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("natureOfContent2", natureOfContents));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("natureOfContent3", natureOfContents));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("serialNatureOfWorkCodes", natureOfContents));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(GOVERNMENT_PUBLICATION_CODE, storageService.getCodesList(lang, GOV_PUBLICATION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("conferencePublicationCode", storageService.getCodesList(lang, CONF_PUBLICATION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("serialOriginAlphabetCodes", storageService.getCodesList(lang, SRL_ORIGIN_ALPHABET).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("serialEntryConvCodes", storageService.getCodesList(lang, SRL_ENTRY_CONVENTION).stream().map(toPairItem).collect(toList())));

  }

  /**
   * Sets values Book type for 008/006 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  public static void setBookMaterialCodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    final List<Pair> bookIllustrations = storageService.getCodesList(lang, BOOK_ILLUSTRATION).stream().map(toPairItem).collect(toList());
    final List<Pair> natureOfContents = storageService.getCodesList(lang, NATURE_OF_CONTENT).stream().map(toPairItem).collect(toList());

    fixedFieldCodesGroup.addResults(new FixedFieldElement("bookIllustrationCode1", bookIllustrations));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("bookIllustrationCode2", bookIllustrations));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("bookIllustrationCode3", bookIllustrations));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("bookIllustrationCode4", bookIllustrations));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("targetAudienceCode", storageService.getCodesList(lang, TARGET_AUDIENCE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(FORM_OF_ITEM_CODE, storageService.getCodesList(lang, FORM_OF_ITEM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("natureOfContent1", natureOfContents));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("natureOfContent2", natureOfContents));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("natureOfContent3", natureOfContents));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("natureOfContent4", natureOfContents));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(GOVERNMENT_PUBLICATION_CODE, storageService.getCodesList(lang, GOV_PUBLICATION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("conferencePublicationCode", storageService.getCodesList(lang, CONF_PUBLICATION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("bookFestschrift", storageService.getCodesList(lang, BOOK_FESTSCHRIFT).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("bookIndexAvailabilityCode", storageService.getCodesList(lang, BOOK_INDEX).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("bookLiteraryFormTypeCode", storageService.getCodesList(lang, BOOK_LITERARY_FORM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("bookBiographyCode", storageService.getCodesList(lang, BOOK_BIOGRAPHY).stream().map(toPairItem).collect(toList())));

  }

  /**
   * Inject codes group for Leader tag.
   *
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   * @param storageService       the storage service.
   * @param lang                 the lang associated with the current request.
   */
  public static void injectLeaderCodes(final FixedFieldCodesGroup fixedFieldCodesGroup, final StorageService storageService, final String lang) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement("itemRecordStatusCode", storageService.getRecordStatusTypes(lang).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("itemRecordTypeCode", storageService.getRecordTypes(lang).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("itemBibliographicLevelCode", storageService.getBibliographicLevels(lang).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("itemControlTypeCode", storageService.getControlTypes(lang).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("characterCodingSchemeCode", storageService.getCharacterEncodingSchemas(lang).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("encodingLevel", storageService.getEncodingLevels(lang).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("descriptiveCataloguingCode", storageService.getDescriptiveCatalogForms(lang).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("linkedRecordCode", storageService.getMultipartResourceLevels(lang).stream().map(toPairItem).collect(toList())));
  }

  public static void injectDefaultValues(FixedFieldCodesGroup fixedFieldCodesGroup, StorageService storageService, Map<String, String> parameter, int headerType, String lang, Map<String, String> configuration) {
    FieldTemplate fieldTemplate = ofNullable(CatalogingInformation.getFixedField(storageService, headerType, parameter.get("code"), parameter.get("leader"), parameter.get("valueField"), lang, configuration))
      .map(f -> {
        final FieldTemplate fieldT = new FieldTemplate();
        fieldT.setFixedField(f);
        return fieldT;
      }).orElseGet(() -> {
        logger.error(_00016_FIELD_PARAMETER_INVALID, CONTROL_FIELD_CATEGORY_CODE, parameter.get("code"));
        return new FieldTemplate();
      });
    fixedFieldCodesGroup.getResults()
      .keySet()
      .forEach(key -> {
        String currentValue = (String) fieldTemplate
          .getFixedField()
          .getAttributes()
          .get(key);
        if (currentValue != null) {
          fixedFieldCodesGroup.getResults().get(key).setDafaultValue(currentValue.trim());
        } else {
          fixedFieldCodesGroup.getResults().get(key).setDafaultValue(EMPTY_STRING);
        }
      });

  }

  public static void injectPhysicalDescriptionCodes(final FixedFieldCodesGroup fixedFieldCodesGroup, final StorageService storageService, final String lang, final int headerTypeCode) {
    String categoryOfMaterial = PHYSICAL_TYPES_MAP.get(headerTypeCode);
    if (categoryOfMaterial != null) {
      fixedFieldCodesGroup.addResults(new FixedFieldElement("categoryOfMaterial", storageService.getCodesList(lang, CodeListsType.CATEGORY_MATERIAL)
        .stream()
        .map(toPairItem)
        .collect(toList())));
      switch (categoryOfMaterial) {
        case ELECTRONIC_RESOURCE:
          setPhysicalInfoCFcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case GLOBE:
          setPhysicalInfoGLBcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case MAP_CODE:
          setPhysicalInfoMAPcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case TACTILE_MATERIAL:
          setPhysicalInfoTCTcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case PROJECTED_GRAPHIC:
          setPhysicalInfoPGcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case MICROFORM:
          setPhysicalInfoMICcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case NON_PROJECTED_GRAPHIC:
          setPhysicalInfoNPGcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case MOTION_PICTURE:
          setPhysicalInfoMPcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case KIT_CODE:
          setPhysicalInfoKITcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case NOTATED_MUSIC:
          setPhysicalInfoNMcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case REMOTE_SENSING_IMAGE:
          setPhysicalInfoRSIcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case SOUND_RECORDING:
          setPhysicalInfoSNDcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case TEXT_CODE:
          setPhysicalInfoTXTcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case UNSPECIFIED:
          setPhysicalInfoUNScodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case VIDEO_RECORDING:
          setPhysicalInfoVRcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        default:
      }

    } else {
      logger.error(_00019_HEADER_TYPE_ID_WRONG, PHYSICAL_DESCRIPTION_TAG_CODE);
    }
  }
}
