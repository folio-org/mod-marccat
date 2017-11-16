/*
 * (c) LibriCore
 * 
 * Created on Dec 20, 2005
 * 
 * DAONameTitleAccessPoint.java
 */
package librisuite.business.cataloguing.bibliographic;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.hibernate.NME_TTL_HDG;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import com.libricore.librisuite.common.HibernateUtil;
import com.libricore.librisuite.common.TransactionalHibernateOperation;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class DAONameTitleAccessPoint extends HibernateUtil {

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.HibernateUtil#delete(librisuite.business.common.Persistence)
	 */
	public void delete(final Persistence p) throws DataAccessException {

		super.delete(p);
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
				throws HibernateException {
				NameTitleAccessPoint a = (NameTitleAccessPoint) p;

				/*
				 * delete the "redundant" access point entries in NME_ACS_PNT and TTL_ACS_PNT
				 */
				s.delete(
					"from NameAccessPoint as n "
						+ " where n.nameTitleHeadingNumber = ? and "
						+ " n.userViewString = ? ",
					new Object[] { a.getHeadingNumber(), a.getUserViewString()},
					new Type[] { Hibernate.INTEGER, Hibernate.STRING });

				s.delete(
					"from TitleAccessPoint as n "
						+ " where n.nameTitleHeadingNumber = ? and "
						+ " n.userViewString = ? ",
					new Object[] { a.getHeadingNumber(), a.getUserViewString()},
					new Type[] { Hibernate.INTEGER, Hibernate.STRING });
			}
		}
		.execute();
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.HibernateUtil#save(librisuite.business.common.Persistence)
	 */
	public void save(Persistence p)
		throws DataAccessException {

		super.save(p);
		NameTitleAccessPoint nt = (NameTitleAccessPoint) p;
		NameAccessPoint a = new NameAccessPoint(nt.getItemNumber());
		a.setNameTitleHeadingNumber(nt.getHeadingNumber().intValue());
		a.setHeadingNumber(
			new Integer(
				((NME_TTL_HDG) nt.getDescriptor()).getNameHeadingNumber()));
		a.setUserViewString(nt.getUserViewString());
		a.setFunctionCode((short) 0);
		persistByStatus(a);
		TitleAccessPoint b = new TitleAccessPoint(nt.getItemNumber());
		b.setNameTitleHeadingNumber(nt.getHeadingNumber().intValue());
		b.setHeadingNumber(
			new Integer(
				((NME_TTL_HDG) nt.getDescriptor()).getTitleHeadingNumber()));
		b.setUserViewString(nt.getUserViewString());
		b.setFunctionCode((short) 0);
		persistByStatus(b);
	}

}
