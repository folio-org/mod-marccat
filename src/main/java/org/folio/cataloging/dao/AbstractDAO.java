package org.folio.cataloging.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.LockMode;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistentObjectWithView;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.persistence.S_LCK_TBL;
import org.folio.cataloging.exception.RecordInUseException;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import static org.folio.cataloging.F.deepCopy;

//TODO remove extends from HibernateUtil
public abstract class AbstractDAO extends HibernateUtil {

    public Transaction getTransaction(final Session session) throws HibernateException{
        return session.beginTransaction();
    }

    /**
     * Invokes hibernate methods save, update depending on the objects updateStatus.
     * If the status is "deleted" then the corresponding DAO.delete() method is invoked.
     *
     * @param persistentObject the persistent object.
     * @param session the current hibernate session.
     * @throws HibernateException in case of hibernate exception.
     */
    public void persistByStatus(final Persistence persistentObject, final Session session) throws HibernateException {
        if (persistentObject.isNew()) {
            save(persistentObject, session);
        } else if (persistentObject.isChanged()) {
            update(persistentObject, session);
        } else if (persistentObject.isDeleted()) {
            delete(persistentObject, session);
        }
    }

    /**
     * Deletes persistent object using hibernate transaction.
     *
     * @param persistentObject the persistent object.
     * @param session the current hibernate session.
     * @throws HibernateException in case of hibernate exception.
     */
    public void delete(final Persistence persistentObject, final Session session) throws HibernateException {
        Transaction tx = null;
        try {
            tx = getTransaction(session);
            session.delete(persistentObject);
            tx.commit();
        } catch (HibernateException exception) {
            cleanUp(tx);
            throw new HibernateException(exception);
        }
    }

    /**
     * Saves persistent object using hibernate transaction.
     *
     * @param persistentObject the persistent object.
     * @param session the current hibernate session.
     * @throws HibernateException in case of hibernate exception.
     */
    public void save(final Persistence persistentObject, final Session session) throws HibernateException {

        Transaction tx = null;
        try {
            tx = getTransaction(session);
            session.save(persistentObject);
            tx.commit();
        } catch (HibernateException exception) {
            cleanUp(tx);
            throw new HibernateException(exception);
        }
    }

    /**
     * Updates persistent object using hibernate transaction.
     *
     * @param persistentObject the persistent object.
     * @param session the current hibernate session.
     * @throws HibernateException in case of hibernate exception.
     */
    public void update(final Persistence persistentObject, final Session session) throws HibernateException {
        Transaction tx = null;
        try {
            tx = getTransaction(session);
            session.update(persistentObject);
            tx.commit();
        } catch (HibernateException exception) {
            cleanUp(tx);
            throw new HibernateException(exception);
        }
    }

    /**
     * Execute rollback transaction in case of exception.
     *
     * @param transaction the current hibernate transaction.
     */
    public void cleanUp(final Transaction transaction) {
        try {
            transaction.rollback();
        } catch (HibernateException ignore) { }
    }

    public List<? extends PersistentObjectWithView> isolateViewForList(List<? extends PersistentObjectWithView> multiView, final int userView, final Session session) throws DataAccessException {
        if (userView == View.ANY) {
            return multiView;
        }
      return multiView.stream().map(po -> {
            try {
                return isolateView(po, userView, session);
            } catch (HibernateException he) {
                throw new RuntimeException(he);
            }
        }).collect(Collectors.toList());
    }

    /**
     * Ensures that the returned PersistentObject is a "single" view row version of the passed argument.
     *
     * @param p -- the persistent object to ensure.
     * @param userView -- the user view associated.
     * @param session -- current hibernate session.
     * @return
     * @throws HibernateException in case of hibernate exception.
     */
    public PersistentObjectWithView isolateView(final PersistentObjectWithView p, final int userView, final Session session) throws HibernateException {
        final String myView = makeSingleViewString(userView);

        if (p.getUserViewString().compareTo(myView) != 0) {
            final PersistentObjectWithView pObjectOriginalView = (PersistentObjectWithView) deepCopy(p);
            final PersistentObjectWithView pObjectWithMyView = (PersistentObjectWithView) deepCopy(p);

            final Transaction transaction = getTransaction(session);

            try {
                session.delete(p);

                pObjectOriginalView.setUserViewString(maskOutViewString(p.getUserViewString(), userView));
                session.save(pObjectOriginalView);

                pObjectWithMyView.setUserViewString(myView);
                session.save(pObjectWithMyView);
                transaction.commit();

                return pObjectWithMyView;

            } catch (HibernateException exception) {
                cleanUp(transaction);
                throw new HibernateException(exception);
            }

        } else {
            return p;
        }
    }

