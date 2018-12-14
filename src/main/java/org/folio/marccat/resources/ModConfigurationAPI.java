package org.folio.marccat.resources;

import org.folio.marccat.ModMarccat;
import org.folio.marccat.config.Global;
import org.springframework.web.bind.annotation.*;

/**
 * @author cchiama
 * @since 1.0
 */

@RestController
@RequestMapping(value = ModMarccat.BASE_URI)
public class ModConfigurationAPI extends BaseResource {

  @GetMapping("/entries")
  public void getConfigurationEntries(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
  }

  @PostMapping("/entries")
  public void saveConfigurationEntries(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
  }

  @PutMapping("/entries")
  public void editConfigurationEntries(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
  }

  @DeleteMapping("/entries")
  public void deleteConfigurationEntries(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
  }
}
