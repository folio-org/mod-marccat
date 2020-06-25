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
public class AutoSuggestionTest extends TestConfiguration {

  @Test
  public void getFilteredTagsList() {

    String url = getURI("/marccat/filteredTagsList");

    given()
      .param("tagNumber", "500")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFilteredTag() {

    String url = getURI("/marccat/filteredTag");

    given()
      .param("tagNumber", "500")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }


}
