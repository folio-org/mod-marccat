/*
 * (c) LibriCore
 *
 * Created on Dec 5, 2005
 *
 * AuthorityTitleHeadingTag.java
 */
package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.business.descriptor.SkipInFiling;
import org.folio.marccat.dao.persistence.CorrelationKey;
import org.folio.marccat.dao.persistence.TTL_HDG;
import org.folio.marccat.shared.CorrelationValues;

import java.util.List;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class AuthorityTitleHeadingTag
  extends AuthorityHeadingTag
  implements SkipInFiling {

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public AuthorityTitleHeadingTag() {
    super(new TTL_HDG());
  }

  /* (non-Javadoc)
   * @see TagInterface#getCategory()
   */
  public int getCategory() {
    return 3;
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
   * @see TagInterface#getFirstCorrelationList()
   */
  public List getFirstCorrelationList() throws DataAccessException {
    // Authority title headings have no correlation lists
    return null;
  }

  /* (non-Javadoc)
   * @see TagInterface#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
   */
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return v.isValueDefined(1);
  }

}
