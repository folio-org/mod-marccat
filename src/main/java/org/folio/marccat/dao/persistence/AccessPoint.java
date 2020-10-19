package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.cataloguing.common.Browsable;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.business.descriptor.SkipInFiling;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.DescriptorDAO;
import org.folio.marccat.util.StringText;
import java.sql.SQLException;
import static java.util.Optional.ofNullable;

/**
 * Abstract class to manage access point class.
 *
 * @author paulm
 * @author nbianchini
 */
public abstract class AccessPoint extends VariableField implements Persistence, Browsable {

  protected Integer headingNumber = null;
  protected int functionCode = -1;


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

  @Override
  public String buildBrowseTerm() {
    return getDescriptor().buildBrowseTerm();
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

  public void setStringText(final StringText stringText) {
    setAccessPointStringText(stringText);
  }

  /**
   * Sets default implementation for browseable property.
   *
   * @return true.
   */
  @Override
  public boolean isBrowsable() {
    return true;
  }

  public Integer getHeadingNumber() {
    return headingNumber;
  }

  public void setHeadingNumber(Integer i) {
    headingNumber = i;
  }

  @Override
  public void generateNewKey(final Session session) throws HibernateException, SQLException {

    if (getDescriptor().isNew()) {
      Descriptor d = ((DescriptorDAO) getDescriptor().getDAO()).getMatchingHeading(getDescriptor(), session);
      if (d == null) {
        getDescriptor().generateNewKey(session);
      } else {
        setDescriptor(d);
      }
    }
    setHeadingNumber(getDescriptor().getKey().getHeadingNumber());
  }

  @Override
 public Object copy() {
    final AccessPoint ap = (AccessPoint) super.copy();
    ap.setDescriptor(this.getDescriptor());
    return ap;
  }

  @Override
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

  public DescriptorDAO getDAODescriptor() {
    return (DescriptorDAO) getDescriptor().getDAO();
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

  @Override
  public CorrelationKey getMarcEncoding() {
    return (getDescriptor() instanceof SkipInFiling)
      ? (super.getMarcEncoding()).changeSkipInFilingIndicator(getDescriptor().getSkipInFiling())
      : super.getMarcEncoding();

  }

  public AbstractDAO getDAO() {
    return getPersistenceState().getDAO();
  }



  /**
   * HashCode function of access point.
   *
   * @return int that represent the hashCode of access point.
   */
  @Override
  public int hashCode() {
    return ofNullable(getHeadingNumber()).map(hashCode -> getItemNumber() + getHeadingNumber().intValue())
      .orElse(getItemNumber());
  }

  public StringText getEditableSubfields() {
    return getAccessPointStringText();
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
  public int getFunction(int n) {
    return n & 65535;
  }
}
