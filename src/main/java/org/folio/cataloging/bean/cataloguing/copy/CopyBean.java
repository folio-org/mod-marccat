package org.folio.cataloging.bean.cataloguing.copy;

import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.bean.LibrisuiteBean;
import org.folio.cataloging.bean.cataloguing.heading.ShelfListHeadingBean;
import org.folio.cataloging.bean.searching.BrowseBean;
import org.folio.cataloging.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.cataloging.business.cataloguing.common.Validation;
import org.folio.cataloging.business.common.CorrelationValues;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.DuplicateDescriptorException;
import org.folio.cataloging.business.common.RecordNotFoundException;
import org.folio.cataloging.business.controller.SessionUtils;
import org.folio.cataloging.business.controller.UserProfile;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.business.descriptor.SortFormParameters;
import org.folio.cataloging.dao.*;
import org.folio.cataloging.dao.persistence.*;
import org.folio.cataloging.exception.LibrisuiteException;
import org.folio.cataloging.exception.RecordInUseException;
import org.folio.cataloging.exception.ValidationException;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.util.StringText;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.folio.cataloging.F.deepCopy;

public class CopyBean extends LibrisuiteBean {

	private static final Log logger = LogFactory.getLog(CopyBean.class);

	public static CopyBean getInstance(HttpServletRequest request) 
	{
		CopyBean bean = (CopyBean) CopyBean.getSessionAttribute(request,CopyBean.class);

		if (bean == null) {
			bean = new CopyBean();
			bean.setSessionAttribute(request, bean.getClass());
			bean.cataloguingView = SessionUtils.getCataloguingView(request);
			bean.usersMainLibrary = SessionUtils.getUsersMainLibrary(request);
			bean.userProfile = SessionUtils.getUserProfile(request);
			bean.locale = SessionUtils.getCurrentLocale(request);
			bean.httpSession = request.getSession();
			bean.shelfHeadingBean = new ShelfListHeadingBean();
			bean.shelfHeadingBean.setLocale(bean.locale);
		}
		bean.setCopyColumnVisible(false);
		return bean;
	}

	private CPY_ID copy = null;
	private SHLF_LIST editingShelfList = null;
	private ShelfListHeadingBean shelfHeadingBean;
	private int cataloguingView;
	private int usersMainLibrary;
	private UserProfile userProfile;
	private Locale locale;
	private HttpSession httpSession;
	private String locationStringText;
	private boolean isDuplicateShelf = false;
	private boolean editMode = false;
	private String typeUpdateShelflist;
	private Date oldTransactionDate;

	private String visibleDiv = "";

	private boolean copyColumnVisible; // in browse page

	public String getVisibleDiv() {
		return visibleDiv;
	}

	public void setVisibleDiv(String visibleDiv) {
		this.visibleDiv = visibleDiv;
	}

	/**
	 * Class constructor
	 * 
	 * 
	 * @since 1.0
	 */
	public CopyBean() {
		super();
	}

	public String getTypeUpdateShelflist() {
		return typeUpdateShelflist;
	}

	public void setTypeUpdateShelflist(String typeUpdateShelflist) {
		this.typeUpdateShelflist = typeUpdateShelflist;
	}

	/**
	 * updates the copy with a new shelf-list object (from browse)
	 * 
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 * 
	 * @since 1.0
	 */
	public void updateShelfFromBrowse(SHLF_LIST shlf) {
		if (getCopy() != null) {
			setEditingShelfList(shlf);
			try {
				getShelfHeadingBean().setDescriptor(shlf);
			} catch (Exception e) {
				// eat this exception for now
				logger.warn("Exception thrown in heading bean constructor");
			}
			getCopy().markChanged();
		}
	}

