package org.folio.cataloging.business.librivision;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
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

	private static final Log logger = new Log(XmlRecord.class);

	private final static ThreadLocal<DocumentBuilderFactory> DOCUMENT_BUILDER_FACTORIES =
            ThreadLocal.withInitial(DocumentBuilderFactory::newInstance);

	private Document data;

	public Document getData() {
	    return data;
    }

	public void setContent(String elementSetName, Document xmlDocument) {
		this.data = xmlDocument;
	    if ((elementSetName != null) && (xmlDocument != null)) {
			super.setContent(elementSetName, xmlDocument);
		}
	}

	public void setContent(final String elementSetName, String xmlString) throws XmlUnsupportedEncodingException, XmlParserConfigurationException {
		if ((elementSetName != null) && (xmlString != null)) {

			try (ByteArrayInputStream byteArrayInputStream =
                         new ByteArrayInputStream(xmlString.getBytes("UTF-8"))){
				    final DocumentBuilder documentBuilder = DOCUMENT_BUILDER_FACTORIES.get().newDocumentBuilder();
                    setContent(elementSetName, documentBuilder.parse(byteArrayInputStream));
            } catch (SAXException | IOException exception) {
                logger.error(MessageCatalog._00021_UNABLE_TO_PARSE_RECORD_DATA, exception);
                final Document xmlDocument = DOCUMENT_BUILDER_FACTORIES.get().newDocumentBuilder();
                final Element recordElement = xmlDocument.createElement("record");
                final Element errorElement = xmlDocument.createElement("error");

                final Node errorTextNode = xmlDocument.createTextNode(toXmlString(elementSetName));
                xmlDocument.appendChild(recordElement);

                recordElement.appendChild(errorElement);
                errorElement.appendChild(errorTextNode);

		    } catch (ParserConfigurationException | IOException exception) {
				logger.error(MessageCatalog._00021_UNABLE_TO_PARSE_RECORD_DATA, exception);
				throw new XmlParserConfigurationException(exception);
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