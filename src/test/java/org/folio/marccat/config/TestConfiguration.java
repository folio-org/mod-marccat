package org.folio.marccat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.folio.marccat.integration.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * This is a dummy configuration object used only for testing purposes.
 *
 * @author cchiama
 * @since 1.0
 */
@Component
@Profile("test")
public class TestConfiguration implements Configuration {

  private String jdbcUrl;
  private String username;
  private String password;

  private static final String CONFIG_NAME = "configName";
  private static final String DATASOURCE = "datasource";
  private static final String VALUE = "value";

  @Override
  public ObjectNode attributes(final String tenant, final boolean withDatasource, final String... configurationSets) {
    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode cfg = mapper.createObjectNode();

    final ArrayNode configs = cfg.putArray("configs");
    configs.add(mapper.createObjectNode().put(CONFIG_NAME, DATASOURCE).put("code","user").put(VALUE, username));
    configs.add(mapper.createObjectNode().put(CONFIG_NAME, DATASOURCE).put("code","password").put(VALUE,password));
    configs.add(mapper.createObjectNode().put(CONFIG_NAME, DATASOURCE).put("code","url").put(VALUE, jdbcUrl));

    return cfg;
  }

  /**
   * Injects the runtime configuration data into this object.
   *
   * @param jdbcUrl the JDBC URL.
   * @param username the database username.
   * @param password the database password.
   */
  public void injectData(final String jdbcUrl, final String username, final String password) {
    this.jdbcUrl = jdbcUrl;
    this.username = username;
    this.password = password;
  }
}
