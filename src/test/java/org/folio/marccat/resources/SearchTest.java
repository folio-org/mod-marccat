package org.folio.marccat.resources;


import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")


public class SearchTest extends TestBase {

  @Test
  public void mergedSearch() {

    String url = getURI( "/marccat/mergedSearch");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("qbib", "TI I promessi sposi")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void mergedSearchSortByTitle() {

    String url = getURI( "/marccat/mergedSearch");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("qbib", "TI Devoto-Oli dei sinonimi e contrari")
      .param("lang", "ita")
      .param("sortBy", "4")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }


  @Test
  public void mergedSearchSortBySubject() {

    String url = getURI( "/marccat/mergedSearch");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("qbib", "TI Devoto-Oli dei sinonimi e contrari")
      .param("lang", "ita")
      .param("sortBy", "4")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void mergedSearchByOperatorAnd() {

    String url = getURI( "/marccat/mergedSearch");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("qbib", "TI Devoto-Oli dei sinonimi e contrari AND LAN ita")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void mergedSearchForAuthority() {

    String url = getURI( "/marccat/mergedSearch");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("qbib", "TI I promessi sposi")
      .param("lang", "ita")
      .param("view", "-1")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }


  @Test
  public void search() {

    String url = getURI("/marccat/search");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("q", "TI Storia")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

   @Test
  public void searchVertical() {

    String url = getURI("/marccat/searchVertical");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("q", "TI I promessi sposi")
      .param("lang", "ita")
      .param("ml", "170")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void countSearch() {

    String url = getURI( "/marccat/countSearch");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("q", "TI I promessi sposi")
      .param("lang", "ita")
      .param("ml", "170")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void searchAuth() {

    String url = getURI( "/marccat/searchAuth");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("q", "NA giannini")
      .param("lang", "eng")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }
  
}
