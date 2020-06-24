package org.folio.marccat.resources;

import org.folio.marccat.StorageTestSuite;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;

import static io.restassured.RestAssured.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")


public class CountDocumentTest {

  @LocalServerPort
  private int localPort;


  @Before
  public void setUp() {
    RestAssured.port = localPort;
  }


  @Test
  public void getDocumentCountById() {

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/document-count-by-id";

    given()
      .param("id", "1")
      .param("view", "-1")
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
      .statusCode(200); //expected fail
  }


}
