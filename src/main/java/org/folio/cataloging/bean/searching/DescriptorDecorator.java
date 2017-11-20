package org.folio.cataloging.bean.searching;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import org.folio.cataloging.bean.cataloguing.bibliographic.codelist.CodeListsBean;
import org.folio.cataloging.business.codetable.ValueLabelElement;
import org.folio.cataloging.business.common.CorrelationValues;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.business.descriptor.SortFormParameters;
import org.folio.cataloging.exception.InvalidDescriptorException;
import org.folio.cataloging.dao.persistence.DescriptorKey;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import org.folio.cataloging.dao.common.HibernateUtil;

/**
 * Implements pattern Decorator about Descriptor to supply 
 * verificationLevelText localized string
 * @author michelem
 */
public class DescriptorDecorator 
{
	private Descriptor concreteDescriptor;
	private Locale currentLocale;
	private String indexingLanguage;
	private String accessPointLanguage;
	private boolean associated = false;
	private String editoreHdg;
	private String descriptionShelfList;;
	/* 201107: aggiunto codice editore breve nello scorri di PU e PP */
	private String shortPublisher;

	public void setIndexingLanguage(String indexingLanguage) {
		this.indexingLanguage = indexingLanguage;
	}

	public DescriptorDecorator(Descriptor concreteDescriptor) {
		super();
		this.concreteDescriptor = concreteDescriptor;
	}

	public Locale getCurrentLocale() {
		return currentLocale;
	}

	public void setCurrentLocale(Locale currentLocale) {
		this.currentLocale = currentLocale;
	}

	public boolean changeAffectsCacheTable() {
		return concreteDescriptor.changeAffectsCacheTable();
	}

	public boolean equals(Object obj) {
		return concreteDescriptor.equals(obj);
	}

	public void evict() throws DataAccessException {
		concreteDescriptor.evict();
	}

	public void generateNewKey() throws DataAccessException {
		concreteDescriptor.generateNewKey();
	}

	public Class getAccessPointClass() {
		return concreteDescriptor.getAccessPointClass();
	}

	public short getAccessPointLanguageCode() {
		return concreteDescriptor.getAccessPointLanguage();
	}

	public final String getAuthoritiesQueryString(Locale locale) throws DataAccessException {
		return concreteDescriptor.getAuthoritiesQueryString(locale);
	}

	public short getAuthorityCount() {
		return concreteDescriptor.getAuthorityCount();
	}

	public String getBrowseKey() {
		return concreteDescriptor.getBrowseKey();
	}

	public short getCategory() {
		return concreteDescriptor.getCategory();
	}

	public CorrelationValues getCorrelationValues() {
		return concreteDescriptor.getCorrelationValues();
	}

	public String getCroppedHtmlText() {
		return concreteDescriptor.getCroppedHtmlText();
	}

	public HibernateUtil getDAO() {
		return concreteDescriptor.getDAO();
	}

	public String getDefaultBrowseKey() {
		return concreteDescriptor.getDefaultBrowseKey();
	}

	public String getDisplayText() {
		return concreteDescriptor.getDisplayText();
	}

	public String getHeadingNumberSearchIndexKey() {
		return concreteDescriptor.getHeadingNumberSearchIndexKey();
	}

	public String getHeadingQueryString(Locale locale) throws DataAccessException {
		return concreteDescriptor.getHeadingQueryString(locale);
	}

	public DescriptorKey getKey() {
		return concreteDescriptor.getKey();
	}

	public String getNextNumberKeyFieldCode() {
		return concreteDescriptor.getNextNumberKeyFieldCode();
	}

	public Class getReferenceClass(Class targetClazz) {
		return concreteDescriptor.getReferenceClass(targetClazz);
	}

	public String getSafeHtmlText() {
		return concreteDescriptor.getSafeHtmlText();
	}

	public String getScriptingLanguage() {
		return concreteDescriptor.getScriptingLanguage();
	}
	
	public short getIndexingLanguageCode(){
		return concreteDescriptor.getIndexingLanguage();
	}

	public short getSkipInFiling() {
		return concreteDescriptor.getSkipInFiling();
	}

	public String getSortForm() {
		return concreteDescriptor.getSortForm();
	}

	public SortFormParameters getSortFormParameters() {
		return concreteDescriptor.getSortFormParameters();
	}

	public String getStringText() {
		return concreteDescriptor.getStringText();
	}

	public int getUpdateStatus() {
		return concreteDescriptor.getUpdateStatus();
	}

	public String getUserViewString() {
		return concreteDescriptor.getUserViewString();
	}

	public char getVerificationLevel() {
		return concreteDescriptor.getVerificationLevel();
	}

