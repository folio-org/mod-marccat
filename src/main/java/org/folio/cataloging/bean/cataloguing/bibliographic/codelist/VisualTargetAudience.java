/*
 * (c) LibriCore
 * 
 * $Author: wimc $
 * $Date: 2005/03/10 08:55:28 $
 * $Locker:  $
 * $Name:  $
 * $Revision: 1.1 $
 * $Source: /source/LibriSuite/src/librisuite/bean/cataloguing/bibliographic/codelist/VisualTargetAudience.java,v $
 * $State: Exp $
 */
package org.folio.cataloging.bean.cataloguing.bibliographic.codelist;

import org.folio.cataloging.dao.persistence.T_VSL_TRGT_AUDNC;

/**
 * @author Wim Crols
 * @version $Revision: 1.1 $, $Date: 2005/03/10 08:55:28 $
 * @since 1.0
 */
public class VisualTargetAudience extends CodeListBean {

	public VisualTargetAudience() {
		super(T_VSL_TRGT_AUDNC.class);
	}

}
