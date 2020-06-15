package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.DAOBibItem;

import java.io.Serializable;

public class BIB_ITM extends ItemEntity implements PersistentObjectWithView, Serializable {

  private char canadianContentIndicator = '0';
  private char controlTypeCode = ' ';
  private String countryStringText;
  private char descriptiveCataloguingCode = ' ';
  private String formOfMusicStringText;
  private int inputSourceCode = 96;
  private char itemBibliographicLevelCode = 'm';
  private String itemDateFirstPublication;
  private String itemDateLastPublication = "    ";
  private char itemDateTypeCode = 's';
  private char itemRecordTypeCode = 'a';
  private String languageCode;
  private String languageStringText;
  private char linkedRecordCode = ' ';
  private String marcCountryCode;
  private String projectedPublicationDateCode;
  private char recordCataloguingSourceCode;
  private char recordModifiedCode = ' ';
  private char replacementStatusCode = '1';
  private String specialCodedDatesStringText;
  private String translationCode = "";
  private String userViewString = "0000000000000000";

  public BIB_ITM() {
    super();
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof BIB_ITM))
      return false;

    BIB_ITM other = (BIB_ITM) obj;
    return (other.getAmicusNumber().equals(this.getAmicusNumber()) && other.getUserViewString().equals(this.getUserViewString()));
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    if (getAmicusNumber() == null) {
      return -1;
    } else {
      return getAmicusNumber().intValue();
    }
  }


  public char getCanadianContentIndicator() {
    return canadianContentIndicator;
  }

  public void setCanadianContentIndicator(char canadianContentIndicator) {
    this.canadianContentIndicator = canadianContentIndicator;
  }

  public char getControlTypeCode() {
    return controlTypeCode;
  }

  public void setControlTypeCode(char c) {
    controlTypeCode = c;
  }

  public String getCountryStringText() {
    return countryStringText;
  }

  public void setCountryStringText(String string) {
    countryStringText = string;
  }

  public AbstractDAO getDAO() {
    return new DAOBibItem();
  }

  public char getDescriptiveCataloguingCode() {
    return descriptiveCataloguingCode;
  }

  public void setDescriptiveCataloguingCode(char c) {
    descriptiveCataloguingCode = c;
  }

  public String getFormOfMusicStringText() {
    return formOfMusicStringText;
  }

  public void setFormOfMusicStringText(String string) {
    formOfMusicStringText = string;
  }

  public int getInputSourceCode() {
    return inputSourceCode;
  }

  public void setInputSourceCode(int inputSourceCode) {
    this.inputSourceCode = inputSourceCode;
  }

  public char getItemBibliographicLevelCode() {
    return itemBibliographicLevelCode;
  }

  public void setItemBibliographicLevelCode(char c) {
    itemBibliographicLevelCode = c;
  }

  public String getItemDateFirstPublication() {
    return itemDateFirstPublication;
  }

  public void setItemDateFirstPublication(String string) {
    itemDateFirstPublication = string;
  }

  public String getItemDateLastPublication() {
    return itemDateLastPublication;
  }

  public void setItemDateLastPublication(String string) {
    itemDateLastPublication = string;
  }

  public char getItemDateTypeCode() {
    return itemDateTypeCode;
  }

  public void setItemDateTypeCode(char c) {
    itemDateTypeCode = c;
  }

  public char getItemRecordTypeCode() {
    return itemRecordTypeCode;
  }

  public void setItemRecordTypeCode(char c) {
    itemRecordTypeCode = c;
  }

  public String getLanguageCode() {
    return languageCode;
  }

  public void setLanguageCode(String string) {
    languageCode = string;
  }

  public String getLanguageStringText() {
    return languageStringText;
  }

  public void setLanguageStringText(String string) {
    languageStringText = string;
  }

  public char getLinkedRecordCode() {
    return linkedRecordCode;
  }

  public void setLinkedRecordCode(char c) {
    linkedRecordCode = c;
  }

  public String getMarcCountryCode() {
    return marcCountryCode;
  }

  public void setMarcCountryCode(String string) {
    marcCountryCode = string;
  }

  public String getProjectedPublicationDateCode() {
    return projectedPublicationDateCode;
  }

  public void setProjectedPublicationDateCode(String c) {
    projectedPublicationDateCode = c;
  }

  public char getRecordCataloguingSourceCode() {
    return recordCataloguingSourceCode;
  }

  public void setRecordCataloguingSourceCode(char c) {
    recordCataloguingSourceCode = c;
  }

  public char getRecordModifiedCode() {
    return recordModifiedCode;
  }

  public void setRecordModifiedCode(char c) {
    recordModifiedCode = c;
  }

  public char getReplacementStatusCode() {
    return replacementStatusCode;
  }

  public void setReplacementStatusCode(char replacementStatusCode) {
    this.replacementStatusCode = replacementStatusCode;
  }

  public String getSpecialCodedDatesStringText() {
    return specialCodedDatesStringText;
  }

  public void setSpecialCodedDatesStringText(String string) {
    specialCodedDatesStringText = string;
  }

  public String getTranslationCode() {
    return translationCode;
  }

  public void setTranslationCode(String c) {
    translationCode = c;
  }

  public String getUserViewString() {
    return userViewString;
  }

  public void setUserViewString(String string) {
    userViewString = string;
  }
}
