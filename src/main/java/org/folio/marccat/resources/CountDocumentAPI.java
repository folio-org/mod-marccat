package org.folio.marccat.resources;

import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.resources.domain.CountDocument;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import static org.folio.marccat.integration.MarccatHelper.doGet;

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
        logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
        return null;
      }
    }, tenant, configurator);
  }


}
