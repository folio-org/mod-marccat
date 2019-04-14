package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.dao.persistence.*;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.ModMarccatException;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * @author carment
 * @author cchiama
 * @since 1.0
 */

public class DAOCodeTable extends AbstractDAO {
  private Log logger = new Log(DAOCodeTable.class);
  private String noSessionMessege = "don't use it: session missing";
  private String unableCreateMessage = "unable to create code table object";

  /**
   * Returns a code table contains elements set key/stringValue
   *
   * @param session the session of hibernate
   * @param c       the mapped class in the hibernate configuration.
   * @param locale  the Locale, used here as a filter criterion.
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Avp<String>> getList(final Session session, final Class c, final Locale locale) {
    try {
      final List<CodeTable> codeTables = session.find(
        "from "
          + c.getName()
          + " as ct "
          + " where ct.language = ?"
          + " and ct.obsoleteIndicator = '0'"
          + " order by ct.code ",
        new Object[]{locale.getISO3Language()},
        new Type[]{Hibernate.STRING});

      return codeTables
        .stream()
        .map(codeTable -> (Avp<String>) new Avp(codeTable.getCodeString(), codeTable.getLongText()))
        .collect(toList());
    } catch (final HibernateException exception) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
      return Collections.emptyList();
    }
  }

  /**
   * * Return a list with the fields I need to fill the newCopy.jsp's checkboxes
   * * the parameter class is the name of the code table I need
   *
   * @since 1.0
   */
  public List getOptionList(Class c, Locale locale) {
    throw new IllegalArgumentException(noSessionMessege);
  }

  public List getOptionListOrderAlphab(Class c, Locale locale) {
    throw new IllegalArgumentException(noSessionMessege);
  }

  public T_SINGLE_CHAR load(Session session, Class c, char code, Locale locale) {
    T_SINGLE_CHAR key;
    try {
      key = (T_SINGLE_CHAR) c.newInstance();
      key.setCode(code);
      key.setLanguage(locale.getISO3Language());
    } catch (Exception e) {
      throw new ModMarccatException(unableCreateMessage);
    }
    return (T_SINGLE_CHAR) loadCodeTableEntry(session, c, key);
  }

  public T_SINGLE load(Session session, Class c, int code, Locale locale) {
    T_SINGLE key;
    try {
      key = (T_SINGLE) c.newInstance();
      key.setCode(code);
      key.setLanguage(locale.getISO3Language());
    } catch (Exception e) {
      throw new ModMarccatException(unableCreateMessage);
    }
    return (T_SINGLE) loadCodeTableEntry(session, c, key);
  }

  /**
   * @param session hibernate session
   * @param c       class bind to db table
   * @param ser     oject to load
   * @return
   * @throws DataAccessException
   */

  public CodeTable loadCodeTableEntry(Session session, Class c, Serializable ser) {
    return (CodeTable) get(session, c, ser);
  }

  /**
   * @param session the hibernate session
   * @param code    the input short code used here as filter criterion
   * @param c       the class of codeTable to get
   * @param locale  current locale used here as filter criterion
   * @return a string representing description of codeTable by code.
   * @throws DataAccessException
   */
  public String getLongText(final Session session, final int code, final Class c, final Locale locale) {
    return ofNullable(load(session, c, code, locale))
      .map(CodeTable::getLongText)
      .orElse(Global.EMPTY_STRING);
  }

  /**
   * @param translationKey
   * @param locale
   * @return
   * @throws DataAccessException
   */
  public String getTranslationString(long translationKey, Locale locale) {
    throw new IllegalArgumentException(noSessionMessege);
  }

  /**
   * @param session
   * @param c
   * @param code
   * @param locale
   * @return
   * @throws DataAccessException
   */
  public T_SINGLE_LONGCHAR load(final Session session, final Class c, final String code, final Locale locale) {
    T_SINGLE_LONGCHAR key;
    try {
      key = (T_SINGLE_LONGCHAR) c.newInstance();
      key.setCode(code);
      key.setLanguage(locale.getISO3Language());
    } catch (Exception e) {
      throw new ModMarccatException(unableCreateMessage);
    }
    return (T_SINGLE_LONGCHAR) loadCodeTableEntry(session, c, key);
  }

  /**
   * Gets statistics number for loading from file.
   *
   * @param session                 -- the hibernate session associated to request.
   * @param loadingStatisticsNumber -- the loading statistic number key.
   * @return LDG_STATS.
   * @throws DataAccessException in case of data access exception.
   */
  public LDG_STATS getStats(final Session session, final int loadingStatisticsNumber) {
    try {
      return (LDG_STATS) session.load(LDG_STATS.class, loadingStatisticsNumber);
    } catch (HibernateException e) {
      throw new DataAccessException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public List<LOADING_MARC_RECORDS> getResults(final Session session, final int loadingStatisticsNumber) {

    try {
      return session.find(
        "from LOADING_MARC_RECORDS as r "
          + " where r.loadingStatisticsNumber = ? "
          + " order by r.sequence ",
        new Object[]{loadingStatisticsNumber},
        new Type[]{Hibernate.INTEGER});
    } catch (HibernateException e) {
      throw new DataAccessException(e);
    }

  }
}
