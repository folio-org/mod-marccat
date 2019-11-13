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


public class FieldTemplateTest {

  @LocalServerPort
  private int localPort;


  @Before
  public void setUp() {
    RestAssured.port = localPort;
  }


  @Test
  public void getFieldTemplate() {

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/field-template";

    given()
      .param("categoryCode", "7")
      .param("ind1", " ")
      .param("ind2", " ")
      .param("code", "500")
      .param("headerType", "50")
      .param("leader", "01657nam a22002057i 4500")
      .param("valueField", "\u001faNota generale")
      .param("lang", "ita")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getDocumentCountById_failed() {

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/document-count-by-id";

    given()
      .param("id", "-1")
      .param("view", "1")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(500); //expected fail
  }


}