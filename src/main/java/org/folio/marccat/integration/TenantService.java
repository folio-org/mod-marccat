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
    final List <String> args = new ArrayList <>();
    args.add("/bin/sh");
    args.add("setup-conf.sh");
    args.add(confHost);
    args.add(confPort);
    args.add(tenant);
    args.add("folio.frontside.atcult.it");
    args.add("5433");
    args.add("folio_marccat_test1");
    args.add("amicus");
    args.add("oracle");
    ProcessBuilder builder = new ProcessBuilder(args);
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


}
