/*
 * (c) LibriCore
 * 
 * Created on Jan 25, 2006
 * 
 * Collection.java
 */
package librisuite.bean.cataloguing.bibliographic.codelist;

import librisuite.hibernate.T_TRSLTN_NTE_TYP;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/02/01 14:07:37 $
 * @since 1.0
 */
public class NoteTranslation extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public NoteTranslation() {
		super(T_TRSLTN_NTE_TYP.class);
	}

}
