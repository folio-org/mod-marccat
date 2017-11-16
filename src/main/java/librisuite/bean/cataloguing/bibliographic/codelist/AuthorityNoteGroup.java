/*
 * (c) LibriCore
 * 
 * Created on Nov 16, 2005
 * 
 * BibliographicTagCategory.java
 */
package librisuite.bean.cataloguing.bibliographic.codelist;


import librisuite.hibernate.AuthoritycNoteGroupType;

/**
 * @author carmen
 * @version $Revision: 1.2 $, $Date: 2016/01/20 13:50:05 $
 * @since 1.0
 */
public class AuthorityNoteGroup extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public AuthorityNoteGroup() {
		super(AuthoritycNoteGroupType.class);
	}

}
