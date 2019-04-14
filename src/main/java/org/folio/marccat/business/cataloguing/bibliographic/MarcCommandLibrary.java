package org.folio.marccat.business.cataloguing.bibliographic;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.cataloguing.common.Browsable;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.descriptor.PublisherTagDescriptor;
import org.folio.marccat.dao.DAODescriptor;
import org.folio.marccat.dao.persistence.*;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.MarcCorrelationException;
import org.folio.marccat.util.StringText;

import java.sql.SQLException;

import static org.folio.marccat.util.F.deepCopy;

/**
 * Command library for operation about tags
 */
public class MarcCommandLibrary {

  private MarcCommandLibrary() {
  }

  /**
   * Moved here from {@link }
   *
   * @param catalogItem
   * @param srcTag
   * @param replacingDescriptor
   * @return
   * @throws DataAccessException
   */
  public static Tag replaceDescriptor(CatalogItem catalogItem, Tag srcTag, Descriptor replacingDescriptor) throws DataAccessException {
    Tag newTag = (Tag) deepCopy(srcTag);
    if (newTag instanceof PersistsViaItem) {
      ((PersistsViaItem) newTag).setItemEntity(
        ((PersistsViaItem) srcTag).getItemEntity());
    }
    newTag.markNew();
    if (!srcTag.isNew()) {
      srcTag.markDeleted();
      catalogItem.getDeletedTags().add(srcTag);
    }

    replaceDescriptor((Browsable) newTag, replacingDescriptor);

    catalogItem.setTag(srcTag, newTag);
    return newTag;
  }

  /**
   * Moved here from {@link }
   *
   * @param tag
   * @param newDescriptor
   * @throws DataAccessException getMarcEncoding verify the new correlation:
   *                             if it is a wrong correlation a MarcCorrelationException occurs.
   *                             See the Subject with correlation 19, ?, 14
   */
  private static void replaceDescriptor(Browsable tag, Descriptor newDescriptor) throws DataAccessException {
    Descriptor oldDescriptor = tag.getDescriptor();

    tag.setDescriptor(newDescriptor);

    try {
      if (((Tag) tag).getMarcEncoding() == null)
        throw new MarcCorrelationException();
    } catch (MarcCorrelationException me) {
      tag.setDescriptor(oldDescriptor);
      throw new DataAccessException();
    } catch (DataAccessException dae) {
      tag.setDescriptor(oldDescriptor);
      throw dae;
    }

    /* -- End block -- */

    tag.setHeadingNumber(
      (newDescriptor.getKey().getHeadingNumber()));
  }

  public static Tag replaceTagWithClone(CatalogItem catalogItem, Tag srcTag) {
    Tag newTag = (Tag) deepCopy(srcTag);
    if (newTag instanceof PersistsViaItem) {
      ((PersistsViaItem) newTag).setItemEntity(
        ((PersistsViaItem) srcTag).getItemEntity());
    }
    newTag.markNew();
    if (!srcTag.isNew()) {
      srcTag.markDeleted();
      catalogItem.addDeletedTag(srcTag);
    }

    catalogItem.setTag(srcTag, newTag);
    return newTag;
  }

  public static Descriptor createNewDescriptor(final Descriptor currDescriptor, final String headingView, final Session session) throws DataAccessException, HibernateException, SQLException {
    if (!currDescriptor.isNew()) return currDescriptor;
    Descriptor matchDescriptor = ((DAODescriptor) currDescriptor.getDAO()).getMatchingHeading(currDescriptor, session);
    if (matchDescriptor == null) {
      if (currDescriptor.getKey().getHeadingNumber() == -1) {// key is not null by default
        currDescriptor.generateNewKey(session);
        currDescriptor.getKey().setUserViewString(headingView);
      }
      currDescriptor.getDAO().persistByStatus(currDescriptor, session);
      return currDescriptor;
    } else {
      return matchDescriptor;
    }
  }

  public static void setNewStringText(AccessPoint tag, StringText text, String headingView,
                                      final Session session) throws HibernateException, SQLException {
    tag.getDescriptor().setUserViewString(headingView);
    tag.setDescriptorStringText(text);
    Descriptor newDescriptor = null;
    if (tag.getDescriptor() instanceof PublisherTagDescriptor) {
      PUBL_TAG pu = ((PublisherTagDescriptor) tag.getDescriptor()).getPublisherTagUnits().get(0);
      newDescriptor = createNewDescriptor(pu.getDescriptor(), headingView, session);
      pu.setDescriptor((PUBL_HDG) newDescriptor);
    } else {
      newDescriptor = createNewDescriptor(tag.getDescriptor(), headingView, session);
      tag.setDescriptor(newDescriptor);
      tag.setHeadingNumber((newDescriptor.getKey().getHeadingNumber()));
    }
  }

  public static void setNewStringText(final PublisherManager tag, final StringText text, final String headingView, final Session session) throws HibernateException, SQLException {
    PublisherAccessPoint pap = tag.getApf();
    setNewStringText(pap, text, headingView, session);
  }

}
