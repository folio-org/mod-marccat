package org.folio.marccat.integration;

/**
 * Tool to run database scripts
 */

import java.io.*;
import java.sql.*;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.exception.ModMarccatException;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

/**
 * The Class ScriptRunner.
 */
public class ScriptRunner {

  /**
   * The Constant logger.
   */
  private static final Log logger = new Log(ScriptRunner.class);

  /**
   * The Constant DEFAULT_DELIMITER.
   */
  private static final String DEFAULT_DELIMITER = ";";


  /**
   * The connection.
   */
  private final Connection connection;

  /**
   * The auto commit.
   */
  private final boolean autoCommit;

  /**
   * The delimiter.
   */
  private String delimiter = DEFAULT_DELIMITER;

  /**
   * The full line delimiter.
   */
  private boolean fullLineDelimiter = false;


  /**
   * Default constructor.
   *
   * @param connection the connection
   * @param autoCommit the auto commit
   */
  public ScriptRunner(Connection connection, boolean autoCommit) {
    this.connection = connection;
    this.autoCommit = autoCommit;

  }



  /**
   * Runs an SQL script (read in using the Reader parameter).
   *
   * @param reader - the source of the script
   * @throws IOException  Signals that an I/O exception has occurred.
   * @throws SQLException the SQL exception
   */
  public void runScript(Reader reader) throws IOException, SQLException {
    try {
      boolean originalAutoCommit = connection.getAutoCommit();
      try {
        if (originalAutoCommit != this.autoCommit) {
          connection.setAutoCommit(this.autoCommit);
        }
        runScript(connection, reader);
      } finally {
        connection.setAutoCommit(originalAutoCommit);
      }
    } catch (Exception e) {
      throw new ModMarccatException("Error running script.  Cause: " + e, e);
    }
  }

  /**
   * Runs an SQL script (read in using the Reader parameter) using the
   * connection passed in.
   *
   * @param conn   - the connection to use for the script
   * @param reader - the source of the script
   * @throws IOException  if there is an error reading from the Reader
   * @throws SQLException if any SQL errors occur
   */
  private void runScript(Connection conn, Reader reader) throws IOException, SQLException {
    StringBuilder command = null;
    try (LineNumberReader lineReader = new LineNumberReader(reader);) {
      String line;
      while ((line = lineReader.readLine()) != null) {
        if (command == null) {
          command = new StringBuilder();
        }
        boolean isFinalFunctionDelimiter = (line.contains("$$;") || line.contains("$_$;"));
        boolean isFinalCopyDelimiter = line.contains("\\.");
        String trimmedLine = line.trim();

        if (!isComment(trimmedLine)) {
          if (isFinalLineDelimiter(trimmedLine) && !command.toString().contains("COPY")) {
            appendLine(command, line);
            if (isNotFunction(command) || isFinalFunctionDelimiter) {
              logger.info("Command Function: " + command.toString());
              this.execCommand(conn, command, lineReader);
              command = null;
            }
          } else if (isFinalCopyDelimiter && command.toString().contains("COPY")) {
            logger.info("Command Copy: " + command.toString());
            executePgCopy(conn, command.toString());
            command = null;

          } else {
             appendLine(command, line);
          }
        }
      }
      if (!autoCommit) {
        conn.commit();
      }
    } catch (IOException e) {
      throw new IOException(String.format("Error executing '%s': %s", command, e.getMessage()), e);
    } finally {
      conn.rollback();
    }
  }

  private void appendLine(StringBuilder command, String line) {
    command.append(line);
    command.append("\n");
  }

  /**
   * Checks if is a comment.
   *
   * @param trimmedLine the trimmed line
   * @return true, if is a comment
   */
  private boolean isComment(String trimmedLine) {
    return (trimmedLine.length() < 1 || trimmedLine.startsWith("//")) ||
      trimmedLine.startsWith("--") || (trimmedLine.length() < 1 || trimmedLine.startsWith("--"));
  }

  /**
   * Checks if is final line delimiter.
   *
   * @param trimmedLine the trimmed line
   * @return true, if is final line delimiter
   */
  private boolean isFinalLineDelimiter(String trimmedLine) {
    return !fullLineDelimiter && trimmedLine.endsWith(delimiter);
  }

  /**
   * Checks if is not function.
   *
   * @param command the command
   * @return true, if is not function
   */
  private boolean isNotFunction(final StringBuilder command) {
    return (command.indexOf("$$") == -1 && command.indexOf("$_$") == -1  && command.indexOf("COPY") == -1);
  }

  /**
   * Exec command.
   *
   * @param conn       the conn
   * @param command    the command
   * @param lineReader the line reader
   * @throws IOException  Signals that an I/O exception has occurred.
   * @throws SQLException the SQL exception
   */
  private void execCommand(final Connection conn, final StringBuilder command,
                           LineNumberReader lineReader) throws SQLException {

    if (command.length() == 0) {
      return;
    }
    this.execSqlCommand(conn, command, lineReader);
  }


  /**
   * Exec sql command.
   *
   * @param conn       the conn
   * @param command    the command
   * @param lineReader the line reader
   * @throws SQLException the SQL exception
   */
  private void execSqlCommand(Connection conn, StringBuilder command,
                              LineNumberReader lineReader) throws SQLException {

    Statement statement = conn.createStatement();
    try {
      String sql = command.toString();
      sql = sql.substring(0, sql.lastIndexOf(';'));
      statement.execute(sql);
    } catch (SQLException exception) {
      final String errText = String.format("Error executing '%s' (line %d): %s", command, lineReader.getLineNumber(), exception.getMessage());
      logger.error(errText, exception);
    }
    try {
      statement.close();
    } catch (Exception exception) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
    }
  }



  /**
   * Execute pg copy.
   *
   * @param connection the connection
   * @param sql        the sql
   * @throws SQLException the SQL exception
   */
  private void executePgCopy(Connection connection, String sql) throws SQLException {
    final int split = sql.indexOf(DEFAULT_DELIMITER);
    final String statement = sql.substring(0, split);
    final String data = sql.substring(split + 1).trim();
    final CopyManager copyManager = new CopyManager(connection.unwrap(BaseConnection.class));
    try {
      if(data != null && !data.isEmpty())
         copyManager.copyIn(statement, new StringReader(data));
    } catch (IOException e) {
      throw new SQLException("Unable to execute COPY operation", e);
    }
  }
}
