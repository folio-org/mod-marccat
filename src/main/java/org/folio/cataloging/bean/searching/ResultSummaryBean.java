package org.folio.cataloging.bean.searching;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.folio.cataloging.bean.LibrisuiteBean;
import org.folio.cataloging.bean.cas.CasaliniCodeListsBean;
import org.folio.cataloging.bean.cataloguing.bibliographic.BibliographicEditBean;
import org.folio.cataloging.bean.cataloguing.common.EditBean;
import org.folio.cataloging.business.amicusSearchEngine.AmicusResultSet;
import org.folio.cataloging.business.authorisation.AuthorisationException;
import org.folio.cataloging.business.cataloguing.bibliographic.*;
import org.folio.cataloging.business.cataloguing.common.CatalogItem;
import org.folio.cataloging.business.cataloguing.common.CataloguingSourceTag;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.business.common.*;
import org.folio.cataloging.business.controller.SessionUtils;
import org.folio.cataloging.business.controller.UserProfile;
import org.folio.cataloging.business.digital.DigitalTagFormatException;
import org.folio.cataloging.business.digital.FileManagerDo;
import org.folio.cataloging.business.librivision.Record;
import org.folio.cataloging.business.searching.ResultSet;
import org.folio.cataloging.business.searching.WeightedAvpComparator;
import org.folio.cataloging.dao.*;
import org.folio.cataloging.dao.common.TransactionalHibernateOperation;
import org.folio.cataloging.dao.persistence.*;
import org.folio.cataloging.exception.LibrisuiteException;
import org.folio.cataloging.exception.RecordInUseException;
import org.folio.cataloging.exception.ValidationException;
import org.folio.cataloging.form.transfer.ResultSummaryForm;
import org.folio.cataloging.model.CART_ITEMS;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.util.ExtractorFieldTag;
import org.folio.cataloging.util.StringText;
import org.folio.cataloging.util.TagConstant;
import org.marc4j.*;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Leader;
import org.marc4j.marc.MarcFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@SuppressWarnings("unchecked")
public class ResultSummaryBean extends LibrisuiteBean 
{
	private static final String DIGITAL_TEXT_RESOURCE = Defaults.getString("digital.text.resource");
	private static boolean equivalentEnabled = Defaults.getBoolean("record.equivalent.enabled", false);
	private static Log logger = LogFactory.getLog(ResultSummaryBean.class);
	private static SortCriteria sortCriteriaList = new SortCriteria();
	private Locale locale = Locale.ENGLISH;
	private String sid = Defaults.getString("openURL.resolver.sid");
	private int tag850enabled = Defaults.getInteger("tag.850.enabled");
	private String userMailLibrarySymbol = "";
	private int cataloguingMode = 0; // NIC disabilitazione link in modalita' catalogazione
	private static boolean customerEnabled = Defaults.getBoolean("customer.casalini.enabled", false);
	private boolean functionsDisabled = Defaults.getBoolean("functions.disabled", false);
	private String cclQuery = "";
	private int firstRecordNumber = 1;
	private int totalNumberOfRecords = 0;
	private int showNumberOfRecords = 10;
	private ArrayList recordArrayList = new ArrayList();
	private ArrayList frbrRecordArrayList = new ArrayList();
	private Vector reservedItemList = new Vector();
	private Vector recordList = new Vector();
	private String idCustomer;
	private String DIGITAL_CONTEXT = "";
	private int reservedItemListNumber = 0;
	private Vector itemToDeleteList = new Vector();
	private int itemToDeleteListNumber = 0;
	private Vector ntiList = new Vector();
	private int ntiListNumber = 0;
	private String elementSetName = "B";
	private T_ITM_DSPLY_FRMT displayFormat;
	private T_ITM_DSPLY defaultDisplayFormat;
	private ArrayList pageArrayList = new ArrayList();
	private int currentPageNumber = 0;
	private String checkUncheckTransf = "0";
	private String checkUncheckDelete = "0";
	private String checkUncheckNti = "0";
	private String sourceFilter = new String();
	private ResultSet resultSet = null;
	private boolean searchingRelationship = false;
	private boolean searchingCollection = false;
	private boolean listIsAlreadyClimbed = false;
	private CatalogItem formerCatalogItem = null;
	private boolean amicusNumberSearch = false;
	private int totalRecordList = 0;
	private String operation = "";
	private boolean searchingMother = false;
	private List languageOfIndexingList = new ArrayList();
	private String[] checkClient;
	private String language = "7";/* sostituire con il file di configurazione */
	private int tag654Hyphen = Defaults.getInteger("tag.654.hyphen"); // stile
	private String digitalRepository;// visualizzare degli 856 solo il $u che punta al repository digitale
	private List presentTag097 = new ArrayList();
	private List serialDigitalList = new ArrayList();
	private boolean isAbleGoBackToCSTColl = false;
	private boolean initialResearch;
	private String tabType = "01";
	private String isbdFormatText = "";
	private int frbrType = 5;//Manifestazione (B), deve essere passato il default dell'authority
	private String ISBDString = "";
	private int numberPages;
	private boolean isOrdersForRecord = false;
	private int cataloguingView; // pm 2011
	private UserProfile userProfile;
	private String tabClassRecord;
	private String tabClassCollections;
	private String tabClassHierarchy;
	private String tabClassLibriLink;
	private String tabClassCodes;
	private String tabClassRda;
	private String tabClassCopies;
	private String tabClassOrders;
	private String tabClassStatusCopies;
	private String tabClassFrbr;
	private String tabClassDiscarded;
	private boolean isRDA=false;
	private String recordURL;
	private String wemiGroupDisplayText;
	private int currentSearchingView = 0;

	//TODO verify commented blocks and methods if we'll use it

	private String[] checkRecordForCart;
	
	public String[] getCheckRecordForCart() {
		return checkRecordForCart;
	}

