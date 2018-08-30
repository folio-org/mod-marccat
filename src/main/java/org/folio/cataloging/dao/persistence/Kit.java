package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.SystemNextNumberDAO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class Kit extends PhysicalDescription {

	public Kit() {
		super();
		setHeaderType(47);
		//setGeneralMaterialDesignationCode('o');
		//setSpecificMaterialDesignationCode('u');
	}

	/* (non-Javadoc)
	 * @see FixedField#getDisplayString()
	 */
	public String getDisplayString() {
		String result =
			""
				+ getGeneralMaterialDesignationCode()
				+ getSpecificMaterialDesignationCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException {
		SystemNextNumberDAO dao = new SystemNextNumberDAO();
		setKeyNumber(dao.getNextNumber("XF"));
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isKit()
	 */
	public boolean isKit() {
		return true;
	}

	public Element generateModelXmlElementContent(Document xmlDocument) {
		Element content = null;
		if (xmlDocument != null) {
			content = xmlDocument.createElement("content");
			content.setAttribute("generalMaterialDesignationCode", "" + getGeneralMaterialDesignationCode());
			content.setAttribute("specificMaterialDesignationCode", "" + getSpecificMaterialDesignationCode());
		}
		return content;
	}

	public void parseModelXmlElementContent(Element xmlElement) {
		Element content = (Element) xmlElement.getChildNodes().item(0);
		setGeneralMaterialDesignationCode(content.getAttribute("generalMaterialDesignationCode").charAt(0));
		setSpecificMaterialDesignationCode(content.getAttribute("specificMaterialDesignationCode").charAt(0));
	}

}
