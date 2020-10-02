package org.folio.marccat.dao.persistence;

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

	@Override
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		return v.isValueDefined(3);
	}

}
