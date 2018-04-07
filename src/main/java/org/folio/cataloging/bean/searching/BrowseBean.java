package org.folio.cataloging.bean.searching;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.Global;
import org.folio.cataloging.business.cataloguing.authority.AuthorityCatalog;
import org.folio.cataloging.business.cataloguing.bibliographic.NewTagException;
import org.folio.cataloging.business.cataloguing.common.CatalogItem;
import org.folio.cataloging.business.codetable.IndexListElement;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.business.controller.SessionUtils;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.business.searching.BrowseManager;
import org.folio.cataloging.business.searching.InvalidBrowseIndexException;
import org.folio.cataloging.dao.*;
import org.folio.cataloging.dao.persistence.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Manages presentation output for the browse frame
 *
 * @since 1.0
 */
public class BrowseBean extends SearchBean {

	private static final Log logger = LogFactory.getLog(BrowseBean.class);
	
	public static final String BROWSE_METHOD_EDIT_HDG = "editHdg";
	public static final String BROWSE_METHOD_PICK_HDG = "pickHdg";
	public static final String BROWSE_METHOD_PICK_NT =  "pickNameTitle";
	public static final String BROWSE_METHOD_PICK_LD =  "pickHierarchy";
	public static final String BROWSE_METHOD_PICK_THESAURUS = "pickThesaurus";
	public static final String BROWSE_METHOD_PICK_SUGGEST = "pickSuggest";
	
	private static final int CATEGORY_NAME_DEFAULT = 17;
	private static short DEFAULT_CATEGORY = 17;
	public static final int NO_SHELF_MAIN_LIBRARY = 0;
	private static final int CATEGORY_NT_LIBRICAT = 11;
	private static final int CATEGORY_NT_AMICUS = 12;
	private static final DAOBibliographicCorrelation daoCorrelation = new DAOBibliographicCorrelation();
	
	/**
	 * false: do not load the decoratedBrowseList and do not show Level column in cBrowse 
	 */
	public static final boolean verificationLevelVisible = Defaults.getBoolean("customer.casalini.hdg.verification.level.coulumn.visible");

	private Integer defaultAuthorityModel;
	private List browseIndexList;
	private List editorBrowseIndexList;
	private Stack browseLinkMethod = new Stack(); 		// TODO _MIKE: to group into a single object then put the object into a stack
	private Stack browseIndexHistory = new Stack();		// TODO _MIKE: to group into a single object then put the object into a stack
	private Stack selectedIndexHistory = new Stack();	// TODO _MIKE: to group into a single object then put the object into a stack
	private boolean showResults;
	private String selectedIndex;
	private String lastBrowseTerm;
	private BrowseManager browseManager;
	private List xrefCountList;
    private List docCountList;
	private List viewTextList; //pm 2011
	private List noteThesaurusCountList;
	private List authCountList;
	private Descriptor lastSelectedDescriptor;
	private int termsToDisplay;
	private short selectedCategory = DEFAULT_CATEGORY;
	private String browseFieldName;
	private Locale currentLocale;
	private List madesCountList;
	private List ntCountList;
	private boolean nameTitle;
	private String browseMethod;
	private boolean editableIconsEnabled;
	private String storedMethod;
	private List publishersWithShortCodeList;
	private String ntColumnIsVisible = "S";
	private String refColumnIsVisible = "S";
	private String authColumnIsVisible = "S";
	private String docColumnIsVisible = "S";
	private String levelColumnIsVisible = "S";
	private String indicColumnIsVisible = "S";
	private String accColumnIsVisible = "S";
	private String defaultFastInsert = "S";
	private String tagNumber;

	/**
	 * default. Not Decorated List
	 */
	private List/*<Descriptor*/ browseList;

	/* Decorated List loaded in setBrowseList if verificationLevelVisible = true */
	private List/*<DescriptorDecorator>*/ decoratedBrowseList;
	
	private String lastBrowseTermSkip;
	private int skipInFiling;

	private List uriCountList;
	private boolean isAbleUri = false;
	private String[] checkHdgUri = new String[0];
	
