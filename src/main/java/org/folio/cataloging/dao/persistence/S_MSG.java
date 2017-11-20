/*
 * (c) LibriCore
 * 
 * Created on 04-nov-2004
 * 
 * S_MSG.java
 */
package org.folio.cataloging.dao.persistence;

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
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public String getMessageEnglishDescription() {
		return messageEnglishDescription;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public String getMessageEnglishHelpName() {
		return messageEnglishHelpName;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public String getMessageFrenchDescription() {
		return messageFrenchDescription;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public String getMessageFrenchHelpName() {
		return messageFrenchHelpName;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public int getMessageNumber() {
		return messageNumber;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public String getMessageTechnicalDescription() {
		return messageTechnicalDescription;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public short getMessageTypeCode() {
		return messageTypeCode;
	}

	/**
	 * 
	 * 
	 * @param string 
	 * @exception
	 * @since 1.0
	 */
	public void setMessageEnglishDescription(String string) {
		messageEnglishDescription = string;
	}

	/**
	 * 
	 * 
	 * @param string 
	 * @exception
	 * @since 1.0
	 */
	public void setMessageEnglishHelpName(String string) {
		messageEnglishHelpName = string;
	}

	/**
	 * 
	 * 
	 * @param string 
	 * @exception
	 * @since 1.0
	 */
	public void setMessageFrenchDescription(String string) {
		messageFrenchDescription = string;
	}

	/**
	 * 
	 * 
	 * @param string 
	 * @exception
	 * @since 1.0
	 */
	public void setMessageFrenchHelpName(String string) {
		messageFrenchHelpName = string;
	}

	/**
	 * 
	 * 
	 * @param i 
	 * @exception
	 * @since 1.0
	 */
	public void setMessageNumber(int i) {
		messageNumber = i;
	}

	/**
	 * 
	 * 
	 * @param string 
	 * @exception
	 * @since 1.0
	 */
	public void setMessageTechnicalDescription(String string) {
		messageTechnicalDescription = string;
	}

	/**
	 * 
	 * 
	 * @param s 
	 * @exception
	 * @since 1.0
	 */
	public void setMessageTypeCode(short s) {
		messageTypeCode = s;
	}

}