     /**
     * Convenience method for session.find.
     *
     * @param query
     *            the query string
     * @param values
     *            an array of values to be bound to the "?" placeholders (JDBC
     *            IN parameters).
     * @param types
     *            an array of Hibernate types of the values
     *
     * @return a distinct list of instances
     */
    public List find(final Session session, final String query, final Object[] values, final Type[] types) {
        try {
            return session.find(query, values, types);
        } catch (HibernateException e) {
            return null;
        }
    }

    /**
     * Convenience method for session.get
     *
     * @param clazz
     *            a persistent class
     * @param id
     *            a valid identifier of an existing persistent instance of the class
     *
     * @return the persistent instance or null.
     */
    public Object get(final Session session, final Class clazz, final Serializable id, final LockMode l) {
        try {
            return session.get(clazz, id, l);
        } catch (HibernateException e) {
            return null;
        }
    }

  /**
   * Lock record or heading for username and uuid.
   *
   * @param key -- the item key number.
   * @param entityType -- the type of entity.
   * @param userName -- the user name.
   * @param uuid -- the generated front-end uuid for locking.
   * @param session -- the current hibernate session id.
   * @throws DataAccessException in case of hibernate exception.
   * @throws RecordInUseException in case of record in use exception.
   */
    public void lock(final int key, final String entityType, final String userName, final String uuid, final Session session) throws DataAccessException, RecordInUseException {
      try {

        S_LCK_TBL myLock = new S_LCK_TBL(key, entityType);
        S_LCK_TBL existingLock = (S_LCK_TBL) get(session, S_LCK_TBL.class, myLock);

        if (existingLock != null) {
          if (!existingLock.getDbSession().trim().equals(uuid.trim()) && !existingLock.getUserName().trim().equals(userName.trim()))
            throw new RecordInUseException();
          else if (existingLock.getDbSession().trim().equals(uuid.trim()) && existingLock.getUserName().trim().equals(userName.trim()))
            return;
          else if (!existingLock.getDbSession().trim().equals(uuid.trim()) && existingLock.getUserName().trim().equals(userName.trim())) { //sessione appesa/vecchia
              existingLock.markDeleted();
              persistByStatus(existingLock, session);
          }
        }

        myLock.setDbSession(uuid);
        myLock.setUserName(userName);
        myLock.markNew();
        persistByStatus(myLock, session);

      } catch (HibernateException e) {
        throw new DataAccessException(e);
      }
    }

  /**
   * Unlock record or heading.
   *
   * @param key -- the item key number.
   * @param entityType -- the type of entity.
   * @param userName -- the user name.
   * @param session -- the current hibernate session id.
   * @throws DataAccessException in case of hibernate exception.
   */
    public void unlock(final int key, final String entityType, final String userName, final Session session) throws DataAccessException {
      try {
        S_LCK_TBL myLock = new S_LCK_TBL(key, entityType);
        S_LCK_TBL existingLock = (S_LCK_TBL) get(session, S_LCK_TBL.class, myLock);

        if (existingLock != null && existingLock.getUserName().trim().equals(userName.trim())) {
          existingLock.markDeleted();
          persistByStatus(existingLock, session);
        }
      } catch (HibernateException e) {
        throw new DataAccessException(e);
      }
    }

  /**
   * Creates a new usr_vw_ind string by setting all positions to '0' except
   * the position specified in arg1. The resultant view string is useful in
   * saving a persistent object after the current cataloguing view of the
   * record is saved or updated;
   *
   *
   * @param cataloguingView --
   *            the position to be set to '1' (1 indexing)
   */

  public static String makeSingleViewString(int cataloguingView) {
    return View.makeSingleViewString(cataloguingView);
  }

  /**
   * Creates a new usr_vw_ind string from the input string by setting the
   * position specified in arg2 to '1'. The resultant view string is useful in
   * saving a persistent object after the current cataloguing view of the
   * record is added (based on a copy from existing views);
   *
   *
   * @param viewString --
   *            the original view String
   * @param cataloguingView --
   *            the position to be set to '1' (1 indexing)
   */

  public String maskOnViewString(String viewString, int cataloguingView) {
    return View.maskOnViewString(viewString, cataloguingView);
  }

  /**
   * Creates a new usr_vw_ind string from the input string by setting the
   * position specified in arg2 to '0'. The resultant view string is useful in
   * saving a persistent object after the current cataloguing view of the
   * record is deleted (or modified)
   *
   *
   * @param viewString --
   *            the original view String
   * @param cataloguingView --
   *            the position to be set to '0' (1 indexing)
   */
  public String maskOutViewString(String viewString, int cataloguingView) {
    return View.maskOutViewString(viewString, cataloguingView);
  }
}
