package org.folio.marccat.integration.search;

import net.sf.hibernate.Session;
import org.folio.marccat.business.common.View;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.dao.SemanticDAO;
import org.folio.marccat.dao.persistence.IndexList;
import org.folio.marccat.dao.persistence.NME_HDG;
import org.folio.marccat.dao.persistence.S_BIB1_SMNTC;
import org.folio.marccat.exception.DataAccessException;

import java.util.Locale;

import static org.folio.marccat.config.log.Global.EMPTY_STRING;

/**
 * Term expression node.
 *
 * @author paulm
 * @author cchiama
 * @author carment
 * @since 1.0
 */
public class TermExpressionNode implements ExpressionNode {

  /**
   * The Constant logger.
   */
  private static final Log logger = new Log(TermExpressionNode.class);

  /**
   * The term.
   */
  private final StringBuilder term = new StringBuilder();

  /**
   * The locale.
   */
  private final Locale locale;

  /**
   * The main library id.
   */
  private final int mainLibraryId;

  /**
   * The searching view.
   */
  private final int searchingView;

  /**
   * The session.
   */
  private final Session session;

  /**
   * The index.
   */
  private IndexList index;

  /**
   * The relation.
   */
  private String relation = "=";

  /**
   * The proximity operator.
   */
  private String proximityOperator;

  /**
   * The right.
   */
  private String right;

  /**
   * The semantic.
   */
  private S_BIB1_SMNTC semantic;

  /**
   * Builds a new expression node.
   *
   * @param session       the session
   * @param locale        the current locale.
   * @param mainLibraryId the main library identifier.
   * @param searchingView the current searching view.
   */
  TermExpressionNode(final Session session, final Locale locale, final int mainLibraryId, final int searchingView) {
    this.session = session;
    this.locale = locale;
    this.mainLibraryId = mainLibraryId;
    this.searchingView = searchingView;
  }

