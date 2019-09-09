package org.folio.marccat.integration;

/**
 * Tool to run database scripts
 */

import java.io.*;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.folio.marccat.config.log.Log;
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
   * regex to detect delimiter.
   * ignores spaces, allows delimiter in comment, allows an equals-sign
   */
  public static final Pattern delimP = Pattern.compile("^\\s*(--)?\\s*delimiter\\s*=?\\s*([^\\s]+)+\\s*.*$", Pattern.CASE_INSENSITIVE);

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
   * Sets the delimiter.
   *
   * @param delimiter         the delimiter
   * @param fullLineDelimiter the full line delimiter
   */
  public void setDelimiter(String delimiter, boolean fullLineDelimiter) {
    this.delimiter = delimiter;
    this.fullLineDelimiter = fullLineDelimiter;
  }


  /**
   * Runs an SQL script (read in using the Reader parameter).
   *
   * @param filepath - the filepath of the script to run.
   * @throws IOException  Signals that an I/O exception has occurred.
   * @throws SQLException the SQL exception
   */
  public void runScript(String filepath) throws IOException, SQLException {
    File file = new File(filepath);
    this.runScript(new BufferedReader(new FileReader(file)));
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
        String trimmedLine = line.trim();
        final Matcher delimMatch = delimP.matcher(trimmedLine);
        if (trimmedLine.length() < 1 || trimmedLine.startsWith("//")) {
          // Do nothing
        } else if (delimMatch.matches()) {
          setDelimiter(delimMatch.group(2), false);
        } else if (trimmedLine.startsWith("--")) {
          // Do nothing
        } else if (trimmedLine.length() < 1 || trimmedLine.startsWith("--")) {
          // Do nothing
        } else if (isFinalLineDelimiter(trimmedLine) && trimmedLine.indexOf("COPY") == -1) {
          command.append(line.substring(0, line.lastIndexOf(';')));
          command.append(" ");
          if (isNotFunction(command) || isFinalFunctionDelimiter) {
            logger.info("Command : " + command.toString());
            this.execCommand(conn, command, lineReader);
            command = null;
          }
        }
        else if (line.contains("\\.") && command.toString().contains("COPY")) {
          logger.info("Command Copy: " + command.toString());
          executePgCopy(conn, command.toString());
          command = null;

        } else {
          if (command != null) {
            command.append(line);
            command.append("\n");
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
  /**
   * Checks if is final line delimiter.
   *
   * @param trimmedLine the trimmed line
   * @return true, if is final line delimiter
   */
  private boolean isFinalLineDelimiter(String trimmedLine) {
    return !fullLineDelimiter && trimmedLine.endsWith(";") /*|| fullLineDelimiter && trimmedLine.equals(";")*/;
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
                           LineNumberReader lineReader) throws  SQLException {

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
      statement.execute(command.toString());
    } catch (SQLException exception) {
      final String errText = String.format("Error executing '%s' (line %d): %s", command, lineReader.getLineNumber(), exception.getMessage());
      logger.error(errText, exception);
    }
    try {
      statement.close();
    } catch (Exception e) {
      // Ignore to workaround a bug in Jakarta DBCP
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
    CopyManager copyManager = new CopyManager(connection.unwrap(BaseConnection.class));
    try {
      if(data != null && !data.isEmpty())
        copyManager.copyIn(statement, new StringReader(data));
    } catch (IOException e) {
      throw new SQLException("Unable to execute COPY operation", e);
    }
  }
}
