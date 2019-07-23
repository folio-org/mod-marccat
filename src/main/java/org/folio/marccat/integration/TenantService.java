package org.folio.marccat.integration;


import org.apache.commons.io.IOUtils;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

/**
 * TenantService  the class for Tenants management.
 *
 * @author ctrazza
 * @since 1.0
 */
@Service("TenantService")
public class TenantService {

  /**
   * The Constant logger.
   */
  private static final Log logger = new Log(TenantService.class);

  /**
   * The Constant BIN_SH.
   */
  public static final String BIN_SH = "/bin/sh";

  /**
   * The remote configuration.
   */
  @Autowired
  private RemoteConfiguration configuration;

  /**
   * The username.
   */
  @Value("${spring.datasource.username}")
  private String username;

  /**
   * Creates the tenant.
   *
   * @param tenant the tenant
   * @throws SQLException the SQL exception
   * @throws IOException  Signals that an I/O exception has occurred.
   */
  public void createTenant(final String tenant) throws SQLException, IOException {
    initializeDatabase(tenant, username);
    initializeConfiguration(tenant, username);
  }

  /**
   * Delete tenant.
   *
   * @param tenant the tenant
   * @throws SQLException the SQL exception
   */
  public void deleteTenant(final String tenant) throws SQLException {
    // Do nothing because deleted a database
  }


  /**
   * Initialize configuration.
   *
   * @param tenant the tenant
   * @param user   the user
   */
  private void initializeConfiguration(final String tenant, final String user) {
    final String configurationUrl = configuration.getConfigurationUrl();
    final Map <String, String> mapConfigurations = getConfigurations(configurationUrl);
    final String pathScript = getPathScript("/database-setup/setup-conf.sh");
    final List <String> commands = Arrays.asList(BIN_SH, pathScript, mapConfigurations.get("host"),
      mapConfigurations.get("port"), tenant, "", "", "", user, "");
    executeScript(commands, " ENABLE TENANT ");
  }

  /**
   * Initialize database.
   *
   * @param tenant the tenant
   * @param user   the user
   */
  private void initializeDatabase(final String tenant, final String user) {
    final String databaseName = tenant + "_" + Global.BASE_URI;
    final String userApp = Global.BASE_URI;
    createRole(user);
    createDatabase(databaseName, user, userApp);
    createObjects(databaseName, userApp);

  }

  /**
   * Creates the role.
   *
   * @param user the user
   */
  private void createRole(final String user) {
    final String pathScript = getPathScript("/database-setup/create-marccat-role.sh");
    final List <String> commands = Arrays.asList(BIN_SH, pathScript, "", "", "", "", user, "");
    executeScript(commands, " CREATE ROLE");
  }

  /**
   * Creates the database.
   *
   * @param databaseName the database name
   * @param user         the user
   * @param userApp      the user app
   */
  private void createDatabase(final String databaseName, final String user, final String userApp) {
    final String pathScript = getPathScript("/database-setup/create-db.sh");
    final List <String> commands = Arrays.asList(BIN_SH, pathScript, databaseName, userApp, "", "", user, "");
    executeScript(commands, " CREATE DATABASE");
  }

  /**
   * Creates the objects.
   *
   * @param databaseName the database name
   * @param userApp      the user app
   */
  private void createObjects(final String databaseName, final String userApp) {
    final String pathScript = getPathScript("/database-setup/create-objects.sh");
    final List <String> commands = Arrays.asList(BIN_SH, pathScript, "", databaseName, userApp);
    executeScript(commands, " CREATE OBJECTS");
  }

  /**
   * Execute script.
   *
   * @param commands   the commands
   * @param messageLog the message log
   */
  private void executeScript(final List <String> commands, final String messageLog) {
    final ProcessBuilder builder = new ProcessBuilder(commands);
    Process process = null;
    try {
      logger.info(messageLog + " - START");
      process = builder.start();
      processWait(process);
      logger.info(messageLog + " - END");

    } catch (IOException exception) {
      logger.error(Message.MOD_MARCCAT_00013_IO_FAILURE, exception);
    }
    if (process != null) {
      process.destroy();
    }
  }

  /**
   * Causes the current thread to wait
   * until the end of the process.
   *
   * @param process the process
   */
  private void processWait(final Process process) {
    try {
      final int exitCode = process.waitFor();
      logger.info(" EXIT CODE %d", exitCode);
    } catch (InterruptedException e) {
      logger.error(Message.MOD_MARCCAT_00033_PROCESS_FAILURE, e);
      Thread.currentThread().interrupt();
    }
  }

  /**
   * Gets the path script.
   *
   * @param fileName the file name
   * @return the path script
   */
  private String getPathScript(final String fileName) {
    final File file = getResourceAsFile(fileName);
    return (file != null) ? file.getAbsolutePath() : null;
  }


  /**
   * Gets the resource as a temporary file.
   *
   * @param resourcePath the resource path
   * @return the resource as file
   */
  private File getResourceAsFile(final String resourcePath) {
    try {
      final InputStream inputStream = getClass().getResourceAsStream(resourcePath);
      if (inputStream == null) {
        return null;
      }
      final File tempFile = File.createTempFile(String.valueOf(inputStream.hashCode()), ".tmp");
      tempFile.deleteOnExit();
      IOUtils.copy(inputStream, new FileOutputStream(tempFile));
      return tempFile;
    } catch (IOException exception) {
      logger.error(Message.MOD_MARCCAT_00013_IO_FAILURE, exception);
      return null;
    }
  }

  /**
   * Gets the configurations.
   *
   * @param configurationUrl the configuration url
   * @return the configurations
   */
  private Map <String, String> getConfigurations(final String configurationUrl) {
    final Map <String, String> configurations = new HashMap <>();
    final int index = configurationUrl.lastIndexOf(':') + 1;
    final String host = configurationUrl.substring(configurationUrl.indexOf("//") + 2, configurationUrl.lastIndexOf(':'));
    final String port = configurationUrl.substring(index, index + 4);
    configurations.put("host", host);
    configurations.put("port", port);
    return configurations;
  }


}
