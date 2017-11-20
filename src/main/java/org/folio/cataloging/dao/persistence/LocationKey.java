/*
 * (c) LibriCore
 * 
 * Created on 15-jul-2004
 * 
 * LocationKey.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author elena
 * @version $Revision: 1.7 $, $Date: 2005/12/21 13:33:33 $
 * @since 1.0
 */
public class LocationKey implements Serializable {

	private int organisationNumber;
	private short locationNumber;
	private String language;
	

	/**
	 * Class constructor
	 * @since 1.0
	 */

	public LocationKey() {
		super();
	}
		
	/**
	 * Class constructor
	 * @since 1.0
	 */

	public LocationKey(int orgNbr, short locNbr) {
		this.setOrganisationNumber(orgNbr);
		this.setLocationNumber(locNbr);
	}

	/**
	 * override equals and hashcode for hibernate key comparison
	 */
	public boolean equals(Object anObject) {
		if (anObject instanceof LocationKey) {
			LocationKey aKey = (LocationKey) anObject;
			return (
			organisationNumber == aKey.getOrganisationNumber()
					&& locationNumber == aKey.getLocationNumber()
					&& language.equals(aKey.language));
		} else {
			return false;
		}
	}

	public int hashCode() {
		return organisationNumber + locationNumber + getLanguage().hashCode();
	}


	public int getOrganisationNumber() {
		return organisationNumber;
	}


	public void setOrganisationNumber(int i) {
		organisationNumber = i;
	}

	public short getLocationNumber() {
		return locationNumber;
	}

	public void setLocationNumber(short s) {
		locationNumber = s;
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

}