	public static SearchBean getInstance(HttpServletRequest request)  {
		BrowseBean bean = (BrowseBean) getSessionAttribute(request, BrowseBean.class);
		if (bean == null) {
			bean = new BrowseBean();
			bean.setSessionAttribute(request, BrowseBean.class);
		}
		bean.setDefaultAuthorityModel(SessionUtils.getUserProfile(request).getDefaultAuthorityModel());
		bean.setSessionAttribute(request, SearchBean.class);
		SearchBean.getInstance(request);
		return bean;
	}

	public String[] getCheckHdgUri() {
		return checkHdgUri;
	}
	
	public void setCheckHdgUri(String[] checkHdgUri) {
		this.checkHdgUri = checkHdgUri;
	}

	public List getUriCountList() {
		return uriCountList;
	}

	public void setUriCountList(List uriCountList) {
		this.uriCountList = uriCountList;
	}
	
	public int getUriCount(int i) {
		return ((Integer) getUriCountList().get(i)).intValue();
	}

	/**
	 * Initialises the Browse bean for a nested operations
	 * @throws InvalidBrowseIndexException 
	 * 
	 */
	public void closeNestedOperation(int mainLibraryForShelfList) throws DataAccessException, InvalidBrowseIndexException 
	{
		setSelectedIndex(retrieveSelectedIndex());
		getBrowseManager().setBrowseIndex(retrieveBrowseIndex(), mainLibraryForShelfList);
		popBrowseLinkMethod();
	}

	public void closeOperation() {
		try {
			popInfo();
		} catch (Exception e) {
			logger.error("Closing Browse operation failed: ignore this error",e);
			// DO NOTHING
		} 
	}

	public CatalogItem createAuthorityFromHeading(Descriptor d)
			throws DataAccessException, NewTagException {
		AuthorityCatalog catalog = new AuthorityCatalog();
		return catalog.createAuthorityFromHeading(d, getDefaultAuthorityModel());
	}

	private Object decodeIndexingLanguageCode(int indexingLanguageCode) {
		if(indexingLanguageCode==0) return "";
		try {
			DAOCodeTable dao = new DAOCodeTable();
			return dao.getLanguageOfIndexing(indexingLanguageCode);
		} catch(DataAccessException e){
			return "";
		}
		
	}

	private Object decodeLanguageAccessPointCode(int accessPointLanguageCode, Descriptor aDescriptor) {
		if(accessPointLanguageCode==0) return "";
		try {
			DAOCodeTable dao = new DAOCodeTable();
			return dao.getAccessPointLanguage(accessPointLanguageCode,aDescriptor);
		} catch(DataAccessException e){
			return "";
		}
		
	}

	private List decorate(List list) 
	{		
		if (list==null || list.isEmpty()){ 
			return list;
		}		
		
		Iterator it = list.iterator();
		List newList = new ArrayList();
		while (it.hasNext()) {
			Descriptor aDescriptor = (Descriptor) it.next();
			DescriptorDecorator helper = new DescriptorDecorator(aDescriptor);
			helper.setCurrentLocale(currentLocale);
			helper.setIndexingLanguage((String)decodeIndexingLanguageCode(helper.getIndexingLanguageCode()));
			helper.setAccessPointLanguage((String)decodeLanguageAccessPointCode(helper.getAccessPointLanguageCode(),aDescriptor));

			if(aDescriptor instanceof SHLF_LIST) 
			{
				Integer amicusNumber = ((SHLF_LIST)aDescriptor).getAmicusNumber();
				Integer org_nbr = ((SHLF_LIST)aDescriptor).getMainLibraryNumber();
				helper.setDescriptionShelfList(refineRecordInformation(aDescriptor.getKey().getHeadingNumber(),amicusNumber,org_nbr));
			}
		
//-------->	201107 inizio: aggiunto codice editore breve nello scorri di PU e PP
			if (isPublisher()){ 
				helper.setShortPublisher(getShortCodeByPublisherCode(publishersWithShortCodeList, aDescriptor.getHeadingNumber()+""));
			}
//-------->	201107 fine
			newList.add(helper);
		}
		return newList;
	}

