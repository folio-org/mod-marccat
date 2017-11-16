/*
 * (c) LibriCore
 * 
 * Created on Nov 16, 2005
 * 
 * VerificationLevel.java
 */
package librisuite.bean.cataloguing.bibliographic.codelist;

import librisuite.hibernate.T_VRFTN_LVL;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:05 $
 * @since 1.0
 */
public class VerificationLevel extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public VerificationLevel() {
		super(T_VRFTN_LVL.class);
	}

}
