/*
 * (c) LibriCore
 *
 * Created on 12 Jun 2007
 *
 * PredictionPattern.java
 */
package org.folio.marccat.business.patterns;

import org.folio.marccat.business.serialControl.SerialsPopulationException;
import org.folio.marccat.dao.DAOSerialNumberFormatting;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class PredictionPattern {
  private static DAOSerialNumberFormatting nbrDAO = new DAOSerialNumberFormatting();
  public int predictionPatternNumber;

  public String label = "";

  public String captionFormat = "";

  List publicationDetails = new ArrayList();

  List calendarChanges = new ArrayList();

  List indexes = new ArrayList();

  List specialPublications = new ArrayList();

  List combinedPublications = new ArrayList();

  List enumerationPattern = new ArrayList();

  private TreeMap enumMap;

  // private static java.util.regex.Pattern p = java.util.regex.Pattern
  // .compile("(.*?)@(?:'(.*?)'){0,1}(\\d?)");

  public static List populate(int predictionPatternNumber,
                              java.sql.Date start, int type, int occurrences, java.sql.Date end) throws SerialsPopulationException {
    PredictionPattern p;
    try {
      p = new DAOPredictionPattern().load(predictionPatternNumber);
      Range r = null;
      if (type == 1) {
        r = new Range(start, occurrences);
      } else {
        r = new Range(start, end);
      }
      return p.getIssues(r);
    } catch (Exception e) {
      throw new SerialsPopulationException();
    }
  }

  public List getIssues(Range r) {
    List result = new ArrayList();
    // get earliest and latest startAtDate
    Iterator iter = getEnumerationPattern().iterator();
    EnumPattern earliestStart = null;
    EnumPattern latestStart = null;
    while (iter.hasNext()) {
      EnumPattern anEnum = (EnumPattern) iter.next();
      if (anEnum.isNumberingContinuous()) {
        if (earliestStart == null
          || anEnum.getStartAtDate().compareTo(
          earliestStart.getStartAtDate()) < 0) {
          earliestStart = anEnum;
        }
        if (latestStart == null
          || anEnum.getStartAtDate().compareTo(
          latestStart.getStartAtDate()) > 0) {
          latestStart = anEnum;
        }
      }
    }

    // get first and last publication date from range
    TreeSet pubDates = getPublicationDates(r);
    if (pubDates.isEmpty()) {
      return null;
    }
    Date endDate = (Date) pubDates.last();
    if (r.getRepeatCount() != null) {
      int count = 0;
      iter = pubDates.iterator();
      while (iter.hasNext()) {
        endDate = (Date) iter.next();
        count = count + 1;
        if (count == r.getRepeatCount().intValue()) {
          break;
        }
      }
    }

    // get the first calendar change before min(earliestStart, earliest pub
    // date)
    Date startDate = (Date) pubDates.first();
    if (earliestStart != null
      && earliestStart.getStartAtDate().compareTo(startDate) < 0) {
      startDate = earliestStart.getStartAtDate();
    }
    Calendar lastYear = new GregorianCalendar();
    lastYear.setTime(startDate);
    lastYear.add(Calendar.YEAR, -1);
    Range r2 = new Range(lastYear.getTime(), startDate);
    TreeSet calChanges = getCalendarChangeDates(r2);
    if (calChanges.size() > 0) {
      startDate = (Date) calChanges.last();
    }

    // generate pubDates from new range
    Range r3;
    if (latestStart != null
      && latestStart.getStartAtDate().compareTo(endDate) > 0) {
      r3 = new Range(startDate, latestStart.getStartAtDate());
    } else {
      r3 = new Range(startDate, endDate);
    }
    pubDates = getPublicationDates(r3);

    // get calendar changes in range of publications
    calChanges = getCalendarChangeDates(r3);

    // set sum-of-lower-levels counts in enum pattern
    EnumPattern anEnum;
    int count = 0;
    for (int i = getEnumerationPattern().size() - 1; i >= 0; i--) {
      anEnum = (EnumPattern) getEnumerationPattern().get(i);
      anEnum.setLowerLevelCount(count);
      if (!anEnum.isNumberingContinuous()) {
        count = count + anEnum.getNumberOfUnits();
      }
    }

    // get first approximation for enum values
    enumMap = new TreeMap();
    Iterator calIter = calChanges.iterator();
    Date curCal;
    curCal = (Date) myNext(calIter);
    iter = pubDates.iterator();
    while (iter.hasNext()) {
      Date curPub = (Date) iter.next();
      curCal = incrementIssue(curCal, curPub, calIter);
      logEnumeration(curPub);
    }

    // calculate continuous enum adjustments
    int[] correctionArray = new int[getEnumerationPattern().size()];
    iter = getEnumerationPattern().iterator();
    while (iter.hasNext()) {
      anEnum = (EnumPattern) iter.next();
      if (anEnum.isNumberingContinuous()) {
        SortedMap sMap = enumMap.tailMap(anEnum.getStartAtDate());
        if (sMap.size() > 0) {
          List entry = (List) sMap.get(sMap.firstKey());
          int calculatedValue = ((Integer) entry.get(anEnum
            .getSequenceNumber() - 1)).intValue();
          int correctionFactor = anEnum.getStartAt()
            - calculatedValue;
          correctionArray[anEnum.getSequenceNumber() - 1] = correctionFactor;
        } else {
          correctionArray[anEnum.getSequenceNumber() - 1] = 0;
        }
      } else {
        correctionArray[anEnum.getSequenceNumber() - 1] = 0;
      }
    }

    // remove unwanted pubs
    iter = enumMap.keySet().iterator();
    while (iter.hasNext()) {
      Date aDate = (Date) iter.next();
      if (aDate.compareTo(r.getStartDate()) < 0
        || aDate.compareTo(endDate) > 0) {
        iter.remove();
      }
    }
    // apply continuous adjustments
    iter = enumMap.values().iterator();
    while (iter.hasNext()) {
      List entry = (List) iter.next();
      for (int i = 0; i < getEnumerationPattern().size(); i++) {
        entry.set(i, new Integer(((Integer) entry.get(i)).intValue()
          + correctionArray[i]));
      }
    }

    formatCaptions(enumMap);

    // apply combined publications
    iter = getCombinedDates(r).iterator();
    while (iter.hasNext()) {
      Date aDate = (Date) iter.next();
      if (enumMap.containsKey(aDate)) {
        String combo = (String) enumMap.get(aDate);
        SortedMap head = enumMap.headMap(aDate);
        if (head.size() > 0) {
          Date previousDate = (Date) head.lastKey();
          enumMap.put(previousDate, ((String) enumMap
            .get(previousDate))
            + '/' + combo);
          enumMap.remove(aDate);
        }
      }
    }
    iter = enumMap.keySet().iterator();
    while (iter.hasNext()) {
      Date aDate = (Date) iter.next();
      result.add(new PopulationEntry(aDate, 1, (String) enumMap.get(aDate)));
    }
    /*
     * indexes and special publications are applied based on start/end
     * date range (not on occurrences) so calculate a new range based on
     * earlier calculated endDate.
     */
    Range r4 = new Range(r.getStartDate(), endDate);
    TreeSet indexDates = getIndexDates(r4);
    iter = indexDates.iterator();
    while (iter.hasNext()) {
      result.add(new PopulationEntry((Date) iter.next(), 3, null));
    }

    TreeSet specialDates = getSpecialDates(r4);
    iter = specialDates.iterator();
    while (iter.hasNext()) {
      result.add(new PopulationEntry((Date) iter.next(), 4, null));
    }

    return result;
  }

  private void formatCaptions(Map enumMap) {
    Iterator iter = enumMap.keySet().iterator();
    while (iter.hasNext()) {
      Object aKey = iter.next();
      enumMap.put(aKey, formatSingleCaption((Date) aKey, (List) enumMap
        .get(aKey)));
    }
  }

  private String formatSingleCaption(Date publicationDate, List enumValues) {
    /*
     * State transition to parse caption strings STATES: 0 Start 1 gather
     * verbatim text 2 gather format string 3 gather enum level GROUPS: 1
     * verbatim text 2 format string 3 enum level
     */
    DateFormat df = DateFormat.getDateInstance();
    String group1 = "";
    String group2 = "";
    String group3 = "";
    String result = new String();
    if (getCaptionFormat() == null) {
      return result;
    }
    StringTokenizer st = new StringTokenizer(getCaptionFormat(), "@'", true);
    int state = 0;
    while (st.hasMoreTokens()) {
      String curToken = st.nextToken();
      if (curToken.equals("@")) {
        switch (state) {
          case 0:
          case 1:
          case 4:
            state = 3;
            break;
          case 2:
            group2 = group2 + curToken;
            break;
        }
      } else if (curToken.equals("'")) {
        switch (state) {
          case 0:
            state = 1;
            group1 = curToken;
            break;
          case 1:
            group1 = group1 + curToken;
            break;
          case 2:
            state = 3;
            break;
          case 3:
            state = 2;
            group2 = "";
            break;
        }
      } else {
        switch (state) {
          case 0:
            state = 1;
            group1 = curToken;
            break;
          case 1:
            group1 = group1 + curToken;
            break;
          case 2:
            group2 = group2 + curToken;
            break;
          case 3:
            group3 = "";
            String nextGroup1 = "";
            for (int i = 0; i < curToken.length(); i++) {
              if (Character.isDigit(curToken.charAt(i))) {
                group3 = group3 + curToken.charAt(i);
              } else {
                nextGroup1 = curToken.substring(i);
                break;
              }
            }
            state = 1;
            int enumRef = Integer.parseInt(group3);
            if (enumRef == 0) {
              if (group2.equals("")) {
                result = result + group1
                  + df.format(publicationDate);
              } else {
                DateFormat df2 = (DateFormat) df.clone();
                ((SimpleDateFormat) df2).applyPattern(group2);
                result = result + group1
                  + df2.format(publicationDate);
              }
            } else {
              if (group2.equals("")) {
                result = result + group1
                  + enumValues.get(enumRef - 1);
              } else {
                result = result
                  + group1
                  + nbrDAO.getFormattedNumber(group2,
                  ((Integer) enumValues
                    .get(enumRef - 1))
                    .intValue());
              }
            }
            group1 = nextGroup1;
            group2 = "";
            break;
          case 4:
            state = 0;
            break;
        }
      }
    }
    result = result + group1;
    System.out.println(publicationDate + " -- " + result);
    return result;

  }

  /*
   * The method below parses the caption string based on java.util.regex
   * classes. These are not supported in Oracle 9 (where this is being
   * deployed at present).
   */
  /*
   * private String formatSingleCaption(Date publicationDate, List enumValues) {
   * DateFormat df = DateFormat.getDateInstance(); String result = new
   * String(); String end = null; Matcher m = p.matcher(getCaptionFormat());
   * while (m.find()) { if (m.groupCount() == 3) { int enumRef =
   * Integer.parseInt(m.group(3)); if (enumRef == 0) { if (m.group(2) == null ||
   * m.group(2).equals("")) { result = result + m.group(1) +
   * df.format(publicationDate); } else { DateFormat df2 = (DateFormat)
   * df.clone(); ((SimpleDateFormat) df2).applyPattern(m.group(2)); result =
   * result + m.group(1) + df2.format(publicationDate); } } else { if
   * (m.group(2) == null || m.group(2).equals("")) { result = result +
   * m.group(1) + enumValues.get(enumRef - 1); } else { result = result +
   * m.group(1) + nbrDAO.getFormattedNumber(m.group(2),
   * ((Integer)enumValues.get(enumRef - 1)).intValue()); } } } end =
   * getCaptionFormat().substring(m.end()); } result = result + end; return
   * result; }
   */
  private TreeSet getPublicationDates(Range r) {
    Iterator iter = getPublicationDetails().iterator();
    return getPredictionPatternDates(r, iter);
  }

  private TreeSet getCalendarChangeDates(Range r) {
    Iterator iter = getCalendarChanges().iterator();
    return getPredictionPatternDates(r, iter);
  }

  private TreeSet getIndexDates(Range r) {
    Iterator iter = getIndexes().iterator();
    return getPredictionPatternDates(r, iter);
  }

  private TreeSet getSpecialDates(Range r) {
    Iterator iter = getSpecialPublications().iterator();
    return getPredictionPatternDates(r, iter);
  }

  private TreeSet getCombinedDates(Range r) {
    Iterator iter = getCombinedPublications().iterator();
    return getPredictionPatternDates(r, iter);
  }

  private TreeSet getPredictionPatternDates(Range r, Iterator iter) {
    TreeSet theDates = new TreeSet();
    while (iter.hasNext()) {
      PredictionPatternDetail aDetail = (PredictionPatternDetail) iter
        .next();
      Pattern p = PatternFactory.createPattern(aDetail.getPattern());
      theDates.addAll(Arrays.asList(p.calculateDates(r)));
    }
    return theDates;
  }

  private Object myNext(Iterator iter) {
    if (iter.hasNext()) {
      return iter.next();
    } else {
      return null;
    }
  }

  /*
   * increments the top enum level and resets lower values
   */
  private void applyCalendarChange() {
    Iterator iter = getEnumerationPattern().iterator();
    while (iter.hasNext()) {
      EnumPattern anEnum = (EnumPattern) iter.next();
      if (anEnum.getSequenceNumber() == 1) {
        anEnum.setLastValueUsed(anEnum.getLastValueUsed() + 1);
      } else if (!anEnum.isNumberingContinuous()) {
        anEnum.setLastValueUsed(1);
      }
    }
  }

  private Date incrementIssue(Date curCal, Date curPub, Iterator calIter) {
    boolean carrying = false;
    boolean topIncremented = false;
    for (int i = getEnumerationPattern().size() - 1; i >= 0; i--) {
      EnumPattern anEnum = (EnumPattern) getEnumerationPattern().get(i);
      if (i == getEnumerationPattern().size() - 1) {
        anEnum.setLastValueUsed(anEnum.getLastValueUsed() + 1);
      }
      if (carrying) {
        anEnum.setLastValueUsed(anEnum.getLastValueUsed() + 1);
        carrying = false;
        if (i == 0) {
          topIncremented = true;
        }
      }
      if (!anEnum.isNumberingContinuous()
        && anEnum.getLastValueUsed() > anEnum.getNumberOfUnits()) {
        anEnum.setLastValueUsed(1);
        carrying = true;
      }
    }
    if (curCal != null && curPub.compareTo(curCal) >= 0) {
      if (!topIncremented) {
        applyCalendarChange();
      }
      curCal = (Date) myNext(calIter);
    }
    return curCal;
  }

  private void logEnumeration(Date date) {
    List enumValues = new ArrayList();
    Iterator iter = getEnumerationPattern().iterator();
    while (iter.hasNext()) {
      EnumPattern anEnum = (EnumPattern) iter.next();
      enumValues.add(new Integer(anEnum.getLastValueUsed()));
    }
    enumMap.put(date, enumValues);
  }

  public List getEnumerationPattern() {
    return enumerationPattern;
  }

  public void setEnumerationPattern(List enumerationPattern) {
    this.enumerationPattern = enumerationPattern;
  }

  public String getCaptionFormat() {
    return captionFormat;
  }

  public void setCaptionFormat(String captionFormat) {
    this.captionFormat = captionFormat;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public int getPredictionPatternNumber() {
    return predictionPatternNumber;
  }

  public void setPredictionPatternNumber(int predictionPatternNumber) {
    this.predictionPatternNumber = predictionPatternNumber;
  }

  public List getCalendarChanges() {
    return calendarChanges;
  }

  public void setCalendarChanges(List calendarChanges) {
    this.calendarChanges = calendarChanges;
  }

  public List getIndexes() {
    return indexes;
  }

  public void setIndexes(List indexes) {
    this.indexes = indexes;
  }

  public List getPublicationDetails() {
    return publicationDetails;
  }

  public void setPublicationDetails(List publicationDetails) {
    this.publicationDetails = publicationDetails;
  }

  public List getSpecialPublications() {
    return specialPublications;
  }

  public void setSpecialPublications(List specialPublications) {
    this.specialPublications = specialPublications;
  }

  public List getCombinedPublications() {
    return combinedPublications;
  }

  public void setCombinedPublications(List combinedPublications) {
    this.combinedPublications = combinedPublications;
  }

  /**
   * Nested class to encapsulate returned list of populated entries
   *
   * @author paul
   */
  public class PopulationEntry {
    private Date date;
    private int type;
    private String caption;

    public PopulationEntry(Date date, int type, String caption) {
      this.setDate(date);
      this.type = type;
      this.caption = caption;
    }

    public Date getDate() {
      return date;
    }

    public void setDate(Date date) {
      this.date = date;
    }

    /**
     * @return the type
     */
    public int getType() {
      return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
      this.type = type;
    }

    /**
     * @return the caption
     */
    public String getCaption() {
      return caption;
    }

    /**
     * @param caption the caption to set
     */
    public void setCaption(String caption) {
      this.caption = caption;
    }
  }
}
