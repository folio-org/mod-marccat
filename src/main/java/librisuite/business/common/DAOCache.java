/*
 * (c) LibriCore
 * 
 * Created on Jan 24, 2005
 * 
 * DAOCache.java
 */
package librisuite.business.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import librisuite.hibernate.Cache;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.libricore.librisuite.common.HibernateUtil;
import com.libricore.librisuite.common.TransactionalHibernateOperation;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/02/02 14:09:42 $
 * @since 1.0
 */
public class DAOCache extends HibernateUtil {
	
	private static final Log logger = LogFactory.getLog(DAOCache.class);

	public Cache load(int bibItemNumber, int cataloguingView)
		throws RecordNotFoundException, DataAccessException {
		List l =
			find(
				"from Cache as c "
					+ " where c.bibItemNumber = ? and c.cataloguingView = ?",
				new Object[] {
					new Integer(bibItemNumber),
					new Integer(cataloguingView)},
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });
		if (l.size() == 0) {
			throw new RecordNotFoundException("Cache entry not found");
		}
		return (Cache) l.get(0);
	}
	
	public void updateMadesCacheTable(final int madItemNumber, final int cataloguingView) throws DataAccessException {
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s) throws HibernateException, SQLException, CacheUpdateException {
				int result;
				CallableStatement proc = null;
				try {
					Connection connection = s.connection();
					logger.info("CFN_PR_CACHE_UPDATE_MADES parameters nbr:" + madItemNumber+ ", view:" + cataloguingView + ", -1");
					proc = connection.prepareCall("{call AMICUS.CFN_PR_CACHE_UPDATE_MADES(?, ?, ?, ?) }");
					proc.setInt(1, madItemNumber);
					proc.setInt(2, cataloguingView);
					proc.setInt(3, -1); // this parameter no longer used
					proc.registerOutParameter(4, Types.INTEGER);
					proc.execute();				
					result = proc.getInt(4);
					// MIKE: store the return code as message
					if (result == 1) {
						throw new CacheUpdateException("No record inserted or updated");
					}
					else if (result == 2) {
						throw new CacheUpdateException("Duplicated value on index");
					}				
					else if (result > 2) {
						throw new CacheUpdateException("SQL_CODE: "+result);
					}
				}  finally {
					try {
						if(proc!=null) proc.close();
					} catch (SQLException ex) {
						// do nothing
						ex.printStackTrace();
					}
				}
			}
		}.execute();
	}

	
	/**
	 * pm 2011
	 * returns the view number of variants of the given record in the cache
	 * @param amicusNumber
	 * @return
	 * @throws DataAccessException 
	 */
	public List getVariantViews(final int amicusNumber) throws DataAccessException {
		final List result = new ArrayList();
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
			throws HibernateException, DataAccessException {
			Connection connection = s.connection();
			PreparedStatement stmt = null;
			try {
				stmt = connection.prepareStatement(
							"SELECT trstn_vw_nbr " +
					        	" FROM s_cache_bib_itm_dsply a " +
					                  " WHERE a.bib_itm_nbr = ? " +
					                  " order by 1 ");
				stmt.setInt(1, amicusNumber);
				java.sql.ResultSet js = stmt.executeQuery();
				while (js.next()) {
					result.add(new Integer(js.getInt("trstn_vw_nbr")));
				}
			} catch (SQLException e) {
				throw new DataAccessException(e);
			}
			finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						// do nothing;
					}
				}
			}
		}
	}
	.execute();
	return result;
	}

	/**
	 * pm 2011
	 * counts the number of variants of the given record in the cache
	 * @param amicusNumber
	 * @return
	 * @throws DataAccessException 
	 */
	public int getVariantCount(final int amicusNumber) throws DataAccessException {
		class Integerwrapper {
			int value;
		}
		final Integerwrapper count = new Integerwrapper();
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
			throws HibernateException, DataAccessException {
			Connection connection = s.connection();
			PreparedStatement stmt = null;
			java.sql.ResultSet js = null;
			try {
				stmt = connection.prepareStatement(
							"SELECT count(*) as count " +
					        	" FROM s_cache_bib_itm_dsply a" +
					                  " WHERE a.bib_itm_nbr = ?");
				stmt.setInt(1, amicusNumber);
				js = stmt.executeQuery();
				while (js.next()) {
					count.value = js.getInt("count");
				}
			} catch (SQLException e) {
				throw new DataAccessException();
			}
			finally {
			if (js != null) {
					try {
						js.close();
					} catch (SQLException e) {
						// do nothing;
					}
				}
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						// do nothing;
					}
				}
			}
		}
	}
	.execute();
	return count.value;
	}
	
	/**
	 * pm 2011
	 * Determines the correct view to retrieve for the given amicusNumber
	 * based on the contents of the cache and the user's selected preference
	 * order
	 * @param amicusNumber
	 * @param preferenceOrder
	 * @return
	 * @throws DataAccessException
	 */
	public int getPreferredView(final int amicusNumber, final int preferenceOrder) throws DataAccessException {
		class Intwrapper {
			int value;
		}
		final Intwrapper preferredView = new Intwrapper();
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
			throws HibernateException, DataAccessException {
			Connection connection = s.connection();
			PreparedStatement stmt = null;
			java.sql.ResultSet js = null;
			try {
				stmt = connection.prepareStatement(
							"SELECT trstn_vw_nbr FROM (" +
					        	"SELECT b.trstn_vw_nbr " +
					        	" FROM s_cache_bib_itm_dsply a, " +
					                  " db_prfr_ordr_seq b " +
					                  " WHERE bib_itm_nbr = ? and " +
					                   " a.trstn_vw_nbr = b.trstn_vw_nbr and " +
					                   " b.DB_PRFNC_ORDR_NBR = ? " +
					        " order by b.vw_seq_nbr) " +
							" where rownum < 2");
				stmt.setInt(1, amicusNumber);
				stmt.setInt(2, preferenceOrder);
				js = stmt.executeQuery();
				while (js.next()) {
					preferredView.value = js.getInt("trstn_vw_nbr");
				}
			} catch (SQLException e) {
				throw new DataAccessException();
			}
			finally {
				if (js   != null) { try {js.close();} catch (SQLException e) {}}
				if (stmt != null) { try {stmt.close();} catch (SQLException e) {}}
			}
		}
	}
	.execute();
	return preferredView.value;
	}

	
}
