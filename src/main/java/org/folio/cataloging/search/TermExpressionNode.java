package org.folio.cataloging.search;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.controller.UserProfile;
import org.folio.cataloging.business.descriptor.SortFormParameters;
import org.folio.cataloging.dao.DAONameDescriptor;
import org.folio.cataloging.dao.persistence.IndexList;

import java.util.Locale;

/**
 * 2018 Paul Search Engine Java
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2018/01/01 14:09:42 $
 * @since 1.0
 */
public class TermExpressionNode implements ExpressionNode {
	private static final Log logger = LogFactory
			.getLog(TermExpressionNode.class);
	private String DATABASE = System.getProperty("database");
	private final static String POSTGRES = "postgres";
	private StringBuffer term = new StringBuffer();
	private IndexList index;
	private String relation = "=";
	private Locale locale;
	private UserProfile userProfile;
	private int searchingView;
	private String proximityOperator;
	private String right;
	private S_BIB1_SMNTC semantic = null;
	

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public int getSearchingView() {
		return searchingView;
	}

	public void setSearchingView(int searchingView) {
		this.searchingView = searchingView;
	}

	public String getProximityOperator() {
		return proximityOperator;
	}

	public void setProximityOperator(String proximityOperator) {
		this.proximityOperator = proximityOperator;
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}

	public Locale getLocale() {
		return locale;
	}

	public TermExpressionNode(Locale locale, UserProfile userProfile,
			int searchingView) {
		setLocale(locale);
		setUserProfile(userProfile);
		setSearchingView(searchingView);
	}

	public NodeType getType() {
		return NodeType.TERM;
	}

	public String getValue() throws CclParserException {
		// get semantic entry based on index and term syntax
		try {
			String s = prepareTerm();
			String viewClause = "";
			if (getSearchingView() != 0 && semantic.getViewClause() != null
					&& !"".equals(semantic.getViewClause())) {
				if (getSearchingView() < -1) {
					viewClause = String.format(" AND (%s.mad_usr_vw_cde = %d)",
							semantic.getViewClause(), getSearchingView());
				} else if (semantic.getPositionNumber() == 3
						&& semantic.isFullText()) {
					viewClause = String.format(" AND (%s.usr_vw_cde = %d)",
							semantic.getViewClause(), getSearchingView());
				} else {
					viewClause = String.format(
							" AND (SUBSTR(%s.usr_vw_ind, %d, 1) = '1')",
							semantic.getViewClause(), getSearchingView());
				}
			}
			if (getProximityOperator() != null) {
				int startOfTerm = s.indexOf('\'') + 1;
				if (startOfTerm > 0) {
					int endOfTerm = s.indexOf('\'', startOfTerm) - 1;
					String oldTerm = s.substring(startOfTerm, endOfTerm);
					int posWithin = oldTerm.toUpperCase().indexOf("WITHIN");
					if (posWithin > 0) {
						oldTerm = oldTerm.substring(0, posWithin - 1);
						endOfTerm = startOfTerm + posWithin - 1;
					}
					String ordered = getProximityOperator().startsWith("N") ? "FALSE"
							: "TRUE";
					int distance = 5;
					try {
						distance = Integer.parseInt(getProximityOperator()
								.substring(1));
					} catch (Exception e) {
						// do nothing take default distance value
					}
					String newText = String.format("NEAR ((%s,{%s}), %d, %s) ",
							oldTerm, getRight(), distance, ordered);
					s = s.substring(0, startOfTerm) + newText
							+ s.substring(endOfTerm + 1);
				}
			}
			String result = "select distinct "
					+ semantic.getSelectClause()
					+ " from "
					+ semantic.getFromClause()
					+ " where "
					+ (semantic.getJoinClause() == null ? "" : semantic
							.getJoinClause()) + s + viewClause;
			logger.debug(result);
			return result;
		} catch (Exception e) {
			logger.warn("getValue threw exception", e);
			throw new CclParserException("Query not supported");
		}
	}

