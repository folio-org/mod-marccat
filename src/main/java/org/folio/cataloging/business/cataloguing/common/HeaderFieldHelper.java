/*
 * (c) LibriCore
 *
 * Created on Nov 4, 2004
 *
 * HeaderFieldHelper.java
 */
package org.folio.cataloging.business.cataloguing.common;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.DAOCodeTable;
import org.folio.cataloging.shared.CorrelationValues;

import java.io.Serializable;
import java.util.List;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public abstract class HeaderFieldHelper implements HeaderField, Serializable {
  private static DAOCodeTable daoCodeTable = new DAOCodeTable ( );

  protected int headerType = -1;

  abstract public int getCategory();

  abstract public Class getHeaderListClass();

  public List getFirstCorrelationList() throws DataAccessException {
    return daoCodeTable.getList (getHeaderListClass ( ), false);
  }

  public List getSecondCorrelationList(int value1) throws DataAccessException {
    return null;
  }

  public List getThirdCorrelationList(int value1, int value2) throws DataAccessException {
    return null;
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

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.HeaderField#getCorrelationValues()
   */
  public CorrelationValues getCorrelationValues() {
    return (new CorrelationValues ( )).change (1, getHeaderType ( ));
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.HeaderField#setCorrelationValues(librisuite.business.common.CorrelationValues)
   */
  public void setCorrelationValues(CorrelationValues v) {
    setHeaderType (v.getValue (1));
  }

}
