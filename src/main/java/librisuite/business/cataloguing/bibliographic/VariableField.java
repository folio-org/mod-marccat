/*
 * (c) LibriCore
 * 
 * Created on Sep 29, 2004
 * 
 * VariableField.java
 */
package librisuite.business.cataloguing.bibliographic;

import java.util.List;

import librisuite.business.cataloguing.common.Tag;
import librisuite.business.common.DataAccessException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.libricore.librisuite.common.StringText;

/**
 * @author paulm
 * @version $Revision: 1.9 $, $Date: 2005/12/21 08:30:32 $
 * @since 1.0
 */
public abstract class VariableField extends Tag  {

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public VariableField() {
		super();
	}

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public VariableField(int itemNumber) {
		super(itemNumber);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#isBrowsable()
	 */
	public boolean isBrowsable() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getSecondCorrelationList(short)
	 */
	public List getSecondCorrelationList(short value1)
		throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getThirdCorrelationList(short, short)
	 */
	public List getThirdCorrelationList(short value1, short value2)
		throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	public abstract StringText getStringText();

	public abstract void setStringText(StringText stringText);

	
	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#isAbleToBeDeleted()
	 */
	public boolean isAbleToBeDeleted() {
		return true; //default implementation
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#isEditableHeader()
	 */
	public boolean isEditableHeader() {
		return false; //default implementation
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#isFixedField()
	 */
	final public boolean isFixedField() {
		return false;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#isHeaderField()
	 */
	public boolean isHeaderField() {
		return false; //default implementation
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#isWorksheetEditable()
	 */
	public boolean isWorksheetEditable() {
		return true;
	}

	public Element generateModelXmlElementContent(Document xmlDocument) {
		Element content = null;
		if (xmlDocument != null) {
			content = getStringText().generateModelXmlElementContent(xmlDocument);
		}
		return content;
	}

	public void parseModelXmlElementContent(Element xmlElement) {
		setStringText(StringText.parseModelXmlElementContent(xmlElement));
	}
	
	public boolean isEmpty(){
		return getStringText()==null || getStringText().isEmpty();
	}

}
