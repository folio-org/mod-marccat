package org.folio.cataloging.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.cataloguing.authority.AuthorityReferenceTag;
import org.folio.cataloging.business.cataloguing.bibliographic.BibliographicNoteTag;
import org.folio.cataloging.business.cataloguing.common.AccessPoint;
import org.folio.cataloging.business.cataloguing.common.CatalogItem;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.*;
import org.folio.cataloging.business.controller.UserProfile;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.common.TransactionalHibernateOperation;
import org.folio.cataloging.dao.persistence.CasCache;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public abstract class CatalogDAO extends HibernateUtil {
	private static Log logger = LogFactory.getLog(CatalogDAO.class);
	private CasCache casCache;

	public CasCache getCasCache() {
		return casCache;
	}

	public void setCasCache(CasCache cas) {
		this.casCache = cas;
	}

	abstract public CatalogItem getCatalogItemByKey(Object[] key) throws DataAccessException;

	public void deleteCatalogItem(final CatalogItem item, final UserProfile user) throws DataAccessException 
	{
//TODO Questa Insert deve essere Assolutamente reinserita per il cliente CASALINI
//		insertDeleteTable(item, user);
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s) throws HibernateException, DataAccessException 
			{
				Iterator iter = item.getTags().iterator();
				Tag aTag = null;
				while (iter.hasNext()) {
					aTag = (Tag) iter.next();
					if (aTag instanceof Persistence && !(aTag instanceof AuthorityReferenceTag)) {
						aTag.markDeleted();
						persistByStatus((Persistence) aTag);
					}
				}
				//delete the bib_itm itself
				item.getItemEntity().markDeleted();
				persistByStatus(item.getItemEntity());
				if (item.getModelItem() != null) {
					item.getModelItem().markDeleted();
					persistByStatus(item.getModelItem());
				}
			}
		}
		.execute();
	}

	abstract protected void updateCacheTable(final CatalogItem item) throws DataAccessException;
	
	abstract protected void insertDeleteTable(final CatalogItem item, final UserProfile user) throws DataAccessException;
    
	protected void loadHeadings(final List allTags, final int userView) throws DataAccessException {
		final Iterator iterator = allTags.iterator();
		while (iterator.hasNext()) {
			AccessPoint tag = (AccessPoint) iterator.next();
			loadHeading(tag, userView);
		}
	}

	private void loadHeading(AccessPoint tag, int userView) throws DataAccessException {
		if (tag.getHeadingNumber() != null) {
			Descriptor heading = tag.getDAODescriptor().load(tag.getHeadingNumber().intValue(), userView);
			if (heading == null)
				throw new DataAccessException("No heading found for heading nbr:" + tag.getHeadingNumber());
			if (logger.isDebugEnabled()) {
				logger.debug("heading loaded: " + heading);
			}
			tag.setDescriptor(heading);
		}
	}

	
	
	public void saveCatalogItem(final CatalogItem item)
	throws DataAccessException {

	logger.debug("Saving catalog item " + item.getAmicusNumber());
	new TransactionalHibernateOperation() {
		public void doInHibernateTransaction(Session s)
			throws DataAccessException {
			String myView = makeSingleViewString(item.getUserView());
			Tag aTag = null;
			Iterator iter = item.getTags().iterator();
			while (iter.hasNext()) {
				//TODO if apf data is changed check for match in db
				aTag = (Tag) iter.next();
				logger.debug("saving tag " + aTag);
				if (aTag.isNew()) {
					aTag.setItemNumber(item.getAmicusNumber().intValue());
					logger.debug(
						"tag is new: item # is " + aTag.getItemNumber());
					if (aTag instanceof PersistentObjectWithView) {
						(
							(
								PersistentObjectWithView) aTag)
									.setUserViewString(
							myView);
						logger.debug(
							"            view is "
								+ ((PersistentObjectWithView) aTag)
									.getUserViewString());
					}
					aTag.generateNewKey();
					
					
					/*
					 * This is a first attempt to detect the situation where a 
					 * series of changes result in returning the new key back
					 * to a pre-existing key
					 */
					//Modificato da carmen
					if (item.getDeletedTags().contains(aTag)) {
						aTag.reinstateDeletedTag();
					}
				}

				if (aTag instanceof Persistence) {
					persistByStatus((Persistence) aTag);
				}
				
			}
			iter = item.getDeletedTags().iterator();
			while (iter.hasNext()) {
				aTag = (Tag) iter.next();
				if (!item.getTags().contains(aTag)) {
					if (aTag instanceof Persistence) {
						persistByStatus((Persistence) aTag);
					}
/*					if (aTag instanceof VariableHeaderUsingItemEntity) {
						((VariableHeaderUsingItemEntity) aTag)
							.deleteFromItem();
					}
*/
				}
				iter.remove();
			}
			/*
			 * always update the ItemEntity itself regardless of state
			 */
			if (!item.getItemEntity().isNew()) {
				item.getItemEntity().setUpdateStatus(UpdateStatus.CHANGED);
			}
			persistByStatus(item.getItemEntity());
			if (item.getModelItem() != null) {
				persistByStatus(item.getModelItem());
			}
//------------>	Anche per gli altri clienti deve scrivere la S_CAS_CACHE
//			if(getCasCache()!=null && casaliniEnabled) {
			if(getCasCache()!=null) {
				saveCasCache(item.getItemEntity().getAmicusNumber().intValue());
			}
			
		}
	}
	.execute();
	updateCacheTable(item);
	modifyNoteStandard(item);
	

}


//	private void refineTagsList(List tags)
//	{
//		Iterator iter = tags.iterator();
//		
//		while(iter.hasNext())
//		{
//			Object o = iter.next();
//			
//			if (o instanceof AuthorityNote)
//			{
//				iter.remove();
//			}
//		}
//		
//	}
	
	
	protected void saveCasCache(int amicusNumber) throws DataAccessException
	{
		persistCasCacheLevel(amicusNumber);
	}
	
	protected void persistCasCacheLevel(int amicusNumber) throws DataAccessException 
	{
		//DAOCasCache cdao = new DAOCasCache();
		//cdao.persistCasCache(amicusNumber, casCache);
	}
	
	public void updateBibNoteTable(final int bibItemNumber, final int numNote) throws DataAccessException 
	{
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s) throws SQLException, HibernateException, CacheUpdateException 
			{
				int result;
				CallableStatement proc = null;
				try {
					Connection connection = s.connection();
					proc = connection.prepareCall("{call AMICUS.updateNotaStandard(?, ?) }");
					proc.setInt(1, bibItemNumber);
					proc.setInt(2, numNote);
					proc.execute();
					
				} finally {
					try {
						if(proc!=null) proc.close();
					} catch (SQLException ex) {
						// TODO _MIKE Auto-generated catch block
						ex.printStackTrace();
					}
				}
			}
		}
		.execute();
	}
	
	public void modifyNoteStandard(final CatalogItem item) throws DataAccessException 
	{
		Tag aTag = null;
		Iterator iter = item.getTags().iterator();
		while (iter.hasNext()) {
			// TODO if apf data is changed check for match in db
			aTag = (Tag) iter.next();
			if (aTag instanceof BibliographicNoteTag) {
				BibliographicNoteTag note = (BibliographicNoteTag) aTag;
				if (note.isStandardNoteType()) {
					updateBibNoteTable(item.getItemEntity().getAmicusNumber().intValue(), note.getNoteNbr());
				}
			}
		}
	}
}