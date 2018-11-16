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
public class Map extends PhysicalDescription {
  private char mapColourCode;
  private char mapPhysicalMediumCode;
  private char mapTypeOfReproductionCode;
  private char mapProductionDetailsCode;
  private char mapPolarityCode;
  private char obsolete1;

  public Map() {
    super();
    setHeaderType(24);
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
        + getMapColourCode()
        + getMapPhysicalMediumCode()
        + getMapTypeOfReproductionCode()
        + getMapProductionDetailsCode()
        + getMapPolarityCode();
    return result;
  }

  /**
   * @since 1.0
   */
  public char getMapColourCode() {
    return mapColourCode;
  }

  /**
   * @since 1.0
   */
  public void setMapColourCode(char c) {
    mapColourCode = c;
  }

  /**
   * @since 1.0
   */
  public char getMapPhysicalMediumCode() {
    return mapPhysicalMediumCode;
  }

  /**
   * @since 1.0
   */
  public void setMapPhysicalMediumCode(char c) {
    mapPhysicalMediumCode = c;
  }

  /**
   * @since 1.0
   */
  public char getMapPolarityCode() {
    return mapPolarityCode;
  }

  /**
   * @since 1.0
   */
  public void setMapPolarityCode(char c) {
    mapPolarityCode = c;
  }

  /**
   * @since 1.0
   */
  public char getMapProductionDetailsCode() {
    return mapProductionDetailsCode;
  }

  /**
   * @since 1.0
   */
  public void setMapProductionDetailsCode(char c) {
    mapProductionDetailsCode = c;
  }

  /**
   * @since 1.0
   */
  public char getMapTypeOfReproductionCode() {
    return mapTypeOfReproductionCode;
  }

  /**
   * @since 1.0
   */
  public void setMapTypeOfReproductionCode(char c) {
    mapTypeOfReproductionCode = c;
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

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isMap()
   */
  public boolean isMap() {
    return true;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
   */
  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setKeyNumber(dao.getNextNumber("X2", session));
  }

  public Element generateModelXmlElementContent(Document xmlDocument) {
    Element content = null;
    if (xmlDocument != null) {
      content = xmlDocument.createElement("content");
      content.setAttribute("generalMaterialDesignationCode", "" + getGeneralMaterialDesignationCode());
      content.setAttribute("specificMaterialDesignationCode", "" + getSpecificMaterialDesignationCode());
      content.setAttribute("mapColourCode", "" + getMapColourCode());
      content.setAttribute("mapPhysicalMediumCode", "" + getMapPhysicalMediumCode());
      content.setAttribute("mapTypeOfReproductionCode", "" + getMapTypeOfReproductionCode());
      content.setAttribute("mapProductionDetailsCode", "" + getMapProductionDetailsCode());
      content.setAttribute("mapPolarityCode", "" + getMapPolarityCode());
    }
    return content;
  }

  public void parseModelXmlElementContent(Element xmlElement) {
    Element content = (Element) xmlElement.getChildNodes().item(0);
    setGeneralMaterialDesignationCode(content.getAttribute("generalMaterialDesignationCode").charAt(0));
    setSpecificMaterialDesignationCode(content.getAttribute("specificMaterialDesignationCode").charAt(0));
    setMapColourCode(content.getAttribute("mapColourCode").charAt(0));
    setMapPhysicalMediumCode(content.getAttribute("mapPhysicalMediumCode").charAt(0));
    setMapTypeOfReproductionCode(content.getAttribute("mapTypeOfReproductionCode").charAt(0));
    setMapProductionDetailsCode(content.getAttribute("mapProductionDetailsCode").charAt(0));
    setMapPolarityCode(content.getAttribute("mapPolarityCode").charAt(0));
  }

  //@paulm, us_bbl_loading
  @Override
  public void setContentFromMarcString(final String s) {
    setGeneralMaterialDesignationCode(s.charAt(0));
    if (s.length() > 1) setSpecificMaterialDesignationCode(s.charAt(1));
    else setSpecificMaterialDesignationCode('u');
    if (s.length() > 3) setMapColourCode(s.charAt(3));
    if (s.length() > 4) setMapPhysicalMediumCode(s.charAt(4));
    if (s.length() > 5) setMapTypeOfReproductionCode(s.charAt(5));
    if (s.length() > 6) setMapProductionDetailsCode(s.charAt(6));
    if (s.length() > 7) setMapPolarityCode(s.charAt(7));
  }
}
