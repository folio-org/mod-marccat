/*
 * (c) LibriCore
 *
 * Created on Aug 6, 2004
 *
 * $Author: Paulm $
 */
package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.dao.persistence.Correlation;
import org.folio.marccat.dao.persistence.CorrelationKey;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.ModMarccatException;
import org.folio.marccat.shared.CorrelationValues;

import java.util.List;

/**
 * Manages access to table S_AUT_MARC_IND_DB_CRLTN -- the correlation between
 * AMICUS database encoding and MARC21 authorities encoding
 *
 * @author paulm
 * @version $Revision: 1.4 $, $Date: 2006/01/11 13:36:22 $
 */
public class DAOAuthorityCorrelation extends DAOCorrelation {
  private static final Log logger = LogFactory
    .getLog(DAOAuthorityCorrelation.class);

  private static final String ALPHABETICAL_ORDER = " order by ct.longText ";
  private static final String SEQUENCE_ORDER = " order by ct.sequence ";
  private String querySelect = " select distinct ct from T_MARC_REF_TYP ";
  private String queryDbFirstVal = " ac.databaseFirstValue = ? and ";
  private String queryKeyHeading = " ac.key.headingType = ? and ";
  private String queryAuthCorr = " as ct, AuthorityCorrelation as ac ";
  private String queryWhereCatCode = " where ac.key.marcTagCategoryCode = ? and ";
  private String queryfromAuthCorr = "from AuthorityCorrelation as ac ";
  private String queryWhereMarcTag = "where ac.key.marcTag = ? and ";

  /**
   * Returns the AuthorityCorrelation from CorrelationKey
   * <p>
   * the database correlationKey
   *
   * @return a Correlation object containing or null when none found
   */
  public Correlation getAuthorityCorrelation(CorrelationKey correlationKey) {
    return getFirstAuthorityCorrelation(correlationKey.getMarcTag(),
      correlationKey.getMarcFirstIndicator(), correlationKey
        .getMarcSecondIndicator(), correlationKey
        .getMarcTagCategoryCode());
  }

  /**
   * Returns the Correlation based on MARC encoding and category code
   *
   * @param tag             --
   *                        marc tag
   * @param firstIndicator  --
   *                        marc first indicator
   * @param secondIndicator --
   *                        marc second indicator
   * @param categoryCode    --
   *                        category code
   * @return a Correlation object or null when none found
   */

  public Correlation getAuthorityCorrelation(String tag, char firstIndicator, char secondIndicator, short categoryCode) {

    List l = find(queryfromAuthCorr
        + queryWhereMarcTag
        + "ac.key.marcFirstIndicator = ? and "
        + "ac.key.marcSecondIndicator = ? and "
        + "ac.key.marcTagCategoryCode = ?", new Object[]{
        tag, firstIndicator,
        secondIndicator, categoryCode},
      new Type[]{Hibernate.STRING, Hibernate.CHARACTER,
        Hibernate.CHARACTER, Hibernate.SHORT});

    if (l.size() == 1) {
      return (Correlation) l.get(0);
    } else {
      return null;
    }
  }

  // "from BibliographicCorrelation as bc "
  // + "where bc.key.marcTag = ? and "
  // + "(bc.key.marcFirstIndicator = ? or bc.key.marcFirstIndicator='S' )and "
  // //Natascia 13/06/2007: scommentate chiocciole
  // + "bc.key.marcFirstIndicator <> '@' and "
  // + "(bc.key.marcSecondIndicator = ? or bc.key.marcSecondIndicator='S')and
  // "
  // + "bc.key.marcSecondIndicator <> '@' and "
  // + "bc.key.marcTagCategoryCode = ?",

