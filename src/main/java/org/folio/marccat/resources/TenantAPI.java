package org.folio.marccat.resources;
import org.folio.marccat.config.constants.Global;
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
  @Autowired
 private TenantService tenantService;

  @PostMapping
  public ResponseEntity<String> create(
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME)  String tenant,
    @RequestBody TenantAttributes attributes
  ) throws SQLException, IOException {
    tenantService.createTenant(tenant);
    return new ResponseEntity("Success", CREATED);
  }

  @DeleteMapping
  public ResponseEntity<Void> delete(
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) String tenant
  ) throws SQLException {
    tenantService.deleteTenant(tenant);
    return new ResponseEntity(NO_CONTENT);
  }
}
