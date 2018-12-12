package org.folio.marccat.business.cataloguing.bibliographic;

import org.folio.marccat.business.cataloguing.common.*;
import org.folio.marccat.business.common.AbstractMapBackedFactory;
import org.folio.marccat.business.common.MapBackedFactory;
import org.folio.marccat.business.common.PropertyBasedFactoryBuilder;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.*;
import org.folio.marccat.dao.persistence.*;
import org.folio.marccat.dao.persistence.Map;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.NewTagException;
import org.folio.marccat.config.GlobalStorage;
import org.folio.marccat.shared.CorrelationValues;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.folio.marccat.util.F.isNotNull;

/**
 * Bibliographic implementation of {@link Catalog} interface.
 *
 * @author paulm
 * @author janick
 * @author cchiama
 */
public class BibliographicCatalog extends Catalog {

  private static final ModelDAO MODEL_DAO = new BibliographicModelDAO();
  private static final BibliographicCatalogDAO CATALOG_DAO = new BibliographicCatalogDAO();

  private static AbstractMapBackedFactory TAG_FACTORY;
  private static AbstractMapBackedFactory FIXED_FIELDS_FACTORY;

  static {
    TAG_FACTORY = new MapBackedFactory();
    FIXED_FIELDS_FACTORY = new MapBackedFactory();
    final PropertyBasedFactoryBuilder builder = new PropertyBasedFactoryBuilder();
    builder.load(
      "/org/folio/marccat/business/cataloguing/bibliographic/tagFactory.properties",
      TAG_FACTORY);
    builder.load(
      "/org/folio/marccat/business/cataloguing/bibliographic/fixedFieldFactory.properties",
      FIXED_FIELDS_FACTORY);
  }

  public static void main(final String args[]) {
    BibliographicCatalog catalog = new BibliographicCatalog();
    Tag tag = (Tag) catalog.getTagFactory().create(1);
  }

  @Override
  public CatalogDAO getCatalogDao() {
    return CATALOG_DAO;
  }

  @Override
  public List getTagCategories(final Locale locale) throws DataAccessException {
    return DAO_CODE_TABLE.getOptionListOrderAlphab(T_BIB_TAG_CAT.class, locale);
  }

  /**
   * Ensures that after creating a new BibItem (usually from a model) that the
   * item has at least the required mandatory tags
   *
   * @since 1.0
   */
  public void addRequiredTags(final CatalogItem item) throws NewTagException {
    final BibliographicLeader leader = createRequiredLeaderTag(item);
    if (!item.getTags().contains(leader)) {
      item.addTag(leader);
    }

    final ControlNumberTag controlnumber = createRequiredControlNumberTag(item);
    if (!item.getTags().contains(controlnumber)) {
      item.addTag(controlnumber);
    }

    final DateOfLastTransactionTag dateTag = createRequiredDateOfLastTransactionTag(item);
    if (!item.getTags().contains(dateTag)) {
      item.addTag(dateTag);
    }

    final MaterialDescription mdTag = createRequiredMaterialDescriptionTag(item);
    if (!item.getTags().contains(mdTag)) {
      item.addTag(mdTag);
    }

    final CataloguingSourceTag source = createRequiredCataloguingSourceTag(item);
    if (!item.getTags().contains(source)) {
      item.addTag(source);
    }

    item.sortTags();
  }

  public DateOfLastTransactionTag createRequiredDateOfLastTransactionTag(CatalogItem item) throws NewTagException {
    final DateOfLastTransactionTag dateTag =
      (DateOfLastTransactionTag) getNewTag(item,
        GlobalStorage.HEADER_CATEGORY,
        new CorrelationValues(
          new BibliographicDateOfLastTransactionTag().getHeaderType(),
          CorrelationValues.UNDEFINED,
          CorrelationValues.UNDEFINED));
    return dateTag;
  }


  public void addRequiredTagsForModel(final CatalogItem item) throws NewTagException {
    BibliographicLeader leader = createRequiredLeaderTag(item);
    if (!item.getTags().contains(leader)) {
      item.addTag(leader);
    }

    ControlNumberTag controlnumber = createRequiredControlNumberTag(item);
    if (!item.getTags().contains(controlnumber)) {
      item.addTag(controlnumber);
    }

    MaterialDescription mdTag = createRequiredMaterialDescriptionTag(item);
    if (!item.getTags().contains(mdTag)) {
      item.addTag(mdTag);

    }

    CataloguingSourceTag source = createRequiredCataloguingSourceTag(item);
    if (!item.getTags().contains(source)) {
      item.addTag(source);
    }
  }

  public CataloguingSourceTag createRequiredCataloguingSourceTag(final CatalogItem item) throws NewTagException {
    CataloguingSourceTag source =
      (CataloguingSourceTag) getNewTag(item,
        GlobalStorage.HEADER_CATEGORY,
        new CorrelationValues(
          new BibliographicCataloguingSourceTag().getHeaderType(),
          CorrelationValues.UNDEFINED,
          CorrelationValues.UNDEFINED));
    return source;
  }

