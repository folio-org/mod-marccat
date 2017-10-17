/*
 * (c) LibriCore
 * 
 * Created on 01-jul-2004
 * 
 * T_ILL.java
 */
package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;


/**
 * @author hansv
 * @version $Revision: 1.3 $, $Date: 2005/12/21 13:33:33 $
 * @since 1.0
 */

public class T_SRCH_KYWRD_IDX implements Serializable{
	
	private int keywordIndexValueCode;
	private short keywordIndexSequenceNumber;
	private boolean keywordIndexValueObsoleteIndicator;
	private String keywordIndexShortText;	
	private String keywordIndexText;	
	private String language;
	 

		/**
	 * Getter for keywordIndexLongLanguage1Text
	 * 
	 * @return keywordIndexLongLanguage1Text
	 * @since 1.0
	 */
	public String getKeywordIndexText() {
		return keywordIndexText;
	}

	
	/**
	 * Getter for keywordIndexSequenceNumber
	 * 
	 * @return keywordIndexSequenceNumber
	 * @since 1.0
	 */
	public short getKeywordIndexSequenceNumber() {
		return keywordIndexSequenceNumber;
	}

	/**
	 * Getter for keywordIndexShortLanguage1Text
	 * 
	 * @return keywordIndexShortLanguage1Text
	 * @since 1.0
	 */
	public String getKeywordIndexShortText() {
		return keywordIndexShortText;
	}

		
	/**
	 * Getter for keywordIndexValueCode
	 * 
	 * @return keywordIndexValueCode
	 * @since 1.0
	 */
	public int getKeywordIndexValueCode() {
		return keywordIndexValueCode;
	}

	/**
	 * Getter for keywordIndexValueObsoleteIndicator
	 * 
	 * @return keywordIndexValueObsoleteIndicator
	 * @since 1.0
	 */
	public boolean isKeywordIndexValueObsoleteIndicator() {
		return keywordIndexValueObsoleteIndicator;
	}

	
	/**
	 * Setter for keywordIndexLongLanguage1Text
	 * 
	 * @param string keywordIndexLongLanguage1Text
	 * @since 1.0
	 */
	public void setKeywordIndexText(String string) {
		keywordIndexText = string;
	}

		/**
	 * Setter for keywordIndexSequenceNumber
	 * 
	 * @param s keywordIndexSequenceNumber
	 * @since 1.0
	 */
	public void setKeywordIndexSequenceNumber(short s) {
		keywordIndexSequenceNumber = s;
	}

	/**
	 * Setter for keywordIndexShortLanguage1Text
	 * 
	 * @param string keywordIndexShortLanguage1Text
	 * @since 1.0
	 */
	public void setKeywordIndexShortText(String string) {
		keywordIndexShortText = string;
	}

	/**
	 * Setter for keywordIndexValueCode
	 * 
	 * @param i keywordIndexValueCode
	 * @since 1.0
	 */
	public void setKeywordIndexValueCode(int i) {
		keywordIndexValueCode = i;
	}

	/**
	 * Setter for keywordIndexValueObsoleteIndicator
	 * 
	 * @param b keywordIndexValueObsoleteIndicator
	 * @since 1.0
	 */
	public void setKeywordIndexValueObsoleteIndicator(boolean b) {
		keywordIndexValueObsoleteIndicator = b;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * 
	 * 
	 * @param string
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setLanguage(String string) {
		language = string;
	}

	public boolean equals(Object obj) {
				if (!(obj instanceof T_SRCH_KYWRD_IDX))
					return false;
				return (((T_SRCH_KYWRD_IDX) obj).getKeywordIndexValueCode() == getKeywordIndexValueCode())
					&& (((T_SRCH_KYWRD_IDX) obj).getLanguage().equals(getLanguage()));			
			}

			/* (non-Javadoc)
			 * @see java.lang.Object#hashCode()
			 */
	public int hashCode() {
				return getKeywordIndexValueCode() + getLanguage().hashCode();
			}

}
