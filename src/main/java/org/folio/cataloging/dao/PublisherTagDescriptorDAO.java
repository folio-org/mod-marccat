package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.business.descriptor.PublisherTagDescriptor;
import org.folio.cataloging.dao.persistence.Descriptor;
import org.folio.cataloging.dao.persistence.PUBL_HDG;
import org.folio.cataloging.dao.persistence.PUBL_TAG;

import java.util.List;

/**
 * Data access object to Publisher Tag.
 * @since 1.0
 * @author natasciab
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
   * @param headingNumber -- the heading number.
   * @param cataloguingView -- the cataloguing view.
   * @param clazz -- the persistent class associated.
   * @param session -- the hibernate session.
   * @return descriptor associated to publisher heading.
   * @throws DataAccessException in case of data access exception.
   * @throws HibernateException in case of hibernate exception.
   */
	@Override
	public Descriptor load(final int headingNumber, final int cataloguingView, final Class clazz, final Session session) throws DataAccessException, HibernateException {
		final PublisherTagDescriptor descriptor = new PublisherTagDescriptor();
    descriptor.setHeadingNumber(headingNumber);
    descriptor.setUserViewString(View.makeSingleViewString(cataloguingView));
    final List<PUBL_TAG> multiView = session.find("from PUBL_TAG as t "
				+ " where t.publisherTagNumber = ? "
				+ " and substr(t.userViewString, ?, 1) = '1' "
				+ " order by t.sequenceNumber ", new Object[] {
				new Integer(headingNumber), new Integer(cataloguingView) },
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });
		final List<PUBL_TAG> singleView = (List<PUBL_TAG>) isolateViewForList(multiView, cataloguingView, session);

		singleView.forEach(publTag -> {
      if (publTag.getPublisherHeadingNumber() != null) {
        try {
          PUBL_HDG publHdg = (PUBL_HDG) publTag.getDescriptorDAO().load((publTag.getPublisherHeadingNumber()).intValue(), cataloguingView, session);
          publTag.setDescriptor(publHdg);
          descriptor.getPublisherTagUnits().add(publTag);
        } catch (HibernateException e) {
          throw new RuntimeException(e);
        }
      }
    });

		return descriptor;
	}
}
