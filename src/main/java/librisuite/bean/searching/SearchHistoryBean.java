package librisuite.bean.searching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import librisuite.bean.LibrisuiteBean;
import librisuite.business.searching.ResultSet;

public class SearchHistoryBean extends LibrisuiteBean {

	private List searchHistoryList = new ArrayList();
	private String querySearch;

	
	public String getQuerySearch() {
		return querySearch;
	}

	public void setQuerySearch(String querySearch) {
		this.querySearch = querySearch;
	}
	
	public SearchHistoryBean() {
	}

	public static SearchHistoryBean getInstance(HttpServletRequest request) {
		SearchHistoryBean bean =
			(SearchHistoryBean) SearchHistoryBean.getSessionAttribute(
				request,
				SearchHistoryBean.class);
		if (bean == null) {
			bean = new SearchHistoryBean();
			bean.setSessionAttribute(request, SearchHistoryBean.class);
		}

		return bean;
	}

	public class SearchHistoryItem implements Comparable {

		@Override
		public int compareTo(Object o) 
		{
//			Order by desc 
			SearchHistoryItem other = (SearchHistoryItem)o;		
		    if (this == other ) return 0;
		    if (this.indexSearch < other.indexSearch) return 1;
		    if (this.indexSearch > other.indexSearch) return -1;
		    return 0;
			
		}
		
		@Override
		public boolean equals(Object obj) 
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final SearchHistoryItem other = (SearchHistoryItem) obj;
			
			return (indexSearch==other.indexSearch);
		}
		
		private String searchType;
		private SearchBean searchBean;
		private String searchTerm;
		private String searchComplexTerm;
		private int searchResultCount;
		private ResultSet searchResult;
		private String searchCodeIndex;
		private int indexSearch;
		
		
		public String getSearchCodeIndex() {
//			return "S" + indexSearch;
			return searchCodeIndex;
		}
		public void setSearchCodeIndex(String searchCodeIndex) {
			this.searchCodeIndex = searchCodeIndex;
		}
		public int getIndexSearch() {
			return indexSearch;
		}
		public void setIndexSearch(int indexSearch) {
			this.indexSearch = indexSearch;
		}
		public SearchHistoryItem(
			SearchBean searchBean,
			ResultSet result) {
			setSearchType(searchBean.getSearchType());
			setSearchBean(searchBean);
			setSearchTerm(result.getDisplayQuery());
			setSearchResultCount(result.getSize());
			setSearchComplexTerm(result.getComplexQuery());
			setSearchResult(result);
		}
		/**
		 * 
		 * 
		 * @return
		 * @exception
		 * @see
		 * @since 1.0
		 */
		public ResultSet getSearchResult() {
			return searchResult;
		}

		/**
		 * 
		 * 
		 * @return
		 * @exception
		 * @see
		 * @since 1.0
		 */
		public int getSearchResultCount() {
			return searchResultCount;
		}

		/**
		 * 
		 * 
		 * @return
		 * @exception
		 * @see
		 * @since 1.0
		 */
		public String getSearchTerm() {
			return searchTerm;
		}

		/**
		 * 
		 * 
		 * @return
		 * @exception
		 * @see
		 * @since 1.0
		 */
		public String getSearchType() {
			return searchType;
		}

		/**
		 * 
		 * 
		 * @param bean
		 * @exception
		 * @see
		 * @since 1.0
		 */
		public void setSearchResult(ResultSet rs) {
			searchResult = rs;
		}

		/**
		 * 
		 * 
		 * @param integer
		 * @exception
		 * @see
		 * @since 1.0
		 */
		public void setSearchResultCount(int integer) {
			searchResultCount = integer;
		}

		/**
		 * 
		 * 
		 * @param string
		 * @exception
		 * @see
		 * @since 1.0
		 */
		public void setSearchTerm(String string) {
			searchTerm = string;
		}

		/**
		 * 
		 * 
		 * @param integer
		 * @exception
		 * @see
		 * @since 1.0
		 */
		public void setSearchType(String string) {
			searchType = string;
		}

		/**
		 * 
		 * 
		 * @return
		 * @exception
		 * @see
		 * @since 1.0
		 */
		public SearchBean getSearchBean() {
			return searchBean;
		}

		/**
		 * 
		 * 
		 * @param bean
		 * @exception
		 * @see
		 * @since 1.0
		 */
		public void setSearchBean(SearchBean bean) {
			searchBean = bean;
		}
		public String getSearchComplexTerm() {
			return searchComplexTerm;
		}
		public void setSearchComplexTerm(String searchComplexTerm) {
			this.searchComplexTerm = searchComplexTerm;
		}

	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public List getSearchHistoryList() {
		return searchHistoryList;
	}

	/**
	 * 
	 * 
	 * @param list
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setSearchHistoryList(List list) {
		searchHistoryList = list;
	}

	public void addSearchHistoryItem(SearchBean searchBean, ResultSet result) 
	{	
		if(searchBean != null && result.getComplexQuery() != null && result.getComplexQuery().trim().length() > 0)	
		{	
//-------->	Deve prendere il più recente e vedere il code per aggiungere 1 al code e all'indice ( SearchCodeIndex e IndexSearch) 
			List<SearchHistoryItem> historyBeanDesc = new ArrayList<SearchHistoryItem>();
			historyBeanDesc = searchHistoryList;
			int indexMaxCode = 0;
			if (searchHistoryList.size()>0){
				Collections.sort(historyBeanDesc);
				SearchHistoryItem historyItemMax = (SearchHistoryItem) historyBeanDesc.get(0);
				indexMaxCode = Integer.parseInt(historyItemMax.getSearchCodeIndex().substring(1));	
			}
			
			int i = searchHistoryList.size();
			SearchHistoryItem item = new SearchHistoryItem(searchBean,result);
//			item.setIndexSearch(i + 1);
			item.setIndexSearch(indexMaxCode + 1);
			item.setSearchCodeIndex("S" + (indexMaxCode+1));
			searchHistoryList.add(item);
		}
	}

	public void makeClean() {
		this.searchHistoryList.clear();
	}
	
	public boolean isHasAtLeastTwoItems() {
		return (this.searchHistoryList.size() >=2);
	}
	
	public void deleteLastItem() {
		this.searchHistoryList.remove(this.searchHistoryList.size()-1);
	}
	
	public SearchHistoryItem getLastItem() {		
		return (SearchHistoryItem) this.searchHistoryList.get(this.searchHistoryList.size()-1);
	}
}