package org.folio.cataloging.bean.cataloguing.copy;

import org.folio.cataloging.bean.LibrisuiteBean;

public class OntologyTypeBean extends LibrisuiteBean 
{

	private String url;
	private String ontologyType;
	
	public OntologyTypeBean(String url, String ontologyType) {
		super();
		this.url = url;
		this.ontologyType = ontologyType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOntologyType() {
		return ontologyType;
	}

	public void setOntologyType(String ontologyType) {
		this.ontologyType = ontologyType;
	}


	
	

}