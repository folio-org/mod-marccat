package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.exception.DataAccessException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class Globe extends PhysicalDescription {
  private char colourCode;
  private char physicalMediumCode;
  private char typeOfReproductionCode;
  private Character obsolete1;
  private Character obsolete2;

  public Globe() {
    super();
    setHeaderType(23);
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
        + getPhysicalMediumCode()
        + getTypeOfReproductionCode();
    return result;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
   */
  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setKeyNumber(dao.getNextNumber("X1", session));
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isGlobe()
   */
  public boolean isGlobe() {
    return true;
  }


  public char getColourCode() {
    return colourCode;
  }


  public void setColourCode(char c) {
    colourCode = c;
  }


  public Character getObsolete1() {
    return obsolete1;
  }


  public void setObsolete1(Character character) {
    obsolete1 = character;
  }


  public Character getObsolete2() {
    return obsolete2;
  }


  public void setObsolete2(Character character) {
    obsolete2 = character;
  }


  public char getPhysicalMediumCode() {
    return physicalMediumCode;
  }


  public void setPhysicalMediumCode(char c) {
    physicalMediumCode = c;
  }


  public char getTypeOfReproductionCode() {
    return typeOfReproductionCode;
  }


  public void setTypeOfReproductionCode(char c) {
    typeOfReproductionCode = c;
  }

  public Element generateModelXmlElementContent(Document xmlDocument) {
    Element content = null;
    if (xmlDocument != null) {
      content = xmlDocument.createElement("content");
      content.setAttribute("generalMaterialDesignationCode", "" + getGeneralMaterialDesignationCode());
      content.setAttribute("specificMaterialDesignationCode", "" + getSpecificMaterialDesignationCode());
      content.setAttribute("colourCode", "" + getColourCode());
      content.setAttribute("physicalMediumCode", "" + getPhysicalMediumCode());
      content.setAttribute("typeOfReproductionCode", "" + getTypeOfReproductionCode());
    }
    return content;
  }

  public void parseModelXmlElementContent(Element xmlElement) {
    Element content = (Element) xmlElement.getChildNodes().item(0);
    setGeneralMaterialDesignationCode(content.getAttribute("generalMaterialDesignationCode").charAt(0));
    setSpecificMaterialDesignationCode(content.getAttribute("specificMaterialDesignationCode").charAt(0));
    setColourCode(content.getAttribute("colourCode").charAt(0));
    setPhysicalMediumCode(content.getAttribute("physicalMediumCode").charAt(0));
    setTypeOfReproductionCode(content.getAttribute("typeOfReproductionCode").charAt(0));
  }

  //@paulm, us_bbl_loading
  @Override
  public void setContentFromMarcString(final String s) {
    setGeneralMaterialDesignationCode(s.charAt(0));
    if (s.length() > 1) setSpecificMaterialDesignationCode(s.charAt(1));
    else setSpecificMaterialDesignationCode('u');
    if (s.length() > 3) setColourCode(s.charAt(3));
    if (s.length() > 4) setPhysicalMediumCode(s.charAt(4));
    if (s.length() > 5) setTypeOfReproductionCode(s.charAt(5));
  }
}
