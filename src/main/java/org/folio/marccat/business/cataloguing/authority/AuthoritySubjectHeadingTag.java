package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.business.descriptor.SkipInFiling;
import org.folio.marccat.dao.persistence.CorrelationKey;
import org.folio.marccat.dao.persistence.SBJCT_HDG;
import org.folio.marccat.shared.CorrelationValues;


public class AuthoritySubjectHeadingTag extends AuthorityHeadingTag implements SkipInFiling {

  public AuthoritySubjectHeadingTag() {
    super(new SBJCT_HDG());
  }

  @Override
  public int getCategory() {
    return 4;
  }

  public int getSkipInFiling() {
    return getDescriptor().getSkipInFiling();
  }

  public void setSkipInFiling(int i) {
    getDescriptor().setSkipInFiling(i);
  }

  @Override
  public CorrelationKey getMarcEncoding() {
    return super.getMarcEncoding().changeSkipInFilingIndicator(
      getSkipInFiling());
  }

  @Override
  public CorrelationValues getCorrelationValues() {
    return super.getCorrelationValues().change(3, CorrelationValues.UNDEFINED);
  }

  @Override
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return v.isValueDefined(2);
  }

}
