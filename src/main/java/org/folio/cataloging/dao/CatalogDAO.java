package org.folio.cataloging.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.cataloguing.authority.AuthorityReferenceTag;
import org.folio.cataloging.business.cataloguing.bibliographic.BibliographicNoteTag;
import org.folio.cataloging.business.cataloguing.bibliographic.VariableHeaderUsingItemEntity;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.*;
import org.folio.cataloging.business.controller.UserProfile;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.dao.common.TransactionalHibernateOperation;
import org.folio.cataloging.dao.persistence.AccessPoint;
import org.folio.cataloging.dao.persistence.CasCache;
import org.folio.cataloging.dao.persistence.CatalogItem;
import org.folio.cataloging.dao.persistence.ItemEntity;
import org.folio.cataloging.integration.log.MessageCatalogStorage;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * Abstract class for common implementations of CatalogDAO (Bib and Auth).
 *
 * @author paulm
 * @author natasciab
 * @since 1.0
 */

public abstract class CatalogDAO extends AbstractDAO {
	private static Log logger = LogFactory.getLog(CatalogDAO.class);
	abstract public CatalogItem getCatalogItemByKey(Object[] key) throws DataAccessException;

	/**
	 * Delete each tag, bibliographic item and model item from db.
	 *
	 * @param item -- the item representing record to delete.
	 * @param session -- the current hibernate session.
	 * @throws HibernateException in case of hibernate exception.
	 * @throws DataAccessException in case of data access failure.
	 */
	public void deleteCatalogItem(final CatalogItem item, final Session session) throws HibernateException, DataAccessException
	{
		final Transaction transaction = getTransaction(session);
		item.getTags().stream()
				.filter(aTag -> aTag instanceof Persistence && !(aTag instanceof AuthorityReferenceTag))
				.forEach(tag -> {
					try {
						session.delete(tag);
					} catch (HibernateException e) {
						cleanUp(transaction);
						throw new RuntimeException(e);
					}
				});

		session.delete(item.getItemEntity());
		session.delete(item.getModelItem());
		transaction.commit();
	}

	abstract protected void updateCacheTable(final CatalogItem item, final Session session) throws DataAccessException;
	
	abstract protected void insertDeleteTable(final CatalogItem item, final UserProfile user) throws DataAccessException;

	/**
	 * For each heading in tag, load and set owner descriptor.
	 *
	 * @param allTags -- list of tags to load descriptor.
	 * @param userView -- the user view associated.
	 * @param session -- the current hibernate session.
	 * @throws DataAccessException in case of data access failure.
	 */
	protected void loadHeadings(final List<? extends PersistentObjectWithView> allTags, final int userView, final Session session) throws DataAccessException {
		allTags.stream().map(tag -> {
			loadHeading((AccessPoint) tag, userView, session);
			return tag;
		}).collect(Collectors.toList());
	}
	private void loadHeading(final AccessPoint tag, final int userView, final Session session) throws DataAccessException {
		if (tag.getHeadingNumber() != null) {
			ofNullable(tag.getDAODescriptor().load(tag.getHeadingNumber().intValue(), userView)).map(heading -> { //TODO: pass session as parameter
				tag.setDescriptor(heading);
				return tag;
			}).orElseGet(() -> {
				throw new DataAccessException(String.format(MessageCatalogStorage._00016_NO_HEADING_FOUND, tag.getHeadingNumber()));
			});
		}
	}

	/**
	 * Saves the record, all associated tags and associated casCache.
	 *
	 * @param item -- the item representing record to save.
	 * @param casCache -- the management data associated to record.
	 * @param session -- the current hibernate session.
	 * @throws HibernateException in case of hibernate exception.
	 */
	public void saveCatalogItem(final CatalogItem item, final CasCache casCache, final Session session) throws HibernateException {

		final Transaction transaction = getTransaction(session);
		final String myView = makeSingleViewString(item.getUserView());
		final ItemEntity itemEntity = item.getItemEntity();

		final List<Tag> tagList = item.getTags().stream().map (aTag -> {
			if (aTag.isNew()) {
				aTag.setItemNumber(item.getAmicusNumber().intValue());
				if (aTag instanceof PersistentObjectWithView)
					((PersistentObjectWithView) aTag).setUserViewString(myView);

				aTag.generateNewKey();
				if (item.getDeletedTags().contains(aTag)) {
					aTag.reinstateDeletedTag();
				}
			}

			if (aTag instanceof Persistence) {
				try {
					persistByStatus((Persistence) aTag, session);
				} catch (HibernateException e) {
					throw new RuntimeException(e);
				}
			}
			return aTag;
		}).collect(Collectors.toList());

		final List<Tag> toRemove = new ArrayList<>(item.getDeletedTags());
		toRemove.stream().forEach(aTag -> {
			if (!tagList.contains(aTag)){
				if (aTag instanceof Persistence) {
					if (aTag instanceof Persistence) {
						try {
							persistByStatus((Persistence) aTag, session);
						} catch (HibernateException e) {
							throw new RuntimeException(e);
						}
					}
				}

				if (aTag instanceof VariableHeaderUsingItemEntity) {
					((VariableHeaderUsingItemEntity) aTag)
							.deleteFromItem();
				}
			}
			item.getDeletedTags().remove(aTag);
		});

		if (!itemEntity.isNew()) {
			itemEntity.setUpdateStatus(UpdateStatus.CHANGED);
		}
		persistByStatus(itemEntity, session);
		if (item.getModelItem() != null) {
			persistByStatus(item.getModelItem(), session);
		}

		if (casCache != null)
			saveCasCache(itemEntity.getAmicusNumber().intValue(), casCache, session);

		updateCacheTable(item, session);
		modifyNoteStandard(item, session);
		transaction.commit();

	}

