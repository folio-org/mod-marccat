package librisuite.business.descriptor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.hibernate.PUBL_TAG;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.libricore.librisuite.common.HibernateUtil;
import com.libricore.librisuite.common.TransactionalHibernateOperation;

public class DAOPublTag extends DAODescriptor {

	//@Override
	public Class getPersistentClass() {
		return PUBL_TAG.class;
	}

}
