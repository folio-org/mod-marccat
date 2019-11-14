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


public class FixedFieldCodesGroupTest {

  @LocalServerPort
  private int localPort;


  @Before
  public void setUp() {
    RestAssured.port = localPort;
  }


  @Test
  public void getFixedFieldCodesGroupsByLeader() {

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/fixed-fields-code-groups-by-leader";

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "008")
      .param("lang", "ita")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  public void getFixedFieldCodesGroups() {

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/fixed-fields-code-groups";

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "008")
      .param("headerTypeCode", "1")
      .param("lang", "ita")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }




}
