package org.folio.marccat.integration.search;

/**
 * Query Term / Token.
 *
 * @author paulm
 * @author agazzarini
 * @since 1.0
 */
public class Token {
  public final String sequence;
  Tokenizer.TokenType token;

  /**
   * Builds a new Token with the given data.
   *
   * @param type     the token type.
   * @param sequence the token char sequence.
   */
  Token(final Tokenizer.TokenType type, final String sequence) {
    this.token = type;
    this.sequence = sequence;
  }
}
