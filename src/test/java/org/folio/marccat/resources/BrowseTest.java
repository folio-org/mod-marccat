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


public class BrowseTest {

  @LocalServerPort
  private int localPort;


  @Before
  public void setUp() {
    RestAssured.port = localPort;
  }


 /* @Test
  public void getFirstPage() {

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/browse";

    given()
      .param("query", "TI I promessi sposi")
      .param("view", "1")
      .param("mainLibrary", "172")
      .param("pageSize", "10")
      .param("lang", "ita")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }*/

  @Test
  public void getNextPage() {

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/next-page";

    given()
      .param("query", "TI I promessi sposi")
      .param("view", "1")
      .param("mainLibrary", "172")
      .param("pageSize", "10")
      .param("lang", "ita")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getPreviousPage() {

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/previous-page";

    given()
      .param("query", "TI I promessi sposi")
      .param("view", "1")
      .param("mainLibrary", "172")
      .param("pageSize", "10")
      .param("lang", "ita")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getHeadingsByTag() {

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/headings-by-tag";

    given()
      .param("tag", "245")
      .param("indicator1", "0")
      .param("indicator2", "2")
      .param("stringText", "\u001faI promessi sposi")
      .param("view", "1")
      .param("mainLibrary", "172")
      .param("pageSize", "10")
      .param("lang", "ita")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }



}
