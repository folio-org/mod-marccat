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
 * @author cctrazza
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

  }

  /**
   * Initialize configuration.
   *
   * @param tenant the tenant
   */
  private void initializeConfiguration(final String tenant) {
    final String configurationUrl = remoteConfiguration.getConfigurationUrl();
    final List <String> args = new ArrayList <String>();
    args.add("sh setup-conf.sh");
    args.add(tenant);
    args.add(configurationUrl);
    args.add("5433");//DB
    args.add("folio_marccat_test1");//DB
    args.add("amicus");//DB
    args.add("oracle");//DB
    ProcessBuilder pb = new ProcessBuilder(args);
    if (pb.directory() != null)
      logger.info("DIRECTORY FILE: " + pb.directory());
    Process process = null;
    try {
      process = pb.start();

    } catch (IOException exception) {
      logger.error(Message.MOD_MARCCAT_00013_IO_FAILURE, exception);
    }
    if (process != null) {
      process.destroy();
    }

  }


}
