package org.folio.marccat.dao.common;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.ReferentialIntegrityException;

import java.io.IOException;
import java.sql.SQLException;

public abstract class TransactionalHibernateOperation {

  private static final Log logger = new Log(TransactionalHibernateOperation.class);
  private static final HibernateUtil util = new HibernateUtil();

  private static ThreadLocal nestingLevel = new ThreadLocal() {
    @Override
    protected synchronized Object initialValue() {
      return Integer.valueOf(0);
    }
  };

  private static ThreadLocal stateManager = new ThreadLocal() {
    @Override
    protected synchronized Object initialValue() {
      return new PersistentStateManager();
    }
  };

  public static int getNestingLevel() {
    return ((Integer) nestingLevel.get()).intValue();
  }

  public static void setNestingLevel(int i) {
    nestingLevel.set(Integer.valueOf(i));
  }

  private static PersistentStateManager getPersistentStateManager() {
    return (PersistentStateManager) stateManager.get();
  }

  public static void register(PersistenceState newPersistenceState) {

    getPersistentStateManager().register(newPersistenceState);
  }

  public void execute(final Session session) throws DataAccessException {
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      setNestingLevel(getNestingLevel() + 1);
      if (getNestingLevel() == 1) {
        getPersistentStateManager().begin();
      }
      doInHibernateTransaction(session);
      if (getNestingLevel() == 1) {
        tx.commit();
        getPersistentStateManager().commit();
        getPersistentStateManager().end();
      }
      setNestingLevel((getNestingLevel() - 1));
    } catch (HibernateException | SQLException | IOException | ReferentialIntegrityException e) {
      cleanup(tx);
      util.logAndWrap(e);
    }  catch (DataAccessException e) {
      cleanup(tx);
      throw e;
    } catch (Throwable e) {
      cleanup(tx);
      util.logAndWrap(e);
    }
  }

  public void execute() throws DataAccessException {
    Transaction tx = null;
    try {
      Session session = util.currentSession();
      tx = session.beginTransaction();
      setNestingLevel(getNestingLevel() + 1);
      if (getNestingLevel() == 1) {
        getPersistentStateManager().begin();
      }
      doInHibernateTransaction(session);
      if (getNestingLevel() == 1) {
        tx.commit();
        getPersistentStateManager().commit();
        getPersistentStateManager().end();
      }
      setNestingLevel((getNestingLevel() - 1));
    } catch (HibernateException | SQLException | IOException | ReferentialIntegrityException e ) {
      cleanup(tx);
      util.logAndWrap(e);
    } catch (DataAccessException e) {
      cleanup(tx);
      throw e;
    } catch (Throwable e) {
      cleanup(tx);
      util.logAndWrap(e);
    }
  }

  private void cleanup(Transaction tx) throws DataAccessException {
    logger.error(
      "Cleaning up after HibernateException in the middle of a Transaction");
    try {
      rollback(tx);
      getPersistentStateManager().rollback();
    } finally {
      // MIKE: only the main transaction can close the session and reset
      // the nested level
      if (getNestingLevel() > 1) {
        setNestingLevel((getNestingLevel() - 1));
      } else {
        setNestingLevel(0);
        getPersistentStateManager().end();
      }
    }
  }

  private void rollback(Transaction tx) throws DataAccessException {
    if (tx != null) {
      try {
        logger.info("trying rollback");
        tx.rollback();
        logger.info("rolled back");
      } catch (HibernateException e1) {
        util.logAndWrap(e1);
      }
    }
  }

 public abstract void doInHibernateTransaction(Session session)
    throws HibernateException, SQLException, DataAccessException, IOException;
}
