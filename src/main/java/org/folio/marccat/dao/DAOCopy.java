package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.common.SortFormException;
import org.folio.marccat.business.descriptor.SortFormParameters;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.dao.common.TransactionalHibernateOperation;
import org.folio.marccat.dao.persistence.CPY_ID;
import org.folio.marccat.dao.persistence.SHLF_LIST;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.RecordNotFoundException;

import java.sql.*;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class DAOCopy extends AbstractDAO {


	private static Log logger = new Log(DAOCopy.class);
	private String queryBibItemNumber = " and c.bibItemNumber = ?";
	private String querySelect = "select count(*) from CPY_ID as c where c.shelfListKeyNumber = ?";

	/**
	 * Loads copy by copy number.
	 *
	 * @param session    -- the hibernate session associated to request.
	 * @param copyNumber -- the copy number.
	 * @return {@link CPY_ID}
	 * @throws DataAccessException in case of hibernate exception.
	 */
	public CPY_ID load(final Session session, final int copyNumber){
		CPY_ID c = null;
		try {
			c = (CPY_ID) session.get(CPY_ID.class, copyNumber);
			if (c != null && c.getShelfListKeyNumber() != null) {
				c.setShelfList(new ShelfListDAO().load(c.getShelfListKeyNumber(), session));
			}
		} catch (HibernateException e) {
			throw new DataAccessException(e);
		}
		return c;
	}


	/**
	 * @param copyBarCode is the barCode of the copy
	 * @return the BibItemNumber from CPY_ID table
	 * @since 1.0
	 */

	/**
	 * Gets the amicus number associated to copy.
	 *
	 * @param session -- the hibernate session associated to request.
	 * @param barCode -- the barcode associated to copy.
	 * @return the amicus number.
	 * @throws DataAccessException in case of hibernate exception.
	 */
	public int getBibItemNumber(final Session session, final String barCode) {

		try {
			List<CPY_ID> listAllCopies = session.find("from CPY_ID ci where ci.barCodeNumber = '" + barCode + "'");
			return listAllCopies.stream().filter(Objects::nonNull).reduce((first, second) -> second).get().getBibItemNumber();

		} catch (HibernateException e) {
			throw new DataAccessException(e);
			//log error and return 0?
		}

	}

	/**
	 * @param copyIdNumber is the copyIdNumber of the copy
	 * @return the BibItemNumber from CPY_ID table
	 * @since 1.0
	 */

	public int getBibItemNumber(int copyIdNumber) {
		int result = 0;

		List listAllCopies = null;
		try {
			Session s = currentSession();
			listAllCopies = s.find("from CPY_ID ci where ci.copyIdNumber = "
					+ copyIdNumber);

			Iterator iter = listAllCopies.iterator();
			while (iter.hasNext()) {
				CPY_ID rawCopy = (CPY_ID) iter.next();
				result = rawCopy.getBibItemNumber();
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}

		return result;
	}


	public void delete(final int copyNumber, final String userName) {
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
					throws HibernateException {
				// TODO make sure no circulation records (AMICUS doesn't)
				CPY_ID copy = (CPY_ID) s.get(CPY_ID.class, (
						copyNumber));
				if (copy.getShelfListKeyNumber() != null) {
					//TODO passare la session
					copy.setShelfList(new ShelfListDAO().load(copy.getShelfListKeyNumber().intValue(), null));
				}
				else if (copy == null) {
					throw new RecordNotFoundException();
				}

				// detach the shelflist
				detachShelfList(copy, copy.getShelfList());

				saveCpyIdAgent(userName, copy.getCopyIdNumber());

				// delete the copy itself
				s.delete(copy);


				DAOSummaryHolding ds = new DAOSummaryHolding();
				ds.deleteRecord(copy.getBibItemNumber(), copy
						.getOrganisationNumber());


			}
		}.execute();
	}

	public void detachShelfList(CPY_ID copy, SHLF_LIST shelf) {
		Session s = currentSession();

		if (shelf == null) {
			return;
		}
		try {
			/*
			 * If only our copies bib_item is using this shelf list then remove
			 * the entry from SHLF_LIST_ACS_PNT
			 */

			if ( (countShelfFromCopyUses(copy, shelf) != 0) && (countShelfListAccessPointUses(copy, shelf) == 1) ) {
				logger.info("Cancella  SHLF_LIST_ACS_PNT");

				s
				.delete(
						"from SHLF_LIST_ACS_PNT as c where c.shelfListKeyNumber = ?"
								+ queryBibItemNumber,
								new Object[]{
										(shelf
												.getShelfListKeyNumber()),
										(copy.getBibItemNumber())},
								new Type[]{Hibernate.INTEGER,
										Hibernate.INTEGER});
				/*
				 * AND if only our copy is using this shelf list number then
				 * delete the shelf list number
				 */
				List l = find(
						querySelect,
						new Object[]{(shelf
								.getShelfListKeyNumber())},
						new Type[]{Hibernate.INTEGER});
				if (!l.isEmpty() && ((Integer) l.get(0)).intValue() == 1) {
					s.delete(shelf);
				}
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}
	}

	/**
	 * Counts the number of copies using this shelf list and bib_itm
	 *
	 * @since 1.0
	 */
	public int countShelfListAccessPointUses(CPY_ID copy, SHLF_LIST shelf)
	{
		List l = find(
				querySelect
						+ queryBibItemNumber, new Object[]{
								(shelf.getShelfListKeyNumber()),
								(copy.getBibItemNumber())}, new Type[]{
										Hibernate.INTEGER, Hibernate.INTEGER});
		if (!l.isEmpty()) {
			return ((Integer) l.get(0)).intValue();
		} else {
			return 0;
		}
	}

	/**
	 * Counts the number of copies using this shelf list and bib_itm
	 *
	 * @since 1.0
	 */
	public int countShelfFromCopyUses(CPY_ID copy, SHLF_LIST shelf)
	{
		List l = find(
				querySelect
						+ queryBibItemNumber
						+ " and c.copyIdNumber = ?", new Object[]{
								(shelf.getShelfListKeyNumber()),
								(copy.getBibItemNumber()),
								(copy.getCopyIdNumber())},
						new Type[]{Hibernate.INTEGER, Hibernate.INTEGER,
								Hibernate.INTEGER});
		if (!l.isEmpty()) {
			return ((Integer) l.get(0)).intValue();
		} else {
			return 0;
		}
	}


	public String calculateSortForm(String text, SortFormParameters parms)
	{
		String result = "";
		int bufSize = 600;
		int rc;

		Session s = currentSession();
		CallableStatement proc = null;
		Connection connection = null;

		try {
			connection = s.connection();
			proc = connection
					.prepareCall("{ ? = call AMICUS.PACK_SORTFORM.SF_PREPROCESS(?, ?, ?, ?, ?, ?, ?, ?) }");
			proc.registerOutParameter(1, Types.INTEGER);
			proc.setString(2, text);
			proc.registerOutParameter(3, Types.VARCHAR);
			proc.setInt(4, bufSize);
			proc.setInt(5, parms.getSortFormMainType());
			proc.setInt(6, parms.getSortFormSubType());
			proc.setInt(7, parms.getNameTitleOrSubjectType());
			proc.setInt(8, parms.getNameSubtype());
			proc.setInt(9, parms.getSkipInFiling());
			proc.execute();

			rc = proc.getInt(1);

			if (rc != 0) {
				throw new SortFormException(String.valueOf(rc));
			}
			result = proc.getString(3);

			proc.close();

			proc = connection
					.prepareCall("{ ? = call AMICUS.PACK_SORTFORM.SF_BUILDSRTFRM(?, ?, ?, ?, ?, ?, ?, ?) }");
			proc.registerOutParameter(1, Types.INTEGER);
			proc.setString(2, result);
			proc.registerOutParameter(3, Types.VARCHAR);
			proc.setInt(4, bufSize);
			proc.setInt(5, parms.getSortFormMainType());
			proc.setInt(6, parms.getSortFormSubType());
			proc.setInt(7, parms.getNameTitleOrSubjectType());
			proc.setInt(8, parms.getNameSubtype());
			proc.setInt(9, parms.getSkipInFiling());
			proc.execute();

			rc = proc.getInt(1);

			if (rc != 0) {
				throw new SortFormException(String.valueOf(rc));
			}
			result = proc.getString(3);
		} catch (SQLException | HibernateException e) {
			logger.error(e.getMessage(), e);
			logAndWrap(e);
		} finally {
			try {
				if (proc != null) {
					proc.close();
				}
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return result;
	}


	public void saveCpyIdAgent(String userName, int cpyIdNbr) {
		Connection connection = null;
		PreparedStatement stmt0 = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		Session session = currentSession();
		int count = 0;
		try {
			connection = session.connection();
			// --------> Lock della riga
			stmt0 = connection.prepareStatement("SELECT * FROM CPY_ID_AGENT WHERE CPY_ID_NBR = ? FOR UPDATE");
			stmt0.setInt(1, cpyIdNbr);

			stmt1 = connection.prepareStatement("UPDATE CPY_ID_AGENT SET USERNAME = ?, TRSTN_DTE = SYSDATE , AGENT_ID = 1 WHERE CPY_ID_NBR = ?");
			stmt1.setString(1, userName);
			stmt1.setInt(2, cpyIdNbr);
			count = stmt1.executeUpdate();

			if (!(count > 0)) {
				stmt2 = connection.prepareStatement("INSERT INTO CPY_ID_AGENT (CPY_ID_NBR, USERNAME, AGENT_ID, TRSTN_DTE) VALUES (?,?,?,SYSDATE)");
				stmt2.setInt(1, cpyIdNbr);
				stmt2.setString(2, userName);
				stmt2.setInt(3, 1);
				stmt2.execute();
			}
			/* Il commit o rollback lo fa hibernate in automatico se le operazioni successive vanno bene: quindi se sulla CPY_ID va tutto ok committa altrimenti no */
		} catch (HibernateException | SQLException e) {
			logger.error(e.getMessage(), e);
			logAndWrap(e);
		} finally {
			try {
				if (stmt0 != null) {
					stmt0.close();
				}
				if (stmt1 != null) {
					stmt1.close();
				}
				if (stmt2 != null) {
					stmt2.close();
				}
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				logAndWrap(e);
			}
		}
	}

}
