/*
 * (c) LibriCore
 * 
 * Created on Jul 20, 2004
 * 
 * DAOIndexList.java
 */
package librisuite.business.searching;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import librisuite.bean.searching.RestrictionBean;
import librisuite.business.common.DataAccessException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.atc.weloan.shared.integration.AbstractDAO;
import com.libricore.librisuite.common.HibernateUtil;

/** 
 * @author hansv
 * @version %I%, %G%
 * @since 1.0
 */
public class DAORestrictions extends HibernateUtil 
{
	private static final Log logger = LogFactory.getLog(DAORestrictions.class);
	
	public List<RestrictionBean> getRestrictions(String userName) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<RestrictionBean> result = new ArrayList<RestrictionBean>();
	try {
		connection = currentSession().connection();
		stmt = connection.prepareStatement("select * from S_QRY_QLFG_TXT  where USR_NME = ?");
		stmt.setString(1, AbstractDAO.fixedCharPadding(userName, 12));
		rs = stmt.executeQuery();
		
		while(rs.next())
		{
			RestrictionBean bean = new RestrictionBean();
			bean.setName( rs.getString("QRY_QLFG_NME"));
			bean.setUserName(rs.getString("USR_NME"));
			bean.setValue(rs.getString("QRY_QLFG_TXT"));
			bean.setDate(rs.getDate("QRY_QLFG_DTE"));
			result.add(bean);
		}
		
	} catch (HibernateException e) {
		logAndWrap(e);
	} catch (SQLException e) {
		logAndWrap(e);
	}
	
	finally
	{
			try {
				rs.close();
			} catch (SQLException e) {
			}
			try {
				stmt.close();
			} catch (SQLException e) {
			}
	}
	
	return result;
	}

	public void updateOrInsertRestriction(String userName,String value,String name,String oldName) throws DataAccessException 
	{
		int count = updateRestriction(userName, value, name, oldName);
		if(count == 0)
			insertRestriction(userName, value, name);
	}
	
	
	public void insertRestriction(String userName,String value,String name) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		Session session = currentSession();
	try {
		connection = session.connection();
		stmt = connection.prepareStatement("INSERT INTO  S_QRY_QLFG_TXT  (USR_NME,QRY_QLFG_NME,QRY_QLFG_TXT,QRY_QLFG_DTE)  VALUES (?,?,?,?) ");
		stmt.setString(1, AbstractDAO.fixedCharPadding(userName, 12));
		stmt.setString(2, name);
		stmt.setString(3, value);
		stmt.setDate(4, new Date(System.currentTimeMillis()));
		stmt.executeUpdate();
	} catch (HibernateException e) {
		logAndWrap(e);
	} catch (SQLException e) {
		logAndWrap(e);
	}
	
	finally
	{
			try {
				stmt.close();
			} catch (SQLException e) {
			}
	}
	
	}
	
	public int  updateRestriction(String userName,String value,String name,String oldName) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		Session session = currentSession();
		int count = 0;
	try {
		connection = session.connection();
		stmt = connection.prepareStatement("UPDATE  S_QRY_QLFG_TXT set QRY_QLFG_NME=?,QRY_QLFG_TXT=?,QRY_QLFG_DTE=?  where USR_NME=? and QRY_QLFG_NME=?");
		stmt.setString(1, name);
		stmt.setString(2, value);
		stmt.setDate(3, new Date(System.currentTimeMillis()));
		stmt.setString(4, AbstractDAO.fixedCharPadding(userName, 12));
		stmt.setString(5, oldName);
		count =  stmt.executeUpdate();
	} catch (HibernateException e) {
		logAndWrap(e);
	} catch (SQLException e) {
		logAndWrap(e);
	}
	
	finally
	{
	try {
				stmt.close();
			} catch (SQLException e) {
			}
	}
		return count;
	}

	public void  deleteRestriction(String userName,String name) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		Session session = currentSession();
	try {
		connection = session.connection();
		stmt = connection.prepareStatement("DELETE  FROM S_QRY_QLFG_TXT where USR_NME=? and QRY_QLFG_NME=?");
		stmt.setString(1, AbstractDAO.fixedCharPadding(userName, 12));
		stmt.setString(2, name);
		stmt.executeUpdate();
	} catch (HibernateException e) {
		logAndWrap(e);
	} catch (SQLException e) {
		logAndWrap(e);
	}
	
	finally
	{
	try {
				stmt.close();
			} catch (SQLException e) {
			}
	}
	}
	
}