	private String foundShortCodeByPublisherCode(String publisherCode, List publishers) 
	{
		CasSapPubl publisherDb = null;
		String shortCode = "";
		for (int i = 0; i < publishers.size(); i++) {
			publisherDb = (CasSapPubl)publishers.get(i);
			if (publisherDb.getCodEditore().equalsIgnoreCase(publisherCode)){
				shortCode = publisherDb.getCodEditoreBreve();
				break;
			}
		}
		return shortCode;
	}

	public String getAccColumnIsVisible() {
		return accColumnIsVisible;
	}
	
	public String getAuthColumnIsVisible() {
		return authColumnIsVisible;
	}

	public int getAuthCount(int index) {
		return ((Integer) getAuthCountList().get(index)).intValue();
	}

	public List getAuthCountList() {
		return authCountList;
	}

	public String getBrowseFieldName() {
		return browseFieldName;
	}

	public Stack getBrowseIndexHistory() {
		return browseIndexHistory;
	}

	public List getBrowseIndexList() {
		return browseIndexList;
	}

	public String getBrowseLinkMethod() {
		if(browseLinkMethod.isEmpty()) {
			return BROWSE_METHOD_EDIT_HDG; // default navigation mode
		}
		return (String)browseLinkMethod.peek();
	}
	public List getBrowseList() {
		return browseList;
	}

	public BrowseManager getBrowseManager() {
		return browseManager;
	}
	
	public String getBrowseMethod() {
		return browseMethod;
	}
	public List getDecoratedBrowseList() {
		return decoratedBrowseList;
	}

	public String getDefaultFastInsert() {
		return defaultFastInsert;
	}


	public String getDocColumnIsVisible() {
		return docColumnIsVisible;
	}

	
	public int getDocCount(int i) {
		return ((Integer) getDocCountList().get(i)).intValue();
	}

	public List getDocCountList() {
		return docCountList;
	}
	
	public Integer getEditionNbr(int i) {
		if (getBrowseList().size()>0){
			Descriptor d = (Descriptor) getBrowseList().get(i);
			return ((CLSTN)d).getDeweyEditionNumber();
		}
		return null;
	}

	public List getEditorBrowseIndexList() {
		return editorBrowseIndexList;
	}

	public String getIndicColumnIsVisible() {
		return indicColumnIsVisible;
	}

	public String getLastBrowseTerm() {
		return lastBrowseTerm;
	}

	public String getLastBrowseTermSkip() {
		return lastBrowseTermSkip;
	}

	public Descriptor getLastSelectedDescriptor() {
		return lastSelectedDescriptor;
	}

	public String getLevelColumnIsVisible() {
		return levelColumnIsVisible;
	}
	
	/**
	 * Finds the language dependent stringValue from browseIndexList based on the
	 * given language independent representation (key)
	 */
	public String getLocalisedIndex(String browseIndexKey) {
		String result = null;
		List l = getBrowseIndexList();
		Iterator iter = l.iterator();
		IndexListElement anElem;
		while (iter.hasNext()) {
			anElem = (IndexListElement) iter.next();
			if (anElem.getKey().equals(browseIndexKey)) {
				return anElem.getValue();
			}
		}
		return result;
	}
	
	public int getMadesCount(int i) {
		return ((Integer) getMadesCountList().get(i)).intValue();
	}
	
	public List getMadesCountList() {
		return madesCountList;
	}
	public int getMarcCategory() throws DataAccessException{
		if (getBrowseList() != null && getBrowseList().size() > 0) {
			return ((Descriptor) getBrowseList().get(0)).getCategory();
		} else {
			/*modifica barbara 13/04/2007 PRN 127 - nuova intestazione su lista vuota default maschera inserimento intestazione nome*/
			CorrelationKey cor=daoCorrelation.getMarcTagCodeBySelectedIndex(this.getSelectedIndex());
			if(cor==null)
				return CATEGORY_NAME_DEFAULT;// default category to names
		    else 
		    {
				short category = cor.getMarcTagCategoryCode();
				/*modifica di Carmen Libricat non supporta la categoria 12 dei nomi titoli*/
				if(category==CATEGORY_NT_AMICUS)
					return CATEGORY_NT_LIBRICAT; 
				else
				{
					//bug 3009: se non trova nessuna heading prende la marcCategory del primo della lista 
					//che in alcuni casi non va bene (caso tag 050 che si ritrova la cat. del tag 851 e va in errore creazione heading)
					if (this.getTagNumber() != null)
					{
						cor = daoCorrelation.getMarcTagCodeBySelectedIndex(this.getSelectedIndex(), this.getTagNumber());
						category = cor.getMarcTagCategoryCode();
					}
					
					return category;
				}
			}
		}
	}
	
