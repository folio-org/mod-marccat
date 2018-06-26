package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.shared.CorrelationValues;

import java.io.Serializable;

/**
 * @author elena
 * @since 1.0
 */

public class Correlation implements Serializable {

	private CorrelationKey key;
	private int databaseFirstValue;
	private int databaseSecondValue;
	private int databaseThirdValue;
	private String searchIndexTypeCode;

	public CorrelationKey getKey() {
		return key;
	}

	public int getDatabaseFirstValue() {
		return databaseFirstValue;
	}

	public int getDatabaseSecondValue() {
		return databaseSecondValue;
	}

	public int getDatabaseThirdValue() {
		return databaseThirdValue;
	}

	public String getSearchIndexTypeCode() {
		return searchIndexTypeCode;
	}

	private void setKey(final CorrelationKey correlation) {
		key = correlation;
	}

	private void setDatabaseFirstValue(final int s) {
		databaseFirstValue = s;
	}

	private void setDatabaseSecondValue(final int s) {
		databaseSecondValue = s;
	}

	private void setDatabaseThirdValue(final int s) {
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
