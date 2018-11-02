package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;

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
  protected char verificationLevel = ' ';
  private Integer amicusNumber = null;

  /**
   * @since 1.0
   */
  public void evict() throws DataAccessException {
    persistenceState.evict(this);
  }

  /**
   * @since 1.0
   */
  public Integer getAmicusNumber() {
    return amicusNumber;
  }

  /**
   * @since 1.0
   */
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

  /**
   * @since 1.0
   */
  public String getGeographicAreaStringText() {
    return geographicAreaStringText;
  }

  /**
   * @since 1.0
   */
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

  /**
   * @since 1.0
   */
  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
  }

  /**
   * @since 1.0
   */
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

  /**
   * @since 1.0
   */
  public boolean isChanged() {
    return persistenceState.isChanged();
  }

  /**
   * @since 1.0
   */
  public boolean isDeleted() {
    return persistenceState.isDeleted();
  }

  /**
   * @since 1.0
   */
  public boolean isNew() {
    return persistenceState.isNew();
  }

  /**
   * @since 1.0
   */
  public void markChanged() {
    persistenceState.markChanged();
  }

  /**
   * @since 1.0
   */
  public void markDeleted() {
    persistenceState.markDeleted();
  }

  /**
   * @since 1.0
   */
  public void markNew() {
    persistenceState.markNew();
  }

  /**
   * @since 1.0
   */
  public void markUnchanged() {
    persistenceState.markUnchanged();
  }

  /**
   * @since 1.0
   */
  public boolean onDelete(Session arg0) throws CallbackException {
    return persistenceState.onDelete(arg0);
  }

  /**
   * @since 1.0
   */
  public void onLoad(Session arg0, Serializable arg1) {
    persistenceState.onLoad(arg0, arg1);
  }

  /**
   * @since 1.0
   */
  public boolean onSave(Session arg0) throws CallbackException {
    return persistenceState.onSave(arg0);
  }

  /**
   * @since 1.0
   */
  public boolean onUpdate(Session arg0) throws CallbackException {
    return persistenceState.onUpdate(arg0);
  }

}