  public MaterialDescription createRequiredMaterialDescriptionTag(CatalogItem item) throws NewTagException {
    MaterialDescription mdTag =
      (MaterialDescription) getNewTag(item,
        GlobalStorage.HEADER_CATEGORY,
        new CorrelationValues(
          GlobalStorage.MATERIAL_DESCRIPTION_HEADER_TYPE,
          CorrelationValues.UNDEFINED,
          CorrelationValues.UNDEFINED));
    return mdTag;
  }

  public ControlNumberTag createRequiredControlNumberTag(CatalogItem item) throws NewTagException {
    ControlNumberTag controlnumber =
      (ControlNumberTag) getNewTag(item,
        GlobalStorage.HEADER_CATEGORY,
        new CorrelationValues(
          new BibliographicControlNumberTag().getHeaderType(),
          CorrelationValues.UNDEFINED,
          CorrelationValues.UNDEFINED));
    return controlnumber;
  }

  public BibliographicLeader createRequiredLeaderTag(CatalogItem item) throws NewTagException {
    BibliographicLeader leader =
      (BibliographicLeader) getNewTag(item,
        GlobalStorage.HEADER_CATEGORY,
        new CorrelationValues(
          new BibliographicLeader().getHeaderType(),
          CorrelationValues.UNDEFINED,
          CorrelationValues.UNDEFINED));
    return leader;
  }
  
  public BibliographicNoteTag createBibliographicNoteTag(final CatalogItem item, final CorrelationValues correlationValues) throws NewTagException, DataAccessException {
	    final BibliographicNoteTag nTag =
	      (BibliographicNoteTag) getNewTag(item,
	        GlobalStorage.BIB_NOTE_CATEGORY,
	        correlationValues);
	    return nTag;
	  }
  
  public SubjectAccessPoint createSubjectAccessPoint(final CatalogItem item, final CorrelationValues correlationValues) throws NewTagException, DataAccessException {
	    final SubjectAccessPoint sap =
	      (SubjectAccessPoint) getNewTag(item,
	        GlobalStorage.SUBJECT_CATEGORY,
	        correlationValues);
	    return sap;
	  }
  
  public ClassificationAccessPoint createClassificationAccessPoint(final CatalogItem item, final CorrelationValues correlationValues) throws NewTagException, DataAccessException {
	    final ClassificationAccessPoint clap =
	      (ClassificationAccessPoint) getNewTag(item,
	        GlobalStorage.CLASSIFICATION_CATEGORY,
	        correlationValues);
	    return clap;
	  }
  
  public ControlNumberAccessPoint createControlNumberAccessPoint(final CatalogItem item, final CorrelationValues correlationValues) throws NewTagException, DataAccessException {
	    final ControlNumberAccessPoint cnap =
	      (ControlNumberAccessPoint) getNewTag(item,
	        GlobalStorage.CONTROL_NUMBER_CATEGORY,
	        correlationValues);
	    return cnap;
	  }

  public NameAccessPoint createNameAccessPointTag(final CatalogItem item, final CorrelationValues correlationValues) throws NewTagException, DataAccessException {
	    final NameAccessPoint nap =
	      (NameAccessPoint) getNewTag(item,
	        GlobalStorage.NAME_CATEGORY,
	        correlationValues);
	    return nap;
	  }
  
  public TitleAccessPoint createTitleAccessPointTag(final CatalogItem item, final CorrelationValues correlationValues) throws NewTagException, DataAccessException {
	    final TitleAccessPoint tap =
	      (TitleAccessPoint) getNewTag(item,
	        GlobalStorage.TITLE_CATEGORY,
	        correlationValues);
	    return tap;
	  }
  
  public PublisherManager createPublisherTag(final CatalogItem item, final CorrelationValues correlationValues) throws NewTagException, DataAccessException {
	    final PublisherManager pap =
	      (PublisherManager) getNewTag(item,
	        GlobalStorage.PUBLISHER_CATEGORY,
	        correlationValues);
	    return pap;
	  }
  
  /**
   * Put leader content into persistent hibernate object.
   *
   * @param leaderValue         -- the string leader value.
   * @param bibliographicLeader -- the persistent hibernate class {@link BibliographicLeader}
   */
  public void toBibliographicLeader(final String leaderValue, final BibliographicLeader bibliographicLeader) {
    bibliographicLeader.setRecordStatusCode(leaderValue.charAt(5));
    bibliographicLeader.setItemRecordTypeCode(leaderValue.charAt(6));
    bibliographicLeader.setItemBibliographicLevelCode(leaderValue.charAt(7));
    bibliographicLeader.setControlTypeCode(leaderValue.charAt(8));
    bibliographicLeader.setCharacterCodingSchemeCode(leaderValue.charAt(9));
    bibliographicLeader.setEncodingLevel(leaderValue.charAt(17));
    bibliographicLeader.setDescriptiveCataloguingCode(leaderValue.charAt(18));
    bibliographicLeader.setLinkedRecordCode(leaderValue.charAt(19));
  }
  
