package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author Hansv
 * @version $Revision: 1.1 $, $Date: 2005/12/21 13:33:34 $ 
 */
public class LCTN implements Serializable {
	
	private LocationKey key;	
	private String labelStringText;
	private int tableSequenceNumber;
	private short tableObsoleteIndicator;
	
	public LocationKey getKey() {
		return key;
	}

	public String getLabelStringText() {
		return labelStringText;
	}

	public short getTableObsoleteIndicator() {
		return tableObsoleteIndicator;
	}

	public int getTableSequenceNumber() {
		return tableSequenceNumber;
	}

	public void setKey(LocationKey key) {
		this.key = key;
	}

	public void setLabelStringText(String string) {
		labelStringText = string;
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
