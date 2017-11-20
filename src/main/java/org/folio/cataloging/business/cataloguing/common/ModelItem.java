/*
 * (c) LibriCore
 * 
 * Created on Nov 8, 2005
 * 
 * ModelItem.java
 */
package org.folio.cataloging.business.cataloguing.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.folio.cataloging.util.XmlUtils;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public abstract class ModelItem implements Persistence, Serializable {
	private PersistenceState persistenceState = new PersistenceState();

	private static final Log logger = LogFactory.getLog(ModelItem.class);

	private long item = 0;

	private Model model = null;

	private Document xmlFieldsDocument = null;

	abstract public Catalog getCatalog();

	public void generateNewKey() throws DataAccessException {
	}

	public void addUnusedTag(int unusedTagIndex, CatalogItem catalogItem) {
		int tagIndex = convertUnusedTagIndexToTagIndex(unusedTagIndex);
		Document xmlDocument = getXmlFieldsDocument();
		Element field =
			(Element) xmlDocument.getDocumentElement().getElementsByTagName(
				"field").item(
				tagIndex);
		//TODO this field was null when model has no fields		
		if (field != null) {
			field.appendChild(xmlDocument.createElement("added"));
			getCatalog().parseModelXmlElementAddToItem(field, catalogItem);
		}
	}

	public void addMandatoryTag(
		int mandatoryTagIndex,
		CatalogItem catalogItem) {
		int tagIndex = convertMandatoryTagIndexToTagIndex(mandatoryTagIndex);
		Document xmlDocument = getXmlFieldsDocument();
		Element field =
			(Element) xmlDocument.getDocumentElement().getElementsByTagName(
				"field").item(
				tagIndex);
		field.appendChild(xmlDocument.createElement("added"));
		getCatalog().parseModelXmlElementAddToItem(field, catalogItem);
	}

	public void addOptionalTag(int optionalTagIndex, CatalogItem catalogItem) {
		int tagIndex = convertOptionalTagIndexToTagIndex(optionalTagIndex);
		Document xmlDocument = getXmlFieldsDocument();
		Element field =
			(Element) xmlDocument.getDocumentElement().getElementsByTagName(
				"field").item(
				tagIndex);
		field.appendChild(xmlDocument.createElement("added"));
		getCatalog().parseModelXmlElementAddToItem(field, catalogItem);
	}

	protected int convertUnusedTagIndexToTagIndex(int unusedTagIndex) {
		Document xmlDocument = getXmlFieldsDocument();
		NodeList fields =
			xmlDocument.getDocumentElement().getElementsByTagName("field");
		int unusedIndex = 0;
		for (int abc = 0; abc < fields.getLength(); abc++) {
			if (((Element) fields.item(abc))
				.getElementsByTagName("added")
				.getLength()
				== 0) {
				if (unusedTagIndex == unusedIndex) {
					return abc;
				}
				unusedIndex++;
			}
		}
		return 0;
	}

	protected int convertMandatoryTagIndexToTagIndex(int mandatoryTagIndex) {
		Document xmlDocument = getXmlFieldsDocument();
		NodeList fields =
			xmlDocument.getDocumentElement().getElementsByTagName("field");
		int mandatoryIndex = 0;
		for (int abc = 0; abc < fields.getLength(); abc++) {
			if (((Element) fields.item(abc))
				.getElementsByTagName("optional")
				.getLength()
				== 0) {
				if (mandatoryTagIndex == mandatoryIndex) {
					return abc;
				}
				mandatoryIndex++;
			}
		}
		return 0;
	}

	protected int convertOptionalTagIndexToTagIndex(int optionalTagIndex) {
		Document xmlDocument = getXmlFieldsDocument();
		NodeList fields =
			xmlDocument.getDocumentElement().getElementsByTagName("field");
		int optionalIndex = 0;
		for (int abc = 0; abc < fields.getLength(); abc++) {
			if (((Element) fields.item(abc))
				.getElementsByTagName("optional")
				.getLength()
				!= 0) {
				if (optionalTagIndex == optionalIndex) {
					return abc;
				}
				optionalIndex++;
			}
		}
		return 0;
	}

	public Element getUnusedTagXmlFieldForAdding(int unusedTagIndex) {
		int tagIndex = convertUnusedTagIndexToTagIndex(unusedTagIndex);
		Document xmlDocument = getXmlFieldsDocument();
		Element field =
			(Element) xmlDocument.getDocumentElement().getElementsByTagName(
				"field").item(
				tagIndex);
		field.appendChild(xmlDocument.createElement("added"));
		markChanged();
		//String s1 = XmlUtils.documentToString(xmlDocument);
		return field;
	}

	public List getUnusedModelTags() {
		List tags = new ArrayList();
		Document xmlDocument = getXmlFieldsDocument();
		NodeList fields =
			xmlDocument.getDocumentElement().getElementsByTagName("field");
		for (int abc = 0; abc < fields.getLength(); abc++) {
			try{
			if (((Element) fields.item(abc))
				.getElementsByTagName("added")
				.getLength()
				== 0) {
				tags.add(getModel().getTags().get(abc));
			}
		}catch (Exception e) {
			continue;
		}
		}
		return tags;
	}

	public List getMandatoryTags() {
		List tags = new ArrayList();
		Document xmlDocument = getXmlFieldsDocument();
		NodeList fields =
			xmlDocument.getDocumentElement().getElementsByTagName("field");
		for (int abc = 0; abc < fields.getLength(); abc++) {
		
			if (((Element) fields.item(abc))
				.getElementsByTagName("optional")
				.getLength()
				== 0) {
				tags.add(getModel().getTags().get(abc));
			}
		
		}
		
		
		return tags;
	}

	public List getOptionalTags() {
		List tags = new ArrayList();
		Document xmlDocument = getXmlFieldsDocument();
		NodeList fields =
			xmlDocument.getDocumentElement().getElementsByTagName("field");
		for (int abc = 0; abc < fields.getLength(); abc++) {
			if (((Element) fields.item(abc))
				.getElementsByTagName("optional")
				.getLength()
				!= 0) {
				tags.add(getModel().getTags().get(abc));
			}
		}
		return tags;
	}

	/**
		 * @since 1.0
		 */
	public List getOptional() 
	{
		List optional = new ArrayList();
		Document xmlDocument = getXmlFieldsDocument();
		NodeList fields = xmlDocument.getDocumentElement().getElementsByTagName("field");
		for (int abc = 0; abc < fields.getLength(); abc++) 
		{
			if (((Element) fields.item(abc)).getElementsByTagName("added").getLength()== 0) {
				if (((Element) fields.item(abc)).getElementsByTagName("optional").getLength() != 0) {
					optional.add(new Boolean(true));
				} else {
					optional.add(new Boolean(false));
				}
			}
		}
		return optional;
	}

	/**
		 * @since 1.0
		 */
	public long getItem() {
		return item;
	}

	/**
		 * @since 1.0
		 */
	public void setItem(long id) {
		this.item = id;
	}

	/**
		 * @since 1.0
		 */
	public Model getModel() {
		return model;
	}

	/**
		 * @since 1.0
		 */
	public void setModel(Model model) {
		this.model = model;
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
	public void setXmlFieldsDocument(Document xmlFieldsDocument) {
		this.xmlFieldsDocument = xmlFieldsDocument;
	}

	/**
		 * 
		 * @since 1.0
		 */
	public String getXmlFields() {
		if (getXmlFieldsDocument() == null) {
			return null;
		} else {
			logger.debug("Getting xmlFields for item " + getItem());
			String result = XmlUtils.documentToString(getXmlFieldsDocument());
			return result;
		}
	}

	/**
		 * 
		 * @since 1.0
		 */
	public void setXmlFields(String xmlFields) {
		DocumentBuilderFactory documentBuilderFactory =
			DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = null;
		Document xmlDocument = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			xmlDocument =
				documentBuilder.parse(
					new ByteArrayInputStream(xmlFields.getBytes("UTF-8")));
		} catch (ParserConfigurationException parserConfigurationException) {
			logger.error("", parserConfigurationException);
		} catch (SAXException saxException) {
			logger.error("", saxException);
		} catch (IOException ioException) {
			logger.error("", ioException);
		}
		setXmlFieldsDocument(xmlDocument);
	}

	/**
		 * 
		 * @since 1.0
		 */
	public void evict(Object obj) throws DataAccessException {
		persistenceState.evict(obj);
	}

	public void evict() throws DataAccessException {
		persistenceState.evict(this);
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
		 * 
		 * @since 1.0
		 */
	public void setUpdateStatus(int i) {
		persistenceState.setUpdateStatus(i);
	}

}
