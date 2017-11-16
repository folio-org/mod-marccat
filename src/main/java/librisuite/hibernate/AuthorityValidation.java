/*
 * (c) LibriCore
 * 
 * Created on Nov 18, 2005
 * 
 * AuthorityValidation.java
 */
package librisuite.hibernate;

import librisuite.business.cataloguing.common.Validation;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:05 $
 * @since 1.0
 */
public class AuthorityValidation extends Validation {
	private AuthorityValidationKey key;
	
	
	/**
	 * 
	 * @since 1.0
	 */
	public ValidationKey getKey() {
		return key;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setKey(AuthorityValidationKey key) {
		this.key = key;
	}

}
