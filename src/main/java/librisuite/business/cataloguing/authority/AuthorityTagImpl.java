/*
 * (c) LibriCore
 * 
 * Created on Oct 31, 2005
 * 
 * AuthorityTag.java
 */
package librisuite.business.cataloguing.authority;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import librisuite.business.cataloguing.bibliographic.MarcCorrelationException;
import librisuite.business.cataloguing.bibliographic.PersistsViaItem;
import librisuite.business.cataloguing.common.Catalog;
import librisuite.business.cataloguing.common.Tag;
import librisuite.business.cataloguing.common.TagImpl;
import librisuite.business.cataloguing.common.Validation;
import librisuite.business.common.DAOAuthorityCorrelation;
import librisuite.business.common.DAOAuthorityValidation;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.SubfieldCodeComparator;
import librisuite.hibernate.AUT;
import librisuite.hibernate.Correlation;
import librisuite.hibernate.CorrelationKey;

/**
 * @author paulm
 * @version $Revision: 1.4 $, $Date: 2005/12/21 08:30:32 $
 * @since 1.0
 */
public class AuthorityTagImpl extends TagImpl {

	private int authorityNumber;

	private static final DAOAuthorityCorrelation daoCorrelation = new DAOAuthorityCorrelation();

	private static final DAOAuthorityValidation daoValidation = new DAOAuthorityValidation();

	public int getItemNumber() {
		return getAuthorityNumber();
	}

	public void setItemNumber(int itemNumber) {
		setAuthorityNumber(itemNumber);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getAuthorityNumber() {
		return authorityNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setAuthorityNumber(int i) {
		authorityNumber = i;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getAuthorityNumber();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.common.TagImpl#getMarcEncoding(librisuite.business.cataloguing.common.Tag)
	 */
	public CorrelationKey getMarcEncoding(Tag t) throws DataAccessException,
			MarcCorrelationException {

		CorrelationKey key = daoCorrelation.getMarcEncoding(t.getCategory(),
				getHeadingType(t), t.getCorrelation(1), t.getCorrelation(2), t
						.getCorrelation(3));

		if (key == null) {
			throw new MarcCorrelationException();
		}

		return key;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.common.TagImpl#getHeadingType(librisuite.business.cataloguing.common.Tag)
	 */
	public String getHeadingType(Tag t) {
		return ((AUT) ((PersistsViaItem) t).getItemEntity()).getHeadingType();
	}

	public Validation getValidation(Tag t) throws MarcCorrelationException,
			DataAccessException {
		CorrelationKey key = getMarcEncoding(t);
		return daoValidation.load(key.getMarcTag(), t.getHeadingType(), t
				.getCategory());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.common.TagImpl#getCatalog()
	 */
	public Catalog getCatalog() {
		return new AuthorityCatalog();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.common.TagImpl#getValidEditableSubfields(short)
	 */
	public Set getValidEditableSubfields(short category) {
		Set set = new TreeSet(new SubfieldCodeComparator());
		switch (category) {
		case 6:
			set.add("d");
			break;
		case 15:
			set.addAll(Arrays.asList(new String[] { "d", "5" }));
			break;
		default:
			set.addAll(Arrays.asList(new String[] { "i", "e", "j", "4"}));
			break;
		}
		return set;
	}

	@Override
	public Correlation getCorrelation(String tagNumber, char indicator1,
			char indicator2, short category) throws DataAccessException {
		return daoCorrelation.getFirstAuthorityCorrelation(tagNumber, indicator1,
				indicator2, category);
	}

}
