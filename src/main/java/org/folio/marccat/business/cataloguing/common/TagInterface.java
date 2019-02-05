package org.folio.marccat.business.cataloguing.common;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.persistence.CorrelationKey;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.ValidationException;
import org.folio.marccat.shared.CorrelationValues;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.Serializable;
import java.util.List;

public interface TagInterface {

  /**
   * Clone.
   *
   * @return the object
   */
  Object clone();

  /**
   * indicates whether the proposed change in correlation values would result in a
   * new persistent key for this tag.  Values of -1 are ignored.
   *
   * @param v the v
   * @return true, if successful
   */
  boolean correlationChangeAffectsKey(CorrelationValues v);

  /**
   * Equals.
   *
   * @param obj the obj
   * @return true, if successful
   */
  boolean equals(Object obj);

  /**
   * Evict.
   *
   * @throws DataAccessException the data access exception
   * @since 1.0
   */
  void evict() throws DataAccessException;


  /**
   * This method is used to generated the tags content in the model xml.
   *
   * @param xmlDocument the xml document
   * @return the element
   * @since 1.0
   */
  Element generateModelXmlElementContent(Document xmlDocument);

  /**
   * Gets the category.
   *
   * @return the tag category used in determining MARC coding (correlation)
   */
  int getCategory();

  /**
   * Gets the correlation.
   *
   * @param i - the correlation value wanted (1, 2, or 3)
   * @return the appropriate correlation value for determining MARC coding (-1 if no
   * value is available or known)
   */
  int getCorrelation(int i);

  /**
   * Gets the correlation values.
   *
   * @return the correlation values
   */
  CorrelationValues getCorrelationValues();

  /**
   * Sets the correlation values.
   *
   * @param v the new correlation values
   */
  void setCorrelationValues(CorrelationValues v);

  /**
   * override when displayed tag category differs from the correlation category.
   *
   * @return the display category
   * @since 1.0
   */

  int getDisplayCategory();


  /**
   * true if the display of a headingType option list is appropriate while editing.
   *
   * @return the displays heading type
   * @since 1.0
   */
  boolean getDisplaysHeadingType();

  /**
   * provides abstract access to item identifier (bib or aut).
   *
   * @return the item number
   * @since 1.0
   */
  int getItemNumber();

  /**
   * provides abstract access to item identifier (bib or aut).
   *
   * @param itemNumber the new item number
   * @since 1.0
   */
  void setItemNumber(int itemNumber);

  /**
   * Gets the marc encoding.
   *
   * @return the MARC tag and indicators for this tag
   * @throws DataAccessException the data access exception
   * @deprecated
   */
  @Deprecated
  CorrelationKey getMarcEncoding() throws DataAccessException;

  /**
   * Gets the persistence state.
   *
   * @return the persistence state
   * @since 1.0
   */
  PersistenceState getPersistenceState();

  /**
   * Sets the persistence state.
   *
   * @param object the new persistence state
   * @since 1.0
   */
  void setPersistenceState(PersistenceState object);

  /**
   * Gets the required edit permission.
   *
   * @return the name of the permission that is required if this tag is
   * allowed to be edited -- to be overridden in concrete classes where needed
   */
  String getRequiredEditPermission();

  /**
   * Gets appropriate values for selection of the second correlation list.  Values
   * are filtered based on the given value from the first correlation list.  Only
   * valid tag combinations are permitted to be chosen
   *
   * @param value1 the first correlation value
   * @return the second correlation list for this tag
   * entry
   * @throws DataAccessException the data access exception
   */
  List getSecondCorrelationList(int value1) throws DataAccessException;

  /**
   * Gets appropriate values for selection of the second correlation list.  Values
   * are filtered based on the given value from the first correlation list.  Only
   * valid tag combinations are permitted to be chosen
   *
   * @param value1 the first correlation value
   * @param value2 the second correlation value
   * @return the second correlation list for this tag
   * entry
   * @throws DataAccessException the data access exception
   */
  List getThirdCorrelationList(int value1, int value2) throws DataAccessException;

  /**
   * Gets the update status.
   *
   * @return the update status
   * @since 1.0
   */
  int getUpdateStatus();

  /**
   * Sets the update status.
   *
   * @param i the new update status
   * @since 1.0
   */
  void setUpdateStatus(int i);

  /**
   * Hash code.
   *
   * @return the int
   */
  int hashCode();

