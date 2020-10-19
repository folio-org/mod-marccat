package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.cataloguing.common.OrderedTag;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.BibliographicNoteTagDAO;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.F;
import org.folio.marccat.util.StringText;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * Manager class for bibliographic note tag.
 *
 * @author paulm
 * @author cchiama
 * @author nbianchini
 */
@SuppressWarnings("unchecked")
public class BibliographicNoteTag extends VariableField implements PersistentObjectWithView, OrderedTag {

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 5008624075912779670L;
  /**
   * The sequence number.
   */
  Integer sequenceNumber;
  /**
   * The note.
   */
  private BibliographicNote note = new BibliographicNote();
  /**
   * The deleted overflow list.
   */
  private List<BibliographicNoteOverflow> deletedOverflowList = new ArrayList<>();
  /**
   * The string text.
   */
  private StringText stringText;
  /**
   * The note type.
   */
  private int noteType;
  /**
   * The note nbr.
   */
  private int noteNbr;
  /**
   * The value element.
   */
  private transient Avp<String> valueElement;


  /**
   * Instantiates a new bibliographic note tag.
   */
  public BibliographicNoteTag() {
    super();
    final StringText s = new StringText();
    s.addSubfield(new Subfield("a", ""));
    setStringText(s);

    setNote(new BibliographicNote());
    setPersistenceState(new PersistenceState());
  }

  /**
   * Instantiates a new bibliographic note tag.
   *
   * @param note the note
   */
  public BibliographicNoteTag(final BibliographicNote note) {
    super();
    setNote(note);
    setPersistenceState(new PersistenceState());
  }

  /**
   * Instantiates a new bibliographic note tag.
   *
   * @param itemNbr the item nbr
   */
  public BibliographicNoteTag(final int itemNbr) {
    super(itemNbr);
  }

  /**
   * Split overflow note and sets in overflowList.
   *
   * @param inputString  -- the overflow string to split.
   * @param charWrap     -- number of chars to split.
   * @param overflowList -- overflow list to set result.
   */
  public static void wrapNoteOverflow(final String inputString, final int charWrap, final List<BibliographicNoteOverflow> overflowList) {
    final List<String> overflows = F.splitString(inputString, charWrap);
    overflowList.addAll(
      overflows.stream().map(overString -> {
        final BibliographicNoteOverflow overflowNote = new BibliographicNoteOverflow();
        overflowNote.setStringText(overString);
        overflowNote.markNew();
        return overflowNote;
      }).collect(Collectors.toList()));
  }



  /**
   * Gets the value element.
   *
   * @return Avp valueElement.
   */
  public Avp<String> getValueElement() {
    return valueElement;
  }

  /**
   * Sets the value element.
   *
   * @param valueElement -- the
   */
  public void setValueElement(final Avp<String> valueElement) {
    this.valueElement = valueElement;
  }

  /**
   * Gets the sequence number.
   *
   * @return the sequence number.
   */
  public Integer getSequenceNumber() {
    return this.getNote().getSequenceNumber();
  }

  /**
   * Sets sequence number of tag.
   *
   * @param sequenceNbr -- the number to set.
   */
  public void setSequenceNumber(final Integer sequenceNbr) {
    sequenceNumber = sequenceNbr;
    this.getNote().setSequenceNumber(sequenceNbr);
    this.getNote().markChanged();
  }

  /**
   * Gets default implementation.
   *
   * @return false.
   */
  @Override
  public boolean isBrowsable() {
    return false;
  }

  /**
   * Gets identifier associated to note tag.
   *
   * @return note number.
   */
  public int getNoteNbr() {
    return note.getNoteNbr();
  }

  /**
   * Sets identifier related to note tag.
   *
   * @param i -- note number to set.
   */
  private void setNoteNbr(int i) {
    note.setNoteNbr(i);
  }

