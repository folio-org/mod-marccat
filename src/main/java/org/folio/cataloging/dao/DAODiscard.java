/*
 * (c) LibriCore
 * 
 * Created on Jan 24, 2005
 * 
 * DAOInventory.java
 */
package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.bean.cataloguing.copy.CopyListElement;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.common.TransactionalHibernateOperation;
import org.folio.cataloging.dao.persistence.*;
import org.folio.cataloging.util.StringText;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.folio.cataloging.F.deepCopy;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/02/02 14:09:42 $
 * @since 1.0
 */
public class DAODiscard extends DAOCopy {
	private Log logger = LogFactory.getLog(DAODiscard.class);

	/**
	 * @param copyNumber
	 * @return
	 * @throws DataAccessException
	 */
	public List loadItem(int copyNumber) throws DataAccessException {
		Session s = currentSession();
		try {
			List l =
				s.find(
					"from DiscardCopy as i"
						+ " where i.copyIdNumber = ?",
					new Object[] { new Integer(copyNumber)},
					new Type[] { Hibernate.INTEGER });
			return l;
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return null;
	}
	
	
	/**
	 * @param copyNumber
	 * @param discardTyp
	 * @throws DataAccessException
	 */
	public void save(final SHLF_LIST shelf,final int copyNumber, final int discardTyp) throws DataAccessException {

		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
					throws HibernateException,
                    DataAccessException {
				CPY_ID copy = (CPY_ID) s.get(CPY_ID.class, new Integer(
						copyNumber));
				copy.setShelfList(shelf);
				if (copy.getShelfListKeyNumber() != null) {
					copy.setShelfListKeyNumber(0);
				}
				// detach the shelflist
				detachShelfList(copy, copy.getShelfList());
				// tabella DISCARD_CPY
				saveDiscard(copy,discardTyp);
				//if(copy.getShelfListKeyNumber()==0)				
				 s.update(copy);

			}
		}.execute();
	}

