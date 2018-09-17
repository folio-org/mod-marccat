package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.Global;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.patterns.PredictionPattern.PopulationEntry;
import org.folio.cataloging.business.serialControl.DuplicateVendorException;
import org.folio.cataloging.business.serialControl.SerialsControlManager;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.common.TransactionalHibernateOperation;
import org.folio.cataloging.dao.persistence.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DAOPredictionPattern extends HibernateUtil {

	private static final Log logger = LogFactory
			.getLog(DAOPredictionPattern.class);

	public SRL_PRED_PAT getPatternByAmicusNumber(int amicusNumber)
			throws DataAccessException {
		List l = find(" from SRL_PRED_PAT as p " + "where p.amicusNumber = ? ",
				new Object[] { new Integer(amicusNumber) },
				new Type[] { Hibernate.INTEGER });
		if (l.size() > 0) {
			return (SRL_PRED_PAT) l.get(0);
		} else {
			return null;
		}
	}

	public List loadChronologies(int predictionPatternNumber)
			throws DataAccessException {
		return find(" from SRL_PRED_PAT_DTL as d "
				+ " where d.predictionPatternNumber = ? "
				+ " order by d.sequenceNumber ", new Object[] { new Integer(
				predictionPatternNumber) }, new Type[] { Hibernate.INTEGER });
	}

	public List loadEnumerations(int predictionPatternNumber)
			throws DataAccessException {
		return find(
				" from SRL_ENUM as e " +

				" where e.predictionPatternNumber = ? "
						+ " order by e.sequenceNumber ",
				new Object[] { new Integer(predictionPatternNumber) },
				new Type[] { Hibernate.INTEGER });
	}

	public List loadOrders(int amicusNumber) throws DataAccessException {
		List result = new ArrayList();
		List l = find(" from SRL_ORDR as o " + " where o.amicusNumber = ? "
				+ " order by o.orderNumber ", new Object[] { new Integer(
				amicusNumber) }, new Type[] { Hibernate.INTEGER });
		Iterator iter = l.iterator();
		while (iter.hasNext()) {
			SRL_ORDR o = (SRL_ORDR) iter.next();
			List l2 = find(" from SerialLogicalCopy lc where "
					+ " lc.orderNumber = ? ", new Object[] { new Integer(o
					.getOrderNumber()) }, new Type[] { Hibernate.INTEGER });
			o.setSubscriptions(l2);
			Iterator iter2 = l2.iterator();
			while (iter2.hasNext()) {
				SerialLogicalCopy lc = (SerialLogicalCopy) iter2.next();
				List l3 = find(" from SerialPart p "
						+ " where p.serialCopyNumber = ? "
						+ " order by p.publicationDate desc ",
						new Object[] { new Integer(lc.getSerialCopyNumber()) },
						new Type[] { Hibernate.INTEGER });
				lc.setIssues(l3);
			}
			result.add(o);
		}
		return result;
	}

	public void save(final SerialsControlManager mgr)
			throws DataAccessException {
		new TransactionalHibernateOperation() {

			public void doInHibernateTransaction(Session session)
					throws HibernateException, SQLException,
					DataAccessException, IOException {
				SRL_PRED_PAT p = mgr.getPredictionPattern();
				if (p.isNew()) {
					p.generateNewKey(session);
				}
				p.markChanged();
				persistByStatus(p);
				session.delete("from SRL_PRED_PAT_DTL as chron "
						+ " where chron.predictionPatternNumber = ? ",
						new Object[] { new Integer(p
								.getPredictionPatternNumber()) },
						new Type[] { Hibernate.INTEGER });
				session.flush();
				Iterator iter = mgr.getChronologies().iterator();
				while (iter.hasNext()) {
					SRL_PRED_PAT_DTL chron = (SRL_PRED_PAT_DTL) iter.next();
					chron.evict();
					if (chron.isNew()) {
						chron.generateNewKey();
						chron.setPredictionPatternNumber(p
								.getPredictionPatternNumber());
					}
					chron.markNew();
					chron.setSequenceNumber(mgr.getChronologies()
							.indexOf(chron) + 1);
					persistByStatus(chron);
				}
				session.delete("from SRL_ENUM as enum "
						+ " where enum.predictionPatternNumber = ? ",
						new Object[] { new Integer(p
								.getPredictionPatternNumber()) },
						new Type[] { Hibernate.INTEGER });
				iter = mgr.getEnumerations().iterator();
				while (iter.hasNext()) {
					SRL_ENUM e = (SRL_ENUM) iter.next();
					e.evict();
					if (e.isNew()) {
						e.generateNewKey();
						e.setPredictionPatternNumber(p
								.getPredictionPatternNumber());
					}
					e.markNew();
					e.setSequenceNumber(mgr.getEnumerations().indexOf(e) + 1);
					persistByStatus(e);
				}
				iter = mgr.getOrders().iterator();
				while (iter.hasNext()) {
					SRL_ORDR o = (SRL_ORDR) iter.next();
					if (o.isNew()) {
						o.generateNewKey(session);
						o.setAmicusNumber(mgr.getAmicusNumber().intValue());
					}
					o.markChanged();
					persistByStatus(o);
					Iterator iter2 = o.getSubscriptions().iterator();
					while (iter2.hasNext()) {
						SerialLogicalCopy c = (SerialLogicalCopy) iter2.next();
						if (c.isNew()) {
							c.generateNewKey(session);
							c.setOrderNumber(o.getOrderNumber());
						}
						c.markChanged();
						persistByStatus(c);
						Iterator iter3 = c.getIssues().iterator();
						while (iter3.hasNext()) {
							SerialPart sp = (SerialPart) iter3.next();
							if (sp.isNew()) {
								sp.generateNewKey(session);
								sp.setSerialCopyNumber(new Integer(c
										.getSerialCopyNumber()));
							}
							sp.markChanged();
							persistByStatus(sp);
						}
						Iterator iter4 = c.getDeletedIssues().iterator();
						while (iter4.hasNext()) {
							SerialPart sp = (SerialPart)iter4.next();
							persistByStatus(sp);
						}
					}
					Iterator iter5 = o.getDeletedSubscriptions().iterator();
					while (iter5.hasNext()) {
						SerialLogicalCopy lc = (SerialLogicalCopy)iter5.next();
						persistByStatus(lc);
					}
				}
				iter = mgr.getDeletedOrders().iterator();
				while (iter.hasNext()) {
					SRL_ORDR o = (SRL_ORDR) iter.next();
					persistByStatus(o);
				}
			}
		}.execute();
	}

	public void checkDuplicateVendor(SRL_VNDR v) throws DataAccessException,
			DuplicateVendorException {
		List l = find("from SRL_VNDR as v " + " where v.name = ?",
				new Object[] { v.getName() }, new Type[] { Hibernate.STRING });
		if (!l.isEmpty()) {
			throw new DuplicateVendorException();
		}
	}

	public void savePopulationResults(final List population, final int scope,
			final boolean replace, final SerialLogicalCopy sub,
			final SRL_ORDR order) throws DataAccessException {
		new TransactionalHibernateOperation() {

			public void doInHibernateTransaction(Session session)
					throws HibernateException, SQLException,
					DataAccessException, IOException {
				String fromClause = null;
				String whereClause = null;
				String existsClause = null;
				String deleteClause = null;

				PreparedStatement stmt = null;
				String sql = null;

				Connection conn = null;
				Transaction tx = null;
				try {
					tx = session.beginTransaction();
					conn = session.connection();

					stmt = conn
							.prepareStatement("INSERT INTO S_WRK_PRED_PAT (PUB_DTE, TYPE, CAPTION) VALUES(?,?,?)");
					Iterator iter = population.iterator();
					while (iter.hasNext()) {
						PopulationEntry e = (PopulationEntry) iter.next();
						logger.debug("insert(" + e.getDate() + ", " + e.getType() + "," + e.getCaption() + ")" );
						stmt.setDate(1, new java.sql.Date(e.getDate().getTime()));
						stmt.setInt(2, e.getType());
						stmt.setString(3, e.getCaption());
						stmt.addBatch();
					}
					stmt.executeBatch();
					if (scope == 1) {
						fromClause = "SRL_LGCL_CPY_ID, S_WRK_PRED_PAT";
						whereClause = " SRL_LGCL_CPY_ID.SRL_CPY_ID_NBR = "
								+ sub.getSerialCopyNumber();
					} else if (scope == 2) {
						fromClause = "SRL_LGCL_CPY_ID, S_WRK_PRED_PAT";
						whereClause = " SRL_LGCL_CPY_ID.ORDR_NBR = "
								+ sub.getOrderNumber();
					} else if (scope == 3) {
						fromClause = "SRL_ORDR, SRL_LGCL_CPY_ID, S_WRK_PRED_PAT";
						whereClause = " SRL_ORDR.ORDR_NBR = SRL_LGCL_CPY_ID.ORDR_NBR AND "
								+ " SRL_ORDR.BIB_ITM_NBR = "
								+ order.getAmicusNumber();
					}

					existsClause = " (SELECT SRL_CPY_ID_NBR, PUB_DTE FROM " + System.getProperty(Global.SCHEMA_SUITE_KEY) + ".SRL_PRT b "
							+ " WHERE b.SRL_CPY_ID_NBR = SRL_LGCL_CPY_ID.SRL_CPY_ID_NBR AND "
							+ " b.PUB_DTE = S_WRK_PRED_PAT.PUB_DTE) ";

					if (replace) {
						deleteClause = " (srl_cpy_id_nbr, pub_dte) in (select srl_cpy_id_nbr, pub_dte from "
								+ fromClause
								+ " where "
								+ whereClause
								+ " AND EXISTS "
								+ existsClause
								+ ")"
								+ " AND srl_prt.cpy_id_nbr is null AND srl_prt.recvd_dte is null ";
						stmt = conn.prepareStatement("DELETE FROM " + System.getProperty(Global.SCHEMA_SUITE_KEY) + ".SRL_PRT "
								+ " WHERE " + deleteClause);
						stmt.execute();
					}

					whereClause = whereClause + " AND NOT EXISTS "
							+ existsClause;
					sql = " INSERT INTO " + System.getProperty(Global.SCHEMA_SUITE_KEY) + ".SRL_PRT ( " + "        SRL_PRT_NBR, "
							+ "        SRL_CPY_ID_NBR, "
							+ "        SRL_CPY_STUS_CDE, "
							+ "        PUB_DTE, " + "        TYPE, "
							+ "        ENUM_DESC " + "   ) "
							+ "        SELECT srl_prt_seq.nextval, "
							+ "               srl_lgcl_cpy_id.srl_cpy_id_nbr, "
							+ "               0, "
							+ "               S_WRK_PRED_PAT.pub_dte, "
							+ "               S_WRK_PRED_PAT.type, "
							+ "               S_WRK_PRED_PAT.caption "
							+ "        FROM  " + fromClause + "        WHERE  "
							+ whereClause;

					stmt = conn.prepareStatement(sql);
					stmt.execute();
				} catch (Exception e) {
					logger.debug(sql);
					logger.warn(e);
					try {
						tx.rollback();
					} catch (Exception e1) {
					}
					throw new DataAccessException();
				} finally {
					try {
						tx.commit();
						if (stmt != null) {
							stmt.close();
						}
					} catch (Exception e) {
					}
				}
			}
		}.execute();
	}


	public void updateOrderLine(String enumDescription, String expiryDate, int orderNo, int bib_itm_nbr) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		Session session = currentSession();

		try {
			connection =session.connection();
			String description = enumDescription+" ricevuta il "+expiryDate;
			String sql = "UPDATE adempiere.c_orderline SET receiptDescription=receiptDescription||?||chr(13)||chr(10)"+
			              " WHERE c_order_id=? AND bib_itm_nbr=?";
		    stmt = connection.prepareStatement (sql);
			stmt.setString(1, description);
			stmt.setInt(2, orderNo);
			stmt.setInt(3, bib_itm_nbr);
		    stmt.execute();

		} catch (HibernateException e) {
			e.printStackTrace();
			logAndWrap(e);

		} catch (SQLException e) {
			e.printStackTrace();
			logAndWrap(e);
		}
		finally
		{
			try {
				if (stmt!=null){
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				logAndWrap(e);
			}
		}
	}


}
