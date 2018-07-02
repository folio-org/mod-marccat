/*
 * (c) LibriCore
 * 
 * Created on Jun 7, 2004
 * 
 * Record.java
 */
package org.folio.cataloging.business.librivision;

import java.net.URL;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.w3c.dom.Document;

/**
 * This is an interface for database records.
 * 
 * @author Wim Crols
 * @version $Revision: 1.10 $, $Date: 2004/12/02 08:44:16 $
 * @since 1.0
 */
public interface Record {
	String getCclQuery();
	void setCclQuery(String cclQuery);

	int getRecordView(); //pm 2011
	void setRecordView(int recordView); //pm 2011
	
	boolean hasContent(String elementSetName);

	void setContent(String elementSetName, Object contentObject);

	Object getContent(String elementSetName);

	String toXmlString(String elementSetName);

	Document toXmlDocument(String elementSetName);

	Document toXmlStyledDocument(String elementSetName,
                                        String stylesheet, Map xsltParameters) throws XmlParserConfigurationException,
			XslTransformerConfigurationException, XslTransformerException;

	Document toXmlStyledDocument(String elementSetName, URL stylesheet,
                                        Map xsltParameters) throws XmlParserConfigurationException,
			XslTransformerConfigurationException, XslTransformerException;

	String toStyledDocument(String elementSetName, String stylesheet,
                                   Map xsltParameters) throws XmlDocumentException,
			XslTransformerConfigurationException, XslTransformerException;

	String toStyledDocument(String elementSetName, URL stylesheet,
                                   Map xsltParameters) throws XmlDocumentException,
			XslTransformerConfigurationException, XslTransformerException;

}

