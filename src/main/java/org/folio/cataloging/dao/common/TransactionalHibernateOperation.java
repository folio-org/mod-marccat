package org.folio.cataloging.dao.common;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.business.common.ReferentialIntegrityException;

import java.io.IOException;
import java.sql.SQLException;

public abstract class TransactionalHibernateOperation {

	private static final Log logger =
		LogFactory.getLog(TransactionalHibernateOperation.class);
	private static final HibernateUtil util = new HibernateUtil();
	private static ThreadLocal nestingLevel = new ThreadLocal() {
		protected synchronized Object initialValue() {
			return new Integer(0);
		}
	};
	
	private static ThreadLocal stateManager = new ThreadLocal() {
		protected synchronized Object initialValue() {
			return new PersistentStateManager();
		}
	};
	
	public static int getNestingLevel() {
		return ((Integer)nestingLevel.get()).intValue();
	}
	
	public static void setNestingLevel(int i) {
		nestingLevel.set(new Integer(i));
	}

	private static PersistentStateManager getPersistentStateManager(){
		return (PersistentStateManager) stateManager.get();
	}
	
	public void execute() throws DataAccessException {
		Transaction tx = null;
		try {
			Session session = util.currentSession();
			tx = session.beginTransaction();
			setNestingLevel(getNestingLevel() + 1);
			if (getNestingLevel() == 1) {
			//	logger.info("reset before...");
				getPersistentStateManager().begin();
			}
			doInHibernateTransaction(session);
			if (getNestingLevel() == 1) {
				tx.commit();
				getPersistentStateManager().commit();
				getPersistentStateManager().end();
		//		logger.info("committed");
			}
			setNestingLevel((getNestingLevel() - 1));
		} catch (HibernateException e) {
			cleanup(tx);
			util.logAndWrap(e);
		} catch (SQLException e) {
			cleanup(tx);
			util.logAndWrap(e);
		} catch (IOException e) {
			cleanup(tx);
			util.logAndWrap(e);
		} catch (ReferentialIntegrityException e) {
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
		logger.warn(
			"Cleaning up after HibernateException in the middle of a Transaction");
		try {
			rollback(tx);
			getPersistentStateManager().rollback();
		} finally {
			// MIKE: only the main transaction can close the session and reset
			// the nested level
			if (getNestingLevel() > 1) {
				setNestingLevel((getNestingLevel() - 1));
			}
			else { 
				// TODO _MIKE: why the session is closed here?
				util.closeSession();
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

	abstract public void doInHibernateTransaction(Session session)
		throws HibernateException, SQLException, DataAccessException, IOException;

	public static void register(PersistenceState newPersistenceState) {
		
		getPersistentStateManager().register(newPersistenceState);
	}
}