  /**
   * Returns the Correlation based on MARC encoding and category code
   *
   * @param tag             --
   *                        marc tag
   * @param firstIndicator  --
   *                        marc first indicator
   * @param secondIndicator --
   *                        marc second indicator
   * @param categoryCode    --
   *                        category code
   * @return a Correlation object or null when none found
   */
  public Correlation getFirstAuthorityCorrelation(String tag, char firstIndicator, char secondIndicator, int categoryCode) {

    List l = null;
    if (categoryCode != 0) {
      l = find(
        queryfromAuthCorr
          + queryWhereMarcTag
          + "(ac.key.marcFirstIndicator = ? or ac.key.marcFirstIndicator in ('S', 'O')) and "
          + "(ac.key.marcSecondIndicator = ? or ac.key.marcSecondIndicator in ('S', 'O') ) and "
          + "ac.key.marcTagCategoryCode = ?", new Object[]{
          tag, firstIndicator,
          secondIndicator,
          categoryCode}, new Type[]{
          Hibernate.STRING, Hibernate.CHARACTER,
          Hibernate.CHARACTER, Hibernate.INTEGER});
    } else {

      l = find(
        queryfromAuthCorr
          + queryWhereMarcTag
          + "(ac.key.marcFirstIndicator = ? or ac.key.marcFirstIndicator in ('S', 'O') )and "
          + "(ac.key.marcSecondIndicator = ? or ac.key.marcSecondIndicator in ('S', 'O') ) order by ac.key.marcTagCategoryCode asc",
        new Object[]{tag,
          firstIndicator,
          secondIndicator}, new Type[]{
          Hibernate.STRING, Hibernate.CHARACTER,
          Hibernate.CHARACTER});
    }

    if (l != null && !l.isEmpty()) {
      return (Correlation) l.get(0);
    } else {
      return null;
    }
  }

  /**
   * Returns the MARC encoding based on the input database encodings
   *
   * @param category --
   *                 the database category (1-name, etc...)
   * @param value1   --
   *                 the first database code
   * @param value2   --
   *                 the second database code
   * @param value3   --
   *                 the third database code
   * @return a BibliographicCorrelationKey object containing the MARC encoding
   * (tag and indicators) or null when none found
   */
  public CorrelationKey getMarcEncoding(int category, String headingType, int value1, int value2, int value3) {

    List l = find(queryfromAuthCorr
      + "where ac.key.marcTagCategoryCode = ? and "
      + "ac.key.headingType = ? and "
      + "ac.databaseFirstValue = ? and "
      + "ac.databaseSecondValue = ? and "
      + "ac.databaseThirdValue = ?", new Object[]{
      category, headingType, value1,
      value2, value3}, new Type[]{
      Hibernate.INTEGER, Hibernate.STRING, Hibernate.INTEGER,
      Hibernate.INTEGER, Hibernate.INTEGER});

    if (l.size() == 1) {
      return ((Correlation) l.get(0)).getKey();
    } else {
      return null;
    }
  }

  public List getValidReferenceTypeList(Tag t) {
    CorrelationValues v = t.getCorrelationValues();
    int position = v.getLastUsedPosition().intValue();

    if (position == 1) {
      return find(querySelect
          + queryAuthCorr
          + queryWhereCatCode
          + queryKeyHeading
          + " ac.databaseFirstValue = ct.code order by ct.sequence ",
        new Object[]{t.getCategory(),
          t.getHeadingType()},
        new Type[]{
          Hibernate.INTEGER,
          Hibernate.STRING});
    } else if (position == 2) {
      return find(
        querySelect
          + queryAuthCorr
          + queryWhereCatCode
          + queryKeyHeading
          + queryDbFirstVal
          + " ac.databaseSecondValue = ct.code order by ct.sequence ",
        new Object[]{t.getCategory(),
          t.getHeadingType(), v.getValue(1)},
        new Type[]{Hibernate.INTEGER, Hibernate.STRING,
          Hibernate.INTEGER});
    } else if (position == 3) {
      return find(querySelect
          + queryAuthCorr
          + queryWhereCatCode
          + queryKeyHeading
          + queryDbFirstVal
          + " ac.databaseSecondValue = ? and "
          + " ac.databaseThirdValue = ct.code order by ct.sequence ",
        new Object[]{t.getCategory(),
          t.getHeadingType(), v.getValue(1),
          v.getValue(2)}, new Type[]{
          Hibernate.INTEGER, Hibernate.STRING, Hibernate.INTEGER,
          Hibernate.INTEGER});
    } else {
      throw new ModMarccatException("Invalid correlation data");
    }
  }

