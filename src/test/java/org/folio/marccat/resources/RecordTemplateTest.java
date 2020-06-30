package org.folio.marccat.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestBase;
import org.folio.marccat.resources.domain.BibliographicRecord;
import org.folio.marccat.resources.domain.RecordTemplate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class RecordTemplateTest extends TestBase {

  @Test
  public void getRecordTemplates() {

    String url = getURI( "/marccat/record-templates");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("type","B")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getCatalogingRecordTemplatesById() {

    String url = getURI( "/marccat/record-template/1");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("type","B")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void createNew_Bibliographic() throws Exception{

    String url = getURI("/marccat/record-template");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/bibliographic/template.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    RecordTemplate recordTemplate = objectMapper.readValue(templateJson, RecordTemplate.class);

    Response response = given()
      .queryParam("type", "B")
      .queryParam("lang", "ita")
      .headers(headers)
      .body(recordTemplate)
      .when()
      .post(url);
    assertThat(response.getStatusCode(), is(201));

    JSONObject  responseJson = new JSONObject(response.getBody().asString());
    JSONArray fields = responseJson.getJSONArray("fields");
    String displayValue = String.valueOf((((JSONObject)fields.getJSONObject(0).get("fixedField")).get("displayValue")));
    assertThat(displayValue, is("00000000000"));
  }


  /*@Test
  public void update_Bibliographic() throws Exception{

    String url = getURI("/marccat/record-template/1");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/bibliographic/template.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    RecordTemplate recordTemplate = objectMapper.readValue(templateJson, RecordTemplate.class);
    recordTemplate.setId(2);

    Response response = given()
      .queryParam("type", "B")
      .queryParam("lang", "ita")
      .headers(headers)
      .body(recordTemplate)
      .when()
      .put(url);
    assertThat(response.getStatusCode(), is(204));
  }*/


  @Test
  public void deleteCatalogingRecordTemplatesById_Bibliographic() throws Exception{

    String url = getURI("/marccat/record-template/1");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    Response response = given()
      .queryParam("type", "B")
      .queryParam("lang", "ita")
      .headers(headers)
      .when()
      .delete(url);
    assertThat(response.getStatusCode(), is(204));
  }



  @Test
  public void createFromRecord_Bibliographic() throws Exception{

    String url = getURI("/marccat/record-template/from-record");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/bibliographic/template_by_record.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    BibliographicRecord bibliographicRecord = objectMapper.readValue(templateJson, BibliographicRecord.class);

    Response response = given()
      .queryParam("templateName", "Book")
      .queryParam("lang", "ita")
      .headers(headers)
      .body(bibliographicRecord)
      .when()
      .post(url);
    assertThat(response.getStatusCode(), is(201));
  }

}
