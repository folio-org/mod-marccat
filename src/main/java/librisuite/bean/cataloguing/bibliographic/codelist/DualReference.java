/*
 * (c) LibriCore
 * 
 * Created on Jan 6, 2006
 * 
 * DualReference.java
 */
package librisuite.bean.cataloguing.bibliographic.codelist;

import librisuite.hibernate.T_DUAL_REF;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/01/11 13:36:22 $
 * @since 1.0
 */
public class DualReference extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public DualReference() {
		super(T_DUAL_REF.class);
	}

}
