/*
 * Created on 20-jul-2004
 *
 */
package org.folio.cataloging.business.cataloguing.bibliographic;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.folio.cataloging.business.cataloguing.common.Catalog;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.cataloguing.common.TagImpl;
import org.folio.cataloging.business.cataloguing.common.Validation;
import org.folio.cataloging.dao.DAOBibliographicCorrelation;
import org.folio.cataloging.dao.DAOBibliographicValidation;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.SubfieldCodeComparator;
import org.folio.cataloging.dao.persistence.Correlation;
import org.folio.cataloging.dao.persistence.CorrelationKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class comment
 * 
 * @author janick
 */
public class BibliographicTagImpl extends TagImpl {
	private static final Log logger = LogFactory
			.getLog(BibliographicTagImpl.class);
	private static final DAOBibliographicValidation daoValidation = new DAOBibliographicValidation();
	private static final DAOBibliographicCorrelation daoCorrelation = new DAOBibliographicCorrelation();

	/**
	 * 
	 */
	public BibliographicTagImpl() {
		super();
	}

	/**
	 * @return the MARC tag and indicators for this tag
	 */
	public CorrelationKey getMarcEncoding(Tag t) throws DataAccessException,
			MarcCorrelationException {
		CorrelationKey key = daoCorrelation.getMarcEncoding(t.getCategory(), t
				.getCorrelation(1), t.getCorrelation(2), t.getCorrelation(3));

		if (key == null) {
			logger
					.warn("MarcCorrelationException in getMarcEncoding for values: ");
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
		// Bibliographic Tags do not have a heading type
		return null;
	}

	public Validation getValidation(Tag t) throws MarcCorrelationException,
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see TagImpl#getValidEditableSubfields(short)
	 */
	public Set getValidEditableSubfields(short category) {
		Set set = new TreeSet(new SubfieldCodeComparator());
		switch (category) {
		case 2:
			set.addAll(Arrays.asList(new String[] { "e", "i", "o", "u", "x",
					"3", "4", "5" }));
			break;
		case 3:
			set.addAll(Arrays.asList(new String[] { "c", "i", "v", "x", "3",
					"5" }));
			break;
		case 4:
			set.addAll(Arrays.asList(new String[] { "e", "u", "3", "4" }));
			break;
		case 11:
			set.addAll(Arrays.asList(new String[] { "v", "3", "5" }));
			break;
		case 12:
			set.addAll(Arrays.asList(new String[] { "v", "3", "5" }));
			break;
		}
		return set;
	}

	@Override
	public Correlation getCorrelation(String tagNumber, char indicator1,
			char indicator2, short category) throws DataAccessException {
		return daoCorrelation.getBibliographicCorrelation(tagNumber,
				indicator1, indicator2, category);
	}

}
