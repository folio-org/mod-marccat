/*
 * (c) LibriCore
 * 
 * $Author: Paulm $
 * $Date: 2006/05/24 13:33:38 $
 * $Locker:  $
 * $Name:  $
 * $Revision: 1.7 $
 * $Source: /source/LibriSuite/src/librisuite/business/cataloguing/bibliographic/BibliographicCatalog.java,v $
 * $State: Exp $
 */
package org.folio.cataloging.business.cataloguing.bibliographic;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.folio.cataloging.business.cataloguing.common.AccessPoint;
import org.folio.cataloging.business.cataloguing.common.Catalog;
import org.folio.cataloging.business.cataloguing.common.CatalogItem;
import org.folio.cataloging.business.cataloguing.common.CataloguingSourceTag;
import org.folio.cataloging.business.cataloguing.common.ControlNumberTag;
import org.folio.cataloging.dao.DAOCatalog;
import org.folio.cataloging.dao.DAOModel;
import org.folio.cataloging.business.cataloguing.common.DateOfLastTransactionTag;
import org.folio.cataloging.business.cataloguing.common.HeaderField;
import org.folio.cataloging.business.cataloguing.common.ItemEntity;
import org.folio.cataloging.business.cataloguing.common.Model;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.AbstractMapBackedFactory;
import org.folio.cataloging.business.common.CorrelationValues;
import org.folio.cataloging.dao.DAOCache;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.LibrisuiteUtils;
import org.folio.cataloging.business.common.MapBackedFactory;
import org.folio.cataloging.business.common.PropertyBasedFactoryBuilder;
import org.folio.cataloging.business.common.RecordNotFoundException;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.business.descriptor.PublisherTagDescriptor;
import org.folio.cataloging.exception.ValidationException;
import org.folio.cataloging.dao.persistence.PUBL_HDG;
import org.folio.cataloging.dao.persistence.PUBL_TAG;
import org.folio.cataloging.dao.persistence.T_BIB_TAG_CAT;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.dao.DAOBibliographicCatalog;
import org.folio.cataloging.dao.DAOBibliographicModel;

/**
 * Class comment
 * @author janick
 */
public class BibliographicCatalog extends Catalog {
	private static final DAOModel modelDAO = new DAOBibliographicModel();

	protected static AbstractMapBackedFactory tagFactory;

	protected static AbstractMapBackedFactory fixedFieldFactory;

	private static final Log logger =
		LogFactory.getLog(BibliographicCatalog.class);
	private static final DAOBibliographicCatalog catalogDao =
		new DAOBibliographicCatalog();
	static {
		tagFactory = new MapBackedFactory();
		fixedFieldFactory = new MapBackedFactory();
		PropertyBasedFactoryBuilder builder = new PropertyBasedFactoryBuilder();
		builder.load(
				"/org/folio/cataloging/business/cataloguing/bibliographic/tagFactory.properties",
			tagFactory);
		builder.load(
				"/org/folio/cataloging/business/cataloguing/bibliographic/fixedFieldFactory.properties",
			fixedFieldFactory);
	}

	/**
	 * 
	 */
	public BibliographicCatalog() {
		super();
	}

