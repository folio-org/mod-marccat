package org.folio.cataloging.business.librivision;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.folio.cataloging.log.Log;
import org.w3c.dom.Document;

import org.folio.cataloging.util.XmlUtils;
       
/**
 * This is an abstract class for database records. It implements the Record
 * interface.
 * 
 * @author Wim Crols
 * @since 1.0
 */
public abstract class AbstractRecord implements Record {

    @JsonIgnore
	private String cclQuery = "";

	private static final String XSLT_PATH = "/xslt/";

	private static final Log logger = new Log(AbstractRecord.class);

	private Map<String, Object> content = new HashMap<>();

	private int recordView;

    /**
     * Returns true if
     *
     * @param elementSetName
     * @return
     */
	public boolean hasContent(final String elementSetName) {
		return content.containsKey(elementSetName);
	}

	public void setContent(String elementSetName, Object contentObject) {
		if ((elementSetName != null) && (contentObject != null)) {
			content.put(elementSetName, contentObject);
		}
	}

	public Object getContent(final String elementSetName) {
		return content.get(elementSetName);
	}

	public String toXmlString(String elementSetName) {
		if (this.hasContent(elementSetName)) {
			return XmlUtils.documentToString(toXmlDocument(elementSetName));
		} else {
			return null;
		}
	}

	public Document toXmlStyledDocument(
	        final String elementSetName,
			final String stylesheet,
            final Map xsltParameters) throws XmlParserConfigurationException,
			XslTransformerConfigurationException, XslTransformerException {
		URL styleURL = AbstractRecord.class.getResource(XSLT_PATH + stylesheet);
		if(styleURL==null) throw new XmlParserConfigurationException("stylesheet "+stylesheet+" not found in "+XSLT_PATH);
		return toXmlStyledDocument(elementSetName, styleURL, xsltParameters);
	}

	public Document toXmlStyledDocument(String elementSetName, URL stylesheet,
			Map xsltParameters) throws XmlParserConfigurationException,
			XslTransformerConfigurationException, XslTransformerException {
		Document xmlStyledDocument = null;

		try {
			// load the transformer using JAXP
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(
					stylesheet.getFile()));

			// First set some parameters if there are any
			if (xsltParameters != null) {
				Iterator iterator = xsltParameters.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry entry = (Map.Entry) iterator.next();
					transformer.setParameter(entry.getKey().toString(), entry
							.getValue().toString());
				}
			}

			// now lets style the given document
			DOMSource source = new DOMSource(this
					.toXmlDocument(elementSetName));
			DocumentBuilderFactory documentBuilderFactory =
				DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = null;
			try {
				documentBuilder = documentBuilderFactory.newDocumentBuilder();
				xmlStyledDocument = documentBuilder.newDocument();
				//<?xml version=\"1.0\" encoding=\"UTF-8\"?>
				DOMResult result = new DOMResult(xmlStyledDocument);
				transformer.transform(source, result);
			} catch (ParserConfigurationException parserConfigurationException) {
				logger.error("", parserConfigurationException);
				throw new XmlParserConfigurationException(parserConfigurationException);
			}
		} catch (TransformerConfigurationException transformerConfigurationException) {
			logger.error(transformerConfigurationException.getMessage());
			throw new XslTransformerConfigurationException(
					transformerConfigurationException);
		} catch (TransformerException transformerException) {
			logger.error(transformerException.getMessage());
			throw new XslTransformerException(transformerException);
		}

		return xmlStyledDocument;
	}

	public String toStyledDocument(String elementSetName, String stylesheet,
			Map xsltParameters) throws XmlDocumentException,
			XslTransformerConfigurationException, XslTransformerException {
		// MIKE: helpful in configuration time
		URL styleURL = AbstractRecord.class.getResource(XSLT_PATH + stylesheet);
		if(styleURL==null) throw new XmlDocumentException("stylesheet "+stylesheet+" not found in "+XSLT_PATH);
		return toStyledDocument(elementSetName, styleURL, xsltParameters);
	}

	public String toStyledDocument(String elementSetName, URL stylesheet,
			Map xsltParameters) throws XmlDocumentException,
			XslTransformerConfigurationException, XslTransformerException {
		String styledDocument = new String();
		try {
			// load the transformer using JAXP
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(
					stylesheet.openStream()));

			// First set some parameters if there are any
			if (xsltParameters != null) {
				Iterator iterator = xsltParameters.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry entry = (Map.Entry) iterator.next();
//CAMMILLETTI inizio
//					transformer.setParameter(entry.getKey().toString(), entry.getValue().toString());
					transformer.setParameter(entry.getKey().toString(), entry.getValue());
//CAMMILLETTI fine
				}
			}

			// now lets style the given document
			DOMSource source = new DOMSource(this
					.toXmlDocument(elementSetName));
			StringWriter buffer = new StringWriter();
		    StreamResult result = new StreamResult(buffer);
			transformer.transform(source, result);

			// return the transformed document
			styledDocument = buffer.toString();
		} catch (TransformerConfigurationException transformerConfigurationException) {
			logger.error(transformerConfigurationException.getMessage());
			throw new XslTransformerConfigurationException(
					transformerConfigurationException);
		} catch (TransformerException transformerException) {
			logger.error(transformerException.getMessage());
			throw new XslTransformerException(transformerException);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return styledDocument;
	}

	public String getCclQuery() {
		return cclQuery;
	}

	public void setCclQuery(String cclQuery) {
		this.cclQuery = cclQuery;
	}
	
	/**
	 * pm 2011
	 * @return the recordView
	 */
	public int getRecordView() {
		return recordView;
	}

	/**
	 * pm 2011
	 * @param recordView the recordView to set
	 */
	public void setRecordView(int recordView) {
		this.recordView = recordView;
	}

}