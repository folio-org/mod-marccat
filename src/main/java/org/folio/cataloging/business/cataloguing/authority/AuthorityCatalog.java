package org.folio.cataloging.business.cataloguing.authority;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.cataloguing.bibliographic.NewTagException;
import org.folio.cataloging.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.cataloging.business.cataloguing.common.*;
import org.folio.cataloging.business.common.*;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.business.descriptor.DescriptorFactory;
import org.folio.cataloging.dao.*;
import org.folio.cataloging.dao.persistence.AUT;
import org.folio.cataloging.dao.persistence.REF;
import org.folio.cataloging.dao.persistence.ReferenceType;
import org.folio.cataloging.dao.persistence.T_AUT_TAG_CAT;

import java.util.*;

/**
 * @author paulm
 * @version $Revision: 1.6 $, $Date: 2006/01/11 13:36:22 $
 * @since 1.0
 */
public class AuthorityCatalog extends Catalog {

	private static Log logger = LogFactory.getLog(AuthorityCatalog.class);
	
	public static final int CATALOGUING_VIEW = 1;
	private static final ModelDAO modelDAO = new AuthorityModelDAO();

	private static Map daoByAutType = new HashMap();
	static {
		daoByAutType.put("NH", DAONameDescriptor.class);
		daoByAutType.put("TH", DAOTitleDescriptor.class);
		daoByAutType.put("SH", DAOSubjectDescriptor.class);
		daoByAutType.put("MH", DAONameTitleDescriptor.class);
	}

	private static Map autTypeByDescriptorType = new HashMap();
	static {
		autTypeByDescriptorType.put(new Short((short) 2), "NH");
		autTypeByDescriptorType.put(new Short((short) 3), "TH");
		autTypeByDescriptorType.put(new Short((short) 4), "SH");
		autTypeByDescriptorType.put(new Short((short) 17), "NH");
		autTypeByDescriptorType.put(new Short((short) 22), "TH");
		autTypeByDescriptorType.put(new Short((short) 18), "SH");
		autTypeByDescriptorType.put(new Short((short) 11), "MH");
	}

	private static final AuthorityCatalogDAO daoCatalog =
		new AuthorityCatalogDAO();

	protected static AbstractMapBackedFactory fixedFieldFactory;

	private static Map headingTagByAutType = new HashMap();
	static {
		headingTagByAutType.put("NH", AuthorityNameHeadingTag.class);
		headingTagByAutType.put("TH", AuthorityTitleHeadingTag.class);
		headingTagByAutType.put("SH", AuthoritySubjectHeadingTag.class);
		headingTagByAutType.put("MH", AuthorityNameTitleHeadingTag.class);
	}

	protected static AbstractMapBackedFactory tagFactory;
	static {
		tagFactory = new MapBackedFactory();
		fixedFieldFactory = new MapBackedFactory();
		PropertyBasedFactoryBuilder builder = new PropertyBasedFactoryBuilder();
		builder.load(
				"/org/folio/cataloging/business/cataloguing/authority/TAG_FACTORY.properties",
			tagFactory);
		builder.load(
				"/org/folio/cataloging/business/cataloguing/authority/FIXED_FIELDS_FACTORY.properties",
			fixedFieldFactory);
	}

	public static AuthorityHeadingTag createHeadingTagByType(String type) {
		AuthorityHeadingTag result = null;
		try {
			result =
				(AuthorityHeadingTag) ((Class) headingTagByAutType.get(type))
					.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Could not create object");
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Could not create object");
		}
		return result;
	}

	public static DAODescriptor getDaoByType(String type) {
		DAODescriptor result = null;
		try {
			result =
				(DAODescriptor) ((Class) daoByAutType.get(type)).newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Could not create object");
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Could not create object");
		}
		return result;
	}

	public static String getAutTypeByDescriptorType(short descriptorType) {
		return (String) autTypeByDescriptorType.get(new Short(descriptorType));
	}