	/**
	 * 
	 */
	public DAOCatalog getCatalogDao() {
		return catalogDao;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Catalog#getTagCateogires()
	 */
	public List getTagCategories(Locale l) throws DataAccessException {
		/*modifica barbara 16/05/2007 prn 55*/
		return daoCodeTable.getOptionListOrderAlphab(T_BIB_TAG_CAT.class, l);
	}

	/**
	 * Ensures that after creating a new BibItem (usually from a model) that the
	 * item has at least the required mandatory tags
	 * 
	 * @since 1.0
	 */
	public void addRequiredTags(CatalogItem item) throws NewTagException {
		BibliographicLeader leader = createRequiredLeaderTag(item);
		if (!item.getTags().contains(leader)) {
			item.addTag(leader);
		}

		ControlNumberTag controlnumber = createRequiredControlNumberTag(item);
		if (!item.getTags().contains(controlnumber)) {
			item.addTag(controlnumber);
		}

		DateOfLastTransactionTag dateTag = createRequiredDateOfLastTransactionTag(item);
		if (!item.getTags().contains(dateTag)) {
			item.addTag(dateTag);
		}

		MaterialDescription mdTag = createRequiredMaterialDescriptionTag(item);
		if (!item.getTags().contains(mdTag)) {
			item.addTag(mdTag);
		}

		CataloguingSourceTag source = createRequiredCataloguingSourceTag(item);
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


	/**
	 *modifica BARBARA 20/02/2007 PR0046 
	 *creo i 4 tag obbligatori e non lo 005
	 * 
	 * @since 1.0
	 */
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

	/* (non-Javadoc)
	 * @see Catalog#getFixedFieldFactory()
	 */
	public AbstractMapBackedFactory getFixedFieldFactory() {
		return fixedFieldFactory;
	}

	/* (non-Javadoc)
	 * @see Catalog#getTagFactory()
	 */
	public AbstractMapBackedFactory getTagFactory() {
		return tagFactory;
	}

	public Tag getNewHeaderTag(CatalogItem item, short header)
		throws NewTagException {
		return (Tag) setItemIfNecessary(
			item,
			getFixedFieldFactory().create(header));
	}

	public Tag getNewTag(
		CatalogItem item,
		short category,
		CorrelationValues correlationValues)
		throws NewTagException {
		Tag tag = (Tag) getTagFactory().create(category);
		tag = (Tag) setItemIfNecessary(item, tag);
		if (correlationValues != null) {
			if (tag.correlationChangeAffectsKey(correlationValues)) {
				if (tag instanceof HeaderField) {
					tag =
						(Tag) getFixedFieldFactory().create(
							correlationValues.getValue(1));
					tag = (Tag) setItemIfNecessary(item, tag);
					tag.setCorrelation(1, correlationValues.getValue(1));
				} else if (tag instanceof BibliographicNoteTag) {
					/*Da creazione modello  al tag vanno settate le correlations*/
					if (item.getAmicusNumber() == null) {
						tag = new PublisherManager();
						tag.setCorrelationValues(correlationValues);
					} else {
						
						tag =
							new PublisherManager(
								item.getAmicusNumber().intValue(),
								((BibliographicItem) item).getUserView());
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

	/* (non-Javadoc)
	 * @see Catalog#getNewItem()
	 */
	protected CatalogItem getNewItem() {
		return new BibliographicItem();
	}

	/* (non-Javadoc)
	 * @see Catalog#getNewItemEntity()
	 */
	protected ItemEntity getNewItemEntity() {
		return new BIB_ITM();
	}

	public CatalogItem applyKeyToItem(CatalogItem item, Object[] key) {
		int cataloguingView = ((Integer) key[0]).intValue();
		BibliographicItem bibliographicItem = (BibliographicItem) item;
		bibliographicItem.getBibItmData().setUserViewString(
			View.makeSingleViewString(cataloguingView));
		bibliographicItem.setUserView(cataloguingView);
		return bibliographicItem;
	}

	/* (non-Javadoc)
	 * @see Catalog#getModelDAO()
	 */
	public DAOModel getModelDAO() {
		return modelDAO;
	}

	/* (non-Javadoc)
	 * @see Catalog#newModel(CatalogItem)
	 */
	public Model newModel(CatalogItem item) {
		return new BibliographicModel(item);
	}

	/* (non-Javadoc)
	 * @see Catalog#addOneTag(CatalogItem)
	 */
	public void addDefaultTags(CatalogItem item) {
		try {
			
			
			item.addTag(getNewTag(item,(short) 1,new ControlNumberAccessPoint().getCorrelationValues()));
			item.addTag(getNewTag(item,(short) 1,new ClassificationAccessPoint().getCorrelationValues()));
			
			
		} catch (NewTagException e) {
			throw new RuntimeException("error creating bibliographic leader");
		}

	}

	public void addDefaultTag(CatalogItem item) {
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
	
	/* (non-Javadoc)
	 * @see Catalog#getMarcTypeCode()
	 */
	public String getMarcTypeCode() {
		return "B";
	}

	/* (non-Javadoc)
	 * @see Catalog#changeDescriptorType(CatalogItem, int, short)
	 */
	public void changeDescriptorType(
		CatalogItem item,
		int index,
		short descriptorType) {
		// do nothing (not applicable to bib)

	}

	/* (non-Javadoc)
	 * @see Catalog#getValidHeadingTypeList(Tag)
	 */
	public List getValidHeadingTypeList(Tag t, Locale locale) {
		// not applicable to bibliographic tags
		return null;
	}

	public void transferItems(Descriptor source, Descriptor target) throws DataAccessException 
	{
		catalogDao.transferItems(source, target);
	}
	
	public void attachEquivalentSubjects(BibliographicItem item) throws DataAccessException {
		Collection newTags = catalogDao.getEquivalentSubjects(item);
		item.getTags().addAll(newTags);
		item.sortTags();

	}
	public String getLockingEntityType() {
		return "BI";
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
		logger.debug("findOrCreateMyView(" + recordView + ", " + amicusNumber
				+ ", " + cataloguingView + ")");

		if (recordView == cataloguingView) {
			// nothing to do
			return getCatalogItem(new Object[] { new Integer(amicusNumber),
					new Integer(recordView) });
		}
		try {
			new DAOCache().load(amicusNumber, cataloguingView);
			return getCatalogItem(new Object[] { new Integer(amicusNumber),
					new Integer(cataloguingView) }); // no exception means
														// record in this view
														// already exists
		} catch (RecordNotFoundException e) {
			// do nothing -- carry on creating record in myView
		}
		CatalogItem item = getCatalogItem(new Object[] {
				new Integer(amicusNumber), new Integer(recordView) });
		item = (CatalogItem) LibrisuiteUtils.deepCopy(item);
		applyKeyToItem(item, new Object[] { new Integer(cataloguingView) });
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
				while(ite.hasNext())
				{
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