  /**
   * Gets overflow note string from note list.
   *
   * @param overflowNoteList -- list of overflow notes.
   * @return note string.
   */
  private String getOverFlowString(final List<BibliographicNoteOverflow> overflowNoteList) {
    try {
      return overflowNoteList.stream().map(BibliographicNoteOverflow::getStringText).collect(Collectors.joining());
    } catch (Exception exception) {
      return (stringText == null ? "" : stringText.toString());
    }
  }

  /**
   * Gets the string text.
   *
   * @return note string text.
   */
  public StringText getStringText() {
    return new StringText(note.getStringTextString() + getOverFlowString(getOverflowList()));
  }

  /**
   * Sets stringText to bibliographic note.
   *
   * @param text -- the text to set.
   */
  public void setStringText(final StringText text) {
    stringText = text;
    breakNotesStringText();
  }

  /**
   * Returns display content of whole standard note.
   * It replaces all '@1' with subfield separator if present.
   *
   * @return note standard stringText.
   */
  public StringText getStandardNoteStringText() {
    String value = (ofNullable(valueElement.getLabel()).isPresent()) ? valueElement.getLabel() : "";
    if (value.contains("@1")) {
      if (note.getStringTextString().indexOf(Global.SUBFIELD_DELIMITER) != -1)
        value = value.replace("@1", note.getStringTextString().substring(2));
      else
        value = value.replace("@1", note.getStringTextString());
    }
    return new StringText(value);
  }

  /**
   * Used to display the entire note as marc string.
   *
   * @return stringText.
   */
  public String getStringTextString() {
    return getStringText().toString();
  }

  /**
   * Gets category associated to bibliographic note.
   *
   * @return category.
   */
  public int getCategory() {
    return Global.BIB_NOTE_CATEGORY;
  }

  /**
   * Gets the note type.
   *
   * @return type note.
   */
  public int getNoteType() {
    return note.getNoteType();
  }

  /**
   * Sets type note to bibliographic note.
   *
   * @param type -- the type note to set.
   */
  public void setNoteType(final int type) {
    noteType = type;
    note.setNoteType(type);
  }

