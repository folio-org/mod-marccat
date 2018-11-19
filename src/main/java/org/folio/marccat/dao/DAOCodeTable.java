package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.business.common.Defaults;
import org.folio.marccat.config.Global;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.MessageCatalog;
import org.folio.marccat.dao.common.TransactionalHibernateOperation;
import org.folio.marccat.dao.persistence.*;
import org.folio.marccat.exception.DataAccessException;

import java.io.Serializable;
import java.util.*;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * TODO: JAvadoc
 *
 * @author carment
 * @author cchiama
 * @since 1.0
 */

public class DAOCodeTable extends AbstractDAO {
  public static final int STEP = 10;
  private static final String ALPHABETICAL_ORDER = " order by ct.longText ";
  private static final String SEQUENCE_ORDER = " order by ct.sequence ";
  private Log logger = new Log(DAOCodeTable.class);

  public static List asOptionList(List raw, Locale locale) {
    if (raw == null) {
      return null;
    }

    List result = new ArrayList();
    Iterator iterator = raw.iterator();

    while (iterator.hasNext()) {
      CodeTable element = (CodeTable) iterator.next();
      if (element.getLanguage().equals(locale.getISO3Language())) {
        result.add(new Avp(element.getCodeString(), element.getLongText()));
      }
    }
    return result;
  }


  /**
   * Returns a code table contains elements set key/stringValue
   *
   * @param session the session of hibernate
   * @param c       the mapped class in the hibernate configuration.
   * @param locale  the Locale, used here as a filter criterion.
   * @return
   */
  public List<Avp<String>> getList(final Session session, final Class c, final Locale locale) {
    try {
      // NOTE: two steps are required because Hibernate doesn't use generics and the inference type
      // mechanism doesn't work.
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
        .map(codeTable -> (Avp<String>) new Avp(codeTable.getCodeString().trim(), codeTable.getLongText()))
        .collect(toList());
    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      return Collections.emptyList();
    }
  }

  public List getList(Class c) throws DataAccessException {
    List listCodeTable = null;
    try {
      Session s = currentSession();
      listCodeTable =
        s.find("from "
          + c.getName()
          + " as ct "
          + " order by ct.sequence ");
    } catch (HibernateException e) {
      logAndWrap(e);
    }
    logger.debug("Got codetable for " + c.getName());
    return listCodeTable;
  }

  public List getList(Class c, boolean alphabeticOrder) throws DataAccessException {
    List listCodeTable = null;
    String order = SEQUENCE_ORDER;
    if (alphabeticOrder)
      order = ALPHABETICAL_ORDER;
    try {
      Session s = currentSession();
      listCodeTable =
        s.find(
          "from "
            + c.getName()
            + " as ct "
            + " where ct.obsoleteIndicator = 0"
            + order);
    } catch (HibernateException e) {
      logAndWrap(e);
    }
    logger.debug("Got codetable for " + c.getName());
    return listCodeTable;
  }

  @Deprecated
  public List getCorrelatedList(Class c, boolean alphabeticOrder, String filtro) throws DataAccessException {
    List listCodeTable = null;
    String order = SEQUENCE_ORDER;
    if (alphabeticOrder)
      order = ALPHABETICAL_ORDER;
    try {
      Session s = currentSession();
      listCodeTable =
        s.find("select distinct ct from "
          + c.getName()
          + " as ct, BibliographicCorrelation as bc "
          + " where ct.obsoleteIndicator = 0"
          + filtro
          + order);
    } catch (HibernateException e) {
      logAndWrap(e);
    }
    logger.debug("Got codetable for " + c.getName());
    return listCodeTable;
  }

  public List getList(Class c, Locale locale) throws DataAccessException {
    List listCodeTable = null;
    try {
      Session s = currentSession();
      listCodeTable =
        s.find(
          "from "
            + c.getName()
            + " as ct "
            + " where ct.language = ?"
            + " and ct.obsoleteIndicator = '0'"
            // TODO pass Context and Session to this method (when needed)
            // TODO The return type of this method is a future, not a boolean
            + Defaults.getBoolean("labels.alphabetical.order", false),
          new Object[]{locale.getISO3Language()},
          new Type[]{Hibernate.STRING});
    } catch (HibernateException e) {
      logAndWrap(e);
    }
    logger.debug("Got codetable for " + c.getName());
    return listCodeTable;
  }

