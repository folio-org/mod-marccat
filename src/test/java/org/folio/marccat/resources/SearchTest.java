package org.folio.marccat.resources;


import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestConfiguration;
import org.folio.marccat.config.constants.Global;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")


public class SearchTest extends TestConfiguration {

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

}
