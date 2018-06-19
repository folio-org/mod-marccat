package org.folio.cataloging.search;

/**
 * 2018 Paul Search Engine Java
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2018/01/01 14:09:42 $
 * @since 1.0
 */
public interface ExpressionNode {
	public enum NodeType {
		TERM, BOOL
	}

	public NodeType getType();

	public String getValue() throws CclParserException;

}
