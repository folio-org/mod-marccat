package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.exception.DataAccessException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.folio.marccat.config.Global.EMPTY_STRING;

/**
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class Unspecified extends PhysicalDescription {

  public Unspecified() {
    super();
    setHeaderType(45);
  }

  /* (non-Javadoc)
   * @see FixedField#getDisplayString()
   */
  public String getDisplayString() {
    String result =
      EMPTY_STRING
        + getGeneralMaterialDesignationCode()
        + getSpecificMaterialDesignationCode();
    return result;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
   */
  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setKeyNumber(dao.getNextNumber("XD", session));
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isUnspecified()
   */
  public boolean isUnspecified() {
    return true;
  }

  public Element generateModelXmlElementContent(Document xmlDocument) {
    Element content = null;
    if (xmlDocument != null) {
      content = xmlDocument.createElement("content");
      content.setAttribute("generalMaterialDesignationCode", EMPTY_STRING + getGeneralMaterialDesignationCode());
      content.setAttribute("specificMaterialDesignationCode", EMPTY_STRING + getSpecificMaterialDesignationCode());
    }
    return content;
  }

  public void parseModelXmlElementContent(Element xmlElement) {
    Element content = (Element) xmlElement.getChildNodes().item(0);
    setGeneralMaterialDesignationCode(content.getAttribute("generalMaterialDesignationCode").charAt(0));
    setSpecificMaterialDesignationCode(content.getAttribute("specificMaterialDesignationCode").charAt(0));
  }

  //@paulm, us_bbl_loading
  @Override
  public void setContentFromMarcString(final String s) {
    setGeneralMaterialDesignationCode(s.charAt(0));
    if (s.length() > 1) setSpecificMaterialDesignationCode(s.charAt(1));
    else setSpecificMaterialDesignationCode('u');
  }
}
