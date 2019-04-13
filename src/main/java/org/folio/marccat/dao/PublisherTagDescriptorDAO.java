package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.common.View;
import org.folio.marccat.business.descriptor.PublisherTagDescriptor;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.PUBL_HDG;
import org.folio.marccat.dao.persistence.PUBL_TAG;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.ModMarccatException;

import java.util.List;

/**
 * Data access object to Publisher Tag.
 *
 * @author natasciab
 * @since 1.0
 */

@SuppressWarnings("unchecked")
public class PublisherTagDescriptorDAO extends DAODescriptor {


  /**
   * Return persistent class associated to publisher tag descriptor.
   */
  @Override
  public Class getPersistentClass() {
    return PublisherTagDescriptor.class;
  }

  /**
   * Loads headings associated to Descriptor.
   *
   * @param keyNumber   -- the heading number.
   * @param cataloguingView -- the cataloguing view.
   * @param clazz           -- the persistent class associated.
   * @param session         -- the hibernate session.
   * @return descriptor associated to publisher heading.
   * @throws DataAccessException in case of data access exception.
   * @throws HibernateException  in case of hibernate exception.
   */
  @Override
  public Descriptor load(final int keyNumber, final int cataloguingView, final Class clazz, final Session session) throws HibernateException {
    final PublisherTagDescriptor descriptor = new PublisherTagDescriptor();
    descriptor.setKeyNumber(keyNumber);
    descriptor.setUserViewString(View.makeSingleViewString(cataloguingView));
    final List<PUBL_TAG> multiView = session.find("from PUBL_TAG as t "
        + " where t.publisherTagNumber = ? "
        + " and t.userViewString = '" + View.makeSingleViewString(cataloguingView) + "' "
        + " order by t.sequenceNumber ", new Object[]{
        keyNumber},
      new Type[]{Hibernate.INTEGER});
    final List<PUBL_TAG> singleView = (List<PUBL_TAG>) isolateViewForList(multiView, cataloguingView, session);

    singleView.forEach((PUBL_TAG publTag) -> {
      if (publTag.getPublisherHeadingNumber() != null) {
        try {
          PUBL_HDG publHdg = (PUBL_HDG) publTag.getDescriptorDAO().load((publTag.getPublisherHeadingNumber()).intValue(), cataloguingView, session);
          publTag.setDescriptor(publHdg);
          descriptor.getPublisherTagUnits().add(publTag);
        } catch (HibernateException e) {
          throw new ModMarccatException(e);
        }
      }
    });

    return descriptor;
  }
}
