/*
 * (c) LibriCore
 *
 * Created on Jun 23, 2006
 *
 * ConversionOutRequest.java
 */
package org.folio.marccat.business.colgate;

import org.folio.marccat.business.common.SocketMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2006/12/14 10:31:06 $
 * @since 1.0
 */
public class ConversionOutRequest extends SocketMessage {
  private boolean bibliographic;
  private int amicusNumber;
  private int searchingView;
  private String librarySymbol;
  private String elementSetName;

  public ConversionOutRequest(
    boolean bibliographic,
    int amicusNumber,
    int searchingView,
    String elementSetName,
    String librarySymbol) {
    setBibliographic(bibliographic);
    setAmicusNumber(amicusNumber);
    setElementSetName(elementSetName);
    setSearchingView(searchingView);
    setLibrarySymbol(librarySymbol);
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.SocketMessage#asByteArray()
   */
  public byte[] asByteArray() throws IOException {
    ByteArrayOutputStream bs = new ByteArrayOutputStream();
    OutputStreamWriter ow = new OutputStreamWriter(bs, "US-ASCII");
    ow.write(bibliographic ? "4" : "3");// 4 for bibliographic and archives, 3 for authority
    ow.flush();
    ow.write(new DecimalFormat("0000000000").format(amicusNumber));
    ow.flush();

    if (searchingView < 0) {
      ow.write(new DecimalFormat("0").format(searchingView));
    } else {
      ow.write(new DecimalFormat("00").format(searchingView));
    }
    ow.flush();
    bs.write(pad(librarySymbol.getBytes("US-ASCII"), 17));
    String flags = "-fauh" + (elementSetName.equals("B") ? "b" : "");
    bs.write(pad(flags.getBytes("US-ASCII"), 21));
    return bs.toByteArray();
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.SocketMessage#fromByteArray(byte[])
   */
  public void fromByteArray(byte[] msg) throws IOException {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see librisuite.business.common.SocketMessage#isMessageComplete(byte[])
   */
  public boolean isMessageComplete(byte[] b) {
    // TODO Auto-generated method stub
    return false;
  }

  /**
   * @since 1.0
   */
  public int getAmicusNumber() {
    return amicusNumber;
  }

  /**
   * @since 1.0
   */
  public void setAmicusNumber(int i) {
    amicusNumber = i;
  }

  /**
   * @since 1.0
   */
  public boolean isBibliographic() {
    return bibliographic;
  }

  /**
   * @since 1.0
   */
  public void setBibliographic(boolean b) {
    bibliographic = b;
  }

  /**
   * @since 1.0
   */
  public String getElementSetName() {
    return elementSetName;
  }

  /**
   * @since 1.0
   */
  public void setElementSetName(String string) {
    elementSetName = string;
  }

  /**
   * @since 1.0
   */
  public String getLibrarySymbol() {
    return librarySymbol;
  }

  /**
   * @since 1.0
   */
  public void setLibrarySymbol(String string) {
    librarySymbol = string;
  }

  /**
   * @since 1.0
   */
  public int getSearchingView() {
    return searchingView;
  }

  /**
   * @since 1.0
   */
  public void setSearchingView(int i) {
    searchingView = i;
  }

}
