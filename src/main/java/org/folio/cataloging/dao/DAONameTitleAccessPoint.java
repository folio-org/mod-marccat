package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.dao.common.TransactionalHibernateOperation;
import org.folio.cataloging.dao.persistence.NME_TTL_HDG;
import org.folio.cataloging.dao.persistence.NameAccessPoint;
import org.folio.cataloging.dao.persistence.NameTitleAccessPoint;
import org.folio.cataloging.dao.persistence.TitleAccessPoint;

/**
 * Data access object to Name-Title access point.
 *
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class DAONameTitleAccessPoint extends AbstractDAO {

  public void delete(final Persistence p) throws DataAccessException {

    super.delete (p);
    new TransactionalHibernateOperation ( ) {
      public void doInHibernateTransaction(Session s)
        throws HibernateException {
        NameTitleAccessPoint a = (NameTitleAccessPoint) p;

        /*
         * delete the "redundant" access point entries in NME_ACS_PNT and TTL_ACS_PNT
         */
        s.delete (
          "from NameAccessPoint as n "
            + " where n.nameTitleHeadingNumber = ? and "
            + " n.userViewString = ? ",
          new Object[]{a.getHeadingNumber ( ), a.getUserViewString ( )},
          new Type[]{Hibernate.INTEGER, Hibernate.STRING});

        s.delete (
          "from TitleAccessPoint as n "
            + " where n.nameTitleHeadingNumber = ? and "
            + " n.userViewString = ? ",
          new Object[]{a.getHeadingNumber ( ), a.getUserViewString ( )},
          new Type[]{Hibernate.INTEGER, Hibernate.STRING});
      }
    }
      .execute ( );
  }

  /* (non-Javadoc)
   * @see HibernateUtil#save(librisuite.business.common.Persistence)
   */
  public void save(Persistence p)
    throws DataAccessException {

    super.save (p);
    NameTitleAccessPoint nt = (NameTitleAccessPoint) p;
    NameAccessPoint a = new NameAccessPoint (nt.getItemNumber ( ));
    a.setNameTitleHeadingNumber (nt.getHeadingNumber ( ).intValue ( ));
    a.setHeadingNumber (
      new Integer (
        ((NME_TTL_HDG) nt.getDescriptor ( )).getNameHeadingNumber ( )));
    a.setUserViewString (nt.getUserViewString ( ));
    a.setFunctionCode ((short) 0);
    persistByStatus (a);
    TitleAccessPoint b = new TitleAccessPoint (nt.getItemNumber ( ));
    b.setNameTitleHeadingNumber (nt.getHeadingNumber ( ).intValue ( ));
    b.setHeadingNumber (
      new Integer (
        ((NME_TTL_HDG) nt.getDescriptor ( )).getTitleHeadingNumber ( )));
    b.setUserViewString (nt.getUserViewString ( ));
    b.setFunctionCode ((short) 0);
    persistByStatus (b);
  }

}