	/* (non-Javadoc)
	 * @see Catalog#addRequiredTags(CatalogItem)
	 */
	public void addRequiredTags(CatalogItem item) throws NewTagException {
		AuthorityLeader leader =
			(AuthorityLeader) getNewTag(item,
				(short) 1,
				new CorrelationValues(
					new AuthorityLeader().getHeaderType(),
					CorrelationValues.UNDEFINED,
					CorrelationValues.UNDEFINED));
		if (!item.getTags().contains(leader)) {
			item.addTag(leader);
		}

		ControlNumberTag controlnumber =
			(ControlNumberTag) getNewTag(item,
				(short) 1,
				new CorrelationValues(
					new AuthorityControlNumberTag().getHeaderType(),
					CorrelationValues.UNDEFINED,
					CorrelationValues.UNDEFINED));
		if (!item.getTags().contains(controlnumber)) {
			item.addTag(controlnumber);
		}

		DateOfLastTransactionTag dateTag =
			(DateOfLastTransactionTag) getNewTag(item,
				(short) 1,
				new CorrelationValues(
					new AuthorityDateOfLastTransactionTag().getHeaderType(),
					CorrelationValues.UNDEFINED,
					CorrelationValues.UNDEFINED));
		if (!item.getTags().contains(dateTag)) {
			item.addTag(dateTag);
		}

		Authority008Tag ffTag =
			(Authority008Tag) getNewTag(item,
				(short) 1,
				new Authority008Tag().getHeaderType(),
				CorrelationValues.UNDEFINED,
				CorrelationValues.UNDEFINED);
		if (!item.getTags().contains(ffTag)) {
			item.addTag(ffTag);
		}

		CataloguingSourceTag source =
			(CataloguingSourceTag) getNewTag(item,
				(short) 1,
				new CorrelationValues(
					new AuthorityCataloguingSourceTag().getHeaderType(),
					CorrelationValues.UNDEFINED,
					CorrelationValues.UNDEFINED));
		if (!item.getTags().contains(source)) {
			item.addTag(source);
		}

		item.sortTags();
	}

	/* (non-Javadoc)
	 * @see Catalog#getCatalogDao()
	 */
	public CatalogDAO getCatalogDao() {
		return daoCatalog;
	}

	/* (non-Javadoc)
	 * @see Catalog#getFixedFieldFactory()
	 */
	public AbstractMapBackedFactory getFixedFieldFactory() {
		return fixedFieldFactory;
	}

