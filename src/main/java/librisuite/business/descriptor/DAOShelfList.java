
package librisuite.business.descriptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import librisuite.bean.cataloguing.copy.CopyListElement;
import librisuite.business.common.DAOSystemNextNumber;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.business.common.RecordNotFoundException;
import librisuite.business.common.ReferentialIntegrityException;
import librisuite.hibernate.SHLF_LIST;
import librisuite.hibernate.SHLF_LIST_ACS_PNT;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import com.libricore.librisuite.common.TransactionalHibernateOperation;

public class DAOShelfList extends DAODescriptor 
{
	public SHLF_LIST load(int shelfListKeyNumber) throws DataAccessException, RecordNotFoundException 
	{
		SHLF_LIST sl = null;
		try {
			Session s = currentSession();
			sl = (SHLF_LIST) s.get(SHLF_LIST.class, new Integer(shelfListKeyNumber));
			if (sl == null) {
				throw new RecordNotFoundException();
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return sl;
	}

	
	public SHLF_LIST loadShelf(int shelfListKeyNumber)
	throws DataAccessException{
	SHLF_LIST sl = null;
	try {
		Session s = currentSession();

		sl =
			(SHLF_LIST) s.get(
				SHLF_LIST.class,
				new Integer(shelfListKeyNumber));
		if (sl == null) {
			return null;
		}
	} 
	catch (Exception e) {
		logAndWrap(e); 
		return null;
	}
	return sl;
}
	
	
	
	public SHLF_LIST_ACS_PNT loadAccessPoint(int shelfListKeyNumber)
		throws DataAccessException {
		SHLF_LIST_ACS_PNT slap = null;
		try {
			Session s = currentSession();
			slap = (SHLF_LIST_ACS_PNT) s.get(SHLF_LIST_ACS_PNT.class, new Integer(shelfListKeyNumber));
		} catch (HibernateException e) {
			logAndWrap(e);

		} catch (NullPointerException e) {
			//			TODO e.printStackTrace() is evil. If you catch, handle the exception.
			e.printStackTrace();
		}
		return slap;
	}
	public SHLF_LIST getShelfList(String shelfListText, char shelfListType, int orgNumber) throws DataAccessException 
	{
		SHLF_LIST result = null;
		SHLF_LIST sl = new SHLF_LIST();
		sl.setStringText(shelfListText);
		sl.setTypeCode(shelfListType);
		sl.setMainLibraryNumber(orgNumber);
		String shelfListSortForm = calculateSortForm(sl);
		try {
			Session s = currentSession();
			List shelfList = (List) s.find("from SHLF_LIST as sl where sl.sortForm = ? "
						+ "AND sl.typeCode = ? "
						+ "AND sl.mainLibraryNumber = ? ",
					new Object[] {
						shelfListSortForm,
						new Character(sl.getTypeCode()),
						new Integer(sl.getMainLibraryNumber())},
					new Type[] {
						Hibernate.STRING,
						Hibernate.CHARACTER,
						Hibernate.INTEGER });

			if (shelfList.size() > 0) {
				sl = (SHLF_LIST) shelfList.get(0);
				sl.setSortForm(shelfListSortForm);
				result = sl;
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (NullPointerException e) {
			//			TODO e.printStackTrace() is evil. If you catch, handle the exception.
			e.printStackTrace();
			throw e;
		}

		return result;
	}
	
	
	public Class getPersistentClass() 
	{
		return SHLF_LIST.class;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.DAODescriptor#getDocCount(librisuite.hibernate.Descriptor, int)
	 */
	public int getDocCount(Descriptor d, int cataloguingView) throws DataAccessException 
	{
		Session s = currentSession();
		List l = null;
		int result = 0;
		try {
			l =
				s.find(
					" select count(*) from "
						+ d.getAccessPointClass().getName()
						+ " as apf "
						+ " where apf.shelfListKeyNumber = ? and apf.mainLibraryNumber=?",
					new Object[] {new Integer(((SHLF_LIST) d).getShelfListKeyNumber()),new Integer(((SHLF_LIST) d).getMainLibraryNumber())},new Type[] { Hibernate.INTEGER,Hibernate.INTEGER });

			if (l.size() > 0) {
				result = ((Integer) l.get(0)).intValue();
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}

	public List getHeadingsBySortform(
			String operator,
			String direction,
			String term,
			String filter,
			int cataloguingView,
			int count)
			throws DataAccessException {
			
				Session s = currentSession();
				List<SHLF_LIST> l = new ArrayList<SHLF_LIST>();
				Statement st = null;
				ResultSet rs = null;
			try {
				
					Connection con = s.connection();

					filter = filter.replaceAll("hdg.typeCode", "hdg.SHLF_LIST_TYP_CDE");
					filter = filter.replaceAll("hdg.mainLibraryNumber", "hdg.ORG_NBR");
				
					String sql = "select * from ( select * from "
						+ "SHLF_LIST "
						+	" hdg left  outer join SHLF_LIST_ACS_PNT acs on hdg.SHLF_LIST_KEY_NBR=acs.SHLF_LIST_KEY_NBR  and hdg.ORG_NBR=acs.ORG_NBR" 
						+ " where hdg.SHLF_LIST_SRT_FORM "
						+ operator
						+ " '" + term + "' " 
						+ filter 
						+ " order by hdg.SHLF_LIST_SRT_FORM "
						+ direction
						+" ) WHERE rownum <="  + count;
					
					st = con.createStatement();
					rs = st.executeQuery(sql);
				while(rs.next())
				{
						SHLF_LIST shlf = new SHLF_LIST();
						shlf.setShelfListKeyNumber(rs.getInt("SHLF_LIST_KEY_NBR"));
						shlf.setTypeCode(rs.getString("SHLF_LIST_TYP_CDE").charAt(0));
						shlf.setMainLibraryNumber(rs.getInt("ORG_NBR"));
						shlf.setStringText(rs.getString("SHLF_LIST_STRNG_TEXT"));
						shlf.setSortForm(rs.getString("SHLF_LIST_SRT_FORM"));
						shlf.setAmicusNumber(rs.getInt("BIB_ITM_NBR"));
						
						l.add(shlf);
				}
				

				if (logger.isDebugEnabled()) {
					logger.debug("About to query:" + sql);
				}

				
			} catch (HibernateException e) {
				logAndWrap(e);
			} catch (SQLException e) {
				logAndWrap(e);
			}
			
			finally{
				try{
					rs.close();
				}catch (Exception e) {
				}
				try{
					st.close();
				}catch (Exception e) {
				}
			}
			
			return l;
		}
	
	/**
	 * 20120330: Add function upper e toUpperCase for stringText (bug 1565)
	 * Add check SORT FORM
	 */
	
	public Descriptor getMatchingHeading(Descriptor d) throws DataAccessException 
	{
		if (!(d instanceof SHLF_LIST)) {
			throw new IllegalArgumentException("I can only match SHLF_LIST objects");
		}
		SHLF_LIST shelf = (SHLF_LIST) d;
				
		try {
			//check String text
			List l = currentSession().find("from " 
					    + getPersistentClass().getName()
						+ " as c "
						+ " where upper(c.stringText) = ? and c.mainLibraryNumber = ? "
						+ " and c.typeCode = ? ",
						
					new Object[] {
						shelf.getStringText().toUpperCase(),
						new Integer(shelf.getMainLibraryNumber()),
						new Character(shelf.getTypeCode())},
					new Type[] {
						Hibernate.STRING,
						Hibernate.INTEGER,
						Hibernate.CHARACTER});
			if(l.size()==0){
					String shelfListSortForm = calculateSortForm(shelf.getStringText());
					shelf.setSortForm(shelfListSortForm.trim());
					l = currentSession().find("from " 
					    + getPersistentClass().getName()
						+ " as c "
						+ " where upper(c.sortForm) = ? and c.mainLibraryNumber = ? "
						+ " and c.typeCode = ? ",
						
					new Object[] {
						shelf.getSortForm(),
						new Integer(shelf.getMainLibraryNumber()),
						new Character(shelf.getTypeCode())},
					new Type[] {
						Hibernate.STRING,
						Hibernate.INTEGER,
						Hibernate.CHARACTER});
			}
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

	public boolean isMatchingAnotherHeading(Descriptor d) 
	{
		if (!(d instanceof SHLF_LIST)) {
			throw new IllegalArgumentException("SHLF_LIST objects required but found "+d);
		}
		SHLF_LIST shelf = (SHLF_LIST) d;
		try {
			List l =
				currentSession().find(
					"select count(*) from "
						+ getPersistentClass().getName()
						+ " as c"
						+ " where c.stringText = ? and c.mainLibraryNumber = ?"
						+ " and c.typeCode = ?"
						+ " and c.shelfListKeyNumber <> ?",
					new Object[] {
							shelf.getStringText(),
						new Integer(shelf.getMainLibraryNumber()),
						new Character(shelf.getTypeCode()),
						new Integer(((SHLF_LIST)d).getShelfListKeyNumber())},
					new Type[] {
						Hibernate.STRING,
						Hibernate.INTEGER,
						Hibernate.CHARACTER,
						Hibernate.INTEGER});
			return ((Integer) l.get(0)).intValue() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.DAODescriptor#supportsCrossReferences()
	 */
	public boolean supportsCrossReferences() 
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.DAODescriptor#load(int, int)
	 */
	/*
	 * override to provide editHeading access to shelflists ignoring userview
	 */
	public Descriptor load(int headingNumber, int cataloguingView) throws DataAccessException 
	{
		return load(headingNumber);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.DAODescriptor#persist(librisuite.business.descriptor.Descriptor)
	 */
	public void persist(Descriptor descriptor) throws DataAccessException 
	{
		if (descriptor.isNew()) {
			((SHLF_LIST) descriptor).setShelfListKeyNumber(
				new DAOSystemNextNumber().getNextNumber(
					descriptor.getNextNumberKeyFieldCode()));
		}
		persistByStatus(descriptor);
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.HibernateUtil#delete(librisuite.business.common.Persistence)
	 */
	public void delete(final Persistence p) throws ReferentialIntegrityException, DataAccessException 
	{
		if (!(p instanceof SHLF_LIST)) {
			throw new IllegalArgumentException("I can only delete SHLF_LIST objects");
		}
		SHLF_LIST d = ((SHLF_LIST) p);
		// check for access point references
		List l = find("select count(*) from " + d.getAccessPointClass().getName() + " as a where a.shelfListKeyNumber = ?",
				new Object[] { new Integer(d.getShelfListKeyNumber())},
				new Type[] { Hibernate.INTEGER });
		if (((Integer) l.get(0)).intValue() > 0) {
			throw new ReferentialIntegrityException(
				d.getAccessPointClass().getName(),
				d.getClass().getName());
		}
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
				throws HibernateException {
				s.delete(p);
			}
		}
		.execute();
	}

	public void updateShelfList(SHLF_LIST shlf_list) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		Session session = currentSession();
		
		try {
			connection = session.connection();
			stmt = connection.prepareStatement("UPDATE SHLF_LIST SET shlf_list_strng_text=?, shlf_list_srt_form = ? WHERE shlf_list_key_nbr=?");
			stmt.setString(1, shlf_list.getStringText());
			stmt.setString(2, shlf_list.getSortForm());
			stmt.setInt(3, shlf_list.getHeadingNumber());
			stmt.executeUpdate();
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		}
		
		finally
		{
			try {
				stmt.close();
			} catch (SQLException e) {
				logAndWrap(e);
			}
		}
	}

	public SHLF_LIST_ACS_PNT getShelfListAcsPntByAmicusNumberOrgNbr(int bibItemOri, int orgNbr, int shelfListKeyNbr) throws DataAccessException
	{
		List l = find("from SHLF_LIST_ACS_PNT as c" 
					+ " where c.mainLibraryNumber = ?"
					+ " and c.bibItemNumber = ?"
					+ " and c.shelfListKeyNumber = ?",
				new Object[] {
						new Integer(orgNbr), 
						new Integer(bibItemOri),
						new Integer(shelfListKeyNbr)}, 
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.INTEGER });
		
		if (l.size() == 1) {
			return (SHLF_LIST_ACS_PNT) l.get(0);
		} else {
			return null;
		}
	}
	
	
	public void deleteShelfListAccessPoint(int bibItemOri, int orgNbr, int shelfListKeyNbr) throws DataAccessException 
	{	
		Session s = currentSession();	
		try{			
			s.delete("from deleteShelfListAccessPoint as c" 
					+ "	where c.bibItemNumber = ?"
					+ " and c.mainLibraryNumber = ?"
					+ " and c.shelfListKeyNumber = ?"
					, 
				  new Object[] {
					 new Integer(bibItemOri)
				    ,new Integer(orgNbr)
				    ,new Integer(shelfListKeyNbr)
					},
				  new Type[] {
					 Hibernate.INTEGER
					,Hibernate.INTEGER
					,Hibernate.INTEGER 
					});
		} catch (HibernateException e) {
			logAndWrap(e);
		}
	}
	
	public void insertShelfListAccessPoint(int bibItem, int orgNbr, int shelfListKeyNbr) throws DataAccessException 
	{	
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			connection = currentSession().connection();
			stmt = connection.prepareStatement("INSERT INTO SHLF_LIST_ACS_PNT(BIB_ITM_NBR, ORG_NBR, SHLF_LIST_KEY_NBR) VALUES (?, ?, ?)");
			stmt.setInt(1, bibItem);
			stmt.setInt(2, orgNbr);
			stmt.setInt(3, shelfListKeyNbr);
			stmt.executeUpdate();
		}catch (Exception e) {
			try {
				logAndWrap(e);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}
		}

		finally{
			try {
				stmt.close();
			} catch (SQLException e) {}
		}
	}
}