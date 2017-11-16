/*
 * (c) LibriCore
 * 
 * $Author: wimc $
 * $Date: 2005/03/10 08:44:38 $
 * $Locker:  $
 * $Name:  $
 * $Revision: 1.2 $
 * $Source: /source/LibriSuite/src/librisuite/bean/cataloguing/bibliographic/codelist/ComputerTargetAudience.java,v $
 * $State: Exp $
 */
package librisuite.bean.cataloguing.bibliographic.codelist;

import librisuite.hibernate.T_CMPTR_TRGT_AUDNC;

/**
 * @author Wim Crols
 * @version $Revision: 1.2 $, $Date: 2005/03/10 08:44:38 $
 * @since 1.0
 */
public class ComputerTargetAudience extends CodeListBean {

	public ComputerTargetAudience() {
		super(T_CMPTR_TRGT_AUDNC.class);
	}

}
