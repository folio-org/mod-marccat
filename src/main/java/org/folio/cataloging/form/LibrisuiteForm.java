/*
 * (c) LibriCore
 * 
 * Created on Jul 1, 2004
 * 
 * LibrisuiteForm.java
 */
package org.folio.cataloging.form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This abstract class can be used as a super class for all ActionForms.
 * All methods common or related to ActionForm can be in this class.
 * 
 * @author Wim Crols
 * @version $Revision: 1.2 $, $Date: 2004/08/30 14:26:28 $
 * @since 1.0
 */
public abstract class LibrisuiteForm  {

	private static Log logger = LogFactory.getLog(LibrisuiteForm.class);

	/**
	 * If this boolean is set then an error should be displayed.
	 */
	private boolean error = false;

	/**
	 * If error is set to true errorId should be set to the property key
	 * for displaying the correct error message.
	 */
	private String errorId = new String("");

	/**
	 * Getter for error
	 * Error is a boolean which is set if
	 * an error message should be displayed on screen.
	 * 
	 * @return error
	 * @since 1.0
	 */
	public boolean isError() {
		return this.error;
	}

	/**
	 * Getter for errorId
	 * ErrorId is the name which is used as a property key for displaying
	 * the error in the choosen language.
	 * 
	 * @return errorId
	 * @since 1.0
	 */
	public String getErrorId() {
		return this.errorId;
	}

	/**
	 * Setter for error
	 * 
	 * @param error error to be set
	 * @since 1.0
	 */
	public void setError(boolean error) {
		this.error = error;
	}

	/**
	 * Setter for errorId
	 * 
	 * @param errorId errorId to be set
	 * @since 1.0
	 */
	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}
	
	public Integer asInteger(String s) {
		try {
			return Integer.valueOf(s);
		}
		catch (Exception e) {
			return null;
		}
	}
}
