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
   * The Constant POSTGRES.
   */
  public static final String POSTGRES = "postgres";
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
   * @param tenant   the tenant
   * @param okapiUrl the okapi url
   * @throws SQLException the SQL exception
   * @throws IOException  Signals that an I/O exception has occurred.
   */
  public void createTenant(final String tenant, final String okapiUrl) throws SQLException, IOException {
    logger.debug("Enable tenant" + " - Start");
    okapiClient.setOkapiUrl(okapiUrl);
    initializeDatabase(tenant);
    ObjectNode value = configuration.attributes(tenant, true, "");
    // TODO 23/06 test presenza del modulo di configurazione
    if(value != null){
      final Map <String, String> config = getConfigurations(value);
      if (config != null && config.size() == 0) {
        initializeConfiguration(tenant);
      }
    }
    logger.debug("Enable tenant" + " - End");
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
    final String databaseName = tenant + marccatSuffix;
    final String configurationUrl = okapiClient.getModuleUrl(Global.MODULE_CONFIGURATION, Global.SUB_PATH_CONFIGURATION);
    final URI uri = URI.create(configurationUrl);
    final String pathScript = getPathScript(DATABASE_SETUP + "setup-conf.sh", tenant, false);
    final List <String> commands = Arrays.asList(BIN_SH, pathScript, uri.getHost(),
      String.valueOf(uri.getPort()), tenant, host, port, databaseName, marccatUser, marccatPassword);
    executeCommand(commands, "", adminPassword);
  }

  /**
   * Initialize database.
   *
   * @param tenant the tenant
   * @throws SQLException the SQL exception
   */
  private void initializeDatabase(final String tenant) throws SQLException {
    final String databaseName = tenant + marccatSuffix;
    final Map <String, String> env =  okapiClient.getModuleEnvs(Global.MODULE_MARCCAT);
    if (!env.isEmpty()) {
      host = env.get("DB_HOST");
      port = env.get("DB_PORT");
      adminUser = env.get("DB_USERNAME");
      adminPassword = env.get("DB_PASSWORD");
    }
    createRole();
    boolean databaseNotExist = databaseExists(databaseName);
    if (databaseNotExist)
      createDatabase(databaseName);
    boolean schemaNotExist = schemaExists(databaseName);
    if (schemaNotExist)
      createObjects(databaseName);
    executePatch(databaseName, patchDatabase, "Install patch MARCCAT DB 1.2", "MARCCAT DB 1.2 found");
    executePatch(databaseName, patchProcedure, "Install patch MARCCAT DB PLPGSQL 3.3", "MARCCAT DB PLPGSQL 3.3");
  }

  /**
   * Creates the role.
   *
   * @throws SQLException the SQL exception
   */
  private void createRole() throws SQLException {
    final String queryRole = "DO $$ BEGIN  CREATE ROLE " + marccatUser + " PASSWORD '" + marccatPassword + "' SUPERUSER CREATEDB INHERIT LOGIN;  EXCEPTION WHEN duplicate_object THEN  RAISE NOTICE 'Role % already exists', 'marccat'; END $$";
    final String queryAlterRole = "ALTER ROLE " + marccatUser + " SET search_path TO amicus,olisuite,public";
    logger.debug("Start role");
    try (Connection connection = getConnection(POSTGRES, adminUser, adminPassword);
         Statement stmRole = connection.createStatement();
         Statement stmAlterRole = connection.createStatement()) {
      stmRole.execute(queryRole);
      stmAlterRole.execute(queryAlterRole);
      logger.debug("End role");
    } catch (SQLException exception) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
      throw exception;
    }
  }

  /**
   * Creates the database.
   *
   * @param databaseName the database name
   * @throws SQLException the SQL exception
   */
  private void createDatabase(final String databaseName) throws SQLException {
    final String queryDatabase = "create database " + databaseName; /*+ " ENCODING 'UTF8'";*/

    logger.debug("Start database " + databaseName);
    try (Connection connection = getConnection(POSTGRES, adminUser, adminPassword);
         Statement statement = connection.createStatement()) {
      statement.execute(queryDatabase);
      logger.debug("End database " + databaseName);

    } catch (SQLException exception) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
      throw exception;
    }
  }

  /**
   * Creates the objects.
   *
   * @param databaseName the database name
   * @throws SQLException the SQL exception
   */
  private void createObjects(final String databaseName) throws SQLException {
    logger.debug("Start create objects");
    final String pathScript = getPathScript(DATABASE_SETUP + "create-objects.sql", databaseName, true);
    final Connection connection = getConnection(databaseName, marccatUser, marccatPassword);
    final ScriptRunner runner = new ScriptRunner(connection, false);
    try {
      runner.runScript(new BufferedReader(new FileReader(pathScript)));
      logger.debug("End create objects");
    } catch (IOException exception) {
      logger.error(Message.MOD_MARCCAT_00013_IO_FAILURE, exception);
    }
  }


  /**
   * Execute patch.
   *
   * @param databaseName the database name
   * @param patch        the patch
   * @param message      the message
   * @param errorMessage the error message
   * @throws SQLException the SQL exception
   */
  private void executePatch(final String databaseName, final String patch, final String message, final String errorMessage) throws SQLException {
    try {
      logger.debug(message);
      final InputStream inputStream = getClass().getResourceAsStream(patch + "/env.conf");
      final List <String> ls = IOUtils.readLines(inputStream, "utf-8");
      final String patchRel = getVersionNumber(ls.get(1), "patch_rel_nbr=");
      final String patchSp = getVersionNumber(ls.get(2), "patch_sp_nbr=");
      final String patchComp = getVersionNumber(ls.get(3), "patch_comp_typ=");
      final boolean patchNotExist = patchExists(databaseName, patchRel, patchSp, patchComp, errorMessage);
      if (patchRel != null && patchNotExist)
        executeScript(patch, "/install-patch.sql", databaseName);
      logger.debug("End " + message);
    } catch (IOException exception) {
      logger.error(Message.MOD_MARCCAT_00013_IO_FAILURE, exception);
    }
  }



  /**
   * Return the version of the patch.
   *
   * @param line     the line
   * @param variable the variable
   * @return the version number
   */
  private String getVersionNumber(final String line, final String variable) {
    return  (line.indexOf(variable) != -1) ?  line.substring(line.indexOf("=") + 1) :  null;
  }

  /**
   * Execute script.
   *
   * @param commands   the commands
   * @param messageLog the message log
   * @param pgPassword the pg password
   * @return the exit code
   */
  private int executeCommand(final List <String> commands, final String messageLog, final String pgPassword) {
    final ProcessBuilder builder = new ProcessBuilder(commands);
    final Map <String, String> mp = builder.environment();
    int exitCode = 0;
    mp.put("PGPASSWORD", pgPassword);
    Process process = null;
    try {
      logger.debug(messageLog + " - Start");
      builder.redirectOutput((ProcessBuilder.Redirect.INHERIT));
      process = builder.start();
      exitCode = processWait(process);
      logger.debug(messageLog + " - End");

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
      logger.debug("Exit code %d :", exitCode);
    } catch (InterruptedException e) {
      logger.error(Message.MOD_MARCCAT_00033_PROCESS_FAILURE, e);
      Thread.currentThread().interrupt();
    }
    return exitCode;
  }

  /**
   * Gets the path script.
   *
   * @param fileName     the file name
   * @param databaseName the database name
   * @param isReplace    the is replace
   * @return the path script
   */
  private String getPathScript(final String fileName, final String databaseName, final boolean isReplace) {
    final File file = getResourceAsFile(fileName, databaseName, isReplace);
    return (file != null) ? file.getAbsolutePath() : null;
  }


  /**
   * Gets the resource as a temporary file.
   *
   * @param resourcePath       the resource path
   * @param databaseName       the database name
   * @param isReplaceVariables the is replace variables
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
          .replaceAll(":user_name", marccatUser)
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
   * Executes a sql script of a temporary file.
   *
   * @param resourcePath the resource path
   * @param fileName     the file name of the parent
   * @param databaseName the database name
   * @return the resource as file
   * @throws SQLException the SQL exception
   */
  private void executeScript(final String resourcePath, final String fileName, final String databaseName) throws SQLException {
    try (Connection connection = getConnection(databaseName, marccatUser, marccatPassword)) {
      final List <String> ls = IOUtils.readLines(getClass().getResourceAsStream(resourcePath + fileName), "utf-8");
      for (String line : ls) {
        if (line.startsWith("\\ir ")) {
          final String fileNameChild = line.substring(4);
          final File tempChildFile = getResourceAsFile(resourcePath + "/" + fileNameChild, databaseName, false);
          ScriptRunner runner = new ScriptRunner(connection, false);
          if (tempChildFile != null)
            runner.runScript(new BufferedReader(new FileReader(tempChildFile.getAbsolutePath())));
        }
      }
    } catch (IOException exception) {
      logger.error(Message.MOD_MARCCAT_00013_IO_FAILURE, exception);
    }
  }


  /**
   * Return true if schema exists.
   *
   * @param databaseName the database name
   * @return true if schema exists
   * @throws SQLException the SQL exception
   */
  private boolean schemaExists(final String databaseName) throws SQLException {
    final String querySchema = "select count(*) from pg_catalog.pg_namespace where nspname in ('amicus', 'olisuite')";
    try (Connection connection = getConnection(databaseName, adminUser, adminPassword);
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(querySchema)) {
      resultSet.next();
      final int count = resultSet.getInt(1);
      if (count != 0)
        logger.debug("Schema found : " + databaseName);
      return count == 0;
    } catch (SQLException exception) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
      throw exception;
    }
  }

  /**
   * Return true if database exists.
   *
   * @param databaseName the database name
   * @return true if database exists
   * @throws SQLException the SQL exception
   */
  private boolean databaseExists(final String databaseName) throws SQLException {
    final String queryDatabase = "SELECT count(*) from pg_database WHERE datname='" + databaseName + "'";
    try (Connection connection = getConnection(POSTGRES, adminUser, adminPassword);
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(queryDatabase)) {
      resultSet.next();
      final int count = resultSet.getInt(1);
      if (count != 0)
        logger.debug("Database found : " + databaseName);
      return count == 0;
    } catch (SQLException exception) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
      throw exception;
    }
  }


  /**
   * Return true if patch exists.
   *
   * @param databaseName the database name
   * @param patchRel     the patch rel
   * @param patchSp      the patch sp
   * @param patchComp    the patch comp
   * @param errorMessage the error message
   * @return true if patch exists
   * @throws SQLException the SQL exception
   */
  private boolean patchExists(final String databaseName, final String patchRel, final String patchSp, final String patchComp, final String errorMessage) throws SQLException {
    final String queryPatch = " select count(*) " +
      " from olisuite.s_patch_history" +
      " where release_number = " + patchRel +
      " and service_pack_number = " + patchSp +
      " and component_typ = " + patchComp;
    try (Connection connection = getConnection(databaseName, adminUser, adminPassword);
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(queryPatch)) {
      resultSet.next();
      final int count = resultSet.getInt(1);
      if (count != 0)
        logger.debug(errorMessage);
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
   * @param username     the username
   * @param password     the password
   * @return the connection
   * @throws SQLException the SQL exception
   */
  private Connection getConnection(final String databaseName, final String username, final String password) throws SQLException {
    final StringBuilder jdbcUrl = new StringBuilder();
    jdbcUrl.append("jdbc:postgresql://").append(host).append(":").append(port).append("/").append(databaseName);
    logger.debug("URL JDBC: " + jdbcUrl);
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
      .map(node -> new AbstractMap.SimpleEntry <>(node.get("code").asText(), node.get("value").asText()))
      .collect(toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
  }


}
