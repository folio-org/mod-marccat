package librisuite.business.descriptor;

import java.util.Iterator;
import java.util.List;

import librisuite.business.common.DAOCache;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Defaults;
import librisuite.business.common.MadesMapBackedFactorySupport;
import librisuite.hibernate.SHLF_LIST;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.libricore.librisuite.common.HibernateUtil;

public class DAOMadesDescriptor extends HibernateUtil {
	protected static Log logger = LogFactory.getLog(DAOMadesDescriptor.class);
	
	static boolean madesSupported = Defaults.getBoolean("mades.supported", false);
	private static DAOMadesShelfList daoShelfList = new DAOMadesShelfList();
	

	// TO_MERGE Hierarchy not present in Libricat
	//private static DAOHierarchy daoHierarchy = new DAOHierarchy();

	// TO_MERGE MadesShelfList not present in Libricat
	//private static DAOMadesShelfList daoShelfList = new DAOMadesShelfList();
	
	// protected static AbstractMapBackedFactory tagFactory;

	/**
	 * MIKE: for mades column in the worksheet
	 * TODO _MIKE this method slow down the performances according to Paul's comment in getAuthCount()
	 * @param descriptor TODO _MIKE
	 * @param d
	 * @param cataloguingView
	 * @return
	 * @throws DataAccessException
	 * @throws UnsupportedHeadingException 
	 */
	public int getMadesCount(Descriptor d, int cataloguingView)
		throws DataAccessException, UnsupportedHeadingException {
		// TO_MERGE Hierarchy not present in Libricat
		/*
		if(daoHierarchy.isHierarchyDescriptor(d)) {
			return daoHierarchy.getHierarchyCount(d, cataloguingView);
		}
		/*
		/* TODO _MIKE: temporary workaround for MAD_SHLF_LST */
		// TO_MERGE MadesShelfList not present in Libricat
		/**/
		int result = 0;
		if(isShelfList(d)) {
			return result;
		}
		
		short categoryAccessPoint = d.getCategory();
		Object accessPoint;
		try {
			accessPoint = MadesMapBackedFactorySupport.getAccessPointFactory().create(categoryAccessPoint);
		} catch (RuntimeException e) {
			throw new UnsupportedHeadingException("unsupported heading for category "+d.getCategory()+": "+e.getMessage(),e);
		}		
		List l =
			find(
				" select count(*) from "
					+ accessPoint.getClass().getName()
					+ " as apf "
					+ " where apf.headingNumber = ?"
					+ " and apf.madUserView = ?",
				new Object[] {
						new Integer(d.getKey().getHeadingNumber()),
						new Integer(cataloguingView),
				},
				new Type[] { 	
						Hibernate.INTEGER, 
						Hibernate.INTEGER }
				);
		if (l.size() > 0) {
			result = ((Integer) l.get(0)).intValue();
		}
		return result;
	}
	
	/**
	 * @param d
	 * @return
	 * @throws DataAccessException
	 * @throws UnsupportedHeadingException
	 */
	public int getMadesCountIgnoringViews(Descriptor d)
	throws DataAccessException, UnsupportedHeadingException {
		int result = 0;
		short categoryAccessPoint = d.getCategory();
		Object accessPoint;
		try {
			accessPoint = MadesMapBackedFactorySupport.getAccessPointFactory().create(categoryAccessPoint);
		} catch (RuntimeException e) {
			throw new UnsupportedHeadingException("unsupported heading for category "+d.getCategory()+": "+e.getMessage(),e);
		}		
		List l =
			find(
				" select count(*) from "
					+ accessPoint.getClass().getName()
					+ " as apf "
					+ " where apf.headingNumber = ?",
				new Object[] {
						new Integer(d.getKey().getHeadingNumber())	},
				new Type[] { 	Hibernate.INTEGER	}
				);
		if (l.size() > 0) {
			result = ((Integer) l.get(0)).intValue();
		}
		return result;
	}

	/* TODO _MIKE: temporary workaround for MAD_SHLF_LST */
	// TO_MERGE MadesShelfList not present in Libricat
	/*
	public boolean isMadesShelfList(Descriptor d){
		return d instanceof MAD_SHLF_LIST;
	}
	*/
	private Object getMadesAccessPoint(Descriptor d, short categoryAccessPoint)
			throws UnsupportedHeadingException {
		Object accessPoint;
		try {
			accessPoint = MadesMapBackedFactorySupport.getAccessPointFactory()
					.create(categoryAccessPoint);
		} catch (RuntimeException e) {
			throw new UnsupportedHeadingException(
					"unsupported heading for category " + d.getCategory()
							+ ": " + e.getMessage(), e);
		}
		return accessPoint;
	}

//	/* TODO _MIKE: temporary workaround for MAD_SHLF_LST */
//	public boolean isMadesShelfList(Descriptor d){
//		return d instanceof MAD_SHLF_LIST;
//	}
	
	public List getMadesDocList(Descriptor d)
	throws DataAccessException, UnsupportedHeadingException {
		
	short categoryAccessPoint = d.getCategory();
	Object accessPoint = getMadesAccessPoint(d, categoryAccessPoint);		

	List l =
		find(
			" select apf.madItemNumber, apf.madUserView from "
				+ accessPoint.getClass().getName()
				+ " as apf "
				+ " where apf.headingNumber = ? ",
			new Object[] {	new Integer(d.getKey().getHeadingNumber())},
			new Type[] { Hibernate.INTEGER }
			);

	return l;
}

	/**
	 * Updates the cache table for each of the documents attached to the descriptor
	 * @throws UnsupportedHeadingException 
	 * 
	 * @since 1.0
	 */
	public void updateMadesCacheTable(Descriptor descriptor)
		throws DataAccessException {
		DAOCache dao = new DAOCache();
		try {
			Iterator iter = getMadesDocList(descriptor).iterator();
			while (iter.hasNext()) {
				Object[] key = (Object[]) iter.next();
				int an = ((Integer)key[0]).intValue();
				int vw = ((Integer)key[1]).intValue();
				dao.updateMadesCacheTable(an, vw);
			}
		} catch (UnsupportedHeadingException e) {
			logger.error(e);
			// do nothing
		}
	}
	/* TODO _MIKE: temporary workaround for MAD_SHLF_LST */
	public boolean isShelfList(Descriptor d){
		return d instanceof SHLF_LIST;
	}
	

}
