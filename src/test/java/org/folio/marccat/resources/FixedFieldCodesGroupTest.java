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
public class FixedFieldCodesGroupTest extends TestBase {

  @Test
  public void getFixedFieldCodesGroupsByLeader() {

    String url = getURI("/marccat/fixed-fields-code-groups-by-leader");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "008")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }


  public void getFixedFieldCodesGroupsFromLeader() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "000")
      .param("headerTypeCode", "1")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  public void getFixedFieldCodesGroupsByTag007() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "007")
      .param("headerTypeCode", "1")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  public void getFixedFieldCodesGroupsByTag008() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "008")
      .param("headerTypeCode", "1")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }




}
