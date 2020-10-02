package org.folio.marccat.dao;

import org.folio.marccat.dao.persistence.Correlation;
import org.folio.marccat.dao.persistence.CorrelationKey;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import java.util.Objects;
import java.util.List;

public class RecordCorrelationDAO extends CorrelationDAO {
	
	protected String queryFirstInd = " c.key.marcFirstIndicator <> '@' and ";
	protected String querySecondInd = " c.key.marcSecondIndicator <> '@' and ";
	protected String queryWhereTag = "where c.key.marcTag = ? and ";
	protected String queryOrFistInd = "(c.key.marcFirstIndicator = ? or c.key.marcFirstIndicator = 'S' ) and ";
	protected String queryOrSecondInd = "(c.key.marcSecondIndicator = ? or c.key.marcSecondIndicator = 'S') and ";
	
	
	/**
	   * Returns the Correlation based on MARC encoding and category code.
	   *
	   * @param tag             -- marc tag
	   * @param firstIndicator  -- marc first indicator
	   * @param secondIndicator -- marc second indicator
	   * @param categoryCode    -- category code
	   * @param queryFrom		-- query from
	   * @return a Correlation object or null when none found
	   */
	  @SuppressWarnings("unchecked")
	  public Correlation getRecordCorrelation(
	    final Session session,
	    final String tag,
	    final char firstIndicator,
	    final char secondIndicator,
	    final int categoryCode,
	    final String queryFrom) throws HibernateException {

	    final List<Correlation> correlations =
	      (categoryCode != 0)
	        ? session.find(
	        queryFrom
	          + queryWhereTag
	          + queryOrFistInd
	          + queryFirstInd
	          + queryOrSecondInd
	          + "c.key.marcSecondIndicator <> '@' and "
	          + "c.key.marcTagCategoryCode = ?",
	        new Object[]{tag, firstIndicator, secondIndicator, categoryCode},
	        new Type[]{Hibernate.STRING, Hibernate.CHARACTER, Hibernate.CHARACTER, Hibernate.INTEGER})
	        : session.find(
	        queryFrom
	          + queryWhereTag
	          + queryOrFistInd
	          + queryFirstInd
	          + queryOrSecondInd
	          + "c.key.marcSecondIndicator <> '@' order by c.key.marcTagCategoryCode asc",
	        new Object[]{tag, firstIndicator, secondIndicator},
	        new Type[]{Hibernate.STRING, Hibernate.CHARACTER, Hibernate.CHARACTER});

	    return correlations.stream().filter(Objects::nonNull).findFirst().orElse(null);
	  }
	  
	  /**
	   * Gets correlations using tag and indicators.
	   *
	   * @param session         -- current hibernate session.
	   * @param tag             -- the tag number.
	   * @param firstIndicator  -- the 1st. indicator.
	   * @param secondIndicator -- the 2nd. indicator.
	   * @return a list of record correlation.
	   * @throws HibernateException in case of hibernate exception.
	   */
	  @SuppressWarnings("unchecked")
	  public List<Correlation> getCategoryCorrelation(
	    final Session session,
	    final String tag,
	    final char firstIndicator,
	    final char secondIndicator,
	    final String queryFrom) throws HibernateException {

	      return session.find(
	        queryFrom
	          + queryWhereTag
	          + queryOrFistInd
	          + queryFirstInd
	          + queryOrSecondInd
	          + "c.key.marcSecondIndicator <> '@' order by c.key.marcTagCategoryCode asc",
	        new Object[]{tag, firstIndicator, secondIndicator},
	        new Type[]{Hibernate.STRING, Hibernate.CHARACTER, Hibernate.CHARACTER});

	  }
	  
	  /**
	   * Returns the MARC encoding based on the input database encodings.
	   *
	   * @param category          -- the database category (1-name, etc...)
	   * @param firstCorrelation  -- the first database code
	   * @param secondCorrelation -- the second database code
	   * @param thirdCorrelation  -- the third database code
	   * @return a CorrelationKey object containing
	   * the MARC encoding (tag and indicators) or null when none found.
	   */
	  @SuppressWarnings("unchecked")
	  public CorrelationKey getMarcEncoding(
			  final int category, 
			  final int firstCorrelation,
			  final int secondCorrelation, 
			  final int thirdCorrelation, 
			  final Session session,
			  final String queryFrom) throws HibernateException {
	    final List<Correlation> result = session.find(
	      queryFrom
	        + "where c.key.marcTagCategoryCode = ? and "
	        + "c.databaseFirstValue = ? and "
	        + "c.databaseSecondValue = ? and "
	        + "c.databaseThirdValue = ?",
	      new Object[]{category, firstCorrelation, secondCorrelation, thirdCorrelation},
	      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.INTEGER});

	    return result.stream().filter(Objects::nonNull).findFirst().map(Correlation::getKey).orElse(null);
	  }
	  
	  /**
	 * Loads the indicators and the subfields of a tag.
	 *
	 * @param session   the session of hibernate
	 * @param tagNumber the tag number used as filter criterion.
	 * @param queryFrom
	 * @return
	 * @throws HibernateException
	 */
	  @SuppressWarnings("unchecked")
	public List<CorrelationKey> getFilteredTag(final String tagNumber, String queryFrom, final Session session)
			throws HibernateException {
		final StringBuilder sqlFilter = new StringBuilder(
				queryFrom)
			.append(queryWhereTag)
			.append(queryFirstInd)
			.append(querySecondInd)
			.append(" v.key.marcTag = c.key.marcTag and ")
			.append(" v.marcTagObsoleteIndicator = '0'");
		return session.find(sqlFilter.toString(), tagNumber, Hibernate.STRING);
	}

}
