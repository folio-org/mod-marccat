package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.business.descriptor.SkipInFiling;
import org.folio.marccat.dao.persistence.CorrelationKey;
import org.folio.marccat.dao.persistence.SBJCT_HDG;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.shared.CorrelationValues;

import java.util.List;


public class AuthoritySubjectHeadingTag extends AuthorityHeadingTag implements SkipInFiling {

  public AuthoritySubjectHeadingTag() {
    super(new SBJCT_HDG());
  }

  public int getCategory() {
    return 4;
  }

  @Deprecated
  public List getFirstCorrelationList() throws DataAccessException {
    return null;
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

  public CorrelationValues getCorrelationValues() {
    return super.getCorrelationValues().change(3, CorrelationValues.UNDEFINED);
  }

  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return v.isValueDefined(2);
  }

}
