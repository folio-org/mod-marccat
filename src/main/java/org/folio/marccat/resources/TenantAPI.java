package org.folio.marccat.resources;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Log;

import org.folio.marccat.integration.TenantRefService;
import org.folio.marccat.integration.TenantService;
import org.folio.rest.jaxrs.model.TenantAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/_/tenant")
public class TenantAPI {

  private static final Log logger = new Log(TenantAPI.class);
  private static Map<String,String> okapiHeaders = new HashMap<>();

  @Autowired
  private TenantService tenantService;

  @Autowired
  private TenantRefService tenantRefService;

  @PostMapping
  public ResponseEntity<String> create(
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl,
    @RequestBody TenantAttributes attributes
  ){
    logger.debug("URL OKAPI:" + okapiUrl);
    okapiHeaders.put(Global.OKAPI_TENANT_HEADER_NAME, tenant);
    okapiHeaders.put(Global.OKAPI_URL, okapiUrl);
   // tenantService.createTenant(tenant, okapiUrl);
    tenantRefService.loadData(attributes, okapiHeaders);
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
