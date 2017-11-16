/*
 * (c) LibriCore
 * 
 * Created on Jan 2, 2006
 * 
 * DAOTitleNameTitleReferences.java
 */
package librisuite.business.crossreference;

import java.util.List;

import librisuite.business.common.DataAccessException;
import librisuite.hibernate.REF;
import librisuite.hibernate.ReferenceType;
import librisuite.hibernate.TTL_NME_TTL_REF;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.Type;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/01/05 13:25:59 $
 * @since 1.0
 */
public class DAOTitleNameTitleReferences extends DAOCrossReferences {

	/* (non-Javadoc)
	 * @see librisuite.business.crossreference.DAOCrossReferences#loadReciprocal(librisuite.hibernate.REF, int)
	 */
	public REF loadReciprocal(REF ref, int cataloguingView)
		throws DataAccessException {

		short reciprocalType = ReferenceType.getReciprocal(ref.getType());

		REF result = null;
		String queryString;
		if (((TTL_NME_TTL_REF) ref).isSourceTitle()) {
			queryString =
				"from TTL_NME_TTL_REF as ref "
					+ " where ref.titleHeadingNumber = ? AND "
					+ " ref.nameTitleHeadingNumber = ? AND "
					+ " ref.sourceHeadingType = 'MH' AND "
					+ " substr(ref.userViewString, ?, 1) = '1' AND "
					+ " ref.type = ?";

		} else {
			queryString =
				"from TTL_NME_TTL_REF as ref "
					+ " where ref.nameTitleHeadingNumber = ? AND "
					+ " ref.titleHeadingNumber = ? AND "
					+ " ref.sourceHeadingType = 'TH' AND "
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
