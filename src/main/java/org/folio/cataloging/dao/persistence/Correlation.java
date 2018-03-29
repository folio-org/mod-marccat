package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.shared.CorrelationValues;

import java.io.Serializable;

/**
 * @author elena
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

	private void setKey(final CorrelationKey correlation) {
		key = correlation;
	}

	private void setDatabaseFirstValue(final short s) {
		databaseFirstValue = s;
	}

	private void setDatabaseSecondValue(final short s) {
		databaseSecondValue = s;
	}

	private void setDatabaseThirdValue(final short s) {
		databaseThirdValue = s;
	}

	private void setSearchIndexTypeCode(final String s) {
		searchIndexTypeCode = s;
	}

	public CorrelationValues getValues() {
		return new CorrelationValues(getDatabaseFirstValue(),
				getDatabaseSecondValue(), getDatabaseThirdValue());
	}

}
