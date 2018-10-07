/*
 * (c) LibriCore
 *
 * Created on Jun 23, 2006
 *
 * ConversionOutResponse.java
 */
package org.folio.cataloging.business.colgate;

import org.folio.cataloging.business.common.SocketMessage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/07/11 08:01:05 $
 * @since 1.0
 */
public class ConversionOutResponse extends SocketMessage {
  private int errorCode;
  private String marcRecord;

  /* (non-Javadoc)
   * @see librisuite.business.common.SocketMessage#asByteArray()
   */
  public byte[] asByteArray() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.SocketMessage#fromByteArray(byte[])
   */
  public void fromByteArray(byte[] msg) throws IOException {
    String message = new String (msg, "UTF-8");
    setErrorCode (Integer.parseInt (message.substring (1, 2)));
    setMarcRecord (message.substring (8));
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.SocketMessage#isMessageComplete(byte[])
   */
  public boolean isMessageComplete(byte[] b) {
    String recLen;
    if (b.length < 8) {
      return false;
    }
    try {
      recLen = new String (b, "US-ASCII").substring (2, 7);
      return b.length >= 8 + Integer.parseInt (recLen);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException ("US-ASCII not supported???");
    }

  }

  /**
   * @since 1.0
   */
  public int getErrorCode() {
    return errorCode;
  }

  /**
   * @since 1.0
   */
  public void setErrorCode(int i) {
    errorCode = i;
  }

  /**
   * @since 1.0
   */
  public String getMarcRecord() {
    return marcRecord;
  }

  /**
   * @since 1.0
   */
  public void setMarcRecord(String string) {
    marcRecord = string;
  }

}
