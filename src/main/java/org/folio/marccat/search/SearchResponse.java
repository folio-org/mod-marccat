package org.folio.marccat.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.View;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.dao.DAOCodeTable;
import org.folio.marccat.dao.persistence.T_AUT_DSPLY_FRMT;
import org.folio.marccat.dao.persistence.T_BIB_DSPLY_FRMT;
import org.folio.marccat.dao.persistence.T_ITM_DSPLY_FRMT;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.ModMarccatException;
import org.folio.marccat.search.domain.MarcRecord;
import org.folio.marccat.search.domain.Record;
import org.folio.marccat.search.engine.SearchEngine;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.OptionalInt;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.folio.marccat.util.F.safe;

/**
 * Mod-Cataloging search SearchResponse implementation.
 *
 * @author paulm
 * @author cchiama
 * @since 1.0
 */
public class SearchResponse {
  private static final int RETRIEVE_MAX_NUMBER_OF_RECORDS = 10;

  private final int searchingView;
  private final String displayQuery;
  protected Record[] record;

  private int from;
  private int to;

  @JsonIgnore
  private int[] idSet;

  @JsonIgnore
  private String complexQuery;

  @JsonIgnore
  private int sortCriteria = 0;

  /**
   * Builds a new {@link SearchResponse} with the given data.
   *
   * @param searchingView the searching view identifier.
   * @param query         the query which produced this instance.
   * @param idSet         the paged record identifiers.
   */
  public SearchResponse(final int searchingView, final String query, final int[] idSet) {
    this.searchingView = searchingView;
    this.displayQuery = ofNullable(query).orElse(Global.EMPTY_STRING);
    this.idSet = safe(idSet);
  }

  /**
   * Constructor for list of variants for a single record.
   *
   * @param recordId     the record identifier.
   * @param variantViews the list of variant views.
   */
  public SearchResponse(final int recordId, final List<Integer> variantViews) {
    this.searchingView = Integer.MIN_VALUE;
    this.displayQuery = Global.EMPTY_STRING;
    this.record =
      safe(variantViews)
        .stream()
        .map(view -> {
          final Record record = new MarcRecord();
          record.setRecordView(view);
          return record;
        })
        .toArray(Record[]::new);
    this.idSet = stream(record).mapToInt(record -> recordId).toArray();
  }

  /**
   * Returns the id set wrapped by this instance.
   *
   * @return the id set wrapped by this instance.
   */
  public int[] getIdSet() {
    return idSet;
  }

  public OptionalInt getRecordIdentifier(final int index) {
    return (index >= 0 && index < getIdSet().length)
      ? OptionalInt.of(idSet[index])
      : OptionalInt.empty();
  }

  /**
   * Returns the display query associated with this result.
   *
   * @return the display query associated with this result.
   */
  public String getDisplayQuery() {
    return displayQuery;
  }

  /**
   * Returns the searching view associated with this instance.
   *
   * @return the searching view associated with this instance.
   */
  public int getSearchingView() {
    return searchingView;
  }

  /**
   * Returns true if the current searchingView is BIBLIOGRAPHICS.
   *
   * @return true if the current searchingView is BIBLIOGRAPHICS.
   */
  @JsonIgnore
  public boolean isBibliographic() {
    return getSearchingView() != View.AUTHORITY;
  }

  /**
   * Returns the size of this result set.
   *
   * @return the size of this result set.
   */
  @JsonIgnore
  public int getPageSize() {
    return this.record.length;
  }

  /**
   * Returns the total size of this result set.
   *
   * @return the total size of this result set.
   */
  public long getNumFound() {
    return safe(getIdSet()).length;
  }

  @JsonProperty("docs")
  public Record[] getRecord() {
    return this.record;
  }

  public Record getRecord(final int recordNumber, final String elementSetName, final SearchEngine searchEngine) {
    if ((recordNumber < this.record.length) && (recordNumber >= 0)) {
      if ((this.record[recordNumber] == null) || (!this.record[recordNumber].hasContent(elementSetName))) {
        retrieveRecords(recordNumber, elementSetName, searchEngine);
      }
      return this.record[recordNumber];
    } else {
      return null;
    }
  }

  /**
   * Sets the current documents page.
   *
   * @param records the documents.
   */
  public void setRecordSet(final Record[] records) {
    this.record = records;
  }

  private void retrieveRecords(final int recordNumber, final String elementSetName, final SearchEngine searchEngine) {
    int firstRecordNumber = recordNumber;
    int lastRecordNumber = recordNumber;

    if ((recordNumber < this.record.length)
      && (recordNumber >= 0)
      && ((this.record[recordNumber] == null)
      || (!this.record[recordNumber].hasContent(elementSetName)))) {
      lastRecordNumber =
        computeLastRecordNumber(
          firstRecordNumber + 1,
          elementSetName,
          RETRIEVE_MAX_NUMBER_OF_RECORDS);
      firstRecordNumber =
        computeFirstRecordNumber(
          lastRecordNumber,
          elementSetName,
          RETRIEVE_MAX_NUMBER_OF_RECORDS);
    }

    searchEngine.fetchRecords(
      this,
      elementSetName,
      firstRecordNumber,
      lastRecordNumber);
  }

  private int computeLastRecordNumber(final int firstRecordNumber, final String elementSetName, final int maxNumberOfRecords) {
    int lastRecordNumber = firstRecordNumber;
    while (((lastRecordNumber - firstRecordNumber) < (maxNumberOfRecords - 1))
      && (lastRecordNumber < getPageSize())
      && ((this.record[lastRecordNumber] == null) || (!this.record[lastRecordNumber].hasContent(elementSetName)))) {
      lastRecordNumber++;
    }
    return lastRecordNumber;
  }

  private int computeFirstRecordNumber(final int lastRecordNumber, final String elementSetName, final int maxNumberOfRecords) {
    int firstRecordNumber = lastRecordNumber;
    while (((lastRecordNumber - firstRecordNumber) < (maxNumberOfRecords - 1))
      && (firstRecordNumber > 1)
      && ((this.record[firstRecordNumber - 2] == null) || (!this.record[firstRecordNumber - 2].hasContent(elementSetName)))) {
      firstRecordNumber--;
    }
    return firstRecordNumber;
  }

  public void clearRecords() {
    Arrays.fill(record, null);
  }

  public int getSortCriteria() {
    return sortCriteria;
  }

  public void setSortCriteria(int i) {
    sortCriteria = i;
  }

  public void sort(final String[] attributes, final String[] directions, final SearchEngine searchEngine) throws ModMarccatException {
    searchEngine.sort(this, attributes, directions);
  }

  public T_ITM_DSPLY_FRMT getDisplayFormat(final Session session, final short code, final Locale locale) throws DataAccessException {
    return (isBibliographic())
      ? (T_BIB_DSPLY_FRMT) new DAOCodeTable().load(session, T_BIB_DSPLY_FRMT.class, code, locale)
      : (T_AUT_DSPLY_FRMT) new DAOCodeTable().load(session, T_AUT_DSPLY_FRMT.class, code, locale);
  }

  public int getFrom() {
    return from;
  }

  public void setFrom(int from) {
    this.from = from;
  }

  public int getTo() {
    return to;
  }

  public void setTo(int to) {
    this.to = to;
  }
}
