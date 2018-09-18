package org.folio.cataloging.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.folio.cataloging.integration.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * This is a dummy configuration object used only for testing purposes.
 *
 * @author agazzarini
 * @since 1.0
 */
@Component
@Profile("test")
public class TestConfiguration implements Configuration {
  private String jdbcUrl;
  private String username;
  private String password;

  @Override
  public ObjectNode attributes(final String tenant, final boolean withDatasource, final String... configurationSets) {
    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode cfg = mapper.createObjectNode();

    final ArrayNode configs = cfg.putArray("configs");
    configs.add(mapper.createObjectNode().put("configName", "datasource").put("code","user").put("value", username));
    configs.add(mapper.createObjectNode().put("configName", "datasource").put("code","password").put("value",password));
    configs.add(mapper.createObjectNode().put("configName", "datasource").put("code","url").put("value", jdbcUrl));

    return cfg;
  }

  /**
   * Injects the runtime configuration data into this object.
   *
   * @param jdbcUrl the JDBC URL.
   * @param username the database username.
   * @param password the database password.
   */
  void injectData(final String jdbcUrl, final String username, final String password) {
    this.jdbcUrl = jdbcUrl;
    this.username = username;
    this.password = password;
  }
}