  /**
   * Generate new key.
   *
   * @param session the session
   * @throws DataAccessException the data access exception
   * @throws HibernateException  the hibernate exception
   */
  @Override
  public void generateNewKey(final Session session) throws HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setNoteNbr(dao.getNextNumber("BN", session));
  }

  /**
   * Gets permission string to compare with authorization agent.
   *
   * @return "editNote".
   */
  @Override
  public String getRequiredEditPermission() {
    return Global.NOTE_REQUIRED_PERMISSION;
  }

  /**
   * Gets correlation values of bibliographic note.
   *
   * @return correlationValues.
   */
  public CorrelationValues getCorrelationValues() {
    return (new CorrelationValues()).change(1, getNoteType());
  }

  /**
   * Sets correlation values to bibliographic note.
   *
   * @param v -- the correlation values to set.
   */
  public void setCorrelationValues(final CorrelationValues v) {
    setNoteType(v.getValue(1));
  }

  /**
   * Gets the dao.
   *
   * @return the dao associated.
   */
  public AbstractDAO getDAO() {
    return new BibliographicNoteTagDAO();
  }

  /**
   * Breaks content note if is an overflow of 1024 characters and split overflows into overflow-list notes of maximum 1000 characters.
   */
  private void breakNotesStringText() {
    deletedOverflowList.addAll(getOverflowList());
    deletedOverflowList.forEach(noteOverflow -> {
      if (!noteOverflow.isNew()) {
        noteOverflow.markDeleted();
      }
    });

    setOverflowList(new ArrayList());
    final String content = stringText.toString();
    final String standardNote = F.splitString(content, Global.STANDARD_NOTE_MAX_LENGHT).stream().filter(Objects::nonNull).findFirst().orElse(content);
    note.setContent(standardNote);
    note.markChanged();

    if (standardNote.length() + 1 < content.length()) {
      wrapNoteOverflow(content.substring(standardNote.length() + 1), Global.OVERFLOW_NOTE_MAX_LENGHT, getOverflowList());
    }
  }

  /**
   * Gets the overflow list.
   *
   * @return list of overflow.
   */
  public List getOverflowList() {
    return note.getOverflowList();
  }

  /**
   * Sets overflow note list.
   *
   * @param list -- the list to set.
   */
  public void setOverflowList(final List<BibliographicNoteOverflow> list) {
    note.setOverflowList(list);
  }


  /**
   * Checks correlation key value is changed for note tag.
   *
   * @param v -- the correlation values.
   * @return boolean.
   */
  @Override
  public boolean correlationChangeAffectsKey(final CorrelationValues v) {
    final int firstCorrelation = v.getValue(1);
    if (firstCorrelation == Global.PUBLISHER_DEFAULT_NOTE_TYPE
      || firstCorrelation == 381 || firstCorrelation == 382 ||
      (firstCorrelation >= 410 && firstCorrelation <= 424)) {
      setNoteType(firstCorrelation);
      return true;
    }

    return false;
  }

  /**
   * Gets the note.
   *
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
    note.setItemNumber(an);
  }

  /**
   * Gets the deleted overflow list.
   *
   * @return deleted overflow note list.
   */
  public List<BibliographicNoteOverflow> getDeletedOverflowList() {
    return deletedOverflowList;
  }

  /**
   * Sets deleted overflow note list.
   *
   * @param list -- the list to set.
   */
  public void setDeletedOverflowList(final List<BibliographicNoteOverflow> list) {
    deletedOverflowList = list;
  }

  /**
   * Checks if is note.
   *
   * @return true if is note tag.
   */
  @Override
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
   * Gets the user view string.
   *
   * @return the user view string associated to tag.
   */
  public String getUserViewString() {
    return note.getUserViewString();
  }

  /**
   * Sets user view string to bibliographic note.
   *
   * @param userViewString -- the user view to set.
   */
  public void setUserViewString(final String userViewString) {
    note.setUserViewString(userViewString);
  }

  /**
   * Sets the item number.
   *
   * @param itemNumber -- the item number to set.
   */
  @Override
  public void setItemNumber(int itemNumber) {
    super.setItemNumber(itemNumber);
    setBibItemNumber(itemNumber);  // also sets the notes
  }

  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    return super.toString() + " n. " + noteNbr + " type: " + noteType;
  }

  /**
   * Hash code.
   *
   * @return the int
   */
  @Override
  public int hashCode() {
    final int PRIME = 31;
    int result = super.hashCode();
    result = PRIME * result + noteNbr;
    return result;
  }

  /**
   * Compares an object with another one.
   *
   * @param obj -- the object to compare.
   * @return true if equals.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;

    final BibliographicNoteTag other = (BibliographicNoteTag) obj;
    return noteNbr == other.noteNbr;
  }

  /**
   * Gets the descriptor.
   *
   * @return note descriptor.
   */
  public Descriptor getDescriptor() {
    return null;
  }

  /**
   * Gets the variant codes.
   *
   * @return variant codes.
   */
  public String getVariantCodes() {
    return null;
  }

  /**
   * Gets the key.
   *
   * @return key associated to note tag.
   * @throws DataAccessException in case of data access exception.
   */
  public String getKey() {
    return getMarcEncoding().getMarcTag();
  }

  /**
   * Adds the punctuation.
   *
   * @return the string text
   * @throws Exception the exception
   */
  @Override
  public StringText addPunctuation() {
    final StringText result = new StringText(getStringText().toString());
    try {
      final CorrelationKey marc = getMarcEncoding();
      if (marc.getMarcTag().equals("490")) {
        result.addPrecedingPunctuation("x", ",", ",");
        result.addPrecedingPunctuation("v", " :", null);
      }
      return result;
    } catch (Exception e) {
      return result;
    }
  }

}
