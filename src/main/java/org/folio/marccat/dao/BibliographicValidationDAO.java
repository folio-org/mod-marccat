package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.dao.persistence.BibliographicValidation;
import org.folio.marccat.dao.persistence.BibliographicValidationKey;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.MarcCorrelationException;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.shared.Validation;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author elena
 * @author natasciab
 * @author cchiama
 * @since 1.0
 */
public class BibliographicValidationDAO extends DAOValidation {

  private static final Log logger = LogFactory.getLog(BibliographicValidationDAO.class);
  private String querySelect = "select distinct v from BibliographicValidation as v, ";
  private String queryFrom = "BibliographicCorrelation as c";
  private String queryWhere = " where c.key.marcTagCategoryCode = ?";
  private String queryAndTag = " and c.key.marcTag = v.key.marcTag";
  private String queryCategory = " and c.key.marcTagCategoryCode =  v.key.marcTagCategoryCode";

  public BibliographicValidation load(final Session session, final String marcNumber, final int category) throws HibernateException {
    return (BibliographicValidation) session.load(BibliographicValidation.class, new BibliographicValidationKey(marcNumber, category));
  }

  /**
   * @param session  the session of hibernate
   * @param category the marc category used as filter criterion
   * @param values   the correlation values used as filter criterion
   * @return a BibliographicValidation object containing
   * the MARC subfield list or null when not found
   * @throws DataAccessException
   */
  @SuppressWarnings("unchecked")
  public BibliographicValidation load(final Session session, final int category, final CorrelationValues values) throws HibernateException {

    List<BibliographicValidation> bibliographicValidations = session.find(querySelect +
        queryFrom +
        queryWhere +
        " and (c.databaseFirstValue = ? or c.databaseFirstValue = -1 or -1 = ?)" +
        " and (c.databaseSecondValue = ? or c.databaseSecondValue = -1 or -1 = ?)" +
        " and (c.databaseThirdValue = ? or c.databaseThirdValue = -1 or -1 = ?)" +
        queryAndTag +
        queryCategory,
      new Object[]{
        category,
        values.getValue(1), values.getValue(1),
        values.getValue(2), values.getValue(2),
        values.getValue(3), values.getValue(3)},
      new Type[]{
        Hibernate.INTEGER,
        Hibernate.INTEGER, Hibernate.INTEGER,
        Hibernate.INTEGER, Hibernate.INTEGER,
        Hibernate.INTEGER, Hibernate.INTEGER}
    );

    Optional<BibliographicValidation> firstElement = bibliographicValidations.stream().filter(Objects::nonNull).findFirst();
    if (firstElement.isPresent()) {
      return firstElement.get();
    }

    bibliographicValidations = session.find(querySelect +
        queryFrom +
        queryWhere +
        queryAndTag +
        queryCategory,
      new Object[]{category},
      new Type[]{Hibernate.INTEGER});

    firstElement = bibliographicValidations.stream().filter(Objects::nonNull).findFirst();
    if (firstElement.isPresent()) {
      return firstElement.get();
    } else {
      logger.error(String.format(Message.MOD_MARCCAT_00014_NO_VALIDATION_FOUND, category, values.toString()));
      return null;
    }
  }

  @Deprecated
  public BibliographicValidation load(final String marcNumber, final int marcCategory) {
    return (BibliographicValidation) load(BibliographicValidation.class, new BibliographicValidationKey(marcNumber, marcCategory));
  }

  @Deprecated
  public Validation load(final int s, final CorrelationValues values) {
    List<BibliographicValidation> validations = find(querySelect +
        queryFrom +
        queryWhere +
        " and (c.databaseFirstValue = ? or c.databaseFirstValue = -1 or -1 = ?)" +
        " and (c.databaseSecondValue = ? or c.databaseSecondValue = -1 or -1 = ?)" +
        " and (c.databaseThirdValue = ? or c.databaseThirdValue = -1 or -1 = ?)" +
        queryAndTag +
        queryCategory,
      new Object[]{
        s,
        values.getValue(1), values.getValue(1),
        values.getValue(2), values.getValue(2),
        values.getValue(3), values.getValue(3)},
      new Type[]{
        Hibernate.INTEGER,
        Hibernate.INTEGER, Hibernate.INTEGER,
        Hibernate.INTEGER, Hibernate.INTEGER,
        Hibernate.INTEGER, Hibernate.INTEGER}
    );

    if (!validations.isEmpty()) {
      if (logger.isDebugEnabled()) {
        logger.debug("BibliographicValidation(s) found:");
        for (int i = 0; i < validations.size(); i++) {
          logger.debug(validations.get(i));
        }
      }
      return validations.get(0);
    } else {
      validations = find(querySelect +
          queryFrom +
          queryWhere +
          queryAndTag +
          queryCategory,
        new Object[]{s},
        new Type[]{Hibernate.INTEGER}
      );

      if (!validations.isEmpty()) {
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
