/*
 * (c) LibriCore
 * 
 * Created on Jun 7, 2004
 * 
 * XmlRecord.java
 */
package librisuite.business.librivision;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * This is a XML record class.
 * 
 * @author Wim Crols
 * @version $Revision: 1.7 $, $Date: 2004/12/02 08:44:16 $
 * @since 1.0
 */
public class XmlRecord extends AbstractRecord {

	private static final Log logger = LogFactory.getLog(XmlRecord.class);

	public void setContent(String elementSetName, Document xmlDocument) {
		if ((elementSetName != null) && (xmlDocument != null)) {
			super.setContent(elementSetName, xmlDocument);
		}
	}

	public void setContent(String elementSetName, String xmlString)
		throws XmlUnsupportedEncodingException, XmlParserConfigurationException {
		if ((elementSetName != null) && (xmlString != null)) {
			DocumentBuilderFactory documentBuilderFactory =
				DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = null;
			Document xmlDocument = null;
			try {
				documentBuilder = documentBuilderFactory.newDocumentBuilder();
				try {
					ByteArrayInputStream byteArrayInputStream =
						new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
					try {
						xmlDocument =
							documentBuilder.parse(byteArrayInputStream);
					} catch (SAXException aSAXException) {
						logger.error("", aSAXException);
						xmlDocument = documentBuilder.newDocument();
						//<?xml version=\"1.0\" encoding=\"UTF-8\"?>
						Element recordElement =
							xmlDocument.createElement("record");
						Element errorElement =
							xmlDocument.createElement("error");
						Node errorTextNode =
							xmlDocument.createTextNode(
								this.toXmlString(elementSetName));
						xmlDocument.appendChild(recordElement);
						recordElement.appendChild(errorElement);
						errorElement.appendChild(errorTextNode);
					} catch (IOException aIOException) {
						logger.error("", aIOException);
						xmlDocument = documentBuilder.newDocument();
						//<?xml version=\"1.0\" encoding=\"UTF-8\"?>
						Element recordElement =
							xmlDocument.createElement("record");
						Element errorElement =
							xmlDocument.createElement("error");
						Node errorTextNode =
							xmlDocument.createTextNode(
								this.toXmlString(elementSetName));
						xmlDocument.appendChild(recordElement);
						recordElement.appendChild(errorElement);
						errorElement.appendChild(errorTextNode);
					}
					setContent(elementSetName, xmlDocument);
				} catch (UnsupportedEncodingException unsupportedEncodingException) {
					logger.error("", unsupportedEncodingException);
					throw new XmlUnsupportedEncodingException(unsupportedEncodingException);
				}
			} catch (ParserConfigurationException parserConfigurationException) {
				logger.error("", parserConfigurationException);
				throw new XmlParserConfigurationException(parserConfigurationException);
			}
		}
	}

	public Document toXmlDocument(String elementSetName) {
		if (hasContent(elementSetName)) {
			return (Document) getContent(elementSetName);
		} else {
			return null;
		}
	}

}