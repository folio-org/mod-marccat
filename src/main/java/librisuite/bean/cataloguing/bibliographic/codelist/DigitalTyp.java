/*
 * (c) LibriCore
 * 
 * Created on Dec 2, 2005
 * 
 * CataloguingSourceCode.java
 */
package librisuite.bean.cataloguing.bibliographic.codelist;

import librisuite.hibernate.T_CAS_DIG_TYP;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public class DigitalTyp extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public DigitalTyp() {
		super(T_CAS_DIG_TYP.class);
	}

}
