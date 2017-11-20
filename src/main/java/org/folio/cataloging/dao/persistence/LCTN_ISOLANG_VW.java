/*
 * (c) LibriCore
 * 
 * Created on 16-jul-2004
 * 
 * LCTN_ISOLANG_VW.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;
import java.util.Date;


/**
 * @author Maite
 * @version $Revision: 1.5 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class LCTN_ISOLANG_VW implements Serializable {
	
	private LocationKey key;
	private String isoLanguage;
	private String labelStringText;
	private int tableSequenceNumber;
	private short tableObsoleteIndicator;
	private Date tableObsoleteDate;
	private Date listUpdateDate;

	
	
	public String getIsoLanguage() {
		return isoLanguage;
	}

	public LocationKey getKey() {
		return key;
	}

	public String getLabelStringText() {
		return labelStringText;
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

	public void setIsoLanguage(String string) {
		isoLanguage = string;
	}

	public void setKey(LocationKey key) {
		this.key = key;
	}

	public void setLabelStringText(String string) {
		labelStringText = string;
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

	public short getLocation() {
		return this.key.getLocationNumber();
	}

}
