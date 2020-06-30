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
public class AutoSuggestionTest extends TestBase {

  @Test
  public void getFilteredTagsList() {

    String url = getURI("/marccat/filteredTagsList");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    given()
      .param("tagNumber", "500")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFilteredTag() {

    String url = getURI("/marccat/filteredTag");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    given()
      .param("tagNumber", "500")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getValidateTag() {

    String url = getURI("/marccat/validateTag");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    given()
      .param("ind1", " ")
      .param("ind2", " ")
      .param("tag", "500")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }


}
