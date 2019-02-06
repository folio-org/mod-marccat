package org.folio.marccat.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.http.HttpClientResponse;
import org.apache.commons.io.IOUtils;
import org.folio.marccat.ModMarccat;
import org.folio.marccat.config.Global;
import org.folio.rest.client.ConfigurationsClient;
import org.folio.rest.client.TenantClient;
import org.folio.rest.jaxrs.model.Config;
import org.folio.rest.jaxrs.model.TenantAttributes;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

/**
 * @author cchiama
 * @since 1.0
 */

@RestController
@RequestMapping(value = ModMarccat.BASE_URI)
public class ModConfigurationAPI extends BaseResource {

  private ConfigurationsClient cc;

  private String entries;
  private JSONObject jsonobj;

  @Value("${configuration.zeta}")
  private String zeta;

  private static String getFile(String filename) throws IOException {
    return IOUtils.toString(ModConfigurationAPI.class.getClassLoader().getResourceAsStream(filename), "UTF-8");
  }

  @ResponseBody
  @GetMapping("/entries")
  public ResponseEntity<Object> getConfigurationEntries(
    @RequestParam(name = "lang", defaultValue = "en") final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(name = Global.OKAPI_TOKEN_HEADER_NAME, defaultValue = "tnx") final String token) throws UnsupportedEncodingException {
    cc = new ConfigurationsClient(zeta, 8085, tenant, token);
    cc.getConfigurationsEntries(null, 0, 100, null, lang, response -> {
      response.bodyHandler(handler -> {
        try {
          entries = new String(handler.getBytes(), "UTF8");
          jsonobj = new JSONObject(entries);
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
      });
    });
    return new ResponseEntity<>((jsonobj != null) ? jsonobj.toMap(): "{}", HttpStatus.OK);
  }

  @ResponseBody
  @PostMapping("/entries")
  public ResponseEntity<Object> saveConfigurationEntries(
    @RequestParam(name = "lang", defaultValue = "en") final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(name = Global.OKAPI_TOKEN_HEADER_NAME, defaultValue = "folio_demo") final String token) throws Exception {
    String content = getFile("sample/marccat_configuration.sample");
    cc = new ConfigurationsClient(zeta, 8086, tenant, token);
    Config conf = new ObjectMapper().readValue(content, Config.class);
    cc.postConfigurationsEntries(lang, conf, response -> {
      response.bodyHandler(handler -> {
        try {
          out.println(new String(handler.getBytes(), "UTF8"));
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      out.println(response.statusCode());
    });
    return new ResponseEntity<>((conf != null) ? conf: "", HttpStatus.OK);
  }

  @DeleteMapping("/entries/{entryId}")
  public void deleteConfigurationEntriesByEntryId(
    @PathVariable final String entryId,
    @RequestParam(name = "lang", defaultValue = "en") final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(name = Global.OKAPI_TOKEN_HEADER_NAME, defaultValue = "folio_demo") final String token) throws UnsupportedEncodingException {
    cc = new ConfigurationsClient(zeta, 8086, tenant, token);
    cc.deleteConfigurationsEntriesByEntryId(entryId, lang, response -> {
      response.bodyHandler(out::println);
    });
  }

  @ResponseBody
  @GetMapping("/entries/deleteAll")
  public ResponseEntity<Object> deleteAllConfigurationEntries(
    @RequestParam(name = "lang", defaultValue = "en") final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(name = Global.OKAPI_TOKEN_HEADER_NAME, defaultValue = "folio_demo") final String token) throws UnsupportedEncodingException {
    cc = new ConfigurationsClient(zeta, 8085, tenant, token);
    cc.getConfigurationsEntries(null, 0, 100, null, lang, response -> {
      response.bodyHandler(handler -> {
        try {
          entries = new String(handler.getBytes(), "UTF8");
          jsonobj = new JSONObject(entries);
          JSONArray list = (JSONArray) jsonobj.get("configs");
          for (Object aList : list) {
            JSONObject js = (JSONObject) aList;
            String entryId = (String) js.get("id");
            cc.deleteConfigurationsEntriesByEntryId(entryId, lang, respons -> {
              out.println("Entry ->" + entryId + " deleted");
            });
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    });
    return new ResponseEntity<>((jsonobj.length() > 0) ? jsonobj.toMap(): "", HttpStatus.OK);
  }

  @PostMapping("/tenant")
  public void createTenant(
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) throws Exception {
    TenantClient tenantClient = new TenantClient(zeta, 8086, "tnx", "tnx");
    TenantAttributes ta = new TenantAttributes();
    ta.setModuleTo(Global.MODULE_NAME);
    tenantClient.postTenant(ta, response -> {
      out.println(ta);
      response.bodyHandler(handler -> {
        try {
          out.println(new String(handler.getBytes(), "UTF8"));

        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      out.println(response.statusCode());
      try {
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
}
