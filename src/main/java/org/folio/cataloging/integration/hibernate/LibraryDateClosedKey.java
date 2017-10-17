/*
 * (c) LibriCore
 * 
 * Created on 02-nov-2004
 * 
 * LibraryDateClosedKey.java
 */
package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Maite
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class LibraryDateClosedKey implements Serializable{
	private int organisationNumber;
	private Date libraryDateCloseDate;
	
	
	
    public LibraryDateClosedKey() {
        super();
    }
    
    
    /**
     * @param organisationNumber
     * @param libraryDateCloseDate
     */
    public LibraryDateClosedKey(int organisationNumber, Date libraryDateCloseDate) {
        this.organisationNumber = organisationNumber;
        this.libraryDateCloseDate = libraryDateCloseDate;
    }
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	 
	public boolean equals(Object obj) {
		if (!(obj instanceof LibraryDateClosedKey))
			return false;
		return super.equals(obj)
			&& (((LibraryDateClosedKey) obj).getOrganisationNumber() == getOrganisationNumber()) &&
		(0 == ((LibraryDateClosedKey) obj).getLibraryDateCloseDate().compareTo(getLibraryDateCloseDate()));
		//TODO don't know if this is right
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getOrganisationNumber(); //TODO don't know if this is right
	}
	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public Date getLibraryDateCloseDate() {
		return libraryDateCloseDate;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public int getOrganisationNumber() {
		return organisationNumber;
	}

	/**
	 * 
	 * 
	 * @param date 
	 * @exception
	 * @since 1.0
	 */
	public void setLibraryDateCloseDate(Date date) {
		libraryDateCloseDate = date;
	}

	/**
	 * 
	 * 
	 * @param i 
	 * @exception
	 * @since 1.0
	 */
	public void setOrganisationNumber(int i) {
		organisationNumber = i;
	}

}
