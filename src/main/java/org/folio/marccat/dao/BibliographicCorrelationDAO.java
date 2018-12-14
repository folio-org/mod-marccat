package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.MessageCatalog;
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
public class BibliographicCorrelationDAO extends DAOCorrelation {

  public static final String SELECT_CLASSIFICATION_TAG_LABELS =
    "SELECT AA.TBL_SEQ_NBR, AA.TYP_VLU_CDE, aa.FNCTN_VLU_CDE, AA.TBL_VLU_OBSLT_IND, AA.SHORT_STRING_TEXT, AA.STRING_TEXT, AA.LANGID"
      + " FROM "
      + " olisuite.S_BIB_CLSTN_TYP_FNCTN AA,"
      + " olisuite.S_BIB_MARC_IND_DB_CRLTN BC"
      + " WHERE BC.MARC_TAG_CAT_CDE = ?"
      + " AND BC.MARC_TAG_1ST_IND <> '@'"
      + " AND BC.MARC_TAG_2ND_IND <> '@'"
      + " AND BC.MARC_TAG_IND_VLU_1_CDE = ?"
      + " AND BC.MARC_TAG_IND_VLU_2_CDE = AA.FNCTN_VLU_CDE"
      + " AND BC.MARC_TAG_IND_VLU_1_CDE = AA.TYP_VLU_CDE"
      + " AND AA.TBL_VLU_OBSLT_IND = '0'"
      + " ORDER BY AA.TBL_SEQ_NBR";
  final private Log logger = new Log(BibliographicCorrelationDAO.class);

