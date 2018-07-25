/*
 * (c) LibriCore
 * 
 * Created on Oct 15, 2004
 * 
 * SoundRecording.java
 */
package org.folio.cataloging.business.cataloguing.bibliographic;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.DAOSystemNextNumber;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public class SoundRecording extends PhysicalDescription {
	private char speedCode = 'u';
	private char configurationCode = 'u';
	private char grooveWidthCode = 'u';
	private char dimensionsCode = 'u';
	private char tapeWidthCode = 'u';
	private char tapeConfigurationCode = 'u';
	private char discTypeCode = 'u';
	private char sndMaterialTypeCode = 'u';
	private char cuttingTypeCode = 'u';
	private char specialPlaybackCharacteristicsCode = 'u';
	private char storageTechniqueCode = 'u';
	private char obsolete1;

	public SoundRecording() {
		super();
		setHeaderType((short) 29);
		setGeneralMaterialDesignationCode('s');
		setSpecificMaterialDesignationCode('u');
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
				+ getSpeedCode()
				+ getConfigurationCode()
				+ getGrooveWidthCode()
				+ getDimensionsCode()
				+ getTapeWidthCode()
				+ getTapeConfigurationCode()
				+ getDiscTypeCode()
				+ getSndMaterialTypeCode()
				+ getCuttingTypeCode()
				+ getSpecialPlaybackCharacteristicsCode()
				+ getStorageTechniqueCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException {
		DAOSystemNextNumber dao = new DAOSystemNextNumber();
		setKeyNumber(dao.getNextNumber("X8"));
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isSoundRecording()
	 */
	public boolean isSoundRecording() {
		return true;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getConfigurationCode() {
		return configurationCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getCuttingTypeCode() {
		return cuttingTypeCode;
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
	public char getDiscTypeCode() {
		return discTypeCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getGrooveWidthCode() {
		return grooveWidthCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getSndMaterialTypeCode() {
		return sndMaterialTypeCode;
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
	public char getSpecialPlaybackCharacteristicsCode() {
		return specialPlaybackCharacteristicsCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getSpeedCode() {
		return speedCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getStorageTechniqueCode() {
		return storageTechniqueCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getTapeConfigurationCode() {
		return tapeConfigurationCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getTapeWidthCode() {
		return tapeWidthCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setConfigurationCode(char c) {
		configurationCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setCuttingTypeCode(char c) {
		cuttingTypeCode = c;
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
	public void setDiscTypeCode(char c) {
		discTypeCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setGrooveWidthCode(char c) {
		grooveWidthCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setSndMaterialTypeCode(char c) {
		sndMaterialTypeCode = c;
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
	public void setSpecialPlaybackCharacteristicsCode(char c) {
		specialPlaybackCharacteristicsCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setSpeedCode(char c) {
		speedCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setStorageTechniqueCode(char c) {
		storageTechniqueCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setTapeConfigurationCode(char c) {
		tapeConfigurationCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setTapeWidthCode(char c) {
		tapeWidthCode = c;
	}

	public Element generateModelXmlElementContent(Document xmlDocument) {
		Element content = null;
		if (xmlDocument != null) {
			content = xmlDocument.createElement("content");
			content.setAttribute("generalMaterialDesignationCode", "" + getGeneralMaterialDesignationCode());
			content.setAttribute("specificMaterialDesignationCode", "" + getSpecificMaterialDesignationCode());
			content.setAttribute("speedCode", "" + getSpeedCode());
			content.setAttribute("configurationCode", "" + getConfigurationCode());
			content.setAttribute("grooveWidthCode", "" + getGrooveWidthCode());
			content.setAttribute("dimensionsCode", "" + getDimensionsCode());
			content.setAttribute("tapeWidthCode", "" + getTapeWidthCode());
			content.setAttribute("tapeConfigurationCode", "" + getTapeConfigurationCode());
			content.setAttribute("discTypeCode", "" + getDiscTypeCode());
			content.setAttribute("sndMaterialTypeCode", "" + getSndMaterialTypeCode());
			content.setAttribute("cuttingTypeCode", "" + getCuttingTypeCode());
			content.setAttribute("specialPlaybackCharacteristicsCode", "" + getSpecialPlaybackCharacteristicsCode());
			content.setAttribute("storageTechniqueCode", "" + getStorageTechniqueCode());
		}
		return content;
	}

	public void parseModelXmlElementContent(Element xmlElement) {
		Element content = (Element) xmlElement.getChildNodes().item(0);
		setGeneralMaterialDesignationCode(content.getAttribute("generalMaterialDesignationCode").charAt(0));
		setSpecificMaterialDesignationCode(content.getAttribute("specificMaterialDesignationCode").charAt(0));
		setSpeedCode(content.getAttribute("speedCode").charAt(0));
		setConfigurationCode(content.getAttribute("configurationCode").charAt(0));
		setGrooveWidthCode(content.getAttribute("grooveWidthCode").charAt(0));
		setDimensionsCode(content.getAttribute("dimensionsCode").charAt(0));
		setTapeWidthCode(content.getAttribute("tapeWidthCode").charAt(0));
		setTapeConfigurationCode(content.getAttribute("tapeConfigurationCode").charAt(0));
		setDiscTypeCode(content.getAttribute("discTypeCode").charAt(0));
		setSndMaterialTypeCode(content.getAttribute("sndMaterialTypeCode").charAt(0));
		setCuttingTypeCode(content.getAttribute("cuttingTypeCode").charAt(0));
		setSpecialPlaybackCharacteristicsCode(content.getAttribute("specialPlaybackCharacteristicsCode").charAt(0));
		setStorageTechniqueCode(content.getAttribute("storageTechniqueCode").charAt(0));
	}

}
