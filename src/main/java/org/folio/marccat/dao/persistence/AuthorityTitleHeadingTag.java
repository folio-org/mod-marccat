package org.folio.marccat.dao.persistence;

import java.util.ArrayList;
import java.util.List;

import org.folio.marccat.business.descriptor.SkipInFiling;
import org.folio.marccat.shared.CorrelationValues;

/**
 * @author elena
 *
 */
public class AuthorityTitleHeadingTag extends AuthorityHeadingTag implements SkipInFiling {

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
		return 3;
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

	public List<?> getFirstCorrelationList() {
		return new ArrayList();
	}

	@Override
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		return v.isValueDefined(1);
	}

}
