package org.folio.marccat.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.dao.persistence.PublCdeHdg;

import java.util.List;

public class DAOPublisher extends AbstractDAO {

  public DAOPublisher() {
    super();
  }

  public List getPublisherList() throws DataAccessException {
    List result = null;
    try {
      Session s = currentSession();
      result = s.find("from CasSapPubl as a ", new Object[]{}, new Type[]{});
    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }


  public List loadHdg(String hdg) throws DataAccessException {
    List result = null;
    try {

      Session s = currentSession();
      result = s.find("from PublCdeHdg as a where a.hdrNumber = '" + hdg + "'");

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }

  public void deleteHdgPubl(PublCdeHdg item) throws DataAccessException {
    try {

      Session s = currentSession();
      s.delete("from PublCdeHdg as a where a.hdrNumber = '" + item.getHdrNumber() + "' and a.publisherCode = '" + item.getPublisherCode() + "'");

    } catch (HibernateException e) {
      logAndWrap(e);
    }
  }

  //dato un codice editore recupera le schede associate
  public List getResultFromPublisher(String publisherCode) throws DataAccessException {
    List result = null;
    try {
      Session s = currentSession();
      Query q = s.createQuery("select distinct ta"
        + " from TitleAccessPoint as ta, "
        + " PublCdeHdg as pu "
        + " where pu.publisherCode = ?"
        + " and pu.hdrNumber = ta.headingNumber");
      q.setString(0, publisherCode);
      q.setMaxResults(100);
      result = q.list();

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }

  public List loadHeadingFromPublisher(String publisherCode) throws DataAccessException {
//		System.out.println("editore per select hdg : " + publisherCode);
    List result = null;
    try {
      Session s = currentSession();
      Query q = s.createQuery("select distinct ta"
        + " from PUBL_HDG as ta, "
        + " PublCdeHdg as pu "
        + " where pu.publisherCode = ?"
        + " and pu.hdrNumber = ta.key.headingNumber"
        + " order by ta.nameSortForm");
      q.setString(0, publisherCode);
//			q.setMaxResults(100);		
      result = q.list();

//			if (result.size() == 0) {
//				throw new RecordNotFoundException();
//			}


    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }

  public List loadTotalHeading() throws DataAccessException {
    List result = null;
    Query q = null;
    try {
      Session s = currentSession();
      q = s.createQuery("select pu, ta"
        + " from PUBL_HDG as ta, "
        + " PublCdeHdg as pu "
        + " where pu.hdrNumber = ta.key.headingNumber"
        + " order by ta.nameSortForm");
//			q.setMaxResults(100);		
      result = q.list();

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }
}
