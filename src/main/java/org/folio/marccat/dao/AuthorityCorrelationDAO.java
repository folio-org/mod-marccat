package org.folio.marccat.dao;

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
