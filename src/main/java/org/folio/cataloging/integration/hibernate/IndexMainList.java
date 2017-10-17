package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;

/**
 * @author Hansv
 * @version $Revision: 1.1 $, $Date: 2005/09/02 13:17:51 $ 
 */
public class IndexMainList implements Serializable {
	private int indexKey;
	private int indexValueCode;
	private String indexMainName;
	private String indexType;
	private String language;
	
	
	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public String getIndexMainName() {
		return indexMainName;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public int getIndexValueCode() {
		return indexValueCode;
	}

	/**
	 * 
	 * 
	 * @param string
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setIndexMainName(String string) {
		indexMainName = string;
	}

	/**
	 * 
	 * 
	 * @param i
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setIndexValueCode(int i) {
		indexValueCode = i;
	}
	
	public boolean equals(Object anObject) {
				IndexMainList aKey = (IndexMainList) anObject;
				if (this.getIndexKey() == aKey.getIndexKey()) {				
						return true;				
				}
				return false;
			}

			/* (non-Javadoc)
			 * @see java.lang.Object#hashCode()
			 */
			public int hashCode() {
				return this.getIndexKey();
			}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public String getIndexType() {
		return indexType;
	}

	/**
	 * 
	 * 
	 * @param string
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setIndexType(String string) {
		indexType = string;
	}

	
	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * 
	 * 
	 * @param string
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setLanguage(String string) {
		language = string;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public int getIndexKey() {
		return indexKey;
	}

	/**
	 * 
	 * 
	 * @param i
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setIndexKey(int i) {
		indexKey = i;
	}

}
	