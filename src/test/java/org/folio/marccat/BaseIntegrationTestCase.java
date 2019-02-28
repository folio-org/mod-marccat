package org.folio.marccat;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.folio.marccat.config.Global;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.fail;
import static org.folio.marccat.ModMarccat.BASE_URI;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;
import static ru.yandex.qatools.embed.postgresql.distribution.Version.Main.V9_6;

/**
 * Supertype layer for all bibliographic test cases.
 *
 * @author cchiama
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTestCase {
  @LocalServerPort
  protected int port;

  @Autowired
  protected TestRestTemplate client;

  @Autowired
  protected TestConfiguration configuration;

  protected final static String DB_USERNAME = "amicus";
  protected final static String DB_PASSWORD = "oracle";

  private final static EmbeddedPostgres POSTGRES = new EmbeddedPostgres(V9_6);
  private static String POSTGRES_JDBC_URL;

  private static String getFile(String filename) throws IOException {
    return IOUtils.toString(BaseIntegrationTestCase.class.getClassLoader().getResourceAsStream(filename), "UTF-8");
  }
  /**
   * Starts the embedded database instance.
   *
   * @throws Exception hopefully never, otherwise the test fails.
   */
  @BeforeClass
  public static void prepareDatabase() throws Exception {
    File f = FileUtils.getFile("src/resources/marccat.sql");
    final File dbdump = new File("src/resources/marccat.sql");
      if (!dbdump.canRead()) {
      fail("Unable to find the database dump.");
    }

    POSTGRES_JDBC_URL = POSTGRES.start("151.1.165.20", 5433, "folio_marccat_test1", DB_USERNAME, DB_PASSWORD);
    POSTGRES.getProcess().ifPresent(pg -> pg.importFromFile(dbdump));
  }

  /**
   * Stops the embedded database instance.
   */
  @AfterClass
  public static void stopDatabase() {
    POSTGRES.stop();
  }


  @Before
  public void setUp() {
    configuration.injectData(POSTGRES_JDBC_URL, DB_USERNAME, DB_PASSWORD);
  }

  /**
   * Retrieves the object associated with that URI.
   *
   * @param uri the target URI.
   * @param responseType the response type.
   * @param <T> the response instance type.
   * @return the object associated with that URI.
   */
  protected  <T> T get(final String uri, Class<T> responseType) {
    final HttpHeaders headers = new HttpHeaders();
    headers.add(Global.OKAPI_TENANT_HEADER_NAME, String.valueOf(System.currentTimeMillis()));

    final ResponseEntity<T> response =  client.getRestTemplate()
      .exchange(
        fromUriString(uri).build().toUri(),
        HttpMethod.GET,
        new HttpEntity<>("parameters", headers),
        responseType);

    if (response.getStatusCode().isError()) {
      throw new RuntimeException(uri + " >> " + response.getStatusCode());
    }
    return response.getBody();
  }

  /**
   * Retrieves the object associated with that URI.
   *
   * @param uri the target URI.
   * @return the object associated with that URI.
   */
  protected void delete(final String uri) {
    final HttpHeaders headers = new HttpHeaders();
    headers.add(Global.OKAPI_TENANT_HEADER_NAME, String.valueOf(System.currentTimeMillis()));

    ResponseEntity<Void> response = client.exchange(
      fromUriString(uri).build().toUri(),
      HttpMethod.DELETE,
      new HttpEntity<>("parameters", headers),
      Void.class);

    if (response.getStatusCode().isError()) {
      throw new RuntimeException(uri + " >> " + response.getStatusCode());
    }
  }

  /**
   * Returns the Pipeline API address endpoint.
   *
   * @param path the specific path service identifier.
   * @return the Pipeline API address endpoint.
   */
  protected String address(final String path) {
    return "http://localhost:" + this.port + "/" + BASE_URI + path + (path.contains("?") ? "&" : "?") + "lang=eng";
  }
}
