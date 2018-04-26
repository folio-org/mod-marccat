package org.folio.cataloging.it;

import org.folio.cataloging.resources.domain.RecordTemplateCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.folio.cataloging.ModCataloging.BASE_URI;

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
        return client.getForEntity(address("record-templates"), responseType).getBody();
    }

    /**
     * Returns the Pipeline API address endpoint.
     *
     * @param id the specific service identifier.
     * @return the Pipeline API address endpoint.
     */
    protected String address(final String id) {
        return "http://localhost:" + this.port + "/" + BASE_URI + id;
    }
}
