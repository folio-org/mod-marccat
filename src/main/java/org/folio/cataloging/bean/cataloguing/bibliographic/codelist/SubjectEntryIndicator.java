/*
 * (c) LibriCore
 * 
 * Created on Dec 2, 2005
 * 
 * SubjectEntryIndicator.java
 */
package org.folio.cataloging.bean.cataloguing.bibliographic.codelist;

import org.folio.cataloging.dao.persistence.T_AUT_SBJCT_ENTRY;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public class SubjectEntryIndicator extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public SubjectEntryIndicator() {
		super(T_AUT_SBJCT_ENTRY.class);
	}

}
