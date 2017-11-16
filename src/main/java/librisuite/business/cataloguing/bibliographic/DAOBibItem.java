/*
 * (c) LibriCore
 * 
 * Created on Dec 3, 2004
 * 
 * DAOBibItem.java
 */
package librisuite.business.cataloguing.bibliographic;

import java.util.List;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.business.common.RecordNotFoundException;
import librisuite.business.common.ReferentialIntegrityException;
import librisuite.business.common.View;
import librisuite.hibernate.Cache;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import com.libricore.librisuite.common.HibernateUtil;
import com.libricore.librisuite.common.TransactionalHibernateOperation;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/02/02 14:09:42 $
 * @since 1.0
 */
public class DAOBibItem extends HibernateUtil 
{
	public void delete(Persistence p) throws ReferentialIntegrityException, DataAccessException 
	{
		if (!(p instanceof BIB_ITM)) {
			throw new IllegalArgumentException("Argument must be a BIB_ITM");
		}
		final BIB_ITM b = (BIB_ITM) p;
		// first check if other views exist for this bib 
		List l = find(" select count(*) from BIB_ITM as b "
					+ " where b.amicusNumber = ? and "
					+ " b.userViewString <> ? ",
				new Object[] { b.getAmicusNumber(), b.getUserViewString()},
				new Type[] { Hibernate.INTEGER, Hibernate.STRING });

		if (((Integer) l.get(0)).intValue() == 0) {
			// if no other views then we can't have any holdings attached
			l = find(" select count(*) from SMRY_HLDG as sh where sh.bibItemNumber = ? ",
					new Object[] { b.getAmicusNumber()},
					new Type[] { Hibernate.INTEGER });
			if (((Integer) l.get(0)).intValue() > 0) {
				throw new ReferentialIntegrityException("SMRY_HLDG", "BIB_ITM");
			}
			// and we can't have any orders attached
			l = find(" select count(*) from ORDR_ITM_BIB_ITM as o where o.bibItemNumber = ? ",
					new Object[] { b.getAmicusNumber()},
					new Type[] { Hibernate.INTEGER });
			if (((Integer) l.get(0)).intValue() > 0) {
				throw new ReferentialIntegrityException("ORDR_ITM_BIT_ITM",	"BIB_ITM");
			}
		}

		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s) throws HibernateException 
			{
				s.delete(b);
				s.delete("from CLCTN_ACS_PNT as c " + " where c.bibItemNumber = ? ",
					new Object[] { b.getAmicusNumber()},
					new Type[] { Hibernate.INTEGER });
				s.delete("from " + Cache.class.getName() + " as c "
						+ " where c.bibItemNumber = ? "
						+ " and c.cataloguingView = ? ",
					new Object[] {b.getAmicusNumber(), new Short(View.toIntView(b.getUserViewString()))},
					new Type[] { Hibernate.INTEGER, Hibernate.SHORT });
			}
		}
		.execute();
	}

	public BIB_ITM load(int id, int userView) throws DataAccessException, RecordNotFoundException 
	{
		BIB_ITM bibItm = null;
		List l =
			find("from BIB_ITM as itm where itm.amicusNumber = ? "
			  + " and substr(itm.userViewString, ?, 1) = '1'",
				new Object[] { new Integer(id), new Integer(userView)},
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });
		if (l.size() > 0) {
			bibItm = (BIB_ITM) l.get(0);
			bibItm = (BIB_ITM) isolateView(bibItm, userView);
			return bibItm;
		} else {
			throw new RecordNotFoundException("BIB_ITM not found");
		}
	}
}