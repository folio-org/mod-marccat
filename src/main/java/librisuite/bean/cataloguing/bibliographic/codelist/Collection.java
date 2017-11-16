/*
 * (c) LibriCore
 * 
 * Created on Jan 25, 2006
 * 
 * Collection.java
 */
package librisuite.bean.cataloguing.bibliographic.codelist;

import librisuite.hibernate.T_CLCTN;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/02/01 14:07:37 $
 * @since 1.0
 */
public class Collection extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public Collection() {
		super(T_CLCTN.class);
	}

}