	public void setCheckRecordForCart(String[] checkRecordForCart) {
		this.checkRecordForCart = checkRecordForCart;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public boolean isOrdersForRecord() {
		return isOrdersForRecord;
	}

	public void setOrdersForRecord(boolean isOrdersForRecord) {
		this.isOrdersForRecord = isOrdersForRecord;
	}

	public int getNumberPages() {
		return numberPages;
	}

	public void setNumberPages(int numberPages) {
		this.numberPages = numberPages;
	}

	public String getIsbdFormatText() {
		return isbdFormatText;
	}

	public void setIsbdFormatText(String isbdFormatText) {
		this.isbdFormatText = isbdFormatText;
	}

	public String getTabType() {
		return tabType;
	}

	public void setTabType(String tabType) {
		this.tabType = tabType;
	}

	public boolean isAbleGoBackToCSTColl() {
		return isAbleGoBackToCSTColl;
	}

	public void setAbleGoBackToCSTColl(boolean isAbleGoBackToCSTColl) {
		this.isAbleGoBackToCSTColl = isAbleGoBackToCSTColl;
	}

	public List getSerialDigitalList() {
		return serialDigitalList;
	}

	public void setSerialDigitalList(List serialDigitalList) {
		this.serialDigitalList = serialDigitalList;
	}

	public List getPresentTag097() {
		return presentTag097;
	}

	public void setPresentTag097(List presentTag097) {
		this.presentTag097 = presentTag097;
	}

	public String getDIGITAL_CONTEXT() {
		return DIGITAL_CONTEXT;
	}

	public void setDIGITAL_CONTEXT(String digital_context) {
		DIGITAL_CONTEXT = digital_context;
	}

	public boolean isEquivalentEnabled() {
		return equivalentEnabled;
	}

	public String getIdCustomer() {
		return idCustomer;
	}

	public void setIdCustomer(String idCustomer) {
		this.idCustomer = idCustomer;
	}

	public String[] getCheckClient() {
		return checkClient;
	}

	public void setCheckClient(String[] checkClient) {
		this.checkClient = checkClient;
	}

	public Vector getRecordList() {
		return recordList;
	}

	public void setRecordList(Vector recordList) {
		this.recordList = recordList;
	}

	public boolean isSearchingCollection() {
		return searchingCollection;
	}

	public void setSearchingCollection(boolean searchingCollection) {
		this.searchingCollection = searchingCollection;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public ResultSummaryBean() {
	}

	public static ResultSummaryBean getInstance(HttpServletRequest request) 
	{
		ResultSummaryBean bean = (ResultSummaryBean) getSessionAttribute(request, ResultSummaryBean.class);
		if (bean == null) {
			bean = new ResultSummaryBean(null, SessionUtils.getDefaultRecordDisplay(request), SessionUtils.getCurrentLocale(request));
			bean.setSessionAttribute(request, bean.getClass());

			// NIC gestione tag 850
			try {
				bean.setUserMailLibrarySymbol(new DAOOrganisationHierarchy().getLibOrBranchSymbol(SessionUtils.getUsersMainLibrary(request)));
				UserProfile userProfile = SessionUtils.getUserProfile(request.getSession(false));
				bean.setUserProfile(userProfile);
			} catch (DataAccessException dae) {
				logger.warn("Database error retrieving code table data");
				throw new RuntimeException(dae);
			}
		}
		EditBean editBean = EditBean.getInstance(request);
		bean.setSearchingRelationship(editBean.isSearchingRelationship());
		bean.setSearchingMother(editBean.isSearchingMother());
		bean.setCurrentSearchingView(SearchTypeBean.getInstance(request)
					.getSearchingView());
		// pm 2011
		bean.setCataloguingView(SessionUtils.getCataloguingView(request));
		bean.setAuthorisationAgent(SessionUtils.getUserProfile(request).getAuthorisationAgent());
		
		// NIC disabilitazione link in modalita' catalogazione
		if (editBean.isCataloguingMode())
			bean.setCataloguingMode(1);
		else
			bean.setCataloguingMode(0);
		
		if(editBean.getWemiFirstGroup()!=null)
			bean.setFrbrType(editBean.getWemiFirstGroup());

		bean.setAmicusNumberSearch();

		return bean;
	}

	/**
	 * Sets a new result set and reinitializes the bean
	 * 
	 * @author paulm
	 * @version $Revision: 1.43 $, $Date: 2006/09/26 09:33:17 $
	 * @since 1.0
	 */
	public void replaceResultSet(ResultSet resultSet) 
	{
		boolean sameFormat = true;
		if (getResultSet() != null) {
			sameFormat = resultSet.getSearchingView() == getResultSet().getSearchingView();
		}
		setResultSet(resultSet);
		setCclQuery(resultSet.getDisplayQuery());
		setAmicusNumberSearch();

		setTotalNumberOfRecords(resultSet.getSize());
		if (!sameFormat) {
			setDisplayFormatToDefault();
		}

		if (isDisplayRecord())
			setDisplayRecord(true);

		firstPage();
	}

	/**
	 * Sets a new result set and reinitializes the bean
	 * 
	 * @author paulm
	 * @version $Revision: 1.43 $, $Date: 2006/09/26 09:33:17 $
	 * @since 1.0
	 */
	public void replaceFrbrResultSet(ResultSet resultSet) {
//		boolean sameFormat = true;
//		if (getFrbrResultSet() != null) {
//			sameFormat = resultSet.getSearchingView() == getFrbrResultSet()
//					.getSearchingView();
//		}
		setFrbrResultSet(resultSet);
		setCclQuery(resultSet.getDisplayQuery());
		setAmicusNumberSearch();

		setTotalNumberOfRecords(resultSet.getSize());
//		if (!sameFormat) {
//			setDisplayFormatToDefault();
//		}

		if (isDisplayRecord())
			setDisplayRecord(true);

		firstFrbrPage();
	}
	
	public ResultSummaryBean(ResultSet resultSet,
			T_ITM_DSPLY defaultRecordDisplay, Locale locale) {
		this.setResultSet(resultSet);
		setDefaultDisplayFormat(defaultRecordDisplay);
		if (defaultRecordDisplay.isBrief()) {
			this.elementSetName = "B";
		} else {
			this.elementSetName = "F";
		}
		this.locale = locale;
		/*
		 * watch out for the order of operation here. Surprising things depend
		 * on each other. In particular, setDisplayFormat will potentially set
		 * recordArrayList(null) which will in turn result in a fetch from LV --
		 * but this requires that totalNumberOfRecords is set
		 */
		if (resultSet != null) {
			this.setTotalNumberOfRecords(resultSet.getSize());
		}
		setDisplayFormatToDefault();
		if (resultSet != null) {
			this.setCclQuery(resultSet.getDisplayQuery());
			this.firstPage();
		}
		setAmicusNumberSearch();
	}

	public void setDisplayFormatToDefault() {
		try {
			if (getDefaultDisplayFormat().isLabelled()) {
				setDisplayFormat(T_ITM_DSPLY_FRMT.LABELLED);
			} else {
				setDisplayFormat(T_ITM_DSPLY_FRMT.MARC);
			}
		} catch (DataAccessException e) {
			logger.warn("Database error retrieving code table data");
			throw new RuntimeException(e);
		}
	}

	public class RecordItem {

		private String recordContent = null;

		private int recordNumber = 0;

		private boolean isImported = false;

		private String isInTheReservedList = "false";
		private String isInTheItemToDeleteList = "false";
		// private String isInTheNTIList="false";
		private boolean checkNTI = false;

		private Record record;

		public RecordItem(String recordContent, int recordNumber, Record record) {
			this.recordContent = recordContent;
			this.recordNumber = recordNumber;
			this.record = record; // pm 2011

			// INIZIO GIUSEPPE
			// 22.4.2009--------------------------------------------------

			for (int i = 0; i < reservedItemList.size(); i++) {
				if (((RecordItem) reservedItemList.get(i)).recordNumber == recordNumber) {
					this.isInTheReservedList = "true";
					break;
				}
			}
			for (int i = 0; i < itemToDeleteList.size(); i++) {
				if (((RecordItem) itemToDeleteList.get(i)).recordNumber == recordNumber) {
					this.isInTheItemToDeleteList = "true";
					break;
				}
			}
			for (int i = 0; i < ntiList.size(); i++) {
				if (((RecordItem) ntiList.get(i)).recordNumber == recordNumber) {
					this.checkNTI = true;
					break;
				}
			}
			// FINE GIUSEPPE
			// 22.4.2009-----------------------------------------------------
		}

		public Object clone() throws CloneNotSupportedException {
			// get initial bit-by-bit copy, which handles all immutable fields
			RecordItem result = (RecordItem) super.clone();

			// mutable fields need to be made independent of this object, for
			// reasons
			// similar to those for defensive copies - to prevent unwanted
			// access to
			// this object's internal state
			return result;
		}

		public String getRecordContent() {
			return recordContent;
		}

		public int getRecordNumber() {
			return recordNumber;
		}

		public void setRecordContent(String string) {
			recordContent = string;
		}

		public void setRecordNumber(int i) {
			recordNumber = i;
			// for(int ind=0;ind<reservedItemList.size();ind++){
			// if (((RecordItem)
			// reservedItemList.get(ind)).getRecordNumber()==recordNumber){
			// this.isInTheReservedList=true;
			// }
			// }
		}

		public boolean isImported() {
			return isImported;
		}

		public void setImported(boolean isImported) {
			this.isImported = isImported;
		}

		public String getIsInTheReservedList() {
			return isInTheReservedList;
		}

		public void setIsInTheReservedList(String isInTheReservedList) {
			this.isInTheReservedList = isInTheReservedList;
		}

		public String getIsInTheItemToDeleteList() {
			return isInTheItemToDeleteList;
		}

		public void setIsInTheItemToDeleteList(String isInTheItemToDeleteList) {
			this.isInTheItemToDeleteList = isInTheItemToDeleteList;
		}

		public boolean getCheckNTI() {
			return checkNTI;
		}

		public void setCheckNTI(boolean checkNTI) {
			this.checkNTI = checkNTI;
		}

		
		/**
		 * Whether the given record should display the "Equivalence" button
		 */
		public boolean isEquivalentDisplayed() {
			boolean result = false;
			if (isEquivalentEnabled()) {
				result = getRecordView() == getCataloguingView();
			}
			return result;
		}

		
		// pm 2011
		private Integer variantCount;

		/**
		 * pm 2011 Whether the current item has variant forms in other views
		 * 
		 * @return
		 * @throws DataAccessException
		 */
		public boolean getHasVariants() throws DataAccessException {
			try {
				if (variantCount == null) {
					variantCount = new Integer(
							new DAOCache()
									.getVariantCount(getAmicusNumber(getRecordNumber() - 1)));
				}
				return variantCount.intValue() > 1;
			} catch (Exception e) {
				return false;
			}
		}
		/**
		 * Whether the current item is a serial (for the purposes of
		 * displaying the "Serials Control" button
		 * @return
		 * @throws DataAccessException 
		 * @throws RecordNotFoundException 
		 */
		public boolean isSerial() throws DataAccessException {
			if (isBibliographic()) {
				return T_ITM_BIB_LVL.isSerial(new DAOBibItem()
					.load(getAmicusNumber(getRecordNumber() - 1), getRecordView())
					.getItemBibliographicLevelCode());
			}
			else {
				return false;
			}
		}

		public Record getRecord() {
			return record;
		}
		
		public int getRecordView() {
			return record.getRecordView();
		}

		/**
		 * The record view as short text
		 * 
		 * @return
		 */
		/*public String getRecordViewText() {
			return View.getViewText(record.getRecordView(), locale);
		}*/

		/**
		 * The record view as short text
		 * 
		 * @return
		 */
		/*public String getRecordCompleteViewText() {
			return View.getCompleteViewText(record.getRecordView(), locale);
		}*/

	}

	public void firstPage() {
		this.setFirstRecordNumber(1);
		this.setRecordArrayList(null);
		this.setPageArrayList(null);
	}

	public void firstFrbrPage() {
		this.setFirstRecordNumber(1);
		this.setFrbrRecordArrayList(null);
		this.setPageArrayList(null);
	}
	
	public void nextPage() {

		if (!((this.getFirstRecordNumber() + this.getShowNumberOfRecords()) > this.totalNumberOfRecords))
			this.setFirstRecordNumber(this.getFirstRecordNumber()
					+ this.getShowNumberOfRecords());
		// ---> fine
		this.setRecordArrayList(null);
		this.setPageArrayList(null);
	}

	public void previousPage() {
		this.setFirstRecordNumber(this.getFirstRecordNumber()
				- this.getShowNumberOfRecords());
		this.setRecordArrayList(null);
		this.setPageArrayList(null);
	}

	public void pageNumber(int pageNumber) {
		this.setFirstRecordNumber((this.getShowNumberOfRecords() * (pageNumber - 1)) + 1);
		this.setRecordArrayList(null);
		this.setPageArrayList(null);
	}
	
	public void nextFrbrPage() {

		if (!((this.getFirstRecordNumber() + this.getShowNumberOfRecords()) > this.totalNumberOfRecords))
			this.setFirstRecordNumber(this.getFirstRecordNumber()
					+ this.getShowNumberOfRecords());
		// ---> fine
		this.setFrbrRecordArrayList(null);
		this.setPageArrayList(null);
	}

	public void previousFrbrPage() {
		this.setFirstRecordNumber(this.getFirstRecordNumber()
				- this.getShowNumberOfRecords());
		this.setFrbrRecordArrayList(null);
		this.setPageArrayList(null);
	}

	public void pageFrbrNumber(int pageNumber) {
		this.setFirstRecordNumber((this.getShowNumberOfRecords() * (pageNumber - 1)) + 1);
		this.setFrbrRecordArrayList(null);
		this.setPageArrayList(null);
	}

	public void setElementSetName(String elementSetName) {
		this.elementSetName = elementSetName;
		this.setRecordArrayList(null);
	}

	private void setPageArrayList(ArrayList pageArrayList) {

		int firstRecordNumber = this.getFirstRecordNumber();
		int showNumberOfRecords = this.getShowNumberOfRecords();
		int totalNumberOfRecords = this.getTotalNumberOfRecords();

		int currentPageNumber = ((firstRecordNumber - 1) / showNumberOfRecords) + 1;
		this.setCurrentPageNumber(currentPageNumber);

		int previousNumberOfPages = 0;
		if (currentPageNumber > 2) {
			previousNumberOfPages = 2;
		} else {
			previousNumberOfPages = currentPageNumber - 1;
		}

		int nextNumberOfPages = 0;
		if ((currentPageNumber * showNumberOfRecords) < (totalNumberOfRecords
				- (showNumberOfRecords * (4 - previousNumberOfPages)) + 1)) {
			nextNumberOfPages = 4 - previousNumberOfPages;
		} else {
			nextNumberOfPages = ((totalNumberOfRecords - 1) / showNumberOfRecords)
					+ 1 - currentPageNumber;
		}

		if (nextNumberOfPages == 0) {

			if (getNumberPages() <= 4)
				previousNumberOfPages = getNumberPages() - 1;
			else
				previousNumberOfPages = 4;
		}
		if (pageArrayList == null) {
			pageArrayList = new ArrayList();
		} else {
			pageArrayList.clear();
		}
		for (int klm = 0; klm < previousNumberOfPages; klm++) {
			pageArrayList.add(new Integer(currentPageNumber
					- previousNumberOfPages + klm));
		}
		pageArrayList.add(new Integer(currentPageNumber));
		for (int klm = 0; klm < nextNumberOfPages; klm++) {
			pageArrayList
					.add(new Integer(currentPageNumber + klm + 1));
		}

		this.pageArrayList = pageArrayList;
	}

	public void setCurrentPageNumber(int currentPageNumber) {
		if (currentPageNumber < 0) {
			this.currentPageNumber = 0;
		} else {
			this.currentPageNumber = currentPageNumber;
		}
	}

	public int getAmicusNumber(int recordIndex) 
	{
		String amicusNumber;
		try {
			amicusNumber = getResultSet().getRecord()[recordIndex].toStyledDocument(getElementSetName(), "amicusNumber.xsl",new Hashtable());
		} catch (LibrisuiteException e) {
			throw new RuntimeException("amicusNumber.xsl failed to transform",e);
		}
		return Integer.parseInt(amicusNumber);
	}

	/**
	 * This method is used with 1 indexed record numbers from the form
	 * 
	 * @since 1.0
	 */
	public int getAmicusNumber(String recordNumber) {
		return getAmicusNumber(recordNumberToIndex(recordNumber));
	}

	public void setReservedItems(RecordItem recItem) {

		boolean itemIsInTheList = false;

		for (int i = 0; i < reservedItemList.size(); i++) {
			if (((RecordItem) reservedItemList.get(i)).recordNumber == recItem.recordNumber) {
				((RecordItem) reservedItemList.get(i)).checkNTI = recItem.checkNTI;
				itemIsInTheList = true;
				break;
			}
		}
		if (!itemIsInTheList) {
			reservedItemList.add(recItem);
			recItem.setIsInTheReservedList("true");

		}

	}

	// LISTA ITEM DA CANCELLARE
	public void setItemsToDelete(RecordItem recItem) {
		boolean itemIsInTheList = false;

		for (int i = 0; i < itemToDeleteList.size(); i++) {
			if (((RecordItem) itemToDeleteList.get(i)).recordNumber == recItem.recordNumber) {

				itemIsInTheList = true;
				break;
			}
		}
		if (!itemIsInTheList) {
			itemToDeleteList.add(recItem);
			recItem.setIsInTheItemToDeleteList("true");
		}

	}

	// LISTA ITEM NTI
	public void setNTIItems(RecordItem recItem) {
		boolean itemIsInTheList = false;

		for (int i = 0; i < ntiList.size(); i++) {
			if (((RecordItem) ntiList.get(i)).recordNumber == recItem.recordNumber) {

				itemIsInTheList = true;
				break;
			}
		}
		if (!itemIsInTheList) {
			if (recItem.checkNTI == false) {
				recItem.checkNTI = true;
			}
			ntiList.add(recItem);

			for (int ind = 0; ind < this.reservedItemList.size(); ind++) {
				if (((RecordItem) this.reservedItemList.get(ind))
						.getRecordNumber() == recItem.recordNumber) {
					((RecordItem) this.reservedItemList.get(ind))
							.setCheckNTI(true);
					break;
				}
			}
			for (int ind = 0; ind < this.itemToDeleteList.size(); ind++) {
				if (((RecordItem) this.itemToDeleteList.get(ind))
						.getRecordNumber() == recItem.recordNumber) {
					((RecordItem) this.itemToDeleteList.get(ind))
							.setCheckNTI(true);
					break;
				}
			}
		}
	}

	// CANCELLAZIONE ITEM DALLA LISTA DEGLI ITEM DA TRASFERIRE
	public void deleteFromReservedItems(String recNumber) {

		int recordNumber = Integer.parseInt(recNumber);

		for (int i = 0; i < this.reservedItemList.size(); i++) {
			if (((RecordItem) this.reservedItemList.get(i)).getRecordNumber() == recordNumber) {
				this.reservedItemList.remove(i);
				for (int ind = 0; ind < this.recordArrayList.size(); ind++) {
					if (((RecordItem) this.recordArrayList.get(ind))
							.getRecordNumber() == recordNumber) {
						((RecordItem) this.recordArrayList.get(ind)).isInTheReservedList = "false";
					}
				}
				break;
			}

		}

	}

	
	public RecordItem getRecordItem()
	{
		return (RecordItem)recordArrayList.get(0);
	}
	
	// CANCELLAZIONE ITEM DALLA LISTA DEGLI ITEM DA CANCELLARE
	public void deleteFromItemsToDelete(String recNumber) {

		int recordNumber = Integer.parseInt(recNumber);

		for (int i = 0; i < this.itemToDeleteList.size(); i++) {
			if (((RecordItem) this.itemToDeleteList.get(i)).getRecordNumber() == recordNumber) {
				this.itemToDeleteList.remove(i);
				for (int ind = 0; ind < this.recordArrayList.size(); ind++) {
					if (((RecordItem) this.recordArrayList.get(ind))
							.getRecordNumber() == recordNumber) {
						((RecordItem) this.recordArrayList.get(ind)).isInTheItemToDeleteList = "false";
					}
				}
				break;
			}

		}

	}

	// CANCELLAZIONE ITEM DALLA LISTA DEGLI NTI
	public void deleteFromNti(String recNumber) {

		int recordNumber = Integer.parseInt(recNumber);

		for (int i = 0; i < this.ntiList.size(); i++) {
			if (((RecordItem) this.ntiList.get(i)).getRecordNumber() == recordNumber) {
				this.ntiList.remove(i);
				for (int ind = 0; ind < this.recordArrayList.size(); ind++) {
					if (((RecordItem) this.recordArrayList.get(ind))
							.getRecordNumber() == recordNumber) {
						((RecordItem) this.recordArrayList.get(ind))
								.setCheckNTI(false);
						break;
					}
				}
				for (int ind = 0; ind < this.itemToDeleteList.size(); ind++) {
					if (((RecordItem) this.itemToDeleteList.get(ind))
							.getRecordNumber() == recordNumber) {
						((RecordItem) this.itemToDeleteList.get(ind))
								.setCheckNTI(false);
						break;
					}
				}
				for (int ind = 0; ind < this.reservedItemList.size(); ind++) {
					if (((RecordItem) this.reservedItemList.get(ind))
							.getRecordNumber() == recordNumber) {
						((RecordItem) this.reservedItemList.get(ind))
								.setCheckNTI(false);
						break;
					}
				}
				break;
			}

		}

	}

	// CANCELLAZIONE DI TUTTI GLI ITEM DELLE 2 LISTE (di trasferimento e di
	// cancellazione)
	public void clearAllList(Vector list) {

		if (list == this.reservedItemList) {
			this.reservedItemList.removeAllElements();
			for (int i = 0; i < this.recordArrayList.size(); i++) {
				((RecordItem) this.recordArrayList.get(i)).isInTheReservedList = "false";
			}
		} else if (list == this.itemToDeleteList) {
			this.itemToDeleteList.removeAllElements();
			for (int i = 0; i < this.recordArrayList.size(); i++) {
				((RecordItem) this.recordArrayList.get(i)).isInTheItemToDeleteList = "false";
			}
		} else if (list == this.ntiList) {
			this.ntiList.removeAllElements();
			for (int i = 0; i < this.recordArrayList.size(); i++) {
				((RecordItem) this.recordArrayList.get(i)).setCheckNTI(false);
			}
		}

	}

	// SETTAGGIO DELLA PROPRIETA' checkNTI X SINGOLO ITEM DELLA LISTA DEGLI ITEM
	// DA TRASFERIRE
	public void setNTIInReservedList(int recNumber, boolean ntiValue) {
		for (int ind = 0; ind < getReservedItemList().size(); ind++) {
			if (((RecordItem) getReservedItemList().get(ind)).recordNumber == recNumber) {
				((RecordItem) getReservedItemList().get(ind)).checkNTI = ntiValue;
				break;
			}
		}

	}

	public void setNTIInRecordArrayList() {
		for (int i = 0; i < getRecordArrayList().size(); i++) {
			for (int ind = 0; ind < getReservedItemList().size(); ind++) {
				if (((RecordItem) getRecordArrayList().get(i))
						.getRecordNumber() == ((RecordItem) getReservedItemList()
						.get(ind)).getRecordNumber()) {
					((RecordItem) getRecordArrayList().get(i))
							.setCheckNTI(((RecordItem) getReservedItemList()
									.get(ind)).getCheckNTI());
					break;
				}
			}
		}
	}

	// FINE GIUSEPPE 22.4.2009
	// ------------------------------------------------------------

	private void setRecordArrayList(ArrayList recordArrayList) 
	{
		ResultSet resultSet = this.getResultSet();
		int firstRecordNumber = this.getFirstRecordNumber();
		int showNumberOfRecords = this.getShowNumberOfRecords();
		int totalNumberOfRecords = this.getTotalNumberOfRecords();
		String elementSetName = this.getElementSetName();
		List listPresent097 = new ArrayList();
		List serialDigitalList = new ArrayList();
		
		try {
			logger.debug("Load digital repository");	
			
			if(customerEnabled){
				FileManagerDo fileManagerDo = new FileManagerDo();
				digitalRepository = new StringBuffer().append("http://").append(Defaults.getString("digital.name.host")).append("/").append(fileManagerDo.getDIGITAL_VIRTUAL_PATH()).toString();
			}
			else
				digitalRepository = "";
			logger.debug("Digital repository per controlli tag 856 $u : " + digitalRepository);
			logger.debug("Digital repository loaded");	
			
		} catch (DataAccessException e) {
			digitalRepository = "";
			logger.info("Errore procedure digital --> non verra' visualizzato bene il tag856!");
//			e.printStackTrace();
		}

		if (recordArrayList == null) {
			recordArrayList = new ArrayList();
		} else {
			recordArrayList.clear();
		}

		if (firstRecordNumber < 1) {
			firstRecordNumber = 1;
			this.setFirstRecordNumber(firstRecordNumber);
		} else if (firstRecordNumber > totalNumberOfRecords) {
			firstRecordNumber = totalNumberOfRecords;
			this.setFirstRecordNumber(firstRecordNumber);
		}

		int lastRecordNumber = 0;
		if ((firstRecordNumber + showNumberOfRecords - 1) <= totalNumberOfRecords) {
			lastRecordNumber = firstRecordNumber + showNumberOfRecords - 1;
		} else {
			lastRecordNumber = totalNumberOfRecords;
		}

		Hashtable xsltParameters = new Hashtable();
		xsltParameters.put("extendedView", "true");
		xsltParameters.put("elementSetName", this.getElementSetName());
		xsltParameters.put("digitalRepository", new String(digitalRepository));
		DAOCasCache daoCasCache = new DAOCasCache();
		
		List<Integer> recordsDiscarded = new ArrayList<Integer>();
		
		logger.debug("Start records elaboration");	
		for (int recordNumber = firstRecordNumber; recordNumber <= lastRecordNumber; recordNumber++) {
			try {
				xsltParameters.put("cataloguingMode", new Integer(0));// NIC
				xsltParameters.put("userMailLibrarySymbol", new String(userMailLibrarySymbol));// NIC
				xsltParameters.put("tag850enabled", new Integer(tag850enabled));
				xsltParameters.put("customerEnabled", new Boolean(customerEnabled));
				xsltParameters.put("tag654Hyphen", new Integer(tag654Hyphen));

				if (!(this.getDisplayFormat() == 3) && (!(isSearchingCollection() == true))) {
					String ntrLvl = daoCasCache.getNtrLvlForDigital(resultSet.getAmicusNumber(recordNumber - 1).intValue(), getResultSet().getSearchingView());
					if (ntrLvl == null) {
						ntrLvl = "001";
					}
					xsltParameters.put("ntrLvlDigital", ntrLvl);
				}

				xsltParameters.put("requestedAmicusNumber", resultSet.getAmicusNumber(recordNumber - 1));
				Record currRecord = resultSet.getRecord(recordNumber - 1, this.getElementSetName());
				currRecord.setCclQuery(cclQuery);

				String s = (String) currRecord.getContent("F");
				
				try {
					Integer an = resultSet.getAmicusNumber(recordNumber - 1);
					DAOLibrary lib = new DAOLibrary();
					String symbolCode = lib.getLibrarySymbol(getUserProfile().getMainLibrary(), an);
					if(symbolCode != null && !symbolCode.trim().equalsIgnoreCase("") && isBibliographic())
					{
						String newContent = insertTags(s,an,symbolCode);
						currRecord.setContent("F", newContent);
					}
					
				} catch (Exception e)
				{
//					logger.debug("Record Not Valid: " + e.getMessage());
				}

				if (getDisplayFormat() == 1 && isRDA() ==false) {
					// formato ISBD
					String styledDocumentISBD = currRecord.toStyledDocument(this.getElementSetName(),"marcslimToISBDText_it.xsl", xsltParameters);
					styledDocumentISBD = styledDocumentISBD.replaceAll("\n", "");
					styledDocumentISBD = styledDocumentISBD.replaceAll("\t", "");
					styledDocumentISBD = styledDocumentISBD.replaceAll("\r", "");
					xsltParameters.put("isbdString", styledDocumentISBD.trim());
				}

				xsltParameters.put("fbrType", ""+getFrbrType());
				xsltParameters.put("lang", locale.getISO3Language());

				
				logger.debug("Start XML Transformation");	
				String styledDocument=null;
				
				styledDocument =currRecord.toStyledDocument(this.getElementSetName(), getStyleSheet(lastRecordNumber),xsltParameters);
				logger.debug("End XML Transformation");
				
				RecordItem recItem = new RecordItem(styledDocument,
						recordNumber, currRecord);


				if ("".equals(currRecord.getContent("F"))) {
					logger.error("----> Impossibile visualizzare il record " + resultSet.getAmicusNumber(recordNumber - 1));
					recordsDiscarded.add(resultSet.getAmicusNumber(recordNumber - 1));
					continue;
				}

				recordArrayList.add(recItem);
				listPresent097.add(load097List(resultSet.getAmicusNumber(recordNumber - 1).intValue()));
				if (this.getDisplayFormat() == 3 && isSearchingCollection() == true) 
				{
					String isSerialDigital = new DAOCasCache().isDigitalSerial(resultSet.getAmicusNumber(recordNumber - 1).intValue(), getResultSet().getSearchingView()) ? "S" : "N";
					recordArrayList.add(recItem);
					listPresent097.add(load097List(resultSet.getAmicusNumber(
					recordNumber - 1).intValue()));
				}
				
			} catch (LibrisuiteException e) {
				throw new RuntimeException("Stylesheet transformation error", e);
			}
		}
		logger.debug("End records elaboration");	
		setRecordsDiscarded(recordsDiscarded);
		setDisplayRecord(false);
		setInitialResearch(false);
		this.presentTag097 = listPresent097;
		this.serialDigitalList = serialDigitalList;
		this.recordArrayList = recordArrayList;

		if (this.reservedItemList != null && this.reservedItemList.size() != 0) {
			setNTIInRecordArrayList();
		}

		int numberPages = getTotalNumberOfRecords() / 10;
		int reminder = getTotalNumberOfRecords() % 10;

		if (reminder > 0)
			numberPages = numberPages + 1;

		setNumberPages(numberPages);
	}

	public void setFrbrRecordArrayList(ArrayList recordArrayList) {

		ResultSet resultSet = this.getFrbrResultSet();
		int firstRecordNumber = this.getFirstRecordNumber();
		int showNumberOfRecords = this.getShowNumberOfRecords();
		int totalNumberOfRecords = this.getTotalNumberOfRecords();
		
		if (recordArrayList == null) {
			recordArrayList = new ArrayList();
		} else {
			recordArrayList.clear();
		}

		if (firstRecordNumber < 1) {
			firstRecordNumber = 1;
			this.setFirstRecordNumber(firstRecordNumber);
		} else if (firstRecordNumber > totalNumberOfRecords) {
			firstRecordNumber = totalNumberOfRecords;
			this.setFirstRecordNumber(firstRecordNumber);
		}

		int lastRecordNumber = 0;
		if ((firstRecordNumber + showNumberOfRecords - 1) <= totalNumberOfRecords) {
			lastRecordNumber = firstRecordNumber + showNumberOfRecords - 1;
		} else {
			lastRecordNumber = totalNumberOfRecords;
		}



		for (int recordNumber = firstRecordNumber; recordNumber <= lastRecordNumber; recordNumber++) {
				Record currRecord = resultSet.getRecord(recordNumber - 1, this.getElementSetName());
				currRecord.setCclQuery(cclQuery);
				RecordItem recItem = new RecordItem("",recordNumber, currRecord);

				if ("".equals(currRecord.getContent("F"))) {
					logger.error("----> Impossibile visualizzare il record "
							+ resultSet.getAmicusNumber(recordNumber - 1));
					continue;
				}
				recordArrayList.add(recItem);
		}

		setDisplayRecord(false);
		setInitialResearch(false);
		int numberPages = getTotalNumberOfRecords() / 10;
		int reminder = getTotalNumberOfRecords() % 10;
		this.frbrRecordArrayList = recordArrayList;
		if (reminder > 0)
			numberPages = numberPages + 1;

		setNumberPages(numberPages);
	}

	
	private List<Integer> recordsDiscarded;
	

	public boolean isLastPage() {
		return numberPages == currentPageNumber;
	}

	/**
	 * ---> 20100831 Metodo che memorizza per ogni record la presenza del tag097
	 * con $a,$d impostato
	 * 
	 * @param amicusNumber
	 * @return "S" oppure "N"
	 * @throws DataAccessException
	 */
	private String load097List(int amicusNumber) throws DataAccessException {
		List list = null;
		String presentTag097 = "N";
		DAOCasDigFiles dao = new DAOCasDigFiles();
		list = dao.loadCasDigFilesByBibItemFiglia(amicusNumber);
		if (list != null && list.size() > 0) {
			CasDigFiles casDigFiles = (CasDigFiles) list.get(0);
			if (new Integer(casDigFiles.getBibItemNumberMadre()) != null
					&& casDigFiles.getOrderProgr() != null) {
				if (casDigFiles.getBibItemNumberMadre() > 0
						&& casDigFiles.getOrderProgr().intValue() > 0) {
					presentTag097 = "S";
				}
			}
		}
		return presentTag097;
	}

	/*
	 * retrieves the localized key "stylesheet" from the "resultSummary" bundle
	 */
	private String getStyleSheet(int totalRecord) {
		try {
			initDisplayFormat();
		} catch (Exception e) {
			logger.error("Errore nel settaggio del diplayFormat ", e);
		}

		String resourceKey = displayFormat.getStylesheetPropertyKey();
		String result = "";
		if (isDisplayRecord() || (totalRecord == 1 && getDisplayFormat() == 1)) {
			if (this.getElementSetName().equals("B")) {
				resourceKey = resourceKey + ".brief";
				return getDisplayBrief(resourceKey);
			} else if (!isRDA()) {
				return getDisplayFull();
			} //RDA
			else {
				return getDisplayRDA();
			}
		}

		if (this.getElementSetName().equals("B") && isDisplayRecord())
			resourceKey = resourceKey + ".brief";

		result = ResourceBundle.getBundle("resources/searching/resultSummary", locale).getString(resourceKey);
		if (isArchive() && resourceKey.equals("label.stylesheet")) {
			return result = "mades" + result.substring(6);
		} else if (this.getDisplayFormat() == 3 && isSearchingCollection() == true) {
			return "marcslimToCOLL_it.xsl";
		} else if (result == null || "".equals(result) || isInitialResearch() == true) {
			return isBibliographic() ? "recordResultTableSummary_" + getTabType() + "_it.xsl" : "recordAuthorityResultTableSummary_" + getTabType() + "_it.xsl";
		} else {
			if (result.equals("recordResultTableSummary_it.xsl"))
				result = isBibliographic() ? "recordResultTableSummary_" + getTabType() + "_it.xsl" : "recordAuthorityResultTableSummary_" + getTabType() + "_it.xsl";
			return result;
		}
	}

	private void initDisplayFormat() throws DataAccessException {
		/*if (isBibliographic()) {
			if (!(displayFormat instanceof T_BIB_DSPLY_FRMT)) {
				displayFormat = (T_BIB_DSPLY_FRMT) new DAOCodeTable().load(T_BIB_DSPLY_FRMT.class, getDisplayFormat(), locale);
			}
		} else {
			if (!(displayFormat instanceof T_AUT_DSPLY_FRMT)) {displayFormat = (T_AUT_DSPLY_FRMT) new DAOCodeTable().load(T_AUT_DSPLY_FRMT.class, getDisplayFormat(), locale);
			}
		}*/
	}

	private String getDisplayBrief(String resourceKey) {
		String result;
		result = ResourceBundle.getBundle(
				"resources/searching/resultSummary", locale).getString(
				resourceKey);
		return result;
	}

	private String getDisplayFull() {
		//FULL
		if (isBibliographic()) {
			return "recordResultSummary_"+locale.getLanguage()+".xsl";
		} else {
			return "autorityRecordResultSummary_"+locale.getLanguage()+".xsl";
		}
	}

 private String getDisplayRDA() {
		String rdaResulSummary =  setDefaultRDA();
		if (getFrbrType() != 0 && getFrbrType()<=8) {
			String frbrType = "" + getFrbrType();
			this.setRDA(false);
			rdaResulSummary = "rdaResultSummary_" + frbrType + ".xsl";
		}
		return rdaResulSummary;
	}

  private String setDefaultRDA() {
		this.setRDA(false);
		if (isBibliographic()) {
			return "rdaResultSummary_" +5+ ".xsl";
		} else {
			return "rdaResultSummary_" +6+ ".xsl";
		}
	}


	public String getCclQuery() {
		return cclQuery;
	}

	public int getFirstRecordNumber() {
		return firstRecordNumber;
	}

	public int getLastRecordNumber() {
		int result = firstRecordNumber + showNumberOfRecords - 1;
		if (result <= getTotalNumberOfRecords()) {
			return result;
		} else {
			return getTotalNumberOfRecords();
		}
	}

	public ArrayList getRecordArrayList() {
		return recordArrayList;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}
	
	public ResultSet getFrbrResultSet() {
		return resultSet;
	}

	public int getShowNumberOfRecords() {
		return showNumberOfRecords;
	}

	public int getTotalNumberOfRecords() {
		return totalNumberOfRecords;
	}

	public void setCclQuery(String cclQuery) {
		if (cclQuery == null) {
			this.cclQuery = "";
		} else {
			this.cclQuery = cclQuery;
		}
	}

	public void setFirstRecordNumber(int i) {
		firstRecordNumber = i;
	}

	public void setResultSet(ResultSet set) {
		resultSet = set;
	}
	
	public void setFrbrResultSet(ResultSet set) {
		resultSet = set;
	}

	public void setShowNumberOfRecords(int i) {
		showNumberOfRecords = i;
	}

	public void setTotalNumberOfRecords(int i) {
		totalNumberOfRecords = i;
	}

	public String getElementSetName() {
		return elementSetName;
	}

	public ArrayList getPageArrayList() {
		return pageArrayList;
	}

	public int getCurrentPageNumber() {
		return currentPageNumber;
	}

	public boolean getSearchingRelationship() {
		return searchingRelationship;
	}

	public boolean isSearchingRelationship() {
		return searchingRelationship;
	}

	public void setSearchingRelationship(boolean b) {
		searchingRelationship = b;
	}

	public int getSortCriteria() {
		return getResultSet().getSortCriteria();
	}

	/**
	 * sortCriteria is the value selected from the T_SRT_CRTRIA list
	 * 
	 * @since 1.0
	 */
	public void setSortCriteria(int i) {
		getResultSet().setSortCriteria(i);
	}

	public List getSortCriteriaList() {
		return sortCriteriaList.getCodeList(locale);
	}

	/**
	 * Invokes a LibriVision sort based on the selected sort criteria
	 * 
	 * @since 1.0
	 */
	public void sortResults(int sortCriteria) throws LibrisuiteException {
		setSortCriteria(sortCriteria);

		if (sortCriteria > 0) {
			List l = new DAOSortCriteriaDetails().getDetails(sortCriteria);

			// convert the List of attribute, direction info from the DAO
			// into two String arrays for attribute and direction as required by
			// LVSort
			List attributes = new ArrayList();
			List directions = new ArrayList();
			SortCriteriaDetails aDetail;
			Iterator iter = l.iterator();
			while (iter.hasNext()) {
				aDetail = (SortCriteriaDetails) iter.next();
				attributes.add(String.valueOf(aDetail.getAttribute()));
				directions.add(String.valueOf(aDetail.getDirection()));
			}
			// sort the result set
			getResultSet().sort((String[]) attributes.toArray(new String[0]),
					(String[]) directions.toArray(new String[0]));
			// redisplay the results
			firstPage();
		}
	}

	public boolean isBibliographic() {
		return getResultSet().isBibliographic();
	}

	// public EditBean prepareItemForEditing(int itemIndex,
	// HttpServletRequest request) throws DataAccessException,
	// MarcCorrelationException, RecordInUseException {
	// int amicusNumber = getAmicusNumber(itemIndex);
	//
	// EditBean bean = super.prepareItemForEditing(new Object[] {
	// new Integer(amicusNumber),
	// new Integer(SessionUtils.getCataloguingView(request)) },
	// request);
	// /*
	// * modifica barbara 28/03/2007 ordinamento tag e editing true sulla
	// * modifica del record bib
	// */
	// Locale currentLocale = SessionUtils.getCurrentLocale(request);
	// activateEditor(bean, currentLocale);
	// /* fine modifica barbara */
	// return bean;
	// }
	/*
	 * pm 2011 retrieve the view number for the given record index
	 */
	private int getRecordView(int itemIndex) {
		return getResultSet().getRecord()[itemIndex].getRecordView();
	}

	public EditBean prepareItemForEditing(int itemIndex,
			HttpServletRequest request) throws DataAccessException,
            RecordInUseException,
			AuthorisationException {
		int amicusNumber = getAmicusNumber(itemIndex);

		EditBean bean;
		try {
			bean = super.prepareItemForEditing(getRecordView(itemIndex), // pm
					// 2011
					new Object[] {
							new Integer(amicusNumber),
							new Integer(SessionUtils
									.getCataloguingView(request)) }, request);
		} catch (ValidationException e) {
			throw new MarcCorrelationException(e);
		}
		/*
		 * modifica barbara 28/03/2007 ordinamento tag e editing true sulla
		 * modifica del record bib
		 */
		Locale currentLocale = SessionUtils.getCurrentLocale(request);
		activateEditor(bean, currentLocale);
		/* fine modifica barbara */
		return bean;
	}
	
	
	public EditBean prepareItemForVisualizeCodes(int itemIndex,
			HttpServletRequest request) throws DataAccessException,
            RecordInUseException,
			AuthorisationException {
		int amicusNumber = getAmicusNumber(itemIndex);

		EditBean bean;
		try {
			bean = super.prepareItemForVisualizeCodes(getRecordView(itemIndex), // pm
					// 2011
					new Object[] {
							new Integer(amicusNumber),
							new Integer(SessionUtils
									.getCataloguingView(request)) }, request);
		} catch (ValidationException e) {
			throw new MarcCorrelationException(e);
		}
		/*
		 * modifica barbara 28/03/2007 ordinamento tag e editing true sulla
		 * modifica del record bib
		 */
		Locale currentLocale = SessionUtils.getCurrentLocale(request);
		activateEditor(bean, currentLocale);
		/* fine modifica barbara */
		return bean;
	}
	

	public EditBean prepareAuthorityItemForEditing(int itemIndex,
			HttpServletRequest request) throws DataAccessException,
            RecordInUseException,
			AuthorisationException {
		int amicusNumber = getAmicusNumber(itemIndex);

		EditBean bean;
		try {
			bean = super.prepareItemForEditing(getRecordView(itemIndex), // pm
					// 2011
					new Object[] {new Integer(amicusNumber),-1 }, request);
		} catch (ValidationException e) {
			throw new MarcCorrelationException(e);
		}
		/*
		 * modifica barbara 28/03/2007 ordinamento tag e editing true sulla
		 * modifica del record bib
		 */
		Locale currentLocale = SessionUtils.getCurrentLocale(request);
		activateEditor(bean, currentLocale);
		/* fine modifica barbara */
		return bean;
	}

	public EditBean prepareItemForEditingDuplicate(int itemIndex,
			HttpServletRequest request) throws DataAccessException,
            RecordInUseException {
		int amicusNumber = getAmicusNumber(itemIndex);

		EditBean bean = super.prepareItemForEditingDuplicate(new Object[] {
				new Integer(amicusNumber),
				new Integer(SessionUtils.getCataloguingView(request)) },
				request);

		Locale currentLocale = SessionUtils.getCurrentLocale(request);
		activateEditor(bean, currentLocale);

		return bean;
	}

	public EditBean restoreItem(HttpServletRequest request)
			throws DataAccessException {

		EditBean bean = EditBean.getInstance(request);
		bean.setCatalogItem(formerCatalogItem);
		Locale currentLocale = SessionUtils.getCurrentLocale(request);
		activateEditor(bean, currentLocale);
		return bean;
	}

	private void activateEditor(EditBean bean, Locale currentLocale) throws DataAccessException 
	{
		bean.setTagIndex(bean.getCatalogItem().getNumberOfTags() - 1);
		bean.getCatalogItem().sortTags();
		bean.refreshCorrelation(bean.getCurrentTag().getCorrelation(1), bean.getCurrentTag().getCorrelation(2), currentLocale);
		bean.setNavigation(true);
	}

	public String getOpenURLBase() {
		return Defaults.getString("openURL.resolver.baseURL");
	}

	public String getOpenURLImage() {
		return Defaults.getString("openURL.resolver.image");
	}

	public String buildOpenURL(int recordIndex) {
		logger.debug("building URL for index " + recordIndex);
		String result = getOpenURLBase() + "?sid=" + sid + "&pid="
				+ getAmicusNumber(recordIndex);
		Record record = getResultSet().getRecord(recordIndex, "F");
		try {
			result = result	+ record.toStyledDocument("F", "openURL.xsl",new Hashtable());
		} catch (Exception e) {
			logger.warn("Unable to parse record for openURL");
			throw new RuntimeException("Unable to parse record for openURL", e);
			// return result;
		}

        logger.debug("result is " + result);
		return result;
	}

	public int recordNumberToIndex(String recordNumber) {
		return Integer.parseInt(recordNumber) - 1;
	}

	public short getDisplayFormat() {
		if (displayFormat == null) {
			return -1;
		} else {
			return displayFormat.getCode();
		}
	}

	public void setDisplayFormat(short code) throws DataAccessException
	{
		short oldDisplayFormat = getDisplayFormat();
		/*if (code != oldDisplayFormat) {
			if (resultSet != null) {
				displayFormat = resultSet.getDisplayFormat(code, locale);
			} else {			
				displayFormat = (T_BIB_DSPLY_FRMT) new DAOCodeTable().load(T_BIB_DSPLY_FRMT.class, code, locale);
			}
		}*/
		if (oldDisplayFormat > 0) {
			/*
			 * If we're initialising the bean then the recordArrayList will be
			 * built within firstPage, but if we're changing the display format
			 * from the jsp, then we need to refetch and transform the data
			 */
			setRecordArrayList(null);
		}
	}

	public List getDisplayFormatList() {
		return resultSet.getDisplayFormatList(locale);
	}

	public T_ITM_DSPLY getDefaultDisplayFormat() {
		return defaultDisplayFormat;
	}

	public void setDefaultDisplayFormat(T_ITM_DSPLY t_itm_dsply) {
		defaultDisplayFormat = t_itm_dsply;
	}

	/**
	 * Metodo che crea un record di diverso formato (cartaceo, digitale)
	 * 
	 * @param itemNumber
	 * @param request
	 * @throws MarcCorrelationException
	 * @throws DataAccessException
	 * @throws RecordInUseException
	 * @throws AuthorisationException
	 * @throws NewTagException
	 * @throws ValidationException
	 */
	public void createOtherFormatRecord(final Session session, int itemNumber,	HttpServletRequest request, int target) throws DataAccessException, RecordInUseException, AuthorisationException, NewTagException, ValidationException
	{
		/* Genero una nuova id (amicus_nbr) per il record duplicato */
		EditBean editBean = prepareItemForEditing(itemNumber, request);
		CatalogItem item = editBean.getCatalogItem();
		item.getItemEntity().generateNewKey();
		item.getItemEntity().markNew();
		
		/* Ciclo tutti i tag del catalogo e li marco come nuovi per cambiare il numero (amicus_nbr) del record nell'ACS_PNT */
		List tagOther = new ArrayList();
		Tag aTag = null;
		Iterator iter = item.getTags().iterator();
		while (iter.hasNext()) {
			aTag = (Tag) iter.next();
			String marcTag = aTag.getMarcEncoding().getMarcTag();
			if (aTag instanceof PublisherManager) {
				((PublisherManager) aTag).setBibItemNumber(item.getAmicusNumber().intValue());
				((PublisherManager) aTag).getApf().setBibItemNumber(item.getAmicusNumber().intValue());
			} 
			if (aTag.isRelationship() || marcTag.equalsIgnoreCase("092") || marcTag.equalsIgnoreCase("097")	|| marcTag.equalsIgnoreCase("997")) {
				iter.remove();
			} else {
				aTag.markNew();
			}

			if (aTag.getMarcEncoding().getMarcTag().equalsIgnoreCase("856")) {
				BibliographicNoteTag noteTag = (BibliographicNoteTag) aTag;
				/* Se l'856 e' di tipo DOC oppure si tratta di 856 diversi da 346 -1 -1 si cancellano */
				if (DIGITAL_TEXT_RESOURCE.equalsIgnoreCase(noteTag.getStringText().getSubfieldsWithCodes("3").getDisplayText().trim())
						|| aTag.getCorrelation(1) != 346
						|| aTag.getCorrelation(2) != -1
						|| aTag.getCorrelation(3) != -1) {
					iter.remove();
				}
			}
			
			if (aTag.getMarcEncoding().getMarcTag().equalsIgnoreCase("008")) {
				aTag.setItemNumber(item.getAmicusNumber().intValue());
			}

			if ("F2".equalsIgnoreCase(editBean.getFormatRecordType())) {
				/* Solo per Casalini gestione tag 040 $b */
				if (customerEnabled){
					if (marcTag.equalsIgnoreCase("040")){
						CataloguingSourceTag cat040 = (CataloguingSourceTag)aTag;
						StringText st = cat040.getStringText().getSubfieldsWithCodes("b");
						if (st.getSubfieldList().size() > 0){
							if (st.getDisplayText().equals("ita")) {
			        			String lingua = "eng";
								StringText st2 = getStringText040(cat040.getStringText(), lingua, item);
								cat040.setStringText(st2);
							}
						}else{
							//$beng di default 
							String lingua = "eng";
							StringText st2 = cat040.getStringText();
							st2.addSubfield(new Subfield("b",lingua));
							item.getItemEntity().setLanguageOfCataloguing(lingua);
							item.getItemEntity().setCataloguingSourceStringText(st2.toString());
						}
					}
				}
				if (marcTag.equalsIgnoreCase("300")){
					duplicateTag300(tagOther, aTag, iter);
					
				}
				if (marcTag.equalsIgnoreCase("020")){
					Tag tag020new=duplicateTag020(aTag, item.getItemEntity().getAmicusNumber().toString(), editBean, request);
					if(tag020new!=null){
					  tagOther.add(tag020new);
					  iter.remove();
				 	}
				}
				if (marcTag.equalsIgnoreCase("006")||marcTag.equalsIgnoreCase("365")||marcTag.equalsIgnoreCase("991")){
					iter.remove();
				}
			}
		}
		
		Iterator iterOther = tagOther.iterator();
		while (iterOther.hasNext()) {
			Tag newTag = (Tag) iterOther.next();
			item.addTag(newTag);
		}
		
		/* Crea il tag di relazione 776 */
		createTag776Original(target, editBean, item.getUserView(), request);
		/* Aggiornamento data di creazione del record tag 008 */
		editBean.updateT008EnteredOnFileDate();

		MaterialDescription t008 = (MaterialDescription) editBean.getCatalogItem().findFirstTagByNumber("008");
		if (t008 != null) {
			/* if digital */
			if ("F2".equalsIgnoreCase(editBean.getFormatRecordType())) {
				t008.setFormOfItemCode(new Character('s'));
			} else {
				t008.setFormOfItemCode(new Character(' '));
			}
		}

		editBean.updateT005DateOfLastTransaction();

		if (customerEnabled && editBean.isEquivalentEnabled() && editBean.isElectronicResourceoOnTag008()) {
			createTag092(item.getAmicusNumber().intValue(), editBean, item,	request);
		}
		Locale currentLocale = SessionUtils.getCurrentLocale(request);
		activateEditor(editBean, currentLocale);
	}
	
	private void duplicateTag300(List tagOther, Tag aTag, Iterator iter) 
	{
		BibliographicNoteTag noteTag = (BibliographicNoteTag) aTag;
		BibliographicNoteTag aTagNew = (BibliographicNoteTag) LibrisuiteUtils.deepCopy(aTag);
		StringText text = noteTag.getStringText();
		Subfield sub = null;
		List listaSub = text.getSubfieldList();
		Iterator ite = listaSub.iterator();
		StringText st=new StringText();
		while (ite.hasNext()) {
			sub = (Subfield) ite.next();
			if (!sub.getCode().equalsIgnoreCase("c")){
     			st.addSubfield(sub);
			}
	    }
		
		if (st.getSubfieldList().size()>0){

			Subfield subfield = (Subfield) st.getSubfieldList().get(st.getSubfieldList().size()-1);
			String cont = subfield.getContent();
			if (cont.endsWith(" :") || cont.endsWith(" ;")) {
				String contentText = cont.substring(0, cont.length()-2);
				subfield.setContent(contentText);
			}
			st.removeSubfield(st.getSubfieldList().size()-1);
			st.addSubfield(subfield);
	
			aTagNew.setStringText(st);
			iter.remove();
			aTagNew.markNew();
			tagOther.add(aTagNew);
		}
	}

	public Tag duplicateTag020(Tag aTag, String amicusNumberNew, EditBean editBean, HttpServletRequest request) throws AuthorisationException, DataAccessException
	{
		ControlNumberAccessPoint tag020 = (ControlNumberAccessPoint) aTag;
		ControlNumberAccessPoint tag020new =null;
		StringText text = tag020.getStringText();
		Subfield sub = null;
		List listaSub = text.getSubfieldList();
		Iterator iter = listaSub.iterator();
		StringText st=new StringText();
		while (iter.hasNext()) {
			sub = (Subfield) iter.next();
			if (!sub.getCode().equalsIgnoreCase("c")){
     			st.addSubfield(sub);
			}
	    }
		
		if(st.getSubfieldList().size()>0){
		   tag020new = new ControlNumberAccessPoint(Integer.parseInt(amicusNumberNew));
		   CorrelationValues v = new CorrelationValues(tag020.getCorrelation(1), tag020.getCorrelation(2), tag020.getCorrelation(3));
		   tag020new.setCorrelationValues(v);
		   tag020new.setUserViewString(View.makeSingleViewString(editBean.getCatalogItem().getUserView()));
		   CNTL_NBR descriptor = (CNTL_NBR) tag020new.getDescriptor();
		   descriptor.setTypeCode(tag020new.getCorrelation(1));
		   MarcCommandLibrary.setNewStringText(tag020new, st, View.makeSingleViewString(editBean.getCatalogItem().getUserView()));
		}
		
		return tag020new;
	}
	
	private void createTag776Original(int target, EditBean editBean,
			int cataloguingView, HttpServletRequest request)
			throws NewTagException, AuthorisationException,
			DataAccessException,
            NumberFormatException, RecordInUseException {
		BibliographicRelationshipTag tag776 = (BibliographicRelationshipTag) editBean
				.newTag(0, (short) 8);
		CorrelationValues v = new CorrelationValues((short) 5, (short) 2,
				(short) -1);
		tag776.setCorrelationValues(v);
		tag776.setReciprocalOption((short) 1);
		tag776.setTargetBibItemNumber(target);
		StringText s = new StringText();
		DAOBibliographicRelationship b = new DAOBibliographicRelationship();
		s = b.buildRelationStringText(target, cataloguingView);
		tag776.setReciprocalStringText(s);

		int amicusNumberSource = Integer.parseInt(tag776.getStringText()
				.getSubfieldsWithCodes("w").toDisplayString());
		// System.out.println("Amicus number source : " + amicusNumberSource);
		tag776.getSourceRelationship().setBibItemNumber(
				editBean.getCatalogItem().getAmicusNumber().intValue());

		tag776.markNew();

		BibliographicEditBean bibBean = (BibliographicEditBean) BibliographicEditBean
				.getInstance(request);
		bibBean.updateRelationshipFromSearch(amicusNumberSource, SessionUtils
				.getCataloguingView(request));
	}

	public void duplicateRecord(final Session session, int itemNumber, HttpServletRequest request)
			throws DataAccessException,
			RecordInUseException, AuthorisationException, NewTagException,
			ValidationException {

		EditBean editBean = prepareItemForEditing(itemNumber, request);
		CatalogItem item = editBean.getCatalogItem();
		item.getItemEntity().generateNewKey();
		item.getItemEntity().markNew();

		Tag aTag = null;
		Tag newTag = null;
		List listaTag097 = new ArrayList();
		Iterator iter = item.getTags().iterator();
		while (iter.hasNext()) {
			aTag = (Tag) iter.next();
			if (aTag instanceof PublisherManager) {
				((PublisherManager) aTag).setBibItemNumber(item
						.getAmicusNumber().intValue());
				((PublisherManager) aTag).getApf().setBibItemNumber(
						item.getAmicusNumber().intValue());
			}

			if (aTag.isRelationship()
					|| aTag.getMarcEncoding().getMarcTag().equalsIgnoreCase(
							"856")
					|| aTag.getMarcEncoding().getMarcTag().equalsIgnoreCase("092")
					|| aTag.getMarcEncoding().getMarcTag().equalsIgnoreCase(
							"997"))

				iter.remove();
			else
				aTag.markNew();
			if (aTag.getMarcEncoding().getMarcTag().equalsIgnoreCase("097")) {
				listaTag097.add(duplicateTag097(aTag, item.getItemEntity()
						.getAmicusNumber().toString(), editBean, request));
				iter.remove();
			}
		}

		Iterator iterT097 = listaTag097.iterator();
		while (iterT097.hasNext()) {
			newTag = (Tag) iterT097.next();
			item.addTag(newTag);
		}

		editBean.updateT008EnteredOnFileDate();

		editBean.updateT005DateOfLastTransaction();

		if (customerEnabled && editBean.isEquivalentEnabled() && editBean.isElectronicResourceoOnTag008())
		{
			createTag092(item.getAmicusNumber().intValue(), editBean, item,
					request);
		}

		Locale currentLocale = SessionUtils.getCurrentLocale(request);
		activateEditor(editBean, currentLocale);
	}

	public Tag duplicateTag097(Tag aTag, String amicusNumberNew,
			EditBean editBean, HttpServletRequest request)
			throws AuthorisationException,
			DataAccessException {
		ControlNumberAccessPoint tag097 = (ControlNumberAccessPoint) aTag;
		StringText text = tag097.getStringText();

		Subfield sub = null;
		List listaSub = text.getSubfieldList();

		Iterator iter = listaSub.iterator();
		while (iter.hasNext()) {
			sub = (Subfield) iter.next();
			if (sub.getCode().equalsIgnoreCase("c")) {
				sub.setContent(amicusNumberNew);
				tag097.setStringText(new StringText(sub));
			} else if (sub.getCode().equalsIgnoreCase("d")
					|| sub.getCode().equalsIgnoreCase("e")
					|| sub.getCode().equalsIgnoreCase("f")) {
				sub.setContent("");
				tag097.setStringText(new StringText(sub));
			} else {
				tag097.setStringText(new StringText(sub));
			}
		}
		ControlNumberAccessPoint tag097new = new ControlNumberAccessPoint(
				Integer.parseInt(amicusNumberNew));
		CorrelationValues v = new CorrelationValues(tag097.getCorrelation(1),
				tag097.getCorrelation(2), tag097.getCorrelation(3));
		tag097new.setCorrelationValues(v);
		tag097new.setUserViewString(View.makeSingleViewString(editBean
				.getCatalogItem().getUserView()));
		CNTL_NBR descriptor = (CNTL_NBR) tag097new.getDescriptor();
		descriptor.setTypeCode(tag097new.getCorrelation(1));
		// MarcCommandLibrary.setNewStringText(tag097new, text,
		// View.makeSingleViewString(SessionUtils.getCataloguingView(request)));
		MarcCommandLibrary.setNewStringText(tag097new, text, View
				.makeSingleViewString(editBean.getCatalogItem().getUserView()));

		return tag097new;
	}

	public String getUserMailLibrarySymbol() {
		return userMailLibrarySymbol;
	}

	public void setUserMailLibrarySymbol(String userMailLibrarySymbol) {
		this.userMailLibrarySymbol = userMailLibrarySymbol;
	}

	public int getCataloguingMode() {
		return cataloguingMode;
	}

	public void setCataloguingMode(int cataloguingMode) {
		this.cataloguingMode = cataloguingMode;
	}

	public CatalogItem getFormerCatalogItem() {
		return formerCatalogItem;
	}

	public void setFormerCatalogItem(CatalogItem formerCatalogItem) {
		this.formerCatalogItem = formerCatalogItem;
	}

	public boolean isAmicusNumberSearch() {
		return amicusNumberSearch;
	}

	public void setAmicusNumberSearch(boolean b) {
		this.amicusNumberSearch = b;
	}

	public void setAmicusNumberSearch() {

		if (!(getCclQuery().trim().equals(""))) {
			if (getCclQuery().trim().substring(0, 2).equalsIgnoreCase("AN"))
				this.setAmicusNumberSearch(true);
			else
				this.setAmicusNumberSearch(false);
		}
	}

	public void createEquivalentRecord(int itemNumber, short indexingLanguage,
			HttpServletRequest request, int target)
			throws DataAccessException,
			RecordInUseException, NewTagException, AuthorisationException,
			EquivalentException, ValidationException {
		DAOSystemNextNumber dao = new DAOSystemNextNumber();
		Integer nextNumber = new Integer(dao.getNextNumber("BI"));
		EditBean editBean = prepareItemForEditingDuplicate(itemNumber, request);
		CatalogItem item = editBean.getCatalogItem();
		item.getItemEntity().setAmicusNumber(nextNumber);
		// item.getItemEntity().generateNewKey();
		String lingua = new DAOCodeTable()
				.getLanguageOfIndexing(indexingLanguage);
		String langOrig = item.getItemEntity().getLanguageOfCataloguing();

		if ((lingua).equals(item.getItemEntity().getLanguageOfCataloguing()
				.trim()))
			throw new EquivalentException();

		item.getItemEntity().setLanguageOfCataloguing(lingua);
		item.getItemEntity().markNew();

		Tag aTag = null;
		Tag newTag = null;
		Tag newTag2 = null;
		Iterator iter = item.getTags().iterator();
		List temp = new ArrayList();
		List tag300 = new ArrayList();

		BibliographicRelationshipTag tagRel = null;

		while (iter.hasNext()) {
			aTag = (Tag) iter.next();
			if (aTag.isRelationship()) {
				tagRel = (BibliographicRelationshipTag) aTag;
				if ("791".equals(tagRel.getMarcEncoding().getMarcTag())) {
					iter.remove();
				} else {
					item.getDeletedTags().add(aTag);
				}
			}
			if (aTag instanceof Equivalent) {
				List newTags = ((Equivalent) aTag).replaceEquivalentDescriptor(
						indexingLanguage, item.getUserView());

				if (!newTags.isEmpty()) {
					Tag aTagNew = null;
					aTagNew = (Tag) newTags.get(0);
					if (aTagNew instanceof PublisherManager) {
						((PublisherManager) aTagNew).setBibItemNumber(item
								.getAmicusNumber().intValue());
						((PublisherManager) aTagNew).getApf().setBibItemNumber(
								item.getAmicusNumber().intValue());
					}

					iter.remove();
					aTagNew.markNew();
					temp.add(aTagNew);
				}
			}
			getEquivalentCataloguingSourceTag(lingua, aTag);
			tag300 = getEquivalentNote(lingua, aTag, item.getUserView(), iter,
					tag300, langOrig, item);
			aTag.markNew();
		}

		Iterator iterTemp = temp.iterator();
		while (iterTemp.hasNext()) {
			newTag = (Tag) iterTemp.next();
			item.addTag(newTag);
		}
		Iterator iter300 = tag300.iterator();
		while (iter300.hasNext()) {
			newTag2 = (Tag) iter300.next();
			item.addTag(newTag2);
		}


		getTag791LanguageOriginal(target, editBean, item.getUserView());
		editBean.updateT008EnteredOnFileDate();

		editBean.updateT005DateOfLastTransaction();

		if (customerEnabled && editBean.isEquivalentEnabled() && editBean.isElectronicResourceoOnTag008()) 
		{
			createTag092(target, editBean, item, request);
		}
		
		Locale currentLocale = SessionUtils.getCurrentLocale(request);
		activateEditor(editBean, currentLocale);
	}

	public String createEquivalentRecordForExport(int itemNumber,
			short indexingLanguage, int target, int VW_T1)
			throws DataAccessException,
			RecordInUseException, NewTagException, AuthorisationException,
			EquivalentException, UnsupportedEncodingException {
		BibliographicCatalog catalog = new BibliographicCatalog();
		CatalogItem item = catalog.getCatalogItem(new Object[] {
				new Integer(itemNumber), new Integer(VW_T1) });
		String lingua = new DAOCodeTable()
				.getLanguageOfIndexing(indexingLanguage);
		String langOrig = item.getItemEntity().getLanguageOfCataloguing();
		item.getItemEntity().setLanguageOfCataloguing(lingua);
		item.getItemEntity().markNew();
		Tag aTag = null;
		Tag newTag = null;
		Tag newTag2 = null;
		Iterator iter = item.getTags().iterator();
		List temp = new ArrayList();
		List tag300 = new ArrayList();
		while (iter.hasNext()) {
			aTag = (Tag) iter.next();
			if (aTag.isRelationship())
				iter.remove();

			if (aTag instanceof Equivalent) {
				List newTags = ((Equivalent) aTag).replaceEquivalentDescriptor(
						indexingLanguage, item.getUserView());
				if (!newTags.isEmpty()) {
					Tag aTagNew = null;
					aTagNew = (Tag) newTags.get(0);
					if (aTagNew instanceof PublisherManager) {
						((PublisherManager) aTagNew).setBibItemNumber(item
								.getAmicusNumber().intValue());
						((PublisherManager) aTagNew).getApf().setBibItemNumber(
								item.getAmicusNumber().intValue());
					}

					iter.remove();
					aTagNew.markNew();
					temp.add(aTagNew);
				}
			}
			getEquivalentCataloguingSourceTag(lingua, aTag);
			tag300 = getEquivalentNote(lingua, aTag, item.getUserView(), iter,
					tag300, langOrig, item);
			if (aTag instanceof BibliographicNoteTag) {
				BibliographicNoteTag note = (BibliographicNoteTag) aTag;
				if (note.isStandardNoteType())
					note.getNote().setStringText(
							note.getStandardNoteStringText());
			}
			aTag.markNew();
		}

		Iterator iterTemp = temp.iterator();
		while (iterTemp.hasNext()) {
			newTag = (Tag) iterTemp.next();
			item.addTag(newTag);
		}
		Iterator iter300 = tag300.iterator();
		while (iter300.hasNext()) {
			newTag2 = (Tag) iter300.next();
			item.addTag(newTag2);
		}


		getTag791LanguageOriginalExport(target, item.getUserView(), item);
		item.sortTags();
		return new String(item.toMarc(), "UTF-8");
	}

	private List getEquivalentNote(String lingua, Tag aTag,
			int cataloguingView, Iterator iter, List tagList, String langOrig,
			CatalogItem item) throws DataAccessException {

		if (aTag instanceof BibliographicNoteTag) {
			BibliographicNoteTag note = (BibliographicNoteTag) aTag;

			StringText st = note.getStringText();
			// Caricamento nota standard
			if (note.isStandardNoteType()) {
				Avp valueElement = new DAOBibliographicStandardNote()
						.getSTDDisplayString(note.getNoteStandard()
								.getTypeCode(), lingua);
				// Se trova la nota standard nell'altra lingua setta la nuova
				if (valueElement != null) {
					note.setNoteStandard(note.loadNoteStandard(cataloguingView,
							lingua));
					note.getNoteStandard().markNew();
				}
				// altrimenti mette quella precedente
				else if (valueElement == null) {
					// Default Note Standard
					lingua = "und";

					CataloguingSourceTag cat040 = (CataloguingSourceTag) item
							.findFirstTagByNumber("040");
					StringText st2 = getStringText040(cat040.getStringText(),
							lingua, item);
					cat040.setStringText(st2);

					note.setNoteStandard(note.loadNoteStandard(cataloguingView,
							lingua));

					note.getNoteStandard().markNew();
				}
			}
			if (note.getNote().getNoteType() == 35) {
				st = getTranslationNoteList(note.getStringText(), lingua,
						langOrig);
				if (st.getSubfieldList().size() > 0) {
					BibliographicNoteTag aTagNew = (BibliographicNoteTag) LibrisuiteUtils
							.deepCopy(aTag);
					aTagNew.getNote().setStringText(st);
					iter.remove();
					aTagNew.markNew();
					tagList.add(aTagNew);
				}
			}
		}
		return tagList;
	}

	private void getEquivalentCataloguingSourceTag(String lingua, Tag aTag) {
		if (aTag instanceof CataloguingSourceTag) {
			StringText st = ((CataloguingSourceTag) aTag).getStringText();
			StringText b = st.getSubfieldsWithCodes("a");
			if (b.getSubfieldsWithCodes("b").getNumberOfSubfields() == 0) {
				b.addSubfield(new Subfield("b", lingua));
				((CataloguingSourceTag) aTag).setStringText(b);
			}

		}
	}

	private void getTag791LanguageOriginal(int target, EditBean editBean,
			int cataloguingView) throws NewTagException,
			AuthorisationException, DataAccessException {
		BibliographicRelationshipTag tag791 = (BibliographicRelationshipTag) editBean
				.newTag(0, (short) 8);

		CorrelationValues v = new CorrelationValues((short) 1, (short) 2,
				(short) -1);
		tag791.setCorrelationValues(v);
		tag791.setReciprocalOption((short) 1);
		tag791.setTargetBibItemNumber(target);
		StringText s = new StringText();
		DAOBibliographicRelationship b = new DAOBibliographicRelationship();
		s = b.buildRelationStringText(target, cataloguingView);
		tag791.setReciprocalStringText(s);
		tag791.markNew();
	}

	private void getTag791LanguageOriginalExport(int target,
			int cataloguingView, CatalogItem item) throws NewTagException,
			AuthorisationException, DataAccessException {
		BibliographicRelationshipTag tag791 = new BibliographicRelationshipTag();
		// BibliographicRelationshipTag tag791 = (BibliographicRelationshipTag)
		// .newTag(0, (short) 8);
		CorrelationValues v = new CorrelationValues((short) 1, (short) 2,
				(short) -1);
		tag791.setCorrelationValues(v);
		tag791.setReciprocalOption((short) 1);
		tag791.setTargetBibItemNumber(target);
		StringText s = new StringText();
		DAOBibliographicRelationship b = new DAOBibliographicRelationship();
		s = b.buildRelationStringText(target, cataloguingView);
		tag791.setReciprocalStringText(s);
		item.addTag(tag791);

	}

	private void createTag092(int target, EditBean editBean, CatalogItem item,
			HttpServletRequest request) throws NewTagException,
            DataAccessException, AuthorisationException,
			ValidationException {
		CataloguingSourceTag cat040 = (CataloguingSourceTag) item
				.findFirstTagByNumber("040");
		StringText st = cat040.getStringText().getSubfieldsWithCodes("b");
		if (st.getSubfieldList().size() > 0)
			if (st.getDisplayText().equals("eng")) {
				ClassificationAccessPoint tag092 = (ClassificationAccessPoint) editBean
						.newTag(0, (short) 6);
				CorrelationValues v = new CorrelationValues((short) 16,
						(short) 61, (short) -1);

				tag092.setCorrelationValues(v);
				editBean.refreshCorrelation(tag092.getCorrelation(1), tag092
						.getCorrelation(2), SessionUtils
						.getCurrentLocale(request));

				StringText text = new StringText(new Subfield("a", "" + target));
				MarcCommandLibrary.setNewStringText(tag092, text, View
						.makeSingleViewString(item.getUserView()));
				// item.addTag(cnap);
				editBean.validateCurrentTag();
				// sortTags(super.getLocale());
				editBean.resetCommands();
				editBean.setNavigation(true);
			}
	}

	public List getLanguageOfIndexingList() {
		List l = null;
		try {
			l = new DAOCodeTable().getOptionList(T_LANG_OF_ACS_PNT.class,
					locale);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Error retrieving codetable");
		}
		return l;
	}

	public StringText getTranslationNoteList(StringText text, String lingua,
			String langOrig) {
		StringText text2 = new StringText();
		try {
			for (int i = 0; i < text.getNumberOfSubfields(); i++) {
				Avp<String> valueElement2 = null;
				Avp<String> valueElement = null;
				T_TRSLTN_NTE_TYP t2 = null;
				T_DFLT_TRSLTN_NTE t3 = null;
				List result = null;
				Subfield s = text.getSubfield(i);
				String content = s.getContent();

				if (lingua.equals("eng"))
					valueElement2 = getDefaultTranslation(content, langOrig);

				if (valueElement2 == null) {
					valueElement = getFullTranslation(content, langOrig);
				}

				if (valueElement2 != null)
					result = new DAOCodeTable().getDefaultNoteTranslation(
							valueElement2.getValue(), lingua);
				else if (valueElement != null)
					result = new DAOCodeTable().getNoteTranslation(valueElement
							.getValue(), lingua);
				if (result != null) {
					if (result.size() > 0) {
						if (result.get(0) instanceof T_TRSLTN_NTE_TYP)
							t2 = (T_TRSLTN_NTE_TYP) result.get(0);
						else
							t3 = (T_DFLT_TRSLTN_NTE) result.get(0);

						if (valueElement2 != null)
							translationTag300(text2, content, s, valueElement2,
									lingua, t2, t3);
						else if (valueElement != null)
							translationTag300(text2, content, s, valueElement,
									lingua, t2, t3);

					}
					if (result.size() == 0)
						text2.addSubfield(s);
				}

				else
					text2.addSubfield(s);
			}

		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Error retrieving codetable");
		}
		return text2;
	}

	public void setLanguageOfIndexingList(List list) {
		languageOfIndexingList = list;
	}

	private Avp getDefaultTranslation(String content,
                                      String langOrig) throws DataAccessException {
		Avp t2 = null;

		// List l2 = new
		// DAOCodeTable().getDefaultNoteTranslationLanguage(langOrig);
		Locale local = getLocaleByISO3Language(langOrig);
		if (local == null)
			local = locale;
		List l2 = CasaliniCodeListsBean.getNoteDefaultTranslation()
				.getCodeList(local);
		Collections.sort(l2, new WeightedAvpComparator());
		Iterator ite2 = l2.iterator();
		while (ite2.hasNext()) {
			Avp t = (Avp) ite2.next();
			if (content.indexOf(t.getLabel()) != -1) {
				// ill.
				if (t.getLabel().equals("ill.")
						&& content.trim().equals("ill.")) {
					t2 = t;
					break;
				}
				// not ill.
				else if (!t.getLabel().equals("ill.")
						&& !content.trim().equals("ill.")) {
					t2 = t;
					break;
				}
			}

		}
		return t2;
	}

	private Avp getFullTranslation(String content, String langOrig)
			throws DataAccessException {
		Avp t2 = null;

		Locale local = getLocaleByISO3Language(langOrig);
		if (local == null)
			local = locale;
		List l = CasaliniCodeListsBean.getNoteTranslation().getCodeList(local);
		// List l = new DAOCodeTable().getNoteTranslationLanguage(langOrig);
		Collections.sort(l, new WeightedAvpComparator());
		Iterator ite = l.iterator();
		while (ite.hasNext()) {
			Avp t = (Avp) ite.next();
			if (content.indexOf(t.getLabel()) != -1) {
				t2 = t;
				break;
			}
		}
		return t2;
	}

	private void translationTag300(StringText text2, String content,
                                   Subfield s, Avp value, String lingua,
                                   T_TRSLTN_NTE_TYP t2, T_DFLT_TRSLTN_NTE t3)
			throws DataAccessException {
		if (content.indexOf(value.getLabel()) != -1) {
			if (t2 != null) {
				int pos = t2.getLongText().indexOf("(");
				int pos2 = t2.getLongText().indexOf(")");
				String word_equivalent = t2.getLongText();
				if (t2.getLongText().indexOf("(") != -1
						&& t2.getLongText().indexOf(")") != -1) {
					word_equivalent = t2.getLongText().substring(pos + 1, pos2);
				}
				if (content.indexOf(word_equivalent) != -1) {
					String app = content;
					app = app.replaceAll(value.getLabel(), "");

					if (app.indexOf(word_equivalent) != -1)
						word_equivalent = "";

				}
				// lo deve fare solo per i termini vuoti anche la sostituzione
				// delle parentesi
				if (value.getLabel().indexOf("(") != -1
						&& (value.getLabel().indexOf(")") != -1)
						&& word_equivalent.equals(""))
					s.setContent(content
							.replaceAll("\\(.*\\)", word_equivalent));
				else
					s.setContent(content.replaceAll(value.getLabel(),
							word_equivalent));

			}
			// Tabella DEFAULT
			else if (t3 != null) {
				int pos = t3.getLongText().indexOf("(");
				int pos2 = t3.getLongText().indexOf(")");
				String word_equivalent = t3.getLongText();
				if (t3.getLongText().indexOf("(") != -1
						&& t3.getLongText().indexOf(")") != -1) {
					word_equivalent = t3.getLongText().substring(pos + 1, pos2);
				}
				if (content.indexOf(word_equivalent) != -1) {
					String app = content;
					app = app.replaceAll(value.getLabel(), "");
					// controllo se c'è un altro termine uguale
					if (app.indexOf(word_equivalent) != -1)
						word_equivalent = "";
				}
				// lo deve fare solo per i termini vuoti anche la sostituzione
				// delle parentesi
				if (value.getLabel().indexOf("(") != -1
						&& (value.getLabel().indexOf(")") != -1)
						&& word_equivalent.equals(""))
					s.setContent(content
							.replaceAll("\\(.*\\)", word_equivalent));
				else
					s.setContent(content.replaceAll(value.getLabel(),
							word_equivalent));

			}

		}
		text2.addSubfield(s);
	}

	public List getAvailableLocales() {
		List availableLocales = new ArrayList();
		availableLocales.add(new Locale("ar"));
		availableLocales.add(new Locale("ar", "OM"));
		availableLocales.add(new Locale("en"));
		availableLocales.add(new Locale("en", "GB"));
		availableLocales.add(new Locale("en", "US"));
		availableLocales.add(new Locale("es"));
		availableLocales.add(new Locale("es", "ES"));
		availableLocales.add(new Locale("de", "DE"));
		availableLocales.add(new Locale("eu"));
		availableLocales.add(new Locale("eu", "ES"));
		availableLocales.add(new Locale("fr"));
		availableLocales.add(new Locale("fr", "BE"));
		availableLocales.add(new Locale("hu"));
		availableLocales.add(new Locale("it"));
		availableLocales.add(new Locale("nl"));
		availableLocales.add(new Locale("nl", "BE"));
		return availableLocales;
	}

	public Locale getLocaleByISO3Language(String language) {
		List l = getAvailableLocales();
		Iterator ite = l.iterator();
		Locale locale = null;
		while (ite.hasNext()) {
			Locale locale2 = (Locale) ite.next();
			if (locale2.getISO3Language().equals(language)) {
				locale = locale2;
				break;
			}
		}
		return locale;

	}

	// CLIMBING LISTS Giuseppe 25/5/2009
	// ----------------------------------------------------------------------------------------------------------------------------------------------------
	public void climbNtiList(String listName1) {
		Vector firstList = new Vector();
		if (listName1.equals("Delete")) {
			firstList = getItemToDeleteList();
		} else if (listName1.equals("Reserved")) {
			firstList = getReservedItemList();
		}

		for (int i = 0; i < ntiList.size(); i++) {

			// System.out.println("CLIMB NTI-------" + i);
			for (int ind = 0; ind < firstList.size(); ind++) {
				if (((RecordItem) ntiList.get(i)).getRecordNumber() == ((RecordItem) firstList
						.get(ind)).getRecordNumber()) {
					for (int indice = 0; indice < recordArrayList.size(); indice++) {
						if (((RecordItem) recordArrayList.get(indice)).recordNumber == ((RecordItem) ntiList
								.get(i)).getRecordNumber()) {
							((RecordItem) recordArrayList.get(indice))
									.setCheckNTI(false);
						}
					}

					ntiList.remove(i);
					i = i - 1;
					// System.out.println("remove
					// NTI-------"+((RecordItem)ntiList.get(i)).getRecordNumber());
					break;
				}
			}
		}
		boolean contr = false;
		for (int i = 0; i < ntiList.size(); i++) {
			int incr = 0;
			for (int indice = 0; indice < firstList.size(); indice++) {
				if (((RecordItem) ntiList.get(i)).getRecordNumber() > ((RecordItem) firstList
						.get(indice)).getRecordNumber()) {
					incr = incr + 1;
					// System.out.println("--------" + incr);
				}
			}
			for (int ind = 0; ind < firstList.size(); ind++) {
				if (((RecordItem) ntiList.get(i)).getRecordNumber() > ((RecordItem) firstList
						.get(ind)).getRecordNumber()) {
					for (int indice = 0; indice < getRecordArrayList().size(); indice++) {
						if (((RecordItem) getRecordArrayList().get(indice))
								.getRecordNumber() == ((RecordItem) ntiList
								.get(i)).getRecordNumber()) {
							((RecordItem) getRecordArrayList().get(indice))
									.setCheckNTI(false);
							((RecordItem) getRecordArrayList().get(
									indice - incr)).setCheckNTI(true);
							// System.out.println("INCR settaggio--------" +
							// incr);
							ntiList.set(i, getRecordArrayList().get(
									indice - incr));
							contr = true;
							break;
						}
					}
					if (contr == true) {
						contr = false;
						break;
					}
					if (((RecordItem) ntiList.get(i)).getRecordNumber() > getRecordArrayList()
							.size()) {
						for (int indice = 0; indice < getRecordArrayList()
								.size(); indice++) {
							if (((RecordItem) getRecordArrayList().get(indice))
									.getRecordNumber() == ((RecordItem) ntiList
									.get(i)).getRecordNumber()
									- incr
									&& listIsAlreadyClimbed == false) {
								ntiList
										.set(i, getRecordArrayList()
												.get(indice));
								logger.debug("settaggio pagine successive----"
										+ indice);
								break;
							}
						}
						if (((RecordItem) ntiList.get(i)).getRecordNumber() <= getRecordArrayList()
								.size()) {

							((RecordItem) getRecordArrayList().get(
									((RecordItem) ntiList.get(i))
											.getRecordNumber() - 1))
									.setCheckNTI(true);

							break;
						} else {

							if (!this.listIsAlreadyClimbed) {
								((RecordItem) ntiList.get(i))
										.setRecordNumber(((RecordItem) ntiList
												.get(i)).getRecordNumber()
												- incr);
								this.listIsAlreadyClimbed = true;
							} else {
								this.listIsAlreadyClimbed = false;
							}
						}
					}
				}
			}

		}
		setNtiListNumber(ntiList.size());
	}

	// ------------------------------------------------------------------------------------------------------------------------
	public void climbReservedList(String listName1) {
		Vector firstList = new Vector();
		if (listName1.equals("Delete")) {
			firstList = getItemToDeleteList();
		} else if (listName1.equals("Nti")) {
			firstList = getNtiList();
		}
		for (int i = 0; i < reservedItemList.size(); i++) {
			for (int ind = 0; ind < firstList.size(); ind++) {
				if (((RecordItem) reservedItemList.get(i)).getRecordNumber() == ((RecordItem) firstList
						.get(ind)).getRecordNumber()) {
					for (int indice = 0; indice < recordArrayList.size(); indice++) {
						if (((RecordItem) recordArrayList.get(indice)).recordNumber == ((RecordItem) reservedItemList
								.get(i)).getRecordNumber()) {
							((RecordItem) recordArrayList.get(indice))
									.setIsInTheReservedList("false");
						}
					}
					reservedItemList.remove(i);
					i = i - 1;
					break;
				}
			}
		}
		boolean contr = false;
		for (int i = 0; i < reservedItemList.size(); i++) {
			int incr = 0;

			for (int indice = 0; indice < firstList.size(); indice++) {
				if (((RecordItem) reservedItemList.get(i)).getRecordNumber() > ((RecordItem) firstList
						.get(indice)).getRecordNumber()) {
					incr = incr + 1;
				}
			}
			for (int ind = 0; ind < firstList.size(); ind++) {
				if (((RecordItem) reservedItemList.get(i)).getRecordNumber() > ((RecordItem) firstList
						.get(ind)).getRecordNumber()) {
					for (int indice = 0; indice < getRecordArrayList().size(); indice++) {
						if (((RecordItem) getRecordArrayList().get(indice))
								.getRecordNumber() == ((RecordItem) reservedItemList
								.get(i)).getRecordNumber()) {
							((RecordItem) getRecordArrayList().get(indice))
									.setIsInTheReservedList("false");
							((RecordItem) getRecordArrayList().get(
									indice - incr))
									.setIsInTheReservedList("true");
							reservedItemList.set(i, getRecordArrayList().get(
									indice - incr));
							contr = true;
							break;
						}
					}
					if (contr == true) {
						contr = false;
						break;
					}
					if (((RecordItem) reservedItemList.get(i))
							.getRecordNumber() > getRecordArrayList().size()) {
						for (int indice = 0; indice < getRecordArrayList()
								.size(); indice++) {
							if (((RecordItem) getRecordArrayList().get(indice))
									.getRecordNumber() == ((RecordItem) reservedItemList
									.get(i)).getRecordNumber()
									- incr
									&& listIsAlreadyClimbed == false) {
								reservedItemList.set(i, getRecordArrayList()
										.get(indice));
								break;
							}
						}
						if (((RecordItem) reservedItemList.get(i))
								.getRecordNumber() <= getRecordArrayList()
								.size()) {

							((RecordItem) getRecordArrayList().get(
									((RecordItem) reservedItemList.get(i))
											.getRecordNumber() - 1))
									.setIsInTheReservedList("true");
							break;
						} else {
							if (!this.listIsAlreadyClimbed) {
								((RecordItem) reservedItemList.get(i))
										.setRecordNumber(((RecordItem) reservedItemList
												.get(i)).getRecordNumber()
												- incr);
								this.listIsAlreadyClimbed = true;
							} else {
								this.listIsAlreadyClimbed = false;
							}
						}
					}
				}
			}

		}
		setReservedItemListNumber(reservedItemList.size());
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public void climbDeleteList(String listName1) {
		Vector firstList = new Vector();
		if (listName1.equals("Nti")) {
			firstList = getNtiList();
		} else if (listName1.equals("Reserved")) {
			firstList = getReservedItemList();
		}
		for (int i = 0; i < itemToDeleteList.size(); i++) {
			for (int ind = 0; ind < firstList.size(); ind++) {
				if (((RecordItem) itemToDeleteList.get(i)).getRecordNumber() == ((RecordItem) firstList
						.get(ind)).getRecordNumber()) {
					for (int indice = 0; indice < recordArrayList.size(); indice++) {
						if (((RecordItem) recordArrayList.get(indice)).recordNumber == ((RecordItem) itemToDeleteList
								.get(i)).getRecordNumber()) {
							((RecordItem) recordArrayList.get(indice))
									.setIsInTheItemToDeleteList("false");
						}
					}
					itemToDeleteList.remove(i);
					i = i - 1;
					break;
				}
			}
		}
		boolean contr = false;
		for (int i = 0; i < itemToDeleteList.size(); i++) {
			int incr = 0;

			for (int indice = 0; indice < firstList.size(); indice++) {
				if (((RecordItem) itemToDeleteList.get(i)).getRecordNumber() > ((RecordItem) firstList
						.get(indice)).getRecordNumber()) {
					incr = incr + 1;
				}
			}
			for (int ind = 0; ind < firstList.size(); ind++) {
				if (((RecordItem) itemToDeleteList.get(i)).getRecordNumber() > ((RecordItem) firstList
						.get(ind)).getRecordNumber()) {
					for (int indice = 0; indice < getRecordArrayList().size(); indice++) {
						if (((RecordItem) getRecordArrayList().get(indice))
								.getRecordNumber() == ((RecordItem) itemToDeleteList
								.get(i)).getRecordNumber()) {
							((RecordItem) getRecordArrayList().get(indice))
									.setIsInTheItemToDeleteList("false");
							((RecordItem) getRecordArrayList().get(
									indice - incr))
									.setIsInTheItemToDeleteList("true");
							itemToDeleteList.set(i, getRecordArrayList().get(
									indice - incr));
							contr = true;
							break;
						}
					}
					if (contr == true) {
						contr = false;
						break;
					}
					if (((RecordItem) itemToDeleteList.get(i))
							.getRecordNumber() > getRecordArrayList().size()) {
						for (int indice = 0; indice < getRecordArrayList()
								.size(); indice++) {
							if (((RecordItem) getRecordArrayList().get(indice))
									.getRecordNumber() == ((RecordItem) itemToDeleteList
									.get(i)).getRecordNumber()
									- incr
									&& listIsAlreadyClimbed == false) {
								itemToDeleteList.set(i, getRecordArrayList()
										.get(indice));
								break;
							}
						}
						if (((RecordItem) itemToDeleteList.get(i))
								.getRecordNumber() <= getRecordArrayList()
								.size()) {

							((RecordItem) getRecordArrayList().get(
									((RecordItem) itemToDeleteList.get(i))
											.getRecordNumber() - 1))
									.setIsInTheItemToDeleteList("true");
							break;
						} else {
							if (!this.listIsAlreadyClimbed) {
								((RecordItem) itemToDeleteList.get(i))
										.setRecordNumber(((RecordItem) itemToDeleteList
												.get(i)).getRecordNumber()
												- incr);
								this.listIsAlreadyClimbed = true;
							} else {
								this.listIsAlreadyClimbed = false;
							}
						}
					}
				}
			}

		}
		setItemToDeleteListNumber(itemToDeleteList.size());
	}

	// FINE CLIMBING LISTS
	// ------------------------------------------------------------------------------------------------------------------------------------------------

	public void updateNtiList(ResultSummaryForm resultSummaryForm) {
		// CANCELLO DALLA LISTA NTI TUTTI GLI ITEM CHE SONO STATI DESELEZIONATI
		// QUANDO DESELEZIONO TUTTI GLI NTI DI UNA PAGINA CLICCANDO IL PULSANTE
		// CHECK/UNCHECK NTI-----------------------------------
		if ((!resultSummaryForm.getRecordToDeleteFromNTIList().equals(""))
				&& resultSummaryForm.getRecordToDeleteFromNTIList() != null) {

			String[] arrayDeleteRecords = resultSummaryForm
					.getRecordToDeleteFromNTIList().split(",");
			for (int ind = 0; ind < arrayDeleteRecords.length; ind++) {
				if (!arrayDeleteRecords[ind].equals("")) {
					for (int i = 0; i < getNtiList().size(); i++) {

						if (((RecordItem) getNtiList().get(i))
								.getRecordNumber() == Integer
								.parseInt(arrayDeleteRecords[ind])) {
							deleteFromNti(arrayDeleteRecords[ind]);
							if (resultSummaryForm.getCheckNTI() != null) {
								// resultSummaryForm.removeElementFromCheckNTI(arrayDeleteRecords[ind]);
								// resultSummaryForm.setCheckNTI(null);
								if (resultSummaryForm.getCheckNTI() != null) {
									for (int index = 0; index < resultSummaryForm
											.getCheckNTI().length; index++) {

										if (arrayDeleteRecords[ind]
												.equals(resultSummaryForm
														.getCheckNTI()[index])) {
											resultSummaryForm.getCheckNTI()[index] = null;
											break;
										}
									}
								}
								break;
							}
							setNtiListNumber(getNtiList().size());
							break;
						}
					}
				}
			}
		}

	}

	public void updateDeleteList(ResultSummaryForm resultSummaryForm) {
		if ((!resultSummaryForm.getRecordToDeleteFromDeleteList().equals(""))
				&& resultSummaryForm.getRecordToDeleteFromDeleteList() != null) {
			String[] arrayDeleteRecords = resultSummaryForm
					.getRecordToDeleteFromDeleteList().split(",");
			for (int ind = 0; ind < arrayDeleteRecords.length; ind++) {
				if (!arrayDeleteRecords[ind].equals("")) {
					for (int i = 0; i < getItemToDeleteList().size(); i++) {

						if (((RecordItem) getItemToDeleteList().get(i))
								.getRecordNumber() == Integer
								.parseInt(arrayDeleteRecords[ind])) {
							deleteFromItemsToDelete(arrayDeleteRecords[ind]);
							setItemToDeleteListNumber(getItemToDeleteList()
									.size());
							break;
						}
					}
				}
			}
		}
	}

	public void updateReservedList(ResultSummaryForm resultSummaryForm) {
		// CANCELLO DALLA LISTA DEI TRASFERIMENTI TUTTI GLI ITEM CHE SONO STATI
		// DESELEZIONATI
		// QUANDO DESELEZIONO TUTTI I CHECK TRASFERISCI DI UNA PAGINA CLICCANDO
		// IL PULSANTE CHECK/UNCHECK
		// Trasferisci-----------------------------------
		if ((!resultSummaryForm.getRecordToDeleteFromTransfList().equals(""))
				&& resultSummaryForm.getRecordToDeleteFromTransfList() != null) {
			String[] arrayTransfRecords = resultSummaryForm
					.getRecordToDeleteFromTransfList().split(",");
			for (int ind = 0; ind < arrayTransfRecords.length; ind++) {
				if (!arrayTransfRecords[ind].equals("")) {
					for (int i = 0; i < getReservedItemList().size(); i++) {

						if (((RecordItem) getReservedItemList().get(i))
								.getRecordNumber() == Integer
								.parseInt(arrayTransfRecords[ind])) {
							deleteFromReservedItems(arrayTransfRecords[ind]);
							setReservedItemListNumber(getReservedItemList()
									.size());
							break;
						}
					}
				}
			}
		}
	}

	public void setNTIInTheBean() throws
            LibrisuiteException {
		DAOCasCache daoCCache = new DAOCasCache();

		int cont = 0;
		CasCache cCache;
		for (int i = 0; i < this.getRecordArrayList().size(); i++) {
			try {
				int recNumber = this.getResultSet()
						.getAmicusNumber(i).intValue();

				cCache = daoCCache.getCasCache(recNumber);
				if (cCache != null && cCache.getFlagNTI() != null) {
					if (cCache.getFlagNTI().equalsIgnoreCase("Y")) {
						cont = cont + 1;
						// ((RecordItem)
						// this.getResultSummaryBean().getRecordArrayList().get(i)).setCheckNTI(true);
						this.setNTIItems((RecordItem) this.getRecordArrayList()
								.get(i));
					}
				}
			} catch (DataAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		this.setNtiListNumber(cont);
	}

	public int getTotalRecordList() {
		return totalRecordList;
	}

	public void setTotalRecordList(int totalRecordList) {
		this.totalRecordList = totalRecordList;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Vector getReservedItemList() {
		return reservedItemList;
	}

	public void setReservedItemList(Vector reservedItemList) {
		this.reservedItemList = reservedItemList;
	}

	public Vector getItemToDeleteList() {
		return itemToDeleteList;
	}

	public void setItemToDeleteList(Vector itemToDeleteList) {
		this.itemToDeleteList = itemToDeleteList;
	}

	public StringText getStringText040(StringText st, String lingua,
			CatalogItem item) {
		for (int i = 0; i < st.getSubfieldList().size(); i++) {
			Subfield sf = (Subfield) st.getSubfieldList().get(i);

			if (sf != null && !sf.getContent().trim().equals(""))
				if (sf.getCode().equals("b")) {
					item.getItemEntity().setLanguageOfCataloguing(lingua);
					sf.setContent(lingua);
					// st.removeSubfield(i);
					break;
				} else
					item.getItemEntity().setLanguageOfCataloguing("   ");
		}
		item.getItemEntity().setCataloguingSourceStringText(st.toString());
		return st;
	}

	public int getReservedItemListNumber() {
		return reservedItemListNumber;
	}

	public void setReservedItemListNumber(int reservedItemListNumber) {
		this.reservedItemListNumber = reservedItemListNumber;
	}

	public int getItemToDeleteListNumber() {
		return itemToDeleteListNumber;
	}

	public void setItemToDeleteListNumber(int itemToDeleteListNumber) {
		this.itemToDeleteListNumber = itemToDeleteListNumber;
	}

	public Vector getNtiList() {
		return ntiList;
	}

	public void setNtiList(Vector ntiList) {
		this.ntiList = ntiList;
	}

	public int getNtiListNumber() {
		return ntiListNumber;
	}

	public void setNtiListNumber(int ntiListNumber) {
		this.ntiListNumber = ntiListNumber;
	}

	public String getCheckUncheckDelete() {
		return checkUncheckDelete;
	}

	public void setCheckUncheckDelete(String checkUncheckDelete) {
		this.checkUncheckDelete = checkUncheckDelete;
	}

	public String getCheckUncheckNti() {
		return checkUncheckNti;
	}

	public void setCheckUncheckNti(String checkUncheckNti) {
		this.checkUncheckNti = checkUncheckNti;
	}

	public String getCheckUncheckTransf() {
		return checkUncheckTransf;
	}

	public void setCheckUncheckTransf(String checkUncheckTransf) {
		this.checkUncheckTransf = checkUncheckTransf;
	}

	public boolean isSearchingMother() {
		return searchingMother;
	}

	public void setSearchingMother(boolean searchingMother) {
		this.searchingMother = searchingMother;
	}

	public boolean isListIsAlreadyClimbed() {
		return listIsAlreadyClimbed;
	}

	public void setListIsAlreadyClimbed(boolean listIsAlreadyClimbed) {
		this.listIsAlreadyClimbed = listIsAlreadyClimbed;
	}

	public String getSourceFilter() {
		return sourceFilter;
	}

	public void setSourceFilter(String sourceFilter) {
		this.sourceFilter = sourceFilter;
	}

	public boolean isArchive() {
		return getResultSet().isArchive();
	}

	// --> 20100819 inizio: metodi per modificare i bookmarks dei pdf caricati
	// nel repository
	public ControlNumberAccessPoint getTag097(EditBean editBean)
			throws DigitalTagFormatException {
		ControlNumberAccessPoint tag097 = (ControlNumberAccessPoint) editBean
				.getCatalogItem().findFirstTagByNumber("097");
		if (tag097 == null) {
			throw new DigitalTagFormatException("error.tag.097");
		}
		if (!(tag097.getStringText().getSubfieldsWithCodes("d")
				.getDisplayText().toString().trim().length() > 0)
				|| !(tag097.getStringText().getSubfieldsWithCodes("a")
						.getDisplayText().toString().trim().length() > 0)) {
			throw new DigitalTagFormatException("error.tag.097");
		}
		return tag097;
	}

	// --> 20100819: metodi per modificare i bookmarks dei pdf caricati nel
	// repository
	public String getPhysicalPath(EditBean editBean, int amicusNumberMother)
			throws HibernateException, DataAccessException,
			DigitalTagFormatException {
		BibliographicNoteTag noteTag = null;
		String pathRelativo = null;
		String fileName = null;
		StringBuffer buffer = new StringBuffer();
		String DIGITAL_TEXT_RESOURCE = Defaults
				.getString("digital.text.resource");

		DAODigital daoDigital = new DAODigital();
		List lista = daoDigital.getBibliographicNotes856(amicusNumberMother, 1,
				"");

		if (lista == null || (!(lista.size() > 0))) {
			throw new DigitalTagFormatException("error.tag.856");
		}

		getHomeRepositoryProc();

		boolean DOC856Found = false;

		// Scorre gli 856 fino a che non trova quello di tipo DOC che punta al
		// pdf - se non lo trova errore
		for (int i = 0; i < lista.size(); i++) {
			noteTag = (BibliographicNoteTag) lista.get(i);
			if (DIGITAL_TEXT_RESOURCE.equalsIgnoreCase(noteTag.getStringText()
					.getSubfieldsWithCodes("3").getDisplayText().toString())
					&& noteTag.getStringText().getSubfieldsWithCodes("u")
							.getDisplayText().toString().length() > 0
					&& noteTag.getStringText().getSubfieldsWithCodes("d")
							.getDisplayText().toString().length() > 0
					&& noteTag.getStringText().getSubfieldsWithCodes("f")
							.getDisplayText().toString().length() > 0) {

				DOC856Found = true;
				String fieldF = noteTag.getStringText().getSubfieldsWithCodes(
						"f").getDisplayText().toString();
				if (!("pdf".equalsIgnoreCase(fieldF.substring(fieldF
						.indexOf(".") + 1)))) {
					throw new DigitalTagFormatException("error.tag.856");
				}
				pathRelativo = noteTag.getStringText().getSubfieldsWithCodes(
						"d").getDisplayText().toString();
				fileName = noteTag.getStringText().getSubfieldsWithCodes("f")
						.getDisplayText().toString();
				buffer = new StringBuffer();
				buffer.append(getDIGITAL_CONTEXT()).append("/").append(
						pathRelativo).append("/").append(fileName);
				break;
			}
		}

		if (DOC856Found == false) {
			throw new DigitalTagFormatException("error.tag.856");
		}

		return buffer.toString();
	}

	// --> 20100819: metodi per modificare i bookmarks dei pdf caricati nel
	// repository
	public void getHomeRepositoryProc() throws DataAccessException {
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
					throws SQLException, HibernateException,
					UploadFileDigitalException {
				CallableStatement proc = null;
				String DIG_HOME_REPOSITORY = Defaults
						.getString("digital.home.rep");
				String home = "";
				try {
					Connection connection = s.connection();
					proc = connection.prepareCall("{ ? = call "
							+ DIG_HOME_REPOSITORY + "() }");
					proc.registerOutParameter(1, Types.CHAR);
					proc.execute();
					home = proc.getString(1);
					if (home == null) {
						throw new UploadFileDigitalException(
								"error.digital.loadedFile");
					}
					setDIGITAL_CONTEXT(home);

				} finally {
					try {
						if (proc != null)
							proc.close();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
			}
		}.execute();
	}

	public void processingPdfBox(String pathFile, int progressivo,
			String newBookmark) throws IOException {
		PDDocument doc = null;

		try {
			doc = PDDocument.load(new File(pathFile));
			PDDocumentOutline root = doc.getDocumentCatalog()
					.getDocumentOutline();
			if (root == null) {
				logger.debug("The PDF does not contain any bookmarks : "
						+ pathFile);
			} else {
				PDOutlineItem item = root.getFirstChild();
				while (item != null) {
					// logger.info("Titolo primo livello :" + item.getTitle());
					// --------------> Prendo la parte numerica prima del #
					int end = item.getTitle().trim().indexOf("#");
					if (end != -1 && end != 0) {
						try {
							int progrPdf = Integer.parseInt(item.getTitle()
									.trim().substring(0, end));
							if (progrPdf == progressivo) {
								// logger.info("Titolo modificato con : " +
								// newBookmark);
								item.setTitle(newBookmark);
							}
						} catch (NumberFormatException e) {
							logger.error("Problemi con formato bookmark --> "
									+ item.getTitle());
						}
					}
					// 20110530 fine
					PDOutlineItem child = item.getFirstChild();
					// scorriBookmarks(child, bookmarkToReplace, newBookmark);
					scorriBookmarks(child, progressivo, newBookmark);
					item = item.getNextSibling();
				}
				root.openNode();
				doc.save(pathFile);
			}
		} finally {
			if (doc != null)
				doc.close();
		}
	}

	private static void scorriBookmarks(PDOutlineItem child, int progressivo,
			String newBookmark) {
		PDOutlineItem childSubLevel = null;
		while (child != null) {
			logger.debug("Titolo Child:" + child.getTitle());
			// --------> Prendo la parte numerica prima del #
			int end = child.getTitle().trim().indexOf("#");
			if (end != -1 && end != 0) {
				try {
					int progrPdf = Integer.parseInt(child.getTitle().trim()
							.substring(0, end));
					if (progrPdf == progressivo) {
						// logger.info("Titolo child modificato con : "+
						// newBookmark);
						child.setTitle(newBookmark);
					}
				} catch (NumberFormatException e) {
					logger.error("Problemi con formato bookmark --> "
							+ child.getTitle());
				}
			}

			childSubLevel = child.getFirstChild();
			if (childSubLevel != null) {
				scorriBookmarks(childSubLevel, progressivo, newBookmark);
			}
			child = child.getNextSibling();
		}
	}

	public String getAuthors(EditBean editBean)
			throws DataAccessException {
		StringBuffer buffer = new StringBuffer();
		List appo = ((BibliographicItem) editBean.getCatalogItem())
				.getOrderableNames();
		Iterator it = appo.iterator();
		while (it.hasNext()) {
			NameAccessPoint name = (NameAccessPoint) it.next();
			buffer.append(name.getDescriptor().getDisplayText());
			if (it.hasNext()) {
				buffer.append(" ; ");
			}
		}
		return buffer.toString();
	}

	public String getTitle(EditBean editBean) throws
            DataAccessException {
		String title = "";
		TitleAccessPoint t245 = (TitleAccessPoint) editBean.getCatalogItem()
				.findFirstTagByNumber("245");
		if (t245 != null) {
			title = t245.getDescriptor().getDisplayText() + " "
					+ t245.getAccessPointStringText().getDisplayText();
		}
		return title;
	}

	// --> 20100819 fine

	public boolean isInitialResearch() {
		return initialResearch;
	}

	public void setInitialResearch(boolean initialResearch) {
		this.initialResearch = initialResearch;
	}

	public void createVariantResultSet(int amicusNumber)
			throws DataAccessException {
		logger.debug("createVariantResultSet(" + amicusNumber + ")");
		List/* <Integer> */variantViews = new DAOCache()
				.getVariantViews(amicusNumber);
		AmicusResultSet myResultSet = new AmicusResultSet(amicusNumber,
				variantViews, resultSet.getSearchEngine());
		replaceResultSet(myResultSet);
	}

	// TODO: ANDREA COMMENT
	public boolean isOrdersForRecord(HttpServletRequest request) 
	{
		throw new IllegalArgumentException("DONT CALL ME!");
/*		if(!functionsDisabled)
			return false;
		boolean isOrdersForRecord = false;
		OrderController orderController = (OrderController) request.getHttpSession().getAttribute("orderController");
		orderController.setAmicusNumber(String.valueOf(getAmicusNumber(0)));
		isOrdersForRecord = orderController.isOrderForRecord(getAmicusNumber(0));
		return isOrdersForRecord;
		*/
	}

	/*
	 * public String getISBDString(String content){
	 * this.setIsbdFormatText(content); try {
	 * this.setDisplayFormat(Short.parseShort("1")); } catch
	 * (NumberFormatException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (DataAccessException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return
	 * getIsbdFormatText(); }
	 */

	/**
	 * pm 2011
	 * 
	 * @param cataloguingView
	 *            the cataloguingView to set
	 */
	public void setCataloguingView(int cataloguingView) {
		this.cataloguingView = cataloguingView;
	}


	/**
	 * pm 2011
	 * 
	 * @return the cataloguingView
	 */
	public int getCataloguingView() {
		return cataloguingView;
	}
	


	/*
	 * public String getISBDString(String content){
	 * this.setIsbdFormatText(content); try {
	 * this.setDisplayFormat(Short.parseShort("1")); } catch
	 * (NumberFormatException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (DataAccessException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return
	 * getIsbdFormatText(); }
	 */
   
	private String insertTags(String content,Integer amicusNumber,String symbolCode) throws DataAccessException, UnsupportedEncodingException {
		org.marc4j.marc.Record result = null;
		MarcReader reader = new MarcPermissiveStreamReader(new ByteArrayInputStream(content.getBytes()),true,false,"UTF-8");
		result = reader.next();
		
		try{
			setTag850(result,symbolCode);
			setTag852(result, amicusNumber);
		}catch(Exception e){}
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		MarcWriter writer = new MarcStreamWriter(out,"UTF-8");

		writer.write(result);
		writer.close();

		return out.toString("UTF-8");
	}

	private void setTag850(org.marc4j.marc.Record record,String symbolCode) throws DataAccessException {
		MarcFactory factory = MarcFactory.newInstance();
		DataField df = factory.newDataField("850", ' ', ' ');
		df.addSubfield(factory.newSubfield('a',symbolCode));
		record.addVariableField(df);
	}
	
	private void setTag852(org.marc4j.marc.Record record,Integer amicusNumber) {
		
		DAOCopy daoCopy = new DAOCopy();
		DAOOrganisationHierarchy lib = new DAOOrganisationHierarchy();
		DAOShelfList shlf = new DAOShelfList();
		MarcFactory factory = MarcFactory.newInstance();
		
		
		try {
			List l = daoCopy.getAllCopiesByMainLibrary(amicusNumber, getUserProfile().getMainLibrary());
			for(int i = 0; i < l.size(); i++)
			{
				
				DataField df = factory.newDataField("852", ' ', ' ');
				CPY_ID cpy = (CPY_ID)l.get(i);
				df.addSubfield(factory.newSubfield('c',lib.getLibOrBranchSymbol(cpy.getBranchOrganisationNumber())));
				df.addSubfield(factory.newSubfield('m',shlf.loadShelf(cpy.getShelfListKeyNumber()).getDisplayText()));
				record.addVariableField(df);
			}
			
		} catch (DataAccessException e) {
		}
		
	}

	public String getTabClassRecord() {
		return tabClassRecord;
	}

	public void setTabClassRecord(String tabClassRecord) {
		this.tabClassRecord = tabClassRecord;
	}

	public String getTabClassCollections() {
		return tabClassCollections;
	}

	public void setTabClassCollections(String tabClassCollections) {
		this.tabClassCollections = tabClassCollections;
	}

	public String getTabClassHierarchy() {
		return tabClassHierarchy;
	}

	public void setTabClassHierarchy(String tabClassHierarchy) {
		this.tabClassHierarchy = tabClassHierarchy;
	}

	public String getTabClassLibriLink() {
		return tabClassLibriLink;
	}

	public void setTabClassLibriLink(String tabClassLibriLink) {
		this.tabClassLibriLink = tabClassLibriLink;
	}

	public String getTabClassCodes() {
		return tabClassCodes;
	}

	public void setTabClassCodes(String tabClassCodes) {
		this.tabClassCodes = tabClassCodes;
	}

	public String getTabClassRda() {
		return tabClassRda;
	}

	public void setTabClassRda(String tabClassRda) {
		this.tabClassRda = tabClassRda;
	}
	public void refineTabClass(HttpServletRequest request)
	{
		int value = 1;
		if(request.getParameter("name_li") != null && request.getParameter("name_li").indexOf("_") != -1)
			value = Integer.parseInt(request.getParameter("name_li").split("_")[1]);
		ResultSummaryBean bean = (ResultSummaryBean) LibrisuiteBean.getSessionAttribute(request,ResultSummaryBean.class);
		clearTabsClass();
		switch (value) {
			case 1:  bean.setTabClassRecord("selected");break;
			case 2:  bean.setTabClassCollections("selected"); break;	
			case 3:  bean.setTabClassHierarchy("selected"); break;
			case 4:  bean.setTabClassLibriLink("selected");break;
			case 5:  bean.setTabClassCodes("selected"); break;
			case 6:  bean.setTabClassRda("selected");break;	
			case 7:  bean.setTabClassOrders("selected");break;	
			case 8:  bean.setTabClassCopies("selected");break;	
			case 9:  bean.setTabClassStatusCopies("selected");break;	
			case 10: bean.setTabClassFrbr("selected");break;
			case 11: bean.setTabClassDiscarded("selected");break;
			case 12: bean.setTabClassRecord("selected");break;
		}
	}

	public void clearTabsClass() {
		setTabClassCodes(null);
		setTabClassCollections(null);
		setTabClassHierarchy(null);
		setTabClassLibriLink(null);
		setTabClassRda(null);
		setTabClassRecord(null);
		setTabClassOrders(null);
		setTabClassCopies(null);
		setTabClassStatusCopies(null);
		setTabClassFrbr(null);
		setTabClassDiscarded(null);
	}

	public String getTabClassCopies() {
		return tabClassCopies;
	}

	public void setTabClassCopies(String tabClassCopies) {
		this.tabClassCopies = tabClassCopies;
	}

	public String getTabClassOrders() {
		return tabClassOrders;
	}

	public void setTabClassOrders(String tabClassOrders) {
		this.tabClassOrders = tabClassOrders;
	}

	public String getTabClassStatusCopies() {
		return tabClassStatusCopies;
	}

	public void setTabClassStatusCopies(String tabClassStatusCopies) {
		this.tabClassStatusCopies = tabClassStatusCopies;
	}

	public String getTabClassFrbr() {
		return tabClassFrbr;
	}

	public void setTabClassFrbr(String tabClassFrbr) {
		this.tabClassFrbr = tabClassFrbr;
	}

	public boolean isRDA() {
		return isRDA;
	}

	public void setRDA(boolean isRDA) {
		this.isRDA = isRDA;
	}

	public int getFrbrType() {
		return frbrType;
	}

	public void setFrbrType(int frbrType) {
		this.frbrType = frbrType;
	}

	public ArrayList getFrbrRecordArrayList() {
		return frbrRecordArrayList;
	}


	public CART_ITEMS loadRecordsForCart(int indexRecord, int branchId, String userName) 
	{
		Record record = getResultSet().getRecord()[indexRecord];
		Integer amicusNumber = getAmicusNumber(indexRecord);
		
		String author = null;
		String title = null;
		String description = null;
		String publisher = null;
		String isbnIssn = null;
		String edition = null;
		Leader leader = null;
		String leaderPos6 = null;
		String leaderPos7 = null;
		
		try 
		{
			author =  ExtractorFieldTag.getFiedValue((String)record.getContent("F"), TagConstant.AUTHORS_TAG);
			title = ExtractorFieldTag.getFiedValue((String)record.getContent("F"), TagConstant.TITLES_TAG);
			description =  ExtractorFieldTag.getFiedValue((String)record.getContent("F"), "300", 'a');
			publisher =  ExtractorFieldTag.getFiedValue((String)record.getContent("F"), TagConstant.PUBLISHERS_TAG);
			isbnIssn =  ExtractorFieldTag.getFiedValue((String)record.getContent("F"), TagConstant.ISBN_ISSN_TAG);
			edition =  ExtractorFieldTag.getFiedValue((String)record.getContent("F"), TagConstant.EDITIONS_TAG);
			leader =  ExtractorFieldTag.getLeader((String)record.getContent("F"));
	
		} catch (MarcException e) {
			logger.error("Metodo LoadRecordsForCart: Problemi nel marc per il record " + amicusNumber + " che e' stato scartato");
			logger.error(e.getMessage());
			throw new MarcException(e.getMessage());
		}
		
		CART_ITEMS cartElement = new CART_ITEMS();
		cartElement.setBibItemNumber(amicusNumber);
		cartElement.setBranchId(branchId);
		cartElement.setAuthor(author);
		cartElement.setTitle(title);
		cartElement.setPublisher(publisher);
		cartElement.setDescriptionTag300(description);
		cartElement.setIsbnIssn(isbnIssn);
		if (leader!=null){
			try { 
				leaderPos6 = Character.toString(leader.getTypeOfRecord()); 
			} catch (Exception e) {
				logger.error("Errore nella leader del record posizione 6 " + amicusNumber + " per la scrittura nel carrello");
				leaderPos6=null;
			}
			try { 
				leaderPos7 = Character.toString(leader.getImplDefined1()[0]); 
			} catch (Exception e) {
				logger.error("Errore nella leader del record posizione 7 " + amicusNumber + " per la scrittura nel carrello");
				leaderPos7=null;
			}
		}
		cartElement.setLeaderPos6(leaderPos6);
		cartElement.setIsJournals("s".equalsIgnoreCase(leaderPos7)?"Y":"N");
		cartElement.setUserName(userName);
		
		return cartElement;
	}

	public Integer getFirstAmicusNumber()
	{
		return getAmicusNumber(0);
	}
	
	public String getTabClassDiscarded() {
		return tabClassDiscarded;
	}

	public void setTabClassDiscarded(String tabClassDiscarded) {
		this.tabClassDiscarded = tabClassDiscarded;
	}

	public String getRecordURL() {
		return recordURL;
	}

	public void setRecordURL(String recordURL) {
		this.recordURL = recordURL;
	}

	public List<Integer> getRecordsDiscarded() {
		return recordsDiscarded;
	}

	public void setRecordsDiscarded(List<Integer> recordsDiscarded) {
		this.recordsDiscarded = recordsDiscarded;
	}

	public  boolean isFunctionsDisabled() {
		return functionsDisabled;
	}

	public  void setFunctionsDisabled(boolean functionsDisabled) {
		this.functionsDisabled = functionsDisabled;
	}	
	
	public String getWemiGroupDisplayText() throws DataAccessException {
		int amicusNumber = getAmicusNumber(0);
	
		if(amicusNumber!=0){
			amicusNumber = getAmicusNumber(0);
			int view = getCurrentSearchingView();
			DAOFrbrModel frbr = new DAOFrbrModel();
			Integer wemiCode = frbr.getWemiFlag(amicusNumber,view == View.AUTHORITY);
			if(wemiCode!=null){
				wemiGroupDisplayText = frbr.getWemiFlagLabel(wemiCode,locale);
			}
			else
				wemiGroupDisplayText=null;
		}
		return wemiGroupDisplayText;
		
	}

	public int getCurrentSearchingView() {
		return currentSearchingView;
	}

	public void setCurrentSearchingView(int currentSearchingView) {
		this.currentSearchingView = currentSearchingView;
	}

}