	public int getNoteThesaurusCount(int i) {
		return ((Integer) getNoteThesaurusCountList().get(i)).intValue();
	}
	
	public List getNoteThesaurusCountList() {
		return noteThesaurusCountList;
	}

	public String getNtColumnIsVisible() {
		return ntColumnIsVisible;
	}
	
	/*barbara modifica 16/05/2007 per skip*/
	
	public int getNtCount(int i) {
		return ((Integer) getNtCountList().get(i)).intValue();
	}


	public List getNtCountList() {
		return ntCountList;
	}
	
	public String getRefColumnIsVisible() {
		return refColumnIsVisible;
	}
	
	/* (non-Javadoc)
	 * @see SearchBean#getSearchType()
	 */
	public String getSearchType() {
		return "browseSearch"; // not really used at present
	}

	public short getSelectedCategory() {
		return selectedCategory;
	}

	public String getSelectedIndex() {
		return selectedIndex;
	}

	public Stack getSelectedIndexHistory() {
		return selectedIndexHistory;
	}

	/*
	 * Finds the language independent key from browseIndexList based on the
	 * users selection
	 */
	public String getSelectedIndexKey() {
		String result = null;
		List l = getBrowseIndexList();

		logger.debug(
			"Looking for key of browse index '" + getSelectedIndex() + "'");
		Iterator iter = l.iterator();
		IndexListElement anElem;
		while (iter.hasNext()) {
			anElem = (IndexListElement) iter.next();
			if (anElem.getValue().toUpperCase().trim().compareTo(getSelectedIndex().toUpperCase().trim()) == 0) {
				return anElem.getKey();
			}
		}
		return result;
	}
	