	/* (non-Javadoc)
	 * @see Catalog#getTagCategories(java.util.Locale)
	 */
	public List getTagCategories(Locale l) throws DataAccessException {
		return DAO_CODE_TABLE.getOptionList(T_AUT_TAG_CAT.class, l);
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
		
		if (correlationValues != null) {
			if (tag.correlationChangeAffectsKey(correlationValues)) {
				if (tag instanceof HeaderField) {
					tag =
						(Tag) getFixedFieldFactory().create(
							correlationValues.getValue(1));
				} else if (tag instanceof AuthorityHeadingTag  ||
						   tag instanceof AuthorityReferenceTag) {
					/*
					 * If we get here we are either getting a new reference tag
					 * based on the category of the target heading (which will start
					 * out as an AuthorityHeadingTag from the factory), or based on
					 * a category 16 (generic reference) with correlation values set to 
					 * a specific reference type (at time of writing this last 
					 * circumstance does not occur.)
					 * 
					 * In both cases we want to get the reference type from the 
					 * correlation values and create the appropriate tag Class.
					 */
					Integer refTypePosition =
						correlationValues.getLastUsedPosition();
					short refType;

					if (refTypePosition != null) {
						refType =
							correlationValues.getValue(
								refTypePosition.intValue());
					} else {
						throw new RuntimeException("attempt to create reference tag with no reference type specified");
					}
					if (ReferenceType.isSeenFrom(refType)) {
						tag = new SeeReferenceTag();
					} else if (ReferenceType.isSeeAlsoFrom(refType)) {
						tag = new SeeAlsoReferenceTag();
					} else if (ReferenceType.isEquivalence(refType)) {
						tag = new EquivalenceReference();
					} else {
						throw new RuntimeException(
								"invalid reference type for authority reference tag"
								);
					}

				}
			}
		}
		if (tag instanceof AuthorityReferenceTag) {
			// set the target descriptor and reference to be compatible with the 
			// given category
			String headingType = ((AUT) item.getItemEntity()).getHeadingType();
			Class sourceHeadingClazz =
				getDaoByType(headingType).getPersistentClass();

			/* if we were called with category == 16 (a generic Reference) then set
			 * the reference to be an interfile type based on the heading type, otherwise
			 * the value of category will correspond to the desired target descriptor
			 * type and we should create an appropriate reference class for this heading->
			 * target combination (e.g. NME_NME_TTL_REF)
			 */
			Class targetHeadingClazz;
			if (getAutTypeByDescriptorType(category) != null) {
				targetHeadingClazz =
					getDaoByType(getAutTypeByDescriptorType(category))
						.getPersistentClass();
			} else {
				targetHeadingClazz = sourceHeadingClazz;
			}

			try {
				Descriptor sourceDescriptor =
					(Descriptor) sourceHeadingClazz.newInstance();
				Descriptor targetDescriptor =
					(Descriptor) targetHeadingClazz.newInstance();
				REF r =
					REF.newInstance(
						sourceDescriptor,
						targetDescriptor,
						ReferenceType.SEEN_FROM,
						CATALOGUING_VIEW,false);
				((AuthorityReferenceTag) tag).setReference(r);
				((AuthorityReferenceTag) tag).setTargetDescriptor(
					targetDescriptor);
			} catch (InstantiationException e) {
				throw new RuntimeException("Could not create object");
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Could not create object");
			}
		} else if (tag instanceof AuthorityHeadingTag) {
			// change the heading type to correspond with the heading
			AUT autItm = (AUT) item.getItemEntity();
			autItm.setHeadingType(getAutTypeByDescriptorType(category));
		}
		if (correlationValues != null) {
			tag.setCorrelationValues(correlationValues);
		}
		tag = (Tag) setItemIfNecessary(item, tag);
		tag.markNew();
		tag.setTagImpl(item.getTagImpl());
		return tag;
	}
	/* (non-Javadoc)
	 * @see Catalog#getNewItem()
	 */
	protected CatalogItem getNewItem() {
		return new AuthorityItem();
	}

	/* (non-Javadoc)
	 * @see Catalog#getNewItemEntity()
	 */
	protected ItemEntity getNewItemEntity() {
		return new AUT();
	}

	/* (non-Javadoc)
	 * @see Catalog#applyKeyToItem(CatalogItem, java.lang.Object[])
	 */
	public CatalogItem applyKeyToItem(CatalogItem item, Object[] key) {
		// no key elements to apply for Authorities
		return item;
	}

	/* (non-Javadoc)
	 * @see Catalog#getModelDAO()
	 */
	public ModelDAO getModelDAO() {
		return modelDAO;
	}

	/* (non-Javadoc)
	 * @see Catalog#newModel(CatalogItem)
	 */
	public Model newModel(CatalogItem item) {
		return new AuthorityModel(item);
	}

	/* (non-Javadoc)
	 * @see Catalog#addOneTag(CatalogItem)
	 */
	public void addDefaultTag(CatalogItem item) {
		try {
			item.addTag(getNewTag(item, (short) 2));
		} catch (NewTagException e) {
			throw new RuntimeException("error creating new Authority heading tag");
		}
	}

	/* (non-Javadoc)
	 * @see Catalog#getMarcTypeCode()
	 */
	public String getMarcTypeCode() {
		return "A";
	}

	/* (non-Javadoc)
	 * @see Catalog#changeDescriptorType(CatalogItem, int, short)
	 */
	@Override
	public void changeDescriptorType(
		CatalogItem item,
		int index,
		short descriptorType) {
		Tag t = item.getTag(index);
		if (t instanceof AuthorityReferenceTag) {
			changeReferenceDescriptorType(
				t,
				item.getItemEntity(),
				descriptorType);
		} else if (t instanceof AuthorityHeadingTag) {
			String authorityType = getAutTypeByDescriptorType(descriptorType);
			Tag newTag;
			try {
				newTag = getNewTag(item, descriptorType);
			} catch (NewTagException e) {
				throw new RuntimeException("error creating new Authority heading tag");
			}
			((AUT) item.getItemEntity()).setHeadingType(authorityType);
			item.getTags().set(index, newTag);
		}
	}

