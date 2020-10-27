package org.folio.marccat.dao.persistence;

import org.folio.marccat.config.constants.Global;
import org.folio.marccat.shared.CorrelationValues;

/**
 * @author elena
 *
 */
@SuppressWarnings("serial")
public class AuthoritySubjectHeadingTag extends AuthorityHeadingTag {
  public AuthoritySubjectHeadingTag() {
    super(new SBJCT_HDG());
    setDefault();
  }

  @Override
  public int getCategory() {
    return Global.SUBJECT_CATEGORY;
  }

  @Override
  public CorrelationValues getCorrelationValues() {
    return super.getCorrelationValues().change(3, CorrelationValues.UNDEFINED);
  }

  @Override
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return v.isValueDefined(2);
  }

  public void setDefault() {
    ((SBJCT_HDG) super.getDescriptor()).setSourceCode(Global.SUBJECT_SOURCE_CODE_OTHERS);
  }

  @Override
  public void setCorrelationValues(CorrelationValues v) {
    ((SBJCT_HDG) super.getDescriptor()).setTypeCode(v.getValue(1));
    ((SBJCT_HDG) super.getDescriptor()).setSourceCode(Global.SUBJECT_SOURCE_CODE_OTHERS);
  }

}
