/*
 * (c) LibriCore
 * 
 * Created on Nov 25, 2005
 * 
 * AuthorityRecordStatus.java
 */
package librisuite.bean.cataloguing.bibliographic.codelist;

import librisuite.hibernate.T_AUT_REC_STUS;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:05 $
 * @since 1.0
 */
public class AuthorityRecordStatus extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public AuthorityRecordStatus() {
		super(T_AUT_REC_STUS.class);
	}

}
