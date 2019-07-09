package org.folio.marccat.integration;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.vertx.core.json.Json;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.resources.domain.DeploymentDescriptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.Objects;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.folio.marccat.util.F.safe;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

/**
 * Mod MARCcat configuration subsystem facade.
 *
 * @author cchiama
 * @since 1.0
 */
@Component
public class RemoteConfiguration implements Configuration {

  private static final String BASE_CQUERY = "module==MARCCAT and configName == ";
  private static final String MODULE_CONFIGURATION = "mod-configuration";
  private static final String SUB_PATH_CONFIGURATION = "/configurations/entries";
  private static final Log logger = new Log(RemoteConfiguration.class);
  private static final int LIMIT = 100;
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
    if(getConfigurationUrl() != null)
      endpoint = getConfigurationUrl();
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
   * Builds the url of the configuration module from Okapi.
   *
   * @return the url of the configuration module.
   */
  public String getConfigurationUrl() {
    try {
      final ResponseEntity <String> response = client.getForEntity(Global.OKAPI_URL_DISCOVERY_MODULES, String.class);
      final DeploymentDescriptor[] deploymentDescriptorList = Json.decodeValue(response.getBody(), DeploymentDescriptor[].class);
      for (DeploymentDescriptor deployDescriptor : deploymentDescriptorList) {
        if (deployDescriptor.getSrvcId().contains(MODULE_CONFIGURATION)) {
          logger.info("REMOTE CONFIGURATION URL: " + deployDescriptor.getUrl() + SUB_PATH_CONFIGURATION);
          return (deployDescriptor.getUrl() + SUB_PATH_CONFIGURATION);

        }
      }
    } catch (RestClientException exception) {
      logger.info("LOCAL CONFIGURATION URL : " + endpoint);
      return endpoint;
    }
    return null;
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
      ? BASE_CQUERY + "datasource" + "&limit=" + LIMIT
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
