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
    // Do nothing because deleted a database
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

    String pathSetupConfig  = getClass().getResource("/resources/setup-conf.bat").getPath();
    logger.info(pathSetupConfig);

    final List <String> commands = getCommands(tenant, confPort, confHost, pathSetupConfig);
    ProcessBuilder builder = new ProcessBuilder(commands);
    logger.info(" ENVIRONMENT: " + builder.environment());

    StringBuilder commadsSB = new StringBuilder();
    for (String arg : builder.command()) {
      commadsSB.append(arg + " ");
    }
    logger.info(" COMMAND: " + commadsSB.toString());

    Process process = null;
    try {
      logger.info(" START INIZIALIZE CONFIGURATION");
      process = builder.start();


      BufferedReader reader = null;
      if (process != null) {
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      }
      String line;
      StringBuilder stringBuilder = new StringBuilder();
      if (reader != null) {
        while ((line = reader.readLine()) != null)
          stringBuilder.append(line).append("\n");
      }

      if (process != null)
        try {
          process.waitFor();
        } catch (InterruptedException e) {
          logger.error(" ERROR IN waitFor(): ", e);
        }

      if(process != null)
        logger.info(" EXIT CODE FROM THE PROCESS: " + process.exitValue());

      if (stringBuilder.length() > 0)
        logger.info(" SETUP FILE CONTENT: " + stringBuilder.toString());
      logger.info(" END INIZIALIZE CONFIGURATION");

    } catch (IOException exception) {
      logger.error(Message.MOD_MARCCAT_00013_IO_FAILURE, exception);
    }
    if (process != null) {
      process.destroy();
    }

  }

  private List <String> getCommands(String tenant, String confPort, String confHost, String pathSetupConfig) {
    final List <String> commands = new ArrayList<>();
    commands.add("/bin/sh");
    commands.add(pathSetupConfig);
    commands.add(confHost);
    commands.add(confPort);
    commands.add(tenant);
    commands.add("folio.frontside.atcult.it");
    commands.add("5433");
    commands.add("folio_marccat_test1");
    commands.add("amicus");
    commands.add("oracle");
    return commands;
  }


}
