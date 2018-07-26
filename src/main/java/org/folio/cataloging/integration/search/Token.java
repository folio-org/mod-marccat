package org.folio.cataloging.integration.search;

/**
 * Query Term / Token.
 *
 * @author paulm
 * @author agazzarini
 * @since 1.0
 */
public class Token {
    Tokenizer.TokenType token;
    public final String sequence;

    /**
     * Builds a new Token with the given data.
     *
     * @param type the token type.
     * @param sequence the token char sequence.
     */
    Token(final Tokenizer.TokenType type, final String sequence) {
        this.token = type;
        this.sequence = sequence;
    }
}
