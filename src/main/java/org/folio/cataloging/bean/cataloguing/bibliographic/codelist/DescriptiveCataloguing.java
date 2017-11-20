/*
 * (c) LibriCore
 * 
 * $Author: wimc $
 * $Date: 2005/03/10 08:44:38 $
 * $Locker:  $
 * $Name:  $
 * $Revision: 1.2 $
 * $Source: /source/LibriSuite/src/librisuite/bean/cataloguing/bibliographic/codelist/DescriptiveCataloguing.java,v $
 * $State: Exp $
 */
package org.folio.cataloging.bean.cataloguing.bibliographic.codelist;

import org.folio.cataloging.dao.persistence.T_ITM_DSCTV_CTLG;

/**
 * @author Wim Crols
 * @version $Revision: 1.2 $, $Date: 2005/03/10 08:44:38 $
 * @since 1.0
 */
public class DescriptiveCataloguing extends CodeListBean {

	public DescriptiveCataloguing() {
		super(T_ITM_DSCTV_CTLG.class);
	}

}
