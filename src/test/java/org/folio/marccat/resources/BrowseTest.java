package org.folio.marccat.resources;


import io.restassured.response.Response;
import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestBase;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")

public class BrowseTest extends TestBase {


  @Test
  public void getFirstPageByTitle() {

    String url = getURI("/marccat/browse");
    Map <String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("query", "TI I promessi sposi")
      .param("view", "1")
      .param("mainLibrary", "172")
      .param("pageSize", "10")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFirstPageByName() {

    String url = getURI("/marccat/browse");
    Map <String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("query", "NA Alessandro Manzoni")
      .param("view", "1")
      .param("mainLibrary", "172")
      .param("pageSize", "10")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFirstPageByControlNumber() {

    String url = getURI("/marccat/browse");
    Map <String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("query", "BN 9788824731041")
      .param("view", "1")
      .param("mainLibrary", "172")
      .param("pageSize", "10")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFirstPageByClassification() {

    String url = getURI("/marccat/browse");
    Map <String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("query", "LC QA37")
      .param("view", "1")
      .param("mainLibrary", "172")
      .param("pageSize", "10")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }


  @Test
  public void getFirstPageBySegnature() {

    String url = getURI("/marccat/browse");
    Map <String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("query", "LL QA37")
      .param("view", "1")
      .param("mainLibrary", "172")
      .param("pageSize", "10")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFirstPageByNameTitle() {

    String url = getURI("/marccat/browse");
    Map <String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("query", "NTN Alessandro Manzoni. Promessi sposi")
      .param("view", "1")
      .param("mainLibrary", "172")
      .param("pageSize", "10")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFirstPageByNameTitleTitle() {

    String url = getURI("/marccat/browse");
    Map <String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("query", "NTT Alessandro Manzoni. Promessi sposi")
      .param("view", "1")
      .param("mainLibrary", "172")
      .param("pageSize", "10")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFirstPageByPublisher() {

    String url = getURI("/marccat/browse");
    Map <String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("query", "PU Bologna : Zanichelli")
      .param("view", "1")
      .param("mainLibrary", "172")
      .param("pageSize", "10")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFirstPageByPublisherPlace() {

    String url = getURI("/marccat/browse");
    Map <String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("query", "PP Bologna : Zanichelli")
      .param("view", "1")
      .param("mainLibrary", "172")
      .param("pageSize", "10")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getNextPageByPublisherPlace() {

    String url = getURI("/marccat/next-page");
    Map <String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("query", "PP Bologna : Zanichelli")
      .param("view", "1")
      .param("mainLibrary", "172")
      .param("pageSize", "10")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }


  @Test
  public void getNextPage() {

    String url = getURI("/marccat/next-page");
    Map <String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("query", "TI I promessi sposi")
      .param("view", "1")
      .param("mainLibrary", "172")
      .param("pageSize", "10")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getPreviousPage() {

    String url = getURI("/marccat/previous-page");
    Map <String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("query", "TI I promessi sposi")
      .param("view", "1")
      .param("mainLibrary", "172")
      .param("pageSize", "10")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getHeadingsByTag() {

    String url = getURI("/marccat/headings-by-tag");
    Map <String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("tag", "245")
      .param("indicator1", "0")
      .param("indicator2", "2")
      .param("stringText", "\u001faI promessi sposi")
      .param("view", "1")
      .param("mainLibrary", "172")
      .param("pageSize", "10")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }
}
