package org.folio.marccat.resources;

import static io.restassured.RestAssured.given;

import java.util.Map;

import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class HeaderTypeTest extends TestBase {

 @Test
  public void getHeaderTypes() {

    String url = getURI("/marccat/header-types");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("code", "008")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getAuthorityHeadingTypes() {

    String url = getURI("/marccat/auth-header-types");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("code", "008")
      .param("lang", "eng")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

}
