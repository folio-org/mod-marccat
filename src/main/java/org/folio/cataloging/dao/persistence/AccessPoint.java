package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.cataloguing.bibliographic.VariableField;
import org.folio.cataloging.business.cataloguing.common.Browsable;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.business.descriptor.SkipInFiling;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.DAODescriptor;
import org.folio.cataloging.util.StringText;
import org.w3c.dom.Element;

import java.sql.SQLException;
import java.util.Set;

import static java.util.Optional.ofNullable;

/**
 * Abstract class to manage access point class.
 *
 * @author paulm
 * @author nbianchini
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
	 * @param itemNumber -- the record item number.
	 */
	public AccessPoint(final int itemNumber) {
		this();
		setItemNumber(itemNumber);
	}

	protected Integer headingNumber = null;

	protected int functionCode = -1;

	public void setStringText(final StringText stringText) {
		setAccessPointStringText(stringText);
	}

    /**
     * Gets string text associated to access point.
     *
     * @return a StringText with content field.
     */
	public StringText getStringText() {
        final StringText result = new StringText();
		if (getDescriptor() != null) {
			result.parse(getDescriptor().getStringText());
		}
		result.add(getAccessPointStringText());
		return result;
	}

    /**
     * Sets default implementation for browseable property.
     *
     * @return true.
     */
	public boolean isBrowsable() {
		return true;
	}

	public abstract Descriptor getDescriptor();

	public abstract void setDescriptor(Descriptor descriptor);

	public Integer getHeadingNumber() {
		return headingNumber;
	}

	public void setHeadingNumber(Integer i) {
		headingNumber = i;
	}

  @Deprecated
  //TODO: use method in storageService class
  public void generateNewKey(final Session session) throws HibernateException, SQLException {

    if (getDescriptor().isNew()) {
      Descriptor d = ((DAODescriptor) getDescriptor().getDAO()).getMatchingHeading(getDescriptor(), session);
      if (d == null) {
        getDescriptor().generateNewKey();
      } else {
        setDescriptor(d);
      }
    }
    setHeadingNumber(getDescriptor().getKey().getHeadingNumber());
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

	public void setFunctionCode(final int i) {
		functionCode = i;
	}

	public DAODescriptor getDAODescriptor() {
		return (DAODescriptor) getDescriptor().getDAO();
	}


    /**
     * Gets category from descriptor.
     *
     * @return the category.
     */
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

  public CorrelationKey getMarcEncoding() throws DataAccessException {
    return (getDescriptor() instanceof SkipInFiling)
      ? (super.getMarcEncoding()).changeSkipInFilingIndicator(getDescriptor().getSkipInFiling())
      : super.getMarcEncoding();

  }

	public AbstractDAO getDAO() {
		return getPersistenceState().getDAO();
	}

    /**
     * Creates and sets string text from xml element content.
     *
     * @param xmlElement -- the xml element content.
     */
	public void parseModelXmlElementContent(final Element xmlElement) {
		final StringText s = StringText.parseModelXmlElementContent(xmlElement);
		setStringText(s);
		setDescriptorStringText(s);
	}

    /**
     * HashCode function of access point.
     *
     * @return int that represent the hashCode of access point.
     */
	public int hashCode() {
	    return ofNullable(getHeadingNumber()).map(hashCode -> getItemNumber() + getHeadingNumber().intValue())
               .orElse(getItemNumber());
	}

	public StringText getEditableSubfields() {
		return getAccessPointStringText();
	}

    /**
     * Sets list of editable subfields.
     *
     * @return set of subfields.
     */
	public Set getValidEditableSubfields() {
		return getTagImpl().getValidEditableSubfields(getCategory());
	}

    /**
     * Sets default implementation.
     *
     * @return null.
     */
	public String getVariantCodes() {
		return null;
	}
	/**
	 * The high order 2 bytes gives the type.
	 *
	 * @param n
	 * @return
	 */
	public int getType(int n) {
		return n >> 16;
	}

	/**
	 * The low order 2 bytes gives the function.
     *
	 * @param n
	 * @return
	 */
	public int  getFunction(int n) {
	   return n & 65535;
	}
}
