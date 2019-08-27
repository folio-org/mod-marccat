package org.folio.marccat.integration;

import io.vertx.core.json.Json;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.resources.domain.DeploymentDescriptor;
import org.folio.marccat.resources.domain.EnvEntry;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;


/**
 * OkapiService the class of Okapi services manager .
 *
 * @author ctrazza
 * @since 1.0
 */

@Service("OkapiService")
public class OkapiService {
  /**
   * The Constant logger.
   */
  private static final Log logger = new Log(OkapiService.class);
  /**
   * The Client.
   */
  private final RestTemplate client;
  /**
   * The url of the environment.
   */
  public static final String OKAPI_URL_ENVIRONMENT = "http://localhost:9130/_/env";
  /**
   * The url of the modules.
   */
  public static final String OKAPI_URL_DISCOVERY_MODULES = "http://localhost:9130/_/discovery/modules";

  /**
   * Client Okapi
   *
   * @param client the HTTP / REST client.
   */
  public OkapiService(final RestTemplate client) {
    this.client = client;
  }

  /**
   * Returns a HashMap of environment variables.
   *
   * @return environment variables .
   */
  public HashMap <String, String> getEnvironments() {
    final ResponseEntity <String> response = client.getForEntity(OKAPI_URL_ENVIRONMENT, String.class);
    final EnvEntry[] env = Json.decodeValue(response.getBody(), EnvEntry[].class);
    final HashMap <String, String> entries = new HashMap <>();
    if (env != null) {
      for (EnvEntry e : env) {
        entries.put(e.getName(), e.getValue());
        logger.info("Environment variables");
        logger.info("Name: " + e.getName());
        logger.info("Value: " + e.getValue());
      }
    }
    return entries;
  }

  /**
   * Builds the url of the module from Okapi.
   *
   * @param moduleDescription the module description.
   * @param subdomain         the sub domain.
   * @return the url of the configuration module.
   */
  public String getModuleUrl(final String moduleDescription, final String subdomain) {
    final ResponseEntity <String> response = client.getForEntity(OKAPI_URL_DISCOVERY_MODULES, String.class);
    final DeploymentDescriptor[] deploymentDescriptorList = Json.decodeValue(response.getBody(), DeploymentDescriptor[].class);
    for (DeploymentDescriptor deployDescriptor : deploymentDescriptorList) {
      if (deployDescriptor.getSrvcId().contains(moduleDescription)) {
        return (deployDescriptor.getUrl() + subdomain);
      }
    }
    return null;
  }
}
