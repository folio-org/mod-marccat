package org.folio.marccat.resources;


import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class HeaderTypeTest extends TestBase {

 @Test
  public void getDocumentCountById() {

    String url = getURI( "/marccat/header-types");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("code", "1")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }




}
