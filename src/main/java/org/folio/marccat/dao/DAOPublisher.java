package org.folio.marccat.dao;

import java.util.List;

import org.folio.marccat.dao.persistence.PublCdeHdg;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

public class DAOPublisher extends AbstractDAO {

	private String queryAsPu = " PublCdeHdg as pu ";
	
  public DAOPublisher() {
    super();
  }

  public List getPublisherList() {
    List result = null;
    try {
      Session s = currentSession();
      result = s.find("from CasSapPubl as a ", new Object[]{}, new Type[]{});
    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }


  public List loadHdg(String hdg) {
    List result = null;
    try {

      Session s = currentSession();
      result = s.find("from PublCdeHdg as a where a.hdrNumber = '" + hdg + "'");

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }

  public void deleteHdgPubl(PublCdeHdg item) {
    try {

      Session s = currentSession();
      s.delete("from PublCdeHdg as a where a.hdrNumber = '" + item.getHdrNumber() + "' and a.publisherCode = '" + item.getPublisherCode() + "'");

    } catch (HibernateException e) {
      logAndWrap(e);
    }
  }

  //dato un codice editore recupera le schede associate
  public List getResultFromPublisher(String publisherCode) {
    List result = null;
    try {
      Session s = currentSession();
      Query q = s.createQuery("select distinct ta"
        + " from TitleAccessPoint as ta, "
        + queryAsPu
        + " where pu.publisherCode = ?"
        + " and pu.hdrNumber = ta.keyNumber");
      q.setString(0, publisherCode);
      q.setMaxResults(100);
      result = q.list();

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }

  public List loadHeadingFromPublisher(String publisherCode) {
    List result = null;
    try {
      Session s = currentSession();
      Query q = s.createQuery("select distinct ta"
        + " from PUBL_HDG as ta, "
        + queryAsPu
        + " where pu.publisherCode = ?"
        + " and pu.hdrNumber = ta.key.keyNumber"
        + " order by ta.nameSortForm");
      q.setString(0, publisherCode);
      result = q.list();

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }

  public List loadTotalHeading() {
    List result = null;
    Query q = null;
    try {
      Session s = currentSession();
      q = s.createQuery("select pu, ta"
        + " from PUBL_HDG as ta, "
        + queryAsPu
        + " where pu.hdrNumber = ta.key.keyNumber"
        + " order by ta.nameSortForm");
      result = q.list();

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }
}