	/**
	 * updates the copy with a new shelf-list object (from classification
	 * browse)
	 * 
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 * 
	 * @since 1.0
	 */
	public void updateShelfFromClassBrowse(CLSTN classNumber, int mainLibrary) {
		if (getCopy() != null) {
			SHLF_LIST newShelf = new SHLF_LIST();
			newShelf.setTypeCode(getShelfListNumberType().charAt(0));
			newShelf.setStringText(classNumber.getStringText());
			newShelf.setMainLibraryNumber(mainLibrary);
			setEditingShelfList(newShelf);
			try {
				getShelfHeadingBean().setDescriptor(newShelf);
			} catch (Exception e) {
				// eat this for now
				logger.warn("Exception thrown in heading bean constructor");
			}
			getCopy().markChanged();

		}
	}

	/**
	 * @return Returns a StringText object for the copies shelflist number
	 * 
	 * @since 1.0
	 */
	public StringText getStringText() {
		return new StringText(getEditingShelfList().getStringText());
	}

	/**
	 * @return Returns the creationDate.
	 */
	public String getCreationDate() {
		if (getCopy() != null) {
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			return formatter.format(copy.getCreationDate());
		} else {
			// TODO create a menaingfull string
			return null;
		}
	}

	/**
	 * @return Returns the transactionDate.
	 */
	public String getTransactionDate() {
		if (getCopy() != null) {
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			return formatter.format(copy.getTransactionDate());
		} else {
			// TODO create a menaingfull string
			return null;
		}
	}

	/**
	 * @return Returns the libraryName.
	 */
	public String getLibraryName() {
		DAOOrganisationHierarchy doh = new DAOOrganisationHierarchy();
		String libraryName = null;
		try {
			libraryName = doh.getLibOrBranchName(getCopy()
					.getOrganisationNumber(), locale);
		} catch (DataAccessException dataAccessException) {
		}
		return libraryName;
	}

	/**
	 *    The branchList to set.
	 */
	public List getBranchList() {
		List branchList = new ArrayList();
		if (getCopy() != null) {
			DAOOrganisationHierarchy doh = new DAOOrganisationHierarchy();
			branchList = doh.getListOfBranchesFromALibrary(getCopy()
					.getOrganisationNumber());
		}
		return branchList;
	}

	/**
	 * @return Returns the locationList.
	 */
	public List getLocationList() {
		List locationList = new ArrayList();
		if (getCopy() != null) {
			DAOLocation daol = new DAOLocation();
			try {
				locationList = daol.LCTN_VWList(getCopy()
						.getBranchOrganisationNumber(), locale);
			} catch (DataAccessException dataAccessException) {
				// TODO
			}
		}
		return locationList;
	}

	/**
	 * @return Returns the shelfListNumberTypeList.
	 */
	public List getShelfListNumberTypeList() {
		List shelfListNumberTypeList = new ArrayList();
		if (getCopy() != null) {
			try {
				shelfListNumberTypeList = new DAOCodeTable().getOptionList(
						T_SHLF_LIST_TYP.class, locale);
			} catch (DataAccessException dataAccessException) {
				// TODO
			}
		}
		return shelfListNumberTypeList;
	}

	/**
	 * @return Returns the loanPrdList.
	 */
	public List getLoanPrdList() {
		List loanPrdList = new ArrayList();
		if (getCopy() != null) {
			DAOOrganisationHierarchy doh = new DAOOrganisationHierarchy();
			try {
				loanPrdList = new DAOCodeTable().getOptionList(
						T_LOAN_PRD.class, locale);
			} catch (DataAccessException dataAccessException) {
				dataAccessException.printStackTrace();
			}
		}
		return loanPrdList;
	}

	/**
	 * @return Returns the statusList.
	 */
	public List getHoldingStatusTypeCodeList() {
		List statusList = new ArrayList();
		if (getCopy() != null) {
			DAOOrganisationHierarchy doh = new DAOOrganisationHierarchy();
			try {
				statusList = new DAOCodeTable().getOptionList(
						T_HLDG_STUS_TYP.class, locale);
			} catch (DataAccessException dataAccessException) {
				// TODO
			}
		}
		return statusList;
	}