	/**
	 * Updates or creates a CasCache associated to an amicus number.
	 *
	 * @param amicusNumber -- the amicus number id.
	 * @param casCache -- the casCache associated.
	 * @param session -- the current hibernate session.
	 * @throws HibernateException in case of hibernate exception.
	 */
	protected void saveCasCache(final int amicusNumber, CasCache casCache, final Session session) throws HibernateException
	{
		final CasCacheDAO casCacheDAO = new CasCacheDAO();
		casCacheDAO.persistCasCache(amicusNumber, casCache, session);
	}

	/**
	 * Updates note standard tags.
	 *
	 * @param item -- the item representing record.
	 * @param session -- the current hibernate session.
	 * @throws HibernateException in case of hibernate exception.
	 */
	public void modifyNoteStandard(final CatalogItem item, final Session session) throws HibernateException
	{
		final int amicusNumber = item.getItemEntity().getAmicusNumber().intValue();
		item.getTags().stream()
				.filter(aTag -> aTag instanceof BibliographicNoteTag && ((BibliographicNoteTag)aTag).isStandardNoteType())
				.forEach(tag -> {
					try {
						updateBibNote(amicusNumber, ((BibliographicNoteTag) tag).getNoteNbr(), session);
					} catch (HibernateException he) {
						throw new RuntimeException(he);
					} catch (SQLException sqle) {
						throw new RuntimeException(sqle);
					}
				});
	}

	/**
	 * Updates bibliographic note table for amicus number.
	 *
	 * @param amicusNumber -- the amicus number id.
	 * @param noteNumber -- the note number id.
	 * @param session -- the current hibernate session.
	 * @throws HibernateException in case of hibernate exception.
	 * @throws SQLException in case of sql exception.
	 */
	public void updateBibNote(final int amicusNumber, final int noteNumber, final Session session) throws HibernateException, SQLException
	{
		final Transaction transaction = getTransaction(session);
		CallableStatement proc = null;
		try {
			Connection connection = session.connection();
			proc = connection.prepareCall("{call AMICUS.updateNotaStandard(?, ?) }");
			proc.setInt(1, amicusNumber);
			proc.setInt(2, noteNumber);
			proc.execute();

		}finally {
			try { if(proc!=null) proc.close();} catch (SQLException ex) {
				// TODO: ignore or print exception?
			}
		}
		transaction.commit();
	}

	// ----------- deprecated methods
	@Deprecated
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
	@Deprecated
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

	@Deprecated
	public void saveCatalogItem(final CatalogItem item) throws DataAccessException {
		/*logger.debug("Saving catalog item " + item.getAmicusNumber());
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
					throws DataAccessException {
				String myView = makeSingleViewString(item.getUserView());
				Tag aTag = null;
				Iterator iter = item.getTags().iterator();
				while (iter.hasNext()) {
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
						if (aTag instanceof VariableHeaderUsingItemEntity) {
							((VariableHeaderUsingItemEntity) aTag)
								.deleteFromItem();
						}

					}
					iter.remove();
				}

				if (!item.getItemEntity().isNew()) {
					item.getItemEntity().setUpdateStatus(UpdateStatus.CHANGED);
				}
				persistByStatus(item.getItemEntity());
				if (item.getModelItem() != null) {
					persistByStatus(item.getModelItem());
				}

				if(getCasCache()!=null) {
					saveCasCache(item.getItemEntity().getAmicusNumber().intValue());
				}

			}
		}
				.execute();
		updateCacheTable(item);
		modifyNoteStandard(item);*/


	}

	@Deprecated
	protected void loadHeadings(final List allTags, final int userView) throws DataAccessException {
		final Iterator iterator = allTags.iterator();
		while (iterator.hasNext()) {
			AccessPoint tag = (AccessPoint) iterator.next();
			loadHeading(tag, userView);
		}
	}

	@Deprecated
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
}