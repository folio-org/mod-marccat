package org.folio.marccat.business.cataloguing.authority;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.marccat.business.cataloguing.common.Catalog;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.cataloguing.common.TagImpl;
import org.folio.marccat.business.common.SubfieldCodeComparator;
import org.folio.marccat.dao.AuthorityCorrelationDAO;
import org.folio.marccat.dao.AuthorityValidationDAO;
import org.folio.marccat.dao.persistence.AUT;
import org.folio.marccat.dao.persistence.Correlation;
import org.folio.marccat.dao.persistence.CorrelationKey;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.shared.Validation;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

/**
 * @author elena
 *
 */
public class AuthorityTagImpl extends TagImpl {
	private static final Log logger = LogFactory.getLog(AuthorityTagImpl.class);

	private static final AuthorityValidationDAO daoValidation = new AuthorityValidationDAO();
	private static final AuthorityCorrelationDAO daoCorrelation = new AuthorityCorrelationDAO();

	/**
	 *
	 */
	public AuthorityTagImpl() {
		super();
	}

	/**
	 * @return the MARC tag and indicators for this tag
	 */
	public CorrelationKey getMarcEncoding(final Tag t, final Session session) {
		CorrelationKey key = null;
		try {
			key = daoCorrelation.getMarcEncoding(t.getCategory(), t.getCorrelation(1),
					t.getCorrelation(2), t.getCorrelation(3), session);
		} catch (HibernateException e) {
			throw new DataAccessException();
		}

		if (key == null) {
			logger.warn("MarcCorrelationException in getMarcEncoding for values: ");
			logger.warn("Corr_1: " + t.getCorrelation(1) + ", Corr_2: " + t.getCorrelation(2) + ", Corr_3: "
					+ t.getCorrelation(3) + ", Category: " + t.getCategory());
		}
		return key;
	}

	public String getHeadingType(Tag t) {
		return ((AUT) ((PersistsViaItem) t).getItemEntity()).getHeadingType();
	}

	public Validation getValidation(Tag t, final Session session) {
		try {
			return daoValidation.load(session, t.getCategory(), t.getHeadingType(), t.getCorrelationValues());
		} catch (HibernateException e) {
			throw new DataAccessException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see TagImpl#getCatalog()
	 */
	public Catalog getCatalog() {
		return new AuthorityCatalog();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see TagImpl#getValidEditableSubfields(short)
	 */
	public Set<String> getValidEditableSubfields(int category) {
		Set<String> set = new TreeSet<>(new SubfieldCodeComparator());
		switch (category) {
		case 6:
			set.addAll(Arrays.asList("d"));
			break;
		case 15:
			set.addAll(Arrays.asList("d", "5"));
			break;
		default:
			return set;
		}
		return set;
	}

	public Correlation getCorrelation(String tagNumber, char indicator1, char indicator2, final int category,
			final Session session) {
		try {
			return daoCorrelation.getAuthorityCorrelation(session, tagNumber, indicator1, indicator2, category);
		} catch (HibernateException e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * @deprecated the correlation key is not used
	 */

	@Deprecated
	public CorrelationKey getMarcEncoding(Tag t) {
		return null;
	}

	/**
	 * @deprecated the tag validation is not used
	 */

	@Deprecated
	public Validation getValidation(Tag t) {
		return null;
	}

}