  /**
   * Put material type content into persistent hibernate object.
   *
   * @param ff                  -- the fixed field representing material description.
   * @param materialDescription -- the persistent hibernate class {@link MaterialDescription}.
   */
  public void toMaterialDescription(final org.folio.marccat.resources.domain.FixedField ff,
                                    final MaterialDescription materialDescription) {
    materialDescription.setMaterialTypeCode(ff.getMaterialTypeCode());
    if (materialDescription.isBook()) {
      if (isNotNull(ff.getFormOfItemCode())) materialDescription.setFormOfItemCode(ff.getFormOfItemCode());
      final String bookIllustration = (isNotNull(ff.getBookIllustrationCode1()) ? ff.getBookIllustrationCode1() : "")
        + (isNotNull(ff.getBookIllustrationCode2()) ? ff.getBookIllustrationCode2() : "")
        + (isNotNull(ff.getBookIllustrationCode3()) ? ff.getBookIllustrationCode3() : "")
        + (isNotNull(ff.getBookIllustrationCode4()) ? ff.getBookIllustrationCode4() : "");
      materialDescription.setBookIllustrationCode(bookIllustration);
      if (isNotNull(ff.getTargetAudienceCode()))
        materialDescription.setTargetAudienceCode(ff.getTargetAudienceCode());
      final String natureContentCodes = (isNotNull(ff.getNatureOfContent1()) ? ff.getNatureOfContent1() : "")
        + (isNotNull(ff.getNatureOfContent2()) ? ff.getNatureOfContent2() : "")
        + (isNotNull(ff.getNatureOfContent3()) ? ff.getNatureOfContent3() : "")
        + (isNotNull(ff.getNatureOfContent4()) ? ff.getNatureOfContent4() : "");
      materialDescription.setNatureOfContentsCode(natureContentCodes);
      if (isNotNull(ff.getGovernmentPublicationCode()))
        materialDescription.setGovernmentPublicationCode(ff.getGovernmentPublicationCode());
      if (isNotNull(ff.getConferencePublicationCode()))
        materialDescription.setConferencePublicationCode(ff.getConferencePublicationCode());
      if (isNotNull(ff.getBookFestschrift()))
        materialDescription.setBookFestschrift(ff.getBookFestschrift());
      if (isNotNull(ff.getBookIndexAvailabilityCode()))
        materialDescription.setBookIndexAvailabilityCode(ff.getBookIndexAvailabilityCode());
      if (isNotNull(ff.getBookLiteraryFormTypeCode()))
        materialDescription.setBookLiteraryFormTypeCode(ff.getBookLiteraryFormTypeCode());
      if (isNotNull(ff.getBookBiographyCode()))
        materialDescription.setBookBiographyCode(ff.getBookBiographyCode());
    } else if (materialDescription.isMap()) {
      if (isNotNull(ff.getFormOfItemCode())) materialDescription.setFormOfItemCode(ff.getFormOfItemCode());
      String codes = (isNotNull(ff.getCartographicReliefCode1()) ? ff.getCartographicReliefCode1() : "")
        + (isNotNull(ff.getCartographicReliefCode2()) ? ff.getCartographicReliefCode2() : "")
        + (isNotNull(ff.getCartographicReliefCode3()) ? ff.getCartographicReliefCode3() : "")
        + (isNotNull(ff.getCartographicReliefCode4()) ? ff.getCartographicReliefCode4() : "");
      materialDescription.setCartographicReliefCode(codes);
      if (isNotNull(ff.getGovernmentPublicationCode()))
        materialDescription.setGovernmentPublicationCode(ff.getGovernmentPublicationCode());
      if (isNotNull(ff.getCartographicProjectionCode()))
        materialDescription.setCartographicProjectionCode(ff.getCartographicProjectionCode());
      if (isNotNull(ff.getCartographicMaterial()))
        materialDescription.setCartographicMeridianCode(ff.getCartographicMaterial());
      if (isNotNull(ff.getCartographicMaterial()))
        materialDescription.setCartographicNarrativeTextCode(ff.getCartographicMaterial());
      if (isNotNull(ff.getCartographicIndexAvailabilityCode()))
        materialDescription.setCartographicIndexAvailabilityCode(ff.getCartographicIndexAvailabilityCode());
      codes = (isNotNull(ff.getCartographicFormatCode1()) ? ff.getCartographicFormatCode1() : "") + (isNotNull(ff.getCartographicFormatCode2()) ? ff.getCartographicFormatCode2() : "");
      materialDescription.setCartographicFormatCode(codes);
    } else if (materialDescription.isVisualMaterial()) {
      if (isNotNull(ff.getVisualRunningTime()))
        materialDescription.setVisualRunningTime(ff.getVisualRunningTime());
      if (isNotNull(ff.getTargetAudienceCode()))
        materialDescription.setVisualTargetAudienceCode(ff.getTargetAudienceCode());
      if (isNotNull(ff.getGovernmentPublicationCode()))
        materialDescription.setGovernmentPublicationCode(ff.getGovernmentPublicationCode());
      materialDescription.setVisualAccompanyingMaterialCode(" ");
      if (isNotNull(ff.getFormOfItemCode())) materialDescription.setFormOfItemCode(ff.getFormOfItemCode());
      if (isNotNull(ff.getVisualMaterialTypeCode()))
        materialDescription.setVisualMaterialTypeCode(ff.getVisualMaterialTypeCode());
      if (isNotNull(ff.getVisualTechniqueCode()))
        materialDescription.setVisualTechniqueCode(ff.getVisualTechniqueCode());
    } else if (materialDescription.isComputerFile()) {
      if (isNotNull(ff.getTargetAudienceCode()))
        materialDescription.setComputerTargetAudienceCode(ff.getTargetAudienceCode());
      if (isNotNull(ff.getComputerFileTypeCode()))
        materialDescription.setComputerFileTypeCode(ff.getComputerFileTypeCode());
      if (isNotNull(ff.getGovernmentPublicationCode()))
        materialDescription.setGovernmentPublicationCode(ff.getGovernmentPublicationCode());
      materialDescription.setComputerFileFormCode(ff.getFormOfItemCode());
    } else if (materialDescription.isSerial()) {
      if (isNotNull(ff.getFormOfItemCode())) materialDescription.setFormOfItemCode(ff.getFormOfItemCode());
      if (isNotNull(ff.getSerialFrequencyCode()))
        materialDescription.setSerialFrequencyCode(ff.getSerialFrequencyCode());
      if (isNotNull(ff.getSerialRegularityCode()))
        materialDescription.setSerialRegularityCode(ff.getSerialRegularityCode());
      materialDescription.setSerialISDSCenterCode(" ");
      if (isNotNull(ff.getSerialTypeCode())) materialDescription.setSerialTypeCode(ff.getSerialTypeCode());
      if (isNotNull(ff.getSerialFormOriginalItemCode()))
        materialDescription.setSerialFormOriginalItemCode(ff.getSerialFormOriginalItemCode());
      if (isNotNull(ff.getSerialOriginalAlphabetOfTitleCode()))
        materialDescription.setSerialOriginalAlphabetOfTitleCode(ff.getSerialOriginalAlphabetOfTitleCode());
      if (isNotNull(ff.getSerialEntryConventionCode()))
        materialDescription.setSerialSuccessiveLatestCode(ff.getSerialEntryConventionCode());
      materialDescription.setSerialTitlePageExistenceCode(" ");
      materialDescription.setSerialIndexAvailabilityCode(" ");
    } else if (materialDescription.isMixedMaterial()) {
      if (isNotNull(ff.getFormOfItemCode())) materialDescription.setFormOfItemCode(ff.getFormOfItemCode());
    } else if (materialDescription.isMusic()) {
      if (isNotNull(ff.getFormOfItemCode())) materialDescription.setFormOfItemCode(ff.getFormOfItemCode());
      if (isNotNull(ff.getMusicFormOfCompositionCode()))
        materialDescription.setMusicFormOfCompositionCode(ff.getMusicFormOfCompositionCode());
      if (isNotNull(ff.getMusicFormatCode())) materialDescription.setMusicFormatCode(ff.getMusicFormatCode());
      if (isNotNull(ff.getMusicPartsCode())) materialDescription.setMusicPartsCode(ff.getMusicPartsCode());
      if (isNotNull(ff.getTargetAudienceCode()))
        materialDescription.setTargetAudienceCode(ff.getTargetAudienceCode());
      String codes = (isNotNull(ff.getMusicTextualMaterialCode1()) ? ff.getMusicTextualMaterialCode1() : " ")
        + (isNotNull(ff.getMusicTextualMaterialCode2()) ? ff.getMusicTextualMaterialCode2() : "")
        + (isNotNull(ff.getMusicTextualMaterialCode3()) ? ff.getMusicTextualMaterialCode3() : "")
        + (isNotNull(ff.getMusicTextualMaterialCode4()) ? ff.getMusicTextualMaterialCode4() : "")
        + (isNotNull(ff.getMusicTextualMaterialCode5()) ? ff.getMusicTextualMaterialCode5() : "")
        + (isNotNull(ff.getMusicTextualMaterialCode6()) ? ff.getMusicTextualMaterialCode6() : "");

      materialDescription.setMusicTextualMaterialCode(codes);
      codes = (isNotNull(ff.getMusicLiteraryTextCode1()) ? ff.getMusicLiteraryTextCode1() : "")
        + (isNotNull(ff.getMusicLiteraryTextCode2()) ? ff.getMusicLiteraryTextCode2() : "");
      materialDescription.setMusicLiteraryTextCode(codes);
    }

    if (materialDescription.getMaterialDescription008Indicator().equals("1")) {
      try {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date date = formatter.parse(ff.getDateEnteredOnFile());
        materialDescription.setEnteredOnFileDate(date);
      } catch (ParseException e) {
        //
      }
      if (isNotNull(ff.getDateTypeCode()))
        materialDescription.setItemDateTypeCode(ff.getDateTypeCode().charAt(0));
      if (isNotNull(ff.getDateFirstPublication()))
        materialDescription.setItemDateFirstPublication(ff.getDateFirstPublication());
      if (isNotNull(ff.getDateLastPublication()))
        materialDescription.setItemDateLastPublication(ff.getDateLastPublication());
      if (isNotNull(ff.getPlaceOfPublication()))
        materialDescription.setMarcCountryCode(ff.getPlaceOfPublication());
      if (isNotNull(ff.getLanguageCode()))
        materialDescription.setLanguageCode(ff.getLanguageCode());
      else
        materialDescription.setLanguageCode("   ");
      if (isNotNull(ff.getRecordModifiedCode()))
        materialDescription.setRecordModifiedCode(ff.getRecordModifiedCode().charAt(0));
      if (isNotNull(ff.getRecordCataloguingSourceCode()))
        materialDescription.setRecordCataloguingSourceCode(ff.getRecordCataloguingSourceCode().charAt(0));
    }
  }


