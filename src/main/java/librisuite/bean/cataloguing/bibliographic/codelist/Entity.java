/*
 * (c) LibriCore
 * 
 * Created on Dec 2, 2005
 * 
 * CataloguingRules.java
 */
package librisuite.bean.cataloguing.bibliographic.codelist;

import librisuite.hibernate.T_FRBR;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public class Entity extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public Entity() {
		super(T_FRBR.class);
	}

}