  /**
   * Returns the BibliographicCorrelation from BibliographicCorrelationKey.
   *
   * @param bibliographicCorrelationKey -- the database bibliographicCorrelationKey
   * @return a BibliographicCorrelation object containing or null when none found
   */
  public Correlation getBibliographicCorrelation(final Session session, final CorrelationKey bibliographicCorrelationKey) throws HibernateException {
    return getBibliographicCorrelation(session,
      bibliographicCorrelationKey.getMarcTag(),
      bibliographicCorrelationKey.getMarcFirstIndicator(),
      bibliographicCorrelationKey.getMarcSecondIndicator(),
      bibliographicCorrelationKey.getMarcTagCategoryCode());
  }

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
        "from BibliographicCorrelation as bc "
          + "where bc.key.marcTag = ? and "
          + "(bc.key.marcFirstIndicator = ? or bc.key.marcFirstIndicator = 'S' )and "
          + "bc.key.marcFirstIndicator <> '@' and "
          + "(bc.key.marcSecondIndicator = ? or bc.key.marcSecondIndicator = 'S')and "
          + "bc.key.marcSecondIndicator <> '@' and "
          + "bc.key.marcTagCategoryCode = ?",
        new Object[]{tag, firstIndicator, secondIndicator, categoryCode},
        new Type[]{Hibernate.STRING, Hibernate.CHARACTER, Hibernate.CHARACTER, Hibernate.INTEGER})
        : session.find(
        "from BibliographicCorrelation as bc "
          + "where bc.key.marcTag = ? and "
          + "(bc.key.marcFirstIndicator = ? or bc.key.marcFirstIndicator = 'S' )and "
          + "bc.key.marcFirstIndicator <> '@' and "
          + "(bc.key.marcSecondIndicator = ? or bc.key.marcSecondIndicator = 'S')and "
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
        "from BibliographicCorrelation as bc "
          + "where bc.key.marcTag = ? and "
          + "(bc.key.marcFirstIndicator = ? or bc.key.marcFirstIndicator = 'S' )and "
          + "bc.key.marcFirstIndicator <> '@' and "
          + "(bc.key.marcSecondIndicator = ? or bc.key.marcSecondIndicator = 'S')and "
          + "bc.key.marcSecondIndicator <> '@' order by bc.key.marcTagCategoryCode asc",
        new Object[]{tag, firstIndicator, secondIndicator},
        new Type[]{Hibernate.STRING, Hibernate.CHARACTER, Hibernate.CHARACTER});

    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      return Collections.emptyList();
    }
  }

  /**
   * Gets third correlation values by marc category, first and second correlations.
   *
   * @param session    the hibernate session
   * @param category   the marc category used as filter criterion
   * @param value1     the first correlation value used as filter criterion
   * @param value2     the second correlation value used as filter criterion
   * @param classTable the mapped class in the hibernate configuration
   * @param locale     the locale associated to language used as filter criterion
   * @return list apv value.
   * @throws DataAccessException in case of data access failure.
   */
  @SuppressWarnings("unchecked")
  public List<Avp<String>> getThirdCorrelationList(final Session session,
                                                   final int category,
                                                   final int value1,
                                                   final int value2,
                                                   final Class classTable,
                                                   final Locale locale) throws DataAccessException {
    try {
      final List<CodeTable> codeTables = session.find(" select distinct ct from "
          + classTable.getName()
          + " as ct, BibliographicCorrelation as bc "
          + " where bc.key.marcTagCategoryCode = ? and "
          + " bc.key.marcFirstIndicator <> '@' and "
          + " bc.key.marcSecondIndicator <> '@' and "
          + " bc.databaseFirstValue = ? and "
          + " bc.databaseSecondValue = ? and "
          + " bc.databaseThirdValue = ct.code and "
          + " ct.obsoleteIndicator = '0' and "
          + " ct.language = ? "
          + " order by ct.sequence ",
        new Object[]{category, value1, value2, locale.getISO3Language()},
        new Type[]{Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.STRING});

      return codeTables
        .stream()
        .map(codeTable -> (Avp<String>) new Avp(codeTable.getCodeString().trim(), codeTable.getLongText()))
        .collect(toList());

    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
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
      "from BibliographicCorrelation as bc "
        + "where bc.key.marcTagCategoryCode = ? and "
        + "bc.databaseFirstValue = ? and "
        + "bc.databaseSecondValue = ? and "
        + "bc.databaseThirdValue = ?",
      new Object[]{category, firstCorrelation, secondCorrelation, thirdCorrelation},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.INTEGER});

    return result.stream().filter(Objects::nonNull).findFirst().map(Correlation::getKey).orElse(null);
  }

  /**
   * Get second correlation values by marc category and first correlation.
   *
   * @param session    the hibernate session
   * @param category   the marc category used as filter criterion
   * @param value1     the first correlation value used as filter criterion
   * @param classTable the mapped class in the hibernate configuration
   * @param locale     the locale associated to language used as filter criterion
   * @return
   * @throws DataAccessException
   */
  @SuppressWarnings("unchecked")
  public List<Avp<String>> getSecondCorrelationList(final Session session,
                                                    final int category,
                                                    final int value1,
                                                    final Class classTable,
                                                    final Locale locale) throws DataAccessException {
    try {
      final List<CodeTable> codeTables = session.find("Select distinct ct from  "
          + classTable.getName()
          + " as ct, BibliographicCorrelation as bc "
          + " where bc.key.marcTagCategoryCode = ? and "
          + " bc.key.marcFirstIndicator <> '@' and "
          + " bc.key.marcSecondIndicator <> '@' and "
          + " bc.databaseFirstValue = ? and "
          + " bc.databaseSecondValue = ct.code and "
          + " ct.obsoleteIndicator = '0' and "
          + " ct.language = ? "
          + " order by ct.sequence ",
        new Object[]{category, value1, locale.getISO3Language()},
        new Type[]{Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.STRING});

      return codeTables
        .stream()
        .map(codeTable -> (Avp<String>) new Avp(codeTable.getCodeString().trim(), codeTable.getLongText()))
        .collect(toList());
    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      return Collections.emptyList();
    }
  }

  /**
   * Gets the first allowed value for second correlation by first and third values.
   *
   * @param category         -- the marc category code.
   * @param firstCorrelation -- the first correlation value.
   * @param thirdCorrelation -- the third correlation value.
   * @param session          -- the current hibernate session.
   * @return allowed second correlation value.
   * @throws HibernateException in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  public int getFirstValueFromSecondCorrelations(final int category, final int firstCorrelation, final int thirdCorrelation, final Session session) throws HibernateException {
    List<BibliographicCorrelation> result = session.find(
      " from BibliographicCorrelation as bc "
        + " where bc.key.marcTagCategoryCode = ? and "
        + " bc.key.marcFirstIndicator <> '@' and "
        + " bc.key.marcSecondIndicator <> '@' and "
        + " bc.databaseFirstValue = ? and "
        + " bc.databaseThirdValue = ? ",
      new Object[]{category, firstCorrelation, thirdCorrelation},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.INTEGER});

    return result.stream().findFirst().map(Correlation::getDatabaseSecondValue).orElse(-1);
  }

  @SuppressWarnings("unchecked")
  public String getClassificationIndexByShelfType(final int shelfType, final Session session) throws HibernateException {
    List<BibliographicCorrelation> l = session.find("from BibliographicCorrelation as bc "
        + " where bc.key.marcTagCategoryCode = 13 and "
        + " bc.databaseFirstValue = ? ",
      new Object[]{shelfType},
      new Type[]{Hibernate.INTEGER});

    if (l.size() == 1) {
      String s = l.stream().findFirst().map(Correlation::getSearchIndexTypeCode).toString();
      return new DAOIndexList().getIndexByEnglishAbreviation(s);
    } else {
      return null;
    }
  }

  /**
   * Gets correlations from selected index.
   *
   * @param selectedIndex -- the selected index.
   * @param session       -- the current hibernate session.
   * @return {@link#CorrelationKey}
   * @throws HibernateException in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  public CorrelationKey getMarcTagCodeBySelectedIndex(final String selectedIndex, final Session session) throws HibernateException {
    if (selectedIndex == null) {
      return null;
    }

    final List<Correlation> list =
      session.find("from BibliographicCorrelation as bc "
          + " where bc.searchIndexTypeCode = ?"
          + " or bc.searchIndexTypeCode = ?",
        new Object[]{selectedIndex.substring(0, 2), selectedIndex.substring(0, 2).toLowerCase()},
        new Type[]{Hibernate.STRING, Hibernate.STRING});
    return list.stream().findFirst().map(Correlation::getKey).orElse(null);
  }

  /**
   * Gets correlations from selected index and tag.
   *
   * @param idx     -- the selected index.
   * @param tag     -- the tag number.
   * @param session -- the current hibernate session.
   * @return {@link#CorrelationKey}
   * @throws HibernateException in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  public CorrelationKey getMarcTagCodeBySelectedIndex(final String idx, final String tag, final Session session) throws HibernateException {
    final List<Correlation> list =
      session.find(
        "from BibliographicCorrelation as bc "
          + " where (bc.searchIndexTypeCode = ?"
          + " or bc.searchIndexTypeCode = ?)"
          + " and bc.key.marcTag = ?",
        new Object[]{idx.substring(0, 2), idx.substring(0, 2).toLowerCase(), tag},
        new Type[]{Hibernate.STRING, Hibernate.STRING, Hibernate.STRING});
    return list.stream().findFirst().map(Correlation::getKey).orElse(null);
  }

  /**
   * Returns the label for the tag to display.
   *
   * @return the label for the tag to display.
   * @throws DataAccessException in case of data access failure.
   */
  @SuppressWarnings("unchecked")
  public List<LabelTagDisplay> getMarcTagDisplay(final String language, final Session session) throws HibernateException {
    try {
      return session.find(
        "from MarcTagDisplay as bc where bc.language = ? ",
        new Object[]{language},
        new Type[]{Hibernate.STRING});
    } catch (final DataAccessException exception) {
      logger.debug(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      return Collections.emptyList();
    }
  }

  /**
   * Gets the second correlation list by first and third values.
   *
   * @param category         -- the marc category code.
   * @param firstCorrelation -- the first correlation value.
   * @param thirdCorrelation -- the third correlation value.
   * @param session          -- the current hibernate session.
   * @return list of second correlation values.
   * @throws HibernateException in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  public List<BibliographicCorrelation> getSecondCorrelationListByOtherCorrelations(final int category, final int firstCorrelation, final int thirdCorrelation, final Session session) throws HibernateException {
    return session.find(" from BibliographicCorrelation as bc "
        + " where bc.key.marcTagCategoryCode = ? and "
        + " bc.key.marcFirstIndicator <> '@' and "
        + " bc.key.marcSecondIndicator <> '@' and "
        + " bc.databaseFirstValue = ? and "
        + " bc.databaseThirdValue = ? ",
      new Object[]{category, firstCorrelation, thirdCorrelation},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.INTEGER});
  }

  /**
   * Loads first correlation list using note group code for tag range values.
   *
   * @param session           the session of hibernate
   * @param noteGroupTypeCode the note group code used as filter criterion.
   * @param locale            the Locale, used here as a filter criterion.
   * @return
   * @throws DataAccessException
   */
  public List<Avp<String>> getFirstCorrelationByNoteGroupCode(final Session session, final String noteGroupTypeCode, final Locale locale) throws DataAccessException {

    final String fromTag = (noteGroupTypeCode.length() == 2 ? "0" + noteGroupTypeCode : noteGroupTypeCode);
    final String toTag = String.valueOf(Integer.parseInt(noteGroupTypeCode) + 99);

    final StringBuilder sqlFilter = new StringBuilder(" and bc.key.marcSecondIndicator <> '@' ")
      .append(" and bc.databaseFirstValue = ct.code ")
      .append(" and bc.key.marcTagCategoryCode = 7 ")
      .append(" and bc.key.marcTag between '").append(fromTag)
      .append("' and '").append(toTag).append("' ");

    return getCorrelatedList(session, BibliographicNoteType.class, true, sqlFilter.toString(), locale);
  }


  /**
   * Generic method that gets first correlation using filter sql as criterion.
   *
   * @param session         the session of hibernate
   * @param clazz           the mapped class in the hibernate configuration.
   * @param alphabeticOrder true if alphabetical order
   * @param sqlFilter       the sql filter added to query
   * @param locale          the Locale, used here as a filter criterion.
   * @return
   * @throws DataAccessException in case of SQL exception.
   */
  @SuppressWarnings("unchecked")
  public List<Avp<String>> getCorrelatedList(final Session session,
                                             final Class clazz,
                                             final boolean alphabeticOrder,
                                             final String sqlFilter,
                                             final Locale locale) throws DataAccessException {
    final String sqlOrder = (alphabeticOrder ? " order by ct.longText " : " order by ct.sequence ");
    try {
      final List<CodeTable> codeTables = session.find("select distinct ct from "
          + clazz.getName()
          + " as ct, BibliographicCorrelation as bc "
          + " where ct.obsoleteIndicator = '0' "
          + " and ct.language = ? "
          + sqlFilter
          + sqlOrder,
        new Object[]{locale.getISO3Language()},
        new Type[]{Hibernate.STRING});

      return codeTables
        .stream()
        .map(codeTable -> (Avp<String>) new Avp(codeTable.getCodeString().trim(), codeTable.getLongText()))
        .collect(toList());

    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      return Collections.emptyList();
    }
  }
}
