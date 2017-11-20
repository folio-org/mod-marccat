/*
 * (c) LibriCore
 * 
 * Created on Oct 15, 2004
 * 
 * Map.java
 */
package org.folio.cataloging.business.cataloguing.bibliographic;

import org.folio.cataloging.bean.cataloguing.bibliographic.PhysicalDescription;
import org.folio.cataloging.dao.DAOSystemNextNumber;
import org.folio.cataloging.business.common.DataAccessException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public class TactileMaterial extends PhysicalDescription {
	private String classOfBrailleWritingCodes = "uu";
	private char levelOfContractionCode = 'u';
	private String brailleMusicFormatCodes = "uuu";
	private char specificPhysicalCharacteristicsCode = 'u';

	public TactileMaterial() {
		super();
		setHeaderType((short) 46);
		setGeneralMaterialDesignationCode('f');
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
				+ getClassOfBrailleWritingCodes()
				+ getLevelOfContractionCode()
				+ getBrailleMusicFormatCodes()
				+ getSpecificPhysicalCharacteristicsCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException {
		DAOSystemNextNumber dao = new DAOSystemNextNumber();
		setKeyNumber(dao.getNextNumber("XE"));
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isTactileMaterial()
	 */
	public boolean isTactileMaterial() {
		return true;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getBrailleMusicFormatCodes() {
		return brailleMusicFormatCodes;
	}

	public char [] getBrailleMusicFormatChar() {
		if (brailleMusicFormatCodes == null) {
			return null;
		}
		else {
			return brailleMusicFormatCodes.toCharArray();
		}
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public String getClassOfBrailleWritingCodes() {
		return classOfBrailleWritingCodes;
	}

	public char [] getClassOfBrailleWritingChar() {
		if (classOfBrailleWritingCodes == null) {
			return null;
		}
		else {
			return classOfBrailleWritingCodes.toCharArray();
		}
	}
	

	/**
	 * 
	 * @since 1.0
	 */
	public char getLevelOfContractionCode() {
		return levelOfContractionCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getSpecificPhysicalCharacteristicsCode() {
		return specificPhysicalCharacteristicsCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setBrailleMusicFormatCodes(String string) {
		brailleMusicFormatCodes = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setClassOfBrailleWritingCodes(String string) {
		classOfBrailleWritingCodes = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setLevelOfContractionCode(char c) {
		levelOfContractionCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setSpecificPhysicalCharacteristicsCode(char c) {
		specificPhysicalCharacteristicsCode = c;
	}

	public Element generateModelXmlElementContent(Document xmlDocument) {
		Element content = null;
		if (xmlDocument != null) {
			content = xmlDocument.createElement("content");
			content.setAttribute("generalMaterialDesignationCode", "" + getGeneralMaterialDesignationCode());
			content.setAttribute("specificMaterialDesignationCode", "" + getSpecificMaterialDesignationCode());
			content.setAttribute("classOfBrailleWritingCodes", "" + getClassOfBrailleWritingCodes());
			content.setAttribute("levelOfContractionCode", "" + getLevelOfContractionCode());
			content.setAttribute("brailleMusicFormatCodes", "" + getBrailleMusicFormatCodes());
			content.setAttribute("specificPhysicalCharacteristicsCode", "" + getSpecificPhysicalCharacteristicsCode());
		}
		return content;
	}

	public void parseModelXmlElementContent(Element xmlElement) {
		Element content = (Element) xmlElement.getChildNodes().item(0);
		setGeneralMaterialDesignationCode(content.getAttribute("generalMaterialDesignationCode").charAt(0));
		setSpecificMaterialDesignationCode(content.getAttribute("specificMaterialDesignationCode").charAt(0));
		setClassOfBrailleWritingCodes(content.getAttribute("classOfBrailleWritingCodes"));
		setLevelOfContractionCode(content.getAttribute("levelOfContractionCode").charAt(0));
		setBrailleMusicFormatCodes(content.getAttribute("brailleMusicFormatCodes"));
		setSpecificPhysicalCharacteristicsCode(content.getAttribute("specificPhysicalCharacteristicsCode").charAt(0));
	}

}
