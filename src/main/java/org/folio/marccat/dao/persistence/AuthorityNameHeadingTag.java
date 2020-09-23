package org.folio.marccat.dao.persistence;

import java.util.Collections;
import java.util.List;

import org.folio.marccat.shared.CorrelationValues;

/**
 * @author elena
 *
 */
@SuppressWarnings("serial")
public class AuthorityNameHeadingTag extends AuthorityHeadingTag {

	public AuthorityNameHeadingTag() {
		super(new NME_HDG());
	}

	@Override
	public int getCategory() {
		return 2;
	}

	public List<Object> getFirstCorrelationList() {
		return Collections.emptyList();
	}

	public List<Object> getSecondCorrelationList() {
		return Collections.emptyList();
	}
	@Override
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		return v.isValueDefined(3);
	}
	@Override
	public String getRequiredEditPermission() {
		return "editAuthorityName";
	}

	public boolean isAuthorityHeadingTag() {
		return true;
	}

}
