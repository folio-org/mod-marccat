package org.folio.marccat.integration;

import io.vertx.core.json.Json;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.resources.domain.DeploymentDescriptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * OkapiService the class of Okapi services manager .
 *
 * @author ctrazza
 * @since 1.0
 */

@Service("OkapiClient")
public class OkapiClient {
  /**
   * The Constant logger.
   */
  private static final Log logger = new Log(OkapiClient.class);

  /**
   * The Client.
   */
  private final RestTemplate client;

  /**
   * The external database.
   */
  @Value("${spring.datasource.external}")
  private String external;

  /**
   * The external host of okapi.
   */
  @Value("${spring.datasource.host}")
  private String host;

  /**
   * The url of the modules.
   */
  public static final String OKAPI_URL_DISCOVERY_MODULES = "/_/discovery/modules";

  /**
   * Client Okapi
   *
   * @param client the HTTP / REST client.
   */
  public OkapiClient(final RestTemplate client) {
    this.client = client;
  }

  /**
   * X-Okapi-Url. Tells the URL where the modules may contact Okapi
   */
  private String okapiUrl = "http://localhost:9130";


  /**
   * Sets the okapi url.
   *
   * @param url the new okapi url
   */
  public void setOkapiUrl(String url) {
     okapiUrl = url;
  }

  /**
   * Gets the okapi url.
   *
   * @return the okapi url
   */
  public String getOkapiUrl() {
    return replaceRemoteHost(okapiUrl);
  }


  /**
   * Builds the url of a module from Okapi.
   *
   * @param moduleDescription the module description.
   * @param subdomain         the sub domain.
   * @return the url of a module otherwise it returns a null value
   */
  public String getModuleUrl(final String moduleDescription, final String subdomain) {
    String moduleUrl = null;
    try {
      logger.debug(okapiUrl + OKAPI_URL_DISCOVERY_MODULES);
      final ResponseEntity <String> response = client.getForEntity(okapiUrl + OKAPI_URL_DISCOVERY_MODULES, String.class);
      final DeploymentDescriptor[] deploymentDescriptorList = Json.decodeValue(response.getBody(), DeploymentDescriptor[].class);
      for (DeploymentDescriptor deployDescriptor : deploymentDescriptorList) {
        if (deployDescriptor.getSrvcId().contains(moduleDescription)) {
          moduleUrl = (deployDescriptor.getUrl() + subdomain);
          moduleUrl = replaceRemoteHost(moduleUrl);
        }
      }
    } catch (RestClientException exception) {
      return moduleUrl;
    }
    return moduleUrl;
  }


  /**
   * Replace the host in the url.
   *
   * @param path the url.
   * @return the url
   */
  public String replaceRemoteHost(String path) {
    final URL url;
    try {
      url = new URL(path);
      final String remoteHost = url.getHost();
      if(external.equals("true"))
        path = path.replace(remoteHost, host);
      logger.debug("URL:" + path);
    } catch (MalformedURLException e) {
      logger.debug("Wrong URL");
    }
    return path;
  }

}
