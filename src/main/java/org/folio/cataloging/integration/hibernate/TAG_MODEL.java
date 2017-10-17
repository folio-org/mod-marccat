/*
 * (c) LibriCore
 * 
 * Created on 21-jun-2004
 * 
 * SHLF_LIST.java
 */
package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;

import librisuite.business.marchelper.parser.PunctuationList;

import com.libricore.librisuite.common.StringText;
import com.libricore.librisuite.common.Subfield;

public class TAG_MODEL implements Serializable {
	private ModelTagKey key;
	private char obsoleteIndicator;
	private int code;
	private int keyGroup;
	private String descriptionText;
	private String sampleText;
	private String patternText;
	private PunctuationList punctuationElements;
	
	
	
	public String getDescriptionText() {
		return descriptionText;
	}
	public void setDescriptionText(String descriptionText) {
		this.descriptionText = descriptionText;
	}
	public String getSampleText() {
		return sampleText;
	}
	public void setSampleText(String sampleText) {
		this.sampleText = sampleText;
	}
	public String getPatternText() {
		return patternText;
	}
	public void setPatternText(String patternText) {
		this.patternText = patternText;
	}
	
	public int getKeyGroup() {
		return keyGroup;
	}
	public void setKeyGroup(int keyGroup) {
		this.keyGroup = keyGroup;
	}
	
	public char getObsoleteIndicator() {
		return obsoleteIndicator;
	}
	public void setObsoleteIndicator(char obsoleteIndicator) {
		this.obsoleteIndicator = obsoleteIndicator;
	}
	
	public void setPunctuationElements(PunctuationList elements) {
		this.punctuationElements = elements;
	}
	public PunctuationList getPunctuationElements() {
		return punctuationElements;
	}
	
	public StringText getPatternStringText(char dbSubfieldSeparator){
		StringText st = new StringText(getPatternText().replace(dbSubfieldSeparator, Subfield.SUBFIELD_DELIMITER.charAt(0)));
		return st;
	}
	public ModelTagKey getKey() {
		return key;
	}
	public void setKey(ModelTagKey key) {
		this.key = key;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TAG_MODEL other = (TAG_MODEL) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	
	public String toString() {
		return "" + key.getSequence() + ": "
		+ getCode() + ", "
		+ key.getLanguage() + ", "
		+ descriptionText;
	}
	
	/**
	 * Simple algorithm to estimate the model complexity
	 * @return
	 */
	public int getComplexity(){
		PunctuationList subfields = getPunctuationElements().getFilteredSubfieldList();
		PunctuationList fields = getPunctuationElements().getFilteredFieldList(null);
		return 11 * subfields.size() + 31 * fields.size();
	}

}
