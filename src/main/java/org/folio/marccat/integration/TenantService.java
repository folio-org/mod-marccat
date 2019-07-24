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
   * The port.
   */
  @Value("${spring.datasource.port}")
  private String port;

  /**
   * The host.
   */
  @Value("${spring.datasource.host}")
  private String host;

  /**
   * The admin user.
   */
  @Value("${admin.username}")
  private String adminUser;

  /**
   * The admin password.
   */
  @Value("${admin.password}")
  private String adminPassword;

  /**
   * The marccat user.
   */
  @Value("${marccat.username}")
  private String marccatUser;

  /**
   * The marccat password .
   */
  @Value("${marccat.database.suffix}")
  private String marccatSuffix;

  /**
   * The marccat password .
   */
  @Value("${marccat.password}")
  private String marccatPassword;

  /**
   * Creates the tenant.
   *
   * @param tenant the tenant
   * @throws SQLException the SQL exception
   * @throws IOException  Signals that an I/O exception has occurred.
   */
  public void createTenant(final String tenant) throws SQLException, IOException {
    logger.info(" ENABLE TENANT"+  " - START");
    initializeDatabase(tenant);
    initializeConfiguration(tenant, username);
    logger.info(" ENABLE TENANT"+  " - END");
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
    final String pathScript = getPathScript("/database-setup/setup-conf.sh", tenant);
    logger.info(pathScript);
    final List <String> commands = Arrays.asList(BIN_SH, pathScript, mapConfigurations.get("host"),
      mapConfigurations.get("port"), tenant, "", "", "", user, "");
    executeScript(commands, "");
  }

  /**
   * Initialize database.
   *
   * @param tenant the tenant
   */
  private void initializeDatabase(final String tenant) {
    final String databaseName =  tenant + marccatSuffix;
    createRole(databaseName);
    createDatabase(databaseName);
    createObjects(databaseName);

  }

  /**
   * Creates the role.
   *
   * @param databaseName the database name
   */
  private void createRole(final String databaseName) {
    final String pathScript = getPathScript("/database-setup/create-marccat-role.sql", databaseName);
    logger.info(" ROLE PATH:" + pathScript);
    final String command =  String.format("psql -h %s -p %s -U %s -f %s", host, port, adminUser, pathScript);
    final List<String> commands = Arrays.asList(command.split("\\s+"));
    StringBuilder commadsSB = new StringBuilder();
    for (String arg : commands) {
      commadsSB.append(arg + " ");
    }
    logger.info(" ROLE COMMANDS: " + commadsSB.toString());
    executeScript(commands, " CREATE ROLE");
  }

  /**
   * Creates the database.
   *
   * @param databaseName the database name
   */
  private void createDatabase(final String databaseName) {
    final String pathScript = getPathScript("/database-setup/create-db.sql", databaseName);
    logger.info(" DATABASE PATH:" +  pathScript);
    final String command =  String.format("psql -h %s -p %s -U %s -f %s", host, port, adminUser, pathScript);
    final List<String> commands = Arrays.asList(command.split("\\s+"));
    StringBuilder commadsSB = new StringBuilder();
    for (String arg : commands) {
      commadsSB.append(arg + " ");
    }
    logger.info(" DATABASE COMMANDS: " + commadsSB.toString());

    executeScript(commands, " CREATE DATABASE");
  }

  /**
   * Creates the objects.
   *
   * @param databaseName the database name
   */
  private void createObjects(final String databaseName) {
    final String pathScript = getPathScript("/database-setup/create-objects.sql", databaseName);
    logger.info(" OBJECTS PATH:" + pathScript);
    final String command =  String.format("psql -h %s -p %s -U %s -f %s", host, port, adminUser, pathScript);
    final List<String> commands = Arrays.asList(command.split("\\s+"));

    StringBuilder commadsSB = new StringBuilder();
    for (String arg : commands) {
      commadsSB.append(arg + " ");
    }
    logger.info(" OBJECTS COMMANDS: " + commadsSB.toString());

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
    Map<String, String> mp = builder.environment();
    mp.put("PGPASSWORD", adminPassword);
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
  private String getPathScript(final String fileName, final String databaseName) {
    final File file = getResourceAsFile(fileName, databaseName);
    return (file != null) ? file.getAbsolutePath() : null;
  }


  /**
   * Gets the resource as a temporary file.
   *
   * @param resourcePath the resource path
   * @return the resource as file
   */
  private File getResourceAsFile(final String resourcePath, final String databaseName) {
    try {
      final InputStream inputStream = getClass().getResourceAsStream(resourcePath);
      if (inputStream == null) {
        return null;
      }
      final File tempFile = File.createTempFile(String.valueOf(inputStream.hashCode()), ".tmp");
      tempFile.deleteOnExit();
      String stringInputStream = IOUtils.toString(inputStream, "UTF-8");
      stringInputStream = stringInputStream.replaceAll("user_name", marccatUser);
      stringInputStream = stringInputStream.replaceAll("password", marccatPassword);
      stringInputStream = stringInputStream.replaceAll("database_name", databaseName);
      InputStream toInputStream = IOUtils.toInputStream(stringInputStream, "UTF-8");
      IOUtils.copy(toInputStream, new FileOutputStream(tempFile));
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
