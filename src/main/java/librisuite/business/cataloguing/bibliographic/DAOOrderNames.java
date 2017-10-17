package librisuite.business.cataloguing.bibliographic;

import java.util.List;

import librisuite.business.common.DataAccessException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.libricore.librisuite.common.HibernateUtil;

public class DAOOrderNames extends HibernateUtil {

	private static final Log logger = LogFactory.getLog(DAOOrderNames.class);

	public List getOrderNames() throws DataAccessException {
		List list = find("from librisuite.hibernate.OrderNames t");
		return list;
	}

}