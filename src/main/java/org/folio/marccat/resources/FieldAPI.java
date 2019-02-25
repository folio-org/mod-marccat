package org.folio.marccat.resources;

import org.folio.marccat.ModMarccat;
import org.folio.marccat.config.Global;
import org.folio.marccat.config.log.MessageCatalog;
import org.folio.marccat.resources.domain.FieldCollection;
import org.springframework.web.bind.annotation.*;

import static java.util.Arrays.asList;
import static org.folio.marccat.integration.MarccatHelper.doGet;
import static org.folio.marccat.resources.shared.RecordUtils.*;

/**
 * FieldsAPI RestFul service to manage fields (tag).
 *
 * @author nbianchini
 * @author agazzarini
 * @since 1.0
 */
@RestController
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
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
        logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
        return null;
      }
    }, tenant, configurator, "bibliographic", "material");
  }


}
