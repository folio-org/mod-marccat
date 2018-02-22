package org.folio.cataloging.form.cataloguing.bibliographic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.form.LibrisuiteForm;
import org.folio.cataloging.shared.CorrelationValues;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class EditTagForm extends LibrisuiteForm 
{
	private static final long serialVersionUID = -8415114598844048640L;
	private static final Log logger = LogFactory.getLog(EditTagForm.class);
	
	private Integer tagIndex = new Integer(0);
	private Integer bibliographicItemNumber = null; 
	private List subfields = new ArrayList();
	private List codes = new ArrayList();
	private List RelationshipReciprocalList = new ArrayList();
	private List fixedSubfields = new ArrayList();
	private List fixedCodes = new ArrayList();
	private CorrelationValues values = new CorrelationValues();
	private short category;
	private String operation;
	private String subfieldIndex;
	private String newSubfieldCode;
	private String newSubfieldContent;
	private short reciprocalOption;
	private short headingType;

	private String controlNumberValidationCode;
	private String addTagFromModel = null;
	
	private String optManagerialLevel;
	private String optDigitalLevel;
	private String checkDigital;
	private String checkOnline;
	private String checkContinuaz;
	
	private String tagNumber;
	private String tagNum;
	private char indicator1;
	private char indicator2;
	private short optNoteGroup;
	private String indexBrowse;
	
	private Integer amicusNumberMother;
	private Integer amicusNumberFiglia;
	private String volume;
	private String doiText;
	
	private String tomo;
	private String edizione;
	private String note;

	private String mdrFgl;
	private String searchString;
	
	private String model;
	private String newFromModelButton;
	private String newFromDitalButton;
	
	private String characterSelected;
	
	/* 20101019 campo per il tipo progressivo del tag097 */	
	private String progressiveType;
	/* 20110201 campo per l'anno del tag097 (per i seriali/fascicolo) */	
	private String year;
	
	/* Se true significa che nel record e' presente un 856 di tipo TESTO con doi assegnato */
	private boolean isPresent856;
	
	private String checkNTOCSB;
	private String checkNCORE;
	private Integer wemiFirstGroup;
	
	private String authorSelect;
	private String authorTextarea;
	private String informationSource;
	private String linkText;
	/*For search SBN*/
	private String sbnNameVid;
	
	private String checkNational;
	private String[] checkDeletedTags;
	private String verificationLevel;
	
	public EditTagForm() 
	{
		super();
		logger.debug("Constructing an EditTagForm");
	}
	
	public String getAuthorTextarea() {
		return authorTextarea;
	}

	public void setAuthorTextarea(String authorTextarea) {
		this.authorTextarea = authorTextarea;
	}

	public String getAuthorSelect() {
		return authorSelect;
	}

	public void setAuthorSelect(String authorSelect) {
		this.authorSelect = authorSelect;
	}

	public String getCheckNTOCSB() {
		return checkNTOCSB;
	}

	public void setCheckNTOCSB(String checkNTOCSB) {
		this.checkNTOCSB = checkNTOCSB;
	}

	public String getCheckNCORE() {
		return checkNCORE;
	}

	public void setCheckNCORE(String checkNCORE) {
		this.checkNCORE = checkNCORE;
	}
	
	public String getVerificationLevel() {
		return verificationLevel;
	}

	public void setVerificationLevel(String verificationLevel) {
		this.verificationLevel = verificationLevel;
	}
	
	public String getCheckNational() {
		return checkNational;
	}

	public void setCheckNational(String checkNational) {
		this.checkNational = checkNational;
	}
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getProgressiveType() {
		return progressiveType;
	}

	public void setProgressiveType(String progressiveType) {
		this.progressiveType = progressiveType;
	}

	public boolean isPresent856() {
		return isPresent856;
	}

	public void setPresent856(boolean isPresent856) {
		this.isPresent856 = isPresent856;
	}

	public String getCharacterSelected() {
		return characterSelected;
	}

	public void setCharacterSelected(String characterSelected) {
		this.characterSelected = characterSelected;
	}

	public String getNewFromDitalButton() {
		return newFromDitalButton;
	}

	public void setNewFromDitalButton(String newFromDitalButton) {
		this.newFromDitalButton = newFromDitalButton;
	}

	public String getNewFromModelButton() {
		return newFromModelButton;
	}

	public void setNewFromModelButton(String newFromModelButton) {
		this.newFromModelButton = newFromModelButton;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getIndexBrowse() {
		return indexBrowse;
	}

	public void setIndexBrowse(String indexBrowse) {
		this.indexBrowse = indexBrowse;
	}

	public short getOptNoteGroup() {
		return optNoteGroup;
	}

	public void setOptNoteGroup(short optNoteGroup) {
		this.optNoteGroup = optNoteGroup;
	}

	public String getTagNumber() {
		return tagNumber;
	}

	public void setTagNumber(String tagNumber) {
		this.tagNumber = tagNumber;
	}

	public char getIndicator1() {
		return indicator1;
	}

	public void setIndicator1(char indicator1) {
		this.indicator1 = indicator1;
	}

	public char getIndicator2() {
		return indicator2;
	}

	public void setIndicator2(char indicator2) {
		this.indicator2 = indicator2;
	}
	
	public String getOptManagerialLevel() {
		return optManagerialLevel;
	}

	public void setOptManagerialLevel(String optManagerialLevel) {
		this.optManagerialLevel = optManagerialLevel;
	}

	private int skipInFiling;
	public int getSkipInFiling() {
		return skipInFiling;
	}

	public void setSkipInFiling(int skipInFiling) {
		logger.debug("skipInFiling : "+skipInFiling);
		this.skipInFiling = skipInFiling;
	}
	/*end inserimento skip in filling nella catalogazione*/

	public Integer getTagIndex() {
		logger.debug("getTagIndex");
		return tagIndex;
	}

	public Integer getBibliographicItemNumber() {
		logger.debug("getBibliographicItemNumber");
		return bibliographicItemNumber;
	}

	public void setBibliographicItemNumber(Integer integer) {
		logger.debug("setBibliographicItemNumber " + integer);
		bibliographicItemNumber = integer;
	}

	public void setTagIndex(Integer integer) {
		tagIndex = integer;
	}

	public String getSubfield(int i) {
		logger.debug("getSubfield" + subfields.get(i));
		return (String)subfields.get(i);
	}

	public void setSubfield(int i, String s) {
		logger.debug("setSubfield[" + i + "] = " + s);
		while (subfields.size() <= i) {
			subfields.add("");
		}
		subfields.set(i, s);
	}

	public String getCode(int i) {
		logger.debug("getCode");
		return (String)codes.get(i);
	}

	public void setCode(int i, String s) {
		logger.debug("setCode[" + i + "] = " + s);
		while (codes.size() <= i) {
			codes.add("");
		}
		codes.set(i, s);
	}

	public String getFixedSubfield(int i) {
		logger.debug("getFixedSubfield");
		return (String)fixedSubfields.get(i);
	}
	
	public void setFixedSubfield(int i, String s) {
		logger.debug("setFixedSubfield[" + i + "] = " + s);
		while (fixedSubfields.size() <= i) {
			fixedSubfields.add("");
		}
		fixedSubfields.set(i, s);
	}
	
	public String getFixedCode(int i) {
		logger.debug("getFixedCode");
		return (String)fixedCodes.get(i);
	}
	
	public void setFixedCode(int i, String s) {
		logger.debug("setFixedCode[" + i + "] = " + s);
		while (fixedCodes.size() <= i) {
			fixedCodes.add("");
		}
		fixedCodes.set(i, s);
	}

	/**
	 * an Array of correlation values
	 * Note that array element 0 is not used.
	 */
	public short getCorrelation(int i) {
		logger.debug("getCorrelation[" + i + "]");
		return values.getValue(i);
	}
	public void setCorrelation(int i, short s) {
		logger.debug("setCorrelation[" + i +
		 "] = " + s);
		values = values.change(i,s);
	}
	public short getCategory() {
		return category;
	}
	public void setCategory(short s) {
		category = s;
	}
	public List getCodes() {
		return codes;
	}
	public List getSubfields() {
		return subfields;
	}
	public void setCodes(List list) {
		codes = list;
	}
	public void setSubfields(List list) {
		subfields = list;
	}
	public List getRelationshipReciprocalList() {
		return RelationshipReciprocalList;
	}
	public void setRelationshipReciprocalList(List list) {
		RelationshipReciprocalList = list;
	}
	public CorrelationValues getCorrelationValues() {
		return values;
	}
	public String getOperation() {
		return operation;
	}
	public String getSubfieldIndex() {
		return subfieldIndex;
	}
	public void setOperation(String string) {
		operation = string;
	}
	public void setSubfieldIndex(String string) {
		subfieldIndex = string;
	}
	public String getNewSubfieldCode() {
		return newSubfieldCode;
	}
	public String getNewSubfieldContent() {
		return newSubfieldContent;
	}
	public void setNewSubfieldCode(String string) {
		newSubfieldCode = string;
	}
	public void setNewSubfieldContent(String string) {
		newSubfieldContent = string;
	}
	public short getReciprocalOption() {
		return reciprocalOption;
	}
	public void setReciprocalOption(short s) {
		reciprocalOption = s;
	}
	public List getFixedCodes() {
		return fixedCodes;
	}
	public List getFixedSubfields() {
		return fixedSubfields;
	}
	public void setFixedCodes(List list) {
		fixedCodes = list;
	}
	public void setFixedSubfields(List list) {
		fixedSubfields = list;
	}
	public String getControlNumberValidationCode() {
		return controlNumberValidationCode;
	}
	public void setControlNumberValidationCode(String string) {
		controlNumberValidationCode = string;
	}
	public String getAddTagFromModel() {
		return addTagFromModel;
	}
	public void setAddTagFromModel(String string) {
		addTagFromModel = string;
	}
	public short getHeadingType() {
		return headingType;
	}
	public void setHeadingType(short s) {
		headingType = s;
	}
	public String getCheckDigital() {
		return checkDigital;	
	}
	public void setCheckDigital(String checkDigital) {
		this.checkDigital = checkDigital;
	}
	public String getOptDigitalLevel() {
		return optDigitalLevel;
	}
	public void setOptDigitalLevel(String optDigitalLevel) {
		this.optDigitalLevel = optDigitalLevel;
	}
	public String getCheckOnline() {
		return checkOnline;
	}
	public void setCheckOnline(String checkOnline) {
		this.checkOnline = checkOnline;
	}
	public String getCheckContinuaz() {
		return checkContinuaz;
	}
	public void setCheckContinuaz(String checkContinuaz) {
		this.checkContinuaz = checkContinuaz;
	}
	public Integer getAmicusNumberMother() {
		return amicusNumberMother;
	}
	public void setAmicusNumberMother(Integer amicusNumberMother) {
		this.amicusNumberMother = amicusNumberMother;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public Integer getAmicusNumberFiglia() {
		return amicusNumberFiglia;
	}
	public void setAmicusNumberFiglia(Integer amicusNumberFiglia) {
		this.amicusNumberFiglia = amicusNumberFiglia;
	}
	public String getDoiText() {
		return doiText;
	}
	public void setDoiText(String doiText) {
		this.doiText = doiText;
	}
	public String getMdrFgl() {
		return mdrFgl;
	}
	public void setMdrFgl(String mdrFgl) {
		this.mdrFgl = mdrFgl;
	}
	public String getTomo() {
		return tomo;
	}
	public void setTomo(String tomo) {
		this.tomo = tomo;
	}
	public String getEdizione() {
		return edizione;
	}
	public void setEdizione(String edizione) {
		this.edizione = edizione;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String[] getCheckDeletedTags() {
		return checkDeletedTags;
	}
	public void setCheckDeletedTags(String[] checkDeletedTags) {
		this.checkDeletedTags = checkDeletedTags;
	}
	public Integer getWemiFirstGroup() {
		return wemiFirstGroup;
	}
	public void setWemiFirstGroup(Integer wemiFirstGroup) {
		this.wemiFirstGroup = wemiFirstGroup;
	}
	
	public String getInformationSource() {
		return informationSource;
	}

	public void setInformationSource(String informationSource) {
		this.informationSource = informationSource;
	}

	public String getLinkText() {
		return linkText;
	}

	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}

	public String getSbnNameVid() {
		return sbnNameVid;
	}

	public void setSbnNameVid(String sbnNameVid) {
		this.sbnNameVid = sbnNameVid;
	}

	public String getTagNum() {
		return tagNum;
	}

	public void setTagNum(String tagNum) {
		this.tagNum = tagNum;
	}
}