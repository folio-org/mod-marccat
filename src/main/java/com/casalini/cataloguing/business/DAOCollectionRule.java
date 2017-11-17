package com.casalini.cataloguing.business;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import librisuite.business.codetable.ValueLabelElement;
import librisuite.business.common.DataAccessException;
import librisuite.hibernate.CLCTN_MST_RULE;
import librisuite.hibernate.CLCTN_MST_RULE_REL;
import librisuite.hibernate.CLCTN_RULE_TMP;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.type.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.casalini.cataloguing.bean.RuleCollectionMSTBean;
import com.libricore.librisuite.common.HibernateUtil;
import com.libricore.librisuite.common.TransactionalHibernateOperation;
import org.folio.cataloging.Global;

public class DAOCollectionRule extends HibernateUtil 
{
	private static Log logger = LogFactory.getLog(DAOCollectionRule.class);
	
	public DAOCollectionRule() {
		super();
	}
	
	/**
	 * Il metodo legge tutte le regole esistenti  
	 */
	public List loadAllRules(Locale locale, List nrtLvlList) throws DataAccessException 
	{
		List result = new ArrayList();
		List result2 = new ArrayList();
		try {
			Session s = currentSession();
			Query q = s.createQuery("Select distinct ct from CLCTN_MST_RULE as ct order by ct.ruleId");
			result = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		
		Iterator iter = result.iterator();
		while (iter.hasNext()) {
			CLCTN_MST_RULE rawRule = (CLCTN_MST_RULE) iter.next();
			RuleListElement rawRuleListElement = new RuleListElement(rawRule);
			
			String descLevel = "";
			for (Iterator iterator = nrtLvlList.iterator(); iterator.hasNext();) {
				ValueLabelElement valueLabelElement = (ValueLabelElement) iterator.next();
				if (valueLabelElement.getValue().equalsIgnoreCase(rawRule.getLevel())){
					descLevel=valueLabelElement.getLabel();
				}
			}
			rawRuleListElement.setLevel(descLevel);
			
			Format formatter = new SimpleDateFormat("dd-MM-yyyy");
			if (rawRule.getDataProcessing() !=null){
				rawRuleListElement.setDataProcessing(formatter.format(rawRule.getDataProcessing()));
			}
			if (rawRule.getDataInsert() !=null){
				rawRuleListElement.setDataInsert(formatter.format(rawRule.getDataInsert()));
			}
			if (rawRule.getDataUpdate() !=null){
				rawRuleListElement.setDataUpdate(formatter.format(rawRule.getDataUpdate()));
			}
			
			if (RuleCollectionMSTBean.PUBBLICATION_DATE.equalsIgnoreCase(rawRule.getDataType())){
				if (rawRule.getDataPublRange()!=null){
					rawRuleListElement.setDataRange(rawRule.getDataPublRange());	
				}
			} else if (RuleCollectionMSTBean.UPLOAD_DATE.equalsIgnoreCase(rawRule.getDataType())){
				if (rawRule.getDataUploadTo()!=null && rawRule.getDataUploadFrom()!=null){
					StringBuffer buffer = new StringBuffer().append(formatter.format(rawRule.getDataUploadFrom())).append(" / ").append(formatter.format(rawRule.getDataUploadTo()));
					rawRuleListElement.setDataRange(buffer.toString());
				}
			}			
			
			List collectionsRule = loadCollections(rawRule.getRuleId().intValue());
			CLCTN_MST_RULE_REL collectionRule = null; 
			StringBuffer buffer = new StringBuffer();
			for (Iterator iterator = collectionsRule.iterator(); iterator.hasNext();) {
				collectionRule = (CLCTN_MST_RULE_REL) iterator.next();
				if (buffer.length()>0){
					buffer.append(", ");
				}
				buffer.append(collectionRule.getIdCollection());
			}
			rawRuleListElement.setCollSource(buffer.toString());
			
			
			result2.add(rawRuleListElement);
		}
		return result2;
	}
	
	/**
	 * Il metodo legge i dati della regola passata 
	 * @param ruleId
	 * @param locale
	 * @return
	 * @throws DataAccessException
	 */
	public List loadRule(int ruleId, Locale locale)  throws DataAccessException 
	{
		List result = new ArrayList();
		List result2 = new ArrayList();
		
		try {
			Session s = currentSession();
			Query q = s.createQuery("Select distinct ct from CLCTN_MST_RULE as ct where ct.ruleId = " + ruleId);
			q.setMaxResults(1);
			result = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		
		Iterator iter = result.iterator();
		while (iter.hasNext()) {
			CLCTN_MST_RULE rawRule = (CLCTN_MST_RULE) iter.next();
			result2.add(rawRule);
		}
		return result2;
	}
	
	/**
	 * Il metodo inserisce le occorrenze nella tabella temporanea che serve alla procedura 
	 * di aggiornamento delle tabelle CLCNT_MST_RULE_REL e CLCNT_MST_RULE_RECORD
	 * @param tmpList
	 * @param idRule
	 * @throws DataAccessException
	 * @throws HibernateException
	 */
	public void insertTmpTable(List tmpList, Integer idRule) throws DataAccessException, HibernateException 
	{
		Session s = currentSession();
		CLCTN_RULE_TMP tmpTable = null;
		for (int i = 0; i < tmpList.size(); i++) {
			tmpTable = (CLCTN_RULE_TMP) tmpList.get(i);
			tmpTable.markNew();
			tmpTable.setIdRule(idRule);
			logger.debug("--------> Insert in tabella temporanea CLCTN_RULE_TMP");
			logger.debug("IdRule : " + tmpTable.getIdRule());
			logger.debug("Type   : " + tmpTable.getType());
			logger.debug("IdItem : " + tmpTable.getIdItem());
//			s.save(tmpTable);
			persistByStatus(tmpTable);
		}
	}

	/**
	 * Il metodo cancella le tre tabelle relative alla regola CLCTN_MST_RULE, CLCTN_MST_RULE_RECORD, CLCTN_MST_RULE_REL
	 * @param idRule
	 * @throws DataAccessException
	 */
	public void delete(final int idRule) throws DataAccessException 
	{
		new TransactionalHibernateOperation() 
		{
			public void doInHibernateTransaction(Session s) throws HibernateException, SQLException 
			{	
				s.delete("from CLCTN_MST_RULE as ct where ct.ruleId = ? ",
				  new Object[] { new Integer(idRule)}, new Type[] {Hibernate.INTEGER});		
				
				s.delete("from CLCTN_MST_RULE_RECORD as ct where ct.ruleId = ? ",
						  new Object[] { new Integer(idRule)}, new Type[] {Hibernate.INTEGER});
				
				s.delete("from CLCTN_MST_RULE_REL as ct where ct.ruleId = ? ",
						  new Object[] { new Integer(idRule)}, new Type[] {Hibernate.INTEGER});
			}
		}
		.execute();
	}

	/**
	 * Il metodo carica record e collection associati alla regola in esame
	 * @param rule
	 * @throws DataAccessException
	 */
	public void loadRelationsRule(CLCTN_MST_RULE rule) throws DataAccessException 
	{
		rule.setRecordsList(loadRecords(rule.getRuleId().intValue()));
		rule.setCollectionsList(loadCollections(rule.getRuleId().intValue()));
	}
	
	/**
	 * Il metodo legge tutti i records associati alla regola passata
	 * @param ruleId
	 * @return
	 * @throws DataAccessException
	 */
	public List loadRecords(int ruleId)  throws DataAccessException 
	{
		List result = new ArrayList();
		
		try {
			Session s = currentSession();
			Query q = s.createQuery("Select distinct ct from CLCTN_MST_RULE_RECORD as ct where ct.ruleId = " 
					+ ruleId + " order by ct.recordId" );
			result = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	/**
	 * Il metodo legge tutte le collection associate alla regola passata
	 * @param ruleId
	 * @return
	 * @throws DataAccessException
	 */	
	public List loadCollections(int ruleId)  throws DataAccessException 
	{
		List result = new ArrayList();
		
		try {
			Session s = currentSession();
			Query q = s.createQuery("Select distinct ct from CLCTN_MST_RULE_REL as ct where ct.ruleId = " 
					+ ruleId + " order by ct.idCollection" );
			result = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	/**
	 * Il metodo gestisce l'aggiornamento del db relativamente alle tabelle delle regole
	 * Se tutto ok COMMIT altrimenti ROLLBACK	
	 * @param item
	 * @throws DataAccessException
	 */
	public void saveRuleAndRelations(CLCTN_MST_RULE item) throws DataAccessException 
	{
		Session s = currentSession();
		CallableStatement proc = null;
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
//-------->	Inserisce l'occorrenza in CLCTN_MST_RULE
//			s.saveOrUpdate(item);  --> NON FUNZIONA FA SEMPRE UPDATE!!!
			if (item.isNew()){
				s.save(item);
			}else{
				s.update(item);
			}					
//-------->	Inserisce le occorrenze nella CLCTN_MST_RULE_TMP
			insertTmpTable(item.getRecordCollectionList(), item.getRuleId());
//			selectTmp();
//-------->	Chiama la procedura di aggiornamento delle tabelle CLCNT_MST_RULE_REL e CLCNT_MST_RULE_RECORD
			Connection connection = s.connection();
			proc = connection.prepareCall("{call  " + System.getProperty(Global.SCHEMA_CUSTOMER_KEY) + ".CAS_CLCTN.CLCTN_MST_RULE_UPD}");
			proc.execute();
//-------->	Se tutto ok COMMIT			
			tx.commit();
		} catch (HibernateException e) {
			logAndWrap(e);
			try {
                tx.rollback();
            } catch (HibernateException e1) {
           	 logAndWrap(e1);
            }
		} catch (SQLException e) {
			logAndWrap(e);
			try {
                tx.rollback();
            } catch (HibernateException e1) {
           	 logAndWrap(e1);
            }
		}
		
		finally{
			try {
				if(proc!=null) proc.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

//	public void saveRuleAndRelations(final CLCTN_MST_RULE item) throws DataAccessException 
//	{
//		new TransactionalHibernateOperation() 
//		{
//			public void doInHibernateTransaction(Session s) throws HibernateException, SQLException, DataAccessException 
//			{	
//				CallableStatement proc = null;
//				Transaction tx = null;
//				try {
//					tx = s.beginTransaction();
//					if (item.isNew()){
//						s.save(item);
//					}else{
//						s.update(item);
//					}
//					CLCTN_RULE_TMP tmpTable = null;
//					for (int i = 0; i < item.getRecordCollectionList().size(); i++) {
//						tmpTable = (CLCTN_RULE_TMP) item.getRecordCollectionList().get(i);
//						tmpTable.markNew();
//						tmpTable.setIdRule(item.getRuleId());
//						logger.info("--------> Insert in tabella temporanea CLCTN_RULE_TMP");
//						logger.info("IdRule : " + tmpTable.getIdRule());
//						logger.info("Type   : " + tmpTable.getType());
//						logger.info("IdItem : " + tmpTable.getIdItem());
//						s.save(tmpTable);
//					}
//					selectTmp();
//					Connection connection = s.connection();
//					proc = connection.prepareCall("{call  " + System.getProperty(org.folio.cataloging.Global.SCHEMA_CUSTOMER_KEY) + ".CLCTN_MST_RULE_UPD}");
//					proc.execute();
//				} catch (HibernateException e) {
//					logAndWrap(e);
//				} catch (SQLException e) {
//					logAndWrap(e);
//				}
//				finally{
//					try {
//						if(proc!=null) proc.close();
//					} catch (SQLException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//		.execute();
//	}
	
//	public void selectTmp() throws DataAccessException
//	{
//		logger.debug("---> In select tmp <---");
//		List result = new ArrayList();
//		try {
//			Session s = currentSession();
//			Query q = s.createQuery("Select distinct ct from CLCTN_RULE_TMP as ct order by ct.idRule");
//			result = q.list();
//
//		} catch (HibernateException e) {
//			logAndWrap(e);
//		}
//		
//		Iterator iter = result.iterator();
//		while (iter.hasNext()) {
//			CLCTN_RULE_TMP item = (CLCTN_RULE_TMP) iter.next();
//			logger.debug("******************************************");
//			logger.debug("DATI INSERITI NELLA TABELLA CLCTN_RULE_TMP");
//			logger.debug("RULE : " + item.getIdRule());
//			logger.debug("TYPE : " + item.getType());
//			logger.debug("ITEM : " + item.getIdItem());
//		}
//	}
}