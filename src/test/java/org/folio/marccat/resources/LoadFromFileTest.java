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
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")


public class LoadFromFileTest {

  @LocalServerPort
  private int localPort;


  @Before
  public void setUp() {
    RestAssured.port = localPort;
  }


  @Test
  public void loadRecords() throws Exception {

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/load-from-file";
    String mrc = IOUtils.toString(this.getClass().getResourceAsStream("/record.mrc"), "UTF-8");
    InputStream inputStream = getClass().getResourceAsStream("/record.mrc");

    given()
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .queryParam("view", "1")
      .queryParam("startRecord", "1")
      .queryParam("numberOfRecords", "1")
      .multiPart("files", inputStream)
      .when()
      .post(url)
      .then()
      .statusCode(200);
  }



}
