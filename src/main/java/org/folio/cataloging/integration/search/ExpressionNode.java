package org.folio.cataloging.integration.search;

/**
 * Search Engine query expression node
 *
 * @author paulm
 * @author agazzarini
 * @since 1.0
 */
public interface ExpressionNode {
    /**
     * Node type list.
     */
    enum NodeType {
		TERM, BOOL
	}

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
	String getValue() throws CclParserException;
}
