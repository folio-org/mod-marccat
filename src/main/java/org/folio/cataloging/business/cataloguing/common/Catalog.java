/*
 * (c) LibriCore
 * 
 * Created on Nov 15, 2005
 * 
 * Catalog.java
 */
package org.folio.cataloging.business.cataloguing.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.cataloguing.bibliographic.BibliographicCatalog;
import org.folio.cataloging.business.cataloguing.bibliographic.NewTagException;
import org.folio.cataloging.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.cataloging.business.common.CorrelationValues;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.business.controller.UserProfile;
import org.folio.cataloging.dao.DAOCatalog;
import org.folio.cataloging.dao.DAOCodeTable;
import org.folio.cataloging.dao.DAOModel;
import org.folio.cataloging.exception.RecordInUseException;
import org.folio.cataloging.exception.ValidationException;
import org.w3c.dom.Element;

import java.util.*;

/**
 * @author paulm
 * @version $Revision: 1.8 $, $Date: 2006/12/14 10:31:07 $
 * @since 1.0
 */
public abstract class Catalog {

	private static final Log logger = LogFactory.getLog(Catalog.class);

	protected static DAOCodeTable daoCodeTable = new DAOCodeTable();

	private static Map viewToInstanceMap = new HashMap();

	public static Catalog getInstanceByView(int cataloguingView) {
		Catalog result = (Catalog)viewToInstanceMap.get(new Integer(cataloguingView));
		if (result == null) {
			Class clazz;
			try {
				clazz = Defaults.getClazz("catalog.implementation.view."
						+ cataloguingView);
			} catch (MissingResourceException e) {
				clazz = null;
			}
			if (clazz == null) {
				result = new BibliographicCatalog();
			}
			else {
				try {
					result = (Catalog)clazz.newInstance();
				} catch (InstantiationException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
			viewToInstanceMap.put(new Integer(cataloguingView), result);
		}
		return result;
	}

	abstract public Model newModel(CatalogItem item);

	abstract public DAOModel getModelDAO();

	/**
	 * gets a list of heading types that are applicable for the current
	 * authority tag's heading type
	 * 
	 * @since 1.0
	 */
	abstract public List getValidHeadingTypeList(Tag t, Locale locale)
			throws DataAccessException;

	/**
	 * change the heading type of an Authority Heading or Reference tag
	 * 
	 * @since 1.0
	 */
	abstract public void changeDescriptorType(CatalogItem item, int index,
			short descriptorType);

	/**
	 * A unique String representing the subclass type for use in jsp logic tags
	 * 
	 * @since 1.0
	 */
	abstract public String getMarcTypeCode();

	/**
	 * Called when a new Catalogue item requires at least one tag (new models)
	 * 
	 * @since 1.0
	 */
	abstract public void addDefaultTag(CatalogItem item);
	abstract public void addDefaultTags(CatalogItem item);

	abstract public Tag getNewTag(CatalogItem item, short category,
			CorrelationValues correlationValues) throws NewTagException;

	public CatalogItem getCatalogItem(Object[] key)
			throws DataAccessException {
		CatalogItem b = getCatalogDao().getCatalogItemByKey(key);
		/* verify Marc Correlations */
		for (int i = 0; i < b.getNumberOfTags(); i++) {
		try{
			b.getTag(i).getMarcEncoding();
		}catch (Exception e) {
			continue;
		}
		}
		b.sortTags();
		return b;
	}

	abstract public Tag getNewHeaderTag(CatalogItem item, short header)
			throws NewTagException;

	/**
	 * 
	 */
	abstract public DAOCatalog getCatalogDao();

	abstract public List getTagCategories(Locale l) throws DataAccessException;

	public final Tag getNewTag(CatalogItem item, short category)
			throws NewTagException {
		return getNewTag(item, category, null);
	}

	protected static Object setItemIfNecessary(CatalogItem item, Object o) {
		if (o instanceof PersistsViaItem) {
			((PersistsViaItem) o).setItemEntity(item.getItemEntity());
		}
		return o;
	}

	public final Tag getNewTag(CatalogItem item, short category,
			short correlationValue1, short correlationValue2,
			short correlationValue3) throws NewTagException {
		return getNewTag(item, category, new CorrelationValues(
				correlationValue1, correlationValue2,
				correlationValue3));
	}

	/**
	 * Ensures that after creating a new Item (usually from a model) that the
	 * item has at least the required mandatory tags
	 * 
	 * @since 1.0
	 */
	abstract public void addRequiredTags(CatalogItem item)
			throws NewTagException;

	/**
	 *modifica BARBARA 20/02/2007 PR0046 
	 *creo i 4 tag obbligatori e non lo 005
	 * 
	 * @since 1.0
	 */

	abstract public void addRequiredTagsForModel(CatalogItem item)
	throws NewTagException;

	public void deleteCatalogItem(CatalogItem item,UserProfile user) throws DataAccessException {
		getCatalogDao().deleteCatalogItem(item, user);
	}

	public void saveCatalogItem(CatalogItem item) throws DataAccessException,
			ValidationException {
		item.validate();
		getCatalogDao().saveCatalogItem(item);
	}

	/**
	 * This method is used to generate tags from a model
	 * 
	 * @since 1.0
	 */
	public Tag parseModelXmlElementAddToItem(Element xmlElement,
			CatalogItem item) {
		Tag tag = parseModelXmlElement(xmlElement, item);
		if (tag != null) {
			item.addTag(tag);
			setItemIfNecessary(item, tag);
		}
		return tag;
	}

	/**
	 * This method is used to generate tags from a model
	 * 
	 * @since 1.0
	 */
	public Tag parseModelXmlElement(Element xmlElement, CatalogItem item) {
		Tag tag = null;
		try {
			tag = getNewTag(item, Short.parseShort(xmlElement
					.getAttribute("categoryCode")), Short.parseShort(xmlElement
					.getAttribute("firstCorrelationValue")), Short
					.parseShort(xmlElement
							.getAttribute("secondCorrelationValue")), Short
					.parseShort(xmlElement
							.getAttribute("thirdCorrelationValue")));
			tag.parseModelXmlElementContent(xmlElement);
		} catch (NewTagException newTagException) {
			logger.error("", newTagException);
		}
		return tag;
	}

	abstract protected CatalogItem getNewItem();

	abstract protected ItemEntity getNewItemEntity();

	public CatalogItem newCatalogItemWithoutAmicusNumber() {
		CatalogItem item = getNewItem();
		ItemEntity itemEntity = getNewItemEntity();
		itemEntity.markNew();
		item.setItemEntity(itemEntity);
		return item;
	}

	public CatalogItem newCatalogItem(final Object[] key) throws DataAccessException {
		CatalogItem result = newCatalogItemWithoutAmicusNumber();
		result.getItemEntity().generateNewKey();
		return applyKeyToItem(result, key);
	}

	abstract public CatalogItem applyKeyToItem(CatalogItem item, Object[] key);

	public CatalogItem newCatalogItem(final Model model, final Object[] key)
			throws DataAccessException {
		CatalogItem item = newCatalogItem(key);
		item.setModelItem(model);
		return item;
	}
	public void lock(final int itemNumber, final String userName)
			throws DataAccessException, RecordInUseException {
		getCatalogDao().lock(itemNumber, getLockingEntityType(), userName);
	}

	public void unlock(int itemNumber) throws DataAccessException {
		getCatalogDao().unlock(itemNumber, getLockingEntityType());
	}

	abstract public String getLockingEntityType();

}
