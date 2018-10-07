package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.cataloging.F;
import org.folio.cataloging.business.cataloguing.bibliographic.VariableField;
import org.folio.cataloging.business.cataloguing.common.OrderedTag;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.business.common.PersistentObjectWithView;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.DAOBibliographicNoteTag;
import org.folio.cataloging.dao.SystemNextNumberDAO;
import org.folio.cataloging.integration.GlobalStorage;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.util.StringText;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * Manager class for bibliographic note tag.
 *
 * @author paulm
 * @author agazzarini
 * @author nbianchini
 */
@SuppressWarnings("unchecked")
public class BibliographicNoteTag extends VariableField implements PersistentObjectWithView, OrderedTag {

  private static final long serialVersionUID = 5008624075912779670L;
  private static final Log logger = new Log (BibliographicNoteTag.class);
  public BibliographicNote note = new BibliographicNote ( );
  public List <BibliographicNoteOverflow> deletedOverflowList = new ArrayList <> ( );
  private StringText stringText;
  private int noteType;
  private int noteNbr;
  private Integer sequenceNumber;
  private Avp <String> valueElement;
  private StandardNoteAccessPoint noteStandard;

  public BibliographicNoteTag() {
    super ( );
    final StringText s = new StringText ( );
    s.addSubfield (new Subfield ("a", ""));
    setStringText (s);

    setNote (new BibliographicNote ( ));
    setPersistenceState (new PersistenceState ( ));
  }

  public BibliographicNoteTag(final BibliographicNote note) {
    super ( );
    setNote (note);
    setPersistenceState (new PersistenceState ( ));
  }

  public BibliographicNoteTag(final int itemNbr) {
    super (itemNbr);
  }

  /**
   * Split overflow note and sets in overflowList.
   *
   * @param inputString  -- the overflow string to split.
   * @param charWrap     -- number of chars to split.
   * @param overflowList -- overflow list to set result.
   */
  public static void wrapNoteOverflow(final String inputString, final int charWrap, final List <BibliographicNoteOverflow> overflowList) {
    final List <String> overflows = F.splitString (inputString, charWrap);
    overflowList.addAll (
      overflows.stream ( ).map (overString -> {
        final BibliographicNoteOverflow overflowNote = new BibliographicNoteOverflow ( );
        overflowNote.setStringText (overString);
        overflowNote.markNew ( );
        return overflowNote;
      }).collect (Collectors.toList ( )));
  }

  @Deprecated
  public StandardNoteAccessPoint loadNoteStandard(final int userView, final String language) {
    return null;
  }

  public final boolean isStandardNoteType() {
    return noteStandard != null;
  }

  /**
   * Return the standard note access point.
   *
   * @return noteStandard.
   */
  public StandardNoteAccessPoint getNoteStandard() {
    return noteStandard;
  }

  /**
   * Sets the standard note access point to note tag.
   *
   * @param noteStandard -- the standard note to set.
   */
  public void setNoteStandard(final StandardNoteAccessPoint noteStandard) {
    this.noteStandard = noteStandard;
  }

  /**
   * @return Avp valueElement.
   */
  public Avp <String> getValueElement() {
    return valueElement;
  }

  /**
   * Sets the value element.
   *
   * @param valueElement -- the
   */
  public void setValueElement(final Avp <String> valueElement) {
    this.valueElement = valueElement;
  }

  /**
   * @return the sequence number.
   */
  public Integer getSequenceNumber() {
    return this.getNote ( ).getSequenceNumber ( );
  }

  /**
   * Sets sequence number of tag.
   *
   * @param sequenceNbr -- the number to set.
   */
  public void setSequenceNumber(final Integer sequenceNbr) {
    sequenceNumber = sequenceNbr;
    this.getNote ( ).setSequenceNumber (sequenceNbr);
    this.getNote ( ).markChanged ( );
  }

  /**
   * Gets default implementation.
   *
   * @return false.
   */
  public boolean isBrowsable() {
    return false;
  }