	public int hashCode() {
		return concreteDescriptor.hashCode();
	}

	public boolean isCanTransfer() {
		return concreteDescriptor.isCanTransfer();
	}

	public boolean isChanged() {
		return concreteDescriptor.isChanged();
	}

	public boolean isDeleted() {
		return concreteDescriptor.isDeleted();
	}

	public boolean isMatchingAnotherHeading() {
		return concreteDescriptor.isMatchingAnotherHeading();
	}

	public boolean isNew() {
		return concreteDescriptor.isNew();
	}

	public void markChanged() {
		concreteDescriptor.markChanged();
	}

	public void markDeleted() {
		concreteDescriptor.markDeleted();
	}

	public void markNew() {
		concreteDescriptor.markNew();
	}

	public void markUnchanged() {
		concreteDescriptor.markUnchanged();
	}

	public boolean onDelete(Session arg0) throws CallbackException {
		return concreteDescriptor.onDelete(arg0);
	}

	public void onLoad(Session arg0, Serializable arg1) {
		concreteDescriptor.onLoad(arg0, arg1);
	}

	public boolean onSave(Session arg0) throws CallbackException {
		return concreteDescriptor.onSave(arg0);
	}

	public boolean onUpdate(Session arg0) throws CallbackException {
		return concreteDescriptor.onUpdate(arg0);
	}

	public void setAccessPointLanguage(short s) {
		concreteDescriptor.setAccessPointLanguage(s);
	}

	public void setAuthorityCount(short s) {
		concreteDescriptor.setAuthorityCount(s);
	}

	public void setCorrelationValues(CorrelationValues v) {
		concreteDescriptor.setCorrelationValues(v);
	}

	public void setKey(DescriptorKey hdg_key) {
		concreteDescriptor.setKey(hdg_key);
	}

	public void setScriptingLanguage(String string) {
		concreteDescriptor.setScriptingLanguage(string);
	}

	public void setSkipInFiling(short skipInFiling) {
		concreteDescriptor.setSkipInFiling(skipInFiling);
	}

	public void setSortForm(String string) {
		concreteDescriptor.setSortForm(string);
	}

	public void setStringText(String string) {
		concreteDescriptor.setStringText(string);
	}

	public void setUpdateStatus(int i) {
		concreteDescriptor.setUpdateStatus(i);
	}

	public void setUserViewString(String s) {
		concreteDescriptor.setUserViewString(s);
	}

	public void setVerificationLevel(char c) {
		concreteDescriptor.setVerificationLevel(c);
	}

	public String toString() {
		return concreteDescriptor.toString();
	}

	public void validate() throws InvalidDescriptorException {
		concreteDescriptor.validate();
	}
	
	/**
	 * Decorated method
	 * @return
	 */
	private List getVerificationLevelList() {
		return CodeListsBean.getVerificationLevel().getCodeList(currentLocale);
	}

	/**
	 * Decorated method
	 * @return
	 */
	public String getVerificationLevelText() 
	{
		String s = ValueLabelElement.decode(String.valueOf(getVerificationLevel()), getVerificationLevelList());
		return s;
	}
	
	/**
	 * Decorated method
	 * @return
	 */
	public String getStyleLevel() {
		String s;
		switch (getVerificationLevel()) {
		case '1':		// Salvato
		case '2':		// Preliminare
			s = "LOW";
			break;
		case '5':		// Verificato
		case '6':		// Verificato-nazionale
		// case '7': 	// Ver.-naz.(in corso)
		case '8':		// Autenticato
			s = "MAX";
			break;
		default:		
						// 3-Non verificato, 
						// 4-Non verificato-nazionale
						// <null>
						// ...
			s = "MID";
			break;
		}
		return s;
	}
	
	public void setAccessPointLanguage(String accessPointLanguage) {
		this.accessPointLanguage = accessPointLanguage;
	}
		
	public String getAccessPointLanguage() {
		return accessPointLanguage;
	}
	
	public String getIndexingLanguage() {
		return indexingLanguage;
	}

	public boolean isAssociated() {
		return associated;
	}

	public void setAssociated(boolean associated) {
		this.associated = associated;
	}

	public String getEditoreHdg() {
		return editoreHdg;
	}

	public void setEditoreHdg(String editoreHdg) {
		this.editoreHdg = editoreHdg;
	}

	public String getShortPublisher() {
		return shortPublisher;
	}

	public void setShortPublisher(String shortPublisher) {
		this.shortPublisher = shortPublisher;
	}

	public String getDescriptionShelfList() {
		return descriptionShelfList;
	}

	public void setDescriptionShelfList(String descriptionShelfList) {
		this.descriptionShelfList = descriptionShelfList;
	}	
}