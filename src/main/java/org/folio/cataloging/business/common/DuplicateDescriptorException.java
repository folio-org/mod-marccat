/*
 * (c) LibriCore
 * 
 * Created on Nov 30, 2004
 * 
 * SortFormException.java
 */
package org.folio.cataloging.business.common;

import org.folio.cataloging.dao.persistence.Descriptor;


/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/12/02 17:20:52 $
 * @since 1.0
 */
public class DuplicateDescriptorException extends DataAccessException {

	private Descriptor descriptorFound = null;
	
	public DuplicateDescriptorException(Descriptor descriptorFound) {
		super();
		this.descriptorFound = descriptorFound;
	}

	public Descriptor getDescriptorFound() {
		return descriptorFound;
	}

	public void setDescriptorFound(Descriptor descriptorFound) {
		this.descriptorFound = descriptorFound;
	}

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public DuplicateDescriptorException() {
		super();
	}

	/**
	 * Class constructor
	 *
	 * @param message
	 * @since 1.0
	 */
	public DuplicateDescriptorException(String message) {
		super(message);
	}

	/**
	 * Class constructor
	 *
	 * @param message
	 * @param cause
	 * @since 1.0
	 */
	public DuplicateDescriptorException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Class constructor
	 *
	 * @param cause
	 * @since 1.0
	 */
	public DuplicateDescriptorException(Throwable cause) {
		super(cause);
	}

}
