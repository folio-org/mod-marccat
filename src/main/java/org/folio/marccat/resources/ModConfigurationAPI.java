package org.folio.marccat.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.folio.marccat.ModMarccat;
import org.folio.marccat.config.Global;
import org.folio.rest.client.ConfigurationsClient;
import org.folio.rest.client.TenantClient;
import org.folio.rest.jaxrs.model.Config;
import org.folio.rest.jaxrs.model.TenantAttributes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author cchiama
 * @since 1.0
 */

@RestController
@RequestMapping(value = ModMarccat.BASE_URI)
public class ModConfigurationAPI extends BaseResource {

  private ConfigurationsClient cc;

  @Value("${configuration.zeta}")
  private String zeta;

  @GetMapping("/entries")
  public void getConfigurationEntries(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) throws UnsupportedEncodingException {
    cc = new ConfigurationsClient(zeta, 8085, "tnx","folio_demo");
    cc.getConfigurationsEntries("module==MARCCAT", 0, 10, new String[1],"en", response -> {
      response.bodyHandler(body -> {
        System.out.println(body);
      });
    });

  }


  @GetMapping("/entries/{code}")
  public void getConfigurationEntriesByCode(
    @PathVariable final String code,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) throws UnsupportedEncodingException {
    cc = new ConfigurationsClient(zeta, 8085, "tnx","folio_demo");
    cc.getConfigurationsEntries("module==MARCCAT and configName == validation_rules", 0, 10, new String[1],"en", response -> {
      response.bodyHandler(body -> {
        System.out.println(body);
      });
    });

  }

  @PostMapping("/entries")
  public ResponseEntity<Object> saveConfigurationEntries(
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) throws Exception {
    String content = getFile("sample/marccat_configuration.sample");
    cc = new ConfigurationsClient(zeta, 8085, "tnx","folio_demo");
    Config conf =  new ObjectMapper().readValue(content, Config.class);
    cc.postConfigurationsEntries(null, conf, response -> {
      response.bodyHandler( handler -> {
        try {
          System.out.println(new String(handler.getBytes(), "UTF8"));
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      System.out.println(response.statusCode());
    });
    return new ResponseEntity<>("ok", HttpStatus.OK);
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

  @PostMapping("/tenant")
  public void createTenant(
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) throws Exception {
    TenantClient tenantClient = new TenantClient(zeta, 8085, "tnx", "tnx");
    TenantAttributes ta = new TenantAttributes();
    ta.setModuleTo("MARCCAT");
    tenantClient.postTenant(ta,response -> {
      System.out.println(ta);
      response.bodyHandler( handler -> {
        try {
          System.out.println(new String(handler.getBytes(), "UTF8"));

        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      System.out.println(response.statusCode());
      try {
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
}

  private static String getFile(String filename) throws IOException {
    return IOUtils.toString(ModConfigurationAPI.class.getClassLoader().getResourceAsStream(filename), "UTF-8");
  }
}
