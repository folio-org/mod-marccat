package org.folio.cataloging.dao.common;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.LockMode;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.cataloguing.authority.AuthorityNote;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistentObjectWithView;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.dao.persistence.S_LCK_TBL;
import org.folio.cataloging.exception.RecordInUseException;
import org.folio.cataloging.log.MessageCatalog;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.folio.cataloging.F.deepCopy;

/**
 * Provides a base class of support utilities for DAO objects
 * 
 * @author wimc
 */
@Deprecated
public class HibernateUtil {

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

	/*
	 * * Ensures that a client DB session exists and returns the Session ID.
	 * Used for unique identifications of search clients in the search engine.
	 * @return
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

	@Deprecated
	public Session currentSession() throws DataAccessException {
		throw new IllegalArgumentException("Don't call me!");

	}

	@Deprecated
	public void closeSession() {
		throw new IllegalArgumentException("Don't call me!");
	}

	@Deprecated
	public void logAndWrap(Throwable e) {
		throw new IllegalArgumentException("Don't call me!");
	}

	/**
	 * Creates a new usr_vw_ind string from the input string by setting the
	 * position specified in arg2 to '0'. The resultant view string is useful in
	 * saving a persistant object after the current cataloguing view of the
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

	/**
	 * Creates a new usr_vw_ind string from the input string by setting the
	 * position specified in arg2 to '1'. The resultant view string is useful in
	 * saving a persistant object after the current cataloguing view of the
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
	 * Creates a new usr_vw_ind string by setting all positions to '0' except
	 * the position specified in arg1. The resultant view string is useful in
	 * saving a persistant object after the current cataloguing view of the
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
	 * Invokes hibernate methods save, update depending on the objects
	 * updateStatus. If the status is "deleted" then the corresponding
	 * DAO.delete() method is invoked.
	 */
	@Deprecated
	public void persistByStatus(Persistence po) throws DataAccessException {
		throw new IllegalArgumentException("DON'T CALL ME!!!");
	}

	private AuthorityNote getNewInstanceOfAuthorityNote(AuthorityNote note) {
		AuthorityNote newNote = new AuthorityNote();

		newNote.setCorrelationValues(note.getCorrelationValues());
		// newNote.setItemEntity(note.getItemEntity());
		newNote.setItemNumber(note.getItemNumber());
		newNote.setNewSubfieldContent(note.getNewSubfieldContent());
		newNote.setNoteNumber(note.getNoteNumber());
		newNote.setNoteStringText(note.getNoteStringText());
		newNote.setNoteType(note.getNoteType());
		newNote.setTagImpl(note.getTagImpl());
		newNote.setUpdateStatus(note.getUpdateStatus());

		return newNote;
	}


	/**
	 * performs isolateView on a List
	 */
	@Deprecated
	public List isolateViewForList(List multiView, int userView) throws DataAccessException {
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
	 * 
	 */
	public PersistentObjectWithView isolateView(
			final PersistentObjectWithView p, final int userView)
			throws DataAccessException {
		if (userView < View.AUTHORITY) {
			logger.error("NO ISOLATION FOR MADES");
			return p;
		}
		final String myView = makeSingleViewString(userView);
		final PersistentObjectWithView p3 = (PersistentObjectWithView) deepCopy(p);

		if (p.getUserViewString().compareTo(myView) != 0) {
			new TransactionalHibernateOperation() {
				public void doInHibernateTransaction(Session s)
						throws HibernateException {
					s.delete(p);
					PersistentObjectWithView p2 = (PersistentObjectWithView) deepCopy(p);
					p2.setUserViewString(maskOutViewString(p.getUserViewString(), userView));
					s.save(p2);
					p3.setUserViewString(myView);
					s.save(p3);
				}
			}.execute();
			return p3;
		} else {
			return p;
		}
	}

	/**
	 * Convenience method for currentSession().load(Class clazz, Serializable
	 * id). If the load method of the Hibernate Session throws a
	 * HibernateException, it wraps it in a DataAccessException
	 * 
	 * @param clazz
	 *            a persistent class
	 * @param id
	 *            a valid identifier of an existing persistent instance of the
	 *            class
	 * @return the persistent instance
	 * @throws DataAccessException
	 */
	public Object load(Class clazz, Serializable id) throws DataAccessException {
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
	 *
	 * @param clazz a persistent classaps it in a DataAccessException
	 *
	 * @param id  a valid identifier of an existing persistent instance of the class
	 *
	 * @return the persistent instance or null
	 */
	public Object get(Session session, Class clazz, Serializable id) {
		try	{
			return session.get(clazz, id);
		}catch (Exception exception) {
			logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
			return null;
		}
	}

	/**
	 * Convenience method for currentSession().get(Class clazz, Serializable id,
	 * LockMode l) If the get method of the Hibernate Session throws a
	 * HibernateException, it wraps it in a DataAccessException
	 *
	 * @param clazz
	 *            a persistent class
	 * @param id
	 *            a valid identifier of an existing persistent instance of the
	 *            class
	 * @return the persistent instance or null
	 * @throws DataAccessException
	 */
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
	 * @param query
	 *            the query string
	 * @param values
	 *            an array of values to be bound to the "?" placeholders (JDBC
	 *            IN parameters).
	 * @param types
	 *            an array of Hibernate types of the values
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
			return null;
		}
	}


