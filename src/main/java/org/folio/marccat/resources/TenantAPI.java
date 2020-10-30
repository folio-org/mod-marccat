package org.folio.marccat.resources;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.folio.marccat.config.constants.Global;
import org.folio.marccat.integration.TenantRefService;
import org.folio.marccat.integration.TenantService;
import org.folio.rest.jaxrs.model.TenantAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/_/tenant")
public class TenantAPI {
  private static Map<String, String> okapiHeaders = new HashMap<>();

  @Autowired
  private TenantService tenantService;

  @Autowired
  private TenantRefService tenantRefService;

  @PostMapping
  public ResponseEntity<String> create(@RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) String tenant,
      @RequestHeader(Global.OKAPI_URL) String okapiUrl, @RequestHeader(Global.OKAPI_TO_URL) String okapiUrlTo,
      @RequestBody TenantAttributes attributes) throws SQLException, IOException {
    addHeaders(tenant, okapiUrl, okapiUrlTo);
    tenantService.createTenant(tenant, okapiUrl);
    if (!okapiUrl.isEmpty())
      tenantRefService.loadData(attributes, okapiHeaders);
    return new ResponseEntity("Success", CREATED);
  }

  public void addHeaders(@RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) String tenant,
      @RequestHeader(Global.OKAPI_URL) String okapiUrl, @RequestHeader(Global.OKAPI_TO_URL) String okapiUrlTo) {
    okapiHeaders.put(Global.OKAPI_TENANT_HEADER_NAME, tenant);
    okapiHeaders.put(Global.OKAPI_URL, okapiUrl);
    okapiHeaders.put(Global.OKAPI_TO_URL, okapiUrlTo);
  }

  @DeleteMapping
  public ResponseEntity<Void> delete(@RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) String tenant)
      throws SQLException {
    tenantService.deleteTenant(tenant);
    return new ResponseEntity(NO_CONTENT);
  }
}
