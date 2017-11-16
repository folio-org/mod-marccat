/*
 * (c) LibriCore
 * 
 * Created on 14-jul-2004
 * 
 * DAOSystemNextNumber.java
 */
package librisuite.business.common;

import librisuite.hibernate.S_NXT_NBR;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.LockMode;
import net.sf.hibernate.Session;

import com.libricore.librisuite.common.HibernateUtil;
import com.libricore.librisuite.common.TransactionalHibernateOperation;

/**
 * @author Maite
 * @version $Revision: 1.7 $, $Date: 2005/02/23 15:03:33 $
 * class representing the access to S_NXT_NBR table
 */
public class DAOSystemNextNumber extends HibernateUtil {

	/**  
	 * This method return the field updated nextNumber and save changes in the table 
	 * @param keyFieldCodeValue is the key of the row I want increase
	 * @return the increased number
	 * @throws DataAccessException
	 * @throws HibernateException
	 * @since 1.0
	 */
	public int getNextNumber(final String keyFieldCodeValue)
		throws DataAccessException {

		final S_NXT_NBR snn =
			(S_NXT_NBR) get(S_NXT_NBR.class, keyFieldCodeValue, LockMode.UPGRADE);
		final int nextNbr = snn.getKeyFieldNextNumber();

		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
				throws HibernateException {
				int i = snn.getKeyFieldNextNumber() + 1;
				snn.setKeyFieldNextNumber(i);
				s.update(snn);
			}
		}.execute();

		return nextNbr;
	}

	/*modifica barbara 12/04/2007 PRN 116*/
	public int getPreviouwsNumber(final String keyFieldCodeValue)
	throws DataAccessException {
	final S_NXT_NBR snn =
		(S_NXT_NBR) get(S_NXT_NBR.class, keyFieldCodeValue, LockMode.UPGRADE);
	final int nextNbr = snn.getKeyFieldNextNumber();

	new TransactionalHibernateOperation() {
		public void doInHibernateTransaction(Session s)
			throws HibernateException {
			int i = snn.getKeyFieldNextNumber() - 1;
			snn.setKeyFieldNextNumber(i);
			s.update(snn);
		}
	}.execute();

	return nextNbr;
}

}
