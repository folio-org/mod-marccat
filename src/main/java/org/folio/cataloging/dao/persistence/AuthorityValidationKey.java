/*
 * (c) LibriCore
 * 
 * Created on Nov 18, 2005
 * 
 * AuthorityValidationKey.java
 */
package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.shared.ValidationKey;

import java.io.Serializable;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:05 $
 * @since 1.0
 */
public class AuthorityValidationKey extends ValidationKey implements Serializable{

	private String headingType;
	
	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public AuthorityValidationKey() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
	 * 
	 * @since 1.0
	 */
	public String getHeadingType() {
		return headingType;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setHeadingType(String string) {
		headingType = string;
	}

}
