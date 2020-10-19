package org.folio.marccat.dao.persistence;

import org.folio.marccat.shared.CorrelationValues;

/**
 * @author elena
 *
 */
public class AuthorityNameHeadingTag extends AuthorityHeadingTag {

  /**
   * 
   */
  private static final long serialVersionUID = -515607833095661985L;

  public AuthorityNameHeadingTag() {
    super(new NME_HDG());
  }

  @Override
  public int getCategory() {
    return 2;
  }

  @Override
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return v.isValueDefined(3);
  }

}
