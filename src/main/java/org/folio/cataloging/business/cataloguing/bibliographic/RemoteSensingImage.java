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
public class RemoteSensingImage extends PhysicalDescription {
	private char altitudeOfSensorCode = 'u';
	private char attitudeOfSensorCode = 'u';
	private char cloudCoverCode = 'u';
	private char platformConstructionTypeCode = 'u';
	private char platformUseCode = 'u';
	private char sensorTypeCode = 'u';
	private String dataTypeCode = "uu";

	public RemoteSensingImage() {
		super();
		setHeaderType((short) 43);
		setGeneralMaterialDesignationCode('r');
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
		+ getAltitudeOfSensorCode()
		+ getAttitudeOfSensorCode()
		+ getCloudCoverCode()
		+ getPlatformConstructionTypeCode()
		+ getPlatformUseCode()
		+ getSensorTypeCode()
		+ getDataTypeCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException {
		DAOSystemNextNumber dao = new DAOSystemNextNumber();
		setKeyNumber(dao.getNextNumber("XB"));
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isRemoteSensingImage()
	 */
	public boolean isRemoteSensingImage() {
		return true;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getAltitudeOfSensorCode() {
		return altitudeOfSensorCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getAttitudeOfSensorCode() {
		return attitudeOfSensorCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getCloudCoverCode() {
		return cloudCoverCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getDataTypeCode() {
		return dataTypeCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getPlatformConstructionTypeCode() {
		return platformConstructionTypeCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getPlatformUseCode() {
		return platformUseCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getSensorTypeCode() {
		return sensorTypeCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setAltitudeOfSensorCode(char c) {
		altitudeOfSensorCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setAttitudeOfSensorCode(char c) {
		attitudeOfSensorCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setCloudCoverCode(char c) {
		cloudCoverCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setDataTypeCode(String string) {
		dataTypeCode = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setPlatformConstructionTypeCode(char c) {
		platformConstructionTypeCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setPlatformUseCode(char c) {
		platformUseCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setSensorTypeCode(char c) {
		sensorTypeCode = c;
	}

	public Element generateModelXmlElementContent(Document xmlDocument) {
		Element content = null;
		if (xmlDocument != null) {
			content = xmlDocument.createElement("content");
			content.setAttribute("generalMaterialDesignationCode", "" + getGeneralMaterialDesignationCode());
			content.setAttribute("specificMaterialDesignationCode", "" + getSpecificMaterialDesignationCode());
			content.setAttribute("altitudeOfSensorCode", "" + getAltitudeOfSensorCode());
			content.setAttribute("attitudeOfSensorCode", "" + getAttitudeOfSensorCode());
			content.setAttribute("cloudCoverCode", "" + getCloudCoverCode());
			content.setAttribute("platformConstructionTypeCode", "" + getPlatformConstructionTypeCode());
			content.setAttribute("platformUseCode", "" + getPlatformUseCode());
			content.setAttribute("sensorTypeCode", "" + getSensorTypeCode());
			content.setAttribute("dataTypeCode", "" + getDataTypeCode());
		}
		return content;
	}

	public void parseModelXmlElementContent(Element xmlElement) {
		Element content = (Element) xmlElement.getChildNodes().item(0);
		setGeneralMaterialDesignationCode(content.getAttribute("generalMaterialDesignationCode").charAt(0));
		setSpecificMaterialDesignationCode(content.getAttribute("specificMaterialDesignationCode").charAt(0));
		setAltitudeOfSensorCode(content.getAttribute("altitudeOfSensorCode").charAt(0));
		setAttitudeOfSensorCode(content.getAttribute("attitudeOfSensorCode").charAt(0));
		setCloudCoverCode(content.getAttribute("cloudCoverCode").charAt(0));
		setPlatformConstructionTypeCode(content.getAttribute("platformConstructionTypeCode").charAt(0));
		setPlatformUseCode(content.getAttribute("platformUseCode").charAt(0));
		setSensorTypeCode(content.getAttribute("sensorTypeCode").charAt(0));
		setDataTypeCode(content.getAttribute("dataTypeCode"));
	}

}
