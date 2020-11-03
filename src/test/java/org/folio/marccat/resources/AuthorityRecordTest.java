package org.folio.marccat.resources;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestBase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author elena
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthorityRecordTest extends TestBase {

  @Test
  public void test1_save_return201Status() throws IOException {
    String url = getURI("/marccat/authority-record");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/authority/name.json"),
        String.valueOf(StandardCharsets.UTF_8));

    given().headers("Content-Type", "application/json").headers(headers).queryParam("view", "-1")
        .queryParam("lang", "eng").body(templateJson).when().post(url).then().statusCode(201);

  }

  @Test
  public void test2_getDocumentCountById() throws IOException {

    String url = getURI("/marccat/document-count-by-id");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().param("id", "1").param("view", "-1").headers(headers).when().get(url).then().statusCode(200);
  }

  @Test
  public void test3_delete_return204Status() throws IOException {

    String url = getURI("/marccat/authority-record/1");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().headers(headers).when().delete(url).then().statusCode(204);

  }

  @Test
  public void test4_save_return201Status() throws IOException {
    String url = getURI("/marccat/authority-record");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/authority/name2.json"),
        String.valueOf(StandardCharsets.UTF_8));

    given().headers("Content-Type", "application/json").headers(headers).queryParam("view", "-1")
        .queryParam("lang", "eng").body(templateJson).when().post(url).then().statusCode(201);

  }

  /*
   * @Test public void test5_delete_return423Status() throws IOException {
   * 
   * String url = getURI("/marccat/authority-record/2"); Map<String, String>
   * headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
   * 
   * given().headers(headers).when().delete(url).then().statusCode(423);
   * 
   * }
   */

  @Test
  public void test6_delete_return404Status() throws IOException {
    String url = getURI("/marccat/authority-record/100");
    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

    given().headers(headers).when().delete(url).then().statusCode(404);

  }

}
