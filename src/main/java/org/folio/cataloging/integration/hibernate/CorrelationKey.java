/*
 * (c) LibriCore
 * 
 * Created on 28-jul-2004
 * 
 * Correlation.java
 */
package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author elena
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:05 $
 * @since 1.0
 */
public class CorrelationKey implements Serializable {

	private static final Log logger = LogFactory.getLog(CorrelationKey.class);
	
	private String marcTag;
	private char marcFirstIndicator;
	private char marcSecondIndicator;
	private short marcTagCategoryCode;

	/**
	 * Class constructor
	 *
	 * @since 1.0
	 */
	public CorrelationKey() {
		super();
	}

	public CorrelationKey(
			String marcTag,
			char marcTag1,
			char marcTag2,
		short marcTagCategory) {
			this.setMarcTag(marcTag);
			this.setMarcFirstIndicator(marcTag1);
			this.setMarcSecondIndicator(marcTag2);
			this.setMarcTagCategoryCode(marcTagCategory);
		}

	/**
	 * override equals and hashcode for hibernate key comparison
	 */

	public boolean equals(Object anObject) {
		if (anObject instanceof CorrelationKey) {
			CorrelationKey aKey =
				(CorrelationKey) anObject;
			return (
				marcTag.equals(aKey.getMarcTag())
					&& marcFirstIndicator == aKey.getMarcFirstIndicator()
					&& marcSecondIndicator == aKey.getMarcSecondIndicator()
					&& marcTagCategoryCode == aKey.getMarcTagCategoryCode());
		} else {
			return false;
		}
	}

	public int hashCode() {
		return marcTag.hashCode()
			+ (3 * marcFirstIndicator)
			+ (5 * marcSecondIndicator)
			+ (7 * marcTagCategoryCode);
	}

	public short getMarcTagCategoryCode() {
		return marcTagCategoryCode;
	}

	public char getMarcFirstIndicator() {
		return marcFirstIndicator;
	}

	public String getMarcTag() {
		return marcTag;
	}

	public char getMarcSecondIndicator() {
		return marcSecondIndicator;
	}

	private void setMarcTagCategoryCode(short s) {
		marcTagCategoryCode = s;
	}

	private void setMarcFirstIndicator(char c) {
		marcFirstIndicator = c;
	}

	private void setMarcTag(String s) {
		marcTag = s;
	}

	private void setMarcSecondIndicator(char c) {
		marcSecondIndicator = c;
	}
	
	public CorrelationKey changeSkipInFilingIndicator(int skip) {
		if (marcFirstIndicator == 'S') return changeFirstIndicator(Integer.toString(skip).charAt(0));
		if (marcSecondIndicator == 'S') return changeSecondIndicator(Integer.toString(skip).charAt(0));
		return this;
	}
	
	public CorrelationKey changeAuthoritySourceIndicator(int source) {
		logger.debug("changeAuthoritySource: " + getMarcSecondIndicator());
		if (marcFirstIndicator == 'O') {
			return changeFirstIndicator(T_AUT_HDG_SRC.toMarcIndicator(source));
		}
		if (marcSecondIndicator == 'O') {
			logger.debug("changing to " + T_AUT_HDG_SRC.toMarcIndicator(source));
			return changeSecondIndicator(T_AUT_HDG_SRC.toMarcIndicator(source));
		}
		return this;
	}
	
	public CorrelationKey changeFirstIndicator(char c) {
		return new CorrelationKey(marcTag, c, marcSecondIndicator, marcTagCategoryCode);
	}
	
	public CorrelationKey changeSecondIndicator(char c) {
		return new CorrelationKey(marcTag, marcFirstIndicator, c, marcTagCategoryCode);
	}

	@Override
	public String toString() {
		return "CorrelationKey('" + getMarcTag() + getMarcFirstIndicator() + getMarcSecondIndicator() + "')";
	}

}
