package org.folio.cataloging.dao;

import net.sf.hibernate.*;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.Global;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.common.TransactionalHibernateOperation;
import org.folio.cataloging.dao.persistence.CLCTN_CST_RULE;
import org.folio.cataloging.dao.persistence.CLCTN_RULE_TMP;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// TODO: Javadoc
// TODO: Must be used within the StorageService
public class DAOCollectionRuleCST extends AbstractDAO {
  private static Log logger = LogFactory.getLog(DAOCollectionRuleCST.class);

  public DAOCollectionRuleCST() {
    super();
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
    List result = new ArrayList();

    try {
      Session s = currentSession();
      Query q = s.createQuery("Select distinct ct from CLCTN_CST_RULE as ct where ct.ruleId = " + ruleId);
      q.setMaxResults(1);
      result = q.list();

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }

  /**
   * Il metodo inserisce le occorrenze nella tabella temporanea che serve alla procedura
   * di aggiornamento delle tabelle CLCNT_CST_RULE_REL e CLCNT_CST_RULE_RECORD
   *
   * @param tmpList
   * @param idRule
   * @throws DataAccessException
   * @throws HibernateException
   */
  public void insertTmpTable(List tmpList, Integer idRule) throws DataAccessException, HibernateException {
//		Session s = currentSession();
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

    /**
     * Se non ci sono records da inserire, significa che TUTTI i records della collection source devono alimentare
     * la collection target. La procedura viene richiamata lo stesso con typ=0 per fare solo la cancelazione della
     *  " + System.getProperty(org.folio.cataloging.Global.SCHEMA_CUSTOMER_KEY) + ".CLCTN_CST_RULE_RECORD per l'id regola
     */
    if (tmpList.size() == 0) {
      tmpTable = new CLCTN_RULE_TMP(new Integer(0), new Long(0), idRule);
      tmpTable.markNew();
      logger.debug("--------> Insert per sola cancellazione della tabella temporanea CLCTN_RULE_TMP");
      logger.debug("IdRule : " + tmpTable.getIdRule());
      logger.debug("Type   : " + tmpTable.getType());
      logger.debug("IdItem : " + tmpTable.getIdItem());
      persistByStatus(tmpTable);
    }
  }

  /**
   * Il metodo cancella le tre tabelle relative alla regola CLCTN_CST_RULE, CLCTN_CST_RULE_RECORD, CLCTN_CST_RULE_REL
   *
   * @param idRule
   * @throws DataAccessException
   */
  public void delete(final int idRule) throws DataAccessException {
    new TransactionalHibernateOperation() {
      public void doInHibernateTransaction(Session s) throws HibernateException, SQLException {
        s.delete("from CLCTN_CST_RULE as ct where ct.ruleId = ? ",
          new Object[]{new Integer(idRule)}, new Type[]{Hibernate.INTEGER});

        s.delete("from CLCTN_CST_RULE_RECORD as ct where ct.ruleId = ? ",
          new Object[]{new Integer(idRule)}, new Type[]{Hibernate.INTEGER});
      }
    }
      .execute();
  }

  /**
   * Il metodo carica record e collection associati alla regola in esame
   *
   * @param rule
   * @throws DataAccessException
   */
  public void loadRelationsRule(CLCTN_CST_RULE rule) throws DataAccessException {
    rule.setRecordsList(loadRecords(rule.getRuleId().intValue()));
  }

  /**
   * Il metodo legge tutti i records associati alla regola passata
   *
   * @param ruleId
   * @return
   * @throws DataAccessException
   */
  public List loadRecords(int ruleId) throws DataAccessException {
    List result = new ArrayList();

    try {
      Session s = currentSession();
      Query q = s.createQuery("Select distinct ct from CLCTN_CST_RULE_RECORD as ct where ct.ruleId = "
        + ruleId + " order by ct.recordId");
      result = q.list();

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }

  /**
   * Il metodo gestisce l'aggiornamento del db relativamente alle tabelle delle regole
   * Se tutto ok COMMIT altrimenti ROLLBACK
   *
   * @param item
   * @throws DataAccessException
   */
  public void saveRuleAndRelations(CLCTN_CST_RULE item) throws DataAccessException {
    Session s = currentSession();
    CallableStatement proc = null;
    Transaction tx = null;
    try {
      tx = s.beginTransaction();
//-------->	Inserisce l'occorrenza in CLCTN_CST_RULE
//			s.saveOrUpdate(item);  --> NON FUNZIONA FA SEMPRE UPDATE!!!
      if (item.isNew()) {
        s.save(item);
      } else {
        s.update(item);
      }

//-------->	Inserisce le occorrenze nella CLCTN_CST_RULE_TMP
      insertTmpTable(item.getRecordCollectionList(), item.getRuleId());
//-------->	Chiama la procedura di aggiornamento delle tabelle CLCNT_CST_RULE_REL e CLCNT_CST_RULE_RECORD
      Connection connection = s.connection();
      proc = connection.prepareCall("{call  " + System.getProperty(Global.SCHEMA_CUSTOMER_KEY) + ".CAS_CLCTN.CLCTN_CST_RULE_UPD}");
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
    } finally {
      try {
        if (proc != null) proc.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

//	public List selectTmp() throws DataAccessException
//	{
//		List result = new ArrayList();
//		try {
//			Session s = currentSession();
//			Query q = s.createQuery("Select distinct ct from CLCTN_CST_RULE_TMP as ct order by ct.idRule");
//			result = q.list();
//
//		} catch (HibernateException e) {
//			logAndWrap(e);
//		}
//		
//		Iterator iter = result.iterator();
//		while (iter.hasNext()) {
//			CLCTN_CST_RULE_TMP item = (CLCTN_CST_RULE_TMP) iter.next();
//			logger.info("RULE : " + item.getIdRule());
//			logger.info("TYPE : " + item.getType());
//			logger.info("ITEM : " + item.getIdItem());
//		}
//		return null;
//	}
}
