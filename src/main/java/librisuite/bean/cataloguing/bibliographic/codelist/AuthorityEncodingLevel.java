/*
 * (c) LibriCore
 * 
 * Created on Nov 25, 2005
 * 
 * AuthorityEncodingLevel.java
 */
package librisuite.bean.cataloguing.bibliographic.codelist;

import librisuite.hibernate.T_AUT_ENCDG_LVL;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:05 $
 * @since 1.0
 */
public class AuthorityEncodingLevel extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public AuthorityEncodingLevel() {
		super(T_AUT_ENCDG_LVL.class);
	}

}
