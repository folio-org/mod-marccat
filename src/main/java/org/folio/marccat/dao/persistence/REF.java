package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.DAOCrossReferences;
import org.folio.marccat.dao.DAODescriptor;
import org.folio.marccat.exception.CrossReferenceExistsException;
import org.folio.marccat.exception.DataAccessException;

import java.io.Serializable;

/**
 * abstract class for all cross-reference tables (including NME_NME_TTL_REF
 * and TTL_NME_TTL_REF)
 *
 * @author paulm
 * @version $Revision: 1.10 $, $Date: 2006/01/19 12:31:26 $
 * @since 1.0
 */
public abstract class REF extends PersistenceState implements Serializable, Cloneable, PersistentObjectWithView {
  private static final DAOCrossReferences theDAO = new DAOCrossReferences();
  private final PersistenceState persistenceState = new PersistenceState();
  private REF_KEY key = new REF_KEY();
  private Character printConstant;
  private Character noteGeneration;
  private Character formerHeading;
  private Character authorityStructure;
  private Character earlierRules;
  private Character linkDisplay;
  private Character replacementComplexity;
  private Character verificationLevel;
  private String stringText;

  public REF() {
    setDefault();
  }

  public static REF add(Descriptor source, Descriptor target, short referenceType, int cataloguingView, boolean isAttribute, Session session) throws DataAccessException, HibernateException {
    /* instantiate the appropriate REF type and populate key from arguments */
    REF ref = REF.newInstance(source, target, referenceType, cataloguingView, isAttribute);
    DAOCrossReferences dao = (DAOCrossReferences) ref.getDAO();
    /* verify that this xref doesn't already exist in the database */
    if (dao.load(source, target, referenceType, cataloguingView, session) != null) {
      throw new CrossReferenceExistsException();
    }
    dao.save(ref);
    return ref;
  }

  static public REF newInstance(Descriptor source, Descriptor target, int referenceType, int cataloguingView, boolean isAttribute) {
    REF ref = null;
    try {
      if (isAttribute)
        ref = new THS_ATRIB();
      else
        ref = (REF) source.getReferenceClass(target.getClass()).newInstance();
    } catch (Exception e) {
      throw new RuntimeException("error creating cross-reference object");
    }
    ref.init(source, target, referenceType, cataloguingView);
    return ref;
  }

  abstract public DAODescriptor getTargetDAO();

  public void init(Descriptor source, Descriptor target, int referenceType, int cataloguingView) {
    setKey(new REF_KEY());
    getKey().setSource(source.getKey().getHeadingNumber());
    getKey().setTarget(target.getKey().getHeadingNumber());
    getKey().setType(referenceType);
    getKey().setUserViewString(View.makeSingleViewString(cataloguingView));
    setDefault();
  }

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

  public Object clone() {
    try {
      REF_KEY newKey = (REF_KEY) getKey().clone();
      REF result = (REF) super.clone();
      result.setKey(newKey);
      return result;
    } catch (CloneNotSupportedException e) {
      return null;
    }
  }

  public REF createReciprocal() {
    REF result = (REF) this.clone();
    result.setSource(getTarget());
    result.setTarget(getSource());
    result.setType(ReferenceType.getReciprocal(result.getType()));
    return result;
  }

  public boolean equals(Object obj) {
    if (obj.getClass().equals(this.getClass())) {
      return this.getKey().equals(((REF) obj).getKey());
    }
    return false;
  }

  public void evict() throws DataAccessException {
    persistenceState.evict(this);
  }

  public void generateNewKey() throws DataAccessException {
    return;
  }

  public Character getAuthorityStructure() {
    return authorityStructure;
  }

  public void setAuthorityStructure(Character b) {
    authorityStructure = b;
  }

  public AbstractDAO getDAO() {
    return new DAOCrossReferences();
  }

  public Character getEarlierRules() {
    return earlierRules;
  }

  public void setEarlierRules(Character b) {
    earlierRules = b;
  }

  public Character getFormerHeading() {
    return formerHeading;
  }

  public void setFormerHeading(Character b) {
    formerHeading = b;
  }

  private REF_KEY getKey() {
    /* getKey is made private so that implementers are required to provide individual
     * delegate methods.  This so that both NME_REF and NME_NME_TTL_REF can behave
     * polymorphically
     */
    return key;
  }

  private void setKey(REF_KEY nme_ref_key) {
    key = nme_ref_key;
  }

  public Character getNoteGeneration() {
    return noteGeneration;
  }

  public void setNoteGeneration(Character b) {
    noteGeneration = b;
  }

  public Character getPrintConstant() {
    return printConstant;
  }

  public void setPrintConstant(Character b) {
    printConstant = b;
  }

  public int getSource() {
    return key.getSource();
  }

  public void setSource(int i) {
    key.setSource(i);
  }

  public String getStringText() {
    return stringText;
  }

  public void setStringText(String stringText) {
    this.stringText = stringText;
  }

  public int getTarget() {
    return key.getTarget();
  }

  public void setTarget(int i) {
    key.setTarget(i);
  }

  public Integer getType() {
    return key.getType();
  }

  public void setType(int s) {
    key.setType(s);
  }

  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
  }

  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus(i);
  }

  public String getUserViewString() {
    return getKey().getUserViewString();
  }

  public void setUserViewString(String s) {
    getKey().setUserViewString(s);
  }

  public Character getVerificationLevel() {
    return verificationLevel;
  }

  public void setVerificationLevel(Character b) {
    verificationLevel = b;
  }

  public int hashCode() {
    return getKey().hashCode();
  }

  public boolean isChanged() {
    return persistenceState.isChanged();
  }

  public boolean isDeleted() {
    return persistenceState.isDeleted();
  }

  public boolean isNew() {
    return persistenceState.isNew();
  }

  public boolean isRemoved() {
    return persistenceState.isRemoved();
  }

  public void markChanged() {
    persistenceState.markChanged();
  }

  public void markDeleted() {
    persistenceState.markDeleted();
  }

  public void markNew() {
    persistenceState.markNew();
  }

  public void markUnchanged() {
    persistenceState.markUnchanged();
  }

  public boolean onDelete(Session s) throws CallbackException {
    return persistenceState.onDelete(s);
  }

  public void onLoad(Session s, Serializable id) {
    persistenceState.onLoad(s, id);
  }

  public boolean onSave(Session s) throws CallbackException {
    return persistenceState.onSave(s);
  }

  public boolean onUpdate(Session s) throws CallbackException {
    return persistenceState.onUpdate(s);
  }

  public Character getLinkDisplay() {
    return linkDisplay;
  }

  public void setLinkDisplay(Character linkDisplay) {
    this.linkDisplay = linkDisplay;
  }

  public Character getReplacementComplexity() {
    return replacementComplexity;
  }

  public void setReplacementComplexity(Character replacementComplexity) {
    this.replacementComplexity = replacementComplexity;
  }
}
