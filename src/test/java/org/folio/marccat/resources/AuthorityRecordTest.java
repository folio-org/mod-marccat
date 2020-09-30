package org.folio.marccat.resources;

import static io.restassured.RestAssured.given;

import java.util.Map;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.folio.marccat.StorageTestSuite;
import org.folio.marccat.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author elena
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AuthorityRecordTest  extends TestBase {


	
	 @Test
	  public void save_return201Status() throws IOException {
	    String url = getURI("/marccat/authority-record");
	    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);
	    String templateJson = IOUtils.toString(this.getClass().getResourceAsStream("/authority/name.json"),String.valueOf(StandardCharsets.UTF_8));
	  

	    given()
	      .headers("Content-Type", "application/json")
	      .headers(headers)
	      .queryParam("view", "-1")
	      .queryParam("lang", "eng")
	      .body(templateJson)
	      .when()
	      .post(url)
	      .then()
	      .statusCode(201);

	  }
	 
	 @Test
	  public void getDocumentCountById() {

	    String url = getURI("/marccat/document-count-by-id");
	    Map<String, String> headers = addDefaultHeaders(url, StorageTestSuite.TENANT_ID);

	    given()
	      .param("id", "1")
	      .param("view", "-1")
	      .headers(headers)
	      .when()
	      .get(url)
	      .then()
	      .statusCode(200);
	  }

	
}
