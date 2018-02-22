package org.folio.cataloging.dao;

import net.sf.hibernate.*;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.RecordNotFoundException;
import org.folio.cataloging.dao.common.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class DAOCasDigAdmin extends HibernateUtil
{	
	public DAOCasDigAdmin() {
		super();
	}

	public List loadCasDigAdmin(int bibNumber) throws DataAccessException 
	{
		List result = null;
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct from CasDigAdmin as ct " + " where ct.bibItemNumber =" + bibNumber);
			q.setMaxResults(1);
			result = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	public List loadCasSapPubl(String editor) throws DataAccessException
	{
		String editore = editor.toLowerCase();
		List result = null;
		try {
			Session s = currentSession();
			result = s.find("from CasSapPubl as c " + " where lower(c.codEditore) = ? ",
					new Object[] {editore},	new Type[] { Hibernate.STRING });
			
			if (result.size() == 0) {
				throw new RecordNotFoundException("CasSapPubl : codice editore " + editor + " not found");
			}

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	public List loadCasSapPublBreve(String editorSmall) throws DataAccessException
	{
		String editorBreve = editorSmall.toLowerCase();
		List result = null;
		try {
			Session s = currentSession();
			result = s.find("from CasSapPubl as c " + " where lower(c.codEditoreBreve) = ? ",
					new Object[] {editorBreve},	new Type[] { Hibernate.STRING });
			
			if (result.size() == 0) {
				throw new RecordNotFoundException();
			}
	
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}

	
	public List loadCasSapPublFromDen(String denEditor) throws DataAccessException 
	{
		List result = null;
		String denominaz= "'" + denEditor.toLowerCase() + "%'";
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct from CasSapPubl as ct where lower(ct.denEditore) like "
					+ denominaz	+ " order by ct.codEditore" );
			result = q.list();
			
			if (result.size() == 0) {
				throw new RecordNotFoundException();
			}

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	/**
	 * Metodo che legge le policy scelte e salvate in precedenza nella tabella CAS_DGA_POLICY
	 * @param bibNumber
	 * @return lista delle policy
	 * @throws DataAccessException
	 */
	public List loadCasDgaPolicy(int bibNumber) throws DataAccessException 
	{	
		List result = new ArrayList();
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct"
					+ " from CasDgaPolicy as ct " + " where ct.bibItemNumber ="
					+ bibNumber 
					+ " order by ct.idPolicy, ct.typePolicy");
			result = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	public void deleteCasDgaPolicyByBibNumber(int bibNumber) throws DataAccessException 
	{
		Session s = currentSession();
		Transaction tx = null;
		try{	
			 tx = s.beginTransaction();
			 s.delete("from CasDgaPolicy as ct where ct.bibItemNumber =" + bibNumber );
			 tx.commit();
	    	
		} catch (HibernateException e) {
			 if (tx != null) {
	             try {
	                 tx.rollback();
	             } catch (HibernateException e1) {
	            	 logAndWrap(e);
	             }
	         }
		}
	}
	
	public void deleteCasDgaPolicy(int bibNumber) throws DataAccessException 
	{
		try {
			Session s = currentSession();
			s.delete("from CasDgaPolicy as ct where ct.bibItemNumber =" + bibNumber );

		} catch (HibernateException e) {
			logAndWrap(e);
		}
	}	
	
	public void deleteCasDgaPolicyFromKey(String code, String type, int bibNumber) throws DataAccessException 
	{
		try {
			Session s = currentSession();
			s.delete("from CasDgaPolicy as ct where ct.bibItemNumber =" + bibNumber 
					+ " and ct.idPolicy = '" + code + "' and ct.typePolicy = '" + type	+ "'");

		} catch (HibernateException e) {
			logAndWrap(e);
		}
	}
}