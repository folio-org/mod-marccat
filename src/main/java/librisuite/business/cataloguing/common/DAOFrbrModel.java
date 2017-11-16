package librisuite.business.cataloguing.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import librisuite.bean.searching.frbr.Expression;
import librisuite.bean.searching.frbr.Manifestation;
import librisuite.bean.searching.frbr.Work;
import librisuite.business.common.DataAccessException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.libricore.librisuite.common.HibernateUtil;

public class DAOFrbrModel extends HibernateUtil 
{
	
	private static final Log logger = LogFactory.getLog(DAOFrbrModel.class);
	/**
	 * Il metodo legge dal modello dei record bibliografici il tipo di WEMI data la LABEL
	 * @return
	 * @throws DataAccessException
	 */
	public Integer getBiblioWemiFirstTypeFromModelById(int modelId) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Integer result = null;
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT FRBR_TYPE_FIRST FROM BIBLIOGRAPHIC_MODEL WHERE ID=?");
			stmt.setInt(1, modelId);
			stmt.setMaxRows(1);
			rs = stmt.executeQuery();
			if (rs.next()){
				result = rs.getInt("FRBR_TYPE_FIRST");
			}
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		}
		finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
			}
		}
		return result;
	}
	
	/**
	 * Il metodo legge dal modello dei record d'autority il tipo di WEMI data la LABEL
	 * @param label
	 * @return
	 * @throws DataAccessException
	 */
	public Integer getAutorityWemiFirstTypeFromModelById(int modelId) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Integer result = null;
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT FRBR_TYPE_FIRST, FRBR_TYPE_SECOND,FRBR_TYPE_THIRD FROM AUTHORITY_MODEL WHERE ID=?");
			stmt.setInt(1, modelId);
			stmt.setMaxRows(1);
			rs = stmt.executeQuery();
			if (rs.next()){
				result = rs.getObject("FRBR_TYPE_FIRST")!= null?rs.getInt("FRBR_TYPE_FIRST"):null;
			    if (result==null){
			    	result = rs.getObject("FRBR_TYPE_SECOND")!= null?rs.getInt("FRBR_TYPE_SECOND"):null;
			    }
			    if (result==null){
			    	result = rs.getObject("FRBR_TYPE_THIRD")!= null?rs.getInt("FRBR_TYPE_THIRD"):null;
			    }
			}
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		}
		finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
			}
		}
		return result;
	}
	
	public void updateWemiBibliographicFlag(Integer id, Integer frbrFirstGroupValue) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("UPDATE BIBLIOGRAPHIC_MODEL set FRBR_TYPE_FIRST=? where ID=?");
			stmt.setInt(1, frbrFirstGroupValue);
			stmt.setInt(2, id);
			stmt.executeUpdate();
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		}
		finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}
	
	public void updateWemiAuthorityFlag(Integer id, Integer frbrFirstGroupValue, Integer frbrSecondGroupValue, Integer frbrThirdGroupValue) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection
					.prepareStatement("UPDATE AUTHORITY_MODEL set FRBR_TYPE_FIRST=? where ID=?");
			stmt.setInt(1, frbrFirstGroupValue);
			stmt.setInt(2, id);
			stmt.executeUpdate();
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}
		
	public void updateRecordBibliographicWemiFlag(Integer amicusNumber,Integer frbrFirstGroupValue) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("UPDATE " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT set FRBR_HDG_TYP_CDE=? where B_BIB_ITM_NBR=?");
			stmt.setInt(1, frbrFirstGroupValue);
			stmt.setInt(2, amicusNumber);
			int updateRows = stmt.executeUpdate();
			
			if(updateRows == 0)
			{
				stmt = connection.prepareStatement("INSERT INTO " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT (FRBR_HDG_TYP_CDE,B_BIB_ITM_NBR) VALUES (?,?)");
				stmt.setInt(1, frbrFirstGroupValue);
				stmt.setInt(2, amicusNumber);
				stmt.executeUpdate();
			}
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}
	
	public void updateRecordAuthorityWemiFlag(Integer amicusNumber,Integer frbrFirstGroupValue) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("UPDATE " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT set FRBR_HDG_TYP_CDE=? where A_BIB_ITM_NBR=?");
			stmt.setInt(1, frbrFirstGroupValue);
			stmt.setInt(2, amicusNumber);
			int updateRows = stmt.executeUpdate();
			
			if(updateRows == 0)
			{
				stmt = connection.prepareStatement("INSERT INTO " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT (FRBR_HDG_TYP_CDE,A_BIB_ITM_NBR) VALUES (?,?)");
				stmt.setInt(1, frbrFirstGroupValue);
				stmt.setInt(2, amicusNumber);
				stmt.executeUpdate();
			}
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}
	
	private final static String SELECT_AUTHORITY_TYPE = "SELECT FRBR_HDG_TYP_CDE FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT WHERE A_BIB_ITM_NBR=?";
	private final static String SELECT_BIBLIOGRAPHIC_TYPE = "SELECT FRBR_HDG_TYP_CDE FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT WHERE B_BIB_ITM_NBR=?";
	public Integer getWemiFlag(Integer amicusNumber,boolean authority) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Integer result = null;
		String query = null;
		if(authority == true)
			query = SELECT_AUTHORITY_TYPE;
		else 
			query = SELECT_BIBLIOGRAPHIC_TYPE;
		
		try {
			Session session = currentSession();
			connection = session.connection();
			
			stmt = connection.prepareStatement(query);
			stmt.setInt(1, amicusNumber);
			rs = stmt.executeQuery();
			boolean res = rs.next();
			
			if(res) result = rs.getInt("FRBR_HDG_TYP_CDE");
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
			}
		}
		return result;
	}
	
	public String getWemiFlagLabel(Integer wemiCode) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = ""; 
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT STRING_TEXT FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".T_FRBR where TBL_VLU_CDE=?");
			stmt.setInt(1, wemiCode);
			
			rs = stmt.executeQuery();
			boolean res = rs.next();
			
			if(res) result = rs.getString("STRING_TEXT");
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
			}
		}
		return result;
	}
	
	public String getWemiFlagLabel(Integer wemiCode, Locale locale) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = ""; 
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT STRING_TEXT FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".T_FRBR where TBL_VLU_CDE=? and LANGID='"+locale.getISO3Language()+"'");
			stmt.setInt(1, wemiCode);
			
			rs = stmt.executeQuery();
			boolean res = rs.next();
			
			if(res) result = rs.getString("STRING_TEXT");
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
			}
		}
		return result;
	}
	
	
	public List<Expression> getExpressionsFromEntity(Integer amicusNumber,Integer entity) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Expression> expressionsList = new ArrayList<Expression>();
		try {
			Session session = currentSession();
			connection = session.connection();
			
			String resultField = getField(entity);
			if(entity == 2 || entity == 4)
			{
				resultField = "A." + resultField  + "=? OR B." + resultField + "=?";
			}
			else resultField = resultField + "=?";
			stmt = connection.prepareStatement("SELECT a.EXP_BIB_ITM_NBR FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".frbr_exp_mnf_rlt a left outer join " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".frbr_wrk_exp_rlt b on a.exp_bib_itm_nbr=b.exp_bib_itm_nbr where " + resultField);
			
			stmt.setInt(1, amicusNumber);
			if(entity == 2 || entity == 4)
			{
				stmt.setInt(2, amicusNumber);
			}
			
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				Expression expression = new Expression();
				expression.setAmicusNumber(rs.getInt("EXP_BIB_ITM_NBR"));
				refineExpression(expression);
				expressionsList.add(expression);
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
		return expressionsList;
	}
	
	public List<Expression> getExpressionsFromEntityWork(Integer amicusNumber) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Expression> expressionsList = new ArrayList<Expression>();
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT EXP_BIB_ITM_NBR FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_WRK_EXP_RLT where WRK_BIB_ITM_NBR=?");
			stmt.setInt(1, amicusNumber);
			
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				Expression expression = new Expression();
				expression.setAmicusNumber(rs.getInt("EXP_BIB_ITM_NBR"));
				refineExpression(expression);
				expressionsList.add(expression);
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
		return expressionsList;
	}
		
	public Expression getExpressionFromManifestation(Integer amicusNumber,Integer entity) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Expression expression = new Expression();
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT EXP_BIB_ITM_NBR FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_EXP_MNF_RLT where MNF_BIB_ITM_NBR=?");
			stmt.setInt(1, amicusNumber);
			
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				expression.setAmicusNumber(rs.getInt("EXP_BIB_ITM_NBR"));
				refineExpression(expression);
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
		return expression;
	}
	
	public List<Manifestation> getManifestationsFromExpression(Integer expressionAmicusNumber) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Manifestation> manifestationList = new ArrayList<Manifestation>();
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT MNF_BIB_ITM_NBR FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_EXP_MNF_RLT where EXP_BIB_ITM_NBR=?");
			stmt.setInt(1, expressionAmicusNumber);
			
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				Manifestation manifestation = new Manifestation();
				manifestation.setAmicusNumber(rs.getInt("MNF_BIB_ITM_NBR"));
				refineManifestation(manifestation);
				manifestationList.add(manifestation);
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
		return manifestationList;
	}
	
	
	public Work loadTreeRelations(Integer amicusNumber,Integer entity) throws DataAccessException
	{
		Work work = null;
		if(entity != null) {
		 work = getWorkFromEntity(amicusNumber, entity);
		} 
		else return new Work(amicusNumber);
		
		if(work.getAmicusNumber() == null || work.getAmicusNumber() == 0)
		{
			List<Expression> expressionList = getExpressionsFromEntity(amicusNumber, entity);
			work.setExpressionList(expressionList);
		}
		
		return work;
	}
	
	public Work getWorkFromEntity(Integer amicusNumber,Integer entity) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Work work= new Work(amicusNumber);
		try {
			Session session = currentSession();
			connection = session.connection();
			
			String resultField = getField(entity);
			if(entity == 2 || entity == 4)
			{
				resultField = "A." + resultField  + "=? OR B." + resultField + "=?";
			}else resultField = resultField + "=?";
			
			stmt = connection.prepareStatement("SELECT WRK_BIB_ITM_NBR FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_WRK_EXP_RLT A LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_EXP_MNF_RLT B ON A.EXP_BIB_ITM_NBR=B.EXP_BIB_ITM_NBR WHERE " + resultField);
			stmt.setInt(1, amicusNumber);
			
			if(entity == 2 || entity == 4)
			{
				stmt.setInt(2, amicusNumber);
			}
			
			rs = stmt.executeQuery();
			if (rs.next()) {
				work.setAmicusNumber(rs.getInt("WRK_BIB_ITM_NBR"));
				refineWork(work);
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
		return work;
	}
	
	
	private String getField(Integer entity)
	{
		String result = "";
		switch (entity) {
		case 1:
			result = "WRK_BIB_ITM_NBR";
			break;
		case 2:
			result = "EXP_BIB_ITM_NBR";
			break;
		case 3:
			result = "WRK_BIB_ITM_NBR";
			break;
		case 4:
			result = "EXP_BIB_ITM_NBR";
			break;
		case 5:
			result = "MNF_BIB_ITM_NBR";
			break;
	}
		return result;
	}
	
	private void refineExpression(Expression expression) throws DataAccessException{
		List<Manifestation> manifestationList = getManifestationsFromExpression(expression.getAmicusNumber());
		expression.setManifestations(manifestationList);
		if(manifestationList != null)
		expression.setCountManifestation(manifestationList.size());
		
		expression.setTitle(getTitle(expression.getAmicusNumber()));
	}
	private void refineWork(Work work) throws DataAccessException{
		List<Expression> expressions =  getExpressionsFromEntityWork(work.getAmicusNumber());
		work.setExpressionList(expressions);		
	}
	private void refineManifestation(Manifestation manifestation) throws DataAccessException{
		manifestation.setTitle(getTitle(manifestation.getAmicusNumber()));
	}
	
	
//	public void insertExpressionManifestationRelation(Integer amicusNumberToRelated, Integer amicusNumberSelected) throws DataAccessException
//	{
//		Connection connection = null;
//		PreparedStatement stmt = null;
//		try {
//			Session session = currentSession();
//			connection = session.connection();
//			String amicusNumberToRelatedField = getField(getWemiFlag(amicusNumberToRelated));
//			String amicusNumberSelectedField = getField(getWemiFlag(amicusNumberSelected));
//			stmt = connection.prepareStatement("INSERT INTO " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_EXP_MNF_RLT (" + amicusNumberToRelatedField + "," +amicusNumberSelectedField+") VALUES (?,?)");
//			stmt.setInt(1, amicusNumberToRelated);
//			stmt.setInt(2, amicusNumberSelected);
//			stmt.executeUpdate();
//		} catch (HibernateException e) {
//			logAndWrap(e);
//		} catch (SQLException e) {
//			logAndWrap(e);
//		} finally {
//			try {
//				stmt.close();
//			} catch (SQLException e) {
//			}
//		}
//	}
	
//	public void insertWorkExpressionRelation(Integer amicusNumberToRelated, Integer amicusNumberSelected) throws DataAccessException
//	{
//		Connection connection = null;
//		PreparedStatement stmt = null;
//		try {
//			Session session = currentSession();
//			connection = session.connection();
//			String amicusNumberToRelatedField = getField(getWemiFlag(amicusNumberToRelated));
//			String amicusNumberSelectedField = getField(getWemiFlag(amicusNumberSelected));
//			stmt = connection.prepareStatement("INSERT INTO " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_WRK_EXP_RLT (" + amicusNumberToRelatedField + "," +amicusNumberSelectedField+") VALUES (?,?)");
//			stmt.setInt(1, amicusNumberToRelated);
//			stmt.setInt(2, amicusNumberSelected);
//			stmt.executeUpdate();
//		} catch (HibernateException e) {
//			logAndWrap(e);
//		} catch (SQLException e) {
//			logAndWrap(e);
//		} finally {
//			try {
//				stmt.close();
//			} catch (SQLException e) {
//			}
//		}
//	}
//	
//	public void createExpressionManifestationRelation(Integer amicusNumberToRelated, Integer amicusNumberSelected) throws DataAccessException
//	{
//		if(!isExpressionManifestationRelationPresent(amicusNumberToRelated,amicusNumberSelected))
//		{
//			insertExpressionManifestationRelation(amicusNumberToRelated, amicusNumberSelected);
//		}
//	}
//	
//	public void createWorkExpressionRelation(Integer amicusNumberToRelated, Integer amicusNumberSelected) throws DataAccessException
//	{
//		if(!isWorkExpressionRelationPresent(amicusNumberToRelated,amicusNumberSelected))
//		{
//			insertWorkExpressionRelation(amicusNumberToRelated, amicusNumberSelected);
//		}
//	}
//	
//	public boolean isExpressionManifestationRelationPresent(Integer amicusNumberToRelated, Integer amicusNumberSelected) throws DataAccessException
//	{
//		Connection connection = null;
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		boolean result = false; 
//		try {
//			Session session = currentSession();
//			connection = session.connection();
//			String amicusNumberToRelatedField = getField(getWemiFlag(amicusNumberToRelated));
//			String amicusNumberSelectedField = getField(getWemiFlag(amicusNumberSelected));
//			stmt = connection.prepareStatement("SELECT * FROM  " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_EXP_MNF_RLT where " + amicusNumberToRelatedField + "=? AND " + amicusNumberSelectedField +"=?");
//			stmt.setInt(1, amicusNumberToRelated);
//			stmt.setInt(2, amicusNumberSelected);
//			
//			rs = stmt.executeQuery();
//			result = rs.next();
//			
//			
//		} catch (HibernateException e) {
//			logAndWrap(e);
//		} catch (SQLException e) {
//			logAndWrap(e);
//		} finally {
//			try {
//				stmt.close();
//				rs.close();
//			} catch (SQLException e) {
//			}
//		}
//		return result;
//	}
//	
//	public boolean isWorkExpressionRelationPresent(Integer amicusNumberToRelated, Integer amicusNumberSelected) throws DataAccessException
//	{
//		Connection connection = null;
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		boolean result = false; 
//		try {
//			Session session = currentSession();
//			connection = session.connection();
//			String amicusNumberToRelatedField = getField(getWemiFlag(amicusNumberToRelated));
//			String amicusNumberSelectedField = getField(getWemiFlag(amicusNumberSelected));
//			stmt = connection.prepareStatement("SELECT * FROM  " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_WRK_EXP_RLT where " + amicusNumberToRelatedField + "=? AND " + amicusNumberSelectedField +"=?");
//			stmt.setInt(1, amicusNumberToRelated);
//			stmt.setInt(2, amicusNumberSelected);
//			
//			rs = stmt.executeQuery();
//			result = rs.next();
//			
//			
//		} catch (HibernateException e) {
//			logAndWrap(e);
//		} catch (SQLException e) {
//			logAndWrap(e);
//		} finally {
//			try {
//				stmt.close();
//				rs.close();
//			} catch (SQLException e) {
//			}
//		}
//		return result;
//	}
	


	public boolean isAuthorityRecord(Integer amicusNumber)
		throws DataAccessException
		{
			Connection connection = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			boolean result = false;
			try {
				Session session = currentSession();
				connection = session.connection();
				stmt = connection.prepareStatement("SELECT A_BIB_ITM_NBR from " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT WHERE A_BIB_ITM_NBR=?");
				stmt.setInt(1, amicusNumber);
				rs = stmt.executeQuery();
				result = rs.next(); 
				
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
			return result;
		}

	
	public String getTitle(Integer amicusNumber) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT TTL_HDG_MAIN_STRNG_TXT FROM S_CACHE_BIB_ITM_DSPLY WHERE BIB_ITM_NBR=?");
			stmt.setInt(1, amicusNumber);
			rs = stmt.executeQuery();
			if (rs.next()) {
				result = rs.getString("TTL_HDG_MAIN_STRNG_TXT");
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
		return result;
	}
	
	public String getHeadingName(Integer headingNumber) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT * FROM NME_HDG WHERE NME_HDG_NBR=?");
			stmt.setInt(1, headingNumber);
			rs = stmt.executeQuery();
			if (rs.next()) {
				result = rs.getString("NME_HDG_STRNG_TXT");
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
		return result;
	}
	
	public boolean isHeadingRelationPresent(Integer amicusNumber,String code,boolean isAuthority) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		boolean result = false;
		
//		logger.info("Code per query su F_HDG_REL: " + code);
		
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT * FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_HDG_REL WHERE BIB_ITM_NBR =? AND RLT_CODE = ? AND TYPE=?");
			stmt.setInt(1, amicusNumber);
			stmt.setString(2, code);
			stmt.setInt(3, isAuthority == true ? 0 : 1);
			ResultSet rs = stmt.executeQuery();
			
			result = rs.next();
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
//		logger.info("Trovato risultati per code: " + code + " ? " + result);
		return result;
	}
	
	public boolean isRecordRelationPresent(Integer amicusNumber,String code,boolean authority) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		boolean result = false;
		
//		logger.info("Code per query su F_RECORD_REL: " + code);
		
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT * FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_RECORD_REL WHERE RLT_CODE = ? AND ((BIB_ITM_NBR  =? AND TYPE=?) OR (BIB_ITM_NBR_REL=? AND TYPE_REL =?))");
											
			stmt.setString(1, code);
			stmt.setInt(2, amicusNumber);
			stmt.setInt(3, authority == true ? 0 : 1);
			stmt.setInt(4, amicusNumber);
			stmt.setInt(5, authority == true ? 0 : 1);

			ResultSet rs = stmt.executeQuery();
			
			result = rs.next();
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
//		logger.info("Trovato risultati per code: " + code + " ? " + result);
		return result;
	}	
}