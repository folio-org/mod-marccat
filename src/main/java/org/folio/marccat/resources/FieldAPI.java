package org.folio.marccat.resources;

import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.resources.domain.FieldCollection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import static java.util.Arrays.asList;
import static org.folio.marccat.integration.MarccatHelper.doGet;
import static org.folio.marccat.resources.shared.RecordUtils.*;


public class FieldAPI extends BaseResource {

  /**
   * Gets the mandatory fields to create a new template / record.
   */
  @GetMapping("/bibliographic/fields/mandatory")
  public FieldCollection getMandatoryFields(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      try {
        final FieldCollection container = new FieldCollection();
        container.setFields(
          asList(createRequiredLeaderField(storageService, lang),
            createControlNumberField(storageService, lang),
            createRequiredMaterialDescriptionField(configuration, storageService, lang),
            createCatalogingSourceField(configuration, storageService, lang)));
        return container;
      } catch (final Exception exception) {
        logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
        return null;
      }
    }, tenant, configurator, "bibliographic", "material");
  }


}
