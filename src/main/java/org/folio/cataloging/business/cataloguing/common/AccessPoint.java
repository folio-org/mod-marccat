package org.folio.cataloging.business.cataloguing.common;

import org.folio.cataloging.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.cataloging.business.cataloguing.bibliographic.VariableField;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.business.descriptor.SkipInFiling;
import org.folio.cataloging.dao.DAODescriptor;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.persistence.CorrelationKey;
import org.folio.cataloging.util.StringText;
import org.w3c.dom.Element;

import java.util.Set;

/**
 * @author paulm
 * @since 1.0
 */
public abstract class AccessPoint extends VariableField implements Persistence, Browsable {

	@Override
	public String buildBrowseTerm() {
		return getDescriptor().buildBrowseTerm();
	}

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public AccessPoint() {
		super();
		setPersistenceState(new PersistenceState());
	}

	/**
	 * Class constructor
	 *
	 * @param itemNumber
	 * @since 1.0
	 */
	public AccessPoint(int itemNumber) {
		this();
		setItemNumber(itemNumber);
	}

	protected Integer headingNumber = null;

	protected int functionCode = -1;

	public void setStringText(StringText stringText) {
		setAccessPointStringText(stringText);
	}

	public StringText getStringText() {
		StringText result = new StringText();
		if (getDescriptor() != null) {
			result.parse(getDescriptor().getStringText());
		}
		result.add(getAccessPointStringText());
		return result;
	}

	public boolean isBrowsable() {
		return true;
	}

	public abstract Descriptor getDescriptor();

	public abstract void setDescriptor(Descriptor descriptor);

	public Integer getHeadingNumber() {
		return headingNumber;
	}

	/**
	 * @param i
	 */
	public void setHeadingNumber(Integer i) {
		headingNumber = i;
	}

	public void generateNewKey() throws DataAccessException {
		//TODO revisit the matching done here
		/* 1. Is this the right place to do heading matching?
		 * 2. The matching heading's view may not be single here - possible problem
		 */
		if (getDescriptor().isNew()) {
			Descriptor d =
				((DAODescriptor) getDescriptor().getDAO()).getMatchingHeading(
					getDescriptor());
			if (d == null) {
				getDescriptor().generateNewKey();
			} else {
				setDescriptor(d);
			}
		}
		setHeadingNumber(new Integer(getDescriptor().getKey().getHeadingNumber()));
	}

	public Object clone() {
		final AccessPoint ap = (AccessPoint) super.clone();
		ap.setDescriptor(this.getDescriptor());
		return ap;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof AccessPoint)) {
			return false;
		}
		AccessPoint other = (AccessPoint) obj;
		if (super.equals(obj)) {
			if (other.getHeadingNumber() != null) {
				return other.getHeadingNumber().equals(this.getHeadingNumber());
			} else {
				return this.getHeadingNumber() == null;
			}
		} else {
			return false;
		}
	}

	public void markChanged() {
		super.markChanged();
		getDescriptor().markChanged();
	}

	/**
		 * 
		 */
	public int getFunctionCode() {
		return functionCode;
	}

	/**
		 * @param i
		 */
	public void setFunctionCode(int i) {
		functionCode = i;
	}

	public DAODescriptor getDAODescriptor() {
		return (DAODescriptor) getDescriptor().getDAO();
	}

	public int getCategory() {
		return getDescriptor().getCategory();
	}

	/**
		 * 
		 */
	public abstract StringText getAccessPointStringText();

	/**
		 * @param stringText
		 */
	public abstract void setAccessPointStringText(StringText stringText);

	public CorrelationKey getMarcEncoding() throws DataAccessException, MarcCorrelationException {
		Descriptor d = getDescriptor();
		if (d instanceof SkipInFiling) {
			return (super.getMarcEncoding()).changeSkipInFilingIndicator(
				((SkipInFiling) d).getSkipInFiling());
		} else {
			return super.getMarcEncoding();
		}
	}

	public HibernateUtil getDAO() {
		return getPersistenceState().getDAO();
	}

	public void parseModelXmlElementContent(Element xmlElement) {
		StringText s = StringText.parseModelXmlElementContent(xmlElement);
		setStringText(s);
		setDescriptorStringText(s);
	}

	public int hashCode() {
		if (getHeadingNumber() == null) {
			return getItemNumber();
		}
		else {
			return getItemNumber() + getHeadingNumber().intValue();
		}
	}

	public StringText getEditableSubfields() {
		return getAccessPointStringText();
	}

	/* (non-Javadoc)
	 * @see Browsable#getValidEditableSubfields()
	 */
	public Set getValidEditableSubfields() {
		return getTagImpl().getValidEditableSubfields(getCategory());
	}
	
	/**
	 * default implementation Browsable
	 */
	public String getVariantCodes() {
		return null;
	}
	/**
	 * The t_xxx_type_fnctn values are made from the combination of the type and function
	 *  (which gives values 1, 2 in the correlation tables)
	 *  For example 589832 = hex(00090008) = Type 9 function 8.  
	 *  So, the high order 2 bytes gives the type and the low order 2 bytes gives the function.
	 * @param n
	 * @return
	 */
	public int getType(int n) {
		int type = n >> 16;
		return type;
	}
	
	/**
	 * So, the high order 2 bytes gives the type and the low order
	 * 2 bytes gives the function.
	 * @param n
	 * @return
	 */
	public int  getFunction(int n) {
	   int function = n & 65535;
		return function;
	}
}
