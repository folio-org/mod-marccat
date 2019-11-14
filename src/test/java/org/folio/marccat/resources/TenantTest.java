package org.folio.marccat.resources;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")


public class TenantTest {

  //public static final String TENANT_ID = "test_tenant";
  public static final String TENANT_ID = "tnx";

  @LocalServerPort
  private int localPort;

  @Before
  public void setUp() {
    RestAssured.port = localPort;
  }

  @Test
  public void create()
    throws InterruptedException,
    ExecutionException,
    TimeoutException,
    MalformedURLException {
    String url = RestAssured.baseURI + ":" + RestAssured.port + "/_/tenant";
    System.out.println(url);
    JSONObject jo = new JSONObject();
    String moduleFrom = "mod-marccat-2.3.0";
    String moduleTo ="";
    if (moduleFrom != null) {
      jo.put("module_from", moduleFrom);
    }
    jo.put("module_to", moduleTo);

    Response response = given()
      .header("Content-Type", "application/json")
      .header("X-Okapi-Tenant", TENANT_ID)
      .header("X-Okapi-Url", "http://localhost:9130")
      .body(jo)
      .when()
      .post(url);

    String failureMessage = String.format("Tenant init failed: %s: %s",
      response.getStatusCode(), response.getBody());

    assertThat(failureMessage,
      response.getStatusCode(), is(201));
  }

}
