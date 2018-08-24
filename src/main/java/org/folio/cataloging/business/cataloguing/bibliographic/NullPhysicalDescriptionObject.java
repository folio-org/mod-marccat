/*
 * (c) LibriCore
 * 
 * Created on Oct 15, 2004
 * 
 * Map.java
 */
package org.folio.cataloging.business.cataloguing.bibliographic;

import org.folio.cataloging.dao.persistence.PhysicalDescription;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public class NullPhysicalDescriptionObject extends PhysicalDescription {

	public Element generateModelXmlElementContent(Document xmlDocument) {
		// TODO Auto-generated method stub
		return null;
	}

	public void parseModelXmlElementContent(Element xmlElement) {
		// TODO Auto-generated method stub
		
	}

}
