/*
 * (c) LibriCore
 * 
 * Created on Jan 25, 2006
 * 
 * DAOCollectionAccessPoint.java
 */
package librisuite.business.cataloguing.bibliographic;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import librisuite.business.codetable.ValueLabelElement;
import librisuite.business.common.DataAccessException;
import librisuite.hibernate.CLCTN_ACS_PNT;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import com.libricore.librisuite.common.HibernateUtil;
import com.libricore.librisuite.common.TransactionalHibernateOperation;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/02/01 14:07:37 $
 * @since 1.0
 */
public class DAOCollectionAccessPoint extends HibernateUtil {

	public List getCollectionsByBibItem(int itemNumber, Locale locale)
		throws DataAccessException {
		List result =
			find(
				"select new librisuite.business.codetable.ValueLabelElement"
					+ "(ct.code, ct.longText) "
					+ " from T_CLCTN as ct, CLCTN_ACS_PNT as apf "
					+ " where apf.bibItemNumber = ? and "
					+ " ct.code = apf.collectionNumber "
					+ " and ct.language = ?",
				new Object[] {
					new Integer(itemNumber),
					locale.getISO3Language()},
				new Type[] { Hibernate.INTEGER, Hibernate.STRING });
		return result;
	}

	public void save(int itemNumber, Set collections)
		throws DataAccessException {
		save(itemNumber, Arrays.asList(collections.toArray()));
	}

	public void save(final int itemNumber, final List collections)
		throws DataAccessException {
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
				throws HibernateException {
				s.delete(
					"from CLCTN_ACS_PNT as apf "
						+ " where apf.bibItemNumber = ? ",
					new Object[] { new Integer(itemNumber)},
					new Type[] { Hibernate.INTEGER });

				Iterator iter = collections.iterator();
				while (iter.hasNext()) {
					ValueLabelElement anElem = (ValueLabelElement) iter.next();
					s.save(
						new CLCTN_ACS_PNT(
							itemNumber,
							Short.parseShort(anElem.getValue())));
				}
			}
		}
		.execute();
	}
}