  public List getSecondCorrelationList(int category, String headingType, int value1, Class codeTable) {

    return find(" select distinct ct from " + codeTable.getName()
        + queryAuthCorr
        + queryWhereCatCode
        + queryKeyHeading
        + queryDbFirstVal
        + " ac.databaseSecondValue = ct.code order by ct.sequence ",
      new Object[]{category, headingType,
        value1}, new Type[]{Hibernate.INTEGER,
        Hibernate.STRING, Hibernate.INTEGER});
  }

  public List getThirdCorrelationList(int category, String headingType, int value1, int value2, Class codeTable) {

    return find(" select distinct ct from " + codeTable.getName()
        + queryAuthCorr
        + queryWhereCatCode
        + " ac.key.headingType = ? "
        + queryDbFirstVal
        + " ac.databaseSecondValue = ? and "
        + " ac.databaseThirdValue = ct.code order by ct.sequence ",
      new Object[]{category, headingType,
        value1, value2}, new Type[]{
        Hibernate.INTEGER, Hibernate.STRING, Hibernate.INTEGER,
        Hibernate.INTEGER});
  }

  public Correlation getFirstCorrelationByType(String tag, char indicator1, char indicator2, String headingType) {
    List l = find(
      queryfromAuthCorr
        + queryWhereMarcTag
        + "(ac.key.marcFirstIndicator = ? or ac.key.marcFirstIndicator in ('S', 'O') ) and "
        + "(ac.key.marcSecondIndicator = ? or ac.key.marcSecondIndicator in ('S', 'O') ) "
        + " and ac.key.headingType = ? "
        + " order by ac.key.marcTagCategoryCode asc",
      new Object[]{
        tag, indicator1,
        indicator2, headingType},
      new Type[]{Hibernate.STRING, Hibernate.CHARACTER,
        Hibernate.CHARACTER, Hibernate.STRING});

    if (l != null && !l.isEmpty()) {
      return (Correlation) l.get(0);
    } else {
      return null;
    }
  }

  /**
   * returns a filtered list for groups of tags as 2xx,3xx....
   *
   * @param c
   * @param alphabeticOrder
   * @param rangTag
   * @return
   * @throws DataAccessException
   */
  public List getFirstCorrelationListFilter(Class c, boolean alphabeticOrder, short rangTag) {
    List listCodeTable = null;
    String rangeTagFrom = "";
    short rangeTagTo = 0;
    //Only Tag 0XX
    if (rangTag == 10)
      rangeTagFrom = "010";
    else
      rangeTagFrom = "" + rangTag;
    //Delta of 99 tag
    if (rangTag != 0)
      rangeTagTo = (short) (rangTag + 99);
    //Filters
    String filtro = " and bc.key.marcSecondIndicator <> '@' and bc.databaseFirstValue = ct.code ";
    String filterTag = " and bc.key.marcTag between '" + rangeTagFrom + "' and '" + rangeTagTo + "'";
    if (rangTag == 0)
      filterTag = "";
    String order = SEQUENCE_ORDER;
    if (alphabeticOrder)
      order = ALPHABETICAL_ORDER;
    try {
      Session s = currentSession();
      listCodeTable =
        s.find("select distinct ct from "
          + c.getName()
          + " as ct, AuthorityCorrelation as bc "
          + " where ct.obsoleteIndicator = 0 and bc.key.marcTagCategoryCode= 7"
          + filterTag
          + filtro
          + order);
    } catch (HibernateException e) {
      logAndWrap(e);
    }
    logger.debug("Got codetable for " + c.getName());
    return listCodeTable;
  }


}
