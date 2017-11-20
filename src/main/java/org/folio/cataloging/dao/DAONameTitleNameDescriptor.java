package org.folio.cataloging.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.UpdateStatus;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.dao.persistence.NME_TTL_HDG;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.descriptor.Descriptor;

/**
 * La classe e' stata modificata per far prendere in esame oltre al nome anche il titolo della hdg. 
 * Ho preso come spunto il DAOPublisherNameDescriptor che gia' faceva una cosa del genere 
 */
public class DAONameTitleNameDescriptor extends DAONameTitleDescriptor {

	@SuppressWarnings("unchecked")
	public List getHeadingsBySortform(String operator, String direction, String term, String filter, int cataloguingView, int count) throws DataAccessException 
	{
		String query="";
		Connection connection = null;
	    PreparedStatement stmt =null;
	    java.sql.ResultSet rs =null;
	    Session s = currentSession();
		String[] parsedTerm = term.split(" : ");
		List l = null;
		if (parsedTerm.length < 2) {
			return getSortformByOneSearchTerm(operator, direction, term, filter, cataloguingView, count, s, l);
		}
		else{
			return getSortformByTwoSearchTerms(operator, direction, filter, cataloguingView, count, query, connection, stmt, rs, s, parsedTerm, l);
		}		
	}

