package org.folio.marccat.resources;

import io.restassured.RestAssured;
import org.folio.marccat.StorageTestSuite;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")


public class SearchTest {

  @LocalServerPort
  private int localPort;


  @Before
  public void setUp() {
    RestAssured.port = localPort;
  }


  @Test
  public void mergedSearch() {

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/mergedSearch";

    given()
      .param("qbib", "TI Storia")
      .param("lang", "ita")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void search() {

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/search";

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

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/searchVertical";

    given()
      .param("q", "TI Storia")
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

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/countSearch";

    given()
      .param("q", "TI Storia")
      .param("lang", "ita")
      .param("ml", "170")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

}
