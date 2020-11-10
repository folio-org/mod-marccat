package org.folio.marccat;

import java.util.HashMap;
import java.util.Map;

import org.folio.marccat.config.constants.Global;
import org.folio.marccat.integration.MarccatHelper;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;

/**
 * The Class TestBase.
 *
 * @author carment
 * @since 1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class TestBase {

  /** The local port. */
  @LocalServerPort
  private int localPort;

  /** The test mode. */
  @Value("${test.mode}")
  private boolean testMode = false;

  /** The marccat user. */
  @Value("${marccat.username}")
  private String marccatUser;

  /** The marccat password. */
  @Value("${marccat.password}")
  private  String marccatPassword;


  /** The database url. */
  @Value("${marccat.database.url}")
  private String databaseUrl;

  /**
   * Sets the up.
   */
  @Before
  public void setUp() {
    RestAssured.port = localPort;
    MarccatHelper.setTestMode(this.testMode);
    MarccatHelper.setMarccatUser(this.marccatUser);
    MarccatHelper.setMarccatPassword(this.marccatPassword);
    MarccatHelper.setDatasourceUrl(this.databaseUrl);
  }

  /**
   * Gets the uri.
   *
   * @param path the path
   * @return the uri
   */
  public String getURI(String path) {
    StringBuilder uri = new StringBuilder();
    uri.append(RestAssured.baseURI).append(":").append(RestAssured.port).append(path);
    return uri.toString();
  }

  /**
   * Adds the default headers.
   *
   * @param url the url
   * @param tenantId the tenant id
   * @return the map
   */
  public Map<String, String> addDefaultHeaders(String url, String tenantId) {
    Map<String, String> headers = new HashMap<>();
    headers.put(Global.OKAPI_TENANT_HEADER_NAME, tenantId);
    if (url != null) {
      headers.put(Global.OKAPI_URL, "");
      headers.put(Global.OKAPI_URL_TO, "");
      headers.put("Content-Type", "application/json");
    }
    return headers;
  }




}
