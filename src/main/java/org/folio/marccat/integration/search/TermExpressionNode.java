package org.folio.marccat.integration.search;

import net.sf.hibernate.Session;
import org.folio.marccat.business.common.View;
import org.folio.marccat.business.descriptor.SortFormParameters;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.MessageCatalog;
import org.folio.marccat.dao.NameDescriptorDAO;
import org.folio.marccat.dao.SemanticDAO;
import org.folio.marccat.dao.persistence.IndexList;
import org.folio.marccat.dao.persistence.S_BIB1_SMNTC;
import org.folio.marccat.exception.DataAccessException;

import java.util.Locale;

/**
 * Term expression node.
 *
 * @author paulm
 * @author cchiama
 * @since 1.0
 */
public class TermExpressionNode implements ExpressionNode {
  private static final Log logger = new Log(TermExpressionNode.class);

  private final StringBuilder term = new StringBuilder();

  private final Locale locale;
  private final int mainLibraryId;
  private final int searchingView;
  private final Session session;
  private IndexList index;
  private String relation = "=";
  private String proximityOperator;
  private String right;
  private S_BIB1_SMNTC semantic;

  /**
   * Builds a new expression node.
   *
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

  @Override
  public String getValue() throws CclParserException {
    // get semantic entry based on index and term syntax
    try {
      String s = prepareTerm();
      if (proximityOperator != null) {
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
          try {
            distance = Integer.parseInt(proximityOperator.substring(1));
          } catch (Exception e) {
            // do nothing take default distance value
          }

          String newText = String.format("NEAR ((%s,{%s}), %d, %s) ", oldTerm, getRight(), distance, ordered);
          s = s.substring(0, startOfTerm) + newText + s.substring(endOfTerm + 1);
        }
      }

      return "select distinct "
        + semantic().getSelectClause()
        + " from "
        + semantic().getFromClause()
        + " where "
        + (semantic().getJoinClause() == null ? "" : semantic.getJoinClause()) + s + viewClause();
    } catch (final Exception exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new CclParserException("Query not supported");
    }
  }

  private String prepareTerm() throws CclParserException {
    removeTrailingBlankInTerm();

    // build sql from semantic entry
    final String preProcessWildCards =
      term.toString()
        .replace('?', '\u0002')
        .replace('#', '\u0003');

    try {
      final SortFormParameters sortFormP =
        new SortFormParameters(
          semantic().getSortFormMainTypeCode(),
          semantic().getSortFormSubTypeCode(),
          semantic().getSortFormTypeCode(),
          semantic().getSortFormFunctionCode(),
          semantic().getSortFormSkipInFilingCode());

      final String sf =
        new NameDescriptorDAO()
          .calculateSortForm(preProcessWildCards, sortFormP, session)
          .replace('\u0002', '%')
          .replace('\u0003', '_');

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

  void setProximityOperator(String proximityOperator) {
    this.proximityOperator = proximityOperator;
  }

  public String getRight() {
    return right;
  }

  public void setRight(String right) {
    this.right = right;
  }

  public Locale getLocale() {
    return locale;
  }

  @Override
  public NodeType getType() {
    return NodeType.TERM;
  }

  String getInnerJoinValue() {
    try {
      return prepareTerm();
    } catch (final Exception exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      return null;
    }
  }

  /**
   *
   * @return
   * @throws DataAccessException
   */
  S_BIB1_SMNTC semantic() throws DataAccessException {
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
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }

  /**
   *
   * @return
   */
  private short getRecordType() {
    if (getSearchingView() >= 0) {
      return 1;
    } else if (getSearchingView() == -1) {
      return 0;
    } else { // MADES
      return 3;
    }
  }

  /**
   *
   * @return
   */
  private int structureAsAttr() {
    int result = index.getStructureAttribute();
    if (result == 0) {
      result = 1;
    }
    return result;
  }

  /**
   *
   * @return
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
   *
   * @return
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

  private int truncationAsAttr() {
    // note that term has an appended blank
    int result = 100;
    if (term.lastIndexOf("?") == term.length() - 1) {
      term.deleteCharAt(term.length() - 1);
      return 1; // right truncation
    }

    return result;
  }

  private int completenessAsAddr() {
    int result = index.getCompletenessAttribute();
    if (term.lastIndexOf("!") == term.length() - 1) {
      term.deleteCharAt(term.length() - 1);
      result = 3;
    }
    if (result == 0) {
      result = 1;
    }
    return result;
  }

  void appendToTerm(final String sequence) {
    term.append(sequence);
  }

  void setRelation(final String sequence) {
    relation = sequence;
  }

  public void setIndex(final IndexList index) {
    this.index = index;
  }

  boolean isType2Index() {
    S_BIB1_SMNTC semantic;
    try {
      semantic = semantic();
      return semantic != null && semantic().getSecondaryIndexCode() == (byte) 2;
    } catch (Exception e) {
      return false;
    }
  }

  private String viewClause() {
    String viewClause = "";
    if (getSearchingView() != 0 && semantic().getViewClause() != null && !"".equals(semantic().getViewClause())) {
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

  private void removeTrailingBlankInTerm() {
    if (term.length() > 0 && term.charAt(term.length() - 1) == ' ') {
      term.deleteCharAt(term.length() - 1);
    }
  }
}
