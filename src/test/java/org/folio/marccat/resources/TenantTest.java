package org.folio.marccat.resources;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestBase;
import org.folio.marccat.config.constants.Global;
import org.folio.rest.jaxrs.model.Parameter;
import org.folio.rest.jaxrs.model.TenantAttributes;
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

    TenantAttributes ta = new TenantAttributes();
    ta.setModuleFrom("mod-marccat-2.3.0");
    ta.setModuleTo("");

    Parameter p = new Parameter();
    p.setKey("loadSample");
    p.setValue("true");

    Parameter p2 = new Parameter();
    p2.setKey("loadReference");
    p2.setValue("false");

    Parameter p3 = new Parameter();
    p3.setKey("loadBibliographicSample");
    p3.setValue("false");
    List<Parameter> parameters = Arrays.asList(p, p2, p3);
    ta.setParameters(parameters);
    Map<String, String> headers = addDefaultHeadersForTenant(url, StorageTestSuite.TENANT_ID);
    Response response = given().headers(headers).body(ta).when().post(url);

    String failureMessage = String.format("Tenant init failed: %s: %s", response.getStatusCode(), response.getBody());

    assertThat(failureMessage, response.getStatusCode(), is(201));

  }

  @Test
  public void create_withoutLoadData()
      throws InterruptedException, ExecutionException, TimeoutException, MalformedURLException {
    String url = getURI("/_/tenant");

    TenantAttributes ta = new TenantAttributes();
    ta.setModuleFrom("mod-marccat-2.3.0");
    ta.setModuleTo("");

    Parameter p = new Parameter();
    p.setKey("loadSample");
    p.setValue("false");

    Parameter p2 = new Parameter();
    p2.setKey("loadReference");
    p2.setValue("false");

    Parameter p3 = new Parameter();
    p3.setKey("loadBibliographicSample");
    p3.setValue("false");
    List<Parameter> parameters = Arrays.asList(p, p2, p3);
    ta.setParameters(parameters);

    Map<String, String> headers = addDefaultHeadersForTenant(url, StorageTestSuite.TENANT_ID);
    Response response = given().headers(headers).body(ta).when().post(url);

    String failureMessage = String.format("Tenant init failed: %s: %s", response.getStatusCode(), response.getBody());

    assertThat(failureMessage, response.getStatusCode(), is(201));

  }

  @Test
  public void create_withLoadReference()
      throws InterruptedException, ExecutionException, TimeoutException, MalformedURLException {
    String url = getURI("/_/tenant");

    TenantAttributes ta = new TenantAttributes();
    ta.setModuleFrom("mod-marccat-2.3.0");
    ta.setModuleTo("");

    Parameter p = new Parameter();
    p.setKey("loadSample");
    p.setValue("true");

    Parameter p2 = new Parameter();
    p2.setKey("loadReference");
    p2.setValue("true");

    Parameter p3 = new Parameter();
    p3.setKey("loadBibliographicSample");
    p3.setValue("false");
    List<Parameter> parameters = Arrays.asList(p, p2, p3);
    ta.setParameters(parameters);

    Map<String, String> headers = addDefaultHeadersForTenant(url, StorageTestSuite.TENANT_ID);
    Response response = given().headers(headers).body(ta).when().post(url);

    String failureMessage = String.format("Tenant init failed: %s: %s", response.getStatusCode(), response.getBody());

    assertThat(failureMessage, response.getStatusCode(), is(201));

  }
  
  /**
   * Adds the default headers.
   *
   * @param url      the url
   * @param tenantId the tenant id
   * @return the map
   */
  public Map<String, String> addDefaultHeadersForTenant(String url, String tenantId) {
    Map<String, String> headers = new HashMap<>();
    headers.put(Global.OKAPI_TENANT_HEADER_NAME, tenantId);
    if (url != null) {
      headers.put(Global.OKAPI_URL, "http://localhost:8080");
      headers.put(Global.OKAPI_URL_TO, "http://localhost:8080");
      headers.put("Content-Type", "application/json");
    }
    return headers;
  }

}
