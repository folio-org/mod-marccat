package org.folio.marccat.resources;

import org.folio.marccat.ModMarccat;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.config.Global;
import org.folio.marccat.config.log.MessageCatalog;
import org.folio.marccat.integration.StorageService;
import org.folio.marccat.resources.domain.FieldTemplate;
import org.folio.marccat.resources.domain.FixedFieldCodesGroup;
import org.folio.marccat.resources.domain.FixedFieldElement;
import org.folio.marccat.resources.domain.Pair;
import org.folio.marccat.enumaration.CodeListsType;
import org.folio.marccat.shared.CatalogingInformation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class FixedFieldCodesGroupAPI extends BaseResource implements  CatalogingInformation{

  /**
   * Adapter that converts existing stringValue object in nature of content code Okapi resource.
   */
  private Function<Avp<String>, Pair> toPairItem = source -> {
    final Pair pairItem = new Pair();
    pairItem.setCode(source.getValue());
    pairItem.setDescription(source.getLabel());
    return pairItem;
  };


  @GetMapping("/fixed-fields-code-groups")
  public FixedFieldCodesGroup getFixedFieldCodesGroups(
    @RequestParam(required = false) final String leader,
    @RequestParam final String code,
    @RequestParam final int headerTypeCode,
    @RequestParam final String lang,
    @RequestParam(required = false) final String valueField,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final FixedFieldCodesGroup fixedFieldCodesGroup = new FixedFieldCodesGroup();

      return ofNullable(code)
        .map(tag -> {
          /**
           * find all values
           */
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
          /**
           * inject default values
           */
          HashMap<String, String> parameter = new HashMap<>();
          parameter.put("leader", leader);
          parameter.put("code", code);
          parameter.put("valueField", valueField);
          injectDefaultValues(fixedFieldCodesGroup, storageService, parameter, headerTypeCode, lang, configuration);
          return fixedFieldCodesGroup;
        }).orElse(null);
    }, tenant, configurator);
  }

  private void injectDefaultValues(FixedFieldCodesGroup fixedFieldCodesGroup, StorageService storageService, Map<String, String> parameter, int headerType, String lang, Map<String, String> configuration) {
    FieldTemplate fieldTemplate = ofNullable(CatalogingInformation.getFixedField(storageService, headerType, parameter.get("code"), parameter.get("leader"), parameter.get("valueField"), lang, configuration))
      .map(fixedField -> {
        final FieldTemplate fieldT = new FieldTemplate();
        fieldT.setFixedField(fixedField);
        return fieldT;
      }).orElseGet(() -> {
      logger.error(MessageCatalog._00016_FIELD_PARAMETER_INVALID, Global.CONTROL_FIELD_CATEGORY_CODE, parameter.get("code"));
      return new FieldTemplate();
    });
    fixedFieldCodesGroup.getResults().keySet()
      .forEach(key -> {
        String currentValue = (String) fieldTemplate.getFixedField().getAttributes().get(key);
        if (currentValue != null) {
          fixedFieldCodesGroup.getResults().get(key).setDafaultValue(currentValue.trim());
        }
        else {
          fixedFieldCodesGroup.getResults().get(key).setDafaultValue("");
        }
      });

  }

  private void injectPhysicalDescriptionCodes(final FixedFieldCodesGroup fixedFieldCodesGroup, final StorageService storageService, final String lang, final int headerTypeCode) {
    String categoryOfMaterial = Global.PHYSICAL_TYPES_MAP.get(headerTypeCode);
    if (categoryOfMaterial != null) {
        fixedFieldCodesGroup.addResults(new FixedFieldElement("categoryOfMaterial", storageService.getCodesList(lang, CodeListsType.CATEGORY_MATERIAL).stream().map(toPairItem).collect(toList())));
        switch (categoryOfMaterial) {
          case Global.ELECTRONIC_RESOURCE :
            setPhysicalInfoCFcodes(lang, storageService, fixedFieldCodesGroup);
            break;
          case Global.GLOBE :
            setPhysicalInfoGLBcodes(lang, storageService, fixedFieldCodesGroup);
            break;
          case Global.MAP_CODE :
            setPhysicalInfoMAPcodes(lang, storageService, fixedFieldCodesGroup);
            break;
          case Global.TACTILE_MATERIAL :
            setPhysicalInfoTCTcodes(lang, storageService, fixedFieldCodesGroup);
            break;
          case Global.PROJECTED_GRAPHIC :
            setPhysicalInfoPGcodes(lang, storageService, fixedFieldCodesGroup);
            break;
          case Global.MICROFORM :
            setPhysicalInfoMICcodes(lang, storageService, fixedFieldCodesGroup);
            break;
          case Global.NON_PROJECTED_GRAPHIC :
            setPhysicalInfoNPGcodes(lang, storageService, fixedFieldCodesGroup);
            break;
          case Global.MOTION_PICTURE :
            setPhysicalInfoMPcodes(lang, storageService, fixedFieldCodesGroup);
            break;
          case Global.KIT_CODE :
            setPhysicalInfoKITcodes(lang, storageService, fixedFieldCodesGroup);
            break;
          case Global.NOTATED_MUSIC :
            setPhysicalInfoNMcodes(lang, storageService, fixedFieldCodesGroup);
            break;
          case Global.REMOTE_SENSING_IMAGE :
            setPhysicalInfoRSIcodes(lang, storageService, fixedFieldCodesGroup);
            break;
          case Global.SOUND_RECORDING :
            setPhysicalInfoSNDcodes(lang, storageService, fixedFieldCodesGroup);
            break;
          case Global.TEXT_CODE :
            setPhysicalInfoTXTcodes(lang, storageService, fixedFieldCodesGroup);
            break;
          case Global.UNSPECIFIED :
            setPhysicalInfoUNScodes(lang, storageService, fixedFieldCodesGroup);
            break;
          case Global.VIDEO_RECORDING :
            setPhysicalInfoVRcodes(lang, storageService, fixedFieldCodesGroup);
            break;
          default :
        }

      }
      else {
        logger.error(MessageCatalog._00019_HEADER_TYPE_ID_WRONG, Global.PHYSICAL_DESCRIPTION_TAG_CODE);
    }
  }

  /**
   * Sets values Video Recording type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoVRcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, CodeListsType.VR_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.COLOR_CODE, storageService.getCodesList(lang, CodeListsType.VR_COLOR).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("videoRecordingFormatCodes", storageService.getCodesList(lang, CodeListsType.VR_FORMAT).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.SOUND_ON_MEDIUM_OR_SEPARATE_CODE, storageService.getCodesList(lang, CodeListsType.SOUND_MEDIUM_OR_SEP).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.MEDIUM_FOR_SOUND_CODE, storageService.getCodesList(lang, CodeListsType.MEDIUM_FOR_SOUND).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.DIMENSION_CODE, storageService.getCodesList(lang, CodeListsType.VR_DIMENSIONS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.CONFIGURATION_CODE, storageService.getCodesList(lang, CodeListsType.VR_CONF_PLAYBACK).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Unspecified type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoUNScodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, CodeListsType.UNS_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Text type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoTXTcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, CodeListsType.TXT_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Sound Recording type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoSNDcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, CodeListsType.SND_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("speedCode", storageService.getCodesList(lang, CodeListsType.SND_SPEED).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("configurationCode", storageService.getCodesList(lang, CodeListsType.SND_CONF_PLAYBACK).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("grooveWidthCode", storageService.getCodesList(lang, CodeListsType.SND_GROOVE_WIDTH).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.DIMENSION_CODE, storageService.getCodesList(lang, CodeListsType.SND_DIMENSIONS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("tapeWidthCode", storageService.getCodesList(lang, CodeListsType.SND_TAPE_WIDTH).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("tapeConfigurationCode", storageService.getCodesList(lang, CodeListsType.SND_TAPE_CONF).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("discTypeCode", storageService.getCodesList(lang, CodeListsType.SND_DISC_TYPE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("sndMaterialTypeCode", storageService.getCodesList(lang, CodeListsType.SND_MATERIAL_TYPE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("cuttingTypeCode", storageService.getCodesList(lang, CodeListsType.SND_CUTTING).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("specialPlaybackCharacteristicsCode", storageService.getCodesList(lang, CodeListsType.SND_SPEC_PLAYBACK).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("storageTechniqueCode", storageService.getCodesList(lang, CodeListsType.SND_STORAGE_TECNIQUE).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Remote Sensing Image type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoRSIcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, CodeListsType.RSI_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("altitudeOfSensorCode", storageService.getCodesList(lang, CodeListsType.RSI_ALTITUDE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("attitudeOfSensorCode", storageService.getCodesList(lang, CodeListsType.RSI_ATTITUDE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("cloudCoverCode", storageService.getCodesList(lang, CodeListsType.RSI_CLOUD_COVER).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("platformConstructionTypeCode", storageService.getCodesList(lang, CodeListsType.RSI_PLAT_CONSTRUCTION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("platformUseCode", storageService.getCodesList(lang, CodeListsType.RSI_PLAT_USE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("sensorTypeCode", storageService.getCodesList(lang, CodeListsType.RSI_SENSOR_TYPE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("remoteSensingDataTypeCodes", storageService.getCodesList(lang, CodeListsType.RSI_DATA_TYPE).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Notated Music type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoNMcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, CodeListsType.NMU_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Kit type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoKITcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, CodeListsType.KIT_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Motion Picture type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoMPcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {

    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, CodeListsType.MP_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.COLOR_CODE, storageService.getCodesList(lang, CodeListsType.MP_COLOR).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("presentationFormatCode", storageService.getCodesList(lang, CodeListsType.MP_PRESENT_FORMAT).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("soundOnMediumOrSeparateCode", storageService.getCodesList(lang, CodeListsType.SOUND_MEDIUM_OR_SEP).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("mediumForSoundCode", storageService.getCodesList(lang, CodeListsType.MEDIUM_FOR_SOUND).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.DIMENSION_CODE, storageService.getCodesList(lang, CodeListsType.MP_DIMENSIONS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("configurationCode", storageService.getCodesList(lang, CodeListsType.MP_CONF_PLAYBACK).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("productionElementsCode", storageService.getCodesList(lang, CodeListsType.MP_PROD_ELEM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.POLARITY_CODE, storageService.getCodesList(lang, CodeListsType.MP_POLARITY).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("generationCode", storageService.getCodesList(lang, CodeListsType.MP_GENERATION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("baseOfFilmCode", storageService.getCodesList(lang, CodeListsType.MP_BASE_FILM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("refinedCategoriesOfColorCode", storageService.getCodesList(lang, CodeListsType.MP_REFINE_CAT_COLOR).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("kindOfColorStockCode", storageService.getCodesList(lang, CodeListsType.MP_KIND_COLORS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("deteriorationStageCode", storageService.getCodesList(lang, CodeListsType.MP_DETERIORATION_STAGE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("completenessCode", storageService.getCodesList(lang, CodeListsType.MP_COMPLETENESS).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Non Project Graphic type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoNPGcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, CodeListsType.NPG_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.COLOR_CODE, storageService.getCodesList(lang, CodeListsType.NPG_COLOR).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("primarySupportMaterialCode", storageService.getCodesList(lang, CodeListsType.NPG_PRIMARY_SUPPORT).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("secondarySupportMaterialCode", storageService.getCodesList(lang, CodeListsType.NPG_SECONDARY_SUPPORT).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Microform type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoMICcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, CodeListsType.MIC_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.POLARITY_CODE, storageService.getCodesList(lang, CodeListsType.MIC_POLARITY).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.DIMENSION_CODE, storageService.getCodesList(lang, CodeListsType.MIC_DIMENSIONS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("reductionRatioRangeCode", storageService.getCodesList(lang, CodeListsType.MIC_REDUCT_RATIO_RANGE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.COLOR_CODE, storageService.getCodesList(lang, CodeListsType.MIC_COLOR).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("emulsionOnFilmCode", storageService.getCodesList(lang, CodeListsType.MIC_EMUL_FILM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("generationCode", storageService.getCodesList(lang, CodeListsType.MIC_GENERATION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("baseOfFilmCode", storageService.getCodesList(lang, CodeListsType.MIC_BASE_FILM).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Project Graphic type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoPGcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, CodeListsType.PG_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.COLOR_CODE, storageService.getCodesList(lang, CodeListsType.PG_COLOR).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("baseOfEmulsionCode", storageService.getCodesList(lang, CodeListsType.PG_EMUL_BASE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("soundOnMediumOrSeparateCode", storageService.getCodesList(lang, CodeListsType.SOUND_MEDIUM_OR_SEP).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("mediumForSoundCodes", storageService.getCodesList(lang, CodeListsType.MEDIUM_FOR_SOUND).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("dimensionCodes", storageService.getCodesList(lang, CodeListsType.PG_DIMENSIONS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("secondarySupportMaterialCode", storageService.getCodesList(lang, CodeListsType.PG_SECONDARY_SUPPORT).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Map type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoMAPcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, CodeListsType.MAP_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.COLOR_CODE, storageService.getCodesList(lang, CodeListsType.MAP_COLOR).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("physicalMediumCode", storageService.getCodesList(lang, CodeListsType.MAP_PHYSICAL_MEDIUM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("typeOfReproductionCode", storageService.getCodesList(lang, CodeListsType.MAP_TYPE_OF_REPRODUCTION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("productionDetailsCode", storageService.getCodesList(lang, CodeListsType.MAP_PRODUCTION_DETAILS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.POLARITY_CODE, storageService.getCodesList(lang, CodeListsType.MAP_POLARITY).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Globe type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoGLBcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.SPECIFIC_MATERIAL_DESIGNAION_ON_CODE, storageService.getCodesList(lang, CodeListsType.GLB_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("colorCode", storageService.getCodesList(lang, CodeListsType.GLB_COLOR).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("physicalMediumCode", storageService.getCodesList(lang, CodeListsType.GLB_PHYSICAL_MEDIUM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("typeOfReproductionCode", storageService.getCodesList(lang, CodeListsType.GLB_TYPE_OF_REPRODUCTION).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Tactile Material type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoTCTcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement("specificMaterialDesignationCode", storageService.getCodesList(lang, CodeListsType.TCT_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("classOfBrailleWritingCodes", storageService.getCodesList(lang, CodeListsType.TCT_CLASS_BRAILLE_WRITING).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("levelOfContractionCode", storageService.getCodesList(lang, CodeListsType.TCT_CONTRACTION_LVL).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("brailleMusicFormatCodes", storageService.getCodesList(lang, CodeListsType.TCT_BRAILLE_MUSIC_FORMAT).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("specificPhysicalCharacteristicsCode", storageService.getCodesList(lang, CodeListsType.TCT_SPECIAL_PHYSICAL_CHAR).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Computer File type for 007 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setPhysicalInfoCFcodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement("specificMaterialDesignationCode", storageService.getCodesList(lang, CodeListsType.CF_SPEC_DESIGN).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("colorCode", storageService.getCodesList(lang, CodeListsType.CF_COLOR).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("dimensionCodes", storageService.getCodesList(lang, CodeListsType.CF_DIMENSIONS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("includesSoundCodes", storageService.getCodesList(lang, CodeListsType.SOUND_MEDIUM_OR_SEP).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("fileFormatsCode", storageService.getCodesList(lang, CodeListsType.CF_FILE_FORMAT).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("qualityAssuranceTargetCode", storageService.getCodesList(lang, CodeListsType.CF_QUALITY_ASS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("antecedentSourceCode", storageService.getCodesList(lang, CodeListsType.CF_ANTECEDENT_SRC).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("levelOfCompressionCode", storageService.getCodesList(lang, CodeListsType.CF_COMPRESSION_LVL).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("reformattingQualityCode", storageService.getCodesList(lang, CodeListsType.CF_REFORMATTING_QUALITY).stream().map(toPairItem).collect(toList())));
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
    Map<String, Object> mapRecordTypeMaterial = storageService.getMaterialTypeInfosByHeaderCode(headerTypeCode, tag);
    if (mapRecordTypeMaterial != null) {
        String material = (String) mapRecordTypeMaterial.get(Global.FORM_OF_MATERIAL_LABEL);
        switch (material){
          case Global.BOOK_TYPE:
            setBookMaterialCodes(lang, storageService, fixedFieldCodesGroup);
            break;
          case Global.MUSIC_TYPE:
            setMusicMaterialCodes(lang, storageService, fixedFieldCodesGroup);
            break;
          case Global.SERIAL_TYPE:
            setSerialMaterialCodes(lang, storageService, fixedFieldCodesGroup);
            break;
          case Global.MIXED_TYPE:
            fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.FORM_OF_ITEM_CODE, storageService.getCodesList(lang, CodeListsType.FORM_OF_ITEM).stream().map(toPairItem).collect(toList())));
            break;
          case Global.MAP_TYPE:
            setMapMaterialCodes(lang, storageService, fixedFieldCodesGroup);
            break;
          case Global.VISUAL_TYPE:
            setVisualMaterialCodes(lang, storageService, fixedFieldCodesGroup);
            break;
          case Global.COMPUTER_TYPE:
            setComputerMaterialCodes(lang, storageService, fixedFieldCodesGroup);
            break;
           default:

      }

        if (tag.equals(Global.MATERIAL_TAG_CODE)) {
          fixedFieldCodesGroup.addResults(new FixedFieldElement("dateTypes", storageService.getCodesList(lang, CodeListsType.DATE_TYPE).stream().map(toPairItem).collect(toList())));
          fixedFieldCodesGroup.addResults(new FixedFieldElement("modifiedRecordTypes", storageService.getCodesList(lang, CodeListsType.MODIFIED_RECORD_TYPE).stream().map(toPairItem).collect(toList())));
          fixedFieldCodesGroup.addResults(new FixedFieldElement("catalogSources", storageService.getCodesList(lang, CodeListsType.CATALOGUING_SOURCE).stream().map(toPairItem).collect(toList())));

        }
    }
      else {
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
  private void setComputerMaterialCodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement("computerTargetAudienceCodes", storageService.getCodesList(lang, CodeListsType.COMPUTER_TARGET_AUDIENCE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("computerFormOfItemCodes", storageService.getCodesList(lang, CodeListsType.COMPUTER_FORM_OF_ITEM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("computerTypeMaterialCodes", storageService.getCodesList(lang, CodeListsType.COMPUTER_TYPE_MATERIAL).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.GOVERNMENT_PUBLICATION_CODE, storageService.getCodesList(lang, CodeListsType.GOV_PUBLICATION).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Visual type for 008/006 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setVisualMaterialCodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement("visualTargetAudienceCodes", storageService.getCodesList(lang, CodeListsType.VSL_TARGET_AUDIENCE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.GOVERNMENT_PUBLICATION_CODE, storageService.getCodesList(lang, CodeListsType.GOV_PUBLICATION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.FORM_OF_ITEM_CODE, storageService.getCodesList(lang, CodeListsType.FORM_OF_ITEM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("visualTypeMaterialCodes", storageService.getCodesList(lang, CodeListsType.VSL_TYPE_MATERIAL).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("visualTechnique", storageService.getCodesList(lang, CodeListsType.VSL_TECHNIQUE).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Map type for 008/006 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setMapMaterialCodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement("mapReliefCodes", storageService.getCodesList(lang, CodeListsType.MAP_RELIEF).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("mapProjectionCodes", storageService.getCodesList(lang, CodeListsType.MAP_PROJECTION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("mapTypeCartographicMaterialCodes", storageService.getCodesList(lang, CodeListsType.MAP_TYPE_MATERIAL).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.GOVERNMENT_PUBLICATION_CODE, storageService.getCodesList(lang, CodeListsType.GOV_PUBLICATION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.FORM_OF_ITEM_CODE, storageService.getCodesList(lang, CodeListsType.FORM_OF_ITEM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("mapIndexCodes", storageService.getCodesList(lang, CodeListsType.MAP_INDEX).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("mapSpecialFormatCharacteristicCodes", storageService.getCodesList(lang, CodeListsType.MAP_SPECIAL_FORMAT_CHARACTERISTIC).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Sets values Music type for 008/006 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setMusicMaterialCodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {

    fixedFieldCodesGroup.addResults(new FixedFieldElement("musicFormats", storageService.getCodesList(lang, CodeListsType.MSC_FORMAT).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("musicFormOfCompositions", storageService.getCodesList(lang, CodeListsType.MSC_PARTS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("musicParts", storageService.getCodesList(lang, CodeListsType.MSC_PARTS).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("targetAudienceCode", storageService.getCodesList(lang, CodeListsType.TARGET_AUDIENCE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.FORM_OF_ITEM_CODE, storageService.getCodesList(lang, CodeListsType.FORM_OF_ITEM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("musicTextualMaterialCodes", storageService.getCodesList(lang, CodeListsType.MSC_TEXTUAL_MAT_CODE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("musicLiteraryTextCodes", storageService.getCodesList(lang, CodeListsType.MSC_LITERARY_TEXT).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("musicTranspositionArrangementCode", storageService.getCodesList(lang, CodeListsType.MSC_TRANSPOSITION_CODE).stream().map(toPairItem).collect(toList())));

  }

  /**
   * Sets values Continuing resource type for 008/006 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setSerialMaterialCodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {

    final List<Pair> natureOfContents = storageService.getCodesList(lang, CodeListsType.NATURE_OF_CONTENT).stream().map(toPairItem).collect(toList());

    fixedFieldCodesGroup.addResults(new FixedFieldElement("serialFrequencyCode", storageService.getCodesList(lang, CodeListsType.SRL_FREQUENCY).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("serialRegularityCode", storageService.getCodesList(lang, CodeListsType.SRL_REGULARITY).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("serialTypeOfContinuingResourceCodes", storageService.getCodesList(lang, CodeListsType.SRL_REGULARITY).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("serialFormOriginalCodes", storageService.getCodesList(lang, CodeListsType.SRL_FORM_ORGNL_ITEM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.FORM_OF_ITEM_CODE, storageService.getCodesList(lang, CodeListsType.FORM_OF_ITEM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("natureOfContent1", natureOfContents));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("natureOfContent2", natureOfContents));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("natureOfContent3", natureOfContents));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("serialNatureOfWorkCodes", natureOfContents));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.GOVERNMENT_PUBLICATION_CODE, storageService.getCodesList(lang, CodeListsType.GOV_PUBLICATION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("conferencePublicationCode", storageService.getCodesList(lang, CodeListsType.CONF_PUBLICATION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("serialOriginAlphabetCodes", storageService.getCodesList(lang, CodeListsType.SRL_ORIGIN_ALPHABET).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("serialEntryConvCodes", storageService.getCodesList(lang, CodeListsType.SRL_ENTRY_CONVENTION).stream().map(toPairItem).collect(toList())));

  }

  /**
   * Sets values Book type for 008/006 fixed field
   *
   * @param lang                 the lang associated with the current request.
   * @param storageService       the storage service.
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   */
  private void setBookMaterialCodes(final String lang, final StorageService storageService, final FixedFieldCodesGroup fixedFieldCodesGroup) {
    final List<Pair> bookIllustrations = storageService.getCodesList(lang, CodeListsType.BOOK_ILLUSTRATION).stream().map(toPairItem).collect(toList());
    final List<Pair> natureOfContents = storageService.getCodesList(lang, CodeListsType.NATURE_OF_CONTENT).stream().map(toPairItem).collect(toList());

    fixedFieldCodesGroup.addResults(new FixedFieldElement("bookIllustrationCode1", bookIllustrations));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("bookIllustrationCode2", bookIllustrations));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("bookIllustrationCode3", bookIllustrations));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("bookIllustrationCode4", bookIllustrations));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("targetAudienceCode", storageService.getCodesList(lang, CodeListsType.TARGET_AUDIENCE).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.FORM_OF_ITEM_CODE, storageService.getCodesList(lang, CodeListsType.FORM_OF_ITEM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("natureOfContent1", natureOfContents));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("natureOfContent2", natureOfContents));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("natureOfContent3", natureOfContents));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("natureOfContent4", natureOfContents));
    fixedFieldCodesGroup.addResults(new FixedFieldElement(Global.GOVERNMENT_PUBLICATION_CODE, storageService.getCodesList(lang, CodeListsType.GOV_PUBLICATION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("conferencePublicationCode", storageService.getCodesList(lang, CodeListsType.CONF_PUBLICATION).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("bookFestschrift", storageService.getCodesList(lang, CodeListsType.BOOK_FESTSCHRIFT).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("bookIndexAvailabilityCode", storageService.getCodesList(lang, CodeListsType.BOOK_INDEX).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("bookLiteraryFormTypeCode", storageService.getCodesList(lang, CodeListsType.BOOK_LITERARY_FORM).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("bookBiographyCode", storageService.getCodesList(lang, CodeListsType.BOOK_BIOGRAPHY).stream().map(toPairItem).collect(toList())));
  }

  /**
   * Inject codes group for Leader tag.
   *
   * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
   * @param storageService       the storage service.
   * @param lang                 the lang associated with the current request.
   */
  private void injectLeaderCodes(final FixedFieldCodesGroup fixedFieldCodesGroup, final StorageService storageService, final String lang) {
    fixedFieldCodesGroup.addResults(new FixedFieldElement("itemRecordStatusCode", storageService.getRecordStatusTypes(lang).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("itemRecordTypeCode",storageService.getRecordTypes(lang).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("itemBibliographicLevelCode", storageService.getBibliographicLevels(lang).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("itemControlTypeCode", storageService.getControlTypes(lang).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("characterCodingSchemeCode", storageService.getCharacterEncodingSchemas(lang).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("encodingLevel", storageService.getEncodingLevels(lang).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("descriptiveCataloguingCode", storageService.getDescriptiveCatalogForms(lang).stream().map(toPairItem).collect(toList())));
    fixedFieldCodesGroup.addResults(new FixedFieldElement("linkedRecordCode", storageService.getMultipartResourceLevels(lang).stream().map(toPairItem).collect(toList())));
  }
}
