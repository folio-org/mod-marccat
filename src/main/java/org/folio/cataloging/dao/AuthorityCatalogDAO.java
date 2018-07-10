package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.cataloguing.authority.*;
import org.folio.cataloging.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.cataloging.business.cataloguing.common.CatalogItem;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.controller.UserProfile;
import org.folio.cataloging.dao.persistence.Descriptor;
import org.folio.cataloging.dao.persistence.AUT;
import org.folio.cataloging.dao.persistence.REF;
import org.folio.cataloging.dao.persistence.ReferenceType;
import org.folio.cataloging.dao.persistence.T_DUAL_REF;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * @author paulm
 * @version $Revision: 1.6 $, $Date: 2006/01/11 13:36:22 $
 * @since 1.0
 */
public class AuthorityCatalogDAO extends CatalogDAO {
	public static final Log logger =
		LogFactory.getLog(AuthorityCatalogDAO.class);

	public AuthorityItem getAuthorityItemByAmicusNumber(int id)
		throws DataAccessException {
		AuthorityItem item = getAuthorityItem(id);
		item.addAllTags((Tag[]) getHeaderFields(item).toArray(new Tag[0]));
		AuthorityHeadingTag heading = getHeadingField(item);
		item.addTag(heading);
		item.addAllTags(
			(Tag[]) getReferenceFields(item, heading).toArray(new Tag[0]));
		item.addAllTags((Tag[]) getAccessPointTags(item).toArray(new Tag[0]));
		item.addAllTags((Tag[]) getNotes(id).toArray(new Tag[0]));
		//TODO passare la sessione al metodo load(id)
		//item.setModelItem(new DAOAuthorityModelItem().load(id));

		Iterator iter = item.getTags().iterator();
		while (iter.hasNext()) {
			Tag ff = (Tag) iter.next();
			ff.setTagImpl(new AuthorityTagImpl());
			if (ff instanceof PersistsViaItem) {
				// set the AUT data portion of the fixed field
				 ((PersistsViaItem) ff).setItemEntity(item.getItemEntity());
			}
		}
		if (logger.isDebugEnabled())
			logger.debug(
				"Authority item: "
					+ Integer.toString(id)
					+ " successfully loaded from DB");
		return item;
	}

	private AuthorityItem getAuthorityItem(int id) throws DataAccessException {
		if (logger.isDebugEnabled())
			logger.debug("loading authority item: " + Integer.toString(id));

		AuthorityItem item = new AuthorityItem();

		//TODO fix session!
		final Session session = currentSession();
		AUT autItm = new DAOAUT().load(session, id);
		item.setAutItmData(autItm);
		return item;
	}

	/* (non-Javadoc)
	 * @see CatalogDAO#getCatalogItemByKey(java.lang.Object[])
	 */
	public CatalogItem getCatalogItemByKey(Object[] key)
		throws DataAccessException {
		return getAuthorityItemByAmicusNumber(((Integer) key[0]).intValue());
	}

	public List getHeaderFields(AuthorityItem item)
		throws DataAccessException {

		List result = new ArrayList();

		if (logger.isDebugEnabled()) {
			logger.debug(
				"loading all Fixed Fields for authority item: "
					+ item.getAmicusNumber());
		}

		result.add(new AuthorityLeader());
		result.add(new AuthorityControlNumberTag());
		result.add(new AuthorityDateOfLastTransactionTag());
		result.add(new Authority008Tag());
		result.add(new AuthorityCataloguingSourceTag());
		if (item.getItemEntity().getAuthenticationCenterStringText() != null) {
			result.add(new AuthorityAuthenticationCodeTag());
		}
		if (item.getItemEntity().getGeographicAreaStringText() != null) {
			result.add(new AuthorityGeographicAreaTag());
		}
		if (item.getItemEntity().getTypeOfDateTimeCode() != null) {
			result.add(new TimePeriodOfHeading());
		}
		return result;
	}

	private AuthorityHeadingTag getHeadingField(AuthorityItem item)
		throws DataAccessException {
		AUT autData = item.getAutItmData();
		AuthorityHeadingTag result =
			AuthorityCatalog.createHeadingTagByType(autData.getHeadingType());
		result.setItemEntity(autData);
		Descriptor heading =
			AuthorityCatalog.getDaoByType(autData.getHeadingType()).load(
				autData.getHeadingNumber(),
				AuthorityCatalog.CATALOGUING_VIEW);
		if (heading == null) {
			logger.warn("No heading found for authority item");
			throw new DataAccessException();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("heading loaded: " + heading);
		}
		result.setDescriptor(heading);
		return result;
	}

