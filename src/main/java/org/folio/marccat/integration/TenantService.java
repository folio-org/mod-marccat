package org.folio.marccat.integration;


import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.URI;
import java.sql.*;
import java.util.*;
import java.util.stream.StreamSupport;
import static java.util.stream.Collectors.toMap;

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
    * The Constant DATABASE_SETUP.
   */

  public static final String DATABASE_SETUP = "/database-setup/";

  /**
    * The Constant UTF_8.
   */
  public static final String UTF_8 = "UTF-8";

  /**
   * The remote configuration.
   */
  @Autowired
  private RemoteConfiguration configuration;

  /**
   * The okapi client.
   */
  @Autowired
  private OkapiClient okapiClient;

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
    logger.info("Enable tenant" + " - Start");
    initializeDatabase(tenant);
//    ObjectNode value =  configuration.attributes(tenant, true, "");
//    final Map <String, String> config = getConfigurations(value);
//    if(config != null && config.size() == 0) {
//      initializeConfiguration(tenant);
//    }
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
   */
  private void initializeConfiguration(final String tenant) {
    final String configurationUrl = okapiClient.getModuleUrl(Global.MODULE_CONFIGURATION, Global.SUB_PATH_CONFIGURATION);
    final URI uri = URI.create(configurationUrl);
    final String pathScript = getPathScript(DATABASE_SETUP + "setup-conf.sh", tenant, false);
    final List <String> commands = Arrays.asList(BIN_SH, pathScript, uri.getHost(),
      String.valueOf(uri.getPort()), tenant, "", "", "", marccatUser, marccatPassword);
    executeScript(commands, "", adminPassword);
  }

  /**
   * Initialize database.
   *
   * @param tenant the tenant
   */
  private void initializeDatabase(final String tenant) throws SQLException {
    final String databaseName =  tenant + marccatSuffix;
    final Map<String, String> env = okapiClient.getEnvironments();
    if(!env.isEmpty()){
      host = env.get("DB_HOST");
      port = env.get("DB_PORT");
      adminUser = env.get("DB_USERNAME");
      adminPassword = env.get("DB_PASSWORD");
    }
    createRole();
    createDatabase(databaseName);
    createObjects(databaseName);
   /* boolean schemaNotExist = schemaExists(databaseName);
    if (schemaNotExist) {
      createObjects(databaseName);
    }
    executePatch(databaseName, patchDatabase, "Install patch MARCCAT DB 1.2", "MARCCAT DB 1.2 found (Exit code 3)");
    executePatch(databaseName, patchProcedure, "Install patch MARCCAT DB PLPGSQL 3.3", "MARCCAT DB PLPGSQL 3.3 found (Exit code 3)");*/
  }

  /**
   * Creates the role.
   *
   * @param databaseName the database name
   */
 /* private void createRole(final String databaseName) {
    final String pathScript = getPathScript(DATABASE_SETUP + "create-marccat-role.sql", databaseName, true);
    final String command =  String.format("psql -h %s -p %s -U %s -f %s", host, port, adminUser, pathScript);
    final List<String> commands = Arrays.asList(command.split("\\s+"));
    executeScript(commands, "Create role", adminPassword);
  }*/

  /**
   * Creates the database.
   *
   */
 /* private void createDatabase(final String databaseName) {
    final String pathScript = getPathScript(DATABASE_SETUP + "create-db.sql", databaseName, true);
    final String command =  String.format("psql -h %s -p %s -U %s -f %s", host, port, adminUser, pathScript);
    final List<String> commands = Arrays.asList(command.split("\\s+"));
    executeScript(commands, "Create database", adminPassword);
  }*/

  /**
   * Creates the objects.
   *
   */
 /* private void createObjects(final String databaseName) {
    final String pathScript = getPathScript(DATABASE_SETUP + "create-objects.sql", databaseName, false);
    final String command =  String.format("psql -h %s -p %s -U %s -d %s -v user_name=%s -f %s", host, port, adminUser, databaseName, marccatUser, pathScript);
    final List<String> commands = Arrays.asList(command.split("\\s+"));
    executeScript(commands, "Create objects", adminPassword);
  }*/

  private void createRole() throws SQLException {
    final String queryRole = "CREATE ROLE marccat PASSWORD 'admin' SUPERUSER CREATEDB INHERIT LOGIN";
    final String queryAlterRole = "ALTER ROLE marccat SET search_path TO amicus,olisuite,public";
    logger.debug("Start role");
    try (Connection connection = getConnection("postgres", adminUser, adminPassword);
         Statement stmRole = connection.createStatement();
         Statement stmAlterRole = connection.createStatement();)
    {
      stmRole.execute(queryRole);
      stmAlterRole.execute(queryAlterRole);
      logger.info("End role");
    } catch (SQLException exception) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
      throw exception;
    }
  }
  private void createDatabase(final String databaseName) throws SQLException {
    final String queryDatabase = "Create database: " + databaseName;
    logger.info("Start database: " + databaseName);
    try (Connection connection = getConnection("postgres");
         Statement statement = connection.createStatement();)
    {
      statement.execute(queryDatabase);
      logger.info("End database: " + databaseName);
    } catch (SQLException exception) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
      throw exception;
    }
  }


  private void createObjects(final String databaseName) throws SQLException {
    final String pathScript = getPathScript(DATABASE_SETUP + "create-objects.sql", databaseName, false);
    Connection connection = getConnection(databaseName, marccatUser, marccatPassword );
    ScriptRunner runner = new ScriptRunner(connection, false, false);
    try {
      runner.runScript(new BufferedReader(new FileReader(pathScript)));
    } catch (IOException exception) {
      logger.error(Message.MOD_MARCCAT_00013_IO_FAILURE, exception);
    }
  }

  /**
   * Executes the patch.
   *
   * @param databaseName the database name
   */
  private void executePatch(final String databaseName, final String patch, final String message, final String errorMessage) {

    try {
      final InputStream inputStream = getClass().getResourceAsStream(patch + "/env.conf");
      final List <String> ls = IOUtils.readLines(inputStream, "utf-8");
      final String patchRel = getVersionNumber(ls.get(1), "patch_rel_nbr=");
      final String patchSp = getVersionNumber(ls.get(2), "patch_sp_nbr=");
      final String patchComp = getVersionNumber(ls.get(3), "patch_comp_typ=");
      final File file = getResourceAsFileWithChild(patch, "/install-patch.sql", databaseName);
      String pathScript = null;
      if (file != null) {
        pathScript = file.getAbsolutePath();
      }
      final String command = String.format("psql -h %s -p %s -U %s -d %s -v user_name=%s -v %s -v %s -v %s -f %s", host, port, marccatUser, databaseName, marccatUser, patchRel, patchSp, patchComp, pathScript);
      final List <String> commands = Arrays.asList(command.split("\\s+"));
      final int exitCode = executeScript(commands, message, marccatPassword);
      if (exitCode == 3)
        logger.info(errorMessage);
    } catch (IOException exception) {
      logger.error(Message.MOD_MARCCAT_00013_IO_FAILURE, exception);
    }

  }

  /**
   * Return the version of the patch.
   *
   * @param line the line
   * @param variable the variable
   */
  private String getVersionNumber(final String line, final String variable) {
    return line.substring(line.indexOf(variable));
  }

  /**
   * Execute script.
   *
   * @param commands   the commands
   * @param messageLog the message log
   * @return the exit code
   */
  private int executeScript(final List <String> commands, final String messageLog, final String pgPassword) {
    final ProcessBuilder builder = new ProcessBuilder(commands);
    final Map<String, String> mp = builder.environment();
    int exitCode = 0;
    mp.put("PGPASSWORD", pgPassword);
    Process process = null;
    try {
      logger.info(messageLog + " - Start");
      builder.redirectOutput((ProcessBuilder.Redirect.INHERIT));
      process = builder.start();
      exitCode =  processWait(process);
      logger.info(messageLog + " - End");

    } catch (IOException exception) {
      logger.error(Message.MOD_MARCCAT_00013_IO_FAILURE, exception);
    }
    if (process != null) {
      process.destroy();
    }
    return exitCode;
  }

  /**
   * Causes the current thread to wait
   * until the end of the process.
   *
   * @param process the process
   * @return the exit code
   */
  private int processWait(final Process process) {
    int exitCode = 0;
    try {
      exitCode = process.waitFor();
      logger.info("Exit code %d :", exitCode);
    } catch (InterruptedException e) {
      logger.error(Message.MOD_MARCCAT_00033_PROCESS_FAILURE, e);
      Thread.currentThread().interrupt();
    }
    return exitCode;
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
      if (isReplaceVariables) {
        stringInputStream = stringInputStream
          .replaceAll("user_name", marccatUser)
          .replaceAll("password", marccatPassword)
          .replaceAll("database_name", databaseName);
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
          final File tempChildFile = getResourceAsFile(resourcePath + "/" + fileNameChild, databaseName, false);
          if (tempChildFile != null) {
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
   * Return true if schema exists.
   *
   * @param databaseName the database name
   * @return true if schema exists
   */
  private boolean schemaExists(final String databaseName) throws SQLException {
    final String querySchema = "select count(*) from pg_catalog.pg_namespace where nspname in ('amicus', 'olisuite')";
    try (Connection connection = getConnection(databaseName, adminUser, adminPassword);
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
  private Connection getConnection(final String databaseName, final String username, final String password) throws SQLException {
    final StringBuilder jdbcUrl = new StringBuilder();
    jdbcUrl.append("jdbc:postgresql://").append(host).append(":").append(port).append("/").append(databaseName);
    return DriverManager.getConnection(jdbcUrl.toString(), username, password);
  }

  /**
   * Return configurations of the database.
   *
   * @param value the value of the object node
   * @return the map of the configurations
   */
  public Map <String, String> getConfigurations(ObjectNode value) {
    return StreamSupport.stream(value.withArray("configs").spliterator(), false)
      .filter(node -> "datasource".equals(node.get("configName").asText()))
      .map(node -> new AbstractMap.SimpleEntry<>(node.get("code").asText(), node.get("value").asText()))
      .collect(toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
  }


}
