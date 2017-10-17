/*
 * (c) LibriCore
 * 
 * Created on 15-jul-2004
 * 
 * LCTN_VW.java
 */
package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;
import java.util.Date;

/**
 * @author elena
 * @version $Revision: 1.5 $, $Date: 2005/05/18 13:07:28 $
 * @since 1.0
 */
public class LCTN_VW implements Serializable{

	private int organisationNumber;
	private short locationNumber;
	private int languageCode;
	private String labelStringText;
	private int tableSequenceNumber;
	private short tableObsoleteIndicator;
	private Date tableObsoleteDate;
	private Date listUpdateDate;
	

	

	public String getLabelStringText() {
		return labelStringText;
	}

	public int getLanguageCode() {
		return languageCode;
	}

	public Date getListUpdateDate() {
		return listUpdateDate;
	}

	public Date getTableObsoleteDate() {
		return tableObsoleteDate;
	}

	public short getTableObsoleteIndicator() {
		return tableObsoleteIndicator;
	}

	public int getTableSequenceNumber() {
		return tableSequenceNumber;
	}

	public void setLabelStringText(String string) {
		labelStringText = string;
	}

	public void setLanguageCode(int i) {
		languageCode = i;
	}

	public void setListUpdateDate(Date date) {
		listUpdateDate = date;
	}

	public void setTableObsoleteDate(Date date) {
		tableObsoleteDate = date;
	}

	public void setTableObsoleteIndicator(short s) {
		tableObsoleteIndicator = s;
	}

	public void setTableSequenceNumber(int i) {
		tableSequenceNumber = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public short getLocationNumber() {
		return locationNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getOrganisationNumber() {
		return organisationNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setLocationNumber(short s) {
		locationNumber = s;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setOrganisationNumber(int i) {
		organisationNumber = i;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof LCTN_VW)) {
			return false;
		}
		LCTN_VW obj = (LCTN_VW)arg0;
		return obj.getOrganisationNumber() == this.getOrganisationNumber() &&
			   obj.getLocationNumber() == this.getLocationNumber() &&
			   obj.getLanguageCode() == this.getLanguageCode();
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getOrganisationNumber() + getLocationNumber() + getLanguageCode();
	}

}
