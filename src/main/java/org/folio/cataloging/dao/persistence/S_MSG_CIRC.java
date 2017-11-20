/*
 * (c) LibriCore
 * 
 * Created on 14-ene-2005
 * 
 * S_MSG_CIRC.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author Elena
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class S_MSG_CIRC implements Serializable{

	private int messageNumber;
	private short messageTypeCode;
	private String messageEnglishHelpName;
	private String messageFrenchHelpName;
	private String messageEnglishDescription;
	private String messageFrenchDescription;
	private String messageTechnicalDescription; 

    /**
     * @return Returns the messageEnglishDescription.
     * @exception
     * @since 1.0
     */
    public String getMessageEnglishDescription() {
        return messageEnglishDescription;
    }
    /**
     * @param messageEnglishDescription The messageEnglishDescription to set.
     * @exception
     * @since 1.0
     */
    public void setMessageEnglishDescription(String messageEnglishDescription) {
        this.messageEnglishDescription = messageEnglishDescription;
    }
    /**
     * @return Returns the messageEnglishHelpName.
     * @exception
     * @since 1.0
     */
    public String getMessageEnglishHelpName() {
        return messageEnglishHelpName;
    }
    /**
     * @param messageEnglishHelpName The messageEnglishHelpName to set.
     * @exception
     * @since 1.0
     */
    public void setMessageEnglishHelpName(String messageEnglishHelpName) {
        this.messageEnglishHelpName = messageEnglishHelpName;
    }
    /**
     * @return Returns the messageFrenchDescription.
     * @exception
     * @since 1.0
     */
    public String getMessageFrenchDescription() {
        return messageFrenchDescription;
    }
    /**
     * @param messageFrenchDescription The messageFrenchDescription to set.
     * @exception
     * @since 1.0
     */
    public void setMessageFrenchDescription(String messageFrenchDescription) {
        this.messageFrenchDescription = messageFrenchDescription;
    }
    /**
     * @return Returns the messageFrenchHelpName.
     * @exception
     * @since 1.0
     */
    public String getMessageFrenchHelpName() {
        return messageFrenchHelpName;
    }
    /**
     * @param messageFrenchHelpName The messageFrenchHelpName to set.
     * @exception
     * @since 1.0
     */
    public void setMessageFrenchHelpName(String messageFrenchHelpName) {
        this.messageFrenchHelpName = messageFrenchHelpName;
    }
    /**
     * @return Returns the messageNumber.
     * @exception
     * @since 1.0
     */
    public int getMessageNumber() {
        return messageNumber;
    }
    /**
     * @param messageNumber The messageNumber to set.
     * @exception
     * @since 1.0
     */
    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
    }
    /**
     * @return Returns the messageTechnicalDescription.
     * @exception
     * @since 1.0
     */
    public String getMessageTechnicalDescription() {
        return messageTechnicalDescription;
    }
    /**
     * @param messageTechnicalDescription The messageTechnicalDescription to set.
     * @exception
     * @since 1.0
     */
    public void setMessageTechnicalDescription(
            String messageTechnicalDescription) {
        this.messageTechnicalDescription = messageTechnicalDescription;
    }
    /**
     * @return Returns the messageTypeCode.
     * @exception
     * @since 1.0
     */
    public short getMessageTypeCode() {
        return messageTypeCode;
    }
    /**
     * @param messageTypeCode The messageTypeCode to set.
     * @exception
     * @since 1.0
     */
    public void setMessageTypeCode(short messageTypeCode) {
        this.messageTypeCode = messageTypeCode;
    }
}
