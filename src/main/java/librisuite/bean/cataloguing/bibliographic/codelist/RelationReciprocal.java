/*
 * (c) LibriCore
 * 
 * Created on Nov 24, 2005
 * 
 * RelationReciprocal.java
 */
package librisuite.bean.cataloguing.bibliographic.codelist;

import librisuite.hibernate.BibliographicRelationReciprocal;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:05 $
 * @since 1.0
 */
public class RelationReciprocal extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public RelationReciprocal() {
		super(BibliographicRelationReciprocal.class);
	}

}
