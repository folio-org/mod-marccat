package org.folio.cataloging.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.persistence.CasCache;
import org.folio.cataloging.dao.persistence.CasTransfRec;

import java.sql.SQLException;
import java.util.List;

public class DAOCasTrnsfPrdct extends AbstractDAO {
  public DAOCasTrnsfPrdct() {
    super();
  }

  public void persistCasTrnsfPrdct(int bibNumber, CasCache cache)
    throws DataAccessException {
    CasCache cache2;
    if (loadCasTrnsfPrdct(bibNumber).size() == 0) {
      cache.setLevelCard(cache.getLevelCard());
      cache.setNtrLevel(cache.getNtrLevel());
      cache.setDigCheck(cache.getDigCheck());
      cache.setBibItemNumber(bibNumber);
      persistByStatus(cache);


    } else {
      cache2 = (CasCache) loadCasTrnsfPrdct(bibNumber).get(0);
      cache2.setLevelCard(cache.getLevelCard());
//	inizio
      cache2.setNtrLevel(cache.getNtrLevel());
      cache2.setDigCheck(cache.getDigCheck());
//	fine
      cache2.markChanged();
      persistByStatus(cache2);
    }

  }

  public List loadListToTransfer(int transactionId) throws DataAccessException {
    List result = null;
    try {
      Session s = currentSession();

      Query q = s.createQuery("select distinct ct"
        + " from CasTransferProd as ct "
        + " where ct.transactionId = " + transactionId
        + " and ct.booked = 'Y'");
      //q.setMaxResults(1);
      result = q.list();

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }

  public List loadCasTrnsfPrdct(int bibNumber) throws DataAccessException {
    List result = null;
    try {
      Session s = currentSession();
      Query q = s.createQuery("select distinct ct"
        + " from CasCache as ct " + " where ct.bibItemNumber ="
        + bibNumber);
      q.setMaxResults(1);
      result = q.list();

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }

  public CasCache getCasCache(int bibNumber) throws DataAccessException {
    List result = null;
    CasCache cas = null;
    try {
      Session s = currentSession();
      Query q = s.createQuery("select distinct ct"
        + " from CasCache as ct " + " where ct.bibItemNumber ="
        + bibNumber);
      q.setMaxResults(1);
      result = q.list();
      if (result.size() == 1)
        cas = (CasCache) result.get(0);

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return cas;
  }

  public void updateCasTrnsfPrdct(List transfProdList) throws DataAccessException {
    Session s = currentSession();
    try {

      for (int i = 0; i < transfProdList.size(); i++) {

        Transaction tr = s.beginTransaction();

        s.update(transfProdList.get(i));

        tr.commit();

//				System.out.println("Update successfully!");
      }
    } catch (HibernateException e) {
      logAndWrap(e);
    }
  }

  public int getTransactionId() throws DataAccessException, SQLException {

    List l = null;
    int transactionId = 0;
    Session s = currentSession();
    CasTransfRec sequence = new CasTransfRec();

    try {
      Transaction tr = s.beginTransaction();
//			l = s.find("select d.TRANSF_REC_SEQ.NEXTVAL from dual d");
      s.save(sequence);
      transactionId = sequence.getTransactionId();
      tr.commit();
      //		Query q= (Query) s.createSQLQuery("select TRANSF_REC_SEQ.NEXTVAL as id from dual").addScalar("id", Hibernate.LONG);
//			Query q=s.createQuery("select (max)TRANSACTION_ID from CasTransfRec");
//			l=q.list();
    } catch (HibernateException e) {
      logAndWrap(e);
    }

//		if (l.size()>0)
//			transactionId = new Integer(l.get(0).toString()).intValue();	

    return transactionId;

  }


}
