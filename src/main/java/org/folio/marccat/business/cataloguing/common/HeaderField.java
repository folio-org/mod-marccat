package org.folio.marccat.business.cataloguing.common;

import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.shared.CorrelationValues;

import java.util.List;


public interface HeaderField {
  int getCategory();

  CorrelationValues getCorrelationValues();

  void setCorrelationValues(CorrelationValues v);

  List getFirstCorrelationList() throws DataAccessException;

  List getSecondCorrelationList(int value1) throws DataAccessException;

  List getThirdCorrelationList(int value1, int value2) throws DataAccessException;

  int getHeaderType();

  void setHeaderType(int s);

  boolean isHeaderField();
}
