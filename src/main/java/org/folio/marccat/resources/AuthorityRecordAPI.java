package org.folio.marccat.resources;

import static org.folio.marccat.config.constants.Global.BASE_URI;
import static org.folio.marccat.integration.MarccatHelper.doGet;
import static org.folio.marccat.integration.MarccatHelper.doPost;
import static org.folio.marccat.resources.shared.ConversionFieldUtils.getAuthorityDisplayValueOfMaterial;
import static org.folio.marccat.resources.shared.RecordUtils.setCategory;
import static org.folio.marccat.util.F.isNotNullOrEmpty;

import org.folio.marccat.business.common.View;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.integration.AuthorityStorageService;
import org.folio.marccat.resources.domain.AuthorityRecord;
import org.folio.marccat.resources.domain.FixedField;
import org.folio.marccat.resources.domain.RecordTemplate;
import org.folio.marccat.resources.shared.FixedFieldUtils;
import org.folio.marccat.shared.GeneralInformation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author elena
 *
 */
@RestController
@RequestMapping(value = BASE_URI, produces = "application/json")
public class AuthorityRecordAPI extends RecordAPI {

  @PostMapping("/authority-record")
  public ResponseEntity<Object> save(@RequestBody final AuthorityRecord record,
    // ContainerRecordTemplate container,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_AUTHORITY_VIEW_AS_STRING) final int view,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {

    return doPost((storageService, configuration) -> {
      try {

        AuthorityStorageService authorityStorageService = new AuthorityStorageService();

        authorityStorageService.setStorageService(storageService);

        record.getFields().forEach(field -> setCategory(field, storageService));

        final GeneralInformation gi = new GeneralInformation();
        gi.setDefaultValues(configuration);

        record.getFields().stream().filter(FixedFieldUtils::isFixedField).filter(field -> field.getCode().equalsIgnoreCase(Global.MATERIAL_TAG_CODE))
            .forEach(field -> {
            });

        authorityStorageService.saveAuthorityRecord(record, view, lang, configuration);
        return new ResponseEntity<>("1", HttpStatus.CREATED);

      } catch (final Exception exception) {
        logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
        return new ResponseEntity<>("0", HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }, tenant, okapiUrl, configurator, () -> isNotNullOrEmpty(record.getId().toString()));

  }

  @PostMapping("/authority-record/fixed-field-display-value")
  public ResponseEntity<FixedField> getFixedFieldWithDisplayValue(@RequestBody final FixedField fixed,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doPost((storageService, configuration) 
        -> getAuthorityDisplayValueOfMaterial(fixed), 
        tenant, okapiUrl, configurator, () -> (isNotNullOrEmpty(fixed.getCode())));
  }

  @GetMapping("/authority-record/from-template/{idTemplate}")
  public AuthorityRecord getEmptyRecord(@PathVariable final Integer idTemplate,
    @RequestParam final String lang,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_AUTHORITY_VIEW_AS_STRING) final int view,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doGet((storageService, configuration) -> {
      AuthorityStorageService authorityStorageService = new AuthorityStorageService();

      authorityStorageService.setStorageService(storageService);

      final RecordTemplate template = authorityStorageService.getAuthorityRecordRecordTemplatesById(idTemplate);
      AuthorityRecord authorityRecord = new AuthorityRecord();
      getEmptyRecord(false, authorityRecord, template, lang, configuration, storageService);
      return authorityRecord;
    }, tenant, okapiUrl, configurator);
  }

}
