/*
 * (c) LibriCore
 * 
 * Created on Dec 20, 2005
 * 
 * AuthorityNameTitleHeadingTag.java
 */
package librisuite.business.cataloguing.authority;

import java.util.List;

import librisuite.business.common.CorrelationValues;
import librisuite.business.common.DAOAuthorityCorrelation;
import librisuite.business.common.DataAccessException;
import librisuite.hibernate.NME_TTL_HDG;
import librisuite.hibernate.NameSubType;
import librisuite.hibernate.NameType;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class AuthorityNameTitleHeadingTag extends AuthorityHeadingTag {
	/**
	 * Class constructor
	 *
	 * @param d
	 * @since 1.0
	 */
	public AuthorityNameTitleHeadingTag() {
		super(new NME_TTL_HDG());
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#getCategory()
	 */
	public short getCategory() {
		return 11;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getFirstCorrelationList(java.util.Locale)
	 */
	public List getFirstCorrelationList() throws DataAccessException {
		return getDaoCodeTable().getList(NameType.class,false);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getSecondCorrelationList(short, java.util.Locale)
	 */
	public List getSecondCorrelationList(short value1)
		throws DataAccessException {
		DAOAuthorityCorrelation dao = new DAOAuthorityCorrelation();
		return dao.getSecondCorrelationList(
			getCategory(),
			getHeadingType(),
			value1,
			NameSubType.class);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
	 */
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		return v.isValueDefined(3);
	}

}
