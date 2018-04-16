package org.folio.cataloging.integration;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.folio.cataloging.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.folio.cataloging.F.safe;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

/**
 * Mod Cataloging configuration subsystem facade.
 *
 * @author agazzarini
 * @since 1.0
 */
@Component
public class Configuration {

    final static String BASE_CQUERY = "module==CATALOGING and configName == ";

    @Value("${configuration.client:http://192.168.0.158:8085/configurations/entries}")
    private String endpoint;

    @Autowired
    private RestTemplate client;

    /**
     * Returns the datasource configuration associated with the given tenant.
     *
     * @param tenant the tenant identifier.
     * @return the datasource configuration associated with the given tenant.
     */
    public ObjectNode datasourceConfiguration(final String tenant) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(Global.OKAPI_TENANT_HEADER_NAME, tenant);

        return client.exchange(
                fromUriString(endpoint)
                    .queryParam(BASE_CQUERY + "datasource")
                    .build()
                    .toUri(),
                HttpMethod.GET,
                new HttpEntity<>("parameters", headers),
                ObjectNode.class)
                .getBody();
    }

    /**
     * Returns the configuration associated with the given tenant and belonging to the input sets (groups).
     *
     * @param tenant the tenant identifier.
     * @param configurationSets the attributes group(s).
     * @return the configuration associated with the given tenant and belonging to the input sets (groups).
     */
    public ObjectNode attributes(final String tenant, final boolean withDatasource, final String ... configurationSets) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(Global.OKAPI_TENANT_HEADER_NAME, tenant);

        return client.exchange(
                fromUriString(endpoint)
                        .queryParam(cQuery(withDatasource, safe(configurationSets)))
                        .build()
                        .toUri(),
                HttpMethod.GET,
                new HttpEntity<>("parameters", headers),
                ObjectNode.class)
                .getBody();
    }

    /**
     * Returns the selection criteria that will be used by the current service for gathering the required configuration.
     *
     * @param configurationsSets the configuration groups.
     * @return the selection criteria that will be used by the current service for gathering the required configuration.
     */
    static String cQuery(boolean withDatasource, final String ... configurationsSets) {
        final String [] values = safe(configurationsSets);
        return (values.length == 0 && withDatasource)
                ? BASE_CQUERY + "datasource"
                : BASE_CQUERY +
                    stream(values)
                            .filter(Objects::nonNull)
                            .collect(joining(
                                    " or ",
                                    values.length != 0
                                            ? withDatasource ? "(datasource or " : "("
                                            : "",
                                    ")"));
    }
}
