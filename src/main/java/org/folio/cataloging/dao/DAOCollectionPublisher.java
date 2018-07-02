package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.Global;
import org.folio.cataloging.business.PublisherListElement;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.persistence.CollectionPublisher;
import org.folio.cataloging.dao.persistence.T_CLCTN_PUBL_LVL_TYP;
import org.folio.cataloging.dao.persistence.T_CLCTN_PUBL_TYP;
import org.folio.cataloging.dao.persistence.T_STS_CLCTN_TYP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

public class DAOCollectionPublisher extends HibernateUtil 
{	
	public DAOCollectionPublisher() {
		super();
	}
	private final DAOCodeTable daoCodeTable = new DAOCodeTable();
	
	public void persistCollectionPublisher(CollectionPublisher collection) throws DataAccessException 
	{
		CollectionPublisher collection2;
		if (collection.getIdCollection()==0 || loadCollectionPublisher(collection.getIdCollection()).size() == 0) {
			persistByStatus(collection);

		} else {
			collection2 = (CollectionPublisher) loadCollectionPublisher(collection.getIdCollection()).get(0);
			collection2.markChanged();
			persistByStatus(collection2);
		}	
	}

	public List loadCollectionPublisher(int idCollection) throws DataAccessException 
	{
		List result = null;
	    result= find(" from CollectionPublisher as ct where ct.idCollection =" + idCollection + " order by ct.idCollection");
		return result;
		
	}

	public List loadCollectionPublisherByIdAndPublisher(int idCollection, String publisherCode) throws DataAccessException 
	{
		publisherCode = publisherCode.toLowerCase();
		List result = null;
	    result= find(" from CollectionPublisher as ct where ct.idCollection =" + idCollection + " and lower(ct.publCode) = '" + publisherCode + "' order by ct.idCollection");
		return result;
		
	}
	