	public void changeReferenceDescriptorType(
		Tag t,
		ItemEntity itemEntity,
		short descriptorType) {
		AuthorityReferenceTag tag = (AuthorityReferenceTag) t;
		Descriptor d = DescriptorFactory.createDescriptor(descriptorType);
		String headingType = ((AUT) itemEntity).getHeadingType();
		Class headingClazz = getDaoByType(headingType).getPersistentClass();
		REF r = null;
		try {
			/*
			 * When we change the heading type, we also can change the class of
			 * the associated reference.  So, create a new REF of the correct type
			 * and compare to the current one.  Replace if required.
			 */
			Descriptor headingInst = (Descriptor) headingClazz.newInstance();
			r =
				REF.newInstance(
					headingInst,
					d,
					tag.getReference().getType(),
					AuthorityCatalog.CATALOGUING_VIEW,false);
		} catch (Exception e) {
			throw new RuntimeException("unable to create reference object");
		}
		if (!(r.getClass() == tag.getReference().getClass())) {

			r.setSource(tag.getReference().getSource());
			r.setUserViewString(tag.getUserViewString());
			tag.setReference(r);
		}
		tag.setTargetDescriptor(d);
	}

	/* (non-Javadoc)
	 * @see Catalog#getValidHeadingTypeList(Tag)
	 */
	public List getValidHeadingTypeList(Tag t, Locale locale)
		throws DataAccessException {
		ItemEntity e = ((PersistsViaItem) t).getItemEntity();
		String headingType = ((AUT) e).getHeadingType();
		return daoCatalog.getValidHeadingTypeList(headingType, locale);
	}

	public void addDefaultTags(CatalogItem item) {
		// PR0046: 4 manadatory bibliographic tags
	}

	public void addRequiredTagsForModel(CatalogItem item) throws NewTagException {
		// PR0046: 4 manadatory bibliographic tags
	}
	public String getLockingEntityType() {
		return "AA";
	}

	public CatalogItem createAuthorityFromHeading(Descriptor d,
			Integer modelId) throws DataAccessException, NewTagException {
		Model model = getModelDAO().load(modelId.intValue());
		AuthorityItem item = (AuthorityItem)model.toCatalogItem(CATALOGUING_VIEW);
		AuthorityHeadingTag t = null;
		try {
			t = (AuthorityHeadingTag) getNewTag(item,
					((AccessPoint) d.getAccessPointClass().newInstance()).getCategory(), d.getCorrelationValues());
			t.setDescriptor(d);
			changeHeadingTag(t, item);
		} catch (Exception e) {
			logger.info("Couldn't create an AuthorityHeadingTag");
		}
		return item;
	}
	/**
	 * Adjusts the reference tags in the item whenever a new heading tag
	 * is saved
	 * @param t
	 * @param item
	 * @throws DataAccessException 
	 */
	public void changeHeadingTag(AuthorityHeadingTag t, AuthorityItem item) throws DataAccessException {
		Iterator iter = item.getTags().iterator();
		while (iter.hasNext()) {
			Tag t1 = (Tag)iter.next();
			if (t1 instanceof AuthorityHeadingTag ||
				t1 instanceof AuthorityReferenceTag) {
				iter.remove();
			}
		}
		
		item.addTag(t);
		item.addAllTags((Tag[])daoCatalog.getReferenceFields(item, t).toArray(new Tag[0]));
		for (int i=0; i < item.getTags().size(); i++) {
			Tag t1 = (Tag)item.getTags().get(i);
			t1.setTagImpl(item.getTagImpl());
			t1 = (Tag)setItemIfNecessary(item, t1);
		}
		
	}
}
