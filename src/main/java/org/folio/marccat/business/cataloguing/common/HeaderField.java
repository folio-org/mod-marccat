package org.folio.marccat.business.cataloguing.common;

import org.folio.marccat.shared.CorrelationValues;


public interface HeaderField {
  int getCategory();

  CorrelationValues getCorrelationValues();

  void setCorrelationValues(CorrelationValues v);

  int getHeaderType();

  void setHeaderType(int s);

  boolean isHeaderField();
}
