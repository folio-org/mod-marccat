/*
 * (c) LibriCore
 *
 * Created on Jan 24, 2005
 *
 * DAOInventory.java
 */
package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.LockMode;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.dao.common.TransactionalHibernateOperation;
import org.folio.marccat.dao.persistence.Inventory;
import org.folio.marccat.dao.persistence.S_INVTRY;

import java.util.List;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/02/02 14:09:42 $
 * @since 1.0
 */
public class DAOInventory extends AbstractDAO {



  private boolean isSerial(Inventory item) throws DataAccessException {
    List l =
      find(
        "select count(*) from SerialPart as s "
          + " where s.copyNumber = ? ",
        new Object[]{new Integer(item.getCopyNumber())},
        new Type[]{Hibernate.INTEGER});
    return l.size() > 0 && ((Integer) l.get(0)).intValue() > 0;
  }

  public void populateNewItem(final Inventory item, final int cataloguingView)
    throws DataAccessException {
		/*DAOCache dao = new DAOCache();
		CPY_ID copy = new DAOCopy().load(item.getCopyNumber());
		item.setBibItemNumber(copy.getBibItemNumber());

		Cache bib = dao.load(item.getBibItemNumber(), cataloguingView);
		item.setAuthor(bib.getNameMainEntryStringText());
		item.setTitle(bib.getTitleHeadingMainStringText());
		item.setEdition(bib.getEditionStrngTxt());
		item.setPublisher(bib.getPublisherStrngTxt());

		BIB_ITM bibItem =
			new DAOBibItem().load(item.getBibItemNumber(), cataloguingView);
		item.setMaterialTypeCode(bibItem.getItemRecordTypeCode());

		Integer vendorOrganisationNumber = null;

		if (isSerial(item)) {
			List l =
				find(
					" select a.vendorOrganisationNumber,"
						+ "  a.orderAcquisitionTypeCode "
						+ "from Order as a, "
						+ "     SerialPart as b, "
						+ "     SerialLogicalCopy as c "
						+ "where b.copyNumber = ? and "
						+ "      a.orderNumber = c.orderNumber and "
						+ "      b.serialCopyNumber = c.serialCopyNumber",
					new Object[] { new Integer(item.getCopyNumber())},
					new Type[] { Hibernate.INTEGER });
			if (l.size() == 1) {
				item.setPrice(new Float(0));
				item.setAcquisitionTypeCode((Short) ((Object[]) l.get(0))[1]);
				vendorOrganisationNumber = (Integer) ((Object[]) l.get(0))[0];
			}
		} else {
			List l =
				find(
					"select    o.vendorOrganisationNumber,"
						+ "	   o.currencyTypeCode,"
						+ "	   o.orderAcquisitionTypeCode,"
						+ "	   oi.price"
						+ " from Order as o, "
						+ "	   OrderItem as oi, "
						+ "	   ReceiveItem as ri, "
						+ "    ReceiveItemCopy as ric"
						+ " where ric.copyNumber = ? and "
						+ "    ric.receiveItemNumber = ri.receiveItemNumber and "
						+ "    ri.orderNumber = oi.orderNumber and "
						+ "    ri.orderItemNumber = oi.orderItemNumber and "
						+ "    oi.orderNumber = o.orderNumber ",
					new Object[] { new Integer(item.getCopyNumber())},
					new Type[] { Hibernate.INTEGER });
			if (l.size() == 1) {
				vendorOrganisationNumber = (Integer) ((Object[]) l.get(0))[0];
				item.setCurrencyCode(
					((Short) ((Object[]) l.get(0))[1]).shortValue());
				item.setAcquisitionTypeCode((Short) ((Object[]) l.get(0))[2]);
				item.setPrice((Float) ((Object[]) l.get(0))[3]);
			}
		}

		if (item.getCurrencyCode() == 0) {
			String s =
				new DAOGlobalVariable().getValueByName("display_currency");
			if (s == null) {
				// TODO: If this method is needed, it requires a Session + a Vertx context (to be passed instead of null)
				//s = Defaults.getString("inventory.defaultCurrency", null);
			}
			item.setCurrencyCode(Short.parseShort(s));
		}

		if (vendorOrganisationNumber != null) {
			//TODO fix session!
			final Session session = currentSession();

			Vendor v = (Vendor) get(session, Vendor.class, vendorOrganisationNumber);
			item.setVendorName(v.getName());
		}*/
  }

  public int getNextNumber(final int mainLibrary)
    throws DataAccessException {
    final S_INVTRY nextNumber =
      (S_INVTRY) get(S_INVTRY.class,
        new Integer(mainLibrary),
        LockMode.UPGRADE);
    int j = nextNumber.getNextNumber();
    new TransactionalHibernateOperation() {
      public void doInHibernateTransaction(Session s)
        throws HibernateException {
        int i = nextNumber.getNextNumber() + 1;
        nextNumber.setNextNumber(i);
        s.update(nextNumber);
      }
    }
      .execute();

    return /*nextNumber.getNextNumber()*/j;
  }


}
