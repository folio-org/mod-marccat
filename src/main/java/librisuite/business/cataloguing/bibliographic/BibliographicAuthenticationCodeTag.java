/*
 * (c) LibriCore
 * 
 * Created on Nov 18, 2005
 * 
 * BibliographicAuthenticationCodeTag.java
 */
package librisuite.business.cataloguing.bibliographic;

import librisuite.business.cataloguing.common.AuthenticationCodeTag;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public class BibliographicAuthenticationCodeTag extends AuthenticationCodeTag {

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public BibliographicAuthenticationCodeTag() {
		super();
		setHeaderType((short) 4);
	}

}
