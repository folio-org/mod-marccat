package org.folio.marccat.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.folio.marccat.business.cataloguing.authority.AuthorityItem;
import org.folio.marccat.business.cataloguing.bibliographic.VariableHeaderUsingItemEntity;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.business.common.UpdateStatus;
import org.folio.marccat.business.common.View;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.dao.persistence.AccessPoint;
import org.folio.marccat.dao.persistence.BibliographicNoteTag;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.ItemEntity;
import org.folio.marccat.dao.persistence.PublisherManager;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.ModMarccatException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

/**
 * Abstract class for common implementations of CatalogDAO (Bib and Auth).
 *
 * @author paulm
 * @author natasciab
 * @since 1.0
 */

public abstract class CatalogDAO extends AbstractDAO {

  public abstract CatalogItem getCatalogItemByKey(Session session, int... key);

  /**
   * Delete each tag, bibliographic item and model item from db.
   *
   * @param item    -- the item representing record to delete.
   * @param session -- the current hibernate session.
   * @throws HibernateException  in case of hibernate exception.
   * @throws DataAccessException in case of data access failure.
   */
  public void deleteCatalogItem(final CatalogItem item, final Session session) throws HibernateException {
    final Transaction transaction = getTransaction(session);
    item.getTags().stream()
        .filter(aTag -> !(aTag instanceof PublisherManager || aTag instanceof BibliographicNoteTag) && aTag instanceof Persistence)
        .forEach(tag -> {
          try {
            session.delete(tag);
          } catch (HibernateException e) {
            cleanUp(transaction);
            throw new ModMarccatException(e);
          }
        });
    new BibItemDAO().delete(item.getItemEntity(), session);
    if (item.getModelItem() != null)
      session.delete(item.getModelItem());
    transaction.commit();
  }

  abstract void updateFullRecordCacheTable(Session session, CatalogItem item) throws HibernateException;

  protected abstract void updateItemDisplayCacheTable(final CatalogItem item, final Session session) throws HibernateException;

  /**
   * For each heading in tag, load and set owner descriptor.
   *
   * @param allTags  -- list of tags to load descriptor.
   * @param userView -- the user view associated.
   * @param session  -- the current hibernate session.
   * @throws DataAccessException in case of data access failure.
   */
  protected void loadHeadings(final List<? extends PersistentObjectWithView> allTags, final int userView, final Session session) {
    allTags.forEach(tag -> loadHeading((AccessPoint) tag, userView, session));
  }

  private void loadHeading(final AccessPoint tag, final int userView, final Session session) {
    if (tag.getHeadingNumber() != null) {
      try {
        Descriptor descriptor = tag.getDAODescriptor().load(tag.getHeadingNumber(), userView, session);
        if (descriptor == null)
          throw new DataAccessException(String.format(Message.MOD_MARCCAT_00016_NO_HEADING_FOUND, tag.getHeadingNumber()));
        tag.setDescriptor(descriptor);

      } catch (HibernateException e) {
        throw new DataAccessException(String.format(Message.MOD_MARCCAT_00016_NO_HEADING_FOUND, tag.getHeadingNumber()));
      }
    }
  }

  /**
   * Saves the record, all associated tags and associated casCache.
   *
   * @param item    -- the item representing record to save.
   * @param session -- the current hibernate session.
   * @throws HibernateException in case of hibernate exception.
   */
  public void saveCatalogItem(final CatalogItem item, final Session session) throws HibernateException {

    final Transaction transaction = getTransaction(session);
    final ItemEntity itemEntity = item.getItemEntity();
    if (!itemEntity.isNew()) {
      itemEntity.setUpdateStatus(UpdateStatus.CHANGED);
    }
    persistByStatus(itemEntity, session);
    final List<Tag> tagList = item.getTags().stream().map(aTag -> {
      String myView = makeSingleViewString(item.getUserView());
      if (item instanceof AuthorityItem)
        myView = makeSingleViewString(View.DEFAULT_BIBLIOGRAPHIC_VIEW);
      try {
        if (aTag.isNew()) {
          aTag.setItemNumber(item.getAmicusNumber());
          if (aTag instanceof PersistentObjectWithView)
            ((PersistentObjectWithView) aTag).setUserViewString(myView);
          aTag.generateNewKey(session);
          if (item.getDeletedTags().contains(aTag)) {
            aTag.reinstateDeletedTag();
          }
        }
        if (aTag instanceof Persistence) {
          persistByStatus((Persistence) aTag, session);
        }
      } catch (HibernateException | SQLException e) {
        throw new ModMarccatException(e);
      }
      return aTag;
    }).collect(Collectors.toList());

    final List<Tag> toRemove = new ArrayList<>(item.getDeletedTags());
    toRemove.forEach(aTag -> {
      if (!tagList.contains(aTag)) {
        if (aTag instanceof Persistence && (!(aTag instanceof BibliographicNoteTag))) {
          try {
            persistByStatus((Persistence) aTag, session);
          } catch (HibernateException e) {
            throw new ModMarccatException(e);
          }
        }

        if (aTag instanceof VariableHeaderUsingItemEntity) {
          ((VariableHeaderUsingItemEntity) aTag)
              .deleteFromItem();
        }
      }
      item.getDeletedTags().remove(aTag);
      item.getTags().remove(aTag);
    });

    if (item.getModelItem() != null) {
      BibliographicModelItemDAO dao = new BibliographicModelItemDAO();
      if (dao.getModelUsageByItem(item.getAmicusNumber(), session)) {
        item.getModelItem().setUpdateStatus(UpdateStatus.CHANGED);
      } else {
        item.getModelItem().markNew();
      }

      persistByStatus(item.getModelItem(), session);
    }
    updateItemDisplayCacheTable(item, session);
    transaction.commit();
  }
}
