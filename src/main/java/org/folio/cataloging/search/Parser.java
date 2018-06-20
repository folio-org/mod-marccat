package org.folio.cataloging.search;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.controller.UserProfile;
import org.folio.cataloging.dao.DAOIndexList;
import org.folio.cataloging.dao.persistence.IndexList;
import org.folio.cataloging.log.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * 2018 Query Parser.
 *
 * @author paulm
 * @author agazzarini
 * @since 1.0
 */
public class Parser {
	private static final Log logger = new Log(Parser.class);

	private LinkedList<Token> tokens;
	private Token lookahead;
	private final Locale locale;
	private final UserProfile userProfile;
	private final int searchingView;
	private DAOIndexList dao = new DAOIndexList();
	private static IndexList defaultIndex;

	Parser(final Locale locale, final UserProfile userProfile, final int searchingView) {
		this.locale = locale;
		this.userProfile = userProfile;
		this.searchingView = searchingView;
	}

	/**
	 * Parses the incoming CCL query.
	 *
	 * @param ccl the CCL query.
	 * @return the parsed string.
	 * @throws CclParserException in case of parsing failure.
	 */
	public String parse(String ccl) throws CclParserException {
		Tokenizer t = new Tokenizer();
		t.tokenize(ccl);
		ExpressionNode n = parse(t.getTokens());
		logger.debug("QUERY: "+"select * from (" + n.getValue() + ") foo order by 1 desc");
		return "select * from (" + n.getValue() + ") foo order by 1 desc";
	}

	/**
	 * Parses the a list of tokens.
	 *
	 * @param tokens the tokens list.
	 * @return the expression node..
	 * @throws CclParserException in case of parsing failure.
	 */
	public ExpressionNode parse(final List<Token> tokens) throws CclParserException {
		this.tokens = new LinkedList<Token>(tokens);
		this.lookahead = this.tokens.getFirst();
		return searchGroup();
	}

	/**
	 * Advance to the next token.
	 */
	private void nextToken() {
		tokens.poll();
		// at the end of input we return an eol type
		if (tokens.isEmpty()) {
			lookahead = new Token(Tokenizer.TokenType.EOL, "");
		} else {
			lookahead = tokens.getFirst();
		}
	}

	private ExpressionNode searchGroup() throws CclParserException {
		ExpressionNode expr;
		// ( <searchGroup> )
		if (lookahead.token == Tokenizer.TokenType.LP) {
			nextToken();
			expr = searchGroup();
			if (lookahead.token != Tokenizer.TokenType.RP) {
				throw new CclParserException("Mismatched parentheses");
			}
			return expr;
		} else {
			// <searchExpression> <BOOL> <searchGroup>
			expr = searchExpression();
			if (lookahead.token == Tokenizer.TokenType.BOOL) {
				final BooleanExpressionNode op = new BooleanExpressionNode();
				op.setLeft(expr);
				op.setOp(lookahead.sequence);

				nextToken();

				op.setRight(searchGroup());

				return op;
			} else {
				return expr;
			}
		}
	}

	private ExpressionNode searchExpression() throws CclParserException {
		TermExpressionNode expr = new TermExpressionNode(locale, userProfile, searchingView);
		if (lookahead.token == Tokenizer.TokenType.WORD) {
			if (lookahead.sequence.length() <= 3) {
				IndexList i;
				try {
					i = dao.getIndexByLocalAbbreviation(lookahead.sequence, locale);
				} catch (DataAccessException e) {
					throw new RuntimeException(e);
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
			// <INDEX> [<REL>] <term>
			nextToken();
			if (lookahead.token == Tokenizer.TokenType.REL) {
				expr.setRelation(lookahead.sequence);
				nextToken();
			}
			return term(expr);
		} else {
			// <term>
			return term(expr);
		}
	}

	private IndexList getDefaultIndex() {
		logger.debug("getDefaultIndex()");
		if (defaultIndex == null) {
			try {
				defaultIndex = dao.getIndexByLocalAbbreviation("AW", Locale.ENGLISH);
			} catch (DataAccessException e) {
				throw new RuntimeException(e);
			}
		}
		logger.debug("returning " + defaultIndex);
		return defaultIndex;
	}

	private ExpressionNode term(TermExpressionNode expr) throws CclParserException {
		if (lookahead.token == Tokenizer.TokenType.WORD) {
			// <WORD> <wordlist>
			expr.appendToTerm(lookahead.sequence + " ");
			nextToken();
			return wordlist(expr);
		} else {
			return quotedString(expr);
		}
	}

	private ExpressionNode wordlist(TermExpressionNode expr) throws CclParserException {
		if (lookahead.token == Tokenizer.TokenType.PROX) {
			expr.setProximityOperator(lookahead.sequence);
			nextToken();
			if (lookahead.token != Tokenizer.TokenType.WORD) {
				throw new CclParserException("proximity operator has invalid arguments");
			}
			else {
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
			// EOL
			return expr;
		}
	}

	private ExpressionNode quotedString(TermExpressionNode expr) {
		if (lookahead.token == Tokenizer.TokenType.QUOTEDSTRING) {
			String noQuotes = lookahead.sequence.replace("\"","");
			expr.appendToTerm(noQuotes);
			nextToken();
			return quotedString(expr);
		} else {
			// EOL
			return expr;
		}
	}


}
