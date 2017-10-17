/*
 * (c) LibriCore
 * 
 * Created on 15-jul-2004
 * 
 * DAOLocation.java
 */
package librisuite.business.common;

import java.util.List;
import java.util.Locale;

import librisuite.hibernate.LCTN;
import librisuite.hibernate.LCTN_ISOLANG_VW;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import com.libricore.librisuite.common.HibernateUtil;

/**
 * @author elena
 * @version $Revision: 1.12 $, $Date: 2005/12/21 13:33:35 $
 * @since 1.0
 */
public class DAOLocation extends HibernateUtil {

	public LCTN load(
		int organisationNumber,
		short locationNumber,
		Locale locale)
		throws DataAccessException {
		Session s = currentSession();

		List result;

		result =
			find(
				"from LCTN as l where "
					+ " l.key.organisationNumber = ? and l.key.locationNumber = ? and "
					+ " l.key.language = ? ",
				new Object[] {
					new Integer(organisationNumber),
					new Integer(locationNumber),
					locale.getISO3Language()},
				new Type[] {
					Hibernate.INTEGER,
					Hibernate.INTEGER,
					Hibernate.STRING });

		if (result.size() > 0) {
			return (LCTN) result.get(0); 
			}
		else {
			return null;
		}
	}

	public List LCTN_VWList(int organisationNumber, Locale locale)
		throws DataAccessException {
		List lo = null;

		lo =
			find(
				"from LCTN as vw"
					+ " where vw.key.organisationNumber = ? and "
					+ " vw.key.language = ? order by vw.key.locationNumber asc",
				new Object[] {
					new Integer(organisationNumber),
					locale.getISO3Language()},
				new Type[] { Hibernate.INTEGER, Hibernate.STRING });

		return lo;
	}


	public String getLocationName(Class c,int organisationNumber,Locale locale,short locationCode) throws DataAccessException {
	    
	    String result = "";
	    List lo = null;
	    try {
			Session s = currentSession();

			lo = (List) s.find("from "
			 + c.getName()
			 + " as vw where vw.key.organisationNumber ="
			 + organisationNumber
			 + " and vw.key.locationNumber ="
			 + locationCode
			 + " and vw.isoLanguage ='"
			 + locale.getISO3Language()
			 + "'");

			if (lo.size() <= 0) {
				lo = (List) s.find("from "
				+ c.getName()
				+ " as vw where vw.key.organisationNumber ="
				+ organisationNumber
				+ " and vw.key.locationNumber ="
				 + locationCode
				+ " and vw.isoLanguage ='"
				+ new Locale("en").getISO3Language()
				+ "'");
			}
			
			if (lo.size() > 0) {
			    
			    LCTN_ISOLANG_VW location = (LCTN_ISOLANG_VW) lo.get(0);
			    
			    result = location.getLabelStringText();
			    
			}
			
			
		}  catch (DataAccessException e) {
			// TODO e.printStackTrace() is evil. If you catch, handle the exception.
		    e.printStackTrace();
		} catch (HibernateException e) {
			// TODO e.printStackTrace() is evil. If you catch, handle the exception.
		    e.printStackTrace();
		} catch (ClassCastException e){
			// TODO e.printStackTrace() is evil. If you catch, handle the exception.
		    e.printStackTrace();
		}

	    return result;
	    
	}
}
