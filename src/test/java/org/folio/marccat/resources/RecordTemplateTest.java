package org.folio.marccat.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.resources.domain.BibliographicRecord;
import org.folio.marccat.resources.domain.RecordTemplate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")


public class RecordTemplateTest {

  @LocalServerPort
  private int localPort;


  @Before
  public void setUp() {
    RestAssured.port = localPort;
  }


  @Test
  public void getRecordTemplates() {

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/record-templates";

    given()
      .param("type","B")
      .param("lang", "ita")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getCatalogingRecordTemplatesById() {

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/record-template/1";

    given()
      .param("type","B")
      .param("lang", "ita")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void createNew_Bibliographic() throws Exception{

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/record-template";
    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/bibliographic/template.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    RecordTemplate recordTemplate = objectMapper.readValue(templateJson, RecordTemplate.class);

    Response response = given()
      .queryParam("type", "B")
      .queryParam("lang", "ita")
      .header("Content-Type", "application/json")
      .header("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .body(recordTemplate)
      .when()
      .post(url);
    assertThat(response.getStatusCode(), is(201));

    JSONObject  responseJson = new JSONObject(response.getBody().asString());
    JSONArray fields = responseJson.getJSONArray("fields");
    String displayValue = String.valueOf((((JSONObject)fields.getJSONObject(0).get("fixedField")).get("displayValue")));
    assertThat(displayValue, is("00000000000"));
  }

 /* @Test
  public void createNew_Authority() throws Exception{

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/record-template";
    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/authority/template.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    RecordTemplate recordTemplate = objectMapper.readValue(templateJson, RecordTemplate.class);

    Response response = given()
      .queryParam("type", "A")
      .queryParam("lang", "ita")
      .header("Content-Type", "application/json")
      .header("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .body(recordTemplate)
      .when()
      .post(url);
    assertThat(response.getStatusCode(), is(201));

    JSONObject  responseJson = new JSONObject(response.getBody().asString());
    JSONArray fields = responseJson.getJSONArray("fields");
    String displayValue = String.valueOf((((JSONObject)fields.getJSONObject(0).get("fixedField")).get("displayValue")));
    assertThat(displayValue, is("00000000000"));
  }*/

  /*@Test
  public void update_Bibliographic() throws Exception{

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/record-template/1";
    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/bibliographic/template.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    RecordTemplate recordTemplate = objectMapper.readValue(templateJson, RecordTemplate.class);
    recordTemplate.setId(43);

    Response response = given()
      .queryParam("type", "B")
      .queryParam("lang", "ita")
      .header("Content-Type", "application/json")
      .header("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .body(recordTemplate)
      .when()
      .put(url);
    assertThat(response.getStatusCode(), is(204));
  }
*/


  @Test
  public void deleteCatalogingRecordTemplatesById_Bibliographic() throws Exception{

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/record-template/1";

    Response response = given()
      .queryParam("type", "B")
      .queryParam("lang", "ita")
      .header("Content-Type", "application/json")
      .header("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .delete(url);
    assertThat(response.getStatusCode(), is(204));
  }

 /* @Test
  public void deleteCatalogingRecordTemplatesById_Authority() throws Exception{

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/record-template/3";

    Response response = given()
      .queryParam("type", "A")
      .queryParam("lang", "ita")
      .header("Content-Type", "application/json")
      .header("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .when()
      .delete(url);
    assertThat(response.getStatusCode(), is(204));
  }*/

  @Test
  public void createFromRecord_Bibliographic() throws Exception{

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/record-template/from-record";
    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/bibliographic/template_by_record.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    BibliographicRecord bibliographicRecord = objectMapper.readValue(templateJson, BibliographicRecord.class);

    Response response = given()
      .queryParam("templateName", "Book")
      .queryParam("lang", "ita")
      .header("Content-Type", "application/json")
      .header("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .body(bibliographicRecord)
      .when()
      .post(url);
    assertThat(response.getStatusCode(), is(201));
  }

}
