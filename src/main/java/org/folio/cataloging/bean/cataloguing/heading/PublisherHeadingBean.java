/*
 * Created on Nov 22, 2004
 */
package org.folio.cataloging.bean.cataloguing.heading;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.dao.DAOCodeTable;
import org.folio.cataloging.dao.persistence.PUBL_HDG;
import org.folio.cataloging.dao.persistence.T_LANG_OF_IDXG;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.util.StringText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Class comment
 * @author janick
 */
public class PublisherHeadingBean extends HeadingBean {
	@Override
	public void initTextFromBrowse(String headingText, String browseIndex) {
		String name = "";
		String place = "";
		List valueList = new ArrayList();
		List codeList = new ArrayList();
		String[] parsedTerm = headingText.split(
				" : ");
		if (parsedTerm.length == 2) {
			place = parsedTerm[0].trim();
			name = parsedTerm[1].trim();
			codeList.add("a");
			codeList.add("b");
			valueList.add(place);
			valueList.add(name);
			setStringText(new StringText(codeList,
					valueList));
		} else {
			if (browseIndex != null && browseIndex.trim().equals("PU")) {
				List valueList1 = new ArrayList();
				List codeList1 = new ArrayList();
				codeList1.add("a");
				codeList1.add("b");
				valueList1.add("");
				valueList1.add(headingText);
				setStringText(new StringText(codeList1,
						valueList1));
			} else {
				setStringText(new StringText(
								Subfield.SUBFIELD_DELIMITER
										+ getValidation()
												.getMarcTagDefaultSubfieldCode()
										+ headingText));
			}
		}
	}


	private PUBL_HDG publisherHeading = new PUBL_HDG();
	
	private List languageOfIndexingList = new ArrayList();

	/* (non-Javadoc)
	 * @see HeadingBean#getHeading()
	 */
	public Descriptor getHeading() {
		return publisherHeading;
	}

	/* (non-Javadoc)
	 * @see HeadingBean#setHeading(librisuite.hibernate.Descriptor)
	 */
	protected void setHeading(Descriptor d) {
		if (!(d instanceof PUBL_HDG)) throw new IllegalArgumentException("I can only set descriptors of type PUBL_HDG");
		publisherHeading = (PUBL_HDG) d;
	}

	/* (non-Javadoc)
	 * @see HeadingBean#onPopulateLists(DAOCodeTable, java.util.Locale)
	 */
	protected void onPopulateLists(DAOCodeTable dao, Locale l) throws DataAccessException {
		setLanguageOfIndexingList(dao.getOptionList(T_LANG_OF_IDXG.class, l));
	}

	/**
	 * @return
	 */
	public List getLanguageOfIndexingList() {
		return languageOfIndexingList;
	}


	/**
	 * @param list
	 */
	public void setLanguageOfIndexingList(List list) {
		languageOfIndexingList = list;
	}

	/**
	 * @return
	 */
	public int getLanguageOfIndexing() {
		return publisherHeading.getIndexingLanguage();
	}


	/**
	 * @param s
	 */
	public void setLanguageOfIndexing(int s) {
		publisherHeading.setIndexingLanguage(s);
	}


	/* (non-Javadoc)
	 * @see HeadingBean#getCategory()
	 */
	public int getCategory() {
		return 7;
	}

	@Override
	public void setSkipInFiling(int s) {
		// TODO Auto-generated method stub
		
	}

}
