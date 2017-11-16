/*
 * (c) LibriCore
 * 
 * Created on Dec 2, 2005
 * 
 * SeriesType.java
 */
package librisuite.bean.cataloguing.bibliographic.codelist;

import librisuite.hibernate.T_AUT_SRS_TYP;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public class SeriesType extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public SeriesType() {
		super(T_AUT_SRS_TYP.class);
	}

}
