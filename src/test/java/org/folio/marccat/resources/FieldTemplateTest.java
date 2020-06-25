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
public class FieldTemplateTest extends TestConfiguration {

  @Test
  public void getFieldTemplate() {

    String url = getURI("/marccat/field-template");

    given()
      .param("categoryCode", "3")
      .param("ind1", "0")
      .param("ind2", "2")
      .param("code", "245")
      .param("headerType", "3")
      .param("leader", "01657nam a22002057i 4500")
      .param("valueField", "\u001faNota generale")
      .param("lang", "ita")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }



}
