package org.folio.cataloging.search;

/**
 * Boolean expression node.
 *
 * @author agazzarini
 * @author paulm
 * @since 1.0
 */
public class BooleanExpressionNode implements ExpressionNode {
	private ExpressionNode left;
	private ExpressionNode right;
	private String op;

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
	public String getValue() throws CclParserException {
		try {
			if (left instanceof TermExpressionNode && right instanceof TermExpressionNode) {
				TermExpressionNode leftTerm = (TermExpressionNode) left;
				TermExpressionNode rightTerm = (TermExpressionNode) right;
				if (leftTerm.isType2Index() || rightTerm.isType2Index()) {
					if (op.toUpperCase().equals("AND")) { // we only handle and op
						if (!rightTerm.isType2Index()) {
							//swap terms so that right is type 2
							TermExpressionNode temp = rightTerm;
							rightTerm = leftTerm;
							leftTerm = temp;
						}
						//check that from clauses are compatible
						if (leftTerm.semantic().getFromClause().contains(rightTerm.semantic().getFromClause())) {
							return "(( " + leftTerm.getValue() + " and " + rightTerm.getInnerJoinValue() + " ))";
						}
					}
				}
			}

			String sqlOp = null;
			switch (op.toUpperCase()) {
				case "AND":
					sqlOp = "intersect";
					break;
				case "OR":
					sqlOp = "union";
					break;
				case "NOT":
					sqlOp = "except";
					break;
			}
			return "(( " + left.getValue() + " ) " + sqlOp + " ( " + right.getValue() + " ))";
		} catch (Exception e) {
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
}
