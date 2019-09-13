package org.folio.marccat.resources;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.integration.TenantService;
import org.folio.marccat.resources.domain.TenantAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/_/tenant")
public class TenantAPI {

  private static final Log logger = new Log(TenantAPI.class);

  @Autowired
  private TenantService tenantService;

  @PostMapping
  public ResponseEntity<String> create(
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) String tenant,
    @RequestHeader(Global.OKAPI_TO_URL) String okapiUrl,
    @RequestBody TenantAttributes attributes
  ) throws SQLException, IOException {
    logger.info("URL OKAPI:" + okapiUrl);
    tenantService.createTenant(tenant);
    return new ResponseEntity<String>("Success", CREATED);
  }

  @DeleteMapping
  public ResponseEntity<Void> delete(
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) String tenant
  ) throws SQLException {
    tenantService.deleteTenant(tenant);
    return new ResponseEntity<Void>(NO_CONTENT);
  }
}
