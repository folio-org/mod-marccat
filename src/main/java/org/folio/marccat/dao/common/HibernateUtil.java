package org.folio.marccat.dao.common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.business.common.View;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.exception.DataAccessException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.LockMode;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

/**
 * Provides a base class of support utilities for DAO objects
 */
@Deprecated
public class HibernateUtil {

  private static String dontCall = "Don't call me!";
  private static Log logger = LogFactory.getLog(HibernateUtil.class);
  private static ThreadLocal/* <Connection> */lockingSession = new ThreadLocal/* <Connection> */();
  private static ThreadLocal/* <String> */lockingSessionId = new ThreadLocal/* <String> */();

  private static Connection getLockingSession() {
    return (Connection) lockingSession.get();
  }

  private static void setLockingSession(Connection con) {
    lockingSession.set(con);
  }

  private static String getLockingSessionId() {
    return (String) lockingSessionId.get();
  }

  private static void setLockingSessionId(String s) {
    lockingSessionId.set(s);
  }

  /**
   * Ensures that a client DB session exists and returns the Session ID.
   * Used for unique identifications of search clients in the search engine.
   *
   * @return unique session id
   */
  @Deprecated
  public String getUniqueSessionId() {
    try {
      if (getLockingSession() == null) {
        setLockingSession(createNewDBSession());
        setLockingSessionId(getSessionID(getLockingSession()));
      }
      return getLockingSessionId();
    } catch (Exception e) {
      return null;
    }
  }

  public Session currentSession() {
    throw new IllegalArgumentException(dontCall);
  }

  @Deprecated
  public void closeSession() {
    throw new IllegalArgumentException(dontCall);
  }

  public void logAndWrap(Throwable e) {
    throw new IllegalArgumentException(dontCall);
  }


  /**
   * Invokes hibernate methods save, update depending on the objects
   * updateStatus. If the status is "deleted" then the corresponding
   * DAO.delete() method is invoked.
   */
  @Deprecated
  public void persistByStatus(Persistence po) {
    throw new IllegalArgumentException(dontCall);
  }

  /**
   * performs isolateView on a List
   */
  @Deprecated
  public List isolateViewForList(List multiView, int userView) {
    if (userView == View.ANY) {
      return multiView;
    }
    List singleView = new ArrayList();
    Iterator iter = multiView.iterator();
    while (iter.hasNext()) {
      singleView.add(isolateView((PersistentObjectWithView) iter.next(),
        userView));
    }
    return singleView;
  }

  /**
   * Ensures that the returned PersistentObject is a "single" view row version
   * of the passed argument
   */
  @Deprecated
  public PersistentObjectWithView isolateView(final PersistentObjectWithView p, final int userView) {
    return null;
  }

  /**
   * Convenience method for currentSession().load(Class clazz, Serializable
   * id). If the load method of the Hibernate Session throws a
   * HibernateException, it wraps it in a DataAccessException
   *
   * @param clazz a persistent class
   * @param id    a valid identifier of an existing persistent instance of the
   *              class
   * @return the persistent instance
   * @throws DataAccessException
   */
  public Object load(Class clazz, Serializable id) {
    try {
      return currentSession().load(clazz, id);
    } catch (HibernateException e) {
      logAndWrap(e);
      return null;
    }
  }

  /**
   * Convenience method for session.get(Class clazz, Serializable id)
   *
   * @param session the current session
   * @param clazz   a persistent classaps it in a DataAccessException
   * @param id      a valid identifier of an existing persistent instance of the class
   * @return the persistent instance or null
   */
  public Object get(Session session, Class clazz, Serializable id) {
    try {
      return session.get(clazz, id);
    } catch (Exception exception) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
      return null;
    }
  }

  /**
   * Convenience method for currentSession().get(Class clazz, Serializable id,
   * LockMode l) If the get method of the Hibernate Session throws a
   * HibernateException, it wraps it in a DataAccessException
   *
   * @param clazz a persistent class
   * @param id    a valid identifier of an existing persistent instance of the
   *              class
   * @return the persistent instance or null
   * @throws DataAccessException
   */
  @Deprecated
  public Object get(Class clazz, Serializable id, LockMode l)
    throws DataAccessException {
    try {
      return currentSession().get(clazz, id, l);
    } catch (HibernateException e) {
      logAndWrap(e);
      return null;
    }
  }

  /**
   * Convenience method for currentSession().find(String query, Object[]
   * values, Type[] types) If the find method of the Hibernate Session throws
   * a HibernateException, it wraps it in a DataAccessException
   *
   * @param query  the query string
   * @param values an array of values to be bound to the "?" placeholders (JDBC
   *               IN parameters).
   * @param types  an array of Hibernate types of the values
   * @return a distinct list of instances
   * @throws DataAccessException
   */
  @Deprecated
  public List find(String query, Object[] values, Type[] types)
    throws DataAccessException {
    try {
      return currentSession().find(query, values, types);
    } catch (HibernateException e) {
      logAndWrap(e);
      return Collections.emptyList();
    }
  }


  /**
   * Convenience method for currentSession().find(String query) If the find
   * method of the Hibernate Session throws a HibernateException, it wraps it
   * in a DataAccessException
   *
   * @param query the query string
   * @return a distinct list of instances
   * @throws DataAccessException
   */
  public List find(Session session, String query) throws DataAccessException {
    try {
      return session.find(query);
    } catch (HibernateException e) {
      logAndWrap(e);
      return Collections.emptyList();  
      }
  }

  /**
   * Default implementation for save (with no extra requirements)
   *
   * @since 1.0
   */
  public void save(final Persistence p) throws DataAccessException {
    new TransactionalHibernateOperation() {
      public void doInHibernateTransaction(Session s)
        throws HibernateException {
        s.save(p);
      }
    }.execute();
  }

  /**
   * Default implementation for update (with no extra requirements)
   *
   * @since 1.0
   */
  public void update(final Persistence p) throws DataAccessException {
    new TransactionalHibernateOperation() {
      public void doInHibernateTransaction(Session s)
        throws HibernateException {
        s.update(p);
      }
    }.execute();
  }

  /**
   * Default implementation for delete with no cascade affects
   *
   * @since 1.0
   */
  public void delete(final Persistence p) throws DataAccessException {
    new TransactionalHibernateOperation() {
      public void doInHibernateTransaction(Session s)
        throws HibernateException {
        s.delete(p);
      }
    }.execute();
  }

  @Deprecated
  public void lock(int key, String entityType, String userName) {
  }

  /*
   *
   */
  @Deprecated
  public void unlock(int key, String entityType) {
  }


  @Deprecated
  private Connection createNewDBSession() throws SQLException {
    throw new IllegalArgumentException("Don't call me!");
  }

  private String getSessionID(Connection con) throws SQLException {
    ResultSet rs = con
      .createStatement()
      .executeQuery(
        "SELECT audsid from v$session where audsid = userenv('sessionid') ");
    if (rs.next()) {
      return rs.getString(1);
    } else {
      return null;
    }
  }

}
