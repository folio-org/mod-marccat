/*
 * (c) LibriCore
 * 
 * Created on Jul 28, 2005
 * 
 * SortCriteria.java
 */
package librisuite.bean.searching;

import librisuite.bean.cataloguing.bibliographic.codelist.CodeListBean;
import librisuite.hibernate.T_SRT_CRTRIA;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/07/28 11:41:23 $
 * @since 1.0
 */
public class SortCriteria extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public SortCriteria() {
		super(T_SRT_CRTRIA.class);
	}

}
