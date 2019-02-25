package org.folio.marccat.resources;

import org.folio.marccat.ModMarccat;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.config.Global;
import org.folio.marccat.config.log.MessageCatalog;
import org.folio.marccat.enumaration.CodeListsType;
import org.folio.marccat.integration.StorageService;
import org.folio.marccat.resources.domain.FieldTemplate;
import org.folio.marccat.resources.domain.FixedFieldCodesGroup;
import org.folio.marccat.resources.domain.FixedFieldElement;
import org.folio.marccat.resources.domain.Pair;
import org.folio.marccat.shared.CatalogingInformation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
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
    }, tenant, configurator, "bibliographic", "material");
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
        } else {
          fixedFieldCodesGroup.getResults().get(key).setDafaultValue("");
        }
      });

  }

  private void injectPhysicalDescriptionCodes(final FixedFieldCodesGroup fixedFieldCodesGroup, final StorageService storageService, final String lang, final int headerTypeCode) {
    String categoryOfMaterial = Global.PHYSICAL_TYPES_MAP.get(headerTypeCode);
    if (categoryOfMaterial != null) {
      fixedFieldCodesGroup.addResults(new FixedFieldElement("categoryOfMaterial", storageService.getCodesList(lang, CodeListsType.CATEGORY_MATERIAL)
        .stream()
        .map(toPairItem)
        .collect(toList())));
      switch (categoryOfMaterial) {
        case Global.ELECTRONIC_RESOURCE:
          setPhysicalInfoCFcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case Global.GLOBE:
          setPhysicalInfoGLBcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case Global.MAP_CODE:
          setPhysicalInfoMAPcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case Global.TACTILE_MATERIAL:
          setPhysicalInfoTCTcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case Global.PROJECTED_GRAPHIC:
          setPhysicalInfoPGcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case Global.MICROFORM:
          setPhysicalInfoMICcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case Global.NON_PROJECTED_GRAPHIC:
          setPhysicalInfoNPGcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case Global.MOTION_PICTURE:
          setPhysicalInfoMPcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case Global.KIT_CODE:
          setPhysicalInfoKITcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case Global.NOTATED_MUSIC:
          setPhysicalInfoNMcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case Global.REMOTE_SENSING_IMAGE:
          setPhysicalInfoRSIcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case Global.SOUND_RECORDING:
          setPhysicalInfoSNDcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case Global.TEXT_CODE:
          setPhysicalInfoTXTcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case Global.UNSPECIFIED:
          setPhysicalInfoUNScodes(lang, storageService, fixedFieldCodesGroup);
          break;
        case Global.VIDEO_RECORDING:
          setPhysicalInfoVRcodes(lang, storageService, fixedFieldCodesGroup);
          break;
        default:
      }

    } else {
      logger.error(MessageCatalog._00019_HEADER_TYPE_ID_WRONG, Global.PHYSICAL_DESCRIPTION_TAG_CODE);
    }
  }
}
