package org.folio.cataloging.search;

import org.folio.cataloging.log.Log;

import java.util.ArrayList;
import java.util.List;
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

	private final List<TokenInfo> tokenInfos;
	private final List<Token> tokens;

	/**
	 * Token types.
	 *
	 * @author agazzarini
	 * @author paulm
	 * @since 1.0
	 */
	public enum TokenType {
		EOL, TERM, REL, PROX, LP, RP, BOOL, SET, QUOTEDSTRING, COMMENT, PHRASE, INDEX, WHITE, WORD
	}

	/**
	 * Builds a new Tokenizer.
	 */
	Tokenizer() {
		tokenInfos = new ArrayList<>();
		tokens = new ArrayList<>();

		register("\\[[^\\]]*\\]", TokenType.COMMENT);
		register("\"[^\"]*\"", TokenType.QUOTEDSTRING);
		register("\\(", TokenType.LP);
		register("\\)", TokenType.RP);
		register("(and|AND|or|OR|not|NOT)", TokenType.BOOL);
		register("((>=)|(<=)|>|<|=)", TokenType.REL);
		register("(n|N|w|W)([0-9]+)(?:(\\b))", TokenType.PROX);
		register("\\s", TokenType.WHITE);
		register("[^\\s=<>\\[\\(]*", TokenType.WORD);
	}

	/**
	 * Tokenizes the given query.
	 *
	 * @param query the input query string.
	 * @return this tokenizer.
	 * @throws CclParserException in case of parser failure.
	 */
	Tokenizer tokenize(final String query) throws CclParserException {
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
		return this;
	}

	/**
	 * Returns the product of this tokenizer.
	 *
	 * @return the product of this tokenizer.
	 */
	List<Token> getTokens() {
		return tokens;
	}

	/**
	 * Registers a new association between a regex and a token type.
	 *
	 * @param regex the regular expression.
	 * @param tokenType the token type.
	 */
	private void register(final String regex, final TokenType tokenType) {
		tokenInfos.add(new TokenInfo(Pattern.compile("^" + regex), tokenType));
	}
}
