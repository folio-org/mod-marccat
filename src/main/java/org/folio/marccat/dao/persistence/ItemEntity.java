package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.exception.DataAccessException;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Abstract class representing the db entity that holds the catalogItem
 * (i.e. BIB_ITM or AUT).
 *
 * @author paulm
 * @since 1.0
 */
public abstract class ItemEntity implements Persistence, Serializable {

  protected String authenticationCenterStringText;
  protected String cataloguingSourceStringText;
  protected char characterCodingSchemeCode = ' ';
  protected Date dateOfLastTransaction = new Date();
  protected char encodingLevel = ' ';
  protected Date enteredOnFileDate = new Date();
  protected String geographicAreaStringText;
  protected String languageOfCataloguing;
  protected PersistenceState persistenceState = new PersistenceState();
  protected char recordStatusCode = 'n';
  protected String timePeriodStringText;
  protected String typeOfDateTimeCode = "";
  protected char verificationLevel = '1';
  private Integer amicusNumber = null;



  public Integer getAmicusNumber() {
    return amicusNumber;
  }


  public void setAmicusNumber(Integer integer) {
    amicusNumber = integer;
  }

  /**
   *
   */
  public String getAuthenticationCenterStringText() {
    return authenticationCenterStringText;
  }

  /**
   *
   */
  public void setAuthenticationCenterStringText(String string) {
    authenticationCenterStringText = string;
  }

  /**
   *
   */
  public String getCataloguingSourceStringText() {
    return cataloguingSourceStringText;
  }

  /**
   *
   */
  public void setCataloguingSourceStringText(String string) {
    cataloguingSourceStringText = string;
  }

  /**
   *
   */
  public char getCharacterCodingSchemeCode() {
    return characterCodingSchemeCode;
  }

  /**
   *
   */
  public void setCharacterCodingSchemeCode(char c) {
    characterCodingSchemeCode = c;
  }

  /**
   *
   */
  public Date getDateOfLastTransaction() {
    return dateOfLastTransaction;
  }

  /**
   *
   */
  public void setDateOfLastTransaction(Date date) {
    dateOfLastTransaction = date;
  }

  /**
   *
   */
  public char getEncodingLevel() {
    return encodingLevel;
  }

  /**
   *
   */
  public void setEncodingLevel(char c) {
    encodingLevel = c;
  }

  public Date getEnteredOnFileDate() {
    return enteredOnFileDate;
  }

  public void setEnteredOnFileDate(Date date) {
    enteredOnFileDate = date;
  }

  public String getEnteredOnFileDateYYMMDD() {
    Format formatter = new SimpleDateFormat("yyMMdd");
    String result =
      formatter.format(getEnteredOnFileDate());
    return result;
  }

  public String getEnteredOnFileDateYYYYMMDD() {
    Format formatter = new SimpleDateFormat("yyyyMMdd");
    String result =
      formatter.format(getEnteredOnFileDate());
    return result;
  }


  public String getGeographicAreaStringText() {
    return geographicAreaStringText;
  }


  public void setGeographicAreaStringText(String string) {
    geographicAreaStringText = string;
  }

  /**
   *
   */
  public String getLanguageOfCataloguing() {
    return languageOfCataloguing;
  }

  /**
   *
   */
  public void setLanguageOfCataloguing(String string) {
    languageOfCataloguing = string;
  }

  /**
   *
   */
  public char getRecordStatusCode() {
    return recordStatusCode;
  }

  /**
   *
   */
  public void setRecordStatusCode(char c) {
    recordStatusCode = c;
  }

  /**
   *
   */
  public String getTimePeriodStringText() {
    return timePeriodStringText;
  }

  /**
   *
   */
  public void setTimePeriodStringText(String string) {
    timePeriodStringText = string;
  }

  /**
   *
   */
  public String getTypeOfDateTimeCode() {
    return typeOfDateTimeCode;
  }

  /**
   *
   */
  public void setTypeOfDateTimeCode(String s) {
    typeOfDateTimeCode = s;
  }


  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
  }


  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus(i);
  }

  /**
   *
   */
  public char getVerificationLevel() {
    return verificationLevel;
  }

  /**
   *
   */
  public void setVerificationLevel(char verificationLevel) {
    this.verificationLevel = verificationLevel;
  }


  public boolean isChanged() {
    return persistenceState.isChanged();
  }


  public boolean isDeleted() {
    return persistenceState.isDeleted();
  }


  public boolean isNew() {
    return persistenceState.isNew();
  }


  public void markChanged() {
    persistenceState.markChanged();
  }


  public void markDeleted() {
    persistenceState.markDeleted();
  }


  public void markNew() {
    persistenceState.markNew();
  }


  public void markUnchanged() {
    persistenceState.markUnchanged();
  }


  public boolean onDelete(Session arg0) throws CallbackException {
    return persistenceState.onDelete(arg0);
  }


  public void onLoad(Session arg0, Serializable arg1) {
    persistenceState.onLoad(arg0, arg1);
  }


  public boolean onSave(Session arg0) throws CallbackException {
    return persistenceState.onSave(arg0);
  }


  public boolean onUpdate(Session arg0) throws CallbackException {
    return persistenceState.onUpdate(arg0);
  }

}
