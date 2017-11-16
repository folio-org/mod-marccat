/*
 * (c) LibriCore
 * 
 * $Author: wimc $
 * $Date: 2005/03/10 08:45:04 $
 * $Locker:  $
 * $Name:  $
 * $Revision: 1.2 $
 * $Source: /source/LibriSuite/src/librisuite/bean/cataloguing/bibliographic/codelist/BookLiteraryForm.java,v $
 * $State: Exp $
 */
package librisuite.bean.cataloguing.bibliographic.codelist;

import librisuite.hibernate.T_BOOK_LTRY_FORM_TYP;

/**
 * @author Wim Crols
 * @version $Revision: 1.2 $, $Date: 2005/03/10 08:45:04 $
 * @since 1.0
 */
public class BookLiteraryForm extends CodeListBean {

	public BookLiteraryForm() {
		super(T_BOOK_LTRY_FORM_TYP.class);
	}

}
