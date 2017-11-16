/*
 * (c) LibriCore
 * 
 * Created on 28-jul-2004
 * 
 * BibliographicValidationKey.java
 */
package librisuite.hibernate;

import java.io.Serializable;

/**
 * @author elena
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:05 $
 * @since 1.0
 */
public class ValidationKey implements Serializable {

		private String marcTag;
		private short marcTagCategoryCode;

		/**
		 * Class constructor
		 *
		 * @since 1.0
		 */
		public ValidationKey() {
			super();
		}

		public ValidationKey(
			String marcTag,
			short marcTagCategory) {
			this.setMarcTag(marcTag);
			this.setMarcTagCategoryCode(marcTagCategory);
		}

		/**
		 * override equals and hashcode for hibernate key comparison
		 */

		public boolean equals(Object o) {
			if (o instanceof ValidationKey) {
				ValidationKey key = (ValidationKey) o;
				return marcTag.equals(key.getMarcTag())
					&& marcTagCategoryCode == key.getMarcTagCategoryCode();
			} else {
				return false;
			}
		}

		public int hashCode() {
			return marcTag.hashCode() + (11 * marcTagCategoryCode);
		}

		/**
		 * 
		 * 
		 * @since 1.0
		 */
		public String getMarcTag() {
			return marcTag;
		}

		/**
		 * 
		 * 
		 * @since 1.0
		 */
		public short getMarcTagCategoryCode() {
			return marcTagCategoryCode;
		}

		/**
		 * 
		 * 
		 * @param string
		 * @since 1.0
		 */
		private void setMarcTag(String string) {
			marcTag = string;
		}

		/**
		 * 
		 * 
		 * @param s
		 * @since 1.0
		 */
		private void setMarcTagCategoryCode(short s) {
			marcTagCategoryCode = s;
		}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "[ tag: '" + marcTag + "' category: '" + marcTagCategoryCode + "' ]"; 
	}

}
