package org.folio.cataloging.action.cataloguing.bibliographic;

import org.folio.cataloging.exception.ModCatalogingException;

public class SaveTagException extends ModCatalogingException {
	private String tagNumber;
	
	public SaveTagException() {
		super();
	}

	public SaveTagException(String message, Throwable cause) {
		super(message, cause);
	}

	public SaveTagException(String message) {
		super(message);
		setTagNumber(message);
	}

	public SaveTagException(Throwable cause) {
		super(cause);
	}
	/**
	 * 
	 * @since 1.0
	 */
	public String getTagNumber() {
		return tagNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setTagNumber(String string) {
		tagNumber = string;
	}
	protected int tagIndex = 0;

	/**
		 * 
		 * @since 1.0
		 */
	public int getTagIndex() {
		return tagIndex;
	}

	/**
		 * 
		 * @since 1.0
		 */
	public void setTagIndex(int i) {
		tagIndex = i;
	}


}
