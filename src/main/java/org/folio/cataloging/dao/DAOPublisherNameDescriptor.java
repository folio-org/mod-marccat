/*
 * (c) LibriCore
 * 
 * Created on Dec 17, 2004
 * 
 * DAOPublisherNameDescriptor.java
 */
package org.folio.cataloging.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.dao.persistence.DescriptorKey;
import org.folio.cataloging.dao.persistence.PUBL_HDG;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.descriptor.Descriptor;

/**
 * This class implements the browse specific methods
 * special to publishers when Publisher name is being browsed
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/21 08:30:33 $
 * @since 1.0
 */
public class DAOPublisherNameDescriptor extends DAOPublisherDescriptor {
	//static protected Class persistentClass = PUBL_HDG_NAME_PLACE.class;
	
	public Class getPersistentClass() {
		return persistentClass;
	}

	public List getHeadingsBySortform(String operator, String direction, String term, String filter, int searchingView, int count) throws DataAccessException
	{
	   	String query="";
		Connection connection = null;
	    PreparedStatement stmt =null;
	    java.sql.ResultSet rs =null;
	    Session s = currentSession();
		String[] parsedTerm = term.split(" : ");
		List l = null;
		if (parsedTerm.length < 2) {
		   return getSortformByOneSearchTerm(operator, direction, term, filter, searchingView, count, s, l);
		}
		else{
			return getSortformByTwoSearchTerms(operator, direction, filter, searchingView, count, query, connection, stmt, rs, s, parsedTerm, l);
		}		
	}

	private List getSortformByTwoSearchTerms(String operator, String direction, String filter, int searchingView, int count, String query, Connection connection, PreparedStatement stmt, java.sql.ResultSet rs, Session s, String[] parsedTerm, List l) throws DataAccessException 
	{
		String name;
		String place;
		String operator2;
		/*
		 * Query 1* elemento della First page
		 * Se operatore = "<" : eseguo la query per Nome Editore <= 
		 *  e Luogo Pubbl. < 
		 */	
	     place = parsedTerm[0].trim();
	     name = parsedTerm[1].trim();
	     String viewClause;
	     String viewClause2;
	     if (searchingView == View.ANY) {
	    	 viewClause = "";
	    	 viewClause2 = "";
	     } else {
	    	 viewClause = " and SUBSTR(hdg.key.userViewString, " + searchingView + ", 1) = '1' ";
	    	 viewClause2 = " and SUBSTR(USR_VW_IND, " + searchingView + ", 1) = '1' ";
	     }
	     
	     if(operator.equals("<"))
	     {
		  try {
			Query q =
				s.createQuery(
					"from "
						+ getPersistentClass().getName()
						+ " as hdg where hdg.nameSortForm "
						+ (operator.equals("<")?"<=":operator)
						+ " :name  and "
						+" hdg.placeSortForm "
						+ operator
						+ " :place "
						+ viewClause
						+ filter
						+ " order by hdg.nameSortForm "
						+ direction
						+ ", hdg.placeSortForm "
						+ direction);
			q.setString("place", place);
			q.setString("name", name);
			q.setMaxResults(count);
	
			l = q.list();
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return l;
	
	  }else{
		/*
		 * Query per elementi successivi e precedenti
		 */	
	  if(operator.indexOf(">=")!=-1 ||operator.indexOf("<=")!=-1){
		  if(operator.indexOf(">=")!=-1)
			  operator2=">";
		  else if(operator.indexOf("<=")!=-1)
			  operator2="<";
		  else 
			  operator2=operator;
		  
		   //TODO use prepared statement below otherwise watch for quotes in name and place
		  try {
			connection = s.connection();
			query="Select * from (SELECT * FROM "+
			  "(select * "+
			  "from publ_hdg "+
			  "where PUBL_HDG_SRT_FRM_NME = '" 
			  +name+
			  "' AND PUBL_HDG_SRT_FRM_PLCE  "
			  +operator
			  +"'"
			  +place+ "'" +
			  //aggiunto
			  viewClause2
			  +" UNION "+
			  "select * "+
			  "from publ_hdg "+
			  "where PUBL_HDG_SRT_FRM_NME "
			  +operator2
			  +"'"
			  +name+ "'" +
		      //aggiunto
			  viewClause2
			  +") ORDER BY PUBL_HDG_SRT_FRM_NME "
			  + direction+
			  ", PUBL_HDG_SRT_FRM_PLCE "
			  + direction
			  +") where rownum <="+count;
			  //System.out.println(query);
			  stmt = connection.prepareStatement(query);
			  rs = stmt.executeQuery();
			  l= new ArrayList();
			  while (rs.next()) {
				 PUBL_HDG pu = new PUBL_HDG();
				 pu.setIndexingLanguage(rs.getShort("LANG_OF_IDXG_CDE"));
				 pu.setNameSortForm(rs.getString("PUBL_HDG_SRT_FRM_NME"));
				 pu.setPlaceSortForm(rs.getString("PUBL_HDG_SRT_FRM_PLCE"));
				 pu.setNameStringText(rs.getString("PUBL_HDG_STRNG_TXT_NME"));
				 pu.setPlaceStringText(rs.getString("PUBL_HDG_STRNG_TXT_PLCE"));
				 pu.setKey(new DescriptorKey(rs.getInt("PUBL_HDG_NBR"),rs.getString("USR_VW_IND")));
				 pu.markUnchanged();
				 l.add(pu);
			  }
		  
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

	private List getSortformByOneSearchTerm(String operator, String direction, String term, String filter, int searchingView, int count, Session s, List l) throws DataAccessException 
	{
		try {
			String viewClause;
			if (searchingView == View.ANY) {
				viewClause = "";
		}
			else {
				viewClause = " and SUBSTR(hdg.key.userViewString, " + searchingView + ", 1) = '1' ";
			}
				Query q =
					s.createQuery(
						"from "
							+ getPersistentClass().getName()
							+ " as hdg where hdg.nameSortForm "
							+ operator
							+ " :term  "
							+ viewClause
							+ filter
							+ " order by hdg.nameSortForm "
							+ direction
							+ ", hdg.placeSortForm "
							+ direction);
				q.setString("term", term);
				q.setMaxResults(count);
		
				l = q.list();
			} catch (HibernateException e) {
				logAndWrap(e);
			}
			return l;
	}

	public String getBrowsingSortForm(Descriptor d)
	{
		if (!(d instanceof PUBL_HDG)) {
			logger.warn("I can only handle PUBL_HDG descriptors");
			throw new IllegalArgumentException();
		}
		PUBL_HDG pub = (PUBL_HDG)d;
		return pub.getPlaceSortForm()+" : "+pub.getNameSortForm();
	}
}
