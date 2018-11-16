package org.folio.marccat.integration;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.folio.marccat.config.Global;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.folio.marccat.util.F.safe;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

/**
 * Mod Cataloging configuration subsystem facade.
 *
 * @author cchiama
 * @since 1.0
 */
@Component
@Profile({"!test"})
public class RemoteConfiguration implements Configuration {

  private final static String BASE_CQUERY = "module==MARCCAT and configName == ";
  private final static int LIMIT = 100;
  private final RestTemplate client;

  @Value("${configuration.endpoint}")
  private String endpoint;

  /**
   * Builds a new configuration with the given http client.
   *
   * @param client the HTTP / REST client.
   */
  public RemoteConfiguration(final RestTemplate client) {
    this.client = client;
  }

  @Override
  public ObjectNode attributes(final String tenant, final boolean withDatasource, final String... configurationSets) {
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
  private String cQuery(boolean withDatasource, final String... configurationsSets) {
    final String[] values = safe(configurationsSets);
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
          ")&limit=" + LIMIT));
  }
}
