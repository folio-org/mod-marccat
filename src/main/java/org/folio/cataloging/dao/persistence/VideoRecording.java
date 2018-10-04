package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.SystemNextNumberDAO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class VideoRecording extends PhysicalDescription {
	private char colourCode;
	private char formatCode;
	private char includesSoundCode;
	private char mediumForSoundCode;
	private char dimensionsCode;
	private char configurationCode;
	private char obsolete1;

	public VideoRecording() {
		super();
		setHeaderType(30);
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
				+ getFormatCode()
				+ getIncludesSoundCode()
				+ getMediumForSoundCode()
				+ getDimensionsCode()
				+ getConfigurationCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
	 */
  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
		SystemNextNumberDAO dao = new SystemNextNumberDAO();
		setKeyNumber(dao.getNextNumber("X9", session));
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isVideoRecording()
	 */
	public boolean isVideoRecording() {
		return true;
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
	public char getConfigurationCode() {
		return configurationCode;
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
	public char getFormatCode() {
		return formatCode;
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
	public void setColourCode(char c) {
		colourCode = c;
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
	public void setDimensionsCode(char c) {
		dimensionsCode = c;
	}

	/**
	 *
	 * @since 1.0
	 */
	public void setFormatCode(char c) {
		formatCode = c;
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

	public Element generateModelXmlElementContent(Document xmlDocument) {
		Element content = null;
		if (xmlDocument != null) {
			content = xmlDocument.createElement("content");
			content.setAttribute("generalMaterialDesignationCode", "" + getGeneralMaterialDesignationCode());
			content.setAttribute("specificMaterialDesignationCode", "" + getSpecificMaterialDesignationCode());
			content.setAttribute("colourCode", "" + getColourCode());
			content.setAttribute("formatCode", "" + getFormatCode());
			content.setAttribute("includesSoundCode", "" + getIncludesSoundCode());
			content.setAttribute("mediumForSoundCode", "" + getMediumForSoundCode());
			content.setAttribute("dimensionsCode", "" + getDimensionsCode());
			content.setAttribute("configurationCode", "" + getConfigurationCode());
		}
		return content;
	}

	public void parseModelXmlElementContent(Element xmlElement) {
		Element content = (Element) xmlElement.getChildNodes().item(0);
		setGeneralMaterialDesignationCode(content.getAttribute("generalMaterialDesignationCode").charAt(0));
		setSpecificMaterialDesignationCode(content.getAttribute("specificMaterialDesignationCode").charAt(0));
		setColourCode(content.getAttribute("colourCode").charAt(0));
		setFormatCode(content.getAttribute("formatCode").charAt(0));
		setIncludesSoundCode(content.getAttribute("includesSoundCode").charAt(0));
		setMediumForSoundCode(content.getAttribute("mediumForSoundCode").charAt(0));
		setDimensionsCode(content.getAttribute("dimensionsCode").charAt(0));
		setConfigurationCode(content.getAttribute("configurationCode").charAt(0));
	}

  //@paulm, us_bbl_loading
  @Override
  public void setContentFromMarcString(final String s) {
    setGeneralMaterialDesignationCode(s.charAt(0));
    setSpecificMaterialDesignationCode(s.charAt(1));
    setColourCode(s.charAt(3));
    setFormatCode(s.charAt(4));
    setIncludesSoundCode(s.charAt(5));
    setMediumForSoundCode(s.charAt(6));
    setDimensionsCode(s.charAt(7));
    setConfigurationCode(s.charAt(8));
  }
}
