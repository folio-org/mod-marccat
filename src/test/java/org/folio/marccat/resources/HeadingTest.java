package org.folio.marccat.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.apache.commons.io.IOUtils;
import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.resources.domain.Heading;
import org.folio.marccat.resources.domain.RecordTemplate;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")


public class HeadingTest {

  @LocalServerPort
  private int localPort;


  @Before
  public void setUp() {
    RestAssured.port = localPort;
  }

  @Test
  public void createTitleHeading() throws Exception{

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/create-heading";

    String headingJson = IOUtils.toString(this.getClass().getResourceAsStream("/bibliographic/title.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    Heading heading = objectMapper.readValue(headingJson, Heading.class);

    given()
      .headers("Content-Type", "application/json")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .queryParam("view", "1")
      .body(heading)
      .when()
      .post(url)
      .then()
      .statusCode(201);

    //Test response keyNumber!=0
  }

  @Test
  public void createTitle2Heading() throws Exception{

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/create-heading";

    String headingJson = IOUtils.toString(this.getClass().getResourceAsStream("/bibliographic/title2.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    Heading heading = objectMapper.readValue(headingJson, Heading.class);

    given()
      .headers("Content-Type", "application/json")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .queryParam("view", "1")
      .body(heading)
      .when()
      .post(url)
      .then()
      .statusCode(201);

    //Test response keyNumber!=0
  }

  @Test
  public void createNameHeading() throws Exception{

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/create-heading";

    String headingJson = IOUtils.toString(this.getClass().getResourceAsStream("/bibliographic/name.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    Heading heading = objectMapper.readValue(headingJson, Heading.class);

    given()
      .headers("Content-Type", "application/json")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .queryParam("view", "1")
      .body(heading)
      .when()
      .post(url)
      .then()
      .statusCode(201);
  }

  @Test
  public void createClassificationHeading() throws Exception{

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/create-heading";

    String headingJson = IOUtils.toString(this.getClass().getResourceAsStream("/bibliographic/classification.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    Heading heading = objectMapper.readValue(headingJson, Heading.class);

    given()
      .headers("Content-Type", "application/json")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .queryParam("view", "1")
      .body(heading)
      .when()
      .post(url)
      .then()
      .statusCode(201);
  }

  @Test
  public void createControlNumberHeading() throws Exception{

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/create-heading";

    String headingJson = IOUtils.toString(this.getClass().getResourceAsStream("/bibliographic/control_number.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    Heading heading = objectMapper.readValue(headingJson, Heading.class);

    given()
      .headers("Content-Type", "application/json")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .queryParam("view", "1")
      .body(heading)
      .when()
      .post(url)
      .then()
      .statusCode(201);
  }

  @Test
  public void createSubjectHeading() throws Exception{

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/create-heading";

    String headingJson = IOUtils.toString(this.getClass().getResourceAsStream("/bibliographic/subject.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    Heading heading = objectMapper.readValue(headingJson, Heading.class);

    given()
      .headers("Content-Type", "application/json")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .queryParam("view", "1")
      .body(heading)
      .when()
      .post(url)
      .then()
      .statusCode(201);
  }

  @Test
  public void createPublisherHeading() throws Exception{

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/create-heading";

    String headingJson = IOUtils.toString(this.getClass().getResourceAsStream("/bibliographic/publisher.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    Heading heading = objectMapper.readValue(headingJson, Heading.class);

    given()
      .headers("Content-Type", "application/json")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .queryParam("view", "1")
      .body(heading)
      .when()
      .post(url)
      .then()
      .statusCode(201);
  }


  @Test
  public void createNameTitleHeading() throws Exception{

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/create-heading";

    String headingJson = IOUtils.toString(this.getClass().getResourceAsStream("/bibliographic/name_title.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    Heading heading = objectMapper.readValue(headingJson, Heading.class);

    given()
      .headers("Content-Type", "application/json")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .queryParam("view", "1")
      .body(heading)
      .when()
      .post(url)
      .then()
      .statusCode(201);
  }



  @Test
  public void updateHeading() {

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/update-heading";
    Heading heading = new Heading();
    heading.setTag("245");
    heading.setCategoryCode(3);
    heading.setKeyNumber(1);
    heading.setInd1("0");
    heading.setInd2("2");
    heading.setDisplayValue("\u001faI promessi sposi/\u001fcAlessandro Manzoni");

    given()
      .headers("Content-Type", "application/json")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .queryParam("view", "1")
      .body(heading)
      .when()
      .put(url)
      .then()
      .statusCode(201);
  }

  @Test
  public void deleteHeading() throws Exception{

    String url = RestAssured.baseURI + ":" + RestAssured.port + "/marccat/delete-heading";
    String headingJson = IOUtils.toString(this.getClass().getResourceAsStream("/bibliographic/title2.json"), "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    Heading heading = objectMapper.readValue(headingJson, Heading.class);

    given()
      .headers("Content-Type", "application/json")
      .headers("X-Okapi-Tenant", StorageTestSuite.TENANT_ID)
      .queryParam("view", "1")
      .body(heading)
      .when()
      .delete(url)
      .then()
      .statusCode(500);
  }
}
