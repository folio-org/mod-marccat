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
public class FieldTemplateTest extends TestBase {

  @Test
  public void getFieldTemplateForVariableTags() {

    String url = getURI("/marccat/field-template");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("categoryCode", "3")
      .param("ind1", "0")
      .param("ind2", "2")
      .param("code", "245")
      .param("headerType", "3")
      .param("leader", "01657nam a22002057i 4500")
      .param("valueField", "\u001faNota generale")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFieldTemplateForFixedTags() {

    String url = getURI("/marccat/field-template");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("categoryCode", "1")
      .param("code", "007")
      .param("ind1", " ")
      .param("ind2", " ")
      .param("headerType", "25")
      .param("leader", "01657nam a22002057i 4500")
      .param("valueField", "hu uuu---uuuu")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }


}
