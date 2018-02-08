/*
 * (c) LibriCore
 * 
 * Created on Aug 13, 2004
 * 
 * Defaults.java
 */
package org.folio.cataloging.business.common;

import java.util.*;

/**
 * provides access to default values established in property pages
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2006/07/11 08:01:06 $
 * @since 1.0
 */
public class Defaults {

	static public Class getClazz(String key) {
		String result = getString(key);
		try {
			return Class.forName(result);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	static public String getString(String key){
		ResourceBundle defaults =
			ResourceBundle.getBundle("resources/defaultValues");
		return defaults.getString(key);
	}
	
	public static String getString(String key, String ifNotPresentValue) {
		try {
			return getString(key);
		} catch (MissingResourceException e) {
			return ifNotPresentValue;
		}
	}
	
	static public short getShort(String key) {
		String result = getString(key);
		return (short)Integer.parseInt(result);
	}

	static public int getInteger(String key) {
		String result = getString(key);
		return Integer.parseInt(result);
	}

	static public char getChar(String key) {
		String result = getString(key);
		return result.charAt(0);
	}

	static public Character getCharacter(String key) {
		String result = getString(key);
		return new Character(result.charAt(0));
	}
	
	static public boolean getBoolean(String key) {
		String result = getString(key);
		return Boolean.valueOf(result).booleanValue();
	}
	
	static public boolean getBoolean(String key, boolean ifNotPresentValue) {
		try {
			return getBoolean(key);
		} catch (MissingResourceException e) {
			return ifNotPresentValue;
		}
	}
	
	public static List/*<String>*/ getAllKeys(){
		ResourceBundle defaults =
			ResourceBundle.getBundle("resources/defaultValues");
		List/*<String>*/ elencoChiavi = new ArrayList/*<String>*/();
		Enumeration/*<String>*/ enm = defaults.getKeys();
		while (enm.hasMoreElements()) {
			String element = (String) enm.nextElement();
			elencoChiavi.add(element);
		}
		return elencoChiavi;
	}

}
