package org.folio.marccat.integration;


import org.apache.commons.io.IOUtils;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.*;
import java.sql.*;
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
  public static final String DATABASE_SETUP = "/database-setup/";
  public static final String UTF_8 = "UTF-8";


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
   * The database patch.
   */
  @Value("${patch.database}")
  private String patchDatabase;

  /**
   * The database procedure.
   */
  @Value("${patch.procedure}")
  private String patchProcedure;

   /**
   * Creates the tenant.
   *
   * @param tenant the tenant
   * @throws SQLException the SQL exception
   * @throws IOException  Signals that an I/O exception has occurred.
   */
  public void createTenant(final String tenant) throws SQLException, IOException {
    logger.debug("Enable tenant" + " - Start");
    boolean schemaNotExist = initializeDatabase(tenant);
    if(schemaNotExist)
      initializeConfiguration(tenant, username);
    logger.info("Enable tenant" + " - End");
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
    final String pathScript = getPathScript(DATABASE_SETUP + "setup-conf.sh", tenant, false);
    final List <String> commands = Arrays.asList(BIN_SH, pathScript, mapConfigurations.get("host"),
      mapConfigurations.get("port"), tenant, "", "", "", user, "");
    executeScript(commands, "", adminPassword);
  }

  /**
   * Initialize database.
   *
   * @param tenant the tenant
   */
  private boolean initializeDatabase(final String tenant) throws SQLException {
    final String databaseName =  tenant + marccatSuffix;
    createRole(databaseName);
    createDatabase(databaseName);
    boolean schemaNotExist = schemaExists(databaseName);
    if (schemaNotExist) {
      createObjects(databaseName);
      createTemplate(databaseName);
    }
    executePatch(databaseName);
    return schemaNotExist;
  }

  /**
   * Creates the role.
   *
   * @param databaseName the database name
   */
  private void createRole(final String databaseName) {
    final String pathScript = getPathScript(DATABASE_SETUP + "create-marccat-role.sql", databaseName, true);
    final String command =  String.format("psql -h %s -p %s -U %s -f %s", host, port, adminUser, pathScript);
    final List<String> commands = Arrays.asList(command.split("\\s+"));
    executeScript(commands, "Create role", adminPassword);
  }

  /**
   * Creates the database.
   *
   * @param databaseName the database name
   */
  private void createDatabase(final String databaseName) {
    final String pathScript = getPathScript(DATABASE_SETUP + "create-db.sql", databaseName, true);
    final String command =  String.format("psql -h %s -p %s -U %s -f %s", host, port, adminUser, pathScript);
    final List<String> commands = Arrays.asList(command.split("\\s+"));
    executeScript(commands, "Create database", adminPassword);
  }

  /**
   * Creates the objects.
   *
   * @param databaseName the database name
   */
  private void createObjects(final String databaseName) {
    final String pathScript = getPathScript(DATABASE_SETUP + "create-objects.sql", databaseName, false);
    final String command =  String.format("psql -h %s -p %s -U %s -d %s -v user_name=%s -f %s", host, port, adminUser, databaseName, marccatUser, pathScript);
    final List<String> commands = Arrays.asList(command.split("\\s+"));
    executeScript(commands, "Create objects", adminPassword);
  }

  /**
   * Creates the template.
   *
   * @param databaseName the database name
   */
  private void createTemplate(final String databaseName) {
    final String pathScript = getPathScript(DATABASE_SETUP + "init_template.sql", databaseName, false);
    final String command =  String.format("psql -h %s -p %s -U %s -d %s -f %s", host, port, marccatUser, databaseName, pathScript);
    final List<String> commands = Arrays.asList(command.split("\\s+"));

    StringBuilder commadsSB = new StringBuilder();
    for (String arg : commands) {
      commadsSB.append(arg + " ");
    }
    logger.info(" Template commands: " + commadsSB.toString());

    executeScript(commands, "Create template", marccatPassword);
  }

  /**
   * Executes the patch.
   *
   * @param databaseName the database name
   */
  private void executePatch(final String databaseName) {

    try {
      final InputStream inputStream = getClass().getResourceAsStream(patchDatabase + "/env.conf");
      final List <String> ls = IOUtils.readLines(inputStream, "utf-8");
      final String patchRel = ls.get(1).substring(ls.get(1).indexOf("patch_rel_nbr="));
      final String patchSp = ls.get(2).substring(ls.get(2).indexOf("patch_sp_nbr="));
      final String patchComp = ls.get(3).substring(ls.get(3).indexOf("patch_comp_typ="));
      final File file = getResourceAsFileWithChild(patchDatabase, "/install-patch.sql", databaseName);
      String pathScript = null;
      if(file != null) {
        pathScript = file.getAbsolutePath();
        logger.info("Path patch: " + pathScript);
      }
      final String command = String.format("psql -h %s -p %s -U %s -d %s -v user_name=%s -v %s -v %s -v %s -f %s", host, port, marccatUser, databaseName, marccatUser, patchRel, patchSp, patchComp, pathScript);
      final List <String> commands = Arrays.asList(command.split("\\s+"));
      StringBuilder commadsSB = new StringBuilder();
      for (String arg : commands) {
        commadsSB.append(arg + " ");
      }
      logger.info("Patch commands: " + commadsSB.toString());

      executeScript(commands, "Execute patch", marccatPassword);
    } catch (IOException exception) {
      logger.error(Message.MOD_MARCCAT_00013_IO_FAILURE, exception);
    }

  }

  /**
   * Execute script.
   *
   * @param commands   the commands
   * @param messageLog the message log
   */
  private void executeScript(final List <String> commands, final String messageLog, final String pgPassword) {
    final ProcessBuilder builder = new ProcessBuilder(commands);
    final Map<String, String> mp = builder.environment();
    mp.put("PGPASSWORD", pgPassword);
    logger.info("PGPASSWORD : "+ pgPassword);
    Process process = null;
    try {
      logger.info(messageLog + " - Start");
      process = builder.start();
      processWait(process);
      logger.info(messageLog + " - End");

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
      logger.info("Exit code %d :", exitCode);
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
  private String getPathScript(final String fileName, final String databaseName, final boolean isReplace) {
    final File file = getResourceAsFile(fileName, databaseName, isReplace);
    return (file != null) ? file.getAbsolutePath() : null;
  }


  /**
   * Gets the resource as a temporary file.
   *
   * @param resourcePath the resource path
   * @return the resource as file
   */
  private File getResourceAsFile(final String resourcePath, final String databaseName, final boolean isReplaceVariables) {
    try {
      final InputStream inputStream = getClass().getResourceAsStream(resourcePath);
      if (inputStream == null) {
        return null;
      }
      final File tempFile = File.createTempFile(String.valueOf(inputStream.hashCode()), ".tmp");
      tempFile.deleteOnExit();
      String stringInputStream = IOUtils.toString(inputStream, UTF_8);
      if(isReplaceVariables) {
        stringInputStream = stringInputStream.replaceAll("user_name", marccatUser);
        stringInputStream = stringInputStream.replaceAll("password", marccatPassword);
        stringInputStream = stringInputStream.replaceAll("database_name", databaseName);
      }
      final InputStream toInputStream = IOUtils.toInputStream(stringInputStream, UTF_8);
      IOUtils.copy(toInputStream, new FileOutputStream(tempFile));
      return tempFile;
    } catch (IOException exception) {
      logger.error(Message.MOD_MARCCAT_00013_IO_FAILURE, exception);
      return null;
    }
  }

  /**
   * Gets the resource as a temporary file that contains multiple child files
   *
   * @param resourcePath the resource path
   * @param fileName the f patile name of the parent
   * @return the resource as file
   */
  private File getResourceAsFileWithChild(final String resourcePath, final String fileName, final String databaseName) {
    try {
      final InputStream inputStream = getClass().getResourceAsStream(resourcePath + fileName);
      if (inputStream == null) {
        return null;
      }
      final File tempFile = File.createTempFile(String.valueOf(inputStream.hashCode()), ".tmp");
      tempFile.deleteOnExit();
      String stringInputStream = IOUtils.toString(inputStream, UTF_8);
      final List <String> ls = IOUtils.readLines(getClass().getResourceAsStream(resourcePath + fileName), "utf-8");
      for (String line : ls) {
        if (line.startsWith("\\ir ")) {
          final String fileNameChild = line.substring(4);
          final File tempChildFile = getResourceAsFile(resourcePath +"/"+ fileNameChild, databaseName, false);
            if(tempChildFile != null) {
              stringInputStream = stringInputStream.replaceAll(fileNameChild, tempChildFile.getName());
            }
        }
      }
      final InputStream toInputStream = IOUtils.toInputStream(stringInputStream, UTF_8);
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
    final String hostConf = configurationUrl.substring(configurationUrl.indexOf("//") + 2, configurationUrl.lastIndexOf(':'));
    final String portConf = configurationUrl.substring(index, index + 4);
    configurations.put("host", hostConf);
    configurations.put("port", portConf);
    return configurations;
  }

  /**
   * Return true if schema exists.
   *
   * @param databaseName the database name
   * @return true if schema exists
   */
  private boolean schemaExists(final String databaseName) throws SQLException {
    final String querySchema = "select count(*) from pg_catalog.pg_namespace where nspname in ('amicus', 'olisuite')";
    try (Connection connection = getConnection(databaseName);
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(querySchema);) {
      resultSet.next();
      final int count  = resultSet.getInt(1);
      if(count != 0)
        logger.info("Database found : " + databaseName);
      return count == 0;
    } catch (SQLException exception) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
      throw exception;
    }
  }

  /**
   * Return connection.
   *
   * @param databaseName the database name
   * @return the connection
   */
  private Connection getConnection(final String databaseName) throws SQLException {
    final StringBuilder jdbcUrl = new StringBuilder();
    jdbcUrl.append("jdbc:postgresql://").append(host).append(":").append(port).append("/").append(databaseName);
    return DriverManager.getConnection(jdbcUrl.toString(), marccatUser, marccatPassword);
  }

}
