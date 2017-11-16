package librisuite.hibernate;

import java.io.Serializable;

/**
 * @author Hansv
 * @version $Revision: 1.1 $, $Date: 2005/09/02 13:18:08 $ 
 */
public class IndexSubList implements Serializable {
	private int listKey;
	private int indexValueCode;
	private int indexSubValueCode;
	private String indexSubName;
	private String indexSearchCode;
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
	public String getIndexSubName() {
		return indexSubName;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public int getIndexSubValueCode() {
		return indexSubValueCode;
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
	public void setIndexSubName(String string) {
		indexSubName = string;
	}

	/**
	 * 
	 * 
	 * @param i
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setIndexSubValueCode(int i) {
		indexSubValueCode = i;
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

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public String getIndexSearchCode() {
		return indexSearchCode;
	}

	/**
	 * 
	 * 
	 * @param string
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setIndexSearchCode(String string) {
		indexSearchCode = string;
	}
	public boolean equals(Object anObject) {
			IndexSubList aKey = (IndexSubList) anObject;
			if (this.getListKey() == aKey.getListKey()) {				
					return true;				
			}
			return false;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			return this.getListKey();
		}


	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public int getListKey() {
		return listKey;
	}

	/**
	 * 
	 * 
	 * @param i
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setListKey(int i) {
		listKey = i;
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

}
	