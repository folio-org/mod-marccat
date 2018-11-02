/*
 * Created on May 13, 2004
 *
 * ColgateLoginRequest.java
 */
package org.folio.marccat.business.colgate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.common.SocketMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Represents the colgate message structure for login requests
 *
 * @author paulm
 */
public class ColgateLoginRequest extends SocketMessage {
  static private Log logger = LogFactory.getLog(ColgateLoginRequest.class);
  static private byte[] type = {56};
  private byte[] name;
  private byte[] password;

  /**
   * Class constructor
   *
   * @param name     - byte array of Latin-1 characters representing the user name
   * @param password - byte array of md5 hex representation of password
   */
  public ColgateLoginRequest(byte[] name, byte[] password) {
    setName(name);
    setPassword(password);
  }


  /**
   * Class constructor
   *
   * @param name          - string representing the user name
   * @param clearPassword - string representing the clear text of the user's password
   */
  public ColgateLoginRequest(String name, String clearPassword) {
    setName(Colgate.asIsoByte(name));
    setPassword(Colgate.encryptPassword(clearPassword));
  }

  /**
   * @return the type of this message
   */
  public static byte[] getType() {
    return type;
  }

  /**
   * @return the name member
   */
  public byte[] getName() {
    return name;
  }

  /**
   * set the name member (pads to length 33)
   */
  public void setName(byte[] name) {
    this.name = SocketMessage.pad(name, 33);
  }

  /**
   * @return the name member
   */
  public byte[] getPassword() {
    return password;
  }

  /**
   * set the password member (pads to length 33)
   */
  public void setPassword(byte[] password) {
    this.password = SocketMessage.pad(password, 33);
  }

  /* (non-Javadoc)
   * @see com.libricore.librisuite.business.ColgateMessage#asByteArray()
   */
  public byte[] asByteArray()
    throws IOException {
    ByteArrayOutputStream s = new ByteArrayOutputStream();

    s.write(getType());
    s.write(getName());
    s.write(getPassword());
    return s.toByteArray();
  }

  /* (non-Javadoc)
   * @see com.libricore.librisuite.business.ColgateMessage#fromByteArray(byte[])
   */
  public void fromByteArray(byte[] msg) throws IOException {
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.SocketMessage#isMessageComplete(byte[])
   */
  public boolean isMessageComplete(byte[] b) {
    // TODO Auto-generated method stub
    return false;
  }

}
