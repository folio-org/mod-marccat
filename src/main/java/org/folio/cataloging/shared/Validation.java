
package org.folio.cataloging.shared;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

/**
 * @author paulm
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

	public void setMarcTagEditableIndicator(char c) {
		marcTagEditableIndicator = c;
	}

	public void setMarcTagObsoleteIndicator(char c) {
		marcTagObsoleteIndicator = c;
	}

	public void setMarcTagRepeatableIndicator(char c) {
		marcTagRepeatableIndicator = c;
	}

	public void setMarcValidSubfieldStringCode(String string) {
		marcValidSubfieldStringCode = string;
	}

	public void setRepeatableSubfieldStringCode(String string) {
		repeatableSubfieldStringCode = string;
	}

	public void setSkipInFlngCode(char c) {
		skipInFlngCode = c;
	}

	public List<String> getValidSubfieldCodes() {
		return stream(marcValidSubfieldStringCode.split("")).collect(Collectors.toList());
	}

	public List<String> getRepeatableSubfieldCodes() {
		return stream(repeatableSubfieldStringCode.split("")).collect(Collectors.toList());
	}

	/*public Set computeRemainingValidSubfields(StringText stringText) {
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
	}*/

	private char marcTagObsoleteIndicator;

	private char marcTagRepeatableIndicator;

	private char marcTagEditableIndicator;

	private char marcTagDefaultSubfieldCode;

	private String marcValidSubfieldStringCode;

	private String repeatableSubfieldStringCode;

	private char skipInFlngCode;

	abstract public ValidationKey getKey();
	
	/*public String toString() {
		return getKey()
			+ "valid codes: '"
			+ marcValidSubfieldStringCode
			+ "' repeatable: '"
			+ repeatableSubfieldStringCode
			+ "'";
	}*/

}
