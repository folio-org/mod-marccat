package org.folio.marccat.resources;


import io.restassured.response.Response;
import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestConfiguration;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")


public class TenantTest extends TestConfiguration {


  @Test
  public void create()
    throws InterruptedException,
    ExecutionException,
    TimeoutException,
    MalformedURLException {
    String url = getURI("/_/tenant");
    JSONObject jo = new JSONObject();
    String moduleFrom = "mod-marccat-2.3.0";
    String moduleTo ="";
    if (moduleFrom != null) {
      jo.put("module_from", moduleFrom);
    }
    jo.put("module_to", moduleTo);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    Response response = given()
      .headers(headers)
      .body(jo)
      .when()
      .post(url);
    //Thread.sleep(120000);

    String failureMessage = String.format("Tenant init failed: %s: %s",
      response.getStatusCode(), response.getBody());

    assertThat(failureMessage,
      response.getStatusCode(), is(201));

  }



}
