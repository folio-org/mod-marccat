/*
 * (c) LibriCore
 * 
 * Created on Dec 5, 2005
 * 
 * AuthorityTitleHeadingTag.java
 */
package org.folio.cataloging.business.cataloguing.authority;

import java.util.List;

import org.folio.cataloging.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.cataloging.business.common.CorrelationValues;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.descriptor.SkipInFiling;
import org.folio.cataloging.dao.persistence.CorrelationKey;
import org.folio.cataloging.dao.persistence.TTL_HDG;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class AuthorityTitleHeadingTag
	extends AuthorityHeadingTag
	implements SkipInFiling {

	/**
	 * Class constructor
	 *
	 * @param d
	 * @since 1.0
	 */
	public AuthorityTitleHeadingTag() {
		super(new TTL_HDG());
	}

	/* (non-Javadoc)
	 * @see TagInterface#getCategory()
	 */
	public short getCategory() {
		return 3;
	}

	/* (non-Javadoc)
	 * @see SkipInFiling#getSkipInFiling()
	 */
	public short getSkipInFiling() {
		return ((TTL_HDG) getDescriptor()).getSkipInFiling();
	}

	/* (non-Javadoc)
	 * @see SkipInFiling#setSkipInFiling(short)
	 */
	public void setSkipInFiling(short i) {
		((TTL_HDG) getDescriptor()).setSkipInFiling(i);
	}
	/* (non-Javadoc)
	 * @see TagInterface#getMarcEncoding()
	 */
	public CorrelationKey getMarcEncoding()
		throws DataAccessException, MarcCorrelationException {
		return super.getMarcEncoding().changeSkipInFilingIndicator(
			getSkipInFiling());
	}

	/* (non-Javadoc)
	 * @see TagInterface#getFirstCorrelationList()
	 */
	public List getFirstCorrelationList() throws DataAccessException {
		// Authority title headings have no correlation lists
		return null;
	}

	/* (non-Javadoc)
	 * @see TagInterface#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
	 */
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		return v.isValueDefined(1);
	}

}
