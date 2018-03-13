/*
 *
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
 * @since 1.0
 */
public abstract class ModelItem implements Persistence, Serializable {
	private PersistenceState persistenceState = new PersistenceState();

	private long item = 0;

	private Model model = null;

	private String recordFields = null;

	public String getRecordFields() {
		return recordFields;
	}

	public void setRecordFields(String recordFields) {
		this.recordFields = recordFields;
	}

	public void generateNewKey() throws DataAccessException {
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
/*	public Document getXmlFieldsDocument() {
		return this.xmlFieldsDocument;
	}*/

	/**
		 * 
		 * @since 1.0
		 */
	/*public void setXmlFieldsDocument(Document xmlFieldsDocument) {
		this.xmlFieldsDocument = xmlFieldsDocument;
	}*/

	/**
		 * 
		 * @since 1.0
		 */
	/*public String getXmlFields() {
		if (getXmlFieldsDocument() == null) {
			return null;
		} else {
			logger.debug("Getting xmlFields for item " + getItem());
			String result = XmlUtils.documentToString(getXmlFieldsDocument());
			return result;
		}
	}*/

	/**
		 * 
		 * @since 1.0
		 */
	/*public void setXmlFields(String xmlFields) {
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
*/
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