	public int getIdCollectionMST(final Session session) throws DataAccessException
	{
		List l = null;
		int progress = 0;

		try {
			l = session.find("select max(a.idCollection) " + "from CollectionPublisher a");
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		if(l.get(0)!=null){
			progress = new Integer(l.get(0).toString()).intValue()+1;	
		}
		else progress= progress+1;
				
		return progress;
	}

	public List loadCollectionPublisher() throws DataAccessException
	{
		List result = null;
		result= find(" from CollectionPublisher as ct order by ct.idCollection");
		return result;
	}
	
	public List loadByDescription(String nameIta) throws DataAccessException 
	{
		List result = null;
        nameIta = nameIta.toLowerCase();
		String description="'"+nameIta+"%'";
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct "
					+ " from CollectionPublisher as ct, T_CLCTN_PUBL_TYP as mst " 
					+ " where ct.nameIta = mst.code and lower(mst.longText) like "
					+ description);
			result = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	public List loadByDescriptionAndPublisher(String nameIta, String publisherCode) throws DataAccessException 
	{
		List result = null;
        nameIta = nameIta.toLowerCase();
		String description="'"+nameIta+"%'";
		publisherCode = publisherCode.toLowerCase();
		
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct "
					+ " from CollectionPublisher as ct, T_CLCTN_PUBL_TYP as mst " 
					+ " where ct.nameIta = mst.code and lower(mst.longText) like "
					+ description
					+ " and lower(ct.publCode) = '" + publisherCode + "'");
			result = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	public List loadByLevelCode(String levelCode) throws DataAccessException 
	{
		List result = null;
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct from CollectionPublisher as ct, T_CLCTN_PUBL_LVL_TYP as lvl where ct.levelCode ='" + levelCode + "'");
			result = q.list();
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	public List loadByLevelCodeAndPublisher(String levelCode, String publisherCode) throws DataAccessException 
	{
		List result = null;
		publisherCode = publisherCode.toLowerCase();
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct from CollectionPublisher as ct, T_CLCTN_PUBL_LVL_TYP as lvl where ct.levelCode ='" + levelCode + "'" + " and lower(ct.publCode) = '" + publisherCode + "'");
			result = q.list();
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	public CollectionPublisher loadPUB(int idCollection)throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		java.sql.ResultSet rs = null;
		Session s = currentSession();
		String query = "";
		List l = null;
		CollectionPublisher co =null;
		try {
			connection = s.connection();
			query = "Select * from   " + System.getProperty(Global.SCHEMA_CUSTOMER_KEY) + ".T_CLCTN_PUBL where  " + System.getProperty(Global.SCHEMA_CUSTOMER_KEY) + ".T_CLCTN_PUBL.CLCTN_PUBL_CDE="	+ idCollection;
		    stmt = connection.prepareStatement(query);
			rs = stmt.executeQuery();
			l = new ArrayList();
			while (rs.next()) {
				co = new CollectionPublisher();
				co.setIdCollection(rs.getInt("CLCTN_PUBL_CDE"));
				co.setNameIta(rs.getInt("NME_PUBL_CDE"));
				co.setStatusCode(rs.getInt("STUS_CDE"));
				co.setDateCreation(rs.getDate("CRTN_DTE"));
				co.setDateCancel(rs.getDate("CNCL_DTE"));
				co.setLevelCode(rs.getInt("PUBL_LVL"));
				co.setPublCode(rs.getString("PUBL_CDE"));
				co.setYear(rs.getInt("YEAR"));
				co.markUnchanged();
				l.add(co);
			}
		} catch (HibernateException e) {
			logAndWrap(e);

		} catch (SQLException e1) {
			logAndWrap(e1);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();

			} catch (SQLException e) {
				logAndWrap(e);
			}
		}
		return co;
	}

	private PublisherListElement toPublisherListElement(final Session session,
							   						final CollectionPublisher source,
													final Locale locale) {

		PublisherListElement publisherListElement = new PublisherListElement(source);
		publisherListElement.setNameIta(daoCodeTable.getLongText(session, (short)source.getNameIta(),
																 T_CLCTN_PUBL_TYP.class, locale));
		publisherListElement.setStatusCode(daoCodeTable.getLongText(session, (short)source.getStatusCode(),
																 T_STS_CLCTN_TYP.class, locale));
		publisherListElement.setLevelCode(daoCodeTable.getLongText(session, (short)source.getLevelCode(),
				                                                 T_CLCTN_PUBL_LVL_TYP.class, locale));
		publisherListElement.setHierarchy(loadHRCY(session, source.getIdCollection()).size() > 0);
		publisherListElement.setCountPub(countCollectionFromRecordUses(source.getIdCollection()));
		return publisherListElement;
	}


    public List orderPubCollection(final Session session,
								   final String tableColumn,
								   final String orderType,
								   final Locale locale) throws DataAccessException
	{
		List<CollectionPublisher> result;
		List<PublisherListElement> result2 = new ArrayList();
		String orderByString= " order by ct."+ tableColumn+" "+orderType;

		try {

			Query q = session.createQuery("select distinct ct " + " from CollectionPublisher as ct " +orderByString);
			result = q.list();

			result2 = result.stream()
						.map(n -> toPublisherListElement(session, n, locale))
						.collect(toList());

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result2;
	}

	//TODO: check commented methods if needs
	/*public List getListPublishersElement(Locale locale) throws DataAccessException
	{
		List result = new ArrayList();
		List listAllCollection = loadCollectionPublisher();
		Iterator iter = listAllCollection.iterator();
		while (iter.hasNext()) {
			CollectionPublisher rawPublisher = (CollectionPublisher) iter.next();
			PublisherListElement rawPublisherListElement = new PublisherListElement(rawPublisher);
			rawPublisherListElement.setIdCollection(rawPublisher.getIdCollection());
			rawPublisherListElement.setNameIta(LibrisuiteAction.getDaoCodeTable().getLongText((short)rawPublisher.getNameIta(),T_CLCTN_PUBL_TYP.class,locale));
			rawPublisherListElement.setPublCode(rawPublisher.getPublCode());
			rawPublisherListElement.setLevelCode(LibrisuiteAction.getDaoCodeTable().getLongText((short)rawPublisher.getLevelCode(),T_CLCTN_PUBL_LVL_TYP.class,locale));
			rawPublisherListElement.setStatusCode(LibrisuiteAction.getDaoCodeTable().getLongText((short)rawPublisher.getStatusCode(),T_STS_CLCTN_TYP.class,locale));
			if(hasHierarchy(rawPublisher.getIdCollection()).size()>0)
				rawPublisherListElement.setHierarchy(true);
			else
				rawPublisherListElement.setHierarchy(false);
			rawPublisherListElement.setCountPub(countCollectionFromRecordUses(rawPublisher.getIdCollection()));
			result.add(rawPublisherListElement);
		}
		return result;
	}*/
	
	/*public List getListPublishersElementById(int idCollection,String publisherCode, Locale locale) throws DataAccessException
	{
		List result = new ArrayList();
		List result2 = new ArrayList();
		if (publisherCode!=null && publisherCode.trim().length()>0 ) {
			result = loadCollectionPublisherByIdAndPublisher(idCollection, publisherCode);
		}else {
			result = loadCollectionPublisher(idCollection);	
		}
		Iterator iter = result.iterator();
		while (iter.hasNext()) {
			CollectionPublisher rawMaster = (CollectionPublisher) iter.next();
			PublisherListElement rawMasterListElement = new PublisherListElement(rawMaster);
			rawMasterListElement.setIdCollection(rawMaster.getIdCollection());
			rawMasterListElement.setNameIta(LibrisuiteAction.getDaoCodeTable().getLongText((short)rawMaster.getNameIta(),T_CLCTN_PUBL_TYP.class,locale));
			rawMasterListElement.setPublCode(rawMaster.getPublCode());
			rawMasterListElement.setLevelCode(LibrisuiteAction.getDaoCodeTable().getLongText((short)rawMaster.getLevelCode(),T_CLCTN_PUBL_LVL_TYP.class,locale));
			rawMasterListElement.setStatusCode(LibrisuiteAction.getDaoCodeTable().getLongText((short)rawMaster.getStatusCode(),T_STS_CLCTN_TYP.class,locale));
			if(hasHierarchy(rawMaster.getIdCollection()).size()>0)
				rawMasterListElement.setHierarchy(true);
			else
				rawMasterListElement.setHierarchy(false);
			rawMasterListElement.setCountPub(countCollectionFromRecordUses(rawMaster.getIdCollection()));
			result2.add(rawMasterListElement);
		}

		return result2;
	}*/
	
	/*public List getListPublishersElementByDescription(String nameIta, String publisherCode, Locale locale) throws DataAccessException
	{
		List result = new ArrayList();
		List result2 = new ArrayList();
		if (publisherCode!=null && publisherCode.trim().length()>0 ) {
			result = loadByDescriptionAndPublisher(nameIta, publisherCode);
		}else {
			result = loadByDescription(nameIta);	
		}
		
		Iterator iter = result.iterator();
		while (iter.hasNext()) {
			CollectionPublisher rawMaster = (CollectionPublisher) iter.next();
			PublisherListElement rawMasterListElement = new PublisherListElement(rawMaster);
			rawMasterListElement.setIdCollection(rawMaster.getIdCollection());
			rawMasterListElement.setNameIta(LibrisuiteAction.getDaoCodeTable().getLongText((short)rawMaster.getNameIta(),T_CLCTN_PUBL_TYP.class,locale));
			rawMasterListElement.setPublCode(rawMaster.getPublCode());
			rawMasterListElement.setLevelCode(LibrisuiteAction.getDaoCodeTable().getLongText((short)rawMaster.getLevelCode(),T_CLCTN_PUBL_LVL_TYP.class,locale));
			rawMasterListElement.setStatusCode(LibrisuiteAction.getDaoCodeTable().getLongText((short)rawMaster.getStatusCode(),T_STS_CLCTN_TYP.class,locale));
			if(hasHierarchy(rawMaster.getIdCollection()).size()>0)
				rawMasterListElement.setHierarchy(true);
			else
				rawMasterListElement.setHierarchy(false);
			rawMasterListElement.setCountPub(countCollectionFromRecordUses(rawMaster.getIdCollection()));
			result2.add(rawMasterListElement);
		}

		return result2;
	}*/
	
	/*public List getListPublishersElementByLevelCode(String levelCode, String publisherCode, Locale locale) throws DataAccessException
	{
		List result = new ArrayList();
		List result2 = new ArrayList();
		
		if (publisherCode!=null && publisherCode.trim().length()>0 ) {
			result = loadByLevelCodeAndPublisher(levelCode, publisherCode);
		}else {
			result = loadByLevelCode(levelCode);	
		}
		
		Iterator iter = result.iterator();
		while (iter.hasNext()) {
			CollectionPublisher rawPublisher = (CollectionPublisher) iter.next();
			PublisherListElement rawMasterListElement = new PublisherListElement(rawPublisher);
			rawMasterListElement.setIdCollection(rawPublisher.getIdCollection());
			rawMasterListElement.setNameIta(LibrisuiteAction.getDaoCodeTable().getLongText((short)rawPublisher.getNameIta(),T_CLCTN_PUBL_TYP.class,locale));
			rawMasterListElement.setPublCode(rawPublisher.getPublCode());
			rawMasterListElement.setLevelCode(LibrisuiteAction.getDaoCodeTable().getLongText((short)rawPublisher.getLevelCode(),T_CLCTN_PUBL_LVL_TYP.class,locale));
			rawMasterListElement.setStatusCode(LibrisuiteAction.getDaoCodeTable().getLongText((short)rawPublisher.getStatusCode(),T_STS_CLCTN_TYP.class,locale));
			if(hasHierarchy(rawPublisher.getIdCollection()).size()>0){
				rawMasterListElement.setHierarchy(true);
			}else{
				rawMasterListElement.setHierarchy(false);
			}
			rawMasterListElement.setCountPub(countCollectionFromRecordUses(rawPublisher.getIdCollection()));
			result2.add(rawMasterListElement);
		}

		return result2;
	}*/
	
	/*public List loadPublisherByName(int nameIta) throws DataAccessException
	{
		List result = null;
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct from CollectionPublisher as ct where ct.nameIta =" + nameIta);
			q.setMaxResults(1);
			result = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}*/
	
	/*public void updateObj (CollectionPublisher item) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		Session session = currentSession();
		String query = "";
		
		try {			
			connection = session.connection();
			
			query = "update T_CLCTN_PUBL i set i.NME_PUBL_CDE = ?" + 
				    ",i.STUS_CDE = ? " +
				    ",i.CRTN_DTE = ? " +
				    ",i.CNCL_DTE = ? " +
				    ",i.PUBL_LVL = ? " +
				    ",i.MDFY_DTE = ? " +
				    ",i.USR_MDFY = ? " +
				    ",i.PUBL_CDE = ? " +
				    ",i.YEAR = ? " +
					" where i.CLCTN_PUBL_CDE = ? ";
					
			Date dataSqlCreation = null;
			Date dataSqlCancel = null;
			Date dataSqlModify = null;
			if (item.getDateCreation()!=null)
				dataSqlCreation = new Date(item.getDateCreation().getTime());
			if (item.getDateCancel()!=null)
				dataSqlCancel = new Date(item.getDateCancel().getTime());
			if (item.getDateModify()!=null)
				dataSqlModify = new Date(item.getDateModify().getTime());	
			if (item.getDateCreation()!=null)stmt = connection.prepareStatement(query);
		    stmt.setInt(1, item.getNameIta());
		    stmt.setInt(2, item.getStatusCode());
		    stmt.setDate(3, dataSqlCreation);
		    stmt.setDate(4, dataSqlCancel);
		    stmt.setInt(5, item.getLevelCode());
		    stmt.setDate(6, dataSqlModify);
		    stmt.setString(7, item.getUserModify());
		    stmt.setString(8, item.getPublCode());
		    stmt.setInt(9, item.getYear());
		    stmt.setInt(10, item.getIdCollection());
		   
			int row = stmt.executeUpdate();
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e1) {
			logAndWrap(e1);
		} finally {
			try {
				connection.commit();
				if (stmt != null)
					stmt.close();

			} catch (SQLException e) {
				logAndWrap(e);
			}
		}
	}*/
	
	/*public void deleteObj (int idCollection, int nameIta) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		Session session = currentSession();
		String query = "";
		
		try {			
			connection = session.connection();
			query = "delete from T_CLCTN_PUBL i where i.CLCTN_PUBL_CDE = ? ";		
			stmt = connection.prepareStatement(query);
		    stmt.setInt(1, idCollection);
			stmt.execute();
			
            query = "delete from T_CLCTN_PUBL_TYP i where i.TBL_VLU_CDE = ? ";		
			stmt = connection.prepareStatement(query);
		    stmt.setInt(1, nameIta);
			stmt.execute();         
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e1) {
			logAndWrap(e1);
		} finally {
			try {
				connection.commit();
				if (stmt != null)
					stmt.close();

			} catch (SQLException e) {
				logAndWrap(e);
			}
		}
	}*/
	
	
	public List loadHRCY(Session session, int collectionCode) throws DataAccessException {
		List result = null;
		try {
			Query q = session.createQuery("select distinct ct from CLCTN_PUBL_HRCY as ct where ct.collectionCode = " + collectionCode);
			q.setMaxResults(1);
			result = q.list();
		} catch (HibernateException e) {
			//TODO dont'use it!
			logAndWrap(e);
		}
		return result;
	}
	
	public int countCollectionFromRecordUses(int idCollection) throws DataAccessException 
	{
		List l = find("select count(*) from CLCTN_PUBL_ACS_PNT apf, CollectionPublisher ct " + " where ct.idCollection = apf.collectionNumber" + " and apf.collectionNumber = ?", new Object[] { new Integer(
				idCollection) }, new Type[] { Hibernate.INTEGER });
		if (l.size() > 0) {
			return ((Integer) l.get(0)).intValue();
		} else {
			return 0;
		}
	}
	
	/*public List getListPublishersElementByItem(int itemNumber, Locale locale) throws DataAccessException
	{
		List result = new ArrayList();
		List listAllCollection = getCollectionsByBibItem(itemNumber);
		Iterator iter = listAllCollection.iterator();
		while (iter.hasNext()) {
			CollectionPublisher rawPublisher = (CollectionPublisher) iter.next();
			PublisherListElement rawPublisherListElement = new PublisherListElement(rawPublisher);
			rawPublisherListElement.setIdCollection(rawPublisher.getIdCollection());
			rawPublisherListElement.setNameIta(LibrisuiteAction.getDaoCodeTable().getLongText((short)rawPublisher.getNameIta(),T_CLCTN_PUBL_TYP.class,locale));
			rawPublisherListElement.setLevelCode(LibrisuiteAction.getDaoCodeTable().getLongText((short)rawPublisher.getLevelCode(),T_CLCTN_PUBL_LVL_TYP.class,locale));
			rawPublisherListElement.setPublCode(rawPublisher.getPublCode());
			rawPublisherListElement.setStatusCode(LibrisuiteAction.getDaoCodeTable().getLongText((short)rawPublisher.getStatusCode(),T_STS_CLCTN_TYP.class,locale));
			if(hasHierarchy(rawPublisher.getIdCollection()).size()>0)
				rawPublisherListElement.setHierarchy(true);
			else
				rawPublisherListElement.setHierarchy(false);
			rawPublisherListElement.setCountPub(countCollectionFromRecordUses(rawPublisher.getIdCollection()));
//-------->	 20101014 inizio: devo memorizzare la data di associazione del record 
			rawPublisherListElement.setDateAssociatedRecord(getRecordAssociatedDate(itemNumber, rawPublisher.getIdCollection()));
//-------->	 20101014 fine
			result.add(rawPublisherListElement);
		}
		return result;
	}*/
	
//	 20101014 inizio
	/*public Date getRecordAssociatedDate(int itemNumber, int idCollection) throws DataAccessException
	{
		Date dateCreation = null; 
		try {
			Session s = currentSession();
			Query q = s.createQuery("Select ct.creationDate from CLCTN_PUBL_ACS_PNT ct where ct.collectionNumber = " + idCollection 
					+ " and ct.bibItemNumber = " + itemNumber);
			q.setMaxResults(1);
			dateCreation = new Date(((Timestamp) q.list().get(0)).getTime());

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return dateCreation;
	}*/

	/*public List getCollectionsByBibItem(int itemNumber) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		java.sql.SearchResponse rs = null;
		Session s = currentSession();
		String query = "";
		List l = null;

		try {
			connection = s.connection();
			query = "Select distinct ct.* from   " + System.getProperty(Global.SCHEMA_CUSTOMER_KEY) + ".CLCTN_PUBL_ACS_PNT apf,  " + System.getProperty(Global.SCHEMA_CUSTOMER_KEY) + ".T_CLCTN_PUBL ct where apf.BIB_ITM_NBR ="+itemNumber
			       +"  and ct.CLCTN_PUBL_CDE = apf.CLCTN_PUBL_CDE";
		    stmt = connection.prepareStatement(query);
			rs = stmt.executeQuery();
			l = new ArrayList();
			while (rs.next()) {
				CollectionPublisher co = new CollectionPublisher();
				co.setIdCollection(rs.getInt("CLCTN_PUBL_CDE"));
				co.setNameIta(rs.getInt("NME_PUBL_CDE"));
				co.setStatusCode(rs.getInt("STUS_CDE"));
				co.setDateCreation(rs.getDate("CRTN_DTE"));
				co.setDateCancel(rs.getDate("CNCL_DTE"));
				co.setPublCode(rs.getString("PUBL_CDE"));
				co.setLevelCode(rs.getInt("PUBL_LVL"));
				co.setYear(rs.getInt("YEAR"));
				co.markUnchanged();
				l.add(co);
			}
		} catch (HibernateException e) {
			logAndWrap(e);

		} catch (SQLException e1) {
			logAndWrap(e1);
		} finally {
			try {
				if (rs != null)	rs.close();
				if (stmt != null)stmt.close();
			} catch (SQLException e) {
				logAndWrap(e);
			}
		}
		return l;
	}*/
	
}