/*
 * (c) LibriCore
 * 
 * $Author: wimc $
 * $Date: 2005/03/10 08:55:28 $
 * $Locker:  $
 * $Name:  $
 * $Revision: 1.2 $
 * $Source: /source/LibriSuite/src/librisuite/bean/cataloguing/bibliographic/codelist/SoundTapeConfiguration.java,v $
 * $State: Exp $
 */
package org.folio.cataloging.bean.cataloguing.bibliographic.codelist;

import org.folio.cataloging.dao.persistence.T_SND_TAPE_CFGTN;

/**
 * @author Wim Crols
 * @version $Revision: 1.2 $, $Date: 2005/03/10 08:55:28 $
 * @since 1.0
 */
public class SoundTapeConfiguration extends CodeListBean {

	public SoundTapeConfiguration() {
		super(T_SND_TAPE_CFGTN.class);
	}

}
