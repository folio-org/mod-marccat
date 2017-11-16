/*
 * (c) LibriCore
 * 
 * Created on Dec 1, 2004
 * 
 * DAOControlNumberDescriptor.java
 */
package librisuite.business.descriptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.SortFormException;
import librisuite.business.common.View;
import librisuite.hibernate.CNTL_NBR;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.type.Type;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2005/12/12 12:54:37 $
 * @since 1.0
 */
public class DAOControlNumberDescriptor extends DAODescriptor {
	private static final Class persistentClass = CNTL_NBR.class;

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.descriptor.DAODescriptor#getPersistentClass()
	 */
	public Class getPersistentClass() {
		return persistentClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.descriptor.DAODescriptor#supportsCrossReferences()
	 */
	public boolean supportsCrossReferences() {
		return false;
	}

	public Descriptor getMatchingHeading(Descriptor desc)
			throws DataAccessException, SortFormException {
	CNTL_NBR d = (CNTL_NBR) desc;
		try {
			List l = currentSession().find(
					"from " + getPersistentClass().getName() + " as c "
							+ " where c.stringText = ?"
							+ " and c.typeCode = ?" // difference from standard
							+ " and c.key.userViewString = ? ",
			       new Object[] { 
						    d.getStringText(),
							new Integer(d.getTypeCode()), // difference from standard
							d.getUserViewString()},
					new Type[] { 
						    Hibernate.STRING,
							Hibernate.INTEGER, // difference from standard
							Hibernate.STRING});
			if (l.size() == 1) {
				return (Descriptor) l.get(0);
			} else {
				return null;
			}
		} catch (HibernateException e) {
			logAndWrap(e);
			return null;
		}
	}

	/**
	 * Notice: In the past some sortforms included "extra" information to help
	 * in identifying uniqueness (some headings added a code for "language of
	 * access point" so that otherwise identical headings could be
	 * differentiated if they had different languages). Now, the only example I
	 * can think of is the Dewey Decimal classification where the sortform
	 * includes the Dewey Edition Number so that identical numbers from
	 * different editions will have different sortforms.
	 * 
	 * @param d
	 * @return
	 * @throws DataAccessException
	 * @throws SortFormException
	 */
	public boolean isMatchingAnotherHeading(Descriptor desc) {
		CNTL_NBR d = (CNTL_NBR) desc;
		try {
			List l = currentSession().find(
					"select count(*) from "
							+ getPersistentClass().getName()
							+ " as c "
							+ " where  c.stringText = ? "
							+ " and c.typeCode = ?" // difference from standard
							+ " and c.key.userViewString = ?"
							+ " and c.key.headingNumber <> ?",
					new Object[] { 
							d.getStringText(),
							new Integer(d.getTypeCode()), // difference from standard
							d.getUserViewString(),
							new Integer(d.getKey().getHeadingNumber()) },
					new Type[] { Hibernate.STRING,
							Hibernate.INTEGER, // difference from standard
							Hibernate.STRING, Hibernate.INTEGER });
			return ((Integer) l.get(0)).intValue() > 0;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * @param d
	 * @param searchingView
	 * @return the records present 
	 * in the table TTL_ACS_PNT.TTL_SRS_ISSN_NBR for the control number with type code =10 (tag 022)
	 * @throws DataAccessException
	 */
	public int getDocCount(Descriptor d, int searchingView)
			throws DataAccessException {

		Connection con;
		PreparedStatement stmt = null;
		int count = 0;
		try {
			CNTL_NBR descriptor = (CNTL_NBR) d;
			if (descriptor.getTypeCode() == 10) {
				String cmd = "select count(*) from TTL_ACS_PNT a  where a.TTL_SRS_ISSN_NBR=?";

				if (searchingView != View.ANY) {
					cmd = "select count(*) from TTL_ACS_PNT a  where a.TTL_SRS_ISSN_NBR=? and substr(a.USR_VW_IND, '"
							+ searchingView + "', 1) = '1'";
				}

				con = currentSession().connection();
				stmt = con.prepareStatement(cmd);
				logger.debug(cmd);
				stmt.setInt(1, d.getHeadingNumber());
				ResultSet rs = stmt.executeQuery();
				rs.next();
				count = rs.getInt(1);
			}
			count = count + super.getDocCount(d, searchingView);
			return count;
		} catch (Exception e) {
			logAndWrap(e);
			return 0;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// do nothing
				}
			}
		}

	}

	
	

}
