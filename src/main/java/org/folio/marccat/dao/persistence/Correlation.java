package org.folio.marccat.dao.persistence;

import org.folio.marccat.shared.CorrelationValues;

import java.io.Serializable;

/**
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

  public void setKey(final CorrelationKey correlation) {
    key = correlation;
  }

  public int getDatabaseFirstValue() {
    return databaseFirstValue;
  }

  public void setDatabaseFirstValue(final int s) {
    databaseFirstValue = s;
  }

  public int getDatabaseSecondValue() {
    return databaseSecondValue;
  }

  public void setDatabaseSecondValue(final int s) {
    databaseSecondValue = s;
  }

  public int getDatabaseThirdValue() {
    return databaseThirdValue;
  }

  public void setDatabaseThirdValue(final int s) {
    databaseThirdValue = s;
  }

  public String getSearchIndexTypeCode() {
    return searchIndexTypeCode;
  }

  public void setSearchIndexTypeCode(final String s) {
    searchIndexTypeCode = s;
  }

  public CorrelationValues getValues() {
    return new CorrelationValues(getDatabaseFirstValue(),
      getDatabaseSecondValue(), getDatabaseThirdValue());
  }

}
