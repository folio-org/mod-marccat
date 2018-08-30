package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.persistence.BibliographicValidation;
import org.folio.cataloging.dao.persistence.BibliographicValidationKey;
import org.folio.cataloging.integration.log.MessageCatalogStorage;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.shared.Validation;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author elena
 * @author natasciab
 * @author agazzarini
 * @since 1.0
 */
public class BibliographicValidationDAO extends DAOValidation {
	
	private static final Log logger = LogFactory.getLog(BibliographicValidationDAO.class);

	public BibliographicValidation load(final Session session, final String marcNumber, final int category) throws HibernateException {
		 return (BibliographicValidation) session.load(BibliographicValidation.class, new BibliographicValidationKey(marcNumber, category));
	}

	/**
	 *
	 * @param session the session of hibernate
	 * @param category the marc category used as filter criterion
	 * @param values the correlation values used as filter criterion
	 * @return a BibliographicValidation object containing
	 *  the MARC subfield list or null when not found
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public BibliographicValidation load(final Session session, final int category, final CorrelationValues values) throws HibernateException {

		List<BibliographicValidation> bibliographicValidations  = session.find("select distinct v from BibliographicValidation as v, " +
						"BibliographicCorrelation as c" +
						" where c.key.marcTagCategoryCode = ?" +
						" and (c.databaseFirstValue = ? or c.databaseFirstValue = -1 or -1 = ?)" +
						" and (c.databaseSecondValue = ? or c.databaseSecondValue = -1 or -1 = ?)" +
						" and (c.databaseThirdValue = ? or c.databaseThirdValue = -1 or -1 = ?)" +
						" and c.key.marcTag = v.key.marcTag" +
						" and c.key.marcTagCategoryCode =  v.key.marcTagCategoryCode",
				new Object[] {
						category,
						values.getValue(1), values.getValue(1),
						values.getValue(2), values.getValue(2),
						values.getValue(3), values.getValue(3)},
				new Type[] {
						Hibernate.INTEGER,
						Hibernate.INTEGER, Hibernate.INTEGER,
						Hibernate.INTEGER, Hibernate.INTEGER,
						Hibernate.INTEGER, Hibernate.INTEGER}
		);

		Optional<BibliographicValidation> firstElement = bibliographicValidations.stream().filter(Objects::nonNull).findFirst();
		if (firstElement.isPresent()) {
			return firstElement.get();
		}

		bibliographicValidations = session.find("select distinct v from BibliographicValidation as v, " +
													"BibliographicCorrelation as c" +
													" where c.key.marcTagCategoryCode = ?"+
													" and c.key.marcTag = v.key.marcTag" +
													" and c.key.marcTagCategoryCode =  v.key.marcTagCategoryCode",
									new Object[] { category},
									new Type[] { Hibernate.INTEGER});

		firstElement = bibliographicValidations.stream().filter(Objects::nonNull).findFirst();
		if (firstElement.isPresent()) {
			return firstElement.get();
		} else {
			logger.error(String.format(MessageCatalogStorage._00014_NO_VALIDATION_FOUND, category, values.toString()));
			return null;
		}
	}

	@Deprecated
	public BibliographicValidation load(final String marcNumber, final int marcCategory) throws DataAccessException {
		return (BibliographicValidation) load(BibliographicValidation.class, new BibliographicValidationKey(marcNumber, marcCategory));
	}

	@Deprecated
	public Validation load(final int s, final CorrelationValues values) throws DataAccessException {
		List<BibliographicValidation> validations = find("select distinct v from BibliographicValidation as v, " +
						"BibliographicCorrelation as c" +
						" where c.key.marcTagCategoryCode = ?" +
						" and (c.databaseFirstValue = ? or c.databaseFirstValue = -1 or -1 = ?)" +
						" and (c.databaseSecondValue = ? or c.databaseSecondValue = -1 or -1 = ?)" +
						" and (c.databaseThirdValue = ? or c.databaseThirdValue = -1 or -1 = ?)" +
						" and c.key.marcTag = v.key.marcTag" +
						" and c.key.marcTagCategoryCode =  v.key.marcTagCategoryCode",
				new Object[] {
						s,
						values.getValue(1), values.getValue(1),
						values.getValue(2), values.getValue(2),
						values.getValue(3), values.getValue(3)},
				new Type[] {
						Hibernate.INTEGER,
						Hibernate.INTEGER, Hibernate.INTEGER,
						Hibernate.INTEGER, Hibernate.INTEGER,
						Hibernate.INTEGER, Hibernate.INTEGER}
		);

		if (validations.size() > 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("BibliographicValidation(s) found:");
				for (int i = 0; i < validations.size(); i++) {
					logger.debug(validations.get(i));
				}
			}
			return validations.get(0);
		} else {
			validations = find("select distinct v from BibliographicValidation as v, " +
							"BibliographicCorrelation as c" +
							" where c.key.marcTagCategoryCode = ?"+
							" and c.key.marcTag = v.key.marcTag" +
							" and c.key.marcTagCategoryCode =  v.key.marcTagCategoryCode",
					new Object[] { s},
					new Type[] { Hibernate.INTEGER}
			);

			if (validations.size() > 0) {
				if (logger.isDebugEnabled()) {
					logger.debug("BibliographicValidation(s) found:");
					for (int i = 0; i < validations.size(); i++) {
						logger.debug(validations.get(i));
					}
				}
				return validations.get(0);
			} else {
				logger.warn("No validation found for category " + s + " and values " + values.toString());
				throw new MarcCorrelationException("no Validation found");
			}
		}
	}
}