	private String prepareTerm() throws CclParserException {
		if (term.length() > 0 && term.charAt(term.length() - 1) == ' ') {
			term.deleteCharAt(term.length() - 1); // remove trailing blank
			// in term
		}
		try {
		getSemantic();
		}
		catch (DataAccessException e) {
			throw new CclParserException("No semantics for given term");
		}
		// build sql from semantic entry
		String preProcessWildCards = term.toString().replace('?', '\u0002');
		preProcessWildCards = preProcessWildCards.replace('#', '\u0003');
		String sf;
		try {
			SortFormParameters sortFormP = new SortFormParameters(semantic
					.getSortFormMainTypeCode(), semantic
					.getSortFormSubTypeCode(), semantic
					.getSortFormTypeCode(), semantic
					.getSortFormFunctionCode(), semantic
					.getSortFormSkipInFilingCode());
			
				sf = new DAONameDescriptor().calculateSortFormForPostgres(
						preProcessWildCards, sortFormP);
		} catch (Exception e) {
			throw new CclParserException("Invalid term found in query");
		}
		sf = sf.replace('\u0002', '%');
		sf = sf.replace('\u0003', '_');
		String s = null;
		if (semantic.getQueryActionCode().equals("T")
				|| semantic.getQueryActionCode().equals("W")) {
			s = String.format(semantic.getWhereClause(), sf);
		} else if (semantic.getQueryActionCode().equals("TT")
				|| semantic.getQueryActionCode().equals("WW")) {
			s = String.format(semantic.getWhereClause(), sf, sf);
		} else if (semantic.getQueryActionCode().equals("TTT")) {
			s = String.format(semantic.getWhereClause(), sf, sf, sf);
		} else if (semantic.getQueryActionCode().equals("TTTT")) {
			s = String.format(semantic.getWhereClause(), sf, sf, sf, sf);
		} else if (semantic.getQueryActionCode().equals("TTTTTTT")) {
			s = String.format(semantic.getWhereClause(), sf, sf, sf, sf, sf,
					sf, sf);
		} else if (semantic.getQueryActionCode().equals("TO")) {
			s = String.format(semantic.getWhereClause(), sf, getUserProfile()
					.getMainLibrary());
		} else if (semantic.getQueryActionCode().equals("TTO")) {
			s = String.format(semantic.getWhereClause(), sf, sf,
					getUserProfile().getMainLibrary());
		}
		return s;
	}

	public String getInnerJoinValue() {
		try {
			String s = prepareTerm();
			String viewClause = "";
			if (getSearchingView() != 0 && semantic.getViewClause() != null
					&& !"".equals(semantic.getViewClause())) {
				if (getSearchingView() < -1) {
					viewClause = String.format(" AND (%s.mad_usr_vw_cde = %d)",
							semantic.getViewClause(), getSearchingView());
				} else if (semantic.getPositionNumber() == 3
						&& semantic.isFullText()) {
					viewClause = String.format(" AND (%s.usr_vw_cde = %d)",
							semantic.getViewClause(), getSearchingView());
				} else {
					viewClause = String.format(
							" AND (SUBSTR(%s.usr_vw_ind, %d, 1) = '1')",
							semantic.getViewClause(), getSearchingView());
				}
			}
			return s;
		} catch (Exception e) {
			logger.warn(e);
			return null;
		}
	}

	public S_BIB1_SMNTC getSemantic() throws DataAccessException {
		try {
			if (semantic == null) {
				semantic = new DAOSemantic().getSemanticEntry(index
						.getUseAttribute(), relationAsAttr(), positionAsAttr(),
						structureAsAttr(), truncationAsAttr(),
						completenessAsAddr(), getRecordType());
				logger.debug(semantic);
			}
			return semantic;
		} catch (Exception e) {
			logger.warn(e);
			throw new DataAccessException();
		}
	}

	private short getRecordType() {
		if (getSearchingView() >= 0) {
			return 1;
		} else if (getSearchingView() == -1) {
			return 0;
		} else { // MADES
			return 3;
		}
	}

	private int structureAsAttr() {
		int result = index.getStructureAttribute();
		if (result == 0) {
			result = 1;
		}
		return result;
	}

	private int positionAsAttr() {
		int result = index.getPositionAttribute();
		if (result == 0) {
			if (index.getStructureAttribute() == 2) {
				result = 3;
			} else {
				result = 1;
			}
		}
		return result;
	}

	private int relationAsAttr() {
		int rel = 3;
		if (relation.equals("=")) {
			return 3;
		} else if (relation.equals("<")) {
			rel = 1;
		} else if (relation.equals("<=")) {
			rel = 2;
		} else if (relation.equals(">")) {
			rel = 4;
		} else if (relation.equals(">=")) {
			rel = 5;
		}
		return rel;
	}

	private int truncationAsAttr() {
		// note that term has an appended blank
		int result = 100;
		if (term.lastIndexOf("?") == term.length() - 1) {
			term.deleteCharAt(term.length() - 1);
			return 1; // right truncation
		}
		logger.debug("truncationAsAttr term: " + term.toString()
				+ " results in attr " + result);
		logger.debug("last index of ? is " + term.lastIndexOf("?")
				+ ", length is " + term.length());
		return result;
	}

	private int completenessAsAddr() {
		int result = index.getCompletenessAttribute();
		if (term.lastIndexOf("!") == term.length() - 1) {
			term.deleteCharAt(term.length() - 1);
			result = 3;
		}
		if (result == 0) {
			result = 1;
		}
		return result;
	}

	public void appendToTerm(String sequence) {
		term.append(sequence);
	}

	public void setRelation(String sequence) {
		relation = sequence;
	}

	/**
	 * @param locale
	 *            the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setIndex(IndexList i) {
		index = i;
	}

	public boolean isIndexSet() {
		return index != null;
	}

	public boolean isType2Index() {
		S_BIB1_SMNTC semantic;
		try {
			semantic = getSemantic();
			return semantic != null
					&& semantic.getSecondaryIndexCode() == (byte) 2;
		} catch (Exception e) {
			return false;
		}
	}

}
