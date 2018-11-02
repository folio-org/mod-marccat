/*
 * (c) LibriCore
 *
 * Created on Nov 4, 2004
 *
 * HeaderField.java
 */
package org.folio.marccat.business.cataloguing.common;

import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.shared.CorrelationValues;

import java.util.List;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
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