	private List getSortformByOneSearchTerm(String operator, String direction, String term, String filter, int searchingView, int count, Session s, List l) throws DataAccessException
	{		
		try {
			Query q = 
				s.createQuery(
					"select distinct hdg, nme.sortForm, ttl.sortForm from "
						+ "NME_TTL_HDG as hdg, "
						+ "NME_HDG as nme, "
						+ "TTL_HDG as ttl"
						+ " where hdg.nameHeadingNumber = nme.key.headingNumber "
						+ " and hdg.titleHeadingNumber = ttl.key.headingNumber "
						+ " and nme.sortForm " + operator + " :term "
						+ " and SUBSTR(hdg.key.userViewString, :view, 1) = '1' " + filter
						+ " order by nme.sortForm " + direction + ", ttl.sortForm " + direction);
			q.setString("term", term);
			q.setInteger("view", searchingView);
			q.setMaxResults(count);

			l = q.list();
			// get the NME_TTL_HDG object from the query
			for (int i = 0; i < l.size(); i++) {
				NME_TTL_HDG h = (NME_TTL_HDG)((Object[]) l.get(i))[0];
				h.setUpdateStatus(UpdateStatus.UNCHANGED);
				l.set(i, h);
			}
			l = isolateViewForList(l, searchingView);
			loadHeadings(l, searchingView);

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return l;
	}
	
	private List getSortformByTwoSearchTerms(String operator, String direction, String filter, int searchingView, int count, String query, Connection connection, PreparedStatement stmt, java.sql.ResultSet rs, Session s, String[] parsedTerm, List l) throws DataAccessException 
	{
		String name;
		String title;
		String operator2;
		/*    Query 1* elemento della First page: Se operatore = "<" : eseguo la query per Name <= e Title <    */	
	    name = parsedTerm[0].trim();
	    title = parsedTerm[1].trim();
	    String viewClause;
	    String viewClause2;
	    if (searchingView == View.ANY) {
	    	viewClause = "";
	    	viewClause2 = "";
	    } else {
	    	viewClause = " and SUBSTR(hdg.key.userViewString, " + searchingView + ", 1) = '1' ";
	    	viewClause2 = " and SUBSTR(USR_VW_IND, " + searchingView + ", 1) = '1' ";
	    }
	     
	    if(operator.equals("<")){
	    	try {				
	    		Query q = s.createQuery(
	    			  "Select distinct hdg, nme.sortForm, ttl.sortForm from "
					+ "NME_TTL_HDG as hdg, "
					+ "NME_HDG as nme, "
					+ "TTL_HDG as ttl"
					+ " where hdg.nameHeadingNumber = nme.key.headingNumber "
					+ " and hdg.titleHeadingNumber = ttl.key.headingNumber "
					+ " and (nme.sortForm " + operator + " :term "
					+ " or (nme.sortForm = :term "
					+ " and ttl.sortForm " + operator + " :term1)) "
					+ viewClause 
					+ filter
					+ " order by nme.sortForm " + direction + ", ttl.sortForm " + direction);
				q.setString("term", name);
				q.setString("term1", title);
	//			q.setInteger("view", cataloguingView);
				q.setMaxResults(count);		
				l = q.list();
				// get the NME_TTL_HDG object from the query
				for (int i = 0; i < l.size(); i++) {
					NME_TTL_HDG h = (NME_TTL_HDG)((Object[]) l.get(i))[0];
					h.setUpdateStatus(UpdateStatus.UNCHANGED);
					l.set(i, h);
				}
				l = isolateViewForList(l, searchingView);
				loadHeadings(l, searchingView);
			} catch (HibernateException e) {
				logAndWrap(e);
			}
			return l;
	    }else {
		    /* Query per elementi successivi e precedenti */
	    	if (operator.indexOf(">=")!=-1 ||operator.indexOf("<=")!=-1){
	    		if(operator.indexOf(">=")!=-1){
	    			operator2=">";
	    		} else if(operator.indexOf("<=")!=-1){
	    			operator2="<";
	    		} else {  
	    			operator2=operator;
	    		}
		  
		    //TODO use prepared statement below otherwise watch for quotes in name and place
	    	try {
			  connection = s.connection();
			  query = 
				"SELECT * FROM ( SELECT * FROM ( Select distinct z.*, NME_HDG.NME_HDG_SRT_FORM, TTL_HDG.TTL_HDG_SRT_FORM"
				+ " from NME_TTL_HDG z, NME_HDG, TTL_HDG"
				+ " where  z.NME_HDG_NBR = NME_HDG.NME_HDG_NBR"
				+ " and  z.TTL_HDG_NBR = TTL_HDG.TTL_HDG_NBR"
				+ " and NME_HDG.NME_HDG_SRT_FORM = '" + name  + "'" 
				+ " and TTL_HDG.TTL_HDG_SRT_FORM " + operator + "'" + title  + "'"
//				+ viewClause2
				+ " and SUBSTR(z.USR_VW_IND, " + searchingView + ", 1) = '1' "
				+ filter
				+ " UNION"
				+ " Select distinct  a.*, b.NME_HDG_SRT_FORM, c.TTL_HDG_SRT_FORM"
				+ " from NME_TTL_HDG a, NME_HDG b, TTL_HDG c"
				+ " where  a.NME_HDG_NBR = b.NME_HDG_NBR"
				+ " and a.TTL_HDG_NBR = c.TTL_HDG_NBR"
				+ " and b.NME_HDG_SRT_FORM" + operator2 + "'" + name  + "'"
//				+ viewClause2
				+ " and SUBSTR(a.USR_VW_IND, " + searchingView + ", 1) = '1' "
				+ filter
				+ " )"
				+ " order by NME_HDG_SRT_FORM " + direction + " , TTL_HDG_SRT_FORM " + direction
				+ " )"
				+ " where rownum <=" + count;

			  stmt = connection.prepareStatement(query);
			  rs = stmt.executeQuery();
			  l= new ArrayList();
			  while (rs.next()) {
				 NME_TTL_HDG  item = new NME_TTL_HDG();
				 item.setHeadingNumber(rs.getInt("NME_TTL_HDG_NBR"));
				 item.setUserViewString(rs.getString("USR_VW_IND"));
				 item.setNameHeadingNumber(rs.getInt("NME_HDG_NBR"));
				 item.setTitleHeadingNumber(rs.getInt("TTL_HDG_NBR"));
				 item.setAuthorityCount(rs.getShort("HDG_AUT_CNT"));
				 item.setUpdateStatus(UpdateStatus.UNCHANGED);
				 String charValueStr=rs.getString("CPY_TO_SBJCT_HDG_IND");  
				 if (charValueStr.length()>0){  
					 item.setCopyToSubjectIndicator(charValueStr.charAt(0));
				 }
				 charValueStr=rs.getString("VRFTN_LVL_CDE");
				 if (charValueStr.length()>0){
					 item.setVerificationLevel(charValueStr.charAt(0));	 
				 }
				 l.add(item);
			  }
			  
			  l = isolateViewForList(l, searchingView);
			  loadHeadings(l, searchingView);
			  
			} catch (HibernateException e) {logAndWrap(e);
			} catch (SQLException e1) {logAndWrap(e1);
			} 
			finally{
				try {
					if(stmt!=null)stmt.close();
					if(rs!=null)rs.close();
			     } catch (SQLException e) {logAndWrap(e);}
			}
	  	}
	  }
	  return l;
	}
	
	public String getBrowsingSortForm(Descriptor d)
	{
		if (!(d instanceof NME_TTL_HDG)) {
			logger.warn("I can only handle NME_TTL_HDG descriptors");
			throw new IllegalArgumentException();
		}
//		return ((NME_TTL_HDG) d).getNameHeading().getSortForm();
		return ((NME_TTL_HDG) d).getNameHeading().getSortForm() + " : " + ((NME_TTL_HDG) d).getTitleHeading().getSortForm();
	}
}