package librisuite.business.amicusSearchEngine;

import java.util.List;

import librisuite.business.librivision.MarcRecord;
import librisuite.business.searching.ResultSet;
import librisuite.business.searching.SearchEngine;

public class AmicusResultSet extends ResultSet 
{
	private int[] amicusNumbers;

	public AmicusResultSet(SearchEngine searchEngine, int searchingView, String query, int[] amicusNumbers) 
	{
		setSearchEngine(searchEngine);
		setSearchingView(searchingView);
		setDisplayQuery(query);
		setAmicusNumbers(amicusNumbers);
		if (amicusNumbers == null) {
			setSize(0);
		}
		else {
			setSize(amicusNumbers.length);
		}
	}
	
	/**
	 * pm 2011
	 * Constructor for list of variants for a single Amicus record
	 * @param amicusNumber
	 * @param variantViews
	 */
	public AmicusResultSet(int amicusNumber, List variantViews, SearchEngine searchEngine) 
	{
		setSearchEngine(searchEngine);
		setSize(variantViews.size());
		setAmicusNumbers(new int[getSize()]);
		for (int i=0; i < getSize(); i++) {
			amicusNumbers[i] = amicusNumber;
			record[i] = new MarcRecord();
			record[i].setRecordView(((Integer)variantViews.get(i)).intValue());
		}
	}
	

	public int[] getAmicusNumbers() {
		return amicusNumbers;
	}

	public void setAmicusNumbers(int[] is) {
		amicusNumbers = is;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.searching.ResultSet#getAmicusNumber(int)
	 */
	public Integer getAmicusNumber(int i) 
	{
		if (i >= 0 && i < getAmicusNumbers().length) {
			return new Integer(getAmicusNumbers()[i]);
		}
		else {
			return null;
		}
	}
}