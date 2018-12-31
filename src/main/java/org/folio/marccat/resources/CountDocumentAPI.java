package org.folio.marccat.resources;

import org.folio.marccat.ModMarccat;
import org.folio.marccat.config.Global;
import org.folio.marccat.config.log.MessageCatalog;
import org.folio.marccat.resources.domain.CountDocument;
import org.springframework.web.bind.annotation.*;

import static org.folio.marccat.integration.MarccatHelper.doGet;

/**
 * CountDocument RESTful APIs.
 *
 * @author carment
 * @since 1.0
 */
@RestController
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class CountDocumentAPI extends BaseResource {

  /**
   * Gets the count of the record.
   */
  @GetMapping("/document-count-by-id")
  public CountDocument getDocumentCountById(
    @RequestParam final int id,
    @RequestParam final int view,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      try {
        return storageService.getCountDocumentByAutNumber(id, view);
      } catch (final Exception exception) {
        logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
        return null;
      }
    }, tenant, configurator);
  }


}
