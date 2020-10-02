package org.folio.marccat.dao;


import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.dao.persistence.*;
import org.folio.marccat.exception.DataAccessException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Manages access to table S_BIB_MARC_IND_DB_CRLTN database encoding and MARC21 encoding.
 *
 * @author paulm
 * @author natasciab
 * @since 1.0
 */
public class BibliographicCorrelationDAO extends RecordCorrelationDAO {

  private final Log logger = new Log(BibliographicCorrelationDAO.class);

  private String queryFrom = "from BibliographicCorrelation as c ";


  /**
   * Returns the BibliographicCorrelation based on MARC encoding and category code.
   *
   * @param tag             -- marc tag
   * @param firstIndicator  -- marc first indicator
   * @param secondIndicator -- marc second indicator
   * @param categoryCode    -- category code
   * @return a BibliographicCorrelation object or null when none found
   */
  public BibliographicCorrelation getBibliographicCorrelation(
    final Session session,
    final String tag,
    final char firstIndicator,
    final char secondIndicator,
    final int categoryCode) throws HibernateException {

	  return (BibliographicCorrelation) super.getRecordCorrelation(session, tag, firstIndicator, secondIndicator, categoryCode, queryFrom);
  }

  /**
   * Gets bibliographic correlations using tag and indicators.
   *
   * @param session         -- current hibernate session.
   * @param tag             -- the tag number.
   * @param firstIndicator  -- the 1st. indicator.
   * @param secondIndicator -- the 2nd. indicator.
   * @return a list of bibliographic correlation.
   * @throws HibernateException in case of hibernate exception.
   */
  public List<BibliographicCorrelation> getCategoryCorrelation(
    final Session session,
    final String tag,
    final char firstIndicator,
    final char secondIndicator) throws HibernateException {

    try {
    	return super.getCategoryCorrelation(session, tag, firstIndicator, secondIndicator, queryFrom).stream().map(s -> (BibliographicCorrelation) s).collect(Collectors.toList());
    } catch (final HibernateException exception) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
      return Collections.emptyList();
    }
  }

  /**
   * Returns the MARC encoding based on the input database encodings.
   *
   * @param category          -- the database category (1-name, etc...)
   * @param firstCorrelation  -- the first database code
   * @param secondCorrelation -- the second database code
   * @param thirdCorrelation  -- the third database code
   * @return a BibliographicCorrelationKey object containing
   * the MARC encoding (tag and indicators) or null when none found.
   */
  public CorrelationKey getMarcEncoding(
    final int category, final int firstCorrelation,
    final int secondCorrelation, final int thirdCorrelation, final Session session) throws HibernateException {
    
    return super.getMarcEncoding(category, firstCorrelation, secondCorrelation, thirdCorrelation, session, queryFrom);
  }
  
  /**
   *  Loads tags list using the like operator on tag.
   *
   * @param session   the session of hibernate
   * @param tagNumber the tag number used as filter criterion.
   * @return
   * @throws DataAccessException
   */
  @SuppressWarnings("unchecked")
  public List <String> getFilteredTagsList (final String tagNumber, final Session session) throws HibernateException {
    final StringBuilder sqlFilter = new StringBuilder("select distinct c.key.marcTag ")
      .append(" from BibliographicCorrelation as c, BibliographicValidation as v ")
      .append(" where c.key.marcTag like ? ||'%' and ")
      .append(" c.key.marcTag not like '%@%' and ")
      .append(queryFirstInd)
      .append(querySecondInd)
      .append(" v.key.marcTag = c.key.marcTag and ")
      .append(" v.marcTagObsoleteIndicator = '0' order by c.key.marcTag ");
    return session.find(sqlFilter.toString(),
      new Object[]{tagNumber},
      new Type[]{Hibernate.STRING});
  }


  /**
   * Loads the indicators and the subfields of a tag.
   *
   * @param session   the session of hibernate
   * @param tagNumber the tag number used as filter criterion.
   * @return
   * @throws HibernateException
   */
  public List <CorrelationKey> getFilteredTag (final String tagNumber, final Session session) throws HibernateException {
	  return super.getFilteredTag(tagNumber, " select c.key from BibliographicCorrelation as c, BibliographicValidation as v   ", session);
  }

  /**
   * Loads the indicators and the subfields of a tag.
   *
   * @param session   the session of hibernate
   * @param tagNumber the tag number used as filter criterion.
    * @return
    * @throws HibernateException
   */
  public String getSubfieldsTag (final String tagNumber, final Session session) throws HibernateException {
    final StringBuilder sqlFilter = new StringBuilder(" select distinct v.marcValidSubfieldStringCode  from BibliographicValidation as v ")
      .append(" where v.key.marcTag = ?");
      Optional optional  = session.find(sqlFilter.toString(),
      new Object[]{tagNumber},
      new Type[]{Hibernate.STRING}).stream().findFirst();
      return optional.isPresent() ?  optional.get().toString() :  "a";
    }

}
