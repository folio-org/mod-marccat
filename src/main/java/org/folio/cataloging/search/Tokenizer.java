package org.folio.cataloging.search;

import org.folio.cataloging.log.Log;

import java.util.LinkedList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ModCataloging search engine.
 *
 * @author paulm
 * @author agazzarini
 * @since 1.0
 */
public final class Tokenizer {
    private static final Log logger = new Log(Tokenizer.class);

	/**
	 * Token metadata.
	 *
	 * @author agazzarini
	 * @author paulm
	 * @since 1.0
	 */
	private class TokenInfo {
		final Pattern regex;
		public final TokenType type;

		/**
		 * Builds a new {@link TokenInfo} with the given data.
		 *
		 * @param regex the type regular expression.
		 * @param type the type type.
		 */
		TokenInfo(final Pattern regex, final TokenType type) {
			this.regex = regex;
			this.type = type;
		}
	}

	private final LinkedList<TokenInfo> tokenInfos;
	private final LinkedList<Token> tokens;

	public enum TokenType {
		EOL, TERM, REL, PROX, LP, RP, BOOL, SET, QUOTEDSTRING, COMMENT, PHRASE, INDEX, WHITE, WORD
	}

	/**
	 * Builds a new Tokenizer.
	 */
	Tokenizer() {
		tokenInfos = new LinkedList<>();
		tokens = new LinkedList<>();

		add("\\[[^\\]]*\\]", TokenType.COMMENT);
		add("\"[^\"]*\"", TokenType.QUOTEDSTRING);
		add("\\(", TokenType.LP);
		add("\\)", TokenType.RP);
		add("(and|AND|or|OR|not|NOT)", TokenType.BOOL);
		add("((>=)|(<=)|>|<|=)", TokenType.REL);
		add("(n|N|w|W)([0-9]+)(?:(\\b))", TokenType.PROX);
		add("\\s", TokenType.WHITE);
		add("[^\\s=<>\\[\\(]*", TokenType.WORD);
	}

	public void add(final String regex, final TokenType token) {
		tokenInfos.add(new TokenInfo(Pattern.compile("^" + regex), token));
	}

	void tokenize(final String query) throws CclParserException {
		String value = Objects.requireNonNull(query).trim();
		tokens.clear();

		while (!value.equals("")) {
			boolean match = false;
			for (TokenInfo info : tokenInfos) {
				Matcher m = info.regex.matcher(value);
				if (m.find()) {
					match = true;
					String tok = m.group().trim();

					logger.debug("matched '" + tok + "'");

					value = m.replaceFirst("").trim();
					if (info.type != TokenType.COMMENT && info.type != TokenType.WHITE) {
						tokens.add(new Token(info.type, tok));
					}
					break;
				}
			}

			if (!match) {
				throw new CclParserException("Unexpected character in input: " + value);
			}
		}
	}

	LinkedList<Token> getTokens() {
			return tokens;
		}
}
