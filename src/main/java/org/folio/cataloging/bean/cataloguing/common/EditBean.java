package org.folio.cataloging.bean.cataloguing.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.action.cataloguing.bibliographic.SaveTagException;
import org.folio.cataloging.bean.LibrisuiteBean;
import org.folio.cataloging.bean.cas.CasaliniCodeListsBean;
import org.folio.cataloging.bean.cas.CasaliniContextBean;
import org.folio.cataloging.bean.cataloguing.authority.AuthorityEditBean;
import org.folio.cataloging.bean.cataloguing.bibliographic.BibliographicEditBean;
import org.folio.cataloging.bean.cataloguing.bibliographic.StringTextEditBean;
import org.folio.cataloging.bean.cataloguing.bibliographic.StringTextEditBeanForItemEditing;
import org.folio.cataloging.bean.cataloguing.bibliographic.codelist.CodeListBean;
import org.folio.cataloging.bean.cataloguing.bibliographic.codelist.CodeListsBean;
import org.folio.cataloging.bean.cataloguing.copy.OntologyTypeBean;
import org.folio.cataloging.bean.digital.DigitalAmminBean;
import org.folio.cataloging.bean.digital.DigitalDoiBean;
import org.folio.cataloging.bean.searching.BibliographicTableManager;
import org.folio.cataloging.bean.searching.ICodeTableManager;
import org.folio.cataloging.bean.searching.SearchTypeBean;
import org.folio.cataloging.business.Command;
import org.folio.cataloging.business.authorisation.AuthorisationException;
import org.folio.cataloging.business.cataloguing.bibliographic.*;
import org.folio.cataloging.business.cataloguing.common.*;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.business.common.*;
import org.folio.cataloging.business.common.group.GroupManager;
import org.folio.cataloging.business.controller.UserProfile;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.business.descriptor.SkipInFiling;
import org.folio.cataloging.business.digital.*;
import org.folio.cataloging.dao.*;
import org.folio.cataloging.dao.persistence.*;
import org.folio.cataloging.exception.*;
import org.folio.cataloging.form.cataloguing.bibliographic.EditTagForm;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.util.StringText;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("unchecked")
public abstract class EditBean extends LibrisuiteBean {
	private static List skipInFilingList = new ArrayList();
	protected static DAOCodeTable daoCodeTable = new DAOCodeTable();
	private static final DAOBibliographicCorrelation daoCorrelation = new DAOBibliographicCorrelation();
	private boolean functionsDisabled = Defaults.getBoolean("functions.disabled", false);
	private static final DAOAuthorityCorrelation daoAuthorityCorrelation = new DAOAuthorityCorrelation();
	private static Log logger = LogFactory.getLog(EditBean.class);
	private boolean searchingRelationship = false;
	private boolean cataloguingMode = false;
	private CasaliniContextBean casaliniBean;
	private String optManagerialLevel;
	private short optNoteGroup;
	private String optDigitalLevel = "001";
	private String checkDigital;
	private String checkOnline;
	private String checkContinuaz;
	private GroupManager groupManager;
	private String userName;
	private String digitalOperation;
	private String itemBibliographicLevelCode;
	private Integer defaultModelId;

	/* inizio gestione tag 097 */
	private String hierarchyType;
	private boolean searchingMother = false;
	private String levelForSearchMother = null;
	private Integer amicusNumberMother = null;
	private boolean presentTag097 = false;
	private boolean levelDisable = true;
	private boolean duplicaDisable = true;
	private boolean sendDoi = false;
	private boolean modifyDoi = false;
	private String operation;

	private String mdrFgl = "001";
	private boolean mdrFglDisable = true;
	private boolean addCatalogItem;
	private String checkSkipSearch;

	private String stringTextForFastDigit;
	private boolean fastInsert;
	private boolean fastInsertUser;
	private boolean headingToAdd;
	private boolean autoCompleteActive;
	// Nat: bug 2303
	private boolean internalDoiPermitted = false;
	private List<OntologyTypeBean> ontologyTypeList;

	private String newSubfieldCode;
	private boolean editCopyFastInsert;

	private String lastTermInsert;

	private List levelTypeList;
	private String conversionCategoryAutocomplete;
	private Integer wemiFirstGroup;
	private String  wemiGroupLabel;
	private String frbrBrowsing;
	
	private String verificationLevel;

	private boolean callFromSaveTag = false;

	public boolean isCallFromSaveTag() {
		return callFromSaveTag;
	}

	public void setCallFromSaveTag(boolean callFromSaveTag) {
		this.callFromSaveTag = callFromSaveTag;
	}

	public List<OntologyTypeBean> getOntologyTypeList() {
		return ontologyTypeList;
	}

	public void setOntologyTypeList(List<OntologyTypeBean> ontologyTypeList) {
		this.ontologyTypeList = ontologyTypeList;
	}

	public boolean isHeadingToAdd() {
		return headingToAdd;
	}

	public void setHeadingToAdd(boolean headingToAdd) {
		this.headingToAdd = headingToAdd;
	}

	public boolean isFastInsert() {
		return fastInsert;
	}

	public void setFastInsert(boolean fastInsert) {
		this.fastInsert = fastInsert;
	}

	public boolean getFastInsertUser() {
		return fastInsertUser;
	}

	public void setFastInsertUser(boolean fastInsertUser) {
		this.fastInsertUser = fastInsertUser;
	}

	public String getStringTextForFastDigit() {
		return stringTextForFastDigit;
	}

	public void setStringTextForFastDigit(String stringTextForFastDigit) {
		this.stringTextForFastDigit = stringTextForFastDigit;
	}

	public String getCheckSkipSearch() {
		return checkSkipSearch;
	}

	public void setCheckSkipSearch(String checkSkipSearch) {
		this.checkSkipSearch = checkSkipSearch;
	}

	public static final String defaultLang = Defaults.getString("default.language");
	private static final String DIGITAL_TEXT_RESOURCE = Defaults.getString("digital.text.resource");
	private static final StringBuffer bufferDigital = new StringBuffer().append("http://").append(Defaults.getString("digital.name.host"));

	/* 20100304 flag lingua catalogazione ita */
	private boolean defaultCatLang = false;
	// 20100304 inizio: flag lingua catalogazione ita
	private boolean engCatLang = false;
	// 20100729:
	private String formatRecordType;
	// 20101007 inizio: campo per il max sequence number assegnato al tag082
	private Integer maxSequence;
	// 20101019 inizio: campo per il tipo di progressivo del tag097
	private String progressiveType = "000";
	/* 20110201 inizio: campo per l'anno del tag097 (per i seriali/fascicolo) */
	private String year;

	private String checkNTOCSB;
	private String checkNCORE;

