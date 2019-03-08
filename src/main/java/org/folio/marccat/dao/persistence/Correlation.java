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

  public CorrelationValues getValues() {
    return new CorrelationValues(getDatabaseFirstValue(),
      getDatabaseSecondValue(), getDatabaseThirdValue());
  }

}
