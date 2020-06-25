package org.folio.marccat.resources;


import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")


public class SearchTest extends TestConfiguration {

  @Test
  public void mergedSearch() {

    String url = getURI( "/marccat/mergedSearch");

    given()
      .param("qbib", "TI I promessi sposi")
      .param("lang", "ita")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void search() {

    String url = getURI("/marccat/search");

    given()
      .param("q", "TI Storia")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

   @Test
  public void searchVertical() {

    String url = getURI("/marccat/searchVertical");

    given()
      .param("q", "TI I promessi sposi")
      .param("lang", "ita")
      .param("ml", "170")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void countSearch() {

    String url = getURI( "/marccat/countSearch");

    given()
      .param("q", "TI I promessi sposi")
      .param("lang", "ita")
      .param("ml", "170")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

}
