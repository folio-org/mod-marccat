package org.folio.cataloging.dao;

import java.util.List;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.persistence.CPY_ID;
import org.folio.cataloging.dao.persistence.SMRY_HLDG;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import org.folio.cataloging.dao.common.HibernateUtil;

public class DAOSummaryHolding extends HibernateUtil 
{
	public void createSummaryHoldingIfRequired(CPY_ID copy) throws DataAccessException 
	{
		SMRY_HLDG aHldg = new SMRY_HLDG(copy);
		if (get(SMRY_HLDG.class, aHldg) == null) 
		{
			// SMRY_HLDG does not yet exist so save the default values
			persistByStatus(aHldg);
		}
	}
	
	/* Natascia 27/06/2007: aggiunto metodo controllo num. di copie per PRN 223 */
	public int countCopies(int amicusNumber, int orgNumber) throws DataAccessException 
	{
		List l = find("select count(*) from CPY_ID as c where c.organisationNumber = ?"
				+ " and c.bibItemNumber = ?", new Object[] {
				new Integer(orgNumber), new Integer(amicusNumber) }, new Type[] {
				Hibernate.INTEGER, Hibernate.INTEGER });
		
		if (l.size() > 0) {
			return ((Integer) l.get(0)).intValue();
		} else {
			return 0;
		}
	}
	
	/* Natascia 27/06/2007: aggiunto metodo deleteRecord per PRN 223 */
	public void deleteRecord(final int amicusNumber, final int orgNumber) throws DataAccessException 
	{	
		Session s = currentSession();	
		try{			
			/* numero copie = 1 perche' si tratta della copia corrente
			*  che si sta eliminando (commit non ancora eseguito su transazione)
			*/
			if (countCopies(amicusNumber, orgNumber) == 1)
			{
				s.delete(
						"from SMRY_HLDG as c where c.mainLibraryNumber = ?"
					  + " and c.bibItemNumber = ?", new Object[] {
						new Integer(orgNumber), new Integer(amicusNumber) },
						new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}
	}
	
	public SMRY_HLDG getSmryHoldingByAmicusNumberOrgNbr(int amicusNumber, int orgNumber) throws DataAccessException 
	{ 
		List l = find("from SMRY_HLDG as c" 
				+ " where c.mainLibraryNumber = ?"
				+ " and c.bibItemNumber = ?", 
				new Object[] {new Integer(orgNumber), new Integer(amicusNumber)}, 
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });
		
		if (l.size() == 1) {
			return (SMRY_HLDG) l.get(0);
		} else {
			return null;
		}
	}
	
	public void deleteSummaryHolding(final int amicusNumber, final int orgNumber) throws DataAccessException 
	{	
		Session s = currentSession();	
		try{			
			s.delete("from SMRY_HLDG as c where c.mainLibraryNumber = ?"
				    + " and c.bibItemNumber = ?", new Object[] {
					new Integer(orgNumber), new Integer(amicusNumber) },
					new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });
		} catch (HibernateException e) {
			logAndWrap(e);
		}
	}
}