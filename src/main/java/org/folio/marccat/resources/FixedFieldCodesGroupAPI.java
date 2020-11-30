package org.folio.marccat.resources;

import static java.util.Optional.ofNullable;
import static org.folio.marccat.config.constants.Global.BASE_URI;
import static org.folio.marccat.config.constants.Global.LEADER_TAG_NUMBER;
import static org.folio.marccat.config.constants.Global.MATERIAL_TAG_CODE;
import static org.folio.marccat.config.constants.Global.OKAPI_TENANT_HEADER_NAME;
import static org.folio.marccat.config.constants.Global.OTHER_MATERIAL_TAG_CODE;
import static org.folio.marccat.config.constants.Global.PHYSICAL_DESCRIPTION_TAG_CODE;
import static org.folio.marccat.config.log.Message.MOD_MARCCAT_00017_CODES_GROUPS_NOT_AVAILABLE;
import static org.folio.marccat.integration.MarccatHelper.doGet;
import static org.folio.marccat.resources.shared.FixedFieldUtils.injectAuthorityLeaderCodes;
import static org.folio.marccat.resources.shared.FixedFieldUtils.injectAuthorityMaterialCodes;
import static org.folio.marccat.resources.shared.FixedFieldUtils.injectDefaultValues;
import static org.folio.marccat.resources.shared.FixedFieldUtils.injectLeaderCodes;
import static org.folio.marccat.resources.shared.FixedFieldUtils.injectMaterialCodes;
import static org.folio.marccat.resources.shared.FixedFieldUtils.injectPhysicalDescriptionCodes;

import java.util.HashMap;
import java.util.Map;

import org.folio.marccat.config.constants.Global;
import org.folio.marccat.integration.AuthorityStorageService;
import org.folio.marccat.resources.domain.FixedFieldCodesGroup;
import org.folio.marccat.resources.shared.RecordUtils;
import org.folio.marccat.shared.CatalogingInformation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = BASE_URI, produces = "application/json")
public class FixedFieldCodesGroupAPI extends BaseResource implements CatalogingInformation {

  @GetMapping("/fixed-fields-code-groups-by-leader")
  public FixedFieldCodesGroup getFixedFieldCodesGroupsByLeader(@RequestParam final String leader,
      @RequestParam(name = "code", defaultValue = Global.MATERIAL_TAG_CODE) final String code,
      @RequestParam final String lang,
      @RequestParam(required = false) final String valueField,
      @RequestHeader(OKAPI_TENANT_HEADER_NAME) final String tenant,
      @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doGet((storageService, configuration) -> {
      final FixedFieldCodesGroup fixedFieldCodesGroup = new FixedFieldCodesGroup();
      final Map<String, Object> map = RecordUtils.getMaterialTypeInfosByLeaderValues(leader.charAt(6), leader.charAt(7), code, storageService.getSession());
      injectMaterialCodes(fixedFieldCodesGroup, storageService, lang, (int) map.get(Global.HEADER_TYPE_LABEL), code);
      fixedFieldCodesGroup.setHeaderTypeCode((int) map.get(Global.HEADER_TYPE_LABEL));
      /**
       * inject default values
       */
      HashMap<String, String> parameter = new HashMap<>();
      parameter.put("leader", leader);
      parameter.put("code", code);
      parameter.put("valueField", valueField);
      injectDefaultValues(fixedFieldCodesGroup, storageService, parameter, fixedFieldCodesGroup.getHeaderTypeCode(), lang, configuration);
      return fixedFieldCodesGroup;
    }, tenant, okapiUrl, configurator);
  }

  @GetMapping("/fixed-fields-code-groups")
  public FixedFieldCodesGroup getFixedFieldCodesGroups(@RequestParam(required = false) final String leader,
      @RequestParam final String code,
      @RequestParam final int headerTypeCode,
      @RequestParam final String lang,
      @RequestParam(required = false) final String valueField,
      @RequestHeader(OKAPI_TENANT_HEADER_NAME) final String tenant,
      @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doGet((storageService, configuration) -> {
      final FixedFieldCodesGroup fixedFieldCodesGroup = new FixedFieldCodesGroup();

      return ofNullable(code).map(tag -> {
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
          logger.error(String.format(MOD_MARCCAT_00017_CODES_GROUPS_NOT_AVAILABLE, code));
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
    }, tenant, okapiUrl, configurator);
  }

  @GetMapping("/auth-fixed-fields-code-groups")
  public FixedFieldCodesGroup getAuthorityFixedFieldCodesGroups(@RequestParam(required = false) final String leader,
      @RequestParam final String code,
      @RequestParam final int headerTypeCode,
      @RequestParam final String lang,
      @RequestParam(required = false) final String valueField,
      @RequestHeader(OKAPI_TENANT_HEADER_NAME) final String tenant,
      @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doGet((storageService, configuration) -> {
      AuthorityStorageService authorityStorageService = new AuthorityStorageService();

      authorityStorageService.setStorageService(storageService);

      final FixedFieldCodesGroup fixedFieldCodesGroup = new FixedFieldCodesGroup();

      return ofNullable(code).map(tag -> {
        /**
         * find all values
         */
        if (tag.equals(LEADER_TAG_NUMBER)) {
          injectAuthorityLeaderCodes(fixedFieldCodesGroup, authorityStorageService, lang);
        } else if (tag.equals(MATERIAL_TAG_CODE)) {
          injectAuthorityMaterialCodes(fixedFieldCodesGroup, authorityStorageService, lang);
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
    }, tenant, okapiUrl, configurator);
  }
}
