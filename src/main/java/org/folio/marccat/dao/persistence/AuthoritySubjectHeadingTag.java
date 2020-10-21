package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.descriptor.SkipInFiling;
import org.folio.marccat.shared.CorrelationValues;

/**
 * @author elena
 *
 */
@SuppressWarnings("serial")
public class AuthoritySubjectHeadingTag extends AuthorityHeadingTag implements SkipInFiling {
	public AuthoritySubjectHeadingTag() {
		super(new SBJCT_HDG());
		setDefault();
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
		return super.getMarcEncoding().changeSkipInFilingIndicator(getSkipInFiling());
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
		((SBJCT_HDG)super.getDescriptor()).setSourceCode(6);
	}
	
	@Override 
	public void setCorrelationValues(CorrelationValues v) {
		 ((SBJCT_HDG)super.getDescriptor()).setTypeCode(v.getValue(1));
		 ((SBJCT_HDG)super.getDescriptor()).setSourceCode(6);
  }

}