	public void setVerificationLevel(String verificationLevel) {
		this.verificationLevel = verificationLevel;
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

	private String checkNational;

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

	public Integer getMaxSequence() {
		return maxSequence;
	}

	public void setMaxSequence(Integer maxSequence) {
		this.maxSequence = maxSequence;
	}


	public String getFormatRecordType() {
		return formatRecordType;
	}

	public void setFormatRecordType(String formatRecordType) {
		this.formatRecordType = formatRecordType;
	}

	public static String getDefaultLang() {
		return defaultLang;
	}

	public boolean isDefaultCatLang() {
		return defaultCatLang;
	}

	public void setDefaultCatLang(boolean defaultCatLang) {
		this.defaultCatLang = defaultCatLang;
	}

	public boolean isAddCatalogItem() {
		return addCatalogItem;
	}

	public void setAddCatalogItem(boolean addCatalogItem) {
		this.addCatalogItem = addCatalogItem;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public short getOptNoteGroup() {
		return optNoteGroup;
	}

	public void setOptNoteGroup(short optNoteGroup) {
		this.optNoteGroup = optNoteGroup;
	}

	public String getOptManagerialLevel() {
		return optManagerialLevel;
	}

	public void setOptManagerialLevel(String optManagerialLevel) {
		this.optManagerialLevel = optManagerialLevel;
	}

	public CasaliniContextBean getCasaliniBean() {
		return casaliniBean;
	}

	public void setCasaliniBean(CasaliniContextBean casaliniBean) {
		this.casaliniBean = casaliniBean;
	}

	// TODO _MIKE: spostare nell'apposito manager la gestione delle labels e
	// procedere per delega
	// TODO _MIKE: Applicare la cache anche alle short label
	/*
	 * MIKE: aggiunta per velocizzare la visualizzazione del worksheet
	 * rallentato dal recupero delle label
	 */
	private Hashtable labelCache = new Hashtable();

	/* modifica carmen 7/03/2007 */
	private boolean navigation = false; // !isEditing = isNavigation

	private short skipInFiling = '0';

	/*
	 * Natascia 4/7/2007 **Prn 189
	 */
	String arrTags[] = { "130", "100", "110", "111" };

	private String index;

	/**
	 * @return
	 */

	public String getIndex() {
		return index;
	}

	public void setIndex(String string) {
		index = string;
	}

	/**
	 * A string to uniquely distinguish concrete EditBean implementations in
	 * tag-based ActionForwards and jsp's
	 * 
	 * @since 1.0
	 */
	public abstract String getMarcTypeCode();

	/**
	 * This method selects the default if the tag 773 bibliographic level = 'a'
	 * or 'b' when you select the category of relations.
	 * 
	 * @since 1.0
	 */
	public abstract void setRelationForAnalytical();

	/**
	 * \ retrieves the bean from the session if available
	 * 
	 */
	public static EditBean getInstance(HttpServletRequest request) {
		logger.debug("Entering getInstance");
		boolean isBibliographic = SearchTypeBean.getInstance(request)
				.getSearchingView() > 0;
		EditBean bean;
		bean = (EditBean) EditBean.getSessionAttribute(request, EditBean.class);
		if (bean == null) {
			if (isBibliographic) {
				bean = (EditBean) EditBean.getSessionAttribute(request,
						BibliographicEditBean.class);
			} else {
				bean = (EditBean) EditBean.getSessionAttribute(request,
						AuthorityEditBean.class);
			}
			if (bean == null) {
				logger.debug("bean is null");
				if (isBibliographic) {
					logger.debug("doing Bibliographic getInstance");
					return BibliographicEditBean.getInstance(request);
				} else {
					return AuthorityEditBean.getInstance(request);
				}
			}
		}
		bean.setCasaliniBean((CasaliniContextBean) getSessionAttribute(request,
				CasaliniContextBean.class));
		return bean;
	}

	protected CatalogItem catalogItem;

	protected List commandList = new ArrayList();

	/* modifica barbara */
	protected int currentCommand = 4;

	private List firstCorrelationList;

	private static Locale locale = Locale.getDefault();

	protected List secondCorrelationList;

	protected StringTextEditBean stringText;

	protected int tagIndex;

	protected List thirdCorrelationList;
	private String lastBrowsedTerm;

	/**
	 * The current tag is to be replaced by a new occurrence of the given
	 * category
	 * 
	 */
	public void changeCategory(short category) throws NewTagException,
			AuthorisationException, DataAccessException {
		checkPermission(getCurrentTag().getRequiredEditPermission());
		Tag t = getCatalog().getNewTag(getCatalogItem(), category);
		checkPermission(t.getRequiredEditPermission());
		Command c = new ReplaceTagCommand(this, getCurrentTag(), t);
		executeCommand(c);
	}

	/**
	 * The current tag's descriptor is to be replaced by a new occurrence of the
	 * given heading type
	 * 
	 */
	public void changeHeadingType(short headingType)
			throws AuthorisationException, DataAccessException {
		checkPermission(getCurrentTag().getRequiredEditPermission());
		Descriptor oldDescriptor = ((Browsable) getCurrentTag())
				.getDescriptor();
		getCatalog().changeDescriptorType(getCatalogItem(), tagIndex,
				headingType);
		Descriptor newDescriptor = ((Browsable) getCurrentTag())
				.getDescriptor();
		((Browsable) getCurrentTag()).setDescriptor(oldDescriptor);
		Command c = new ReplaceDescriptorCommand(this, getCurrentTag(),
				newDescriptor);
		executeCommand(c);
	}

	/**
	 * The current tag text is to be modified
	 * 
	 */
	public void changeText(List codes, List subfields)
			throws AuthorisationException, DataAccessException {
		changeText(new StringText(codes, subfields));
	}

	/**
	 * The current tag text is to be modified
	 * 
	 */
	public void changeText(StringText s) throws AuthorisationException,
			DataAccessException {
		Command c = new ChangeTextCommand(this, (VariableField) getCurrentTag(), s);
		checkPermission(getCurrentTag().getRequiredEditPermission());
		executeCommand(c);
	}

	public void changeValues(CorrelationValues v)
			throws AuthorisationException, DataAccessException {
		checkPermission(getCurrentTag().getRequiredEditPermission());
		logger.debug("about to change values for " + getCurrentTag() + " to "
				+ v);
		executeCommand(new ChangeValuesCommand(this, v));
		logger.debug("after the change tag is a " + getCurrentTag());
	}

	public void changeValuesIndicator(CorrelationValues v)
			throws AuthorisationException, DataAccessException {
		checkPermission(getCurrentTag().getRequiredEditPermission());
		logger.debug("about to change values for " + getCurrentTag() + " to "
				+ v);
		executeCommand(new ChangeValuesIndicatorCommand(this, v));
		logger.debug("after the change tag is a " + getCurrentTag());
	}

	public void changeValues(int value1, int value2, int value3)
			throws AuthorisationException, DataAccessException {
		changeValues(new CorrelationValues((short) value1, (short) value2,
				(short) value3));
	}

	public void createStringTextEditBean() throws MarcCorrelationException 
	{
		Tag tag = getCurrentTag();
		if (tag instanceof VariableField) {
			try {
				stringText = new StringTextEditBeanForItemEditing((VariableField) tag);
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException("Auto-generated catch block", e);
			}
		}
	}

	public void deleteRecord(final UserProfile user) throws DataAccessException,
			RecordInUseException {
		getCatalog().lock(getCatalogItem().getAmicusNumber().intValue(), getUserName());
		getCatalog().deleteCatalogItem(getCatalogItem(), user);

		new DAOCasCache().deleteCasCache(getCatalogItem().getAmicusNumber().intValue());
		getCatalog().unlock(getCatalogItem().getAmicusNumber().intValue());
	}

	/**
	 * The current tag is to be deleted from the Item
	 * 
	 */
	public void deleteTag() throws AuthorisationException, DataAccessException {
		checkPermission(getCurrentTag().getRequiredEditPermission());
		Command c = new DeleteTagCommand(this);

		executeCommand(c);
	}

	public void executeCommand(Command c) throws DataAccessException {
		c.execute();
		while (commandList.size() > currentCommand) {
			commandList.remove(commandList.size() - 1);
		}
		commandList.add(c);
		currentCommand++;
		createStringTextEditBean();
	}

	abstract public Catalog getCatalog();

	public CatalogItem getCatalogItem() {
		return catalogItem;
	}

	/**
	 * 
	 */
	protected List getCommandList() {
		return commandList;
	}
	
	/**
	 * check duplicate the tags 1XX in authority record to be deleted
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 * @throws ValidationException
	 */
	public abstract boolean checkTags() throws DataAccessException,
            ValidationException ;



	abstract public Set getControlNumberValidationOptions();

	/**
	 * 
	 */
	protected int getCurrentCommand() {
		return currentCommand;
	}

	/**
	 * @return the currently selected tag from the item
	 * @since 1.0
	 */
	public Tag getCurrentTag() {
		try {
			getCatalogItem().getTag(getTagIndex());
		} catch (Exception e) {
			return null;
		}
		return getCatalogItem().getTag(getTagIndex());
	}

	/**
	 * 
	 */
	public List getFirstCorrelationList() {
		return firstCorrelationList;
	}

	public Locale getLocale() {
		return locale;
	}
	/**
	 * 
	 */
	public List getSecondCorrelationList() {
		return secondCorrelationList;
	}

	public StringTextEditBean getStringText() {
		return stringText;
	}

	abstract public List getTagCategories();

	abstract public int getCountTagsEqual();

	public int getTagIndex() {
		return tagIndex;
	}

	public List getThirdCorrelationList() {
		return thirdCorrelationList;
	}

	public char getVerificationLevel() {
		return getCatalogItem().getItemEntity().getVerificationLevel();
	}

	public List getVerificationLevelList() {
		return CodeListsBean.getVerificationLevel().getCodeList(locale);
	}

	public String getVerificationLevelText() {
		return Avp.decode(String.valueOf(getCatalogItem()
				.getItemEntity().getVerificationLevel()),
				getVerificationLevelList());
	}

	/**
	 * Does one-time initialising of the bean
	 * 
	 */
	public void init(Locale l) {
		/* barbara modifica 15/05/2007 skip in filing */
		try {
			onPopulateLists(daoCodeTable, locale);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Auto-generated catch block", e);
		}
	}

	public boolean isAbleToBeAdded() {
		try {
			checkPermission(getCurrentTag().getRequiredEditPermission());
			return true;
		} catch (AuthorisationException e) {
			return false;
		}
	}

	public boolean isAbleToBeDeleted() {
		try {
			checkPermission(getCurrentTag().getRequiredEditPermission());
			return getCatalogItem().getNumberOfTags() > 1;
		} catch (AuthorisationException e) {
			return false;
		}
	}

	public boolean isBrowsable() {
		try {
			checkPermission(getCurrentTag().getRequiredEditPermission());
			return getCurrentTag().isBrowsable();
		} catch (AuthorisationException e) {
			return false;
		}
	}

	public boolean isCanRedo() {
		return currentCommand < commandList.size();
	}

	public boolean isCanUndo() {
		return currentCommand > 0;
	}

	public boolean isEditableHeader() {
		try {
			checkPermission(getCurrentTag().getRequiredEditPermission());
			return getCurrentTag().isEditableHeader();
		} catch (AuthorisationException e) {
			return false;
		}
	}

	/**
	 * This is used in the jsp to determine if the EditSubfieldW button should
	 * be displayed
	 */
	public boolean isHasSubfieldW() {
		try {
			checkPermission(getCurrentTag().getRequiredEditPermission());
			return getCurrentTag().isHasSubfieldW();
		} catch (AuthorisationException e) {
			return false;
		}
	}
	
	/**
	 * This is used in the jsp to determine if the EditEquivSubfieldW button
	 * should be displayed
	 */
	public boolean isEquivalenceReference() {
		try {
			checkPermission(getCurrentTag().getRequiredEditPermission());
			return getCurrentTag().isEquivalenceReference();
		} catch (AuthorisationException e) {
			return false;
		}
	}

	/**
	 * This is used in the jsp to determine if the EditPublisher button should
	 * be displayed
	 */
	public boolean isPublisherEditable() {
		try {
			checkPermission(getCurrentTag().getRequiredEditPermission());
			return getCurrentTag().isPublisher();
		} catch (AuthorisationException e) {
			return false;
		}
	}

	public abstract void loadItem(Object[] key)
			throws DataAccessException,
			RecordInUseException;

	public abstract void loadItemDuplicate(Object[] key)
			throws DataAccessException,
			RecordInUseException;

	/**
	 * Creates a new tag by category (no correlation information passed)
	 * @param tagIndex
	 * @param category
	 * @return
	 * @throws NewTagException
	 * @throws AuthorisationException
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 */
	public Tag newTag(int tagIndex, short category) throws NewTagException,
			AuthorisationException, DataAccessException {
		return newTag(tagIndex, category, null);
	}

	/**
	 * A new tag of the given category is to be added with the given
	 * correlation values
	 * 
	 */
	public Tag newTag(int tagIndex, short category, CorrelationValues v)
			throws NewTagException, AuthorisationException,
			DataAccessException {
		Tag t = getCatalog().getNewTag(getCatalogItem(), category, v);
		checkPermission(t.getRequiredEditPermission());
		Command c = new NewTagCommand(this, tagIndex, t);

		executeCommand(c);
		return t;
	}


	public void newTagFromModel(int unusedTagIndex) throws NewTagException,
			AuthorisationException, DataAccessException {
		Tag t = getCatalog().parseModelXmlElement(
				getCatalogItem().getModelItem().getUnusedTagXmlFieldForAdding(
						unusedTagIndex), getCatalogItem());
		checkPermission(t.getRequiredEditPermission());
		Command c = new NewTagCommand(this, tagIndex, t);

		executeCommand(c);
	}

	public void redoCommand() throws DataAccessException {
		if (currentCommand < commandList.size()) {
			Command c = (Command) commandList.get(currentCommand);
			c.reExecute();
			currentCommand++;
			createStringTextEditBean();
		}
	}

	/**
	 * Empty Factory Method
	 * @return
	 * @throws DataAccessException 
	 */
	public abstract List getFirstList() throws DataAccessException;

	/**
	 * resets the values in the displayed correlation lists ensuring that
	 * possible choices are still valid MARC encodings
	 * 
	 */
	public void refreshCorrelation(short value1, short value2, Locale l) throws DataAccessException 
	{
		logger.debug("refreshing correlation");
		logger.debug("value1 is " + value1);
		logger.debug("value2 is " + value2);
		List firstList = null;

		if (this.getOptNoteGroup() != 0 && getCurrentTag().getCategory() == 7)
			firstList = getFirstList();
		else
			firstList = getCurrentTag().getFirstCorrelationList();

		setFirstCorrelationList(DAOCodeTable.asOptionList(firstList, l));

		if ((value1 == -1) && (firstList != null)) {
			value1 = ((T_SINGLE) firstList.get(0)).getCode();
		}
		List secondList = getCurrentTag().getSecondCorrelationList(value1);
		setSecondCorrelationList(DAOCodeTable.asOptionList(secondList, l));

		if ((value2 == -1) && (secondList != null)) {
			value2 = ((T_SINGLE) secondList.get(0)).getCode();
		}
		List thirdList = getCurrentTag()
				.getThirdCorrelationList(value1, value2);
		setThirdCorrelationList(DAOCodeTable.asOptionList(thirdList, l));

	}

	/*------------------
	 * LABEL MANAGING
	 *------------------*/
	public String getShortLabel(int tagNum) throws DataAccessException {
		return loadShortLabel(catalogItem.getTag(tagNum));
	}

	public String getUnusedShortLabel(int tagNum) throws DataAccessException {
		return loadShortLabel((Tag) catalogItem.getModelItem()
				.getUnusedModelTags().get(tagNum));
	}

	public String getCurrentShortLabel() throws DataAccessException {
		return loadShortLabel(getCurrentTag());
	}

	public String getLongLabel(int tagNum) throws DataAccessException {
//		return loadLongLabel(catalogItem.getTag(tagNum));
		return loadShortLabelForScheda(catalogItem.getTag(tagNum));
	}

	public String getUnusedLongLabel(int tagNum) throws DataAccessException {
//		return loadLongLabel((Tag) catalogItem.getModelItem().getUnusedModelTags().get(tagNum));
		return loadShortLabelForScheda((Tag) catalogItem.getModelItem().getUnusedModelTags().get(tagNum));
	}

	public String getCurrentLongLabel() throws DataAccessException {
		return loadLongLabel(getCurrentTag());
	}

	public String getShortLabelWithoutTag(int tagNum) throws DataAccessException {
		T_SINGLE ct = loadSelectedCodeTable(catalogItem.getTag(tagNum));
		return  ct != null ? ct.getShortText() : "";
	}
	
	/**
	 * @param processingTag
	 * @return
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 */
	private String loadShortLabel(Tag processingTag) throws DataAccessException {
		T_SINGLE ct =  loadSelectedCodeTable(processingTag);
		String marcTag = processingTag.getMarcEncoding().getMarcTag();
		if (ct == null) {
			return marcTag;
		}
		return buildLabel(marcTag, ct.getShortText(), processingTag.getMarcEncoding().getMarcTagCategoryCode());
	}

	/**
	 * @param processingTag
	 * @return
	 * @throws DataAccessException
	 */
	private T_SINGLE loadSelectedCodeTable(Tag processingTag)
			throws DataAccessException {
		short value1 = processingTag.getCorrelation(1);

		List firstList = processingTag.getFirstCorrelationList();

		T_SINGLE ct = DAOCodeTable.getSelectedCodeTable(firstList, getLocale(),
				value1);
		return ct;
	}

	/**
	 * @param processingTag
	 * @return
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 */
	private String loadLongLabel(Tag processingTag) throws DataAccessException {
		if (isLabelCached(processingTag)) {
			return getCachedLabel(processingTag);
		}
		CodeTable ct = loadSelectedCodeTable(processingTag);
		String marcTag = processingTag.getMarcEncoding().getMarcTag();
		if (ct == null) {
			return marcTag;
		}
		String label = buildLabel(marcTag, ct.getLongText(), processingTag.getMarcEncoding().getMarcTagCategoryCode());
		cacheLabel(processingTag, label);
		return label;
	}
	
	/* Bug 4284 */
	private String loadShortLabelForScheda(Tag processingTag) throws DataAccessException {
		if (isLabelCached(processingTag)) {
			return getCachedLabel(processingTag);
		}
		CodeTable ct = loadSelectedCodeTable(processingTag);
		String marcTag = processingTag.getMarcEncoding().getMarcTag();
		String marcInd1 = String.valueOf(processingTag.getMarcEncoding().getMarcFirstIndicator());
		String marcInd2 = String.valueOf(processingTag.getMarcEncoding().getMarcSecondIndicator());
		if (ct == null) {
			return marcTag;
		}
		String label = buildLabelForScheda(marcTag, marcInd1, marcInd2, ct.getShortText(), processingTag.getMarcEncoding().getMarcTagCategoryCode());
		cacheLabel(processingTag, label);
		return label;
	}
	
	/* Bug 4284 */
	private String buildLabelForScheda(String marcNumber, String marcInd1, String marcInd2, String textLabel, short category) 
	{
		StringBuilder builder = new StringBuilder();
//		if (!textLabel.startsWith("00") && category != 7) {
		if (!textLabel.startsWith("00")) {
			builder.append(marcNumber).append(" ").append(marcInd1).append(marcInd2).append(" ").append("-").append(textLabel);
		} else {
			builder.append(textLabel);
		}
		return builder.toString();
	}

	private String getLabelKey(Tag processingTag) 
	{
		return "" + processingTag.getCategory() + "," + processingTag.getCorrelationValues().toString();
	}

	private void cacheLabel(Tag processingTag, String label) {
		labelCache.put(getLabelKey(processingTag), label);
	}

	private String getCachedLabel(Tag processingTag) {
		return (String) labelCache.get(getLabelKey(processingTag));
	}

	private boolean isLabelCached(Tag processingTag) {
		return labelCache.containsKey(getLabelKey(processingTag));
	}

	/**
	 * Build fixed tags with marc tag number if needed
	 * 
	 * @param marcNumber
	 * @param textLabel
	 * @return
	 */
	private String buildLabel(String marcNumber, String textLabel, short category) 
	{
		String result = textLabel;
		if (!textLabel.startsWith("00") && category != 7) {
			result = marcNumber + "-" + textLabel;
		}
		return result;
	}

	/*------------------
	 * END LABEL MANAGING
	 *------------------*/

	/**
	 * The current tag is to be replaced by the given tag
	 * 
	 */
	public void replaceCurrentTag(Tag t) throws AuthorisationException,
			DataAccessException {
		checkPermission(getCurrentTag().getRequiredEditPermission());
		Command c = new ReplaceTagCommand(this, getCurrentTag(), t);
		executeCommand(c);
	}

		// ----> Anche per gli altri clienti deve scrivere la S_CAS_CACHE e
		// scrivere la tabella CAS_FILES e CAS_DIG_FILES
	public void saveRecord() throws DataAccessException,
			AuthorisationException,
            ValidationException {
		getCatalog().getCatalogDao()
		.setCasCache(casaliniBean.getCasCache());
		if (casaliniBean.isEnabled()) {
		


		// --------> Tag 097 cancellati
		Tag aTag = null;
		
		Iterator iter = getCatalogItem().getDeletedTags().iterator();
		while (iter.hasNext()) {
			aTag = (Tag) iter.next();
			if (aTag instanceof ControlNumberAccessPoint) {
				if (aTag.getMarcEncoding().getMarcTag().equalsIgnoreCase("097")) {
					if (aTag.getCorrelation(1) == 69)
						// deleteDigitalHierarchy(getCatalogItem().getAmicusNumber().intValue());
						deleteDigitalHierarchy((ControlNumberAccessPoint) aTag);
					else {
						ControlNumberAccessPoint tag = (ControlNumberAccessPoint) aTag;
						deleteHierarchy(tag);
					}
				}
			}
		}
		
		// --------> Tag 097 inseriti
		List tags097 = get097Tags();
		for (int i = 0; i < tags097.size(); i++) {
			ControlNumberAccessPoint tag097 = (ControlNumberAccessPoint) tags097
					.get(i);
			if (tag097 != null && tag097.isNew()) {
				if (tag097.getCorrelation(1) == 69) {
					saveDigitalHierarchy(tag097);
				} else {
					saveHierarchy(tag097);
					cntrTag098(tag097);
				}
			}
		}
	   }
   	
		// ----> 20101007 inizio: tag082 attribuzione sequence number
		List tags082 = get082_084Tags("082");
		int sequenceNumber = getMaxSequence().intValue();
		for (int i = 0; i < tags082.size(); i++) {
			ClassificationAccessPoint tag082 = (ClassificationAccessPoint) tags082
					.get(i);
			if (tag082 != null) {
				sequenceNumber = sequenceNumber + 1;
				try {
					tag082.setSequenceNumber(new Integer(
							sequenceNumber));
				} catch (NumberFormatException e) {
					tag082.setSequenceNumber(null);
				}
				tag082.markChanged();
			}
		}
		// ----> 20101007 fine

		// ----> 20101027 inizio: tag084 attribuzione sequence number
		List tags084 = get082_084Tags("084");
		sequenceNumber = getMaxSequence().intValue();
		for (int i = 0; i < tags084.size(); i++) {
			ClassificationAccessPoint tag084 = (ClassificationAccessPoint) tags084
					.get(i);
			if (tag084 != null) {
				sequenceNumber = sequenceNumber + 1;
				try {
					tag084.setSequenceNumber(new Integer(
							sequenceNumber));
				} catch (NumberFormatException e) {
					tag084.setSequenceNumber(null);
				}
				tag084.markChanged();
			}
		}
		// ----> 20101007 fine

		// --------> 20110214 inizio: controllo tag 982 e 082
		List tags982List = get982Tags();
		List tags082List = get082Tags();
		Iterator it982 = null;
		try {
			it982 = tags982List.iterator();
		} catch (Exception e) {
		}
		ClassificationAccessPoint tag982 = null;
		String deweyCode = null;
		try{
		while (it982.hasNext()) {
			Tag tag = (Tag) it982.next();
			tag982 = (ClassificationAccessPoint) tag;
			if (tag982.getStringText().getSubfieldsWithCodes("a")
					.getDisplayText().trim().length() > 0) {
				deweyCode = tag982.getStringText().getSubfieldsWithCodes("a")
						.getDisplayText().trim();
				if (!exist082WithDewey(tags082List, deweyCode)) {
					int t = getCatalogItem().getTags().indexOf(tag982);
					setTagIndex(t);
					deleteTag();
					setTagIndex(getCatalogItem().getNumberOfTags() - 1);
					getCatalogItem().sortTags();
					refreshCorrelation(getCurrentTag().getCorrelation(1),
							getCurrentTag().getCorrelation(2), getLocale());
					setCurrentCommand(0);
					setNavigation(true);
				}
			}
		}
		}catch (Exception e) {
		}
		getCatalog().saveCatalogItem(getCatalogItem());

		}
	
	
	// ----> Anche per gli altri clienti deve scrivere la S_CAS_CACHE e
	// scrivere la tabella CAS_FILES e CAS_DIG_FILES
public void saveAuthorityRecord() throws DataAccessException,
		AuthorisationException,
        ValidationException {
	getCatalog().getCatalogDao()
	.setCasCache(casaliniBean.getCasCache());




	getCatalog().saveCatalogItem(getCatalogItem());

	}

	public List get097Tags() throws DataAccessException {
		List tags = getCatalogItem().getTags();
		List tags097 = new ArrayList();
		Iterator it = tags.iterator();
		while (it.hasNext()) {
			Tag tag = (Tag) it.next();
			if (tag instanceof ControlNumberAccessPoint) {
				if (tag.getMarcEncoding().getMarcTag().equalsIgnoreCase("097"))
					tags097.add(tag);
			}
		}
		return tags097;
	}

	public List get098Tags() throws DataAccessException {
		List tags = getCatalogItem().getTags();
		List tags098 = new ArrayList();
		Iterator it = tags.iterator();
		while (it.hasNext()) {
			Tag tag = (Tag) it.next();
			if (tag instanceof ControlNumberAccessPoint) {
				if (tag.getMarcEncoding().getMarcTag().equalsIgnoreCase("098"))
					tags098.add(tag);
			}
		}
		return tags098;
	}

	// 20101007 inizio: recupero i tag082
	public List get082_084Tags(String tagToSearch) throws DataAccessException {
		List tagsToReturn = new ArrayList();
		if ("082".equals(tagToSearch) || "084".equals(tagToSearch)) {
			List tags = getCatalogItem().getTags();
			int maxSequence = 0;
			setMaxSequence(new Integer(0));
			Iterator it = tags.iterator();
			ClassificationAccessPoint tagCurr = null;
			while (it.hasNext()) {
				Tag tag = (Tag) it.next();
				if (tag instanceof ClassificationAccessPoint) {
					if (tag.getMarcEncoding().getMarcTag().equalsIgnoreCase(
							tagToSearch)) {
						tagCurr = (ClassificationAccessPoint) tag;
						if (tagCurr.getSequenceNumber() == null) {
							tagsToReturn.add(tag);
						} else {
							if (tagCurr.getSequenceNumber().intValue() > maxSequence) {
								maxSequence = tagCurr.getSequenceNumber()
										.intValue();
							}
						}
					}
				}
			}
			setMaxSequence(new Integer(maxSequence));
		}
		return tagsToReturn;
	}

	// 20101007 fine

	public void setCatalogItem(CatalogItem item) {
		catalogItem = item;
	}

	/**
	 * 
	 */
	public void setCommandList(List list) {
		commandList = list;
	}

	/**
	 * 
	 */
	public void setCurrentCommand(int i) {
		currentCommand = i;
	}

	/**
	 * 
	 */
	public void setFirstCorrelationList(List list) {
		firstCorrelationList = list;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setLocale(Locale locale) {
		EditBean.locale = locale;
	}

	/**
	 * 
	 */
	public void setSecondCorrelationList(List list) {
		secondCorrelationList = list;
	}

	/**
	 * @param bean
	 */
	public void setStringText(StringTextEditBean bean) {
		stringText = bean;
	}

	public void setTagIndex(int i) {
		tagIndex = i;
	}

	/**
	 * 
	 */
	public void setThirdCorrelationList(List list) {
		thirdCorrelationList = list;
	}

	public void undoCommand() throws DataAccessException {
		if (currentCommand > 0) {
			currentCommand--;
			Command c = (Command) commandList.get(currentCommand);
			c.unExecute();
			createStringTextEditBean();
		}
	}

	/**
	 * Replaces the current tag's descriptor with one selected from a browse
	 * list
	 * 
	 */
	public void updateDescriptorFromBrowse(Descriptor d)
			throws AuthorisationException, DataAccessException {
		checkPermission(getCurrentTag().getRequiredEditPermission());
		/*
		 * Browse may return with a heading from a different view 
		 * (if searching in ANY view).  So, we make sure that we get
		 * the heading in the user's cataloguing view for this situation.
		 */
		d = ((DAODescriptor) d.getDAO()).findOrCreateMyView(d
				.getHeadingNumber(), d.getUserViewString(), getCatalogItem()
				.getUserView());
		logger.debug("descriptor from find or create has view: " + d.getUserViewString());
		Command c = new ReplaceDescriptorCommand(this, getCurrentTag(), d);

		executeCommand(c);
	}

	public void validateCurrentTag() throws DataAccessException,
            ValidationException {
		getCurrentTag().validate(getTagIndex());
		getCatalogItem().checkRepeatability(getTagIndex());
		// Natascia 4/7/2007 **prn 189
		checkParticularTag(getTagIndex());

	}

	public void validateCurrentTagHeading() throws DataAccessException,
            ValidationException {
		getCurrentTag().validate(getTagIndex());
		// Natascia 4/7/2007 **prn 189
		checkParticularTag(getTagIndex());

	}

	/*
	 * Natascia 4/7/2007 **prn189 se il tag corrente e' un tag particolare, deve
	 * escludere la presenza di altri tag particolari (es. tag 130 esclude la
	 * presenza di 100/110/111)
	 */
	public void checkParticularTag(int index) throws DataAccessException,
            DuplicateTagException {
		Tag t = getCatalogItem().getTag(index);
		if (isPresentInArrTags(t.getMarcEncoding().getMarcTag())) {
			if (!isAllowedTag(t.getMarcEncoding().getMarcTag()))
				throw new DuplicateTagException();
		}
	}

	/*
	 * Natascia 4/7/2007 **prn 189
	 */
	private boolean isPresentInArrTags(String strTag) {

		for (int i = 0; i < arrTags.length; i++) {
			if (strTag.equals(arrTags[i]))
				return true;
		}
		return false;
	}

	/*
	 * Natascia 4/7/2007 **prn 189
	 */
	private boolean isAllowedTag(String strTag) {

		for (int i = 0; i < arrTags.length; i++) {
			if (!strTag.equals(arrTags[i])) {
				Tag t = getCatalogItem().findFirstTagByNumber(arrTags[i]);
				if (t != null)
					return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setVerificationLevel(char verificationLevel) {
		getCatalogItem().setVerificationLevel(verificationLevel);
	}

	/**
	 * Replaces the current fixed field tag with a new one
	 * 
	 */
	public void changeFixedFieldValues(FixedFieldUsingItemEntity ff)
			throws AuthorisationException, DataAccessException {
		checkPermission("editHeader");
		Command c = new ChangeFixedFieldCommand(this, ff);

		executeCommand(c);
	}

	public void newItem(CatalogItem item, Locale locale)
			throws DataAccessException {
		setCatalogItem(item);
		setCommandList(new ArrayList());
		resetCommands();
		setTagIndex(0);
		refreshCorrelation(
		/*
		 * getCurrentTag().getCorrelation(1), getCurrentTag().getCorrelation(2),
		 */
		/* modifica barbara */
		item.getTag(0).getCorrelation(1), item.getTag(0).getCorrelation(2),
				locale);
		createStringTextEditBean();
	}

	public Model newModel() {
		return getCatalog().newModel(getCatalogItem());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isSearchingRelationship() {
		return searchingRelationship;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setSearchingRelationship(boolean b) {
		searchingRelationship = b;
	}

	/* modifica carmen */
	public boolean isNavigation() {
		return navigation;
	}

	public void setNavigation(boolean navigationMode) {
		this.navigation = navigationMode;
	}

	/* modifica barbara 15/05/2007 skip in filing */
	final public void populateLists(DAOCodeTable dao, Locale l)
			throws DataAccessException {
		onPopulateLists(dao, l);
	}

	protected static void onPopulateLists(DAOCodeTable dao, Locale l)
			throws DataAccessException {
		List list = dao.getOptionList(T_SKP_IN_FLNG_CNT.class, l);
		setSkipInFilingLista(list);
	}

	public List getSkipInFilingList() {
		return skipInFilingList;
	}

	public static void setSkipInFilingLista(List list) {
		skipInFilingList = list;
	}

	public void setSkipInFilingList(List list) {
		skipInFilingList = list;
	}

	public short getSkipInFiling() {
		return skipInFiling;
	}

	public short getSkipInFilingHeading() {
		/*
		 * Natascia 2/07/2007 ***PRN 224 non visualizzava correttamente lo
		 * skipInFiling perche' il valore corretto e' quello della heading che
		 * e' presente nel descrittore Mike 20/02/2008: questo Ã¨ giusto, ma
		 * solo se il descrittore non sia quello di dummy appena creato e non
		 * letto dalla base dati
		 */
		Descriptor d = ((Browsable) getCurrentTag()).getDescriptor();
		if (d instanceof TTL_HDG && !d.isNew())
			return d.getSkipInFiling();
		else
			return skipInFiling;
	}

	public void setSkipInFiling(short skipInFiling) {
		this.skipInFiling = skipInFiling;
	}

	/**
	 * TODO _MIKE: promote to BibliographicEditBean
	 * 
	 * @throws NoHeadingSetException
	 */
	public void checkHeading() throws NoHeadingSetException {
		if (isBrowsable() && isNoHeadingSet()) {
			resetCommands();
			throw new NoHeadingSetException(
					"error.cataloguing.common.NoHeadingSetException");
		}
	}

	/**
	 * 
	 */
	public void resetCommands() {
		setCurrentCommand(0);
	}

	/**
	 * TODO _MIKE: promote to BibliographicEditBean
	 * 
	 * @return
	 */
	public boolean isNoHeadingSet() {
	
		
		if(getCurrentTag() instanceof VariableField){
		/* Modificato da Carmen */
		return /*
				 * tagForm.getFixedSubfield(0).equals("") || 
				 * 
				 */
		((VariableField) getCurrentTag()).getStringText().toDisplayString()
				.equals("");
		}
		else if(getCurrentTag() instanceof Browsable ){
			/* Modificato da Carmen */
			return /*
					 * tagForm.getFixedSubfield(0).equals("") || 
					 * 
					 */
			((Browsable) getCurrentTag()).getHeadingNumber() == null;
			}
			
		
		return false;
	}

	/**
	 * TODO _MIKE: promote to BibliographicEditBean
	 * 
	 * @param locale
	 * @throws DataAccessException
	 */
	public void sortTags(Locale locale) throws DataAccessException {
		setTagIndex(getCatalogItem().getNumberOfTags() - 1);
		getCatalogItem().sortTags();
		refreshCorrelation(getCurrentTag().getCorrelation(1), getCurrentTag()
				.getCorrelation(2), locale);
	}

	public void setPickedHdgOnLastTermInsert() {
		setLastTermInsert(getBrowsableFixedSubfield());
	}

	public void setLastTermInsertedInEdit(EditTagForm editForm) {
		int subfieldCount = editForm.getSubfields().size();
		int fixedSubfieldCount = editForm.getFixedSubfields().size();

		boolean isHeadingSet = false;

		if (getCurrentTag() instanceof VariableField) {

			isHeadingSet = isNoHeadingSet();
		}

		if (fixedSubfieldCount > 0 && isHeadingSet) {
			if (editForm.getFixedSubfield(0) != null
					&& !editForm.getFixedSubfield(0).equals("")) {
				if (getBrowsableFixedSubfield() != null
						&& !getBrowsableFixedSubfield().equals("")) {
					setLastTermInsert(getBrowsableFixedSubfield());
				} else {
					setLastTermInsert(editForm.getFixedSubfield(0));
				}

			} else
				setLastTermInsert("");

			return;
		}

		if (subfieldCount > 0 && isNoHeadingSet()) {
			if (editForm.getSubfield(0) != null
					&& !editForm.getSubfield(0).equals("")) {
				if (getBrowsableSubfield() != null
						&& !getBrowsableSubfield().equals("")) {
					setLastTermInsert(getBrowsableSubfield());
				} else {
					setLastTermInsert(editForm.getSubfield(0));
				}
			} else {
				setLastTermInsert("");
			}
			return;
		}

		if (getStringText() != null) {

			if (getStringText().getFixedSubfields().getSubfieldList() != null
					&& getStringText().getFixedSubfields().getSubfieldList()
							.size() > 0)
				setLastTermInsert(getStringText().getFixedSubfields()
						.getSubfield(0).getContent());
			else {
				if (getStringText().getEditableSubfields().getSubfieldList() != null
						&& getStringText().getEditableSubfields()
								.getSubfieldList().size() > 0) {
					setLastTermInsert(getStringText().getEditableSubfields()
							.getSubfield(0).getContent());
				} else {
					setLastTermInsert("");
				}
			}
		} else {
			setLastTermInsert("");
		}
	}

	private String getBrowsableSubfield(StringText st) {
		String subfieldContent = "";
		try {
			subfieldContent = st.getSubfield(0).getContent();
		} catch (RuntimeException e) {
			// do nothing
		}
		if (getCurrentTag().isBrowsable()) {
			if (subfieldContent.trim().length() == 0) {
				return lastBrowsedTerm;
			}
		}
		return subfieldContent;
	}

	public String getBrowsableSubfield() {
		return getBrowsableSubfield(getStringText().getEditableSubfields());
	}

	public String getBrowsableFixedSubfield() {
		return getBrowsableSubfield(getStringText().getFixedSubfields());
	}

	public String getLastBrowsedTerm() {
		return lastBrowsedTerm;
	}

	public void setLastBrowsedTerm(String lastBrowsedTerm) {
		this.lastBrowsedTerm = lastBrowsedTerm;
	}

	/*
	 * TODO MIKE: riportare in MADES le implementazioni dei seguenti metodi
	 * astratti:
	 */
	public abstract boolean isFixedField() throws DataAccessException;

	public abstract boolean isAbleDeleteButton() throws DataAccessException;

	public abstract boolean isAbleNew991Button() throws DataAccessException;

	public abstract boolean isAbleSubdivision99X() throws DataAccessException;

	public abstract boolean isAbleDigital() throws DataAccessException;

	public abstract boolean isAbleEquivalentNote();

	/**
	 * aggiornamento tag 005 in duplica record
	 * 
	 * @author Carmen 23/11/2007
	 */
	public abstract void updateT005DateOfLastTransaction();

	/**
	 * aggiornamento tag 008 nel modello posizione 00-05
	 * 
	 * @author Carmen 23/11/2007
	 */
	public abstract void updateT008EnteredOnFileDate();

	/**
	 * restituisce true se nel tag Ã¨ presente lo skipInFiling
	 * 
	 * @author Carmen 20/11/2008
	 */
	public abstract boolean isSkipFiling() throws DataAccessException;

	/*
	 * no Authority
	 */
	public abstract void changeReciprocalOption(short reciprocalOption)
			throws MarcCorrelationException;

	public boolean isCataloguingMode() {
		return cataloguingMode;
	}

	public void setCataloguingMode(boolean cataloguingMode) {
		this.cataloguingMode = cataloguingMode;
	}

	public void checkEmptyTags() throws SaveTagException, DataAccessException {
		Iterator itTags = getCatalogItem().getTags().iterator();
		while (itTags.hasNext()) {
			Tag tag = (Tag) itTags.next();
			if (!tag.isNew() && !tag.isChanged())
				continue; // with the next
			if (tag instanceof VariableField) {
				if (tag instanceof BibliographicNoteTag) {
					if (((BibliographicNoteTag) tag).isStandardNoteType())
						continue;
					else if (((VariableField) tag).isEmpty()) {
						logger.error("REMOVING EMPTY TAG "
								+ tag.getClass().getName() + " st:"
								+ ((VariableField) tag).getStringText());
						throw new SaveTagException(tag.getMarcEncoding()
								.getMarcTag());
					}
				} else if (((VariableField) tag).isEmpty()) {
					logger.error("REMOVING EMPTY TAG "
							+ tag.getClass().getName() + " st:"
							+ ((VariableField) tag).getStringText());
					throw new SaveTagException(tag.getMarcEncoding()
							.getMarcTag());
				}
			}

		}
		// sortTags(locale);

	}

	/**
	 * @param tag
	 */
	public void changeCodeValidationISBN(Tag tag) {
		if (tag instanceof ControlNumberAccessPoint) {
			CNTL_NBR descr = (CNTL_NBR) ((Browsable) tag).getDescriptor();
			if (descr.getTypeCode() == 9) {
				ControlNumberAccessPoint tag1 = (ControlNumberAccessPoint) getCurrentTag();
				StringText s = tag1.getStringText();
				if (s.toString().indexOf("*") != -1) {
					tag1.setValidationCode('z');
					tag1.markChanged();
				} else {
					tag1.setValidationCode('a');
					tag1.markChanged();
				}
			}
		}
	}

	public void checkSubfieldsEmpty(StringText text) throws SaveTagException {
		List subfield = text.getSubfieldList();
		Iterator iter = subfield.iterator();
		while (iter.hasNext()) {
			Subfield sub = (Subfield) iter.next();
			if (sub.isEmpty()) {
				throw new SaveTagException(
						"error.cataloguing.bibliographic.subfiledEmpty.save");
			}
		}

	}
	

	public abstract void checkSubfieldListEmpty(StringText text)
			throws SaveTagException;




	/**
	 * override for mades
	 * 
	 * @return
	 */
	public ICodeTableManager getCodeTableManager() {
		return new BibliographicTableManager();
	}

	public abstract void crea991(HttpServletRequest request, Tag rootTag)
			throws DataAccessException, NewTagException,
			AuthorisationException, ValidationException;
	
	public abstract void saveTag856(HttpServletRequest request, String result,
			EditBean bean, String tagSave) throws DataAccessException,
			AuthorisationException,
            ValidationException, RecordInUseException;


	public abstract void createTag982(HttpServletRequest request, Tag rootTag)
			throws DataAccessException, NewTagException,
			AuthorisationException, ValidationException;


	public abstract void createEquivalentTag792(int amicuNumber)
			throws DataAccessException,
			RecordInUseException, NewTagException, AuthorisationException,
			ValidationException;

	public abstract void create997UserProfile(HttpServletRequest request)
			throws DataAccessException, NewTagException,
			AuthorisationException, ValidationException;

	public abstract void createTag092() throws
            NewTagException, AuthorisationException, DataAccessException,
			ValidationException;

	public abstract boolean isEquivalentEnabled();

	public abstract void create032TagLibricat(HttpServletRequest request)
			throws DataAccessException, NewTagException,
			AuthorisationException, ValidationException;

	public abstract void modify997UserProfile(HttpServletRequest request)
			throws DataAccessException, NewTagException,
			AuthorisationException, ValidationException;

	public abstract void modify097(Tag rootTag, StringText text)
			throws DataAccessException, NewTagException,
			AuthorisationException, ValidationException;

	public abstract boolean isModifyCatalogItem(final CatalogItem item)
			throws DataAccessException;

	public abstract boolean isNewCatalogItem(final CatalogItem item)
			throws DataAccessException;

	public abstract int getCanBeSorted(int index);

	public abstract List/* <Tag> */getFilteredSubList(int index)
			throws DataAccessException;

	public abstract List getSubdivisionEncoding()
			throws DataAccessException;

	// inizio
	public abstract List getDiacriticiList();

	// fine

	public abstract String getNoteStandardText();

	public abstract boolean isNoteStandard();

	public CorrelationValues getCorrelationValuesFromCorrelationKey(
			CorrelationKey corrKey) throws DataAccessException {
		Correlation corr = daoCorrelation.getBibliographicCorrelation(corrKey);
		return new CorrelationValues(corr.getDatabaseFirstValue(), corr
				.getDatabaseSecondValue(), corr.getDatabaseThirdValue());
	}

	public Correlation getCorrelationFromCorrKey(CorrelationKey corrKey)
			throws DataAccessException {
		Correlation corr = null;
		
		if(this instanceof BibliographicEditBean)
			corr = daoCorrelation.getBibliographicCorrelation(corrKey);
		else if(this instanceof AuthorityEditBean)
			corr = daoAuthorityCorrelation.getAuthorityCorrelation(corrKey);
			
		return corr;
	}

	public GroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public String getDigitalOperation() {
		return digitalOperation;
	}

	public void setDigitalOperation(String digitalOperation) {
		this.digitalOperation = digitalOperation;
	}

	public String getCheckDigital() {
		return checkDigital;
	}

	public void setCheckDigital(String checkDigital) {
		this.checkDigital = checkDigital;
	}

	public abstract void createTag097(HttpServletRequest request,
			StringText text, String hierarchyType)
			throws NewTagException,
			AuthorisationException, DataAccessException, ValidationException,
			RecordInUseException;

	public abstract void saveDigitalHierarchy(ControlNumberAccessPoint tag097)
			throws DataAccessException;

	public abstract void saveHierarchy(ControlNumberAccessPoint tag097)
			throws DataAccessException;

	public abstract void deleteDigitalHierarchy(ControlNumberAccessPoint tag)
			throws DataAccessException;

	public abstract void deleteHierarchy(ControlNumberAccessPoint tag)
			throws DataAccessException;

	// Inizio: Gestione tag856 e codice doi
	public abstract List presenzaTag856Testo() throws DataAccessException;

	public abstract List get856Tags() throws DataAccessException;

	public abstract boolean isPresentTag856Testo() throws DataAccessException;

	public abstract String getDoiCode() throws DataAccessException;

	public abstract BibliographicNoteTag get856WithDoi()
			throws DataAccessException;

	public abstract void aggiornaDoi(BibliographicNoteTag tag, String codiceDoi)
			throws AuthorisationException,
			DataAccessException, ValidationException;

	// Fine

	public abstract void cntrHierarchyType();

	public abstract void refreshBean();

	public abstract void cntrTag098(ControlNumberAccessPoint tag097)
			throws DataAccessException, AuthorisationException;

	public abstract void createTag260(PUBL_HDG publHdg,
			HttpServletRequest request) throws DataAccessException,
			AuthorisationException, NewTagException;

	public abstract void verifyAdminData(String checkDigital)
			throws DataAccessException, DataAdminException,
			DataDigAdminException;

	// public abstract void controlDigAdminDataForDelete(int amicusNumber)
	// throws DataAccessException, DataDigAdminException;
	public abstract void controlDigAdminDataForDelete(int amicusNumber)
			throws DataAccessException;

	public String getItemBibliographicLevelCode() {
		return itemBibliographicLevelCode;
	}

	public void setItemBibliographicLevelCode(String itemBibliographicLevelCode) {
		this.itemBibliographicLevelCode = itemBibliographicLevelCode;
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

	public String getHierarchyType() {
		return hierarchyType;
	}

	public void setHierarchyType(String hierarchyType) {
		this.hierarchyType = hierarchyType;
	}

	public boolean isSearchingMother() {
		return searchingMother;
	}

	public void setSearchingMother(boolean searchingMother) {
		this.searchingMother = searchingMother;
	}

	public Integer getAmicusNumberMother() {
		return amicusNumberMother;
	}

	public void setAmicusNumberMother(Integer amicusNumberMother) {
		this.amicusNumberMother = amicusNumberMother;
	}

	public boolean isPresentTag097() {
		return presentTag097;
	}

	public void setPresentTag097(boolean presentTag097) {
		this.presentTag097 = presentTag097;
	}

	public boolean isLevelDisable() {
		// if ((!isPresentTag097()) &&
		// (getCheckContinuaz().equalsIgnoreCase("S") ||
		// (getCheckDigital().equalsIgnoreCase("S")))) {
		if ((!isPresentTag097())
				&& ("S".equalsIgnoreCase(getCheckContinuaz()) || ("S"
						.equalsIgnoreCase(getCheckDigital())))) {
			// setLevelDisable(false);
			levelDisable = false;
		}
		return levelDisable;
	}

	public void setLevelDisable(boolean levelDisable) {
		this.levelDisable = levelDisable;
	}

	public boolean isDuplicaDisable() {
		return duplicaDisable;
	}

	public void setDuplicaDisable(boolean duplicaDisable) {
		this.duplicaDisable = duplicaDisable;
	}

	public String getLevelForSearchMother() {
		return levelForSearchMother;
	}

	public void setLevelForSearchMother(String levelForSearchMother) {
		this.levelForSearchMother = levelForSearchMother;
	}

	public boolean isSendDoi() {
		return sendDoi;
	}

	public void setSendDoi(boolean sendDoi) {
		this.sendDoi = sendDoi;
	}

	public boolean isModifyDoi() {
		return modifyDoi;
	}

	public void setModifyDoi(boolean modifyDoi) {
		this.modifyDoi = modifyDoi;
	}

	public String getMdrFgl() {
		return mdrFgl;
	}

	public void setMdrFgl(String mdrFgl) {
		this.mdrFgl = mdrFgl;
	}

	public boolean isMdrFglDisable() {
		setMdrFglDisable(true);

		if (getItemBibliographicLevelCode().equalsIgnoreCase("m")
				&& (!isLevelDisable())) {
			setMdrFglDisable(false);
		}

		return mdrFglDisable;
	}

	public void setMdrFglDisable(boolean mdrFglDisable) {
		this.mdrFglDisable = mdrFglDisable;
	}

	public List getDigitalTypList() {
		return CodeListsBean.getDigitalTyp().getCodeList(getLocale());
	}

	// 20100729 inizio: aggiunta tabella di decodifica T_FORMAT_REC come
	// tendina
	public List getFormatRecordTypeList() {
		return CodeListsBean.getFormatRecordType().getCodeList(getLocale());
	}

	/**
	 * Metodo che a seconda del $b del tag040 imposta un boolean per la
	 * visualizzazione o no di DLA, DGA e tag097
	 * 
	 * @param bean
	 * @param text
	 * @param tagForm
	 * @throws MarcCorrelationException
	 * @throws DataAccessException
	 * @throws DataDigAdminException
	 * @throws DataAdminException
	 * @throws NewTagException
	 */
	public void checkupdateCatLang(EditBean bean, StringText text, EditTagForm tagForm) throws DataAccessException, DataDigAdminException, DataAdminException, NewTagException
	{
		String displayText = text.getSubfieldsWithCodes("b").getDisplayText().trim();

		/* Se sta aggiungendo il $b lo trovo nel campo del form tagForm.getNewSubfieldCode() e non nello StringText */
		if ("b".equalsIgnoreCase(tagForm.getNewSubfieldCode())	&& displayText.length() == 0) {
			displayText = tagForm.getNewSubfieldContent().trim();
		}

		/* Se prima c'era ENG e l'utente ha digitato qualcosa di diverso da ENG deve controllare le schede e i tag097 */
		if (getLanguageFromOldTag040(bean).equalsIgnoreCase(getDefaultLang())) {
			if (displayText.length() > 0) {
				if (!displayText.equalsIgnoreCase(getDefaultLang())) {
					if (bean.isPresentTag097()){
						throw new NewTagException();
					}

					verifyAdminData(bean);

					/* Se i controlli sono andati bene e il nuovo $b e' diverso da ENG quindi devo aggiornare il flag che mi serve per controllarne la sua presenza */
					bean.setDefaultCatLang(false);
				} else
					bean.setDefaultCatLang(true);
			} else
				bean.setDefaultCatLang(false);
		} else
			bean.setDefaultCatLang(displayText.length() > 0	&& getDefaultLang().equalsIgnoreCase(displayText));
	}

	/**
	 * Metodo che recupera il $b del tag040
	 * 
	 * @param bean
	 * @return a stringa che e' associata al $b
	 * @throws MarcCorrelationException
	 * @throws DataAccessException
	 */
	private String getLanguageFromOldTag040(EditBean bean) throws DataAccessException
	{
		String langFromOldT040 = new String();

		String tagCurrent = bean.getCurrentTag().getMarcEncoding().getMarcTag();
		if (bean.getCurrentTag() instanceof CataloguingSourceTag && tagCurrent.equalsIgnoreCase("040")) {
			CataloguingSourceTag sourceTag = (CataloguingSourceTag) bean.getCurrentTag();
			if (sourceTag.getStringText().getSubfieldsWithCodes("b").getDisplayText().trim().length() > 0) {
				langFromOldT040 = sourceTag.getStringText().getSubfieldsWithCodes("b").getDisplayText().trim();
			}
		}
		return langFromOldT040;
	}

	/**
	 * Metodo che controlla l'esistenza o dei dati DLA o dei dati DGA
	 * 
	 * @param bean
	 * @throws DataAccessException
	 * @throws DataAdminException
	 * @throws DataDigAdminException
	 */
	public void verifyAdminData(EditBean bean) throws DataAccessException, DataAdminException, DataDigAdminException 
	{
		CasCache casCache = null;
		casCache = bean.getCasaliniBean().loadCasCache(bean.getCatalogItem().getAmicusNumber().intValue());

		/* Se il record e' digital controllo che non ci sia una scheda DGA associata */
		if ("S".equalsIgnoreCase(bean.getCheckDigital())) {
			DigitalAmminBean digitalAmminBean = new DigitalAmminBean();
			digitalAmminBean.loadItems(bean.getCatalogItem().getAmicusNumber().intValue());
			if (digitalAmminBean.isExistItem()) {
				throw new DataDigAdminException();
			}
		} else {
			/* Se il record NON e' digital controllo che non ci sia una scheda DLA associata */
			if (casCache != null && casCache.isExistAdminData())
				throw new DataAdminException();
		}
	}

	public void deleteDigitalFile(String operation, String relPath,
			String fileName) throws DataAccessException,
			DigitalFileSystemException {
		FileManagerDo fileManagerDo = new FileManagerDo();

		// ---> 20101102 inizio: Controllo se esiste la directory
		// principale
		if (!fileManagerDo.repositoryExsist(fileManagerDo.getDIGITAL_CONTEXT())) {
			throw new DigitalFileSystemException();
		}
		// ---> 20101102 fine

		String relativePath = new String("");
		// Il path relativo della risorsa puo' essere non impostato se hanno
		// caricato il documento nella root principale
		if (relPath.trim().length() > 0)
			relativePath = relPath + "/";

		// String realPath = (fileManagerDo.getDIGITAL_CONTEXT() + relPath + "/"
		// + fileName).trim();
		String realPath = (fileManagerDo.getDIGITAL_CONTEXT() + relativePath + fileName)
				.trim();

		// System.out.println("Path per cancellazione : " + realPath);
		// System.out.println("Operazione : " + operation);

		fileManagerDo.deleteDigProc(relPath, fileName, operation);
		File fileDelete = new File(realPath);
		fileDelete.delete();
	}

	// 20100304 inizio: flag lingua catalogazione
	public void setCatalogLanguage() {
		setDefaultCatLang(false);
		CataloguingSourceTag tag040 = (CataloguingSourceTag) getCatalogItem()
				.findFirstTagByNumber("040");
		if (tag040 != null
				&& tag040.getStringText().getSubfieldsWithCodes("b")
						.getDisplayText().trim().length() > 0) {
			if (tag040.getStringText().getSubfieldsWithCodes("b")
					.getDisplayText().equalsIgnoreCase(getDefaultLang()))
				setDefaultCatLang(true);
		}
	}

	// 20100304 fine

	public void managementDeleteTags856(HttpServletRequest request)
			throws DataAccessException, RelationshipTagException,
			NumberFormatException, DigitalDoiException,
			IOException, DigitalFileSystemException, RequiredFieldsException,
			DigitalLevelException {
		List tags856 = get856Tags();
		if (tags856 != null) {
			// --------> 20100311 inizio: se il record ha relazioni e tags 856,
			// non puo' essere cancellato
			if (isRelationshipTags()) {
				throw new RelationshipTagException();
			}

			BibliographicNoteTag tag = null;
			for (int i = 0; i < tags856.size(); i++) {
				tag = (BibliographicNoteTag) tags856.get(i);
				// -------------> 20100311: se ci sono 856 con DOI assegnato
				// questo deve essere annullato
				if (DIGITAL_TEXT_RESOURCE.equalsIgnoreCase(tag.getStringText()
						.getSubfieldsWithCodes("3").getDisplayText().trim())
						&& tag.getStringText().getSubfieldsWithCodes("w")
								.getDisplayText().trim().length() > 0) {
					if (!isInternalDoiPermitted()) {
						int t = getCatalogItem().getTags().indexOf(tag);
						setTagIndex(t);
						DigitalDoiBean digitalDoiBean = new DigitalDoiBean(
								this, request);
						digitalDoiBean.loadForDoi(this, request);
						digitalDoiBean.setAnnullaDoi("YES");
						digitalDoiBean.setCreaDOI("NO");
						digitalDoiBean.setDOIEsistente("");
						// TODO: ANDREA COMMENTATO
						// digitalDoiBean.httpPost();
					}
				}
				// -----------> Cancellazione fisica del file e
				// de-indicizzazione
				// -----------> Vedo se esiste il $u inserito in automatico da
				// WeCat
				Subfield subfield = getUrl856FromWeCat(tag);
				if (subfield != null) {
					// System.out.println("$u Code : " + subfield.getCode());
					// System.out.println("$u Content : " +
					// subfield.getContent());
					if (0 < subfield.getContentLength()
							&& 0 < tag.getStringText().getSubfieldsWithCodes(
									"f").getDisplayText().trim().length()) {

						deleteDigitalFile(tag.getStringText()
								.getSubfieldsWithCodes("3").getDisplayText()
								.trim(), tag.getStringText()
								.getSubfieldsWithCodes("d").getDisplayText()
								.trim(), tag.getStringText()
								.getSubfieldsWithCodes("f").getDisplayText()
								.trim());
					}
				}
			}
		}
	}

	public boolean isRelationshipTags() {
		boolean present = false;

		if (getCatalogItem()
				.findFirstTagByNumber("791") != null)
			present = true;
		else if (getCatalogItem()
				.findFirstTagByNumber("792") != null)
			present = true;
		else if (getCatalogItem()
				.findFirstTagByNumber("776") != null)
			present = true;

		return present;
	}

	// Prende il $u del tag856 inserito da WeCat quello che inizia con
	// http://+(DIGITAL_NAME_HOST)
	public Subfield getUrl856FromWeCat(BibliographicNoteTag tag856) {
		Subfield subField = null;
		// StringBuffer buffer = new
		// StringBuffer().append("http://").append(DIGITAL_NAME_HOST);
		List urlList = tag856.getStringText().getSubfieldsWithCodes("u")
				.getSubfieldList();
		for (int i = 0; i < urlList.size(); i++) {
			subField = (Subfield) urlList.get(i);
			if (subField.getContent().startsWith(bufferDigital.toString())) {
				break;
			}
		}
		return subField;
	}

	// Prende la lista delle url aggiuntive (inserite a mana) del tag856
	public List getAdditionalUrl856(BibliographicNoteTag tag856) {
		Subfield subField = null;
		List subList = new ArrayList();
		// StringBuffer buffer = new
		// StringBuffer().append("http://").append(DIGITAL_NAME_HOST);
		List urlList = tag856.getStringText().getSubfieldsWithCodes("u")
				.getSubfieldList();
		for (int i = 0; i < urlList.size(); i++) {
			subField = (Subfield) urlList.get(i);
			if (!subField.getContent().startsWith(bufferDigital.toString())) {
				subList.add(subField);
			}
		}
		return subList;
	}

	public boolean isLanguageAble() {
		return (digitalOperation != null
				&& digitalOperation.trim().length() > 0 && ("ABS"
				.equalsIgnoreCase(digitalOperation)
				|| "CABS".equalsIgnoreCase(digitalOperation)
				|| "REV".equalsIgnoreCase(digitalOperation)
				|| "CPRO".equalsIgnoreCase(digitalOperation) || "RRE"
				.equalsIgnoreCase(digitalOperation)));
	}


	/**
	 * Il metodo conta le relazioni non cieche del record (se ce ne sono la
	 * cancellazione del record non e' possibile)
	 * 
	 * @param amicusNumber
	 * @return count
	 * @throws DataAccessException
	 */
	public int countRelationship(int amicusNumber) throws DataAccessException {
		DAOBibliographicRelationship daoRelationship = new DAOBibliographicRelationship();
		return daoRelationship.countRLTSP(amicusNumber);
	}

	public void setInternalDoiPermitted(boolean internalDoiPermitted) {
		this.internalDoiPermitted = internalDoiPermitted;
	}

	public boolean isInternalDoiPermitted() {
		return internalDoiPermitted;
	}



	public abstract List get082Tags() throws DataAccessException;

	public abstract List get982Tags() throws DataAccessException;


	public abstract boolean getSkipDeleteTags(String marcTag)
			throws DataAccessException;

	public abstract List getBibliographicRelationshipTags()
			throws DataAccessException;

	/**
	 * Extracted from LibrisuiteBean the portions of the method that were more
	 * appropriately handled by the EditBean (i.e. other than instantiating the
	 * appropriate bean)
	 * 
	 * @param recordView
	 * @param key
	 * @throws DataAccessException
	 * @throws ValidationException
	 * @throws AuthorisationException
	 * @throws RecordInUseException
	 * @throws DuplicateTagException
	 */
	public abstract void prepareItemForEditing(int recordView, Object[] key)
			throws DataAccessException, AuthorisationException,
			ValidationException, RecordInUseException;

	public abstract boolean isElectronicResourceoOnTag008();

	// bug 3035: mesh
	public abstract boolean isAbleSubjectsMesh();

	public abstract String getIsbnOrIssnCode(String recordType);

	public abstract BibliographicNoteTag get856_4_2_Tag()
			throws DataAccessException, DuplicateTagException;

	public abstract void createTag856_4_2() throws PermalinkException,
			DuplicateTagException;

	public abstract void checkDigitalLevel();

	public String getLastTermInsert() {
		return lastTermInsert;
	}

	public void setLastTermInsert(String lastTermInsert) {
		this.lastTermInsert = lastTermInsert;
	}

	public boolean isAutoCompleteActive() {
		return autoCompleteActive;
	}

	public void setAutoCompleteActive(boolean autoCompleteActive) {
		this.autoCompleteActive = autoCompleteActive;
	}

	/**
	 * promoted from NewCatalogItemAction to allow polymorphism
	 */
	public abstract void impostaItemBibliographicLevelCode(); // TODO

	public String getNewSubfieldCode() {
		return newSubfieldCode;
	}

	public void setNewSubfieldCode(String newSubfieldCode) {
		this.newSubfieldCode = newSubfieldCode;
	}

	public List getLevelTypeList() throws DataAccessException {
		return levelTypeList;
	}

	public void setLevelTypeList(List levelTypeList) {
		this.levelTypeList = levelTypeList;
	}

	public static final int PHYSICAL_MATERIAL = 1;
	public static final int NOT_PHYSICAL_MATERIAL = 0;
	public int physical;

	public int getPhysical() {
		return physical;
	}

	public void setPhysical(int physical) {
		this.physical = physical;
	}

	public List getBibliographicNoteGroup() {
		return CodeListsBean.getBibliographicNoteGroup().getCodeList(
				getLocale());
	}

	public boolean isEditCopyFastInsert() {
		return editCopyFastInsert;
	}

	public void setEditCopyFastInsert(boolean editCopyFastInsert) {
		this.editCopyFastInsert = editCopyFastInsert;
	}

	public String getConversionCategoryAutocomplete() {
		short category = -1;
		try {
			category = getCurrentTag().getCategory();
		} catch (Exception e) {
			category = -1;
		}

		switch (category) {
		case 2:
			conversionCategoryAutocomplete = "name";
			break;
		case 3:
			conversionCategoryAutocomplete = "title";
			break;
		case 4:
			conversionCategoryAutocomplete = "subject";
			break;
		case 5:
			conversionCategoryAutocomplete = "";
			break;
		case 7:
			if (optNoteGroup == 200)
				conversionCategoryAutocomplete = "publisher";
			else
				conversionCategoryAutocomplete = "";
			break;
		default:
			conversionCategoryAutocomplete = "";
			break;
		}

		return conversionCategoryAutocomplete;
	}


	public Integer getWemiFirstGroup() {
		return wemiFirstGroup;
	}

	public void setWemiFirstGroup(Integer wemiFirstGroup) {
		this.wemiFirstGroup = wemiFirstGroup;
	}

	public abstract boolean exist082WithDewey(List tags082, String deweyCode)
			throws DataAccessException;

	public abstract ClassificationAccessPoint get982TagByDewey(String deweyCode)
			throws DataAccessException;

	public String getWemiGroupLabel() {
		return wemiGroupLabel;
	}

	public void setWemiGroupLabel(String wemiGroupLabel) {
		this.wemiGroupLabel = wemiGroupLabel;
	}

	/* Bug 4306 */
	public boolean isAuthorTag() throws DataAccessException
	{
		boolean check = false;
		if ("100".equalsIgnoreCase(getCurrentTag().getMarcEncoding().getMarcTag())
				|| "110".equalsIgnoreCase(getCurrentTag().getMarcEncoding().getMarcTag())
				|| "111".equalsIgnoreCase(getCurrentTag().getMarcEncoding().getMarcTag())
				|| "700".equalsIgnoreCase(getCurrentTag().getMarcEncoding().getMarcTag())
				|| "710".equalsIgnoreCase(getCurrentTag().getMarcEncoding().getMarcTag())
				|| "711".equalsIgnoreCase(getCurrentTag().getMarcEncoding().getMarcTag())) {
			check = true;
		}
		return check;
	}
	
	public abstract Integer getWemiGroupFieldsFromModel(int modelId) throws DataAccessException;
	
	public String getRdamediasub() {
		return "rdamedia";
	}

	public String getRdacontentsub() {
		return "rdacontent";
	}

	public String getRdacarriersub() {
		return "rdacarrier";
	}
	
	public String getFrbrBrowsing() {
		return frbrBrowsing;
	}

	public void setFrbrBrowsing(String frbrBrowsing) {
		this.frbrBrowsing = frbrBrowsing;
	}
	
	public boolean isTag336_337_338() throws DataAccessException
	{
		boolean check = false;
		if ("336".equalsIgnoreCase(getCurrentTag().getMarcEncoding()
				.getMarcTag())
				|| "337".equalsIgnoreCase(getCurrentTag().getMarcEncoding()
						.getMarcTag())
				|| "338".equalsIgnoreCase(getCurrentTag().getMarcEncoding()
						.getMarcTag())) {
			check = true;
		}
		return check;
	}

	public List getRelatorTermCodeForSubfieldEList() {
		return CodeListsBean.getRelatorTermType().getReversedCodeList(getLocale());
	}

	public Integer getDefaultModelId() {
		return defaultModelId;
	}

	public void setDefaultModelId(Integer defaultModelId) {
		this.defaultModelId = defaultModelId;
	}

	public abstract Correlation getCorrelation(String tagNumber, char indicator1,
			char indicator2, short category) throws DataAccessException;

	
	/**
	 * Changes any non-numeric indicators from the correlation table (
	 * S for skipinfiling and O for authoritySource at the time of writing)
	 * to any value set on the form, and updates the corresponding tag field 
	 * to reflect the set value.
	 *
	 * @param indicator1
	 * @param indicator2
	 */
	public void updateNonNumericIndicators(CorrelationKey coKey, char indicator1,
			char indicator2) {
		Character mySource = null;
		if (coKey.getMarcFirstIndicator() == 'S') {
			if (indicator1 == ' ') {
				setSkipInFiling((short)0);
			}
			else {
				setSkipInFiling(Short.parseShort("" + indicator1));
			}
		}
		else if (coKey.getMarcSecondIndicator() == 'S') {
			if (indicator2 == ' ') {
				setSkipInFiling((short)0);
			}
			else {
				setSkipInFiling(Short.parseShort("" + indicator2));
			}
		}
		else if (coKey.getMarcFirstIndicator() == 'O') {
				mySource = Character.valueOf(indicator1);
		}
		else if (coKey.getMarcSecondIndicator() == 'O') {
				mySource = Character.valueOf(indicator2);
		}
		Tag tag = getCurrentTag();
		if (tag.isBrowsable()) {
			Descriptor d = ((Browsable) tag)
					.getDescriptor();
			if (d instanceof SkipInFiling) {
				d.setSkipInFiling(getSkipInFiling());
			}
			if (mySource != null) {
				d.setAuthoritySourceCode(T_AUT_HDG_SRC.fromMarcIndicator(mySource));
			}
		}
	}
	public abstract void refreshCatalogItem() throws
            DataAccessException, RecordInUseException;

	public void onPostSaveTag(Tag rootTag) throws DataAccessException {
		//Default implementation does nothing
	}

	
	public abstract void processPickedHeading(Descriptor d, String selectedIndex)
			throws DataAccessException, AuthorisationException, ValidationException;

	public void prepareItemForVisualizeCodes(int recordView, Object[] key)
	throws DataAccessException, AuthorisationException,
	ValidationException, RecordInUseException {}
		
	public abstract void updateCasCacheBeforeSaveRecord() throws DataAccessException;
	
	public abstract void isISBNValid(Descriptor descr) throws DataAccessException, InvalidDescriptorException;
	
	public abstract List getNoteGroup();
	
	/**
	 * function enabled of the S_SYS_GLBL_VRBL
	 * save the heading and close the tag
	 * @param currentTag
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 * @throws ValidationException
	 */
	public void saveHeadingAndTag(Tag currentTag, ConfigHandler handler) throws DataAccessException, ValidationException
	 {
		short saveTag = 0;
		String marcTag = currentTag.getMarcEncoding().getMarcTag();
		short category = currentTag.getCategory();
		if(handler.getValue("save_tag") != null)
		   saveTag = Short.parseShort(handler.getValue("save_tag"));
		if (saveTag == 1 && isTagsNotExcluded(marcTag) && category!=2 ) {
			 validateCurrentTag();
			 sortTags(Locale.getDefault());
			 resetCommands();
			 setNavigation(true);
	    }
	 }

	private boolean isTagsNotExcluded(String marcTag) {
		return (!"245".equals(marcTag) && !"260".equals(marcTag)&& !"830".equals(marcTag));
	}

	public boolean isFunctionsDisabled() {
		return functionsDisabled;
	}

	public void setFunctionsDisabled(boolean functionsDisabled) {
		this.functionsDisabled = functionsDisabled;
	}
	
	public List getContentValuesList() {
		/* Bug 5936 */
//		return CasaliniCodeListsBean.getContentType().getCodeList(getLocale());
		return CasaliniCodeListsBean.getContentType().getCodeList(getLanguageFromTag040());
	}

	public List getMediaValuesList() {
		/* Bug 5936 */
//		return CasaliniCodeListsBean.getMediaType().getCodeList(getLocale());
		return CasaliniCodeListsBean.getMediaType().getCodeList(getLanguageFromTag040());
	}

	/**
	 * Metodo fatto perche' in questa tabella ci sono più righe con lo stesso
	 * ValueCode ma con label diverse (CodeTable non gestisce questa cosa)
	 * 
	 * @return
	 */
	public List getCarrierValuesList() 
	{
		List<Avp> list = new ArrayList<Avp>();
		try {
			/* Bug 5936 */
//			list = CodeListBean.getRdaCarrierList(getLocale());
			list = CodeListBean.getRdaCarrierList(getLanguageFromTag040());
		} catch (Exception e) {
			logger.error("Problemi nel caricamento della lista T_RDA_CARRIER: " + e.getMessage());
		}
		return list;
	}
	
	public abstract void loadItemWithoutLock(Object[] key)throws DataAccessException,RecordInUseException;
	
	/**
	 * Bug 3900: il metodo prende il Locale relativo al tag040 $b, se non esiste o e' diverso da ita/eng prende quello del logim
	 * @return
	 */
	public Locale getLanguageFromTag040()
	{
		CataloguingSourceTag cat040 = (CataloguingSourceTag) getCatalogItem().findFirstTagByNumber("040");
		StringText st = cat040.getStringText().getSubfieldsWithCodes("b");
		if (st.getSubfieldList().size() > 0)
		{
			if ("eng".equalsIgnoreCase(st.getDisplayText())){
				return (new Locale("en", "EN"));
			}
			if ("ita".equalsIgnoreCase(st.getDisplayText())){ 
				return (new Locale("it", "IT"));
			}
			/* Bug 6090 */
			if ("fre".equalsIgnoreCase(st.getDisplayText())){ 
				return (new Locale("fr", "FR"));
			}
			return getLocale();
		}
		return getLocale();
	}
}