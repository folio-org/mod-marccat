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
    @RequestParam(name = "lang", defaultValue = "en") final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(name = Global.OKAPI_TOKEN_HEADER_NAME, defaultValue = "folio_demo") final String token) throws UnsupportedEncodingException {
    cc = new ConfigurationsClient(zeta, 8085, tenant,token);
    cc.getConfigurationsEntries("module==MARCCAT", 0, 10, new String[1],lang, response -> {
      response.bodyHandler(System.out::println);
    });

  }


  @GetMapping("/entries/{code}")
  public void getConfigurationEntriesByCode(
    @PathVariable final String code,
    @RequestParam(name = "lang", defaultValue = "en") final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(name = Global.OKAPI_TOKEN_HEADER_NAME, defaultValue = "folio_demo") final String token) throws UnsupportedEncodingException {
    cc = new ConfigurationsClient(zeta, 8085, tenant,token);
    cc.getConfigurationsEntries("module==MARCCAT and configName == " + code, 0, 10, new String[1],lang, response -> {
      response.bodyHandler(System.out::println);
    });

  }

  @PostMapping("/entries")
  public ResponseEntity<Object> saveConfigurationEntries(
    @RequestParam(name = "lang", defaultValue = "en") final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(name = Global.OKAPI_TOKEN_HEADER_NAME, defaultValue = "folio_demo") final String token) throws Exception {
    String content = getFile("sample/marccat_configuration.sample");
    cc = new ConfigurationsClient(zeta, 8085, tenant,token);
    Config conf =  new ObjectMapper().readValue(content, Config.class);
    cc.postConfigurationsEntries(lang, conf, response -> {
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


  @DeleteMapping("/entries/{entryId}")
  public void deleteConfigurationEntriesByEntryId(
    @PathVariable final String entryId,
    @RequestParam(name = "lang", defaultValue = "en") final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(name = Global.OKAPI_TOKEN_HEADER_NAME, defaultValue = "folio_demo") final String token) throws UnsupportedEncodingException {
    cc = new ConfigurationsClient(zeta, 8085, tenant,token);
    cc.deleteConfigurationsEntriesByEntryId(entryId, lang,  response -> {
      response.bodyHandler(System.out::println);
    });
  }

  @PostMapping("/tenant")
  public void createTenant(
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) throws Exception {
    TenantClient tenantClient = new TenantClient(zeta, 8085, "tnx", "tnx");
    TenantAttributes ta = new TenantAttributes();
    ta.setModuleTo(Global.MODULE_NAME);
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