	/**
	 * @param copyNumber
	 * @throws DataAccessException
	 */
	public void saveDiscardInventory(final int copyNumber) throws DataAccessException {

		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
					throws HibernateException,
                    DataAccessException {
				saveDiscardInventory(copyNumber,  s);
	
			}
		}.execute();
	
   }
	
	/**
	 * @param cpy
	 * @param discardTyp
	 * @throws DataAccessException
	 */
	public void saveDiscard(CPY_ID cpy, int discardTyp) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		Session session = currentSession();
		//SAVE se non esiste il record
		try {
			connection =session.connection();
		    stmt = connection.prepareStatement ("INSERT INTO DSCRD_CPY (BIB_ITM_NBR, CPY_ID_NBR , ORG_NBR, BRNCH_ORG_NBR, DSCRD_CDE, DSCRD_DTE, LCTN_NME_CDE ) VALUES (?,?,?,?,?,SYSDATE,?)");
			stmt.setInt (1, cpy.getBibItemNumber());
			stmt.setInt (2, cpy.getCopyIdNumber());
			stmt.setInt(3, cpy.getOrganisationNumber());
			stmt.setInt(4, cpy.getBranchOrganisationNumber());
			stmt.setInt(5, discardTyp);
			stmt.setInt(6, cpy.getLocationNameCode());
		    stmt.execute();
		//Update se esiste gia il record solo delle due info DSCRD_CDE, DSCRD_DTE
		} catch (HibernateException e) {
			e.printStackTrace();
			logAndWrap(e);
			
		} catch (SQLException e) {
			e.printStackTrace();
			logAndWrap(e);
		}
		finally
		{
			try {
				if (stmt!=null){
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				logAndWrap(e);
			}			
		}
	}


	/**
	 * @param discardTyp
	 * @throws DataAccessException
	 */
	public void updateDiscard(int copyIdNumber, int discardTyp) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		Session session = currentSession();
		//SAVE se non esiste il record
		try {
			connection =session.connection();
		    stmt = connection.prepareStatement ("UPDATE DSCRD set DSCRD_CDE = ? , DSCRD_DTE = SYSDATE  where CPY_ID_NBR = ?");
			stmt.setInt(1, discardTyp);
			stmt.setInt (2, copyIdNumber);
		    stmt.execute();
		//Update se esiste gia il record solo delle due info DSCRD_CDE, DSCRD_DTE
		} catch (HibernateException e) {
			e.printStackTrace();
			logAndWrap(e);
			
		} catch (SQLException e) {
			e.printStackTrace();
			logAndWrap(e);
		}
		finally
		{
			try {
				if (stmt!=null){
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				logAndWrap(e);
			}			
		}
	}
	
	
	public void updateInventry(int copyIdNumber) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		Session session = currentSession();
		//SAVE se non esiste il record
		String messaggio="Scarto del ";
		try {
			connection =session.connection();
		    stmt = connection.prepareStatement ("UPDATE INVTRY set  NOTE_STRNG  ='"+ messaggio+"'"+"||to_char(sysdate,'dd/mm/yyyy') where CPY_ID_NBR = ?");
			stmt.setInt (1, copyIdNumber);
		    stmt.execute();
		//Update se esiste gia il record solo delle due info DSCRD_CDE, DSCRD_DTE
		} catch (HibernateException e) {
			e.printStackTrace();
			logAndWrap(e);
			
		} catch (SQLException e) {
			e.printStackTrace();
			logAndWrap(e);
		}
		finally
		{
			try {
				if (stmt!=null){
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				logAndWrap(e);
			}			
		}
	}
	
	public void saveDiscardInventory(int copyIdNumber, Session s) throws DataAccessException, HibernateException{
		List dsItems = loadInventoryItems(copyIdNumber);
		Iterator ite = dsItems.iterator();
		while(ite.hasNext()){
			Inventory inv = (Inventory)ite.next();
			DiscardInventory ds = (DiscardInventory)deepCopy(inv);
			s.save(ds);
		}
		
	}
	
	public List loadInventoryItems(int copyNumber) throws DataAccessException {
		Session s = currentSession();
		try {
			List l =
				s.find(
					"from Inventory as i"
						+ " where i.copyNumber = ?",
					new Object[] { new Integer(copyNumber)},
					new Type[] { Hibernate.INTEGER });
			return l;
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return null;
	}
	
	public List getListDiscardCopiesElement(int amicusNumber,Locale locale, int branch) throws DataAccessException {
		List listAllCopies = null;
		List result = new ArrayList();
		DAOOrganisationHierarchy doh = new DAOOrganisationHierarchy();
		DAOLocation dl = new DAOLocation();
		DAOShelfList dsl = new DAOShelfList();
		//DAOCopyNotes dcn = new DAOCopyNotes();
		DAOInventory dci = new DAOInventory();
		DAODiscard   ddsc = new DAODiscard();
		Format formatter = new SimpleDateFormat("dd/MM/yyyy");

		listAllCopies = getAllDiscardCopies(amicusNumber, branch);
		Iterator iter = listAllCopies.iterator();
	try{	
		while (iter.hasNext()) {
			CPY_ID rawCopy = (CPY_ID) iter.next();
		
			CopyListElement rawCopyListElement = new CopyListElement(rawCopy);
			List discaredList= ddsc.loadItem(rawCopy.getCopyIdNumber());
			if(discaredList.size()>0){
				DiscardCopy discard  = (DiscardCopy)discaredList.get(0);
				rawCopyListElement.setDiscardCode(discard.getDiscardCode());
				rawCopyListElement.setDiscardDate(formatter.format(discard.getDiscardDate()));
			}
			
			
			rawCopyListElement.setBranchSymbol(doh.getLibOrBranchSymbol(rawCopy
					.getBranchOrganisationNumber()));
			rawCopyListElement.setLibrarySymbol(doh.getLibOrBranchSymbol(rawCopy.getOrganisationNumber())); //NIC

			LCTN location = dl.load(rawCopy.getBranchOrganisationNumber(),
					rawCopy.getLocationNameCode(), locale);
			if (location == null) {

			} else {
				rawCopyListElement.setLocation(location.getLabelStringText());
			}
			
            int  shelfListKeyNumber = getShelfListKeyNumber(rawCopy.getCopyIdNumber());
            rawCopy.setShelfListKeyNumber(shelfListKeyNumber);
           	if (rawCopy.getShelfListKeyNumber() != null) {
				rawCopy.setShelfList(new DAOShelfList().loadShelf(rawCopy
						.getShelfListKeyNumber().intValue()));
				if (rawCopy.getShelfList() != null) {
					rawCopyListElement
							.setShelfList((new StringText(rawCopy
									.getShelfList().getStringText()))
									.toDisplayString());
				}
			}
			
			//rawCopyListElement.setLoanType( new DAOCodeTable().getLongText(rawCopy.getLoanPrd(),T_LOAN_PRD.class,locale));
			//rawCopyListElement.setCopyStatementText(rawCopy.getCopyStatementText());
			//rawCopyListElement.setCopyRemarkNote(rawCopy.getCopyRemarkNote());


			/*rawCopyListElement.setHowManyNotes(dcn.getCopyNotesList(
					rawCopy.getCopyIdNumber(), locale).size());*/
			rawCopyListElement.setHowManyInventory(dci
					.getInventoryCount(rawCopy.getCopyIdNumber()));
			
			//refineCopyListElementForSummarizeHolding(rawCopyListElement,result,locale);
			result.add(rawCopyListElement);
		}
	}catch (Exception e) {
	e.printStackTrace();
	}
		return result;
	}
	
	public List getAllDiscardCopies(int amicusNumber, int branch) throws DataAccessException {
		List listAllCopies1 = null;
		List listAllCopies2 = null;
		List result = new ArrayList();
		Session s = currentSession();
		Query q1 = null;
		Query q2 = null;
		try{
			
			q1=s.createQuery("select ci.bibItemNumber, ci.copyIdNumber,ci.organisationNumber,ci.branchOrganisationNumber, ci.locationNameCode  from CPY_ID ci where ci.bibItemNumber=" +amicusNumber +" and ci.branchOrganisationNumber="+branch); 
			listAllCopies1 = q1.list();
			Iterator iter = listAllCopies1.iterator();
			while (iter.hasNext()) {
				Object[] obj = (Object[])iter.next();
				CPY_ID rawCopy = new CPY_ID();
				rawCopy.setBibItemNumber((Integer)obj[0]);
				rawCopy.setCopyIdNumber((Integer)obj[1]);
				rawCopy.setOrganisationNumber((Integer)obj[2]);
				rawCopy.setBranchOrganisationNumber((Integer)obj[3]);
				if(obj[4]!=null)
				  rawCopy.setLocationNameCode((Short)obj[4]);
				result.add(rawCopy);
			}
		
			q2=s.createQuery("select ds.bibItemNumber, ds.copyIdNumber,ds.organisationNumber,ds.branchOrganisationNumber, ds.locationNameCode  from DiscardCopy ds where ds.bibItemNumber=" +amicusNumber+" and ds.branchOrganisationNumber="+branch); 
		    listAllCopies2 = q2.list();
			Iterator iter2 = listAllCopies2.iterator();
			while (iter2.hasNext()) {
				Object[] obj = (Object[])iter2.next();
				CPY_ID rawCopy = new CPY_ID();
				rawCopy.setBibItemNumber((Integer)obj[0]);
				rawCopy.setCopyIdNumber((Integer)obj[1]);
				rawCopy.setOrganisationNumber((Integer)obj[2]);
				rawCopy.setBranchOrganisationNumber((Integer)obj[3]);
				if(obj[4]!=null)
				   rawCopy.setLocationNameCode((Short)obj[4]);
				result.add(rawCopy);
			}

		   } catch (HibernateException e) {
			logAndWrap(e);
		 }
		
		   List nonUniqueCollection =  result;
		 //Collection equivalent to Union (as non-unique elements are removed)
		  HashSet uniqueCollection = new HashSet(nonUniqueCollection);
		  List<CPY_ID> list = new ArrayList<CPY_ID>(uniqueCollection);
		  Collections.sort(list, CPY_ID_COMPARATOR);
			
		return list;
	}
	
	/**
	 * @param copyIdNumber
	 * @return
	 * @throws DataAccessException
	 * for copies discarded
	 */
	public int getShelfListKeyNumber(int copyIdNumber) throws DataAccessException 
	{
		Session s = currentSession();
		List l = null;
		int result = 0;
		try {
			l =
				s.find(
					" select ci.shelfListKeyNumber from CPY_ID ci"
						
						+ " where ci.copyIdNumber = ? ",
					new Object[] {new Integer(copyIdNumber)},new Type[] { Hibernate.INTEGER});

			if (l.size() > 0) {
				if(l.get(0)!=null)
				  result = ((Integer) l.get(0)).intValue();
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}

 
	
}
