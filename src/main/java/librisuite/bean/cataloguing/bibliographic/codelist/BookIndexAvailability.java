/*
 * (c) LibriCore
 * 
 * $Author: wimc $
 * $Date: 2005/03/10 08:45:04 $
 * $Locker:  $
 * $Name:  $
 * $Revision: 1.2 $
 * $Source: /source/LibriSuite/src/librisuite/bean/cataloguing/bibliographic/codelist/BookIndexAvailability.java,v $
 * $State: Exp $
 */
package librisuite.bean.cataloguing.bibliographic.codelist;

import librisuite.hibernate.T_BOOK_IDX_AVBTY;

/**
 * @author Wim Crols
 * @version $Revision: 1.2 $, $Date: 2005/03/10 08:45:04 $
 * @since 1.0
 */
public class BookIndexAvailability extends CodeListBean {

	public BookIndexAvailability() {
		super(T_BOOK_IDX_AVBTY.class);
	}

}
