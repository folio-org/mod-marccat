package librisuite.bean.cataloguing.authority;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import librisuite.action.cataloguing.bibliographic.SaveTagException;
import librisuite.bean.cataloguing.bibliographic.codelist.CodeListsBean;
import librisuite.bean.cataloguing.common.EditBean;
import librisuite.business.authorisation.AuthorisationException;
import librisuite.business.cataloguing.authority.Authority008Tag;
import librisuite.business.cataloguing.authority.AuthorityCatalog;
import librisuite.business.cataloguing.authority.AuthorityHeadingTag;
import librisuite.business.cataloguing.authority.AuthorityItem;
import librisuite.business.cataloguing.bibliographic.BibliographicNoteTag;
import librisuite.business.cataloguing.bibliographic.ClassificationAccessPoint;
import librisuite.business.cataloguing.bibliographic.ControlNumberAccessPoint;
import librisuite.business.cataloguing.bibliographic.MarcCorrelationException;
import librisuite.business.cataloguing.bibliographic.NewTagException;
import librisuite.business.cataloguing.common.AccessPoint;
import librisuite.business.cataloguing.common.Browsable;
import librisuite.business.cataloguing.common.Catalog;
import librisuite.business.cataloguing.common.CatalogItem;
import librisuite.business.cataloguing.common.DAOFrbrModel;
import librisuite.business.cataloguing.common.DateOfLastTransactionTag;
import librisuite.business.cataloguing.common.Tag;
import librisuite.business.codetable.DAOCodeTable;
import librisuite.business.codetable.ValueLabelElement;
import librisuite.business.common.DAOAuthorityCorrelation;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.DataAdminException;
import librisuite.business.common.DataDigAdminException;
import librisuite.business.common.DuplicateDescriptorException;
import librisuite.business.common.filter.FilterManager;
import librisuite.business.common.filter.TagFilter;
import librisuite.business.common.group.TagGroup;
import librisuite.business.descriptor.Descriptor;
import librisuite.business.exception.DuplicateTagException;
import librisuite.business.exception.InvalidDescriptorException;
import librisuite.business.exception.RecordInUseException;
import librisuite.business.exception.ValidationException;
import librisuite.hibernate.AUT;
import librisuite.hibernate.Correlation;
import librisuite.hibernate.PUBL_HDG;
import librisuite.hibernate.T_AUT_NTE_TYP;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.casalini.digital.business.PermalinkException;
import com.libricore.librisuite.common.StringText;
import com.libricore.librisuite.controller.SessionUtils;

public class AuthorityEditBean extends EditBean {
	private static final AuthorityCatalog authorityCatalog = new AuthorityCatalog();
	private static final Log logger = LogFactory
			.getLog(AuthorityEditBean.class);

	@Override
	public List getFirstList() throws DataAccessException{
		return new DAOAuthorityCorrelation().getFirstCorrelationListFilter(T_AUT_NTE_TYP.class, true, getOptNoteGroup());
	}

	public void checkSubfieldListEmpty(StringText text) throws SaveTagException {
		// Not an error in Authorities
	}

