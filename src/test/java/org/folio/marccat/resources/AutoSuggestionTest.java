package org.folio.marccat.resources;


import io.restassured.response.Response;
import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.apache.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;
import java.util.Map;


import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AutoSuggestionTest extends TestBase {

  private static final String VALIDATE_TAG_URL = "/marccat/validateTag";

  @Test
  public void getFilteredTagsList_return200Status() {

    String url = getURI("/marccat/filteredTagsList");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    given()
      .param("tagNumber", "500")
      .headers(headers)
      .when()
      .get(url).then()
      .statusCode(200);
  }

  @Test
  public void getFilteredTag_return200Status() {

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
  public void getValidateTag_return200Status() {

    String url = getURI(VALIDATE_TAG_URL);
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

  @Test
  public void getValidateTagWith_return404Status() {

    String url = getURI(VALIDATE_TAG_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    Response response = given()
      .param("ind1", "0")
      .param("ind2", "0")
      .param("tag", "500")
      .headers(headers)
      .when()
      .get(url);
     assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusCode());
  }

}
