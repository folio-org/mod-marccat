package org.folio.marccat.business.cataloguing.common;

import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.shared.CorrelationValues;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


public abstract class HeaderFieldHelper implements HeaderField, Serializable {

  protected int headerType = -1;


  public List getFirstCorrelationList() throws DataAccessException {
    return Collections.emptyList();
  }

  public List getSecondCorrelationList(int value1) throws DataAccessException {
    return Collections.emptyList();
  }

  public List getThirdCorrelationList(int value1, int value2) throws DataAccessException {
    return Collections.emptyList();
  }

  public int getHeaderType() {
    return headerType;
  }

  public void setHeaderType(int s) {
    headerType = s;
  }

  public final boolean isHeaderField() {
    return true;
  }

  public CorrelationValues getCorrelationValues() {
    return (new CorrelationValues()).change(1, getHeaderType());
  }

  public void setCorrelationValues(CorrelationValues v) {
    setHeaderType(v.getValue(1));
  }

}
