package org.folio.cataloging.dao;

import net.sf.hibernate.*;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.Global;
import org.folio.cataloging.business.RuleListElement;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.common.TransactionalHibernateOperation;
import org.folio.cataloging.dao.persistence.CLCTN_MST_RULE;
import org.folio.cataloging.dao.persistence.CLCTN_MST_RULE_REL;
import org.folio.cataloging.dao.persistence.CLCTN_RULE_TMP;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class DAOCollectionRule extends AbstractDAO {
  private final static ThreadLocal <SimpleDateFormat> FORMATTERS = new ThreadLocal ( ) {
    @Override
    protected Object initialValue() {
      final SimpleDateFormat formatter = new SimpleDateFormat ("dd-MM-yyyy");
      formatter.setLenient (false);
      return formatter;
    }
  };
  private static Log logger = new Log (DAOCollectionRule.class);

  public DAOCollectionRule() {
    super ( );
  }

  /**
   * Reads all configured rules.
   *
   * @return a list of configured rules.
   * @throws DataAccessException in case of data access failure.
   *                             TODO: Session needs to be injected from {@link org.folio.cataloging.integration.StorageService}
   */
  public List loadAllRules(final Locale locale, final List <Avp <String>> nrtLvlList) throws DataAccessException {
    List result2 = new ArrayList ( );
    try {
      Session s = currentSession ( );
      Query query = s.createQuery ("Select distinct ct from CLCTN_MST_RULE as ct order by ct.ruleId");
      List <CLCTN_MST_RULE> rules = query.list ( );
      return rules.stream ( )
        .map (rawRule -> {
          final RuleListElement item = new RuleListElement (rawRule);
          nrtLvlList.stream ( )
            .filter (avp -> avp.getValue ( ).equalsIgnoreCase (rawRule.getLevel ( )))
            .findFirst ( )
            .ifPresent (avp -> item.setLevel (avp.getLabel ( )));


          if (rawRule.getDataProcessing ( ) != null) {
            item.setDataProcessing (FORMATTERS.get ( ).format (rawRule.getDataProcessing ( )));
          }

          if (rawRule.getDataInsert ( ) != null) {
            item.setDataInsert (FORMATTERS.get ( ).format (rawRule.getDataInsert ( )));
          }

          if (rawRule.getDataUpdate ( ) != null) {
            item.setDataUpdate (FORMATTERS.get ( ).format (rawRule.getDataUpdate ( )));
          }

						/*if (RuleCollectionMSTBean.PUBBLICATION_DATE.equalsIgnoreCase(rawRule.getDataType())) {
							if (rawRule.getDataPublRange() != null) {
								item.setDataRange(rawRule.getDataPublRange());
							}
						} else if (RuleCollectionMSTBean.UPLOAD_DATE.equalsIgnoreCase(rawRule.getDataType())) {
							if (rawRule.getDataUploadTo() != null && rawRule.getDataUploadFrom() != null) {
								final StringBuilder buffer =
										new StringBuilder()
												.append(FORMATTERS.get().format(rawRule.getDataUploadFrom()))
												.append(" / ")
												.append(FORMATTERS.get().format(rawRule.getDataUploadTo()));
								item.setDataRange(buffer.toString());
							}
						}*/

          item.setCollSource (loadCollections (rawRule.getRuleId ( )).stream ( )
            .map (CLCTN_MST_RULE_REL::getIdCollection)
            .map (Object::toString)
            .collect (joining (", ")));
          return item;
        }).collect (toList ( ));
    } catch (final HibernateException exception) {
      logger.error (MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException (exception);
    }
  }

  /**
   * Il metodo legge i dati della regola passata
   *
   * @param ruleId
   * @param locale
   * @return
   * @throws DataAccessException
   */
  public List loadRule(int ruleId, Locale locale) throws DataAccessException {
    List result = new ArrayList ( );
    List result2 = new ArrayList ( );

    try {
      Session s = currentSession ( );
      Query q = s.createQuery ("Select distinct ct from CLCTN_MST_RULE as ct where ct.ruleId = " + ruleId);
      q.setMaxResults (1);
      result = q.list ( );

    } catch (HibernateException e) {
      logAndWrap (e);
    }

    Iterator iter = result.iterator ( );
    while (iter.hasNext ( )) {
      CLCTN_MST_RULE rawRule = (CLCTN_MST_RULE) iter.next ( );
      result2.add (rawRule);
    }
    return result2;
  }

  /**
   * Il metodo inserisce le occorrenze nella tabella temporanea che serve alla procedura
   * di aggiornamento delle tabelle CLCNT_MST_RULE_REL e CLCNT_MST_RULE_RECORD
   *
   * @param tmpList
   * @param idRule
   * @throws DataAccessException
   * @throws HibernateException
   */
  public void insertTmpTable(List tmpList, Integer idRule) throws DataAccessException, HibernateException {
    Session s = currentSession ( );
    CLCTN_RULE_TMP tmpTable = null;
    for ( int i = 0; i < tmpList.size ( ); i++ ) {
      tmpTable = (CLCTN_RULE_TMP) tmpList.get (i);
      tmpTable.markNew ( );
      tmpTable.setIdRule (idRule);
      logger.debug ("--------> Insert in tabella temporanea CLCTN_RULE_TMP");
      logger.debug ("IdRule : " + tmpTable.getIdRule ( ));
      logger.debug ("Type   : " + tmpTable.getType ( ));
      logger.debug ("IdItem : " + tmpTable.getIdItem ( ));
//			s.save(tmpTable);
      persistByStatus (tmpTable);
    }
  }

  /**
   * Il metodo cancella le tre tabelle relative alla regola CLCTN_MST_RULE, CLCTN_MST_RULE_RECORD, CLCTN_MST_RULE_REL
   *
   * @param idRule
   * @throws DataAccessException
   */
  public void delete(final int idRule) throws DataAccessException {
    new TransactionalHibernateOperation ( ) {
      public void doInHibernateTransaction(Session s) throws HibernateException, SQLException {
        s.delete ("from CLCTN_MST_RULE as ct where ct.ruleId = ? ",
          new Object[]{new Integer (idRule)}, new Type[]{Hibernate.INTEGER});

        s.delete ("from CLCTN_MST_RULE_RECORD as ct where ct.ruleId = ? ",
          new Object[]{new Integer (idRule)}, new Type[]{Hibernate.INTEGER});

        s.delete ("from CLCTN_MST_RULE_REL as ct where ct.ruleId = ? ",
          new Object[]{new Integer (idRule)}, new Type[]{Hibernate.INTEGER});
      }
    }
      .execute ( );
  }

  /**
   * Il metodo carica record e collection associati alla regola in esame
   *
   * @param rule
   * @throws DataAccessException
   */
  public void loadRelationsRule(CLCTN_MST_RULE rule) throws DataAccessException {
    rule.setRecordsList (loadRecords (rule.getRuleId ( ).intValue ( )));
    rule.setCollectionsList (loadCollections (rule.getRuleId ( ).intValue ( )));
  }

  /**
   * Il metodo legge tutti i records associati alla regola passata
   *
   * @param ruleId
   * @return
   * @throws DataAccessException
   */
  public List loadRecords(int ruleId) throws DataAccessException {
    List result = new ArrayList ( );

    try {
      Session s = currentSession ( );
      Query q = s.createQuery ("Select distinct ct from CLCTN_MST_RULE_RECORD as ct where ct.ruleId = "
        + ruleId + " order by ct.recordId");
      result = q.list ( );

    } catch (HibernateException e) {
      logAndWrap (e);
    }
    return result;
  }

  /**
   * Returns the collections associated with the given rule identifier.
   *
   * @param ruleId the rule identifier.
   * @return the collections associated with the given rule identifier.
   * @throws DataAccessException in case of data access failure.
   *                             TODO: Use within {@link org.folio.cataloging.integration.StorageService}
   */
  public List <CLCTN_MST_RULE_REL> loadCollections(final int ruleId) {
    List result = new ArrayList ( );

    try {
      Session s = currentSession ( );
      final Query query = s.createQuery (
        "Select distinct ct from CLCTN_MST_RULE_REL as ct where ct.ruleId = " +
          ruleId +
          " order by ct.idCollection");
      return query.list ( );
    } catch (final HibernateException exception) {
      logger.error (MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException (exception);
    }
  }

  /**
   * Il metodo gestisce l'aggiornamento del db relativamente alle tabelle delle regole
   * Se tutto ok COMMIT altrimenti ROLLBACK
   *
   * @param item
   * @throws DataAccessException
   */
  public void saveRuleAndRelations(CLCTN_MST_RULE item) throws DataAccessException {
    Session s = currentSession ( );
    CallableStatement proc = null;
    Transaction tx = null;
    try {
      tx = s.beginTransaction ( );
//-------->	Inserisce l'occorrenza in CLCTN_MST_RULE
//			s.saveOrUpdate(item);  --> NON FUNZIONA FA SEMPRE UPDATE!!!
      if (item.isNew ( )) {
        s.save (item);
      } else {
        s.update (item);
      }
//-------->	Inserisce le occorrenze nella CLCTN_MST_RULE_TMP
      insertTmpTable (item.getRecordCollectionList ( ), item.getRuleId ( ));
//			selectTmp();
//-------->	Chiama la procedura di aggiornamento delle tabelle CLCNT_MST_RULE_REL e CLCNT_MST_RULE_RECORD
      Connection connection = s.connection ( );
      proc = connection.prepareCall ("{call  " + System.getProperty (Global.SCHEMA_CUSTOMER_KEY) + ".CAS_CLCTN.CLCTN_MST_RULE_UPD}");
      proc.execute ( );
//-------->	Se tutto ok COMMIT			
      tx.commit ( );
    } catch (HibernateException e) {
      logAndWrap (e);
      try {
        tx.rollback ( );
      } catch (HibernateException e1) {
        logAndWrap (e1);
      }
    } catch (SQLException e) {
      logAndWrap (e);
      try {
        tx.rollback ( );
      } catch (HibernateException e1) {
        logAndWrap (e1);
      }
    } finally {
      try {
        if (proc != null) proc.close ( );
      } catch (SQLException e) {
        e.printStackTrace ( );
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
