package org.folio.marccat.resources;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestBase;
import org.folio.marccat.resources.domain.FixedField;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.response.Response;

/**
 * @author elena
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthorityRecordTest extends TestBase {

  private static final String AUTHORITY_RECORD_URL = "/marccat/authority-record";
  private static final String CONTENT_TYPE = "Content-Type";
  private static final String FILE_TYPE = "application/json";
  
  private static String authorityId;

  @Test
  public void test1_save_return201Status() throws IOException {
    String url = getURI(AUTHORITY_RECORD_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/authority/name3.json"),
        String.valueOf(StandardCharsets.UTF_8));
    
    Response myResponse = 
        given().headers(CONTENT_TYPE, FILE_TYPE).headers(headers).queryParam("view", "-1")
        .queryParam("lang", "eng").body(templateJson).when().post(url);

    authorityId = myResponse.jsonPath().get("body").toString();

    myResponse.then().statusCode(201);

  }

  @Test
  public void test2_getDocumentCountById() throws IOException {

    String url = getURI("/marccat/document-count-by-id");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("id", authorityId).param("view", "-1").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void test3_delete_return204Status() throws IOException {

    String url = getURI(AUTHORITY_RECORD_URL + "/" + authorityId);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().headers(headers).when().delete(url).then().statusCode(204);

  }

  @Test
  public void test5_save_return201Status() throws IOException {
    String url = getURI(AUTHORITY_RECORD_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/authority/name2.json"),
        String.valueOf(StandardCharsets.UTF_8));

    Response myResponse = given().headers(CONTENT_TYPE, FILE_TYPE).headers(headers).queryParam("view", "-1")
        .queryParam("lang", "eng").body(templateJson).when().post(url);

    authorityId = myResponse.jsonPath().get("body").toString();

    myResponse.then().statusCode(201);

  }

  @Test
  public void test6_delete_return423Status() throws IOException {

    String url = getURI(AUTHORITY_RECORD_URL + "/" + authorityId);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().headers(headers).when().delete(url).then().statusCode(423);

  }

  @Test
  public void test7_delete_return404Status() throws IOException {
    String url = getURI("/marccat/authority-record/1000");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().headers(headers).when().delete(url).then().statusCode(404);

  }
  
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
  public void saveTitleReturn201Status() throws IOException {
    String url = getURI(AUTHORITY_RECORD_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/authority/title.json"),
        String.valueOf(StandardCharsets.UTF_8));

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
  public void saveSubjectReturn201Status() throws IOException {
    String url = getURI(AUTHORITY_RECORD_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/authority/subject.json"),
        String.valueOf(StandardCharsets.UTF_8));

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
  public void saveGeographicReturn201Status() throws IOException {
    String url = getURI(AUTHORITY_RECORD_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/authority/geographic.json"),
        String.valueOf(StandardCharsets.UTF_8));

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
