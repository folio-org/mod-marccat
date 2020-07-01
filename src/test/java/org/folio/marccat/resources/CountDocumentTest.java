package org.folio.marccat.resources;

import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static io.restassured.RestAssured.*;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class CountDocumentTest  extends TestBase {

  @Test
  public void getDocumentCountById() {

    String url = getURI("/marccat/document-count-by-id");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("id", "1")
      .param("view", "-1")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getDocumentCountById_failed() {

    String url = getURI("/marccat/document-count-by-id");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("id", "-1")
      .param("view", "1")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200); //expected fail
  }


}
