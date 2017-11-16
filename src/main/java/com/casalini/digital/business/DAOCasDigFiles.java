package com.casalini.digital.business;

import java.util.List;

import librisuite.business.cataloguing.bibliographic.ControlNumberAccessPoint;
import librisuite.business.common.DataAccessException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.casalini.hibernate.model.CasDigFiles;
import com.libricore.librisuite.common.HibernateUtil;

public class DAOCasDigFiles extends HibernateUtil 
{	
	private static Log logger = LogFactory.getLog(DAOCasDigFiles.class);
	
	public DAOCasDigFiles() {
		super();
	}
	
	public void persistCasDigFiles(CasDigFiles digFiles) throws DataAccessException
	{
		CasDigFiles digFiles2;
		List result = loadCasDigFilesByKey(digFiles.getBibItemNumberFiglia(), digFiles.getBibItemNumberMadre());
		
		if (result.size() == 0){
			digFiles.markNew();
			persistByStatus(digFiles);	
		} else {
			digFiles2 = (CasDigFiles) result.get(0);
			digFiles2.markChanged();
			persistByStatus(digFiles2);
		}	
	}
	
	/**
	 * Metodo che legge la tabella CasDigFiles per chiave 
	 */
	public List loadCasDigFilesByKey(int bibItemFiglia, int bibItemMadre) throws DataAccessException 
	{
		List result = null;
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct" 
						+ " from CasDigFiles as ct " 
						+ " where ct.bibItemNumberFiglia = " + bibItemFiglia  
						+ " and ct.bibItemNumberMadre = " + bibItemMadre);
			q.setMaxResults(1);
			result = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}	
	
	/**
	 * Metodo che legge la CasDigFiles per chiave e poi la cancella
	 * @param tag097
	 * @throws DataAccessException
	 */
	public void deleteCasDigFiles(ControlNumberAccessPoint tag097) throws DataAccessException 
	{
		int bibItemNumberMadre = 0;
		int bibItemNumberFiglia = 0;
		StringBuffer buffer = new StringBuffer();
		CasDigFiles digFiles;
		
		if (tag097.getStringText().getSubfieldsWithCodes("c").getDisplayText().toString().trim().length()>0)
			bibItemNumberFiglia = Integer.parseInt(tag097.getStringText().getSubfieldsWithCodes("c").getDisplayText().toString().trim());
		if (tag097.getStringText().getSubfieldsWithCodes("a").getDisplayText().toString().trim().length()>0)
			bibItemNumberMadre = Integer.parseInt(tag097.getStringText().getSubfieldsWithCodes("a").getDisplayText().toString().trim());
		
		List result = loadCasDigFilesByKey(bibItemNumberFiglia, bibItemNumberMadre);
		if (result.size()==0){
			buffer.append("ATTENZIONE --> In DAOCasDigFiles, metodo deleteCasDigFiles - Cancellazione impossibile record non trovato per AN_FIGLIA: ")
			  .append(bibItemNumberFiglia)
			  .append(" e AN_MADRE: ")
			  .append(bibItemNumberMadre);
			logger.error(buffer.toString());	
		}else {
			digFiles = (CasDigFiles) result.get(0);
			digFiles.markDeleted();
			digFiles.getDAO().persistByStatus(digFiles);
		}
	}
	/**
	 * Metodo che legge la tabella CasDigFiles per bibItemFiglia 
	 */
	public List loadCasDigFilesByBibItemFiglia(int bibItemFiglia) throws DataAccessException 
	{
		List result = null;
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct from CasDigFiles as ct where ct.bibItemNumberFiglia = " + bibItemFiglia);
			q.setMaxResults(1);
			result = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}

	/**
	 * 20101018 
	 * Metodo che legge la tabella CasDigFiles per bibItemMadre
	 */
	public List loadCasDigFilesByBibItemMadre(int bibItemMadre) throws DataAccessException 
	{
		List result = null;
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct from CasDigFiles as ct where ct.bibItemNumberMadre = " + bibItemMadre);
			result = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}

}
