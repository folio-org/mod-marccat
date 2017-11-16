/*
 * (c) LibriCore
 * 
 * $Author: wimc $
 * $Date: 2005/03/10 08:45:04 $
 * $Locker:  $
 * $Name:  $
 * $Revision: 1.2 $
 * $Source: /source/LibriSuite/src/librisuite/bean/cataloguing/bibliographic/codelist/CharacterCodingScheme.java,v $
 * $State: Exp $
 */
package librisuite.bean.cataloguing.bibliographic.codelist;

import librisuite.hibernate.T_ITM_CCS;

/**
 * @author Wim Crols
 * @version $Revision: 1.2 $, $Date: 2005/03/10 08:45:04 $
 * @since 1.0
 */
public class CharacterCodingScheme extends CodeListBean {

	public CharacterCodingScheme() {
		super(T_ITM_CCS.class);
	}

}
