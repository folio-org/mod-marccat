/*
 * (c) LibriCore
 * 
 * Created on Jan 2, 2006
 * 
 * DAONameNameTitleReferences.java
 */
package librisuite.business.crossreference;

import java.util.List;

import librisuite.business.common.DataAccessException;
import librisuite.hibernate.NME_NME_TTL_REF;
import librisuite.hibernate.REF;
import librisuite.hibernate.ReferenceType;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.Type;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/01/05 13:25:59 $
 * @since 1.0
 */
public class DAONameNameTitleReferences extends DAOCrossReferences {

	/* (non-Javadoc)
	 * @see librisuite.business.crossreference.DAOCrossReferences#loadReciprocal(librisuite.hibernate.REF, int)
	 */
	public REF loadReciprocal(REF ref, int cataloguingView)
		throws DataAccessException {

		short reciprocalType = ReferenceType.getReciprocal(ref.getType());

		REF result = null;
		String queryString;
		if (((NME_NME_TTL_REF) ref).isSourceName()) {
			queryString =
				"from NME_NME_TTL_REF as ref "
					+ " where ref.nameHeadingNumber = ? AND "
					+ " ref.nameTitleHeadingNumber = ? AND "
					+ " ref.sourceHeadingType = 'MH' AND "
					+ " substr(ref.userViewString, ?, 1) = '1' AND "
					+ " ref.type = ?";

		} else {
			queryString =
				"from NME_NME_TTL_REF as ref "
					+ " where ref.nameTitleHeadingNumber = ? AND "
					+ " ref.nameHeadingNumber = ? AND "
					+ " ref.sourceHeadingType = 'NH' AND "
					+ " substr(ref.userViewString, ?, 1) = '1' AND "
					+ " ref.type = ?";
		}
		List l =
			find(
				queryString,
				new Object[] {
					new Integer(ref.getSource()),
					new Integer(ref.getTarget()),
					new Integer(cataloguingView),
					new Short(reciprocalType)},
				new Type[] {
					Hibernate.INTEGER,
					Hibernate.INTEGER,
					Hibernate.INTEGER,
					Hibernate.SHORT });
		if (l.size() == 1) {
			result = (REF) l.get(0);
		}

		return result;
	}

}
