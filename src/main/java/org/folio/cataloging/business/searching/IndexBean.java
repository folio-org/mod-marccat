package org.folio.cataloging.business.searching;

import java.util.ArrayList;
import java.util.List;

import org.folio.cataloging.bean.LibrisuiteBean;

public class IndexBean extends LibrisuiteBean   
{	
	private String searchCode;
	private String indexName;
	private int trltnNbr;
	private String lang;
	private String indexType;	
	private List indexChild = new ArrayList();

	public IndexBean(){
		super();
	}
	
	public IndexBean(String searchCode, String indexName, int trltnNbr, String indexType, String lang) 
	{
		super();
		this.searchCode = searchCode;
		this.indexName = indexName;
		this.trltnNbr = trltnNbr;
		this.indexType=indexType;
		this.lang=lang;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getIndexType() {
		return indexType;
	}

	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

	public List getIndexChild() {
		return indexChild;
	}

	public void setIndexChild(List indexChild) {
		this.indexChild = indexChild;
	}

	public String getSearchCode() {
		return searchCode;
	}

	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public int getTrltnNbr() {
		return trltnNbr;
	}

	public void setTrltnNbr(int trltnNbr) {
		this.trltnNbr = trltnNbr;
	}
}