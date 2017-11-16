/*
 * (c) LibriCore
 * 
 * Created on 18-ago-2004
 * 
 * T_LANG_OF_IDXG_LANG.java
 */
package librisuite.hibernate;

/**
 * @author elena
 * @version $Revision: 1.1 $, $Date: 2004/08/19 14:05:26 $
 * @since 1.0
 */
public class T_LANG_OF_IDXG_LANG {

	private int languageIndexing;
	private int language;

	/**
	 * 
	 * 
	 * @since 1.0
	 */
	public int getLanguage() {
		return language;
	}

	/**
	 * 
	 * 
	 * @since 1.0
	 */
	public int getLanguageIndexing() {
		return languageIndexing;
	}

	/**
	 * 
	 * 
	 * @param i
	  * @since 1.0
	 */
	public void setLanguage(int i) {
		language = i;
	}

	/**
	 * 
	 * 
	 * @param i
	 * @since 1.0
	 */
	public void setLanguageIndexing(int i) {
		languageIndexing = i;
	}

}