  /**
   * Gets identifier associated to note tag.
   *
   * @return note number.
   */
  public int getNoteNbr() {
    return note.getNoteNbr ( );
  }

  /**
   * Sets identifier related to note tag.
   *
   * @param i -- note number to set.
   */
  private void setNoteNbr(int i) {
    note.setNoteNbr (i);
  }

  /**
   * Gets overflow note string from note list.
   *
   * @param overflowNoteList -- list of overflow notes.
   * @return note string.
   */
  private String getOverFlowString(final List <BibliographicNoteOverflow> overflowNoteList) {
    try {
      final String overflowString = overflowNoteList.stream ( ).map (oNote -> oNote.getStringText ( ).toString ( )).collect (Collectors.joining ( ));
      return overflowString;
    } catch (Exception exception) {
      return (stringText == null ? "" : stringText.toString ( ));
    }
  }

  /**
   * @return note string text.
   */
  public StringText getStringText() {
    return new StringText (note.getContent ( ) + getOverFlowString (getOverflowList ( )));
  }

  /**
   * Sets stringText to bibliographic note.
   *
   * @param text -- the text to set.
   */
  public void setStringText(final StringText text) {
    stringText = text;
    breakNotesStringText ( );
  }

  /**
   * Returns display content of whole standard note.
   * It replaces all '@1' with subfield separator if present.
   *
   * @return note standard stringText.
   */
  public StringText getStandardNoteStringText() {
    String value = (ofNullable (valueElement.getLabel ( )).isPresent ( )) ? valueElement.getLabel ( ) : "";
    if (value.contains ("@1")) {
      if (note.getContent ( ).indexOf (GlobalStorage.DOLLAR) != -1)
        value = value.replaceAll ("@1", note.getContent ( ).substring (2));
      else
        value = value.replaceAll ("@1", note.getContent ( ));
    }
    return new StringText (value);
  }

  /**
   * Used to display the entire note as marc string.
   *
   * @return stringText.
   */
  public String getStringTextString() {
    return getStringText ( ).toString ( );
  }

  /**
   * Gets category associated to bibliographic note.
   *
   * @return category.
   */
  public int getCategory() {
    return GlobalStorage.BIB_NOTE_CATEGORY;
  }

  /**
   * @return type note.
   */
  public int getNoteType() {
    return note.getNoteType ( );
  }

