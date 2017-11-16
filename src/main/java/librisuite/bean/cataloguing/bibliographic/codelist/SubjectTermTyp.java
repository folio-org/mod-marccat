/*
 * (c) LibriCore
 * 
 * Created on Dec 2, 2005
 * 
 * CataloguingSourceCode.java
 */
package librisuite.bean.cataloguing.bibliographic.codelist;

import librisuite.hibernate.T_AUT_CTLGG_SRC;
import librisuite.hibernate.T_SBJCT_TRM_TYP;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public class SubjectTermTyp extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public SubjectTermTyp() {
		super(T_SBJCT_TRM_TYP.class);
	}

}
