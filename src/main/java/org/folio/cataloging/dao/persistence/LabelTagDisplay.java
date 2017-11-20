package org.folio.cataloging.dao.persistence;

public abstract class LabelTagDisplay 
{
	private static final long serialVersionUID = -8261629624048690065L;
	private String marcTag;
	private Character marcFirstIndicator;
	private Character marcSecondIndicator;
	private String marcTagDescription;
	private String language;
	private String marcTagPunctuation;
	private String marcTagSubfieldCode;
	private String marcFbrType;
	private String marcTagNumberText;
	
	
	public String getMarcTag() {
		return marcTag;
	}
	public void setMarcTag(String marcTag) {
		this.marcTag = marcTag;
	}
	public String getMarcTagDescription() {
		return marcTagDescription;
	}
	public void setMarcTagDescription(String marcTagDescription) {
		this.marcTagDescription = marcTagDescription;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getMarcTagPunctuation() {
		return marcTagPunctuation;
	}
	public void setMarcTagPunctuation(String marcTagPunctuation) {
		this.marcTagPunctuation = marcTagPunctuation;
	}
	public Character getMarcFirstIndicator() {
		return marcFirstIndicator;
	}
	public void setMarcFirstIndicator(Character marcFirstIndicator) {
		this.marcFirstIndicator = marcFirstIndicator;
	}
	public Character getMarcSecondIndicator() {
		return marcSecondIndicator;
	}
	public void setMarcSecondIndicator(Character marcSecondIndicator) {
		this.marcSecondIndicator = marcSecondIndicator;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((language == null) ? 0 : language.hashCode());
		result = prime * result
				+ ((marcFbrType == null) ? 0 : marcFbrType.hashCode());
		result = prime
				* result
				+ ((marcFirstIndicator == null) ? 0 : marcFirstIndicator
						.hashCode());
		result = prime
				* result
				+ ((marcSecondIndicator == null) ? 0 : marcSecondIndicator
						.hashCode());
		result = prime * result + ((marcTag == null) ? 0 : marcTag.hashCode());
		result = prime
				* result
				+ ((marcTagSubfieldCode == null) ? 0 : marcTagSubfieldCode
						.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final LabelTagDisplay other = (LabelTagDisplay) obj;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (marcFbrType == null) {
			if (other.marcFbrType != null)
				return false;
		} else if (!marcFbrType.equals(other.marcFbrType))
			return false;
		if (marcFirstIndicator == null) {
			if (other.marcFirstIndicator != null)
				return false;
		} else if (!marcFirstIndicator.equals(other.marcFirstIndicator))
			return false;
		if (marcSecondIndicator == null) {
			if (other.marcSecondIndicator != null)
				return false;
		} else if (!marcSecondIndicator.equals(other.marcSecondIndicator))
			return false;
		if (marcTag == null) {
			if (other.marcTag != null)
				return false;
		} else if (!marcTag.equals(other.marcTag))
			return false;
		if (marcTagSubfieldCode == null) {
			if (other.marcTagSubfieldCode != null)
				return false;
		} else if (!marcTagSubfieldCode.equals(other.marcTagSubfieldCode))
			return false;
		return true;
	}
	public String getMarcTagSubfieldCode() {
		return marcTagSubfieldCode;
	}
	public void setMarcTagSubfieldCode(String marcTagSubfieldCode) {
		this.marcTagSubfieldCode = marcTagSubfieldCode;
	}
	public String getMarcFbrType() {
		return marcFbrType;
	}
	public void setMarcFbrType(String marcFbrType) {
		this.marcFbrType = marcFbrType;
	}
	public String getMarcTagNumberText() {
		return marcTagNumberText;
	}
	public void setMarcTagNumberText(String marcTagNumberText) {
		this.marcTagNumberText = marcTagNumberText;
	}
	
	@Override
	/*
	 * 			key = rdaBuildKey(
						marc.getMarcTag(),
					   (marc.getMarcFirstIndicator()==null?null:marc.getMarcFirstIndicator().toString()),
					   (marc.getMarcSecondIndicator()==null?null:marc.getMarcSecondIndicator().toString()), 
						marc.getMarcFbrType(),
					   (marc.getMarcTagSubfieldCode()==null?null:marc.getMarcTagSubfieldCode().toString()));

	 */
	public String toString() {
		return "K("+marcTag+","+marcFirstIndicator+","+marcSecondIndicator+","+marcFbrType+","+marcTagSubfieldCode+")";
	}
}