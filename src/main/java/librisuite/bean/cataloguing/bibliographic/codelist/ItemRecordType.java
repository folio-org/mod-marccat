/*
 * (c) LibriCore
 * 
 * $Author: wimc $
 * $Date: 2005/03/10 08:50:51 $
 * $Locker:  $
 * $Name:  $
 * $Revision: 1.2 $
 * $Source: /source/LibriSuite/src/librisuite/bean/cataloguing/bibliographic/codelist/ItemRecordType.java,v $
 * $State: Exp $
 */
package librisuite.bean.cataloguing.bibliographic.codelist;

import librisuite.hibernate.T_ITM_REC_TYP;

/**
 * @author Wim Crols
 * @version $Revision: 1.2 $, $Date: 2005/03/10 08:50:51 $
 * @since 1.0
 */
public class ItemRecordType extends CodeListBean {

	public ItemRecordType() {
		super(T_ITM_REC_TYP.class);
	}

}
