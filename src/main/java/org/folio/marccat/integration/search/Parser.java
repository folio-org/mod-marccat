package org.folio.marccat.integration.search;

import net.sf.hibernate.Session;
import org.folio.marccat.business.common.View;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.dao.DAOIndexList;
import org.folio.marccat.dao.persistence.IndexList;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.ModMarccatException;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static org.folio.marccat.config.log.Global.EMPTY_STRING;

/**
 * Query Parser.
 *
 * @author paulm
 * @author cchiama
 * @author carment
 * @since 1.0
 */
public class Parser {
  /**
   * The Constant logger.
   */
  private static final Log logger = new Log(Parser.class);

  /**
   * The default index.
   */
  private static IndexList defaultIndex;

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
   * The dao.
   */
  private final DAOIndexList dao = new DAOIndexList();

  /**
   * The tokens.
   */
  private LinkedList<Token> tokens;

  /**
   * The lookahead.
   */
  private Token lookahead;

  /**
   * Builds a new parser with the given data.
   *
   * @param locale        the current locale.
   * @param mainLibraryId the main library identifier.
   * @param searchingView the current search view.
   * @param session       the session
   */
  public Parser(final Locale locale, final int mainLibraryId, final int searchingView, final Session session) {
    this.locale = locale;
    this.mainLibraryId = mainLibraryId;
    this.searchingView = searchingView;
    this.session = session;
  }

  /**
   * Parses the incoming CCL query, sort and page the results
   *
   * @param ccl         the CCL query.
   * @param firstRecord the first record.
   * @param lastRecord  the last record.
   * @param attributes  the attributes of the search index.
   * @param directions  the directions asc or desc.
   * @return the parsed string.
   * @throws CclParserException in case of parsing failure.
   */
  public String parse(final String ccl, final int firstRecord, final int lastRecord, final String[] attributes, String[] directions) throws CclParserException {
    final Tokenizer tokenizer = new Tokenizer().tokenize(ccl);
    final ExpressionNode n = parse(tokenizer.getTokens());
    final int limitSize = (lastRecord - firstRecord) + 1;
    final int offsetSize = firstRecord - 1;
    final String orderByClause = buildOrderByClause(attributes, directions);
    final String columnSortForm = attributes != null ? getSortFormOrDateByAtributes(attributes) : EMPTY_STRING;
    final String columnItemNumber = searchingView == -1 ? "aut_nbr" : "bib_itm_nbr";
    final String query = "select res." + columnItemNumber +
      " from (select distinct " + columnSortForm + " smtc." + columnItemNumber + " from ((" + n.getValue() + ")) smtc " +
      orderByClause + ") res" +
      " limit " + limitSize + " offset " + offsetSize;
    logger.debug(
      Message.MOD_MARCCAT_00020_SE_QUERY,
      ccl, query);

    return query;
  }

  /**
   * Parses the incoming CCL query and count the document
   *
   * @param ccl the CCL query.
   * @return the parsed string.
   * @throws CclParserException in case of parsing failure.
   */
  public String parseAndCount(String ccl) throws CclParserException {
    final Tokenizer tokenizer = new Tokenizer();
    tokenizer.tokenize(ccl);
    final ExpressionNode n = parse(tokenizer.getTokens());
    return "select count(*) from ((" + n.getValue() + ")) smtc";
  }


  /**
   * Parses the a list of tokens.
   *
   * @param tokens the tokens list.
   * @return the expression node..
   * @throws CclParserException in case of parsing failure.
   */
  public ExpressionNode parse(final List<Token> tokens) throws CclParserException {
    this.tokens = new LinkedList<>(tokens);
    this.lookahead = this.tokens.getFirst();
    ExpressionNode expr = null;
    while (lookahead.token != Tokenizer.TokenType.EOL) {
      expr = searchGroup(expr);
    }
    return expr;
  }


