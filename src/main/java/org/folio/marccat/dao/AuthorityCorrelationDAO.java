package org.folio.marccat.dao;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.folio.marccat.dao.persistence.AuthorityCorrelation;
import org.folio.marccat.dao.persistence.CorrelationKey;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

/**
 * The class manages the authority record
 *
 * @author elena
 */
public class AuthorityCorrelationDAO extends RecordCorrelationDAO {
  private String queryFrom = " from AuthorityCorrelation as c ";

  /**
   * Returns the AuthorityCorrelation based on MARC encoding and category code.
   *
   * @param tag             -- marc tag
   * @param firstIndicator  -- marc first indicator
   * @param secondIndicator -- marc second indicator
   * @param categoryCode    -- category code
   * @return a AuthorityCorrelation object or null when none found
   */
  public AuthorityCorrelation getAuthorityCorrelation(final Session session, final String tag,
      final char firstIndicator, final char secondIndicator, final int categoryCode) throws HibernateException {

    return (AuthorityCorrelation) super.getRecordCorrelation(session, tag, firstIndicator, secondIndicator,
        categoryCode, queryFrom);
  }

  /**
   * Gets correlations using tag and indicators.
   *
   * @param session         -- current hibernate session.
   * @param tag             -- the tag number.
   * @param firstIndicator  -- the 1st. indicator.
   * @param secondIndicator -- the 2nd. indicator.
   * @return a list of authority correlation.
   * @throws HibernateException in case of hibernate exception.
   */
  public List<AuthorityCorrelation> getCategoryCorrelation(final Session session, final String tag,
      final char firstIndicator, final char secondIndicator) throws HibernateException {

    try {
      return super.getCategoryCorrelation(session, tag, firstIndicator, secondIndicator, queryFrom).stream()
          .map(s -> (AuthorityCorrelation) s).collect(Collectors.toList());

    } catch (final HibernateException exception) {
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
   * @return a AuthorityCorrelationKey object containing the MARC encoding (tag
   *         and indicators) or null when none found.
   */
  public CorrelationKey getMarcEncoding(final int category, final int firstCorrelation, final int secondCorrelation,
      final int thirdCorrelation, final Session session) throws HibernateException {

    return super.getMarcEncoding(category, firstCorrelation, secondCorrelation, thirdCorrelation, session, queryFrom);
  }
}
