package org.folio.marccat.business.cataloguing.common;

import org.folio.marccat.shared.CorrelationValues;
import java.io.Serializable;



public abstract class HeaderFieldHelper implements HeaderField, Serializable {

  protected int headerType = -1;

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
