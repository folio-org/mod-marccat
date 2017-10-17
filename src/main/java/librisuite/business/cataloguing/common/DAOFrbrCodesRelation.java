package librisuite.business.cataloguing.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import librisuite.bean.searching.FrbrHierarchyItem;
import librisuite.bean.searching.FrbrHierarchyRelationItem;
import librisuite.bean.searching.FrbrItem;
import librisuite.bean.searching.frbr.Expression;
import librisuite.bean.searching.frbr.GenericEntity;
import librisuite.bean.searching.frbr.HeadingRel;
import librisuite.bean.searching.frbr.Manifestation;
import librisuite.bean.searching.frbr.Work;
import librisuite.business.codetable.ValueLabelElement;
import librisuite.business.common.DataAccessException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DAOFrbrCodesRelation extends DAOFrbrModel 
{
	private static Log logger = LogFactory.getLog(DAOFrbrCodesRelation.class);
	private Locale locale;

	public DAOFrbrCodesRelation(Locale locale){
		this.locale = locale;
	}
	
	public List<ValueLabelElement> getPrimaryRelations() throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ValueLabelElement> vl = new ArrayList<ValueLabelElement>();
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT DISTINCT A.LVL_RLT_TYPE,A.LVL_RLT_NAME FROM " 
					+ System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_LVL_RLT A LEFT OUTER JOIN " 
					+ System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_WRK_RLT_TYP B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE " 
					+ " WHERE A.LANGUAGE='"+ locale.getLanguage() +"' ORDER BY A.LVL_RLT_NAME DESC");
			rs = stmt.executeQuery();
			while (rs.next()) {
				ValueLabelElement element = new ValueLabelElement(rs.getString("LVL_RLT_TYPE"), rs.getString("LVL_RLT_NAME"));
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

	public List<ValueLabelElement> getRelationShips(String code) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ValueLabelElement> vl = new ArrayList<ValueLabelElement>();
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT WRK_RLT_CODE,WRK_RLT_NAME FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_WRK_RLT_TYP WHERE LANGUAGE='"+ locale.getLanguage() +"' AND LVL_RLT_TYPE=? ");
			stmt.setString(1, code);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ValueLabelElement element = new ValueLabelElement(rs.getString("WRK_RLT_CODE"), rs.getString("WRK_RLT_NAME"));
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

	public List<ValueLabelElement> getDesignatorGroups(String code) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ValueLabelElement> vl = new ArrayList<ValueLabelElement>();
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT WRK_RLT_SUB_CODE,WRK_RLT_SUB_NAME FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_WRK_SUB_RLT_TYP WHERE LANGUAGE='"+ locale.getLanguage() +"' AND WRK_RLT_CODE=? ");
			stmt.setString(1, code);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ValueLabelElement element = new ValueLabelElement(rs.getString("WRK_RLT_SUB_CODE"), rs.getString("WRK_RLT_SUB_NAME"));
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

	public List<ValueLabelElement> getDesignators(String code) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ValueLabelElement> vl = new ArrayList<ValueLabelElement>();
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT ID,DES_NAME FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) 
					+ ".F_DESIGNATORS WHERE RLT_CODE=? AND LANGUAGE='"+ locale.getLanguage() + "'"
					/* Bug 4923 inizio */
					+ " AND TAG_MARC_AUTH_SEQ IS NULL AND TAG_MARC_BIB_SEQ IS NULL"
					/* Bug 4923 fine */
					+ " ORDER BY DES_NAME ASC");
			stmt.setString(1, code);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ValueLabelElement element = new ValueLabelElement(rs.getString("ID"), rs.getString("DES_NAME"));
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

	public List<ValueLabelElement> getSubDesignators(String code) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ValueLabelElement> vl = new ArrayList<ValueLabelElement>();
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT ID,SUB_DES_NAME FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_SUB_DESIGNATORS  WHERE ID_DESIGNATOR=? AND LANGUAGE='"+ locale.getLanguage() +"' ORDER BY SUB_DES_NAME ASC");
			stmt.setString(1, code);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ValueLabelElement element = new ValueLabelElement(rs.getString("ID"), rs.getString("SUB_DES_NAME"));
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

	public List<ValueLabelElement> getSubSubDesignators(String code) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ValueLabelElement> vl = new ArrayList<ValueLabelElement>();
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT ID,SUB_SUB_DES_NAME FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) 
					+ ".F_SUB_SUB_DESIGNATORS  WHERE ID_SUB_DESIGNATOR=? AND LANGUAGE='"+ locale.getLanguage() +"' ORDER BY SUB_SUB_DES_NAME ASC");
			stmt.setString(1, code);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ValueLabelElement element = new ValueLabelElement(rs.getString("ID"), rs.getString("SUB_SUB_DES_NAME"));
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

	public String getSequenceTagMapping(String sql, String code, boolean authority) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String mapping = "";
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, code);
			rs = stmt.executeQuery();
			if (rs.next()) {
				if (authority)
					mapping = rs.getString("TAG_MARC_AUTH_SEQ");
				else
					mapping = rs.getString("TAG_MARC_BIB_SEQ");
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
		return mapping;
	}

	public List<FrbrHierarchyItem> getTypesRelation(Integer amicusNumber, boolean authority,Integer wemiCode,List<FrbrHierarchyItem> result) throws DataAccessException 
	{		
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String type = getQueryParams(wemiCode);
			Session session = currentSession();
			connection = session.connection();
			String query = getRelationQuery(wemiCode);
			
//			logger.info("Query eseguita: " + query);
			
			stmt = connection.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				
				String key = rs.getString("LVL_RLT_NAME");
				String value = rs.getString(type + "_RLT_NAME");
				String subValue = rs.getString(type + "_RLT_SUB_NAME");
				String code = rs.getString(type + "_RLT_CODE");
				String subCode = rs.getString(type + "_RLT_SUB_CODE");	
				
				code = subCode == null ? code : subCode;
				value = subCode == null ? value : subValue;
				if (isHeadingRelationPresent(amicusNumber, subCode == null ? code : subCode, authority)
						|| isRecordRelationPresent(amicusNumber, subCode == null ? code : subCode,authority)) {
					List<Map<String, FrbrItem>> list = null;
					
					FrbrHierarchyItem it = new FrbrHierarchyItem();
					it.setRelationType(key);
		
					FrbrHierarchyItem item = getFrbrHierarchyItem(result,it);
					if(item == null){
					    item = it;
						list = new ArrayList<Map<String, FrbrItem>>();
						list.addAll(getSummaryHeadingRelation(amicusNumber, code));
						list.addAll(getSummaryRecordRelation(amicusNumber, code));
						FrbrHierarchyRelationItem rel = getFrbrHierarchyRelationItem(item.getRelationsList(),item,code,value);
						rel.setList(list);
						
						List<FrbrHierarchyRelationItem> l = new ArrayList<FrbrHierarchyRelationItem>();
						l.add(rel);
						item.setRelationsList(l);
						
						result.add(item);

					} else
					{
						FrbrHierarchyRelationItem rel = getFrbrHierarchyRelationItem(item.getRelationsList(),item,code,value);
						
						list = rel.getList() == null ? new ArrayList<Map<String,FrbrItem>>() : rel.getList();
						list.addAll(getSummaryHeadingRelation(amicusNumber, code));
						list.addAll(getSummaryRecordRelation(amicusNumber, code));
						
						rel.setList(list);
						
						if(rel.isNewItem())
						{
							item.getRelationsList().add(rel);
						}
						
					}
				}
			}

		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} catch (Exception e) {
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

	public List<FrbrHierarchyItem> getTypesRelation(Integer amicusNumber,boolean authority,Integer wemiCode) throws DataAccessException 
	{	
		List<FrbrHierarchyItem> result = new ArrayList<FrbrHierarchyItem>();
		getTypesRelation(amicusNumber, authority, wemiCode,result);
		for(int i = 6; i <= 10; i++)
		{
			try{
				getTypesRelation(amicusNumber, authority, i,result);
			}catch (Exception e) {
				continue;
			}
		}
		
		Collections.sort(result,new Comparator<FrbrHierarchyItem>(){

			@Override
			public int compare(FrbrHierarchyItem o1, FrbrHierarchyItem o2) {
				String a = o1.getRelationType();
				String b = o2.getRelationType();
				
				return a.compareTo(b);
			}
			
		});
		
		Collections.reverse(result);
		
		return result;
	}
	
	
	private FrbrHierarchyItem getFrbrHierarchyItem(List<FrbrHierarchyItem> result,FrbrHierarchyItem item)
	{
		for(FrbrHierarchyItem frbrItem: result)
		{
			if(frbrItem.equals(item)){
				return frbrItem;
			}
		}
		return null;
	}
	
	private FrbrHierarchyRelationItem getFrbrHierarchyRelationItem(List<FrbrHierarchyRelationItem> result,FrbrHierarchyItem item,String code,String value)
	{
		FrbrHierarchyRelationItem rel = new FrbrHierarchyRelationItem();
		rel.setRelationCode(code);
		rel.setRelationName(value);
		if(result != null)
		{
			for(FrbrHierarchyRelationItem frbrItem: result)
			{
				if(frbrItem.equals(rel)){
					return frbrItem;
				}
			}
		}
		
		rel.setNewItem(true);
		return rel;
	}
	
	private String getRelationQuery(Integer type) throws Exception
	{
		/**
		 * Modificate le query per prendere anche i codici padre che hanno delle sotto relazioni (esempio: non prendeva WRE_001 con null come sotto relazione" 
		 */
//		String sql_work ="SELECT DISTINCT A.LVL_RLT_TYPE, A.WRK_RLT_NAME, B.LVL_RLT_NAME, A.WRK_RLT_CODE, c.WRK_RLT_SUB_CODE, c.WRK_RLT_SUB_NAME" 
//			+ " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_WRK_RLT_TYP A"
//			+ " LEFT JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE ='"+ locale.getLanguage() +"' LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_WRK_SUB_RLT_TYP c ON a.wrk_rlt_code=c.wrk_rlt_code" 
//			+ " AND C.LANGUAGE ='"+ locale.getLanguage() +"' where A.LANGUAGE ='"+ locale.getLanguage() +"'";
		
		String sql_work =
			  " SELECT DISTINCT A.LVL_RLT_TYPE, A.WRK_RLT_NAME, B.LVL_RLT_NAME, A.WRK_RLT_CODE, C.WRK_RLT_SUB_CODE, C.WRK_RLT_SUB_NAME"
			+ " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_WRK_RLT_TYP A" 
			+ " LEFT JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE = '" + locale.getLanguage() + "'"
			+ " LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_WRK_SUB_RLT_TYP C ON A.WRK_RLT_CODE=C.WRK_RLT_CODE AND C.LANGUAGE = '" + locale.getLanguage() + "'" 
			+ " WHERE A.LANGUAGE = '"+ locale.getLanguage() + "'"
			+ " UNION "
			+ " SELECT DISTINCT A.LVL_RLT_TYPE, A.WRK_RLT_NAME, B.LVL_RLT_NAME, A.WRK_RLT_CODE, NULL, NULL "
			+ " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_WRK_RLT_TYP A" 
			+ " LEFT JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE = '"+ locale.getLanguage() + "'"
			+ " WHERE A.LANGUAGE = '"+ locale.getLanguage() + "'";

//		String sql_expression="SELECT DISTINCT A.LVL_RLT_TYPE,A.EXP_RLT_NAME,B.LVL_RLT_NAME,A.EXP_RLT_CODE,c.EXP_RLT_SUB_CODE,c.EXP_RLT_SUB_NAME "
//			+ " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_EXP_RLT_TYP A LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE ='"+ locale.getLanguage() +"' "
//			+ " LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_EXP_SUB_RLT_TYP c on a.exp_rlt_code=c.exp_rlt_code AND C.LANGUAGE ='"+ locale.getLanguage() +"' where A.LANGUAGE ='"+ locale.getLanguage() +"'";
		
		String sql_expression=
			  " SELECT DISTINCT A.LVL_RLT_TYPE, A.EXP_RLT_NAME, B.LVL_RLT_NAME, A.EXP_RLT_CODE, C.EXP_RLT_SUB_CODE, C.EXP_RLT_SUB_NAME"
		    + " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_EXP_RLT_TYP A" 
		    + " LEFT JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE = '"+ locale.getLanguage() + "'"
		    + " LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_EXP_SUB_RLT_TYP C ON A.EXP_RLT_CODE=C.EXP_RLT_CODE AND C.LANGUAGE = '"+ locale.getLanguage() + "'" 
		    + " WHERE A.LANGUAGE = '"+ locale.getLanguage() + "'"
		    + " UNION"
		    + " SELECT DISTINCT A.LVL_RLT_TYPE, A.EXP_RLT_NAME, B.LVL_RLT_NAME, A.EXP_RLT_CODE, NULL, NULL" 
		    + " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_EXP_RLT_TYP A"
		    + " LEFT JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE = '"+ locale.getLanguage() + "'"
		    + " WHERE A.LANGUAGE = '"+ locale.getLanguage() + "'";
		
//		String sql_manifestation="SELECT DISTINCT A.LVL_RLT_TYPE,A.MNF_RLT_NAME,B.LVL_RLT_NAME,A.MNF_RLT_CODE,c.MNF_RLT_SUB_CODE,c.MNF_RLT_SUB_NAME"
//			+ " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_MNF_RLT_TYP A"
//			+ " LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE ='"+ locale.getLanguage() +"' LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_MNF_SUB_RLT_TYP c on a.mnf_rlt_code=c.mnf_rlt_code AND c.LANGUAGE ='"+ locale.getLanguage() +"' where A.LANGUAGE ='"+ locale.getLanguage() +"'";
		
		String sql_manifestation=
			  " SELECT DISTINCT A.LVL_RLT_TYPE, A.MNF_RLT_NAME, B.LVL_RLT_NAME, A.MNF_RLT_CODE, C.MNF_RLT_SUB_CODE, C.MNF_RLT_SUB_NAME"
			+ " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_MNF_RLT_TYP A"
			+ " LEFT JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE = '"+ locale.getLanguage() + "'"
			+ " LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_MNF_SUB_RLT_TYP C ON A.MNF_RLT_CODE=C.MNF_RLT_CODE AND C.LANGUAGE = '"+ locale.getLanguage() + "'"
			+ " WHERE A.LANGUAGE = '"+ locale.getLanguage() + "'"
			+ " UNION"
			+ " SELECT DISTINCT A.LVL_RLT_TYPE, A.MNF_RLT_NAME, B.LVL_RLT_NAME, A.MNF_RLT_CODE, NULL, NULL"
			+ " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_MNF_RLT_TYP A"
			+ " LEFT JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE = '"+ locale.getLanguage() + "'"
			+ " WHERE A.LANGUAGE = '"+ locale.getLanguage() + "'"; 
		
//		String sql_person ="SELECT DISTINCT A.LVL_RLT_TYPE,A.PRS_RLT_NAME,B.LVL_RLT_NAME,A.PRS_RLT_CODE,c.PRS_RLT_SUB_CODE,c.PRS_RLT_SUB_NAME" 
//			+ " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_PRS_RLT_TYP A" 
//			+ " LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE ='"+ locale.getLanguage() +"' LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_PRS_SUB_RLT_TYP c on  a.prs_rlt_code=c.prs_rlt_code AND c.LANGUAGE ='"+ locale.getLanguage() +"' where A.LANGUAGE ='"+ locale.getLanguage() +"'";
		
		String sql_person =
		      " SELECT DISTINCT A.LVL_RLT_TYPE, A.PRS_RLT_NAME, B.LVL_RLT_NAME, A.PRS_RLT_CODE, C.PRS_RLT_SUB_CODE, C.PRS_RLT_SUB_NAME"
			+ " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_PRS_RLT_TYP A"
			+ " LEFT JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE = '"+ locale.getLanguage() + "'"
			+ " LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_PRS_SUB_RLT_TYP C ON  A.PRS_RLT_CODE=C.PRS_RLT_CODE AND C.LANGUAGE = '"+ locale.getLanguage() + "'"
			+ " WHERE A.LANGUAGE = '"+ locale.getLanguage() + "'"
			+ " UNION"
			+ " SELECT DISTINCT A.LVL_RLT_TYPE, A.PRS_RLT_NAME, B.LVL_RLT_NAME, A.PRS_RLT_CODE, NULL, NULL"
			+ " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_PRS_RLT_TYP A"
			+ " LEFT JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE = '"+ locale.getLanguage() + "'"
			+ " WHERE A.LANGUAGE = '"+ locale.getLanguage() + "'";
		
		
//		String sql_family="SELECT DISTINCT A.LVL_RLT_TYPE,A.FML_RLT_NAME,B.LVL_RLT_NAME,A.FML_RLT_CODE,c.FML_RLT_SUB_CODE,c.FML_RLT_SUB_NAME" 
//			+ " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_FML_RLT_TYP" 
//			+ " A LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE ='"+ locale.getLanguage() +"' LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_FML_SUB_RLT_TYP c on  a.fml_rlt_code=c.fml_rlt_code AND c.LANGUAGE ='"+ locale.getLanguage() +"' where A.LANGUAGE ='"+ locale.getLanguage() +"'";
		
		String sql_family=
			  " SELECT DISTINCT A.LVL_RLT_TYPE, A.FML_RLT_NAME, B.LVL_RLT_NAME, A.FML_RLT_CODE, C.FML_RLT_SUB_CODE, C.FML_RLT_SUB_NAME"
			+ " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_FML_RLT_TYP A"
			+ " LEFT JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE = '"+ locale.getLanguage() + "'" 
			+ " LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_FML_SUB_RLT_TYP C ON A.FML_RLT_CODE=C.FML_RLT_CODE AND C.LANGUAGE = '"+ locale.getLanguage() + "'"
			+ " WHERE A.LANGUAGE = '"+ locale.getLanguage() + "'"
			+ " UNION"
			+ " SELECT DISTINCT A.LVL_RLT_TYPE, A.FML_RLT_NAME, B.LVL_RLT_NAME, A.FML_RLT_CODE, NULL, NULL"
			+ " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_FML_RLT_TYP A" 
			+ " LEFT JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE = '"+ locale.getLanguage() + "'" 
			+ " WHERE A.LANGUAGE = '"+ locale.getLanguage() + "'";
		
		
//		String sql_corparate="SELECT DISTINCT A.LVL_RLT_TYPE,A.CRB_RLT_NAME,B.LVL_RLT_NAME,A.CRB_RLT_CODE,c.CRB_RLT_SUB_CODE,c.CRB_RLT_SUB_NAME" 
//			+ " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_CRB_RLT_TYP A"
//			+ " LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE ='"+ locale.getLanguage() +"' LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_CRB_SUB_RLT_TYP c on  a.crb_rlt_code=c.crb_rlt_code AND c.LANGUAGE ='"+ locale.getLanguage() +"' where A.LANGUAGE ='"+ locale.getLanguage() +"'";
		
		String sql_corparate=
			  " SELECT DISTINCT A.LVL_RLT_TYPE, A.CRB_RLT_NAME, B.LVL_RLT_NAME, A.CRB_RLT_CODE, C.CRB_RLT_SUB_CODE, C.CRB_RLT_SUB_NAME"
			+ " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_CRB_RLT_TYP A"
			+ " LEFT JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE = '"+ locale.getLanguage() + "'"
			+ " LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_CRB_SUB_RLT_TYP C ON A.CRB_RLT_CODE=C.CRB_RLT_CODE AND C.LANGUAGE = '"+ locale.getLanguage() + "'"
			+ " WHERE A.LANGUAGE = '"+ locale.getLanguage() + "'"
			+ " UNION"
			+ " SELECT DISTINCT A.LVL_RLT_TYPE, A.CRB_RLT_NAME, B.LVL_RLT_NAME, A.CRB_RLT_CODE, NULL, NULL"
			+ " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_CRB_RLT_TYP A"
			+ " LEFT JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE = '"+ locale.getLanguage() + "'"
			+ " WHERE A.LANGUAGE = '"+ locale.getLanguage() + "'";
		
		
//		String sql_item_relation ="SELECT DISTINCT A.LVL_RLT_TYPE,A.ITM_RLT_NAME,B.LVL_RLT_NAME,A.ITM_RLT_CODE,c.ITM_RLT_SUB_CODE,c.ITM_RLT_SUB_NAME" 
//			+ " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_ITM_RLT_TYP A" 
//			+ " LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE ='"+ locale.getLanguage() +"' LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_ITM_SUB_RLT_TYP c on  a.itm_rlt_code=c.itm_rlt_code AND c.LANGUAGE ='"+ locale.getLanguage() +"' where A.LANGUAGE ='"+ locale.getLanguage() +"'";
		
		String sql_item_relation =
			  " SELECT DISTINCT A.LVL_RLT_TYPE, A.ITM_RLT_NAME, B.LVL_RLT_NAME, A.ITM_RLT_CODE, C.ITM_RLT_SUB_CODE, C.ITM_RLT_SUB_NAME"
			+ " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_ITM_RLT_TYP A"
			+ " LEFT JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE = '"+ locale.getLanguage() + "'"
			+ " LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_ITM_SUB_RLT_TYP C ON A.ITM_RLT_CODE=C.ITM_RLT_CODE AND C.LANGUAGE = '"+ locale.getLanguage() + "'"
			+ " WHERE A.LANGUAGE = '"+ locale.getLanguage() + "'"
			+ " UNION"
			+ " SELECT DISTINCT A.LVL_RLT_TYPE, A.ITM_RLT_NAME, B.LVL_RLT_NAME, A.ITM_RLT_CODE, NULL, NULL"
			+ " FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_ITM_RLT_TYP A"
			+ " LEFT JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)+ ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE AND B.LANGUAGE = '"+ locale.getLanguage() + "'"
			+ " WHERE A.LANGUAGE = '"+ locale.getLanguage() + "'";
		
		switch (type){
		case 1:
			return sql_work;
		case 2:
			return sql_expression;
		case 3:
			return sql_work;
		case 4:
			return sql_expression;
		case 5:
			return sql_manifestation;
		case 6:
			return sql_person;
		case 7:
			return sql_family;
		case 8:
			return sql_corparate;
		case 10:
			return sql_item_relation;
		}
		throw new Exception("Query selection not found" + type);
	}

	private String getQueryParams(Integer type) throws Exception 
	{
		switch (type) {
		case 1:
			return "WRK";
		case 2:
			return "EXP";
		case 3:
			return "WRK";
		case 4:
			return "EXP";
		case 5:
			return "MNF";
		case 6:
			return "PRS";
		case 7:
			return "FML";
		case 8:
			return "CRB"; 
		case 10:
			return "ITM";
		}
		throw new Exception("Entity selection not found: " + type);
	}

	public List<Manifestation> getManifestationList(Integer amicusNumber,String query, String code) throws DataAccessException 
	{
		List<Manifestation> manifestationList = new ArrayList<Manifestation>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement(query);
			stmt.setString(1, code);
			stmt.setInt(2, amicusNumber);
			stmt.setInt(3, amicusNumber);
			rs = stmt.executeQuery();
			while (rs.next()) 
			{
				Integer aNumber = null;
				Integer bibItmNbrRel = rs.getInt("BIB_ITM_NBR_REL");
				Integer bibItmNbr = rs.getInt("BIB_ITM_NBR");

				if (bibItmNbr.equals(amicusNumber))
					aNumber = bibItmNbrRel;
				else
					aNumber = bibItmNbr;

				Manifestation manifestation = new Manifestation();
				manifestation.setAmicusNumber(aNumber);
				manifestation.setAuthority(rs.getInt("TYPE") == 0 ? true : false);
				manifestation.setTitle(getTitle(manifestation.getAmicusNumber()));
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

	public List<Expression> getExpressionBibAndAuth(Integer amicusNumber,String code) throws DataAccessException 
	{
		List<Expression> list = new ArrayList<Expression>();
		list.addAll(getExpressionList(amicusNumber, ConstantSqlRelation.SELECT_AUTHORITY_EXPRESSION, code));
		list.addAll(getExpressionList(amicusNumber, ConstantSqlRelation.SELECT_BIBLIOGRAPHIC_EXPRESSION, code));

		Collections.sort(list, new Comparator<Expression>() 
		{
			@Override
			public int compare(Expression o1, Expression o2) {
				return o1.getAmicusNumber().compareTo(o2.getAmicusNumber());
			}
		});
		return list;
	}

	public List<Manifestation> getManifestationBibAndAuth(Integer amicusNumber, String code) throws DataAccessException 
	{
		List<Manifestation> list = new ArrayList<Manifestation>();
		list.addAll(getManifestationList(amicusNumber,ConstantSqlRelation.SELECT_AUTHORITY_MANIFESTATION, code));
		list.addAll(getManifestationList(amicusNumber,ConstantSqlRelation.SELECT_BIBLIOGRAPHIC_MANIFESTATION, code));

		Collections.sort(list, new Comparator<Manifestation>() 
		{
			@Override
			public int compare(Manifestation o1, Manifestation o2) {
				return o1.getAmicusNumber().compareTo(o2.getAmicusNumber());
			}
		});
		return list;
	}

	public List<Work> getWorkBibAndAuth(Integer amicusNumber, String code) throws DataAccessException 
	{
		List<Work> list = new ArrayList<Work>();
		list.addAll(getWorkList(amicusNumber,ConstantSqlRelation.SELECT_AUTHORITY_WORK, code));
		list.addAll(getWorkList(amicusNumber,ConstantSqlRelation.SELECT_BIBLIOGRAPHIC_WORK, code));

		Collections.sort(list, new Comparator<Work>() 
		{
			@Override
			public int compare(Work o1, Work o2) {
				return o1.getAmicusNumber().compareTo(o2.getAmicusNumber());
			}
		});
		return list;
	}

	public List<Expression> getExpressionList(Integer amicusNumber, String query, String code) throws DataAccessException 
	{
		List<Expression> expressionList = new ArrayList<Expression>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement(query);
			stmt.setString(1, code);
			stmt.setInt(2, amicusNumber);
			stmt.setInt(3, amicusNumber);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Integer aNumber = null;
				Integer bibItmNbrRel = rs.getInt("BIB_ITM_NBR_REL");
				Integer bibItmNbr = rs.getInt("BIB_ITM_NBR");

				if (bibItmNbr.equals(amicusNumber))
					aNumber = bibItmNbrRel;
				else
					aNumber = bibItmNbr;

				Expression expression = new Expression();
				expression.setAmicusNumber(aNumber);
				expression.setAuthority(rs.getInt("TYPE") == 0 ? true : false);
				expression.setTitle(getTitle(expression.getAmicusNumber()));
				expressionList.add(expression);
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
		return expressionList;
	}

	public List<Work> getWorkList(Integer amicusNumber, String query, String code) throws DataAccessException 
	{
		List<Work> workList = new ArrayList<Work>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement(query);
			stmt.setString(1, code);
			stmt.setInt(2, amicusNumber);
			stmt.setInt(3, amicusNumber);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Integer aNumber = null;
				Integer bibItmNbrRel = rs.getInt("BIB_ITM_NBR_REL");
				Integer bibItmNbr = rs.getInt("BIB_ITM_NBR");

				if (bibItmNbr.equals(amicusNumber))
					aNumber = bibItmNbrRel;
				else
					aNumber = bibItmNbr;

				Work work = new Work(aNumber);
				work.setAuthority(rs.getInt("TYPE") == 0 ? true : false);
				work.setTitle(getTitle(work.getAmicusNumber()));
				workList.add(work);
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
		return workList;
	}

	public List<HeadingRel> getHeadRelation(Integer amicusNumber, String code) throws DataAccessException 
	{
		List<HeadingRel> headinRelList = new ArrayList<HeadingRel>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement(ConstantSqlRelation.SELECT_HEADING_RELATION);
			stmt.setString(1, code);
			stmt.setInt(2, amicusNumber);
			rs = stmt.executeQuery();
			while (rs.next()) {
				HeadingRel rel = new HeadingRel();
				rel.setAuthority(rs.getInt("TYPE") == 0 ? false : true);
				rel.setLabel(rs.getString("NAME"));
				rel.setHeadingNumber(rs.getInt("HDG_NBR"));
				rel.setAmicusNumberRel(rs.getInt("BIB_ITM_NBR"));
				rel.setCode(rs.getString("RLT_CODE"));
				headinRelList.add(rel);
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
		return headinRelList;
	}

	public List<Map<String, FrbrItem>> getSummaryRecordRelation(Integer amicusNumber, String code) throws DataAccessException 
	{			
		List<Map<String, FrbrItem>> recordRelList = new ArrayList<Map<String, FrbrItem>>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement(ConstantSqlRelation.SELECT_SUMMARY_RECORD_RELATION);
			stmt.setString(1, code);
			stmt.setInt(2, amicusNumber);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Integer aNumber = null;
				Integer bibItmNbrRel = rs.getInt("BIB_ITM_NBR_REL");
				Integer bibItmNbr = rs.getInt("BIB_ITM_NBR");
				boolean typeAuthority = rs.getInt("TYPE") == 0 ? true : false;
				boolean typeAuthorityRel = rs.getInt("TYPE_REL") == 0 ? true : false;
				boolean auth = false;
				if (bibItmNbr.equals(amicusNumber)){
					auth = typeAuthorityRel;
					aNumber = bibItmNbrRel;
				}
				else{
					auth = typeAuthority;
					aNumber = bibItmNbr;
				}
				Map<String, FrbrItem> map = new HashMap<String, FrbrItem>();
				FrbrItem item = new FrbrItem();
				String title = getTitle(aNumber,auth);
				String key = "";
				if (title != null) {
					key = title.length() > 25 ? title.substring(0, 24) : title;
					item.setTitle(title);
				}
				map.put(key, item);
				item.setAuthority(auth);
				item.setAmicusNumber(String.valueOf(aNumber));
				try{
					item.setLastDesignator(getLastDesignatorElement(rs.getInt("SUB_SUB_DESIGNATOR"),rs.getInt("SUB_DESIGNATOR"),rs.getInt("DESIGNATOR"),rs.getInt("RECIPROCAL")));
				}catch (Exception e) {
					item.setLastDesignator("");
				}
				recordRelList.add(map);
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
		return recordRelList;
	}
	
	/**
	 * Bug 4923: sistemati i campi passati nelle query per subDesignator e designator perche' erano errati e la query restituiva null
	 * @param subSubDesignator
	 * @param subDesignator
	 * @param designator
	 * @param isReciprocal
	 * @return
	 * @throws DataAccessException
	 */
	private String getLastDesignatorElement(Integer subSubDesignator,Integer subDesignator,Integer designator,Integer isReciprocal) throws DataAccessException
	{
		String result = "";
		
		if(subSubDesignator != null && subSubDesignator != 0) {
			return isReciprocal == 0 ? result = getSubSubDesignatorName(subSubDesignator) : getReciprocalSubSubDesignatorCode(getSubSubDesignatorName(subSubDesignator), locale);
		
		} else if(subDesignator != null && subDesignator != 0) {
			return isReciprocal == 0 ? result = getSubDesignatorName(subDesignator) : getReciprocalSubDesignatorCode(getSubDesignatorName(subDesignator), locale);
		
		} else if(designator != null && designator != 0) {
			return isReciprocal == 0 ? result = getDesignatorName(designator) : getReciprocalDesignatorCode(getDesignatorName(designator), locale);
		}
		return result;
	}
	
	private String getReciprocalDesignatorCode(String code, Locale locale) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		String result = null;
		if (code != null) {
			try {
				Session session = currentSession();
				connection = session.connection();
				stmt = connection.prepareStatement("SELECT * FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)
								+ ".F_RECIPROCAL_DESIGNATOR WHERE DESIGNATOR=? AND LANGUAGE='" + locale.getLanguage() + "'");
				stmt.setString(1, code);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					result = rs.getString("DESIGNATOR_REC");
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
		return result;
	}

	private String getReciprocalSubDesignatorCode(String code, Locale locale) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		String result = null;
		if (code != null) {
			try {
				Session session = currentSession();
				connection = session.connection();
				stmt = connection.prepareStatement("SELECT * FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)
								+ ".F_RECIPROCAL_SUB_DESIGNATOR WHERE SUB_DESIGNATOR=? AND LANGUAGE='" + locale.getLanguage() + "'");
				stmt.setString(1, code);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					result = rs.getString("SUB_DESIGNATOR_REC");
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
		return result;
	}

	private String getReciprocalSubSubDesignatorCode(String code, Locale locale) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		String result = null;
		if (code != null) {
			try {
				Session session = currentSession();
				connection = session.connection();
				stmt = connection
						.prepareStatement("SELECT * FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY)
								+ ".F_RECIPROCAL_SUB_SUB_DESIGN WHERE SUB_SUB_DESIGNATOR=? AND LANGUAGE='" + locale.getLanguage() + "'");
				stmt.setString(1, code);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					result = rs.getString("SUB_SUB_DESIGNATOR_REC");
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
		return result;
	}

	public List<Map<String, FrbrItem>> getSummaryHeadingRelation(Integer amicusNumber, String code) throws DataAccessException 
	{		
		List<Map<String, FrbrItem>> headinRelList = new ArrayList<Map<String, FrbrItem>>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement(ConstantSqlRelation.SELECT_SUMMARY_HEADING_RELATION);
			stmt.setString(1, code);
			stmt.setInt(2, amicusNumber);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Map<String, FrbrItem> map = new HashMap<String, FrbrItem>();
				FrbrItem item = new FrbrItem();
				item.setHdgNumber(rs.getInt("HDG_NBR"));
				item.setTitle(DAORelation.excludeDelimitatorSeparators(getHeadingName(item.getHdgNumber())));
				item.setAuthority(rs.getInt("TYPE") == 0 ? true : false);
				map.put(rs.getString("NAME"), item);
				headinRelList.add(map);
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
		return headinRelList;
	}

	public List<GenericEntity> viewRecordRelation(Integer amicusNumber, String code) throws DataAccessException 
	{
		List<GenericEntity> recordRelList = new ArrayList<GenericEntity>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement(ConstantSqlRelation.SELECT_SUMMARY_RECORD_RELATION);
			stmt.setString(1, code);
			stmt.setInt(2, amicusNumber);
			stmt.setInt(3, amicusNumber);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Integer aNumber = null;
				Integer bibItmNbrRel = rs.getInt("BIB_ITM_NBR_REL");
				Integer bibItmNbr = rs.getInt("BIB_ITM_NBR");
				if (bibItmNbr.equals(amicusNumber))
					aNumber = bibItmNbrRel;
				else
					aNumber = bibItmNbr;

				GenericEntity entity = new GenericEntity();
				entity.setId(aNumber);
				entity.setHeading(false);
				String title = getTitle(aNumber);
				if (title != null) {
					entity.setLabel(title);
				}
				recordRelList.add(entity);
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
		return recordRelList;
	}
		
	public List<GenericEntity> viewHeadingRelation(Integer amicusNumber, String code) throws DataAccessException 
	{
		List<GenericEntity> headinRelList = new ArrayList<GenericEntity>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement(ConstantSqlRelation.SELECT_SUMMARY_HEADING_RELATION);
			stmt.setString(1, code);
			stmt.setInt(2, amicusNumber);
			rs = stmt.executeQuery();
			while (rs.next()) {
				GenericEntity entity = new GenericEntity();
				entity.setId(rs.getInt("HDG_NBR"));
				entity.setLabel(getHeadingName(entity.getId()));
				entity.setHeading(true);
				headinRelList.add(entity);
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
		return headinRelList;
	}
	
	public List<GenericEntity> viewRelation(Integer amicusNumber, String code) throws DataAccessException
	{
		List<GenericEntity> result = new ArrayList<GenericEntity>();		
		List<GenericEntity> recordList = viewRecordRelation(amicusNumber, code);
		List<GenericEntity> headingList = viewHeadingRelation(amicusNumber, code);	
		result.addAll(recordList);
		result.addAll(headingList);
		return result;
	}

	public String getDesignatorName(Integer code) throws DataAccessException 
	{	
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = "";
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT DES_NAME FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) 
					+ ".F_DESIGNATORS WHERE ID=? AND LANGUAGE='" + locale.getLanguage() + "'");
			stmt.setInt(1, code);
			rs = stmt.executeQuery();
			if (rs.next()) {
				result = rs.getString("DES_NAME");
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

	public String getSubDesignatorName(Integer code) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = "";
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT SUB_DES_NAME FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) 
					+ ".F_SUB_DESIGNATORS WHERE ID=? AND LANGUAGE='" + locale.getLanguage() + "'");
			stmt.setInt(1, code);
			rs = stmt.executeQuery();
			if (rs.next()) {
				result = rs.getString("SUB_DES_NAME");
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

	public String getSubSubDesignatorName(Integer code) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = "";
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT SUB_SUB_DES_NAME FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) 
					+ ".F_SUB_SUB_DESIGNATORS WHERE ID=? AND LANGUAGE='" + locale.getLanguage() + "'");
			stmt.setInt(1, code);
			rs = stmt.executeQuery();
			if (rs.next()) {
				result = rs.getString("SUB_SUB_DES_NAME");
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

	public String[] getAuthorityCodeTable(Integer amicusNumber) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String[] result = null;
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT HDG_NBR,HDG_TYP_CDE FROM AUT WHERE AUT_NBR=?");
			stmt.setInt(1, amicusNumber);
			rs = stmt.executeQuery();
			if (rs.next()) {
				result = new String[]{rs.getString("HDG_TYP_CDE"),rs.getString("HDG_NBR")};
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
	
	private String getAuthorityTable(String code) 
	{
		String table = "";
		if(code.equals("TH")) {
			table = "TTL_HDG";
		}
			
		if(code.equals("NH")) {
			table = "NME_HDG";
		}
		if(code.equals("SH")) {
			table = "SBJCT_HDG";
		}
		return table;
	}
	
	public String getAuthorityLabel(Integer amicusNumber) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String label = null;
		String[] result = getAuthorityCodeTable(amicusNumber);
		if(result == null) return "";
		
		String table = getAuthorityTable(result[0]);
		Integer hdgNbr = Integer.parseInt(result[1]);
		String code = table.split("_")[0];
		
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT " + code +"_HDG_STRNG_TXT FROM " + table + " WHERE " + code + "_HDG_NBR=?");
			stmt.setInt(1, hdgNbr);
			rs = stmt.executeQuery();
			if (rs.next()) {
				label = DAORelation.excludeDelimitatorSeparators(rs.getString(code + "_HDG_STRNG_TXT"));
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
		return label;
	}
	
	public String getTitle(Integer amicusNumber,boolean authority) throws DataAccessException
	{
		String result = "";	
		if(authority == false)
			result = getTitle(amicusNumber);
		else result = getAuthorityLabel(amicusNumber);
		return result;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}