  public AbstractMapBackedFactory getFixedFieldFactory() {
    return FIXED_FIELDS_FACTORY;
  }

  public AbstractMapBackedFactory getTagFactory() {
    return TAG_FACTORY;
  }

  @Override
  public Tag getNewHeaderTag(final CatalogItem item, final int header) throws NewTagException {
    return (Tag) setItemIfNecessary(item, getFixedFieldFactory().create(header));
  }

  @Override
  public Tag getNewTag(final CatalogItem item, final int category, final CorrelationValues correlationValues) throws NewTagException {
    Tag tag = (Tag) getTagFactory().create(category);
    tag = (Tag) setItemIfNecessary(item, tag);

    if (correlationValues != null) {
      if (tag.correlationChangeAffectsKey(correlationValues)) {
        if (tag instanceof HeaderField) {
          tag = (Tag) getFixedFieldFactory().create(correlationValues.getValue(1));
          tag = (Tag) setItemIfNecessary(item, tag);
          tag.setCorrelation(1, correlationValues.getValue(1));
        } else if (tag instanceof BibliographicNoteTag) {
          if (item.getAmicusNumber() == null) {
            tag = new PublisherManager();
            tag.setCorrelationValues(correlationValues);
          } else {
            tag = new PublisherManager(item.getAmicusNumber().intValue(), item.getUserView());
            tag.setCorrelationValues(correlationValues);
          }
          tag = (Tag) setItemIfNecessary(item, tag);
        } else {
          tag.setCorrelationValues(correlationValues);
        }
      } else {
        tag.setCorrelationValues(correlationValues);
      }
    }
    tag.markNew();
    tag.setTagImpl(item.getTagImpl());
    return tag;
  }

