/*
 * (c) LibriCore
 *
 * Created on 04-nov-2004
 *
 * S_MSG.java
 */
package org.folio.marccat.dao.persistence;

import java.io.Serializable;

/**
 * @author Maite
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class S_MSG implements Serializable {
  private int messageNumber;
  private short messageTypeCode;
  private String messageEnglishHelpName;
  private String messageFrenchHelpName;
  private String messageEnglishDescription;
  private String messageFrenchDescription;
  private String messageTechnicalDescription;


  /**
   * @return
   * @throws
   * @since 1.0
   */
  public String getMessageEnglishDescription() {
    return messageEnglishDescription;
  }

  /**
   * @param string
   * @throws
   * @since 1.0
   */
  public void setMessageEnglishDescription(String string) {
    messageEnglishDescription = string;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public String getMessageEnglishHelpName() {
    return messageEnglishHelpName;
  }

  /**
   * @param string
   * @throws
   * @since 1.0
   */
  public void setMessageEnglishHelpName(String string) {
    messageEnglishHelpName = string;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public String getMessageFrenchDescription() {
    return messageFrenchDescription;
  }

  /**
   * @param string
   * @throws
   * @since 1.0
   */
  public void setMessageFrenchDescription(String string) {
    messageFrenchDescription = string;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public String getMessageFrenchHelpName() {
    return messageFrenchHelpName;
  }

  /**
   * @param string
   * @throws
   * @since 1.0
   */
  public void setMessageFrenchHelpName(String string) {
    messageFrenchHelpName = string;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public int getMessageNumber() {
    return messageNumber;
  }

  /**
   * @param i
   * @throws
   * @since 1.0
   */
  public void setMessageNumber(int i) {
    messageNumber = i;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public String getMessageTechnicalDescription() {
    return messageTechnicalDescription;
  }

  /**
   * @param string
   * @throws
   * @since 1.0
   */
  public void setMessageTechnicalDescription(String string) {
    messageTechnicalDescription = string;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public short getMessageTypeCode() {
    return messageTypeCode;
  }

  /**
   * @param s
   * @throws
   * @since 1.0
   */
  public void setMessageTypeCode(short s) {
    messageTypeCode = s;
  }

}
