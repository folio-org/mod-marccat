package org.folio.marccat.dao.persistence;

import java.util.Collections;
import java.util.List;

import org.folio.marccat.business.descriptor.SkipInFiling;
import org.folio.marccat.shared.CorrelationValues;

/**
 * @author elena
 *
 */
@SuppressWarnings("serial")
public class AuthorityTitleHeadingTag extends AuthorityHeadingTag implements SkipInFiling {

	/**
	 * Class constructor
	 *
	 * @param d
	 * @since 1.0
	 */
	public AuthorityTitleHeadingTag() {
		super(new TTL_HDG());
	}

	@Override
	public int getCategory() {
		return 3;
	}

	public int getSkipInFiling() {
		return ((TTL_HDG) getDescriptor()).getSkipInFiling();
	}

	public void setSkipInFiling(short i) {
		((TTL_HDG) getDescriptor()).setSkipInFiling(i);
	}
	@Override
	public CorrelationKey getMarcEncoding() {
		return super.getMarcEncoding().changeSkipInFilingIndicator(getSkipInFiling());
	}

	public List<Correlation> getFirstCorrelationList() {
		// Authority title headings have no correlation lists
		return Collections.emptyList();
	}

	@Override
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		return v.isValueDefined(1);
	}

	@Override
	public String getRequiredEditPermission() {
		return "editAuthorityTitle";
	}

	public boolean isAuthorityHeadingTag() {
		return true;
	}

	public boolean isSkipInFiling() {
		return true;
	}

	@Override
	public void setSkipInFiling(int i) {
		((TTL_HDG) getDescriptor()).setSkipInFiling(i);
	}

}
