package org.folio.marccat.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.folio.marccat.business.cataloguing.authority.AuthorityReferenceTag;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.business.controller.UserProfile;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.MessageCatalog;
import org.folio.marccat.dao.common.TransactionalHibernateOperation;
import org.folio.marccat.dao.persistence.AccessPoint;
import org.folio.marccat.dao.persistence.BibliographicNoteTag;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.exception.CacheUpdateException;
import org.folio.marccat.exception.DataAccessException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

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


  // ----------- deprecated methods
  @Deprecated
  public void updateBibNoteTable(final int bibItemNumber, final int numNote) throws DataAccessException {
    new TransactionalHibernateOperation() {
      public void doInHibernateTransaction(Session s) throws SQLException, HibernateException, CacheUpdateException {
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
            if (proc != null) proc.close();
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
  public void modifyNoteStandard(final CatalogItem item) throws DataAccessException {
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
      if (heading == null) {
        throw new DataAccessException("No heading found for heading nbr:" + tag.getHeadingNumber());
      }
      logger.debug("heading loaded: " + heading);
      tag.setDescriptor(heading);
    }
  }
}
