package org.folio.cataloging.dao;

import net.sf.hibernate.*;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.*;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.persistence.CollectionCustomer;
import org.folio.cataloging.dao.persistence.CollectionCustomerArch;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DAOCollectionCustom extends AbstractDAO
{
	private static Log logger = LogFactory.getLog(DAOCollectionCustom.class);
	
	private static final String SORT_NAME_ITA_CODE = "nameIta";
	private static final String SORT_TYPOLOGY_CODE = "typologyCode";
	private static final String SORT_STATUS_CODE   = "statusCode";
	private static final String SORT_ASC_CODE      = "ASC";
	
	public DAOCollectionCustom() {
		super();
	}

	//TODO: refactoring all methods
	public void persistCollectionCustom( CollectionCustomer collection) throws DataAccessException 
	{
		CollectionCustomer collection2;
		if (collection.getIdCollection().intValue()==0 || loadCollectionCustom(collection.getIdCollection().intValue()).size() == 0) {
			persistByStatus(collection);

		} else {
			collection2 = (CollectionCustomer) loadCollectionCustom(collection.getIdCollection().intValue()).get(0);
			collection2.markChanged();
			persistByStatus(collection2);
		}
		
	}

	public List loadCollectionCustom(int idCollection) throws DataAccessException 
	{
		List result = null;
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct "
					+ " from CollectionCustomer as ct " 
					+ " where ct.idCollection = "
					+ idCollection
					+ " order by ct.idCollection");
			q.setMaxResults(1);
			result = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	public CollectionCustomer loadCST(int idCollection)throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		java.sql.ResultSet rs = null;
		Session s = currentSession();
		String query = "";
		List l = null;
		CollectionCustomer co =null;
		try {
			connection = s.connection();
			query = "Select * from  custom.T_CLCTN_CST where custom.T_CLCTN_CST.CLCTN_CST_CDE="	
					+ idCollection;
		    stmt = connection.prepareStatement(query);
			rs = stmt.executeQuery();
			l = new ArrayList();
			while (rs.next()) {
				co = new CollectionCustomer();
				co.setIdCollection(new Integer(rs.getInt("CLCTN_CST_CDE")));
				co.setNameIta(rs.getInt("NME_CST_CDE"));
				co.setStatusCode(new Integer(rs.getInt("STUS_CDE")));
				co.setDateCreation(rs.getDate("CRTN_DTE"));
				co.setDateCancel(rs.getDate("CNCL_DTE"));
				co.setTypologyCode(rs.getString("TPLG_CDE"));
				co.setUpgrade(rs.getString("UPGRD"));
				co.setIdCollectionMST(new Integer(rs.getInt("CLCTN_MST_CDE")));
				co.setYear(rs.getInt("YEAR"));
//------------>	20110131 inizio:
				co.setDateIniVal(rs.getDate("DT_INI_VAL"));
				co.setDateFinVal(rs.getDate("DT_FIN_VAL"));
				co.setDateType(rs.getString("DT_TYPE"));
//------------>	20110131 fine
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
	
	public int getIdCollectionCST() throws DataAccessException 
	{
		List l = null;
		int progress = 0;
		Session s = currentSession();

		try {
			l = s.find("select max(a.idCollection) from CollectionCustomer a");
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		if(l.get(0)!=null){
		 	progress = new Integer(l.get(0).toString()).intValue()+1;	
		}
		else progress= progress+1;
				
		return progress;
	}
	

	public List loadCollectionCustom() throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		java.sql.ResultSet rs = null;
		Session s = currentSession();
		String query = "";
		List l = null;

		try {
			connection = s.connection();
			query = "Select * from  custom.T_CLCTN_CST order by CLCTN_CST_CDE";
		    stmt = connection.prepareStatement(query);
			rs = stmt.executeQuery();
			l = new ArrayList();
			while (rs.next()) {
				CollectionCustomer co = new CollectionCustomer();
				co.setIdCollection(new Integer(rs.getInt("CLCTN_CST_CDE")));
				co.setNameIta(rs.getInt("NME_CST_CDE"));
				co.setStatusCode(new Integer(rs.getInt("STUS_CDE")));
				co.setDateCreation(rs.getDate("CRTN_DTE"));
				co.setDateCancel(rs.getDate("CNCL_DTE"));
				co.setTypologyCode(rs.getString("TPLG_CDE"));
				co.setCustomerId(rs.getString("CSTMR_CDE"));
				co.setUpgrade(rs.getString("UPGRD"));
				co.setIdCollectionMST(new Integer(rs.getInt("CLCTN_MST_CDE")));
				co.setYear(rs.getInt("YEAR"));
//------------>	 20110131 inizio:
				co.setDateIniVal(rs.getDate("DT_INI_VAL"));
				co.setDateFinVal(rs.getDate("DT_FIN_VAL"));
				co.setDateType(rs.getString("DT_TYPE"));
//------------>	 20110131 fine
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
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				logAndWrap(e);
			}
		}

		return l;
	}

	public CollectionCustomer loadCollectionCST(int idCollection)throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		java.sql.ResultSet rs = null;
		Session s = currentSession();
		String query = "";
		List l = null;
		CollectionCustomer co =null;
		try {
			connection = s.connection();
			query = "Select * from  custom.T_CLCTN_CST where custom.T_CLCTN_CST.CLCTN_MST_CDE="	
					+ idCollection;
		    stmt = connection.prepareStatement(query);
			rs = stmt.executeQuery();
			l = new ArrayList();
			while (rs.next()) {
				co = new CollectionCustomer();
				co.setIdCollection(new Integer(rs.getInt("CLCTN_CST_CDE")));
				co.setNameIta(rs.getInt("NME_CST_CDE"));
				co.setStatusCode(new Integer(rs.getInt("STUS_CDE")));
				co.setDateCreation(rs.getDate("CRTN_DTE"));
				co.setDateCancel(rs.getDate("CNCL_DTE"));
				co.setTypologyCode(rs.getString("TPLG_CDE"));
				co.setYear(rs.getInt("YEAR"));
//------------>	 20110131 inizio:
				co.setDateIniVal(rs.getDate("DT_INI_VAL"));
				co.setDateFinVal(rs.getDate("DT_FIN_VAL"));
				co.setDateType(rs.getString("DT_TYPE"));
//------------>	 20110131 fine
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
				if (stmt != null) stmt.close();

			} catch (SQLException e) {
				logAndWrap(e);
			}
		}

		return co;
	}

	public List loadByDescription(String nameIta) throws DataAccessException 
	{
		List result = null;
		nameIta = nameIta.toLowerCase();
		String description="'"+nameIta+"%'";
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct "
					+ " from CollectionCustomer as ct, T_CLCTN_CST_TYP as cst " 
					+ " where ct.nameIta=cst.code and lower(cst.longText) like "
					+ description);
			result = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	private String workQuery(String typologyCode, String searchNome, String searchId, String colonna, String status) 
	{
		StringBuffer buffer = new StringBuffer();
		String query = "select distinct ct from CollectionCustomer as ct ";
		String sort = " order by ct." + colonna + " " + status;
		
		if (typologyCode!=null && !"0  ".equals(typologyCode)){
			buffer.append("ct.typologyCode ='" + typologyCode+"'");
		}else if (searchNome!=null && searchNome.trim().length()>0) {
			query = query + ", T_CLCTN_CST_TYP as cst";
			buffer.append(" ct.nameIta = cst.code and lower(cst.longText) like '" + searchNome.toLowerCase() + "%'");
		}else if (searchId!=null && searchId.trim().length()>0) {
//			buffer.append("ct.T_CLCTN_CST.CLCTN_CST_CDE =" + searchId);
			buffer.append("ct.idCollection =" + searchId);
		}
		
		if (buffer.toString().trim().length()>0){
			buffer.insert(0, query + " where ");
		}else {
			buffer.insert(0, query);
		}
		
		buffer.append(sort);
		
		logger.debug("Collections custom --> Query eseguita per l'ordinamento : " + buffer.toString());
		
		return buffer.toString();
	}
	
//	 20110203 inizio: ordinamento lista
	/**
	 * Il metodo ordina la lista delle collection custom tenendo conto 
	 * non dei codici ma delle DESCRIZIONI di Status, Typology e Nome collection
	 */
	public List sortCustomList(String orderType, String orderField, List result2) 
	{
		logger.debug("Ordinamento richiesto : " + orderType);
		logger.debug("Campo di ordinamento  : " + orderField);
		
		if (SORT_ASC_CODE.equalsIgnoreCase(orderType)){
			if (SORT_NAME_ITA_CODE.equalsIgnoreCase(orderField)){
				Collections.sort(result2, new ComparatorCustomNameAsc());
			}else if (SORT_TYPOLOGY_CODE.equalsIgnoreCase(orderField)){
				Collections.sort(result2, new ComparatorCustomTypologyAsc());
			}else if (SORT_STATUS_CODE.equalsIgnoreCase(orderField)){
				Collections.sort(result2, new ComparatorCustomStatusAsc());
			}
		}else {
			if (SORT_NAME_ITA_CODE.equalsIgnoreCase(orderField)){
				Collections.sort(result2, new ComparatorCustomNameDesc());
			}else if (SORT_TYPOLOGY_CODE.equalsIgnoreCase(orderField)){
				Collections.sort(result2, new ComparatorCustomTypologyDesc());
			}else if (SORT_STATUS_CODE.equalsIgnoreCase(orderField)){
				Collections.sort(result2, new ComparatorCustomStatusDesc());
			}
		}
		return result2;
	}
//	 20110203 fine
	
	public List getListCustomsElement(Locale locale) throws DataAccessException 
	{
		List result = new ArrayList();
		List listAllCollection = loadCollectionCustom();
		Iterator iter = listAllCollection.iterator();
		while (iter.hasNext()) {
			CollectionCustomer rawCustom = (CollectionCustomer) iter.next();
			CustomListElement rawCustomListElement = new CustomListElement(rawCustom);
			/*rawCustomListElement.setIdCollection(rawCustom.getIdCollection().intValue());
			rawCustomListElement.setNameIta(LibrisuiteAction.getDaoCodeTable().getLongText((short)rawCustom.getNameIta(),T_CLCTN_CST_TYP.class,locale));
			rawCustomListElement.setTypologyCode(LibrisuiteAction.getDaoCodeTable().getLongText(rawCustom.getTypologyCode(),T_CLCTN_TYP.class,locale));
			rawCustomListElement.setStatusCode(LibrisuiteAction.getDaoCodeTable().getLongText(rawCustom.getStatusCode().shortValue(),T_STS_CLCTN_TYP.class,locale));
			rawCustomListElement.setCountCst(countCollectionFromRecordUses(rawCustom.getIdCollection().intValue()));*/
			result.add(rawCustomListElement);
		}
		return result;
	}
	
	public List getListCustomsElementById(int idCollection,Locale locale) throws DataAccessException 
	{
		List result = new ArrayList();
		List result2 = new ArrayList();
		result = loadCollectionCustom(idCollection);
		Iterator iter = result.iterator();
		while (iter.hasNext()) {
			CollectionCustomer rawCustom = (CollectionCustomer) iter.next();
			CustomListElement rawCustomListElement = new CustomListElement(rawCustom);
			/*rawCustomListElement.setIdCollection(rawCustom.getIdCollection().intValue());
			rawCustomListElement.setNameIta(LibrisuiteAction.getDaoCodeTable().getLongText((short)rawCustom.getNameIta(),T_CLCTN_CST_TYP.class,locale));
			rawCustomListElement.setTypologyCode(LibrisuiteAction.getDaoCodeTable().getLongText(rawCustom.getTypologyCode(),T_CLCTN_TYP.class,locale));
			rawCustomListElement.setStatusCode(LibrisuiteAction.getDaoCodeTable().getLongText(rawCustom.getStatusCode().shortValue(),T_STS_CLCTN_TYP.class,locale));
			rawCustomListElement.setCountCst(countCollectionFromRecordUses(rawCustom.getIdCollection().intValue()));*/
			result2.add(rawCustomListElement);
		}
		return result2;
	}
	
	public List getListCustomsElementByDescription(String nameIta,Locale locale) throws DataAccessException 
	{
		List result = new ArrayList();
		List result2 = new ArrayList();
		result = loadByDescription(nameIta);
		Iterator iter = result.iterator();
		while (iter.hasNext()) {
			CollectionCustomer rawCustom = (CollectionCustomer) iter.next();
			CustomListElement rawCustomListElement = new CustomListElement(rawCustom);
			/*rawCustomListElement.setIdCollection(rawCustom.getIdCollection().intValue());
			rawCustomListElement.setNameIta(LibrisuiteAction.getDaoCodeTable().getLongText((short)rawCustom.getNameIta(),T_CLCTN_CST_TYP.class,locale));
			rawCustomListElement.setTypologyCode(LibrisuiteAction.getDaoCodeTable().getLongText(rawCustom.getTypologyCode(),T_CLCTN_TYP.class,locale));
			rawCustomListElement.setStatusCode(LibrisuiteAction.getDaoCodeTable().getLongText(rawCustom.getStatusCode().shortValue(),T_STS_CLCTN_TYP.class,locale));
			rawCustomListElement.setCountCst(countCollectionFromRecordUses(rawCustom.getIdCollection().intValue()));*/
			result2.add(rawCustomListElement);
		}
//---->  20110203 inizio: visualizza le collection ordinate per descrizione nome e non per il codice numerico corrispondente al nome 
		return sortCustomList(SORT_ASC_CODE, SORT_NAME_ITA_CODE, result2);
//		return result2;
//---->  20110203 fine
	}
	
	public List getListCustomElementByTypology(String typology,Locale locale) throws DataAccessException 
	{
		List result = new ArrayList();
		List result2 = new ArrayList();
		result = loadByTypology(typology);
		Iterator iter = result.iterator();
		while (iter.hasNext()) {
			CollectionCustomer rawCustom = (CollectionCustomer) iter.next();
			CustomListElement rawCustomListElement = new CustomListElement(rawCustom);
			/*rawCustomListElement.setIdCollection(rawCustom.getIdCollection().intValue());
			rawCustomListElement.setNameIta(LibrisuiteAction.getDaoCodeTable().getLongText((short)rawCustom.getNameIta(),T_CLCTN_CST_TYP.class,locale));
			rawCustomListElement.setTypologyCode(LibrisuiteAction.getDaoCodeTable().getLongText(rawCustom.getTypologyCode(),T_CLCTN_TYP.class,locale));
			rawCustomListElement.setStatusCode(LibrisuiteAction.getDaoCodeTable().getLongText(rawCustom.getStatusCode().shortValue(),T_STS_CLCTN_TYP.class,locale));
			rawCustomListElement.setCountCst(countCollectionFromRecordUses(rawCustom.getIdCollection().intValue()));*/
			result2.add(rawCustomListElement);
		}
//---->  20110203 inizio: visualizza le collection ordinate per descrizione nome e non per il codice numerico corrispondente al nome 
		return sortCustomList(SORT_ASC_CODE, SORT_TYPOLOGY_CODE, result2);
//		return result2;
//---->  20110203 fine
	}
	
	public List loadCustomByName(int nameIta) throws DataAccessException 
	{
		List result = null;
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct "
					+ " from CollectionCustomer as ct " 
					+ " where ct.nameIta ="
					+ nameIta);
			q.setMaxResults(1);
			result = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	public int countCollectionFromRecordUses(int idCollection) throws DataAccessException 
	{
		List l = find("select count(*) from CLCTN_CST_ACS_PNT apf, CollectionCustomer ct " 
				+ " where ct.idCollection = apf.collectionNumber" 
				+ " and apf.collectionNumber = ?", 
				new Object[] { new Integer(idCollection) }, new Type[] { Hibernate.INTEGER });
		
		if (l.size() > 0) {
			return ((Integer) l.get(0)).intValue();
		} else {
			return 0;
		}
	}
	
	public List loadByTypology(String typology) throws DataAccessException 
	{
		List result = null;
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct "
					+ " from CollectionCustomer as ct,T_CLCTN_TYP as mst " 
					+ " where ct.typologyCode ='"
					+ typology+"'");
			result = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
  
	public List getListCustomsElementByIem(int itemNumber,Locale locale) throws DataAccessException 
	{
		List result = new ArrayList();
		List result2 = new ArrayList();
		result = getCollectionsByBibItem(itemNumber);
		Iterator iter = result.iterator();
		while (iter.hasNext()) {
			CollectionCustomer rawCustom = (CollectionCustomer) iter.next();
			CustomListElement rawCustomListElement = new CustomListElement(rawCustom);
			/*rawCustomListElement.setIdCollection(rawCustom.getIdCollection().intValue());
			rawCustomListElement.setNameIta(LibrisuiteAction.getDaoCodeTable().getLongText((short)rawCustom.getNameIta(),T_CLCTN_CST_TYP.class,locale));
			rawCustomListElement.setTypologyCode(LibrisuiteAction.getDaoCodeTable().getLongText(rawCustom.getTypologyCode(),T_CLCTN_TYP.class,locale));
			rawCustomListElement.setStatusCode(LibrisuiteAction.getDaoCodeTable().getLongText(rawCustom.getStatusCode().shortValue(),T_STS_CLCTN_TYP.class,locale));
			rawCustomListElement.setCountCst(countCollectionFromRecordUses(rawCustom.getIdCollection().intValue()));
//-------->	 20101014 inizio: devo memorizzare la data di associazione del record 
			rawCustomListElement.setDateAssociatedRecord(getRecordAssociatedDate(itemNumber, rawCustom.getIdCollection().intValue()));
//-------->	 20101014 fine*/
			result2.add(rawCustomListElement);
		}
		return result2;
	}
  
//	 20101014 inizio
	public Date getRecordAssociatedDate(int itemNumber, int idCollection) throws DataAccessException 
	{
		Date dateCreation = null; 
		try {
			Session s = currentSession();
			Query q = s.createQuery("Select ct.creationDate from CLCTN_CST_ACS_PNT ct where ct.collectionNumber = " 
					+ idCollection 
					+ " and ct.bibItemNumber = " + itemNumber);
			q.setMaxResults(1);
			dateCreation = new Date(((Timestamp) q.list().get(0)).getTime());

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return dateCreation;
	}
  
	
	public List getCollectionsByBibItem(int itemNumber) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		java.sql.ResultSet rs = null;
		Session s = currentSession();
		String query = "";
		List l = null;
		try {
			connection = s.connection();
			query = "Select distinct ct.* from  custom.CLCTN_CST_ACS_PNT apf, custom.T_CLCTN_CST ct where apf.BIB_ITM_NBR ="
					+ itemNumber
					+ "  and ct.CLCTN_CST_CDE = apf.CLCTN_CST_CDE";
		    stmt = connection.prepareStatement(query);
			rs = stmt.executeQuery();
			l = new ArrayList();
			while (rs.next()) {
				CollectionCustomer co = new CollectionCustomer();
				co.setIdCollection(new Integer(rs.getInt("CLCTN_CST_CDE")));
				co.setNameIta(rs.getInt("NME_CST_CDE"));
				co.setStatusCode(new Integer(rs.getInt("STUS_CDE")));
				co.setDateCreation(rs.getDate("CRTN_DTE"));
				co.setDateCancel(rs.getDate("CNCL_DTE"));
				co.setTypologyCode(rs.getString("TPLG_CDE"));
				co.setCustomerId(rs.getString("CSTMR_CDE"));
				co.setUpgrade(rs.getString("UPGRD"));
				co.setIdCollectionMST(new Integer(rs.getInt("CLCTN_MST_CDE")));
				co.setYear(rs.getInt("YEAR"));
//------------>	 20110131 inizio:
				co.setDateIniVal(rs.getDate("DT_INI_VAL"));
				co.setDateFinVal(rs.getDate("DT_FIN_VAL"));
				co.setDateType(rs.getString("DT_TYPE"));
//------------>	 20110131 fine
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
				if (stmt != null) stmt.close();

			} catch (SQLException e) {
				logAndWrap(e);
			}
		}
		return l;
	}
	
	/**
	 *  20110223: Aggiunta scrittura della tabella archivio per le custom eleminate 
     * e gestito tutto in modo che se tutto ok commit altrimenti rollback	
	 */
	public void delete(CollectionCustomer collectionCustomer, String user) throws DataAccessException 
	{
		Session s = currentSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
//-------->	Cancella collection custom
			s.delete(collectionCustomer);
//-------->	Cancella nome collection custom eliminata
			deleteCollName(collectionCustomer.getNameIta());
//-------->	Scrittura tabella archivio per custom eliminata			
			CollectionCustomerArch collectionCustomerArch = new CollectionCustomerArch(collectionCustomer, user);
			collectionCustomerArch.markNew();
			s.save(collectionCustomerArch);
//-------->	Se tutto ok COMMIT
			tx.commit();
		} catch (HibernateException e) {
			logAndWrap(e);
			try {
                tx.rollback();
            } catch (HibernateException e1) {
           	 logAndWrap(e1);
            }
		}
	}
	
	/**
	 * Metodo che cancella il nome della collection eliminata
	 * @param nameIta
	 * @throws DataAccessException
	 */
	public void deleteCollName (int nameIta) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		Session session = currentSession();
		String query = "";
		try {			
			connection = session.connection();
            query = "delete from T_CLCTN_CST_TYP i where i.TBL_VLU_CDE = ? ";		
			stmt = connection.prepareStatement(query);
		    stmt.setInt(1, nameIta);
			stmt.execute();         
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e1) {
			logAndWrap(e1);
		} finally {
			try {
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				logAndWrap(e);
			}
		}
	}
}