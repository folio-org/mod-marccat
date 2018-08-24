package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.PersistentObjectWithView;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.DAOBibItem;
import org.folio.cataloging.dao.DAOSystemNextNumber;

import java.io.Serializable;

public class BIB_ITM extends ItemEntity implements PersistentObjectWithView, Serializable 
{

	private char canadianContentIndicator = '0';
	private char controlTypeCode = ' ';
	private String countryStringText;
	private char descriptiveCataloguingCode = ' ';
	private String formOfMusicStringText;
	private int inputSourceCode = 0;
	private char itemBibliographicLevelCode = 'm';
	private String itemDateFirstPublication;
	private String itemDateLastPublication = "    ";
    private char itemDateTypeCode; //Defaults.getChar("bibliographicItem.itemDateTypeCode");
	private char itemRecordTypeCode = 'a';
	private String languageCode; //Defaults.getString("bibliographicItem.languageCode");
	private String languageStringText;
	private char linkedRecordCode = ' ';
	private String marcCountryCode; //= Defaults.getString("bibliographicItem.marcCountryCode");
	private String projectedPublicationDateCode;
	private char recordCataloguingSourceCode; //= Defaults.getChar("bibliographicItem.recordCataloguingSourceCode");
	private char recordModifiedCode = ' ';
	private char replacementStatusCode = ' ';
	private String specialCodedDatesStringText = "";
	private String translationCode = "";
	private String userViewString = "0000000000000000";

	//TODO use configuration module
	public BIB_ITM() 
	{
		super();		
		/*setLanguageOfCataloguing(Defaults.getString("bibliographicItem.languageCode"));
		setCataloguingSourceStringText(Defaults.getString("bibligraphicItem.cataloguingSourceStringText"));
		setMarcCountryCode(Defaults.getString("bibliographicItem.languageOfCataloguing"));*/
	}

	public boolean equals(Object obj) 
	{
		if (!(obj instanceof BIB_ITM))
			return false;
		
		BIB_ITM other = (BIB_ITM) obj;
		return (other.getAmicusNumber().equals(this.getAmicusNumber()) && other.getUserViewString().equals(this.getUserViewString()));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		if (getAmicusNumber() == null) {
			return -1;
		}
		else {
			return getAmicusNumber().intValue();
		}
	}

	/* (non-Javadoc)
	 * @see librisuite.business.common.PersistentObject#generateNewKey()
	 */
	@Deprecated
	public void generateNewKey() throws DataAccessException
	{
		DAOSystemNextNumber dao = new DAOSystemNextNumber();
		setAmicusNumber(new Integer(dao.getNextNumber("BI")));
	}

	public char getCanadianContentIndicator() {
		return canadianContentIndicator;
	}

	public char getControlTypeCode() {
		return controlTypeCode;
	}

	public String getCountryStringText() {
		return countryStringText;
	}
	
	public AbstractDAO getDAO() {
		return new DAOBibItem();
	}

	public char getDescriptiveCataloguingCode() {
		return descriptiveCataloguingCode;
	}

	public String getFormOfMusicStringText() {
		return formOfMusicStringText;
	}

	public int getInputSourceCode() {
		return inputSourceCode;
	}

	public char getItemBibliographicLevelCode() {
		return itemBibliographicLevelCode;
	}

	public String getItemDateFirstPublication() {
		return itemDateFirstPublication;
	}

	public String getItemDateLastPublication() {
		return itemDateLastPublication;
	}

	public char getItemDateTypeCode() {
		return itemDateTypeCode;
	}

	public char getItemRecordTypeCode() {
		return itemRecordTypeCode;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public String getLanguageStringText() {
		return languageStringText;
	}

	public char getLinkedRecordCode() {
		return linkedRecordCode;
	}

	public String getMarcCountryCode() {
		return marcCountryCode;
	}

	public String getProjectedPublicationDateCode() {
		return projectedPublicationDateCode;
	}

	public char getRecordCataloguingSourceCode() {
		return recordCataloguingSourceCode;
	}

	public char getRecordModifiedCode() {
		return recordModifiedCode;
	}

	public char getReplacementStatusCode() {
		return replacementStatusCode;
	}

	public String getSpecialCodedDatesStringText() {
		return specialCodedDatesStringText;
	}

	public String getTranslationCode() {
		return translationCode;
	}

	public String getUserViewString() {
		return userViewString;
	}

	public void setCanadianContentIndicator(char canadianContentIndicator) {
		this.canadianContentIndicator = canadianContentIndicator;
	}

	public void setControlTypeCode(char c) {
		controlTypeCode = c;
	}

	public void setCountryStringText(String string) {
		countryStringText = string;
	}

	public void setDescriptiveCataloguingCode(char c) {
		descriptiveCataloguingCode = c;
	}

	public void setFormOfMusicStringText(String string) {
		formOfMusicStringText = string;
	}

	public void setInputSourceCode(int inputSourceCode) {
		this.inputSourceCode = inputSourceCode;
	}

	public void setItemBibliographicLevelCode(char c) {
		itemBibliographicLevelCode = c;
	}

	public void setItemDateFirstPublication(String string) {
		itemDateFirstPublication = string;
	}

	public void setItemDateLastPublication(String string) {
		itemDateLastPublication = string;
	}

	public void setItemDateTypeCode(char c) {
		itemDateTypeCode = c;
	}

	public void setItemRecordTypeCode(char c) {
		itemRecordTypeCode = c;
	}

	public void setLanguageCode(String string) {
		languageCode = string;
	}

	public void setLanguageStringText(String string) {
		languageStringText = string;
	}

	public void setLinkedRecordCode(char c) {
		linkedRecordCode = c;
	}

	public void setMarcCountryCode(String string) {
		marcCountryCode = string;
	}

	public void setProjectedPublicationDateCode(String string) {
		projectedPublicationDateCode = string;
	}

	public void setRecordCataloguingSourceCode(char c) {
		recordCataloguingSourceCode = c;
	}

	public void setRecordModifiedCode(char c) {
		recordModifiedCode = c;
	}

	public void setReplacementStatusCode(char replacementStatusCode) {
		this.replacementStatusCode = replacementStatusCode;
	}

	public void setSpecialCodedDatesStringText(String string) {
		specialCodedDatesStringText = string;
	}

	public void setTranslationCode(String string) {
		translationCode = string;
	}

	public void setUserViewString(String string) {
		userViewString = string;
	}
}