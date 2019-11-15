package org.folio.marccat.resources;

import io.restassured.RestAssured;
import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.resources.domain.Heading;
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
  public void createHeading() {

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/create-heading";
   /* JSONObject joTitle = new JSONObject();
    joTitle.put("categoryCode","3");
    joTitle.put("keyNumber","0");
    joTitle.put("ind1","1");
    joTitle.put("ind2","0");
    joTitle.put("displayValue","\u001faStoria");*/
    Heading heading = new Heading();
    heading.setCategoryCode(3);
    heading.setKeyNumber(0);
    heading.setInd1("1");
    heading.setInd1("0");
    heading.setDisplayValue("\u001faStoria");

    given()
      .headers("Content-Type", "application/json")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .queryParam("view", "1")
      .body(heading)
      .when()
      .post(url)
      .then()
      .statusCode(201);
  }


}
