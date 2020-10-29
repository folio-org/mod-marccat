package org.folio.marccat.resources;

import static io.restassured.RestAssured.given;

import java.util.Map;

import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class FixedFieldCodesGroupTest extends TestBase {

  private static final String FIXED_FIELDS_CODE_GROUPS_URL = "/marccat/fixed-fields-code-groups";
  private static final String AUT_FIXED_FIELDS_CODE_GROUPS_URL = "/marccat/auth-fixed-fields-code-groups";
  private static final String AUTHORITY_LEADER = "00215nz   2200097n  4500";

  @Test
  public void getFixedFieldsByLeader_return200Status() {

    String url = getURI("/marccat/fixed-fields-code-groups-by-leader");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "008").param("lang", "ita").headers(headers)
        .when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsFromLeader_return200Status() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "000").param("headerTypeCode", "1")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfGlobe_return200Status() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "007").param("headerTypeCode", "23")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfMap_return200Status() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "007").param("headerTypeCode", "24")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfMicroform_return200Status() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "007").param("headerTypeCode", "25")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfMotionPicture_return200Status() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "007").param("headerTypeCode", "26")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfNonProjecteGraphic_return200Status() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "007").param("headerTypeCode", "27")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfProjecteGraphic_return200Status() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "007").param("headerTypeCode", "28")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfSoundRecording_return200Status() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "007").param("headerTypeCode", "29")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfVideoRecording_return200Status() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "007").param("headerTypeCode", "30")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfElectronicResourc_return200Statuse() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "007").param("headerTypeCode", "42")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfRemoteSensingImage_return200Status() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "007").param("headerTypeCode", "43")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfText() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "007").param("headerTypeCode", "44")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfUnspecified_return200Status() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "007").param("headerTypeCode", "45")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfTactileMaterial_return200Status() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "007").param("headerTypeCode", "46")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfKit_return200Status() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "007").param("headerTypeCode", "47")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfNotedMusic_return200Status() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "007").param("headerTypeCode", "48")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfComputerFile_return200Status() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "006").param("headerTypeCode", "17")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfMixedMaterial_return200Status() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "006").param("headerTypeCode", "19")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfMaps_return200Status() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "006").param("headerTypeCode", "18")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfMusic() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "006").param("headerTypeCode", "20")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfSerial_return200Status() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "006").param("headerTypeCode", "21")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsOfVisualMaterial_return200Status() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "006").param("headerTypeCode", "22")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getFixedFieldsByTag008_return200Status() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "008").param("headerTypeCode", "31")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  public void getFixedFieldCodesGroups_failed() {

    String url = getURI(FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", "01657nam a22002057i 4500").param("code", "009").param("headerTypeCode", "1")
        .param("lang", "ita").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getAuthorityFixedFieldsFromLeaderReturn200Status() {

    String url = getURI(AUT_FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", AUTHORITY_LEADER).param("code", "000").param("headerTypeCode", "9").param("lang", "eng")
        .headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void getAuthorityFixedFieldsByTag008Return200Status() {

    String url = getURI(AUT_FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", AUTHORITY_LEADER).param("code", "008").param("headerTypeCode", "10").param("lang", "eng")
        .headers(headers).when().get(url).then().statusCode(200);
  }

  public void getAuthorityFixedFieldCodesGroupsFailed() {

    String url = getURI(AUT_FIXED_FIELDS_CODE_GROUPS_URL);
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("leader", AUTHORITY_LEADER).param("code", "009").param("headerTypeCode", "10").param("lang", "eng")
        .headers(headers).when().get(url).then().statusCode(400);
  }

}
