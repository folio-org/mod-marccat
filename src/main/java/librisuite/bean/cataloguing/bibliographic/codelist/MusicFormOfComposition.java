/*
 * (c) LibriCore
 * 
 * $Author: wimc $
 * $Date: 2005/03/10 09:45:07 $
 * $Locker:  $
 * $Name:  $
 * $Revision: 1.1 $
 * $Source: /source/LibriSuite/src/librisuite/bean/cataloguing/bibliographic/codelist/MusicFormOfComposition.java,v $
 * $State: Exp $
 */
package librisuite.bean.cataloguing.bibliographic.codelist;

import librisuite.hibernate.T_MSC_FORM_OR_TYP;

/**
 * @author Wim Crols
 * @version $Revision: 1.1 $, $Date: 2005/03/10 09:45:07 $
 * @since 1.0
 */
public class MusicFormOfComposition extends CodeListBean {

	public MusicFormOfComposition() {
		super(T_MSC_FORM_OR_TYP.class);
	}

}
