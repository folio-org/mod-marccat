/*
 * (c) LibriCore
 *
 * Created on Nov 18, 2005
 *
 * AuthorityNameHeadingTag.java
 */
package org.folio.cataloging.business.cataloguing.authority;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.DAOAuthorityCorrelation;
import org.folio.cataloging.dao.persistence.NME_HDG;
import org.folio.cataloging.dao.persistence.NameSubType;
import org.folio.cataloging.shared.CorrelationValues;

import java.util.List;

/**
 * @author paulm
 * @version $Revision: 1.4 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class AuthorityNameHeadingTag extends AuthorityHeadingTag {

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public AuthorityNameHeadingTag() {
    super (new NME_HDG ( ));
  }

  /* (non-Javadoc)
   * @see TagInterface#getCategory()
   */
  public int getCategory() {
    return 2;
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
  public List getSecondCorrelationList(short value1)
    throws DataAccessException {
    DAOAuthorityCorrelation dao = new DAOAuthorityCorrelation ( );
    return dao.getSecondCorrelationList (
      getCategory ( ),
      getHeadingType ( ),
      value1,
      NameSubType.class);
  }

  /* (non-Javadoc)
   * @see TagInterface#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
   */
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    /*
     * if v3 is defined then we are a Reference tag and not a heading
     */
    return v.isValueDefined (3);
  }

}
