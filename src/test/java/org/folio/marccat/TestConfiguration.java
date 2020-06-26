package org.folio.marccat;

import io.restassured.RestAssured;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.integration.MarccatHelper;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class TestConfiguration {

  @LocalServerPort
  private int localPort;

  @Value("${test.mode}")
  private boolean testMode = false;

  @Value("${marccat.username}")
  private String marccatUser;

  @Value("${marccat.password}")
  private  String marccatPassword;


  @Value("${marccat.database.url}")
  private String databaseUrl;

  @Before
  public void setUp() {
    RestAssured.port = localPort;
    MarccatHelper.testMode = this.testMode;
    MarccatHelper.marccatUser = this.marccatUser;
    MarccatHelper.marccatPassword = this.marccatPassword;
    MarccatHelper.datasourceUrl = databaseUrl;
  }

  public String getURI(String path) {
    StringBuilder uri = new StringBuilder();
    uri.append(RestAssured.baseURI).append(":").append(RestAssured.port).append(path);
    return uri.toString();
  }

  public Map<String, String> addDefaultHeaders(String url, String tenantId) {
    Map<String, String> headers = new HashMap<>();
    headers.put(Global.OKAPI_TENANT_HEADER_NAME, tenantId);
    if (url != null) {
      headers.put(Global.OKAPI_URL, "");
      headers.put(Global.OKAPI_TO_URL, "");
      headers.put("Content-Type", "application/json");
    }
    return headers;
  }




}