  /**
   * Gets the value of the semantic entry based on index and term syntax
   *
   * @return the value
   */
  @Override
  public String getValue() {
    try {
      String s = prepareTerm();
      if (proximityOperator != null && s != null) {
        int startOfTerm = s.indexOf('\'') + 1;
        if (startOfTerm > 0) {
          int endOfTerm = s.indexOf('\'', startOfTerm) - 1;
          String oldTerm = s.substring(startOfTerm, endOfTerm);
          int posWithin = oldTerm.toUpperCase().indexOf("WITHIN");
          if (posWithin > 0) {
            oldTerm = oldTerm.substring(0, posWithin - 1);
            endOfTerm = startOfTerm + posWithin - 1;
          }
          final String ordered = String.valueOf(proximityOperator.startsWith("N")).toUpperCase();

          int distance = 5;
          distance = getDistance(distance);
          String newText = String.format("NEAR ((%s,{%s}), %d, %s) ", oldTerm, getRight(), distance, ordered);
          s = s.substring(0, startOfTerm) + newText + s.substring(endOfTerm + 1);
        }
      }

      return "select distinct "
        + semantic().getSelectClause()
        + " from "
        + semantic().getFromClause()
        + " where "
        + (semantic().getJoinClause() == null ? EMPTY_STRING : semantic.getJoinClause()) + s + viewClause();
    } catch (final Exception exception) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
      throw new CclParserException("Query not supported");
    }
  }

  /**
   * Gets the distance.
   *
   * @param distance the distance
   * @return the distance
   */
  private int getDistance(int distance) {
    try {
      distance = Integer.parseInt(proximityOperator.substring(1));
    } catch (Exception e) {
      // do nothing take default distance value
    }
    return distance;
  }

  /**
   * Prepare term build sql from semantic entry
   *
   * @return the string
   */
  private String prepareTerm() {
    removeTrailingBlankInTerm();
    try {
      String sf = null;
      if(!semantic().isFullText() &&  semantic().getSecondaryIndexCode()==0) {
        final String preProcessWildCards =
          term.toString()
            .replace('?', '\u0002')
            .replace('#', '\u0003');
        final NME_HDG nme_hdg = new NME_HDG();
        nme_hdg.setStringText("\u001fa" + preProcessWildCards);
        nme_hdg.calculateAndSetSortForm();

        sf = nme_hdg
          .getSortForm()
          .replace('\u0002', '%')
          .replace('\u0003', '_');
      }
      else
        sf = term.toString();
      switch (semantic().getQueryActionCode()) {
        case "T":
        case "W":
          return String.format(semantic().getWhereClause(), sf);
        case "TT":
        case "WW":
          return String.format(semantic().getWhereClause(), sf, sf);
        case "TTT":
          return String.format(semantic().getWhereClause(), sf, sf, sf);
        case "TTTT":
          return String.format(semantic().getWhereClause(), sf, sf, sf, sf);
        case "TTTTTTT":
          return String.format(semantic().getWhereClause(), sf, sf, sf, sf, sf, sf, sf);
        case "TO":
          return String.format(semantic().getWhereClause(), sf, mainLibraryId);
        case "TTO":
          return String.format(semantic().getWhereClause(), sf, sf, mainLibraryId);
        default:
          return null;
      }
    } catch (final Exception e) {
      throw new CclParserException("Invalid term found in query");
    }
  }

  /**
   * Returns the searching view associated with this node.
   *
   * @return the searching view associated with this node.
   */
  public int getSearchingView() {
    return searchingView;
  }

  /**
   * Sets the proximity operator.
   *
   * @param proximityOperator the new proximity operator
   */
  void setProximityOperator(String proximityOperator) {
    this.proximityOperator = proximityOperator;
  }

  /**
   * Gets the right.
   *
   * @return the right
   */
  public String getRight() {
    return right;
  }

  /**
   * Sets the right.
   *
   * @param right the new right
   */
  public void setRight(String right) {
    this.right = right;
  }

  /**
   * Gets the locale.
   *
   * @return the locale
   */
  public Locale getLocale() {
    return locale;
  }

  /**
   * Gets the type.
   *
   * @return the type
   */
  @Override
  public NodeType getType() {
    return NodeType.TERM;
  }

  /**
   * Gets the inner join value.
   *
   * @return the inner join value
   */
  String getInnerJoinValue() {
    try {
      return prepareTerm();
    } catch (final Exception exception) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
      return null;
    }
  }

  /**
   * Get the Semantic for build the query.
   *
   * @return the s bib1 smntc
   */
  S_BIB1_SMNTC semantic() {
    try {
      if (semantic == null) {
        semantic = new SemanticDAO()
          .getSemanticEntry(
            session,
            index.getUseAttribute(),
            relationAsAttr(),
            positionAsAttr(),
            structureAsAttr(),
            truncationAsAttr(),
            completenessAsAddr(),
            getRecordType());
      }
      return semantic;
    } catch (final Exception exception) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }

  /**
   * Gets the record type.
   *
   * @return the record type
   */
  private short getRecordType() {
    if (getSearchingView() >= 0) {
      return 1;
    } else if (getSearchingView() == -1) {
      return 0;
    } else {
      return 3;
    }
  }

  /**
   * Structure as attr.
   *
   * @return the int
   */
  private int structureAsAttr() {
    int result = index.getStructureAttribute();
    if (result == 0) {
      result = 1;
    }
    return result;
  }

  /**
   * Position as attr.
   *
   * @return the int
   */
  private int positionAsAttr() {
    int result = index.getPositionAttribute();
    if (result == 0) {
      if (index.getStructureAttribute() == 2) {
        result = 3;
      } else {
        result = 1;
      }
    }
    return result;
  }

  /**
   * Relation as attr.
   *
   * @return the int
   */
  private int relationAsAttr() {
    switch (relation) {
      case "=":
        return 3;
      case "<":
        return 1;
      case "<=":
        return 2;
      case ">":
        return 4;
      case ">=":
        return 5;
      default:
        return 3;
    }
  }

  /**
   * Truncation as attribule, the term has an appended blank
   *
   * @return the int
   */
  private int truncationAsAttr() {
    int result = 100;
    if (!term.toString().equals(EMPTY_STRING) && term.lastIndexOf("?") == term.length() - 1) {
      term.deleteCharAt(term.length() - 1);
      return 1;
    }
    return result;
  }

  /**
   * Completeness as addr.
   *
   * @return the int
   */
  private int completenessAsAddr() {
    int result = index.getCompletenessAttribute();
    if (!term.toString().equals(EMPTY_STRING) && term.lastIndexOf("!") == term.length() - 1) {
      term.deleteCharAt(term.length() - 1);
      result = 3;
    }
    if (result == 0) {
      result = 1;
    }
    return result;
  }

  /**
   * Append to term.
   *
   * @param sequence the sequence
   */
  void appendToTerm(final String sequence) {
    term.append(sequence);
  }

  /**
   * Sets the relation.
   *
   * @param sequence the new relation
   */
  void setRelation(final String sequence) {
    relation = sequence;
  }

  /**
   * Sets the index.
   *
   * @param index the new index
   */
  public void setIndex(final IndexList index) {
    this.index = index;
  }

  /**
   * Checks if is type 2 index.
   *
   * @return true, if is type 2 index
   */
  boolean isType2Index() {
    S_BIB1_SMNTC semanticRow;
    try {
      semanticRow = semantic();
      return semanticRow != null && semantic().getSecondaryIndexCode() == (byte) 2;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * View clause.
   *
   * @return the string
   */
  private String viewClause() {
    String viewClause = EMPTY_STRING;
    if (getSearchingView() != 0 && semantic().getViewClause() != null && !EMPTY_STRING.equals(semantic().getViewClause())) {
      if (getSearchingView() < -1) {
        viewClause = String.format(" AND (%s.mad_usr_vw_cde = %d)", semantic().getViewClause(), getSearchingView());
      } else if (semantic().getPositionNumber() == 3 && semantic().isFullText()) {
        viewClause = String.format(" AND (%s.usr_vw_cde = %d)", semantic().getViewClause(), getSearchingView());
      } else {
        viewClause = String.format(" AND (%s.usr_vw_ind = '%s')", semantic().getViewClause(), View.makeSingleViewString(getSearchingView()));
      }
    }
    return viewClause;
  }

  /**
   * Removes the trailing blank in term.
   */
  private void removeTrailingBlankInTerm() {
    if (term.length() > 0 && term.charAt(term.length() - 1) == ' ') {
      term.deleteCharAt(term.length() - 1);
    }
  }
}
