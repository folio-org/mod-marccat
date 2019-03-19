package org.folio.marccat.config;

import org.folio.marccat.config.TestConfiguration;
import org.folio.marccat.config.log.Global;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

import static org.folio.marccat.ModMarccat.BASE_URI;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;
import static ru.yandex.qatools.embed.postgresql.distribution.Version.Main.V9_6;

/**
 * Supertype layer for all bibliographic test cases.
 *
 * @author agazzarini
 * @since 1.0
 */
public abstract class AbstractUnitTest {

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

    /**
     * Starts the embedded database instance.
     *
     * @throws Exception hopefully never, otherwise the test fails.
     */
    @BeforeClass
    public static void prepareDatabase() throws Exception {
        POSTGRES_JDBC_URL = POSTGRES.start("151.1.165.20", 5433, "folio_marccat_test1", DB_USERNAME, DB_PASSWORD);
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
        headers.add(Global.OKAPI_TENANT_HEADER_NAME,"tnx");

        final ResponseEntity<T> response =  client.getRestTemplate()
                .exchange(
                    fromUriString(uri)
                      .build()
                      .toUri(),
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
        headers.add(Global.OKAPI_TENANT_HEADER_NAME, "tnx");

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
        return "http://151.1.165.20:" + 8080 + "/" + BASE_URI + path + (path.contains("?") ? "&" : "?") + "lang=eng";
    }
}
