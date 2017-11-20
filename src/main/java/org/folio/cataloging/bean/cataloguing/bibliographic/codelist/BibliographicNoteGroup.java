/*
 * (c) LibriCore
 * 
 * Created on Nov 16, 2005
 * 
 * BibliographicTagCategory.java
 */
package org.folio.cataloging.bean.cataloguing.bibliographic.codelist;


import org.folio.cataloging.dao.persistence.BibliographicNoteGroupType;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:05 $
 * @since 1.0
 */
public class BibliographicNoteGroup extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public BibliographicNoteGroup() {
		super(BibliographicNoteGroupType.class);
	}

}
