package org.folio.cataloging.shared;

import java.io.Serializable;

/**
 *
 * @author elena
 * @since 1.0
 */
public class ValidationKey implements Serializable {
		private String marcTag;
		private int marcTagCategoryCode;

		/**
		 * Class constructor
		 *
		 * @since 1.0
		 */
		public ValidationKey() {
			super();
		}

		public ValidationKey(final String marcTag, final int marcTagCategory) {
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
		public int getMarcTagCategoryCode() {
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
		private void setMarcTagCategoryCode(int s) {
			marcTagCategoryCode = s;
		}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "[ tag: '" + marcTag + "' category: '" + marcTagCategoryCode + "' ]"; 
	}

}
