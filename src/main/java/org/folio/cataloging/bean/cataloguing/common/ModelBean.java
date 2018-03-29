package org.folio.cataloging.bean.cataloguing.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.bean.LibrisuiteBean;
import org.folio.cataloging.bean.cataloguing.bibliographic.StringTextEditBean;
import org.folio.cataloging.bean.cataloguing.bibliographic.StringTextEditBeanForModelEditing;
import org.folio.cataloging.bean.cataloguing.bibliographic.codelist.CodeListsBean;
import org.folio.cataloging.business.Command;
import org.folio.cataloging.business.authorisation.AuthorisationException;
import org.folio.cataloging.business.cataloguing.bibliographic.*;
import org.folio.cataloging.business.cataloguing.common.Catalog;
import org.folio.cataloging.business.cataloguing.common.CatalogItem;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.controller.SessionUtils;
import org.folio.cataloging.dao.DAOCodeTable;
import org.folio.cataloging.dao.persistence.Model;
import org.folio.cataloging.dao.persistence.T_SINGLE;
import org.folio.cataloging.exception.DuplicateTagException;
import org.folio.cataloging.exception.LibrisuiteException;
import org.folio.cataloging.exception.ModelLabelNotSetException;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.shared.Validation;
import org.folio.cataloging.util.StringText;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class ModelBean extends LibrisuiteBean 
{
	private static final Log logger = LogFactory.getLog(ModelBean.class);
	protected List commandList = new ArrayList();
	protected int currentCommand = 4;
	private Catalog catalog;
	private int currentFieldIndex = 0;
	private List firstCorrelationList;
	private Locale locale = Locale.getDefault();
	private Model model;
	private List secondCorrelationList;
	private StringTextEditBean stringText;
	private List tagCategories;
	private List thirdCorrelationList;
	private int index;

	public void editModel(Model model) 
	{
		setModel(model);
		//setCurrentFieldIndex(model.getTags().size()-1);
		refreshCorrelation(locale);
		setStringText(createStringTextEditBean());
		try {
			setTagCategories(getCatalog().getTagCategories(locale));
		} catch (DataAccessException dataAccessException) {
			throw new RuntimeException();
		}
	}

	public static ModelBean getInstance(HttpServletRequest request) 
	{
		ModelBean modelBean = (ModelBean) ModelBean.getSessionAttribute(request, ModelBean.class);
		if (modelBean == null) {
			modelBean = new ModelBean();
			modelBean.setSessionAttribute(request, modelBean.getClass());
		}
		modelBean.locale = SessionUtils.getCurrentLocale(request);
		modelBean.catalog = SessionUtils.getCatalog(request);
		return modelBean;
	}

	public void newModel() throws NewTagException
	{
		CatalogItem item = getCatalog().newCatalogItemWithoutAmicusNumber();
		getCatalog().addRequiredTagsForModel(item);
		Model model = getCatalog().newModel(item);
		editModel(model);
	}

	public void newModelAuthority() throws NewTagException
	{
		CatalogItem item = getCatalog().newCatalogItemWithoutAmicusNumber();
		getCatalog().addDefaultTag(item);
		Model model = getCatalog().newModel(item);
		editModel(model);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int ind) {
		index = ind;
	}

	private boolean isEditing= false; // !isEditing = isNavigation
	
	public boolean isEditing() {
		return isEditing;
	}

	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}

	@Deprecated
	public void changeCategory(
		short category,
		Locale locale) {
		//model.changeCategoryCode(getCurrentFieldIndex(), category);
		refreshCorrelation(locale);
		setStringText(createStringTextEditBean());
	}

	@Deprecated
	public void changeCorrelationValues(CorrelationValues correlationValues, Locale locale)
	{
		logger.debug("changing correlationValues to " + correlationValues);
		logger.debug("for tag at index " + getCurrentFieldIndex());
		//getModel().changeCorrelationValues(getCurrentFieldIndex(),correlationValues);
		logger.debug("after change correlation: index is " + getCurrentFieldIndex());
		logger.debug("tag is " + getCurrentField());
		refreshCorrelation(locale);
		logger.debug("after refresh correlation: index is " + getCurrentFieldIndex());
		logger.debug("tag is " + getCurrentField());
		setStringText(createStringTextEditBean());
		logger.debug("after setStringText: index is " + getCurrentFieldIndex());
		logger.debug("tag is " + getCurrentField());
	}

	public void changeCorrelationValues(int value1,int value2,int value3,Locale locale) 
	{
		changeCorrelationValues(new CorrelationValues((short) value1,(short) value2,(short) value3),locale);
	}
   @Deprecated
	public void changeHeadingType(short headingType, Locale locale) 
	{
		//model.changeHeadingType(getCurrentFieldIndex(), headingType);
		refreshCorrelation(locale);
		setStringText(createStringTextEditBean());
	}

	@Deprecated
	public void changeText(StringText text)
	{
		logger.debug("changeText");
		//model.changeText(getCurrentFieldIndex(), text);
		setStringText(createStringTextEditBean());
	}

	public StringTextEditBean createStringTextEditBean() 
	{
		StringTextEditBean stringTextEditBean = null;
		Tag field = getCurrentField();
		if (field instanceof VariableField) {
			try {
				stringTextEditBean = new StringTextEditBeanForModelEditing((VariableField) field);
			} catch (MarcCorrelationException e) {
				logger.error(e);
			} catch (DataAccessException e) {
				logger.error(e);
			}
		}
		return stringTextEditBean;
	}
    @Deprecated
	public void deleteField(Locale locale) 
	{
		/*if (getModel().getTags().size() > 1) {
			getModel().deleteTag(getCurrentField());
			if (getCurrentFieldIndex() >= getModel().getTags().size()) {
				setCurrentFieldIndex(getCurrentFieldIndex() - 1);
			}
		}*/
		refreshCorrelation(locale);
		setStringText(createStringTextEditBean());
	}

	public List getAuthorityEncodingLevelList() {
		return CodeListsBean.getAuthorityEncodingLevel().getCodeList(locale);
	}

	public List getAuthorityRecordStatusCodeList() {
		return CodeListsBean.getAuthorityRecordStatus().getCodeList(locale);
	}

	public List getBilingualUsageList() {
		return CodeListsBean.getBilingualUsage().getCodeList(locale);		
	}

	public List getBookBiographyList() {
		return CodeListsBean.getBookBiography().getCodeList(locale);
	}

	public List getBookFestschriftList() {
		return CodeListsBean.getBookFestschrift().getCodeList(locale);
	}

	public List getBookIllustrationList() {
		return CodeListsBean.getBookIllustration().getCodeList(locale);
	}

	public List getBookIndexAvailabilityList() {
		return CodeListsBean.getBookIndexAvailability().getCodeList(locale);
	}

	public List getBookLiteraryFormList() {
		return CodeListsBean.getBookLiteraryForm().getCodeList(locale);
	}

	public List getBookMaterialTypeCodeList() {
		return CodeListsBean.getBookMaterialType().getCodeList(locale);
	}

	public List getCartographicFormatCodeList() {
		return CodeListsBean.getCartographicFormat().getCodeList(locale);
	}

	public List getCartographicIndexAvailabilityCodeList() {
		return CodeListsBean.getCartographicIndexAvailability().getCodeList(
			locale);
	}

	public List getCartographicMaterialList() {
		return CodeListsBean.getCartographicMaterial().getCodeList(locale);
	}

	public List getCartographicProjectionCodeList() {
		return CodeListsBean.getCartographicProjection().getCodeList(locale);
	}

	public List getCartographicReliefCodeList() {
		return CodeListsBean.getCartographicRelief().getCodeList(locale);
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public List getCataloguingRulesList() {
		return CodeListsBean.getCataloguingRules().getCodeList(locale);		
	}

	public List getCataloguingSourceCodeList() {
		return CodeListsBean.getCataloguingSourceCode().getCodeList(locale);		
	}

	public List getCataloguingSourceList() {
		return CodeListsBean.getCataloguingSource().getCodeList(locale);
	}

	public List getCfAntecedentSourceCodeList() {
		return CodeListsBean.getComputerFileAntecedentSource().getCodeList(
			locale);
	}

	public List getCfColourCodeList() {
		return CodeListsBean.getComputerFileColour().getCodeList(locale);
	}

	public List getCfDimensionsCodeList() {
		return CodeListsBean.getComputerFileDimensions().getCodeList(locale);
	}

	public List getCfFileFormatsCodeList() {
		return CodeListsBean.getComputerFileFormats().getCodeList(locale);
	}

	public List getCfLevelOfCompressionCodeList() {
		return CodeListsBean.getComputerFileLevelOfCompression().getCodeList(
			locale);
	}

	public List getCfQualityAssuranceTargetCodeList() 
	{
		return CodeListsBean.getComputerFileQualityAssuranceTarget().getCodeList(locale);
	}

	public List getCfReformattingQualityCodeList() 
	{
		return CodeListsBean.getComputerFileReformattingQuality().getCodeList(locale);
	}

	public List getCfSpecificMaterialDesignationCodeList() 
	{
		return CodeListsBean.getComputerFileSpecificMaterialDesignation().getCodeList(locale);
	}

	public List getCharacterCodingSchemeCodeList() {
		return CodeListsBean.getCharacterCodingScheme().getCodeList(locale);
	}

	public List getComputerFileTypeCodeList() {
		return CodeListsBean.getComputerFileType().getCodeList(locale);
	}

	public List getComputerTargetAudienceCodeList() {
		return CodeListsBean.getComputerTargetAudience().getCodeList(locale);
	}

	public List getConferencePublicationList() {
		return CodeListsBean.getConferencePublication().getCodeList(locale);
	}

	public Set getControlNumberValidationOptions() {
		return null;
	}

	public List getControlTypeCodeList() {
		return CodeListsBean.getControlType().getCodeList(locale);
	}

	public List getCountryList() {
		return CodeListsBean.getMarcCountry().getCodeList(locale);
	}
	@Deprecated
	public Tag getCurrentField() {
		//return (Tag) model.getTags().get(getCurrentFieldIndex());
		return null;
	}

	public int getCurrentFieldIndex() {
		return currentFieldIndex;
	}

	public List getDateTypeList() {
		return CodeListsBean.getItemDateType().getCodeList(locale);
	}

	public List getDescriptiveCataloguingCodeList() {
		return CodeListsBean.getDescriptiveCataloguing().getCodeList(locale);
	}

	public List getEncodingLevelCodeList() {
		return CodeListsBean.getEncodingLevel().getCodeList(locale);
	}

	public List getFirstCorrelationList() {
		return firstCorrelationList;
	}

	public List getFormOfItemList() {
		return CodeListsBean.getFormOfItem().getCodeList(locale);
	}

	public List getGlbColourCodeList() {
		return CodeListsBean.getGlobeColour().getCodeList(locale);
	}

	public List getGlbSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getGlobeSpecificMaterialDesignation().getCodeList(
			locale);
	}

	public List getGlbTypeOfReproductionCodeList() {
		return CodeListsBean.getGlobeReproductionType().getCodeList(locale);
	}

	public List getGovernmentAgencyList() {
		return CodeListsBean.getGovernmentAgency().getCodeList(locale);		
	}

	public List getGovernmentPublicationList() {
		return CodeListsBean.getGovernmentPublication().getCodeList(locale);
	}

	public List getHeadingStatusList() {
		return CodeListsBean.getHeadingStatus().getCodeList(locale);		
	}

	public List getHeadingTypeList() throws DataAccessException {
		return getCatalog().getValidHeadingTypeList(getCurrentField(), locale);
	}

	public List getIncludesSoundCodeList() {
		return CodeListsBean.getIncludesSound().getCodeList(locale);
	}

	public List getItemBibliographicLevelCodeList() {
		return CodeListsBean.getItemBibliographicLevel().getCodeList(locale);
	}

	public List getItemRecordTypeCodeList() {
		return CodeListsBean.getItemRecordType().getCodeList(locale);
	}

	public List getKitSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getKitSpecificMaterialDesignation().getCodeList(
			locale);
	}

	public List getLanguageList() {
		return CodeListsBean.getLanguage().getCodeList(locale);
	}

	public List getLinkedRecordCodeList() {
		return CodeListsBean.getLinkedRecord().getCodeList(locale);
	}

	public List getMainAddedEntryIndicatorList() {
		return CodeListsBean.getMainAddedEntryIndicator().getCodeList(locale);		
	}

	public List getMapColourCodeList() {
		return CodeListsBean.getMapColour().getCodeList(locale);
	}

	public List getMapMaterialTypeCodeList() {
		return CodeListsBean.getMapMaterialType().getCodeList(locale);
	}

	public List getMapPhysicalMediumCodeList() {
		return CodeListsBean.getMapPhysicalMedium().getCodeList(locale);
	}

	public List getMapPolarityCodeList() {
		return CodeListsBean.getMapPolarity().getCodeList(locale);
	}

	public List getMapProductionDetailsCodeList() {
		return CodeListsBean.getMapProductionDetails().getCodeList(locale);
	}

	public List getMapSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getMapSpecificMaterialDesignation().getCodeList(
			locale);
	}

	public List getMapTypeOfReproductionCodeList() {
		return CodeListsBean.getMapReproductionType().getCodeList(locale);
	}

	public String getMarcTypeCode() {
		return getCatalog().getMarcTypeCode();
	}

	public List getMicBaseOfFilmCodeList() {
		return CodeListsBean.getMicroformBaseOfFilm().getCodeList(locale);
	}

	public List getMicColourCodeList() {
		return CodeListsBean.getMicroformColour().getCodeList(locale);
	}

	public List getMicDimensionsCodeList() {
		return CodeListsBean.getMicroformDimensions().getCodeList(locale);
	}

	public List getMicEmulsionOnFilmCodeList() {
		return CodeListsBean.getMicroformEmulsionOnFilm().getCodeList(locale);
	}

	public List getMicGenerationCodeList() {
		return CodeListsBean.getMicroformGeneration().getCodeList(locale);
	}

	public List getMicPolarityCodeList() {
		return CodeListsBean.getMicroformPolarity().getCodeList(locale);
	}

	public List getMicReductionRatioRangeCodeList() {
		return CodeListsBean.getMicroformReductionRatioRange().getCodeList(
			locale);
	}

	public List getMicSpecificMaterialDesignationCodeList() 
	{
		return CodeListsBean.getMicroformSpecificMaterialDesignation().getCodeList(locale);
	}

	public Model getModel() {
		return model;
	}

	public List getModifiedRecordList() {
		return CodeListsBean.getModifiedRecord().getCodeList(locale);
	}

	public List getMpBaseOfFilmCodeList() {
		return CodeListsBean.getMotionPictureBaseOfFilm().getCodeList(locale);
	}

	public List getMpColourCodeList() {
		return CodeListsBean.getMotionPictureColour().getCodeList(locale);
	}

	public List getMpCompletenessCodeList() {
		return CodeListsBean.getMotionPictureCompleteness().getCodeList(locale);
	}

	public List getMpConfigurationCodeList() {
		return CodeListsBean.getMotionPictureConfiguration().getCodeList(
			locale);
	}

	public List getMpDeteriorationStageCodeList() {
		return CodeListsBean.getMotionPictureDeteriorationStage().getCodeList(
			locale);
	}

	public List getMpDimensionsCodeList() {
		return CodeListsBean.getMotionPictureDimensions().getCodeList(locale);
	}

	public List getMpGenerationCodeList() {
		return CodeListsBean.getMotionPictureGeneration().getCodeList(locale);
	}

	public List getMpKindOfColourStockCodeList() {
		return CodeListsBean.getMotionPictureKindOfColourStock().getCodeList(locale);
	}

	public List getMpMediumForSoundCodeList() {
		return CodeListsBean.getMotionPictureMediumForSound().getCodeList(locale);
	}

	public List getMpPolarityCodeList() {
		return CodeListsBean.getMotionPicturePolarity().getCodeList(locale);
	}

	public List getMpPresentationFormatCodeList() {
		return CodeListsBean.getMotionPicturePresentationFormat().getCodeList(locale);
	}

	public List getMpProductionElementsCodeList() {
		return CodeListsBean.getMotionPictureProductionElements().getCodeList(locale);
	}

	public List getMpRefinedCategoriesOfColourCodeList() 
	{
		return CodeListsBean.getMotionPictureRefinedCategoriesOfColour().getCodeList(locale);
	}

	public List getMpSpecificMaterialDesignationCodeList() 
	{
		return CodeListsBean.getMotionPictureSpecificMaterialDesignation().getCodeList(locale);
	}

	public List getMusicFormatCodeList() {
		return CodeListsBean.getMusicFormat().getCodeList(locale);
	}

	public List getMusicFormOfCompositionCodeList() {
		return CodeListsBean.getMusicFormOfComposition().getCodeList(locale);
	}

	public List getMusicLiteraryTextCodeList() {
		return CodeListsBean.getMusicLiteraryText().getCodeList(locale);
	}

	public List getMusicMaterialTypeCodeList() {
		return CodeListsBean.getMusicMaterialType().getCodeList(locale);
	}

	public List getMusicTextualMaterialCodeList() {
		return CodeListsBean.getMusicTextualMaterial().getCodeList(locale);
	}

	public List getNatureOfContentList() {
		return CodeListsBean.getNatureOfContent().getCodeList(locale);
	}

	public List getNmSpecificMaterialDesignationCodeList() 
	{
		return CodeListsBean.getNotatedMusicSpecificMaterialDesignation().getCodeList(locale);
	}

	public List getNonUniqueNameList() {
		return CodeListsBean.getNonUniqueName().getCodeList(locale);		
	}

	public List getNpgColourCodeList() {
		return CodeListsBean.getNonprojectedGraphicColour().getCodeList(locale);
	}

	public List getNpgPrimarySupportMaterialCodeList() 
	{
		return CodeListsBean.getNonprojectedGraphicPrimarySupportMaterial().getCodeList(locale);
	}

	public List getNpgSecondarySupportMaterialCodeList() 
	{
		return CodeListsBean.getNonprojectedGraphicSecondarySupportMaterial().getCodeList(locale);
	}

	public List getNpgSpecificMaterialDesignationCodeList() 
	{
		return CodeListsBean.getNonprojectedGraphicSpecificMaterialDesignation().getCodeList(locale);
	}

	public List getPgBaseOfEmulsionCodeList() 
	{
		return CodeListsBean.getProjectedGraphicBaseOfEmulsion().getCodeList(locale);
	}

	public List getPgColourCodeList() {
		return CodeListsBean.getProjectedGraphicColour().getCodeList(locale);
	}

	public List getPgMediumForSoundCodeList() {
		return CodeListsBean.getProjectedGraphicMediumForSound().getCodeList(locale);
	}

	public List getPgSecondarySupportMaterialCodeList() 
	{
		return CodeListsBean.getProjectedGraphicSecondarySupportMaterial().getCodeList(locale);
	}

	public List getPgSpecificMaterialDesignationCodeList() 
	{
		return CodeListsBean.getProjectedGraphicSpecificMaterialDesignation().getCodeList(locale);
	}

	public List getRecordModificationList() {
		return CodeListsBean.getRecordModification().getCodeList(locale);		
	}

	public List getRecordRevisionList() {
		return CodeListsBean.getRecordRevision().getCodeList(locale);		
	}

	public List getRecordStatusCodeList() {
		return CodeListsBean.getRecordStatus().getCodeList(locale);
	}

	public List getRecordTypeList() {
		return CodeListsBean.getRecordType().getCodeList(locale);		
	}

	public List getReferenceStatusList() {
		return CodeListsBean.getReferenceStatus().getCodeList(locale);		
	}
	
	public List getRomanizationSchemeList() {
		return CodeListsBean.getRomanizationScheme().getCodeList(locale);		
	}

	public List getRsiAltitudeOfSensorCodeList() 
	{
		return CodeListsBean.getRemoteSensingImageAltitudeOfSensor().getCodeList(locale);
	}

	public List getRsiAttitudeOfSensorCodeList() 
	{
		return CodeListsBean.getRemoteSensingImageAttitudeOfSensor().getCodeList(locale);
	}

	public List getRsiCloudCoverCodeList() {
		return CodeListsBean.getRemoteSensingImageCloudCover().getCodeList(locale);
	}

	public List getRsiDataTypeCodeList() {
		return CodeListsBean.getRemoteSensingImageDataType().getCodeList(locale);
	}

	public List getRsiPlatformConstructionTypeCodeList() {
		return CodeListsBean.getRemoteSensingImagePlatformConstructionType().getCodeList(locale);
	}

	public List getRsiPlatformUseCodeList() {
		return CodeListsBean.getRemoteSensingImagePlatformUse().getCodeList(locale);
	}

	public List getRsiSensorTypeCodeList() {
		return CodeListsBean.getRemoteSensingImageSensorType().getCodeList(locale);
	}

	public List getRsiSpecificMaterialDesignationCodeList() 
	{
		return CodeListsBean.getRemoteSensingImageSpecificMaterialDesignation().getCodeList(locale);
	}

	public List getSecondCorrelationList() {
		return secondCorrelationList;
	}

	public List getSerialFormOriginalItemCodeList() {
		return CodeListsBean.getSerialFormOriginalItem().getCodeList(locale);
	}

	public List getSerialFrequencyCodeList() {
		return CodeListsBean.getSerialFrequency().getCodeList(locale);
	}

	public List getSerialOriginalAlphabetOfTitleCodeList() {
		return CodeListsBean.getSerialOriginalAlphabetOfTitle().getCodeList(locale);
	}

	public List getSerialRegularityCodeList() {
		return CodeListsBean.getSerialRegularity().getCodeList(locale);
	}

	public List getSerialSuccessiveLatestCodeList() {
		return CodeListsBean.getSerialSuccessiveLatest().getCodeList(locale);
	}

	public List getSerialTypeCodeList() {
		return CodeListsBean.getSerialType().getCodeList(locale);
	}

	public List getSeriesEntryIndicatorList() {
		return CodeListsBean.getSeriesEntryIndicator().getCodeList(locale);		
	}

	public List getSeriesNumberingList() {
		return CodeListsBean.getSeriesNumbering().getCodeList(locale);		
	}

	public List getSeriesTypeList() {
		return CodeListsBean.getSeriesType().getCodeList(locale);		
	}

	public List getSndConfigurationCodeList() {
		return CodeListsBean.getSoundConfiguration().getCodeList(locale);
	}

	public List getSndCuttingTypeCodeList() {
		return CodeListsBean.getSoundCuttingType().getCodeList(locale);
	}

	public List getSndDimensionsCodeList() {
		return CodeListsBean.getSoundDimensions().getCodeList(locale);
	}

	public List getSndDiscTypeCodeList() {
		return CodeListsBean.getSoundDiscType().getCodeList(locale);
	}

	public List getSndGrooveWidthCodeList() {
		return CodeListsBean.getSoundGrooveWidth().getCodeList(locale);
	}

	public List getSndMaterialTypeCodeList() {
		return CodeListsBean.getSoundMaterialType().getCodeList(locale);
	}

	public List getSndSpecialPlaybackCharacteristicsCodeList() 
	{
		return CodeListsBean.getSoundSpecialPlaybackCharacteristics().getCodeList(locale);
	}
	
	public List getSndSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getSoundSpecificMaterialDesignation().getCodeList(locale);
	}

	public List getSndSpeedCodeList() {
		return CodeListsBean.getSoundSpeed().getCodeList(locale);
	}

	public List getSndStorageTechniqueCodeList() {
		return CodeListsBean.getSoundStorageTechnique().getCodeList(locale);
	}

	public List getSndTapeConfigurationCodeList() {
		return CodeListsBean.getSoundTapeConfiguration().getCodeList(locale);
	}

	public List getSndTapeWidthCodeList() {
		return CodeListsBean.getSoundTapeWidth().getCodeList(locale);
	}

	public StringTextEditBean getStringText() {
		return stringText;
	}

	public List getSubDivisionTypeList() {
		return CodeListsBean.getSubDivisionType().getCodeList(locale);		
	}

	public List getSubjectDescriptorList() {
		return CodeListsBean.getSubjectDescriptor().getCodeList(locale);
	}

	public List getSubjectEntryIndicatorList() {
		return CodeListsBean.getSubjectEntryIndicator().getCodeList(locale);		
	}

	public List getSubjectSystemList() {
		return CodeListsBean.getSubjectSystem().getCodeList(locale);		
	}

	public List getTagCategories() {
		//this.tagCategories.remove(6);
		return this.tagCategories;
	}

	public List getTargetAudienceList() {
		return CodeListsBean.getTargetAudience().getCodeList(locale);
	}

	public List getThirdCorrelationList() {
		return thirdCorrelationList;
	}

	public List getTmBrailleMusicFormatCodesList() 
	{
		return CodeListsBean.getTactileMaterialBrailleMusicFormat().getCodeList(locale);
	}

	public List getTmClassOfBrailleWritingCodesList() {
		return CodeListsBean.getTactileMaterialClassOfBrailleWriting().getCodeList(locale);
	}

	public List getTmLevelOfContractionCodeList() {
		return CodeListsBean.getTactileMaterialLevelOfContraction().getCodeList(locale);
	}

	public List getTmSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getTactileMaterialSpecificMaterialDesignation().getCodeList(locale);
	}

	public List getTmSpecificPhysicalCharacteristicsCodeList() {
		return CodeListsBean.getTactileMaterialSpecificPhysicalCharacteristics().getCodeList(locale);
	}

	public List getTxtSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getTextSpecificMaterialDesignation().getCodeList(locale);
	}

	public List getUspSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getUnspecifiedSpecificMaterialDesignation().getCodeList(locale);
	}

	public List getVisualMaterialTypeCodeList() {
		return CodeListsBean.getVisualMaterialType().getCodeList(locale);
	}

	public List getVisualTargetAudienceCodeList() {
		return CodeListsBean.getVisualTargetAudience().getCodeList(locale);
	}

	public List getVisualTechniqueCodeList() {
		return CodeListsBean.getVisualTechnique().getCodeList(locale);
	}

	public List getVisualTypeCodeList() {
		return CodeListsBean.getVisualType().getCodeList(locale);
	}

	public List getVrColourCodeList() {
		return CodeListsBean.getVideoRecordingColour().getCodeList(locale);
	}

	public List getVrConfigurationCodeList() {
		return CodeListsBean.getVideoRecordingConfiguration().getCodeList(locale);
	}

	public List getVrDimensionsCodeList() {
		return CodeListsBean.getVideoRecordingDimensions().getCodeList(locale);
	}

	public List getVrFormatCodeList() {
		return CodeListsBean.getVideoRecordingFormat().getCodeList(locale);
	}

	public List getVrMediumForSoundCodeList() {
		return CodeListsBean.getVideoRecordingMediumForSound().getCodeList(locale);
	}

	public List getVrSpecificMaterialDesignationCodeList() {
		return CodeListsBean.getVideoRecordingSpecificMaterialDesignation().getCodeList(locale);
	}

	public List getPrintConstantList() {
		return CodeListsBean.getPrintConstant().getCodeList(locale);
	}

	public List getAuthorityStructureList() {
		return CodeListsBean.getAuthorityStructure().getCodeList(locale);
	}

	public List getEarlierRulesList() {
		return CodeListsBean.getEarlierRules().getCodeList(locale);
	}

	public List getNoteGenerationList() {
		return CodeListsBean.getNoteGeneration().getCodeList(locale);
	}

	public boolean isAbleToBeAdded() {
		return true;
	}

	public boolean isAbleToBeDeleted() {
		return true;
	}

	public boolean isBrowsable() {
		return false;
	}

	public boolean isPublisherEditable() {
		return getCurrentField().isPublisher();
	}

	public boolean isHasSubfieldW() {
		return getCurrentField().isHasSubfieldW();
	}

	@Deprecated
	public void newField(Locale locale) throws NewTagException
	{
		Tag tag = getCurrentField();
		//model.addTag(getCurrentFieldIndex(), tag);
		setCurrentFieldIndex(getCurrentFieldIndex() + 1);
		refreshCorrelation(locale);
		setStringText(createStringTextEditBean());
	}

	public void refreshCorrelation(Locale locale) 
	{
		int value1 = getCurrentField().getCorrelation(1);
		int value2 = getCurrentField().getCorrelation(2);
		try {
			List firstList = getCurrentField().getFirstCorrelationList();
			setFirstCorrelationList(DAOCodeTable.asOptionList(firstList, locale));

			if (value1 == -1 && firstList != null) {
				value1 = ((T_SINGLE) firstList.get(0)).getCode();
			}
			List secondList = getCurrentField().getSecondCorrelationList(value1);
			setSecondCorrelationList(DAOCodeTable.asOptionList(secondList, locale));

			if (value2 == -1 && secondList != null) {
				value2 = ((T_SINGLE) secondList.get(0)).getCode();
			}
			List thirdList = getCurrentField().getThirdCorrelationList(value1, value2);
			setThirdCorrelationList(DAOCodeTable.asOptionList(thirdList, locale));
		} catch (DataAccessException dataAccessException) {
		}
	}
	@Deprecated
	public void refreshTag() {
		//model.refreshTag(getCurrentFieldIndex(), getCurrentField());
	}

	public void saveModel() throws ModelLabelNotSetException, DataAccessException, DuplicateTagException
	{
		validateModelData();
		model.getDAO().persistByStatus(model);
	}

	public void selectField(int selectedFieldIndex, Locale locale) throws DataAccessException {
		setCurrentFieldIndex(selectedFieldIndex);
		refreshCorrelation(locale);
		setStringText(createStringTextEditBean());
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public void setCurrentFieldIndex(int i) {
		currentFieldIndex = i;
		setStringText(createStringTextEditBean());
	}

	public void setFirstCorrelationList(List list) {
		firstCorrelationList = list;
	}

	public void setModel(Model model) {
		this.model = model;
		//TODO for now always mark model as "changed"
		this.model.markChanged();
	}

	public void setSecondCorrelationList(List list) {
		secondCorrelationList = list;
	}

	public void setStringText(StringTextEditBean bean) {
		stringText = bean;
	}

	private void setTagCategories(List list) {
		tagCategories = list;
	}

	public void setThirdCorrelationList(List list) {
		thirdCorrelationList = list;
	}

	@Deprecated
	public void toggleOptional() {
		//model.toggleOptional(getCurrentFieldIndex());
	}

	/**
	 * validates the Model data
	 * 
	 * @since 1.0
	 */
	public void validateModelData() throws ModelLabelNotSetException, DataAccessException, DuplicateTagException
	{
		if (getModel().getLabel() == null || "".equals(getModel().getLabel())) {
			throw new ModelLabelNotSetException();
		}
		checkRepeatability(this.getCurrentFieldIndex());
	}

	/**
	 * validates the Model field
	 * 
	 * @since 1.0
	 */
	public void validateFieldData() throws DataAccessException, DuplicateTagException
	{
		checkRepeatability(this.getCurrentFieldIndex());
	}
	
	/**
	 * Checks if the specified tag is illegally repeated in the item and
	 * throws an exception if so
	 * 
	 * @since 1.0
	 */
	public void checkRepeatability(int index) throws DataAccessException, MarcCorrelationException, DuplicateTagException 
	{
		Tag t = this.getCurrentField();
		Validation bv = t.getValidation();
		if (!bv.isMarcTagRepeatable()) {
			logger.debug("this.getTagCategories() == "+this.getTagCategories().size());
			//List l = new ArrayList(this.getModel().getTags());
			List l = new ArrayList();
			l.remove(index);
			Collections.sort(l, tagComparator);
			if (Collections.binarySearch(l, t, tagComparator) >= 0) {
				throw new DuplicateTagException(index);
			}
		}
	}
		
	private static final Comparator tagComparator = new Comparator() 
	{
		/* to compare tags based on Marc Tag number */
		public int compare(Object obj1, Object obj2) {
			try {
				return ((Tag) obj1).getMarcEncoding().getMarcTag().compareTo(((Tag) obj2).getMarcEncoding().getMarcTag());
			} catch (LibrisuiteException e) {
				throw new RuntimeException("Error comparing tags");
			}
		}
	};

	public void change008Type(BibliographicLeader ldr) throws AuthorisationException, DataAccessException {
		//TODO is this working?		
		checkPermission("editHeader");
		Command c = new ChangeBib008TypeCommand(this, ldr);
		executeCommand(c);
	}

	public void changeFixedFieldValues(FixedFieldUsingItemEntity ff) throws AuthorisationException, DataAccessException {
		//checkPermission("editHeader");
		Command c = new ChangeFixedFieldCommand(this, ff);
		logger.debug("command "+c.getClass());
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

	@Deprecated
	public String getLongLabel(int tagNum) throws DataAccessException, MarcCorrelationException
	{
		//return loadLongLabel((Tag)model.getTags().get(tagNum));
		return null;
	}

	/**
	 * @param processingTag
	 * @return
	 * @throws DataAccessException
	 */
	private T_SINGLE loadSelectedCodeTable(Tag processingTag) throws DataAccessException 
	{
		int value1 = processingTag.getCorrelation(1);
		List firstList = processingTag.getFirstCorrelationList();
		T_SINGLE ct = DAOCodeTable.getSelectedCodeTable(firstList, locale, value1);
		return ct;
	}
	
	/**
	 * @param processingTag
	 * @return
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 */
	private String loadLongLabel(Tag processingTag) throws DataAccessException {
		if(isLabelCached(processingTag)){
			return getCachedLabel(processingTag);
		}
		T_SINGLE ct = loadSelectedCodeTable(processingTag);
		String marcTag = processingTag.getMarcEncoding().getMarcTag();
		if(ct==null) {
			return marcTag;
		}
		String label =  buildLabel(marcTag, ct.getLongText(),processingTag.getMarcEncoding().getMarcTagCategoryCode());
		cacheLabel(processingTag, label);
		return label;
	}
	
	private String getLabelKey(Tag processingTag){
		return ""+processingTag.getCategory()+","+processingTag.getCorrelationValues().toString();
	}
	
	private void cacheLabel(Tag processingTag, String label) {
		labelCache.put(getLabelKey(processingTag), label);
	}

	private String getCachedLabel(Tag processingTag) {
		return (String)labelCache.get(getLabelKey(processingTag));
	}

	private boolean isLabelCached(Tag processingTag) {
		return labelCache.containsKey(getLabelKey(processingTag));
	}

	/**
	 * Build fixed tags with marc tag number if needed
	 * @param marcNumber
	 * @param textLabel
	 * @return
	 */
	private String buildLabel(String marcNumber, String textLabel,short category){
		String result = textLabel;
		if(!textLabel.startsWith("00")&& category!=7){
			result=marcNumber+"-"+textLabel;
		}
		return result;
	}
	

	private Hashtable labelCache = new Hashtable();		
}