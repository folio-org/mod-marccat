package org.folio.marccat.integration;


import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * TenantService  the class for Tenants management.
 *
 * @author ctrazza
 * @since 1.0
 */
@Service("TenantService")
public class TenantService {

  /** The Constant logger. */
  private static final Log logger = new Log(TenantService.class);

  /** The url. */
  @Value("${spring.datasource.url}")
  private String url;

  /** The username. */
  @Value("${spring.datasource.username}")
  private String username;

  /** The password. */
  @Value("${spring.datasource.password}")
  private String password;

  /** The driver. */
  @Value("${spring.datasource.driverClassName}")
  private String driver;

  /** The resource loader. */
  @Autowired
  private ResourceLoader resourceLoader;

  /** The remote configuration. */
  @Autowired
  private RemoteConfiguration remoteConfiguration;

  /**
   * Creates the tenant.
   *
   * @param tenant the tenant
   * @throws SQLException the SQL exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void createTenant(String tenant) throws SQLException, IOException {
    initializeConfiguration(tenant);
  }

  /**
   * Delete tenant.
   *
   * @param tenant the tenant
   * @throws SQLException the SQL exception
   */
  public void deleteTenant(String tenant) throws SQLException {
    // Do nothing because it removes the database
  }

  /**
   * Initialize configuration.
   *
   * @param tenant the tenant
   */
  private void initializeConfiguration(final String tenant) {
    final String configurationUrl = remoteConfiguration.getConfigurationUrl();
    int index = configurationUrl.lastIndexOf(':') + 1;
    final String confPort = configurationUrl.substring(index, index + 4);
    final String confHost = configurationUrl.substring(configurationUrl.indexOf("//") + 2, configurationUrl.lastIndexOf(':'));
    final List <String> commands = getCommands(tenant, confPort, confHost);
    ProcessBuilder builder = new ProcessBuilder(commands);
    if (builder.directory() != null)
      logger.info("DIRECTORY FILE: " + builder.directory());
    Process process = null;
    try {
      logger.info("START INIZIALIZE CONFIGURATION");
      process = builder.start();
      logger.info("END INIZIALIZE CONFIGURATION");
    } catch (IOException exception) {
      logger.error(Message.MOD_MARCCAT_00013_IO_FAILURE, exception);
    }
    if (process != null) {
      process.destroy();
    }

  }

  private List <String> getCommands(String tenant, String confPort, String confHost) {
    final List <String> commands = new ArrayList<>();
    commands.add("sh");
    commands.add("setup-conf.sh");
    commands.add(confPort);
    commands.add(tenant);
    commands.add(confHost);
    commands.add("5433");
    commands.add("folio_marccat_test1");
    commands.add("amicus");
    commands.add("oracle");
    return commands;
  }


}
