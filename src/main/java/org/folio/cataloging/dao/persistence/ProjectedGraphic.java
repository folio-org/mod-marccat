package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.DAOSystemNextNumber;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class ProjectedGraphic extends PhysicalDescription {
	private char colourCode;
	private char baseOfEmulsionCode;
	private char soundOnMediumOrSeparateCode;
	private char mediumForSoundCode;
	private char dimensionsCode;
	private char secondarySupportMaterialCode;
	private char obsolete1;

	/*
	private char colourCode = 'u';
	private char baseOfEmulsionCode = 'u';
	private char soundOnMediumOrSeparateCode = 'u';
	private char mediumForSoundCode = 'u';
	private char dimensionsCode = 'u';
	private char secondarySupportMaterialCode = 'u';
	private char obsolete1;
	 */
	public ProjectedGraphic() {
		super();
		setHeaderType(28);
		//setGeneralMaterialDesignationCode('g');
		//setSpecificMaterialDesignationCode('u');
	}

	/* (non-Javadoc)
	 * @see FixedField#getDisplayString()
	 */
	public String getDisplayString() {
		String result =
			""
				+ getGeneralMaterialDesignationCode()
				+ getSpecificMaterialDesignationCode()
				+ " "
				+ getColourCode()
				+ getBaseOfEmulsionCode()
		+ getSoundOnMediumOrSeparateCode()
		+ getMediumForSoundCode()
		+ getDimensionsCode()
		+ getSecondarySupportMaterialCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException {
		DAOSystemNextNumber dao = new DAOSystemNextNumber();
		setKeyNumber(dao.getNextNumber("X7"));
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isProjectedGraphic()
	 */
	public boolean isProjectedGraphic() {
		return true;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getBaseOfEmulsionCode() {
		return baseOfEmulsionCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getColourCode() {
		return colourCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getDimensionsCode() {
		return dimensionsCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getMediumForSoundCode() {
		return mediumForSoundCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getObsolete1() {
		return obsolete1;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getSecondarySupportMaterialCode() {
		return secondarySupportMaterialCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getSoundOnMediumOrSeparateCode() {
		return soundOnMediumOrSeparateCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setBaseOfEmulsionCode(char c) {
		baseOfEmulsionCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setColourCode(char c) {
		colourCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setDimensionsCode(char c) {
		dimensionsCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setMediumForSoundCode(char c) {
		mediumForSoundCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setObsolete1(char c) {
		obsolete1 = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setSecondarySupportMaterialCode(char c) {
		secondarySupportMaterialCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setSoundOnMediumOrSeparateCode(char c) {
		soundOnMediumOrSeparateCode = c;
	}

	public Element generateModelXmlElementContent(Document xmlDocument) {
		Element content = null;
		if (xmlDocument != null) {
			content = xmlDocument.createElement("content");
			content.setAttribute("generalMaterialDesignationCode", "" + getGeneralMaterialDesignationCode());
			content.setAttribute("specificMaterialDesignationCode", "" + getSpecificMaterialDesignationCode());
			content.setAttribute("colourCode", "" + getColourCode());
			content.setAttribute("baseOfEmulsionCode", "" + getBaseOfEmulsionCode());
			content.setAttribute("soundOnMediumOrSeparateCode", "" + getSoundOnMediumOrSeparateCode());
			content.setAttribute("mediumForSoundCode", "" + getMediumForSoundCode());
			content.setAttribute("dimensionsCode", "" + getDimensionsCode());
			content.setAttribute("secondarySupportMaterialCode", "" + getSecondarySupportMaterialCode());
		}
		return content;
	}

	public void parseModelXmlElementContent(Element xmlElement) {
		Element content = (Element) xmlElement.getChildNodes().item(0);
		setGeneralMaterialDesignationCode(content.getAttribute("generalMaterialDesignationCode").charAt(0));
		setSpecificMaterialDesignationCode(content.getAttribute("specificMaterialDesignationCode").charAt(0));
		setColourCode(content.getAttribute("colourCode").charAt(0));
		setBaseOfEmulsionCode(content.getAttribute("baseOfEmulsionCode").charAt(0));
		setSoundOnMediumOrSeparateCode(content.getAttribute("soundOnMediumOrSeparateCode").charAt(0));
		setMediumForSoundCode(content.getAttribute("mediumForSoundCode").charAt(0));
		setDimensionsCode(content.getAttribute("dimensionsCode").charAt(0));
		setSecondarySupportMaterialCode(content.getAttribute("secondarySupportMaterialCode").charAt(0));
	}

}
