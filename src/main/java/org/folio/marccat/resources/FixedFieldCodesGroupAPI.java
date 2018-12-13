package org.folio.marccat.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.marccat.config.Global;
import org.folio.marccat.ModMarccat;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.integration.StorageService;
import org.folio.marccat.config.log.MessageCatalog;
import org.folio.marccat.resources.domain.FixedFieldCodesGroup;
import org.folio.marccat.resources.domain.Pair;
import org.folio.marccat.shared.CodeListsType;
import org.folio.marccat.shared.GeneralInformation;
import org.folio.marccat.shared.PhysicalInformation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.folio.marccat.integration.CatalogingHelper.doGet;

/**
 * Fixed-Field Codes Groups RESTful APIs.
 *
 * @author nbianchini
 * @since 1.0
 */
@RestController
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class FixedFieldCodesGroupAPI extends BaseResource {

  /**
   * Adapter that converts existing stringValue object in nature of content code Okapi resource.
   */
  private Function <Avp <String>, Pair> toPairItem = source -> {
    final Pair pairItem = new Pair();
    pairItem.setCode(source.getValue());
    pairItem.setDescription(source.getLabel());
    return pairItem;
  };


  @GetMapping("/fixed-fields-code-groups")
  public FixedFieldCodesGroup getFixedFieldCodesGroups(
	@RequestParam (required = false) final String leader ,
    @RequestParam final String code,
    @RequestParam final int headerTypeCode,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final FixedFieldCodesGroup fixedFieldCodesGroup = new FixedFieldCodesGroup();
      return ofNullable(code)
        .map(tag -> {
          if (tag.equals(Global.LEADER_TAG_NUMBER)) {
            injectLeaderCodes(fixedFieldCodesGroup, storageService, lang);
          } else if (tag.equals(Global.MATERIAL_TAG_CODE) || tag.equals(Global.OTHER_MATERIAL_TAG_CODE)) {
            injectMaterialCodes(fixedFieldCodesGroup, storageService, lang, headerTypeCode, tag);
          } else if (tag.equals(Global.PHYSICAL_DESCRIPTION_TAG_CODE)) {
            injectPhysicalDescriptionCodes(fixedFieldCodesGroup, storageService, lang, headerTypeCode);
          } else {
            logger.error(String.format(MessageCatalog._00017_CODES_GROUPS_NOT_AVAILABLE, code));
            return null;
          }
          return fixedFieldCodesGroup;
        }).orElse(null);
    }, tenant, configurator);
  }

  private void injectPhysicalDescriptionCodes(final FixedFieldCodesGroup fixedFieldCodesGroup, final StorageService storageService, final String lang, final int headerTypeCode) {
    ofNullable(Global.PHYSICAL_TYPES_MAP.get(headerTypeCode))
      .map(categoryOfMaterial -> {

        final PhysicalInformation pi = new PhysicalInformation();
        fixedFieldCodesGroup.setCategoryOfMaterialCodes(storageService.getCodesList(lang, CodeListsType.CATEGORY_MATERIAL).stream().map(toPairItem).collect(toList()));

        if (pi.isElectronicResource(categoryOfMaterial)) {
          setPhysicalInfoCFcodes(lang, storageService, fixedFieldCodesGroup);
        } else if (pi.isGlobe(categoryOfMaterial)) {
          setPhysicalInfoGLBcodes(lang, storageService, fixedFieldCodesGroup);
        } else if (pi.isMap(categoryOfMaterial)) {
          setPhysicalInfoMAPcodes(lang, storageService, fixedFieldCodesGroup);
        } else if (pi.isTactileMaterial(categoryOfMaterial)) {
          setPhysicalInfoTCTcodes(lang, storageService, fixedFieldCodesGroup);
        } else if (pi.isProjectedGraphic(categoryOfMaterial)) {
          setPhysicalInfoPGcodes(lang, storageService, fixedFieldCodesGroup);
        } else if (pi.isMicroform(categoryOfMaterial)) {
          setPhysicalInfoMICcodes(lang, storageService, fixedFieldCodesGroup);
        } else if (pi.isNonProjectedGraphic(categoryOfMaterial)) {
          setPhysicalInfoNPGcodes(lang, storageService, fixedFieldCodesGroup);
        } else if (pi.isMotionPicture(categoryOfMaterial)) {
          setPhysicalInfoMPcodes(lang, storageService, fixedFieldCodesGroup);
        } else if (pi.isKit(categoryOfMaterial)) {
          setPhysicalInfoKITcodes(lang, storageService, fixedFieldCodesGroup);
        } else if (pi.isNotatedMusic(categoryOfMaterial)) {
          setPhysicalInfoNMcodes(lang, storageService, fixedFieldCodesGroup);
        } else if (pi.isRemoteSensingImage(categoryOfMaterial)) {
          setPhysicalInfoRSIcodes(lang, storageService, fixedFieldCodesGroup);
        } else if (pi.isSoundRecording(categoryOfMaterial)) {
          setPhysicalInfoSNDcodes(lang, storageService, fixedFieldCodesGroup);
        } else if (pi.isText(categoryOfMaterial)) {
          setPhysicalInfoTXTcodes(lang, storageService, fixedFieldCodesGroup);
        } else if (pi.isUnspecified(categoryOfMaterial)) {
          setPhysicalInfoUNScodes(lang, storageService, fixedFieldCodesGroup);
        } else if (pi.isVideoRecording(categoryOfMaterial)) {
          setPhysicalInfoVRcodes(lang, storageService, fixedFieldCodesGroup);
        }

        return fixedFieldCodesGroup;
      }).orElseGet(() -> {
      logger.error(MessageCatalog._00019_HEADER_TYPE_ID_WRONG, Global.PHYSICAL_DESCRIPTION_TAG_CODE);
      return null;
    });

  }

  /**
   * Sets values Video Recording type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoVRcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.setSpecificMaterialDesignationCodes(storageService.getCodesList(lang, CodeListsType.VR_SPEC_DESIGN).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setColorCodes(storageService.getCodesList(lang, CodeListsType.VR_COLOR).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setVideoRecordingFormatCodes(storageService.getCodesList(lang, CodeListsType.VR_FORMAT).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setSoundOnMediumOrSeparateCodes(storageService.getCodesList(lang, CodeListsType.SOUND_MEDIUM_OR_SEP).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setMediumForSoundCodes(storageService.getCodesList(lang, CodeListsType.MEDIUM_FOR_SOUND).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setDimensionCodes(storageService.getCodesList(lang, CodeListsType.VR_DIMENSIONS).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setConfigurationCodes(storageService.getCodesList(lang, CodeListsType.VR_CONF_PLAYBACK).stream().map(toPairItem).collect(toList()));
  }

  /**
   * Sets values Unspecified type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoUNScodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.setSpecificMaterialDesignationCodes(storageService.getCodesList(lang, CodeListsType.UNS_SPEC_DESIGN).stream().map(toPairItem).collect(toList()));
  }

  /**
   * Sets values Text type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoTXTcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.setSpecificMaterialDesignationCodes(storageService.getCodesList(lang, CodeListsType.TXT_SPEC_DESIGN).stream().map(toPairItem).collect(toList()));
  }

  /**
   * Sets values Sound Recording type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoSNDcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.setSpecificMaterialDesignationCodes(storageService.getCodesList(lang, CodeListsType.SND_SPEC_DESIGN).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setSpeedCodes(storageService.getCodesList(lang, CodeListsType.SND_SPEED).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setConfigurationCodes(storageService.getCodesList(lang, CodeListsType.SND_CONF_PLAYBACK).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setGrooveWidthCodes(storageService.getCodesList(lang, CodeListsType.SND_GROOVE_WIDTH).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setDimensionCodes(storageService.getCodesList(lang, CodeListsType.SND_DIMENSIONS).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setTapeWidthCodes(storageService.getCodesList(lang, CodeListsType.SND_TAPE_WIDTH).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setTapeConfigurationCodes(storageService.getCodesList(lang, CodeListsType.SND_TAPE_CONF).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setDiscTypeCodes(storageService.getCodesList(lang, CodeListsType.SND_DISC_TYPE).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setSndMaterialTypeCodes(storageService.getCodesList(lang, CodeListsType.SND_MATERIAL_TYPE).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setCuttingTypeCodes(storageService.getCodesList(lang, CodeListsType.SND_CUTTING).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setSpecialPlaybackCharacteristicsCodes(storageService.getCodesList(lang, CodeListsType.SND_SPEC_PLAYBACK).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setStorageTechniqueCodes(storageService.getCodesList(lang, CodeListsType.SND_STORAGE_TECNIQUE).stream().map(toPairItem).collect(toList()));
  }

  /**
   * Sets values Remote Sensing Image type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoRSIcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.setSpecificMaterialDesignationCodes(storageService.getCodesList(lang, CodeListsType.RSI_SPEC_DESIGN).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setAltitudeOfSensorCodes(storageService.getCodesList(lang, CodeListsType.RSI_ALTITUDE).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setAttitudeOfSensorCodes(storageService.getCodesList(lang, CodeListsType.RSI_ATTITUDE).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setCloudCoverCodes(storageService.getCodesList(lang, CodeListsType.RSI_CLOUD_COVER).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setPlatformConstructionTypeCodes(storageService.getCodesList(lang, CodeListsType.RSI_PLAT_CONSTRUCTION).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setPlatformUseCodes(storageService.getCodesList(lang, CodeListsType.RSI_PLAT_USE).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setSensorTypeCodes(storageService.getCodesList(lang, CodeListsType.RSI_SENSOR_TYPE).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setRemoteSensingDataTypeCodes(storageService.getCodesList(lang, CodeListsType.RSI_DATA_TYPE).stream().map(toPairItem).collect(toList()));
  }

  /**
   * Sets values Notated Music type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoNMcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.setSpecificMaterialDesignationCodes(storageService.getCodesList(lang, CodeListsType.NMU_SPEC_DESIGN).stream().map(toPairItem).collect(toList()));
  }

  /**
   * Sets values Kit type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoKITcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.setSpecificMaterialDesignationCodes(storageService.getCodesList(lang, CodeListsType.KIT_SPEC_DESIGN).stream().map(toPairItem).collect(toList()));
  }

  /**
   * Sets values Motion Picture type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoMPcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {

    fixedFieldCodesGroup.setSpecificMaterialDesignationCodes(storageService.getCodesList(lang, CodeListsType.MP_SPEC_DESIGN).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setColorCodes(storageService.getCodesList(lang, CodeListsType.MP_COLOR).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setPresentationFormatCodes(storageService.getCodesList(lang, CodeListsType.MP_PRESENT_FORMAT).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setSoundOnMediumOrSeparateCodes(storageService.getCodesList(lang, CodeListsType.SOUND_MEDIUM_OR_SEP).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setMediumForSoundCodes(storageService.getCodesList(lang, CodeListsType.MEDIUM_FOR_SOUND).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setDimensionCodes(storageService.getCodesList(lang, CodeListsType.MP_DIMENSIONS).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setConfigurationCodes(storageService.getCodesList(lang, CodeListsType.MP_CONF_PLAYBACK).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setProductionElementsCodes(storageService.getCodesList(lang, CodeListsType.MP_PROD_ELEM).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setPolarityCodes(storageService.getCodesList(lang, CodeListsType.MP_POLARITY).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setGenerationCodes(storageService.getCodesList(lang, CodeListsType.MP_GENERATION).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setBaseOfFilmCodes(storageService.getCodesList(lang, CodeListsType.MP_BASE_FILM).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setRefinedCategoriesOfColourCodes(storageService.getCodesList(lang, CodeListsType.MP_REFINE_CAT_COLOR).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setKindOfColourStockCodes(storageService.getCodesList(lang, CodeListsType.MP_KIND_COLORS).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setDeteriorationStageCodes(storageService.getCodesList(lang, CodeListsType.MP_DETERIORATION_STAGE).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setCompletenessCodes(storageService.getCodesList(lang, CodeListsType.MP_COMPLETENESS).stream().map(toPairItem).collect(toList()));
  }

  /**
   * Sets values Non Project Graphic type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoNPGcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.setSpecificMaterialDesignationCodes(storageService.getCodesList(lang, CodeListsType.NPG_SPEC_DESIGN).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setColorCodes(storageService.getCodesList(lang, CodeListsType.NPG_COLOR).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setPrimarySupportMaterialCodes(storageService.getCodesList(lang, CodeListsType.NPG_PRIMARY_SUPPORT).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setSecondarySupportMaterialCodes(storageService.getCodesList(lang, CodeListsType.NPG_SECONDARY_SUPPORT).stream().map(toPairItem).collect(toList()));
  }

  /**
   * Sets values Microform type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoMICcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.setSpecificMaterialDesignationCodes(storageService.getCodesList(lang, CodeListsType.MIC_SPEC_DESIGN).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setPolarityCodes(storageService.getCodesList(lang, CodeListsType.MIC_POLARITY).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setDimensionCodes(storageService.getCodesList(lang, CodeListsType.MIC_DIMENSIONS).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setReductionRatioRangeCodes(storageService.getCodesList(lang, CodeListsType.MIC_REDUCT_RATIO_RANGE).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setColorCodes(storageService.getCodesList(lang, CodeListsType.MIC_COLOR).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setEmulsionOnFilmCodes(storageService.getCodesList(lang, CodeListsType.MIC_EMUL_FILM).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setGenerationCodes(storageService.getCodesList(lang, CodeListsType.MIC_GENERATION).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setBaseOfFilmCodes(storageService.getCodesList(lang, CodeListsType.MIC_BASE_FILM).stream().map(toPairItem).collect(toList()));
  }

  /**
   * Sets values Project Graphic type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoPGcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.setSpecificMaterialDesignationCodes(storageService.getCodesList(lang, CodeListsType.PG_SPEC_DESIGN).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setColorCodes(storageService.getCodesList(lang, CodeListsType.PG_COLOR).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setBaseOfEmulsionCodes(storageService.getCodesList(lang, CodeListsType.PG_EMUL_BASE).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setSoundOnMediumOrSeparateCodes(storageService.getCodesList(lang, CodeListsType.SOUND_MEDIUM_OR_SEP).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setMediumForSoundCodes(storageService.getCodesList(lang, CodeListsType.MEDIUM_FOR_SOUND).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setDimensionCodes(storageService.getCodesList(lang, CodeListsType.PG_DIMENSIONS).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setSecondarySupportMaterialCodes(storageService.getCodesList(lang, CodeListsType.PG_SECONDARY_SUPPORT).stream().map(toPairItem).collect(toList()));
  }

  /**
   * Sets values Map type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoMAPcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.setSpecificMaterialDesignationCodes(storageService.getCodesList(lang, CodeListsType.MAP_SPEC_DESIGN).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setColorCodes(storageService.getCodesList(lang, CodeListsType.MAP_COLOR).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setPhysicalMediumCodes(storageService.getCodesList(lang, CodeListsType.MAP_PHYSICAL_MEDIUM).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setTypeOfReproductionCodes(storageService.getCodesList(lang, CodeListsType.MAP_TYPE_OF_REPRODUCTION).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setProductionDetailsCodes(storageService.getCodesList(lang, CodeListsType.MAP_PRODUCTION_DETAILS).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setPolarityCodes(storageService.getCodesList(lang, CodeListsType.MAP_POLARITY).stream().map(toPairItem).collect(toList()));
  }

  /**
   * Sets values Globe type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoGLBcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.setSpecificMaterialDesignationCodes(storageService.getCodesList(lang, CodeListsType.GLB_SPEC_DESIGN).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setColorCodes(storageService.getCodesList(lang, CodeListsType.GLB_COLOR).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setPhysicalMediumCodes(storageService.getCodesList(lang, CodeListsType.GLB_PHYSICAL_MEDIUM).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setTypeOfReproductionCodes(storageService.getCodesList(lang, CodeListsType.GLB_TYPE_OF_REPRODUCTION).stream().map(toPairItem).collect(toList()));
  }

  /**
   * Sets values Tactile Material type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoTCTcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.setSpecificMaterialDesignationCodes(storageService.getCodesList(lang, CodeListsType.TCT_SPEC_DESIGN).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setClassOfBrailleWritingCodes(storageService.getCodesList(lang, CodeListsType.TCT_CLASS_BRAILLE_WRITING).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setLevelOfContractionCodes(storageService.getCodesList(lang, CodeListsType.TCT_CONTRACTION_LVL).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setBrailleMusicFormatCodes(storageService.getCodesList(lang, CodeListsType.TCT_BRAILLE_MUSIC_FORMAT).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setSpecificPhysicalCharacteristicsCodes(storageService.getCodesList(lang, CodeListsType.TCT_SPECIAL_PHYSICAL_CHAR).stream().map(toPairItem).collect(toList()));
  }

  /**
   * Sets values Computer File type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoCFcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.setSpecificMaterialDesignationCodes(storageService.getCodesList(lang, CodeListsType.CF_SPEC_DESIGN).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setColorCodes(storageService.getCodesList(lang, CodeListsType.CF_COLOR).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setDimensionCodes(storageService.getCodesList(lang, CodeListsType.CF_DIMENSIONS).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setIncludesSoundCodes(storageService.getCodesList(lang, CodeListsType.SOUND_MEDIUM_OR_SEP).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setFileFormatsCodes(storageService.getCodesList(lang, CodeListsType.CF_FILE_FORMAT).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setQualityAssuranceTargetCodes(storageService.getCodesList(lang, CodeListsType.CF_QUALITY_ASS).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setAntecedentSourceCodes(storageService.getCodesList(lang, CodeListsType.CF_ANTECEDENT_SRC).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setLevelOfCompressionCodes(storageService.getCodesList(lang, CodeListsType.CF_COMPRESSION_LVL).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setReformattingQualityCodes(storageService.getCodesList(lang, CodeListsType.CF_REFORMATTING_QUALITY).stream().map(toPairItem).collect(toList()));
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
  private void injectMaterialCodes(final FixedFieldCodesGroup fixedFieldCodesGroup,
                                   final StorageService storageService,
                                   final String lang,
                                   final int headerTypeCode,
                                   final String tag) {
    ofNullable(storageService.getMaterialTypeInfosByHeaderCode(headerTypeCode, tag))
      .map(mapRecordTypeMaterial -> {
        final GeneralInformation gi = new GeneralInformation();
        gi.setFormOfMaterial((String) mapRecordTypeMaterial.get(Global.FORM_OF_MATERIAL_LABEL));
        if (gi.isBook()) {
          setBookMaterialCodes(lang, storageService, fixedFieldCodesGroup);
        } else if (gi.isMusic()) {
          setMusicMaterialCodes(lang, storageService, fixedFieldCodesGroup);
        } else if (gi.isSerial()) {
          setSerialMaterialCodes(lang, storageService, fixedFieldCodesGroup);
        } else if (gi.isMixedMaterial()) {
          fixedFieldCodesGroup.setFormOfItemCodes(storageService.getCodesList(lang, CodeListsType.FORM_OF_ITEM).stream().map(toPairItem).collect(toList()));
        } else if (gi.isMap()) {
          setMapMaterialCodes(lang, storageService, fixedFieldCodesGroup);
        } else if (gi.isVisualMaterial()) {
          setVisualMaterialCodes(lang, storageService, fixedFieldCodesGroup);
        } else if (gi.isComputerFile()) {
          setComputerMaterialCodes(lang, storageService, fixedFieldCodesGroup);
        }

        if (tag.equals(Global.MATERIAL_TAG_CODE)) {
          fixedFieldCodesGroup.setDateTypes(storageService.getCodesList(lang, CodeListsType.DATE_TYPE).stream().map(toPairItem).collect(toList()));
          fixedFieldCodesGroup.setModifiedRecordTypes(storageService.getCodesList(lang, CodeListsType.MODIFIED_RECORD_TYPE).stream().map(toPairItem).collect(toList()));
          fixedFieldCodesGroup.setCatalogSources(storageService.getCodesList(lang, CodeListsType.CATALOGUING_SOURCE).stream().map(toPairItem).collect(toList()));
        }

        return fixedFieldCodesGroup;
      }).orElseGet(() -> {
      logger.error(MessageCatalog._00019_HEADER_TYPE_ID_WRONG, tag);
      return null;
    });
  }

  /**
   * Sets values Computer File type for 008/006 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setComputerMaterialCodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.setComputerTargetAudienceCodes(storageService.getCodesList(lang, CodeListsType.COMPUTER_TARGET_AUDIENCE).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setComputerFormOfItemCodes(storageService.getCodesList(lang, CodeListsType.COMPUTER_FORM_OF_ITEM).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setComputerTypeMaterialCodes(storageService.getCodesList(lang, CodeListsType.COMPUTER_TYPE_MATERIAL).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setGovernmentPublicationCodes(storageService.getCodesList(lang, CodeListsType.GOV_PUBLICATION).stream().map(toPairItem).collect(toList()));
  }

  /**
   * Sets values Visual type for 008/006 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setVisualMaterialCodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.setVisualTargetAudienceCodes(storageService.getCodesList(lang, CodeListsType.VSL_TARGET_AUDIENCE).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setGovernmentPublicationCodes(storageService.getCodesList(lang, CodeListsType.GOV_PUBLICATION).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setFormOfItemCodes(storageService.getCodesList(lang, CodeListsType.FORM_OF_ITEM).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setVisualTypeMaterialCodes(storageService.getCodesList(lang, CodeListsType.VSL_TYPE_MATERIAL).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setVisualTechnique(storageService.getCodesList(lang, CodeListsType.VSL_TECHNIQUE).stream().map(toPairItem).collect(toList()));
  }

  /**
   * Sets values Map type for 008/006 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setMapMaterialCodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.setMapReliefCodes(storageService.getCodesList(lang, CodeListsType.MAP_RELIEF).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setMapProjectionCodes(storageService.getCodesList(lang, CodeListsType.MAP_PROJECTION).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setMapTypeCartographicMaterialCodes(storageService.getCodesList(lang, CodeListsType.MAP_TYPE_MATERIAL).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setGovernmentPublicationCodes(storageService.getCodesList(lang, CodeListsType.GOV_PUBLICATION).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setFormOfItemCodes(storageService.getCodesList(lang, CodeListsType.FORM_OF_ITEM).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setMapIndexCodes(storageService.getCodesList(lang, CodeListsType.MAP_INDEX).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setMapSpecialFormatCharacteristicCodes(storageService.getCodesList(lang, CodeListsType.MAP_SPECIAL_FORMAT_CHARACTERISTIC).stream().map(toPairItem).collect(toList()));
  }

  /**
   * Sets values Music type for 008/006 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setMusicMaterialCodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.setMusicFormatCodes(storageService.getCodesList(lang, CodeListsType.MSC_FORMAT).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setMusicFormOfCompositionCodes(storageService.getCodesList(lang, CodeListsType.MSC_FORM_OF_COMPOSITION).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setMusicPartCodes(storageService.getCodesList(lang, CodeListsType.MSC_PARTS).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setTargetAudienceCodes(storageService.getCodesList(lang, CodeListsType.TARGET_AUDIENCE).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setFormOfItemCodes(storageService.getCodesList(lang, CodeListsType.FORM_OF_ITEM).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setMusicTextualMaterialCodes(storageService.getCodesList(lang, CodeListsType.MSC_TEXTUAL_MAT_CODE).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setMusicLiteraryTextCodes(storageService.getCodesList(lang, CodeListsType.MSC_LITERARY_TEXT).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setMusicTranspositionArrangementCodes(storageService.getCodesList(lang, CodeListsType.MSC_TRANSPOSITION_CODE).stream().map(toPairItem).collect(toList()));
  }

  /**
   * Sets values Continuing resource type for 008/006 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setSerialMaterialCodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.setSerialFrequencyCodes(storageService.getCodesList(lang, CodeListsType.SRL_FREQUENCY).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setSerialRegularityCodes(storageService.getCodesList(lang, CodeListsType.SRL_REGULARITY).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setSerialTypeOfContinuingResourceCodes(storageService.getCodesList(lang, CodeListsType.SRL_TYPE_CONTINUING_RESOURCE).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setSerialFormOriginalCodes(storageService.getCodesList(lang, CodeListsType.SRL_FORM_ORGNL_ITEM).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setFormOfItemCodes(storageService.getCodesList(lang, CodeListsType.FORM_OF_ITEM).stream().map(toPairItem).collect(toList()));

    final List <Pair> natureOfContents = storageService.getCodesList(lang, CodeListsType.NATURE_OF_CONTENT).stream().map(toPairItem).collect(toList());
    fixedFieldCodesGroup.setNatureOfContents1(natureOfContents);
    fixedFieldCodesGroup.setNatureOfContents2(natureOfContents);
    fixedFieldCodesGroup.setNatureOfContents3(natureOfContents);
    fixedFieldCodesGroup.setSerialNatureOfWorkCodes(natureOfContents);

    fixedFieldCodesGroup.setGovernmentPublicationCodes(storageService.getCodesList(lang, CodeListsType.GOV_PUBLICATION).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setConferencePublicationCodes(storageService.getCodesList(lang, CodeListsType.CONF_PUBLICATION).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setSerialOriginAlphabetCodes(storageService.getCodesList(lang, CodeListsType.SRL_ORIGIN_ALPHABET).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setSerialEntryConvCodes(storageService.getCodesList(lang, CodeListsType.SRL_ENTRY_CONVENTION).stream().map(toPairItem).collect(toList()));
  }

  /**
   * Sets values Book type for 008/006 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setBookMaterialCodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    final List <Pair> bookIllustrations = storageService.getCodesList(lang, CodeListsType.BOOK_ILLUSTRATION).stream().map(toPairItem).collect(toList());
    fixedFieldCodesGroup.setBookIllustrationCodes1(bookIllustrations);
    fixedFieldCodesGroup.setBookIllustrationCodes2(bookIllustrations);
    fixedFieldCodesGroup.setBookIllustrationCodes3(bookIllustrations);
    fixedFieldCodesGroup.setBookIllustrationCodes4(bookIllustrations);

    fixedFieldCodesGroup.setTargetAudienceCodes(storageService.getCodesList(lang, CodeListsType.TARGET_AUDIENCE).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setFormOfItemCodes(storageService.getCodesList(lang, CodeListsType.FORM_OF_ITEM).stream().map(toPairItem).collect(toList()));

    final List <Pair> natureOfContents = storageService.getCodesList(lang, CodeListsType.NATURE_OF_CONTENT).stream().map(toPairItem).collect(toList());
    fixedFieldCodesGroup.setNatureOfContents1(natureOfContents);
    fixedFieldCodesGroup.setNatureOfContents2(natureOfContents);
    fixedFieldCodesGroup.setNatureOfContents3(natureOfContents);
    fixedFieldCodesGroup.setNatureOfContents4(natureOfContents);

    fixedFieldCodesGroup.setGovernmentPublicationCodes(storageService.getCodesList(lang, CodeListsType.GOV_PUBLICATION).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setConferencePublicationCodes(storageService.getCodesList(lang, CodeListsType.CONF_PUBLICATION).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setBookFestschrifts(storageService.getCodesList(lang, CodeListsType.BOOK_FESTSCHRIFT).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setBookIndexAvailabilityCodes(storageService.getCodesList(lang, CodeListsType.BOOK_INDEX).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setBookLiteraryFormTypeCodes(storageService.getCodesList(lang, CodeListsType.BOOK_LITERARY_FORM).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setBookBiographyCodes(storageService.getCodesList(lang, CodeListsType.BOOK_BIOGRAPHY).stream().map(toPairItem).collect(toList()));
  }

  /**
   * Inject codes group for Leader tag.
   *
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   * @param storageService       the storage service.
   * @param lang                 the lang associated with the current request.
   */
  private void injectLeaderCodes(final FixedFieldCodesGroup fixedFieldCodesGroup, final StorageService storageService, final String lang) {
    fixedFieldCodesGroup.setRecordStatusTypes(storageService.getRecordStatusTypes(lang).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setRecordTypes(storageService.getRecordTypes(lang).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setBibliographicLevels(storageService.getBibliographicLevels(lang).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setControlTypes(storageService.getControlTypes(lang).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setCharacterEncodingSchemas(storageService.getCharacterEncodingSchemas(lang).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setEncodingLevels(storageService.getEncodingLevels(lang).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setDescriptiveCatalogForms(storageService.getDescriptiveCatalogForms(lang).stream().map(toPairItem).collect(toList()));
    fixedFieldCodesGroup.setLinkedRecordCodes(storageService.getMultipartResourceLevels(lang).stream().map(toPairItem).collect(toList()));
  }
}
