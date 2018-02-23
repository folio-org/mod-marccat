/*
 * (c) LibriCore
 * 
 * Created on Nov 8, 2005
 * 
 * Model.java
 */
package org.folio.cataloging.business.cataloguing.common;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.cataloguing.bibliographic.BibliographicModel;
import org.folio.cataloging.business.cataloguing.bibliographic.NewTagException;
import org.folio.cataloging.business.cataloguing.bibliographic.VariableField;
import org.folio.cataloging.business.common.CorrelationValues;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.dao.ModelDAO;
import org.folio.cataloging.util.StringText;
import org.folio.cataloging.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Base class for template models for catalogItems
 * 
 * @author paulm
 * @version $Revision: 1.8 $, $Date: 2007/03/07 13:57:36 $
 * @since 1.0
 */
public abstract class Model implements Persistence, Serializable {

	protected static final Log logger = LogFactory
			.getLog(BibliographicModel.class);

	protected CatalogItem catalogItem = null;

	protected int id = 0;

	protected String label = "";

	protected List optional = new ArrayList();

	protected PersistenceState persistenceState = new PersistenceState();

	protected Document xmlFieldsDocument = null;
	
	private Integer wemiFirstGroup;

	public Model() {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			xmlFieldsDocument = documentBuilder.newDocument();
		} catch (ParserConfigurationException parserConfigurationException) {
			logger.error("", parserConfigurationException);
		}
		if (xmlFieldsDocument != null) {
			Element record = xmlFieldsDocument.createElement("record");
			xmlFieldsDocument.appendChild(record);
		}
		populateModel();
	}

	public Model(CatalogItem catalogItem) {
		this();
		List tags = catalogItem.getTags();
		Iterator tagsIterator = tags.iterator();
		while (tagsIterator.hasNext()) {
			Tag tag = (Tag) tagsIterator.next();
			addTag(tag);
		}
	}

	abstract public Catalog getCatalog();

	public void addTag(int tagIndex, Tag tag) throws NewTagException {
		Tag t = getCatalog().getNewTag(getCatalogItem(), tag.getCategory(),
				tag.getCorrelationValues());
		Document xmlDocument = getXmlFieldsDocument();
		Element record = xmlDocument.getDocumentElement();
		NodeList fields = record.getElementsByTagName("field");
		if (tagIndex < 0) {
			Element field = (Element) fields.item(0);
			record.insertBefore(t.generateModelXmlElement(xmlDocument), field);
		} else if (tagIndex > (fields.getLength() - 2)) {
			record.appendChild(t.generateModelXmlElement(xmlDocument));
		} else {
			Element field = (Element) fields.item(tagIndex + 1);
			record.insertBefore(t.generateModelXmlElement(xmlDocument), field);
		}
		if (tagIndex < 0) {
			if (t.isAbleToBeDeleted()) {
				getOptional().add(new Boolean(true));
				Element field = (Element) record.getElementsByTagName("field")
						.item(0);
				field.appendChild(xmlDocument.createElement("optional"));
			} else {
				getOptional().add(new Boolean(false));
			}

		} else if (tagIndex > (fields.getLength() - 2)) {
			if (t.isAbleToBeDeleted()) {
				getOptional().add(new Boolean(true));
				Element field = (Element) record.getElementsByTagName("field")
						.item(
								record.getElementsByTagName("field")
										.getLength() - 1);
				field.appendChild(xmlDocument.createElement("optional"));
			} else {
				getOptional().add(new Boolean(false));
			}
		} else {
			if (t.isAbleToBeDeleted()) {
				getOptional().add(new Boolean(true));
				Element field = (Element) record.getElementsByTagName("field")
						.item(tagIndex);
				field.appendChild(xmlDocument.createElement("optional"));
			} else {
				getOptional().add(new Boolean(false));
			}
		}
		catalogItem.addTag(tagIndex, t);
	}

	public void addTag(short categoryCode, short correlationValue1,
			short correlationValue2, short correlationValue3)
			throws NewTagException {
		addTag(getCatalog().getNewTag(getCatalogItem(), categoryCode,
				correlationValue1, correlationValue2, correlationValue3));
	}

	public void addTag(Tag tag) {
		Document xmlDocument = getXmlFieldsDocument();
		CatalogItem catalogItem = getCatalogItem();
		Element record = xmlDocument.getDocumentElement();
		record.appendChild(tag.generateModelXmlElement(xmlDocument));
		if (tag.isAbleToBeDeleted()) {
			getOptional().add(new Boolean(true));
			Element field = (Element) record.getElementsByTagName("field")
					.item(record.getElementsByTagName("field").getLength() - 1);
			field.appendChild(xmlDocument.createElement("optional"));
		} else {
			getOptional().add(new Boolean(false));
		}
		getCatalog().parseModelXmlElementAddToItem(
				tag.generateModelXmlElement(xmlDocument), catalogItem);
	}

	public void changeCategoryCode(int tagIndex, short categoryCode) {
		Tag newTag = null;
		try {
			newTag = getCatalog().getNewTag(catalogItem, categoryCode);
			// TODO check if we can copy stringtext
		} catch (NewTagException newTagException) {
		}
		refreshTag(tagIndex, newTag);
		getCatalogItem().getTags().set(tagIndex, newTag);
	}

	public void changeHeadingType(int tagIndex, short headingType) {
		getCatalog().changeDescriptorType(getCatalogItem(), tagIndex,
				headingType);
		Tag tag = getCatalogItem().getTag(tagIndex);
		refreshTag(tagIndex, tag);
	}

	public void changeCorrelationValues(int tagIndex,
			CorrelationValues newValues) {
		Tag tag = (Tag) getTags().get(tagIndex);
		Tag newTag = (Tag) tag.clone();
		if (newValues.isValueDefined(1)
				&& tag.getCorrelation(1) != newValues.getValue(1)) {
			try {
				newTag.updateFirstCorrelation(newValues.getValue(1));
			} catch (DataAccessException e) {
				newValues.change(2, (short) -1);
				newValues.change(3, (short) -1);
			}
		} else if (newValues.isValueDefined(2)
				&& tag.getCorrelation(2) != newValues.getValue(2)) {
			try {
				newTag.updateSecondCorrelation(newValues.getValue(2));
			} catch (DataAccessException e) {
				newValues.change(3, (short) -1);
			}
		} else if (newValues.isValueDefined(3)
				&& tag.getCorrelation(3) != newValues.getValue(3)) {
			newTag.setCorrelation(3, newValues.getValue(3));
		}
		newValues = newTag.getCorrelationValues();

		if (tag.correlationChangeAffectsKey(newValues)) {
			try {
				newTag = getCatalog().getNewTag(getCatalogItem(),
						tag.getCategory(), newValues);
				if (tag instanceof VariableField
						&& newTag instanceof VariableField) {
					((VariableField) newTag)
							.setStringText(((VariableField) tag)
									.getStringText());
				}
			} catch (NewTagException e) {
				throw new RuntimeException("Unable to instantiate new tag");
			}
			refreshTag(tagIndex, newTag);
			getCatalogItem().getTags().set(tagIndex, newTag);
		} else {
			tag.setCorrelationValues(newValues);
			refreshTag(tagIndex, tag);
		}
	}

	public void changeText(int tagIndex, StringText text) {
		VariableField t = (VariableField) getCatalogItem().getTag(tagIndex);
		t.setStringText(text);
		if (t instanceof Browsable) {
			((Browsable) t).setDescriptorStringText(text);
		}
		refreshTag(tagIndex, t);
	}

	public void deleteTag(Tag tag) {
		if (getCatalogItem().getTags().contains(tag)) {
			int tagIndex = getCatalogItem().getTags().indexOf(tag);
			Document xmlDocument = getXmlFieldsDocument();
			Element record = xmlDocument.getDocumentElement();
			Element field = (Element) record.getElementsByTagName("field")
					.item(tagIndex);
			record.removeChild(field);
			getCatalogItem().getTags().remove(tag);
			getOptional().remove(tagIndex);
		}
	}

	public void evict() throws DataAccessException {
		persistenceState.evict(this);
	}

	public void evict(Object obj) throws DataAccessException {
		persistenceState.evict(obj);
	}

	public void generateNewKey() throws DataAccessException {
	}

	/**
	 * @since 1.0
	 */
	protected CatalogItem getCatalogItem() {
		return catalogItem;
	}

	/**
	 * @since 1.0
	 */
	public int getId() {
		return id;
	}

	/**
	 * @since 1.0
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @since 1.0
	 */
	public List getOptional() {
		return optional;
	}

	public List getTags() {
		if (getCatalogItem() == null) {
			populateModel();
		}
		return getCatalogItem().getTags();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getXmlFields() {
		return XmlUtils.documentToString(getXmlFieldsDocument());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public Document getXmlFieldsDocument() {
		return this.xmlFieldsDocument;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isChanged() {
		return persistenceState.isChanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isDeleted() {
		return persistenceState.isDeleted();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isNew() {
		return persistenceState.isNew();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isRemoved() {
		return persistenceState.isRemoved();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markChanged() {
		persistenceState.markChanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markDeleted() {
		persistenceState.markDeleted();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markNew() {
		persistenceState.markNew();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markUnchanged() {
		persistenceState.markUnchanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onDelete(Session session) throws CallbackException {
		return persistenceState.onDelete(session);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void onLoad(Session session, Serializable serializable) {
		persistenceState.onLoad(session, serializable);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onSave(Session session) throws CallbackException {
		return persistenceState.onSave(session);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onUpdate(Session session) throws CallbackException {
		return persistenceState.onUpdate(session);
	}

	/**
	 * creates an associated CatalogItem and populates it with tags based on the
	 * current XML_FIELDS document
	 * 
	 * @since 1.0
	 */
	public void populateModel() {
		List isOptional = new ArrayList();
		CatalogItem catalogItem = null;
		catalogItem = newItemWithoutAmicusNumber();
		setCatalogItem(catalogItem);
		setOptional(isOptional);
		Document xmlDocument = getXmlFieldsDocument();
		NodeList fieldList = xmlDocument.getDocumentElement()
				.getElementsByTagName("field");
		for (int fieldIndex = 0; fieldIndex < fieldList.getLength(); fieldIndex++) {
			Element field = (Element) fieldList.item(fieldIndex);
			getCatalog().parseModelXmlElementAddToItem(field, catalogItem);
			if (field.getElementsByTagName("optional").getLength() > 0) {
				isOptional.add(new Boolean(true));
			} else {
				isOptional.add(new Boolean(false));
			}
		}
		
		sortTags();
	}
	
	public void sortTags() {
		Map oldOptionals = new HashMap();
		for (int i = 0; i < getOptional().size(); i++) {
			oldOptionals.put(catalogItem.getTags().get(i), getOptional().get(i));
		}
		catalogItem.sortTags();
		List newOptionals = new ArrayList();
		setOptional(newOptionals);
		for (int  i = 0; i < catalogItem.getTags().size(); i++) {
			newOptionals.add(oldOptionals.get(catalogItem.getTag(i)));
			refreshTag(i, catalogItem.getTag(i));
		}
	}

	public void refreshTag(int tagIndex, Tag tag) {
		Document xmlDocument = getXmlFieldsDocument();
		Element record = xmlDocument.getDocumentElement();
		Element oldField = (Element) record.getElementsByTagName("field").item(
				tagIndex);
		Element newField = tag.generateModelXmlElement(xmlDocument);
		if (((Boolean) getOptional().get(tagIndex)).booleanValue()) {
			newField.appendChild(xmlDocument.createElement("optional"));
		}
		record.replaceChild(newField, oldField);
	}

	/**
	 * @since 1.0
	 */
	protected void setCatalogItem(CatalogItem item) {
		catalogItem = item;
	}

	/**
	 * @since 1.0
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @since 1.0
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @since 1.0
	 */
	protected void setOptional(List list) {
		optional = list;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setUpdateStatus(int i) {
		persistenceState.setUpdateStatus(i);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setXmlFields(String xmlFields) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder = null;
		Document xmlDocument = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			xmlDocument = documentBuilder.parse(new ByteArrayInputStream(
					xmlFields.getBytes("UTF-8")));
		} catch (ParserConfigurationException parserConfigurationException) {
			logger.error("", parserConfigurationException);
		} catch (SAXException saxException) {
			logger.error("", saxException);
		} catch (IOException ioException) {
			logger.error("", ioException);
		}
		setXmlFieldsDocument(xmlDocument);
		// TODO see if lazy initialization of tags (in getTags) is sufficient
		// no it is not for now -- but should be possible
		populateModel();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setXmlFieldsDocument(Document xmlFieldsDocument) {
		this.xmlFieldsDocument = xmlFieldsDocument;
	}

	/**
	 * @since 1.0
	 */
	public void toggleOptional(int tagIndex) {
		Document xmlDocument = getXmlFieldsDocument();
		Element record = xmlDocument.getDocumentElement();
		Element field = (Element) record.getElementsByTagName("field").item(
				tagIndex);
		if (((Boolean) getOptional().get(tagIndex)).booleanValue()) {
			getOptional().set(tagIndex, new Boolean(false));
			Element optional = (Element) field.getElementsByTagName("optional")
					.item(0);
			field.removeChild(optional);
		} else {
			getOptional().set(tagIndex, new Boolean(true));
			field.appendChild(xmlDocument.createElement("optional"));
		}
	}

	public CatalogItem newItemWithoutAmicusNumber() {
		CatalogItem catalogItem = null;
		catalogItem = getCatalog().newCatalogItemWithoutAmicusNumber();
		return catalogItem;
	}

	/**
	 * @return true if this model is used by any bibliographic model items
	 * 
	 * @since 1.0
	 */
	/*public boolean isLinkedToItems() throws DataAccessException {
		return ((ModelDAO) getDAO()).getModelUsage(getId());
	}*/

	public CatalogItem toCatalogItem(int cataloguingView) {
		CatalogItem item = null;
		try {
			item = getCatalog().newCatalogItem(
					new Object[] { new Integer(cataloguingView) });
			item.setModelItem(this);
			List mandatories = item.getModelItem().getMandatoryTags();
			for (int i = 0; i < mandatories.size(); i++) {
				item.getModelItem().addMandatoryTag(i, item);
			}
			getCatalog().addRequiredTags(item);
		} catch (NewTagException newTagException) {
			logger.error("", newTagException);
			throw new RuntimeException();
		} catch (DataAccessException dataAccessException) {
			logger.error("", dataAccessException);
			throw new RuntimeException();
		}
		return item;
	}

	public Integer getWemiFirstGroup() {
		return wemiFirstGroup;
	}

	public void setWemiFirstGroup(Integer wemiFirstGroup) {
		this.wemiFirstGroup = wemiFirstGroup;
	}



}