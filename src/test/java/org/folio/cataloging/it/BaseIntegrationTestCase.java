package org.folio.cataloging.it;

import org.folio.cataloging.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.folio.cataloging.ModCataloging.BASE_URI;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

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
