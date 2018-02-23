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
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.shared.Validation;

import java.util.List;
import java.util.Optional;

/**
 * @author elena
 * @author natasciab
 * @since 1.0
 */
public class DAOBibliographicValidation extends DAOValidation {
	
	private static final Log logger = LogFactory.getLog(DAOBibliographicValidation.class);

	public BibliographicValidation load(final Session session, final String marcNumber, final short category) throws DataAccessException {

		try {

			 return (BibliographicValidation) session.load(BibliographicValidation.class, new BibliographicValidationKey(marcNumber, category));
		} catch (final HibernateException exception) {
			logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
			return null;
		}
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
	public BibliographicValidation load(final Session session, final short category, final CorrelationValues values) throws DataAccessException {

		List<BibliographicValidation> bibliographicValidations = null;
		try {
			bibliographicValidations = session.find("select distinct v from BibliographicValidation as v, " +
                            "BibliographicCorrelation as c" +
                            " where c.key.marcTagCategoryCode = ?" +
                            " and (c.databaseFirstValue = ? or c.databaseFirstValue = -1 or -1 = ?)" +
                            " and (c.databaseSecondValue = ? or c.databaseSecondValue = -1 or -1 = ?)" +
                            " and (c.databaseThirdValue = ? or c.databaseThirdValue = -1 or -1 = ?)" +
                            " and c.key.marcTag = v.key.marcTag" +
                            " and c.key.marcTagCategoryCode =  v.key.marcTagCategoryCode",
                    new Object[] { new Short(category),
                            new Short(values.getValue(1)), new Short(values.getValue(1)),
                            new Short(values.getValue(2)), new Short(values.getValue(2)),
                            new Short(values.getValue(3)), new Short(values.getValue(3))},
                    new Type[] { Hibernate.SHORT,
                            Hibernate.SHORT, Hibernate.SHORT,
                            Hibernate.SHORT, Hibernate.SHORT,
                            Hibernate.SHORT, Hibernate.SHORT}
            );

			Optional<BibliographicValidation> firstElement = bibliographicValidations.stream().findFirst();
			if (firstElement.isPresent()) {
				return firstElement.get();
			}

			bibliographicValidations = session.find("select distinct v from BibliographicValidation as v, " +
														"BibliographicCorrelation as c" +
														" where c.key.marcTagCategoryCode = ?"+
														" and c.key.marcTag = v.key.marcTag" +
														" and c.key.marcTagCategoryCode =  v.key.marcTagCategoryCode",
										new Object[] { new Short(category)},
										new Type[] { Hibernate.SHORT});

			firstElement = bibliographicValidations.stream().findFirst();
			if (firstElement.isPresent()) {
				return firstElement.get();
			} else {
				logger.error(String.format(MessageCatalog._00014_NO_VALIDATION_FOUND, category, values.toString()));
				return null;
			}

		} catch (final HibernateException exception) {
			logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
			return null;
		}


	}

	@Deprecated
	public BibliographicValidation load(String marcNumber, short marcCategory)
			throws DataAccessException {
		return (BibliographicValidation) load(
				BibliographicValidation.class,
				new BibliographicValidationKey(marcNumber, marcCategory));
	}

	@Deprecated
	public Validation load(short s, CorrelationValues values) throws DataAccessException {
		List l = find("select distinct v from BibliographicValidation as v, " +
						"BibliographicCorrelation as c" +
						" where c.key.marcTagCategoryCode = ?" +
						" and (c.databaseFirstValue = ? or c.databaseFirstValue = -1 or -1 = ?)" +
						" and (c.databaseSecondValue = ? or c.databaseSecondValue = -1 or -1 = ?)" +
						" and (c.databaseThirdValue = ? or c.databaseThirdValue = -1 or -1 = ?)" +
						" and c.key.marcTag = v.key.marcTag" +
						" and c.key.marcTagCategoryCode =  v.key.marcTagCategoryCode",
				new Object[] { new Short(s),
						new Short(values.getValue(1)), new Short(values.getValue(1)),
						new Short(values.getValue(2)), new Short(values.getValue(2)),
						new Short(values.getValue(3)), new Short(values.getValue(3))},
				new Type[] { Hibernate.SHORT,
						Hibernate.SHORT, Hibernate.SHORT,
						Hibernate.SHORT, Hibernate.SHORT,
						Hibernate.SHORT, Hibernate.SHORT}
		);
		if (l.size() > 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("BibliographicValidation(s) found:");
				for (int i = 0; i < l.size(); i++) {
					logger.debug(l.get(i));
				}
			}
			return (BibliographicValidation) l.get(0);
		} else {

			l = find("select distinct v from BibliographicValidation as v, " +
							"BibliographicCorrelation as c" +
							" where c.key.marcTagCategoryCode = ?"+
							" and c.key.marcTag = v.key.marcTag" +
							" and c.key.marcTagCategoryCode =  v.key.marcTagCategoryCode",
					new Object[] { new Short(s)},
					new Type[] { Hibernate.SHORT}
			);
			if (l.size() > 0) {
				if (logger.isDebugEnabled()) {
					logger.debug("BibliographicValidation(s) found:");
					for (int i = 0; i < l.size(); i++) {
						logger.debug(l.get(i));
					}
				}
				return (BibliographicValidation) l.get(0);
			} else {
				logger.warn("No validation found for category " + s + " and values " + values.toString());
				throw new MarcCorrelationException("no Validation found");
			}
		}
	}
}
