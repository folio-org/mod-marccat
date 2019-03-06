package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.business.descriptor.SkipInFiling;
import org.folio.marccat.dao.persistence.CorrelationKey;
import org.folio.marccat.dao.persistence.TTL_HDG;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.shared.CorrelationValues;

import java.util.List;


public class AuthorityTitleHeadingTag
  extends AuthorityHeadingTag
  implements SkipInFiling {


  public AuthorityTitleHeadingTag() {
    super(new TTL_HDG());
  }

  /* (non-Javadoc)
   * @see TagInterface#getCategory()
   */
  public int getCategory() {
    return 3;
  }


  public int getSkipInFiling() {
    return getDescriptor().getSkipInFiling();
  }


  public void setSkipInFiling(int i) {
    getDescriptor().setSkipInFiling(i);
  }


  public CorrelationKey getMarcEncoding()
    throws DataAccessException {
    return super.getMarcEncoding().changeSkipInFilingIndicator(
      getSkipInFiling());
  }

  public List getFirstCorrelationList() throws DataAccessException {
    return null;
  }

  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return v.isValueDefined(1);
  }

}
