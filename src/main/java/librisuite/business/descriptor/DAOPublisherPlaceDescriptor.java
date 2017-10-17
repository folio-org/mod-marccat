/*
 * (c) LibriCore
 * 
 * Created on Dec 17, 2004
 * 
 * DAOPublisherPlaceDescriptor.java
 */
package librisuite.business.descriptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import librisuite.business.common.DataAccessException;
import librisuite.hibernate.DescriptorKey;
import librisuite.hibernate.PUBL_HDG;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

/**
 * This class implements the browse specific methods
 * special to publishers when Publisher place is being browsed
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/21 08:30:33 $
 * @since 1.0
 */
public class DAOPublisherPlaceDescriptor extends DAOPublisherDescriptor {
	
		
	public Class getPersistentClass() {
		return DAOPublisherPlaceDescriptor.persistentClass;
	}

	public List getHeadingsBySortform(String operator, String direction, String term, String filter, int cataloguingView, int count) throws DataAccessException {
		
		Session s = currentSession();
		String query="";
		List l = null;
		Connection connection = null;
	    PreparedStatement stmt =null;
	    java.sql.ResultSet rs =null;
	   	String[] parsedTerm = term.split(" : ");
	   	if (parsedTerm.length < 2) {
			return getSortformByOneSearchTerm(operator, direction, term, filter, cataloguingView, count, s, l);
		}
		else{
			return getSortformByTwoSearchTerms(operator, direction, filter, cataloguingView, count, s, query, l, connection, stmt, rs, parsedTerm);
		}
	}

	/**
	 * @param operator
	 * @param direction
	 * @param filter
	 * @param cataloguingView
	 * @param count
	 * @param s
	 * @param query
	 * @param l
	 * @param connection
	 * @param stmt
	 * @param rs
	 * @param parsedTerm
	 * @return
	 * @throws DataAccessException
	 */
	private List getSortformByTwoSearchTerms(String operator, String direction, String filter, int cataloguingView, int count, Session s, String query, List l, Connection connection, PreparedStatement stmt, java.sql.ResultSet rs, String[] parsedTerm) throws DataAccessException {
		String name;
		String place;
		String operator2;
		/*
		 * Se operatore = "<" : eseguo la query per Luogo Pubbl. <= 
		 * e Nome Editore < 
		 */
		
		place = parsedTerm[0].trim();
		name = parsedTerm[1].trim();
       if(operator.equals("<")){
		try {
			Query q =
				s.createQuery(
						"from "
						+ getPersistentClass().getName()
						+ " as hdg where hdg.nameSortForm "
						+ operator
						+ " :name  and "
						+" hdg.placeSortForm "
						+ (operator.equals("<")?"<=":operator)
						+ " :place  and "
						+ " SUBSTR(hdg.key.userViewString, :view, 1) = '1' "
						+ filter
						+ " order by hdg.placeSortForm "
						+ direction
						+ ", hdg.nameSortForm "
						+ direction);
			q.setString("place", place);
			q.setString("name", name);
			q.setInteger("view", cataloguingView);
			q.setMaxResults(count);

			l = q.list();
			l = isolateViewForList(l, cataloguingView);
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return l;

    }else{
		if(operator.indexOf(">=")!=-1 ||operator.indexOf("<=")!=-1){
		if(operator.indexOf(">=")!=-1)
		   operator2=">";
		else if(operator.indexOf("<=")!=-1)
		  operator2="<";
		else 
		  operator2=operator;
		
		try {
			connection = s.connection();
			query="Select * from (SELECT * FROM "+
			  "(select * "+
			  "from publ_hdg "+
			  "where PUBL_HDG_SRT_FRM_PLCE = '" 
			  +place+
			  "' AND PUBL_HDG_SRT_FRM_NME  "
			  +operator
			  +"'"
			  +name+
              //aggiunto
			  "' and substr(usr_vw_ind,"+cataloguingView+", 1) = '1' "
			  +" UNION "+
			  "select * "+
			  "from publ_hdg "+
			  "where PUBL_HDG_SRT_FRM_PLCE "
			  +operator2
			  +"'"
			  +place+
              //aggiunto
			  "' and substr(usr_vw_ind,"+cataloguingView+", 1) = '1' "+
			  ") ORDER BY PUBL_HDG_SRT_FRM_PLCE "
			  + direction+
			  ", PUBL_HDG_SRT_FRM_NME "
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
			  l = isolateViewForList(l, cataloguingView);
		  
		    } catch (HibernateException e) {
					logAndWrap(e);
					
			} catch (SQLException e1) {
					logAndWrap(e1);
			} 
		    finally{
		        try {
			     if(stmt!=null)stmt.close();
			     if(rs!=null)rs.close();
			    
		         } catch (SQLException e) {
			     	   logAndWrap(e);
		         }
			   }
		   }
		}
			return l;
	}

	/**
	 * @param operator
	 * @param direction
	 * @param term
	 * @param filter
	 * @param cataloguingView
	 * @param count
	 * @param s
	 * @param l
	 * @return
	 * @throws DataAccessException
	 */
	private List getSortformByOneSearchTerm(String operator, String direction, String term, String filter, int cataloguingView, int count, Session s, List l) throws DataAccessException {
		try {
			Query q =
				s.createQuery(
					"from "
						+ getPersistentClass().getName()
						+ " as hdg where hdg.placeSortForm "
						+ operator
						+ " :term  and "
						+ " SUBSTR(hdg.key.userViewString, :view, 1) = '1' "
						+ filter
						+ " order by hdg.placeSortForm "
						+ direction
						+ ", hdg.nameSortForm "
						+ direction);
			q.setString("term", term);
			q.setInteger("view", cataloguingView);
			q.setMaxResults(count);

			l = q.list();
			l = isolateViewForList(l, cataloguingView);
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return l;
	}

	public String getBrowsingSortForm(Descriptor d) {
		if (!(d instanceof PUBL_HDG)) {
			logger.warn("I can only handle PUBL_HDG descriptors");
			throw new IllegalArgumentException();
		}
		PUBL_HDG pub = (PUBL_HDG)d;
		return pub.getPlaceSortForm()+" : "+pub.getNameSortForm();
	}
	
	
  

}
