package org.folio.marccat.dao;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.dao.persistence.AuthorityCorrelation;
import org.folio.marccat.dao.persistence.Correlation;
import org.folio.marccat.dao.persistence.CorrelationKey;
import org.folio.marccat.exception.DataAccessException;

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
public class AuthorityCorrelationDAO extends CorrelationDAO {
	private final Log logger = new Log(AuthorityCorrelationDAO.class);
	private String queryFirstInd = " ac.key.marcFirstIndicator <> '@' and ";
	private String querySecondInd = " ac.key.marcSecondIndicator <> '@' and ";
	private String queryFrom = "from AuthorityCorrelation as ac ";
	private String queryWhereTag = "where ac.key.marcTag = ? and ";
	private String queryOrFistInd = "(ac.key.marcFirstIndicator = ? or ac.key.marcFirstIndicator = 'S' ) and ";
	private String queryOrSecondInd = "(ac.key.marcSecondIndicator = ? or ac.key.marcSecondIndicator = 'S') and ";

	/**
	 * Returns the AuthorityCorrelation based on MARC encoding and category code.
	 *
	 * @param tag             -- marc tag
	 * @param firstIndicator  -- marc first indicator
	 * @param secondIndicator -- marc second indicator
	 * @param categoryCode    -- category code
	 * @return a AuthorityCorrelation object or null when none found
	 */
	@SuppressWarnings("unchecked")
	public AuthorityCorrelation getAuthorityCorrelation(final Session session, final String tag,
			final char firstIndicator, final char secondIndicator, final int categoryCode) throws HibernateException {

		final List<AuthorityCorrelation> correlations = (categoryCode != 0)
				? session.find(
						queryFrom + queryWhereTag + queryOrFistInd + queryFirstInd + queryOrSecondInd
								+ "ac.key.marcSecondIndicator <> '@' and " + "ac.key.marcTagCategoryCode = ?",
						new Object[] { tag, firstIndicator, secondIndicator, categoryCode },
						new Type[] { Hibernate.STRING, Hibernate.CHARACTER, Hibernate.CHARACTER, Hibernate.INTEGER })
				: session.find(
						queryFrom + queryWhereTag + queryOrFistInd + queryFirstInd + queryOrSecondInd
								+ "ac.key.marcSecondIndicator <> '@' order by ac.key.marcTagCategoryCode asc",
						new Object[] { tag, firstIndicator, secondIndicator },
						new Type[] { Hibernate.STRING, Hibernate.CHARACTER, Hibernate.CHARACTER });

		return correlations.stream().filter(Objects::nonNull).findFirst().orElse(null);
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
	@SuppressWarnings("unchecked")
	public List<AuthorityCorrelation> getCategoryCorrelation(final Session session, final String tag,
			final char firstIndicator, final char secondIndicator) throws HibernateException {

		try {
			return session.find(
					queryFrom + queryWhereTag + queryOrFistInd + queryFirstInd + queryOrSecondInd
							+ "ac.key.marcSecondIndicator <> '@' order by ac.key.marcTagCategoryCode asc",
					new Object[] { tag, firstIndicator, secondIndicator },
					new Type[] { Hibernate.STRING, Hibernate.CHARACTER, Hibernate.CHARACTER });

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
	 * @return a AuthorityCorrelationKey object containing the MARC encoding (tag
	 *         and indicators) or null when none found.
	 */
	@SuppressWarnings("unchecked")
	public CorrelationKey getMarcEncoding(final int category, final String headingType, final int firstCorrelation,
			final int secondCorrelation, final int thirdCorrelation, final Session session) throws HibernateException {
		final List<Correlation> result = session.find(
				queryFrom + "where ac.key.marcTagCategoryCode = ? and " + "ac.key.headingType = ? and "
						+ "ac.databaseFirstValue = ? and " + "ac.databaseSecondValue = ? and "
						+ "ac.databaseThirdValue = ?",
				new Object[] { category, headingType, firstCorrelation, secondCorrelation, thirdCorrelation },
				new Type[] { Hibernate.INTEGER, Hibernate.STRING, Hibernate.INTEGER, Hibernate.INTEGER,
						Hibernate.INTEGER });

		return result.stream().filter(Objects::nonNull).findFirst().map(Correlation::getKey).orElse(null);
	}

	/**
	 * Loads tags list using the like operator on tag.
	 *
	 * @param session   the session of hibernate
	 * @param tagNumber the tag number used as filter criterion.
	 * @return
	 * @throws DataAccessException
	 */
	public List<String> getFilteredTagsList(final String tagNumber, final Session session) throws HibernateException {
		final StringBuilder sqlFilter = new StringBuilder("select distinct ac.key.marcTag ")
				.append(" from AuthorityCorrelation as ac, AuthorityValidation as v ")
				.append(" where ac.key.marcTag like ? ||'%' and ").append(" ac.key.marcTag not like '%@%' and ")
				.append(queryFirstInd).append(querySecondInd).append(" v.key.marcTag = ac.key.marcTag and ")
				.append(" v.marcTagObsoleteIndicator = '0' order by ac.key.marcTag ");
		return session.find(sqlFilter.toString(), new Object[] { tagNumber }, new Type[] { Hibernate.STRING });
	}

	/**
	 * Loads the indicators and the subfields of a tag.
	 *
	 * @param session   the session of hibernate
	 * @param tagNumber the tag number used as filter criterion.
	 * @return
	 * @throws HibernateException
	 */
	public List<CorrelationKey> getFilteredTag(final String tagNumber, final Session session)
			throws HibernateException {
		final StringBuilder sqlFilter = new StringBuilder(
				" select ac.key from AuthorityCorrelation as ac, AuthorityValidation as v  ").append(queryWhereTag)
						.append(queryFirstInd).append(querySecondInd).append(" v.key.marcTag = ac.key.marcTag and ")
						.append(" v.marcTagObsoleteIndicator = '0'");
		return session.find(sqlFilter.toString(), new Object[] { tagNumber }, new Type[] { Hibernate.STRING });
	}



}