	/**
	 * @return Returns the subStatusList.
	 */
	public List getHoldingSubscriptionStatusCodeList() {
		List subStatusList = new ArrayList();
		if (getCopy() != null) {
			DAOOrganisationHierarchy doh = new DAOOrganisationHierarchy();
			try {
				subStatusList = new DAOCodeTable().getOptionList(
						T_HLDG_SBCPT_STUS.class, locale);
			} catch (DataAccessException dataAccessException) {
				// TODO
			}
		}
		return subStatusList;
	}

	/**
	 * @return Returns the detailLvlList.
	 */
	public List getHoldingLevelOfDetailCodeList() {
		List detailLvlList = new ArrayList();
		if (getCopy() != null) {
			DAOOrganisationHierarchy doh = new DAOOrganisationHierarchy();
			try {
				detailLvlList = new DAOCodeTable().getOptionList(
						T_HLDG_LVL_OF_DTL.class, locale);
			} catch (DataAccessException dataAccessException) {
				// TODO
			}
		}
		return detailLvlList;
	}

	/**
	 * @return Returns the retentionList.
	 */
	public List getHoldingRetentionCodeList() {
		List retentionList = new ArrayList();
		if (getCopy() != null) {
			DAOOrganisationHierarchy doh = new DAOOrganisationHierarchy();
			try {
				retentionList = new DAOCodeTable().getOptionList(
						T_SRL_GENRETENTION.class, locale);
			} catch (DataAccessException dataAccessException) {
				// TODO
			}
		}
		return retentionList;
	}

	/**
	 * @return Returns the serTrmtList.
	 */
	public List getHoldingSeriesTrmtCodeList() {
		List serTrmtList = new ArrayList();
		if ((getCopy() != null)) {
			DAOOrganisationHierarchy doh = new DAOOrganisationHierarchy();
			try {
				serTrmtList = new DAOCodeTable().getOptionList(
						T_HLDG_SRS_TRMT.class, locale);
			} catch (DataAccessException dataAccessException) {
				// TODO
			}
		}
		return serTrmtList;
	}

	/**
	 * @return Returns the shelfListNumberType.
	 */
	public String getShelfListNumberType() {
		System.out.println("TypeCode " + getEditingShelfList().getTypeCode());
		return String.valueOf(getEditingShelfList().getTypeCode());
	}

	public boolean getIllCode() {
        return (getCopy() != null) && (getCopy().getIllCode() == '1');
    }

	public CPY_ID getCopy() {
		return copy;
	}

	/**
	 * Used on the web page to display the copy remark in text form (with $
	 * instead of subfield codes)
	 * 
	 * @return
	 */
	public String getCopyRemarkNote() {
		return copy.getCopyRemarkNoteForMap();
	}

	public String getCopyStatementText() {
		return copy.getCopyStatementTextForMap();
	}

	public void setCopy(CPY_ID copy) {
		this.copy = copy;
	}

	/**
	 * Controls the visibility of the copy/modify column in the browse page (for
	 * scan shelf) as well as the presence of the Save button in the modify
	 * ShelfList page
	 */
	public void setCopyColumnVisible(boolean copyColumnVisible) {
		this.copyColumnVisible = copyColumnVisible;
	}

	public void setCopyRemarkNote(String s) {
		String result = s;
		if (s != null && s.length() > 0) {
			if (!s.startsWith("$q")) {
				result = "$q" + s;
			}
			result = result.replaceFirst("\\$", Subfield.SUBFIELD_DELIMITER);
		}
		copy.setCopyRemarkNote(result);
	}

