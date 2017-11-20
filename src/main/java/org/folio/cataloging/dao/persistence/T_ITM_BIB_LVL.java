/*
 * (c) LibriCore
 * 
 * Created on Oct 13, 2004
 * 
 * T_ITM_BIB_LVL.java
 */
package org.folio.cataloging.dao.persistence;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/10/15 15:15:05 $
 * @since 1.0
 */
public class T_ITM_BIB_LVL extends T_SINGLE_CHAR {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final char SERIAL = 's';
	public static final char COMPONENT_PART_SERIAL = 'b';

	public static boolean isSerial(char bib_level) {
		return bib_level == SERIAL || bib_level == COMPONENT_PART_SERIAL;
	}

}

