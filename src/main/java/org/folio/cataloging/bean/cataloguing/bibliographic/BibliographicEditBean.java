package org.folio.cataloging.bean.cataloguing.bibliographic;

import net.sf.hibernate.JDBCException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.routines.ISBNValidator;
import org.folio.cataloging.IGlobalConst;
import org.folio.cataloging.action.cataloguing.bibliographic.SaveTagException;
import org.folio.cataloging.bean.cas.CasaliniCodeListsBean;
import org.folio.cataloging.bean.cas.CasaliniContextBean;
import org.folio.cataloging.bean.cataloguing.bibliographic.codelist.CodeListsBean;
import org.folio.cataloging.bean.cataloguing.common.EditBean;
import org.folio.cataloging.bean.crossreference.CrossReferenceBean;
import org.folio.cataloging.bean.digital.DigitalAmminBean;
import org.folio.cataloging.bean.digital.DigitalDoiBean;
import org.folio.cataloging.bean.marchelper.MarcHelperBean;
import org.folio.cataloging.business.Command;
import org.folio.cataloging.business.authorisation.AuthorisationException;
import org.folio.cataloging.business.cataloguing.bibliographic.*;
import org.folio.cataloging.business.cataloguing.bibliographic.Map;
import org.folio.cataloging.business.cataloguing.common.*;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.business.common.*;
import org.folio.cataloging.business.common.filter.FilterManager;
import org.folio.cataloging.business.common.filter.TagFilter;
import org.folio.cataloging.business.common.group.BibliographicGroupManager;
import org.folio.cataloging.business.common.group.TagGroup;
import org.folio.cataloging.business.controller.SessionUtils;
import org.folio.cataloging.business.controller.UserProfile;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.business.digital.PermalinkException;
import org.folio.cataloging.business.searching.CodeTableParser;
import org.folio.cataloging.business.searching.DuplicateKeyException;
import org.folio.cataloging.business.searching.NoResultsFoundException;
import org.folio.cataloging.dao.*;
import org.folio.cataloging.dao.persistence.*;
import org.folio.cataloging.exception.*;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.util.StringText;

import javax.servlet.http.HttpServletRequest;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("unchecked")
public class BibliographicEditBean extends EditBean {

	private static final Log logger = LogFactory
			.getLog(BibliographicEditBean.class);
	private static final BibliographicCatalog theCatalog = new BibliographicCatalog();
	private static final String DIGITAL_TEXT_RESOURCE = Defaults
			.getString("digital.text.resource");
	private static boolean equivalentEnabled = Defaults.getBoolean(
			"record.equivalent.enabled", false);

	@Override
	public List getFirstList() throws DataAccessException{
		return new DAOBibliographicCorrelation().getFirstCorrelationListFilter(BibliographicNoteType.class, true, getOptNoteGroup());
	}
	
	/**
	 * retrieves the bean from the session if available otherwise initialises a
	 * new bean and establishes its addressability in the session
	 * 
	 */
	public static EditBean getInstance(HttpServletRequest request) {
		EditBean bean = (EditBean) BibliographicEditBean.getSessionAttribute(
				request, BibliographicEditBean.class);

		if (bean == null) {
			bean = new BibliographicEditBean();
			bean.setAuthorisationAgent(SessionUtils.getUserProfile(
					request.getSession(false)).getAuthorisationAgent());
			bean.setUserName(SessionUtils.getUserProfile(
					request.getSession(false)).getName());
			bean.setDefaultModelId(SessionUtils.getUserProfile(request)
					.getDefaultBibliographicModel());
			bean.init(SessionUtils.getCurrentLocale(request));
			bean.setGroupManager(BibliographicGroupManager.getInstance());
			((BibliographicEditBean) bean).setCataloguingView(SessionUtils
					.getCataloguingView(request));
			bean.setSessionAttribute(request, bean.getClass());
		}
		// register MarcHelperBean for MarcHelper functionality
		MarcHelperBean.getInstance(request);

		bean.setLocale(SessionUtils.getCurrentLocale(request));
		bean.setSessionAttribute(request, EditBean.class);
		return bean;
	}

	private PublisherManager revisedPublisher;
	private List diacriticiList;
	private String searchString;
	private String newSubfieldCode;
	private int cataloguingView;

	// private static DeweyClassificationType deweyClassificationType = new
	// DeweyClassificationType();

	public int getCataloguingView() {
		return cataloguingView;
	}

	public void setCataloguingView(int cataloguingView) {
		this.cataloguingView = cataloguingView;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public List getDiacriticiList() {
		return diacriticiList;
	}

	public void setDiacriticiList(List diacriticiList) {
		this.diacriticiList = diacriticiList;
	}

	public void checkSubfieldListEmpty(StringText text) throws SaveTagException {
		List subfield = text.getSubfieldList();
		if (subfield.isEmpty()) {
			throw new SaveTagException(
					"error.cataloguing.bibliographic.subfiledEmpty.save");
		}
	}

	/**
	 * Replaces the current leader and 008 with a new one
	 * 
	 */
	public void change008Type(BibliographicLeader ldr)
			throws AuthorisationException, DataAccessException,
			MarcCorrelationException {
		// TODO is this working?
		checkPermission("editHeader");
		Command c = new ChangeBib008TypeCommand(this, ldr);

		executeCommand(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.bean.cataloguing.EditBean#getActionDescriminator()
	 */
	public String getMarcTypeCode() {
		return new BibliographicCatalog().getMarcTypeCode();
	}

	public BibliographicItem getBibliographicItem() {
		return (BibliographicItem) getCatalogItem();
	}

	public List getBook_biographyList() {
		return CodeListsBean.getBookBiography().getCodeList(getLocale());
	}

	public List getBook_festschriftList() {
		return CodeListsBean.getBookFestschrift().getCodeList(getLocale());
	}

	public List getNoteGroup() {
		return CodeListsBean.getBibliographicNoteGroup().getCodeList(getLocale());
	}

	public List getBook_illustrationList() {
		return CodeListsBean.getBookIllustration().getCodeList(getLocale());
	}

	public List getBook_indexAvailabilityList() {
		return CodeListsBean.getBookIndexAvailability()
				.getCodeList(getLocale());
	}

	public List getBook_literaryFormList() {
		return CodeListsBean.getBookLiteraryForm().getCodeList(getLocale());
	}

	public List getBookMaterialTypeCodeList() {
		return CodeListsBean.getBookMaterialType().getCodeList(getLocale());
	}

	public List getCartographicFormatCodeList() {
		return CodeListsBean.getCartographicFormat().getCodeList(getLocale());
	}

	public List getCartographicIndexAvailabilityCodeList() {
		return CodeListsBean.getCartographicIndexAvailability().getCodeList(
				getLocale());
	}

	public List getCartographicMaterialList() {
		return CodeListsBean.getCartographicMaterial().getCodeList(getLocale());
	}

	public List getCartographicProjectionCodeList() {
		return CodeListsBean.getCartographicProjection().getCodeList(
				getLocale());
	}

	public List getCartographicReliefCodeList() {
		return CodeListsBean.getCartographicRelief().getCodeList(getLocale());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.bean.cataloguing.EditBean#getCatalog()
	 */
	public Catalog getCatalog() {
		return theCatalog;
	}

	public List getCataloguingSourceList() {
		return CodeListsBean.getCataloguingSource().getCodeList(getLocale());
	}

	public List getCfAntecedentSourceCodeList() {
		return CodeListsBean.getComputerFileAntecedentSource().getCodeList(
				getLocale());
	}

	public List getCfColourCodeList() {
		return CodeListsBean.getComputerFileColour().getCodeList(getLocale());
	}

	public List getCfDimensionsCodeList() {
		return CodeListsBean.getComputerFileDimensions().getCodeList(
				getLocale());
	}

	public List getCfFileFormatsCodeList() {
		return CodeListsBean.getComputerFileFormats().getCodeList(getLocale());
	}

	/* Bug 4119 */
	public List getComputerFileFormCodeList() {
		return CodeListsBean.getComputerFileForm().getCodeList(getLocale());
	}

	public List getCfLevelOfCompressionCodeList() {
		return CodeListsBean.getComputerFileLevelOfCompression().getCodeList(
				getLocale());
	}

	public List getCfQualityAssuranceTargetCodeList() {
		return CodeListsBean.getComputerFileQualityAssuranceTarget()
				.getCodeList(getLocale());
	}

	public List getCfReformattingQualityCodeList() {
		return CodeListsBean.getComputerFileReformattingQuality().getCodeList(
				getLocale());
	}

	public List getCfSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getComputerFileSpecificMaterialDesignation()
				.getCodeList(getLocale());
	}

	public List getCharacterCodingSchemeCodeList() {
		return CodeListsBean.getCharacterCodingScheme()
				.getCodeList(getLocale());
	}

	public List getComputerFileTypeCodeList() {
		return CodeListsBean.getComputerFileType().getCodeList(getLocale());
	}

	public List getComputerTargetAudienceCodeList() {
		return CodeListsBean.getComputerTargetAudience().getCodeList(
				getLocale());
	}

	public List getConferencePublicationList() {
		return CodeListsBean.getConferencePublication()
				.getCodeList(getLocale());
	}

	/*
	 * TODO change the user interface for $y and $z so that the user can just
	 * change the first subfield code (from the heading).
	 */
	/**
	 * Used in jsp to present options for validation code for control numbers
	 * 
	 * @since 1.0
	 */
	public Set getControlNumberValidationOptions() {
		if (getCurrentTag() instanceof ControlNumberAccessPoint) {
			try {
				ControlNumberAccessPoint f = (ControlNumberAccessPoint) getCurrentTag();
				Set s = new DAOBibliographicValidation().load(f.getCategory(),
						f.getCorrelationValues()).getValidSubfieldCodes();
				s.retainAll(Arrays.asList(new String[] { "a", "y", "z" }));
				if (s.size() > 1) {
					return s;
				} else {
					return null;
				}
			} catch (MarcCorrelationException e) {
				logger
						.debug("MarcCorrelationException getting control number validation");
				return null;
			} catch (DataAccessException e) {
				logger
						.debug("DataAccessException getting control number validation");
				return null;
			}
		} else {
			return null;
		}
	}

	public List getControlTypeCodeList() {
		return CodeListsBean.getControlType().getCodeList(getLocale());
	}

	public List getCountryList() {
		return CodeListsBean.getMarcCountry().getCodeList(getLocale());

		// TEST modo alternativo di caricare le tendine
		// List marcCountry = null;
		// try {
		// marcCountry = DAOCodeTable.asOptionList(new
		// DAOCodeTable().getList(T_MARC_CNTRY.class, false), Locale.ITALY);
		// } catch (DataAccessException e) {
		// logger.debug(
		// "DataAccessException getting marc country list");
		// return null;
		// }
		// return marcCountry;
	}

	public String getSomething(String s) {
		return s;
	}

	public List getDateTypeList() {
		return CodeListsBean.getItemDateType().getCodeList(getLocale());
	}

	public String getItemDateTypeValue() {

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(CodeListsBean.getItemDateType().getCodeList(
				getLocale()), md.getItemDateTypeCode());
	}

	public String getMarcCountryCodeValue() {

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getCountryList(), md.getMarcCountryCode());
	}

	public String getMaterialTypeCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getBookMaterialTypeCodeList(), md
				.getMarcCountryCode());
	}

	public String getMaterialTypeCodeMusicValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getMusicMaterialTypeCodeList(), md
				.getMarcCountryCode());
	}

	public String getMaterialTypeCodeVisualValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getVisualMaterialTypeCodeList(), md
				.getMarcCountryCode());
	}

	public String getBook_illustrationOneValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getBook_illustrationList(), md
				.getBookIllustrationChar()[0]);
	}

	public String getBook_illustrationTwoValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getBook_illustrationList(), md
				.getBookIllustrationChar()[1]);
	}

	public String getBook_illustrationThreeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getBook_illustrationList(), md
				.getBookIllustrationChar()[2]);
	}

	public String getBook_illustrationFourValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getBook_illustrationList(), md
				.getBookIllustrationChar()[3]);
	}

	public String getTargetAudienceCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getTargetAudienceList(), md.getTargetAudienceCode());
	}

	public String getFormOfItemCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getFormOfItemList(), md.getFormOfItemCode());
	}

	public String getNatureOfContentOneValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getNatureOfContentList(), md
				.getNatureOfContentsChar()[0]);
	}

	public String getNatureOfContentTwoValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getNatureOfContentList(), md
				.getNatureOfContentsChar()[1]);
	}

	public String getNatureOfContentThreeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getNatureOfContentList(), md
				.getNatureOfContentsChar()[2]);
	}

	public String getNatureOfContentFourValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getNatureOfContentList(), md
				.getNatureOfContentsChar()[3]);
	}

	public String getGovernmentPublicationCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getGovernmentPublicationList(), md
				.getGovernmentPublicationCode());
	}

	public String getConferencePublicationCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getConferencePublicationList(), md
				.getConferencePublicationCode());
	}

	public String getBookFestschriftValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getBook_festschriftList(), md.getBookFestschrift());
	}

	public String getBookIndexAvailabilityCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getBook_indexAvailabilityList(), md
				.getBookIndexAvailabilityCode());
	}

	public String getBookLiteraryFormTypeCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getBook_literaryFormList(), md
				.getBookLiteraryFormTypeCode());
	}

	public String getBookBiographyCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getBook_biographyList(), md.getBookBiographyCode());
	}

	public String getSerialFrequencyCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getSerialFrequencyCodeList(), md
				.getSerialFrequencyCode());
	}

	public String getSerialRegularityCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getSerialRegularityCodeList(), md
				.getSerialRegularityCode());
	}

	public String getSerialTypeCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getSerialTypeCodeList(), md.getSerialTypeCode());
	}

	public String getSerialFormOriginalItemCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getSerialFormOriginalItemCodeList(), md
				.getSerialFormOriginalItemCode());
	}

	public String getSerialOriginalAlphabetOfTitleCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getSerialOriginalAlphabetOfTitleCodeList(), md
				.getSerialOriginalAlphabetOfTitleCode());
	}

	public String getSerialSuccessiveLatestCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getSerialSuccessiveLatestCodeList(), md
				.getSerialSuccessiveLatestCode());
	}

	public String getComputerTargetAudienceCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getComputerTargetAudienceCodeList(), md
				.getComputerTargetAudienceCode());
	}

	/* Bug 4119 */
	public String getComputerFileFormCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getComputerFileFormCodeList(), md
				.getComputerFileFormCode());
	}

	public String getComputerFileTypeCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getComputerFileTypeCodeList(), md
				.getComputerFileTypeCode());
	}

	public String getCartographicReliefCode1Value() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getCartographicReliefCodeList(), md
				.getCartographicReliefChar()[0]);
	}

	public String getCartographicReliefCode2Value() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getCartographicReliefCodeList(), md
				.getCartographicReliefChar()[1]);
	}

	public String getCartographicReliefCode3Value() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getCartographicReliefCodeList(), md
				.getCartographicReliefChar()[2]);
	}

	public String getCartographicReliefCode4Value() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getCartographicReliefCodeList(), md
				.getCartographicReliefChar()[3]);
	}

	public String getCartographicIndexAvailabilityCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getCartographicIndexAvailabilityCodeList(), md
				.getCartographicIndexAvailabilityCode());
	}

	public String getCartographicProjectionCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getCartographicProjectionCodeList(), md
				.getCartographicProjectionCode());
	}

	public String getCartographicMaterialValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getCartographicMaterialList(), md
				.getCartographicMaterial());
	}

	public String getCartographicFormatCode1Value() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getCartographicFormatCodeList(), md
				.getCartographicFormatChar()[0]);
	}

	public String getCartographicFormatCode2Value() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getCartographicFormatCodeList(), md
				.getCartographicFormatChar()[1]);
	}

	public String getMusicFormOfCompositionCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getMusicFormOfCompositionCodeList(), md
				.getMusicFormOfCompositionCode());
	}

	public String getMusicFormatCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getMusicFormatCodeList(), md.getMusicFormatCode());
	}

	/* Bug 4161 inizio */
	public String getMusicPartsCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getMusicPartsCodeList(), md.getMusicPartsCode());
	}

	public String getMusicTranspositionArrangementCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getMusicTranspositionArrangementCodeList(), md
				.getMusicTranspositionArrangementCode());
	}

	public String getMusicPartsCode006Value() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getMusicPartsCodeList(), md.getMusicPartsCode());
	}

	public String getMusicTranspositionArrangementCode006Value() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getMusicTranspositionArrangementCodeList(), md
				.getMusicTranspositionArrangementCode());
	}

	/* Bug 4161 fine */

	public String getMusicTextualMaterialCode1Value() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getMusicTextualMaterialCodeList(), md
				.getMusicTextualMaterialChar()[0]);
	}

	public String getMusicTextualMaterialCode2Value() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getMusicTextualMaterialCodeList(), md
				.getMusicTextualMaterialChar()[1]);
	}

	public String getMusicTextualMaterialCode3Value() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getMusicTextualMaterialCodeList(), md
				.getMusicTextualMaterialChar()[2]);
	}

	public String getMusicTextualMaterialCode4Value() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getMusicTextualMaterialCodeList(), md
				.getMusicTextualMaterialChar()[3]);
	}

	public String getMusicTextualMaterialCode5Value() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getMusicTextualMaterialCodeList(), md
				.getMusicTextualMaterialChar()[4]);
	}

	public String getMusicTextualMaterialCode6Value() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getMusicTextualMaterialCodeList(), md
				.getMusicTextualMaterialChar()[5]);
	}

	public String getMusicLiteraryTextCode1Value() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getMusicLiteraryTextCodeList(), md
				.getMusicLiteraryTextChar()[0]);
	}

	public String getMusicLiteraryTextCode2Value() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getMusicLiteraryTextCodeList(), md
				.getMusicLiteraryTextChar()[1]);
	}

	public String getVisualTechniqueCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getVisualTechniqueCodeList(), md
				.getVisualTechniqueCode());
	}

	public String getVisualTargetAudienceCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getVisualTargetAudienceCodeList(), md
				.getVisualTargetAudienceCode());
	}

	public String getLanguageCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getLanguageList(), md.getLanguageCode());
	}

	public String getRecordModifiedCodeValue() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getModifiedRecordList(), md.getRecordModifiedCode());
	}

	public String getRecordCataloguingSourceCodeValue() {

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getCataloguingSourceList(), md
				.getRecordCataloguingSourceCode());
	}

	// TAG =007 Start Map Object

	public String getSpecificMaterialDesignationCodeValue() {
		Map map = (Map) getCatalogItem().findFirstTagByNumber("007");
		return getElement(getMapSpecificMaterialDesignationCodeList(), map
				.getSpecificMaterialDesignationCode());
	}

	public String getMapColourCodeValue() {
		Map map = (Map) getCatalogItem().findFirstTagByNumber("007");
		return getElement(getMapColourCodeList(), map.getMapColourCode());
	}

	public String getMapPhysicalMediumCodeValue() {
		Map map = (Map) getCatalogItem().findFirstTagByNumber("007");
		return getElement(getMapColourCodeList(), map
				.getMapPhysicalMediumCode());
	}

	public String getMapTypeOfReproductionCodeValue() {
		Map map = (Map) getCatalogItem().findFirstTagByNumber("007");
		return getElement(getMapTypeOfReproductionCodeList(), map
				.getMapTypeOfReproductionCode());
	}

	public String getMapProductionDetailsCodeValue() {
		Map map = (Map) getCatalogItem().findFirstTagByNumber("007");
		return getElement(getMapProductionDetailsCodeList(), map
				.getMapProductionDetailsCode());
	}

	public String getMapPolarityCodeValue() {
		Map map = (Map) getCatalogItem().findFirstTagByNumber("007");
		return getElement(getMapPolarityCodeList(), map.getMapPolarityCode());
	}

	// END TAG 007 Map Object

	public String getErSpecificMaterialDesignationCodeValue() {
		ElectronicResource type = (ElectronicResource) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getCfSpecificMaterialDesignationCodeList(), type
				.getSpecificMaterialDesignationCode());
	}

	public String getErColourCodeValue() {
		ElectronicResource type = (ElectronicResource) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getCfColourCodeList(), type.getColourCode());
	}

	public String getErDimensionsCodeValue() {
		ElectronicResource type = (ElectronicResource) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getCfDimensionsCodeList(), type.getDimensionsCode());
	}

	public String getErIncludesSoundCodeValue() {
		ElectronicResource type = (ElectronicResource) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getIncludesSoundCodeList(), type
				.getIncludesSoundCode());
	}

	public String getErFileFormatsCodeValue() {
		ElectronicResource type = (ElectronicResource) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getCfFileFormatsCodeList(), type.getFileFormatsCode());
	}

	public String getErQualityAssuranceTargetCodeValue() {
		ElectronicResource type = (ElectronicResource) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getCfQualityAssuranceTargetCodeList(), type
				.getQualityAssuranceTargetCode());
	}

	public String getErAntecedentSourceCodeValue() {
		ElectronicResource type = (ElectronicResource) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getCfAntecedentSourceCodeList(), type
				.getAntecedentSourceCode());
	}

	public String getErLevelOfCompressionCodeValue() {
		ElectronicResource type = (ElectronicResource) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getCfLevelOfCompressionCodeList(), type
				.getLevelOfCompressionCode());
	}

	public String getErReformattingQualityCodeValue() {
		ElectronicResource type = (ElectronicResource) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getCfReformattingQualityCodeList(), type
				.getReformattingQualityCode());
	}

	public String getGLbSpecificMaterialDesignationCodeValue() {
		Globe type = (Globe) getCatalogItem().findFirstTagByNumber("007");
		return getElement(getGlbSpecificMaterialDesignationCodeList(), type
				.getSpecificMaterialDesignationCode());
	}

	public String getGlbColourCodeValue() {
		Globe type = (Globe) getCatalogItem().findFirstTagByNumber("007");
		return getElement(getGlbColourCodeList(), type.getColourCode());
	}

	public String getGlbPhysicalMediumCodeValue() {
		Globe type = (Globe) getCatalogItem().findFirstTagByNumber("007");
		return getElement(getGlbPhysicalMediumCodeList(), type.getColourCode());
	}

	public String getGlbTypeOfReproductionCodeValue() {
		Globe type = (Globe) getCatalogItem().findFirstTagByNumber("007");
		return getElement(getGlbTypeOfReproductionCodeList(), type
				.getTypeOfReproductionCode());
	}

	public String getTmSpecificMaterialDesignationCodeValue() {
		TactileMaterial type = (TactileMaterial) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getTmSpecificMaterialDesignationCodeList(), type
				.getSpecificMaterialDesignationCode());
	}

	public String getTmClassOfBrailleWritingCodesOneValue() {
		TactileMaterial type = (TactileMaterial) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getTmClassOfBrailleWritingCodesList(), type
				.getClassOfBrailleWritingChar()[0]);
	}

	public String getTmClassOfBrailleWritingCodesTwoValue() {
		TactileMaterial type = (TactileMaterial) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getTmClassOfBrailleWritingCodesList(), type
				.getClassOfBrailleWritingChar()[1]);
	}

	public String getTmLevelOfContractionCodeValue() {
		TactileMaterial type = (TactileMaterial) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getTmLevelOfContractionCodeList(), type
				.getLevelOfContractionCode());
	}

	public String getTmBrailleMusicFormatCodesOneValue() {
		TactileMaterial type = (TactileMaterial) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getTmBrailleMusicFormatCodesList(), type
				.getBrailleMusicFormatChar()[0]);
	}

	public String getTmBrailleMusicFormatCodesTwoValue() {
		TactileMaterial type = (TactileMaterial) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getTmBrailleMusicFormatCodesList(), type
				.getBrailleMusicFormatChar()[1]);
	}

	public String getTmBrailleMusicFormatCodesThreeValue() {
		TactileMaterial type = (TactileMaterial) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getTmBrailleMusicFormatCodesList(), type
				.getBrailleMusicFormatChar()[2]);
	}

	public String getTmSpecificPhysicalCharacteristicsCodeValue() {
		TactileMaterial type = (TactileMaterial) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getTmSpecificPhysicalCharacteristicsCodeList(), type
				.getSpecificPhysicalCharacteristicsCode());
	}

	public String getPgSpecificMaterialDesignationCodeValue() {
		ProjectedGraphic type = (ProjectedGraphic) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getPgSpecificMaterialDesignationCodeList(), type
				.getSpecificMaterialDesignationCode());
	}

	public String getPgColourCodeValue() {
		ProjectedGraphic type = (ProjectedGraphic) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getPgColourCodeList(), type.getColourCode());
	}

	public String getPgBaseOfEmulsionCodeValue() {
		ProjectedGraphic type = (ProjectedGraphic) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getPgBaseOfEmulsionCodeList(), type
				.getBaseOfEmulsionCode());
	}

	public String getPgSoundOnMediumOrSeparateCodeValue() {
		ProjectedGraphic type = (ProjectedGraphic) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getPgSoundOnMediumOrSeparateCodeList(), type
				.getSoundOnMediumOrSeparateCode());
	}

	public String getPgMediumForSoundCodeValue() {
		ProjectedGraphic type = (ProjectedGraphic) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getPgMediumForSoundCodeList(), type
				.getMediumForSoundCode());
	}

	public String getPgDimensionsCodeValue() {
		ProjectedGraphic type = (ProjectedGraphic) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getPgDimensionsCodeList(), type.getDimensionsCode());
	}

	public String getSecondarySupportMaterialCodeValue() {
		ProjectedGraphic type = (ProjectedGraphic) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getPgSecondarySupportMaterialCodeList(), type
				.getSecondarySupportMaterialCode());
	}

	public String getMicSpecificMaterialDesignationCodeValue() {
		Microform type = (Microform) getCatalogItem().findFirstTagByNumber(
				"007");
		return getElement(getMicSpecificMaterialDesignationCodeList(), type
				.getSpecificMaterialDesignationCode());
	}

	public String getMicPolarityCodeValue() {
		Microform type = (Microform) getCatalogItem().findFirstTagByNumber(
				"007");
		return getElement(getMicPolarityCodeList(), type.getPolarityCode());
	}

	public String getMicDimensionsCodeValue() {
		Microform type = (Microform) getCatalogItem().findFirstTagByNumber(
				"007");
		return getElement(getMicDimensionsCodeList(), type.getDimensionsCode());
	}

	public String getMicReductionRatioRangeCodeValue() {
		Microform type = (Microform) getCatalogItem().findFirstTagByNumber(
				"007");
		return getElement(getMicReductionRatioRangeCodeList(), type
				.getReductionRatioRangeCode());
	}

	public String getMicGenerationCodeValue() {
		Microform type = (Microform) getCatalogItem().findFirstTagByNumber(
				"007");
		return getElement(getMicReductionRatioRangeCodeList(), type
				.getGenerationCode());
	}

	public String getReductionRatioCodeValue() {
		Microform type = (Microform) getCatalogItem().findFirstTagByNumber(
				"007");
		return getElement(getMicReductionRatioRangeCodeList(), type
				.getReductionRatioCode());
	}

	public String getMicColourCodeValue() {
		Microform type = (Microform) getCatalogItem().findFirstTagByNumber(
				"007");
		return getElement(getMicColourCodeList(), type.getColourCode());
	}

	public String getMicEmulsionOnFilmCodeValue() {
		Microform type = (Microform) getCatalogItem().findFirstTagByNumber(
				"007");
		return getElement(getMicEmulsionOnFilmCodeList(), type
				.getEmulsionOnFilmCode());
	}

	public String getMicBaseOfFilmCodeValue() {
		Microform type = (Microform) getCatalogItem().findFirstTagByNumber(
				"007");
		return getElement(getMicBaseOfFilmCodeList(), type.getBaseOfFilmCode());
	}

	public String getNpgSpecificMaterialDesignationCodeValue() {
		NonProjectedGraphic type = (NonProjectedGraphic) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getNpgSpecificMaterialDesignationCodeList(), type
				.getSpecificMaterialDesignationCode());
	}

	public String getNpgcolourCodeValue() {
		NonProjectedGraphic type = (NonProjectedGraphic) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getNpgColourCodeList(), type.getColourCode());
	}

	public String getNpgPrimarySupportMaterialCodeValue() {
		NonProjectedGraphic type = (NonProjectedGraphic) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getNpgPrimarySupportMaterialCodeList(), type
				.getPrimarySupportMaterialCode());
	}

	public String getNpgSecondarySupportMaterialCodeValue() {
		NonProjectedGraphic type = (NonProjectedGraphic) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getNpgSecondarySupportMaterialCodeList(), type
				.getSecondarySupportMaterialCode());
	}

	public String getMpSpecificMaterialDesignationCodeValue() {
		MotionPicture type = (MotionPicture) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getMpSpecificMaterialDesignationCodeList(), type
				.getSpecificMaterialDesignationCode());
	}

	public String getMpColourCodeValue() {
		MotionPicture type = (MotionPicture) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getMpColourCodeList(), type.getColourCode());
	}

	public String getMpPresentationFormatCodeValue() {
		MotionPicture type = (MotionPicture) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getMpPresentationFormatCodeList(), type
				.getPresentationFormatCode());
	}

	public String getMpIncludesSoundCodeValue() {
		MotionPicture type = (MotionPicture) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getIncludesSoundCodeList(), type
				.getIncludesSoundCode());
	}

	public String getMpMediumForSoundCodeValue() {
		MotionPicture type = (MotionPicture) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getMpMediumForSoundCodeList(), type
				.getMediumForSoundCode());
	}

	public String getMpDimensionsCodeValue() {
		MotionPicture type = (MotionPicture) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getMpDimensionsCodeList(), type.getDimensionsCode());
	}

	public String getMpConfigurationCodeValue() {
		MotionPicture type = (MotionPicture) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getMpConfigurationCodeList(), type
				.getConfigurationCode());
	}

	public String getMpProductionElementsCodeValue() {
		MotionPicture type = (MotionPicture) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getMpProductionElementsCodeList(), type
				.getProductionElementsCode());
	}

	public String getMpPolarityCodeValue() {
		MotionPicture type = (MotionPicture) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getMpPolarityCodeList(), type.getPolarityCode());
	}

	public String getMpGenerationCodeValue() {
		MotionPicture type = (MotionPicture) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getMpGenerationCodeList(), type.getGenerationCode());
	}

	public String getMpBaseOfFilmCodeValue() {
		MotionPicture type = (MotionPicture) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getMpBaseOfFilmCodeList(), type.getBaseOfFilmCode());
	}

	public String getMpRefinedCategoriesOfColourCodeValue() {
		MotionPicture type = (MotionPicture) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getMpRefinedCategoriesOfColourCodeList(), type
				.getRefinedCategoriesOfColourCode());
	}

	public String getMpKindOfColourStockCodeValue() {
		MotionPicture type = (MotionPicture) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getMpKindOfColourStockCodeList(), type
				.getKindOfColourStockCode());
	}

	public String getMpDeteriorationStageCodeValue() {
		MotionPicture type = (MotionPicture) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getMpDeteriorationStageCodeList(), type
				.getDeteriorationStageCode());
	}

	public String getMpCompletenessCodeValue() {
		MotionPicture type = (MotionPicture) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getMpCompletenessCodeList(), type
				.getCompletenessCode());
	}

	public String getKitSpecificMaterialDesignationCodeValue() {
		Kit type = (Kit) getCatalogItem().findFirstTagByNumber("007");
		return getElement(getKitSpecificMaterialDesignationCodeList(), type
				.getSpecificMaterialDesignationCode());
	}

	public String getNmSpecificMaterialDesignationCodeValue() {
		NotatedMusic type = (NotatedMusic) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getNmSpecificMaterialDesignationCodeList(), type
				.getSpecificMaterialDesignationCode());
	}

	public String getRsiSpecificMaterialDesignationCodeValue() {
		RemoteSensingImage type = (RemoteSensingImage) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getRsiSpecificMaterialDesignationCodeList(), type
				.getSpecificMaterialDesignationCode());
	}

	public String getRsiAltitudeOfSensorCodeValue() {
		RemoteSensingImage type = (RemoteSensingImage) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getRsiAltitudeOfSensorCodeList(), type
				.getAltitudeOfSensorCode());
	}

	public String getRsiCloudCoverCodeValue() {
		RemoteSensingImage type = (RemoteSensingImage) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getRsiCloudCoverCodeList(), type.getCloudCoverCode());
	}

	public String getRsiPlatformConstructionTypeCodeValue() {
		RemoteSensingImage type = (RemoteSensingImage) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getRsiPlatformConstructionTypeCodeList(), type
				.getPlatformConstructionTypeCode());
	}

	public String getRsiPlatformUseCodeValue() {
		RemoteSensingImage type = (RemoteSensingImage) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getRsiPlatformUseCodeList(), type
				.getPlatformUseCode());
	}

	public String getRsiSensorTypeCodeValue() {
		RemoteSensingImage type = (RemoteSensingImage) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getRsiSensorTypeCodeList(), type.getSensorTypeCode());
	}

	public String getRsiDataTypeCodeValue() {
		RemoteSensingImage type = (RemoteSensingImage) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getRsiDataTypeCodeList(), type.getDataTypeCode());
	}

	public String getSndSpecificMaterialDesignationCodeValue() {
		SoundRecording type = (SoundRecording) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getSndSpecificMaterialDesignationCodeList(), type
				.getSpecificMaterialDesignationCode());
	}

	public String getSndSpeedCodeValue() {
		SoundRecording type = (SoundRecording) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getSndSpeedCodeList(), type.getSpeedCode());
	}

	public String getSndConfigurationCodeValue() {
		SoundRecording type = (SoundRecording) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getSndConfigurationCodeList(), type
				.getConfigurationCode());
	}

	public String getSndGrooveWidthCodeValue() {
		SoundRecording type = (SoundRecording) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getSndGrooveWidthCodeList(), type
				.getGrooveWidthCode());
	}

	public String getSndDimensionsCodeValue() {
		SoundRecording type = (SoundRecording) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getSndDimensionsCodeList(), type.getDimensionsCode());
	}

	public String getSndTapeWidthCodeValue() {
		SoundRecording type = (SoundRecording) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getSndTapeWidthCodeList(), type.getTapeWidthCode());
	}

	public String getSndTapeConfigurationCodeValue() {
		SoundRecording type = (SoundRecording) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getSndTapeConfigurationCodeList(), type
				.getTapeConfigurationCode());
	}

	public String getSndDiscTypeCodeValue() {
		SoundRecording type = (SoundRecording) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getSndDiscTypeCodeList(), type.getDiscTypeCode());
	}

	public String getSndMaterialTypeCodeValue() {
		SoundRecording type = (SoundRecording) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getSndMaterialTypeCodeList(), type
				.getSndMaterialTypeCode());
	}

	public String getSndCuttingTypeCodeValue() {
		SoundRecording type = (SoundRecording) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getSndCuttingTypeCodeList(), type
				.getCuttingTypeCode());
	}

	public String getSndSpecialPlaybackCharacteristicsCodeValue() {
		SoundRecording type = (SoundRecording) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getSndSpecialPlaybackCharacteristicsCodeList(), type
				.getSpecialPlaybackCharacteristicsCode());
	}

	public String getSndStorageTechniqueCodeValue() {
		SoundRecording type = (SoundRecording) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getSndStorageTechniqueCodeList(), type
				.getStorageTechniqueCode());
	}

	public String getTxtSpecificMaterialDesignationCodeValue() {
		Text type = (Text) getCatalogItem().findFirstTagByNumber("007");
		return getElement(getTxtSpecificMaterialDesignationCodeList(), type
				.getSpecificMaterialDesignationCode());
	}

	public String getUspSpecificMaterialDesignationCodeValue() {
		Unspecified type = (Unspecified) getCatalogItem().findFirstTagByNumber(
				"007");
		return getElement(getUspSpecificMaterialDesignationCodeList(), type
				.getSpecificMaterialDesignationCode());
	}

	public String getVrSpecificMaterialDesignationCodeValue() {
		VideoRecording type = (VideoRecording) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getVrSpecificMaterialDesignationCodeList(), type
				.getSpecificMaterialDesignationCode());
	}

	public String getVrColourCodeValue() {
		VideoRecording type = (VideoRecording) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getVrColourCodeList(), type.getColourCode());
	}

	public String getVrFormatCodeValue() {
		VideoRecording type = (VideoRecording) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getVrFormatCodeList(), type.getFormatCode());
	}

	public String getVrIncludesSoundCodeValue() {
		VideoRecording type = (VideoRecording) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getIncludesSoundCodeList(), type
				.getIncludesSoundCode());
	}

	public String getVrMediumForSoundCodeValue() {
		VideoRecording type = (VideoRecording) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getVrMediumForSoundCodeList(), type
				.getMediumForSoundCode());
	}

	public String getVrDimensionsCodeValue() {
		VideoRecording type = (VideoRecording) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getVrDimensionsCodeList(), type.getDimensionsCode());
	}

	public String getVrConfigurationCodeValue() {
		VideoRecording type = (VideoRecording) getCatalogItem()
				.findFirstTagByNumber("007");
		return getElement(getVrConfigurationCodeList(), type
				.getConfigurationCode());
	}

	// S T A R T T A G 0 0 6

	public String getItemDateType006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(CodeListsBean.getItemDateType().getCodeList(
				getLocale()), md.getItemDateTypeCode());
	}

	public String getMarcCountryCode006Value() {
		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getCountryList(), md.getMarcCountryCode());
	}

	public String getMaterialTypeCode006Value() {
		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		// return
		// getElement(getBookMaterialTypeCodeList(),md.getMarcCountryCode());
		return getElement(getBookMaterialTypeCodeList(), md
				.getMaterialTypeCode());
	}

	public String getMaterialTypeCodeMusic006Value() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		// return
		// getElement(getMusicMaterialTypeCodeList(),md.getMarcCountryCode());
		return getElement(getMusicMaterialTypeCodeList(), md
				.getMaterialTypeCode());
	}

	public String getMaterialTypeCodeVisual006Value() {
		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		// return
		// getElement(getVisualMaterialTypeCodeList(),md.getMarcCountryCode());
		return getElement(getVisualMaterialTypeCodeList(), md
				.getVisualMaterialTypeCode());
	}

	public String getBook_illustrationOne006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getBook_illustrationList(), md
				.getBookIllustrationChar()[0]);
	}

	public String getBook_illustrationTwo006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getBook_illustrationList(), md
				.getBookIllustrationChar()[1]);
	}

	public String getBook_illustrationThree006Value() {
		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getBook_illustrationList(), md
				.getBookIllustrationChar()[2]);
	}

	public String getBook_illustrationFour006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getBook_illustrationList(), md
				.getBookIllustrationChar()[3]);
	}

	public String getTargetAudienceCode006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getTargetAudienceList(), md.getTargetAudienceCode());
	}

	public String getFormOfItemCode006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getFormOfItemList(), md.getFormOfItemCode());
	}

	public String getNatureOfContentOne006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getNatureOfContentList(), md
				.getNatureOfContentsChar()[0]);
	}

	public String getNatureOfContentTwo006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getNatureOfContentList(), md
				.getNatureOfContentsChar()[1]);
	}

	public String getNatureOfContentThree006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getNatureOfContentList(), md
				.getNatureOfContentsChar()[2]);
	}

	public String getNatureOfContentFour006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getNatureOfContentList(), md
				.getNatureOfContentsChar()[3]);
	}

	public String getGovernmentPublicationCode006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getGovernmentPublicationList(), md
				.getGovernmentPublicationCode());
	}

	public String getConferencePublicationCode006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getConferencePublicationList(), md
				.getConferencePublicationCode());
	}

	public String getBookFestschrift006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getBook_festschriftList(), md.getBookFestschrift());
	}

	public String getBookIndexAvailabilityCode006Value() {
		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getBook_indexAvailabilityList(), md
				.getBookIndexAvailabilityCode());
	}

	public String getBookLiteraryFormTypeCode006Value() {
		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getBook_literaryFormList(), md
				.getBookLiteraryFormTypeCode());
	}

	public String getBookBiographyCode006Value() {
		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getBook_biographyList(), md.getBookBiographyCode());
	}

	public String getSerialFrequencyCode006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getSerialFrequencyCodeList(), md
				.getSerialFrequencyCode());
	}

	public String getSerialRegularityCode006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getSerialRegularityCodeList(), md
				.getSerialRegularityCode());
	}

	public String getSerialTypeCode006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getSerialTypeCodeList(), md.getSerialTypeCode());
	}

	public String getSerialFormOriginalItemCode006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");
		return getElement(getSerialFormOriginalItemCodeList(), md
				.getSerialFormOriginalItemCode());
	}

	public String getSerialOriginalAlphabetOfTitleCode006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getSerialOriginalAlphabetOfTitleCodeList(), md
				.getSerialOriginalAlphabetOfTitleCode());
	}

	public String getSerialSuccessiveLatestCode006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getSerialSuccessiveLatestCodeList(), md
				.getSerialSuccessiveLatestCode());
	}

	public String getComputerTargetAudienceCode006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getComputerTargetAudienceCodeList(), md
				.getComputerTargetAudienceCode());
	}

	public String getComputerFileTypeCode006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getComputerFileTypeCodeList(), md
				.getComputerFileTypeCode());
	}

	public String getCartographicReliefCode1006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getCartographicReliefCodeList(), md
				.getCartographicReliefChar()[0]);
	}

	public String getCartographicReliefCode2006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getCartographicReliefCodeList(), md
				.getCartographicReliefChar()[1]);
	}

	public String getCartographicReliefCode3006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getCartographicReliefCodeList(), md
				.getCartographicReliefChar()[2]);
	}

	public String getCartographicReliefCode4006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getCartographicReliefCodeList(), md
				.getCartographicReliefChar()[3]);
	}

	public String getCartographicIndexAvailabilityCode006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getCartographicIndexAvailabilityCodeList(), md
				.getCartographicIndexAvailabilityCode());
	}

	public String getCartographicProjectionCode006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getCartographicProjectionCodeList(), md
				.getCartographicProjectionCode());
	}

	public String getCartographicMaterial006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getCartographicMaterialList(), md
				.getCartographicMaterial());
	}

	public String getCartographicFormatCode1006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getCartographicFormatCodeList(), md
				.getCartographicFormatChar()[0]);
	}

	public String getCartographicFormatCode2006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getCartographicFormatCodeList(), md
				.getCartographicFormatChar()[1]);
	}

	public String getMusicFormOfCompositionCode006Value() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getMusicFormOfCompositionCodeList(), md
				.getMusicFormOfCompositionCode());
	}

	public String getMusicFormatCode006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getMusicFormatCodeList(), md.getMusicFormatCode());
	}

	public String getMusicTextualMaterialCode1006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getMusicTextualMaterialCodeList(), md
				.getMusicTextualMaterialChar()[0]);
	}

	public String getMusicTextualMaterialCode2006Value() {
		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getMusicTextualMaterialCodeList(), md
				.getMusicTextualMaterialChar()[1]);
	}

	public String getMusicTextualMaterialCode3006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getMusicTextualMaterialCodeList(), md
				.getMusicTextualMaterialChar()[2]);
	}

	public String getMusicTextualMaterialCode4006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getMusicTextualMaterialCodeList(), md
				.getMusicTextualMaterialChar()[3]);
	}

	public String getMusicTextualMaterialCode5006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getMusicTextualMaterialCodeList(), md
				.getMusicTextualMaterialChar()[4]);
	}

	public String getMusicTextualMaterialCode6006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getMusicTextualMaterialCodeList(), md
				.getMusicTextualMaterialChar()[5]);
	}

	public String getMusicLiteraryTextCode1006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getMusicLiteraryTextCodeList(), md
				.getMusicLiteraryTextChar()[0]);
	}

	public String getMusicLiteraryTextCode2006Value() {
		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getMusicLiteraryTextCodeList(), md
				.getMusicLiteraryTextChar()[1]);
	}

	public String getVisualTechniqueCode006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getVisualTechniqueCodeList(), md
				.getVisualTechniqueCode());
	}

	public String getVisualTargetAudienceCode006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getVisualTargetAudienceCodeList(), md
				.getVisualTargetAudienceCode());
	}

	public String getLanguageCode006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getLanguageList(), md.getLanguageCode());
	}

	public String getRecordModifiedCode006Value() {

		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getModifiedRecordList(), md.getRecordModifiedCode());
	}

	public String getRecordCataloguingSourceCode006Value() {
		if (getCatalogItem().findFirstTagByNumber("006") == null)
			return "";

		MaterialDescription md = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("006");
		return getElement(getCataloguingSourceList(), md
				.getRecordCataloguingSourceCode());
	}

	// E N D T A G 0 0 6

	private String getElement(List<Avp> l, Object c) {
		String value = null;
		try {
			for (Avp element : l) {
				if (element.getValue().equals(String.valueOf(c))) {
					value = element.getLabel();
					break;
				}
			}
		} catch (Exception e) {
			value = "";
		}
		return value;
	}

	public List getDescriptiveCataloguingCodeList() {
		return CodeListsBean.getDescriptiveCataloguing().getCodeList(
				getLocale());
	}

	public List getEncodingLevelList() {
		return CodeListsBean.getEncodingLevel().getCodeList(getLocale());
	}

	public List getFormOfItemList() {
		return CodeListsBean.getFormOfItem().getCodeList(getLocale());
	}

	public List getGlbColourCodeList() {
		return CodeListsBean.getGlobeColour().getCodeList(getLocale());
	}

	public List getGlbPhysicalMediumCodeList() {
		return CodeListsBean.getGlobePhysicalMedium().getCodeList(getLocale());
	}

	public List getGlbSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getGlobeSpecificMaterialDesignation().getCodeList(
				getLocale());
	}

	public List getGlbTypeOfReproductionCodeList() {
		return CodeListsBean.getGlobeReproductionType()
				.getCodeList(getLocale());
	}

	public List getGovernmentPublicationList() {
		return CodeListsBean.getGovernmentPublication()
				.getCodeList(getLocale());
	}

	public List getIncludesSoundCodeList() {
		return CodeListsBean.getIncludesSound().getCodeList(getLocale());
	}

	public List getItemBibliographicLevelCodeList() {
		return CodeListsBean.getItemBibliographicLevel().getCodeList(
				getLocale());
	}

	public List getItemRecordTypeCodeList() {
		return CodeListsBean.getItemRecordType().getCodeList(getLocale());
	}

	public List getKitSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getKitSpecificMaterialDesignation().getCodeList(
				getLocale());
	}

	public List getLanguageList() {
		return CodeListsBean.getLanguage().getCodeList(getLocale());
	}

	public List getLinkedRecordCodeList() {
		return CodeListsBean.getLinkedRecord().getCodeList(getLocale());
	}

	public List getMapColourCodeList() {
		return CodeListsBean.getMapColour().getCodeList(getLocale());
	}

	public List<Avp> getDescriptionSubfield4() {
		try {
			return new DAOCopy().getDescriptionSubfield4();
		} catch (DataAccessException e) {
			return new ArrayList<Avp>();
		}
	}

	public List<Avp> getDescriptionSubfieldE() {
		try {
			return new DAOCopy().getDescriptionSubfieldE();
		} catch (DataAccessException e) {
			return new ArrayList<Avp>();
		}
	}

	public List getManagerialLevelList() {
		return CasaliniCodeListsBean.getManagerialLevelType().getCodeList(
				getLocale());
	}

	public List getMdrFglList() throws DataAccessException {
		return DAOCodeTable.asOptionList(new DAOCodeTable().getList(
				T_CAS_MDR_FGL.class, false), getLocale());
	}

	public List getDigitalLevelTypeList() throws DataAccessException {
		List levels = DAOCodeTable.asOptionList(new DAOCodeTable().getList(
				T_CAS_DIG_LEVEL_TYP.class, false), getLocale());
		return cntrDigLeader(levels);
	}

	public List getLevelTypeList() throws DataAccessException {
		List levels = DAOCodeTable.asOptionList(new DAOCodeTable().getList(
				T_CAS_LEVEL_TYP.class, false), getLocale());
		return cntrLeader(levels);
	}

	// 20101019 inizio: tabella di decodifica del progressivo del tag097
	// (CasFiles/CasDigFiles)
	public List getProgressiveTypeList() throws DataAccessException {
		return DAOCodeTable.asOptionList(new DAOCodeTable().getList(
				T_PROGR_TYPE.class, false), getLocale());
	}

	// 20101019 fine

	private List cntrDigLeader(List levels) {
		List leaderLevels = new ArrayList();

		// BibliographicLeader leader =
		// (BibliographicLeader)getCatalogItem().findFirstTagByNumber("000");
		// String encoding = leader.getItemBibliographicLevelCode() + "";
		// System.out.println("leader : " + leader.getDisplayString());

		String encoding = getItemBibliographicLevelCode();

		if (encoding == null)
			encoding = "";

		// System.out.println("encoding : " + encoding);

		if (encoding.trim().equalsIgnoreCase("m")) {
			for (int i = 0; i < levels.size(); i++) {
				Avp<String> element = (Avp) levels.get(i);
				if ((element.getValue().equalsIgnoreCase("014"))
						|| (element.getValue().equalsIgnoreCase("001")))
					leaderLevels.add(element);
			}
		} else if (encoding.trim().equalsIgnoreCase("s")) {
			for (int i = 0; i < levels.size(); i++) {
				Avp<String> element = (Avp) levels.get(i);
				if ((element.getValue().equalsIgnoreCase("011"))
						|| (element.getValue().equalsIgnoreCase("001")))
					leaderLevels.add(element);
			}
		} else if (encoding.trim().equalsIgnoreCase("a")) {
			for (int i = 0; i < levels.size(); i++) {
				Avp<String> element = (Avp) levels.get(i);
				if ((element.getValue().equalsIgnoreCase("015"))
						|| (element.getValue().equalsIgnoreCase("001")))
					leaderLevels.add(element);
			}
		} else if (encoding.trim().equalsIgnoreCase("b")) {
			for (int i = 0; i < levels.size(); i++) {
				Avp<String> element = (Avp) levels.get(i);
				if ((element.getValue().equalsIgnoreCase("012"))
						|| (element.getValue().equalsIgnoreCase("013"))
						|| (element.getValue().equalsIgnoreCase("001")))
					leaderLevels.add(element);
			}
		} else
			for (int i = 0; i < levels.size(); i++) {
				Avp<String> element = (Avp) levels.get(i);
				if (element.getValue().equalsIgnoreCase("001"))
					leaderLevels.add(element);
			}

		return leaderLevels;
	}

	private List cntrLeader(List levels) {
		List leaderLevels = new ArrayList();

		// BibliographicLeader leader =
		// (BibliographicLeader)getCatalogItem().findFirstTagByNumber("000");
		// String encoding = leader.getItemBibliographicLevelCode() + "";
		// System.out.println("leader : " + leader.getDisplayString());
		String encoding = getItemBibliographicLevelCode();
		// System.out.println("encoding : " + encoding);

		if (encoding == null)
			encoding = "";

		if (encoding.trim().equalsIgnoreCase("m")) {
			// 20090826 inizio

			if (getMdrFgl().equalsIgnoreCase("003")) { // Se livello figlia //
				for (int i = 0; i < levels.size(); i++) {
					Avp<String> element = (Avp) levels
							.get(i);
					if ((element.getValue().equalsIgnoreCase("005"))
							|| (element.getValue().equalsIgnoreCase("007"))
							|| (element.getValue().equalsIgnoreCase("008"))
							|| (element.getValue().equalsIgnoreCase("001")))
						leaderLevels.add(element);
				}
			} else { // Se livello madre o non specificato //
				for (int i = 0; i < levels.size(); i++) {
					Avp<String> element = (Avp) levels
							.get(i);
					if ((element.getValue().equalsIgnoreCase("002"))
							|| (element.getValue().equalsIgnoreCase("004"))
							|| (element.getValue().equalsIgnoreCase("006"))
							|| (element.getValue().equalsIgnoreCase("001")))
						leaderLevels.add(element);
				}
			}
			// 20090826 fine
			// for (int i = 0; i < levels.size(); i++) {
			// Avp element = (Avp) levels.get(i);
			// if ((element.getValue().equalsIgnoreCase("002")) ||
			// (element.getValue().equalsIgnoreCase("004")) ||
			// (element.getValue().equalsIgnoreCase("006")) ||
			// (element.getValue().equalsIgnoreCase("001")))
			// leaderLevels.add(element);
			// }
			// }
		} else if (encoding.trim().equalsIgnoreCase("s")) {
			for (int i = 0; i < levels.size(); i++) {
				Avp<String> element = (Avp) levels.get(i);
				if ((element.getValue().equalsIgnoreCase("002"))
						|| (element.getValue().equalsIgnoreCase("003"))
						|| (element.getValue().equalsIgnoreCase("004"))
						|| (element.getValue().equalsIgnoreCase("001")))
					leaderLevels.add(element);
			}
		} else if (encoding.trim().equalsIgnoreCase("a")) {
			for (int i = 0; i < levels.size(); i++) {
				Avp<String> element = (Avp) levels.get(i);
				if ((element.getValue().equalsIgnoreCase("005"))
						|| (element.getValue().equalsIgnoreCase("007"))
						|| (element.getValue().equalsIgnoreCase("008"))
						|| (element.getValue().equalsIgnoreCase("001")))
					leaderLevels.add(element);
			}
		} else if (encoding.trim().equalsIgnoreCase("b")) {
			for (int i = 0; i < levels.size(); i++) {
				Avp<String> element = (Avp) levels.get(i);
				if ((element.getValue().equalsIgnoreCase("005"))
						|| (element.getValue().equalsIgnoreCase("008"))
						|| (element.getValue().equalsIgnoreCase("009"))
						|| (element.getValue().equalsIgnoreCase("001")))
					leaderLevels.add(element);
			}
		} else
			for (int i = 0; i < levels.size(); i++) {
				Avp<String> element = (Avp) levels.get(i);
				if (element.getValue().equalsIgnoreCase("001"))
					leaderLevels.add(element);
			}

		return leaderLevels;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMapMaterialTypeCodeList() {
		return CodeListsBean.getMapMaterialType().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMapPhysicalMediumCodeList() {
		return CodeListsBean.getMapPhysicalMedium().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMapPolarityCodeList() {
		return CodeListsBean.getMapPolarity().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMapProductionDetailsCodeList() {
		return CodeListsBean.getMapProductionDetails().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMapSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getMapSpecificMaterialDesignation().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMapTypeOfReproductionCodeList() {
		return CodeListsBean.getMapReproductionType().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMaterialTypeCodeList() {
		return CodeListsBean.getMaterialType().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMicBaseOfFilmCodeList() {
		return CodeListsBean.getMicroformBaseOfFilm().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMicColourCodeList() {
		return CodeListsBean.getMicroformColour().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMicDimensionsCodeList() {
		return CodeListsBean.getMicroformDimensions().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMicEmulsionOnFilmCodeList() {
		return CodeListsBean.getMicroformEmulsionOnFilm().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMicGenerationCodeList() {
		return CodeListsBean.getMicroformGeneration().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMicPolarityCodeList() {
		return CodeListsBean.getMicroformPolarity().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMicReductionRatioRangeCodeList() {
		return CodeListsBean.getMicroformReductionRatioRange().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMicSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getMicroformSpecificMaterialDesignation()
				.getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getModifiedRecordList() {
		return CodeListsBean.getModifiedRecord().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMpBaseOfFilmCodeList() {
		return CodeListsBean.getMotionPictureBaseOfFilm().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMpColourCodeList() {
		return CodeListsBean.getMotionPictureColour().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMpCompletenessCodeList() {
		return CodeListsBean.getMotionPictureCompleteness().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMpConfigurationCodeList() {
		return CodeListsBean.getMotionPictureConfiguration().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMpDeteriorationStageCodeList() {
		return CodeListsBean.getMotionPictureDeteriorationStage().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMpDimensionsCodeList() {
		return CodeListsBean.getMotionPictureDimensions().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMpGenerationCodeList() {
		return CodeListsBean.getMotionPictureGeneration().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMpKindOfColourStockCodeList() {
		return CodeListsBean.getMotionPictureKindOfColourStock().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMpMediumForSoundCodeList() {
		return CodeListsBean.getMotionPictureMediumForSound().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMpPolarityCodeList() {
		return CodeListsBean.getMotionPicturePolarity()
				.getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMpPresentationFormatCodeList() {
		return CodeListsBean.getMotionPicturePresentationFormat().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMpProductionElementsCodeList() {
		return CodeListsBean.getMotionPictureProductionElements().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMpRefinedCategoriesOfColourCodeList() {
		return CodeListsBean.getMotionPictureRefinedCategoriesOfColour()
				.getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getMpSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getMotionPictureSpecificMaterialDesignation()
				.getCodeList(getLocale());
	}

	public List getMusicFormatCodeList() {
		return CodeListsBean.getMusicFormat().getCodeList(getLocale());
	}

	public List getMusicFormOfCompositionCodeList() {
		return CodeListsBean.getMusicFormOfComposition().getCodeList(
				getLocale());
	}

	public List getMusicLiteraryTextCodeList() {
		return CodeListsBean.getMusicLiteraryText().getCodeList(getLocale());
	}

	public List getMusicMaterialTypeCodeList() {
		return CodeListsBean.getMusicMaterialType().getCodeList(getLocale());
	}

	public List getMusicTextualMaterialCodeList() {
		return CodeListsBean.getMusicTextualMaterial().getCodeList(getLocale());
	}

	/* Bug 4161 inizio */
	public List getMusicPartsCodeList() {
		return CodeListsBean.getMusicParts().getCodeList(getLocale());
	}

	public List getMusicTranspositionArrangementCodeList() {
		return CodeListsBean.getMusicTranspositionArrangement().getCodeList(
				getLocale());
	}

	/* Bug 4161 fine */

	public List getNatureOfContentList() {
		return CodeListsBean.getNatureOfContent().getCodeList(getLocale());
	}

	public List getNmSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getNotatedMusicSpecificMaterialDesignation()
				.getCodeList(getLocale());
	}

	public List getNpgColourCodeList() {
		return CodeListsBean.getNonprojectedGraphicColour().getCodeList(
				getLocale());
	}

	public List getNpgPrimarySupportMaterialCodeList() {
		return CodeListsBean.getNonprojectedGraphicPrimarySupportMaterial()
				.getCodeList(getLocale());
	}

	public List getNpgSecondarySupportMaterialCodeList() {
		return CodeListsBean.getNonprojectedGraphicSecondarySupportMaterial()
				.getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getNpgSpecificMaterialDesignationCodeList() {
		return CodeListsBean
				.getNonprojectedGraphicSpecificMaterialDesignation()
				.getCodeList(getLocale());
	}

	public int getNumberOfTags() {
		return getCatalogItem().getNumberOfTags();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getPgBaseOfEmulsionCodeList() {
		return CodeListsBean.getProjectedGraphicBaseOfEmulsion().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getPgColourCodeList() {
		return CodeListsBean.getProjectedGraphicColour().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getPgDimensionsCodeList() {
		return CodeListsBean.getProjectedGraphicDimensions().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getPgMediumForSoundCodeList() {
		return CodeListsBean.getProjectedGraphicMediumForSound().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getPgSecondarySupportMaterialCodeList() {
		return CodeListsBean.getProjectedGraphicSecondarySupportMaterial()
				.getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getPgSoundOnMediumOrSeparateCodeList() {
		return CodeListsBean.getIncludesSound().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getPgSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getProjectedGraphicSpecificMaterialDesignation()
				.getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getRecordStatusCodeList() {
		return CodeListsBean.getRecordStatus().getCodeList(getLocale());
	}

	public List getRelationshipReciprocalList() {
		return CodeListsBean.getRelationReciprocal().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public PublisherManager getRevisedPublisher() {
		return revisedPublisher;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getRsiAltitudeOfSensorCodeList() {
		return CodeListsBean.getRemoteSensingImageAltitudeOfSensor()
				.getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getRsiAttitudeOfSensorCodeList() {
		return CodeListsBean.getRemoteSensingImageAttitudeOfSensor()
				.getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getRsiCloudCoverCodeList() {
		return CodeListsBean.getRemoteSensingImageCloudCover().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getRsiDataTypeCodeList() {
		return CodeListsBean.getRemoteSensingImageDataType().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getRsiPlatformConstructionTypeCodeList() {
		return CodeListsBean.getRemoteSensingImagePlatformConstructionType()
				.getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getRsiPlatformUseCodeList() {
		return CodeListsBean.getRemoteSensingImagePlatformUse().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getRsiSensorTypeCodeList() {
		return CodeListsBean.getRemoteSensingImageSensorType().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getRsiSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getRemoteSensingImageSpecificMaterialDesignation()
				.getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getSerialFormOriginalItemCodeList() {
		return CodeListsBean.getSerialFormOriginalItem().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getSerialFrequencyCodeList() {
		return CodeListsBean.getSerialFrequency().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getSerialOriginalAlphabetOfTitleCodeList() {
		return CodeListsBean.getSerialOriginalAlphabetOfTitle().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getSerialRegularityCodeList() {
		return CodeListsBean.getSerialRegularity().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getSerialSuccessiveLatestCodeList() {
		return CodeListsBean.getSerialSuccessiveLatest().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getSerialTypeCodeList() {
		return CodeListsBean.getSerialType().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getSndConfigurationCodeList() {
		return CodeListsBean.getSoundConfiguration().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getSndCuttingTypeCodeList() {
		return CodeListsBean.getSoundCuttingType().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getSndDimensionsCodeList() {
		return CodeListsBean.getSoundDimensions().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getSndDiscTypeCodeList() {
		return CodeListsBean.getSoundDiscType().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getSndGrooveWidthCodeList() {
		return CodeListsBean.getSoundGrooveWidth().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getSndMaterialTypeCodeList() {
		return CodeListsBean.getSoundMaterialType().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getSndSpecialPlaybackCharacteristicsCodeList() {
		return CodeListsBean.getSoundSpecialPlaybackCharacteristics()
				.getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getSndSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getSoundSpecificMaterialDesignation().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getSndSpeedCodeList() {
		return CodeListsBean.getSoundSpeed().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getSndStorageTechniqueCodeList() {
		return CodeListsBean.getSoundStorageTechnique()
				.getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getSndTapeConfigurationCodeList() {
		return CodeListsBean.getSoundTapeConfiguration().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getSndTapeWidthCodeList() {
		return CodeListsBean.getSoundTapeWidth().getCodeList(getLocale());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.bean.cataloguing.EditBean#getTagCategories()
	 */
	public List getTagCategories() {
		return CodeListsBean.getBibliographicTagCategory().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getTargetAudienceList() {
		return CodeListsBean.getTargetAudience().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getTmBrailleMusicFormatCodesList() {
		return CodeListsBean.getTactileMaterialBrailleMusicFormat()
				.getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getTmClassOfBrailleWritingCodesList() {
		return CodeListsBean.getTactileMaterialClassOfBrailleWriting()
				.getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getTmLevelOfContractionCodeList() {
		return CodeListsBean.getTactileMaterialLevelOfContraction()
				.getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getTmSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getTactileMaterialSpecificMaterialDesignation()
				.getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getTmSpecificPhysicalCharacteristicsCodeList() {
		return CodeListsBean
				.getTactileMaterialSpecificPhysicalCharacteristics()
				.getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getTxtSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getTextSpecificMaterialDesignation().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getUspSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getUnspecifiedSpecificMaterialDesignation()
				.getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getVisualMaterialTypeCodeList() {
		return CodeListsBean.getVisualMaterialType().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getVisualTargetAudienceCodeList() {
		return CodeListsBean.getVisualTargetAudience().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getVisualTechniqueCodeList() {
		return CodeListsBean.getVisualTechnique().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getVisualTypeCodeList() {
		return CodeListsBean.getVisualType().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getVrColourCodeList() {
		return CodeListsBean.getVideoRecordingColour().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getVrConfigurationCodeList() {
		return CodeListsBean.getVideoRecordingConfiguration().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getVrDimensionsCodeList() {
		return CodeListsBean.getVideoRecordingDimensions().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getVrFormatCodeList() {
		return CodeListsBean.getVideoRecordingFormat().getCodeList(getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getVrMediumForSoundCodeList() {
		return CodeListsBean.getVideoRecordingMediumForSound().getCodeList(
				getLocale());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getVrSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getVideoRecordingSpecificMaterialDesignation()
				.getCodeList(getLocale());
	}

	public void loadItem(int itemNumber, int cataloguingView)
			throws MarcCorrelationException, DataAccessException,
			RecordInUseException {
		theCatalog.lock(itemNumber, getUserName());
		BibliographicItem item = (BibliographicItem) getCatalog()
				.getCatalogItem(
						new Object[] { new Integer(itemNumber),
								new Integer(cataloguingView) });
		setCatalogItem(item);
		setTagIndex(0);
		setCommandList(new ArrayList());
		setCurrentCommand(0);
	}

	public void loadItemWithoutLock(int itemNumber, int cataloguingView)
			throws MarcCorrelationException, DataAccessException,
			RecordInUseException {
		BibliographicItem item = (BibliographicItem) getCatalog()
				.getCatalogItem(
						new Object[] { new Integer(itemNumber),
								new Integer(cataloguingView) });
		setCatalogItem(item);
		setTagIndex(0);
		setCommandList(new ArrayList());
		setCurrentCommand(0);
	}

	public void loadItemDuplicate(int itemNumber, int cataloguingView)
			throws MarcCorrelationException, DataAccessException,
			RecordInUseException {
		BibliographicItem item = (BibliographicItem) getCatalog()
				.getCatalogItem(
						new Object[] { new Integer(itemNumber),
								new Integer(cataloguingView) });
		setCatalogItem(item);
		setTagIndex(0);
		setCommandList(new ArrayList());
		setCurrentCommand(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.bean.cataloguing.EditBean#loadItem(java.lang.Object[])
	 */
	public void loadItem(Object[] key) throws MarcCorrelationException,
			DataAccessException, RecordInUseException {
		loadItem(((Integer) key[0]).intValue(), ((Integer) key[1]).intValue());
	}

	public void loadItemWithoutLock(Object[] key)
			throws MarcCorrelationException, DataAccessException,
			RecordInUseException {
		loadItemWithoutLock(((Integer) key[0]).intValue(), ((Integer) key[1])
				.intValue());
	}

	public void loadItemDuplicate(Object[] key)
			throws MarcCorrelationException, DataAccessException,
			RecordInUseException {
		loadItemDuplicate(((Integer) key[0]).intValue(), ((Integer) key[1])
				.intValue());
	}

	public void newItem(BibliographicItem bibliographicItem) {
		setCatalogItem(bibliographicItem);
		setTagIndex(0);
		setCommandList(new ArrayList());
		setCurrentCommand(0);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setRevisedPublisher(PublisherManager tag) {
		revisedPublisher = tag;
		tag.parseForEditing();
	}

	/**
	 * Delegate to PublisherTag
	 * 
	 */
	public void updatePublisherFromBrowse(PUBL_HDG p) throws DataAccessException {
		(getRevisedPublisher()).updatePublisherFromBrowse(p);
	}
	
	


	public void attachEquivalentSubjects() throws DataAccessException {
		theCatalog
				.attachEquivalentSubjects((BibliographicItem) getCatalogItem());
	}

	public boolean isEquivalentSubjects() {
		try {
			return ((DAOBibliographicCatalog) getCatalog().getCatalogDao())
					.getEquivalentSubjects(getCatalogItem()).size() > 0;
			// don't throw exception here since method is called from jsp
		} catch (DataAccessException e) {
			logger.warn("Testing for equivalent subjects threw exception");
			return true;
		}
	}

	public boolean isNamesOrderable() {
		try {
			return getBibliographicItem().getOrderableNames().size() > 1;
			// don't throw exception here because method called from jsp
		} catch (LibrisuiteException e) {
			logger.warn("OrderNames threw exception");
			return true;
		}
	}

	public boolean isSubjectsOrderable() {
		return getBibliographicItem().getOrderableSubjects().size() > 1;
	}

	public boolean isNoteOrderable() {
		return getBibliographicItem().getOrderableNotes().size() > 1;
	}

	public boolean isOrderableTitles() {
		return getBibliographicItem().getOrderableTitles().size() > 1;
	}

	public boolean isOrderableClassifications() {
		return getBibliographicItem().getOrderableClassifications().size() > 1;
	}

	public boolean isOrderableControlNumbers() {
		return getBibliographicItem().getOrderableControlNumbers().size() > 1;
	}

	public boolean isOrderableRelations() {
		return getBibliographicItem().getOrderableRelations().size() > 1;
	}

	/* Aggiungere gli altri tag */
	public boolean isOrderable() {
		if (isNamesOrderable() || isSubjectsOrderable() || isNoteOrderable()
				|| isOrderableTitles() || isOrderableClassifications()
				|| isOrderableControlNumbers() || isOrderableRelations())
			return true;
		else
			return false;
	}

	/**
	 * Updates the BibliographicRelationshipTag after a search for the related
	 * bib item
	 * @throws RelationshipTagException 
	 * 
	 * @since 1.0
	 */
	public void updateRelationshipFromSearch(int amicusNumber,
			int cataloguingView) throws DataAccessException,
			MarcCorrelationException {
		BibliographicRelationshipTag tag = (BibliographicRelationshipTag) getCurrentTag();
		//bug for ItemNumber == -1 in the tag
		tag.setItemNumber(this.getCatalogItem().getAmicusNumber());
		tag.replaceTargetRelationship(amicusNumber, cataloguingView);
		// update the correlation settings
		refreshCorrelation(tag.getCorrelation(1), tag.getCorrelation(2),
				getLocale());

		createStringTextEditBean();
		setSearchingRelationship(false);
	}

	public boolean isAbleDeleteButton() throws DataAccessException,
			MarcCorrelationException {
		String tagNbr = getCurrentTag().getMarcEncoding().getMarcTag();
		if (tagNbr.equals("000") || tagNbr.equals("008"))
			return false;
		else
			return true;
	}

	public boolean isAbleNew991Button() throws DataAccessException,
			MarcCorrelationException {
		// String tagNbr = getCurrentTag().getMarcEncoding().getMarcTag();
		// if(tagNbr.equals("082")&& this.getCasaliniBean().isEnabled())
		// return true;
		// else
		// return false;
		return false;
	}

	public boolean isAbleEquivalentNote() {
		short category = getCurrentTag().getCategory();
		if (category == 7 && this.getCasaliniBean().isEnabled())
			return true;
		else
			return false;
	}

	public boolean isAbleSubdivision99X() throws DataAccessException,
			MarcCorrelationException {
		if (!isNavigation() && isCataloguingMode()
				&& this.getCasaliniBean().isEnabled()) {
			String tagNbr = getCurrentTag().getMarcEncoding().getMarcTag();
			if (!tagNbr.equals("991"))
				return false;
			else
				return true;
		}
		return false;
	}

	public boolean isAbleDigital() throws DataAccessException,
			MarcCorrelationException {
		boolean isAble = false;
		// ----> 20130121 inizio: deve farlo per tutti i clienti
		// if (this.getCasaliniBean().isEnabled()) {
		// ----> 20130121 fine
		if ("856".equals(getCurrentTag().getMarcEncoding().getMarcTag())
				&& getCurrentTag().isNew()) {
			BibliographicNoteTag tag856 = (BibliographicNoteTag) getCurrentTag();
			// ------------> Deve abilitare il tasto Esplora solo se si tratta
			// di un 856 che crea WeCat in automatico
			if (tag856.getCorrelation(1) == 346
					&& tag856.getCorrelation(2) == -1
					&& tag856.getCorrelation(3) == -1) {
				isAble = true;
			}
		}
		// }
		return isAble;
	}

	public boolean isFixedField() throws DataAccessException,
			MarcCorrelationException {
		String tagNbr = getCurrentTag().getMarcEncoding().getMarcTag();
		return tagNbr.equals("000") || tagNbr.equals("001")
				|| tagNbr.equals("005") || tagNbr.equals("006")
				|| tagNbr.equals("007") || tagNbr.equals("008");
	}

	/**
	 * Carmen 23/11/2007 aggiornamento tag 005 in duplica record
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
	 * Carmen 23/11/2007 aggiornamento tag 008 nel modello posizione 00-05
	 */
	public void updateT008EnteredOnFileDate() {
		MaterialDescription t008 = (MaterialDescription) getCatalogItem()
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
		BibliographicRelationshipTag tag = (BibliographicRelationshipTag) getCurrentTag();
		tag.changeReciprocalOption(reciprocalOption);
		createStringTextEditBean();
	}

	/**
	 * TODO _CARMEN: promote to BibliographicEditBean
	 * 
	 * @return
	 */
	public boolean isNoPublisherSet(PublisherManager tag) {
		boolean isNoDescriptor = false;
		Iterator iter = tag.getPublisherTagUnits().iterator();
		while (iter.hasNext()) {
			PUBL_TAG pap = ((PUBL_TAG) iter.next());
			PUBL_HDG pu = ((PUBL_HDG) pap.getDescriptor());
			if (pu != null) {
				if (pu.getKey().getHeadingNumber() == -1) {
					if (!pap.getDate().equals(""))
						return isNoDescriptor = false;
					else
						return isNoDescriptor = true;
				}
			}
		}
		return isNoDescriptor;
	}

	/**
	 * TODO _CARMEN: promote to BibliographicEditBean
	 * 
	 * @throws NoHeadingSetException
	 */
	public void checkPublisher(PublisherManager tag)
			throws NoHeadingSetException {
		if (isNoPublisherSet(tag)) {
			resetCommands();
			throw new NoHeadingSetException(
					"error.cataloguing.record.validationException.260");
		}
	}

	public void isISBNValid(Descriptor descr) throws DataAccessException,
			MarcCorrelationException, InvalidDescriptorException {
		DAOGlobalVariable dgv = new DAOGlobalVariable();
		char value_hyphen = dgv.getValueByName("isbn_hyphen").charAt(0);
		if (descr instanceof CNTL_NBR) {
			if (((CNTL_NBR) descr).getTypeCode() == 9) {
				String isbn = descr.getDisplayText();
				// da fare
				StringText a = new StringText(descr.getStringText());
				if (!a.getSubfieldsWithCodes("a").isEmpty()) {
					if (value_hyphen == '0') {
						checkISBNWithoutHyphens(isbn);
					} else if (value_hyphen == '1') {
						checkISBNWithHyphens(isbn);
					}
				}
			}
		}
	}

	/**
	 * @param isbn
	 * @throws SaveTagException
	 */
	private void checkISBNWithoutHyphens(String isbn)
			throws InvalidDescriptorException {
		// if * is present. No check
		if (isbn.indexOf('*') != -1)
			return;

		if (isbn.indexOf('-') != -1) { // hyphen present
			throw new InvalidDescriptorException(
					"error.cataloguing.bibliographic.isbn.not.hyphen");
		} else {// hyphen not present
			ISBNValidator isbnValidator = new ISBNValidator();
			/*
			 * 15/12/2008 Modica che consente di controllora gli ISBN con il
			 * volume
			 */
			String isbn10 = null;
			String isbn13 = null;
			boolean isCorrect10 = false;
			boolean isCorrect13 = false;
			if (isbn.length() >= 13) {
				isbn13 = isbn.substring(0, 13);
				isCorrect13 = (isbnValidator.isValidISBN13(isbn13));
				if (isCorrect13)
					return;
			}
			if (isbn.length() >= 10) {
				isbn10 = isbn.substring(0, 10);
				isCorrect10 = isbnValidator.isValidISBN10(isbn10);
				if (isCorrect10)
					return;
			}
			if (!isCorrect10 && !isCorrect13)
				throw new InvalidDescriptorException(
						"error.cataloguing.bibliographic.isbn.valid.save");
			/* Fine modifica */

			/*
			 * if (!isbnValidator.isValid(isbn)) { throw new
			 * InvalidDescriptorException("error.cataloguing.bibliographic.isbn.valid.save"); }
			 */
		}
	}

	/**
	 * @param isbn
	 * @throws SaveTagException
	 */
	private void checkISBNWithHyphens(String isbn)
			throws InvalidDescriptorException {
		// if * is present. No check
		if (isbn.indexOf('*') != -1)
			return;

		if (isbn.indexOf('-') == -1) { // hyphen not present
			throw new InvalidDescriptorException(
					"error.cataloguing.bibliographic.isbn.hyphen.save");
		} else {// hyphen present
			ISBNValidator isbnValidator = new ISBNValidator();
			/*
			 * 15/12/2008 Modica che consente di controllora gli ISBN con il
			 * volume
			 */
			isbn = isbn.replaceAll("-", "");
			String isbn10 = null;
			String isbn13 = null;
			boolean isCorrect10 = false;
			boolean isCorrect13 = false;
			if (isbn.length() >= 13) {
				isbn13 = isbn.substring(0, 13);
				isCorrect13 = (isbnValidator.isValidISBN13(isbn13));
				if (isCorrect13)
					return;
			}
			if (isbn.length() >= 10) {
				isbn10 = isbn.substring(0, 10);
				isCorrect10 = isbnValidator.isValidISBN10(isbn10);
				if (isCorrect10)
					return;
			}
			if (!isCorrect10 && !isCorrect13)
				throw new InvalidDescriptorException(
						"error.cataloguing.bibliographic.isbn.valid.save");
			/*
			 * if (!isbnValidator.isValid(isbn)) { throw new
			 * InvalidDescriptorException("error.cataloguing.bibliographic.isbn.valid.save"); }
			 */
		}
	}

	public void crea991(HttpServletRequest request, Tag rootTag)
			throws DataAccessException, NewTagException,
			AuthorisationException, ValidationException {
		// 20110114 inizio:
		createTag982(request, rootTag);
		// 20110114 fine

		if (rootTag instanceof ClassificationAccessPoint) {
			if (rootTag.getMarcEncoding().getMarcTag().equals("082")) {
				ClassificationAccessPoint tag082 = (ClassificationAccessPoint) rootTag;
				String dewey = tag082.getStringText()
						.getSubfieldsWithCodes("a").toDisplayString();

				ClassificationAccessPoint tag991 = (ClassificationAccessPoint) newTag(
						1, (short) 6);
				CorrelationValues v = new CorrelationValues((short) 29,
						(short) 73, (short) -1);
				tag991.setCorrelationValues(v);

				refreshCorrelation(tag991.getCorrelation(1), tag991
						.getCorrelation(2), SessionUtils
						.getCurrentLocale(request));

				List subfieldList = new ArrayList();
				List subfields = new ArrayList();
				subfieldList.add("a");
				subfields.add(dewey);

				CLSTN descriptor = (CLSTN) tag991.getDescriptor();
				descriptor.setTypeCode((short) 29);
				StringText text = new StringText(subfieldList, subfields);
				MarcCommandLibrary.setNewStringText(tag991, text, View
						.makeSingleViewString(SessionUtils
								.getCataloguingView(request)));

				validateCurrentTag();
				createStringTextEditBean();
				// sortTags(super.getLocale());
				resetCommands();
				setNavigation(false);
			}
		}

	}

	// 20110114: inizio
	public void createTag982(HttpServletRequest request, Tag rootTag)
			throws DataAccessException, NewTagException,
			AuthorisationException, ValidationException {
		if ((rootTag instanceof ClassificationAccessPoint)
				&& "082".equals(rootTag.getMarcEncoding().getMarcTag())) {
			ClassificationAccessPoint tag082 = (ClassificationAccessPoint) rootTag;
			String dewey = tag082.getStringText().getSubfieldsWithCodes("a")
					.toDisplayString();

			// Controllo se gia' c'e' un 982 con lo stesso codice dewey
			ClassificationAccessPoint tag982exist = get982TagByDewey(dewey);
			if (tag982exist == null) {
				ClassificationAccessPoint tag982 = (ClassificationAccessPoint) newTag(
						1, (short) 6);
				CorrelationValues v = new CorrelationValues((short) 29,
						(short) 84, (short) -1);
				tag982.setCorrelationValues(v);

				refreshCorrelation(tag982.getCorrelation(1), tag982
						.getCorrelation(2), SessionUtils
						.getCurrentLocale(request));

				List subfieldList = new ArrayList();
				List subfields = new ArrayList();
				subfieldList.add("a");
				subfields.add(dewey);

				String descriptionDewey = null;
				try {
					descriptionDewey = daoCodeTable.getLongText(dewey,
							T_DWY_TYP.class, getLocale());
				} catch (Exception e) {
					descriptionDewey = ResourceBundle.getBundle(
							"resources/cataloguing/bibliographic/editItem",
							getLocale()).getString("missing.dsc");
				}

				subfieldList.add("b");
				subfields.add(descriptionDewey);

				CLSTN descriptor = (CLSTN) tag982.getDescriptor();
				descriptor.setTypeCode((short) 29);
				StringText text = new StringText(subfieldList, subfields);
				MarcCommandLibrary.setNewStringText(tag982, text, View
						.makeSingleViewString(SessionUtils
								.getCataloguingView(request)));

				// validateCurrentTag();
				// createStringTextEditBean();
				sortTags(super.getLocale());
				resetCommands();
				// setNavigation(false);
			}
		}
	}

	// 20110114: fine

	public List getSubdivisionEncoding() throws MarcCorrelationException,
			DataAccessException {
		List li = new ArrayList();
		String marcTag = this.getCurrentTag().getMarcEncoding().getMarcTag();
		if (marcTag.equals("991")) {
			ClassificationAccessPoint tag911 = (ClassificationAccessPoint) getCurrentTag();
			StringText st = new StringText(tag911.getDescriptor()
					.getStringText());
			List subfields = st.getSubfieldList();
			Iterator it = subfields.iterator();
			while (it.hasNext()) {
				Avp valueElement = new Avp();
				Subfield sf = (Subfield) it.next();
				String code = sf.getCode();
				String content = sf.getContent();
				if (code.equals("b") || code.equals("c") || code.equals("d")) {
					String encoding = CodeTableParser.getStringText(sf
							.getContent(), code, getLocale().getISO3Language());
					valueElement.setLabel(code);
					valueElement.setValue(encoding);
				} else if (code.equals("a")) {
					valueElement.setLabel(code);
					valueElement.setValue(content + " Dewey");
				} else {
					valueElement.setLabel(code);
					valueElement.setValue(content);
				}
				li.add(valueElement);
			}
		}
		return li;
	}

	public boolean isSkipFiling() throws MarcCorrelationException,
			DataAccessException {
		String marcTag = this.getCurrentTag().getMarcEncoding().getMarcTag();
		if (getCurrentTag().getCategory() == 3
				|| (getCurrentTag().getCategory() == 4 && marcTag.equals("630")))
			return true;
		else
			return false;

	}

	public void create997UserProfile(HttpServletRequest request)
			throws DataAccessException, NewTagException,
			AuthorisationException, ValidationException {
		UserProfile user = SessionUtils.getUserProfile(request
				.getSession(false));
		ClassificationAccessPoint t997 = (ClassificationAccessPoint) getCatalogItem()
				.findFirstTagByNumber("997");
		if (t997 == null) {
			ClassificationAccessPoint tag997 = (ClassificationAccessPoint) newTag(
					1, (short) 6);
			CorrelationValues v = new CorrelationValues((short) 29, (short) 79,
					(short) -1);
			tag997.setCorrelationValues(v);

			refreshCorrelation(tag997.getCorrelation(1), tag997
					.getCorrelation(2), SessionUtils.getCurrentLocale(request));

			List subfieldList = new ArrayList();
			List subfields = new ArrayList();
			subfieldList.add("a");
			subfields.add(user.getName());
			CLSTN descriptor = (CLSTN) tag997.getDescriptor();
			descriptor.setTypeCode((short) 29);
			StringText text = new StringText(subfieldList, subfields);
			MarcCommandLibrary.setNewStringText(tag997, text, View
					.makeSingleViewString(SessionUtils
							.getCataloguingView(request)));
		}
		validateCurrentTag();
		sortTags(super.getLocale());
		resetCommands();
		setNavigation(true);
	}

	public void create032TagLibricat(HttpServletRequest request)
			throws DataAccessException, NewTagException,
			AuthorisationException, ValidationException {
		UserProfile user = SessionUtils.getUserProfile(request
				.getSession(false));
		ControlNumberAccessPoint t032 = (ControlNumberAccessPoint) getCatalogItem()
				.findFirstTagByNumber("032");
		if (t032 == null) {
			ControlNumberAccessPoint tag032 = (ControlNumberAccessPoint) newTag(
					1, (short) 5);

			CorrelationValues v = new CorrelationValues((short) 53, (short) 8,
					(short) -1);
			tag032.setCorrelationValues(v);

			refreshCorrelation(tag032.getCorrelation(1), tag032
					.getCorrelation(2), SessionUtils.getCurrentLocale(request));

			List subfieldList = new ArrayList();
			List subfields = new ArrayList();
			subfieldList.add("a");
			subfields.add("LibriCat");
			CNTL_NBR descriptor = (CNTL_NBR) tag032.getDescriptor();
			descriptor.setTypeCode((short) 53);
			StringText text = new StringText(subfieldList, subfields);
			MarcCommandLibrary.setNewStringText(tag032, text, View
					.makeSingleViewString(SessionUtils
							.getCataloguingView(request)));
		}

		validateCurrentTag();
		sortTags(super.getLocale());
		resetCommands();
		setNavigation(true);

	}

	public void modify997UserProfile(HttpServletRequest request)
			throws DataAccessException, NewTagException,
			AuthorisationException, ValidationException {
		UserProfile user = SessionUtils.getUserProfile(request
				.getSession(false));
		ClassificationAccessPoint t997 = (ClassificationAccessPoint) getCatalogItem()
				.findFirstTagByNumber("997");
		if (t997 != null) {
			// aggiunta eventuale del $b
			StringText text = new StringText(t997.getDescriptor()
					.getStringText());
			Subfield sub = new Subfield("b", user.getName());
			// Stesso utente
			if (!text.containsSubfield(sub)) {
				if (text.getSubfieldList().size() > 1
						&& ((Subfield) text.getSubfieldList().get(1)).getCode()
								.equals("b")) {
					// rimuove il $b
					text.removeSubfield(1);
				}
				text.addSubfield(new Subfield(sub));

				CLSTN newDescriptor = new CLSTN(); // creazione nuovo
													// descrittore chiave di
													// ricerca
				newDescriptor.setTypeCode((short) 29);
				newDescriptor.setUserViewString(t997.getUserViewString());
				newDescriptor.setStringText(text.toString());
				Descriptor replaceDescriptor = MarcCommandLibrary
						.createNewDescriptor(newDescriptor, t997
								.getUserViewString());
				MarcCommandLibrary.replaceDescriptor(getCatalogItem(), t997,
						replaceDescriptor);
			}
			validateCurrentTag();
			sortTags(super.getLocale());
			resetCommands();
			setNavigation(true);
		} else
			/* se non è presente viene creato anche in modifica */
			create997UserProfile(request);

	}

	public boolean isModifyCatalogItem(final CatalogItem item)
			throws DataAccessException {
		Tag aTag = null;
		boolean isModify = false;
		Iterator iter = item.getTags().iterator();
		while (iter.hasNext()) {
			aTag = (Tag) iter.next();
			if (aTag.isNew() || (item.getDeletedTags().size() >= 1)
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
		}
		return count;
	}

	/**
	 * Restituisce la lista filtrata
	 *
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

	public String getNoteStandardText() {
		String noteStandardText = "";
		BibliographicNoteTag tag = (BibliographicNoteTag) getCurrentTag();
		if (tag.getValueElement() != null)
			noteStandardText = tag.getValueElement().getLabel();
		return noteStandardText;
	}

	public boolean isNoteStandard() {
		boolean noteStandard = false;
		BibliographicNoteTag tag = (BibliographicNoteTag) getCurrentTag();
		if (tag.getValueElement() != null)
			noteStandard = true;
		else
			noteStandard = false;
		return noteStandard;
	}

	public List getDiacritici() {
		DAOCodeTable dao = new DAOCodeTable();
		List diacritici = null;
		try {
			return dao.getDiacritici();
		} catch (DataAccessException e) {
			logger.warn("Error in load table of the diacritics");
			logger.warn(e);
		}
		return diacritici;
	}

	public void find(String searchString) {
		List indexSearchList = new ArrayList();
		List diacriticiList = getDiacriticiList();
		int nRows = diacriticiList.size();
		for (int r = 0; r < nRows; r++) {
			Diacritici diacritici = (Diacritici) diacriticiList.get(r);
			if (isPresent(diacritici, searchString.toLowerCase())) {
				indexSearchList.add(diacritici);
			}
		}
		getDiacritici().removeAll(diacriticiList);
		setDiacriticiList(indexSearchList);
	}

	private final boolean isPresent(Diacritici diacritici, String searchString) {
		String element = diacritici.getNomeCarattere();
		if (element.toLowerCase().indexOf(searchString) >= 0) {
			return true;
		}
		return false;
	}

	// public void createEquivalentTag792(int amicuNumber) throws
	// MarcCorrelationException, DataAccessException, RecordInUseException,
	// NewTagException, AuthorisationException, ValidationException
	// {
	// amicuNumber = getCatalogItem().getAmicusNumber().intValue();
	// BibliographicRelationshipTag tag2 = (BibliographicRelationshipTag)
	// getCatalogItem().findFirstTagByNumber("791");
	// String w =
	// tag2.getStringText().getSubfieldsWithCodes("w").toDisplayString();
	// loadItemDuplicate(new Integer(w).intValue(), 1);
	// if (getCatalogItem().findFirstTagByNumber("792") == null) {
	// newTag(0, (short) 8);
	// changeValues(2, 2, -1);
	// BibliographicRelationshipTag tag = (BibliographicRelationshipTag)
	// getCurrentTag();
	// tag.setReciprocalOption((short) 2); // one-way
	// updateRelationshipFromSearch(amicuNumber, 1);
	//
	// saveRecord();
	// }
	// }

	public void createEquivalentTag792(int amicuNumber)
			throws MarcCorrelationException, DataAccessException,
			RecordInUseException, NewTagException, AuthorisationException,
			ValidationException, DuplicateTagException {
		amicuNumber = getCatalogItem().getAmicusNumber().intValue();
		int cataloguingView = getCatalogItem().getUserView();
		BibliographicRelationshipTag tag2 = (BibliographicRelationshipTag) getCatalogItem()
				.findFirstTagByNumber("791");
		String w = tag2.getStringText().getSubfieldsWithCodes("w")
				.toDisplayString();
		// pm aut - removed loadItemDuplicate
		setCatalogItem(getCatalog().getCatalogItem(
				new Object[] { new Integer(w), new Integer(cataloguingView) }));
		if (getCatalogItem().findFirstTagByNumber("792") == null) {
			newTag(0, (short) 8);
			changeValues(2, 2, -1);
			BibliographicRelationshipTag tag = (BibliographicRelationshipTag) getCurrentTag();
			tag.setReciprocalOption((short) 2); // one-way
			updateRelationshipFromSearch(amicuNumber, cataloguingView);
			// Creazione tag 092
			if (getCatalogItem().findFirstTagByNumber("092") == null) {
				newTag(0, (short) 6);
				changeValues(16, 61, -1);
				ClassificationAccessPoint cnap = (ClassificationAccessPoint) getCurrentTag();
				StringText text = new StringText(new Subfield("a", "" + w));
				MarcCommandLibrary.setNewStringText(cnap, text, View
						.makeSingleViewString(getCatalogItem().getUserView()));
				// getCatalogItem().addTag(cnap);
			}
			saveRecord();
		}
	}

	public void createTag092() throws MarcCorrelationException,
			NewTagException, AuthorisationException, DataAccessException,
			ValidationException {
		// //Creazione tag 092
		if (!(getCasaliniBean() == null) && getCasaliniBean().isEnabled()) {
			if (getCatalogItem().findFirstTagByNumber("092") == null) {
				CataloguingSourceTag cat040 = (CataloguingSourceTag) getCatalogItem()
						.findFirstTagByNumber("040");
				StringText st = cat040.getStringText().getSubfieldsWithCodes(
						"b");
				if (st.getSubfieldList().size() > 0)
					if (st.getDisplayText().equals("eng")) {
						newTag(0, (short) 6);
						changeValues(16, 61, -1);
						ClassificationAccessPoint cnap = (ClassificationAccessPoint) getCurrentTag();
						StringText text = new StringText(
								new Subfield("a", ""
										+ getCatalogItem().getAmicusNumber()
												.intValue()));
						MarcCommandLibrary.setNewStringText(cnap, text, View
								.makeSingleViewString(getCatalogItem()
										.getUserView()));
					}
			}
		}
	}

	/**
	 * BUG 3063: This method selects the default if the tag 773 bibliographic
	 * level = 'a' or 'b' when you select the category of relations.
	 */
	public void setRelationForAnalytical() {
		BibliographicLeader leader = (BibliographicLeader) getCatalogItem()
				.findFirstTagByNumber("000");
		if (leader.getItemBibliographicLevelCode() == 'a'
				|| leader.getItemBibliographicLevelCode() == 'b') {
			if ((getCurrentTag()) instanceof BibliographicRelationshipTag)
				((BibliographicRelationshipTag) getCurrentTag())
						.setRelationTypeCode((short) 8);
		}
	}

	public void createTag097(HttpServletRequest request, StringText text,
			String hierarchyType) throws MarcCorrelationException,
			NewTagException, AuthorisationException, DuplicateKeyException,
			DataAccessException, ValidationException, RecordInUseException,
			MandatoryTagException {
		// ----> 20100617: poiche' la gestione del tag 097 non e' standard prima
		// di creare il tag
		// ----> devo essere sicura che non esista l'occorrenza sulla CAS_FILES
		// o sulla CAS_DIG_FILES
		// ----> altrimenti crea il tag e poi da errore di chiave duplicata e si
		// disallinea tutto LibriCat!
		if ("S".equalsIgnoreCase(getCheckDigital())) {
			DAOCasDigFiles daoCasDigFiles = new DAOCasDigFiles();
			int bibItemFiglia = 0;
			int bibItemMadre = 0;
			if (text.getSubfieldsWithCodes("c").getDisplayText().toString()
					.trim().length() > 0)
				bibItemFiglia = Integer.parseInt(text
						.getSubfieldsWithCodes("c").getDisplayText().trim());
			if (text.getSubfieldsWithCodes("a").getDisplayText().toString()
					.trim().length() > 0)
				bibItemMadre = Integer.parseInt(text.getSubfieldsWithCodes("a")
						.getDisplayText().trim());
			if (daoCasDigFiles
					.loadCasDigFilesByKey(bibItemFiglia, bibItemMadre).size() > 0)
				throw new DuplicateKeyException();
		} else {
			DAOCasFiles daoCasFiles = new DAOCasFiles();
			if (daoCasFiles.loadCasFilesByKey(
					text.getSubfieldsWithCodes("c").getDisplayText().trim(),
					text.getSubfieldsWithCodes("a").getDisplayText().trim())
					.size() > 0)
				throw new DuplicateKeyException();
		}

		getCatalogItem().checkForMandatoryTags();
		// Mettere il controllo che se gia' presente un tag 097 del tipo digital
		// deve dare errore!!!!
		// ControlNumberAccessPoint tag097 =
		// (ControlNumberAccessPoint)getCatalogItem().findFirstTagByNumber("097");

		ControlNumberAccessPoint tag097 = (ControlNumberAccessPoint) newTag(1,
				(short) 5);
		CorrelationValues v = null;

		if ((getCheckDigital() != null)
				&& (getCheckDigital().equalsIgnoreCase("S"))) {
			v = new CorrelationValues((short) 69, (short) 8, (short) -1);
		} else if ((getCheckOnline() != null)
				&& (getCheckOnline().equalsIgnoreCase("S"))) {
			v = new CorrelationValues((short) 71, (short) 8, (short) -1);
		} else {
			v = new CorrelationValues((short) 70, (short) 8, (short) -1);
		}

		tag097.setCorrelationValues(v);
		refreshCorrelation(tag097.getCorrelation(1), tag097.getCorrelation(2),
				SessionUtils.getCurrentLocale(request));
		changeText(text);

		CNTL_NBR descriptor = (CNTL_NBR) tag097.getDescriptor();
		descriptor.setTypeCode(tag097.getCorrelation(1));
		MarcCommandLibrary
				.setNewStringText(tag097, text, View
						.makeSingleViewString(SessionUtils
								.getCataloguingView(request)));

		createTag773ForComponentPart();

		validateCurrentTag();
		sortTags(super.getLocale());
		resetCommands();
		setNavigation(true);
		setPresentTag097(true);

		updateCasCacheBeforeSaveRecord();

		saveRecord();

		getCatalog().unlock(getCatalogItem().getAmicusNumber().intValue());

		/*
		 * 20100816 inizio: dopo il salvataggio intermedio deve ricaricare il
		 * record per ripulire il catalogItem (altrimenti duplicava il tag 260)
		 */
		loadItem(getCatalogItem().getAmicusNumber().intValue(), SessionUtils
				.getCataloguingView(request));
		sortTags(SessionUtils.getCurrentLocale(request));
	}

	/**
	 * Bug 1594 This method creates the tag 773 for the level 012 (Serial
	 * component part) or 013 (Serial article) or 015(Monographic component
	 * part)
	 * 
	 * @throws NewTagException
	 * @throws AuthorisationException
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 * 
	 */
	private void createTag773ForComponentPart() throws NewTagException,
			AuthorisationException, DataAccessException,
			MarcCorrelationException {
		ControlNumberAccessPoint tag097;
		BibliographicRelationshipTag tag773 = (BibliographicRelationshipTag) getCatalogItem()
				.findFirstTagByNumber("773");
		tag097 = (ControlNumberAccessPoint) getCatalogItem()
				.findFirstTagByNumber("097");
		if (tag773 == null) {
			String amicuNumber = tag097.getStringText().getSubfieldsWithCodes(
					"a").getDisplayText().toString().trim();
			String level = tag097.getStringText().getSubfieldsWithCodes("b")
					.getDisplayText().toString().trim();
			if (amicuNumber.length() > 0) {
				if (level.equals("012") || level.equals("013")
						|| level.equals("015")) {
					newTag(0, (short) 8);
					changeValues(8, 2, -1);
					BibliographicRelationshipTag tag = (BibliographicRelationshipTag) getCurrentTag();
					tag.setReciprocalOption((short) 2); // one-way
					updateRelationshipFromSearch(new Integer(amicuNumber)
							.intValue(), 1);
				}
			}
		}
	}

	public void saveHierarchy(ControlNumberAccessPoint tag097)
			throws DataAccessException {
		CasFiles casFiles = new CasFiles();
		if (tag097 != null) {
			if (tag097.getStringText().getSubfieldsWithCodes("a")
					.getDisplayText().toString().trim().length() > 0)
				casFiles.setBibItemNumberMadre(new Integer(tag097
						.getStringText().getSubfieldsWithCodes("a")
						.getDisplayText().toString()).intValue());

			if (tag097.getStringText().getSubfieldsWithCodes("b")
					.getDisplayText().toString().trim().length() > 0)
				casFiles
						.setNtrLevel(tag097.getStringText()
								.getSubfieldsWithCodes("b").getDisplayText()
								.toString());

			if (tag097.getStringText().getSubfieldsWithCodes("c")
					.getDisplayText().toString().trim().length() > 0)
				casFiles.setBibItemNumberFiglia(new Integer(tag097
						.getStringText().getSubfieldsWithCodes("c")
						.getDisplayText().toString()).intValue());

			if (tag097.getStringText().getSubfieldsWithCodes("d")
					.getDisplayText().toString().trim().length() > 0)
				casFiles
						.setOrderProgr(new Integer(tag097.getStringText()
								.getSubfieldsWithCodes("d").getDisplayText()
								.toString()));

			if (tag097.getStringText().getSubfieldsWithCodes("e")
					.getDisplayText().toString().trim().length() > 0)
				casFiles
						.setNumTomo(new Integer(tag097.getStringText()
								.getSubfieldsWithCodes("e").getDisplayText()
								.toString()));

			if (tag097.getStringText().getSubfieldsWithCodes("f")
					.getDisplayText().toString().trim().length() > 0)
				casFiles
						.setNumEdizione(new Integer(tag097.getStringText()
								.getSubfieldsWithCodes("f").getDisplayText()
								.toString()));

			if (tag097.getStringText().getSubfieldsWithCodes("g")
					.getDisplayText().toString().trim().length() > 0)
				casFiles.setNote(tag097.getStringText().getSubfieldsWithCodes(
						"g").getDisplayText().toString());

			// 20101020 inizio: nuovo sottocampo tipo progressivo
			if (tag097.getStringText().getSubfieldsWithCodes("h")
					.getDisplayText().toString().trim().length() > 0)
				casFiles
						.setProgressiveType(tag097.getStringText()
								.getSubfieldsWithCodes("h").getDisplayText()
								.toString());
			// 20101020 fine

			// --------> Deve salvare anche la chiave della cntr_nbr
			casFiles.setCntlKeyNbr(tag097.getHeadingNumber().intValue());
		}
		DAOCasFiles dao = (DAOCasFiles) casFiles.getDAO();
		dao.persistCasFiles(casFiles, tag097);
	}

	public void cntrTag098(ControlNumberAccessPoint tag097)
			throws DataAccessException, AuthorisationException {
		List tags098 = null;
		ControlNumberAccessPoint tag098 = null;
		String tag097Mother = new String();
		String tag098Mother = new String();

		// Devo cancellare il tag098 che ha l'amicusNumber madre uguale alla
		// madre del tag097 inserito

		if (tag097 != null
				&& tag097.getStringText().getSubfieldsWithCodes("a")
						.getDisplayText().toString().trim().length() > 0) {
			tag097Mother = tag097.getStringText().getSubfieldsWithCodes("a")
					.getDisplayText().toString().trim();
			tags098 = get098Tags();
			for (int i = 0; i < tags098.size(); i++) {
				tag098 = (ControlNumberAccessPoint) tags098.get(i);
				if (tag098.getStringText().getSubfieldsWithCodes("a")
						.getDisplayText().toString().trim().length() > 0) {
					tag098Mother = tag098.getStringText()
							.getSubfieldsWithCodes("a").getDisplayText()
							.toString().trim();
					if (tag097Mother.equalsIgnoreCase(tag098Mother)) {
						// getCatalogItem().addDeletedTag(tag098);
						int t = getCatalogItem().getTags().indexOf(tag098);
						setTagIndex(t);
						deleteTag();
						/* modifica barbara ordinamento dei tag */
						setTagIndex(getCatalogItem().getNumberOfTags() - 1);
						getCatalogItem().sortTags();
						refreshCorrelation(getCurrentTag().getCorrelation(1),
								getCurrentTag().getCorrelation(2), getLocale());
						setCurrentCommand(0);
						/* Modifica Carmen 7/03/2007 era false */
						setNavigation(true);
					}
				}
			}
		}
	}

	public void deleteHierarchy(ControlNumberAccessPoint tag)
			throws DataAccessException {
		DAOCasFiles dao = new DAOCasFiles();
		dao.deleteCasFiles(tag);
	}

	public void saveDigitalHierarchy(ControlNumberAccessPoint tag097)
			throws DataAccessException {
		CasDigFiles casDigFiles = new CasDigFiles();
		if (tag097 != null) {
			if (tag097.getStringText().getSubfieldsWithCodes("a")
					.getDisplayText().toString().trim().length() > 0)
				casDigFiles.setBibItemNumberMadre(new Integer(tag097
						.getStringText().getSubfieldsWithCodes("a")
						.getDisplayText().toString()).intValue());

			if (tag097.getStringText().getSubfieldsWithCodes("b")
					.getDisplayText().toString().trim().length() > 0)
				casDigFiles
						.setDigLevel(tag097.getStringText()
								.getSubfieldsWithCodes("b").getDisplayText()
								.toString());

			if (tag097.getStringText().getSubfieldsWithCodes("c")
					.getDisplayText().toString().trim().length() > 0)
				casDigFiles.setBibItemNumberFiglia(new Integer(tag097
						.getStringText().getSubfieldsWithCodes("c")
						.getDisplayText().toString()).intValue());

			if (tag097.getStringText().getSubfieldsWithCodes("d")
					.getDisplayText().toString().trim().length() > 0)
				casDigFiles
						.setOrderProgr(new Integer(tag097.getStringText()
								.getSubfieldsWithCodes("d").getDisplayText()
								.toString()));

			if (tag097.getStringText().getSubfieldsWithCodes("g")
					.getDisplayText().toString().trim().length() > 0)
				casDigFiles
						.setNote(tag097.getStringText().getSubfieldsWithCodes(
								"g").getDisplayText().toString());

			// 20101020 inizio: nuovo sottocampo tipo progressivo
			if (tag097.getStringText().getSubfieldsWithCodes("h")
					.getDisplayText().toString().trim().length() > 0) {
				casDigFiles
						.setProgressiveType(tag097.getStringText()
								.getSubfieldsWithCodes("h").getDisplayText()
								.toString());
			}
			// 20101020 fine

			// 20110201 inizio: nuovo sottocampo per anno
			if (tag097.getStringText().getSubfieldsWithCodes("i")
					.getDisplayText().toString().trim().length() > 0) {
				casDigFiles
						.setYear(new Integer(tag097.getStringText()
								.getSubfieldsWithCodes("i").getDisplayText()
								.toString()));
			}
			// 20110201 fine

			// --------> Deve salvare anche la chiave della cntr_nbr
			casDigFiles.setCntlKeyNbr(tag097.getHeadingNumber().intValue());
		}
		DAOCasDigFiles dao = (DAOCasDigFiles) casDigFiles.getDAO();
		// dao.persistCasDigFiles(casDigFiles.getBibItemNumberFiglia(),
		// casDigFiles);
		dao.persistCasDigFiles(casDigFiles);
	}

	// public void deleteDigitalHierarchy(int bibNumber) throws
	// DataAccessException
	public void deleteDigitalHierarchy(ControlNumberAccessPoint tag)
			throws DataAccessException {
		DAOCasDigFiles dao = new DAOCasDigFiles();
		dao.deleteCasDigFiles(tag);
	}

	public void cntrHierarchyType() {
		if ((getCheckDigital() != null)
				&& (getCheckDigital().equalsIgnoreCase("S"))) {
			// Se digitale e multivolume
			if ((getOptDigitalLevel().equalsIgnoreCase("012"))
					|| (getOptDigitalLevel().equalsIgnoreCase("013"))
					|| (getOptDigitalLevel().equalsIgnoreCase("015"))) {
				setHierarchyType("1");
				BibliographicRelationshipTag tag773 = (BibliographicRelationshipTag) getCatalogItem()
						.findFirstTagByNumber("773");
				if (tag773 != null) {
					if (tag773.getStringText().getSubfieldsWithCodes("w")
							.getDisplayText().toString().trim().length() > 0) {
						setAmicusNumberMother(new Integer(tag773
								.getStringText().getSubfieldsWithCodes("w")
								.getDisplayText()));
					}
				}
				// Se digitale e volume
			} else if (getOptDigitalLevel().equalsIgnoreCase("014")
					|| getOptDigitalLevel().equalsIgnoreCase("011"))
				setHierarchyType("2");

		} else {
			// Se non digitale e natura livello indica un livello madre
			if ((getOptDigitalLevel().equalsIgnoreCase("002"))
					|| (getOptDigitalLevel().equalsIgnoreCase("003")) ||
					// (getOptDigitalLevel().equalsIgnoreCase("004")) ||
					(getOptDigitalLevel().equalsIgnoreCase("006"))) {
				setHierarchyType("2");
			} else if (getOptDigitalLevel().equalsIgnoreCase("004"))
				setHierarchyType("1");
			else {
				// Se non digitale e natura livello indica un livello figlia
				if ((getOptDigitalLevel().equalsIgnoreCase("005"))
						|| (getOptDigitalLevel().equalsIgnoreCase("007"))
						|| (getOptDigitalLevel().equalsIgnoreCase("008"))
						|| (getOptDigitalLevel().equalsIgnoreCase("009"))) {
					// setHierarchyType("3");
					setHierarchyType("1");
					if (isPresentTag097()) {
						ControlNumberAccessPoint tag097 = (ControlNumberAccessPoint) getCatalogItem()
								.findFirstTagByNumber("097");
						if (tag097 != null) {
							if (tag097.getStringText().getSubfieldsWithCodes(
									"a").getDisplayText().toString().trim()
									.length() > 0) {
								setAmicusNumberMother(new Integer(tag097
										.getStringText().getSubfieldsWithCodes(
												"a").getDisplayText()));
							}
						}
					}
				}
			}
		}
	}

	public void refreshBean() {
		setCheckNational("0");
		setVerificationLevel("3");
		setCheckDigital("N");
		setCheckOnline("N");
		setCheckContinuaz("N");
		setOptDigitalLevel("001");
		setOptManagerialLevel("L1");
		setMdrFgl("001");
		setLevelDisable(true);
		setAmicusNumberMother(null);
		setPresentTag097(false);
		setHierarchyType("");
		setSearchingMother(false);
		setLevelForSearchMother(null);
		setDigitalOperation("0");
		setSendDoi(false);
		setModifyDoi(false);
		setInternalDoiPermitted(false);
		setMdrFglDisable(true);
		setAddCatalogItem(false);
		setFormatRecordType("F1");
		setCheckSkipSearch("N");
		setCheckNCORE("N");
		setCheckNTOCSB("N");
		setWemiFirstGroup(null);
		setWemiGroupLabel(null);
		// setDigitalOperation(null);
	}

	public List get856Tags() throws DataAccessException {
		List tags = getCatalogItem().getTags();
		List tags856 = new ArrayList();
		Iterator it = tags.iterator();
		while (it.hasNext()) {
			Tag tag = (Tag) it.next();
			if (tag instanceof BibliographicNoteTag) {
				if (tag.getMarcEncoding().getMarcTag().equalsIgnoreCase("856"))
					// ----------------> Devo prendere solo gli 856 che crea
					// WeCat
					if (tag.getCorrelation(1) == 346
							&& tag.getCorrelation(2) == -1
							&& tag.getCorrelation(3) == -1) {
						// System.out.println("--------------> Tag 856 di WeCat
						// caricato");
						tags856.add(tag);
					}
			}
		}
		return tags856;
	}

	public List presenzaTag856Testo() throws DataAccessException {
		List tagsTesto856 = new ArrayList();

		List tags856 = get856Tags();
		for (Iterator iterator = tags856.iterator(); iterator.hasNext();) {
			BibliographicNoteTag tag = (BibliographicNoteTag) iterator.next();
			// if
			// ("Testo".equalsIgnoreCase(tag.getStringText().getSubfieldsWithCodes("3").getDisplayText().trim())){
			if (DIGITAL_TEXT_RESOURCE.equalsIgnoreCase(tag.getStringText()
					.getSubfieldsWithCodes("3").getDisplayText().trim())) {
				tagsTesto856.add(tag);
			}
		}
		return tagsTesto856;
	}

	/**
	 * Metodo che ricerca nel record almeno un tag 856 di tipo TESTO con codice
	 * Doi assegnato e ritorna True se l'ha trovato e False se non l'ha trovato
	 */
	public boolean isPresentTag856Testo() throws DataAccessException {
		boolean isPresent = false;
		List tags856 = get856Tags();
		for (Iterator iterator = tags856.iterator(); iterator.hasNext();) {
			BibliographicNoteTag tag = (BibliographicNoteTag) iterator.next();
			if (DIGITAL_TEXT_RESOURCE.equalsIgnoreCase(tag.getStringText()
					.getSubfieldsWithCodes("3").getDisplayText().trim())) {
				if (tag.getStringText().getSubfieldsWithCodes("w")
						.getDisplayText().trim().length() > 0) {
					isPresent = true;
					break;
				}
			}
		}
		return isPresent;
	}

	/**
	 * Metodo che ritorna l'eventuale codice doi associato al record
	 */
	public String getDoiCode() throws DataAccessException {
		String doi = "";
		List tags856 = get856Tags();
		for (Iterator iterator = tags856.iterator(); iterator.hasNext();) {
			BibliographicNoteTag tag = (BibliographicNoteTag) iterator.next();
			if (DIGITAL_TEXT_RESOURCE.equalsIgnoreCase(tag.getStringText()
					.getSubfieldsWithCodes("3").getDisplayText().trim())) {
				if (tag.getStringText().getSubfieldsWithCodes("w")
						.getDisplayText().trim().length() > 0) {
					doi = tag.getStringText().getSubfieldsWithCodes("w")
							.getDisplayText().trim();
					break;
				}
			}
		}
		return doi;
	}

	/**
	 * Metodo che ritorna il tag856 con doi associato al record
	 */
	public BibliographicNoteTag get856WithDoi() throws DataAccessException {
		List tags856 = get856Tags();
		BibliographicNoteTag tag856WithDoi = null;
		for (Iterator iterator = tags856.iterator(); iterator.hasNext();) {
			BibliographicNoteTag tag = (BibliographicNoteTag) iterator.next();
			if (DIGITAL_TEXT_RESOURCE.equalsIgnoreCase(tag.getStringText()
					.getSubfieldsWithCodes("3").getDisplayText().trim())) {
				if (tag.getStringText().getSubfieldsWithCodes("w")
						.getDisplayText().trim().length() > 0) {
					tag856WithDoi = tag;
					break;
				}
			}
		}
		return tag856WithDoi;
	}

	public void aggiornaDoi(BibliographicNoteTag tag856, String codiceDoi)
			throws MarcCorrelationException, AuthorisationException,
			DataAccessException, ValidationException {
		// --> ISTRUZIONE PER IMPOSTARE IL TAG CORRENTE CON QUELLO PASSATO!!!
		int t = getCatalogItem().getTags().indexOf(tag856);
		setTagIndex(t);

		BibliographicNoteTag tagTesto = (BibliographicNoteTag) tag856;
		StringText text = tagTesto.getStringText();
		Subfield sub = new Subfield("w", codiceDoi);
		List lista = text.getSubfieldList();
		for (int i = 0; i < lista.size(); i++) {
			if (((Subfield) lista.get(i)).getCode().equalsIgnoreCase("w")) {
				text.removeSubfield(i);
			}
		}
		text.addSubfield(sub);
		changeText(text);
		validateCurrentTag();
		sortTags(super.getLocale());
		resetCommands();
		setNavigation(true);
	}

	public void modify097(Tag rootTag, StringText text)
			throws DataAccessException, NewTagException,
			AuthorisationException, ValidationException {
		if (rootTag instanceof ControlNumberAccessPoint) {
			if (rootTag.getMarcEncoding().getMarcTag().equals("097")) {
				ControlNumberAccessPoint t097 = (ControlNumberAccessPoint) rootTag;
				if (t097 != null) {
					// ----------> Deve andare in update del contr_nbr gia'
					// presente
					CNTL_NBR descriptor = (CNTL_NBR) t097.getDescriptor();
					descriptor.setTypeCode(t097.getCorrelation(1));
					descriptor.setUserViewString(t097.getUserViewString());
					descriptor.setStringText(text.toString());
					descriptor.markChanged();
					t097.markChanged();
					descriptor.getDAO().persistByStatus(descriptor);
				}

				validateCurrentTag();
				sortTags(super.getLocale());
				resetCommands();
				setNavigation(true);
			}
		}
	}

	private void saveDescriptor(Descriptor descriptor)
			throws DataAccessException {
		try {
			((DAODescriptor) descriptor.getDAO()).persist(descriptor);
		} catch (DataAccessException e) {
			Throwable th = e.getCause();
			Throwable prevTh = null; // to prevent loops
			while (th != null && prevTh != th) {
				if (th instanceof JDBCException) {
					JDBCException jdbcEx = (JDBCException) th;
					int rc = jdbcEx.getErrorCode();
					if (rc == 1) {// ORA-00001 MIKE: this block is oracle
									// specific!
						DuplicateDescriptorException dde = new DuplicateDescriptorException(
								e);
						// unfortunately here we haven't the saved descriptor
						// I still set in exception the current descriptor
						dde.setDescriptorFound(descriptor);
						throw dde;
					}
				}
				prevTh = th;
				th = prevTh.getCause();
			}
			throw e;
		}
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
					count = getCatalogItem().findTagsEqual(
							((AccessPoint) rootTag).getFunctionCode()).size();
					return count;
				} else if (rootTag instanceof FixedField) {
					count = getCatalogItem().findTagsFixedEqual(
							rootTag.getMarcEncoding().getMarcTag()).size();
					return count;
				} else if (rootTag instanceof VariableField) {
					count = getCatalogItem().findTagsVariableEqual(
							rootTag.getMarcEncoding().getMarcTag()).size();
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

	/**
	 * Se un DIGITAL viene trasformato in NO DIGITAL ed e' presente
	 * un�occorrenza nella CAS_DIG_ADMIN visualizzare un errore del tipo
	 * �Per modificare la natura del record prima cancellare la scheda
	 * amministrativa digitale (DGA-Digital)� Se un NO DIGITAL viene
	 * trasformato in DIGITAL ed e' presente un�occorrenza nella S_CAS_CACHE
	 * visualizzare un errore del tipo �Per modificare la natura del record
	 * prima cancellare la scheda amministrativa (DLA)�
	 * 
	 * @param checkDigital
	 * @throws DataAccessException
	 * @throws DataAdminException
	 * @throws DataDigAdminException
	 */
	public void verifyAdminData(String checkDigital)
			throws DataAccessException, DataAdminException,
			DataDigAdminException {
		CasCache casCache = null;
		casCache = getCasaliniBean().loadCasCache(
				getCatalogItem().getAmicusNumber().intValue());

		if (checkDigital.equalsIgnoreCase("S")) {
			if (casCache != null) {
				if (casCache.getDigCheck() != null
						&& !casCache.getDigCheck().equalsIgnoreCase("S")) {
					if (casCache.isExistAdminData())
						throw new DataAdminException();
				}
			}
		} else {
			if (casCache != null) {
				if (casCache.getDigCheck() != null
						&& casCache.getDigCheck().equalsIgnoreCase("S")) {
					// DigitalAmminBean digitalAmminBean =
					// DigitalAmminBean.getInstance(request);
					DigitalAmminBean digitalAmminBean = new DigitalAmminBean();
					digitalAmminBean.loadItems(getCatalogItem()
							.getAmicusNumber().intValue());
					if (digitalAmminBean.isExistItem()) {
						throw new DataDigAdminException();
					}
				}
			}
		}
	}

	public void createTag260(PUBL_HDG publHdg, HttpServletRequest request)
			throws DataAccessException, AuthorisationException, NewTagException {
		newTag(0, (short) 7);
		changeValues(24, -1, -1);
		PublisherManager t = (PublisherManager) getCurrentTag();

		t.parseForEditing();
		t.addNewTagUnit();

		List date = new ArrayList(1);
		date.add(new String(""));
		t.setDates(date);
		((PUBL_TAG) t.getPublisherTagUnits().get(0)).setDescriptor(publHdg);
		t.saveEdits();
		replaceCurrentTag(t);
		// t =(PublisherManager) LibrisuiteUtils.deepCopy(getCurrentTag());
		setRevisedPublisher(t);

		// sortTags(super.getLocale());
		// resetCommands();
		// setNavigation(true);
		setNavigation(false);
	}

	public boolean isEquivalentEnabled() {
		return equivalentEnabled;
	}

	// --> 20100901 inizio: metodo che non permette la cancellazione del record
	// se prima non si cancella la DGA associata
	// public void controlDigAdminDataForDelete(int amicusNumber) throws
	// DataAccessException, DataDigAdminException
	public void controlDigAdminDataForDelete(int amicusNumber)
			throws DataAccessException {
		CasaliniContextBean casalini = getCasaliniBean();
		if (casalini != null && casalini.isEnabled()) {
			CasCache casCache = null;
			casCache = casalini.loadCasCache(amicusNumber);
			if (casCache != null) {
				if ("S".equalsIgnoreCase(casCache.getDigCheck())) {
					DigitalAmminBean digitalAmminBean = new DigitalAmminBean();
					digitalAmminBean.loadItems(amicusNumber);
					if (digitalAmminBean.isExistItem()) {
						digitalAmminBean.delete();
					}
				}
			}
		}
	}

	// --> 20100901 fine

	// --> 20100909 inizio: creazione tag998
	public void createTag998(String publisherCode) throws DataAccessException,
			NewTagException, AuthorisationException, ValidationException {
		ClassificationAccessPoint t998 = (ClassificationAccessPoint) getCatalogItem()
				.findFirstTagByNumber("998");
		if (t998 != null) {
			int i = getCatalogItem().getTags().indexOf(t998);
			setTagIndex(i);
			deleteTag();
			// setTagIndex(getCatalogItem().getNumberOfTags()-1);
			// getCatalogItem().sortTags();
			// refreshCorrelation(getCurrentTag().getCorrelation(1),getCurrentTag().getCorrelation(2),getLocale());
			// setCurrentCommand(0);
			// setNavigation(true);
		}
		ClassificationAccessPoint tag998 = (ClassificationAccessPoint) newTag(
				1, (short) 6);
		CorrelationValues v = new CorrelationValues((short) 29, (short) 80,
				(short) -1);
		tag998.setCorrelationValues(v);
		refreshCorrelation(tag998.getCorrelation(1), tag998.getCorrelation(2),
				getLocale());
		List subfieldList = new ArrayList();
		List subfields = new ArrayList();
		subfieldList.add("a");
		subfields.add(publisherCode);
		CLSTN descriptor = (CLSTN) tag998.getDescriptor();
		descriptor.setTypeCode((short) 29);
		StringText text = new StringText(subfieldList, subfields);
		MarcCommandLibrary.setNewStringText(tag998, text, View
				.makeSingleViewString(getCatalogItem().getUserView()));

		validateCurrentTag();
		sortTags(super.getLocale());
		resetCommands();
		// setNavigation(false);
	}

	public String verifyTag260() throws MarcCorrelationException,
			DataAccessException, NoResultsFoundException, PublisherException {
		String publisherTag260 = new String("");
		PublisherManager manager = getPublisher();
		if (manager != null) {
			int hdgNbr = ((PUBL_TAG) manager.getPublisherTagUnits().get(0))
					.getDescriptor().getHeadingNumber();
			List hdgList = new DAOPublisher().loadHdg(hdgNbr + "");
			if (hdgList.size() > 0) {
				publisherTag260 = ((PublCdeHdg) hdgList.get(0))
						.getPublisherCode();
			} else {
				throw new NoResultsFoundException();
			}
		} else {
			throw new PublisherException();
		}
		return publisherTag260;
	}

	public PublisherManager getPublisher() throws MarcCorrelationException,
			DataAccessException {
		PublisherManager manager = null;
		DigitalDoiBean doiBean = new DigitalDoiBean();
		List listTag260 = doiBean.searchTag260(this);
		if (listTag260.size() != 0) {
			manager = doiBean.chooseTag260(listTag260);
		}
		return manager;
	}

	// --> 20100909 fine

	// --> 20110201 inizio:

	public List get082Tags() throws DataAccessException {
		List tags = getCatalogItem().getTags();
		List tag082List = new ArrayList();
		Iterator it = tags.iterator();
		ClassificationAccessPoint tag082 = null;
		while (it.hasNext()) {
			Tag tag = (Tag) it.next();
			if ("082".equals(tag.getMarcEncoding().getMarcTag())) {
				tag082 = (ClassificationAccessPoint) tag;
				tag082List.add(tag082);
			}
		}
		return tag082List;
	}

	public List get982Tags() throws DataAccessException {
		List tags = getCatalogItem().getTags();
		List tag982List = new ArrayList();
		Iterator it = tags.iterator();
		ClassificationAccessPoint tag982 = null;
		while (it.hasNext()) {
			Tag tag = (Tag) it.next();
			if ("982".equals(tag.getMarcEncoding().getMarcTag())) {
				tag982 = (ClassificationAccessPoint) tag;
				tag982List.add(tag982);
			}
		}
		return tag982List;
	}

	public ClassificationAccessPoint get982TagByDewey(String deweyCode)
			throws DataAccessException {
		// ----> Prendo solo i tag 982 del record
		List tags = get982Tags();
		Iterator it = tags.iterator();
		ClassificationAccessPoint tag982 = null;
		ClassificationAccessPoint tag982return = null;
		while (it.hasNext()) {
			Tag tag = (Tag) it.next();
			tag982 = (ClassificationAccessPoint) tag;
			if (tag982.getStringText().getSubfieldsWithCodes("a")
					.getDisplayText().trim().length() > 0
					&& deweyCode
							.equals(tag982.getStringText()
									.getSubfieldsWithCodes("a")
									.getDisplayText().trim())) {
				tag982return = tag982;
				break;
			}
		}
		return tag982return;
	}

	public boolean exist082WithDewey(List tags082, String deweyCode)
			throws DataAccessException {
		boolean find082 = false;
		Iterator it = tags082.iterator();
		ClassificationAccessPoint tag082 = null;
		while (it.hasNext()) {
			Tag tag = (Tag) it.next();
			tag082 = (ClassificationAccessPoint) tag;
			if (tag082.getStringText().getSubfieldsWithCodes("a")
					.getDisplayText().trim().length() > 0
					&& deweyCode
							.equals(tag082.getStringText()
									.getSubfieldsWithCodes("a")
									.getDisplayText().trim())) {
				find082 = true;
				break;
			}
		}
		return find082;
	}

	// --> 20110201 fine

	/*
	 * This method skips some tags because they have a number of validity
	 * checks. This tags must be a deleted individually.
	 * 
	 * @see EditBean#isSkipDeleteTags()
	 */
	public boolean getSkipDeleteTags(String marcTag)
			throws MarcCorrelationException, DataAccessException {
		// disability deletion tag 260 if the tag is 998
		Tag aTag = this.getCatalogItem().findFirstTagByNumber("998");
		if (marcTag.equals("097") || (marcTag.equals("260") && aTag != null)
				|| marcTag.equals("856"))
			return true;
		else
			return false;
	}

	@Override
	public List getBibliographicRelationshipTags() throws DataAccessException {
		List tags = getCatalogItem().getTags();
		List relationshipTagsList = new ArrayList();
		Iterator it = tags.iterator();
		while (it.hasNext()) {
			Tag tag = (Tag) it.next();
			if (tag instanceof BibliographicRelationshipTag) {
				relationshipTagsList.add(tag);
			}
		}
		return relationshipTagsList;
	}

	public Tag getCurrentTag007ForCodes() {

		Tag tag = getCatalogItem().findFirstTagByNumber("007");
		if (tag != null) {
			setTagIndex(getCatalogItem().getTags().indexOf(tag));
			setPhysical(PHYSICAL_MATERIAL);
			return getCatalogItem().getTag(getTagIndex());
		}
		setPhysical(NOT_PHYSICAL_MATERIAL);
		return new NullPhysicalDescriptionObject();
	}

	public Tag getCurrentTag006ForCodes() {

		Tag tag = getCatalogItem().findFirstTagByNumber("006");
		if (tag != null) {
			setTagIndex(getCatalogItem().getTags().indexOf(tag));
			setPhysical(PHYSICAL_MATERIAL);
			return getCatalogItem().getTag(getTagIndex());
		}
		setPhysical(NOT_PHYSICAL_MATERIAL);
		return new NullPhysicalDescriptionObject();
	}

	public String getNewSubfieldCode() {
		return newSubfieldCode;
	}

	public void setNewSubfieldCode(String newSubfieldCode) {
		this.newSubfieldCode = newSubfieldCode;
	}

	public void prepareItemForEditing(int recordView, Object[] key)
			throws DataAccessException, AuthorisationException,
			ValidationException, RecordInUseException, DuplicateTagException {
		setCatalogItem(findOrCreateMyView(recordView, ((Integer) key[0])
				.intValue(), ((Integer) key[1]).intValue()));
		saveRecord();
		loadItem(key);
		finalizePreparationForEditing(); // pm aut
	}

	public void prepareItemForVisualizeCodes(int recordView, Object[] key)
			throws DataAccessException, AuthorisationException,
			ValidationException, RecordInUseException, DuplicateTagException {
		setCatalogItem(findOrCreateMyView(recordView, ((Integer) key[0])
				.intValue(), ((Integer) key[1]).intValue()));
		loadItemWithoutLock(key);
		finalizePreparationForEditing(); // pm aut
	}

	/**
	 * pm 2011 Determines whether the given bib record exists in the cataloguing
	 * view. If it does not, then the record in the searching view is duplicated
	 * to a new record in the cataloguing view
	 * @param amicusNumber
	 * @param cataloguingView
	 * @throws DataAccessException
	 * @throws ValidationException
	 */
	public CatalogItem findOrCreateMyView(int recordView, int amicusNumber,
			int cataloguingView) throws DataAccessException {
		return theCatalog.findOrCreateMyView(recordView, amicusNumber,
				cataloguingView);
	}

	public void finalizePreparationForEditing() {
		// inizio
		defaultForTag097();
		impostaItemBibliographicLevelCode();
	}

	@Override
	public void processPickedHeading(Descriptor d, String selectedIndex)
			throws MarcCorrelationException, DuplicateDescriptorException,
			DataAccessException, AuthorisationException, ValidationException {
		String marcTag = getCurrentTag().getMarcEncoding().getMarcTag();
		if (isSeries(selectedIndex, marcTag)) {
			((TitleAccessPoint)getCurrentTag()).setSeriesIssnHeadingNumber(new Integer(d.getKey().getHeadingNumber()));
    		changeText(((TitleAccessPoint)getCurrentTag()).getStringText());
    	
		} else {
			if (getCatalogItem() != null && getCatalogItem().isDecriptorAlreadyPresent(d,
							getCurrentTag()))
				throw new DuplicateDescriptorException();
			else {

				// get the see reference for the heading
				// if a see reference is found, d will contain the
				// Descriptor of the reference
				CrossReferenceBean crm = new CrossReferenceBean();
				d = crm.getSeeReference(d, getCataloguingView());
				// and save it in the CatalogItem
				if (getCatalogItem() != null)
					updateDescriptorFromBrowse(d);
				Tag tag = getCurrentTag();
				if (tag != null) {
					((Browsable) tag).setDescriptor(d);
					// update the correlation settings
					refreshCorrelation(tag.getCorrelation(1), tag
							.getCorrelation(2), getLocale());
					setSkipInFiling((short) d.getSkipInFiling());
					if (isAddCatalogItem())
						validateCurrentTagHeading();
					else
						validateCurrentTag();
					changeCodeValidationISBN(tag);
					
				}
			}
		}
	}

	private boolean isSeries(String selectedIndex, String marcTag) {
		return getCurrentTag() != null	&& (marcTag.equals("440")||marcTag.equals("830"))
				&& selectedIndex.equals("SN       ");
	}

	/**
	 * pm aut Moved from ResultSummaryAction by pm Not sure what its doing but
	 * it is bibliographic specific and so belongs here
	 */
	public void defaultForTag097() {
		setDuplicaDisable(true);

		ControlNumberAccessPoint tag097 = (ControlNumberAccessPoint) getCatalogItem()
				.findFirstTagByNumber("097");
		if (tag097 != null) {
			setPresentTag097(true);
			if ((getCheckContinuaz() != null)
					&& (getCheckContinuaz().equalsIgnoreCase("S"))) {
				if ((getOptDigitalLevel().equalsIgnoreCase("005"))
						|| (getOptDigitalLevel().equalsIgnoreCase("007"))
						||
						// 20091013 inizio
						(getOptDigitalLevel().equalsIgnoreCase("004"))
						||
						// 20091013 fine
						(getOptDigitalLevel().equalsIgnoreCase("008"))
						|| (getOptDigitalLevel().equalsIgnoreCase("009"))) {
					setDuplicaDisable(false);
				}
			}
		} else {
			setPresentTag097(false);
		}
	}

	// pm aut moved from ResultSummaryAction and NewCatalogItemAction
	public void impostaItemBibliographicLevelCode() {
		BibliographicLeader leader = (BibliographicLeader) getCatalogItem()
				.findFirstTagByNumber("000");
		String encoding = leader.getItemBibliographicLevelCode() + "";
		setItemBibliographicLevelCode(encoding.trim());
	}

	@Override
	// Nat:enh. 2688 (tolto suff. medra su doi $w)
	// Nat:enh. 2802 (url per fascicoli con an)
	// Nat:enh. 2803 (url per art. e pdm con doi)
	public void createTag856_4_2() throws PermalinkException,
			DuplicateTagException {
		String isbnIssn = "";

		if ("S".equalsIgnoreCase(getCheckDigital())) {
			if ("m".equalsIgnoreCase(getItemBibliographicLevelCode())
					|| "s".equalsIgnoreCase(getItemBibliographicLevelCode())) {
				isbnIssn = getIsbnOrIssnCode(getItemBibliographicLevelCode());
				if (isbnIssn.trim().length() > 0) {
					createTagUrlByParam(isbnIssn);
				}
			} else if ("a".equalsIgnoreCase(getItemBibliographicLevelCode())
					|| "b".equalsIgnoreCase(getItemBibliographicLevelCode())) {
				try {
					if ((getOptDigitalLevel().equalsIgnoreCase("013"))
							|| (getOptDigitalLevel().equalsIgnoreCase("015"))) {
						String doi = getDoiCode();
						if (!"".equals(doi.trim())) {
							createTagUrlByParam(doi);
						}
					} else if (getOptDigitalLevel().equalsIgnoreCase("012")) {
						String amicuNumber = getCatalogItem().getAmicusNumber()
								.toString();
						createTagUrlByParam("an/" + amicuNumber);
					}

				} catch (DataAccessException e) {
					throw new PermalinkException();
				} catch (Exception e) {
					e.printStackTrace();
					throw new PermalinkException();
				}
			}
		}
	}

	private void createTagUrlByParam(String paramUrl) throws PermalinkException {
		final String startDoiMonog = "http://digital.casalini.it/";

		try {
			BibliographicNoteTag bibliographicNoteTag = null;
			bibliographicNoteTag = get856_4_2_Tag();
			if (!(bibliographicNoteTag == null)) {
				int i = getCatalogItem().getTags()
						.indexOf(bibliographicNoteTag);
				setTagIndex(i);
				deleteTag();
			}
			BibliographicNoteTag tag856;
			tag856 = (BibliographicNoteTag) newTag(1, (short) 7);
			CorrelationValues v = new CorrelationValues((short) 309,
					(short) -1, (short) -1);
			tag856.setCorrelationValues(v);
			refreshCorrelation(tag856.getCorrelation(1), tag856
					.getCorrelation(2), getLocale());
			StringText text = new StringText(new Subfield("u", startDoiMonog
					+ paramUrl));
			tag856.setStringText(text);
		} catch (DataAccessException e) {
			throw new PermalinkException();
		} catch (AuthorisationException e) {
			throw new PermalinkException();
		} catch (ValidationException e) {
			throw new PermalinkException();
		} catch (Exception e) {
			e.printStackTrace();
			throw new PermalinkException();
		}
	}

	@Override
	public BibliographicNoteTag get856_4_2_Tag() throws DataAccessException,
			DuplicateTagException {
		BibliographicNoteTag bibliographicNoteTag = null;
		List tags = getCatalogItem().getTags();
		List tags856 = new ArrayList();
		Iterator it = tags.iterator();
		while (it.hasNext()) {
			Tag tag = (Tag) it.next();
			if (tag instanceof BibliographicNoteTag) {
				if (tag.getMarcEncoding().getMarcTag().equalsIgnoreCase("856"))
					if (tag.getCorrelation(1) == 309
							&& tag.getCorrelation(2) == -1
							&& tag.getCorrelation(3) == -1) {
						tags856.add(tag);
					}
			}
		}
		if (tags856.size() > 1) {
			throw new DuplicateTagException();
		} else if (tags856.size() == 1) {
			bibliographicNoteTag = (BibliographicNoteTag) tags856.get(0);
		}
		return bibliographicNoteTag;
	}

	@Override
	public String getIsbnOrIssnCode(String recordType) {
		String isbnOrIssn = "";
		ControlNumberAccessPoint accessPoint = null;
		if ("s".equalsIgnoreCase(recordType)) {
			accessPoint = (ControlNumberAccessPoint) getCatalogItem()
					.findFirstTagByNumber("022");
			if (accessPoint != null) {
				if (accessPoint.getStringText().getSubfieldsWithCodes("a")
						.getDisplayText().toString().trim().length() > 0) {
					isbnOrIssn = accessPoint.getStringText()
							.getSubfieldsWithCodes("a").getDisplayText();
				}
			}
		} else if ("m".equalsIgnoreCase(recordType)) {
			accessPoint = (ControlNumberAccessPoint) getCatalogItem()
					.findFirstTagByNumber("020");
			if (accessPoint != null) {
				if (accessPoint.getStringText().getSubfieldsWithCodes("a")
						.getDisplayText().toString().trim().length() > 0) {
					isbnOrIssn = (accessPoint.getStringText()
							.getSubfieldsWithCodes("a").getDisplayText());
				}
			}
		}

		if (isbnOrIssn.trim().length() > 0) {
			isbnOrIssn = isbnOrIssn.replaceAll("-", "");
		}

		return isbnOrIssn;
	}

	/**
	 * Checks if has electronic in FormOfItem
	 */
	// Nat: bug 1779
	public boolean isElectronicResourceoOnTag008() {
		MaterialDescription t008 = (MaterialDescription) getCatalogItem()
				.findFirstTagByNumber("008");

		if (t008 != null) {
			return (t008.getFormOfItemCode() == 's');
		}

		return false;
	}

	/**
	 * This method makes a control level the nature of the tag 097 and the cache
	 * table
	 */
	public void checkDigitalLevel() {
		ControlNumberAccessPoint tag097 = (ControlNumberAccessPoint) getCatalogItem()
				.findFirstTagByNumber("097");
		if (tag097 != null) {
			// controllo livello
			StringText text = new StringText(tag097.getDescriptor()
					.getStringText());
			String digitalLevel = text.getSubfieldsWithCodes("b")
					.toDisplayString();
			if (!getOptDigitalLevel().equals(digitalLevel)) {
				setOptDigitalLevel(digitalLevel);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see EditBean#isAbleSubjectsMesh()
	 *      able subjects Mesh with value from S_SYS_GBL_VRBL 0=no subjects
	 *      mesh/ 1=yes subjects mesh bug 3035: mesh
	 */
	@Override
	public boolean isAbleSubjectsMesh() {
		DAOGlobalVariable dgv = new DAOGlobalVariable();
		char value_mesh = 0;
		boolean isSubjectsMesh = false;
		try {
			value_mesh = dgv.getValueByName("subjects_mesh").charAt(0);
		} catch (DataAccessException e) {
			e.printStackTrace();
			return isSubjectsMesh;

		}
		if (value_mesh == '1' && getCurrentTag() != null) {
			// controlla che sia tag 650 con ind. #2
			try {
				if (getCurrentTag().getMarcEncoding().getMarcTag()
						.equals("650")
						&& getCurrentTag().getMarcEncoding()
								.getMarcFirstIndicator() == ' '
						&& getCurrentTag().getMarcEncoding()
								.getMarcSecondIndicator() == '2') {
					isSubjectsMesh = true;
				}
			} catch (MarcCorrelationException e) {
				e.printStackTrace();
			} catch (DataAccessException e) {
				e.printStackTrace();
				return isSubjectsMesh;
			}
		} else {
			isSubjectsMesh = false;
		}
		return isSubjectsMesh;
	}

	public List getRelatorTermCodeForSubfield4List() {
		return CodeListsBean.getRelatorTermType().getCodeList(getLanguageFromTag040());
	}

	@Override
	public Integer getWemiGroupFieldsFromModel(int modelId)
			throws DataAccessException {
		DAOFrbrModel frbrDao = new DAOFrbrModel();
		return frbrDao.getBiblioWemiFirstTypeFromModelById(modelId);
	}

	

	public void updateCasCacheBeforeSaveRecord() throws DataAccessException {
		CasCache casCache = null;
		casCache = getCasaliniBean().loadCasCache(
				getCatalogItem().getAmicusNumber().intValue());
		getCasaliniBean().setCasCache(casCache);
		if (getCasaliniBean().getCasCache() == null) {
			getCasaliniBean().createCasCache();
		}
		getCasaliniBean().getCasCache().setBibItemNumber(
				getCatalogItem().getAmicusNumber().intValue());
		getCasaliniBean().getCasCache().setDigCheck(getCheckDigital());
		getCasaliniBean().getCasCache().setOnlineCheck(getCheckOnline());
		getCasaliniBean().getCasCache().setContinuazCheck(getCheckContinuaz());
		getCasaliniBean().getCasCache().setMdrFgl(getMdrFgl());
		getCasaliniBean().getCasCache().setNtrLevel(getOptDigitalLevel());
		getCasaliniBean().getCasCache().setCheckNCORE(getCheckNCORE());
		getCasaliniBean().getCasCache().setCheckNTOCSB(getCheckNTOCSB());
		if (getCasaliniBean().getCasCache().getLstUser() == null)
			getCasaliniBean().getCasCache().setLstUser(getUserName());

		getCasaliniBean().getCasCache().setFlagIsStock(
				"S".equalsIgnoreCase(getCasaliniBean().getCasCache()
						.getFlagIsStock()) ? "S" : "N");
		getCasaliniBean().getCasCache().setCheckNCORE(
				"S".equalsIgnoreCase(getCasaliniBean().getCasCache()
						.getCheckNCORE()) ? "S" : "N");
		getCasaliniBean().getCasCache().setCheckNTOCSB(
				"S".equalsIgnoreCase(getCasaliniBean().getCasCache()
						.getCheckNTOCSB()) ? "S" : "N");

		// if (isToSetOnReiterationFlag(getCatalogItem()))
		// {
		// getCasaliniBean().getCasCache().setFlagReiteration("S");
		// }
		// else
		// {
		getCasaliniBean().getCasCache().setFlagReiteration(
				"S".equalsIgnoreCase(getCasaliniBean().getCasCache()
						.getFlagReiteration()) ? "S" : "N");
		// }

		getCasaliniBean().getCasCache().setDigCheck(
				"S".equalsIgnoreCase(getCasaliniBean().getCasCache()
						.getDigCheck()) ? "S" : "N");
		getCasaliniBean().getCasCache().setFlagNTI(
				"S".equalsIgnoreCase(getCasaliniBean().getCasCache()
						.getFlagNTI()) ? "S" : "N");
		getCasaliniBean().getCasCache().setContinuazCheck(
				"S".equalsIgnoreCase(getCasaliniBean().getCasCache()
						.getContinuazCheck()) ? "S" : "N");
		getCasaliniBean().getCasCache().setOnlineCheck(
				"S".equalsIgnoreCase(getCasaliniBean().getCasCache()
						.getOnlineCheck()) ? "S" : "N");
		getCasaliniBean().getCasCache().setPrintFourCover(
				"S".equalsIgnoreCase(getCasaliniBean().getCasCache()
						.getPrintFourCover()) ? "S" : "N");
		getCasaliniBean()
				.getCasCache()
				.setLevelCard(
						getCasaliniBean().getCasCache().getLevelCard() == null ? IGlobalConst.DEFAULT_LEVEL_CARD
								: getCasaliniBean().getCasCache()
										.getLevelCard());
		getCasaliniBean().getCasCache().setNtrLevel(
				getCasaliniBean().getCasCache().getNtrLevel() == null ? "001"
						: getCasaliniBean().getCasCache().getNtrLevel());
		getCasaliniBean().getCasCache().setMdrFgl(
				getCasaliniBean().getCasCache().getMdrFgl() == null ? "001"
						: getCasaliniBean().getCasCache().getMdrFgl());
		// getCasaliniBean().getCasCache().setWorkingCode(getCasaliniBean().getCasCache().getWorkingCode()==null?IGlobalConst.DEFAULT_WORKING_CODE:getCasaliniBean().getCasCache().getWorkingCode());
		getCasaliniBean()
				.getCasCache()
				.setStatusDisponibilit(
						getCasaliniBean().getCasCache().getStatusDisponibilit() == null ? new Integer(
								99)
								: getCasaliniBean().getCasCache()
										.getStatusDisponibilit());
	}

	@Override
	public void saveTag856(HttpServletRequest request, String result,
			EditBean bean, String tagSave) throws DataAccessException,
			AuthorisationException, MarcCorrelationException,
			ValidationException, RecordInUseException {

		BibliographicNoteTag tag856Save;
		tag856Save = (BibliographicNoteTag) bean.getCurrentTag();
		if (tagSave.equalsIgnoreCase("856")
				&& tag856Save.getCorrelation(1) == 346
				&& tag856Save.getCorrelation(2) == -1
				&& tag856Save.getCorrelation(3) == -1) {
			if (!result.equalsIgnoreCase("openForm")) {
				bean.setSendDoi(false);
				bean.setModifyDoi(false);
				bean.setInternalDoiPermitted(false);
			}
			bean.saveRecord();
			BibliographicEditBean biblio = (BibliographicEditBean) bean;
			biblio.loadItem(bean.getCatalogItem().getAmicusNumber().intValue(),
					SessionUtils.getCataloguingView(request));
			biblio.sortTags(SessionUtils.getCurrentLocale(request));
		}
	}
	
	/**
	 * @param amicusNumber
	 * @throws RelationshipTagException
	 */
	public void checkRelations(int amicusNumber) throws RelationshipTagException 
	{
		BibliographicRelationshipTag tag = (BibliographicRelationshipTag) getCurrentTag();
		if(tag.getItemNumber()== amicusNumber)throw new RelationshipTagException();
	}
	@Override
	public void refreshCatalogItem() throws MarcCorrelationException,
			DataAccessException, RecordInUseException {
		/* Bug 4791 inizio */
		loadItem(getCatalogItem().getAmicusNumber().intValue(),
				getCataloguingView());
		sortTags(getLocale());
		if (!isCallFromSaveTag()) {
			getCatalog().unlock(getCatalogItem().getAmicusNumber().intValue());
		}
		/* Bug 4791 fine */
	}

	// ----> Anche per gli altri clienti deve scrivere la S_CAS_CACHE e
	// scrivere la tabella CAS_FILES e CAS_DIG_FILES
	@Override
	public void saveRecord() throws DataAccessException, AuthorisationException, MarcCorrelationException, ValidationException 
	{
		getCatalog().getCatalogDao().setCasCache(getCasaliniBean().getCasCache());
		if (getCasaliniBean().isEnabled()) {

			// --------> Tag 097 cancellati
			Tag aTag = null;

			Iterator iter = getCatalogItem().getDeletedTags().iterator();
			while (iter.hasNext()) {
				aTag = (Tag) iter.next();
				if (aTag instanceof ControlNumberAccessPoint) {
					if (aTag.getMarcEncoding().getMarcTag().equalsIgnoreCase(
							"097")) {
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
					((OrderedTag) tag082).setSequenceNumber(new Integer(
							sequenceNumber));
				} catch (NumberFormatException e) {
					((OrderedTag) tag082).setSequenceNumber(null);
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
					((OrderedTag) tag084).setSequenceNumber(new Integer(
							sequenceNumber));
				} catch (NumberFormatException e) {
					((OrderedTag) tag084).setSequenceNumber(null);
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
		try {
			while (it982.hasNext()) {
				Tag tag = (Tag) it982.next();
				tag982 = (ClassificationAccessPoint) tag;
				if (tag982.getStringText().getSubfieldsWithCodes("a")
						.getDisplayText().trim().length() > 0) {
					deweyCode = tag982.getStringText().getSubfieldsWithCodes(
							"a").getDisplayText().trim();
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
		} catch (Exception e) {
		}
		getCatalog().saveCatalogItem(getCatalogItem());

	}

	@Override
	public Correlation getCorrelation(String tagNumber, char indicator1,
			char indicator2, short category) throws DataAccessException {
		return getCatalogItem().getTagImpl().getCorrelation(tagNumber,
				indicator1, indicator2, category);
	}
	public List getGeneralMaterialDesignationList() {
		return CodeListsBean.getGeneralMaterialDesignation().getCodeList(getLocale());
	}

	@Override
	public boolean checkTags() throws DataAccessException,
			MarcCorrelationException, ValidationException {
		// TODO Auto-generated method stub
		return false;
	}
}