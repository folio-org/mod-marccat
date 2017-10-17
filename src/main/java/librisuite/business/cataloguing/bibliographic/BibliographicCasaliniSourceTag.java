/*
 * (c) LibriCore
 * 
 * Created on Nov 18, 2005
 * 
 * BibliographicDateOfLastTransactionTag.java
 */
package librisuite.business.cataloguing.bibliographic;

import librisuite.business.cataloguing.common.CasaliniSourceTag;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public class BibliographicCasaliniSourceTag
	extends CasaliniSourceTag {

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public BibliographicCasaliniSourceTag() {
		super();
		setHeaderType((short)100);
	}

}
