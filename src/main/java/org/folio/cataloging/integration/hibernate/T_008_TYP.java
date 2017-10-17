/*
 * (c) LibriCore
 * 
 * Created on 17-ago-2004
 * 
 * T_OO8_TYP.java
 */
package org.folio.cataloging.integration.hibernate;

/**
 * @author Maite
 * @version $Revision: 1.1 $, $Date: 2004/08/18 14:39:35 $
 * @since 1.0
 */
public class T_008_TYP {
	
	private T_008_TYPKey key;
	private short bibliographicHeaderCode;
	
	
	
	/** * GETTER AND SETTERS * **/
	public short getBibliographicHeaderCode() {
		return bibliographicHeaderCode;
	}

	public void setBibliographicHeaderCode(short s) {
		bibliographicHeaderCode = s;
	}

	public T_008_TYPKey getKey() {
		return key;
	}

	
	public void setKey(T_008_TYPKey key) {
		this.key = key;
	}

}
