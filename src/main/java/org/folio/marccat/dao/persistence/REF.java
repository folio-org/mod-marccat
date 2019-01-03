package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.CrossReferencesDAO;
import org.folio.marccat.dao.DAODescriptor;
import org.folio.marccat.exception.DataAccessException;

import java.io.Serializable;

/**
 * abstract class for all cross-reference tables (including NME_NME_TTL_REF
 * and TTL_NME_TTL_REF).
 *
 * @author paulm
 * @author carment
 */
public abstract class REF extends PersistenceState implements Serializable, Cloneable, PersistentObjectWithView {

  /** The persistence state. */
  private final PersistenceState persistenceState = new PersistenceState();

  /** The key. */
  private RefKey key = new RefKey();

  /** The print constant. */
  private Character printConstant;

  /** The note generation. */
  private Character noteGeneration;

  /** The former heading. */
  private Character formerHeading;

  /** The authority structure. */
  private Character authorityStructure;

  /** The earlier rules. */
  private Character earlierRules;

  /** The link display. */
  private Character linkDisplay;

  /** The replacement complexity. */
  private Character replacementComplexity;

  /** The verification level. */
  private Character verificationLevel;

  /** The string text. */
  private String stringText;

  /** The target string text. */
  private String targetStringText;


  /**
   * Instantiates a new ref.
   */
  public REF() {
    setDefault();
  }


  /**
   * New instance of a cross reference.
   *
   * @param source the source
   * @param target the target
   * @param referenceType the reference type
   * @param cataloguingView the cataloguing view
   * @return the ref
   */
   public static REF newInstance(Descriptor source, Descriptor target, int referenceType, int cataloguingView) {
    REF ref;
    try {
      ref = (REF) source.getReferenceClass(target.getClass()).newInstance();
    } catch (Exception e) {
      throw new RuntimeException("error creating cross-reference object");
    }
    ref.init(source, target, referenceType, cataloguingView);
    return ref;
  }

  /**
   * Gets the target DAO.
   *
   * @return the target DAO
   */
  public abstract DAODescriptor getTargetDAO();

  /**
   * Inits the cross reference and add the default values.
   *
   * @param source the source
   * @param target the target
   * @param referenceType the reference type
   * @param cataloguingView the cataloguing view
   */
  public void init(Descriptor source, Descriptor target, int referenceType, int cataloguingView) {
    setKey(new RefKey());
    getKey().setSource(source.getKey().getHeadingNumber());
    getKey().setTarget(target.getKey().getHeadingNumber());
    getKey().setType(referenceType);
    getKey().setUserViewString(View.makeSingleViewString(cataloguingView));
    setDefault();
  }

  /**
   * Sets the default.
   */
  public void setDefault() {
    this.setAuthorityStructure('a');
    this.setEarlierRules('x');
    this.setFormerHeading('x');
    if (getKey() != null
      && ReferenceType.isEquivalence(getKey().getType())) {
      this.setNoteGeneration('x');
    } else {
      this.setNoteGeneration('@');
    }
    this.setLinkDisplay('n');
    this.setReplacementComplexity('n');
    this.setVerificationLevel('1');
  }


  /**
   * Clone.
   *
   * @return the object
   */
  public Object clone() {
    try {
      RefKey newKey = (RefKey) getKey().clone();
      REF result = (REF) super.clone();
      result.setKey(newKey);
      return result;
    } catch (CloneNotSupportedException e) {
      return new Object();
    }
  }

  /**
   * Creates the cross reference reciprocal.
   *
   * @return the ref
   */
  public REF createReciprocal() {
    REF result = (REF) this.clone();
    result.setSource(getTarget());
    result.setTarget(getSource());
    result.setType(ReferenceType.getReciprocal(result.getType()));
    return result;
  }

  /**
   * Evict.
   *
   * @throws DataAccessException the data access exception
   */
  public void evict() throws DataAccessException {
    persistenceState.evict(this);
  }

  /**
   * Equals.
   *
   * @param obj the obj
   * @return true, if successful
   */
  public boolean equals(Object obj) {
    if (obj != null  && obj.getClass().equals(this.getClass())) {
      return this.getKey().equals(((REF) obj).getKey());
    }
    return false;
  }


  /**
   * Gets the authority structure.
   *
   * @return the authority structure
   */
  public Character getAuthorityStructure() {
    return authorityStructure;
  }

  /**
   * Sets the authority structure.
   *
   * @param b the new authority structure
   */
  public void setAuthorityStructure(Character b) {
    authorityStructure = b;
  }

  /**
   * Gets the dao.
   *
   * @return the dao
   */
  public AbstractDAO getDAO() {
    return new CrossReferencesDAO();
  }

  /**
   * Gets the earlier rules.
   *
   * @return the earlier rules
   */
  public Character getEarlierRules() {
    return earlierRules;
  }

  /**
   * Sets the earlier rules.
   *
   * @param b the new earlier rules
   */
  public void setEarlierRules(Character b) {
    earlierRules = b;
  }

  /**
   * Gets the former heading.
   *
   * @return the former heading
   */
  public Character getFormerHeading() {
    return formerHeading;
  }

  /**
   * Sets the former heading.
   *
   * @param b the new former heading
   */
  public void setFormerHeading(Character b) {
    formerHeading = b;
  }

  /**
   * Gets the key.
   *
   * @return the key
   */
  private RefKey getKey() {
    return key;
  }

  /**
   * Sets the key.
   *
   * @param refKey the new key
   */
  private void setKey(RefKey refKey) {
    key = refKey;
  }

