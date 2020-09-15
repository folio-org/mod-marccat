package org.folio.marccat.resources;

import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestBase;
import org.folio.marccat.config.constants.Global;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.File;

import static io.restassured.RestAssured.given;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LoadFromFileTest extends TestBase {

  @Test
  public void loadRecords_return201Status() throws Exception {

    String url = getURI("/marccat/load-from-file");
    String path = this.getClass().getResource("/bibliographic/record.mrc").getFile().toString();

    given()
      .headers(Global.OKAPI_TENANT_HEADER_NAME, StorageTestSuite.TENANT_ID)
      .headers(Global.OKAPI_URL, "")
      .queryParam("view", "1")
      .queryParam("startRecord", "1")
      .queryParam("numberOfRecords", "1")
      .multiPart("files", new File(path))
      .when()
      .post(url)
      .then()
      .statusCode(201);
  }

  @Test
  public void loadRecordsWithAll007_return201Status() throws Exception {

    String url = getURI("/marccat/load-from-file");
    String path = this.getClass().getResource("/bibliographic/record_007.mrc").getFile().toString();

    given()
      .headers(Global.OKAPI_TENANT_HEADER_NAME, StorageTestSuite.TENANT_ID)
      .headers(Global.OKAPI_URL, "")
      .queryParam("view", "1")
      .queryParam("startRecord", "1")
      .queryParam("numberOfRecords", "1")
      .multiPart("files", new File(path))
      .when()
      .post(url)
      .then()
      .statusCode(201);
  }


  @Test
  public void loadRecordsComputerFile_return201Status() throws Exception {

    String url = getURI("/marccat/load-from-file");
    String path = this.getClass().getResource("/bibliographic/record_computer_file.mrc").getFile().toString();

    given()
      .headers(Global.OKAPI_TENANT_HEADER_NAME, StorageTestSuite.TENANT_ID)
      .headers(Global.OKAPI_URL, "")
      .queryParam("view", "1")
      .queryParam("startRecord", "1")
      .queryParam("numberOfRecords", "1")
      .multiPart("files", new File(path))
      .when()
      .post(url)
      .then()
      .statusCode(201);
  }

  @Test
  public void loadRecordsMap_return201Status() throws Exception {

    String url = getURI("/marccat/load-from-file");
    String path = this.getClass().getResource("/bibliographic/record_map.mrc").getFile().toString();

    given()
      .headers(Global.OKAPI_TENANT_HEADER_NAME, StorageTestSuite.TENANT_ID)
      .headers(Global.OKAPI_URL, "")
      .queryParam("view", "1")
      .queryParam("startRecord", "1")
      .queryParam("numberOfRecords", "1")
      .multiPart("files", new File(path))
      .when()
      .post(url)
      .then()
      .statusCode(201);
  }

  @Test
  public void loadRecordsMixedMaterial_return201Status() throws Exception {

    String url = getURI("/marccat/load-from-file");
    String path = this.getClass().getResource("/bibliographic/record_mixed_material.mrc").getFile().toString();

    given()
      .headers(Global.OKAPI_TENANT_HEADER_NAME, StorageTestSuite.TENANT_ID)
      .headers(Global.OKAPI_URL, "")
      .queryParam("view", "1")
      .queryParam("startRecord", "1")
      .queryParam("numberOfRecords", "1")
      .multiPart("files", new File(path))
      .when()
      .post(url)
      .then()
      .statusCode(201);
  }


  @Test
  public void loadRecordsMusic_return201Status() throws Exception {

    String url = getURI("/marccat/load-from-file");
    String path = this.getClass().getResource("/bibliographic/record_music.mrc").getFile().toString();

    given()
      .headers(Global.OKAPI_TENANT_HEADER_NAME, StorageTestSuite.TENANT_ID)
      .headers(Global.OKAPI_URL, "")
      .queryParam("view", "1")
      .queryParam("startRecord", "1")
      .queryParam("numberOfRecords", "1")
      .multiPart("files", new File(path))
      .when()
      .post(url)
      .then()
      .statusCode(201);
  }

  @Test
  public void loadRecordsSerial_return201Status() throws Exception {

    String url = getURI("/marccat/load-from-file");
    String path = this.getClass().getResource("/bibliographic/record_serial.mrc").getFile().toString();

    given()
      .headers(Global.OKAPI_TENANT_HEADER_NAME, StorageTestSuite.TENANT_ID)
      .headers(Global.OKAPI_URL, "")
      .queryParam("view", "1")
      .queryParam("startRecord", "1")
      .queryParam("numberOfRecords", "1")
      .multiPart("files", new File(path))
      .when()
      .post(url)
      .then()
      .statusCode(201);
  }

  @Test
  public void loadRecordsVisualMaterial_return201Status() throws Exception {

    String url = getURI("/marccat/load-from-file");
    String path = this.getClass().getResource("/bibliographic/record_visual_material.mrc").getFile().toString();

    given()
      .headers(Global.OKAPI_TENANT_HEADER_NAME, StorageTestSuite.TENANT_ID)
      .headers(Global.OKAPI_URL, "")
      .queryParam("view", "1")
      .queryParam("startRecord", "1")
      .queryParam("numberOfRecords", "1")
      .multiPart("files", new File(path))
      .when()
      .post(url)
      .then()
      .statusCode(201);
  }

}
