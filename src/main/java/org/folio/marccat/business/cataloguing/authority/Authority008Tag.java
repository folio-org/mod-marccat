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


  public char getCataloguingRules() {
    return getAutItm().getCataloguingRules();
  }


  public void setCataloguingRules(char c) {
    getAutItm().setCataloguingRules(c);
  }


  public char getCataloguingSourceCode() {
    return getAutItm().getCataloguingSourceCode();
  }


  public void setCataloguingSourceCode(char c) {
    getAutItm().setCataloguingSourceCode(c);
  }


  public Date getEnteredOnFileDate() {
    return getAutItm().getEnteredOnFileDate();
  }


  public void setEnteredOnFileDate(Date date) {
    getAutItm().setEnteredOnFileDate(date);
  }


  public String getEnteredOnFileDateYYMMDD() {
    return getAutItm().getEnteredOnFileDateYYMMDD();
  }


  public String getEnteredOnFileDateYYYYMMDD() {
    return getAutItm().getEnteredOnFileDateYYYYMMDD();
  }


  public char getGovernmentAgency() {
    return getAutItm().getGovernmentAgency();
  }


  public void setGovernmentAgency(char c) {
    getAutItm().setGovernmentAgency(c);
  }


  public char getHeadingStatus() {
    return getAutItm().getHeadingStatus();
  }


  public void setHeadingStatus(char c) {
    getAutItm().setHeadingStatus(c);
  }


  public String getLanguageOfCataloguing() {
    return getAutItm().getLanguageOfCataloguing();
  }


  public char getMainAddedEntryIndicator() {
    return getAutItm().getMainAddedEntryIndicator();
  }


  public void setMainAddedEntryIndicator(char c) {
    getAutItm().setMainAddedEntryIndicator(c);
  }


  public char getNonUniqueName() {
    return getAutItm().getNonUniqueName();
  }


  public void setNonUniqueName(char c) {
    getAutItm().setNonUniqueName(c);
  }


  public char getRecordModification() {
    return getAutItm().getRecordModification();
  }


  public void setRecordModification(char c) {
    getAutItm().setRecordModification(c);
  }


  public char getRecordRevision() {
    return getAutItm().getRecordRevision();
  }


  public void setRecordRevision(char c) {
    getAutItm().setRecordRevision(c);
  }


  public char getRecordStatusCode() {
    return getAutItm().getRecordStatusCode();
  }


  public void setRecordStatusCode(char c) {
    getAutItm().setRecordStatusCode(c);
  }


  public char getRecordType() {
    return getAutItm().getRecordType();
  }


  public void setRecordType(char c) {
    getAutItm().setRecordType(c);
  }


  public char getReferenceStatus() {
    return getAutItm().getReferenceStatus();
  }


  public void setReferenceStatus(char c) {
    getAutItm().setReferenceStatus(c);
  }


  public char getRomanizationScheme() {
    return getAutItm().getRomanizationScheme();
  }


  public void setRomanizationScheme(char c) {
    getAutItm().setRomanizationScheme(c);
  }


  public char getSeriesEntryIndicator() {
    return getAutItm().getSeriesEntryIndicator();
  }


  public void setSeriesEntryIndicator(char c) {
    getAutItm().setSeriesEntryIndicator(c);
  }


  public char getSeriesNumbering() {
    return getAutItm().getSeriesNumbering();
  }


  public void setSeriesNumbering(char c) {
    getAutItm().setSeriesNumbering(c);
  }


  public char getSeriesType() {
    return getAutItm().getSeriesType();
  }


  public void setSeriesType(char c) {
    getAutItm().setSeriesType(c);
  }


  public char getSubDivisionType() {
    return getAutItm().getSubDivisionType();
  }


  public void setSubDivisionType(char c) {
    getAutItm().setSubDivisionType(c);
  }


  public char getSubjectEntryIndicator() {
    return getAutItm().getSubjectEntryIndicator();
  }


  public void setSubjectEntryIndicator(char c) {
    getAutItm().setSubjectEntryIndicator(c);
  }


  public char getSubjectSystem() {
    return getAutItm().getSubjectSystem();
  }


  public void setSubjectSystem(char c) {
    getAutItm().setSubjectSystem(c);
  }


  public void setEncodingLevel(char c) {
    getAutItm().setEncodingLevel(c);
  }


  public char getSubjectDescriptor() {
    return getAutItm().getSubjectDescriptor();
  }


  public void setSubjectDescriptor(char c) {
    getAutItm().setSubjectDescriptor(c);
  }

  private AUT getAutItm() {
    return (AUT) getItemEntity();
  }


  public char getBilingualUsage() {
    return getAutItm().getBilingualUsage();
  }


  public void setBilingualUsage(char c) {
    getAutItm().setBilingualUsage(c);
  }

}
