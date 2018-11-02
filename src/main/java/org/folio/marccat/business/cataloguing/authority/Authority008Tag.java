/*
 * (c) LibriCore
 *
 * Created on Dec 1, 2005
 *
 * Authority008Tag.java
 */
package org.folio.marccat.business.cataloguing.authority;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.cataloguing.bibliographic.FixedFieldUsingItemEntity;
import org.folio.marccat.dao.persistence.AUT;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class Authority008Tag extends FixedFieldUsingItemEntity {
  private static Log logger = LogFactory.getLog(Authority008Tag.class);

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public Authority008Tag() {
    super();
    setHeaderField(new AuthorityHeaderFieldHelper());
    setHeaderType(10);
  }

  /* (non-Javadoc)
   * @see FixedField#getDisplayString()
   */
  public String getDisplayString() {
    String str = "";
    str =
      str
        + getEnteredOnFileDateYYMMDD()
        + getSubjectDescriptor()
        + getRomanizationScheme()
        + getBilingualUsage()
        + getRecordType()
        + getCataloguingRules()
        + getSubjectSystem()
        + getSeriesType()
        + getSeriesNumbering()
        + getMainAddedEntryIndicator()
        + getSubjectEntryIndicator()
        + getSeriesEntryIndicator()
        + getSubDivisionType()
        + "          "
        + getGovernmentAgency()
        + getReferenceStatus()
        + " "
        + getRecordRevision()
        + getNonUniqueName()
        + getHeadingStatus()
        + "    "
        + getRecordModification()
        + getCataloguingSourceCode();

    logger.debug("displayString: " + str);
    return str;
  }

  /* (non-Javadoc)
   * @see TagInterface#parseModelXmlElementContent(org.w3c.dom.Element)
   */
  public void parseModelXmlElementContent(Element xmlElement) {
    Element content = (Element) xmlElement.getChildNodes().item(0);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    Date date =
      formatter.parse(
        content.getAttribute("enteredOnFileDateYYYYMMDD"),
        new ParsePosition(0));
    setEnteredOnFileDate(date);
    setSubjectDescriptor(
      content.getAttribute("subjectDescriptor").charAt(0));
    setRomanizationScheme(
      content.getAttribute("romanizationScheme").charAt(0));
    setBilingualUsage(content.getAttribute("bilingualUsage").charAt(0));
    setRecordType(content.getAttribute("recordType").charAt(0));
    setCataloguingRules(content.getAttribute("cataloguingRules").charAt(0));
    setSubjectSystem(content.getAttribute("subjectSystem").charAt(0));
    setSeriesType(content.getAttribute("seriesType").charAt(0));
    setSeriesNumbering(content.getAttribute("seriesNumbering").charAt(0));
    setMainAddedEntryIndicator(
      content.getAttribute("mainAddedEntryIndicator").charAt(0));
    setSeriesEntryIndicator(
      content.getAttribute("seriesEntryIndicator").charAt(0));
    setSubDivisionType(content.getAttribute("subDivisionType").charAt(0));
    setGovernmentAgency(content.getAttribute("governmentAgency").charAt(0));
    setReferenceStatus(content.getAttribute("referenceStatus").charAt(0));
    setRecordRevision(content.getAttribute("recordRevision").charAt(0));
    setNonUniqueName(content.getAttribute("nonUniqueName").charAt(0));
    setHeadingStatus(content.getAttribute("headingStatus").charAt(0));
    setRecordModification(
      content.getAttribute("recordModification").charAt(0));
    setCataloguingSourceCode(
      content.getAttribute("cataloguingSourceCode").charAt(0));
  }

  /* (non-Javadoc)
   * @see TagInterface#generateModelXmlElementContent(org.w3c.dom.Document)
   */
  public Element generateModelXmlElementContent(Document xmlDocument) {
    Element content = null;
    if (xmlDocument != null) {
      content = xmlDocument.createElement("content");
      content.setAttribute(
        "enteredOnFileDateYYYYMMDD",
        getEnteredOnFileDateYYYYMMDD());
      content.setAttribute(
        "subjectDescriptor",
        "" + getSubjectDescriptor());

      content.setAttribute(
        "romanizationScheme",
        "" + getRomanizationScheme());
      content.setAttribute("bilingualUsage", "" + getBilingualUsage());
      content.setAttribute("recordType", "" + getRecordType());
      content.setAttribute(
        "cataloguingRules",
        "" + getCataloguingRules());
      content.setAttribute("subjectSystem", "" + getSubjectSystem());
      content.setAttribute("seriesType", "" + getSeriesType());
      content.setAttribute("seriesNumbering", "" + getSeriesNumbering());
      content.setAttribute(
        "mainAddedEntryIndicator",
        "" + getMainAddedEntryIndicator());
      content.setAttribute(
        "seriesEntryIndicator",
        "" + getSeriesEntryIndicator());
      content.setAttribute("subDivisionType", "" + getSubDivisionType());
      content.setAttribute(
        "governmentAgency",
        "" + getGovernmentAgency());
      content.setAttribute("referenceStatus", "" + getReferenceStatus());
      content.setAttribute("recordRevision", "" + getRecordRevision());
      content.setAttribute("nonUniqueName", "" + getNonUniqueName());
      content.setAttribute("headingStatus", "" + getHeadingStatus());
      content.setAttribute(
        "recordModification",
        "" + getRecordModification());
      content.setAttribute(
        "cataloguingSourceCode",
        "" + getCataloguingSourceCode());
    }
    return content;
  }

  /**
   * @since 1.0
   */
  public char getCataloguingRules() {
    return getAutItm().getCataloguingRules();
  }

  /**
   * @since 1.0
   */
  public void setCataloguingRules(char c) {
    getAutItm().setCataloguingRules(c);
  }

  /**
   * @since 1.0
   */
  public char getCataloguingSourceCode() {
    return getAutItm().getCataloguingSourceCode();
  }

  /**
   * @since 1.0
   */
  public void setCataloguingSourceCode(char c) {
    getAutItm().setCataloguingSourceCode(c);
  }

  /**
   * @since 1.0
   */
  public Date getEnteredOnFileDate() {
    return getAutItm().getEnteredOnFileDate();
  }

  /**
   * @since 1.0
   */
  public void setEnteredOnFileDate(Date date) {
    getAutItm().setEnteredOnFileDate(date);
  }

  /**
   * @since 1.0
   */
  public String getEnteredOnFileDateYYMMDD() {
    return getAutItm().getEnteredOnFileDateYYMMDD();
  }

  /**
   * @since 1.0
   */
  public String getEnteredOnFileDateYYYYMMDD() {
    return getAutItm().getEnteredOnFileDateYYYYMMDD();
  }

  /**
   * @since 1.0
   */
  public char getGovernmentAgency() {
    return getAutItm().getGovernmentAgency();
  }

  /**
   * @since 1.0
   */
  public void setGovernmentAgency(char c) {
    getAutItm().setGovernmentAgency(c);
  }

  /**
   * @since 1.0
   */
  public char getHeadingStatus() {
    return getAutItm().getHeadingStatus();
  }

  /**
   * @since 1.0
   */
  public void setHeadingStatus(char c) {
    getAutItm().setHeadingStatus(c);
  }

  /**
   * @since 1.0
   */
  public String getLanguageOfCataloguing() {
    return getAutItm().getLanguageOfCataloguing();
  }

  /**
   * @since 1.0
   */
  public char getMainAddedEntryIndicator() {
    return getAutItm().getMainAddedEntryIndicator();
  }

  /**
   * @since 1.0
   */
  public void setMainAddedEntryIndicator(char c) {
    getAutItm().setMainAddedEntryIndicator(c);
  }

  /**
   * @since 1.0
   */
  public char getNonUniqueName() {
    return getAutItm().getNonUniqueName();
  }

  /**
   * @since 1.0
   */
  public void setNonUniqueName(char c) {
    getAutItm().setNonUniqueName(c);
  }

  /**
   * @since 1.0
   */
  public char getRecordModification() {
    return getAutItm().getRecordModification();
  }

  /**
   * @since 1.0
   */
  public void setRecordModification(char c) {
    getAutItm().setRecordModification(c);
  }

  /**
   * @since 1.0
   */
  public char getRecordRevision() {
    return getAutItm().getRecordRevision();
  }

  /**
   * @since 1.0
   */
  public void setRecordRevision(char c) {
    getAutItm().setRecordRevision(c);
  }

  /**
   * @since 1.0
   */
  public char getRecordStatusCode() {
    return getAutItm().getRecordStatusCode();
  }

  /**
   * @since 1.0
   */
  public void setRecordStatusCode(char c) {
    getAutItm().setRecordStatusCode(c);
  }

  /**
   * @since 1.0
   */
  public char getRecordType() {
    return getAutItm().getRecordType();
  }

  /**
   * @since 1.0
   */
  public void setRecordType(char c) {
    getAutItm().setRecordType(c);
  }

  /**
   * @since 1.0
   */
  public char getReferenceStatus() {
    return getAutItm().getReferenceStatus();
  }

  /**
   * @since 1.0
   */
  public void setReferenceStatus(char c) {
    getAutItm().setReferenceStatus(c);
  }

  /**
   * @since 1.0
   */
  public char getRomanizationScheme() {
    return getAutItm().getRomanizationScheme();
  }

  /**
   * @since 1.0
   */
  public void setRomanizationScheme(char c) {
    getAutItm().setRomanizationScheme(c);
  }

  /**
   * @since 1.0
   */
  public char getSeriesEntryIndicator() {
    return getAutItm().getSeriesEntryIndicator();
  }

  /**
   * @since 1.0
   */
  public void setSeriesEntryIndicator(char c) {
    getAutItm().setSeriesEntryIndicator(c);
  }

  /**
   * @since 1.0
   */
  public char getSeriesNumbering() {
    return getAutItm().getSeriesNumbering();
  }

  /**
   * @since 1.0
   */
  public void setSeriesNumbering(char c) {
    getAutItm().setSeriesNumbering(c);
  }

  /**
   * @since 1.0
   */
  public char getSeriesType() {
    return getAutItm().getSeriesType();
  }

  /**
   * @since 1.0
   */
  public void setSeriesType(char c) {
    getAutItm().setSeriesType(c);
  }

  /**
   * @since 1.0
   */
  public char getSubDivisionType() {
    return getAutItm().getSubDivisionType();
  }

  /**
   * @since 1.0
   */
  public void setSubDivisionType(char c) {
    getAutItm().setSubDivisionType(c);
  }

  /**
   * @since 1.0
   */
  public char getSubjectEntryIndicator() {
    return getAutItm().getSubjectEntryIndicator();
  }

  /**
   * @since 1.0
   */
  public void setSubjectEntryIndicator(char c) {
    getAutItm().setSubjectEntryIndicator(c);
  }

  /**
   * @since 1.0
   */
  public char getSubjectSystem() {
    return getAutItm().getSubjectSystem();
  }

  /**
   * @since 1.0
   */
  public void setSubjectSystem(char c) {
    getAutItm().setSubjectSystem(c);
  }

  /**
   * @since 1.0
   */
  public void setEncodingLevel(char c) {
    getAutItm().setEncodingLevel(c);
  }

  /**
   * @since 1.0
   */
  public char getSubjectDescriptor() {
    return getAutItm().getSubjectDescriptor();
  }

  /**
   * @since 1.0
   */
  public void setSubjectDescriptor(char c) {
    getAutItm().setSubjectDescriptor(c);
  }

  private AUT getAutItm() {
    return (AUT) getItemEntity();
  }

  /**
   * @since 1.0
   */
  public char getBilingualUsage() {
    return getAutItm().getBilingualUsage();
  }

  /**
   * @since 1.0
   */
  public void setBilingualUsage(char c) {
    getAutItm().setBilingualUsage(c);
  }

}
