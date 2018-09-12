package org.folio.cataloging.business.cataloguing.bibliographic;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.cataloguing.common.*;
import org.folio.cataloging.business.common.*;
import org.folio.cataloging.business.descriptor.PublisherTagDescriptor;
import org.folio.cataloging.dao.*;
import org.folio.cataloging.dao.persistence.*;
import org.folio.cataloging.dao.persistence.Map;
import org.folio.cataloging.exception.ValidationException;
import org.folio.cataloging.integration.GlobalStorage;
import org.folio.cataloging.shared.CorrelationValues;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.folio.cataloging.F.deepCopy;
import static org.folio.cataloging.F.isNotNull;

/**
 * Bibliographic implementation of {@link Catalog} interface.
 *
 * @author paulm
 * @author janick
 * @author agazzarini
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
				"/org/folio/cataloging/business/cataloguing/bibliographic/tagFactory.properties",
				TAG_FACTORY);
		builder.load(
				"/org/folio/cataloging/business/cataloguing/bibliographic/fixedFieldFactory.properties",
				FIXED_FIELDS_FACTORY);
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

  public PhysicalDescription createPhysicalDescriptionTag(final CatalogItem item, final CorrelationValues correlationValues) throws NewTagException {
    final PhysicalDescription physicalDescription =
      (PhysicalDescription) getNewTag(item,
        GlobalStorage.HEADER_CATEGORY,
        correlationValues);
    return physicalDescription;
  }

  public PublisherManager createPublisherTag(final CatalogItem item, final CorrelationValues correlationValues) throws NewTagException, DataAccessException {
    final PublisherManager pap =
      (PublisherManager) getNewTag(item,
        GlobalStorage.PUBLISHER_CATEGORY,
        correlationValues);
    return pap;
  }

  public TitleAccessPoint createTitleAccessPointTag(final CatalogItem item, final CorrelationValues correlationValues) throws NewTagException, DataAccessException {
    final TitleAccessPoint tap =
      (TitleAccessPoint) getNewTag(item,
        GlobalStorage.TITLE_CATEGORY,
        correlationValues);
    return tap;
  }

  public NameAccessPoint createNameAccessPointTag(final CatalogItem item, final CorrelationValues correlationValues) throws NewTagException, DataAccessException {
    final NameAccessPoint nap =
      (NameAccessPoint) getNewTag(item,
        GlobalStorage.NAME_CATEGORY,
        correlationValues);
    return nap;
  }

  public ClassificationAccessPoint createClassificationAccessPoint(final CatalogItem item, final CorrelationValues correlationValues) throws NewTagException, DataAccessException {
    final ClassificationAccessPoint clap =
      (ClassificationAccessPoint) getNewTag(item,
        GlobalStorage.CLASSIFICATION_CATEGORY,
        correlationValues);
    return clap;
  }

  public SubjectAccessPoint createSubjectAccessPoint(final CatalogItem item, final CorrelationValues correlationValues) throws NewTagException, DataAccessException {
    final SubjectAccessPoint sap =
      (SubjectAccessPoint) getNewTag(item,
        GlobalStorage.SUBJECT_CATEGORY,
        correlationValues);
    return sap;
  }

  public ControlNumberAccessPoint createControlNumberAccessPoint(final CatalogItem item, final CorrelationValues correlationValues) throws NewTagException, DataAccessException {
    final ControlNumberAccessPoint cnap =
      (ControlNumberAccessPoint) getNewTag(item,
        GlobalStorage.CONTROL_NUMBER_CATEGORY,
        correlationValues);
    return cnap;
  }

  public BibliographicNoteTag createBibliographicNoteTag(final CatalogItem item, final CorrelationValues correlationValues) throws NewTagException, DataAccessException {
    final BibliographicNoteTag nTag =
      (BibliographicNoteTag) getNewTag(item,
        GlobalStorage.BIB_NOTE_CATEGORY,
        correlationValues);
    return nTag;
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

	public static void main(final String args []) {
		BibliographicCatalog catalog = new BibliographicCatalog();
		Tag tag = (Tag) catalog.getTagFactory().create(1);
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
		int cataloguingView = (Integer) key[0];
		int amicusNumber = (Integer) key[1];
		BibliographicItem bibliographicItem = (BibliographicItem) item;
		bibliographicItem.getBibItmData().setAmicusNumber(amicusNumber);
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
			item.addTag(getNewTag(item,(short) 1,new ControlNumberAccessPoint().getCorrelationValues()));
			item.addTag(getNewTag(item,(short) 1,new ClassificationAccessPoint().getCorrelationValues()));
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
			item.addTag(getNewTag(item,(short) 1,new BibliographicLeader().getCorrelationValues()));
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

	// TODO: Is this method still in use?
	public void transferItems(Descriptor source, Descriptor target) throws DataAccessException {
		/*CATALOG_DAO.transferItems(source, target);*/
	}

	public void attachEquivalentSubjects(final BibliographicItem item) throws DataAccessException {
		final Collection newTags = CATALOG_DAO.getEquivalentSubjects(item);
		item.getTags().addAll(newTags);
		item.sortTags();
	}

	@Override
	public String getLockingEntityType() {
		return "BI";
	}

	/**
	 * Determines whether the given bib record exists in the cataloguing view.
	 * If it does not, then the record in the searching view is duplicated
	 * to a new record in the cataloguing view
	 *
	 * @param recordView the record view identifier.
	 * @param amicusNumber the record identifier.
	 * @param cataloguingView the cataloguing view identifier.
	 * @throws DataAccessException in case of data access failure.
	 * @throws ValidationException in case of validation failure while checking the entity.
	 */
	//TODO
	public CatalogItem findOrCreateMyView(
    final int recordView,
    final int amicusNumber,
    final int cataloguingView,
    final Session session) throws DataAccessException, HibernateException {

	    if (recordView == cataloguingView) {
        return getCatalogItem(amicusNumber, recordView);
      }

      try {
        new DAOCache().load(amicusNumber, cataloguingView);
        return getCatalogItem(amicusNumber, cataloguingView);
      } catch (final RecordNotFoundException exception) {
      }

      final CatalogItem item = (CatalogItem) deepCopy(getCatalogItem(amicusNumber, recordView));
      applyKeyToItem(item, new Object[] { cataloguingView });
      item.getItemEntity().markNew();
      Iterator iter = item.getTags().iterator();
      Tag aTag;
      while (iter.hasNext()) {
        aTag = (Tag) iter.next();
        aTag.markNew();
        if (aTag instanceof AccessPoint) {
          AccessPoint apf = ((AccessPoint) aTag);
          Descriptor orig = apf.getDescriptor();
          Descriptor d = apf.getDAODescriptor().findOrCreateMyView(orig.getHeadingNumber(), View.makeSingleViewString(recordView), cataloguingView, session);
          apf.setDescriptor(d);
        } else if (aTag instanceof PublisherManager) {
          PublisherManager pm = (PublisherManager) aTag;
          PublisherAccessPoint apf = pm.getApf();
          Descriptor orig = apf.getDescriptor();
          List<PUBL_TAG> publTags = ((PublisherTagDescriptor)orig).getPublisherTagUnits();
          Iterator/*<PUBL_TAG>*/ ite = publTags.iterator();
          while(ite.hasNext()) {
            PUBL_TAG t =(PUBL_TAG)ite.next();
            PUBL_HDG ph = null;
            ph = (PUBL_HDG) t.getDescriptorDAO().findOrCreateMyView(
                t.getPublisherHeadingNumber(),
                View.makeSingleViewString(recordView), cataloguingView, session);
            t.setDescriptor(ph);
              t.setUserViewString(View.makeSingleViewString(cataloguingView));
          }
          apf.setUserViewString(View.makeSingleViewString(cataloguingView));
          apf.setDescriptor(orig);
          pm.setApf(apf);
        } else if (aTag instanceof BibliographicRelationshipTag) {
          BibliographicRelationshipTag relTag = (BibliographicRelationshipTag)aTag;
          relTag.copyFromAnotherItem();
        }
      }
      return item;
    }

    /**
     * Put leader content into persistent hibernate object.
     *
     * @param leaderValue -- the string leader value.
     * @param bibliographicLeader -- the persistent hibernate class {@link BibliographicLeader}
     */
	public void toBibliographicLeader(final String leaderValue, final BibliographicLeader bibliographicLeader){
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
	 * @param ff -- the fixed field representing material description.
	 * @param materialDescription -- the persistent hibernate class {@link MaterialDescription}.
	 */
    public void toMaterialDescription(final org.folio.cataloging.resources.domain.FixedField ff,
													 final MaterialDescription materialDescription){
        materialDescription.setMaterialTypeCode(ff.getMaterialTypeCode());
        if (materialDescription.isBook()) {
			if (isNotNull(ff.getFormOfItemCode())) materialDescription.setFormOfItemCode(ff.getFormOfItemCode());
			final String bookIllustration = (isNotNull(ff.getBookIllustrationCode1()) ?ff.getBookIllustrationCode1() :"")
											+(isNotNull(ff.getBookIllustrationCode2()) ?ff.getBookIllustrationCode2() :"")
											+(isNotNull(ff.getBookIllustrationCode3()) ?ff.getBookIllustrationCode3() :"")
											+(isNotNull(ff.getBookIllustrationCode4()) ?ff.getBookIllustrationCode4() :"");
			materialDescription.setBookIllustrationCode(bookIllustration);
			if (isNotNull(ff.getTargetAudienceCode())) materialDescription.setTargetAudienceCode(ff.getTargetAudienceCode());
			final String natureContentCodes = (isNotNull(ff.getNatureOfContent1()) ?ff.getNatureOfContent1() :"")
											+ (isNotNull(ff.getNatureOfContent2()) ?ff.getNatureOfContent2() :"")
											+ (isNotNull(ff.getNatureOfContent3()) ?ff.getNatureOfContent3() :"")
											+ (isNotNull(ff.getNatureOfContent4()) ?ff.getNatureOfContent4() :"");
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
        	String codes = (isNotNull(ff.getCartographicReliefCode1()) ?ff.getCartographicReliefCode1() :"")
					+ (isNotNull(ff.getCartographicReliefCode2()) ?ff.getCartographicReliefCode2() :"")
					+ (isNotNull(ff.getCartographicReliefCode3()) ?ff.getCartographicReliefCode3() :"")
					+ (isNotNull(ff.getCartographicReliefCode4()) ?ff.getCartographicReliefCode4() :"");
			materialDescription.setCartographicReliefCode(codes);
			if (isNotNull(ff.getGovernmentPublicationCode())) materialDescription.setGovernmentPublicationCode(ff.getGovernmentPublicationCode());
			if (isNotNull(ff.getCartographicProjectionCode())) materialDescription.setCartographicProjectionCode(ff.getCartographicProjectionCode());
			if (isNotNull(ff.getCartographicMaterial())) materialDescription.setCartographicMeridianCode(ff.getCartographicMaterial());
			if (isNotNull(ff.getCartographicMaterial())) materialDescription.setCartographicNarrativeTextCode(ff.getCartographicMaterial());
			if (isNotNull(ff.getCartographicIndexAvailabilityCode())) materialDescription.setCartographicIndexAvailabilityCode(ff.getCartographicIndexAvailabilityCode());
			codes = (isNotNull(ff.getCartographicFormatCode1()) ?ff.getCartographicFormatCode1() :"") + (isNotNull(ff.getCartographicFormatCode2()) ?ff.getCartographicFormatCode2() :"");
			materialDescription.setCartographicFormatCode(codes);
		}else if (materialDescription.isVisualMaterial()) {
			if(isNotNull(ff.getVisualRunningTime())) materialDescription.setVisualRunningTime(ff.getVisualRunningTime());
			if(isNotNull(ff.getTargetAudienceCode())) materialDescription.setVisualTargetAudienceCode(ff.getTargetAudienceCode());
			if (isNotNull(ff.getGovernmentPublicationCode())) materialDescription.setGovernmentPublicationCode(ff.getGovernmentPublicationCode());
			materialDescription.setVisualAccompanyingMaterialCode(" ");
			if (isNotNull(ff.getFormOfItemCode())) materialDescription.setFormOfItemCode(ff.getFormOfItemCode());
			if(isNotNull(ff.getVisualMaterialTypeCode())) materialDescription.setVisualMaterialTypeCode(ff.getVisualMaterialTypeCode());
			if(isNotNull(ff.getVisualTechniqueCode())) materialDescription.setVisualTechniqueCode(ff.getVisualTechniqueCode());
		} else if (materialDescription.isComputerFile()) {
			if(isNotNull(ff.getTargetAudienceCode())) materialDescription.setComputerTargetAudienceCode(ff.getTargetAudienceCode());
			if(isNotNull(ff.getComputerFileTypeCode())) materialDescription.setComputerFileTypeCode(ff.getComputerFileTypeCode());
        	if(isNotNull(ff.getGovernmentPublicationCode())) materialDescription.setGovernmentPublicationCode(ff.getGovernmentPublicationCode());
        	materialDescription.setComputerFileFormCode(ff.getFormOfItemCode());
		} else if (materialDescription.isSerial()) {
			if (isNotNull(ff.getFormOfItemCode())) materialDescription.setFormOfItemCode(ff.getFormOfItemCode());
			if(isNotNull(ff.getSerialFrequencyCode())) materialDescription.setSerialFrequencyCode(ff.getSerialFrequencyCode());
			if(isNotNull(ff.getSerialRegularityCode())) materialDescription.setSerialRegularityCode(ff.getSerialRegularityCode());
			materialDescription.setSerialISDSCenterCode(" ");
			if(isNotNull(ff.getSerialTypeCode())) materialDescription.setSerialTypeCode(ff.getSerialTypeCode());
			if(isNotNull(ff.getSerialFormOriginalItemCode())) materialDescription.setSerialFormOriginalItemCode(ff.getSerialFormOriginalItemCode());
			if(isNotNull(ff.getSerialOriginalAlphabetOfTitleCode())) materialDescription.setSerialOriginalAlphabetOfTitleCode(ff.getSerialOriginalAlphabetOfTitleCode());
			if(isNotNull(ff.getSerialEntryConventionCode())) materialDescription.setSerialSuccessiveLatestCode(ff.getSerialEntryConventionCode());
			materialDescription.setSerialTitlePageExistenceCode(" ");
			materialDescription.setSerialIndexAvailabilityCode(" ");
		} else if (materialDescription.isMixedMaterial()) {
			if (isNotNull(ff.getFormOfItemCode())) materialDescription.setFormOfItemCode(ff.getFormOfItemCode());
		} else if (materialDescription.isMusic()) {
			if (isNotNull(ff.getFormOfItemCode())) materialDescription.setFormOfItemCode(ff.getFormOfItemCode());
			if(isNotNull(ff.getMusicFormOfCompositionCode())) materialDescription.setMusicFormOfCompositionCode(ff.getMusicFormOfCompositionCode());
			if(isNotNull(ff.getMusicFormatCode())) materialDescription.setMusicFormatCode(ff.getMusicFormatCode());
			if(isNotNull(ff.getMusicPartsCode())) materialDescription.setMusicPartsCode(ff.getMusicPartsCode());
			if (isNotNull(ff.getTargetAudienceCode())) materialDescription.setTargetAudienceCode(ff.getTargetAudienceCode());
			String codes = (isNotNull(ff.getMusicTextualMaterialCode1()) ?ff.getMusicTextualMaterialCode1() :" ")
					+ (isNotNull(ff.getMusicTextualMaterialCode2()) ?ff.getMusicTextualMaterialCode2() :"")
					+ (isNotNull(ff.getMusicTextualMaterialCode3()) ?ff.getMusicTextualMaterialCode3() :"")
			        + (isNotNull(ff.getMusicTextualMaterialCode4()) ?ff.getMusicTextualMaterialCode4() :"")
					+ (isNotNull(ff.getMusicTextualMaterialCode5()) ?ff.getMusicTextualMaterialCode5() :"")
			        + (isNotNull(ff.getMusicTextualMaterialCode6()) ?ff.getMusicTextualMaterialCode6() :"");

			materialDescription.setMusicTextualMaterialCode(codes);
			codes = (isNotNull(ff.getMusicLiteraryTextCode1()) ?ff.getMusicLiteraryTextCode1() :"")
					+ (isNotNull(ff.getMusicLiteraryTextCode2()) ?ff.getMusicLiteraryTextCode2() :"");
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
			if (isNotNull(ff.getDataTypeCode()))	materialDescription.setItemDateTypeCode(ff.getDataTypeCode().charAt(0));
            if (isNotNull(ff.getDateFirstPublication())) materialDescription.setItemDateFirstPublication(ff.getDateFirstPublication());
			if (isNotNull(ff.getDateLastPublication())) materialDescription.setItemDateLastPublication(ff.getDateLastPublication());
            if (isNotNull(ff.getPlaceOfPublication())) materialDescription.setMarcCountryCode(ff.getPlaceOfPublication());
            if (isNotNull(ff.getLanguageCode())) materialDescription.setLanguageCode(ff.getLanguageCode());
            if (isNotNull(ff.getRecordModifiedCode())) materialDescription.setRecordModifiedCode(ff.getRecordModifiedCode().charAt(0));
            if (isNotNull(ff.getRecordCataloguingSourceCode())) materialDescription.setRecordCataloguingSourceCode(ff.getRecordCataloguingSourceCode().charAt(0));
        }

    }

  /**
   *
   * @param ff
   * @param physicalDescription
   */
  public void toPhysicalDescription(final org.folio.cataloging.resources.domain.FixedField ff,
                                    final PhysicalDescription physicalDescription) {

    physicalDescription.setSpecificMaterialDesignationCode(ff.getSpecificMaterialDesignationCode().charAt(0));
    if (physicalDescription instanceof ElectronicResource) {
      if (isNotNull(ff.getColourCode())) ((ElectronicResource) physicalDescription).setColourCode(ff.getColourCode().charAt(0));
      if (isNotNull(ff.getDimensionsCode())) ((ElectronicResource) physicalDescription).setDimensionsCode(ff.getDimensionsCode().charAt(0));
      if (isNotNull(ff.getIncludesSoundCode())) ((ElectronicResource) physicalDescription).setIncludesSoundCode(ff.getIncludesSoundCode().charAt(0));
      if (isNotNull(ff.getImageBitDepth())) ((ElectronicResource) physicalDescription).setImageBitDepth(ff.getImageBitDepth());
      if (isNotNull(ff.getFileFormatsCode())) ((ElectronicResource) physicalDescription).setFileFormatsCode(ff.getFileFormatsCode().charAt(0));
      if (isNotNull(ff.getQualityAssuranceTargetCode())) ((ElectronicResource) physicalDescription).setQualityAssuranceTargetCode(ff.getQualityAssuranceTargetCode().charAt(0));
      if (isNotNull(ff.getAntecedentSourceCode())) ((ElectronicResource) physicalDescription).setAntecedentSourceCode(ff.getAntecedentSourceCode().charAt(0));
      if (isNotNull(ff.getLevelOfCompressionCode())) ((ElectronicResource) physicalDescription).setLevelOfCompressionCode(ff.getLevelOfCompressionCode().charAt(0));
      if (isNotNull(ff.getReformattingQualityCode())) ((ElectronicResource) physicalDescription).setReformattingQualityCode(ff.getReformattingQualityCode().charAt(0));
      if (isNotNull(ff.getReformattingQualityCode())) ((ElectronicResource) physicalDescription).setReformattingQualityCode(ff.getReformattingQualityCode().charAt(0));
    } else if (physicalDescription instanceof Globe) {
      if (isNotNull(ff.getColourCode())) ((Globe) physicalDescription).setColourCode(ff.getColourCode().charAt(0));
      if (isNotNull(ff.getPhysicalMediumCode())) ((Globe) physicalDescription).setPhysicalMediumCode(ff.getPhysicalMediumCode().charAt(0));
      if (isNotNull(ff.getTypeOfReproductionCode())) ((Globe) physicalDescription).setTypeOfReproductionCode(ff.getTypeOfReproductionCode().charAt(0));
    } else if (physicalDescription instanceof Map) {
      if (isNotNull(ff.getColourCode())) ((Map) physicalDescription).setMapColourCode(ff.getColourCode().charAt(0));
      if (isNotNull(ff.getPhysicalMediumCode())) ((Map) physicalDescription).setMapPhysicalMediumCode(ff.getPhysicalMediumCode().charAt(0));
      if (isNotNull(ff.getTypeOfReproductionCode())) ((Map) physicalDescription).setMapTypeOfReproductionCode(ff.getTypeOfReproductionCode().charAt(0));
			/*
			fixedField.setProductionDetailsCode(String.valueOf(valueField.charAt(6)));
			fixedField.setPolarityCode(String.valueOf(valueField.charAt(7)));
			fixedField.setPhysicalType(org.folio.cataloging.resources.domain.FixedField.PhysicalType.MAP);
		} else if (categoryOfMaterial.equals(Global.TACTILE_MATERIAL)) {
			fixedField.setClassOfBrailleWritingCodes(valueField.substring(3, 5));
			fixedField.setLevelOfContractionCode(String.valueOf(valueField.charAt(5)));
			fixedField.setBrailleMusicFormatCodes(valueField.substring(6, 9));
			fixedField.setSpecificPhysicalCharacteristicsCode(String.valueOf(valueField.charAt(9)));
			fixedField.setPhysicalType(org.folio.cataloging.resources.domain.FixedField.PhysicalType.TACTILE_MATERIAL);
		} else if (categoryOfMaterial.equals(Global.PROJECTED_GRAPHIC)) {
			fixedField.setColourCode(String.valueOf(valueField.charAt(3)));
			fixedField.setBaseOfEmulsionCode(String.valueOf(valueField.charAt(4)));
			fixedField.setSoundOnMediumOrSeparateCode(String.valueOf(valueField.charAt(5)));
			fixedField.setMediumForSoundCode(String.valueOf(valueField.charAt(6)));
			fixedField.setDimensionsCode(String.valueOf(valueField.charAt(7)));
			fixedField.setSecondarySupportMaterialCode(String.valueOf(valueField.charAt(8)));
			fixedField.setPhysicalType(org.folio.cataloging.resources.domain.FixedField.PhysicalType.PROJECTED_GRAPHIC);
		} else if (categoryOfMaterial.equals(Global.MICROFORM)) {
			fixedField.setPolarityCode(String.valueOf(valueField.charAt(3)));
			fixedField.setDimensionsCode(String.valueOf(valueField.charAt(4)));
			fixedField.setReductionRatioRangeCode(String.valueOf(valueField.charAt(5)));
			fixedField.setReductionRatioCode(valueField.substring(6, 9));
			fixedField.setColourCode(String.valueOf(valueField.charAt(9)));
			fixedField.setEmulsionOnFilmCode(String.valueOf(valueField.charAt(10)));
			fixedField.setGenerationCode(String.valueOf(valueField.charAt(11)));
			fixedField.setBaseOfFilmCode(String.valueOf(valueField.charAt(12)));
			fixedField.setPhysicalType(org.folio.cataloging.resources.domain.FixedField.PhysicalType.MICROFORM);
		} else if (categoryOfMaterial.equals(Global.NON_PROJECTED_GRAPHIC)) {
			fixedField.setColourCode(String.valueOf(valueField.charAt(3)));
			fixedField.setPrimarySupportMaterialCode(String.valueOf(valueField.charAt(4)));
			fixedField.setSecondarySupportMaterialCode(String.valueOf(valueField.charAt(5)));
			fixedField.setPhysicalType(org.folio.cataloging.resources.domain.FixedField.PhysicalType.NON_PROJECTED_GRAPHIC);
		} else if (categoryOfMaterial.equals(Global.MOTION_PICTURE)) {
			fixedField.setColourCode(String.valueOf(valueField.charAt(3)));
			fixedField.setPresentationFormatCode(String.valueOf(valueField.charAt(4)));
			fixedField.setSoundOnMediumOrSeparateCode(String.valueOf(valueField.charAt(5)));
			fixedField.setMediumForSoundCode(String.valueOf(valueField.charAt(6)));
			fixedField.setDimensionsCode(String.valueOf(valueField.charAt(7)));
			fixedField.setConfigurationCode(String.valueOf(valueField.charAt(8)));
			fixedField.setProductionElementsCode(String.valueOf(valueField.charAt(9)));
			fixedField.setPolarityCode(String.valueOf(valueField.charAt(10)));
			fixedField.setGenerationCode(String.valueOf(valueField.charAt(11)));
			fixedField.setBaseOfFilmCode(String.valueOf(valueField.charAt(12)));
			fixedField.setRefinedCategoriesOfColourCode(String.valueOf(valueField.charAt(13)));
			fixedField.setKindOfColourStockCode(String.valueOf(valueField.charAt(14)));
			fixedField.setDeteriorationStageCode(String.valueOf(valueField.charAt(15)));
			fixedField.setCompletenessCode(String.valueOf(valueField.charAt(16)));
			fixedField.setInspectionDate(valueField.substring(17));
			fixedField.setPhysicalType(org.folio.cataloging.resources.domain.FixedField.PhysicalType.MOTION_PICTURE);
		} else if (categoryOfMaterial.equals(Global.KIT_CODE)) {
			fixedField.setPhysicalType(org.folio.cataloging.resources.domain.FixedField.PhysicalType.KIT);
		} else if (categoryOfMaterial.equals(Global.NOTATED_MUSIC)) {
			fixedField.setPhysicalType(org.folio.cataloging.resources.domain.FixedField.PhysicalType.NOTATED_MUSIC);
		} else if (categoryOfMaterial.equals(Global.REMOTE_SENSING_IMAGE)) {
			fixedField.setAltitudeOfSensorCode(String.valueOf(valueField.charAt(3)));
			fixedField.setAttitudeOfSensorCode(String.valueOf(valueField.charAt(4)));
			fixedField.setCloudCoverCode(String.valueOf(valueField.charAt(5)));
			fixedField.setPlatformConstructionTypeCode(String.valueOf(valueField.charAt(6)));
			fixedField.setPlatformUseCode(String.valueOf(valueField.charAt(7)));
			fixedField.setSensorTypeCode(String.valueOf(valueField.charAt(8)));
			fixedField.setDataTypeCode(valueField.substring(9));
			fixedField.setPhysicalType(org.folio.cataloging.resources.domain.FixedField.PhysicalType.REMOTE_SENSING_IMAGE);
		} else if (categoryOfMaterial.equals(Global.SOUND_RECORDING)) {
			fixedField.setSpeedCode(String.valueOf(valueField.charAt(3)));
			fixedField.setConfigurationCode(String.valueOf(valueField.charAt(4)));
			fixedField.setGrooveWidthCode(String.valueOf(valueField.charAt(5)));
			fixedField.setDimensionsCode(String.valueOf(valueField.charAt(6)));
			fixedField.setTapeWidthCode(String.valueOf(valueField.charAt(7)));
			fixedField.setTapeConfigurationCode(String.valueOf(valueField.charAt(8)));
			fixedField.setDiscTypeCode(String.valueOf(valueField.charAt(9)));
			fixedField.setSndMaterialTypeCode(String.valueOf(valueField.charAt(10)));
			fixedField.setCuttingTypeCode(String.valueOf(valueField.charAt(11)));
			fixedField.setSpecialPlaybackCharacteristicsCode(String.valueOf(valueField.charAt(12)));
			fixedField.setStorageTechniqueCode(String.valueOf(valueField.charAt(13)));
			fixedField.setPhysicalType(org.folio.cataloging.resources.domain.FixedField.PhysicalType.SOUND_RECORDING);
		} else if (categoryOfMaterial.equals(Global.TEXT_CODE)) {
			fixedField.setPhysicalType(org.folio.cataloging.resources.domain.FixedField.PhysicalType.TEXT);
		} else if (categoryOfMaterial.equals(Global.UNSPECIFIED)) {
			fixedField.setPhysicalType(org.folio.cataloging.resources.domain.FixedField.PhysicalType.UNSPECIFIED);
		} else if (categoryOfMaterial.equals(Global.VIDEO_RECORDING)) {
			fixedField.setColourCode(String.valueOf(valueField.charAt(3)));
			fixedField.setFormatCode(String.valueOf(valueField.charAt(4)));
			fixedField.setIncludesSoundCode(String.valueOf(valueField.charAt(5)));
			fixedField.setMediumForSoundCode(String.valueOf(valueField.charAt(6)));
			fixedField.setDimensionsCode(String.valueOf(valueField.charAt(7)));
			fixedField.setConfigurationCode(String.valueOf(valueField.charAt(8)));
			fixedField.setPhysicalType(org.folio.cataloging.resources.domain.FixedField.PhysicalType.VIDEO_RECORDING);*/
    }

  }

}
