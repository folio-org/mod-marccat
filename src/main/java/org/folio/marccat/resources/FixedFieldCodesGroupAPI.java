package org.folio.marccat.resources;

import org.folio.marccat.ModMarccat;
import org.folio.marccat.enumaration.CodeListsType;
import org.folio.marccat.integration.StorageService;
import org.folio.marccat.resources.domain.FieldTemplate;
import org.folio.marccat.resources.domain.FixedFieldCodesGroup;
import org.folio.marccat.resources.domain.FixedFieldElement;
import org.folio.marccat.shared.CatalogingInformation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.folio.marccat.config.log.Global.*;
import static org.folio.marccat.config.log.Global.OTHER_MATERIAL_TAG_CODE;
import static org.folio.marccat.config.log.Global.PHYSICAL_DESCRIPTION_TAG_CODE;
import static org.folio.marccat.config.log.MessageCatalog.*;
import static org.folio.marccat.integration.MarccatHelper.doGet;
import static org.folio.marccat.resources.shared.FixeFieldUtils.*;
import static org.folio.marccat.resources.shared.MappingUtils.toPairItem;

/**
 * Fixed-Field Codes Groups RESTful APIs.
 *
 * @author cchiama
 * @author nbianchini
 * @since 1.0
 */
@RestController
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class FixedFieldCodesGroupAPI extends BaseResource implements CatalogingInformation {

  @GetMapping("/fixed-fields-code-groups")
  public FixedFieldCodesGroup getFixedFieldCodesGroups(
    @RequestParam(required = false) final String leader,
    @RequestParam final String code,
    @RequestParam final int headerTypeCode,
    @RequestParam final String lang,
    @RequestParam(required = false) final String valueField,
    @RequestHeader(OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final FixedFieldCodesGroup fixedFieldCodesGroup = new FixedFieldCodesGroup();

      return ofNullable(code)
        .map(tag -> {
          /**
           * find all values
           */
          if (tag.equals(LEADER_TAG_NUMBER)) {
            injectLeaderCodes(fixedFieldCodesGroup, storageService, lang);
          } else if (!(!tag.equals(MATERIAL_TAG_CODE) && !tag.equals(OTHER_MATERIAL_TAG_CODE))) {
            injectMaterialCodes(fixedFieldCodesGroup, storageService, lang, headerTypeCode, tag);
          } else if (tag.equals(PHYSICAL_DESCRIPTION_TAG_CODE)) {
            injectPhysicalDescriptionCodes(fixedFieldCodesGroup, storageService, lang, headerTypeCode);
          } else {
            logger.error(String.format(_00017_CODES_GROUPS_NOT_AVAILABLE, code));
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
    }, tenant, configurator, "bibliographic", "material");
  }

  private void injectDefaultValues(FixedFieldCodesGroup fixedFieldCodesGroup, StorageService storageService, Map<String, String> parameter, int headerType, String lang, Map<String, String> configuration) {
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

  private void injectPhysicalDescriptionCodes(final FixedFieldCodesGroup fixedFieldCodesGroup, final StorageService storageService, final String lang, final int headerTypeCode) {
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
