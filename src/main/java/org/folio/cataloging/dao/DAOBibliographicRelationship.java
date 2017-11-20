package org.folio.cataloging.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.folio.cataloging.business.cataloguing.bibliographic.*;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.searching.NoResultsFoundException;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.util.StringText;
import org.folio.cataloging.model.Subfield;

/**
 * @author hansv
 * @version $Revision: 1.5 $, $Date: 2006/01/19 14:43:57 $
 * @since 1.0
 */
public class DAOBibliographicRelationship extends HibernateUtil 
{
	private static final Log logger = LogFactory.getLog(DAOBibliographicRelationship.class);

	public short getReciprocalBibItem(int bibItemNumber, int targetBibItemNumber, int userView) throws DataAccessException 
	{
		Session s = currentSession();
		List l = null;
		try {
			l =
				s.find(
					" select count(*) from "
						+ "BibliographicRelationship t "
						+ " where t.bibItemNumber = ? and "
						+ " t.targetBibItemNumber = ? and "
						+ " substr(t.userViewString, ?, 1) = '1'",
					new Object[] {
						new Integer(bibItemNumber),
						new Integer(targetBibItemNumber),
						new Integer(userView)},
					new Type[] {
						Hibernate.INTEGER,
						Hibernate.INTEGER,
						Hibernate.INTEGER });
			if (((Integer) l.get(0)).shortValue() > 0) {
				return 1;
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}

		return 2;
	}

	public BibliographicRelationship loadReciprocalBibItem(int bibItemNumber, int targetBibItemNumber, int userView) throws DataAccessException
	{
		BibliographicRelationship relationship = null;
		try {
			List multiView =
				currentSession().find(
					"from BibliographicRelationship t "
						+ "where t.bibItemNumber = ? and t.targetBibItemNumber = ? and substr(t.userViewString,?,1) = '1'",
					new Object[] {
						new Integer(bibItemNumber),
						new Integer(targetBibItemNumber),
						new Integer(userView)},
					new Type[] {
						Hibernate.INTEGER,
						Hibernate.INTEGER,
						Hibernate.INTEGER });

			List singleView = isolateViewForList(multiView, userView);

			if (singleView.size() > 0) {
				return (BibliographicRelationship) singleView.get(0);
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}

		return relationship;
	}

	public StringText buildRelationStringText(int bibItemNumber, int userView) throws DataAccessException 
	{
		StringText stringText = new StringText();
		DAOBibliographicCatalog catalog = new DAOBibliographicCatalog();

		BibliographicItem item = catalog.getBibliographicItemWithoutRelationships(bibItemNumber, userView);

		/* Name Tags */

		VariableField t = (VariableField) item.findFirstTagByNumber("1");
		if (t != null && t instanceof NameAccessPoint) {
			stringText.addSubfield(
				new Subfield("a", t.getStringText().toDisplayString()));
		}

		/* Title Tags */

		t = (VariableField) item.findFirstTagByNumber("130");

		if (t != null) {
			stringText.addSubfield(
				new Subfield("t", t.getStringText().toDisplayString()));
		} else {
			t = (VariableField) item.findFirstTagByNumber("245");
			if (t != null) {
				stringText.addSubfield(
					new Subfield("t", t.getStringText().toDisplayString()));
			}
			// TODO _MIKE: This code is evil! Cavolo, non va per niente bene...
			else logger.error("245 is a required tag");
		}

		t = (VariableField) item.findFirstTagByNumber("210");
		if (t != null) {
			stringText.addSubfield(
				new Subfield("p", t.getStringText().toDisplayString()));
		}

		/* Note Tags */

		t = (VariableField) item.findFirstTagByNumber("250");
		if (t != null) {
			stringText.addSubfield(
				new Subfield("b", t.getStringText().toDisplayString()));
		}

		/* Publisher Tags */

		t = (VariableField) item.findFirstTagByNumber("260");
		if (t != null) {
			stringText.addSubfield(
				new Subfield("d", t.getStringText().toDisplayString()));
		}

		/* ControlNumber Tags */

		t = (VariableField) item.findFirstTagByNumber("020");
		if (t != null) {
			stringText.addSubfield(
				new Subfield("z", t.getStringText().toDisplayString()));
		} else {
			t = (VariableField) item.findFirstTagByNumber("022");
			if (t != null) {
				stringText.addSubfield(
					new Subfield("x", t.getStringText().toDisplayString()));
			}
		}

		/* ClassificationNumber Tags */

		t = (VariableField) item.findFirstTagByNumber("088");
		if (t != null) {
			stringText.addSubfield(
				new Subfield("r", t.getStringText().toDisplayString()));
		}

		t = (VariableField) item.findFirstTagByNumber("027");
		if (t != null) {
			stringText.addSubfield(
				new Subfield("u", t.getStringText().toDisplayString()));
		}

		t = (VariableField) item.findFirstTagByNumber("030");
		if (t != null) {
			stringText.addSubfield(
				new Subfield("y", t.getStringText().toDisplayString()));
		}

		/* BibItem data */
		stringText.addSubfield(
			new Subfield("e", item.getBibItmData().getLanguageCode()));
		stringText.addSubfield(
			new Subfield("f", item.getBibItmData().getMarcCountryCode()));

		//TODO add missing subfields s and k			

		return stringText;
	}
	
	public void updateRelation(BibliographicRelationshipTag tag, int amicusNumberTo, int cataloguingView) throws DataAccessException, NoResultsFoundException
	{	
		if (isReciprocalRelation(tag.getItemNumber(), tag.getTargetBibItemNumber())){
			updateReciprocalRelation(tag, amicusNumberTo, cataloguingView, "SOURCE_TARGET");
			
		}else if (isUnivocalRelation(tag.getItemNumber(), tag.getTargetBibItemNumber())){
			updateReciprocalRelation(tag, amicusNumberTo, cataloguingView, "TARGET");
			
		}else if (isBlindRelation(tag.getItemNumber())){
			updateReciprocalRelation(tag, amicusNumberTo, cataloguingView, "SOURCE");	
		}else {
			throw new NoResultsFoundException();
		}
	}

	public int updateReciprocalRelation(BibliographicRelationshipTag tag, int amicusNumberTo, int cataloguingView, String typeQuery) 
	throws DataAccessException, NoResultsFoundException
{ 
	Connection connection = null;
	PreparedStatement stmt = null;
	Session session = currentSession();
	int count = 0;
	try {
		connection = session.connection();
		
		if ("SOURCE".equalsIgnoreCase(typeQuery)){
			stmt = null;
			stmt = connection.prepareStatement(
					  "UPDATE RLTSP SET SRC_BIB_ITM_NBR = ? WHERE SRC_BIB_ITM_NBR = ? AND TRGT_BIB_ITM_NBR = ? AND RLTSP_TYP_CDE = ? " +
					  "AND SUBSTR(USR_VW_IND,?,1) = '1'");
			stmt.setInt(1, amicusNumberTo);
			stmt.setInt(2, tag.getItemNumber()); 
			stmt.setInt(3, tag.getTargetBibItemNumber());
		    stmt.setInt(4, tag.getRelationTypeCode());
			stmt.setInt(5, cataloguingView);
			count = count + stmt.executeUpdate();
			
		}else if ("TARGET".equalsIgnoreCase(typeQuery)){
			stmt = null;
			
			stmt = connection.prepareStatement(
					  "UPDATE RLTSP SET SRC_BIB_ITM_NBR = ? WHERE SRC_BIB_ITM_NBR = ? AND TRGT_BIB_ITM_NBR = ? AND RLTSP_TYP_CDE = ? " +
			  		  "AND SUBSTR(USR_VW_IND,?,1) = '1'");
			stmt.setInt(1, amicusNumberTo);
			stmt.setInt(2, tag.getItemNumber()); 
			stmt.setInt(3, tag.getTargetBibItemNumber());
			stmt.setInt(4, tag.getRelationTypeCode());
			stmt.setInt(5, cataloguingView);
			count = count + stmt.executeUpdate();
			
		}else if ("SOURCE_TARGET".equalsIgnoreCase(typeQuery)){
//------------> Se relazione reciproca devo modificare due righe nel db le ho messe insieme per poter fare un solo committ solo se vanno bene tutte e due
			stmt = null;
			
			stmt = connection.prepareStatement(
					  "UPDATE RLTSP SET TRGT_BIB_ITM_NBR = ? WHERE SRC_BIB_ITM_NBR = ? AND TRGT_BIB_ITM_NBR = ? AND RLTSP_TYP_CDE = ? " +
					  "AND SUBSTR(USR_VW_IND,?,1) = '1'");
			stmt.setInt(1, amicusNumberTo);
			stmt.setInt(2, tag.getTargetBibItemNumber());
			stmt.setInt(3, tag.getItemNumber());
			
			stmt.setInt(4, tag.getTargetRelationship().getRelationTypeCode());
			stmt.setInt(5, cataloguingView);
			count = count + stmt.executeUpdate();
					
			stmt = null;
			
			stmt = connection.prepareStatement(
					  "UPDATE RLTSP SET SRC_BIB_ITM_NBR = ? WHERE SRC_BIB_ITM_NBR = ? AND TRGT_BIB_ITM_NBR = ? AND RLTSP_TYP_CDE = ? " + 
					  "AND SUBSTR(USR_VW_IND,?,1) = '1'");
			stmt.setInt(1, amicusNumberTo);
			stmt.setInt(2, tag.getItemNumber()); 
			stmt.setInt(3, tag.getTargetBibItemNumber());
			
			stmt.setInt(4, tag.getRelationTypeCode());
			stmt.setInt(5, cataloguingView);
			count = count + stmt.executeUpdate();
		}
		if (count==0){
			throw new NoResultsFoundException();
		}
		logger.debug("Count: " + count);
	} catch (HibernateException e) {
		logAndWrap(e);
	} catch (SQLException e) {
		logAndWrap(e);
	}
	
	finally
	{
		try {
			stmt.close();
			} catch (SQLException e) {
		}
	}
	return count;
 }
	/**
	 * Il metodo conta le relazioni non cieche del record (se ce ne sono la cancellazione del record non e' possibile) 
	 * @param amicusNumber
	 * @return count
	 * @throws DataAccessException
	 */
	public int countRLTSP(int amicusNumber) throws DataAccessException 
	{
		int result = 0;
		try {
			Session s = currentSession();
			Query q = s.createQuery("select count(*) from BibliographicRelationship as br where br.bibItemNumber = " 
					+ amicusNumber + "and br.targetBibItemNumber > 0");
			q.setMaxResults(1);
			result = ((Integer)q.list().get(0)).intValue();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;	
	}
	
	/**
	 * Il metodo vede sul db se esiste una relazione reciproca fra il source ed il target passati  
	 * @param source 
	 * @param target
	 * @return true/false
	 * @throws DataAccessException
	 */
	public boolean isReciprocalRelation(int source, int target) throws DataAccessException 
	{
		int count = 0;
		boolean result = false;
		try {
			Session s = currentSession();
			Query q = s.createQuery("select count(*) from BibliographicRelationship as br " 
					+ "where (br.bibItemNumber = " + source + " and br.targetBibItemNumber = " + target + ")" 
//					+ " and br.bibItemNumber = " + target + " and br.targetBibItemNumber = " + source);
					+ " or (br.bibItemNumber = " + target + " and br.targetBibItemNumber = " + source + ")");
			
			q.setMaxResults(1);
			count = ((Integer)q.list().get(0)).intValue();
			if (count==2){
				result = true;
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;	
	}
	
	/**
	 * Il metodo vede sul db se esiste una relazione univoca tra source e target 
	 * @param source
	 * @param target
	 * @return true/false
	 * @throws DataAccessException
	 */
	public boolean isUnivocalRelation(int source, int target) throws DataAccessException 
	{
		int count = 0;
		boolean result = false;
		try {
			Session s = currentSession();
			Query q = s.createQuery("select count(*) from BibliographicRelationship as br " 
					+ "where br.bibItemNumber = " + source + " and br.targetBibItemNumber = " + target 
					+ " and br.targetBibItemNumber >0");
			
			q.setMaxResults(1);
			count = ((Integer)q.list().get(0)).intValue();
			if (count==1){
				result = true;
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;	
	}
	
	/**
	 * Il metodo vede sul db se esiste una relazione cieca 
	 * @param source
	 * @param target
	 * @return true/false
	 * @throws DataAccessException
	 */
	public boolean isBlindRelation(int source) throws DataAccessException 
	{
		int count = 0;
		boolean result = false;
		try {
			Session s = currentSession();
			Query q = s.createQuery("select count(*) from BibliographicRelationship as br " 
					+ "where br.bibItemNumber = " + source + " and br.targetBibItemNumber < 0");
			
			q.setMaxResults(1);
			count = ((Integer)q.list().get(0)).intValue();
			if (count==1){
				result = true;
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;	
	}
}