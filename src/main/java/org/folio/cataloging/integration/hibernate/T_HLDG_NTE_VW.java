/*
 * (c) LibriCore
 * 
 * Created on 27-jul-2004
 * 
 * T_HLDG_NT_VW.java
 */
package org.folio.cataloging.integration.hibernate;

import java.util.Date;

/**
 * @author elena
 * @version $Revision: 1.2 $, $Date: 2004/08/02 09:39:52 $
 * @since 1.0
 */
public class T_HLDG_NTE_VW {

	private short code;
	private short sequence;
	private short repeteable;
	private short languageCode;
	private String labelStringText;
	private Date lastUpdateDate;
	private short tableObsoletoIndex;
	private Date tableObsoletoDate;

	public short getCode() {
		return code;
	}

	public String getLabelStringText() {
		return labelStringText;
	}

	public short getLanguageCode() {
		return languageCode;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public short getRepeteable() {
		return repeteable;
	}

	public short getSequence() {
		return sequence;
	}

	public Date getTableObsoletoDate() {
		return tableObsoletoDate;
	}

	public short getTableObsoletoIndex() {
		return tableObsoletoIndex;
	}

	public void setCode(short s) {
		code = s;
	}

	public void setLabelStringText(String string) {
		labelStringText = string;
	}

	public void setLanguageCode(short s) {
		languageCode = s;
	}

	public void setLastUpdateDate(Date date) {
		lastUpdateDate = date;
	}

	public void setRepeteable(short s) {
		repeteable = s;
	}

	public void setSequence(short s) {
		sequence = s;
	}

	public void setTableObsoletoDate(Date date) {
		tableObsoletoDate = date;
	}

	public void setTableObsoletoIndex(short s) {
		tableObsoletoIndex = s;
	}

}