	/**
	 * Convenience method for currentSession().find(String query) If the find
	 * method of the Hibernate Session throws a HibernateException, it wraps it
	 * in a DataAccessException
	 * 
	 * @param query
	 *            the query string
	 * @return a distinct list of instances
	 * @throws DataAccessException
	 */
	public List find(Session session, String query) throws DataAccessException {
		try {
			return session.find(query);
		} catch (HibernateException e) {
			logAndWrap(e);
			return null;
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

	public void lock(int key, String entityType, String userName)
			throws DataAccessException, RecordInUseException {
		ResultSet rs = null;
		Statement statement = null;

		//TODO refactoring this
		Session session = currentSession();

		try {
			if (getLockingSession() == null || getLockingSession().isClosed()) {
				setLockingSession(createNewDBSession());

				String sessionID = null;
				statement = getLockingSession().createStatement();
				rs = statement
						.executeQuery("SELECT audsid from v$session where audsid = userenv('sessionid') ");

				if (rs.next())
					sessionID = rs.getString(1);

				setLockingSessionId(sessionID);

				// setLockingSessionId(getSessionID(getLockingSession()));
			}
			S_LCK_TBL myLock = new S_LCK_TBL(key, entityType);
			S_LCK_TBL existingLock = (S_LCK_TBL) get(session, S_LCK_TBL.class, myLock);

			if (existingLock != null) {
				if (isSessionAlive(existingLock.getDbSession())
						&& /* !isUserPresent(userName) */!existingLock
								.getUserName().equals(userName)) {
					throw new RecordInUseException();
				} else if (existingLock.getDbSession() != null
						&& getLockingSessionId() != null
						&& existingLock.getDbSession().trim().equals(
								getLockingSessionId().trim())) {
					// same lock already exists so return
					return;
					// test se non presente anche lo stesso USER caso di
					// chiusura della sessione
				} else {
					// remove lock from dead session
					existingLock.markDeleted();
					persistByStatus(existingLock);
				}
			}
			myLock.setDbSession(getLockingSessionId());
			myLock.setUserName(userName);
			// 16/06/2001 aggiunto da Carmen
			myLock.markNew();
			persistByStatus(myLock);

		} catch (SQLException e) {
			throw new DataAccessException();

		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if (statement != null)
				try {
					statement.close();
				} catch (SQLException e) {
				}
		}
	}

	/*
	*
	*/
	public void unlock(int key, String entityType) throws DataAccessException {
		S_LCK_TBL myLock = new S_LCK_TBL(key, entityType);

		//TODO session
		final Session session = currentSession();

		S_LCK_TBL existingLock = (S_LCK_TBL) get(session, S_LCK_TBL.class, myLock);
		if (existingLock != null) {
			existingLock.markDeleted();
			persistByStatus(existingLock);
		}
	}

	@Deprecated
	private boolean isSessionAlive(String sessionId) {
		throw new IllegalArgumentException("Don't call me!");
	}

	@Deprecated
	private boolean isUserPresent(String user) {
		throw new IllegalArgumentException("Don't call me!");
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

	protected String getHibernateTableName(Class c) {
		return HibernateSessionProvider.getHibernateTableName(c);
	}

	protected String getHibernateColumnName(Class c, String property)  {
			return HibernateSessionProvider.getHibernateColumnName(c, property);
	}
}