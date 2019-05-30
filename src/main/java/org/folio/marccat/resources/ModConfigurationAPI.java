package org.folio.marccat.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.folio.marccat.config.constants.Global;
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

import static java.lang.System.*;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author cchiama
 * @since 1.0
 */
@RestController
public class ModConfigurationAPI extends BaseResource {


  private ConfigurationsClient cc;

  private String entries;
  private JSONObject jsonobj;

  @Value("${configuration.local}")
  private String local;


  private static String getFile() throws IOException {
    return IOUtils.toString(ModConfigurationAPI.class.getClassLoader().getResourceAsStream("sample/marccat_configuration.sample"), "UTF-8");
  }

  @ResponseBody
  @GetMapping("/entries")
  public ResponseEntity<Object> entries(
    @RequestParam(name = "lang", defaultValue = "en") final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(name = Global.OKAPI_TOKEN_HEADER_NAME, defaultValue = "tnx") final String token) throws UnsupportedEncodingException {
    cc = new ConfigurationsClient(local, tenant, token);
    cc.getConfigurationsEntries(null, 0, 100, null, lang, response -> response.bodyHandler(handler -> {
      entries = new String(handler.getBytes(), UTF_8);
      jsonobj = new JSONObject(entries);
    }));
    return new ResponseEntity<>((jsonobj != null) ? jsonobj.toMap() : "{}", HttpStatus.OK);
  }

  @ResponseBody
  @PostMapping("/entries")
  public ResponseEntity<Object> saveConfigurationEntries(
    @RequestParam(name = "lang", defaultValue = "en") final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(name = Global.OKAPI_TOKEN_HEADER_NAME, defaultValue = "folio_marccat") final String token) throws Exception {
    String content = getFile();
    cc = new ConfigurationsClient(local, tenant, "folio_demo");
    Config conf = new ObjectMapper().readValue(content, Config.class);
    cc.postConfigurationsEntries(lang, conf, response -> {
      response.bodyHandler(handler -> {
        try {
          out.println(new String(handler.getBytes(), UTF_8));
        } catch (Exception e) {
          logger.debug(String.valueOf(e));
        }
      });
      out.println(response.statusCode());
    });
    return new ResponseEntity<>((conf != null) ? conf : "", HttpStatus.OK);
  }

  @DeleteMapping("/entries/{entryId}")
  public void deleteConfigurationEntriesByEntryId(
    @PathVariable final String entryId,
    @RequestParam(name = "lang", defaultValue = "en") final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(name = Global.OKAPI_TOKEN_HEADER_NAME, defaultValue = "folio_demo") final String token) throws UnsupportedEncodingException {
    cc = new ConfigurationsClient(local, tenant, token);
    cc.deleteConfigurationsEntriesByEntryId(entryId, lang, response -> response.bodyHandler(out::println));
  }

  @ResponseBody
  @GetMapping("/entries/deleteAll")
  public ResponseEntity<Object> deleteAll(
    @RequestParam(name = "lang", defaultValue = "en") final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(name = Global.OKAPI_TOKEN_HEADER_NAME, defaultValue = "folio_demo") final String token) throws UnsupportedEncodingException {
    cc = new ConfigurationsClient(local, "tnx", "folio_demo");
    cc.getConfigurationsEntries(null, 0, 100, null, lang, response -> response.bodyHandler(handler -> {
      try {
        entries = new String(handler.getBytes(), UTF_8);
        jsonobj = new JSONObject(entries);
        JSONArray list = (JSONArray) jsonobj.get("configs");
        for (Object aList : list) {
          JSONObject js = (JSONObject) aList;
          String entryId = (String) js.get("id");
          cc.deleteConfigurationsEntriesByEntryId(entryId, lang, respons -> out.println("Entry ->" + entryId + " deleted"));
        }
      } catch (Exception e) {
        logger.debug(String.valueOf(e));
      }
    }));
    return new ResponseEntity<>((jsonobj.length() > 0) ? jsonobj.toMap() : "", HttpStatus.OK);
  }

  @PostMapping("/tenant")
  public void tenant(
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) throws Exception {
    TenantClient tenantClient = new TenantClient(local, "tnx", "tnx");
    TenantAttributes ta = new TenantAttributes();
    ta.setModuleTo(Global.MODULE_NAME);
    tenantClient.postTenant(ta, response -> {
      out.println(ta);
      response.bodyHandler(handler -> {
        try {
          out.println(new String(handler.getBytes(), UTF_8));

        } catch (Exception e) {
          logger.debug(String.valueOf(e));
        }
      });
      out.println(response.statusCode());
      try {
      } catch (Exception e) {
        logger.debug(String.valueOf(e));
      }
    });
  }
}
