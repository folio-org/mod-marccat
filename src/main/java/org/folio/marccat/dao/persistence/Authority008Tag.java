package org.folio.marccat.dao.persistence;

import java.util.Date;

import org.folio.marccat.business.cataloguing.authority.AuthorityHeaderFieldHelper;
import org.folio.marccat.business.cataloguing.bibliographic.FixedFieldUsingItemEntity;

/**
 * @author elena
 *
 */
public class Authority008Tag extends FixedFieldUsingItemEntity {

  /**
   * 
   */
  private static final long serialVersionUID = -3552697623964822985L;

  public Authority008Tag() {
    super();
    setHeaderField(new AuthorityHeaderFieldHelper());
    setHeaderType((short) 10);
  }

  public String getDisplayString() {
    String str = "";
    str = str + getEnteredOnFileDateYYMMDD() + getSubjectDescriptor() + getRomanizationScheme() + getBilingualUsage()
        + getRecordType() + getCataloguingRules() + getSubjectSystem() + getSeriesType() + getSeriesNumbering()
        + getMainAddedEntryIndicator() + getSubjectEntryIndicator() + getSeriesEntryIndicator() + getSubDivisionType()
        + "          " + getGovernmentAgency() + getReferenceStatus() + " " + getRecordRevision() + getNonUniqueName()
        + getHeadingStatus() + "    " + getRecordModification() + getCataloguingSourceCode();

    return str;
  }

  public char getCataloguingRules() {
    return (getAutItm() != null ? getAutItm().getCataloguingRules() : ' ');
  }

  public char getCataloguingSourceCode() {
    return (getAutItm() != null ? getAutItm().getCataloguingSourceCode() : ' ');
  }

  public String getEnteredOnFileDateYYMMDD() {
    return (getAutItm() != null ? getAutItm().getEnteredOnFileDateYYMMDD() : "");
  }

  public char getGovernmentAgency() {
    return (getAutItm() != null ? getAutItm().getGovernmentAgency() : ' ');
  }

  public char getHeadingStatus() {
    return (getAutItm() != null ? getAutItm().getHeadingStatus() : ' ');
  }

  public char getMainAddedEntryIndicator() {
    return (getAutItm() != null ? getAutItm().getMainAddedEntryIndicator() : ' ');
  }

  public char getNonUniqueName() {
    return (getAutItm() != null ? getAutItm().getNonUniqueName() : ' ');
  }

  public char getRecordModification() {
    return (getAutItm() != null ? getAutItm().getRecordModification() : ' ');
  }

  public char getRecordRevision() {
    return (getAutItm() != null ? getAutItm().getRecordRevision() : ' ');
  }

  public char getRecordType() {
    return (getAutItm() != null ? getAutItm().getRecordType() : ' ');
  }

  public char getReferenceStatus() {
    return (getAutItm() != null ? getAutItm().getReferenceStatus() : ' ');
  }

  public char getRomanizationScheme() {
    return (getAutItm() != null ? getAutItm().getRomanizationScheme() : ' ');
  }

  public char getSeriesEntryIndicator() {
    return (getAutItm() != null ? getAutItm().getSeriesEntryIndicator() : ' ');
  }

  public char getSeriesNumbering() {
    return (getAutItm() != null ? getAutItm().getSeriesNumbering() : ' ');
  }

  public char getSeriesType() {
    return (getAutItm() != null ? getAutItm().getSeriesType() : ' ');
  }

  public char getSubDivisionType() {
    return (getAutItm() != null ? getAutItm().getSubDivisionType() : ' ');
  }

  public char getSubjectEntryIndicator() {
    return (getAutItm() != null ? getAutItm().getSubjectEntryIndicator() : ' ');
  }

  public char getSubjectSystem() {
    return (getAutItm() != null ? getAutItm().getSubjectSystem() : ' ');
  }

  public void setCataloguingRules(char c) {
    if (getAutItm() != null)
      getAutItm().setCataloguingRules(c);
  }

  public void setCataloguingSourceCode(char c) {
    if (getAutItm() != null)
      getAutItm().setCataloguingSourceCode(c);
  }

  public void setEncodingLevel(char c) {
    if (getAutItm() != null)
      getAutItm().setEncodingLevel(c);
  }

  public void setEnteredOnFileDate(Date date) {
    if (getAutItm() != null)
      getAutItm().setEnteredOnFileDate(date);
  }

  public void setGovernmentAgency(char c) {
    if (getAutItm() != null)
      getAutItm().setGovernmentAgency(c);
  }

  public void setHeadingStatus(char c) {
    if (getAutItm() != null)
      getAutItm().setHeadingStatus(c);
  }

  public void setMainAddedEntryIndicator(char c) {
    if (getAutItm() != null)
      getAutItm().setMainAddedEntryIndicator(c);
  }

  public void setNonUniqueName(char c) {
    if (getAutItm() != null)
      getAutItm().setNonUniqueName(c);
  }

  public void setRecordModification(char c) {
    if (getAutItm() != null)
      getAutItm().setRecordModification(c);
  }

  public void setRecordRevision(char c) {
    if (getAutItm() != null)
      getAutItm().setRecordRevision(c);
  }

  public void setRecordStatusCode(char c) {
    if (getAutItm() != null)
      getAutItm().setRecordStatusCode(c);
  }

  public void setRecordType(char c) {
    if (getAutItm() != null)
      getAutItm().setRecordType(c);
  }

  public void setReferenceStatus(char c) {
    if (getAutItm() != null)
      getAutItm().setReferenceStatus(c);
  }

  public void setRomanizationScheme(char c) {
    if (getAutItm() != null)
      getAutItm().setRomanizationScheme(c);
  }

  public void setSeriesEntryIndicator(char c) {
    if (getAutItm() != null)
      getAutItm().setSeriesEntryIndicator(c);
  }

  public void setSeriesNumbering(char c) {
    if (getAutItm() != null)
      getAutItm().setSeriesNumbering(c);
  }

  public void setSeriesType(char c) {
    if (getAutItm() != null)
      getAutItm().setSeriesType(c);
  }

  public void setSubDivisionType(char c) {
    if (getAutItm() != null)
      getAutItm().setSubDivisionType(c);
  }

  public void setSubjectDescriptor(char c) {
    if (getAutItm() != null)
      getAutItm().setSubjectDescriptor(c);
  }

  public void setSubjectEntryIndicator(char c) {
    if (getAutItm() != null)
      getAutItm().setSubjectEntryIndicator(c);
  }

  public char getSubjectDescriptor() {
    return (getAutItm() != null ? getAutItm().getSubjectDescriptor() : ' ');
  }

  public void setSubjectSystem(char c) {
    if (getAutItm() != null)
      getAutItm().setSubjectSystem(c);
  }

  public char getBilingualUsage() {
    return (getAutItm() != null ? getAutItm().getBilingualUsage() : ' ');
  }

  public void setBilingualUsage(char c) {
    if (getAutItm() != null)
      getAutItm().setBilingualUsage(c);
  }

  private AUT getAutItm() {
    return (AUT) getItemEntity();
  }

}
