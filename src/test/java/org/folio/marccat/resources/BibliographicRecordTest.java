package org.folio.marccat.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestBase;
import org.folio.marccat.resources.domain.ContainerRecordTemplate;
import org.folio.marccat.resources.domain.FixedField;
import org.folio.marccat.resources.domain.LockEntityType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class BibliographicRecordTest extends TestBase {

  @Test
  public void getRecord() {

    String url = getURI("/marccat/bibliographic-record/2");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("view", "1")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getRecord_failed() {

    String url = getURI("/marccat/bibliographic-record/100");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("view", "1")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(404); //expected fail
  }

  @Test
  public void getEmptyRecord() {

    String url = getURI("/marccat/bibliographic-record/from-template/1");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("lang", "ita")
      .param("view", "1")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }


  @Test
  public void save() throws Exception {
    String url = getURI("/marccat/bibliographic-record");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/bibliographic/record.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    ContainerRecordTemplate containerRecordTemplate = objectMapper.readValue(templateJson, ContainerRecordTemplate.class);

    given()
      .headers("Content-Type", "application/json")
      .headers(headers)
      .queryParam("view", "1")
      .queryParam("lang", "ita")
      .body(containerRecordTemplate)
      .when()
      .post(url)
      .then()
      .statusCode(201);

  }

  @Test
  public void update() throws Exception {
    String url = getURI("/marccat/bibliographic-record");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/bibliographic/record.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    ContainerRecordTemplate containerRecordTemplate = objectMapper.readValue(templateJson, ContainerRecordTemplate.class);

    given()
      .headers("Content-Type", "application/json")
      .headers(headers)
      .queryParam("view", "1")
      .queryParam("lang", "ita")
      .body(containerRecordTemplate)
      .when()
      .post(url)
      .then()
      .statusCode(201);

  }

  @Test
  public void getFixedFieldWithDisplayValue() throws Exception {

    String url = getURI("/marccat/bibliographic-record/fixed-field-display-value");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/bibliographic/fixed_field.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    FixedField fixedField = objectMapper.readValue(templateJson, FixedField.class);

    given()
      .headers("Content-Type", "application/json")
      .headers(headers)
      .body(fixedField)
      .when()
      .post(url)
      .then()
      .statusCode(201);

  }

   @Test
  public void duplicate() {

    String url = getURI("/marccat/bibliographic-record/duplicate");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("id", new Integer(2))
      .param("lang", "ita")
      .param("view", "1")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }


  @Test
  public void delete() {

    String url = getURI( "/marccat/bibliographic-record/2");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("view", "1")
      .headers(headers)
      .when()
      .delete(url)
      .then()
      .statusCode(204);
  }


  @Test
  public void lock() {

    String url = getURI( "/marccat/bibliographic-record/lock/1");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("id", "1")
      .param("uuid", "1")
      .param("userName", "test")
      .param("type",  LockEntityType.R)
      .headers(headers)
      .when()
      .put(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void unlock() {

    String url = getURI("/marccat/bibliographic-record/unlock/1");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("id", "1")
      .param("uuid", "1")
      .param("userName", "test")
      .param("type",  LockEntityType.R)
      .headers(headers)
      .when()
      .delete(url)
      .then()
      .statusCode(204);
  }




}
