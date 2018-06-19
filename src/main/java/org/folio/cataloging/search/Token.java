package org.folio.cataloging.search;

/**
 * 2018 Paul Search Engine Java
 * @author paulm
 * @since 1.0
 */
public class Token {
		public Tokenizer.TokenType token;
		public final String sequence;

		public Token(Tokenizer.TokenType token2, String sequence) {
			super();
			this.token = token2;
			this.sequence = sequence;
		}

}
