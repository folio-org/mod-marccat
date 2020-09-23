package org.folio.marccat.dao;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.dao.persistence.AuthorityValidation;
import org.folio.marccat.dao.persistence.BibliographicValidation;
import org.folio.marccat.dao.persistence.BibliographicValidationKey;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.shared.CorrelationValues;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

/**
 * The class manages the authority record
 *
 * @author elena
 * @since 1.0
 */
public class AuthorityValidationDAO  extends ValidationDAO {
	private static final Log logger = LogFactory.getLog(AuthorityValidationDAO.class);
	  private String querySelect = "select distinct v from AuthorityValidation as v, ";
	  private String queryFrom = "AuthorityCorrelation as c";
	  private String queryWhere = " where c.key.marcTagCategoryCode = ? and c.key.headingType =?";
	  private String queryAndTag = " and c.key.marcTag = v.key.marcTag";
	  private String queryCategory = " and c.key.marcTagCategoryCode =  v.key.marcTagCategoryCode";
	  private String queryHeading = " and c.key.headingType =  v.key.headingType";

	  public BibliographicValidation load(final Session session, final String marcNumber, final int category) throws HibernateException {
	    return (BibliographicValidation) session.load(BibliographicValidation.class, new BibliographicValidationKey(marcNumber, category));
	  }

	  /**
	   * @param session  the session of hibernate
	   * @param category the marc category used as filter criterion
	   * @param values   the correlation values used as filter criterion
	   * @return a AuthorityValidation object containing
	   * the MARC subfield list or null when not found
	   * @throws DataAccessException
	   */
	  @SuppressWarnings("unchecked")
	  public AuthorityValidation load(final Session session, final int category, final String headingType,final CorrelationValues values) throws HibernateException {

	    List <AuthorityValidation> authorityValidations = session.find(querySelect +
	        queryFrom +
	        queryWhere +
	        " and (c.databaseFirstValue = ? or c.databaseFirstValue = -1 or -1 = ?)" +
	        " and (c.databaseSecondValue = ? or c.databaseSecondValue = -1 or -1 = ?)" +
	        " and (c.databaseThirdValue = ? or c.databaseThirdValue = -1 or -1 = ?)" +
	        queryAndTag +
	        queryCategory + 
	        queryHeading,
	      new Object[]{
	        category,
	        headingType,
	        values.getValue(1), values.getValue(1),
	        values.getValue(2), values.getValue(2),
	        values.getValue(3), values.getValue(3)},
	      new Type[]{
	        Hibernate.INTEGER,
	        Hibernate.STRING,
	        Hibernate.INTEGER, Hibernate.INTEGER,
	        Hibernate.INTEGER, Hibernate.INTEGER,
	        Hibernate.INTEGER, Hibernate.INTEGER}
	    );

	    Optional <AuthorityValidation> firstElement = authorityValidations.stream().filter(Objects::nonNull).findFirst();
	    if (firstElement.isPresent()) {
	      return firstElement.get();
	    }

	    authorityValidations = session.find(querySelect +
	        queryFrom +
	        queryWhere +
	        queryAndTag +
	        queryCategory + 
	        queryHeading,
	      new Object[]{category, headingType},
	      new Type[]{Hibernate.INTEGER, Hibernate.STRING});

	    firstElement = authorityValidations.stream().filter(Objects::nonNull).findFirst();
	    if (firstElement.isPresent()) {
	      return firstElement.get();
	    } else {
	      logger.error(String.format(Message.MOD_MARCCAT_00015_NO_VALIDATION_FOUND, category, values.toString()));
	      return null;
	    }
	  }
}
