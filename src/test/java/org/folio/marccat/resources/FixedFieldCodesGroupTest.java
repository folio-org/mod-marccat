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
public class FixedFieldCodesGroupTest extends TestBase {

  @Test
  public void getFixedFieldCodesGroupsByLeader() {

    String url = getURI("/marccat/fixed-fields-code-groups-by-leader");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "008")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFixedFieldCodesGroupsFromLeader() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "000")
      .param("headerTypeCode", "1")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }



  @Test
  public void getFixedFieldCodesGroupsOfGlobe() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "007")
      .param("headerTypeCode", "23")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFixedFieldCodesGroupsOfMap() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "007")
      .param("headerTypeCode", "24")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFixedFieldCodesGroupsOfMicroform() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "007")
      .param("headerTypeCode", "25")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFixedFieldCodesGroupsOfMotionPicture() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "007")
      .param("headerTypeCode", "26")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFixedFieldCodesGroupsOfNonProjecteGraphic() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "007")
      .param("headerTypeCode", "27")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFixedFieldCodesGroupsOfProjecteGraphic() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "007")
      .param("headerTypeCode", "28")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFixedFieldCodesGroupsOfSoundRecording() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "007")
      .param("headerTypeCode", "29")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFixedFieldCodesGroupsOfVideoRecording() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "007")
      .param("headerTypeCode", "30")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFixedFieldCodesGroupsOfElectronicResource() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "007")
      .param("headerTypeCode", "42")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFixedFieldCodesGroupsOfRemoteSensingImage() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "007")
      .param("headerTypeCode", "43")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFixedFieldCodesGroupsOfText() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "007")
      .param("headerTypeCode", "44")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFixedFieldCodesGroupsOfUnspecified() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "007")
      .param("headerTypeCode", "45")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFixedFieldCodesGroupsOfTactileMaterial() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "007")
      .param("headerTypeCode", "46")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFixedFieldCodesGroupsOfKit() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "007")
      .param("headerTypeCode", "47")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFixedFieldCodesGroupsOfNotedMusic() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "007")
      .param("headerTypeCode", "48")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFixedFieldCodesGroupsOfComputerFile() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "006")
      .param("headerTypeCode", "17")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }


  @Test
  public void getFixedFieldCodesGroupsOfMixedMaterial(){

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "006")
      .param("headerTypeCode", "19")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFixedFieldCodesGroupsOfMaps() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "006")
      .param("headerTypeCode", "18")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFixedFieldCodesGroupsOfMusic() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "006")
      .param("headerTypeCode", "20")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFixedFieldCodesGroupsOfSerial() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "006")
      .param("headerTypeCode", "21")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  @Test
  public void getFixedFieldCodesGroupsOfVisualMaterial() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "006")
      .param("headerTypeCode", "22")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }




  @Test
  public void getFixedFieldCodesGroupsByTag008() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "008")
      .param("headerTypeCode", "1")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }

  public void getFixedFieldCodesGroups_failed() {

    String url = getURI("/marccat/fixed-fields-code-groups");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given()
      .param("leader", "01657nam a22002057i 4500")
      .param("code", "009")
      .param("headerTypeCode", "1")
      .param("lang", "ita")
      .headers(headers)
      .when()
      .get(url)
      .then()
      .statusCode(200);
  }




}
