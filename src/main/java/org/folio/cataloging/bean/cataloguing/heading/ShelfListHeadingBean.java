/*
 * (c) LibriCore
 * 
 * Created on Apr 5, 2006
 * 
 * ShelfListHeadingBean.java
 */
package org.folio.cataloging.bean.cataloguing.heading;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.dao.DAOCodeTable;
import org.folio.cataloging.dao.persistence.SHLF_LIST;
import org.folio.cataloging.dao.persistence.T_SHLF_LIST_TYP;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/04/05 14:30:56 $
 * @since 1.0
 */
public class ShelfListHeadingBean extends HeadingBean {

	private SHLF_LIST shelfListHeading = new SHLF_LIST();

	private List typeCodeList = new ArrayList();

	/* (non-Javadoc)
	 * @see HeadingBean#getHeading()
	 */
	public Descriptor getHeading() {
		return shelfListHeading;
	}

	/* (non-Javadoc)
	 * @see HeadingBean#setHeading(librisuite.hibernate.Descriptor)
	 */
	protected void setHeading(Descriptor d) {
		if (!(d instanceof SHLF_LIST))
			throw new IllegalArgumentException("I can only set descriptors of type SHLF_LIST");
		shelfListHeading = (SHLF_LIST) d;
	}

	/* (non-Javadoc)
	 * @see HeadingBean#onPopulateLists(DAOCodeTable, java.util.Locale)
	 */
	protected void onPopulateLists(DAOCodeTable dao, Locale l)
		throws DataAccessException {
		setTypeCodeList(dao.getOptionList(T_SHLF_LIST_TYP.class, l));
	}

	/**
	 * @return
	 */
	public List getTypeCodeList() {
		return typeCodeList;
	}

	/**
	 * @param list
	 */
	public void setTypeCodeList(List list) {
		typeCodeList = list;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getTypeCode() {
		return shelfListHeading.getTypeCode();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setTypeCode(char c) {
		shelfListHeading.setTypeCode(c);
	}

	/* (non-Javadoc)
	 * @see HeadingBean#setUsersMainLibrary(int)
	 */
	public void setUsersMainLibrary(int mainLibraryNumber) {
		shelfListHeading.setMainLibraryNumber(mainLibraryNumber);
	}

	@Override
	public void setSkipInFiling(int s) {
		// TODO Auto-generated method stub
		
	}

}
