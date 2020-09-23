package org.folio.marccat.dao.persistence;

import java.util.Collections;
import java.util.List;

import org.folio.marccat.business.descriptor.SkipInFiling;
import org.folio.marccat.shared.CorrelationValues;

/**
 * @author Elena
 *
 */
public class AuthoritySubjectHeadingTag extends AuthorityHeadingTag implements SkipInFiling {
	public AuthoritySubjectHeadingTag() {
		super(new SBJCT_HDG());
	}

	@Override
	public int getCategory() {
		return 4;
	}

	public List<Object> getFirstCorrelationList() {
		return Collections.emptyList();
				/**new CodeTableDAO().getList(T_AUT_SBJCT_HDG_TYP.class);*/
	}

	public int getSkipInFiling() {
		return ((SBJCT_HDG) getDescriptor()).getSkipInFiling();
	}

	public void setSkipInFiling(short i) {
		((SBJCT_HDG) getDescriptor()).setSkipInFiling(i);
	}

	@Override
	public CorrelationKey getMarcEncoding()  {
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

	@Override
	public String getRequiredEditPermission() {
		return "editAuthoritySubject";
	}

	public boolean isAuthorityHeadingTag() {
		return true;
	}

	public boolean isSkipInFiling() {
		return (getCorrelation(1) == 17);
	}

	@Override
	public void setSkipInFiling(int i) {
		((SBJCT_HDG) getDescriptor()).setSkipInFiling(i);
	}

}
