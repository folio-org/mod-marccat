/*
 * (c) LibriCore
 * 
 * Created on Dec 15, 2005
 * 
 * DAONameTitleTitleDescriptor.java
 */
package librisuite.business.descriptor;

import java.util.List;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.UpdateStatus;
import librisuite.hibernate.NME_TTL_HDG;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/12/21 08:30:33 $
 * @since 1.0
 */
public class DAONameTitleTitleDescriptor extends DAONameTitleDescriptor {
	
	public List getHeadingsBySortform(String operator, String direction, String term, String filter, int cataloguingView, int count) throws DataAccessException {
		Session s = currentSession();
	
		List l = null;
		try {
			Query q =
				s.createQuery(
					"select distinct hdg, nme.sortForm, ttl.sortForm from "
						+ "NME_TTL_HDG as hdg, "
						+ "NME_HDG as nme, " 
						+ "TTL_HDG as ttl"
						+ " where hdg.nameHeadingNumber = nme.key.headingNumber "
						+ " and hdg.titleHeadingNumber = ttl.key.headingNumber "
						+ " and ttl.sortForm "
						+ operator
						+ " :term  and "
						+ " SUBSTR(hdg.key.userViewString, :view, 1) = '1' "
						+ filter
						+ " order by ttl.sortForm "
						+ direction
						+ ", nme.sortForm "
						+ direction);
			q.setString("term", term);
			q.setInteger("view", cataloguingView);
			q.setMaxResults(count);
	
			l = q.list();
			// get the NME_TTL_HDG object from the query
			for (int i = 0; i < l.size(); i++) {
				NME_TTL_HDG h = (NME_TTL_HDG)((Object[])l.get(i))[0];
				h.setUpdateStatus(UpdateStatus.UNCHANGED);
				l.set(i, h);
			}
			l = isolateViewForList(l, cataloguingView);
			loadHeadings(l, cataloguingView);
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return l;
	}

	public String getBrowsingSortForm(Descriptor d) {
		if (!(d instanceof NME_TTL_HDG)) {
			logger.warn("I can only handle NME_TTL_HDG descriptors");
			throw new IllegalArgumentException();
		}
		return ((NME_TTL_HDG)d).getTitleHeading().getSortForm();
	}

}
