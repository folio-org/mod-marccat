/*
 * (c) LibriCore
 *
 * Created on Dec 5, 2005
 *
 * AuthoritySubjectHeadingTag.java
 */
package org.folio.cataloging.business.cataloguing.authority;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.descriptor.SkipInFiling;
import org.folio.cataloging.dao.persistence.CorrelationKey;
import org.folio.cataloging.dao.persistence.SBJCT_HDG;
import org.folio.cataloging.shared.CorrelationValues;

import java.util.List;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class AuthoritySubjectHeadingTag extends AuthorityHeadingTag implements SkipInFiling {
  /**
   * Class constructor
   *
   * @since 1.0
   */
  public AuthoritySubjectHeadingTag() {
    super(new SBJCT_HDG());
  }

  /* (non-Javadoc)
   * @see TagInterface#getCategory()
   */
  public int getCategory() {
    return 4;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#getFirstCorrelationList(java.util.Locale)
   */
  @Deprecated
  public List getFirstCorrelationList() throws DataAccessException {
    //return getDaoCodeTable().getList(T_AUT_SBJCT_HDG_TYP.class,false);
    return null;
  }

  /* (non-Javadoc)
   * @see SkipInFiling#getSkipInFiling()
   */
  public int getSkipInFiling() {
    return getDescriptor().getSkipInFiling();
  }

  /* (non-Javadoc)
   * @see SkipInFiling#setSkipInFiling(short)
   */
  public void setSkipInFiling(int i) {
    getDescriptor().setSkipInFiling(i);
  }

  /* (non-Javadoc)
   * @see TagInterface#getMarcEncoding()
   */
  public CorrelationKey getMarcEncoding()
    throws DataAccessException {
    return super.getMarcEncoding().changeSkipInFilingIndicator(
      getSkipInFiling());
  }

  /* (non-Javadoc)
   * @see TagInterface#getCorrelationValues()
   */
  public CorrelationValues getCorrelationValues() {
    return super.getCorrelationValues().change(3, CorrelationValues.UNDEFINED);
  }

  /* (non-Javadoc)
   * @see TagInterface#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
   */
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return v.isValueDefined(2);
  }

}
