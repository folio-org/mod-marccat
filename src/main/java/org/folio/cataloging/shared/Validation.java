package org.folio.cataloging.shared;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

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
		return stream(marcValidSubfieldStringCode.split("")).collect(toList());
	}

	public List<String> getRepeatableSubfieldCodes() {
		return stream(repeatableSubfieldStringCode.split("")).collect(toList());
	}

	private char marcTagObsoleteIndicator;

	private char marcTagRepeatableIndicator;

	private char marcTagEditableIndicator;

	private char marcTagDefaultSubfieldCode;

	private String marcValidSubfieldStringCode;

	private String repeatableSubfieldStringCode;

	private char skipInFlngCode;

	abstract public ValidationKey getKey();
}
