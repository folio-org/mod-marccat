/*
 * (c) LibriCore
 * 
 * Created on 28-jul-2004
 * 
 * S_BIB_MARC_IND_DB_CRLTN.java
 */
package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;

import librisuite.business.common.CorrelationValues;

/**
 * @author elena
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:05 $
 * @since 1.0
 */
public class Correlation implements Serializable {

	private CorrelationKey key;
	private short databaseFirstValue;
	private short databaseSecondValue;
	private short databaseThirdValue;
	private String searchIndexTypeCode;

	public CorrelationKey getKey() {
		return key;
	}

	public short getDatabaseFirstValue() {
		return databaseFirstValue;
	}

	public short getDatabaseSecondValue() {
		return databaseSecondValue;
	}

	public short getDatabaseThirdValue() {
		return databaseThirdValue;
	}

	public String getSearchIndexTypeCode() {
		return searchIndexTypeCode;
	}

	private void setKey(CorrelationKey correlation) {
		key = correlation;
	}

	private void setDatabaseFirstValue(short s) {
		databaseFirstValue = s;
	}

	private void setDatabaseSecondValue(short s) {
		databaseSecondValue = s;
	}

	private void setDatabaseThirdValue(short s) {
		databaseThirdValue = s;
	}

	private void setSearchIndexTypeCode(String s) {
		searchIndexTypeCode = s;
	}

	public CorrelationValues getValues() {
		return new CorrelationValues(getDatabaseFirstValue(),
				getDatabaseSecondValue(), getDatabaseThirdValue());
	}

}
