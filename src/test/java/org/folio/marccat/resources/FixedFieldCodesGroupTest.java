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
public class FixedFieldCodesGroupTest extends TestConfiguration {

  @Test
  public void getFixedFieldCodesGroupsByLeader() {

    String url = getURI("/marccat/fixed-fields-code-groups-by-leader");

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

    String url = getURI("/marccat/fixed-fields-code-groups");

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
