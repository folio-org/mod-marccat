
package org.folio.cataloging.business.cataloguing.common;

import org.folio.cataloging.business.common.SubfieldCodeComparator;
import org.folio.cataloging.dao.persistence.ValidationKey;
import org.folio.cataloging.util.StringText;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public abstract class Validation {

	public char getMarcTagDefaultSubfieldCode() {
		return marcTagDefaultSubfieldCode;
	}

	public char getMarcTagEditableIndicator() {
		return marcTagEditableIndicator;
	}

	public char getMarcTagObsoleteIndicator() {
		return marcTagObsoleteIndicator;
	}

	public char getMarcTagRepeatableIndicator() {
		return marcTagRepeatableIndicator;
	}

	/**
		 * helper method to provide boolean result
		 * 
		 * @since 1.0
		 */
	public boolean isMarcTagRepeatable() {
		return marcTagRepeatableIndicator == '1';
	}

	public String getMarcValidSubfieldStringCode() {
		return marcValidSubfieldStringCode;
	}

	public String getRepeatableSubfieldStringCode() {
		return repeatableSubfieldStringCode;
	}

	public char getSkipInFlngCode() {
		return skipInFlngCode;
	}

	public void setMarcTagDefaultSubfieldCode(char c) {
		marcTagDefaultSubfieldCode = c;
	}

	protected void setMarcTagEditableIndicator(char c) {
		marcTagEditableIndicator = c;
	}

	protected void setMarcTagObsoleteIndicator(char c) {
		marcTagObsoleteIndicator = c;
	}

	protected void setMarcTagRepeatableIndicator(char c) {
		marcTagRepeatableIndicator = c;
	}

	protected void setMarcValidSubfieldStringCode(String string) {
		marcValidSubfieldStringCode = string;
	}

	protected void setRepeatableSubfieldStringCode(String string) {
		repeatableSubfieldStringCode = string;
	}

	protected void setSkipInFlngCode(char c) {
		skipInFlngCode = c;
	}

	public Set getValidSubfieldCodes() {
		return StringText.stringToSetOfSubfieldCodes(marcValidSubfieldStringCode);
	}

	public Set getRepeatableSubfieldCodes() {
		return StringText.stringToSetOfSubfieldCodes(repeatableSubfieldStringCode);
	}

	public Set computeRemainingValidSubfields(StringText stringText) {
		Set remainingValidSubfields = new TreeSet(new SubfieldCodeComparator());
		remainingValidSubfields.addAll(getValidSubfieldCodes());
		remainingValidSubfields.removeAll(stringText.getUsedSubfieldCodes());
		remainingValidSubfields.addAll(getRepeatableSubfieldCodes());
		return remainingValidSubfields;
	}

	public List computeValidSubfieldList(StringText stringText) {
		List validSubfieldList = new ArrayList();
		Set remainingValidSubfields = computeRemainingValidSubfields(stringText);
		
		for (int i = 0; i < stringText.getNumberOfSubfields(); i++) {
			Set validCodesForCurrentSubfield = new TreeSet(new SubfieldCodeComparator());
			validCodesForCurrentSubfield.addAll(remainingValidSubfields);
			validCodesForCurrentSubfield.add(stringText.getSubfield(i).getCode());
			validSubfieldList.add(validCodesForCurrentSubfield);
		}
		return validSubfieldList;
	}

	protected char marcTagObsoleteIndicator;

	protected char marcTagRepeatableIndicator;

	protected char marcTagEditableIndicator;

	protected char marcTagDefaultSubfieldCode;

	protected String marcValidSubfieldStringCode;

	protected String repeatableSubfieldStringCode;

	protected char skipInFlngCode;

	abstract public ValidationKey getKey();
	
	public String toString() {
		return getKey()
			+ "valid codes: '"
			+ marcValidSubfieldStringCode
			+ "' repeatable: '"
			+ repeatableSubfieldStringCode
			+ "'";
	}

}
