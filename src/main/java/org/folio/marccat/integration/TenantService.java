package org.folio.marccat.integration;


import org.apache.commons.io.IOUtils;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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


  /** The username. */
  @Value("${spring.datasource.username}")
  private String username;

  /** The password. */
  @Value("${spring.datasource.password}")
  private String password;

  /** The platform. */
  @Value("${spring.datasource.platform}")
  private String platform;

  /** The port. */
  @Value("${spring.datasource.port}")
  private String port;

  /** The host. */
  @Value("${spring.datasource.host}")

  private String host;
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
    String pathSetupConfig = null;
    final String configurationUrl = remoteConfiguration.getConfigurationUrl();
    final Map<String, String>  mapConfigurations  = getConfigurations(configurationUrl);
    final File file = getResourceAsFile("/setup-conf.sh");
    if (file != null)
      pathSetupConfig = file.getAbsolutePath();
    final List <String> commands = getCommands(tenant, mapConfigurations, pathSetupConfig);
    final ProcessBuilder builder = new ProcessBuilder(commands);
    Process process = null;
    try {
      logger.info(" ENABLE TENANT - START");
      process = builder.start();
      logger.info(" ENABLE TENANT - END");

    } catch (IOException exception) {
      logger.error(Message.MOD_MARCCAT_00013_IO_FAILURE, exception);
    }
    if (process != null) {
      process.destroy();
    }
  }

  /**
   * Gets the commands.
   *
   * @param tenant the tenant
   * @param mapConfigurations the map configurations
   * @param pathSetupConfig the path setup config
   * @return the commands
   */
  private List <String> getCommands(final String tenant, final Map<String, String> mapConfigurations, final String pathSetupConfig) {
    final List <String> commands = new ArrayList<>();
    commands.add("sh");
    commands.add(pathSetupConfig);
    commands.add(mapConfigurations.get("hostConf"));
    commands.add(mapConfigurations.get("portConf"));
    commands.add(tenant);
    commands.add(host);
    commands.add(port);
    commands.add(platform);
    commands.add(username);
    commands.add(password);
    return commands;
  }

  /**
   * Gets the resource as file.
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
  private Map<String, String> getConfigurations(final String configurationUrl){
    final Map<String, String> configurations = new HashMap<>();
    final int index = configurationUrl.lastIndexOf(':') + 1;
    final String hostConf = configurationUrl.substring(configurationUrl.indexOf("//") + 2, configurationUrl.lastIndexOf(':'));
    final String portConf= configurationUrl.substring(index, index + 4);
    configurations.put("hostConf",hostConf);
    configurations.put("portConf",portConf);
    return configurations;
  }

}