	public static EditBean getInstance(HttpServletRequest request) {
		AuthorityEditBean bean = (AuthorityEditBean) AuthorityEditBean
				.getSessionAttribute(request, AuthorityEditBean.class);
		if (bean == null) {
			bean = new AuthorityEditBean();
			bean.setSessionAttribute(request, bean.getClass());
			EditBean.getInstance(request);
			bean.setAuthorisationAgent(SessionUtils.getUserProfile(
					request.getSession(false)).getAuthorisationAgent());
			bean.setDefaultModelId(SessionUtils.getUserProfile(request)
					.getDefaultAuthorityModel());
			bean.setUserName(SessionUtils.getUserProfile(
					request.getSession(false)).getName());
		}
		bean.setSessionAttribute(request, EditBean.class);
		bean.setLocale(SessionUtils.getCurrentLocale(request));
		return bean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.bean.cataloguing.EditBean#getActionDescriminator()
	 */
	public String getMarcTypeCode() {
		return new AuthorityCatalog().getMarcTypeCode();
	}

	public List getAuthorityStructureList() {
		return CodeListsBean.getAuthorityStructure().getCodeList(getLocale());
	}

	public List getLinkDisplayList() {
		return CodeListsBean.getLinkDisplay().getCodeList(getLocale());
	}
	
	public List getReplacementComplexityList() {
		return CodeListsBean.getReplacementComplexity().getCodeList(getLocale());
	}
	
	public List getBilingualUsageList() {
		return CodeListsBean.getBilingualUsage().getCodeList(getLocale());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.bean.cataloguing.EditBean#getCatalog()
	 */
	public Catalog getCatalog() {
		return authorityCatalog;
	}

	public List getCataloguingRulesList() {
		return CodeListsBean.getCataloguingRules().getCodeList(getLocale());
	}

	public List getCataloguingSourceCodeList() {
		return CodeListsBean.getCataloguingSourceCode()
				.getCodeList(getLocale());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.bean.cataloguing.EditBean#getControlNumberValidationOptions()
	 */
	public Set getControlNumberValidationOptions() {
		return null;
	}

	public List getDualReferenceList() {
		return CodeListsBean.getDualReference().getCodeList(getLocale());
	}

	public List getEarlierRulesList() {
		return CodeListsBean.getEarlierRules().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getAuthorityEncodingLevelList() {
		return CodeListsBean.getAuthorityEncodingLevel().getCodeList(
				getLocale());
	}

	public List getGovernmentAgencyList() {
		return CodeListsBean.getGovernmentAgency().getCodeList(getLocale());
	}

	public List getHeadingStatusList() {
		return CodeListsBean.getHeadingStatus().getCodeList(getLocale());
	}

	public List getMainAddedEntryIndicatorList() {
		return CodeListsBean.getMainAddedEntryIndicator().getCodeList(
				getLocale());
	}

	public List getNonUniqueNameList() {
		return CodeListsBean.getNonUniqueName().getCodeList(getLocale());
	}

	public List getNoteGenerationList() {
		return CodeListsBean.getNoteGeneration().getCodeList(getLocale());
	}

	public List getPrintConstantList() {
		return CodeListsBean.getPrintConstant().getCodeList(getLocale());
	}

	public List getRecordModificationList() {
		return CodeListsBean.getRecordModification().getCodeList(getLocale());
	}

	public List getRecordRevisionList() {
		return CodeListsBean.getRecordRevision().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getAuthorityRecordStatusCodeList() {
		return CodeListsBean.getAuthorityRecordStatus()
				.getCodeList(getLocale());
	}

	public List getRecordTypeList() {
		return CodeListsBean.getRecordType().getCodeList(getLocale());
	}

	public List getReferenceStatusList() {
		return CodeListsBean.getReferenceStatus().getCodeList(getLocale());
	}

	public List getRomanizationSchemeList() {
		return CodeListsBean.getRomanizationScheme().getCodeList(getLocale());
	}

	public List getSeriesEntryIndicatorList() {
		return CodeListsBean.getSeriesEntryIndicator().getCodeList(getLocale());
	}

	public List getSeriesNumberingList() {
		return CodeListsBean.getSeriesNumbering().getCodeList(getLocale());
	}

	public List getSeriesTypeList() {
		return CodeListsBean.getSeriesType().getCodeList(getLocale());
	}

	public List getSubDivisionTypeList() {
		return CodeListsBean.getSubDivisionType().getCodeList(getLocale());
	}

	public List getSubjectDescriptorList() {
		return CodeListsBean.getSubjectDescriptor().getCodeList(getLocale());
	}

	public List getSubjectEntryIndicatorList() {
		return CodeListsBean.getSubjectEntryIndicator()
				.getCodeList(getLocale());
	}

	public List getSubjectSystemList() {
		return CodeListsBean.getSubjectSystem().getCodeList(getLocale());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.bean.cataloguing.EditBean#getTagCategories()
	 */
	public List getTagCategories() {
		List categories = CodeListsBean.getAuthorityTagCategory().getCodeList(
				getLocale());
		List tags = getCatalogItem().getTags();
		Iterator iter = tags.iterator();
		boolean hasHeadingTag = false;
		while (iter.hasNext()) {
			Tag t = (Tag) iter.next();
			if (t != getCurrentTag() && t instanceof AuthorityHeadingTag) {
				if (!((AuthorityHeadingTag)t).getDescriptor().isNew()) {
					hasHeadingTag = true;
					break;
				}
			}
		}
		if (hasHeadingTag) {
			List myList = new ArrayList();
			for (Object o : categories) {
				ValueLabelElement e = (ValueLabelElement) o;
				if (e.getValue().equals("2") || e.getValue().equals("3")
						|| e.getValue().equals("4")
						|| e.getValue().equals("11")) {
				} else {
					myList.add(e);
				}
			}
			return myList;
		}
		else {
			return categories;
		}
	}

	public void loadItem(int authorityNumber) throws MarcCorrelationException,
			DataAccessException, RecordInUseException {
		loadItem(new Object[] { new Integer(authorityNumber) });
	}

	public void loadItem(Object[] key) throws MarcCorrelationException,
			DataAccessException, RecordInUseException {
		authorityCatalog.lock((Integer) key[0], getUserName());
		CatalogItem item = getCatalog().getCatalogItem(key);
		setCatalogItem(item);
		setTagIndex(0);
		setCommandList(new ArrayList());
		setCurrentCommand(0);
	}

	public List getHeadingTypeList() throws DataAccessException {
		return getCatalog().getValidHeadingTypeList(getCurrentTag(),
				getLocale());
	}

	/**
	 * TODO MIKE: adattare per authority
	 */
	public boolean isFixedField() throws DataAccessException,
			MarcCorrelationException {
		String tagNbr = getCurrentTag().getMarcEncoding().getMarcTag();
		return tagNbr.equals("000") || tagNbr.equals("001")
				|| tagNbr.equals("005") || tagNbr.equals("006")
				|| tagNbr.equals("007") || tagNbr.equals("008");
	}

	/**
	 * TODO Carmen: adattare per authority
	 */
	public boolean isAbleDeleteButton() throws DataAccessException,
			MarcCorrelationException {
		String tagNbr = getCurrentTag().getMarcEncoding().getMarcTag();
		if (tagNbr.equals("000") || tagNbr.equals("008"))
			return false;
		else
			return true;
	}

	/**
	 * Carmen 23/11/2007 aggiornamento tag 005 in duplica record TODO _Carmen:
	 * adattare per authority
	 */
	public void updateT005DateOfLastTransaction() {
		DateOfLastTransactionTag t005 = (DateOfLastTransactionTag) getCatalogItem()
				.findFirstTagByNumber("005");

		if (t005 != null) {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyyMMddHHmmss.S");
			String data = formatter.format(new Date());
			Date date = formatter.parse(data, new ParsePosition(0));

			getCatalogItem().getItemEntity().setDateOfLastTransaction(date);
		}
	}

	/**
	 * TODO _Carmen: adattare per authority
	 */
	public void updateT008EnteredOnFileDate() {
		Authority008Tag t008 = (Authority008Tag) getCatalogItem()
				.findFirstTagByNumber("008");

		if (t008 != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			String data = formatter.format(new Date());
			Date date = formatter.parse(data, new ParsePosition(0));
			t008.setEnteredOnFileDate(date);
		}
	}

	public void changeReciprocalOption(short reciprocalOption)
			throws MarcCorrelationException {
		// do nothing
	}

	public void crea991(HttpServletRequest request, Tag rootTag)
			throws DataAccessException, NewTagException,
			AuthorisationException, ValidationException {
		// do nothing
	}

	public boolean isAbleNew991Button() throws DataAccessException,
			MarcCorrelationException {
		// do nothing
		return false;
	}

	public boolean isAbleSubdivision99X() throws DataAccessException,
			MarcCorrelationException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isAbleDigital() throws DataAccessException,
			MarcCorrelationException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isAbleEquivalentNote() {
		// TODO Auto-generated method stub
		return false;
	}

	public List getSubdivisionEncoding() throws MarcCorrelationException,
			DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSkipFiling() throws MarcCorrelationException,
			DataAccessException {
		if (getCurrentTag().getCategory() == 3)
			return true;
		else
			return false;

	}

	public void create997UserProfile(HttpServletRequest request)
			throws DataAccessException, NewTagException,
			AuthorisationException, ValidationException {
		// TODO Auto-generated method stub
	}

	public void create032TagLibricat(HttpServletRequest request)
			throws DataAccessException, NewTagException,
			AuthorisationException, ValidationException {
		// TODO Auto-generated method stub
	}

	public void modify997UserProfile(HttpServletRequest request)
			throws DataAccessException, NewTagException,
			AuthorisationException, ValidationException {
		// TODO Auto-generated method stub
	}

	public void createEquivalentTag792(int amicuNumber)
			throws MarcCorrelationException, DataAccessException,
			RecordInUseException, NewTagException, AuthorisationException,
			ValidationException {
		// TODO Auto-generated method stub
	}

	public boolean isModifyCatalogItem(final CatalogItem item)
			throws DataAccessException {
		Tag aTag = null;
		boolean isModify = false;
		Iterator iter = item.getTags().iterator();
		while (iter.hasNext()) {
			aTag = (Tag) iter.next();
			if (aTag.isNew() || (item.getDeletedTags().contains(aTag))
					|| aTag.isChanged()) {
				isModify = true;
				break;
			} else
				isModify = false;
		}
		return isModify;
	}

	public boolean isNewCatalogItem(final CatalogItem item)
			throws DataAccessException {
		return item.getItemEntity().isNew();
	}

	public int getCanBeSorted(int index) {
		int count = 0;
		try {
			Tag tag = getCatalogItem().getTag(index);
			TagGroup group = getGroupManager().getGroup(tag);
			if (group != null && !group.isCanSort())
				return count = 0;
			List subList = getFilteredSubList(index);
			if (subList.size() > 1)
				return count = 1;
		} catch (MarcCorrelationException e) {
			logger.warn(e);
			return count = 0;
		} catch (DataAccessException e) {
			logger.warn(e);
			return count = 0;
		} catch (Exception e) {
			logger.warn(e);
			return count = 0;
		}
		return count;
	}

	/**
	 * Restituisce la lista filtrata
	 * 
	 * @param tag
	 * @return
	 * @throws MarcCorrelationException
	 * @throws DataAccessException
	 */
	public List/* <Tag> */getFilteredSubList(int index)
			throws MarcCorrelationException, DataAccessException {
		// il GroupManager funge anche da FilterManager
		Tag tag = getCatalogItem().getTag(index);
		FilterManager filterManager = (FilterManager) getGroupManager();
		TagFilter filter = filterManager.getFilter(tag);
		List subList = getCatalogItem().findTags(filter, null);
		return subList;
	}

	// inizio
	public List getdiacriticiList() throws DataAccessException {
		DAOCodeTable dao = new DAOCodeTable();
		return dao.getDiacritici();
	}

	// fine

	public String getNoteStandardText() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isNoteStandard() {
		// TODO Auto-generated method stub
		return false;
	}

	public void saveDigitalHierarchy(ControlNumberAccessPoint tag097)
			throws DataAccessException {
	}

	public void deleteDigitalHierarchy(ControlNumberAccessPoint tag097)
			throws DataAccessException {
	}

	public void saveHierarchy(ControlNumberAccessPoint tag097)
			throws DataAccessException {
	}

	public void deleteHierarchy(ControlNumberAccessPoint tag097)
			throws DataAccessException {
	}

	public void loadItemDuplicate(Object[] key)
			throws MarcCorrelationException, DataAccessException,
			RecordInUseException {
		// TODO Auto-generated method stub

	}

	public void createTag097(HttpServletRequest request, StringText text,
			String hierarchyType) throws MarcCorrelationException,
			NewTagException, AuthorisationException, DataAccessException,
			ValidationException, RecordInUseException {

	}

	public void cntrHierarchyType() {
		// TODO Auto-generated method stub
	}

	public void refreshBean() {
		// TODO Auto-generated method stub

	}

	public List presenzaTag856Testo() throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	// public Tag presenzaTag856Testo() throws DataAccessException {
	// // TODO Auto-generated method stub
	// return null;
	// }

	public List get856Tags() throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	public void aggiornaDoi(BibliographicNoteTag tag, String codiceDoi)
			throws MarcCorrelationException, AuthorisationException,
			DataAccessException, ValidationException {
		// TODO Auto-generated method stub

	}

	public void modify097(Tag rootTag, StringText text)
			throws DataAccessException, NewTagException,
			AuthorisationException, ValidationException {
		// TODO Auto-generated method stub

	}

	public void cntrTag098(ControlNumberAccessPoint tag097)
			throws DataAccessException {
		// TODO Auto-generated method stub

	}

	public List getDiacriticiList() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getCountTagsEqual() {
		int count = 0;
		Tag rootTag = getCurrentTag();
		/*
		 * Controllo in Aggiungi modello se ho un solo tag non ripetibile deve
		 * apparire Salva Tag e non Sostituisci tag
		 */
		try {
			if (!rootTag.getValidation().isMarcTagRepeatable()) {
				if (rootTag instanceof AccessPoint) {
					if (getCatalogItem().findTagsEqual(
							((AccessPoint) rootTag).getFunctionCode()).size() == 1)
						return count;
				}
			}
		} catch (MarcCorrelationException e) {
			logger.warn(e);
			logger.warn("Error in getCountTagsEqual() in findTagsEqual");
		} catch (DataAccessException e) {
			logger.warn(e);
			logger.warn("Error in getCountTagsEqual() in findTagsEqual");
		}
		// System.out.println("COUNT: "+count);
		return count;
	}

	public void verifyAdminData(String checkDigital)
			throws DataAccessException, DataAdminException,
			DataDigAdminException {
		// TODO Auto-generated method stub
	}

	public void createTag260(PUBL_HDG publHdg, HttpServletRequest request)
			throws DataAccessException, AuthorisationException, NewTagException {
		// TODO Auto-generated method stub

	}

	public void createTag092() throws MarcCorrelationException,
			NewTagException, AuthorisationException, DataAccessException,
			ValidationException {
		// TODO Auto-generated method stub

	}

	public boolean isEquivalentEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	// public void controlDigAdminDataForDelete(int amicusNumber) throws
	// DataAccessException, DataDigAdminException {
	public void controlDigAdminDataForDelete(int amicusNumber)
			throws DataAccessException {
		// TODO Auto-generated method stub
	}

	public BibliographicNoteTag get856WithDoi() throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDoiCode() throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isPresentTag856Testo() throws DataAccessException {
		// TODO Auto-generated method stub
		return false;
	}

	// 20110114 inizio:
	public void createTag982(HttpServletRequest request, Tag rootTag)
			throws DataAccessException, NewTagException,
			AuthorisationException, ValidationException {
	}

	// 20110114 fine

	public boolean exist082WithDewey(List tags082, String deweyCode)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return false;
	}

	public ClassificationAccessPoint get982TagByDewey(String deweyCode)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	public List get982Tags() throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	public List get082Tags() throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getSkipDeleteTags(String marcTag) throws DataAccessException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List getBibliographicRelationshipTags() throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * extracted from LibrisuiteBean
	 */
	public void prepareItemForEditing(int recordView, Object[] key)
			throws DataAccessException, AuthorisationException,
			ValidationException, RecordInUseException {
		checkPermission("editAuthority");
		loadItem(key);
		finalizePreparationForEditing(); // pm aut
	}

	@Override
	public void processPickedHeading(Descriptor d, String selectedIndex)
			throws MarcCorrelationException, DuplicateDescriptorException,
			DataAccessException, AuthorisationException, ValidationException {
		if (getCatalogItem() != null
				&& getCatalogItem().isDecriptorAlreadyPresent(d,
						getCurrentTag())) {
			throw new DuplicateDescriptorException();
		}
		// and save it in the CatalogItem
		if (getCatalogItem() != null)
			updateDescriptorFromBrowse(d);
		Tag tag = getCurrentTag();

		if (tag != null) {
			((Browsable) tag).setDescriptor(d);
			// update the correlation settings
			refreshCorrelation(tag.getCorrelation(1), tag.getCorrelation(2),
					getLocale());
			setSkipInFiling((short) d.getSkipInFiling());

			if (isAddCatalogItem())
				validateCurrentTagHeading();
			else
				validateCurrentTag();

		}
	}

	public void finalizePreparationForEditing() {
		// no Authority specific preparations right now
	}

	@Override
	public void createTag856_4_2() throws PermalinkException,
			DuplicateTagException {
		// TODO Auto-generated method stub

	}

	@Override
	public BibliographicNoteTag get856_4_2_Tag() throws DataAccessException,
			DuplicateTagException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIsbnOrIssnCode(String recordType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isElectronicResourceoOnTag008() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void checkDigitalLevel() {
		// TODO Auto-generated method stub

	}

	public void impostaItemBibliographicLevelCode() {
		// paulm aut
		// not yet implemented in authorities
	}

	@Override
	public void setRelationForAnalytical() {
		// TODO Auto-generated method stub

	}

	@Override
	// bug 3035: Mesh
	public boolean isAbleSubjectsMesh() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Integer getWemiGroupFieldsFromModel(int modelId)
			throws DataAccessException {
		DAOFrbrModel frbrDao = new DAOFrbrModel();
		return frbrDao.getAutorityWemiFirstTypeFromModelById(modelId);
	}

	@Override
	public void saveTag856(HttpServletRequest request, String result,
			EditBean bean, String tagSave) throws DataAccessException,
			AuthorisationException, MarcCorrelationException,
			ValidationException, RecordInUseException {
		// TODO Auto-generated method stub

	}

	public void updateCasCacheBeforeSaveRecord() throws DataAccessException {
		// TODO Auto-generated method stub

	}
	@Override
	public void refreshCatalogItem() throws MarcCorrelationException,
			DataAccessException, RecordInUseException {
		/* Bug 4791 inizio */
		loadItem(getCatalogItem().getAmicusNumber().intValue());
		sortTags(getLocale());
		if (!isCallFromSaveTag()) {
			getCatalog().unlock(getCatalogItem().getAmicusNumber().intValue());
		}
		/* Bug 4791 fine */
	}

	@Override
	public void onPostSaveTag(Tag t) throws DataAccessException {
		if (t instanceof AuthorityHeadingTag) {
			((AuthorityCatalog) getCatalog()).changeHeadingTag(
					(AuthorityHeadingTag) t, (AuthorityItem) getCatalogItem());
		}
		sortTags(getLocale());
		setTagIndex(getCatalogItem().getNumberOfTags() - 1);
	}

	// ----> Anche per gli altri clienti deve scrivere la S_CAS_CACHE e
	// scrivere la tabella CAS_FILES e CAS_DIG_FILES
	@Override
	public void saveRecord() throws DataAccessException,
			AuthorisationException, MarcCorrelationException,
			ValidationException {
		getCatalog().getCatalogDao().setCasCache(
				getCasaliniBean().getCasCache());

		getCatalog().saveCatalogItem(getCatalogItem());

	}

	@Override
	public Correlation getCorrelation(String tagNumber, char indicator1,
			char indicator2, short category) throws DataAccessException {
		if (category != 0) {
			return getCatalogItem().getTagImpl().getCorrelation(tagNumber,
					indicator1, indicator2, category);
		} else {
			/*
			 * introduce the headingType to the correlation query to ensure that
			 * we get the best default tag type
			 */
			return new DAOAuthorityCorrelation().getFirstCorrelationByType(
					tagNumber, indicator1, indicator2, ((AUT) getCatalogItem()
							.getItemEntity()).getHeadingType());
		}
	}
	@Override
	public void isISBNValid(Descriptor descr) throws DataAccessException,
			MarcCorrelationException, InvalidDescriptorException {
		// TODO Auto-generated method stub
		
	}
	public List getNoteGroup() {
		return CodeListsBean.getAuthorityNoteGroup().getCodeList(getLocale());
	}
	@Override
	/**
	 * the tags 1XX not deleted from Authority
	 * check duplicate tag 
	 * @param tag
	 * @param view
	 * @param catalog
	 * @return
	 * @throws MarcCorrelationException
	 * @throws DataAccessException
	 * @throws ValidationException 
	 */
	public boolean checkTags()
			throws MarcCorrelationException, DataAccessException,
			ValidationException {
		boolean result = false;
		String tagMarc = this.getCurrentTag().getMarcEncoding().getMarcTag();
		boolean isNamesOrUniformTitle = tagMarc.startsWith("1");
		if ((isNamesOrUniformTitle)) {
			try {
				if(!((Browsable)getCurrentTag()).getDescriptor().isNew()){
					validateCurrentTag();
					result = true;
				}
			} catch (DuplicateTagException e) {
				return result = false;
			}
		}
		return result;
	}

	@Override
	public void loadItemWithoutLock(Object[] key)
			throws MarcCorrelationException, DataAccessException,
			RecordInUseException {
		// TODO Auto-generated method stub
		
	}
}