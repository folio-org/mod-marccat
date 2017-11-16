/*
 * (c) LibriCore
 * 
 * Created on Dec 2, 2005
 * 
 * MainAddedEntryIndicator.java
 */
package librisuite.bean.cataloguing.bibliographic.codelist;

import librisuite.hibernate.T_AUT_MAIN_ADD_ENTRY;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public class MainAddedEntryIndicator extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public MainAddedEntryIndicator() {
		super(T_AUT_MAIN_ADD_ENTRY.class);
	}

}
