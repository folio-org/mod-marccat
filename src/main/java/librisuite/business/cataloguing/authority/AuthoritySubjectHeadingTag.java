/*
 * (c) LibriCore
 * 
 * Created on Dec 5, 2005
 * 
 * AuthoritySubjectHeadingTag.java
 */
package librisuite.business.cataloguing.authority;

import java.util.List;

import librisuite.business.cataloguing.bibliographic.MarcCorrelationException;
import librisuite.business.common.CorrelationValues;
import librisuite.business.common.DataAccessException;
import librisuite.business.descriptor.SkipInFiling;
import librisuite.hibernate.CorrelationKey;
import librisuite.hibernate.SBJCT_HDG;
import librisuite.hibernate.T_AUT_SBJCT_HDG_TYP;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class AuthoritySubjectHeadingTag
	extends AuthorityHeadingTag
	implements SkipInFiling {
	/**
	 * Class constructor
	 *
	 * @param d
	 * @since 1.0
	 */
	public AuthoritySubjectHeadingTag() {
		super(new SBJCT_HDG());
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#getCategory()
	 */
	public short getCategory() {
		return 4;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getFirstCorrelationList(java.util.Locale)
	 */
	public List getFirstCorrelationList() throws DataAccessException {
		return getDaoCodeTable().getList(T_AUT_SBJCT_HDG_TYP.class,false);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.SkipInFiling#getSkipInFiling()
	 */
	public short getSkipInFiling() {
		return ((SBJCT_HDG) getDescriptor()).getSkipInFiling();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.SkipInFiling#setSkipInFiling(short)
	 */
	public void setSkipInFiling(short i) {
		((SBJCT_HDG) getDescriptor()).setSkipInFiling(i);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#getMarcEncoding()
	 */
	public CorrelationKey getMarcEncoding()
		throws DataAccessException, MarcCorrelationException {
		return super.getMarcEncoding().changeSkipInFilingIndicator(
			getSkipInFiling());
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return super.getCorrelationValues().change(3, CorrelationValues.UNDEFINED);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
	 */
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		return v.isValueDefined(2);
	}

}