  /**
   * Advance to the next token.
   */
  private void nextToken() {
    tokens.poll();
    lookahead =
      tokens.isEmpty()
        ? new Token(Tokenizer.TokenType.EOL, EMPTY_STRING)
        : tokens.getFirst();
  }


  /**
   * Search group.
   *
   * @param expr the expr
   * @return the expression node
   * @throws CclParserException the ccl parser exception
   */
  private ExpressionNode searchGroup(ExpressionNode expr) throws CclParserException {
    if (lookahead.token == Tokenizer.TokenType.LP) {
      nextToken();
      while (lookahead.token != Tokenizer.TokenType.RP && lookahead.token != Tokenizer.TokenType.EOL) {
        expr = searchGroup(expr);
      }
      nextToken();
      return expr;
    } else if (lookahead.token == Tokenizer.TokenType.WORD) {
      expr = searchExpression();
      return expr;
    } else if (lookahead.token == Tokenizer.TokenType.BOOL) {
      final BooleanExpressionNode op = new BooleanExpressionNode();
      op.setLeft(expr);
      op.setOp(lookahead.sequence);
      nextToken();
      op.setRight(searchGroup(expr));
      return op;
    } else {
      return expr;
    }
  }

  /**
   * Search expression.
   *
   * @return the expression node
   * @throws CclParserException the ccl parser exception
   */
  private ExpressionNode searchExpression() throws CclParserException {
    final TermExpressionNode expr = new TermExpressionNode(session, locale, mainLibraryId, searchingView);
    if (lookahead.token == Tokenizer.TokenType.WORD) {
      if (lookahead.sequence.length() <= 3) {
        IndexList i;
        try {
          i = dao.getIndexByLocalAbbreviation(session, lookahead.sequence, locale);
        } catch (DataAccessException e) {
          throw new ModMarccatException(e);
        }
        if (i != null) {
          expr.setIndex(i);
          lookahead.token = Tokenizer.TokenType.INDEX;
        } else {
          expr.setIndex(getDefaultIndex());
          return term(expr);
        }
      } else {
        expr.setIndex(getDefaultIndex());
        return term(expr);
      }
    }
    if (lookahead.token == Tokenizer.TokenType.INDEX) {
      nextToken();
      if (lookahead.token == Tokenizer.TokenType.REL) {
        expr.setRelation(lookahead.sequence);
        nextToken();
      }
      return term(expr);
    } else {
      return term(expr);
    }
  }

  /**
   * Gets the default index.
   *
   * @return the default index
   */
  private IndexList getDefaultIndex() {
    if (defaultIndex == null) {
      try {
        defaultIndex = dao.getIndexByLocalAbbreviation(session, "AW", Locale.ENGLISH);
      } catch (DataAccessException e) {
        throw new ModMarccatException(e);
      }
    }
    return defaultIndex;
  }

  /**
   * Term.
   *
   * @param expr the expr
   * @return the expression node
   * @throws CclParserException the ccl parser exception
   */
  private ExpressionNode term(TermExpressionNode expr) throws CclParserException {
    if (lookahead.token == Tokenizer.TokenType.WORD) {
      expr.appendToTerm(lookahead.sequence + " ");
      nextToken();
      return wordlist(expr);
    } else {
      return quotedString(expr);
    }
  }

  /**
   * Wordlist.
   *
   * @param expr the expr
   * @return the expression node
   * @throws CclParserException the ccl parser exception
   */
  private ExpressionNode wordlist(TermExpressionNode expr) throws CclParserException {
    if (lookahead.token == Tokenizer.TokenType.PROX) {
      expr.setProximityOperator(lookahead.sequence);
      nextToken();
      if (lookahead.token != Tokenizer.TokenType.WORD) {
        throw new CclParserException("proximity operator has invalid arguments");
      } else {
        expr.setRight(lookahead.sequence);
        nextToken();
        return expr;
      }
    }
    if (lookahead.token == Tokenizer.TokenType.WORD) {
      expr.appendToTerm(lookahead.sequence + " ");
      nextToken();
      return wordlist(expr);
    } else {
      return expr;
    }
  }

