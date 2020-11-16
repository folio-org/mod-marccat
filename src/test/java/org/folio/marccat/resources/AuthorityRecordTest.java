package org.folio.marccat.resources;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestBase;
import org.folio.marccat.resources.domain.FixedField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author elena
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AuthorityRecordTest extends TestBase {

  private static final String AUTHORITY_RECORD_URL = "/marccat/authority-record";
  private static final String CONTENT_TYPE = "Content-Type";
  private static final String FILE_TYPE = "application/json";

  @Test
  public void saveNameReturn201Status() throws IOException {
    String url = getURI(AUTHORITY_RECORD_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    String templateJson = getTemplateJson("/authority/name.json");

    given()
      .headers(CONTENT_TYPE, FILE_TYPE)
      .headers(headers)
      .queryParam("view", "-1")
      .queryParam("lang", "eng")
      .body(templateJson)
      .when()
      .post(url)
      .then()
      .statusCode(201);

  }

  @Test
  public void getDocumentCountById() throws IOException {

    String url = getURI(AUTHORITY_RECORD_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    String templateJson = getTemplateJson("/authority/name.json");

    given()
      .headers(CONTENT_TYPE, FILE_TYPE)
      .headers(headers)
      .queryParam("view", "-1")
      .queryParam("lang", "eng")
      .body(templateJson)
      .when()
      .post(url)
      .then()
      .statusCode(201);

    url = getURI("/marccat/document-count-by-id");
    headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("id", "1")
      .param("view", "-1")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getEmptyRecord() {

    String url = getURI("/marccat/authority-record/from-template/1");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("lang", "eng")
      .param("view", "-1")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getAuthorityFixedFieldDisplayValue() throws IOException {

    String url = getURI("/marccat/authority-record/fixed-field-display-value");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    String templateJson =  getTemplateJson("/authority/fixedField.json");
    ObjectMapper objectMapper = new ObjectMapper();
    FixedField fixedField = objectMapper.readValue(templateJson, FixedField.class);

    given()
      .headers(CONTENT_TYPE, FILE_TYPE)
      .headers(headers)
      .body(fixedField)
      .when()
      .post(url)
      .then()
      .statusCode(201);

  }
}
