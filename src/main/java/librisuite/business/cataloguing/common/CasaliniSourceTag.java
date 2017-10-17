/*
 * (c) LibriCore
 * 
 * Created on Oct 12, 2004
 * 
 * DateOfLastTransactionTag.java
 */
package librisuite.business.cataloguing.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import librisuite.business.cataloguing.bibliographic.FixedFieldUsingItemEntity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.casalini.digital.action.DigitalUploadFileAction;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public abstract class CasaliniSourceTag extends FixedFieldUsingItemEntity {

	private static final Log logger = LogFactory.getLog(DigitalUploadFileAction.class);
	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public CasaliniSourceTag() {
		super();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.FixedField#getDisplayString()
	 */
	public String getDisplayString() {

 		String catSourceStringTxt = getItemEntity().getCataloguingSourceStringText();
		logger.debug("'"+catSourceStringTxt+"'");
		String SUBFIELD_DELIMITER = "\u001f";
		int index = catSourceStringTxt.substring(2).indexOf(SUBFIELD_DELIMITER);
		if (index==-1)
			return catSourceStringTxt.substring(2);
		else
			return catSourceStringTxt.substring(2,index);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#isEditableHeader()
	 */
	public boolean isEditableHeader() {
		return false;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#isAbleToBeDeleted()
	 */
	public boolean isAbleToBeDeleted() {
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof CasaliniSourceTag) {
			return super.equals(obj);
		}
		else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return super.hashCode();
	}

	public Element generateModelXmlElementContent(Document xmlDocument) {
		Element content = null;
		if (xmlDocument != null) {
			content = xmlDocument.createElement("content");
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss.S");
			String result = df.format(getItemEntity().getDateOfLastTransaction());
			Text text = xmlDocument.createTextNode(result);
			content.appendChild(text);
		}
		return content;
	}

	public void parseModelXmlElementContent(Element xmlElement) {
		Element content = (Element) xmlElement.getChildNodes().item(0);
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss.S");
//		if ((content.getFirstChild() != null) && (((Text)content.getFirstChild()).getData() != null)) {
			try {
				getItemEntity().setDateOfLastTransaction(df.parse(((Text)content.getFirstChild()).getData()));
			} catch (ParseException parseException) {
			}
//		}
	}

}