  public List getListOrderAlphab(Class c, Locale locale) throws DataAccessException {
    List listCodeTable = null;
    try {
      Session s = currentSession();
      listCodeTable =
        s.find(
          "from "
            + c.getName()
            + " as ct "
            + ALPHABETICAL_ORDER);

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    logger.debug("Got codetable for " + c.getName());
    return listCodeTable;
  }

  /**
   * * Return a list with the fields I need to fill the newCopy.jsp's checkboxes
   * * the parameter class is the name of the code table I need
   *
   * @since 1.0
   */
  public List getOptionList(Class c, Locale locale) throws DataAccessException {
    return asOptionList(getList(c, locale), locale);
  }

  public List getOptionListOrderAlphab(Class c, Locale locale) throws DataAccessException {
    return asOptionList(getListOrderAlphab(c, locale), locale);
  }

  public T_SINGLE_CHAR load(Session session, Class c, char code, Locale locale) throws DataAccessException {
    T_SINGLE_CHAR key;
    try {
      key = (T_SINGLE_CHAR) c.newInstance();
      key.setCode(code);
      key.setLanguage(locale.getISO3Language());
    } catch (Exception e) {
      throw new RuntimeException("unable to create code table object");
    }
    return (T_SINGLE_CHAR) loadCodeTableEntry(session, c, key);
  }

  public T_SINGLE load(Session session, Class c, int code, Locale locale) throws DataAccessException {
    T_SINGLE key;
    try {
      key = (T_SINGLE) c.newInstance();
      key.setCode(code);
      key.setLanguage(locale.getISO3Language());
    } catch (Exception e) {
      throw new RuntimeException("unable to create code table object");
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

  public CodeTable loadCodeTableEntry(Session session, Class c, Serializable ser) throws DataAccessException {
    return (CodeTable) get(session, c, ser);
  }

  /**
   * @param session the hibernate session
   * @param code    the input char code used here as filter criterion
   * @param c       the class of codeTable to get
   * @param locale  current locale used here as filter criterion
   * @return a string representing description of codeTable by code.
   * @throws DataAccessException
   */
  public String getLongText(final Session session, final char code, final Class c, final Locale locale) throws DataAccessException {
    return ofNullable(load(session, c, code, locale))
      .map(CodeTable::getLongText)
      .orElse(Global.EMPTY_STRING);
  }

  /**
   * @param session the hibernate session
   * @param code    the input short code used here as filter criterion
   * @param c       the class of codeTable to get
   * @param locale  current locale used here as filter criterion
   * @return a string representing description of codeTable by code.
   * @throws DataAccessException
   */
  public String getLongText(final Session session, final int code, final Class c, final Locale locale) throws DataAccessException {
    return ofNullable(load(session, c, code, locale))
      .map(CodeTable::getLongText)
      .orElse(Global.EMPTY_STRING);
  }

  /**
   * @param session the hibernate session
   * @param code    the input string code used here as filter criterion
   * @param c       the class of codeTable to get
   * @param locale  current locale used here as filter criterion
   * @return a string representing description of codeTable by code.
   * @throws DataAccessException
   */
  public String getLongText(final Session session, final String code, final Class c, final Locale locale) throws DataAccessException {
    return ofNullable(load(session, c, code, locale))
      .map(CodeTable::getLongText)
      .orElse(Global.EMPTY_STRING);
  }

  public String getTranslationString(long translationKey, Locale locale) throws DataAccessException {
    List l =
      find(
        "select t.text "
          + " from T_TRLTN as t, T_LANG as l "
          + " where t.stringNumber = ? and t.languageNumber = l.sequence / 10 "
          + " and l.code = ? ",
        new Object[]{
          translationKey,
          locale.getISO3Language()},
        new Type[]{Hibernate.LONG, Hibernate.STRING});
    if (l.size() > 0) {
      return (String) l.get(0);
    } else {
      return null;
    }
  }

  /**
   * Save a group of rows in the same code table. If it is an insertion then the
   * sequence is calculated the same for all items present in the list
   * Be carefull to do not mixed CodeTables in items list
   *
   * @param items
   * @throws DataAccessException
   */
  public void save(final List/*<CodeTable>*/ items) throws DataAccessException {
    new TransactionalHibernateOperation() {
      public void doInHibernateTransaction(Session s)
        throws DataAccessException {
        if (items == null || items.isEmpty()) {
          return;
        }
        int sequence = suggestNewSequence((CodeTable) items.get(0));
        Iterator it = items.iterator();
        while (it.hasNext()) {
          CodeTable nextCodeTable = (CodeTable) it.next();
          if (nextCodeTable.isNew()) {
            nextCodeTable.setSequence(sequence);
          }
          persistByStatus(nextCodeTable);
        }
      }
    }.execute();
  }


  public synchronized int suggestNewSequence(CodeTable codeTable) throws DataAccessException {
    try {
      Session s = currentSession();
      Query q = s.createQuery("select distinct ct.sequence from "
        + codeTable.getClass().getName()
        + " as ct order by ct.sequence desc");
      q.setMaxResults(1);
      List results = q.list();
      int newSequence = STEP; // default for empty table
      if (results.size() > 0) {
        newSequence = ((Integer) results.get(0)).intValue() + STEP;
      }
      return newSequence;
    } catch (HibernateException e) {
      logAndWrap(e);
    }
    /* pratically unreachable because logAndWrap throws an exception everytime */
    return STEP;
  }

  public synchronized int suggestNewCode(CodeTable codeTable) throws DataAccessException {
    int newCode = 0;
    try {
      Session s = currentSession();
      Query q = s.createQuery("select ct.code from "
        + codeTable.getClass().getName()
        + " as ct order by ct.code desc");
      q.setMaxResults(1);
      List results = q.list();
      // default for empty table
      if (results.size() > 0) {
        newCode = ((Short) results.get(0)).intValue() + 1;
      }

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    /* pratically unreachable because logAndWrap throws an exception everytime */
    return newCode;
  }

  public List getList(Class c, Locale locale, boolean alphabeticOrder) throws DataAccessException {
    List listCodeTable = null;
    String order = SEQUENCE_ORDER;
    if (alphabeticOrder)
      order = ALPHABETICAL_ORDER;

    try {
      Session s = currentSession();
      listCodeTable =
        s.find(
          "from "
            + c.getName()
            + " as ct "
            + " where ct.language = ?"
            /*Carmen modifica 01/02/2008 non devo visualizzare nella lista gli obsoleti*/
            + " and ct.obsoleteIndicator = 0"
            + order,
          new Object[]{locale.getISO3Language()},
          new Type[]{Hibernate.STRING});
    } catch (HibernateException e) {
      logAndWrap(e);
    }
    logger.debug("Got codetable for " + c.getName());
    return listCodeTable;
  }


  public List getCodeNoteList(int code) throws DataAccessException {
    List result = null;
    try {
      Session s = currentSession();
      Query q = s.createQuery("select distinct ct from T_CAS_STND_NTE_SUB_TYP as ct where ct.sequence =" + code);
      q.setMaxResults(1);
      result = q.list();

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }


  @Deprecated
  public List<Diacritics> getDiacritics() throws DataAccessException {
    return null;
  }


  public T_SINGLE_LONGCHAR load(final Session session, final Class c, final String code, final Locale locale) throws DataAccessException {
    T_SINGLE_LONGCHAR key;
    try {
      key = (T_SINGLE_LONGCHAR) c.newInstance();
      key.setCode(code);
      key.setLanguage(locale.getISO3Language());
    } catch (Exception e) {
      throw new RuntimeException("unable to create code table object");
    }
    return (T_SINGLE_LONGCHAR) loadCodeTableEntry(session, c, key);
  }

}
