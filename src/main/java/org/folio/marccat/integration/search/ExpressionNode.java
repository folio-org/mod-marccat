package org.folio.marccat.integration.search;

/**
 * Search Engine query expression node
 *
 * @author paulm
 * @author cchiama
 * @since 1.0
 */
public interface ExpressionNode {
  /**
   * Returns the node type.
   *
   * @return the node type.
   */
  NodeType getType();

  /**
   * Returns the value of this expression node.
   *
   * @return the value of this expression node.
   * @throws CclParserException in case of parsing failure.
   */
  String getValue();

  /**
   * Node type list.
   */
  enum NodeType {
    TERM, BOOL
  }
}
