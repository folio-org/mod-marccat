/*
 * (c) LibriCore
 * 
 * Created on Nov 16, 2005
 * 
 * BibliographicTagCategory.java
 */
package librisuite.bean.cataloguing.bibliographic.codelist;


import librisuite.hibernate.BibliographicNoteGroupType;
import librisuite.hibernate.ReferenceThsTyp;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:05 $
 * @since 1.0
 */
public class ReferenceThsType extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public ReferenceThsType() {
		super(ReferenceThsTyp.class);
	}

}
