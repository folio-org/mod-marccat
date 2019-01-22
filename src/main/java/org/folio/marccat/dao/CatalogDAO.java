package org.folio.marccat.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.folio.marccat.business.cataloguing.authority.AuthorityReferenceTag;
import org.folio.marccat.business.cataloguing.bibliographic.VariableHeaderUsingItemEntity;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.business.common.UpdateStatus;
import org.folio.marccat.business.controller.UserProfile;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.MessageCatalog;
import org.folio.marccat.dao.persistence.*;
import org.folio.marccat.exception.DataAccessException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract class for common implementations of CatalogDAO (Bib and Auth).
 *
 * @author paulm
 * @author natasciab
 * @since 1.0
 */

public abstract class CatalogDAO extends AbstractDAO {
  private static Log logger = new Log(CatalogDAO.class);

  public abstract CatalogItem getCatalogItemByKey(Session session, int... key) throws DataAccessException;

  /**
   * Delete each tag, bibliographic item and model item from db.
   *
   * @param item    -- the item representing record to delete.
   * @param session -- the current hibernate session.
   * @throws HibernateException  in case of hibernate exception.
   * @throws DataAccessException in case of data access failure.
   */
  public void deleteCatalogItem(final CatalogItem item, final Session session) throws HibernateException, DataAccessException {
    final Transaction transaction = getTransaction(session);
    item.getTags().stream()
      .filter(aTag -> aTag instanceof Persistence && !(aTag instanceof AuthorityReferenceTag || aTag instanceof PublisherManager || aTag instanceof BibliographicNoteTag))
      .forEach(tag -> {
        try {
            tag.markDeleted();
            persistByStatus((Persistence) tag, session);
        } catch (HibernateException e) {
          cleanUp(transaction);
          throw new RuntimeException(e);
        }
      });
    session.delete(item.getItemEntity());
    if (item.getModelItem() != null)
      session.delete(item.getModelItem());
    transaction.commit();
  }

  abstract void updateFullRecordCacheTable(Session session, CatalogItem item) throws HibernateException;

  abstract protected void updateItemDisplayCacheTable(final CatalogItem item, final Session session) throws HibernateException;

  abstract protected void insertDeleteTable(final CatalogItem item, final UserProfile user) throws DataAccessException;

  /**
   * For each heading in tag, load and set owner descriptor.
   *
   * @param allTags  -- list of tags to load descriptor.
   * @param userView -- the user view associated.
   * @param session  -- the current hibernate session.
   * @throws DataAccessException in case of data access failure.
   */
  protected void loadHeadings(final List<? extends PersistentObjectWithView> allTags, final int userView, final Session session) throws DataAccessException {
    allTags.forEach(tag -> {
      loadHeading((AccessPoint) tag, userView, session);
    });
  }

  private void loadHeading(final AccessPoint tag, final int userView, final Session session) throws DataAccessException {
    if (tag.getHeadingNumber() != null) {
      try {
        Descriptor descriptor = tag.getDAODescriptor().load(tag.getHeadingNumber().intValue(), userView, session);
        if (descriptor == null)
          throw new DataAccessException(String.format(MessageCatalog._00016_NO_HEADING_FOUND, tag.getHeadingNumber()));
        tag.setDescriptor(descriptor);

      } catch (HibernateException e) {
        throw new DataAccessException(String.format(MessageCatalog._00016_NO_HEADING_FOUND, tag.getHeadingNumber()));
      }
    }
  }


  /**
   * Updates or creates a CasCache associated to an amicus number.
   *
   * @param amicusNumber -- the amicus number id.
   * @param casCache     -- the casCache associated.
   * @param session      -- the current hibernate session.
   * @throws HibernateException in case of hibernate exception.
   */
  protected void saveCasCache(final int amicusNumber, CasCache casCache, final Session session) throws HibernateException {
    final CasCacheDAO casCacheDAO = new CasCacheDAO();
    casCacheDAO.persistCasCache(amicusNumber, casCache, session);
  }

  /**
   * Updates bibliographic note table for amicus number.
   *
   * @param amicusNumber -- the amicus number id.
   * @param noteNumber   -- the note number id.
   * @param session      -- the current hibernate session.
   * @throws HibernateException in case of hibernate exception.
   * @throws SQLException       in case of sql exception.
   */
  public void updateBibNote(final int amicusNumber, final int noteNumber, final Session session) throws HibernateException, SQLException {
    final Transaction transaction = getTransaction(session);
    CallableStatement proc = null;
    try {
      Connection connection = session.connection();
      proc = connection.prepareCall("{call AMICUS.updateNotaStandard(?, ?) }");
      proc.setInt(1, amicusNumber);
      proc.setInt(2, noteNumber);
      proc.execute();

    } finally {
      try {
        if (proc != null) proc.close();
      } catch (SQLException ex) {
      }
    }
    transaction.commit();
  }

  /**
   * Updates note standard tags.
   *
   * @param item    -- the item representing record.
   * @param session -- the current hibernate session.
   * @throws HibernateException in case of hibernate exception.
   */
  public void modifyNoteStandard(final CatalogItem item, final Session session) throws HibernateException {
    final int amicusNumber = item.getItemEntity().getAmicusNumber().intValue();
    item.getTags().stream()
      .filter(aTag -> aTag instanceof BibliographicNoteTag && ((BibliographicNoteTag) aTag).isStandardNoteType())
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
   * Saves the record, all associated tags and associated casCache.
   *
   * @param item     -- the item representing record to save.
   * @param casCache -- the management data associated to record.
   * @param session  -- the current hibernate session.
   * @throws HibernateException in case of hibernate exception.
   */
  public void saveCatalogItem(final CatalogItem item, final CasCache casCache, final Session session) throws HibernateException {

    final Transaction transaction = getTransaction(session);
    final String myView = makeSingleViewString(item.getUserView());
    final ItemEntity itemEntity = item.getItemEntity();

    final List<Tag> tagList = item.getTags().stream().map(aTag -> {

      if (aTag.isNew()) {
        aTag.setItemNumber(item.getAmicusNumber().intValue());
        if (aTag instanceof PersistentObjectWithView)
          ((PersistentObjectWithView) aTag).setUserViewString(myView);

        try {
          aTag.generateNewKey(session);
        } catch (HibernateException e) {
          throw new RuntimeException(e);
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }

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
    toRemove.forEach(aTag -> {
      if (!tagList.contains(aTag)) {
        if (aTag instanceof Persistence) {
          try {
            persistByStatus((Persistence) aTag, session);
          } catch (HibernateException e) {
            throw new RuntimeException(e);
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
      BibliographicModelItemDAO dao = new BibliographicModelItemDAO();
      if (dao.getModelUsageByItem(item.getAmicusNumber(), session)) {
        item.getModelItem().setUpdateStatus(UpdateStatus.CHANGED);
      } else {
        item.getModelItem().markNew();
      }

      persistByStatus(item.getModelItem(), session);
    }

    if (casCache != null)
      saveCasCache(itemEntity.getAmicusNumber(), casCache, session);

    updateItemDisplayCacheTable(item, session);
    modifyNoteStandard(item, session);
    transaction.commit();

  }
}
