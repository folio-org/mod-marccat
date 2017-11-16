/*
 * (c) LibriCore
 * 
 * Created on Jan 25, 2006
 * 
 * Collection.java
 */
package librisuite.bean.cataloguing.bibliographic.codelist;

import librisuite.hibernate.T_CLCTN_MST_TYP;;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/02/01 14:07:37 $
 * @since 1.0
 */
public class CollectionMst extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public CollectionMst() {
		super(T_CLCTN_MST_TYP.class);
	}

}