  @Override
  protected CatalogItem getNewItem() {
    return new BibliographicItem();
  }

  @Override
  protected ItemEntity getNewItemEntity() {
    return new BIB_ITM();
  }

  @Override
  public CatalogItem applyKeyToItem(final CatalogItem item, final Object[] key) {
    BibliographicItem bibliographicItem = (BibliographicItem) item;

    int cataloguingView = (Integer) key[0];
    if (key.length > 1) {
      int amicusNumber = (Integer) key[1];
      bibliographicItem.getBibItmData().setAmicusNumber(amicusNumber);
    }

    bibliographicItem.getBibItmData().setUserViewString(View.makeSingleViewString(cataloguingView));
    bibliographicItem.setUserView(cataloguingView);
    return bibliographicItem;
  }

  @Override
  public ModelDAO getModelDAO() {
    return MODEL_DAO;
  }

  @Override
  public Model newModel(final CatalogItem item) {
    //return new BibliographicModel(item);
    return null;
  }

  @Override
  public void addDefaultTags(final CatalogItem item) {
    try {
      item.addTag(getNewTag(item, (short) 1, new ControlNumberAccessPoint().getCorrelationValues()));
      item.addTag(getNewTag(item, (short) 1, new ClassificationAccessPoint().getCorrelationValues()));
    } catch (NewTagException e) {
      throw new RuntimeException("error creating bibliographic leader");
    }
  }

  @Override
  public void addDefaultTag(final CatalogItem item) {
    try {
      item.addTag(
        getNewTag(
          item,
          (short) 1,
          new BibliographicLeader().getCorrelationValues()));
      item.addTag(getNewTag(item, (short) 1, new BibliographicLeader().getCorrelationValues()));
    } catch (NewTagException e) {
      throw new RuntimeException("error creating bibliographic leader");
    }

  }

  @Override
  public String getMarcTypeCode() {
    return "B";
  }

  @Override
  public void changeDescriptorType(final CatalogItem item, final int index, final int descriptorType) {
    // do nothing (not applicable to bib)
  }

  @Override
  public List getValidHeadingTypeList(final Tag tag, final Locale locale) {
    // not applicable to bibliographic tags
    return null;
  }

  @Override
  public String getLockingEntityType() {
    return "BI";
  }
  
  public PhysicalDescription createPhysicalDescriptionTag(final CatalogItem item, final CorrelationValues correlationValues) throws NewTagException {
	    final PhysicalDescription physicalDescription =
	      (PhysicalDescription) getNewTag(item,
	        GlobalStorage.HEADER_CATEGORY,
	        correlationValues);
	    return physicalDescription;
	  }
  
