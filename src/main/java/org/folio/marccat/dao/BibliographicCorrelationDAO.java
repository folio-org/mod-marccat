package org.folio.marccat.dao;


import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.dao.persistence.*;
import org.folio.marccat.exception.DataAccessException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import static java.util.stream.Collectors.toList;

/**
 * Manages access to table S_BIB_MARC_IND_DB_CRLTN database encoding and MARC21 encoding.
 *
 * @author paulm
 * @author natasciab
 * @since 1.0
 */
public class BibliographicCorrelationDAO extends CorrelationDAO {


  private final Log logger = new Log(BibliographicCorrelationDAO.class);
  private String queryFirstInd = " bc.key.marcFirstIndicator <> '@' and ";
  private String querySecondInd = " bc.key.marcSecondIndicator <> '@' and ";
  private String queryFrom = "from BibliographicCorrelation as bc ";
  private String queryWhereTag = "where bc.key.marcTag = ? and ";
  private String queryOrFistInd = "(bc.key.marcFirstIndicator = ? or bc.key.marcFirstIndicator = 'S' )and ";
  private String queryOrSecondInd = "(bc.key.marcSecondIndicator = ? or bc.key.marcSecondIndicator = 'S')and ";




  /**
   * Returns the BibliographicCorrelation based on MARC encoding and category code.
   *
   * @param tag             -- marc tag
   * @param firstIndicator  -- marc first indicator
   * @param secondIndicator -- marc second indicator
   * @param categoryCode    -- category code
   * @return a BibliographicCorrelation object or null when none found
   */
  @SuppressWarnings("unchecked")
  public BibliographicCorrelation getBibliographicCorrelation(
    final Session session,
    final String tag,
    final char firstIndicator,
    final char secondIndicator,
    final int categoryCode) throws HibernateException {

    final List<BibliographicCorrelation> correlations =
      (categoryCode != 0)
        ? session.find(
        queryFrom
          + queryWhereTag
          + queryOrFistInd
          + queryFirstInd
          + queryOrSecondInd
          + "bc.key.marcSecondIndicator <> '@' and "
          + "bc.key.marcTagCategoryCode = ?",
        new Object[]{tag, firstIndicator, secondIndicator, categoryCode},
        new Type[]{Hibernate.STRING, Hibernate.CHARACTER, Hibernate.CHARACTER, Hibernate.INTEGER})
        : session.find(
        queryFrom
          + queryWhereTag
          + queryOrFistInd
          + queryFirstInd
          + queryOrSecondInd
          + "bc.key.marcSecondIndicator <> '@' order by bc.key.marcTagCategoryCode asc",
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
   * @return a list of bibliographic correlation.
   * @throws HibernateException in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  public List<BibliographicCorrelation> getCategoryCorrelation(
    final Session session,
    final String tag,
    final char firstIndicator,
    final char secondIndicator) throws HibernateException {

    try {
      return session.find(
        queryFrom
          + queryWhereTag
          + queryOrFistInd
          + queryFirstInd
          + queryOrSecondInd
          + "bc.key.marcSecondIndicator <> '@' order by bc.key.marcTagCategoryCode asc",
        new Object[]{tag, firstIndicator, secondIndicator},
        new Type[]{Hibernate.STRING, Hibernate.CHARACTER, Hibernate.CHARACTER});

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
  @SuppressWarnings("unchecked")
  public CorrelationKey getMarcEncoding(
    final int category, final int firstCorrelation,
    final int secondCorrelation, final int thirdCorrelation, final Session session) throws HibernateException {
    final List<Correlation> result = session.find(
      queryFrom
        + "where bc.key.marcTagCategoryCode = ? and "
        + "bc.databaseFirstValue = ? and "
        + "bc.databaseSecondValue = ? and "
        + "bc.databaseThirdValue = ?",
      new Object[]{category, firstCorrelation, secondCorrelation, thirdCorrelation},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.INTEGER});

    return result.stream().filter(Objects::nonNull).findFirst().map(Correlation::getKey).orElse(null);
  }



  /**
   *  Loads tags list using the like operator on tag.
   *
   * @param session   the session of hibernate
   * @param tagNumber the tag number used as filter criterion.
   * @return
   * @throws DataAccessException
   */
  public List <String> getFilteredTagsList (final String tagNumber, final Session session) throws HibernateException {
    final StringBuilder sqlFilter = new StringBuilder("select distinct bc.key.marcTag ")
      .append(" from BibliographicCorrelation as bc, BibliographicValidation as v ")
      .append(" where bc.key.marcTag like ? ||'%' and ")
      .append(" bc.key.marcTag not like '%@%' and ")
      .append(queryFirstInd)
      .append(querySecondInd)
      .append(" v.key.marcTag = bc.key.marcTag and ")
      .append(" v.marcTagObsoleteIndicator = '0' order by bc.key.marcTag ");
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
    final StringBuilder sqlFilter = new StringBuilder(" select bc.key from BibliographicCorrelation as bc, BibliographicValidation as v  ")
      .append(queryWhereTag)
      .append(queryFirstInd)
      .append(querySecondInd)
      .append(" v.key.marcTag = bc.key.marcTag and ")
      .append(" v.marcTagObsoleteIndicator = '0'");
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
  public String getSubfieldsTag (final String tagNumber, final Session session) throws HibernateException {
    final StringBuilder sqlFilter = new StringBuilder(" select distinct v.marcValidSubfieldStringCode  from BibliographicValidation as v ")
      .append(" where v.key.marcTag = ?");
      java.util.Optional optional  = session.find(sqlFilter.toString(),
      new Object[]{tagNumber},
      new Type[]{Hibernate.STRING}).stream().findFirst();
      return optional.isPresent() ?  optional.get().toString() :  "a";
    }

}
