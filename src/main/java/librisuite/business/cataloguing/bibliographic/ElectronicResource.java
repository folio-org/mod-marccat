/*
 * (c) LibriCore
 * 
 * Created on Oct 15, 2004
 * 
 * Map.java
 */
package librisuite.business.cataloguing.bibliographic;

import librisuite.bean.cataloguing.bibliographic.PhysicalDescription;
import librisuite.business.common.DAOSystemNextNumber;
import librisuite.business.common.DataAccessException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public class ElectronicResource extends PhysicalDescription {
	private char colourCode = 'u';
	private char dimensionsCode = 'u';
	private char includesSoundCode = 'u';
	private String imageBitDepth = "---";
	private char fileFormatsCode = 'u';
	private char qualityAssuranceTargetCode = 'u';
	private char antecedentSourceCode = 'u';
	private char levelOfCompressionCode = 'u';
	private char reformattingQualityCode = 'u';
	private char obsolete1;

	public ElectronicResource() {
		super();
		setHeaderType((short) 42);
		setGeneralMaterialDesignationCode('c');
		setSpecificMaterialDesignationCode('u');
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.FixedField#getDisplayString()
	 */
	public String getDisplayString() {
		String result =
			""
				+ getGeneralMaterialDesignationCode()
				+ getSpecificMaterialDesignationCode()
				+ " "
				+ getColourCode()
				+ getDimensionsCode()
				+ getIncludesSoundCode()
		+ getImageBitDepth()
		+ getFileFormatsCode()
		+ getQualityAssuranceTargetCode()
		+ getAntecedentSourceCode()
		+ getLevelOfCompressionCode()
		+ getReformattingQualityCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException {
		DAOSystemNextNumber dao = new DAOSystemNextNumber();
		setKeyNumber(dao.getNextNumber("XA"));
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isElectronicResource()
	 */
	public boolean isElectronicResource() {
		return true;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getAntecedentSourceCode() {
		return antecedentSourceCode;
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
	public char getFileFormatsCode() {
		return fileFormatsCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getImageBitDepth() {
		return imageBitDepth;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getIncludesSoundCode() {
		return includesSoundCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getLevelOfCompressionCode() {
		return levelOfCompressionCode;
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
	public char getQualityAssuranceTargetCode() {
		return qualityAssuranceTargetCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getReformattingQualityCode() {
		return reformattingQualityCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setAntecedentSourceCode(char c) {
		antecedentSourceCode = c;
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
	public void setFileFormatsCode(char c) {
		fileFormatsCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setImageBitDepth(String string) {
		imageBitDepth = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setIncludesSoundCode(char c) {
		includesSoundCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setLevelOfCompressionCode(char c) {
		levelOfCompressionCode = c;
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
	public void setQualityAssuranceTargetCode(char c) {
		qualityAssuranceTargetCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setReformattingQualityCode(char c) {
		reformattingQualityCode = c;
	}

	public Element generateModelXmlElementContent(Document xmlDocument) {
		Element content = null;
		if (xmlDocument != null) {
			content = xmlDocument.createElement("content");
			content.setAttribute("generalMaterialDesignationCode", "" + getGeneralMaterialDesignationCode());
			content.setAttribute("specificMaterialDesignationCode", "" + getSpecificMaterialDesignationCode());
			content.setAttribute("colourCode", "" + getColourCode());
			content.setAttribute("dimensionsCode", "" + getDimensionsCode());
			content.setAttribute("includesSoundCode", "" + getIncludesSoundCode());
			content.setAttribute("imageBitDepth", getImageBitDepth());
			content.setAttribute("fileFormatsCode", "" + getFileFormatsCode());
			content.setAttribute("qualityAssuranceTargetCode", "" + getQualityAssuranceTargetCode());
			content.setAttribute("antecedentSourceCode", "" + getAntecedentSourceCode());
			content.setAttribute("levelOfCompressionCode", "" + getLevelOfCompressionCode());
			content.setAttribute("reformattingQualityCode", "" + getReformattingQualityCode());
		}
		return content;
	}

	public void parseModelXmlElementContent(Element xmlElement) {
		Element content = (Element) xmlElement.getChildNodes().item(0);
		setGeneralMaterialDesignationCode(content.getAttribute("generalMaterialDesignationCode").charAt(0));
		setSpecificMaterialDesignationCode(content.getAttribute("specificMaterialDesignationCode").charAt(0));
		setColourCode(content.getAttribute("colourCode").charAt(0));
		setDimensionsCode(content.getAttribute("dimensionsCode").charAt(0));
		setIncludesSoundCode(content.getAttribute("includesSoundCode").charAt(0));
		setImageBitDepth(content.getAttribute("imageBitDepth"));
		setFileFormatsCode(content.getAttribute("fileFormatsCode").charAt(0));
		setQualityAssuranceTargetCode(content.getAttribute("qualityAssuranceTargetCode").charAt(0));
		setAntecedentSourceCode(content.getAttribute("antecedentSourceCode").charAt(0));
		setLevelOfCompressionCode(content.getAttribute("levelOfCompressionCode").charAt(0));
		setReformattingQualityCode(content.getAttribute("reformattingQualityCode").charAt(0));
	}

}