  /**
   * Put physical type content into persistent hibernate object.
   *
   * @param ff                  -- the fixed field representing material description.
   * @param physicalDescription -- the persistent hibernate class {@link PhysicalDescription}.
   */
  public void toPhysicalDescription(final org.folio.marccat.resources.domain.FixedField ff,
                                    final PhysicalDescription physicalDescription) {

    physicalDescription.setGeneralMaterialDesignationCode(ff.getCategoryOfMaterial().charAt(0));
    physicalDescription.setSpecificMaterialDesignationCode(ff.getSpecificMaterialDesignationCode().charAt(0));
    if (physicalDescription instanceof ElectronicResource) {
      if (isNotNull(ff.getColourCode()))
        ((ElectronicResource) physicalDescription).setColourCode(ff.getColourCode().charAt(0));
      if (isNotNull(ff.getDimensionsCode()))
        ((ElectronicResource) physicalDescription).setDimensionsCode(ff.getDimensionsCode().charAt(0));
      if (isNotNull(ff.getIncludesSoundCode()))
        ((ElectronicResource) physicalDescription).setIncludesSoundCode(ff.getIncludesSoundCode().charAt(0));
      if (isNotNull(ff.getImageBitDepth()))
        ((ElectronicResource) physicalDescription).setImageBitDepth(ff.getImageBitDepth());
      if (isNotNull(ff.getFileFormatsCode()))
        ((ElectronicResource) physicalDescription).setFileFormatsCode(ff.getFileFormatsCode().charAt(0));
      if (isNotNull(ff.getQualityAssuranceTargetCode()))
        ((ElectronicResource) physicalDescription).setQualityAssuranceTargetCode(ff.getQualityAssuranceTargetCode().charAt(0));
      if (isNotNull(ff.getAntecedentSourceCode()))
        ((ElectronicResource) physicalDescription).setAntecedentSourceCode(ff.getAntecedentSourceCode().charAt(0));
      if (isNotNull(ff.getLevelOfCompressionCode()))
        ((ElectronicResource) physicalDescription).setLevelOfCompressionCode(ff.getLevelOfCompressionCode().charAt(0));
      if (isNotNull(ff.getReformattingQualityCode()))
        ((ElectronicResource) physicalDescription).setReformattingQualityCode(ff.getReformattingQualityCode().charAt(0));
      if (isNotNull(ff.getReformattingQualityCode()))
        ((ElectronicResource) physicalDescription).setReformattingQualityCode(ff.getReformattingQualityCode().charAt(0));
    } else if (physicalDescription instanceof Globe) {
      if (isNotNull(ff.getColourCode()))
        ((Globe) physicalDescription).setColourCode(ff.getColourCode().charAt(0));
      if (isNotNull(ff.getPhysicalMediumCode()))
        ((Globe) physicalDescription).setPhysicalMediumCode(ff.getPhysicalMediumCode().charAt(0));
      if (isNotNull(ff.getTypeOfReproductionCode()))
        ((Globe) physicalDescription).setTypeOfReproductionCode(ff.getTypeOfReproductionCode().charAt(0));
    } else if (physicalDescription instanceof Map) {
      if (isNotNull(ff.getColourCode()))
        ((Map) physicalDescription).setMapColourCode(ff.getColourCode().charAt(0));
      if (isNotNull(ff.getPhysicalMediumCode()))
        ((Map) physicalDescription).setMapPhysicalMediumCode(ff.getPhysicalMediumCode().charAt(0));
      if (isNotNull(ff.getTypeOfReproductionCode()))
        ((Map) physicalDescription).setMapTypeOfReproductionCode(ff.getTypeOfReproductionCode().charAt(0));
      if (isNotNull(ff.getProductionDetailsCode()))
        ((Map) physicalDescription).setMapProductionDetailsCode(ff.getProductionDetailsCode().charAt(0));
      if (isNotNull(ff.getPolarityCode()))
        ((Map) physicalDescription).setMapPolarityCode(ff.getPolarityCode().charAt(0));
    } else if (physicalDescription instanceof TactileMaterial) {
      if (isNotNull(ff.getClassOfBrailleWritingCodes()))
        ((TactileMaterial) physicalDescription).setClassOfBrailleWritingCodes(ff.getClassOfBrailleWritingCodes());
      if (isNotNull(ff.getLevelOfContractionCode()))
        ((TactileMaterial) physicalDescription).setLevelOfContractionCode(ff.getLevelOfContractionCode().charAt(0));
      if (isNotNull(ff.getBrailleMusicFormatCodes()))
        ((TactileMaterial) physicalDescription).setBrailleMusicFormatCodes(ff.getBrailleMusicFormatCodes());
      if (isNotNull(ff.getSpecificPhysicalCharacteristicsCode()))
        ((TactileMaterial) physicalDescription).setSpecificPhysicalCharacteristicsCode(ff.getSpecificPhysicalCharacteristicsCode().charAt(0));
    } else if (physicalDescription instanceof ProjectedGraphic) {
      if (isNotNull(ff.getColourCode()))
        ((ProjectedGraphic) physicalDescription).setColourCode(ff.getColourCode().charAt(0));
      if (isNotNull(ff.getBaseOfEmulsionCode()))
        ((ProjectedGraphic) physicalDescription).setBaseOfEmulsionCode(ff.getBaseOfEmulsionCode().charAt(0));
      if (isNotNull(ff.getSoundOnMediumOrSeparateCode()))
        ((ProjectedGraphic) physicalDescription).setSoundOnMediumOrSeparateCode(ff.getSoundOnMediumOrSeparateCode().charAt(0));
      if (isNotNull(ff.getMediumForSoundCode()))
        ((ProjectedGraphic) physicalDescription).setMediumForSoundCode(ff.getMediumForSoundCode().charAt(0));
      if (isNotNull(ff.getDimensionsCode()))
        ((ProjectedGraphic) physicalDescription).setDimensionsCode(ff.getDimensionsCode().charAt(0));
      if (isNotNull(ff.getSecondarySupportMaterialCode()))
        ((ProjectedGraphic) physicalDescription).setSecondarySupportMaterialCode(ff.getSecondarySupportMaterialCode().charAt(0));
    } else if (physicalDescription instanceof Microform) {
      if (isNotNull(ff.getPolarityCode()))
        ((Microform) physicalDescription).setPolarityCode(ff.getPolarityCode().charAt(0));
      if (isNotNull(ff.getDimensionsCode()))
        ((Microform) physicalDescription).setDimensionsCode(ff.getDimensionsCode().charAt(0));
      if (isNotNull(ff.getReductionRatioRangeCode()))
        ((Microform) physicalDescription).setReductionRatioRangeCode(ff.getReductionRatioRangeCode().charAt(0));
      if (isNotNull(ff.getReductionRatioCode()))
        ((Microform) physicalDescription).setReductionRatioCode(ff.getReductionRatioCode());
      if (isNotNull(ff.getColourCode()))
        ((Microform) physicalDescription).setColourCode(ff.getColourCode().charAt(0));
      if (isNotNull(ff.getEmulsionOnFilmCode()))
        ((Microform) physicalDescription).setEmulsionOnFilmCode(ff.getEmulsionOnFilmCode().charAt(0));
      if (isNotNull(ff.getGenerationCode()))
        ((Microform) physicalDescription).setGenerationCode(ff.getGenerationCode().charAt(0));
      if (isNotNull(ff.getBaseOfFilmCode()))
        ((Microform) physicalDescription).setBaseOfFilmCode(ff.getBaseOfFilmCode().charAt(0));
    } else if (physicalDescription instanceof NonProjectedGraphic) {
      if (isNotNull(ff.getColourCode()))
        ((NonProjectedGraphic) physicalDescription).setColourCode(ff.getColourCode().charAt(0));
      if (isNotNull(ff.getPrimarySupportMaterialCode()))
        ((NonProjectedGraphic) physicalDescription).setPrimarySupportMaterialCode(ff.getPrimarySupportMaterialCode().charAt(0));
      if (isNotNull(ff.getSecondarySupportMaterialCode()))
        ((NonProjectedGraphic) physicalDescription).setSecondarySupportMaterialCode(ff.getSecondarySupportMaterialCode().charAt(0));
    } else if (physicalDescription instanceof MotionPicture) {
      if (isNotNull(ff.getColourCode()))
        ((MotionPicture) physicalDescription).setColourCode(ff.getColourCode().charAt(0));
      if (isNotNull(ff.getPresentationFormatCode()))
        ((MotionPicture) physicalDescription).setPresentationFormatCode(ff.getPresentationFormatCode().charAt(0));
      if (isNotNull(ff.getSoundOnMediumOrSeparateCode()))
        ((MotionPicture) physicalDescription).setIncludesSoundCode(ff.getSoundOnMediumOrSeparateCode().charAt(0));
      if (isNotNull(ff.getMediumForSoundCode()))
        ((MotionPicture) physicalDescription).setMediumForSoundCode(ff.getMediumForSoundCode().charAt(0));
      if (isNotNull(ff.getDimensionsCode()))
        ((MotionPicture) physicalDescription).setDimensionsCode(ff.getDimensionsCode().charAt(0));
      if (isNotNull(ff.getConfigurationCode()))
        ((MotionPicture) physicalDescription).setConfigurationCode(ff.getConfigurationCode().charAt(0));
      if (isNotNull(ff.getProductionElementsCode()))
        ((MotionPicture) physicalDescription).setProductionElementsCode(ff.getProductionElementsCode().charAt(0));
      if (isNotNull(ff.getPolarityCode()))
        ((MotionPicture) physicalDescription).setPolarityCode(ff.getPolarityCode().charAt(0));
      if (isNotNull(ff.getGenerationCode()))
        ((MotionPicture) physicalDescription).setGenerationCode(ff.getPolarityCode().charAt(0));
      if (isNotNull(ff.getBaseOfFilmCode()))
        ((MotionPicture) physicalDescription).setBaseOfFilmCode(ff.getBaseOfFilmCode().charAt(0));
      if (isNotNull(ff.getRefinedCategoriesOfColourCode()))
        ((MotionPicture) physicalDescription).setRefinedCategoriesOfColourCode(ff.getRefinedCategoriesOfColourCode().charAt(0));
      if (isNotNull(ff.getKindOfColourStockCode()))
        ((MotionPicture) physicalDescription).setKindOfColourStockCode(ff.getKindOfColourStockCode().charAt(0));
      if (isNotNull(ff.getDeteriorationStageCode()))
        ((MotionPicture) physicalDescription).setDeteriorationStageCode(ff.getDeteriorationStageCode().charAt(0));
      if (isNotNull(ff.getCompletenessCode()))
        ((MotionPicture) physicalDescription).setCompletenessCode(ff.getCompletenessCode().charAt(0));
      if (isNotNull(ff.getInspectionDate()))
        ((MotionPicture) physicalDescription).setInspectionDate(ff.getInspectionDate());
    } else if (physicalDescription instanceof Kit) {
    } else if (physicalDescription instanceof NotatedMusic) {
    } else if (physicalDescription instanceof RemoteSensingImage) {
      if (isNotNull(ff.getAltitudeOfSensorCode()))
        ((RemoteSensingImage) physicalDescription).setAltitudeOfSensorCode(ff.getAltitudeOfSensorCode().charAt(0));
      if (isNotNull(ff.getAttitudeOfSensorCode()))
        ((RemoteSensingImage) physicalDescription).setAttitudeOfSensorCode(ff.getAttitudeOfSensorCode().charAt(0));
      if (isNotNull(ff.getCloudCoverCode()))
        ((RemoteSensingImage) physicalDescription).setCloudCoverCode(ff.getCloudCoverCode().charAt(0));
      if (isNotNull(ff.getPlatformConstructionTypeCode()))
        ((RemoteSensingImage) physicalDescription).setPlatformConstructionTypeCode(ff.getPlatformConstructionTypeCode().charAt(0));
      if (isNotNull(ff.getPlatformUseCode()))
        ((RemoteSensingImage) physicalDescription).setPlatformUseCode(ff.getPlatformUseCode().charAt(0));
      if (isNotNull(ff.getSensorTypeCode()))
        ((RemoteSensingImage) physicalDescription).setSensorTypeCode(ff.getSensorTypeCode().charAt(0));
      if (isNotNull(ff.getRemoteDataTypeCode()))
        ((RemoteSensingImage) physicalDescription).setDataTypeCode(ff.getRemoteDataTypeCode());
    } else if (physicalDescription instanceof SoundRecording) {
      if (isNotNull(ff.getSpeedCode()))
        ((SoundRecording) physicalDescription).setSpeedCode(ff.getSpeedCode().charAt(0));
      if (isNotNull(ff.getConfigurationCode()))
        ((SoundRecording) physicalDescription).setConfigurationCode(ff.getConfigurationCode().charAt(0));
      if (isNotNull(ff.getGrooveWidthCode()))
        ((SoundRecording) physicalDescription).setGrooveWidthCode(ff.getGrooveWidthCode().charAt(0));
      if (isNotNull(ff.getDimensionsCode()))
        ((SoundRecording) physicalDescription).setDimensionsCode(ff.getDimensionsCode().charAt(0));
      if (isNotNull(ff.getTapeWidthCode()))
        ((SoundRecording) physicalDescription).setTapeWidthCode(ff.getTapeWidthCode().charAt(0));
      if (isNotNull(ff.getTapeConfigurationCode()))
        ((SoundRecording) physicalDescription).setTapeConfigurationCode(ff.getTapeConfigurationCode().charAt(0));
      if (isNotNull(ff.getDiscTypeCode()))
        ((SoundRecording) physicalDescription).setDiscTypeCode(ff.getDiscTypeCode().charAt(0));
      if (isNotNull(ff.getSndMaterialTypeCode()))
        ((SoundRecording) physicalDescription).setSndMaterialTypeCode(ff.getSndMaterialTypeCode().charAt(0));
      if (isNotNull(ff.getCuttingTypeCode()))
        ((SoundRecording) physicalDescription).setCuttingTypeCode(ff.getCuttingTypeCode().charAt(0));
      if (isNotNull(ff.getSpecialPlaybackCharacteristicsCode()))
        ((SoundRecording) physicalDescription).setSpecialPlaybackCharacteristicsCode(ff.getSpecialPlaybackCharacteristicsCode().charAt(0));
      if (isNotNull(ff.getStorageTechniqueCode()))
        ((SoundRecording) physicalDescription).setStorageTechniqueCode(ff.getStorageTechniqueCode().charAt(0));
    } else if (physicalDescription instanceof Text) {
    } else if (physicalDescription instanceof Unspecified) {
    } else if (physicalDescription instanceof VideoRecording) {
      if (isNotNull(ff.getColourCode()))
        ((VideoRecording) physicalDescription).setColourCode(ff.getColourCode().charAt(0));
      if (isNotNull(ff.getFormatCode()))
        ((VideoRecording) physicalDescription).setFormatCode(ff.getFormatCode().charAt(0));
      if (isNotNull(ff.getIncludesSoundCode()))
        ((VideoRecording) physicalDescription).setIncludesSoundCode(ff.getIncludesSoundCode().charAt(0));
      if (isNotNull(ff.getMediumForSoundCode()))
        ((VideoRecording) physicalDescription).setMediumForSoundCode(ff.getMediumForSoundCode().charAt(0));
      if (isNotNull(ff.getDimensionsCode()))
        ((VideoRecording) physicalDescription).setDimensionsCode(ff.getDimensionsCode().charAt(0));
      if (isNotNull(ff.getConfigurationCode()))
        ((VideoRecording) physicalDescription).setConfigurationCode(ff.getConfigurationCode().charAt(0));
    }
  }

  
}
