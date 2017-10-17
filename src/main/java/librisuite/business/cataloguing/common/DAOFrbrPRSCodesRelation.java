/*
 * (c) LibriCore
 * 
 * Created on Dec 20, 2005
 * 
 * DAOModel.java
 */
package librisuite.business.cataloguing.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import librisuite.business.codetable.ValueLabelElement;
import librisuite.business.common.DataAccessException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.libricore.librisuite.common.HibernateUtil;

/**
 * 
 * @since 1.0
 */
public class DAOFrbrPRSCodesRelation extends DAOFrbrCodesRelation {
	
	
	public DAOFrbrPRSCodesRelation(Locale locale) {
		super(locale);
		// TODO Auto-generated constructor stub
	}

	public 	List<ValueLabelElement> getPrimaryRelations() throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ValueLabelElement> vl = new ArrayList<ValueLabelElement>();
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT DISTINCT A.LVL_RLT_TYPE,A.LVL_RLT_NAME FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_LVL_RLT A RIGHT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_PRS_RLT_TYP B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE WHERE A.LANGUAGE='"+ getLocale().getLanguage() +"'");
			rs = stmt.executeQuery();
			while (rs.next()) {
				ValueLabelElement element = new ValueLabelElement(rs.getString("LVL_RLT_TYPE"),rs.getString("LVL_RLT_NAME"));
				vl.add(element);
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
			}
		}
		return vl;
	}
	
	public 	List<ValueLabelElement> getRelationShips(String code) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ValueLabelElement> vl = new ArrayList<ValueLabelElement>();
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT PRS_RLT_CODE,PRS_RLT_NAME FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_PRS_RLT_TYP WHERE LVL_RLT_TYPE=? AND LANGUAGE='"+ getLocale().getLanguage() +"'");
			stmt.setString(1, code);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ValueLabelElement element = new ValueLabelElement(rs.getString("PRS_RLT_CODE"),rs.getString("PRS_RLT_NAME"));
				vl.add(element);
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
			}
		}
		return vl;
	}
	
	public 	List<ValueLabelElement> getDesignatorGroups(String code) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ValueLabelElement> vl = new ArrayList<ValueLabelElement>();
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT PRS_RLT_SUB_CODE,PRS_RLT_SUB_NAME FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_PRS_SUB_RLT_TYP WHERE PRS_RLT_CODE=? AND LANGUAGE='"+ getLocale().getLanguage() +"'");
			stmt.setString(1, code);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ValueLabelElement element = new ValueLabelElement(rs.getString("PRS_RLT_SUB_CODE"),rs.getString("PRS_RLT_SUB_NAME"));
				vl.add(element);
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
			}
		}
		return vl;
	}
	
}
