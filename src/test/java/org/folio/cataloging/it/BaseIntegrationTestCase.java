package org.folio.cataloging.it;

import org.folio.cataloging.Global;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

import java.io.File;

import static junit.framework.TestCase.fail;
import static org.folio.cataloging.ModCataloging.BASE_URI;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;
import static ru.yandex.qatools.embed.postgresql.distribution.Version.Main.V9_6;

/**
 * Supertype layer for all integration test cases.
 *
 * @author agazzarini
 * @since 1.0
 */
public abstract class BaseIntegrationTestCase {
    @LocalServerPort
    protected int port;

    @Value("${local.management.port}")
    protected int mgt;

    @Autowired
    protected TestRestTemplate client;

    @Autowired
    protected TestConfiguration configuration;

    protected final static String DB_USERNAME = "amicus";
    protected final static String DB_PASSWORD = "oracle";

    private final static EmbeddedPostgres POSTGRES = new EmbeddedPostgres(V9_6);
    private static String POSTGRES_JDBC_URL;
    /**
     * Starts the embedded database instance.
     *
     * @throws Exception hopefully never, otherwise the test fails.
     */
    @BeforeClass
    public static void prepareDatabase() throws Exception {
        final File dbdump = new File("src/test/resources/marccat.sql");
        if (!dbdump.canRead()) {
            fail("Unable to find the database dump.");
        }

        POSTGRES_JDBC_URL = POSTGRES.start("localhost", 5432, "dbName", DB_USERNAME, DB_PASSWORD);
        POSTGRES.getProcess().ifPresent(pg -> pg.importFromFile(dbdump));
    }

    /**
     * Stops the embedded database instance.
     */
    @AfterClass
    public static void stopDatabase() {
        POSTGRES.stop();
    }

    /**
     * Initialises the configuration.
     * NOTE THAT IF THE TEST CASE PROVIDES A @Before METHOD, THE FIRST LINE OF CODE MUST BE super.setUp();
     *
     * @see TemplateManagementTestCase#setUp()
     */
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
     * @param id the specific service identifier.
     * @return the Pipeline API address endpoint.
     */
    protected String address(final String id) {
        return "http://localhost:" + this.port + "/" + BASE_URI + id + (id.contains("?") ? "&" : "?") + "lang=eng";
    }
}