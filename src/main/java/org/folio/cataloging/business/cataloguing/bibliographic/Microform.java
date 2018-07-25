/*
 * (c) LibriCore
 * 
 * Created on Oct 15, 2004
 * 
 * Map.java
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
public class Microform extends PhysicalDescription {
	private char polarityCode = 'u';
	private char dimensionsCode = 'u';
	private char reductionRatioRangeCode = 'u';
	private String reductionRatioCode = "---";
	private char colourCode = 'u';
	private char emulsionOnFilmCode = 'u';
	private char generationCode = 'u';
	private char baseOfFilmCode = 'u';
	private char obsolete1;

	public Microform() {
		super();
		setHeaderType((short) 25);
		setGeneralMaterialDesignationCode('h');
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
				+ getPolarityCode()
				+ getDimensionsCode()
				+ getReductionRatioRangeCode()
				+ getReductionRatioCode()
				+ getColourCode()
				+ getEmulsionOnFilmCode()
				+ getGenerationCode()
				+ getBaseOfFilmCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException {
		DAOSystemNextNumber dao = new DAOSystemNextNumber();
		setKeyNumber(dao.getNextNumber("X3"));
	}
	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isMicroform()
	 */
	public boolean isMicroform() {
		return true;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getBaseOfFilmCode() {
		return baseOfFilmCode;
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
	public char getEmulsionOnFilmCode() {
		return emulsionOnFilmCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getGenerationCode() {
		return generationCode;
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
	public char getPolarityCode() {
		return polarityCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getReductionRatioCode() {
		return reductionRatioCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getReductionRatioRangeCode() {
		return reductionRatioRangeCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setBaseOfFilmCode(char c) {
		baseOfFilmCode = c;
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
	public void setEmulsionOnFilmCode(char c) {
		emulsionOnFilmCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setGenerationCode(char c) {
		generationCode = c;
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
	public void setPolarityCode(char c) {
		polarityCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setReductionRatioCode(String string) {
		reductionRatioCode = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setReductionRatioRangeCode(char c) {
		reductionRatioRangeCode = c;
	}

	public Element generateModelXmlElementContent(Document xmlDocument) {
		Element content = null;
		if (xmlDocument != null) {
			content = xmlDocument.createElement("content");
			content.setAttribute("generalMaterialDesignationCode", "" + getGeneralMaterialDesignationCode());
			content.setAttribute("specificMaterialDesignationCode", "" + getSpecificMaterialDesignationCode());
			content.setAttribute("polarityCode", "" + getPolarityCode());
			content.setAttribute("dimensionsCode", "" + getDimensionsCode());
			content.setAttribute("reductionRatioRangeCode", "" + getReductionRatioRangeCode());
			content.setAttribute("reductionRatioCode", "" + getReductionRatioCode());
			content.setAttribute("colourCode", "" + getColourCode());
			content.setAttribute("emulsionOnFilmCode", "" + getEmulsionOnFilmCode());
			content.setAttribute("generationCode", "" + getGenerationCode());
			content.setAttribute("baseOfFilmCode", "" + getBaseOfFilmCode());
		}
		return content;
	}

	public void parseModelXmlElementContent(Element xmlElement) {
		Element content = (Element) xmlElement.getChildNodes().item(0);
		setGeneralMaterialDesignationCode(content.getAttribute("generalMaterialDesignationCode").charAt(0));
		setSpecificMaterialDesignationCode(content.getAttribute("specificMaterialDesignationCode").charAt(0));
		setPolarityCode(content.getAttribute("polarityCode").charAt(0));
		setDimensionsCode(content.getAttribute("dimensionsCode").charAt(0));
		setReductionRatioRangeCode(content.getAttribute("reductionRatioRangeCode").charAt(0));
		setReductionRatioCode(content.getAttribute("reductionRatioCode"));
		setColourCode(content.getAttribute("colourCode").charAt(0));
		setEmulsionOnFilmCode(content.getAttribute("emulsionOnFilmCode").charAt(0));
		setGenerationCode(content.getAttribute("generationCode").charAt(0));
		setBaseOfFilmCode(content.getAttribute("baseOfFilmCode").charAt(0));
	}

}
