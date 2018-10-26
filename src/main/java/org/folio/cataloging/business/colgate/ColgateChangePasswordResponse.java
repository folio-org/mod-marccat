/*
 * (c) LibriCore
 *
 * Created on Jun 11, 2004
 *
 * ColgateChangePasswordResponse.java
 */
package org.folio.cataloging.business.colgate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.authentication.AuthenticationException;
import org.folio.cataloging.business.authentication.PasswordDuplicatesPreviousException;
import org.folio.cataloging.business.common.SocketMessage;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * maps to C structure used by Colgate to return a Change Password response
 *
 * @author paulm
 */
public class ColgateChangePasswordResponse extends SocketMessage {
  private static Log logger = LogFactory.getLog(ColgateChangePasswordResponse.class);
  private static int expectedLength = 44;
  private static byte type = 59;
  public int errorNumber;
  public byte[] encodedPassword;

  /**
   * @return type member
   */
  public static byte getType() {
    return type;
  }

  /**
   * set the type of this message
   */
  public static void setType(byte b) {
    type = b;
  }

  /**
   * set the expected length of this message
   */
  public static void setExpectedLength(int i) {
    expectedLength = i;
  }

  /* (non-Javadoc)
   * @see com.libricore.librisuite.business.ColgateMessage#asByteArray()
   */
  public byte[] asByteArray() throws IOException {
    return null;
  }

  /* (non-Javadoc)
   * @see com.libricore.librisuite.business.ColgateMessage#fromByteArray(byte[])
   */
  public void fromByteArray(byte[] msg) throws IOException {
    if (msg.length != expectedLength) {
      logger.warn("Wrong length message returned from Colgate");
      throw new IOException();
    }

    if (msg[0] != getType()) {
      logger.warn("Unexpected message type received from Colgate");
      throw new IOException();
    }

    ByteArrayInputStream bs = new ByteArrayInputStream(msg);
    DataInputStream ds = new DataInputStream(bs);
    ds.skipBytes(4);
    setErrorNumber(ds.readInt());
    logger.info("Colgate error number is :" + getErrorNumber());
    byte[] encodedPassword = new byte[33];
    ds.read(encodedPassword, 0, 33);
    setEncodedPassword(encodedPassword);
  }

  /**
   * tests the returned integer from Colgate and throws the appropriate exception
   *
   * @throws AuthenticationException
   */
  public void testExceptions() throws AuthenticationException {
    switch (getErrorNumber()) {
      case 0:
        return;
      case 11:
        throw new PasswordDuplicatesPreviousException("Colgate returned error number 11");
      default:
        throw new AuthenticationException("Colgate returned error number: " + getErrorNumber());
    }
  }

  /**
   * @return byte array of encoded oracle password
   */
  public byte[] getEncodedPassword() {
    return encodedPassword;
  }

  /**
   * set the encoded Oracle Password of this message
   */
  public void setEncodedPassword(byte[] bs) {
    encodedPassword = bs;
  }

  /**
   * @return errorNumber member
   */
  public int getErrorNumber() {
    return errorNumber;
  }

  /**
   * set the errorNumber member for this message
   */
  public void setErrorNumber(int i) {
    errorNumber = i;
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.SocketMessage#isMessageComplete(byte[])
   */
  public boolean isMessageComplete(byte[] b) {
    return b.length == expectedLength;
  }

}