	public List getReferenceFields(
		AuthorityItem item,
		AuthorityHeadingTag heading)
		throws DataAccessException {
		List result = new ArrayList();
		Descriptor d = heading.getDescriptor();
		DAODescriptor dao = (DAODescriptor) d.getDAO();
		List refs =
			dao.getCrossReferences(
				heading.getDescriptor(),
				AuthorityCatalog.CATALOGUING_VIEW);
		Iterator iter = refs.iterator();
		while (iter.hasNext()) {
			REF anXref = (REF) iter.next();
			int refType = anXref.getType();
			if (ReferenceType.isAuthorityTag(refType)) {
				AuthorityReferenceTag t;
				if (ReferenceType.isEquivalence(refType)) {
					t = new EquivalenceReference();
				} else if (ReferenceType.isSeenFrom(refType)) {
					t = new SeeReferenceTag();
				} else {
					t = new SeeAlsoReferenceTag();
					/*
					 * set the seeAlso dual reference indicator based on the contents
					 * of the database
					 */
					REF dual = (REF) anXref.clone();
					dual.setType(ReferenceType.getReciprocal(anXref.getType()));
					if (refs.contains(dual)) {
						((SeeAlsoReferenceTag) t).setDualReferenceIndicator(
							T_DUAL_REF.YES);
							((REF)refs.get(refs.indexOf(dual))).evict();
					}
				}

				t.setReference(anXref);
				t.setDescriptor(
					anXref.getTargetDAO().load(
						anXref.getTarget(),
						AuthorityCatalog.CATALOGUING_VIEW));
				result.add(t);
			}
		}
		return result;
	}

	private List getAccessPointTags(AuthorityItem item)
		throws DataAccessException {
		if (logger.isDebugEnabled())
			logger.debug(
				"loading all access points"
					+ " for authority item: "
					+ item.getAmicusNumber());
		List apfs =
			find(
				"from "
					+ "AuthorityAccessPoint"
					+ " as t "
					+ " where t.itemNumber = ?",
				new Object[] { item.getAmicusNumber()},
				new Type[] { Hibernate.INTEGER });

		loadHeadings(apfs, AuthorityCatalog.CATALOGUING_VIEW);

		return apfs;
	}

	private List getNotes(int id) throws DataAccessException {
		if (logger.isDebugEnabled())
			logger.debug(
				"loading all Notes for authority item: "
					+ Integer.toString(id));
		
		return find(
			"from AuthorityNote as t "
				+ "where t.itemNumber = ? ",
			new Object[] { new Integer(id)},
			new Type[] { Hibernate.INTEGER });
		
//		Query query = currentSession().createSQLQuery("SELECT t.AUT_NBR,t.AUT_NTE_NBR,t.AUT_NTE_TYP_CDE,t.AUT_NTE_LANG_SCRPT_CDE,t.AUT_NTE_STRNG_TXT from AUT_NTE {t} where nte.AUT_NBR=" + id,"nte",AuthorityNote.class);
//		
//		List result = null;
//		try {
//			result = query.list();
//		} catch (HibernateException e) {
//			
//			e.printStackTrace();
//		}
//		return result;
		
	}

	private List getNoteListWithoutDuplication(int id) throws DataAccessException {
		if (logger.isDebugEnabled())
			logger.debug(
				"loading all Notes for authority item: "
					+ Integer.toString(id));

		return findNotes(id);
	}
	
	/* (non-Javadoc)
	 * @see CatalogDAO#updateCacheTable(CatalogItem)
	 */
	protected void updateCacheTable(CatalogItem item)
		throws DataAccessException {
		// do nothing -- Authorities don't have a cache table (yet)
	}


	public List getValidHeadingTypeList(String headingType, Locale locale)
		throws DataAccessException {
		return find(
			"select distinct new "
				+ "Avp(ct.code, ct.longText) "
				+ " from T_AUT_REF_HDG_TYP as ct, AuthorityCorrelation as db "
				+ " where ct.code = db.key.marcTagCategoryCode "
				+ " and db.key.headingType = ? "
				+ " and ct.language = ? ",
			new Object[] { headingType, locale.getISO3Language()},
			new Type[] { Hibernate.STRING, Hibernate.STRING });
	}

	protected void insertDeleteTable(CatalogItem item, UserProfile user)
			throws DataAccessException {
		// do nothing -- Authorities don't have a cache table (yet)
		
	}
	public List findNotes(Integer amicusNumber) {
		List notes = new ArrayList();
		ResultSet rs = null;
		try {
			Connection con = currentSession().connection();
			rs = con.createStatement().executeQuery("SELECT * from AUT_NTE where AUT_NBR=" + amicusNumber);
			while (rs.next()) 
			{
				AuthorityNote note = new AuthorityNote();
				
				note.setItemNumber(rs.getInt("AUT_NBR"));
				note.setNoteNumber(rs.getInt("AUT_NTE_NBR"));
				note.setNoteType(rs.getShort("AUT_NTE_TYP_CDE"));
				note.setNoteStringText(rs.getString("AUT_NTE_STRNG_TXT"));
				
				notes.add(note);
			} 
		} catch (Exception e) {
			return notes;
		}
		finally{
			if(rs != null) try {rs.close();} catch (SQLException e) {}
		}
		return notes;
		}
}