	/**
	 * 201107 - Metodo che ritorna il codice editore breve associato al codice editore della headings in esame 
	 * @param publishersWithShortCodeList
	 * @param hdgNumber
	 * @return
	 */
	private String getShortCodeByPublisherCode(List publishersWithShortCodeList, String hdgNumber) 
	{
		String shortCode = "";
		DAOPublisher daoPublisher = new DAOPublisher();
        try {
			List publishersList = daoPublisher.loadHdg(hdgNumber);
			if (publishersList!=null && publishersList.size()>0){
				String publisherCode = ((PublCdeHdg)publishersList.get(0)).getPublisherCode();
//				logger.info("Publisher code for hdg  " + hdgNumber + " --> " + publisherCode);
				shortCode = foundShortCodeByPublisherCode(publisherCode, publishersWithShortCodeList);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			shortCode = "?";
			logger.warn("A_T_T_E_N_Z_I_O_N_E --> Problemi nella lettura della tabella PUBL_CDE_HDG per hdgNumber: " + hdgNumber);
		}
		return shortCode;
	}

	public int getSkipInFiling() {
		return skipInFiling;
	}

	public String getSkippedTerm(){
		if(lastBrowseTerm!=null && skipInFiling>0 && lastBrowseTerm.length()>skipInFiling){
			return lastBrowseTerm.substring(0, skipInFiling);
		}
		return "";
	}

	public String getStoredMethod() {
		return storedMethod;
	}

	public String getTagNumber() {
		return tagNumber;
	}
	
	public int getTermsToDisplay() {
		return termsToDisplay;
	}

	public String getViewText(int i) {
		logger.debug("getViewText(" + i + ")");
		return (String)(getViewTextList().get(i));
	}

	public List getViewTextList() {
		return viewTextList;
	}
	
	public int getXrefCount(int i) {
		return ((Integer) getXrefCountList().get(i)).intValue();
	}
	
	public List getXrefCountList() {
		return xrefCountList;
	}

	/**
	 * Initialises the Browse bean for operations
	 * 
	 */
	public void init(Locale locale) throws DataAccessException 
	{
		currentLocale = locale; 
		initBrowseIndexList(locale);
		setBrowseManager(new BrowseManager());
		setTermsToDisplay(Defaults.getInteger("browse.termsPerPage"));
		setShowResults(false);
		storeSelectedIndex(getSelectedIndex());
		setSelectedIndex("NTN      ");
		logger.debug("Selected index set to '" + getSelectedIndex() + "'");
		setBrowseMethod("browse");
	}

	/*
	 * gets the "browse" entries from IDX_LIST 
	 */
	private void initBrowseIndexList(Locale l) throws DataAccessException 
	{
		DAOIndexList dao = new DAOIndexList();
		setBrowseIndexList(dao.getBrowseIndex(Locale.ITALY));
		setEditorBrowseIndexList(dao.getEditorBrowseIndex(Locale.ITALY));
	}

	/**
	 * Initialises the Browse bean for a nested operations
	 */
	public void initForNestedOperation(Locale locale) throws DataAccessException 
	{
		currentLocale = locale;
		storeBrowseIndex(getBrowseManager().getBrowseIndex());
		setTermsToDisplay(Defaults.getInteger("browse.termsPerPage"));
		setShowResults(false);
		storeSelectedIndex(getSelectedIndex());
		setSelectedIndex(
			((IndexListElement) (getBrowseIndexList().get(0))).getValue());
		logger.debug("Selected index set to '" + getSelectedIndex() + "'");
	}

	public boolean isDewey()
	{
        return this.getSelectedIndexKey().equals("24P5");
	}

	public boolean isNameTitle()
	{
		if(getSelectedIndexKey().equals("7P0")||getSelectedIndexKey().equals("2P0"))
			return nameTitle=true;
		else
			return nameTitle=false;
	}

	/**
	 * 201107 - Metodo che ritorna true se stiamo visualizzando le headings dei publisher
	 */
	public boolean isPublisher()
	{
        return (("PU       ").equalsIgnoreCase(selectedIndex)) || (("PP       ").equalsIgnoreCase(selectedIndex));
	}
	
	/**
	 * Rule for determining if the modify icon should be shown on the browse screen (normally
	 * true when the standard link is other than edit heading
	 * TODO investigate the use of <logic:equals> in the jsp to see if it could be simplified through use of
	 * methods such as this (i.e. prefer to implement complex conditions in Java rather than in struts tags)
	 * @return
	 */
	public boolean isShowModifyIcon() {
		return getBrowseLinkMethod().equals("pickHdg") || getBrowseLinkMethod().equals("pickNameTitle");
	}
	
	public boolean isShowResults() {
		return showResults;
	}

	public boolean isSupportsAuthorities() {
		return browseManager.isSupportsAuthorities();
	}

	public boolean isSupportsCrossReferences() {
		return browseManager.isSupportsCrossReferences();
	}

	public boolean isThesaurus(){
        return selectedIndex.equals("TH       ");
	}

	/**
	 * used in cBrowse too
	 */
	public boolean isVerificationLevelVisible() {
		return verificationLevelVisible;
	}

	/**
	 * gets the next page of entries 
	 * 
	 */
	public void next(int cataloguingView) throws DataAccessException {
		BrowseManager b = getBrowseManager();
		
		List l = null;
		
		/*modifica barbara 02/04/2007 PRN 93*/
		if(getBrowseList().size()>1){
			
			int lastIndex = getBrowseList().size() - 1;
			Descriptor d = (Descriptor) getBrowseList().get(lastIndex);
			
			l = b.getNextPage(d, cataloguingView);
			
		}
		else{
			l =getBrowseManager().getFirstPage("",
					cataloguingView,
					getTermsToDisplay());
		}
		updateCounts(l, cataloguingView);
		setBrowseList(l);
	}

	public void popBrowseLinkMethod() {
		browseLinkMethod.pop();
	}

	private void popInfo() {
		// popBrowseLinkMethod();
		if(!browseIndexHistory.isEmpty()) {
			retrieveBrowseIndex();
		}
		if(!selectedIndexHistory.isEmpty()) {
			retrieveSelectedIndex();
		}
	}

	/**
	 * gets the previous page of browse entries
	 * 
	 */
	public void previous(int cataloguingView) throws DataAccessException {
		BrowseManager b = getBrowseManager();
		List l = null;

		/*modifica barbara 02/04/2007 PRN 93*/
		if(getBrowseList().size()>0){
			Descriptor d = (Descriptor) getBrowseList().get(0);
			l = b.getPreviousPage(d, cataloguingView);
		}
		else{
			l =getBrowseManager().getFirstPage("",
					cataloguingView,
					getTermsToDisplay());
		}
		updateCounts(l, cataloguingView);
		setBrowseList(l);

	}

	private String refineRecordInformation(Integer key,Integer amicusNumber,Integer mainLibrary) {
		
		try {
			DAOCodeTable dao = new DAOCodeTable();
			return dao.getRecordInformation(key,amicusNumber,mainLibrary);
		} catch(DataAccessException e){
			return "";
		}
		
	}

	public void refresh(String term, int cataloguingView, int mainLibrary) throws DataAccessException, InvalidBrowseIndexException 
	{
		getBrowseManager().setBrowseIndex(getSelectedIndexKey(), mainLibrary);
		List l = null;
		logger.debug("Doing refresh with index: " + getSelectedIndexKey());
		l = getBrowseManager().getFirstPage(term, cataloguingView, getTermsToDisplay());
		updateCounts(l, cataloguingView);
	
//----> 201107 inizio: carico la lista degli editori che hanno il codice breve impostato
		publishersWithShortCodeList = new ArrayList();
		if (isPublisher()){
			try {
				publishersWithShortCodeList = new DAOCasSapPubl().loadPublishersWithShortCode();
			} catch (DataAccessException e) {
				logger.warn("A_T_T_E_N_Z_I_O_N_E --> Problemi nella lettura della tabella CAS_SAP_PUBL per lista editori con codice breve impostato");
				e.printStackTrace();
			}
		}
//----> 201107 fine
		
		setLastBrowseTerm(term);
		setBrowseList(l);
		setShowResults(true);
	}

	/**
	 * redoes the browse to refresh the entries and the xref counts
	 * 
	 */
	public void refresh(String term, String termSkip, int cataloguingView, int mainLibrary) throws DataAccessException, InvalidBrowseIndexException 
	{	
		getBrowseManager().setBrowseIndex(getSelectedIndexKey(), mainLibrary);
		List l = null;
		logger.debug("Doing refresh with index: " + getSelectedIndexKey());
		l =	getBrowseManager().getFirstPage(termSkip, cataloguingView, getTermsToDisplay());
		updateCounts(l, cataloguingView);

		//if (cataloguingView == View.ANY) {
			List x = getBrowseManager().getViewTexts(l, getLocale());
			setViewTextList(x);
		//}
		
		setLastBrowseTerm(term);
		setLastBrowseTermSkip(termSkip);		
		setBrowseList(l);
		setShowResults(true);
	}

	/**
	 * redoes the browse to refresh the entries and the xref counts
	 * 
	 */
	public void refreshSkipSearch(String term, String termSkip, int cataloguingView, int mainLibrary)
		throws DataAccessException, InvalidBrowseIndexException {

		getBrowseManager().setBrowseIndex(getSelectedIndexKey(), mainLibrary);

		List l = null;

		logger.debug("Doing refresh with index: " + getSelectedIndexKey());
		l =
			getBrowseManager().getFirstElement(
				termSkip,
				cataloguingView,
				getTermsToDisplay());
		updateCounts(l, cataloguingView);

		setLastBrowseTerm(term);
		/*modifica barbara 15/05/2007 PRN 129*/
		setLastBrowseTermSkip(termSkip);		
		setBrowseList(l);
		setShowResults(true);
	}

	private String retrieveBrowseIndex() {
		return (String)browseIndexHistory.pop();
	}

	private String retrieveSelectedIndex() {
		return (String)selectedIndexHistory.pop();
	}

	public void setAccColumnIsVisible(String accColumnIsVisible) {
		this.accColumnIsVisible = accColumnIsVisible;
	}

	public void setAuthColumnIsVisible(String authColumnIsVisible) {
		this.authColumnIsVisible = authColumnIsVisible;
	}

	public void setAuthCountList(List list) {
		authCountList = list;
	}

	public void setBrowseFieldName(String browseFieldName) {
		this.browseFieldName = browseFieldName;
	}

	public void setBrowseIndexList(List list) {
		browseIndexList = list;
	}
	
	public void setBrowseLinkMethod(String string) {
		logger.debug("pushing " + string + " on to the browse link method stack");
		browseLinkMethod.push(string);
	}
	public void setBrowseList(List list) 
	{
		browseList = list;
		if(!isVerificationLevelVisible()) {
			decoratedBrowseList = list; // use not decorated list<Descriptor>
		} else {
			decoratedBrowseList = decorate(list);
		}
	}

	public void setBrowseManager(BrowseManager manager) {
		browseManager = manager;
	}
	
	public void setBrowseMethod(String browseMethod) {
		this.browseMethod = browseMethod;
	}

	public void setDefaultFastInsert(String defaultFastInsert) {
		this.defaultFastInsert = defaultFastInsert;
	}

	public void setDocColumnIsVisible(String docColumnIsVisible) {
		this.docColumnIsVisible = docColumnIsVisible;
	}
 
	public void setDocCount(int i, int val) {
		List l = getDocCountList();
		Integer entry = (Integer) l.get(i);
		l.set(i, new Integer(val));
	}

	public void setDocCountList(List list) {
		docCountList = list;
	}
	
	public void setEditorBrowseIndexList(List editorBrowseIndexList) {
		this.editorBrowseIndexList = editorBrowseIndexList;
	}

	public void setIndicColumnIsVisible(String indicColumnIsVisible) {
		this.indicColumnIsVisible = indicColumnIsVisible;
	}

	/* modifica natasha 28/05/2007 riduzione termine di ricerca a 250car. */
	public void setLastBrowseTerm(String string) {
		if (string.length() > 250)
			lastBrowseTerm = string.substring(0, 250);
		else
			lastBrowseTerm = string;
	}
	
	/* modifica natasha 28/05/2007 riduzione termine di ricerca a 250car. */
	public void setLastBrowseTermSkip(String lastBrowseTermSkips) {
		if (lastBrowseTermSkips.trim().length() > BrowseManager.MAX_BROWSE_TERM_LENGTH)
			lastBrowseTermSkip = lastBrowseTermSkips.substring(0, BrowseManager.MAX_BROWSE_TERM_LENGTH
	);
		else
			lastBrowseTermSkip = lastBrowseTermSkips;
	}

	public void setLastSelectedDescriptor(Descriptor descriptor) {
		lastSelectedDescriptor = descriptor;
	}
	
	public void setLevelColumnIsVisible(String levelColumnIsVisible) {
		this.levelColumnIsVisible = levelColumnIsVisible;
	}
	
	public void setMadesCountList(List madesCountList) {
		this.madesCountList = madesCountList;
	}
	
	public void setNoteThesaurusCountList(List noteThesaurusCountList) {
		this.noteThesaurusCountList = noteThesaurusCountList;
	}

	public void setNtColumnIsVisible(String ntColumnIsVisible) {
		this.ntColumnIsVisible = ntColumnIsVisible;
	}

	public void setNtCountList(List ntCountList) {
		this.ntCountList = ntCountList;
	}

	public void setRefColumnIsVisible(String refColumnIsVisible) {
		this.refColumnIsVisible = refColumnIsVisible;
	}

	public void setSelectedCategory(short defaultCategory) {
		this.selectedCategory = defaultCategory;
	}

	public void setSelectedIndex(String s) {
		logger.debug("setting selectedIndex to '" + s + "'");
		selectedIndex = s;
	}

	public void setShowResults(boolean b) {
		showResults = b;
	}

	public void setSkipInFiling(int skipInFiling) {
		this.skipInFiling = skipInFiling;
	}

	public void setStoredMethod(String storedMethod) {
		this.storedMethod = storedMethod;
	}

	public void setTagNumber(String tagNumber) {
		this.tagNumber = tagNumber;
	}

	public void setTermsToDisplay(int i) {
		termsToDisplay = i;
	}

	/**
	 * @param viewTextList the viewTextList to set
	 */
	public void setViewTextList(List viewTextList) {
		logger.debug("setViewText( list of " + viewTextList.size() + ")");
		this.viewTextList = viewTextList;
	}

	public void setXrefCount(int i, int val) {
		List l = getXrefCountList();
		Integer entry = (Integer) l.get(i);
		l.set(i, new Integer(val));
	}
	
	public void setXrefCountList(List list) {
		xrefCountList = list;
	}
	
	private void storeBrowseIndex(String browseIndex) {
		browseIndexHistory.push(browseIndex);
	}

	private void storeSelectedIndex(String browseIndex) {
		selectedIndexHistory.push(browseIndex);
	}

	public String toString() {
		return super.toString()
		+ "\r\n Link Method History: "+ browseLinkMethod
		+ "\r\n Selected Indexes History: "+ selectedIndexHistory
		+ "\r\n Browse Index History: "+ browseIndexHistory;
	}

	private void updateCounts(List list, int searchingView) throws DataAccessException 
	{
		List x;
		List y;
		List z;
		List j;
		List m;
		List n;
		
		if (getBrowseManager().isSupportsCrossReferences()) {
			x = getBrowseManager().getXrefCounts(list, searchingView);
			setXrefCountList(x);
		}
		
		if (searchingView == View.ANY) {
			x = getBrowseManager().getViewTexts(list, getLocale());
			setViewTextList(x);
		} else {
			logger.debug("searchingView is not ANY, view text not updated");
		}
		
		y = getBrowseManager().getDocCounts(list, searchingView);
		setDocCountList(y);
		//Note thesaurus
		j = getBrowseManager().getThesaurusNoteCounts(list, searchingView);
		setNoteThesaurusCountList(j);

	/*
	 * TODO
	 * It seems that the implementer of Thesaurus took over the existing support structure for
	 * authorities.  Now that authorities have been re-implemented, it would probably be
	 * better if Thesaurus had its own methods, etc.  As things stand now, it is not possible
	 * to support both traditional authorities and thesaurus at the same time. 
	 */
		if (getBrowseManager().isSupportsAuthorities()) {
			if (isThesaurus()) {
				z = getBrowseManager().getXAtrCounts(list, searchingView);
			}
			else {
				z = getBrowseManager().getAuthCounts(list);
				// paulm aut reinstate auth count (not sure what xatr counts are?)
			}
			//z = getBrowseManager().getXAtrCounts(list, searchingView);
			setAuthCountList(z);
		}
		
		if(getSelectedIndexKey().equals("7P0")||getSelectedIndexKey().equals("2P0")){
			n = getBrowseManager().getDocCountNT(list, searchingView);
			setNtCountList(n);
		}
		
		isAbleUri = false;
		if (list!=null && list.size()>0){
			Descriptor descriptor = (Descriptor) list.get(0);
			Integer headingType = Global.HEADING_TYPE_MAP.get(descriptor.getCategory()+"");
			if (headingType != null){
				n = getBrowseManager().getDocCountUri(list, searchingView);
				setUriCountList(n);
				isAbleUri = true;
			}	
		}
	}  
	
	public boolean isAbleUri()
	{
		return isAbleUri;
	}
	
	private Integer getDefaultAuthorityModel() {
		return defaultAuthorityModel;
	}

	private void setDefaultAuthorityModel(Integer defaultAuthorityModel) {
		this.defaultAuthorityModel = defaultAuthorityModel;
	}

	/**
	 * editableIconsEnabled equal true
	 * enabled the modify icon and the copy icon for the browse 
	 * @return
	 */
	public boolean isEditableIconsEnabled() 
	{
        editableIconsEnabled = getBrowseLinkMethod().equals("pickHdg") ||
                getBrowseLinkMethod().equals("pickNameTitle") ||
                getBrowseLinkMethod().equals("pickXref") ||
                getBrowseLinkMethod().equals("pickShelf") ||
                getBrowseLinkMethod().equals("pickPublisher");
		
		return editableIconsEnabled;
	}
	
	/* Bug 5424 */
	public List<HDG_URI> getURIByHeadingNumber(Descriptor descriptor, int searchingView) throws DataAccessException
	{
		return getBrowseManager().getUriList(descriptor, searchingView);
	}
}