  /**
   * Quoted string.
   *
   * @param expr the expr
   * @return the expression node
   */
  private ExpressionNode quotedString(TermExpressionNode expr) {
    if (lookahead.token == Tokenizer.TokenType.QUOTEDSTRING) {
      String noQuotes = lookahead.sequence.replace("\"", EMPTY_STRING);
      expr.appendToTerm(noQuotes);
      nextToken();
      return quotedString(expr);
    } else {
      return expr;
    }
  }

  /**
   * Builds the ordering clause through the attributes and the directions.
   *
   * @param attributes the attributes of the search index.
   * @param directions the directions asc or desc.
   * @return the ordering clause
   */
  private String buildOrderByClause(
    final String[] attributes,
    final String[] directions) {
    final String columnItemNumber = (searchingView == -1) ? "aut_nbr " : "bib_itm_nbr ";
    final String direction = (directions != null && directions[0].equals("0")) ? "asc" : "desc";
    final String columnForOrderBy = attributes != null ? getSortFormOrDateByAtributes(attributes) : EMPTY_STRING;
    String orderByItemNumber = String.format(" order by smtc.%s %s ", columnItemNumber, direction);
    String order = String.format(" order by %s %s, smtc.%s ", columnForOrderBy.replace(",", EMPTY_STRING), direction, columnItemNumber);
    String orderByClause = orderByItemNumber;
    if (attributes != null) {
      for (String attribute : attributes) {
        switch (Integer.parseInt(attribute)) {
          case 4:
            orderByClause = (searchingView == -1) ? SQLCommand.TITLE_AUT_JOIN : SQLCommand.TITLE_JOIN;
            orderByClause += viewClause() + order;
            break;
          case 21:
            orderByClause = (searchingView == -1) ? SQLCommand.SUBJECT_AUT_JOIN : SQLCommand.SUBJECT_JOIN;
            orderByClause += viewClause() + order;
            break;
          case 31:
            orderByClause = SQLCommand.DATE_JOIN + viewClause() + order;
            break;
          case 54:
            orderByClause = orderByItemNumber;
            break;
          case 1003:
            orderByClause = (searchingView == -1) ? SQLCommand.NME_AUT_JOIN : SQLCommand.NAME_JOIN;
            orderByClause += viewClause() + order;
            break;
          case 2074:
            orderByClause = SQLCommand.DATE_JOIN + viewClause() + order;
            break;
          case 2096:
            orderByClause = SQLCommand.UNIFORM_TITLE_JOIN + viewClause() + order;
            break;
        }
      }
    }
    return orderByClause;
  }

  /**
   * Return the column of the sort form or date for the ordering clause.
   *
   * @param attributes the attributes of the search index.
   * @return the column of the sort form or date
   */
  private String getSortFormOrDateByAtributes(final String[] attributes) {
    String column = EMPTY_STRING;
    for (final String attribute : attributes) {
      switch (Integer.parseInt(attribute)) {
        case 4:
        case 2096:
          column = "t2.ttl_hdg_srt_form,";
          break;
        case 1003:
          column = "t2.nme_hdg_srt_form,";
          break;
        case 21:
          column = "t2.sbjct_hdg_srt_form,";
          break;
        case 31:
          column = "t1.itm_dte_1_dsc,";
          break;
        case 2074:
          column = "t1.itm_dte_2_dsc,";
          break;
        default:
          column = EMPTY_STRING;
          break;
      }
    }
    return column;
  }


  /**
   * Return the view clause.
   *
   * @return the view clause
   */
  private String viewClause() {
    return (searchingView != 0 && searchingView != -1) ? String.format(" AND (t1.usr_vw_ind = '%s')", View.makeSingleViewString(this.searchingView)) : EMPTY_STRING;
  }


}
