package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.descriptor.SkipInFiling;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.shared.CorrelationValues;

/**
 * @author elena
 *
 */
public class AuthorityTitleHeadingTag extends AuthorityHeadingTag implements SkipInFiling {

  /**
  * 
  */
  private static final long serialVersionUID = 7303329914542120254L;

  public AuthorityTitleHeadingTag() {
    super(new TTL_HDG());
  }

  /*
   * (non-Javadoc)
   * 
   * @see TagInterface#getCategory()
   */
  @Override
  public int getCategory() {
    return Global.TITLE_CATEGORY;
  }

  public int getSkipInFiling() {
    return getDescriptor().getSkipInFiling();
  }

  public void setSkipInFiling(int i) {
    getDescriptor().setSkipInFiling(i);
  }

  @Override
  public CorrelationKey getMarcEncoding() {
    return super.getMarcEncoding().changeSkipInFilingIndicator(getSkipInFiling());
  }

  @Override
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return v.isValueDefined(1);
  }

}