  /**
   * Sets type note to bibliographic note.
   *
   * @param type -- the type note to set.
   */
  public void setNoteType(final int type) {
    noteType = type;
    note.setNoteType (type);
  }

  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO ( );
    setNoteNbr (dao.getNextNumber ("BN", session));
  }

  /**
   * Gets permission string to compare with authorization agent.
   *
   * @return "editNote".
   */
  public String getRequiredEditPermission() {
    return GlobalStorage.NOTE_REQUIRED_PERMISSION;
  }

  /**
   * Gets correlation values of bibliographic note.
   *
   * @return correlationValues.
   */
  public CorrelationValues getCorrelationValues() {
    return (new CorrelationValues ( )).change (1, getNoteType ( ));
  }

  /**
   * Sets correlation values to bibliographic note.
   *
   * @param v -- the correlation values to set.
   */
  public void setCorrelationValues(final CorrelationValues v) {
    setNoteType (v.getValue (1));
  }

  /**
   * @return the dao associated.
   */
  public AbstractDAO getDAO() {
    return new DAOBibliographicNoteTag ( );
  }

  /**
   * Breaks content note if is an overflow of 1024 characters and split overflows into overflow-list notes of maximum 1000 characters.
   */
  private void breakNotesStringText() {
    deletedOverflowList.addAll (getOverflowList ( ));
    deletedOverflowList.forEach (noteOverflow -> {
      if (!noteOverflow.isNew ( )) {
        noteOverflow.markDeleted ( );
      }
    });

    setOverflowList (new ArrayList ( ));
    final String content = stringText.toString ( );

    final String standardNote = F.splitString (content, GlobalStorage.STANDARD_NOTE_MAX_LENGHT).stream ( ).findFirst ( ).get ( );
    note.setContent (standardNote);
    note.markChanged ( );

    if (standardNote.length ( ) + 1 < content.length ( )) {
      wrapNoteOverflow (content.substring (standardNote.length ( ) + 1), GlobalStorage.OVERFLOW_NOTE_MAX_LENGHT, getOverflowList ( ));
    }
  }

  /**
   * @return list of overflow.
   */
  public List getOverflowList() {
    return note.overflowList;
  }

  /**
   * Sets overflow note list.
   *
   * @param list -- the list to set.
   */
  public void setOverflowList(final List <BibliographicNoteOverflow> list) {
    note.setOverflowList (list);
  }

  @Deprecated
  public List getOverflowList(int userView) {
    return null;
  }

  /**
   * Checks correlation key value is changed for note tag.
   *
   * @param v -- the correlation values.
   * @return boolean.
   */
  public boolean correlationChangeAffectsKey(final CorrelationValues v) {
    final int firstCorrelation = v.getValue (1);
    if (firstCorrelation == GlobalStorage.PUBLISHER_DEFAULT_NOTE_TYPE
      || firstCorrelation == 381 || firstCorrelation == 382 ||
      (firstCorrelation >= 410 && firstCorrelation <= 424)) {
      setNoteType (firstCorrelation);
      return true;
    }

    return false;
  }

  /**
   * @return the bibliographic note associated to note tag.
   */
  public BibliographicNote getNote() {
    return note;
  }

  /**
   * Sets amicus number to bibliographic note tag.
   *
   * @param an -- the amicus number to set.
   */
  public void setBibItemNumber(final int an) {
    note.setItemNumber (an);
  }

  /**
   * @return deleted overflow note list.
   */
  public List <BibliographicNoteOverflow> getDeletedOverflowList() {
    return deletedOverflowList;
  }

  /**
   * Sets deleted overflow note list.
   *
   * @param list -- the list to set.
   */
  public void setDeletedOverflowList(final List <BibliographicNoteOverflow> list) {
    deletedOverflowList = list;
  }

  /**
   * @return true if is note tag.
   */
  public boolean isNote() {
    return true;
  }

  /**
   * Sets bibliographic note to note tag.
   *
   * @param note -- the note to set.
   */
  public void setNote(final BibliographicNote note) {
    this.note = note;
  }

  /**
   * @return the user view string associated to tag.
   */
  public String getUserViewString() {
    return note.getUserViewString ( );
  }

  /**
   * Sets user view string to bibliographic note.
   *
   * @param userViewString -- the user view to set.
   */
  public void setUserViewString(final String userViewString) {
    note.setUserViewString (userViewString);
  }

  /**
   * Sets the item number.
   *
   * @param itemNumber -- the item number to set.
   */
  public void setItemNumber(int itemNumber) {
    super.setItemNumber (itemNumber);
    setBibItemNumber (itemNumber);  // also sets the notes
  }

  public String toString() {
    return super.toString ( ) + " n. " + noteNbr + " type: " + noteType;
  }

  public int hashCode() {
    final int PRIME = 31;
    int result = super.hashCode ( );
    result = PRIME * result + noteNbr;
    return result;
  }

  /**
   * Compares an object with another one.
   *
   * @param obj -- the object to compare.
   * @return true if equals.
   */
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals (obj))
      return false;
    if (getClass ( ) != obj.getClass ( ))
      return false;

    final BibliographicNoteTag other = (BibliographicNoteTag) obj;
    return noteNbr == other.noteNbr;
  }

  /**
   * @return note descriptor.
   */
  public Descriptor getDescriptor() {
    return null;
  }

  /**
   * @return variant codes.
   */
  public String getVariantCodes() {
    return null;
  }

  /**
   * @return key associated to note tag.
   * @throws DataAccessException in case of data access exception.
   */
  public String getKey() throws DataAccessException {
    return getMarcEncoding ( ).getMarcTag ( );
  }

}