	public void setCopyStatementText(String s) {
		String result = s;
		if (s != null && s.length() > 0) {
			result = s.replaceAll("\\$", Subfield.SUBFIELD_DELIMITER);
		}
		copy.setCopyStatementText(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see HeadingBean#getHeading()
	 */
	public Descriptor getHeading() {
		return getEditingShelfList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see HeadingBean#onPopulateLists(DAOCodeTable,
	 *      java.util.Locale)
	 */
	protected void onPopulateLists(DAOCodeTable dao, Locale l)
			throws DataAccessException {
		// TODO perhaps shelflist-type should be included in this framework

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see HeadingBean#setHeading(librisuite.hibernate.Descriptor)
	 */
	protected void setHeading(Descriptor descriptor) {
		if (getCopy() != null) {
			if (!(descriptor instanceof SHLF_LIST)) {
				throw new IllegalArgumentException(
						"I can only set descriptors of type SHLF_LIST");
			}
			setEditingShelfList((SHLF_LIST) descriptor);
		}
	}

	/**
	 * 
	 * @since 1.0
	 */
	public SHLF_LIST getEditingShelfList() {
		return editingShelfList;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setEditingShelfList(SHLF_LIST shlf_list) {
		editingShelfList = shlf_list;
	}

	public void saveCopy(final Session session, final String userName) throws DataAccessException, ValidationException, RecordInUseException
	{
		DAOCopy dc = new DAOCopy();
		SHLF_LIST oldShelfList = null;

		// Check if the editing shelf list is a dup and the user needs to
		// confirm
		if (editingShelfHasBeenModified() && !isDuplicateShelf()) {
			checkShelflistAlreadyInUse(); // throws exception if true
		}
		/**
		 * 
		 */
		if (!(getEditingShelfList().equals(copy.getShelfList()))) { // compare
			// keys
			// if the edited shelf list is different than the original
			logger.debug("shelves are not equal");
			if (!isEditingShelfListDefault()) {
				/*
				 * if the edited version has been changed then we need to detach
				 * the current shelf and set the copy's shelf to the edited
				 * version
				 */
				logger.debug("edit shelf is not default");
				oldShelfList = copy.getShelfList();
				copy.setShelfList(getEditingShelfList());
			} else { // the shelfs are different but the edit version is
				// unchanged
				// the user has detached the original shelf list
				logger.debug("non null shelf is being detached");
				oldShelfList = copy.getShelfList();
				copy.setShelfList(null);
			}
		} else {
			if (isEditingShelfListDefault()) {
				// same shelf key -- but editing is set to blank -- treat same
				// as detach
				oldShelfList = copy.getShelfList();
				copy.setShelfList(null);
			}
		}
		/*
		 * User can manually set transaction date (on the form) so if they have
		 * leave it alone otherwise set the transaction date to today
		 */
		if (copy.getTransactionDate() != null
				&& getOldTransactionDate() != null) {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
			if (fmt.format(copy.getTransactionDate()).equals(
					fmt.format(getOldTransactionDate()))) {
				copy.setTransactionDate(new Date());
			}
		} else {
			copy.setTransactionDate(new Date());
		}
		/*
		 * TODO Should probably be setting markChanged at every stage of the
		 * edit action, but to be sure -- set it here. May result in some
		 * unnecessary db activity.
		 */
		copy.validate();
		copy.markChanged();
		dc.saveCopy(session, copy, oldShelfList, userName);

		BibliographicCatalogDAO dao = new BibliographicCatalogDAO();
		dao.updateCacheTable(copy.getBibItemNumber(), cataloguingView);

		logger.debug("back from save");
		// result = "copySaved";
		// refresh copy from db (just to be sure we start correctly after save)
		copy = dc.load(copy.getCopyIdNumber());
		prepareForEditing(copy);
		setDuplicateShelf(false);

		/*
		 * REMOVED BY PM -- Doesn't support multi-user int bibItemNumber =
		 * copyBean.getCopy().getBibItemNumber(); try{
		 * 
		 * if(copy.isNew()&&!copy.isChanged()) copy.deleteNewKey(); } catch
		 * (DataAccessException e1) { addError(request, errors,
		 * "error.copy.save"); return mapping.findForward("fail"); }
		 */
	}

	/**
	 * @param copy
	 * @param dc
	 * @throws DataAccessException
	 * @throws DuplicateDescriptorException
	 */
	private void checkHeadingUsed(CPY_ID copy, DAOCopy dc)
			throws DataAccessException {
		int count = dc.countCopyByShelf(copy, getEditingShelfList());
		if (!isDuplicateShelf()) {
			// CHECK SHELF DUPLICATE
			if (count >= 1) {
				// MODIFY E DETACH SEGNATURE
				if ("1".equalsIgnoreCase(getTypeUpdateShelflist())
						|| "2".equals(getTypeUpdateShelflist())) {
					if (!copy.getShelfList().equals(getEditingShelfList())) {
						setDuplicateShelf(true);
						logger
								.debug("Count delle copie duplicate: "
										+ count
										+ " - Numero della collocazione da utilizzare: "
										+ getEditingShelfList()
												.getShelfListKeyNumber());
						throw new DuplicateDescriptorException();
					}
					// NEW SEGNATURE
				} else if (copy.getShelfList() == null
						|| !copy.getShelfList().equals(getEditingShelfList())) {
					logger.debug("non sono uguali");
					setDuplicateShelf(true);
					logger.debug("Count delle copie duplicate: " + count
							+ " - Numero della collocazione da utilizzare: "
							+ getEditingShelfList().getShelfListKeyNumber());
					throw new DuplicateDescriptorException();
					// MODIFY COPIE
				} else if (copy.getCopyIdNumber() != 0
						&& !copy.getShelfList().equals(getEditingShelfList())) {
					logger.debug("sono uguali");
					setDuplicateShelf(true);
					logger.debug("Count delle copie duplicate: " + count
							+ " - Numero della collocazione da utilizzare: "
							+ getEditingShelfList().getShelfListKeyNumber());
					throw new DuplicateDescriptorException();
				}
				// DUPLICATE COPIE
				else if (copy.isNew()
						&& copy.getShelfList().equals(getEditingShelfList())) {
					logger.debug("sono uguali");
					setDuplicateShelf(true);
					logger.debug("Count delle copie duplicate: " + count
							+ " - Numero della collocazione da utilizzare: "
							+ getEditingShelfList().getShelfListKeyNumber());
					throw new DuplicateDescriptorException();
				}
			}
		}
		logger.debug("Count delle copie duplicate: " + count
				+ " - Numero della collocazione da utilizzare: "
				+ getEditingShelfList().getShelfListKeyNumber());
	}

	/**
	 * Checks if the currently displayed shelf list, if saved, would collide
	 * with another copies shelf list (same type, same main library)
	 *
	 * @throws DataAccessException
	 * @throws DuplicateDescriptorException
	 */
	private void checkShelflistAlreadyInUse() throws DataAccessException {
		int count = new DAOCopy().countCopyByShelf(copy, getEditingShelfList());
		if (count >= 1) {
			setDuplicateShelf(true);
			logger.debug("Count delle copie duplicate: " + count
					+ " - Numero della collocazione da utilizzare: "
					+ getEditingShelfList().getShelfListKeyNumber());
			throw new DuplicateDescriptorException();
		}
	}

	/**
	 * Detects if the edit version of the shelf list is the default supplied
	 * stringValue
	 * 
	 * @since 1.0
	 */
	private boolean isEditingShelfListDefault() {
		/*
		 * differs from the persistence tests (isNew, etc.) since an original
		 * default with modified text would also be considered New from a
		 * persistence perspective
		 */
		return (Subfield.SUBFIELD_DELIMITER + "a").equals(getEditingShelfList()
				.getStringText());
	}

	public SHLF_LIST getDefaultShelfList(int mainLibrary) {
		SHLF_LIST shelf = new SHLF_LIST();
		shelf.setStringText(Subfield.SUBFIELD_DELIMITER + "a");
		shelf.setMainLibraryNumber(mainLibrary);
		return shelf;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public HttpSession getHttpSession() {
		return httpSession;
	}

	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	public void editCopy( final int copyIdNumber) throws
            DataAccessException, RecordInUseException {
		DAOCopy dc = new DAOCopy();
		CPY_ID copy = dc.load(copyIdNumber);
		editCopy(copy);
	}

	public void editCopy(final CPY_ID copy) throws
            DataAccessException, RecordInUseException {
		DAOCopy dc = new DAOCopy();
		DAOOrganisationHierarchy doh = new DAOOrganisationHierarchy();
		if (copy == null) {
			throw new RecordNotFoundException();
		} else {
			dc.lock(copy.getBibItemNumber(), "BI", userProfile.getName());
			httpSession.setAttribute("isLockCopy", true);
			prepareForEditing(copy);
		}
	}

	/**
	 * Determine if the shelf list field(s) have been modified in the form
	 * 
	 * @return
	 */
	private boolean editingShelfHasBeenModified() {
		if (getEditingShelfList().isChanged()) {
			return true;
		}
		if (getEditingShelfList().isNew()) {
			if (!isEditingShelfListDefault()) {
				return true;
			}
		}
		return false;
	}

	public void detachShelfList() {
		setEditingShelfList(getDefaultShelfList(usersMainLibrary));
		try {
			shelfHeadingBean.setDescriptor(getEditingShelfList());
		} catch (Exception e) {
			// eat this for now
			logger.warn("Exception thrown in heading bean constructor");
		}
	}

	public void duplicateCopy(int copyIdNumber, int mainLibrary)
			throws DataAccessException {
		DAOCopy dc = new DAOCopy();
		CPY_ID copy = null;

		copy = dc.load(copyIdNumber);

		if (copy == null) {
			throw new RecordNotFoundException();
		} else {
			String sortForm = copy.getShelfList().getSortForm();
			copy = (CPY_ID) deepCopy(copy);
			setCopy(copy);
			copy.setBarCodeNumber("0");
			copy.setCopyIdNumber(0);
			copy.markNew();
			prepareForEditing(copy);
			copy.setShelfList(getDefaultShelfList(usersMainLibrary));
			getEditingShelfList().markChanged(); // so that dup shelf warning
													// appears
		}
	}

	public void prepareForEditing(CPY_ID copy) throws
            DataAccessException {
		setCopy(copy);
		SHLF_LIST shelf = copy.getShelfList();
		if (shelf == null) {
			shelf = getDefaultShelfList(usersMainLibrary);
		}
		setEditingShelfList(shelf);
		Validation bibliographicValidation = new DAOBibliographicValidation()
				.load(shelf.getCategory(),
				// TODO bib correlation values for shelflists have 851 and 852
						new CorrelationValues());
		getShelfHeadingBean().setValidation(bibliographicValidation);
		getShelfHeadingBean().setDescriptor(shelf);
	}

	public void processShelfListChanges(StringText text,
			String newSubfieldCode, String newSubfieldContent,
			String operation, String subfieldIndex) {
		if (operation.equals("newLine")) {
			text.addSubfield(new Subfield(newSubfieldCode, newSubfieldContent));
			logger.debug("CHIAMATA ************* COPYACTION");
		} else if (operation.equals("up")) {
			text.moveSubfieldUp(Integer.parseInt(subfieldIndex));
		} else if (operation.equals("down")) {
			text.moveSubfieldDown(Integer.parseInt(subfieldIndex));
		} else if (operation.equals("delete")) {
			text.removeSubfield(Integer.parseInt(subfieldIndex));
		} else if (operation.equals("change")) {
			// is ok
		}

		shelfHeadingBean.setStringText(text);
		getEditingShelfList().markChanged();
	}

	public void scanShelfLists(HttpServletRequest request)
			throws
            LibrisuiteException {
		logger.debug("Setting browse parameters for shelf list scan");
		BrowseBean bean = (BrowseBean) BrowseBean.getInstance(request);
		bean.init(request.getLocale());
		String s = new DAOIndexList().getIndexBySortFormType((short) 200,
				(short) getEditingShelfList().getTypeCode());
		if (s != null) {
			bean.setSelectedIndex(bean.getLocalisedIndex(s));
		} else {
			bean.setSelectedIndex(bean.getLocalisedIndex("28P30"));
		}
		logger.debug("index is: " + bean.getSelectedIndex());
		bean.setLastBrowseTermSkip(getStringText().toDisplayString());
		bean.setBrowseLinkMethod("pickShelf");
		setCopyColumnVisible(true);
		bean.refresh(getStringText().toDisplayString(),
				bean.getSearchingView(), SessionUtils
						.getUsersMainLibrary(request));

	}

	public void scanClassification(HttpServletRequest request)
			throws
            LibrisuiteException {
		logger.debug("Setting browse parameters for classification scan");
		BrowseBean bean = (BrowseBean) BrowseBean.getInstance(request);
		bean.init(request.getLocale());

		String index = new DAOBibliographicCorrelation()
				.getClassificationIndexByShelfType((short) getEditingShelfList()
						.getTypeCode());
		if (index != null) {
			bean.setSelectedIndex(bean.getLocalisedIndex(index));
			bean.setLastBrowseTermSkip(getStringText().toDisplayString());
		} else {
			bean.setSelectedIndex(bean.getLocalisedIndex("46P40"));
		}
		logger.debug("index is: " + bean.getSelectedIndex());
		bean.setBrowseLinkMethod("pickClass");
		bean.refresh(getStringText().toDisplayString(), SessionUtils
				.getCataloguingView(request), SessionUtils
				.getUsersMainLibrary(request));
	}

	public void newCopy(int amicusNumber, int usersMainLibrary,
			int usersBranchLibrary) throws
            DataAccessException {

		CPY_ID copy = new CPY_ID();
		setCopy(copy);
		copy.setBibItemNumber(amicusNumber);
		copy.setOrganisationNumber(usersMainLibrary);
		copy.setBranchOrganisationNumber(usersBranchLibrary);
		copy.setLocationNameCode((short) 0);
		copy.setShelfList(null);
		copy.setLoanPrd('1');
		copy.setHoldingStatusTypeCode('3');
		copy.setIllCode('1');
		copy.setHoldingSubscriptionStatusCode(' ');
		copy.setHoldingLevelOfDetailCode(' ');
		copy.setHoldingRetentionCode(' ');
		copy.setHoldingSeriesTrmtCode('7');
		copy.setBarCodeNumber("0");
		copy.setCopyIdNumber(0);
		Date createTime = new Date();
		copy.setCreationDate(createTime);
		copy.setTransactionDate(createTime);
	    prepareForEditing(copy);
	}

	public void setLocationStringText(String locationStringText) {
		this.locationStringText = locationStringText;
	}

	public void clean() {
		copy = null;

	}


	public boolean isCopyColumnVisible() {
		return copyColumnVisible;
	}

	public boolean isDuplicateShelf() {
		return isDuplicateShelf;
	}

	public void setDuplicateShelf(boolean isDuplicateShelf) {
		this.isDuplicateShelf = isDuplicateShelf;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public void unlock() throws DataAccessException, RecordInUseException {
		DAOCopy dc = new DAOCopy();
		dc.unlock(copy.getBibItemNumber(), "BI");
		getHttpSession().setAttribute("isLockCopy", false);
	}

	public void unlock(final int bibitmNbr) throws DataAccessException,
			RecordInUseException {
		DAOCopy dc = new DAOCopy();
		dc.unlock(bibitmNbr, "BI");
	}

	public void lock(final DAOCopy dc) throws DataAccessException,
			RecordInUseException {
		dc.lock(copy.getBibItemNumber(), "BI", getUserProfile().getName());
		getHttpSession().setAttribute("isLockCopy", true);
	}

	public void setSortForm(String remarkNote) throws
            DataAccessException {
		String noteWithoutSubfiled = "";
		if (remarkNote != null && remarkNote.length() > 0) {
			if (remarkNote.substring(0, 1).equalsIgnoreCase("$")) {
				noteWithoutSubfiled = remarkNote.substring(2);
				logger.debug("Nota sortForm: " + noteWithoutSubfiled);
				String noteWithSubfiled = "$q" + noteWithoutSubfiled;
				noteWithSubfiled = noteWithSubfiled.replaceFirst("\\$",
						"\u001f");
				logger.debug("Nota per db: " + noteWithSubfiled);
				this.copy.setCopyRemarkNote(noteWithSubfiled);
			} else {
				noteWithoutSubfiled = remarkNote;
				String noteWithSubfiled = "$q" + remarkNote;
				noteWithSubfiled = noteWithSubfiled.replaceFirst("\\$",
						"\u001f");
				logger.debug("Nota per db: " + noteWithSubfiled);
				this.copy.setCopyRemarkNote(noteWithSubfiled);
			}
		}
		this.copy
				.setCopyRemarkNoteSortForm(calculateSortForm(noteWithoutSubfiled));
	}

	public String calculateSortForm(String noteWithoutSubfiled)
			throws DataAccessException {
		String sortForm = "";
		if (noteWithoutSubfiled != null && noteWithoutSubfiled.length() > 0) {
			SortFormParameters parms = new SortFormParameters(100, 105, 0, 0, 0);
			sortForm = new DAOCopy().calculateSortForm(noteWithoutSubfiled,
					parms);
		}
		return sortForm;
	}

	public void attachNonLabelledCopy(int amicusNumber, String barcodeNumber)
			throws DataAccessException {
		logger.debug("looking for non-labelled barcode " + barcodeNumber);
		CPY_ID copy = new DAOCopy().getNonLabelledCopy(barcodeNumber);

		if (copy == null) {
			logger.debug("non-labelled barcode not found");
			throw new RecordNotFoundException();
		}

		logger.debug("barcode found -- preparing for edit");
		setCopy(copy);
		copy.setBibItemNumber(amicusNumber);
		prepareForEditing(copy);
		logger.debug("copy ready for editing");
	}

	public String calculateShelfSortForm(String shelfText, char shelfTyp)
			throws DataAccessException {
		String sortForm = "";
		if (!shelfText.isEmpty()) {
			SortFormParameters parms = new SortFormParameters(200,
					(int) shelfTyp, 0, 0, 0);
			sortForm = new DAOCopy().calculateSortForm(shelfText, parms);
		}
		return sortForm;
	}

	public void subfieldsControl(String statementText)
			throws ValidationException {
		int index = 0;
		char character = '$';
		for (int i = 0; i < statementText.length(); i++) {
			if (character == (statementText.charAt(i))) {
				index = i + 1;
				try {
					String subfield = Character.toString(statementText
							.charAt(index));
					if (!subfield.toLowerCase().matches(
							"[a,b,c,e,g,h,j,i,k,l,m,n,p,q,s,t,x,z]")) {
						throw new ValidationException();
					}
				} catch (IndexOutOfBoundsException e) {
					throw new ValidationException();
				}
			}
		}
	}

	public boolean isLockCopy(int bibItmNbr) throws DataAccessException {
		return new DAOCopy().isLockCopy(bibItmNbr);

	}

	public Date getOldTransactionDate() {
		return oldTransactionDate;
	}

	public void setOldTransactionDate(Date oldTransactionDate) {
		this.oldTransactionDate = oldTransactionDate;
	}

	public ShelfListHeadingBean getShelfHeadingBean() {
		return shelfHeadingBean;
	}

	public void setShelfHeadingBean(ShelfListHeadingBean shelfHeadingBean) {
		this.shelfHeadingBean = shelfHeadingBean;
	}

}