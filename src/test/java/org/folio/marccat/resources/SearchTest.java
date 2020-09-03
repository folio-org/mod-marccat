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


public class SearchTest extends TestBase {

  private static final String MERGED_SEARCH_URL = "/marccat/mergedSearch";

  @Test
  public void getMergedSearch_return200Status() {

    String url = getURI(MERGED_SEARCH_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("qbib", "TI I promessi sposi")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getMergedSearchWithAnyView_return200Status() {

    String url = getURI( MERGED_SEARCH_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("qbib", "TI I promessi sposi")
      .param("lang", "ita")
      .param("view", 0)
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getMergedSearchSortByTitle_return200Status() {

    String url = getURI( MERGED_SEARCH_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("qbib", "TI Devoto-Oli dei sinonimi e contrari")
      .param("lang", "ita")
      .param("sortBy", "4")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }


  @Test
  public void getMergedSearchSortBySubject_return200Status() {

    String url = getURI( MERGED_SEARCH_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("qbib", "SU Manzoni, Alessandro, 1785-1873")
      .param("lang", "ita")
      .param("sortBy", "21")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getMergedSearchByOperatorAnd_return200Status() {

    String url = getURI( MERGED_SEARCH_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("qbib", "TI Devoto-Oli dei sinonimi e contrari AND LAN ita")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getMergedSearchForAuthority_return200Status() {

    String url = getURI( MERGED_SEARCH_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("qbib", "TI I promessi sposi")
      .param("qauth", "TI I promessi sposi")
      .param("lang", "ita")
      .param("view", -1)
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }


  @Test
  public void getSearch_return200Status() {

    String url = getURI("/marccat/search");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("q", "TI Storia")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

   @Test
  public void getSearchVertica_return200Statusl() {

    String url = getURI("/marccat/searchVertical");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("q", "TI I promessi sposi")
      .param("lang", "ita")
      .param("ml", "170")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getCountSearch_return200Status() {

    String url = getURI( "/marccat/countSearch");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("q", "TI I promessi sposi")
      .param("lang", "ita")
      .param("ml", "170")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void searchAuth() {

    String url = getURI( "/marccat/searchAuth");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("q", "NA giannini")
      .param("lang", "eng")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }
  
}
