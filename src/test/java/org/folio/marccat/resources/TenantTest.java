package org.folio.marccat.resources;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestBase;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.response.Response;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")

public class TenantTest extends TestBase {

  @Test
  public void create_withLoadData()
      throws InterruptedException, ExecutionException, TimeoutException, MalformedURLException {
    String url = getURI("/_/tenant");
    JSONObject jo = new JSONObject();
    String moduleFrom = "mod-marccat-2.3.0";
    String moduleTo = "";
    if (moduleFrom != null) {
      jo.put("module_from", moduleFrom);
    }
    jo.put("module_to", moduleTo);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    Response response = given().headers(headers).queryParam("loadSample", "true")
        .queryParam("loadBibliographicSample", "false").body(jo).when().post(url);

    String failureMessage = String.format("Tenant init failed: %s: %s", response.getStatusCode(), response.getBody());

    assertThat(failureMessage, response.getStatusCode(), is(201));

  }

  @Test
  public void create_withoutLoadData()
      throws InterruptedException, ExecutionException, TimeoutException, MalformedURLException {
    String url = getURI("/_/tenant");
    JSONObject jo = new JSONObject();
    String moduleFrom = "mod-marccat-2.3.0";
    String moduleTo = "";
    if (moduleFrom != null) {
      jo.put("module_from", moduleFrom);
    }
    jo.put("module_to", moduleTo);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    Response response = given().headers(headers).queryParam("loadSample", "false")
        .queryParam("loadBibliographicSample", "false").body(jo).when().post(url);

    String failureMessage = String.format("Tenant init failed: %s: %s", response.getStatusCode(), response.getBody());

    assertThat(failureMessage, response.getStatusCode(), is(201));

  }

}
