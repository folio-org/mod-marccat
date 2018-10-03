/*
 * Created on 20-jul-2004
 *
 */
package org.folio.cataloging.business.cataloguing.bibliographic;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.cataloguing.common.Catalog;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.cataloguing.common.TagImpl;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.SubfieldCodeComparator;
import org.folio.cataloging.dao.BibliographicCorrelationDAO;
import org.folio.cataloging.dao.BibliographicValidationDAO;
import org.folio.cataloging.dao.persistence.Correlation;
import org.folio.cataloging.dao.persistence.CorrelationKey;
import org.folio.cataloging.shared.Validation;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * Class comment
 *
 * @author janick
 */
public class BibliographicTagImpl extends TagImpl {
	private static final Log logger = LogFactory.getLog(BibliographicTagImpl.class);

	private static final BibliographicValidationDAO daoValidation = new BibliographicValidationDAO();
	private static final BibliographicCorrelationDAO daoCorrelation = new BibliographicCorrelationDAO();

	/**
	 *
	 */
	public BibliographicTagImpl() {
		super();
	}

	/**
	 * @return the MARC tag and indicators for this tag
	 */
	public CorrelationKey getMarcEncoding(final Tag t, final Session session) throws DataAccessException {
		CorrelationKey key = null;
		try {
			key = daoCorrelation.getMarcEncoding(t.getCategory(), t.getCorrelation(1), t.getCorrelation(2), t.getCorrelation(3), session);
		} catch (HibernateException e) {
			throw new DataAccessException();
		}

		if (key == null) {
			logger.warn("MarcCorrelationException in getMarcEncoding for values: ");
			logger.warn("Corr_1: " + t.getCorrelation(1) + ", Corr_2: "
					+ t.getCorrelation(2) + ", Corr_3: " + t.getCorrelation(3)
					+ ", Category: " + t.getCategory());
			throw new MarcCorrelationException();
		}

		return key;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see TagImpl#getHeadingType(Tag)
	 */
	public String getHeadingType(Tag t) {
		return null;
	}

	public Validation getValidation(Tag t) throws
            DataAccessException {
		return daoValidation.load(t.getCategory(), t.getCorrelationValues());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see TagImpl#getCatalog()
	 */
	public Catalog getCatalog() {
		return new BibliographicCatalog();
	}

	@Deprecated
	public CorrelationKey getMarcEncoding(Tag t) throws DataAccessException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see TagImpl#getValidEditableSubfields(short)
	 */
	public Set getValidEditableSubfields(int category) {
		Set set = new TreeSet(new SubfieldCodeComparator());
		switch (category) {
		case 2:
			set.addAll(Arrays.asList("e", "i", "o", "u", "x",
                    "3", "4", "5"));
			break;
		case 3:
			set.addAll(Arrays.asList("c", "i", "v", "x", "3",
                    "5"));
			break;
		case 4:
			set.addAll(Arrays.asList("e", "u", "3", "4"));
			break;
		case 11:
			set.addAll(Arrays.asList("v", "3", "5"));
			break;
		case 12:
			set.addAll(Arrays.asList("v", "3", "5"));
			break;
		}
		return set;
	}

	public Correlation getCorrelation(String tagNumber, char indicator1, char indicator2, final int category, final Session session) throws DataAccessException {
    try {
      return daoCorrelation.getBibliographicCorrelation(session, tagNumber, indicator1, indicator2, category);
    } catch (HibernateException e) {
      throw new DataAccessException(e);
    }
  }

}
