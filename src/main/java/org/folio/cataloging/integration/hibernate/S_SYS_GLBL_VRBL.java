/*
 * Created on May 6, 2004
 * */
package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;

/**
 * Hibernate class for table S_SYS_GLBL_VRBL
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2004/07/22 09:30:01 $
 * @since 1.0
 */
public class S_SYS_GLBL_VRBL implements Serializable {
	private String name;
	private String value;
	private String note;

	/**
	 * Getter for name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for value
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Setter for name
	 * 
	 * @param string name
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * Setter for value
	 * 
	 * @param string value
	 */
	public void setValue(String string) {
		value = string;
	}

	/**
	 * Getter for note
	 * 
	 * @return note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * Setter for note
	 * 
	 * @param string note
	 */
	public void setNote(String string) {
		note = string;
	}

}
