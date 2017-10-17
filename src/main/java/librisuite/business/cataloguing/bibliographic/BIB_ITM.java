package librisuite.business.cataloguing.bibliographic;

import java.io.Serializable;

import librisuite.business.cataloguing.common.ItemEntity;
import librisuite.business.common.DAOSystemNextNumber;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Defaults;
import librisuite.business.common.PersistentObjectWithView;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.libricore.librisuite.common.HibernateUtil;

public class BIB_ITM extends ItemEntity implements PersistentObjectWithView, Serializable 
{
	private Log logger = LogFactory.getLog(BIB_ITM.class);
	private static final long serialVersionUID = 1006288231698510602L;
	
	private char canadianContentIndicator = '0';
	private char controlTypeCode = ' ';
	private String countryStringText = new String("");
	private char descriptiveCataloguingCode = ' ';
	private String formOfMusicStringText = new String("");
	private int inputSourceCode = 0;
	private char itemBibliographicLevelCode = 'm';
	private String itemDateFirstPublication = new String("    ");
	private String itemDateLastPublication = new String("    ");
    private char itemDateTypeCode = Defaults.getChar("bibliographicItem.itemDateTypeCode");
	private char itemRecordTypeCode = 'a';
	private String languageCode = Defaults.getString("bibliographicItem.languageCode");
	private String languageStringText = new String("");
	private char linkedRecordCode = ' ';
	private String marcCountryCode = Defaults.getString("bibliographicItem.marcCountryCode");
	private String projectedPublicationDateCode = new String("");
	private char recordCataloguingSourceCode = Defaults.getChar("bibliographicItem.recordCataloguingSourceCode");
	private char recordModifiedCode = ' ';
	private char replacementStatusCode = ' ';
	private String specialCodedDatesStringText = new String("");
	private String translationCode = new String("");
	private String userViewString = "0000000000000000";
	
	public BIB_ITM() 
	{
		super();		
		setLanguageOfCataloguing(Defaults.getString("bibliographicItem.languageCode"));
		setCataloguingSourceStringText(Defaults.getString("bibligraphicItem.cataloguingSourceStringText"));
		/*MODIFICA BARBARA DEFAULT DEL PAESE*/
		setMarcCountryCode(Defaults.getString("bibliographicItem.languageOfCataloguing"));
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
	
	/* (non-Javadoc)
	 * @see librisuite.business.common.Persistence#getDAO()
	 */
	public HibernateUtil getDAO() {
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