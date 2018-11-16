package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class NonProjectedGraphic extends PhysicalDescription {
  private char colourCode;
  private char primarySupportMaterialCode;
  private char secondarySupportMaterialCode;
  private char obsolete1;

  public NonProjectedGraphic() {
    super();
    setHeaderType(27);
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
        + getPrimarySupportMaterialCode()
        + getSecondarySupportMaterialCode();
    return result;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
   */
  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setKeyNumber(dao.getNextNumber("X6", session));
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isNonProjectedGraphic()
   */
  public boolean isNonProjectedGraphic() {
    return true;
  }

  /**
   * @since 1.0
   */
  public char getColourCode() {
    return colourCode;
  }

  /**
   * @since 1.0
   */
  public void setColourCode(char c) {
    colourCode = c;
  }

  /**
   * @since 1.0
   */
  public char getPrimarySupportMaterialCode() {
    return primarySupportMaterialCode;
  }

  /**
   * @since 1.0
   */
  public void setPrimarySupportMaterialCode(char c) {
    primarySupportMaterialCode = c;
  }

  /**
   * @since 1.0
   */
  public char getSecondarySupportMaterialCode() {
    return secondarySupportMaterialCode;
  }

  /**
   * @since 1.0
   */
  public void setSecondarySupportMaterialCode(char c) {
    secondarySupportMaterialCode = c;
  }

  /**
   * @since 1.0
   */
  public char getObsolete1() {
    return obsolete1;
  }

  /**
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
      content.setAttribute("primarySupportMaterialCode", "" + getPrimarySupportMaterialCode());
      content.setAttribute("secondarySupportMaterialCode", "" + getSecondarySupportMaterialCode());
    }
    return content;
  }

  public void parseModelXmlElementContent(Element xmlElement) {
    Element content = (Element) xmlElement.getChildNodes().item(0);
    setGeneralMaterialDesignationCode(content.getAttribute("generalMaterialDesignationCode").charAt(0));
    setSpecificMaterialDesignationCode(content.getAttribute("specificMaterialDesignationCode").charAt(0));
    setColourCode(content.getAttribute("colourCode").charAt(0));
    setPrimarySupportMaterialCode(content.getAttribute("primarySupportMaterialCode").charAt(0));
    setSecondarySupportMaterialCode(content.getAttribute("secondarySupportMaterialCode").charAt(0));
  }

  //@paulm, us_bbl_loading
  @Override
  public void setContentFromMarcString(final String s) {
    setGeneralMaterialDesignationCode(s.charAt(0));
    if (s.length() > 1) setSpecificMaterialDesignationCode(s.charAt(1));
    else setSpecificMaterialDesignationCode('u');
    if (s.length() > 3) setColourCode(s.charAt(3));
    if (s.length() > 4) setPrimarySupportMaterialCode(s.charAt(4));
    if (s.length() > 5) setSecondarySupportMaterialCode(s.charAt(5));
  }

}
