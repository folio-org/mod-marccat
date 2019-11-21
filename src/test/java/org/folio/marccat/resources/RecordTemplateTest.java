package org.folio.marccat.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.dao.BibliographicModelDAO;
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
  public void createNew() throws Exception{

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/record-template";
    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/template.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    RecordTemplate recordTemplate = objectMapper.readValue(templateJson, RecordTemplate.class);
    System.out.println(templateJson);

    Response response = given()
      .queryParam("type", "B")
      .queryParam("lang", "ita")
      .header("Content-Type", "application/json")
      .header("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .body(recordTemplate)
      .when()
      .post(url);

    assertThat(response.getStatusCode(), is(201));
    System.out.println("Response : " + response.getBody().asString());
    JSONObject  responseJson = new JSONObject(response.getBody().asString());
    JSONArray fields = responseJson.getJSONArray("fields");
   /* for (int i = 0; i< fields.length(); i++){
       System.out.println(fields.getJSONObject(i) + "contatore" + i);
    }*/
    System.out.println(fields.getJSONObject(0).get("fixedField"));
    assertThat(fields.getJSONObject(0).get("fixedField"), is("00000000000"));
  }



}