  /**
   * Checks if is able to be deleted.
   *
   * @return true if tag may be deleted
   */
  boolean isAbleToBeDeleted();

  /**
   * Checks if is browsable.
   *
   * @return true if tag can be chosen from a browse list
   */
  boolean isBrowsable();

  /**
   * Checks if is changed.
   *
   * @return true, if is changed
   * @since 1.0
   */
  boolean isChanged();

  /**
   * Checks if is deleted.
   *
   * @return true, if is deleted
   * @since 1.0
   */
  boolean isDeleted();

  /**
   * Checks if is editable header.
   *
   * @return true if tag is an editable header (category 1) field
   */
  boolean isEditableHeader();

  /**
   * Checks if is fixed field.
   *
   * @return true if tag is a fixed (control) field
   */
  boolean isFixedField();


  /**
   * Checks if is new.
   *
   * @return true, if is new
   * @since 1.0
   */
  boolean isNew();

  /**
   * return true if tag is a note.
   *
   * @return true, if is note
   */
  boolean isNote();

  /**
   * Checks if is publisher.
   *
   * @return true if tag is a publisher (260)
   */
  boolean isPublisher();


  /**
   * Checks if is removed.
   *
   * @return true, if is removed
   * @since 1.0
   */
  boolean isRemoved();

  /**
   * Mark changed.
   *
   * @since 1.0
   */
  void markChanged();

  /**
   * Mark deleted.
   *
   * @since 1.0
   */
  void markDeleted();

  /**
   * Mark new.
   *
   * @since 1.0
   */
  void markNew();

  /**
   * Mark unchanged.
   *
   * @since 1.0
   */
  void markUnchanged();

  /**
   * On delete.
   *
   * @param arg0 the arg 0
   * @return true, if successful
   * @throws CallbackException the callback exception
   * @since 1.0
   */
  boolean onDelete(Session arg0) throws CallbackException;

  /**
   * On load.
   *
   * @param arg0 the arg 0
   * @param arg1 the arg 1
   * @since 1.0
   */
  void onLoad(Session arg0, Serializable arg1);

  /**
   * On save.
   *
   * @param arg0 the arg 0
   * @return true, if successful
   * @throws CallbackException the callback exception
   * @since 1.0
   */
  boolean onSave(Session arg0) throws CallbackException;

  /**
   * On update.
   *
   * @param arg0 the arg 0
   * @return true, if successful
   * @throws CallbackException the callback exception
   * @since 1.0
   */
  boolean onUpdate(Session arg0) throws CallbackException;

  /**
   * This method is used to generate the tags content from a model.
   *
   * @param xmlElement the xml element
   * @since 1.0
   */
  void parseModelXmlElementContent(Element xmlElement);

  /**
   * This method creates a XML Document as follows
   * <datafield tag="100" ind1="1" ind2="@">
   * <subfield code="a">content</subfield>
   * <subfield code="b">content</subfield>
   * </datafield>
   * or for a control field
   * <controlfield tag="001">000000005581</controlfield>.
   *
   * @return a Document
   */
  Document toXmlDocument();

  /**
   * This method creates a XML Element as follows
   * <datafield tag="100" ind1="1" ind2="@">
   * <subfield code="a">content</subfield>
   * <subfield code="b">content</subfield>
   * </datafield>
   * or for a control field
   * <controlfield tag="001">000000005581</controlfield>.
   *
   * @param xmlDocument the xml document
   * @return an Element
   */
  Element toXmlElement(Document xmlDocument);

  /**
   * After a change in correlation value 1, the available choices for values 2 and
   * 3 are recalculated and the values are reset (to the first available valid choice).
   *
   * @param s the new value1
   * @throws DataAccessException the data access exception
   */
  void updateFirstCorrelation(int s)
    throws DataAccessException;

  /**
   * After a change in correlation value 2, the available choices for values 3
   * are recalculated and the value is reset (to the first available valid choice).
   *
   * @param s the new value 2
   * @throws DataAccessException the data access exception
   */
  void updateSecondCorrelation(int s)
    throws DataAccessException;

  /**
   * Validate.
   *
   * @param index the index
   * @throws ValidationException the validation exception
   */
  void validate(int index) throws ValidationException;

  /**
   * Called where a
   * series of changes result in returning the new key back
   * to a pre-existing key.
   */
  void reinstateDeletedTag();
}
