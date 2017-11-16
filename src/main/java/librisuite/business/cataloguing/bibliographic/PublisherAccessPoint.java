/*
 * Created on 29-jul-2004
 *
 */
package librisuite.business.cataloguing.bibliographic;

import java.util.List;

import librisuite.business.common.CorrelationValues;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Defaults;
import librisuite.business.descriptor.Descriptor;
import librisuite.business.descriptor.PublisherTagDescriptor;
import librisuite.hibernate.PublisherFunction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.libricore.librisuite.common.StringText;

/**
 * @author paulm
 * @version $Revision: 1.6 $, $Date: 2005/12/21 08:30:32 $
 * @since 1.0
 */
public class PublisherAccessPoint extends BibliographicAccessPoint
{
	private static final long serialVersionUID = 8837067946333999273L;

	private static final Log logger = LogFactory.getLog(PublisherAccessPoint.class);

	private static final String VARIANT_CODES = "368cefg";

	private String otherSubfields;
	private int sequenceNumber;
	private PublisherTagDescriptor descriptor = new PublisherTagDescriptor();

	

	public Descriptor getDescriptor() {
		return descriptor;
	}

	/**
	 * 
	 */
	public void setDescriptor(Descriptor publisherTagDescriptor) {
		descriptor = (PublisherTagDescriptor) publisherTagDescriptor;
	}

	/**
	 * 
	 */
	public PublisherAccessPoint() {
		super();
		setFunctionCode(Defaults.getShort("publisherAccessPoint.functionCode"));
	}

	/**
	 * @param itemNbr
	 */
	public PublisherAccessPoint(int itemNbr) {
		super(itemNbr);
		setFunctionCode(Defaults.getShort("publisherAccessPoint.functionCode"));
	}

	/**
	 * @return the content of subfield c
	 * 
	 * @since 1.0
	 */
	public String getDate() {
		return new StringText(getOtherSubfields())
			.getSubfieldsWithCodes("c")
			.toDisplayString();
	}

	/**
	 * 
	 */
	public String getOtherSubfields() {
		return otherSubfields;
	}

	/**
	 * 
	 */
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @param string
	 */
	public void setOtherSubfields(String string) {
		otherSubfields = string;
	}

	/**
	 * @param integer
	 */
	public void setSequenceNumber(int integer) {
		sequenceNumber = integer;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof PublisherAccessPoint))
			return false;
		PublisherAccessPoint other = (PublisherAccessPoint) obj;
		if (this.getHeadingNumber() == null) {
			return (other.getItemNumber() == this.getItemNumber()
					&& other.getUserViewString().equals(
							this.getUserViewString())
					&& other.getHeadingNumber() == null && other
					.getFunctionCode() == this.getFunctionCode());
		} else {
			return (other.getItemNumber() == this.getItemNumber()
					&& other.getUserViewString().equals(
							this.getUserViewString())
//	20100819 inizio: andava in nullpointer se si cancellava il tag 260 e poi si reinseriva e poi si salvava il record
					&& (!(other.getHeadingNumber()==null))&&  
//	20100819 fine
					other.getHeadingNumber().equals(this.getHeadingNumber()) && other
					.getFunctionCode() == this.getFunctionCode());
		}
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		/*
		 * The previous hashcode was: return super.hashcode() This caused errors
		 * in hibernate because the super implementation includes headingNumber
		 * in the hash code, but headingNumber is not part of the business key
		 * (for publishers).
		 */
		return this.getItemNumber() + 3 * this.getFunctionCode();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getFirstCorrelationList(java.util.Locale)
	 */
	public List getFirstCorrelationList() throws DataAccessException {
		return getDaoCodeTable().getList(PublisherFunction.class,false);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getRequiredEditPermission()
	 */
	public String getRequiredEditPermission() {
		return "editNotes";
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return getDescriptor().getCorrelationValues().change(
			1,
			getFunctionCode());
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		setFunctionCode(v.getValue(1));
		getDescriptor().setCorrelationValues(v);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getCategory()
	 */
	public short getCategory() {
		return 7;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.AccessPoint#getAccessPointStringText()
	 */
	public StringText getAccessPointStringText() {
		StringText text = new StringText(otherSubfields);
		return text;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.AccessPoint#setAccesspointStringText(com.libricore.librisuite.common.StringText)
	 */
	public void setAccessPointStringText(StringText stringText) {
		//TODO _JANICK externalize codes
		otherSubfields = stringText.getSubfieldsWithCodes("cefg").toString();
	}

	public void setDescriptorStringText(StringText stringText) {
		getDescriptor().setStringText(
			stringText.getSubfieldsWithoutCodes(VARIANT_CODES).toString());
	}
	
	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.Browsable#setHeadingNumber(java.lang.Integer)
	 */
	 /*
	  * This method is overridden in Publisher Access Point because the AMICUS client
	  * behaviour has been to assign 0 to the heading number field when no heading
	  * is related.  We catch this here and use null internally.
	  */
	public void setHeadingNumber(Integer i) {
		if (i != null && i.intValue() == 0) {
			headingNumber = null;
		}
		else {
			headingNumber = i;
		}
	}
	public String getVariantCodes() {
		return VARIANT_CODES;
	}
	 
	public void markChanged() {
		if(getDescriptor()!=null)
			getDescriptor().markChanged();
	}
}
