package org.folio.cataloging.business.cataloguing.bibliographic;

import org.folio.cataloging.business.cataloguing.common.*;
import org.folio.cataloging.business.common.*;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.business.descriptor.PublisherTagDescriptor;
import org.folio.cataloging.dao.*;
import org.folio.cataloging.dao.persistence.PUBL_HDG;
import org.folio.cataloging.dao.persistence.PUBL_TAG;
import org.folio.cataloging.dao.persistence.T_BIB_TAG_CAT;
import org.folio.cataloging.exception.ValidationException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static org.folio.cataloging.F.deepCopy;

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

	protected static AbstractMapBackedFactory TAG_FACTORY;
	protected static AbstractMapBackedFactory FIXED_FIELDS_FACTORY;

	static {
		TAG_FACTORY = new MapBackedFactory();
		FIXED_FIELDS_FACTORY = new MapBackedFactory();
		final PropertyBasedFactoryBuilder builder = new PropertyBasedFactoryBuilder();
		builder.load(
				"/org/folio/cataloging/business/cataloguing/bibliographic/TAG_FACTORY.properties",
				TAG_FACTORY);
		builder.load(
				"/org/folio/cataloging/business/cataloguing/bibliographic/FIXED_FIELDS_FACTORY.properties",
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
		DateOfLastTransactionTag dateTag =
			(DateOfLastTransactionTag) getNewTag(item,
				(short) 1,
				new CorrelationValues(
					new BibliographicDateOfLastTransactionTag().getHeaderType(),
					CorrelationValues.UNDEFINED,
					CorrelationValues.UNDEFINED));
		return dateTag;
	}


	public void addRequiredTagsForModel(CatalogItem item) throws NewTagException {
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

	public CataloguingSourceTag createRequiredCataloguingSourceTag(CatalogItem item) throws NewTagException {
		CataloguingSourceTag source =
			(CataloguingSourceTag) getNewTag(item,
				(short) 1,
				new CorrelationValues(
					new BibliographicCataloguingSourceTag().getHeaderType(),
					CorrelationValues.UNDEFINED,
					CorrelationValues.UNDEFINED));
		return source;
	}

	public MaterialDescription createRequiredMaterialDescriptionTag(CatalogItem item) throws NewTagException {
		MaterialDescription mdTag =
			(MaterialDescription) getNewTag(item,
				(short) 1,
				new CorrelationValues(
					(short) 31,
					CorrelationValues.UNDEFINED,
					CorrelationValues.UNDEFINED));
		return mdTag;
	}

	public ControlNumberTag createRequiredControlNumberTag(CatalogItem item) throws NewTagException {
		ControlNumberTag controlnumber =
			(ControlNumberTag) getNewTag(item,
				(short) 1,
				new CorrelationValues(
					new BibliographicControlNumberTag().getHeaderType(),
					CorrelationValues.UNDEFINED,
					CorrelationValues.UNDEFINED));
		return controlnumber;
	}

	public BibliographicLeader createRequiredLeaderTag(CatalogItem item) throws NewTagException {
		BibliographicLeader leader =
			(BibliographicLeader) getNewTag(item,
				(short) 1,
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
	public Tag getNewHeaderTag(final CatalogItem item, final short header) throws NewTagException {
		return (Tag) setItemIfNecessary(item, getFixedFieldFactory().create(header));
	}

	public static void main(final String args []) {
		BibliographicCatalog catalog = new BibliographicCatalog();
		Tag tag = (Tag) catalog.getTagFactory().create(1);
	}

	@Override
	public Tag getNewTag(final CatalogItem item, final short category, final CorrelationValues correlationValues) throws NewTagException {
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
		BibliographicItem bibliographicItem = (BibliographicItem) item;
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
		return new BibliographicModel(item);
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
	public void changeDescriptorType(final CatalogItem item, final int index, final short descriptorType) {
		// do nothing (not applicable to bib)
	}

	@Override
	public List getValidHeadingTypeList(final Tag tag, final Locale locale) {
		// not applicable to bibliographic tags
		return null;
	}

	// TODO: Is this method still in use?
	public void transferItems(Descriptor source, Descriptor target) throws DataAccessException {
		CATALOG_DAO.transferItems(source, target);
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
	public CatalogItem findOrCreateMyView(
			final int recordView,
			final int amicusNumber,
			final int cataloguingView) throws DataAccessException {
		if (recordView == cataloguingView) {
			// nothing to do
			return getCatalogItem(new Object[] { amicusNumber, recordView });
		}
		try {
			new DAOCache().load(amicusNumber, cataloguingView);
			return getCatalogItem(new Object[] { amicusNumber, cataloguingView });
		} catch (final RecordNotFoundException exception) {
			// do nothing -- carry on creating record in myView
		}

		final CatalogItem item = (CatalogItem) deepCopy(getCatalogItem(new Object[] { amicusNumber, recordView }));
		applyKeyToItem(item, new Object[] { cataloguingView });
		item.getItemEntity().markNew();
		Iterator iter = item.getTags().iterator();
		Tag aTag;
		while (iter.hasNext()) {
			aTag = (Tag) iter.next();
			aTag.markNew();
			// TODO PAUL provide a common api for AccessPoint and
			// PublisherManager
			if (aTag instanceof AccessPoint) {
				AccessPoint apf = ((AccessPoint) aTag);
				Descriptor orig = apf.getDescriptor();
				Descriptor d = apf.getDAODescriptor().findOrCreateMyView(
						orig.getHeadingNumber(),
						View.makeSingleViewString(recordView), cataloguingView);
				apf.setDescriptor(d);
			} else if (aTag instanceof PublisherManager) {
				PublisherManager pm = (PublisherManager) aTag;
				PublisherAccessPoint apf = pm.getApf();
				Descriptor orig = apf.getDescriptor();
				List/*<PUBL_TAG>*/ publTags = ((PublisherTagDescriptor)orig).getPublisherTagUnits();
				Iterator/*<PUBL_TAG>*/ ite = publTags.iterator();
				while(ite.hasNext()) {
					PUBL_TAG t =(PUBL_TAG)ite.next();
					PUBL_HDG ph = null;
					ph = (PUBL_HDG) t.getDescriptorDAO().findOrCreateMyView(
							t.getPublisherHeadingNumber(),
							View.makeSingleViewString(recordView), cataloguingView);
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
	
	
}
