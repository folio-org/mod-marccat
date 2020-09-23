package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.authority.AuthorityHeaderFieldHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author elena
 *
 */
public class AuthorityLeader extends Leader  {
	  public AuthorityLeader() {
		    super();
		    setHeaderField(new AuthorityHeaderFieldHelper());
		    setHeaderType((short)9);
		  }
	  
	  @Override
	  public String getDisplayString() {
			String result = "00000";
			result =
				result
					+ getRecordStatusCode()
					+ "z   2200000"
					+ getEncodingLevel()
					+ "  4500";
			return result;
	  }
		public Element generateModelXmlElementContent(final Document xmlDocument) {
			Element content = null;
				if (xmlDocument != null) {
					content = xmlDocument.createElement("content");
					content.setAttribute("recordStatusCode", "" + getRecordStatusCode());
					content.setAttribute("encodingLevel", "" + getEncodingLevel());
				}
				return content;
			}
		public void parseModelXmlElementContent(Element xmlElement) {
			Element content = (Element) xmlElement.getChildNodes().item(0);
			setRecordStatusCode(content.getAttribute("recordStatusCode").charAt(0));
			setEncodingLevel(content.getAttribute("encodingLevel").charAt(0));
		}
		private AUT getAutItm() {
			return (AUT)getItemEntity();
		}
		public char getEncodingLevel() {
			return getAutItm().getEncodingLevel();
		}
		public void setEncodingLevel(char c) {
			getAutItm().setEncodingLevel(c);
		}
		public char getRecordStatusCode() {
			return getAutItm().getRecordStatusCode();
		}
		public void setRecordStatusCode(char c) {
			getAutItm().setRecordStatusCode(c);
		}


}
