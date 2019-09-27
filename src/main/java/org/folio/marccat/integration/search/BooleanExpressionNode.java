package org.folio.marccat.integration.search;

/**
 * Boolean expression node.
 *
 * @author cchiama
 * @author paulm
 * @author carment
 * @since 1.0
 */
public class BooleanExpressionNode implements ExpressionNode {

  /**
   * The left.
   */
  private ExpressionNode left;

  /**
   * The right.
   */
  private ExpressionNode right;

  /**
   * The op.
   */
  private String op;

  /**
   * Gets the type.
   *
   * @return the type
   */
  @Override
  public NodeType getType() {
    return NodeType.BOOL;
  }

  /**
   * Returns the expression (as a string) of this boolean node.
   *
   * @return the expression (as a string) of this boolean node.
   * @throws CclParserException in case the node cannot be parsed as a valid expression.
   */
  public String getValue() {
    try {
      if (left instanceof TermExpressionNode && right instanceof TermExpressionNode) {
        TermExpressionNode leftTerm = (TermExpressionNode) left;
        TermExpressionNode rightTerm = (TermExpressionNode) right;
        if (leftTerm.isType2Index() || rightTerm.isType2Index()) {
          if ("AND".equalsIgnoreCase(op) || !rightTerm.isType2Index()) {
            TermExpressionNode temp = rightTerm;
            rightTerm = leftTerm;
            leftTerm = temp;
          }
          if ("AND".equalsIgnoreCase(op) || leftTerm.semantic().getFromClause().contains(rightTerm.semantic().getFromClause())) {
            return "(( " + leftTerm.getValue() + " and " + rightTerm.getInnerJoinValue() + " ))";
          }
        }
      }
      return "(( " + left.getValue() + " ) " + operator(op) + " ( " + right.getValue() + " ))";
    } catch (final Exception e) {
      throw new CclParserException("Query parsing error: " + e.getMessage());
    }
  }

  /**
   * Sets the left expression node.
   *
   * @param left the left expression node.
   */
  public void setLeft(final ExpressionNode left) {
    this.left = left;
  }

  /**
   * Sets the right expression node.
   *
   * @param right the right expression node.
   */
  public void setRight(final ExpressionNode right) {
    this.right = right;
  }

  /**
   * Sets the boolean operator.
   *
   * @param operator the boolean operator.
   */

  public void setOp(final String operator) {
    this.op = operator;
  }

  /**
   * Operator.
   *
   * @param input the input
   * @return the string
   */
  private String operator(final String input) {
    switch (input.toUpperCase()) {
      case "AND":
        return "intersect all";
      case "OR":
        return "union all";
      case "NOT":
        return "except all";
      default:
        return "intersect all";
    }
  }
}
