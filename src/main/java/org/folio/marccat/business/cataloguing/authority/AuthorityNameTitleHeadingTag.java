/*
 * (c) LibriCore
 *
 * Created on Dec 20, 2005
 *
 * AuthorityNameTitleHeadingTag.java
 */
package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.dao.DAOAuthorityCorrelation;
import org.folio.marccat.dao.persistence.NME_TTL_HDG;
import org.folio.marccat.dao.persistence.NameSubType;
import org.folio.marccat.shared.CorrelationValues;

import java.util.List;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class AuthorityNameTitleHeadingTag extends AuthorityHeadingTag {
  /**
   * Class constructor
   *
   * @since 1.0
   */
  public AuthorityNameTitleHeadingTag() {
    super(new NME_TTL_HDG());
  }

  /* (non-Javadoc)
   * @see TagInterface#getCategory()
   */
  public int getCategory() {
    return 11;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#getFirstCorrelationList(java.util.Locale)
   */
  @Deprecated
  public List getFirstCorrelationList() throws DataAccessException {
    //return getDaoCodeTable().getList(NameType.class,false);
    return null;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#getSecondCorrelationList(short, java.util.Locale)
   */
  @Deprecated
  public List getSecondCorrelationList(short value1)
    throws DataAccessException {
    DAOAuthorityCorrelation dao = new DAOAuthorityCorrelation();
    return dao.getSecondCorrelationList(
      getCategory(),
      getHeadingType(),
      value1,
      NameSubType.class);
  }

  /* (non-Javadoc)
   * @see TagInterface#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
   */
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return v.isValueDefined(3);
  }

}
