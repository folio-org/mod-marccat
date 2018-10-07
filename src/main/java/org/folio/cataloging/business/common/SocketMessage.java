/*
 * Created on May 13, 2004
 *
 */
package org.folio.cataloging.business.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Abstract class to support sending and receiving
 * of socket messages.
 *
 * @author paulm
 */
public abstract class SocketMessage {
  static int timeout = Defaults.getInteger ("socket.timeout.seconds") * 1000;
  static private Log logger = LogFactory.getLog (SocketMessage.class);

  /**
   * pads a byte array (in) to a specific length.  Padding with 0 bytes.
   *
   * @param in  - the input byte array to be padded
   * @param len - the resulting length of the new byte array
   * @return the padded result
   */
  static public byte[] pad(byte[] in, int len) {
    byte[] result = new byte[len];

    for ( int i = 0; i < in.length; i++ ) {
      result[i] = in[i];
    }

    for ( int i = in.length; i < len; i++ ) {
      result[i] = 0;
    }

    return result;
  }

  /**
   * @since 1.0
   */
  public static int getTimeout() {
    return timeout;
  }

  /**
   * @since 1.0
   */
  public static void setTimeout(int i) {
    timeout = i;
  }

  /**
   * copies the members of the class into a byte array, suitable for streaming to a socket
   *
   * @return the resultant byte array
   * @throws IOException when stream IO errors occur.
   */
  abstract public byte[] asByteArray() throws IOException;

  /**
   * extracts the individual members of the class from a byte array
   *
   * @param msg - the input byte array containing the member data
   * @throws IOException - when underlying streams have IO errors
   */
  abstract public void fromByteArray(byte[] msg) throws IOException;

  /**
   * @return whether this message as received on the socket is complete
   */
  abstract public boolean isMessageComplete(byte[] b);

  /**
   * sends this message on the specified socket
   *
   * @param s - the socket
   * @throws IOException when IO errors occur on the stream
   */
  public void send(Socket s)
    throws IOException {
    s.getOutputStream ( ).write (this.asByteArray ( ));
  }

  /**
   * Receives this message from a socket
   *
   * @param s - the socket
   * @throws IOException - when IO errors occur on the stream
   */
  public void recv(Socket s)
    throws IOException {
    byte[] buf = new byte[512];
    ByteArrayOutputStream out = new ByteArrayOutputStream ( );
    int rc = 0;
    int bytesRead = 0;

    s.setSoTimeout (getTimeout ( ));
    while (this.isMessageComplete (out.toByteArray ( )) == false) {
      rc = s.getInputStream ( ).read (buf);
      if (rc > 0) {
        //			logger.debug("got " + rc + " bytes");
        bytesRead = bytesRead + rc;
        out.write (buf, 0, rc);
      } else {
        // read returns -1 ==> EOF
        logger.warn ("Did not receive expected number of bytes");
        break;
      }
    }
    this.fromByteArray (out.toByteArray ( ));
  }

}

