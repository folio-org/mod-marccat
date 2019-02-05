package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.dao.persistence.NME_TTL_HDG;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.shared.CorrelationValues;

import java.util.List;

public class AuthorityNameTitleHeadingTag extends AuthorityHeadingTag {

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
//    DAOAuthorityCorrelation dao = new DAOAuthorityCorrelation();
//    return dao.getSecondCorrelationList(
//      getCategory(),
//      getHeadingType(),
//      value1,
//      NameSubType.class);
    return null;
  }

  /* (non-Javadoc)
   * @see TagInterface#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
   */
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return v.isValueDefined(3);
  }

}
