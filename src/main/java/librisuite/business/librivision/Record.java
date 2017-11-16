/*
 * (c) LibriCore
 * 
 * Created on Jun 7, 2004
 * 
 * Record.java
 */
package librisuite.business.librivision;

import java.net.URL;
import java.util.Map;

import org.w3c.dom.Document;

/**
 * This is an interface for database records.
 * 
 * @author Wim Crols
 * @version $Revision: 1.10 $, $Date: 2004/12/02 08:44:16 $
 * @since 1.0
 */
public interface Record {
	
	String cclQuery = "";
	public String getCclQuery();
	public void setCclQuery(String cclQuery);

	public int getRecordView(); //pm 2011
	public void setRecordView(int recordView); //pm 2011
	
	public boolean hasContent(String elementSetName);

	public void setContent(String elementSetName, Object contentObject);

	public Object getContent(String elementSetName);

	public String toXmlString(String elementSetName);

	public Document toXmlDocument(String elementSetName);

	public Document toXmlStyledDocument(String elementSetName,
                                        String stylesheet, Map xsltParameters) throws XmlParserConfigurationException,
			XslTransformerConfigurationException, XslTransformerException;

	public Document toXmlStyledDocument(String elementSetName, URL stylesheet,
                                        Map xsltParameters) throws XmlParserConfigurationException,
			XslTransformerConfigurationException, XslTransformerException;

	public String toStyledDocument(String elementSetName, String stylesheet,
                                   Map xsltParameters) throws XmlDocumentException,
			XslTransformerConfigurationException, XslTransformerException;

	public String toStyledDocument(String elementSetName, URL stylesheet,
                                   Map xsltParameters) throws XmlDocumentException,
			XslTransformerConfigurationException, XslTransformerException;

}

