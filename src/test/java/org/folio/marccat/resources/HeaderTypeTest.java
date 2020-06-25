package org.folio.marccat.resources;


import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class HeaderTypeTest extends TestConfiguration {

 @Test
  public void getDocumentCountById() {

    String url = getURI( "/marccat/header-types");

    given()
      .param("code", "1")
      .param("lang", "ita")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }




}
