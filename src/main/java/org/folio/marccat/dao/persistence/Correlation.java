package org.folio.marccat.dao.persistence;

import org.folio.marccat.shared.CorrelationValues;

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

  private void setKey(final CorrelationKey correlation) {
    key = correlation;
  }

  public int getDatabaseFirstValue() {
    return databaseFirstValue;
  }

  private void setDatabaseFirstValue(final int s) {
    databaseFirstValue = s;
  }

  public int getDatabaseSecondValue() {
    return databaseSecondValue;
  }

  private void setDatabaseSecondValue(final int s) {
    databaseSecondValue = s;
  }

  public int getDatabaseThirdValue() {
    return databaseThirdValue;
  }

  private void setDatabaseThirdValue(final int s) {
    databaseThirdValue = s;
  }

  public String getSearchIndexTypeCode() {
    return searchIndexTypeCode;
  }

  private void setSearchIndexTypeCode(final String s) {
    searchIndexTypeCode = s;
  }

  public CorrelationValues getValues() {
    return new CorrelationValues(getDatabaseFirstValue(),
      getDatabaseSecondValue(), getDatabaseThirdValue());
  }

}
