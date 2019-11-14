package org.folio.marccat.resources;

import io.restassured.RestAssured;
import org.folio.marccat.StorageTestSuite;
import org.json.JSONObject;
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


public class HeadingTest {

  @LocalServerPort
  private int localPort;


  @Before
  public void setUp() {
    RestAssured.port = localPort;
  }


  @Test
  public void getDocumentCountById() {

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/create-heading";

    given()
      .param("id", "110")
      .param("view", "-1")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getDocumentCountById_failed() {

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/create-heading";
    JSONObject joTitle = new JSONObject();
    joTitle.put("categoryCode","3");
    joTitle.put("keyNumber","0");
    joTitle.put("ind1","1");
    joTitle.put("ind2","0");
    joTitle.put("displayValue","\u001faStoria");

    given()
      .param("view", "1")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .body(joTitle)
      .when()
      .get(url)
      .then()
      .statusCode(201); //expected fail
  }


}