  /**
   * Gets the note generation.
   *
   * @return the note generation
   */
  public Character getNoteGeneration() {
    return noteGeneration;
  }

  /**
   * Sets the note generation.
   *
   * @param b the new note generation
   */
  public void setNoteGeneration(Character b) {
    noteGeneration = b;
  }

  /**
   * Gets the prints the constant.
   *
   * @return the prints the constant
   */
  public Character getPrintConstant() {
    return printConstant;
  }

  /**
   * Sets the prints the constant.
   *
   * @param b the new prints the constant
   */
  public void setPrintConstant(Character b) {
    printConstant = b;
  }

  /**
   * Gets the source.
   *
   * @return the source
   */
  public int getSource() {
    return key.getSource();
  }

  /**
   * Sets the source.
   *
   * @param i the new source
   */
  public void setSource(int i) {
    key.setSource(i);
  }

  /**
   * Gets the string text.
   *
   * @return the string text
   */
  public String getStringText() {
    return stringText;
  }

  /**
   * Sets the string text.
   *
   * @param stringText the new string text
   */
  public void setStringText(String stringText) {
    this.stringText = stringText;
  }

  /**
   * Gets the target String Text.
   *
   * @return the target string text
   */
  public String getTargetStringText() {
    return targetStringText;
  }

  /**
   * Sets the target string text.
   *
   * @param targetStringText String Text the new target string text
   */
  public void setTargetStringText(String targetStringText) {
    this.targetStringText = targetStringText;
  }

  /**
   * Gets the target.
   *
   * @return the target
   */
  public int getTarget() {
    return key.getTarget();
  }

  /**
   * Sets the target.
   *
   * @param i the new target
   */
  public void setTarget(int i) {
    key.setTarget(i);
  }

  /**
   * Gets the type.
   *
   * @return the type
   */
  public Integer getType() {
    return key.getType();
  }

  /**
   * Sets the type.
   *
   * @param s the new type
   */
  public void setType(int s) {
    key.setType(s);
  }

  /**
   * Gets the update status.
   *
   * @return the update status
   */
  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
  }

  /**
   * Sets the update status.
   *
   * @param i the new update status
   */
  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus(i);
  }

  /**
   * Gets the user view string.
   *
   * @return the user view string
   */
  public String getUserViewString() {
    return getKey().getUserViewString();
  }

  /**
   * Sets the user view string.
   *
   * @param s the new user view string
   */
  public void setUserViewString(String s) {
    getKey().setUserViewString(s);
  }

  /**
   * Gets the verification level.
   *
   * @return the verification level
   */
  public Character getVerificationLevel() {
    return verificationLevel;
  }

  /**
   * Sets the verification level.
   *
   * @param b the new verification level
   */
  public void setVerificationLevel(Character b) {
    verificationLevel = b;
  }

  /**
   * Hash code.
   *
   * @return the int
   */
  public int hashCode() {
    return getKey().hashCode();
  }

  /**
   * Checks if is changed.
   *
   * @return true, if is changed
   */
  public boolean isChanged() {
    return persistenceState.isChanged();
  }

  /**
   * Checks if is deleted.
   *
   * @return true, if is deleted
   */
  public boolean isDeleted() {
    return persistenceState.isDeleted();
  }

  /**
   * Checks if is new.
   *
   * @return true, if is new
   */
  public boolean isNew() {
    return persistenceState.isNew();
  }

  /**
   * Checks if is removed.
   *
   * @return true, if is removed
   */
  @Override
  public boolean isRemoved() {
    return persistenceState.isRemoved();
  }

  /**
   * Mark changed.
   */
  public void markChanged() {
    persistenceState.markChanged();
  }

  /**
   * Mark deleted.
   */
  public void markDeleted() {
    persistenceState.markDeleted();
  }

  /**
   * Mark new.
   */
  public void markNew() {
    persistenceState.markNew();
  }

  /**
   * Mark unchanged.
   */
  public void markUnchanged() {
    persistenceState.markUnchanged();
  }

  /**
   * On delete.
   *
   * @param s the s
   * @return true, if successful
   * @throws CallbackException the callback exception
   */
  public boolean onDelete(Session s) throws CallbackException {
    return persistenceState.onDelete(s);
  }

  /**
   * On load.
   *
   * @param s the s
   * @param id the id
   */
  public void onLoad(Session s, Serializable id) {
    persistenceState.onLoad(s, id);
  }

  /**
   * On save.
   *
   * @param s the s
   * @return true, if successful
   * @throws CallbackException the callback exception
   */
  public boolean onSave(Session s) throws CallbackException {
    return persistenceState.onSave(s);
  }

  /**
   * On update.
   *
   * @param s the s
   * @return true, if successful
   * @throws CallbackException the callback exception
   */
  public boolean onUpdate(Session s) throws CallbackException {
    return persistenceState.onUpdate(s);
  }

  /**
   * Gets the link display.
   *
   * @return the link display
   */
  public Character getLinkDisplay() {
    return linkDisplay;
  }

  /**
   * Sets the link display.
   *
   * @param linkDisplay the new link display
   */
  public void setLinkDisplay(Character linkDisplay) {
    this.linkDisplay = linkDisplay;
  }

  /**
   * Gets the replacement complexity.
   *
   * @return the replacement complexity
   */
  public Character getReplacementComplexity() {
    return replacementComplexity;
  }

  /**
   * Sets the replacement complexity.
   *
   * @param replacementComplexity the new replacement complexity
   */
  public void setReplacementComplexity(Character replacementComplexity) {
    this.replacementComplexity = replacementComplexity;
  }
}
