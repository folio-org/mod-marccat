package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.dao.persistence.NME_TTL_HDG;
import org.folio.marccat.shared.CorrelationValues;

public class AuthorityNameTitleHeadingTag extends AuthorityHeadingTag {

  public AuthorityNameTitleHeadingTag() {
    super(new NME_TTL_HDG());
  }

  /* (non-Javadoc)
   * @see TagInterface#getCategory()
   */
  @Override
  public int getCategory() {
    return 11;
  }

  /* (non-Javadoc)
   * @see TagInterface#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
   */
  @Override
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return v.isValueDefined(3);
  }

}
