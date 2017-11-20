/*
 * (c) LibriCore
 * 
 * Created on Nov 4, 2005
 * 
 * ItemEntity.java
 */
package org.folio.cataloging.business.cataloguing.common;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

/**
 * Abstract class representing the db entity that holds the catalogItem 
 * (i.e. BIB_ITM or AUT)
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public abstract class ItemEntity implements Persistence, Serializable {

	private Integer amicusNumber = null;

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

	protected String timePeriodStringText = new String("");

	protected String typeOfDateTimeCode = new String("");

	protected char verificationLevel = Defaults.getChar("bibliographicItem.verificationLevel");

	/**
		 * 
		 * @since 1.0
		 */
	public void evict() throws DataAccessException {
		persistenceState.evict(this);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public Integer getAmicusNumber() {
		return amicusNumber;
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
	public String getCataloguingSourceStringText() {
		return cataloguingSourceStringText;
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
	public Date getDateOfLastTransaction() {
		return dateOfLastTransaction;
	}

	/**
		 * 
		 */
	public char getEncodingLevel() {
		return encodingLevel;
	}

	public Date getEnteredOnFileDate() {
		return enteredOnFileDate;
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
		 * 
		 * @since 1.0
		 */
	public String getGeographicAreaStringText() {
		return geographicAreaStringText;
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
	public char getRecordStatusCode() {
		return recordStatusCode;
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
	public String getTypeOfDateTimeCode() {
		return typeOfDateTimeCode;
	}

	/**
		 * 
		 * @since 1.0
		 */
	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
	}

	/**
		 * 
		 */
	public char getVerificationLevel() {
		return verificationLevel;
	}

	/**
		 * 
		 * @since 1.0
		 */
	public boolean isChanged() {
		return persistenceState.isChanged();
	}

	/**
		 * 
		 * @since 1.0
		 */
	public boolean isDeleted() {
		return persistenceState.isDeleted();
	}

	/**
		 * 
		 * @since 1.0
		 */
	public boolean isNew() {
		return persistenceState.isNew();
	}

	/**
		 * 
		 * @since 1.0
		 */
	public void markChanged() {
		persistenceState.markChanged();
	}

	/**
		 * 
		 * @since 1.0
		 */
	public void markDeleted() {
		persistenceState.markDeleted();
	}

	/**
		 * 
		 * @since 1.0
		 */
	public void markNew() {
		persistenceState.markNew();
	}

	/**
		 * 
		 * @since 1.0
		 */
	public void markUnchanged() {
		persistenceState.markUnchanged();
	}

	/**
		 * 
		 * @since 1.0
		 */
	public boolean onDelete(Session arg0) throws CallbackException {
		return persistenceState.onDelete(arg0);
	}

	/**
		 * 
		 * @since 1.0
		 */
	public void onLoad(Session arg0, Serializable arg1) {
		persistenceState.onLoad(arg0, arg1);
	}

	/**
		 * 
		 * @since 1.0
		 */
	public boolean onSave(Session arg0) throws CallbackException {
		return persistenceState.onSave(arg0);
	}

	/**
		 * 
		 * @since 1.0
		 */
	public boolean onUpdate(Session arg0) throws CallbackException {
		return persistenceState.onUpdate(arg0);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setAmicusNumber(Integer integer) {
		amicusNumber = integer;
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
	public void setCataloguingSourceStringText(String string) {
		cataloguingSourceStringText = string;
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
	public void setDateOfLastTransaction(Date date) {
		dateOfLastTransaction = date;
	}

	/**
		 * 
		 */
	public void setEncodingLevel(char c) {
		encodingLevel = c;
	}

	public void setEnteredOnFileDate(Date date) {
		enteredOnFileDate = date;
	}

	/**
		 * 
		 * @since 1.0
		 */
	public void setGeographicAreaStringText(String string) {
		geographicAreaStringText = string;
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
	public void setRecordStatusCode(char c) {
		recordStatusCode = c;
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
	public void setTypeOfDateTimeCode(String string) {
		typeOfDateTimeCode = string;
	}

	/**
		 * 
		 * @since 1.0
		 */
	public void setUpdateStatus(int i) {
		persistenceState.setUpdateStatus(i);
	}

	/**
		 * 
		 */
	public void setVerificationLevel(char verificationLevel) {
		this.verificationLevel = verificationLevel;
	}

}
