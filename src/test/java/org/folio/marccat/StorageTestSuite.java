package org.folio.marccat;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.folio.marccat.resources.*;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static io.restassured.RestAssured.given;




/**
 * The Class StorageTestSuite.
 *
 * @author carment
 * @since 1.0
 */
@RunWith(Suite.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")

@Suite.SuiteClasses({
  TenantTest.class,
  LoadFromFileTest.class,
  CountDocumentTest.class,
  SearchTest.class,
  HeadingTest.class,
  BibliographicRecordTest.class,
  BrowseTest.class,
  FieldTest.class,
  FieldTemplateTest.class,
  FixedFieldCodesGroupTest.class,
  HeaderTypeTest.class,
  AutoSuggestionTest.class,
  RecordTemplateTest.class,
  AuthorityRecordTest.class
})

public class StorageTestSuite {

  /** The Constant TENANT_ID. */
  public static final String TENANT_ID = "test";

  /** The local port. */
  @LocalServerPort
  private int localPort;

  /**
   * Instantiates a new storage test suite.
   */
  private StorageTestSuite() {
    throw new UnsupportedOperationException("Cannot instantiate utility class.");
  }


  /**
   * After class.
   *
   * @throws InterruptedException the interrupted exception
   * @throws ExecutionException the execution exception
   * @throws TimeoutException the timeout exception
   * @throws MalformedURLException the malformed URL exception
   */
  @AfterClass
  public static void afterClass()
    throws InterruptedException,
    ExecutionException,
    TimeoutException,
    MalformedURLException {
    //Delete tenant
    //removeTenant(TENANT_ID);

  }

  /**
   * Prepare tenant.
   *
   * @param tenantId the tenant id
   * @param loadSample the load sample
   * @throws InterruptedException the interrupted exception
   * @throws ExecutionException the execution exception
   * @throws TimeoutException the timeout exception
   * @throws MalformedURLException the malformed URL exception
   */
  static void prepareTenant(String tenantId, boolean loadSample)
    throws InterruptedException,
    ExecutionException,
    TimeoutException,
    MalformedURLException {
    prepareTenant(tenantId, null, "mod-marccat-2.3.0", loadSample);
  }

  /**
   * Prepare tenant.
   *
   * @param tenantId the tenant id
   * @param moduleFrom the module from
   * @param moduleTo the module to
   * @param loadSample the load sample
   * @throws InterruptedException the interrupted exception
   * @throws ExecutionException the execution exception
   * @throws TimeoutException the timeout exception
   * @throws MalformedURLException the malformed URL exception
   */
  static void prepareTenant(String tenantId, String moduleFrom, String moduleTo, boolean loadSample)
    throws InterruptedException,
    ExecutionException,
    TimeoutException,
    MalformedURLException {
    RestAssured.port = 8080;
    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/_/tenant";
    JSONObject jo = new JSONObject();
    if (moduleFrom != null) {
      jo.put("module_from", moduleFrom);
    }
    jo.put("module_to", moduleTo);

    Response response = given()
      .headers("Content-Type", "application/json")
      .headers("X-Okapi-Tenant", TENANT_ID)
      .headers("X-Okapi-Url", "http://localhost:9130")
      .body(jo)
      .when()
      .post(url);

    String failureMessage = String.format("Tenant init failed: %s: %s",
      response.getStatusCode(), response.getBody());

    assertThat(failureMessage,
      response.getStatusCode(), is(201));
  }


  /**
   * Removes the tenant.
   *
   * @param tenantId the tenant id
   * @throws InterruptedException the interrupted exception
   * @throws ExecutionException the execution exception
   * @throws TimeoutException the timeout exception
   * @throws MalformedURLException the malformed URL exception
   */
  static void removeTenant(String tenantId)
    throws InterruptedException,
    ExecutionException,
    TimeoutException,
    MalformedURLException {
    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/_/tenant";
    Response response = given()
      .headers("Content-Type", "application/json")
      .headers("X-Okapi-Tenant", TENANT_ID)
      .headers("X-Okapi-Url", "http://localhost:9130")
      .when()
      .delete(url);

    String failureMessage = String.format("Tenant cleanup failed: %s: %s",
      response.getStatusCode(), response.getBody());

    assertThat(failureMessage,
      response.getStatusCode(), is(204));

  }
}
