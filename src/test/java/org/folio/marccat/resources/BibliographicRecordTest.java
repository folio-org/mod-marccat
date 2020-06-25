package org.folio.marccat.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.apache.commons.io.IOUtils;
import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestConfiguration;
import org.folio.marccat.resources.domain.ContainerRecordTemplate;
import org.folio.marccat.resources.domain.FixedField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class BibliographicRecordTest extends TestConfiguration {

  /*@Test
  public void getRecord() {

    String url = getURI("/marccat/bibliographic-record/2");

    given()
      .param("view", "1")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }*/

  @Test
  public void getRecord_failed() {

    String url = getURI("/marccat/bibliographic-record/100");

    given()
      .param("view", "1")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(500); //expected fail
  }

  @Test
  public void getEmptyRecord() {

    String url = getURI("/marccat/bibliographic-record/from-template/1");

    given()
      .param("lang", "ita")
      .param("view", "1")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }


  @Test
  public void save() throws Exception {
    String url = getURI("/marccat/bibliographic-record");
    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/bibliographic/record.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    ContainerRecordTemplate containerRecordTemplate = objectMapper.readValue(templateJson, ContainerRecordTemplate.class);

    given()
      .headers("Content-Type", "application/json")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
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
    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/bibliographic/fixed_field.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    FixedField fixedField = objectMapper.readValue(templateJson, FixedField.class);

    given()
      .headers("Content-Type", "application/json")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .body(fixedField)
      .when()
      .post(url)
      .then()
      .statusCode(201);

  }


  @Test
  public void delete() {

    String url = getURI( "/marccat/bibliographic-record/1");

    given()
      .param("view", "1")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .delete(url)
      .then()
      .statusCode(204);
  }

  /*@Test
  public void unlock() {

    String url = getURI("/marccat/bibliographic-record/unlock/1");

    given()
      .param("uuid", "1")
      .param("userName", "tx")
      .param( "R")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .delete(url)
      .then()
      .statusCode(204);
  }

  @Test
  public void lock() {

    String url = getURI( "/marccat/bibliographic-record/lock/1");

    given()
      .param("uuid", "1")
      .param("userName", "tx")
      .param( "R")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .delete(url)
      .then()
      .statusCode(204);
  }
*/
 /* @Test
  public void duplicate() {

    String url = getURI("/marccat/bibliographic-record/duplicate");

    given()
      .param("id", new Integer(2))
      .param("lang", "ita")
      .param("view", "1")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }*/



}
