/*
 * (c) LibriCore
 * 
 * Created on Dec 5, 2005
 * 
 * EquivalenceReference.java
 */
package librisuite.business.cataloguing.authority;

import librisuite.business.cataloguing.bibliographic.MarcCorrelationException;
import librisuite.business.common.CorrelationValues;
import librisuite.business.common.DataAccessException;
import librisuite.hibernate.CorrelationKey;
import librisuite.hibernate.ReferenceType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import com.libricore.librisuite.common.StringText;
import com.libricore.librisuite.common.Subfield;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class EquivalenceReference extends AuthorityReferenceTag {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Log logger = LogFactory.getLog(EquivalenceReference.class);
	
	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public EquivalenceReference() {
		super();
	}

	@Override
	public boolean isEquivalenceReference() {
		return true;
	}

	@Override
	public CorrelationKey getMarcEncoding() throws DataAccessException,
			MarcCorrelationException {
		CorrelationKey key = super.getMarcEncoding();		
		logger.debug("getMarcEncoding before source: " + key);
		key = key.changeAuthoritySourceIndicator(getDescriptor().getAuthoritySourceCode());
		logger.debug("getMarcEncoding after source: " + key);
		return key;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.VariableField#getStringText()
	 */
	@Override
	public StringText getStringText() {
		String subw =
			""
				+ getReference().getLinkDisplay()
				+ getReference().getReplacementComplexity();
		StringText result = super.getStringText();
		result.addSubfield(new Subfield("w", subw));
		return result;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
	 */
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		if (!super.correlationChangeAffectsKey(v)) {
			return !ReferenceType.isEquivalence(v.getValue(getRefTypeCorrelationPosition()));
		}
		else {
			return false;
		}
	}

	@Override
	public void parseModelXmlElementContent(Element xmlElement) {
		StringText s = StringText.parseModelXmlElementContent(xmlElement);
		String subw = s.getSubfieldsWithCodes("w").getSubfield(0).getContent();
		getReference().setLinkDisplay(subw.charAt(0));
		getReference().setReplacementComplexity(subw.charAt(1));
	}

}
