/*
 * Created on May 5, 2004
 *
 */
package org.folio.cataloging.business.colgate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.authentication.AuthenticationBroker;
import org.folio.cataloging.business.authentication.AuthenticationException;
import org.folio.cataloging.business.authentication.InvalidPasswordFormatException;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.DAOGlobalVariable;
import org.folio.cataloging.exception.ConnectException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Maintains a Colgate session and supports sending requests and analysing responses
 *
 * @author Paul Mouland
 */
public class Colgate implements AuthenticationBroker {
  private static Log logger = LogFactory.getLog(Colgate.class);
  private static int minPasswordLength = 5;
  private static String hostName = null;
  private static short portNumber = 0;
  private Socket socket;

  /**
   * Converts a byte array to a String representing
   * the hex stringValue of the byte array.
   * <p>
   * Makes use of Integer.toHexString but values returned
   * are always of the form XX (i.e. two digits in uppercase.
   *
   * @param array the input byte stream
   * @return the converted string
   */
  static public String toHexString(byte[] array) {
    String aString = new String();
    String hexString = new String();
    for (int i = 0; i < array.length; i++) {
      aString = Integer.toHexString(array[i]);
      if (array[i] < 0) {
        aString = aString.substring(6, 8);
      } else if (array[i] <= 15) {
        aString = "0" + aString;
      }
      hexString = hexString + aString;
    }
    hexString = hexString.toUpperCase();
    logger.debug("hexString is " + hexString);
    return hexString;
  }

  /**
   * Generates an md5 encrypted form of the AMICUS password
   *
   * @param clearPassword - the clear text password
   * @return - the encrypted password
   */
  public static byte[] encryptPassword(byte[] clearPassword) {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("MD5");
      md.update(clearPassword);
      return asIsoByte(toHexString(md.digest()));
    } catch (NoSuchAlgorithmException e) {
      logger.warn("MD5 encryption not supported");
      return null;
    }
  }

  /**
   * Generates an md5 encrypted form of the AMICUS password
   *
   * @param clearPassword - the clear text password
   * @return - the encrypted password
   */
  public static byte[] encryptPassword(String clearPassword) {
    return encryptPassword(asIsoByte(clearPassword));
  }

  /**
   * Converts the input string to a byte array of ISO-Latin-1 characters
   *
   * @param s - the string to be encoded
   * @return - the encrypted password
   */
  public static byte[] asIsoByte(String s) {
    try {
      return s.getBytes("ISO-8859-1");
    } catch (UnsupportedEncodingException e) {
      logger.warn("No support for ISO-8859-1");
      return null;
    }
  }

  //TODO ISO Latin one may not be the correct choice (for hungary for example)

  /*
   *  (non-Javadoc)
   * @see AuthenticationBroker#authenticateUser(java.lang.String, java.lang.String)
   */
  public void authenticateUser(String userName, String password)
    throws ConnectException, AuthenticationException {

    connect();

    ColgateLoginRequest q = new ColgateLoginRequest(userName, password);

    ColgateLoginResponse r;
    try {
      q.send(getSocket());

      r = new ColgateLoginResponse();

      r.recv(getSocket());
    } catch (IOException e1) {
      throw new ConnectException();
    }

    disconnect();

    r.testExceptions();
    // throws specific exceptions based on Colgate rc
  }

  /*
   * disconnects from Colgate
   */
  public void disconnect() throws ConnectException {
    byte closeRequest[] = {50, 0, 0, 0, 0};

    try {
      getSocket().getOutputStream().write(closeRequest);
      getSocket().close();
    } catch (IOException e) {
      throw new ConnectException();
    }
  }

  /**
   * Gets the IP name and port of the Colgate server from the database and
   * establishes a connection
   */
  public void connect() throws ConnectException {
    try {
      if (hostName == null) {
        // Get the host and port from the database
        DAOGlobalVariable dao = new DAOGlobalVariable();
        hostName = dao.getValueByName("host_ip_nme");
        portNumber = Short.parseShort(dao.getValueByName("ir_coport_id"));

        logger.warn("Host name: " + hostName);
        logger.warn("Port number: " + portNumber);

      }
      setSocket(new Socket(hostName, portNumber));
    } catch (NumberFormatException e1) {
      logger.warn("Global Variable ir_coport_id is not a valid number");
      throw new ConnectException();
    } catch (DataAccessException e1) {
      throw new ConnectException();
    } catch (UnknownHostException e) {
      logger.warn("Cannot resolve" + hostName + ":" + portNumber);
      throw new ConnectException("Host_unknown");
    } catch (IOException e) {
      logger.warn("IOException creating socket");
      throw new ConnectException("IO_error_2");
    }
  }

  /**
   * @return the socket used for the Colgate connection
   */
  public Socket getSocket() {
    return socket;
  }

  /**
   * @param socket - sets the socket used by the Colgate connection
   */
  public void setSocket(Socket socket) {
    this.socket = socket;
  }

  /**
   * validates the input password for valid form.  The password must
   * be at least 5 characters long, must contain at least one digit
   * and at least one alpha character.  The password may also contain
   * $ and _ characters
   *
   * @param password - the input password
   * @throws InvalidPasswordFormatException if the password fails the validation
   * @since 1.0
   */
  public void validatePasswordFormat(String password)
    throws AuthenticationException {
    if (password.length() < minPasswordLength) {
      throw new InvalidPasswordFormatException("A password should contain at least " + minPasswordLength + " characters");
    }
    if (password.matches("[a-zA-Z0-9$_]*") == false) {
      throw new InvalidPasswordFormatException("A password can only contain alphabetic and numeric characters: a-z , A-Z, 0-9");
    }
    if (password.matches(".*\\d+.*") == false) { // at least one digit
      throw new InvalidPasswordFormatException("A password should contain at least one digit");
    }
    if (password.matches(".*[a-zA-Z]+.*") == false) { // at least one alpha
      throw new InvalidPasswordFormatException("A password should contain at least one alphabetical character");
    }
  }

  /* (non-Javadoc)
   * @see com.libricore.librisuite.business.AuthenticationBroker#changePassword(java.lang.String, java.lang.String, java.lang.String)
   */
  public void changePassword(
    String userName,
    String oldPassword,
    String newPassword)
    throws ConnectException, AuthenticationException {

    validatePasswordFormat(newPassword);

    connect();

    ColgateChangePasswordRequest q =
      new ColgateChangePasswordRequest(
        userName,
        oldPassword,
        newPassword);

    ColgateChangePasswordResponse r;
    try {
      q.send(getSocket());

      r = new ColgateChangePasswordResponse();

      r.recv(getSocket());
    } catch (IOException e1) {
      throw new ConnectException();
    }

    disconnect();

    r.testExceptions();
    // throws specific exceptions based on Colgate rc
  }

}
