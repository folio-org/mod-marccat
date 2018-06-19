package org.folio.cataloging.search;


/**
 * 2018 Paul Search Engine Java
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2018/01/01 14:09:42 $
 * @since 1.0
 */
public class BooleanExpressionNode implements ExpressionNode {

	private ExpressionNode left;
	private ExpressionNode right;
	private String op;
	
	public NodeType getType() {
		return NodeType.BOOL;
	}

	public String getValue() throws CclParserException {
		try {
		if (left instanceof TermExpressionNode && right instanceof TermExpressionNode) {
			TermExpressionNode leftTerm = (TermExpressionNode)left;
			TermExpressionNode rightTerm = (TermExpressionNode)right;
			if (leftTerm.isType2Index() || rightTerm.isType2Index()) {
				if (op.toUpperCase().equals("AND")) { // we only handle and op
					if (!rightTerm.isType2Index()) {
						//swap terms so that right is type 2
						TermExpressionNode temp = rightTerm;
						rightTerm = leftTerm;
						leftTerm = temp;
					}
					//check that from clauses are compatible
					if (leftTerm.getSemantic().getFromClause().contains(rightTerm.getSemantic().getFromClause())) {
						return "(( " + leftTerm.getValue() + " and " + rightTerm.getInnerJoinValue() + " ))";
					}
				}
			}
		}
		String sqlOp = null;
		if (op.toUpperCase().equals("AND")) {
			sqlOp = "intersect";
		}
		else if (op.toUpperCase().equals("OR")) {
			sqlOp = "union";
		}
		else if (op.toUpperCase().equals("NOT")) {
			sqlOp = "except";
		}
		return "(( " + left.getValue() + " ) " + sqlOp + " ( " + right.getValue() + " ))";
		}
		catch (Exception e) {
			throw new CclParserException("Query parsing error: " + e.getMessage());
		}
	}

	/**
	 * @param left the left to set
	 */
	public void setLeft(ExpressionNode left) {
		this.left = left;
	}

	/**
	 * @param right the right to set
	 */
	public void setRight(ExpressionNode right) {
		this.right = right;
	}

	/**
	 * @param op the op to set
	 */
	public void setOp(String op) {
		this.op = op;
	}

}
