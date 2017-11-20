/*
 * (c) LibriCore
 * 
 * Created on Nov 17, 2004
 * 
 * ModuleAuthorisation.java
 */
package org.folio.cataloging.business.authorisation;

import java.io.Serializable;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/11/19 16:48:32 $
 * @since 1.0
 */
public class ModuleAuthorisation implements Serializable {
	private String moduleCode;
	private String userAccount;
	private int authorisationLevel;

	/**
	 * 
	 * @since 1.0
	 */
	public int getAuthorisationLevel() {
		return authorisationLevel;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getModuleCode() {
		return moduleCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getUserAccount() {
		return userAccount;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setAuthorisationLevel(int i) {
		authorisationLevel = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setModuleCode(String string) {
		moduleCode = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setUserAccount(String string) {
		userAccount = string;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		if (arg0 instanceof ModuleAuthorisation) {
			ModuleAuthorisation ma = (ModuleAuthorisation)arg0;
			return ma.getUserAccount().equals(getUserAccount()) &&
				   ma.getModuleCode().equals(getModuleCode());
		}
		else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getUserAccount().hashCode() +
			   getModuleCode().hashCode();
	}

}
